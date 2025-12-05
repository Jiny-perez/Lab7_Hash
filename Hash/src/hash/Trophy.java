/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package hash;

/**
 *
 * @author axel
 */
public enum Trophy {
    PLATINO(5), ORO(3), PLATA(2), BRONCE(1);
    
    public final int points;
    
    private Trophy(int puntos){
        this.points=puntos;
    }
}
