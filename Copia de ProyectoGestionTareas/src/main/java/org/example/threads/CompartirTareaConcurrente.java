package org.example.threads;

import org.example.model.Estado;
import org.example.model.Tarea;
import org.example.model.Usuario;
import org.example.service.GestorUsuarios;

public class CompartirTareaConcurrente implements Runnable {

    private Tarea tarea;
    private Usuario emisor;
    private Usuario destinatario;
    private GestorUsuarios gestor; // Recibe al gestor para hablar con PostgreSQL

    public CompartirTareaConcurrente(Tarea tarea, Usuario emisor, Usuario destinatario, GestorUsuarios gestor) {
        this.tarea = tarea;
        this.emisor = emisor;
        this.destinatario = destinatario;
        this.gestor = gestor;
    }

    @Override
    public void run() {
        try {
            System.out.println("\n  [Hilo Compartir] " + emisor.getNombre() + " está compartiendo: " + tarea.getTitulo());

            // Simulamos el proceso en la red (3 segunditos)
            Thread.sleep(3000);

            // 1. Guardamos la relación en PostgreSQL (Tabla intermedia)
            gestor.guardarComparticionEnBD(tarea, destinatario);

            // 2. Cambiamos el estado a EN_PROGRESO (Esto actualiza RAM y BD al mismo tiempo)
            gestor.actualizarEstadoTarea(tarea, Estado.EN_PROGRESO);

            // 3. Visualizamos usando tu Patrón Strategy
            tarea.visualizar();

            System.out.println("  [Hilo Compartir] Proceso finalizado. Tarea compartida con éxito de forma persistente.");
        } catch (InterruptedException e) {
            System.err.println("  [Hilo Compartir] Error en la concurrencia.");
            Thread.currentThread().interrupt();
        }
    }
}