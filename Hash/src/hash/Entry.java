/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hash;

/**
 *
 * @author marye
 */
public class Entry {
    String username;
    long posicion;

    Entry siguiente;

    public Entry(String username, long posicion) {
        this.username = username;
        this.posicion = posicion;
        siguiente = null;
    }
}
