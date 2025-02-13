/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas;

import java.io.Serializable;

/**
 *
 * @author Adriana Hinojar 
 * @author Ainara Vanesa Tusan 
 * @author Maria Garcia
 * 
 */

public abstract class Casilla implements Serializable {
    private int x;
    private int y;
    private boolean oculta;
   
    
    public Casilla(int x, int y){
        this.x = x;
        this.y = y;
        oculta = true; 
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public boolean estaOculta(){
        return oculta;
    }
    
    public void setOculta(boolean oculta){
        this.oculta = oculta;
    }
    
    // mostrar la casilla al jugador
    public void mostrarCasilla(){
        oculta = false;
    }
    
    public abstract boolean esMina();
    
    public abstract boolean esVida();   
}
