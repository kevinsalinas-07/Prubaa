package org.example.model;

public class Tarea extends Elemento {

  private Estado estado;

  public Tarea(String titulo, String descripcion, Prioridad prioridad) {
    super(titulo, descripcion, prioridad);
    this.estado = Estado.PENDIENTE;
  }
  // Constructor para reconstruir desde la base de datos
  public Tarea(int id, String titulo, String descripcion, Prioridad prioridad) {
    super(id, titulo, descripcion, prioridad); // Le pasamos el ID al padre
    this.estado = Estado.PENDIENTE;
  }

  public synchronized void cambiarEstado(Estado nuevoEstado) {
    System.out.println(
            "  [Tarea #" + getId() + "] Estado cambiado: " + this.estado + " -> " + nuevoEstado);
    this.estado = nuevoEstado;
  }

  public Estado getEstado() {
    return estado;
  }

  @Override
  public void mostrarInfo() {
    System.out.println("  [TAREA] ID: " + getId());
    System.out.println("    Titulo:      " + getTitulo());
    System.out.println("    Descripcion: " + getDescripcion());
    System.out.println(
            "    Prioridad:   " + getPrioridad() + " (" + getPrioridad().getColor() + ")");
    System.out.println("    Estado:      " + estado);
  }
}