package org.example.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Conexion instancia;
    private Connection conexionBD;

    // Constantes
    private static final String URL = "jdbc:postgresql://localhost:5433/bd_tareas"; // Cambiá el nombre de la BD si le pusiste otro
    private static final String USUARIO = "postgres";
    private static final String CLAVE = "postgres";

    // Constructor privado
    private Conexion() {
        cargarCredenciales();
    }

    private void cargarCredenciales() {
        try {
            // Levantamos el driver y abrimos el túnel
            Class.forName("org.postgresql.Driver");
            conexionBD = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("[Conexion] ¡Túnel hacia PostgreSQL abierto exitosamente!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[Conexion] Error al conectar con la base de datos.");
            e.printStackTrace();
        }
    }

    // El método global para que los DAOs pidan la conexión
    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexionBD() {
        return conexionBD;
    }

    public void desconectar() {
        try {
            if (conexionBD != null && !conexionBD.isClosed()) {
                conexionBD.close();
                System.out.println("[Conexion] Conexión cerrada. Recursos liberados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}