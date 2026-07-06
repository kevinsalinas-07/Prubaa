package org.example.model;

// Enumeracion que representa los niveles de prioridad de una tarea
// Cada nivel tiene un color asociado para la interfaz grafica
public enum Prioridad {
  ALTA,
  MEDIA,
  BAJA;

  // Retorna el color asociado a la prioridad
  public String getColor() {
    return switch (this) {
      case ALTA -> "Rojo";
      case MEDIA -> "Naranja";
      case BAJA -> "Verde";
    };
  }
}
