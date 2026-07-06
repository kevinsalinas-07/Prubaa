package org.example.dao;

import org.example.bd.Conexion;
import org.example.model.Estado;
import org.example.model.Prioridad;
import org.example.model.Tarea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TareaDAOImpl implements TareaDAO {

    private Connection getConexion() {
        return Conexion.getInstancia().getConexionBD();
    }

    @Override
    public boolean guardar(Tarea tarea, int usuarioId) {
        String sql = "INSERT INTO elemento (usuario_id, titulo, descripcion, prioridad, tipo_elemento, estado) VALUES (?, ?, ?, ?, 'TAREA', ?)";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, tarea.getTitulo());
            stmt.setString(3, tarea.getDescripcion());
            stmt.setString(4, tarea.getPrioridad().name());
            stmt.setString(5, tarea.getEstado().name());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Tarea> listarPorUsuario(int usuarioId) {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT * FROM elemento WHERE usuario_id = ? AND tipo_elemento = 'TAREA'";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Sacamos el ID de la base de datos primero
                    int id = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String descripcion = rs.getString("descripcion");
                    Prioridad prioridad = Prioridad.valueOf(rs.getString("prioridad"));

                    // Usamos el nuevo constructor que acabás de crear
                    Tarea t = new Tarea(id, titulo, descripcion, prioridad);
                    t.cambiarEstado(Estado.valueOf(rs.getString("estado")));

                    tareas.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tareas;
    }

    @Override
    public boolean actualizar(Tarea tarea) {
        String sql = "UPDATE elemento SET titulo = ?, descripcion = ?, prioridad = ?, estado = ? WHERE id = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, tarea.getTitulo());
            stmt.setString(2, tarea.getDescripcion());
            stmt.setString(3, tarea.getPrioridad().name());
            stmt.setString(4, tarea.getEstado().name());
            stmt.setInt(5, tarea.getId());

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
    public Tarea buscarPorId(int id) {
        String sql = "SELECT * FROM elemento WHERE id = ? AND tipo_elemento = 'TAREA'";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Sacamos el ID de la base de datos primero
                    int idObtenido = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String descripcion = rs.getString("descripcion");
                    Prioridad prioridad = Prioridad.valueOf(rs.getString("prioridad"));
                    Tarea t = new Tarea(idObtenido, titulo, descripcion, prioridad);
                    t.cambiarEstado(Estado.valueOf(rs.getString("estado")));

                    return t;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean compartirTareaBD(int tareaId, int destinatarioId) {
        String sql = "INSERT INTO elemento_compartido (tarea_id, usuario_id) VALUES (?, ?)";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, tareaId);
            stmt.setInt(2, destinatarioId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error al compartir (¿Quizás ya estaba compartida?): " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Tarea> listarCompartidasPorUsuario(int usuarioId) {
        List<Tarea> tareasCompartidas = new ArrayList<>();
        // Unimos la tabla de elementos con la tabla intermedia para sacar las tareas que le regalaron
        String sql = "SELECT e.* FROM elemento e " +
                "INNER JOIN elemento_compartido ec ON e.id = ec.tarea_id " +
                "WHERE ec.usuario_id = ? AND e.tipo_elemento = 'TAREA'";

        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String descripcion = rs.getString("descripcion");
                    Prioridad prioridad = Prioridad.valueOf(rs.getString("prioridad"));

                    Tarea t = new Tarea(id, titulo, descripcion, prioridad);
                    t.cambiarEstado(Estado.valueOf(rs.getString("estado")));

                    tareasCompartidas.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tareasCompartidas;
    }
}