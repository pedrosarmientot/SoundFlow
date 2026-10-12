public class NodoDoble {

    private Cancion cancion;
    private NodoDoble anterior;
    private NodoDoble siguiente;

    public NodoDoble(Cancion cancion) {
        this.cancion = cancion;
        this.anterior = null;
        this.siguiente = null;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    public NodoDoble getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoDoble anterior) {
        this.anterior = anterior;
    }

    public NodoDoble getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoDoble siguiente) {
        this.siguiente = siguiente;
    }
}
