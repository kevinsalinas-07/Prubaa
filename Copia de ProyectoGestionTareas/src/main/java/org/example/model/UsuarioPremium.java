package org.example.model;

// Representa un usuario con plan premium. No tiene restricciones funcionales: puede crear tareas
// ilimitadas y compartir con varios usuarios
public class UsuarioPremium extends Usuario {

  // Crea un nuevo usuario premium
  public UsuarioPremium(String nombre, String email, String password) {
    super(nombre, email, password);
  }

  // Constructor para reconstruir desde la base de datos
  public UsuarioPremium(int id, String nombre, String email, String password) {
    super(id, nombre, email, password);
  }

  // Muestra un mensaje indicando que el usuario tiene acceso completo a las funcionalidades
  public void accesoCompleto() {
    System.out.println("  [*] " + getNombre() + " tiene acceso Premium sin restricciones.");
  }

  @Override
  public boolean crearElemento(Elemento elemento) {
    getElementos().add(elemento);
    System.out.println(
        "  [+] Elemento '" + elemento.getTitulo() + "' creado por " + getNombre() + " (Premium).");
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
            + " (Premium) compartio '"
            + tarea.getTitulo()
            + "' con "
            + destinatario.getNombre()
            + ".");
    return true;
  }

  @Override
  public String getTipoUsuario() {
    return "Premium";
  }
}
