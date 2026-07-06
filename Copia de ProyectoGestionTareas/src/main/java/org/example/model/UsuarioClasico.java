package org.example.model;

public class UsuarioClasico extends Usuario {

  private static final int LIMITE_TAREAS_ACTIVAS = 5;
  private int tareasCompartidas = 0; // NUEVO: Contador de comparticiones

  public UsuarioClasico(String nombre, String email, String password) {
    super(nombre, email, password);
  }

  public UsuarioClasico(int id, String nombre, String email, String password) {
    super(id, nombre, email, password);
  }

  public boolean verificarLimiteTareas() {
    boolean alcanzado = contarTareasActivas() >= LIMITE_TAREAS_ACTIVAS;
    if (alcanzado) {
      System.out.println("  [!] " + getNombre() + " alcanzo el limite de " + LIMITE_TAREAS_ACTIVAS + " tareas activas.");
    }
    return alcanzado;
  }

  @Override
  public boolean crearElemento(Elemento elemento) {
    if (elemento instanceof Tarea && verificarLimiteTareas()) {
      return false;
    }
    getElementos().add(elemento);
    return true;
  }

  @Override
  public boolean compartirTarea(Tarea tarea, Usuario destinatario) {
    // NUEVA REGLA: Si ya compartió 1 vez, lo bloqueamos
    if (tareasCompartidas >= 1) {
      System.out.println("  [!] " + getNombre() + " no puede compartir más (Límite Clásico alcanzado).");
      return false;
    }

    if (!getElementos().contains(tarea) || destinatario.getElementos().contains(tarea)) {
      return false;
    }

    destinatario.getElementos().add(tarea);
    tareasCompartidas++; // Aumentamos el contador
    return true;
  }

  @Override
  public String getTipoUsuario() {
    return "Clasico";
  }
}