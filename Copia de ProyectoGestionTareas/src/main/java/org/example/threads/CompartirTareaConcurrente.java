package org.example.threads;

import org.example.model.Estado;
import org.example.model.Tarea;
import org.example.model.Usuario;

public class CompartirTareaConcurrente implements Runnable {

    private Tarea tarea;
    private Usuario emisor;
    private Usuario destinatario;

    public CompartirTareaConcurrente(Tarea tarea, Usuario emisor, Usuario destinatario) {
        this.tarea = tarea;
        this.emisor = emisor;
        this.destinatario = destinatario;
    }

    @Override
    public void run() {
        try {
            System.out.println("\n  [Hilo CompartirTareaConcurrente] " + emisor.getNombre()
                    + " compartiendo: " + tarea.getTitulo());
            Thread.sleep(5000);
            emisor.compartirTarea(tarea, destinatario);
            tarea.cambiarEstado(Estado.EN_PROGRESO);
            tarea.visualizar();
            System.out.println("  [Hilo CompartirTareaConcurrente] Proceso finalizado.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

