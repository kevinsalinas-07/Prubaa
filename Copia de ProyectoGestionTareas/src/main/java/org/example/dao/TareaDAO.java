package org.example.dao;

import org.example.model.Tarea;
import java.util.List;

public interface TareaDAO {
    // El contrato ahora exige el usuarioId
    boolean guardar(Tarea tarea, int usuarioId);
    Tarea buscarPorId(int id);
    List<Tarea> listarPorUsuario(int usuarioId);
    boolean actualizar(Tarea tarea);
    boolean eliminar(int id);
}