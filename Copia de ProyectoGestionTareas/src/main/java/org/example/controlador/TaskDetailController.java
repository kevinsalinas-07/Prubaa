package org.example.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.model.Estado;
import org.example.model.Usuario;
import org.example.service.GestorUsuarios;
import org.example.ui.SceneRouter;
import org.example.model.Tarea;
import org.example.patterns.strategy.VisualizacionCompleta;

import java.util.List;
import java.util.Optional;

public class TaskDetailController {

    @FXML private Region barraPrioridad;
    @FXML private Label labelTitulo;
    @FXML private Label labelPrioridad;
    @FXML private Label labelDescripcion;
    @FXML private Label labelEstado;
    @FXML private Label labelTipo;
    @FXML private VBox cajaCompartidos;

    private Tarea tareaActual;
    private final GestorUsuarios gestor = new GestorUsuarios();

    @FXML
    private void initialize() {
        tareaActual = (Tarea) SceneRouter.getElementoSeleccionado();
        if (tareaActual == null) return;

        // Patrón Strategy
        tareaActual.setEstrategia(new VisualizacionCompleta());
        tareaActual.visualizar();

        actualizarUI();
    }

    private void actualizarUI() {
        String color = switch (tareaActual.getPrioridad()) {
            case ALTA -> "#EF4444";
            case MEDIA -> "#F59E0B";
            default -> "#10B981";
        };
        barraPrioridad.setStyle("-fx-background-color: " + color + ";");

        labelTitulo.setText(tareaActual.getTitulo());
        labelPrioridad.setText("Prioridad " + tareaActual.getPrioridad().name().toLowerCase());
        labelPrioridad.getStyleClass().setAll(estiloBadgePrioridad(tareaActual.getPrioridad().name()));
        labelDescripcion.setText(tareaActual.getDescripcion());
        labelEstado.setText(tareaActual.getEstado().getEtiqueta());
        labelTipo.setText("Tarea");

        cajaCompartidos.getChildren().clear();
        Label texto = new Label("Usa el botón inferior para compartir.");
        texto.getStyleClass().add("texto-secundario");
        cajaCompartidos.getChildren().add(texto);
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
        // Un popup nativo para elegir el nuevo estado
        ChoiceDialog<Estado> dialog = new ChoiceDialog<>(tareaActual.getEstado(), Estado.values());
        dialog.setTitle("Editar Tarea");
        dialog.setHeaderText("Modificar el progreso de la tarea");
        dialog.setContentText("Selecciona el nuevo estado:");

        Optional<Estado> resultado = dialog.showAndWait();
        resultado.ifPresent(nuevoEstado -> {
            if (gestor.actualizarEstadoTarea(tareaActual, nuevoEstado)) {
                actualizarUI(); // Refresca el texto en pantalla
            }
        });
    }

    @FXML
    private void onCompartir() {
        Usuario usuarioActual = SceneRouter.getUsuarioActual();
        List<Usuario> todos = gestor.getUsuarios();

        // Evitamos que se comparta consigo mismo
        todos.removeIf(u -> u.getId() == usuarioActual.getId());

        ChoiceDialog<Usuario> dialog = new ChoiceDialog<>(null, todos);
        dialog.setTitle("Compartir Tarea");
        dialog.setHeaderText("Colaboración de equipo");
        dialog.setContentText("Selecciona el destinatario:");

        Optional<Usuario> resultado = dialog.showAndWait();
        resultado.ifPresent(destinatario -> {
            boolean exito = usuarioActual.compartirTarea(tareaActual, destinatario);

            if (exito) {
                // Si la lógica en RAM fue exitosa, levantamos el hilo
                gestor.compartirTareaConcurrente(tareaActual, usuarioActual, destinatario);
                mostrarAlerta(Alert.AlertType.INFORMATION, "¡Éxito!", "La tarea se está compartiendo con " + destinatario.getNombre());
            } else {
                // Aquí el usuario sabe exactamente por qué falló
                mostrarAlerta(Alert.AlertType.WARNING, "Límite alcanzado",
                        "No se pudo compartir. Si eres usuario Clásico, recuerda que solo puedes colaborar con un (1) usuario máximo.");
            }
        });
    }

    @FXML
    private void onEliminar() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Tarea");
        confirmacion.setHeaderText("¿Estás seguro de eliminar esta tarea?");
        confirmacion.setContentText("Esta acción borrará la tarea de la base de datos.");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (gestor.eliminarElemento(tareaActual, SceneRouter.getUsuarioActual())) {
                    SceneRouter.goToDashboard();
                }
            }
        });
    }

    @FXML
    private void onVolver() {
        SceneRouter.goToDashboard();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}