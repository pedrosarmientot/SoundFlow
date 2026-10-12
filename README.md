# SoundFlow— Reproductor de música con Lista Doblemente Enlazada

*Examen Parcial · Estructuras de Datos (Curso 2026-2027) · Facultad de Ciencias Técnicas, Ingeniería Informática 2do año
(canciones de Queen, Backstreet Boys y NSYNC) · **Equipo 7** 

| Integrante | Método implementado |
|---|---|
| Onel Alejandro Olivera Díaz | a) `NavegarAdelanteYAtras()` |
| Pedro Luis Sarmiento Torres | b) `MoverCancionAlInicio(String id)` |
| Daniela González Alemán | c) `DuracionTotalPlaylist()` |
| María Fernanda Rodríguez Díaz | d) `EliminarCancionPorArtista(String artista)` |
| Amalia Aguiar Pereda | e) `IntercambiarCanciones(String id1, String id2)` |

## 1. Problemática: «SoundFlow»

Un reproductor de audio digital necesita controlar su lista de reproducción (playlist). De cada canción se conoce: **id, título, artista y duraciónSegundos**. El reproductor debe poder avanzar y retroceder por la lista, subir una canción al principio, calcular cuánto dura toda la playlist, limpiar las canciones de un artista e intercambiar la posición de dos canciones.

## 2. Estructura de datos: Lista Doblemente Enlazada

Cada canción vive dentro de un **nodo** (`NodoDoble`) que tiene dos punteros: `anterior` (prev) y `siguiente` (next). La playlist guarda el primer nodo (`primero`), el último (`ultimo`) y la cantidad de canciones (`tamanio`):

```
null <- [S1] <-> [S2] <-> [S3] <-> [S4] -> null
         ^                          ^
      primero                     ultimo
```

Reglas (invariantes) que siempre deben cumplirse y que verifica `Playlist.verificarIntegridad()` en las pruebas:

- `primero.anterior == null` y `ultimo.siguiente == null`.
- Para cada nodo, `nodo.siguiente.anterior == nodo`.
- Recorrer hacia adelante y hacia atrás visita exactamente `tamanio` nodos.

Se usan **solo nodos y punteros**: no se utiliza `ArrayList`, `LinkedList` ni listas simples.

### Clases (`src`)

| Clase | Descripción |
|---|---|
| `Cancion` | id, titulo, artista, duracionSegundos; constructor, getters, setters, `toString()`. Valida datos (sin vacíos, duración ≥ 0). |
| `NodoDoble` | cancion, anterior, siguiente; constructor. |
| `Playlist` | primero, ultimo, tamanio; inserción, consulta y los 5 métodos requeridos. |
| `Main` | Demostración por consola del caso sugerido. |

## 3. Métodos implementados y autoría

| Método | Qué hace | Autor |
|---|---|---|
| `NavegarAdelanteYAtras()` | Recorre la lista del primero al último (usando `siguiente`) y del último al primero (usando `anterior`); devuelve un texto con ambos órdenes. Complejidad O(n). | Onel Alejandro Olivera Díaz |
| `MoverCancionAlInicio(id)` | Busca el nodo, lo desenlaza reconectando sus vecinos y lo enlaza como nuevo `primero`. Retorna `true` si la canción existía. | Pedro Luis Sarmiento Torres |
| `DuracionTotalPlaylist()` | Suma los segundos (en `long`) y los expresa como minutos y segundos (`977 s` → `16 min 17 s`). | Daniela González Alemán |
| `EliminarCancionPorArtista(artista)` | Elimina todas las canciones del artista y retorna cuántas eliminó. | María Fernanda Rodríguez Díaz |
| `IntercambiarCanciones(id1, id2)` | Busca los dos nodos y los intercambia reenlazando sus punteros `anterior` y `siguiente` (sin copiar las canciones); contempla nodos adyacentes, primero y último. Retorna `true` si ambos existían, `false` si alguno no se encontró. | Amalia Aguiar Pereda |

Métodos de apoyo: `insertarAlFinal`, `insertarAlInicio`, `buscarPorId`, `estaVacia`, `getTamanio`, `recorrerAdelante`, `recorrerAtras`, `verificarIntegridad` y los privados `desenlazar` / `enlazarAlInicio` (reutilizados por mover y eliminar).


## 4. Casos de prueba ejecutados

Se usan aserciones manuales (sin JUnit) para que el proyecto no tenga dependencias y compile en cualquier JDK 11 o superior. **69 pruebas, todas exitosas** (todas en un solo archivo: `PruebasPlaylist.java`).

| Sección de `PruebasPlaylist` | N.º | Cubre |
|---|---|---|
| Estructura base | 13 | `Cancion`, `NodoDoble`, inserción, **caso sugerido: insertar 4 canciones y verificar prev/next**, duplicados, nulos |
| a) Navegar | 8 | Directo e inverso, texto devuelto, lista vacía, 1 canción, copias de los arreglos |
| b) Mover | 11 | **Caso sugerido: mover la última al inicio y verificar prev/next**, intermedia, ya primera, 1 y 2 nodos, id no encontrado, lista vacía, id nulo |
| c) Duración | 10 | 16 min 17 s, lista vacía, duración cero, 59/60/3600 s, acarreo, desbordamiento, tras eliminar y mover |
| d) Eliminar | 13 | Cabeza, cola, medio, no consecutivas, todas, 1 nodo, artista sin canciones, lista vacía, nulo/vacío, mayúsculas/espacios, coincidencia parcial |
| e) Intercambiar | 14 | No adyacentes, primera y última con prev/next, adyacentes (inicio, medio, final), argumentos al revés, doble intercambio, 2 nodos, mismo id, id no encontrado, lista vacía, id nulo |

Clasificación: **positivas** (flujos normales), **negativas** (lista vacía, elemento no encontrado, argumentos nulos/inválidos con excepción) y **de borde** (un solo nodo, cabeza, cola, duración 0, vaciar toda la lista). Después de cada operación que modifica enlaces se llama a `verificarIntegridad()`.

Datos del caso sugerido: S1 «Bohemian Rhapsody» (Queen, 355 s), S2 «Don't Stop Me Now» (Queen, 209 s), S3 «I Want It That Way» (Backstreet Boys, 213 s), S4 «Bye Bye Bye» (NSYNC, 200 s). Mover S4 al inicio deja `S4 <-> S1 <-> S2 <-> S3` con `S4.prev = null` y `S3.next = null`.

## 5. Estructura del repositorio

SoundFlow ED Equipo 7/
├── src/ (Main, Cancion, NodoDoble, Playlist, PruebasPlaylist)
├── SoundFlow_Equipo7_Presentacion.pptx   <- presentación (con notas del orador)
├── README.md
├── .idea/ y SoundFlow ED Equipo 7.iml   (configuración de IntelliJ)
└── .gitignore


## 6. Declaración sobre el uso de Inteligencia Artificial

Este proyecto fue **asistido por una herramienta de Inteligencia Artificial (Claude, de Anthropic)**. En concreto, la IA se empleó para generar el borrador del código, las pruebas y parte de la redacción del README. El equipo revisó el código linea por linea, lo compiló en IntelliJ, ejecutó las pruebas y cada integrante estudió y puede explicar el método que le correspondió.


