package org.example.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.example.ui.SceneRouter;
import org.example.model.Estado;
import org.example.model.Prioridad;
import org.example.model.Recordatorio;
import org.example.model.Tarea;
import org.example.model.Usuario;
import org.example.service.GestorUsuarios;

import java.time.LocalDate;

public class TaskFormController {

    @FXML private Label labelTituloFormulario;
    @FXML private TextField campoTitulo;
    @FXML private TextArea campoDescripcion;
    @FXML private ComboBox<String> comboTipo;
    @FXML private ComboBox<Estado> comboEstado;
    @FXML private ComboBox<Usuario> comboCompartirCon;
    @FXML private ToggleButton botonAlta;
    @FXML private ToggleButton botonMedia;
    @FXML private ToggleButton botonBaja;

    // Etiqueta para mostrar el error visual integrado
    @FXML private Label labelErrorFormulario;

    private Prioridad prioridadSeleccionada = Prioridad.ALTA;
    private final GestorUsuarios gestor = new GestorUsuarios();

    @FXML
    private void initialize() {
        comboTipo.getItems().setAll("Tarea", "Recordatorio");
        comboTipo.getSelectionModel().select(SceneRouter.getTipoPreseleccionado());

        actualizarTitulo();

        comboTipo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            actualizarTitulo();
        });

        comboEstado.getItems().setAll(Estado.values());
        comboEstado.getSelectionModel().selectFirst();

        comboCompartirCon.getItems().setAll(gestor.getUsuarios());

        // Evitamos que se pueda compartir consigo mismo desde el inicio
        Usuario usuarioActual = SceneRouter.getUsuarioActual();
        if (usuarioActual != null) {
            comboCompartirCon.getItems().removeIf(u -> u.getId() == usuarioActual.getId());
        }

        comboCompartirCon.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Usuario usuario) {
                return usuario == null ? "" : usuario.getNombre();
            }
            @Override
            public Usuario fromString(String string) {
                return null;
            }
        });
    }

    private void actualizarTitulo() {
        if ("Recordatorio".equals(comboTipo.getValue())) {
            labelTituloFormulario.setText("Nuevo recordatorio");
        } else {
            labelTituloFormulario.setText("Nueva tarea");
        }
    }

    @FXML
    private void onSeleccionarPrioridad() {
        if (botonAlta.isSelected()) {
            prioridadSeleccionada = Prioridad.ALTA;
        } else if (botonMedia.isSelected()) {
            prioridadSeleccionada = Prioridad.MEDIA;
        } else {
            prioridadSeleccionada = Prioridad.BAJA;
        }
        marcarSeleccionado(botonAlta, prioridadSeleccionada == Prioridad.ALTA);
        marcarSeleccionado(botonMedia, prioridadSeleccionada == Prioridad.MEDIA);
        marcarSeleccionado(botonBaja, prioridadSeleccionada == Prioridad.BAJA);
    }

    private void marcarSeleccionado(ToggleButton boton, boolean seleccionado) {
        boton.getStyleClass().setAll(seleccionado ? "boton-chip-seleccionado" : "boton-chip");
    }

    // Método para mostrar el error visual integrado
    private void mostrarError(String mensaje) {
        labelErrorFormulario.setText(mensaje);
        labelErrorFormulario.setVisible(true);
        labelErrorFormulario.setManaged(true);
    }

    @FXML
    private void onGuardar() {
        // Limpiamos el error previo si existe
        labelErrorFormulario.setVisible(false);
        labelErrorFormulario.setManaged(false);

        Usuario usuarioActual = SceneRouter.getUsuarioActual();
        if (usuarioActual == null) {
            SceneRouter.goToLogin();
            return;
        }

        String titulo = campoTitulo.getText();
        String descripcion = campoDescripcion.getText();

        if (titulo.isBlank()) {
            mostrarError("El título no puede estar vacío.");
            return;
        }

        if ("Recordatorio".equals(comboTipo.getValue())) {
            Recordatorio recordatorio = new Recordatorio(0, titulo, descripcion, prioridadSeleccionada, LocalDate.now().plusDays(1));
            gestor.guardarNuevoElemento(usuarioActual, recordatorio);
        } else {
            Tarea tarea = new Tarea(0, titulo, descripcion, prioridadSeleccionada);
            tarea.cambiarEstado(comboEstado.getValue());

            // 1. Primero guardamos la tarea para que exista legalmente en BD y RAM
            gestor.guardarNuevoElemento(usuarioActual, tarea);

            Usuario destinatario = comboCompartirCon.getValue();

            // 2. Ahora que ya es tuya, intentamos compartirla
            if (destinatario != null) {
                boolean puedeCompartir = usuarioActual.compartirTarea(tarea, destinatario);

                if (puedeCompartir) {
                    gestor.compartirTareaConcurrente(tarea, usuarioActual, destinatario);
                } else {
                    // Si falla ahora, sí es 100% por el límite del plan Clásico.
                    // Te avisamos con un popup porque la tarea SÍ se creó para ti, pero no se envió.
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Límite alcanzado");
                    alert.setHeaderText("Tarea creada, pero NO compartida");
                    alert.setContentText("Tu tarea se guardó con éxito en tu cuenta, pero como usuario Clásico ya no puedes colaborar con más personas.");
                    alert.showAndWait();
                }
            }
        }

        SceneRouter.goToDashboard();
    }

    @FXML
    private void onCancelar() {
        SceneRouter.goToDashboard();
    }
}