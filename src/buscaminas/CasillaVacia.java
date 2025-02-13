/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas;

/**
 *
 * @author Adriana Hinojar 
 * @author Ainara Vanesa Tusan 
 * @author Maria Garcia
 * 
 */

public class CasillaVacia extends Casilla{
    private int numMinasAlrededor;
    private boolean tieneVidaAlrededor;
    
    public CasillaVacia(int x, int y){
        super(x, y);
        numMinasAlrededor = 0;
        tieneVidaAlrededor=false;
    }
    
    @Override
    public boolean esVida(){
        return false;
    }
    
    @Override
    public boolean esMina(){
        return false;
    }
    
    public boolean getTieneVidasAlrededor(){
        return tieneVidaAlrededor;
    }
    
    public int getNumMinasAlrededor(){
        return numMinasAlrededor;
    }
     
    public void incrementarMinasAlrededor(){
        numMinasAlrededor++;
    }
    
    public void contarMinasYVidasAlrededor(Tablero tablero){
        int x = getX();
        int y = getY();

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
              
                if (i >= 0 && i < tablero.getDimX() && j >= 0 && j < tablero.getDimY()) {  // Comprueba que las coordenadas (i, j) están dentro de los límites del tablero
                    Casilla casillaActual = tablero.getCasilla(i, j);
                    
                    if (casillaActual instanceof CasillaConMina) {// Incrementa el contador si la casilla es una mina
                        incrementarMinasAlrededor();
                    }else if(casillaActual instanceof CasillaConVida){//indicar que la casilla tiene una vida
                        tieneVidaAlrededor=true;
                    }
                }
            }
        }
    }
    
    @Override
    public String toString() {
        if (tieneVidaAlrededor && numMinasAlrededor > 0) {//tiene minas y alguna vida alrededor
            return numMinasAlrededor + "V";
        } else if (tieneVidaAlrededor) {
            return "V";//tiene alguna vida alrededor
        } else if (numMinasAlrededor > 0) {
            return Integer.toString(numMinasAlrededor);//tiene alguna mina alrdedor
        } else {
            return "O";//no indica nada cerca
        }
    }
}


