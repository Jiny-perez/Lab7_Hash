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

    public boolean remove(String username) {
        Entry actual = inicio;
        Entry anterior = null;

        while (actual != null) {
          
            if (username.equals(actual.username)) {
                if (anterior == null) {
                    inicio = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                return true;
            }
            anterior=actual;
            actual=actual.siguiente;
            
        }
        return false;
    }
    
    public long search (String username){
        Entry temp=inicio;
        
        while(temp!=null){
           if(username.equals(temp.username)){
               return temp.posicion;
           }
            temp=temp.siguiente;
        }
        return -1;
    }

}
