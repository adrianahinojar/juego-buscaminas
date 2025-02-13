package buscaminas;

import java.io.Serializable;
import java.util.ArrayList;


public class Jugador implements Serializable{
    private String nombre;
    private String foto;
    private int puntos;
    private int ganadas;
    private int perdidas;
    private ArrayList<Partida> partidas;
    
    public Jugador(String nombre, String foto){
        this(nombre);
        this.foto = foto;
        
    }
    
    public Jugador(String nombre){
            this.nombre=nombre;
            partidas=new ArrayList<>();
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getFoto(){
        return foto;
    }
    
    public void setFoto(String foto){
        this.foto = foto;
    }
    
    public int getPuntos(){
        return puntos;
    }
    
    public void setPuntos(int puntos){
        this.puntos += puntos;
    }
    
    public int getGanadas(){
        return ganadas;
    }
    
    public int getPerdidas(){
        return perdidas;
    }
    public ArrayList<Partida> getPartidas(){
        return partidas;
    }
    
    public void anyadirPartida(Partida p){
        if(p.getResultado() == Resultado.GANADA){
            ganadas++;
            puntos += p.calcularPuntos();
        }
        else {
            perdidas++;
        } 
        partidas.add(p);
    }
    
    public void restarPuntos(int puntos){
        this.puntos -= puntos;
        if(this.puntos < 0){
            this.puntos = 0;
        }
    }
    
    public void mostrarJugador(){
        System.out.println("Información jugador " + nombre + ":");
        System.out.println("\tPuntos totales: " + puntos);
        System.out.println("\tPartidas ganadas: " + ganadas);
        System.out.println("\tPartidas perdidas: "+ perdidas);
    
        // Mostrar información de las partidas
        System.out.println("\tPartidas jugadas:");
        for (Partida p : partidas) {
            System.out.println("\t\tNivel: " + p.getNivel() + ", Resultado: " + p.getResultado());
        }
    }
    
    @Override
    public String toString(){
        String aux = "Jugador " + nombre + "\tPuntos: " + puntos + "\tVictorias: " + ganadas;
        return aux;
    }
   
    @Override
    public boolean equals(Object o){
       if(this==o)
           return true;
       if(o==null)
           return false;
       if(getClass()!=o.getClass())
           return false;
       Jugador j=(Jugador)o;
       return j.getNombre().equals(getNombre());
    }
}
