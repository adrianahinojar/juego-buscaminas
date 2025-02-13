/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas;


public class CasillaConMina extends Casilla{
    
    public CasillaConMina(int x, int y){
        super(x, y);
    }
    
    @Override
    public boolean esMina(){
        return true;
    }
    
    @Override
    public boolean esVida(){
        return false;
    }
    
    @Override
    public String toString(){
        return "M";
    }
}
