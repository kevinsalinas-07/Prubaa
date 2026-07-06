package org.example.dao;

import org.example.model.Usuario;
import java.util.List;

public interface UsuarioDAO {

    // Método para registrar un nuevo usuario en la base de datos
    boolean guardar(Usuario usuario);

    // Método fundamental para tu pantalla de Login
    Usuario buscarPorCredenciales(String email, String password);

    // Método para obtener a todos los usuarios registrados (útil para validaciones)
    List<Usuario> listarTodos();

    // Método por si el usuario quiere cambiar su nombre o plan (Premium/Clásico)
    boolean actualizar(Usuario usuario);

    // Método para dar de baja una cuenta
    boolean eliminar(int idUsuario);

}