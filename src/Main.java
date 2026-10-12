public class Main {

    public static void main(String[] args) {
        Playlist playlist = new Playlist();
        playlist.insertarAlFinal(new Cancion("S1", "Bohemian Rhapsody", "Queen", 355));
        playlist.insertarAlFinal(new Cancion("S2", "Don't Stop Me Now", "Queen", 209));
        playlist.insertarAlFinal(new Cancion("S3", "I Want It That Way", "Backstreet Boys", 213));
        playlist.insertarAlFinal(new Cancion("S4", "Bye Bye Bye", "NSYNC", 200));

        titulo("1. Playlist inicial (4 canciones)");
        System.out.println(playlist.NavegarAdelanteYAtras());

        titulo("2. Duración total");
        System.out.println(playlist.DuracionTotalPlaylist());

        titulo("3. Mover la última canción (S4) al inicio");
        System.out.println("Movida: " + playlist.MoverCancionAlInicio("S4"));
        System.out.println(playlist.NavegarAdelanteYAtras());
        mostrarPunteros(playlist);
        System.out.println("Integridad de punteros: " + playlist.verificarIntegridad());

        titulo("4. Mover un id que no existe");
        System.out.println("Movida: " + playlist.MoverCancionAlInicio("XX"));

        titulo("5. Eliminar canciones de 'Queen'");
        System.out.println("Eliminadas: " + playlist.EliminarCancionPorArtista("Queen"));
        System.out.println(playlist.NavegarAdelanteYAtras());
        System.out.println("Duración total: " + playlist.DuracionTotalPlaylist());
        System.out.println("Integridad de punteros: " + playlist.verificarIntegridad());

        titulo("6. Eliminar un artista inexistente");
        System.out.println("Eliminadas: " + playlist.EliminarCancionPorArtista("Nadie"));

        titulo("7. Intercambiar S3 y S4");
        System.out.println("Intercambiadas: " + playlist.IntercambiarCanciones("S3", "S4"));
        System.out.println(playlist.NavegarAdelanteYAtras());
        mostrarPunteros(playlist);
        System.out.println("Integridad de punteros: " + playlist.verificarIntegridad());

        titulo("8. Intercambiar con un id que no existe");
        System.out.println("Intercambiadas: " + playlist.IntercambiarCanciones("S3", "XX"));
    }

    private static void mostrarPunteros(Playlist playlist) {
        for (NodoDoble n = playlist.getPrimero(); n != null; n = n.getSiguiente()) {
            String prev = n.getAnterior() == null ? "null" : n.getAnterior().getCancion().getId();
            String next = n.getSiguiente() == null ? "null" : n.getSiguiente().getCancion().getId();
            System.out.printf("  %s: prev=%s, next=%s%n", n.getCancion().getId(), prev, next);
        }
    }

    private static void titulo(String texto) {
        System.out.println("\n=== " + texto + " ===");
    }
}
