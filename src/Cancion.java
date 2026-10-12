public final class Cancion {

    private String id;
    private String titulo;
    private String artista;
    private int duracionSegundos;

    /**
     * @throws IllegalArgumentException si id/título/artista son nulos o vacíos,
     *                                  o si la duración es negativa.
     */
    public Cancion(String id, String titulo, String artista, int duracionSegundos) {
        setId(id);
        setTitulo(titulo);
        setArtista(artista);
        setDuracionSegundos(duracionSegundos);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = textoObligatorio(id, "id");
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = textoObligatorio(titulo, "titulo");
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = textoObligatorio(artista, "artista");
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(int duracionSegundos) {
        if (duracionSegundos < 0) {
            throw new IllegalArgumentException("La duración no puede ser negativa: " + duracionSegundos);
        }
        this.duracionSegundos = duracionSegundos;
    }

    private static String textoObligatorio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' no puede ser nulo ni vacío");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%d:%02d)", id, titulo, artista,
                duracionSegundos / 60, duracionSegundos % 60);
    }
}
