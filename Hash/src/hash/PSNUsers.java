/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hash;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author najma
 */
public class PSNUsers {
    
    private RandomAccessFile archivo;
    private HashTable users;
    
    public PSNUsers(String nombreArchivo) throws IOException {
        
        users = new HashTable();
        archivo = new RandomAccessFile(nombreArchivo + ".psn", "rw");
        reloadHashTable();
    }
    
    private void reloadHashTable() throws IOException {
        archivo.seek(0);
        
        while (archivo.getFilePointer() < archivo.length()) {
            long pos = archivo.getFilePointer();
            
            byte[] buffer = new byte[50];
            archivo.readFully(buffer);
            String username = new String(buffer, "UTF-8").trim().replace("\0", "");
            
            archivo.readInt();
            archivo.readInt();
            boolean activo = archivo.readBoolean();
            
            if (activo) {
                users.add(username, pos);
            }
        }
    }
    
    public void addUser(String username) throws IOException {
        
        archivo.seek(archivo.length());
        long pos = archivo.getFilePointer();
        
        byte[] buffer = new byte[50];
        byte[] data = username.getBytes("UTF-8");
        int len = Math.min(data.length, 50);
        System.arraycopy(data, 0, buffer, 0, len);
        archivo.write(buffer);
        
        archivo.writeInt(0);
        archivo.writeInt(0);
        archivo.writeBoolean(true);
        
        users.add(username, pos);
    }
    
    public void deactivateUser(String username) throws IOException {
        
        long pos = users.search(username);
        
        if (pos != -1) {
            archivo.seek(pos + 50 + 8);
            archivo.writeBoolean(false);
            users.remove(username);
        }
    }
    
    public void addTrophyTo(String username, String trophyGame, String trophyName, 
            Trophy type, byte[] trophyImageBytes) throws IOException {
        
        long pos = users.search(username);
        if (pos == -1) {
            System.out.println("El usuario no existe");
            return;
        }
        
        archivo.seek(pos + 50);
        int puntos = archivo.readInt();
        archivo.seek(pos + 50);
        archivo.writeInt(puntos + type.points);
        
        int trofeos = archivo.readInt();
        archivo.seek(pos + 54);
        archivo.writeInt(trofeos + 1);
        
        RandomAccessFile rtrophy = new RandomAccessFile("trophies.psn", "rw");
        rtrophy.seek(rtrophy.length());
        
        escribir(rtrophy, username, 50);
        escribir(rtrophy, type.name(), 20);
        escribir(rtrophy, trophyGame, 100);
        escribir(rtrophy, trophyName, 100);
        rtrophy.writeLong(System.currentTimeMillis());
        rtrophy.writeInt(trophyImageBytes.length);
        rtrophy.write(trophyImageBytes);
        
        rtrophy.close();
    }
    
    public void playerInfo(String username) throws IOException {
        long pos = users.search(username);
        
        if (pos == -1) {
            System.out.println("El usuario no se encontra");
            return;
        }
        
        archivo.seek(pos + 50);
        int puntos = archivo.readInt();
        int trofeos = archivo.readInt();
        
        System.out.println("Usuario: " + username);
        System.out.println("Puntos: " + puntos);
        System.out.println("Trofeos: " + trofeos);
        System.out.println();
        
        File f = new File("trophies.psn");
        if (!f.exists()) 
            return;
        
        RandomAccessFile rtrophy = new RandomAccessFile("trophies.psn", "r");
        
        while (rtrophy.getFilePointer() < rtrophy.length()) {
            String user = leer(rtrophy, 50);
            String tipo = leer(rtrophy, 20);
            String juego = leer(rtrophy, 100);
            String nombre = leer(rtrophy, 100);
            long fecha = rtrophy.readLong();
            int tam = rtrophy.readInt();
            rtrophy.skipBytes(tam);
            
            if (user.equals(username)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                System.out.println(sdf.format(new Date(fecha)) + " - " + tipo + " - " 
                        + juego + " - " + nombre + " - (IMAGEN)");
            }
        }
        
        rtrophy.close();
    }
    
    private String leer(RandomAccessFile raf, int tam) throws IOException {
        
        byte[] buffer = new byte[tam];
        raf.readFully(buffer);
        return new String(buffer, "UTF-8").trim().replace("\0", "");
    }
    
    private void escribir(RandomAccessFile raf, String texto, int tam) throws IOException {
        
        byte[] buffer = new byte[tam];
        byte[] data = texto.getBytes("UTF-8");
        int len = Math.min(data.length, tam);
        System.arraycopy(data, 0, buffer, 0, len);
        raf.write(buffer);
    }
    
    public void cerrar() throws IOException {
        if (archivo != null) {
            archivo.close();
        }
    }
}
