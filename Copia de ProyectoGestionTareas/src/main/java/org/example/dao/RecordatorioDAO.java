package org.example.dao;

import org.example.model.Recordatorio;
import java.util.List;

public interface RecordatorioDAO {

    // Guarda un nuevo recordatorio asociado al ID del usuario dueño
    boolean guardar(Recordatorio recordatorio, int usuarioId);

    // Busca un recordatorio específico por su ID
    Recordatorio buscarPorId(int id);

    // Trae solo los recordatorios del usuario que inició sesión
    List<Recordatorio> listarPorUsuario(int usuarioId);

    // Actualiza el título, descripción, prioridad o fecha del recordatorio
    boolean actualizar(Recordatorio recordatorio);

    // Elimina el recordatorio de la base de datos
    boolean eliminar(int id);
}