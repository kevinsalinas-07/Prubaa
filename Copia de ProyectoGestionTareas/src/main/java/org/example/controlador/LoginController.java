package org.example.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.ui.SceneRouter;
import org.example.service.GestorUsuarios;
import org.example.model.Usuario;

public class LoginController {

    @FXML
    private TextField campoCorreo;

    @FXML
    private PasswordField campoContrasena;

    @FXML
    private Label labelError;

    private final GestorUsuarios gestor = new GestorUsuarios();

    @FXML
    private void onIniciarSesion() {
        String correo = campoCorreo.getText();
        String contrasena = campoContrasena.getText();

        // Ahora usamos nuestro Gestor que se conecta a PostgreSQL
        Usuario usuario = gestor.autenticarUsuario(correo, contrasena);

        if (usuario == null) {
            mostrarError("Correo o contrasena incorrectos.");
            return;
        }
        SceneRouter.setUsuarioActual(usuario);
        SceneRouter.goToDashboard();
    }

    @FXML
    private void onIrARegistro() {
        SceneRouter.goToSignUp();
    }

    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
        labelError.setVisible(true);
        labelError.setManaged(true);
    }
}