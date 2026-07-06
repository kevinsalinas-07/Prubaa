package org.example.model;

// Enumeracion que representa los posibles estados de una tarea
public enum Estado {
  PENDIENTE,
  EN_PROGRESO,
  COMPLETADA,
  CANCELADA;

  public String getEtiqueta() {
    switch(this) {
      case PENDIENTE: return "Pendiente";
      case EN_PROGRESO: return "En progreso";
      case COMPLETADA: return "Completada";
      default: return this.name();
    }
  }
}