package org.example.model;

// Representa un usuario con plan clasico. Tiene restricciones: limite de tareas activas y solo
// puede compartir con un usuario a la vez
public class UsuarioClasico extends Usuario {

  private static final int LIMITE_TAREAS_ACTIVAS = 5;

  // Crea un nuevo usuario clasico
  public UsuarioClasico(String nombre, String email, String password) {
    super(nombre, email, password);
  }

  // Constructor para reconstruir desde la base de datos
  public UsuarioClasico(int id, String nombre, String email, String password) {
    super(id, nombre, email, password);
  }

  // Verifica si el usuario ha alcanzado el limite de tareas activas
  public boolean verificarLimiteTareas() {
    boolean alcanzado = contarTareasActivas() >= LIMITE_TAREAS_ACTIVAS;
    if (alcanzado) {
      System.out.println(
          "  [!] "
              + getNombre()
              + " alcanzo el limite de "
              + LIMITE_TAREAS_ACTIVAS
              + " tareas activas.");
    }
    return alcanzado;
  }

  @Override
  public boolean crearElemento(Elemento elemento) {
    if (elemento instanceof Tarea && verificarLimiteTareas()) {
      return false;
    }
    getElementos().add(elemento);
    System.out.println(
        "  [+] Elemento '" + elemento.getTitulo() + "' creado por " + getNombre() + ".");
    return true;
  }

  @Override
  public boolean compartirTarea(Tarea tarea, Usuario destinatario) {
    if (!getElementos().contains(tarea)) {
      System.out.println(
          "  [!] " + getNombre() + " no posee la tarea '" + tarea.getTitulo() + "'.");
      return false;
    }
    if (destinatario.getElementos().contains(tarea)) {
      System.out.println(
          "  [!] " + destinatario.getNombre() + " ya tiene acceso a '" + tarea.getTitulo() + "'.");
      return false;
    }
    destinatario.getElementos().add(tarea);
    System.out.println(
        "  [->] "
            + getNombre()
            + " compartio '"
            + tarea.getTitulo()
            + "' con "
            + destinatario.getNombre()
            + ".");
    return true;
  }

  @Override
  public String getTipoUsuario() {
    return "Clasico";
  }
}
