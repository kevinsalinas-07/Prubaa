package org.example.controlador;

import org.example.ui.SceneRouter;
import javafx.collections.FXCollections;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.model.Elemento;
import org.example.model.Recordatorio;
import org.example.model.Tarea;
import org.example.model.Usuario;
import org.example.threads.EnvioRecordatorio;
import org.example.patterns.strategy.VisualizacionSimple; // Importación de la estrategia

public class AppUIController {

    @FXML private ListView<Elemento> listaElementos;
    @FXML private Button botonFiltroTodos;
    @FXML private Button botonFiltroTareas;
    @FXML private Button botonFiltroRecordatorios;
    @FXML private Label labelIniciales;
    @FXML private Label labelNombreUsuario;
    @FXML private Label labelTipoUsuario;
    @FXML private VBox toastRecordatorio;
    @FXML private Label labelTituloToast;
    @FXML private Label labelFechaToast;

    private ObservableList<Elemento> todosLosElementos;

    @FXML
    private void initialize() {
        Usuario usuarioActual = SceneRouter.getUsuarioActual();
        if (usuarioActual != null) {
            labelIniciales.setText(usuarioActual.getIniciales());
            labelNombreUsuario.setText(usuarioActual.getNombre());
            labelTipoUsuario.setText("Usuario " + usuarioActual.getTipoUsuario().toLowerCase());
        }

        todosLosElementos = usuarioActual != null
                ? FXCollections.observableArrayList(usuarioActual.getElementos())
                : FXCollections.observableArrayList();

        // ====================================================================
        // OPTIMIZACIÓN DEL PATRÓN STRATEGY:
        // Ejecutamos la estrategia simple una sola vez por cada elemento al cargar
        // el Dashboard, evitando saturar la consola durante el scroll.
        System.out.println("\n[Patrón Strategy Activado - Resumen del Dashboard]");
        for (Elemento e : todosLosElementos) {
            e.setEstrategia(new VisualizacionSimple());
            e.visualizar();
        }
        // ====================================================================

        listaElementos.setItems(todosLosElementos);
        listaElementos.setCellFactory(lista -> new ElementoListCell());
        listaElementos.setOnMouseClicked(evento -> {
            Elemento seleccionado = listaElementos.getSelectionModel().getSelectedItem();
            if (seleccionado instanceof Tarea tarea && evento.getClickCount() == 1) {
                SceneRouter.setElementoSeleccionado(tarea);
                SceneRouter.goToTaskDetail();
            }
        });

        Recordatorio primerRecordatorio = todosLosElementos.stream()
                .filter(e -> e instanceof Recordatorio)
                .map(e -> (Recordatorio) e)
                .findFirst()
                .orElse(null);
        if (primerRecordatorio != null) {
            EnvioRecordatorio hilo = new EnvioRecordatorio(primerRecordatorio, () -> Platform.runLater(() -> mostrarToast(primerRecordatorio)));
            hilo.start();
        }
    }

    @FXML
    private void onFiltrarTodos() {
        listaElementos.setItems(todosLosElementos);
        marcarFiltroActivo(botonFiltroTodos);
    }

    @FXML
    private void onFiltrarTareas() {
        listaElementos.setItems(FXCollections.observableArrayList(
                todosLosElementos.filtered(e -> e instanceof Tarea)));
        marcarFiltroActivo(botonFiltroTareas);
    }

    @FXML
    private void onFiltrarRecordatorios() {
        listaElementos.setItems(FXCollections.observableArrayList(
                todosLosElementos.filtered(e -> e instanceof Recordatorio)));
        marcarFiltroActivo(botonFiltroRecordatorios);
    }

    private void marcarFiltroActivo(Button activo) {
        for (Button b : new Button[]{botonFiltroTodos, botonFiltroTareas, botonFiltroRecordatorios}) {
            b.getStyleClass().setAll(b == activo ? "nav-item-activo" : "nav-item");
        }
    }

    @FXML
    private void onNuevaTarea() {
        SceneRouter.goToTaskForm();
    }

    @FXML
    private void onAbrirPerfil() {
        SceneRouter.goToProfile();
    }

    @FXML
    private void onAbrirNotificaciones() {
    }

    private void mostrarToast(Recordatorio recordatorio) {
        labelTituloToast.setText(recordatorio.getTitulo());
        labelFechaToast.setText(recordatorio.getFechaFormateada());
        toastRecordatorio.setVisible(true);
        toastRecordatorio.setManaged(true);
    }

    @FXML
    private void onCerrarToast() {
        toastRecordatorio.setVisible(false);
        toastRecordatorio.setManaged(false);
    }

    @FXML
    private void onPosponerRecordatorio() {
        onCerrarToast();
    }

    private static class ElementoListCell extends ListCell<Elemento> {
        @Override
        protected void updateItem(Elemento elemento, boolean vacio) {
            super.updateItem(elemento, vacio);
            if (vacio || elemento == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            // La lógica de impresión se movió al initialize() para mantener limpia la consola.

            Region barraPrioridad = new Region();
            barraPrioridad.setPrefSize(6, 34);
            barraPrioridad.setStyle("-fx-background-color: " + elemento.getPrioridad().getColor() + ";");

            Label titulo = new Label(elemento.getTitulo());
            titulo.setStyle("-fx-font-size: 13px;");

            String subtitulo = elemento instanceof Tarea tarea
                    ? tarea.getEstado().getEtiqueta()
                    : ((Recordatorio) elemento).getFechaFormateada();
            Label subtituloLabel = new Label(subtitulo);
            subtituloLabel.getStyleClass().add("texto-secundario");

            VBox textos = new VBox(2, titulo, subtituloLabel);

            Label icono = new Label(elemento instanceof Recordatorio ? "\u23F0" : "\u2611");
            icono.setStyle("-fx-text-fill: #9A988F; -fx-font-size: 14px;");

            Region espaciador = new Region();
            HBox.setHgrow(espaciador, javafx.scene.layout.Priority.ALWAYS);

            HBox fila = new HBox(10, barraPrioridad, textos, espaciador, icono);
            fila.setAlignment(Pos.CENTER_LEFT);
            fila.setPadding(new Insets(8, 10, 8, 10));

            setGraphic(fila);
            setText(null);
        }
    }
}