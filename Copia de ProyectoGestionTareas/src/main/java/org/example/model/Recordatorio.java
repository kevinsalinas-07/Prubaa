package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Representa un recordatorio dentro del sistema. Extiende de Elemento y agrega una fecha
// y un icono distintivo para diferenciarlo de las tareas
public class Recordatorio extends Elemento {

  private static final String ICONO = "[REC]";

  private LocalDate fecha;

  // Crea un nuevo recordatorio con la fecha indicada
  public Recordatorio(String titulo, String descripcion, Prioridad prioridad, LocalDate fecha) {
    super(titulo, descripcion, prioridad);
    this.fecha = fecha;
  }
  // Constructor para reconstruir desde la base de datos (Uso exclusivo del DAO)
  public Recordatorio(int id, String titulo, String descripcion, Prioridad prioridad, LocalDate fecha) {
    super(id, titulo, descripcion, prioridad); // Le pasa el ID real a la clase Elemento
    this.fecha = fecha;
  }

  // Imprime un mensaje simulando la activacion de la alerta del recordatorio
  public void activarAlerta() {
    System.out.println(
        "  "
            + ICONO
            + " ALERTA: Recordatorio '"
            + getTitulo()
            + "' - Fecha: "
            + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  @Override
  public void mostrarInfo() {
    System.out.println("  " + ICONO + " [RECORDATORIO] ID: " + getId());
    System.out.println("    Titulo:      " + getTitulo());
    System.out.println("    Descripcion: " + getDescripcion());
    System.out.println(
        "    Prioridad:   " + getPrioridad() + " (" + getPrioridad().getColor() + ")");
    System.out.println(
        "    Fecha:       " + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
  }
  public String getFechaFormateada() {
    if (this.fecha == null) return "";
    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return this.fecha.format(formatter);
  }
}
