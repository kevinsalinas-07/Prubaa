package org.example.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.model.Tarea;
import org.example.model.Usuario;

import java.io.IOException;

public final class SceneRouter {

    private static Stage stage;
    private static Usuario usuarioActual;
    private static Tarea elementoSeleccionado;
    private static String tipoPreseleccionado = "Tarea"; // NUEVO

    private SceneRouter() {
    }

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setElementoSeleccionado(Tarea tarea) {
        elementoSeleccionado = tarea;
    }

    public static Tarea getElementoSeleccionado() {
        return elementoSeleccionado;
    }

    // NUEVO: para que el formulario sepa qué tipo preseleccionar
    public static void setTipoPreseleccionado(String tipo) {
        tipoPreseleccionado = tipo;
    }

    public static String getTipoPreseleccionado() {
        return tipoPreseleccionado;
    }

    public static void goToLogin() {
        cargar("/fxml/login.fxml");
    }

    public static void goToSignUp() {
        cargar("/fxml/signup.fxml");
    }

    public static void goToDashboard() {
        cargar("/fxml/dashboard.fxml");
    }

    public static void goToTaskForm() {
        cargar("/fxml/task_form.fxml");
    }

    public static void goToTaskDetail() {
        cargar("/fxml/task_detail.fxml");
    }

    public static void goToProfile() {
        cargar("/fxml/profile.fxml");
    }

    private static void cargar(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneRouter.class.getResource(rutaFxml));
            Parent root = loader.load();
            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root, 1120, 700);
                scene.getStylesheets().add(SceneRouter.class.getResource("/css/style.css").toExternalForm());
                stage.setScene(scene);
            } else {
                scene.setRoot(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la pantalla: " + rutaFxml, e);
        }
    }
}