package org.example.model;

public abstract class Elemento {

  private static int contadorId = 0;

  private final int id;
  private String titulo;
  private String descripcion;
  private Prioridad prioridad;
  private EstrategiaVisualizacion estrategia;

  protected Elemento(String titulo, String descripcion, Prioridad prioridad) {
    this.id = ++contadorId;
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.prioridad = prioridad;
  }
  // Constructor sobrecargado para reconstruir desde la base de datos (DAO)
  protected Elemento(int id, String titulo, String descripcion, Prioridad prioridad) {
    this.id = id;
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.prioridad = prioridad;
  }

  public abstract void mostrarInfo();

  public void visualizar() {
    if (estrategia != null) {
      estrategia.visualizar(this);
    } else {
      mostrarInfo();
    }
  }

  public void setEstrategia(EstrategiaVisualizacion estrategia) {
    this.estrategia = estrategia;
  }

  public int getId() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Prioridad getPrioridad() {
    return prioridad;
  }

  public void setPrioridad(Prioridad prioridad) {
    this.prioridad = prioridad;
  }

  public static void reiniciarContador() {
    contadorId = 0;
  }
}