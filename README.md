# Buscaminas

Este proyecto es una implementación del clásico juego "Buscaminas" en Java, que incluye varias clases y una interfaz gráfica para jugar. 

## Estructura del Proyecto

El proyecto está organizado en diferentes paquetes y clases, cada uno con su propia funcionalidad:

- **Buscaminas**: Clase principal que gestiona el juego. Se encarga de administrar los jugadores y las partidas, así como de la lógica del juego.
- **Jugador**: Clase que representa a un jugador en el juego, almacenando información como el nombre, la puntuación y las partidas jugadas.
- **Partida**: Clase que representa una partida de Buscaminas, incluyendo la inicialización del tablero y el manejo de la lógica de juego.
- **Nivel**: Enum que define los diferentes niveles de dificultad del juego (Principiante, Intermedio, Experto y Personalizado).
- **Resultado**: Enum que define los posibles resultados de una partida (Ganada, Perdida, Abandonada).
- **Interfaz**: Clase que proporciona la interfaz gráfica del juego, permitiendo a los jugadores interactuar con el juego de manera visual.

## Funcionalidades

- **Gestión de Jugadores**: Los jugadores pueden ser añadidos, eliminados y listados. Se pueden guardar y cargar los datos de los jugadores desde archivos de texto y binarios.
- **Partidas**: Los jugadores pueden iniciar partidas en diferentes niveles de dificultad, y el estado de las partidas se guarda automáticamente.
- **Ranking**: Se generan archivos de texto con el ranking de jugadores basado en sus puntuaciones, así como un listado completo de jugadores.
- **Interfaz Gráfica**: Una interfaz gráfica fácil de usar que permite jugar al Buscaminas de manera visual.

## Requisitos

- Java 8 o superior
- IDE de tu elección (se recomienda IntelliJ IDEA)

## Cómo Ejecutar el Proyecto

1. Clona el repositorio en tu máquina local.
2. Abre el proyecto en tu IDE.
3. Ejecuta la clase `Buscaminas` para iniciar el juego. Puedes optar por usar la interfaz gráfica o la consola.
