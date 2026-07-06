package org.example.dao;

import org.example.bd.Conexion;
import org.example.model.Prioridad;
import org.example.model.Recordatorio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioDAOImpl implements RecordatorioDAO {

    private Connection getConexion() {
        return Conexion.getInstancia().getConexionBD();
    }

    @Override
    public boolean guardar(Recordatorio recordatorio, int usuarioId) {
        // Fijate que aquí inyectamos 'RECORDATORIO' y usamos la columna fecha_recordatorio
        String sql = "INSERT INTO elemento (usuario_id, titulo, descripcion, prioridad, tipo_elemento, fecha_recordatorio) VALUES (?, ?, ?, ?, 'RECORDATORIO', ?)";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, recordatorio.getTitulo());
            stmt.setString(3, recordatorio.getDescripcion());
            stmt.setString(4, recordatorio.getPrioridad().name());

            // Conversión clave: de LocalDate (Java) a Date (SQL)
            stmt.setDate(5, Date.valueOf(recordatorio.getFecha()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Recordatorio> listarPorUsuario(int usuarioId) {
        List<Recordatorio> recordatorios = new ArrayList<>();
        String sql = "SELECT * FROM elemento WHERE usuario_id = ? AND tipo_elemento = 'RECORDATORIO'";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String descripcion = rs.getString("descripcion");
                    Prioridad prioridad = Prioridad.valueOf(rs.getString("prioridad"));

                    // Conversión de regreso: de Date (SQL) a LocalDate (Java)
                    LocalDate fecha = rs.getDate("fecha_recordatorio").toLocalDate();

                    // Usamos el constructor sobrecargado
                    Recordatorio r = new Recordatorio(id, titulo, descripcion, prioridad, fecha);
                    recordatorios.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordatorios;
    }

    @Override
    public boolean actualizar(Recordatorio recordatorio) {
        String sql = "UPDATE elemento SET titulo = ?, descripcion = ?, prioridad = ?, fecha_recordatorio = ? WHERE id = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, recordatorio.getTitulo());
            stmt.setString(2, recordatorio.getDescripcion());
            stmt.setString(3, recordatorio.getPrioridad().name());
            stmt.setDate(4, Date.valueOf(recordatorio.getFecha()));
            stmt.setInt(5, recordatorio.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM elemento WHERE id = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Recordatorio buscarPorId(int id) {
        String sql = "SELECT * FROM elemento WHERE id = ? AND tipo_elemento = 'RECORDATORIO'";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idObtenido = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String descripcion = rs.getString("descripcion");
                    Prioridad prioridad = Prioridad.valueOf(rs.getString("prioridad"));
                    LocalDate fecha = rs.getDate("fecha_recordatorio").toLocalDate();

                    return new Recordatorio(idObtenido, titulo, descripcion, prioridad, fecha);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}