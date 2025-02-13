/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas;

import java.io.Serializable;
import java.util.Scanner;


public class Partida implements Serializable {
    private boolean vidaGastada;
    private int numCasillasDescubiertas;
    private int puntos;
    private boolean vidaEncontrada;
    private int vidas;
    private Tablero tablero;
    private Jugador jugador;
    private Nivel nivel; 
    private Resultado resultado;
    private int dimXPersonalizado;
    private int dimYPersonalizado;
    private int numMinasPersonalizado;
    
    public Partida(Jugador jugador, Nivel nivel){
        this.jugador = jugador;
        this.nivel = nivel;
        this.vidaGastada = false;
        this.numCasillasDescubiertas = 0;
        this.puntos = 0;
        this.vidaEncontrada = false;
        this.resultado = null;
        this.vidas=0;
        
        //inicializarTablero(nivel);
    }
    
    public Resultado getResultado(){
        return resultado;
    }
    
    public void setResultado(Resultado resultado){
        this.resultado = resultado;
    }
    
    public void setVidaEncontrada(boolean vidaEncontrada){
        this.vidaEncontrada = vidaEncontrada;
    }
    
    public void setCasillasDescubiertas(int numCasillasDescubiertas){
        this.numCasillasDescubiertas = numCasillasDescubiertas;
    }
    
    public Tablero getTablero(){
        return tablero;
    }
    
    public void setTablero(Tablero tablero){
        this.tablero = tablero;
    }
    
    public int getPuntos(){
        return puntos;
    }
    
    public Jugador getJugador(){
        return jugador;
    }
    
    public Nivel getNivel(){
        return nivel;
    }
    
    public void inicializarTablero(Nivel nivel){
       int dimX = 0, dimY = 0, numMinas = 0;

        switch (nivel) {
            case PRINCIPIANTE:
                dimX = 8;
                dimY = 8;
                numMinas = 10;
                break;
            case INTERMEDIO:
                dimX = 16;
                dimY = 16;
                numMinas = 40;
                break;
            case EXPERTO:
                dimX = 16;
                dimY = 30;
                numMinas = 99;
                break;
            case PERSONALIZADO:
                
                Scanner scanner = new Scanner(System.in);
                System.out.println("Introduzca las dimension X: ");
                dimXPersonalizado = scanner.nextInt();
                System.out.println("Introduzca las dimension Y: ");
                dimYPersonalizado = scanner.nextInt();
                System.out.println("Introduzca el numero de minas: ");
                numMinasPersonalizado = scanner.nextInt();
                if(numMinasPersonalizado > dimXPersonalizado*dimYPersonalizado){
                    System.out.println("Las minas no entran en el tablero. Vuelve a introducir: ");
                    numMinasPersonalizado = scanner.nextInt();
                }
                
                dimX = dimXPersonalizado;
                dimY = dimYPersonalizado;
                numMinas = numMinasPersonalizado;
                break;
        }

        tablero = new Tablero(dimX, dimY, numMinas);
        tablero.inicializarTablero(); 
        tablero.colocarMinas();
        tablero.colocarVidas();
    }
    
    public void descubrirCasilla(int x, int y) {
        if (x >= 0 && x < tablero.getDimX() && y >= 0 && y < tablero.getDimY()) {
            Casilla casilla = tablero.getCasilla(x, y);

            if (casilla.estaOculta()) {
                casilla.mostrarCasilla();

                if (casilla instanceof CasillaConMina) {
                    if (vidas > 0) {
                        vidas--;
                        // Mostrar el tablero después de perder una vida
                        tablero.imprimirTablero();
                    } else {
                        vidaGastada = true;
                        resultado = Resultado.PERDIDA;
                    }
                } else if (casilla instanceof CasillaConVida) {
                    vidaEncontrada = true;
                    vidas++;
                    numCasillasDescubiertas++;
                } else if (casilla instanceof CasillaVacia) {
                    numCasillasDescubiertas++;
                    CasillaVacia casillaVacia = (CasillaVacia) casilla;
                    casillaVacia.contarMinasYVidasAlrededor(tablero);

                    if (casillaVacia.getNumMinasAlrededor() == 0) {
                        descubrirCasillasVacias(x, y);
                    }
                }

                verificarJuegoGanado();
            }
        }
    }
    
