# Sistema de Gestión de Tareas Colaborativas

## Descripción general

Aplicación de gestión de tareas y recordatorios colaborativos desarrollada en Java.
Permite a distintos tipos de usuarios (Clásico y Premium) crear, organizar y compartir
tareas y recordatorios. Las tareas se diferencian por prioridad y los recordatorios
por un ícono distintivo. El sistema aplica principios de POO, el patrón de diseño
Strategy y concurrencia con hilos.

### Entregable 1 — Modelado y estructura inicial

- Diagrama de clases UML con herencia, polimorfismo, encapsulamiento y abstracción.
- Diagrama de objetos UML con instancias de ejemplo.
- Implementación del modelo en Java con ejecución en consola.
- Datos quemados para verificar el funcionamiento.

### Entregable 2 — Patrones de diseño y concurrencia

- Actualización del diagrama de clases y objetos.
- Diagrama de secuencia y diagrama de actividad con concurrencia.
- Implementación del patrón Strategy para visualización de elementos.
- Implementación de hilos (`extends Thread` y `implements Runnable`) para accesos concurrentes.
- Método `synchronized` en `Tarea.cambiarEstado()` para evitar conflictos entre hilos.

---

## Estructura del proyecto

```
ProyectoGestionTareas/
├── build.gradle
├── settings.gradle
├── README.md
├── diagramasUML/
│   ├── diagrama-clases.png
│   ├── diagrama-objetos.png
│   ├── diagrama-secuencia.png
│   └── diagrama-actividad.png
└── src/
    └── main/
        └── java/
            ├── org.example.Main.java
            ├── org.example.model/
            │   ├── Autenticable.java
            │   ├── Elemento.java
            │   ├── EstrategiaVisualizacion.java
            │   ├── Estado.java
            │   ├── Prioridad.java
            │   ├── Recordatorio.java
            │   ├── Tarea.java
            │   ├── Usuario.java
            │   ├── UsuarioClasico.java
            │   └── UsuarioPremium.java
            ├── org.example.patterns/
            │   └── strategy/
            │       ├── VisualizacionSimple.java
            │       └── VisualizacionCompleta.java
            ├── org.example.service/
            │   └── GestorUsuarios.java
            └── org.example.threads/
                ├── EnvioRecordatorio.java
                └── CompartirTareaConcurrente.java
```

---

## Guía de uso

### Requisitos

- **Java:** Oracle OpenJDK 17.0.18 o superior
- **Build tool:** Gradle
- **IDE:** IntelliJ IDEA

### Cómo ejecutar

1. Clonar el repositorio:
   ```
   git clone https://github.com/UCASV/proyectofinalpoo2026-grupoproyectofinali.git
   ```
2. Abrir el proyecto en IntelliJ IDEA: **File → Open** → seleccionar la carpeta `ProyectoGestionTareas`.
3. Esperar a que Gradle sincronice las dependencias.
4. Ejecutar la clase `org.example.Main` (click derecho → **Run 'org.example.Main'**).

### Cómo compilar desde terminal

```
./gradlew run
```

En Windows:

```
gradlew.bat run
```

---

## Casos de prueba — datos quemados

La clase `org.example.Main` ejecuta todas las pruebas automáticamente al correr el programa.

| # | Prueba | Resultado esperado |
|---|--------|--------------------|
| 1 | Registro de usuarios (Clásico y Premium) | Se registran 3 usuarios: Ana (Clásico), Carlos (Premium), María (Clásico) |
| 2 | Autenticación con credenciales correctas e incorrectas | Carlos se autentica correctamente; los otros dos intentos muestran fallo |
| 3 | Creación de tareas y recordatorios | Ana crea 2 tareas + 1 recordatorio; Carlos crea 2 tareas + 1 recordatorio |
| 4 | Cambio de estado de tareas | tarea1 → EN_PROGRESO, tarea2 → EN_PROGRESO, tarea3 → COMPLETADA, tarea4 → CANCELADA |
| 5 | Compartir tareas entre usuarios | Ana comparte con Carlos; Carlos comparte con Ana y María; intentos inválidos muestran error |
| 6 | Límite de tareas para usuario clásico | María llega al límite de 5 tareas activas; la 6.ª no se crea |
| 7 | Patrón Strategy — visualización | tarea1 y rec1 usan `VisualizacionCompleta`; tarea2 y rec2 usan `VisualizacionSimple`; se cambia la estrategia de tarea1 en tiempo de ejecución |
| 8 | Concurrencia — hilos en paralelo | `hiloEnvio` espera 3 s y activa la alerta de rec2; `hiloCompartir` espera 5 s y comparte tarea2 de Ana con Carlos; el programa usa `join()` para esperar que ambos terminen |

### Formato de datos de usuario

- **Nombre:** texto — ej. `"Ana Lopez"`
- **Correo:** email válido — ej. `"ana@mail.com"`
- **Contraseña:** texto — ej. `"ana123"`
- **Tipo:** `UsuarioClasico` o `UsuarioPremium`

---

## Principios de POO aplicados

- **Herencia:** `Tarea` y `Recordatorio` extienden `Elemento`; `UsuarioClasico` y `UsuarioPremium` extienden `Usuario`.
- **Polimorfismo:** `mostrarInfo()` se comporta diferente en `Tarea` y `Recordatorio`; `crearElemento()` y `compartirTarea()` varían según el tipo de usuario; `visualizar()` delega en la estrategia configurada.
- **Encapsulamiento:** atributos privados con getters/setters; lógica interna protegida en cada clase.
- **Abstracción:** `Elemento` y `Usuario` son clases abstractas; `Autenticable` y `EstrategiaVisualizacion` son interfaces.

## Patrón de diseño aplicado

**Strategy — Visualización de elementos**

La interfaz `EstrategiaVisualizacion` define cómo se visualiza un elemento. Cada `Elemento` tiene una estrategia que se puede cambiar en tiempo de ejecución mediante `setEstrategia()`.

- `VisualizacionSimple` — muestra solo el título y la prioridad.
- `VisualizacionCompleta` — muestra todos los detalles (título, descripción, prioridad, estado o fecha).

## Concurrencia

El sistema implementa dos hilos que se ejecutan en paralelo:

- **`EnvioRecordatorio` (extends Thread):** simula el envío de un recordatorio con una espera de 3 segundos antes de activar la alerta.
- **`CompartirTareaConcurrente` (implements Runnable):** simula que un usuario comparte una tarea con otro y actualiza su estado, con una espera de 5 segundos.

El método `cambiarEstado()` en `Tarea` está marcado como `synchronized` para garantizar que solo un hilo lo ejecute a la vez. El `org.example.Main` usa `join()` para esperar que ambos hilos terminen antes de mostrar el listado final.