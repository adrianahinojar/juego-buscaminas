/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas;


public class CasillaConVida extends Casilla {
    public CasillaConVida(int x, int y){
        super(x, y);
    }
    
    @Override
    public boolean esVida(){
        return true;
    }
    
    @Override
    public boolean esMina(){
        return false;
    }
    
    @Override
    public String toString(){
        return ":)";
    }
}
