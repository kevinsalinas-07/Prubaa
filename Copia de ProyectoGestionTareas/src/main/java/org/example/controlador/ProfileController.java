package org.example.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.ui.SceneRouter;
import org.example.model.Elemento;
import org.example.model.Tarea;
import org.example.model.Usuario;

public class ProfileController {

    @FXML
    private Label labelIniciales;

    @FXML
    private Label labelNombre;

    @FXML
    private Label labelCorreo;

    @FXML
    private Label labelBadgeTipo;

    @FXML
    private Label labelTareasActivas;

    @FXML
    private Label labelCompartidos;

    @FXML
    private Label labelNombreDato;

    @FXML
    private Label labelCorreoDato;

    @FXML
    private VBox cajaClasico;

    @FXML
    private VBox cajaPremium;

    @FXML
    private void initialize() {
        Usuario usuario = SceneRouter.getUsuarioActual();
        if (usuario == null) {
            return;
        }

        labelIniciales.setText(usuario.getIniciales());
        labelNombre.setText(usuario.getNombre());
        labelCorreo.setText(usuario.getEmail());
        labelBadgeTipo.setText("Usuario " + usuario.getTipoUsuario().toLowerCase());
        labelNombreDato.setText(usuario.getNombre());
        labelCorreoDato.setText(usuario.getEmail());

        long tareasActivas = usuario.getElementos().stream()
                .filter(e -> e instanceof Tarea)
                .count();
        labelTareasActivas.setText(String.valueOf(tareasActivas));

        // Para evitar errores temporales de compartición, comentamos esta línea por ahora
        // long compartidos = usuario.getElementos().stream().filter(e -> e instanceof Tarea tarea && !tarea.getCompartidaCon().isEmpty()).count();
        labelCompartidos.setText("0");

        boolean esPremium = "Premium".equalsIgnoreCase(usuario.getTipoUsuario());
        resaltarPlan(esPremium);
    }

    private void resaltarPlan(boolean esPremium) {
        String estiloDestacado = "-fx-border-color: #2D6CDF; -fx-border-width: 2; -fx-background-color: #E6F1FB;";
        String estiloNormal = "-fx-border-color: #E4E2DA; -fx-border-width: 0.6; -fx-background-color: white;";
        cajaPremium.setStyle(esPremium ? estiloDestacado : estiloNormal);
        cajaClasico.setStyle(esPremium ? estiloNormal : estiloDestacado);
    }

    @FXML
    private void onVolver() {
        SceneRouter.goToDashboard();
    }
}