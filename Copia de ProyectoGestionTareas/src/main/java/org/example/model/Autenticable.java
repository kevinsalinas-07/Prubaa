package org.example.model;

// Interfaz que define el comportamiento de autenticacion para los usuarios del sistema
public interface Autenticable {

  boolean autenticar(String email, String password);
}
