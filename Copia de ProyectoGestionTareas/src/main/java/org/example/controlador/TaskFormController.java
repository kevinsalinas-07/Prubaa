package org.example.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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

    @FXML private TextField campoTitulo;
    @FXML private TextArea campoDescripcion;
    @FXML private ComboBox<String> comboTipo;
    @FXML private ComboBox<Estado> comboEstado;
    @FXML private ComboBox<Usuario> comboCompartirCon;
    @FXML private ToggleButton botonAlta;
    @FXML private ToggleButton botonMedia;
    @FXML private ToggleButton botonBaja;

    private Prioridad prioridadSeleccionada = Prioridad.ALTA;
    private final GestorUsuarios gestor = new GestorUsuarios();

    @FXML
    private void initialize() {
        comboTipo.getItems().setAll("Tarea", "Recordatorio");
        comboTipo.getSelectionModel().selectFirst();

        comboEstado.getItems().setAll(Estado.values());
        comboEstado.getSelectionModel().selectFirst();

        comboCompartirCon.getItems().setAll(gestor.getUsuarios());
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

    @FXML
    private void onGuardar() {
        Usuario usuarioActual = SceneRouter.getUsuarioActual();
        if (usuarioActual == null) {
            SceneRouter.goToLogin();
            return;
        }

        String titulo = campoTitulo.getText();
        String descripcion = campoDescripcion.getText();

        if ("Recordatorio".equals(comboTipo.getValue())) {
            Recordatorio recordatorio = new Recordatorio(0, titulo, descripcion, prioridadSeleccionada, LocalDate.now().plusDays(1));
            gestor.guardarNuevoElemento(usuarioActual, recordatorio);
        } else {
            Tarea tarea = new Tarea(0, titulo, descripcion, prioridadSeleccionada);
            tarea.cambiarEstado(comboEstado.getValue());

            gestor.guardarNuevoElemento(usuarioActual, tarea);

            Usuario destinatario = comboCompartirCon.getValue();
            if (destinatario != null) {
                // CORRECCIÓN: Validamos primero la regla de negocio del tipo de usuario antes de lanzar el hilo
                boolean puedeCompartir = usuarioActual.compartirTarea(tarea, destinatario);
                if (puedeCompartir) {
                    gestor.compartirTareaConcurrente(tarea, usuarioActual, destinatario);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Límite alcanzado");
                    alert.setHeaderText(null);
                    alert.setContentText("No se pudo compartir al crear la tarea. Si eres usuario Clásico, recuerda que solo puedes colaborar con un (1) usuario máximo.");
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