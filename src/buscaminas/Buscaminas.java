package buscaminas;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * @author Adriana Hinojar 
 * @author Ainara Vanesa Tusan 
 * @author Maria Garcia
 * 
 */

public class Buscaminas{
    private static final int NOEXISTE = -1;
    private static final String BINARIO = "Buscaminas.dat";
   
    private ArrayList<Jugador> jugadores;
    private ArrayList<Partida> partidasTotales;
    
    public Buscaminas(){
        jugadores = new ArrayList<>();
        partidasTotales = new ArrayList<>();
        
    }
    
    public void agregarJugador(Jugador jugador){
        if(!jugadores.contains(jugador))
            jugadores.add(jugador); 
    }
   
    public void anadirPartida(Partida partida){
        partidasTotales.add(partida); 
    }
    
    public boolean borraJugador(String n){
        boolean jugadorEliminado = false;
        
        // Elimina jugador de "jugadores"
        int pos = 0;
        while (pos < jugadores.size()){
            if(jugadores.get(pos).getNombre().equals(n)){
                jugadores.remove(pos);
                jugadorEliminado = true;
                break;
            }
            pos++;
        }
        
        // Elimina las partidas jugadas por el jugador eliminado
        int posP = 0;
        while (posP < partidasTotales.size()){
            if(partidasTotales.get(posP).getJugador().getNombre().equals(n)){
                partidasTotales.remove(posP);
            } else {
                posP++;
            }
        }
        return jugadorEliminado;
    }
    
    public ArrayList<Jugador> getJugadores(){
        return jugadores;
    }
    
    public int getIndex(Jugador jug){
        return jugadores.indexOf(jug);
    }
    
    public void iniciarPartida(Jugador jugador, Nivel nivel){
        Partida partida = new Partida(jugador, nivel);
        partida.inicializarTablero(nivel);
        partida.jugarPartida();

        actualizarPartida(partida, jugador);
    }
    
