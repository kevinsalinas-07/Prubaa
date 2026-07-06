package org.example.dao;

import org.example.model.Tarea;
import java.util.List;

public interface TareaDAO {
    boolean guardar(Tarea tarea, int usuarioId);
    Tarea buscarPorId(int id);
    List<Tarea> listarPorUsuario(int usuarioId);
    boolean actualizar(Tarea tarea);
    boolean eliminar(int id);

    // --- NUEVOS MÉTODOS PARA COLABORACIÓN ---
    // Guarda el registro en la tabla intermedia
    boolean compartirTareaBD(int tareaId, int destinatarioId);

    // Busca las tareas que OTRAS personas le han compartido a este usuario
    List<Tarea> listarCompartidasPorUsuario(int usuarioId);
}