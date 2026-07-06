package org.example.dao;

import org.example.bd.Conexion;
import org.example.model.Usuario;
import org.example.model.UsuarioClasico;
import org.example.model.UsuarioPremium;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    // Pedimos prestada la conexión al Singleton
    private Connection getConexion() {
        return Conexion.getInstancia().getConexionBD();
    }

    @Override
    public boolean guardar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre, email, password, tipo_usuario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getTipoUsuario());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Usuario buscarPorCredenciales(String email, String password) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Sacamos todos los datos reales de PostgreSQL
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo_usuario");

                // Magia de Polimorfismo inyectando el ID real
                if ("Premium".equalsIgnoreCase(tipo)) {
                    return new UsuarioPremium(id, nombre, email, password);
                } else {
                    return new UsuarioClasico(id, nombre, email, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Aquí también sacamos el ID real
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String tipo = rs.getString("tipo_usuario");

                if ("Premium".equalsIgnoreCase(tipo)) {
                    usuarios.add(new UsuarioPremium(id, nombre, email, password));
                } else {
                    usuarios.add(new UsuarioClasico(id, nombre, email, password));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre = ?, password = ?, tipo_usuario = ? WHERE email = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getTipoUsuario());
            stmt.setString(4, usuario.getEmail());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}