    // si tocas una casilla vacia descubrir todas las vacias de alrededor
    public void descubrirCasillasVacias(int x, int y) {
        for (int i = Math.max(0, x - 1); i <= Math.min(tablero.getDimX() - 1, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(tablero.getDimY() - 1, y + 1); j++) {
                Casilla casillaActual = tablero.getCasilla(i, j);

                if (casillaActual.estaOculta() && !(casillaActual instanceof CasillaConMina) && !(casillaActual instanceof CasillaConVida)) {
                    casillaActual.setOculta(false);
                    numCasillasDescubiertas++;

                    if (casillaActual instanceof CasillaVacia) {
                        CasillaVacia casillaVacia = (CasillaVacia) casillaActual;
                        casillaVacia.contarMinasYVidasAlrededor(tablero);
                        // muestra las minas adyacentes después 
                        casillaVacia.mostrarCasilla();

                        // si no tiene minas adyacentes descubrir casillas vacías cercanas
                        if (casillaVacia.getNumMinasAlrededor() == 0 && !casillaVacia.getTieneVidasAlrededor()) {
                            descubrirCasillasVacias(i, j);
                        }
                    } else if (casillaActual instanceof CasillaConVida) {
                        vidaEncontrada = true;
                    }
                }
            }
        }
    }

    public void verificarJuegoGanado() {
        int numCasillasTotales = tablero.getDimX() * tablero.getDimY();
        int numCasillasNoMina = numCasillasTotales - tablero.getNumMinas();
        int numCasillasConVida = numCasillasTotales - tablero.getNumMinas() - 1;
        
        if(vidaEncontrada){
            if (numCasillasDescubiertas == numCasillasNoMina) {
                resultado = Resultado.GANADA;
            } 
        } else {
            if (numCasillasDescubiertas == numCasillasConVida) {
                resultado = Resultado.GANADA;
            }
        } 
    }
    
    public int calcularPuntos(){
        int puntosGanados = 0;
        
        switch (nivel) {
            case PRINCIPIANTE:
                if(resultado == Resultado.GANADA){
                    puntosGanados = 3;
                    puntos += 3;
                }
                break;
            case INTERMEDIO:
                if (resultado == Resultado.GANADA) {
                    puntosGanados = 6;
                    puntos += 6;
                }
                break;
            case EXPERTO:
                if (resultado == Resultado.GANADA) {
                    puntosGanados = 10;
                    puntos += 10;
                }
                break;
            case PERSONALIZADO:
                int numBombas = tablero.getNumMinas();
                if (numBombas >= 1 && numBombas <= 10) {
                    puntosGanados = 3;
                    puntos += 3;
                } else if (numBombas >= 11 && numBombas <= 40) {
                    puntosGanados = 6;
                    puntos += 6;
                } else if (numBombas >= 41 && numBombas <= 99) {
                    puntosGanados = 10;
                    puntos += 10;
                } else {
                    puntosGanados = 15;
                    puntos += 15;
                }
                break;
            default:
                System.err.println("Nivel no reconocido: " + nivel);
                break;
        }
        return puntosGanados;
    }
    
    public void abandonarPartida(){
        resultado = Resultado.ABANDONO;
    }


    @Override
    public String toString() {
        String aux = "Jugador: " + jugador.getNombre();
        aux += "\n\tResultado Partida: " + resultado;
        aux += "\n\tPuntos ganados: " + puntos;
        aux += "\n\tNivel: " + nivel;
        int vidasP;
        if(vidaEncontrada){
            vidasP = 1;
        } else {
            vidasP = 0;
        }
        aux += "\n\tVidas descubiertas: " + vidasP;
        
        return aux;
    }
    
    public void jugarPartida(){
        while (true) {
            Scanner scanner = new Scanner(System.in);

            if (getResultado() == null) {
                System.out.println("Tablero:");
                getTablero().imprimirTablero();
                System.out.println("Si quiere abandonar la partida pulse 0.");
                System.out.print("Ingrese la fila: ");
                int fila = scanner.nextInt();

                System.out.print("Ingrese la columna: ");
                int columna = scanner.nextInt();

                if((fila | columna) == 0){
                    abandonarPartida();
                }

                descubrirCasilla(fila-1, columna-1);

                if (getResultado() != null) {
                    System.out.println("Resultado de la partida: " + getResultado());
                    break;
                }
            } else {
                System.out.println("Resultado de la partida: " + getResultado());
                break;
            }
        }
    }
}
   
