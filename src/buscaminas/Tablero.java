/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Adriana Hinojar 
 * @author Ainara Vanesa Tusan 
 * @author Maria Garcia
 * 
 */

public class Tablero implements Serializable{
    private int dimX;
    private int dimY;
    private int numMinas;
    private Casilla[][] casillas;
    
    public Tablero(int dimX, int dimY, int numMinas){
        this.dimX = dimX;
        this.dimY = dimY;
        this.numMinas = numMinas;
        this.casillas = new Casilla[dimX][dimY];
        
    }
    
    public int getDimX(){
        return dimX;
    }
    
    public int getDimY(){
        return dimY;
    }
    
    public void setDimX(int dimX){
        this.dimX = dimX;
    }
    public void setDimY(int dimY){
        this.dimY = dimY;
    }
    
    
    public int getNumMinas(){
        return numMinas;
    }
    
    public void setNumMinas(int numMinas){
        this.numMinas = numMinas;
    }
    
    public Casilla getCasilla(int x, int y){
        return casillas[x][y];
    }
    
    public void inicializarTablero(){
        for (int x = 0; x < dimX; x++){
            for(int y = 0; y < dimY; y++){
                casillas[x][y] = new CasillaVacia(x, y);
            } 
        }
    }
    
    public void colocarMinas(){
        Random random = new Random();
        int minasColocadas = 0;
        
        while(minasColocadas < numMinas){
            int x = random.nextInt(dimX);
            int y = random.nextInt(dimY);
            
            if(!(casillas[x][y] instanceof CasillaConMina)){
                casillas[x][y] = new CasillaConMina(x, y);
                minasColocadas++;
            }
        }
    }
    
    public void colocarVidas(){
        Random random = new Random();
        int vidasColocadas = 0;
        
        while(vidasColocadas < 1){
            int x = random.nextInt(dimX);
            int y = random.nextInt(dimY);
            
            if(!(casillas[x][y] instanceof CasillaConMina) && !(casillas[x][y] instanceof CasillaConVida)){
                casillas[x][y] = new CasillaConVida(x, y);
                vidasColocadas++;
            }
        }
        //imprimirEstado();
    }
    
    public String obtenerValorCasilla(int x, int y) {
        Casilla casilla = getCasilla(x, y);

        if (casilla instanceof CasillaConMina || casilla instanceof CasillaConVida) {
            return casillas[x][y].toString() + "   ";
        } else {
            return casillas[x][y].toString() + "   ";
        }
    }
   
    public void imprimirTablero() {
    for (int x = 0; x < dimX; x++) {
        for (int y = 0; y < dimY; y++) {
            if (casillas[x][y].estaOculta()) {
                System.out.print("x\t");
            } else {
                System.out.print(casillas[x][y] + "\t");
            }
        }
        System.out.println("\n");
    }
}

    public void imprimirEstado() {
        System.out.println("Estado actual del tablero:");
        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                System.out.print(obtenerValorCasilla(x, y) + "\t");
            }
            System.out.println("\n");
        }
    }
}
