package org.example.model;

import java.util.ArrayList;
import java.util.List;

// Clase abstracta que representa un usuario del sistema. Implementa Autenticable para la
// gestion de credenciales. Aplica herencia y polimorfismo al ser extendida por tipos especificos
public abstract class Usuario implements Autenticable {

  private static int contadorId = 0;

  private final int id;
  private String nombre;
  private String email;
  private String password;
  private final List<Elemento> elementos;

  // Crea un nuevo usuario con los datos proporcionados
  protected Usuario(String nombre, String email, String password) {
    this.id = ++contadorId;
    this.nombre = nombre;
    this.email = email;
    this.password = password;
    this.elementos = new ArrayList<>();
  }
  // Constructor sobrecargado para uso exclusivo de los DAOs (Reconstrucción desde Base de Datos)
  protected Usuario(int id, String nombre, String email, String password) {
    this.id = id;
    this.nombre = nombre;
    this.email = email;
    this.password = password;
    this.elementos = new ArrayList<>();
  }

  @Override
  public boolean autenticar(String email, String password) {
    return this.email.equals(email) && this.password.equals(password);
  }

  // Crea un elemento y lo agrega a la lista del usuario. Las subclases pueden sobrescribir para
  // aplicar restricciones
  public abstract boolean crearElemento(Elemento elemento);

  // Comparte una tarea con otro usuario. Las subclases definen las restricciones de comparticion
  public abstract boolean compartirTarea(Tarea tarea, Usuario destinatario);

  // Muestra todos los elementos del usuario en consola
  public void listarElementos() {
    System.out.println("\n--- Elementos de " + nombre + " (" + getTipoUsuario() + ") ---");
    if (elementos.isEmpty()) {
      System.out.println("  (Sin elementos que mostrar.)");
      return;
    }
    for (Elemento elemento : elementos) {
      elemento.mostrarInfo();
      System.out.println();
    }
  }

  // Retorna el tipo de usuario como texto
  public abstract String getTipoUsuario();

  public int getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Elemento> getElementos() {
    return elementos;
  }

  // Reinicia el contador de IDs
  public static void reiniciarContador() {
    contadorId = 0;
  }

  // Cuenta las tareas activas (no completadas ni canceladas)
  protected int contarTareasActivas() {
    int contador = 0;
    for (Elemento elemento : elementos) {
      if (elemento instanceof Tarea tarea) {
        if (tarea.getEstado() != Estado.COMPLETADA && tarea.getEstado() != Estado.CANCELADA) {
          contador++;
        }
      }
    }
    return contador;
  }
  public String getIniciales() {
    if (nombre == null || nombre.isEmpty()) return "";
    String[] partes = nombre.trim().split(" ");
    if (partes.length == 1) return partes[0].substring(0, 1).toUpperCase();
    return (partes[0].substring(0, 1) + partes[partes.length - 1].substring(0, 1)).toUpperCase();
  }
}
