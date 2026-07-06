package org.example.threads;

import org.example.model.Recordatorio;

/**
 * Simula la espera de 3 segundos antes de activar la alerta de un
 * recordatorio, ejecutandose en un hilo aparte para no bloquear la
 * interfaz grafica.
 */
public class EnvioRecordatorio extends Thread {

    private final Recordatorio recordatorio;
    private final Runnable alAlertar;

    public EnvioRecordatorio(Recordatorio recordatorio, Runnable alAlertar) {
        this.recordatorio = recordatorio;
        this.alAlertar = alAlertar;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        recordatorio.activarAlerta();
        if (alAlertar != null) {
            alAlertar.run();
        }
    }
}
