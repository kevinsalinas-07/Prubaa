package org.example.service;

import org.example.dao.*;
import org.example.model.Elemento;
import org.example.model.Estado;
import org.example.model.Recordatorio;
import org.example.model.Tarea;
import org.example.model.Usuario;

import java.util.List;

public class GestorUsuarios {

    private final UsuarioDAO usuarioDAO;
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
        }
        return exito;
    }

    public Usuario autenticarUsuario(String email, String password) {
        Usuario usuario = usuarioDAO.buscarPorCredenciales(email, password);

        if (usuario != null) {
            System.out.println("[Sistema] Autenticación exitosa: " + usuario.getNombre());

            // 1. Cargamos las tareas propias (Dueño)
            List<Tarea> misTareas = tareaDAO.listarPorUsuario(usuario.getId());
            for (Tarea t : misTareas) {
                usuario.getElementos().add(t);
            }

            // 2. NUEVO: Cargamos las tareas que OTRAS personas le compartieron
            List<Tarea> compartidas = tareaDAO.listarCompartidasPorUsuario(usuario.getId());
            for (Tarea t : compartidas) {
                usuario.getElementos().add(t);
            }

            // 3. Cargamos sus recordatorios
            List<Recordatorio> recordatorios = recordatorioDAO.listarPorUsuario(usuario.getId());
            for (Recordatorio r : recordatorios) {
                usuario.getElementos().add(r);
            }

            return usuario;
        }
        return null;
    }

    public List<Usuario> getUsuarios() {
        return usuarioDAO.listarTodos();
    }

    public boolean guardarNuevoElemento(Usuario usuario, Elemento elemento) {
        boolean exito = false;
        if (elemento instanceof Tarea tarea) {
            exito = tareaDAO.guardar(tarea, usuario.getId());
        } else if (elemento instanceof Recordatorio recordatorio) {
            exito = recordatorioDAO.guardar(recordatorio, usuario.getId());
        }

        if (exito) {
            usuario.getElementos().add(elemento);
        }
        return exito;
    }

    public boolean eliminarElemento(Elemento elemento, Usuario usuario) {
        boolean exito = false;
        if (elemento instanceof Tarea) {
            exito = tareaDAO.eliminar(elemento.getId());
        } else if (elemento instanceof Recordatorio) {
            exito = recordatorioDAO.eliminar(elemento.getId());
        }
        if (exito) {
            usuario.getElementos().remove(elemento);
        }
        return exito;
    }

    public boolean actualizarEstadoTarea(Tarea tarea, Estado nuevoEstado) {
        tarea.cambiarEstado(nuevoEstado);
        return tareaDAO.actualizar(tarea);
    }

    // NUEVO: Método para que el Hilo guarde en la BD
    public void guardarComparticionEnBD(Tarea tarea, Usuario destinatario) {
        tareaDAO.compartirTareaBD(tarea.getId(), destinatario.getId());
    }

    public void compartirTareaConcurrente(Tarea tarea, Usuario origen, Usuario destino) {
        // Le mandamos 'this' (el Gestor) al hilo para que tenga acceso a la base de datos
        Thread hilo = new Thread(new org.example.threads.CompartirTareaConcurrente(tarea, origen, destino, this));
        hilo.start();
    }
}