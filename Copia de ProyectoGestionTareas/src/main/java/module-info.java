module ProyectoGestionTareas {
    // Dependencias que necesita tu proyecto
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    // Exportamos los paquetes para que JavaFX pueda ver tus clases
    exports org.example;
    exports org.example.controlador;
    exports org.example.model;
    exports org.example.dao;
    exports org.example.service;
    exports org.example.ui;

    // Le damos permiso especial a JavaFX para que inyecte los botones y textos (@FXML)
    opens org.example.controlador to javafx.fxml;

    // Le damos permiso a JavaFX para que pueda arrancar tu MainFX
    opens org.example to javafx.graphics;
}