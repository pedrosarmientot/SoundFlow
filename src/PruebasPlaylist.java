public final class PruebasPlaylist {

    private PruebasPlaylist() {
    }

    public static void main(String[] args) {
        pruebasEstructura();
        pruebasNavegar();
        pruebasMover();
        pruebasDuracion();
        pruebasEliminar();
        pruebasIntercambiar();

        System.out.println("\n==============================");
        System.out.println("Pruebas ejecutadas: " + total);
        System.out.println("Exitosas:           " + (total - fallidas));
        System.out.println("Fallidas:           " + fallidas);
        System.out.println("==============================");
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    // ==================================================================
    // Pruebas
    // ==================================================================

    private static void pruebasEstructura() {
        seccion("Estructura base (Cancion, NodoDoble, inserción)");

        prueba("Cancion: constructor, getters y toString", () -> {
            Cancion c = new Cancion("S1", "Tema", "Artista", 125);
            assertEquals("S1", c.getId(), "id");
            assertEquals("Tema", c.getTitulo(), "titulo");
            assertEquals("Artista", c.getArtista(), "artista");
            assertEquals(125, c.getDuracionSegundos(), "duracion");
            assertEquals("[S1] Tema - Artista (2:05)", c.toString(), "toString");
        });

        prueba("Cancion: setters actualizan los campos", () -> {
            Cancion c = new Cancion("S1", "Tema", "Artista", 10);
            c.setId("S9");
            c.setTitulo("Nuevo");
            c.setArtista("Otro");
            c.setDuracionSegundos(0);
            assertEquals("S9", c.getId(), "id");
            assertEquals("Nuevo", c.getTitulo(), "titulo");
            assertEquals("Otro", c.getArtista(), "artista");
            assertEquals(0, c.getDuracionSegundos(), "duracion cero permitida");
        });

        prueba("Cancion (negativa): id vacío/nulo lanza IllegalArgumentException", () -> {
            assertThrows(IllegalArgumentException.class, () -> new Cancion(" ", "T", "A", 1), "id vacío");
            assertThrows(IllegalArgumentException.class, () -> new Cancion(null, "T", "A", 1), "id nulo");
        });

        prueba("Cancion (negativa): título/artista nulos y duración negativa", () -> {
            assertThrows(IllegalArgumentException.class, () -> new Cancion("S1", null, "A", 1), "titulo nulo");
            assertThrows(IllegalArgumentException.class, () -> new Cancion("S1", "T", "", 1), "artista vacío");
            assertThrows(IllegalArgumentException.class, () -> new Cancion("S1", "T", "A", -1), "duración negativa");
        });

        prueba("NodoDoble: constructor deja anterior y siguiente en null", () -> {
            Cancion c = c("S1", "A", 1);
            NodoDoble n = new NodoDoble(c);
            assertSame(c, n.getCancion(), "cancion");
            assertNull(n.getAnterior(), "anterior");
            assertNull(n.getSiguiente(), "siguiente");
        });

        prueba("Playlist nueva está vacía", () -> {
            Playlist p = new Playlist();
            assertTrue(p.estaVacia(), "vacía");
            assertEquals(0, p.getTamanio(), "tamaño");
            assertNull(p.getPrimero(), "primero");
            assertNull(p.getUltimo(), "último");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Insertar 1 canción: primero y último son el mismo nodo", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("S1", "A", 10));
            assertSame(p.getPrimero(), p.getUltimo(), "mismo nodo");
            assertNull(p.getPrimero().getAnterior(), "prev null");
            assertNull(p.getPrimero().getSiguiente(), "next null");
            assertEquals(1, p.getTamanio(), "tamaño");
        });

        prueba("Caso sugerido: insertar 4 canciones y verificar punteros prev/next", () -> {
            Playlist p = playlistCuatro();
            assertEquals(4, p.getTamanio(), "tamaño 4");
            NodoDoble n1 = p.getPrimero();
            NodoDoble n2 = n1.getSiguiente();
            NodoDoble n3 = n2.getSiguiente();
            NodoDoble n4 = n3.getSiguiente();
            assertEquals("S1", n1.getCancion().getId(), "n1");
            assertEquals("S4", n4.getCancion().getId(), "n4");
            assertNull(n1.getAnterior(), "S1.prev == null");
            assertSame(n1, n2.getAnterior(), "S2.prev == S1");
            assertSame(n2, n3.getAnterior(), "S3.prev == S2");
            assertSame(n3, n4.getAnterior(), "S4.prev == S3");
            assertNull(n4.getSiguiente(), "S4.next == null");
            assertSame(n4, p.getUltimo(), "ultimo == S4");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("insertarAlInicio coloca la canción como primera", () -> {
            Playlist p = playlistCuatro();
            p.insertarAlInicio(c("S0", "Z", 5));
            assertEquals("S0,S1,S2,S3,S4", ids(p.recorrerAdelante()), "orden");
            assertNull(p.getPrimero().getAnterior(), "prev del primero");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("insertarAlInicio en lista vacía", () -> {
            Playlist p = new Playlist();
            p.insertarAlInicio(c("S1", "A", 1));
            assertSame(p.getPrimero(), p.getUltimo(), "único nodo");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("buscarPorId: encontrada y no encontrada", () -> {
            Playlist p = playlistCuatro();
            assertEquals("S3", p.buscarPorId("S3").getId(), "encontrada");
            assertNull(p.buscarPorId("S99"), "no encontrada");
            assertNull(p.buscarPorId(null), "nulo");
            assertNull(new Playlist().buscarPorId("S1"), "lista vacía");
        });

        prueba("Insertar (negativa): id duplicado lanza IllegalArgumentException", () -> {
            Playlist p = playlistCuatro();
            assertThrows(IllegalArgumentException.class, () -> p.insertarAlFinal(c("S2", "X", 1)), "duplicado final");
            assertThrows(IllegalArgumentException.class, () -> p.insertarAlInicio(c("S2", "X", 1)), "duplicado inicio");
            assertEquals(4, p.getTamanio(), "tamaño intacto");
        });

        prueba("Insertar (negativa): canción nula lanza IllegalArgumentException", () -> {
            Playlist p = new Playlist();
            assertThrows(IllegalArgumentException.class, () -> p.insertarAlFinal(null), "nula final");
            assertThrows(IllegalArgumentException.class, () -> p.insertarAlInicio(null), "nula inicio");
            assertTrue(p.estaVacia(), "sigue vacía");
        });
    }

    private static void pruebasNavegar() {
        seccion("a) NavegarAdelanteYAtras() — Onel Alejandro Olivera Díaz");

        prueba("Positiva: recorrido directo e inverso de 4 canciones", () -> {
            Playlist p = playlistCuatro();
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S4,S3,S2,S1", ids(p.recorrerAtras()), "atrás");
        });

        prueba("Positiva: el inverso es exactamente el directo al revés", () -> {
            Playlist p = playlistCuatro();
            Cancion[] adelante = p.recorrerAdelante();
            Cancion[] atras = p.recorrerAtras();
            assertEquals(adelante.length, atras.length, "misma cantidad");
            for (int i = 0; i < adelante.length; i++) {
                assertSame(adelante[i], atras[adelante.length - 1 - i], "posición " + i);
            }
        });

        prueba("Positiva: NavegarAdelanteYAtras devuelve el texto con ambos órdenes", () -> {
            String esperado = "Adelante: Bohemian Rhapsody -> Don't Stop Me Now -> I Want It That Way -> Bye Bye Bye\n"
                    + "Atras:    Bye Bye Bye -> I Want It That Way -> Don't Stop Me Now -> Bohemian Rhapsody";
            assertEquals(esperado, playlistCuatro().NavegarAdelanteYAtras(), "texto");
        });

        prueba("Borde: una sola canción (ambos recorridos iguales)", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("S1", "A", 1));
            assertEquals("S1", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S1", ids(p.recorrerAtras()), "atrás");
            assertEquals("Adelante: Tema S1\nAtras:    Tema S1", p.NavegarAdelanteYAtras(), "texto");
        });

        prueba("Negativa/borde: lista vacía devuelve recorridos vacíos sin excepción", () -> {
            Playlist p = new Playlist();
            assertEquals(0, p.recorrerAdelante().length, "adelante vacío");
            assertEquals(0, p.recorrerAtras().length, "atrás vacío");
        });

        prueba("Negativa/borde: lista vacía indica «(playlist vacía)» en el texto", () -> {
            String texto = new Playlist().NavegarAdelanteYAtras();
            assertEquals("Adelante: (playlist vacía)\nAtras:    (playlist vacía)", texto, "texto");
        });

        prueba("Positiva: el recorrido refleja inserciones al inicio", () -> {
            Playlist p = playlistCuatro();
            p.insertarAlInicio(c("S0", "Z", 5));
            assertEquals("S0,S1,S2,S3,S4", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S4,S3,S2,S1,S0", ids(p.recorrerAtras()), "atrás");
        });

        prueba("Robustez: modificar el arreglo devuelto no altera la playlist", () -> {
            Playlist p = playlistCuatro();
            Cancion[] copia = p.recorrerAdelante();
            copia[0] = null;
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "playlist intacta");
        });
    }

    private static void pruebasMover() {
        seccion("b) MoverCancionAlInicio(String id) — Pedro Luis Sarmiento Torres");

        prueba("Caso sugerido: mover la ÚLTIMA canción al inicio y verificar prev/next", () -> {
            Playlist p = playlistCuatro();
            assertTrue(p.MoverCancionAlInicio("S4"), "debe retornar true");
            NodoDoble s4 = p.getPrimero();
            NodoDoble s1 = s4.getSiguiente();
            NodoDoble s2 = s1.getSiguiente();
            NodoDoble s3 = s2.getSiguiente();
            assertEquals("S4", s4.getCancion().getId(), "S4 es el primero");
            assertNull(s4.getAnterior(), "S4.prev == null");
            assertSame(s1, s4.getSiguiente(), "S4.next == S1");
            assertSame(s4, s1.getAnterior(), "S1.prev == S4");
            assertSame(s1, s2.getAnterior(), "S2.prev == S1");
            assertSame(s2, s3.getAnterior(), "S3.prev == S2");
            assertNull(s3.getSiguiente(), "S3.next == null (nuevo último)");
            assertSame(s3, p.getUltimo(), "último == S3");
            assertEquals(4, p.getTamanio(), "tamaño intacto");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: el orden en ambos sentidos tras mover la última", () -> {
            Playlist p = playlistCuatro();
            p.MoverCancionAlInicio("S4");
            assertEquals("S4,S1,S2,S3", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S3,S2,S1,S4", ids(p.recorrerAtras()), "atrás");
        });

        prueba("Positiva: mover una canción intermedia (S3)", () -> {
            Playlist p = playlistCuatro();
            assertTrue(p.MoverCancionAlInicio("S3"), "true");
            assertEquals("S3,S1,S2,S4", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S4,S2,S1,S3", ids(p.recorrerAtras()), "atrás");
            assertSame(p.getUltimo().getCancion(), p.buscarPorId("S4"), "S4 sigue siendo el último");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Borde: mover la que ya es primera no cambia nada y retorna true", () -> {
            Playlist p = playlistCuatro();
            assertTrue(p.MoverCancionAlInicio("S1"), "true");
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "sin cambios");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Borde: playlist con una sola canción", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("S1", "A", 1));
            assertTrue(p.MoverCancionAlInicio("S1"), "true");
            assertSame(p.getPrimero(), p.getUltimo(), "único nodo");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Borde: playlist de 2 canciones, mover la segunda", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("A", "X", 1));
            p.insertarAlFinal(c("B", "X", 1));
            p.MoverCancionAlInicio("B");
            assertEquals("B,A", ids(p.recorrerAdelante()), "adelante");
            assertEquals("A,B", ids(p.recorrerAtras()), "atrás");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: movimientos consecutivos mantienen la integridad", () -> {
            Playlist p = playlistCuatro();
            p.MoverCancionAlInicio("S4");
            p.MoverCancionAlInicio("S3");
            p.MoverCancionAlInicio("S2");
            assertEquals("S2,S3,S4,S1", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S1,S4,S3,S2", ids(p.recorrerAtras()), "atrás");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Negativa: id no encontrado retorna false y la lista no cambia", () -> {
            Playlist p = playlistCuatro();
            assertFalse(p.MoverCancionAlInicio("S99"), "false");
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "sin cambios");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Negativa: lista vacía retorna false sin excepción", () -> {
            Playlist p = new Playlist();
            assertFalse(p.MoverCancionAlInicio("S1"), "false");
            assertTrue(p.estaVacia(), "sigue vacía");
        });

        prueba("Negativa: id nulo o vacío lanza IllegalArgumentException", () -> {
            Playlist p = playlistCuatro();
            assertThrows(IllegalArgumentException.class, () -> p.MoverCancionAlInicio(null), "nulo");
            assertThrows(IllegalArgumentException.class, () -> p.MoverCancionAlInicio("  "), "vacío");
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "sin cambios");
        });

        prueba("Borde: los ids se comparan de forma exacta (s4 != S4)", () -> {
            Playlist p = playlistCuatro();
            assertFalse(p.MoverCancionAlInicio("s4"), "minúscula no coincide");
        });
    }

    private static void pruebasDuracion() {
        seccion("c) DuracionTotalPlaylist() — Daniela González Alemán");

        prueba("Positiva: 4 canciones suman 977 s = 16 min 17 s", () -> {
            assertEquals("16 min 17 s", playlistCuatro().DuracionTotalPlaylist(), "texto");
        });

        prueba("Borde: lista vacía devuelve 0 min 0 s", () -> {
            assertEquals("0 min 0 s", new Playlist().DuracionTotalPlaylist(), "texto");
        });

        prueba("Borde: canciones con duración cero suman 0", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("S1", "A", 0));
            p.insertarAlFinal(c("S2", "A", 0));
            assertEquals("0 min 0 s", p.DuracionTotalPlaylist(), "texto");
        });

        prueba("Borde: una canción de 59 s → 0 min 59 s", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("S1", "A", 59));
            assertEquals("0 min 59 s", p.DuracionTotalPlaylist(), "texto");
        });

        prueba("Borde: exactamente 60 s → 1 min 0 s", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("S1", "A", 60));
            assertEquals("1 min 0 s", p.DuracionTotalPlaylist(), "texto");
        });

        prueba("Borde: la suma de segundos se acarrea a minutos (45 + 45 = 1 min 30 s)", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("S1", "A", 45));
            p.insertarAlFinal(c("S2", "A", 45));
            assertEquals("1 min 30 s", p.DuracionTotalPlaylist(), "texto");
        });

        prueba("Borde: 3600 s se expresa como 60 min 0 s (sin horas)", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("S1", "A", 3600));
            assertEquals("60 min 0 s", p.DuracionTotalPlaylist(), "texto");
        });

        prueba("Robustez: la suma no se desborda (2 x Integer.MAX_VALUE)", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("S1", "A", Integer.MAX_VALUE));
            p.insertarAlFinal(c("S2", "A", Integer.MAX_VALUE));
            assertEquals("71582788 min 14 s", p.DuracionTotalPlaylist(), "4294967294 s");
        });

        prueba("Positiva: la duración total cambia al eliminar canciones", () -> {
            Playlist p = playlistCuatro();
            p.EliminarCancionPorArtista("Queen"); // quita 355 + 209 → quedan 413 s
            assertEquals("6 min 53 s", p.DuracionTotalPlaylist(), "413 s");
        });

        prueba("Positiva: mover canciones no altera la duración total", () -> {
            Playlist p = playlistCuatro();
            p.MoverCancionAlInicio("S4");
            assertEquals("16 min 17 s", p.DuracionTotalPlaylist(), "igual");
        });
    }

    private static void pruebasEliminar() {
        seccion("d) EliminarCancionPorArtista(String) — María Fernanda Rodríguez Díaz");

        prueba("Positiva: eliminar 2 canciones de 'Queen' (al inicio) y verificar punteros", () -> {
            Playlist p = playlistCuatro();
            assertEquals(2, p.EliminarCancionPorArtista("Queen"), "cantidad");
            assertEquals(2, p.getTamanio(), "tamaño");
            assertEquals("S3,S4", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S4,S3", ids(p.recorrerAtras()), "atrás");
            assertNull(p.getPrimero().getAnterior(), "S3.prev == null");
            assertEquals("S3", p.getPrimero().getCancion().getId(), "nuevo primero");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: eliminar la última canción (cola)", () -> {
            Playlist p = playlistCuatro();
            assertEquals(1, p.EliminarCancionPorArtista("NSYNC"), "cantidad");
            assertEquals("S3", p.getUltimo().getCancion().getId(), "nuevo último");
            assertNull(p.getUltimo().getSiguiente(), "S3.next == null");
            assertEquals("S1,S2,S3", ids(p.recorrerAdelante()), "orden");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: eliminar una canción intermedia reenlaza los vecinos", () -> {
            Playlist p = playlistCuatro();
            assertEquals(1, p.EliminarCancionPorArtista("Backstreet Boys"), "cantidad");
            assertSame(p.buscarPorId("S2"), p.getPrimero().getSiguiente().getCancion(), "S1.next == S2");
            assertEquals("S1,S2,S4", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S4,S2,S1", ids(p.recorrerAtras()), "atrás");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: eliminar múltiples canciones NO consecutivas del mismo artista", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("1", "A", 10));
            p.insertarAlFinal(c("2", "B", 10));
            p.insertarAlFinal(c("3", "A", 10));
            p.insertarAlFinal(c("4", "B", 10));
            p.insertarAlFinal(c("5", "A", 10));
            assertEquals(3, p.EliminarCancionPorArtista("A"), "cantidad");
            assertEquals("2,4", ids(p.recorrerAdelante()), "adelante");
            assertEquals("4,2", ids(p.recorrerAtras()), "atrás");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Borde: eliminar TODAS las canciones deja la lista vacía", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("1", "A", 10));
            p.insertarAlFinal(c("2", "A", 10));
            assertEquals(2, p.EliminarCancionPorArtista("A"), "cantidad");
            assertTrue(p.estaVacia(), "vacía");
            assertNull(p.getPrimero(), "primero null");
            assertNull(p.getUltimo(), "último null");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Borde: playlist con una sola canción", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("1", "A", 10));
            assertEquals(1, p.EliminarCancionPorArtista("A"), "cantidad");
            assertTrue(p.estaVacia(), "vacía");
        });

        prueba("Positiva: se puede volver a insertar tras vaciar la lista", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("1", "A", 10));
            p.EliminarCancionPorArtista("A");
            p.insertarAlFinal(c("2", "B", 20));
            assertEquals("2", ids(p.recorrerAdelante()), "orden");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Negativa: artista sin canciones retorna 0 y no modifica la lista", () -> {
            Playlist p = playlistCuatro();
            assertEquals(0, p.EliminarCancionPorArtista("Nadie"), "cantidad");
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "sin cambios");
            assertEquals(4, p.getTamanio(), "tamaño");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Negativa/borde: lista vacía retorna 0", () -> {
            assertEquals(0, new Playlist().EliminarCancionPorArtista("Queen"), "cantidad");
        });

        prueba("Negativa: artista nulo o vacío lanza IllegalArgumentException", () -> {
            Playlist p = playlistCuatro();
            assertThrows(IllegalArgumentException.class, () -> p.EliminarCancionPorArtista(null), "nulo");
            assertThrows(IllegalArgumentException.class, () -> p.EliminarCancionPorArtista("   "), "vacío");
            assertEquals(4, p.getTamanio(), "tamaño intacto");
        });

        prueba("Borde: comparación sin distinguir mayúsculas ni espacios ('  qUEEN ')", () -> {
            Playlist p = playlistCuatro();
            assertEquals(2, p.EliminarCancionPorArtista("  qUEEN "), "cantidad");
        });

        prueba("Borde: un artista que sólo contiene el texto buscado NO se elimina", () -> {
            Playlist p = playlistCuatro();
            assertEquals(0, p.EliminarCancionPorArtista("Que"), "coincidencia parcial");
            assertEquals(4, p.getTamanio(), "tamaño");
        });

        prueba("Positiva: eliminar dos artistas seguidos", () -> {
            Playlist p = playlistCuatro();
            assertEquals(2, p.EliminarCancionPorArtista("Queen"), "Queen");
            assertEquals(1, p.EliminarCancionPorArtista("NSYNC"), "NSYNC");
            assertEquals("S3", ids(p.recorrerAdelante()), "queda S3");
            assertSame(p.getPrimero(), p.getUltimo(), "único nodo");
            assertTrue(p.verificarIntegridad(), "integridad");
        });
    }

    private static void pruebasIntercambiar() {
        seccion("e) IntercambiarCanciones(String, String) — Amalia Aguiar Pereda");

        prueba("Positiva: intercambiar dos canciones intermedias no adyacentes", () -> {
            Playlist p = new Playlist();
            for (String id : new String[]{"1", "2", "3", "4", "5"}) {
                p.insertarAlFinal(c(id, "A", 10));
            }
            assertTrue(p.IntercambiarCanciones("2", "4"), "true");
            assertEquals("1,4,3,2,5", ids(p.recorrerAdelante()), "adelante");
            assertEquals("5,2,3,4,1", ids(p.recorrerAtras()), "atrás");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: intercambiar primera y última y verificar prev/next", () -> {
            Playlist p = playlistCuatro();
            assertTrue(p.IntercambiarCanciones("S1", "S4"), "true");
            NodoDoble s4 = p.getPrimero();
            NodoDoble s2 = s4.getSiguiente();
            NodoDoble s3 = s2.getSiguiente();
            NodoDoble s1 = s3.getSiguiente();
            assertEquals("S4", s4.getCancion().getId(), "S4 primero");
            assertEquals("S1", s1.getCancion().getId(), "S1 último");
            assertNull(s4.getAnterior(), "S4.prev == null");
            assertSame(s4, s2.getAnterior(), "S2.prev == S4");
            assertSame(s2, s3.getAnterior(), "S3.prev == S2");
            assertSame(s3, s1.getAnterior(), "S1.prev == S3");
            assertNull(s1.getSiguiente(), "S1.next == null");
            assertSame(s1, p.getUltimo(), "último == S1");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: adyacentes al inicio (S1, S2)", () -> {
            Playlist p = playlistCuatro();
            assertTrue(p.IntercambiarCanciones("S1", "S2"), "true");
            assertEquals("S2,S1,S3,S4", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S4,S3,S1,S2", ids(p.recorrerAtras()), "atrás");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: adyacentes en el medio con los ids al revés (S3, S2)", () -> {
            Playlist p = playlistCuatro();
            assertTrue(p.IntercambiarCanciones("S3", "S2"), "true");
            assertEquals("S1,S3,S2,S4", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S4,S2,S3,S1", ids(p.recorrerAtras()), "atrás");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: adyacentes al final (S3, S4) actualizan el último", () -> {
            Playlist p = playlistCuatro();
            assertTrue(p.IntercambiarCanciones("S3", "S4"), "true");
            assertEquals("S1,S2,S4,S3", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S3", p.getUltimo().getCancion().getId(), "último");
            assertNull(p.getUltimo().getSiguiente(), "último.next == null");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: el orden de los argumentos no cambia el resultado", () -> {
            Playlist p1 = playlistCuatro();
            Playlist p2 = playlistCuatro();
            p1.IntercambiarCanciones("S1", "S3");
            p2.IntercambiarCanciones("S3", "S1");
            assertEquals(ids(p1.recorrerAdelante()), ids(p2.recorrerAdelante()), "mismo orden");
            assertEquals("S3,S2,S1,S4", ids(p1.recorrerAdelante()), "orden esperado");
        });

        prueba("Positiva: intercambiar dos veces restaura el orden original", () -> {
            Playlist p = playlistCuatro();
            p.IntercambiarCanciones("S2", "S4");
            p.IntercambiarCanciones("S2", "S4");
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "adelante");
            assertEquals("S4,S3,S2,S1", ids(p.recorrerAtras()), "atrás");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Borde: playlist con solo dos canciones", () -> {
            Playlist p = new Playlist();
            p.insertarAlFinal(c("A", "X", 1));
            p.insertarAlFinal(c("B", "X", 1));
            assertTrue(p.IntercambiarCanciones("A", "B"), "true");
            assertEquals("B,A", ids(p.recorrerAdelante()), "adelante");
            assertEquals("A,B", ids(p.recorrerAtras()), "atrás");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Borde: mismo id en ambos argumentos retorna true sin cambios", () -> {
            Playlist p = playlistCuatro();
            assertTrue(p.IntercambiarCanciones("S2", "S2"), "true");
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "sin cambios");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Positiva: el tamaño y la duración total no cambian", () -> {
            Playlist p = playlistCuatro();
            p.IntercambiarCanciones("S1", "S4");
            assertEquals(4, p.getTamanio(), "tamaño");
            assertEquals("16 min 17 s", p.DuracionTotalPlaylist(), "duración");
        });

        prueba("Negativa: primer id no encontrado retorna false y la lista no cambia", () -> {
            Playlist p = playlistCuatro();
            assertFalse(p.IntercambiarCanciones("S99", "S2"), "false");
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "sin cambios");
            assertTrue(p.verificarIntegridad(), "integridad");
        });

        prueba("Negativa: segundo id no encontrado retorna false y la lista no cambia", () -> {
            Playlist p = playlistCuatro();
            assertFalse(p.IntercambiarCanciones("S2", "S99"), "false");
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "sin cambios");
        });

        prueba("Negativa: lista vacía retorna false sin excepción", () -> {
            assertFalse(new Playlist().IntercambiarCanciones("S1", "S2"), "false");
        });

        prueba("Negativa: ids nulos o vacíos lanzan IllegalArgumentException", () -> {
            Playlist p = playlistCuatro();
            assertThrows(IllegalArgumentException.class, () -> p.IntercambiarCanciones(null, "S2"), "id1 nulo");
            assertThrows(IllegalArgumentException.class, () -> p.IntercambiarCanciones("S1", " "), "id2 vacío");
            assertEquals("S1,S2,S3,S4", ids(p.recorrerAdelante()), "sin cambios");
        });
    }

    // ==================================================================
    // Mini marco de pruebas y datos de apoyo
    // ==================================================================

    private static int total = 0;
    private static int fallidas = 0;

    static void prueba(String nombre, Runnable cuerpo) {
        total++;
        try {
            cuerpo.run();
            System.out.println("  [PASS] " + nombre);
        } catch (Throwable t) {
            fallidas++;
            System.out.println("  [FAIL] " + nombre + " -> " + t);
        }
    }

    static void seccion(String titulo) {
        System.out.println("\n== " + titulo + " ==");
    }

    static void assertTrue(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    static void assertFalse(boolean condicion, String mensaje) {
        assertTrue(!condicion, mensaje);
    }

    static void assertEquals(Object esperado, Object real, String mensaje) {
        boolean iguales = esperado == null ? real == null : esperado.equals(real);
        if (!iguales) {
            throw new AssertionError(mensaje + " (esperado=" + esperado + ", real=" + real + ")");
        }
    }

    static void assertSame(Object esperado, Object real, String mensaje) {
        if (esperado != real) {
            throw new AssertionError(mensaje + " (no es el mismo objeto)");
        }
    }

    static void assertNull(Object valor, String mensaje) {
        if (valor != null) {
            throw new AssertionError(mensaje + " (se esperaba null, real=" + valor + ")");
        }
    }

    static void assertThrows(Class<? extends Throwable> tipo, Runnable cuerpo, String mensaje) {
        try {
            cuerpo.run();
        } catch (Throwable t) {
            if (tipo.isInstance(t)) {
                return;
            }
            throw new AssertionError(mensaje + " (se esperaba " + tipo.getSimpleName()
                    + " pero se lanzó " + t.getClass().getSimpleName() + ")");
        }
        throw new AssertionError(mensaje + " (no se lanzó " + tipo.getSimpleName() + ")");
    }

    /** Concatena los ids de un arreglo de canciones: "S1,S2,S3". */
    static String ids(Cancion[] canciones) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < canciones.length; i++) {
            sb.append(i == 0 ? "" : ",").append(canciones[i].getId());
        }
        return sb.toString();
    }

    /** Las 4 canciones del caso de prueba sugerido: S1,S2 (Queen), S3 (Backstreet Boys), S4 (NSYNC). */
    static Playlist playlistCuatro() {
        Playlist p = new Playlist();
        p.insertarAlFinal(new Cancion("S1", "Bohemian Rhapsody", "Queen", 355));
        p.insertarAlFinal(new Cancion("S2", "Don't Stop Me Now", "Queen", 209));
        p.insertarAlFinal(new Cancion("S3", "I Want It That Way", "Backstreet Boys", 213));
        p.insertarAlFinal(new Cancion("S4", "Bye Bye Bye", "NSYNC", 200));
        return p;
    }

    static Cancion c(String id, String artista, int segundos) {
        return new Cancion(id, "Tema " + id, artista, segundos);
    }

    static int total() {
        return total;
    }

    static int fallidas() {
        return fallidas;
    }
}
