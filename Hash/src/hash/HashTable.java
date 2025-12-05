package hash;

/**
 *
 * @author marye
 */
public class HashTable {

    private Entry inicio = null;

    public void add(String username, long pos) {

        Entry nuevoUsuario = new Entry(username, pos);

        if (inicio == null) {
            inicio = nuevoUsuario;
        } else {
            Entry temp = inicio;

            while (temp.siguiente != null) {
                temp = temp.siguiente;
            }
            temp.siguiente = nuevoUsuario;
        }

    }
    
    
}