    public void actualizarPartida(Partida partida, Jugador jugador){
        if(partida.getResultado() != Resultado.ABANDONO){
            jugador.anyadirPartida(partida);
            anadirPartida(partida);
        }
    }
     public void guardarEnArchivoTextoRanking(){
        LocalDateTime fecha=LocalDateTime.now();
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd.MM.yy-hh-mm");
        String nombreArchivo="Ranking."+fecha.format(f)+".txt";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))){
                
            int orden = 1;
            for(Jugador j : jugadores){
                writer.write(orden + ". " + j.toString());
                writer.newLine();
                orden++;
            }
        }  catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void guardarEnArchivoTextoListado(){
        LocalDateTime fecha=LocalDateTime.now();
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd.MM.yy-hh-mm");
        String nombreArchivo="Listado."+fecha.format(f)+".txt";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))){
            int ord = 1;
            for(Jugador j : jugadores){
                writer.write(ord + ". " + j.toString());
                writer.newLine();
                ord++;
            }
        }  catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
     
    public void guardarEnArchivoBinario() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BINARIO))){
            oos.writeObject(jugadores);
            oos.writeObject(partidasTotales);
            oos.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void cargarArchivoBinario()  {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BINARIO))){
              jugadores = (ArrayList<Jugador>) ois.readObject();
              partidasTotales = (ArrayList<Partida>) ois.readObject();  
              ois.close();
        } catch (FileNotFoundException e){
            guardarEnArchivoBinario();
        } catch (ClassNotFoundException | IOException e){
            System.err.println(e.getMessage());
        }
    }
    
    public int existeJugador(String nombre){
        int i = 0;
        while (i<jugadores.size()){
            if(jugadores.get(i).getNombre().equals(nombre)) 
                return i;
            i++;
        }
        return -1;
    }
    
    public ArrayList<Jugador> obtenerJugadores() {
        return jugadores;
    }

    public ArrayList<Partida> obtenerPartidas() {
        return partidasTotales;
    }
    
    public void ordenarPorPuntuacion(){
        Collections.sort(jugadores, Comparator.comparing(Jugador::getPuntos).reversed());
    }
    
    public void ordenarPorNombre(){
        Collections.sort(jugadores, Comparator.comparing(Jugador::getNombre));
    }
    
    public void ordenarPorGanadas(){
        Collections.sort(jugadores, Comparator.comparing(Jugador::getGanadas).reversed());
    }
    
    public void mostrarJugadores(){
        for(Jugador j: jugadores){
            System.out.println("Información jugador " + j.getNombre() + ":");
            System.out.println("\tPuntos totales: " + j.getPuntos());
            System.out.println("\tPartidas ganadas: " + j.getGanadas());
            System.out.println("\tPartidas perdidas: "+ j.getPerdidas());
    
            // Mostrar información de las partidas
            System.out.println("\tPartidas jugadas:" + j.getPartidas().size());
            for (Partida p : j.getPartidas()) {
                System.out.println("\t\tNivel: " + p.getNivel() + ", Resultado: " + p.getResultado());
            }
        }
    }
    
    public void menuAdministrador(){
        // Menu administrador
        System.out.println("***********************************");
        System.out.println("\tMENÚ ADMINISTRADOR");
        System.out.println("***********************************");
        System.out.println("\t1. Iniciar Sesion.");
        System.out.println("\t2. Dar de alta a un jugador.");
        System.out.println("\t3. Dar de baja a un jugador.");
        System.out.println("\t4. Obtener información de las partidas.");
        System.out.println("\t5. Obtener Ranking.");
        System.out.println("\t6. Obtener Listado.");
        System.out.println("\t0. Salir.");
        System.out.println("Elija una opcion: ");
        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();
        
        // Variables auxiliares que luego se usan en varios puntos del switch
        String nombre;
        int pos;
        Jugador jugador;
        
        switch(opcion){
            case 1:
                System.out.println("Introduzca su nombre: ");
                nombre = scanner.next();
                pos = existeJugador(nombre);
                if( pos == NOEXISTE){
                    System.out.println("Quiere añadir foto?");
                    System.out.println("\t1. Si.");
                    System.out.println("\t2. No.");
                    int op = scanner.nextInt();
                    switch(op){
                        case 1:
                            System.out.println("Introduce la foto.");
                            String foto = scanner.next();
                            jugador = new Jugador(nombre, foto);  
                            agregarJugador(jugador);
                            menuJugador(jugador);
                            break;
                        case 2: 
                            jugador = new Jugador(nombre);
                            agregarJugador(jugador);
                            menuJugador(jugador);
                            break;
                        default:
                            System.out.println("Opcion no valida. Vuelva a elegir");
                            menuAdministrador();
                    }
                } else {
                    menuJugador(jugadores.get(pos));
                }
                break;
            case 2: 
                System.out.println("Introduzca el nombre del jugador que quieres dar de alta: ");
                nombre = scanner.next();
                pos = existeJugador(nombre);
                if(pos == NOEXISTE){
                    System.out.println("Quiere añadir foto?");
                    System.out.println("\t1. Si.");
                    System.out.println("\t2. No.");
                    int op = scanner.nextInt();
                    switch(op){
                        case 1:
                            System.out.println("Introduce la foto.");
                            String foto = scanner.next();
                            jugador = new Jugador(nombre, foto);  
                            agregarJugador(jugador);
                            menuAdministrador();  
                            break;
                        case 2:
                            jugador = new Jugador(nombre);
                            agregarJugador(jugador);
                            menuAdministrador();
                            break;
                        default:
                            System.out.println("Opcion no valida. Vuelva a elegir");
                            menuAdministrador();
                    }
                } else {
                    System.out.println("** Ya existía el jugador " + nombre + " ** ");
                    System.out.println("** Los jugadores que están dados de alta en el buscaminas son ** ");
                    mostrarJugadores();
                    menuAdministrador();
                }
                break;
            case 3:
                System.out.println("Introduzca el nombre del jugador que quieres dar de baja: ");
                nombre = scanner.next();
                if (borraJugador(nombre)){
                    System.out.println("Se ha borrado correctamente");
                    break;
                }
                System.out.println("** Los jugadores que están dados de alta en el buscaminas son ** ");
                mostrarJugadores();
                menuAdministrador();
                break;
            case 4:
                System.out.println("Partidas Jugadas: ");
                int i = 1;
                for(Partida p : partidasTotales){
                    System.out.println("Partida " + i + ":");
                    System.out.println("\t" + p.toString());
                    i++;
                }
                menuAdministrador();
                break;
            case 5:
                ordenarPorPuntuacion();
                System.out.println("Ranking obtenido.");
                int num = 1;
                for(Jugador j: jugadores){
                    System.out.println(num + " " + j.toString());
                    num++;
                }
                guardarEnArchivoTextoRanking();
                menuAdministrador();
                break;
            case 6: 
                ordenarPorNombre();
                System.out.println("Listado obtenido.");
                int n = 1;
                for(Jugador j: jugadores){
                    System.out.println(n + " " + j.toString());
                    n++;
                }
                guardarEnArchivoTextoListado();
                menuAdministrador();
                break;
            case 0:
                System.exit(0);
                break;
            default: 
                
        }
    }
    
    public void menuJugador(Jugador jugador){
        System.out.println("***********************************");
        System.out.println("\tMENÚ JUGADOR");
        System.out.println("***********************************");
        System.out.println("\t1. Iniciar Partida.");
        System.out.println("\t2. Consultar Mi Perfil.");
        System.out.println("\t3. Consultar clasificacion.");
        System.out.println("\t0. Salir. Volver a menu administrador.");
        System.out.println("Elija una opcion: ");
        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();
        
        switch(opcion){
            case 1:
                System.out.println("\t1. Principiante. ");
                System.out.println("\t2. Intermedio. ");
                System.out.println("\t3. Experto. ");
                System.out.println("\t4. Personalizado. ");
                System.out.println("Elija el nivel: ");
                int nivel = scanner.nextInt();
                switch(nivel){
                    case 1:
                        iniciarPartida(jugador, Nivel.PRINCIPIANTE);
                        break;
                    case 2:
                        iniciarPartida(jugador, Nivel.INTERMEDIO);
                        break;
                    case 3: 
                        iniciarPartida(jugador, Nivel.EXPERTO);
                        break;
                    case 4: 
                        iniciarPartida(jugador, Nivel.PERSONALIZADO);
                        break;
                    default: 
                        System.out.println("Opcion no válida.");
                        menuJugador(jugador);
                }
                guardarEnArchivoBinario();
                jugador.mostrarJugador();
                menuJugador(jugador);
                
                break;
            case 2: 
                jugador.mostrarJugador();
                menuJugador(jugador);
                break;
            case 3:
                System.out.println("\t1. Ordenar por Puntuación. ");
                System.out.println("\t2. Ordenar por Nombre. ");
                System.out.println("\t3. Ordenar por numero de victorias. ");
                System.out.println("¿Como quiere ordenar la clasificacion?: ");
                int orden = scanner.nextInt();
                switch(orden){
                    case 1:
                        ordenarPorPuntuacion();
                        System.out.println("Clasificación: ");
                        int num = 1;
                        for(Jugador j: jugadores){
                            System.out.println(num + " " + j.toString());
                            num++;
                        }
                        menuJugador(jugador);
                        break;
                    case 2:
                        ordenarPorNombre();
                        System.out.println("Clasificación: ");
                         num = 1;
                        for(Jugador j: jugadores){
                            System.out.println(num + " " + j.toString());
                            num++;
                        }
                        menuJugador(jugador);
                        break;
                    case 3: 
                        ordenarPorGanadas();
                        System.out.println("Clasificación: ");
                        num = 1;
                        for(Jugador j: jugadores){
                            System.out.println(num + " " + j.toString());
                            num++;
                        }
                        menuJugador(jugador);
                        break;
                    default: 
                        System.out.println("Opcion no válida.");
                        menuJugador(jugador);
                }
                ordenarPorPuntuacion();
                System.out.println("Clasificación: ");
                int num = 1;
                for(Jugador j: jugadores){
                    System.out.println(num + " " + j.toString());
                    num++;
                }
                menuJugador(jugador);
                break;
            case 0:
                guardarEnArchivoBinario();
                menuAdministrador();
                break;   
        }
    }
    
    public static void main(String[] args) {
        
        new Interfaz().setVisible(true);
        /* 
        DESCOMENTAR ESTO Y COMENTAR LA FILA DE LA INTERFAZ GRÁFICA EN CASO DE QUERER VER EL JUEGO POR CONSOLA
        Buscaminas juego = new Buscaminas();
        juego.cargarArchivoBinario();
        juego.menuAdministrador();
        juego.guardarEnArchivoBinario();
        */
    }
}
