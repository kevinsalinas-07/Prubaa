package org.example.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.ui.SceneRouter;
import org.example.model.Tarea;
import org.example.patterns.strategy.VisualizacionCompleta;

public class TaskDetailController {

    @FXML
    private Region barraPrioridad;

    @FXML
    private Label labelTitulo;

    @FXML
    private Label labelPrioridad;

    @FXML
    private Label labelDescripcion;

    @FXML
    private Label labelEstado;

    @FXML
    private Label labelTipo;

    @FXML
    private VBox cajaCompartidos;

    @FXML
    private void initialize() {
        // Necesitas castearlo porque el SceneRouter maneja "Elemento" genérico
        Tarea tarea = (Tarea) SceneRouter.getElementoSeleccionado();
        if (tarea == null) {
            return;
        }

        // =========================================================
        // ¡DEMOSTRACIÓN DEL PATRÓN STRATEGY!
        // Al visualizar el detalle, activamos la estrategia completa.
        // Esto aparecerá en la consola cada vez que veas una tarea.
        tarea.setEstrategia(new VisualizacionCompleta());
        System.out.println("\n[Patrón Strategy Activado - Visualizando detalles]");
        tarea.visualizar();
        // =========================================================

        // Mapeo de colores para la interfaz
        String color = switch (tarea.getPrioridad()) {
            case ALTA -> "#EF4444";
            case MEDIA -> "#F59E0B";
            default -> "#10B981";
        };
        barraPrioridad.setStyle("-fx-background-color: " + color + ";");

        labelTitulo.setText(tarea.getTitulo());

        // Ajustamos la etiqueta de prioridad
        labelPrioridad.setText("Prioridad " + tarea.getPrioridad().name().toLowerCase());
        labelPrioridad.getStyleClass().setAll(estiloBadgePrioridad(tarea.getPrioridad().name()));

        labelDescripcion.setText(tarea.getDescripcion());
        labelEstado.setText(tarea.getEstado().getEtiqueta());

        labelTipo.setText("Tarea");

        cajaCompartidos.getChildren().clear();
        Label sinCompartir = new Label("La función de compartir está en desarrollo para la BD.");
        sinCompartir.getStyleClass().add("texto-secundario");
        cajaCompartidos.getChildren().add(sinCompartir);
    }

    private String estiloBadgePrioridad(String prioridad) {
        return switch (prioridad) {
            case "ALTA" -> "badge-alta";
            case "MEDIA" -> "badge-media";
            default -> "badge-baja";
        };
    }

    @FXML
    private void onEditar() {
        SceneRouter.goToTaskForm();
    }

    @FXML
    private void onCompartir() {
    }

    @FXML
    private void onEliminar() {
        SceneRouter.goToDashboard();
    }

    @FXML
    private void onVolver() {
        SceneRouter.goToDashboard();
    }
}