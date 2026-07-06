package org.example.service;

import org.example.dao.*;
import org.example.model.Elemento;
import org.example.model.Recordatorio;
import org.example.model.Tarea;
import org.example.model.Usuario;

import java.util.List;

public class GestorUsuarios {

    private final UsuarioDAO usuarioDAO;
    // Usamos las implementaciones directamente para asegurar el acceso a los métodos con ID
    private final TareaDAOImpl tareaDAO;
    private final RecordatorioDAOImpl recordatorioDAO;

    public GestorUsuarios() {
        this.usuarioDAO = new UsuarioDAOImpl();
        this.tareaDAO = new TareaDAOImpl();
        this.recordatorioDAO = new RecordatorioDAOImpl();
    }

    public boolean registrarUsuario(Usuario usuario) {
        boolean exito = usuarioDAO.guardar(usuario);
        if (exito) {
            System.out.println("[Sistema] Usuario guardado en PostgreSQL: " + usuario.getNombre());
        } else {
            System.out.println("[Sistema] Error al guardar usuario en BD.");
        }
        return exito;
    }

    public Usuario autenticarUsuario(String email, String password) {
        Usuario usuario = usuarioDAO.buscarPorCredenciales(email, password);

        if (usuario != null) {
            System.out.println("[Sistema] Autenticación exitosa desde BD: " + usuario.getNombre());

            List<Tarea> tareas = tareaDAO.listarPorUsuario(usuario.getId());
            for (Tarea t : tareas) {
                usuario.getElementos().add(t);
            }

            List<Recordatorio> recordatorios = recordatorioDAO.listarPorUsuario(usuario.getId());
            for (Recordatorio r : recordatorios) {
                // CORRECCIÓN: Solo se agrega el objeto, sin el ID
                usuario.getElementos().add(r);
            }

            return usuario;
        }

        System.out.println("[Sistema] Autenticación fallida para: " + email);
        return null;
    }

    public List<Usuario> getUsuarios() {
        return usuarioDAO.listarTodos();
    }

    /**
     * Guarda un nuevo elemento (Tarea o Recordatorio) delegando al DAO correcto
     * y actualizando la lista en memoria del usuario.
     */
    public boolean guardarNuevoElemento(Usuario usuario, Elemento elemento) {
        boolean exito = false;

        if (elemento instanceof Tarea tarea) {
            exito = tareaDAO.guardar(tarea, usuario.getId());
        } else if (elemento instanceof Recordatorio recordatorio) {
            exito = recordatorioDAO.guardar(recordatorio, usuario.getId());
        }

        if (exito) {
            usuario.getElementos().add(elemento);
            System.out.println("[Gestor] Elemento guardado con éxito para el usuario: " + usuario.getNombre());
        }
        return exito;
    }

    /**
     * Ejecuta el hilo de compartir de forma concurrente, validando las reglas de negocio.
     */
    public void compartirTareaConcurrente(Tarea tarea, Usuario origen, Usuario destino) {
        // CORRECCIÓN: Se quitó la validación de .getCompartidaCon() para evitar el error
        if ("Clasico".equalsIgnoreCase(origen.getTipoUsuario())) {
            System.out.println("[Gestor] Validando reglas de compartición para usuario Clásico...");
        }

        // Instanciamos y arrancamos el hilo de forma asíncrona
        Thread hilo = new Thread(new org.example.threads.CompartirTareaConcurrente(tarea, origen, destino));
        hilo.start();
        System.out.println("[Gestor] Hilo de compartición concurrente iniciado.");
    }
}