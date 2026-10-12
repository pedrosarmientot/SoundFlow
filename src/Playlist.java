public class Playlist {

    private NodoDoble primero;
    private NodoDoble ultimo;
    private int tamanio;

    public Playlist() {
        this.primero = null;
        this.ultimo = null;
        this.tamanio = 0;
    }

    // ------------------------------------------------------------------
    // Inserción y consulta básica
    // ------------------------------------------------------------------

    /** Agrega una canción al final de la playlist. */
    public void insertarAlFinal(Cancion cancion) {
        validarNueva(cancion);
        NodoDoble nuevo = new NodoDoble(cancion);
        if (ultimo == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.setAnterior(ultimo);
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
        tamanio++;
    }

    /** Agrega una canción al principio de la playlist. */
    public void insertarAlInicio(Cancion cancion) {
        validarNueva(cancion);
        enlazarAlInicio(new NodoDoble(cancion));
    }

    /** @return la canción con ese id, o null si no existe. */
    public Cancion buscarPorId(String id) {
        NodoDoble nodo = buscarNodo(id);
        return nodo == null ? null : nodo.getCancion();
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public int getTamanio() {
        return tamanio;
    }

    public NodoDoble getPrimero() {
        return primero;
    }

    public NodoDoble getUltimo() {
        return ultimo;
    }

    // ------------------------------------------------------------------
    // a) NavegarAdelanteYAtras()
    // ------------------------------------------------------------------

    /**
     * Recorre la playlist en orden directo (usando "siguiente" desde el primero) y en orden
     * inverso (usando "anterior" desde el último) y retorna un texto con ambos recorridos.
     * En una playlist vacía indica "(playlist vacía)" (no lanza excepción).
     *
     * @author Onel Alejandro Olivera Díaz
     */
    public String NavegarAdelanteYAtras() {
        return "Adelante: " + unir(recorrerAdelante()) + "\nAtras:    " + unir(recorrerAtras());
    }

    /** Recorrido directo: primero → último, siguiendo los punteros "siguiente". */
    public Cancion[] recorrerAdelante() {
        Cancion[] resultado = new Cancion[tamanio];
        int i = 0;
        for (NodoDoble actual = primero; actual != null; actual = actual.getSiguiente()) {
            resultado[i++] = actual.getCancion();
        }
        return resultado;
    }

    /** Recorrido inverso: último → primero, siguiendo los punteros "anterior". */
    public Cancion[] recorrerAtras() {
        Cancion[] resultado = new Cancion[tamanio];
        int i = 0;
        for (NodoDoble actual = ultimo; actual != null; actual = actual.getAnterior()) {
            resultado[i++] = actual.getCancion();
        }
        return resultado;
    }

    // ------------------------------------------------------------------
    // b) MoverCancionAlInicio(String id)
    // ------------------------------------------------------------------

    /**
     * Ubica la canción con el id indicado al principio de la playlist reenlazando
     * los punteros anterior y siguiente (el nodo no se copia: se mueve).
     *
     * @return true si la canción existía (y quedó en la primera posición);
     *         false si la playlist está vacía o el id no se encontró (la lista no cambia).
     * @throws IllegalArgumentException si el id es nulo o vacío.
     * @author Pedro Luis Sarmiento Torres
     */
    public boolean MoverCancionAlInicio(String id) {
        validarId(id);
        NodoDoble nodo = buscarNodo(id);
        if (nodo == null) {
            return false;
        }
        if (nodo == primero) {
            return true; // ya está al inicio: no hay nada que reenlazar
        }
        desenlazar(nodo);
        enlazarAlInicio(nodo);
        return true;
    }

    // ------------------------------------------------------------------
    // c) DuracionTotalPlaylist()
    // ------------------------------------------------------------------

    /**
     * Suma la duración de todas las canciones y la devuelve en minutos y segundos,
     * por ejemplo "16 min 17 s". Una playlist vacía (o con canciones de 0 s) devuelve "0 min 0 s".
     * Los minutos no se convierten a horas (3600 s = "60 min 0 s").
     *
     * @author Daniela González Alemán
     */
    public String DuracionTotalPlaylist() {
        long total = 0; // long: evita desbordamiento con playlists muy largas
        for (NodoDoble actual = primero; actual != null; actual = actual.getSiguiente()) {
            total += actual.getCancion().getDuracionSegundos();
        }
        return (total / 60) + " min " + (total % 60) + " s";
    }

    // ------------------------------------------------------------------
    // d) EliminarCancionPorArtista(String artista)
    // ------------------------------------------------------------------

    /**
     * Elimina todas las canciones del artista indicado (sin distinguir mayúsculas
     * ni espacios en los extremos) y retorna la cantidad eliminada.
     * Si el artista no tiene canciones o la lista está vacía retorna 0.
     *
     * @throws IllegalArgumentException si el artista es nulo o vacío.
     * @author María Fernanda Rodríguez Díaz
     */
    public int EliminarCancionPorArtista(String artista) {
        if (artista == null || artista.trim().isEmpty()) {
            throw new IllegalArgumentException("El artista no puede ser nulo ni vacío");
        }
        String buscado = artista.trim();
        int eliminadas = 0;
        NodoDoble actual = primero;
        while (actual != null) {
            NodoDoble proximo = actual.getSiguiente(); // se guarda antes de desenlazar
            if (actual.getCancion().getArtista().equalsIgnoreCase(buscado)) {
                desenlazar(actual);
                eliminadas++;
            }
            actual = proximo;
        }
        return eliminadas;
    }

    // ------------------------------------------------------------------
    // e) IntercambiarCanciones(String id1, String id2)
    // ------------------------------------------------------------------

    /**
     * Intercambia las posiciones de las canciones con id1 e id2 reenlazando los nodos.
     * Retorna false si alguno de los ids no existe; lanza IllegalArgumentException si alguno es nulo o vacío.
     *
     * @author Amalia Aguiar Pereda
     */
    public boolean IntercambiarCanciones(String id1, String id2) {
        validarId(id1);
        validarId(id2);
        NodoDoble a = buscarNodo(id1);
        NodoDoble b = buscarNodo(id2);
        if (a == null || b == null) {
            return false;
        }
        if (a == b) {
            return true;
        }
        NodoDoble antA = a.getAnterior();
        NodoDoble sigA = a.getSiguiente();
        NodoDoble antB = b.getAnterior();
        NodoDoble sigB = b.getSiguiente();

        if (sigA == b) {
            a.setAnterior(b);
            a.setSiguiente(sigB);
            b.setAnterior(antA);
            b.setSiguiente(a);
            if (antA != null) antA.setSiguiente(b);
            if (sigB != null) sigB.setAnterior(a);
        } else if (sigB == a) {
            b.setAnterior(a);
            b.setSiguiente(sigA);
            a.setAnterior(antB);
            a.setSiguiente(b);
            if (antB != null) antB.setSiguiente(a);
            if (sigA != null) sigA.setAnterior(b);
        } else {
            a.setAnterior(antB);
            a.setSiguiente(sigB);
            b.setAnterior(antA);
            b.setSiguiente(sigA);
            if (antA != null) antA.setSiguiente(b);
            if (sigA != null) sigA.setAnterior(b);
            if (antB != null) antB.setSiguiente(a);
            if (sigB != null) sigB.setAnterior(a);
        }

        if (primero == a) primero = b;
        else if (primero == b) primero = a;
        if (ultimo == a) ultimo = b;
        else if (ultimo == b) ultimo = a;
        return true;
    }

    // ------------------------------------------------------------------
    // Verificación de consistencia (usada por las pruebas)
    // ------------------------------------------------------------------

    /**
     * Comprueba que los punteros sean consistentes en ambos sentidos:
     * primero.anterior == null, ultimo.siguiente == null, nodo.siguiente.anterior == nodo
     * para cada nodo, y que el tamaño coincida con la cantidad real de nodos.
     */
    public boolean verificarIntegridad() {
        if (primero == null || ultimo == null) {
            return primero == null && ultimo == null && tamanio == 0;
        }
        if (primero.getAnterior() != null || ultimo.getSiguiente() != null) {
            return false;
        }
        int contadorAdelante = 0;
        NodoDoble ultimoVisitado = null;
        for (NodoDoble n = primero; n != null; n = n.getSiguiente()) {
            if (n.getSiguiente() != null && n.getSiguiente().getAnterior() != n) {
                return false;
            }
            contadorAdelante++;
            ultimoVisitado = n;
            if (contadorAdelante > tamanio) {
                return false; // ciclo o tamaño incorrecto
            }
        }
        int contadorAtras = 0;
        for (NodoDoble n = ultimo; n != null; n = n.getAnterior()) {
            contadorAtras++;
            if (contadorAtras > tamanio) {
                return false;
            }
        }
        return ultimoVisitado == ultimo && contadorAdelante == tamanio && contadorAtras == tamanio;
    }

    // ------------------------------------------------------------------
    // Métodos auxiliares privados
    // ------------------------------------------------------------------

    private NodoDoble buscarNodo(String id) {
        if (id == null) {
            return null;
        }
        for (NodoDoble actual = primero; actual != null; actual = actual.getSiguiente()) {
            if (actual.getCancion().getId().equals(id.trim())) {
                return actual;
            }
        }
        return null;
    }

    /** Enlaza un nodo (ya creado o previamente desenlazado) como nuevo primero. */
    private void enlazarAlInicio(NodoDoble nodo) {
        nodo.setAnterior(null);
        nodo.setSiguiente(primero);
        if (primero != null) {
            primero.setAnterior(nodo);
        } else {
            ultimo = nodo;
        }
        primero = nodo;
        tamanio++;
    }

    /** Saca un nodo de la lista, reconectando sus vecinos (cabeza, cola o intermedio). */
    private void desenlazar(NodoDoble nodo) {
        NodoDoble ant = nodo.getAnterior();
        NodoDoble sig = nodo.getSiguiente();
        if (ant != null) {
            ant.setSiguiente(sig);
        } else {
            primero = sig;
        }
        if (sig != null) {
            sig.setAnterior(ant);
        } else {
            ultimo = ant;
        }
        nodo.setAnterior(null);
        nodo.setSiguiente(null);
        tamanio--;
    }

    private void validarNueva(Cancion cancion) {
        if (cancion == null) {
            throw new IllegalArgumentException("La canción no puede ser nula");
        }
        if (buscarNodo(cancion.getId()) != null) {
            throw new IllegalArgumentException("Ya existe una canción con id " + cancion.getId());
        }
    }

    private static String unir(Cancion[] canciones) {
        if (canciones.length == 0) {
            return "(playlist vacía)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < canciones.length; i++) {
            sb.append(i == 0 ? "" : " -> ").append(canciones[i].getTitulo());
        }
        return sb.toString();
    }

    private static void validarId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id no puede ser nulo ni vacío");
        }
    }
}
