package org.example.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.example.ui.SceneRouter;
import org.example.service.GestorUsuarios;
import org.example.model.Usuario;
import org.example.model.UsuarioClasico;
import org.example.model.UsuarioPremium;

public class SignUpController {

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoCorreo;

    @FXML
    private PasswordField campoContrasena;

    @FXML
    private ToggleButton botonClasico;

    @FXML
    private ToggleButton botonPremium;

    @FXML
    private Label labelError;

    private final GestorUsuarios gestor = new GestorUsuarios();

    @FXML
    private void onSeleccionarTipo() {
        botonClasico.setStyle(null);
        botonPremium.setStyle(null);
        if (botonClasico.isSelected()) {
            botonClasico.getStyleClass().setAll("boton-chip-seleccionado");
            botonPremium.getStyleClass().setAll("boton-chip");
        } else {
            botonPremium.getStyleClass().setAll("boton-chip-seleccionado");
            botonClasico.getStyleClass().setAll("boton-chip");
        }
    }

    @FXML
    private void onCrearCuenta() {
        String nombre = campoNombre.getText();
        String correo = campoCorreo.getText();
        String contrasena = campoContrasena.getText();

        if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank()) {
            mostrarError("Completa todos los campos.");
            return;
        }

        // Le mandamos ID 0 temporalmente, la Base de Datos le dará el real autoincrementable
        Usuario nuevoUsuario = botonPremium.isSelected()
                ? new UsuarioPremium(0, nombre, correo, contrasena)
                : new UsuarioClasico(0, nombre, correo, contrasena);

        // Guardamos en la base de datos
        boolean exito = gestor.registrarUsuario(nuevoUsuario);

        if(exito){
            // Hacemos login automático
            Usuario usuarioLogeado = gestor.autenticarUsuario(correo, contrasena);
            SceneRouter.setUsuarioActual(usuarioLogeado);
            SceneRouter.goToDashboard();
        } else {
            mostrarError("Hubo un error al registrar en la BD. Posible correo duplicado.");
        }
    }

    @FXML
    private void onIrALogin() {
        SceneRouter.goToLogin();
    }

    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
        labelError.setVisible(true);
        labelError.setManaged(true);
    }
}