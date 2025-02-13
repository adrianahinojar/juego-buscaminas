/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buscaminas;

import static buscaminas.Nivel.EXPERTO;
import static buscaminas.Nivel.INTERMEDIO;
import static buscaminas.Nivel.PERSONALIZADO;
import static buscaminas.Nivel.PRINCIPIANTE;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Interfaz extends javax.swing.JFrame {
    private Partida partida;
    private Buscaminas buscaminas;
    private Jugador jugador;
    private Container contenedor;
    private JFrame tableroFrame;
    private GridLayout layout;
    private JButton[][] botones;
    private int[][] minas;
    private int[][] vidas;
    private int numCasillasDescubiertas;
    private boolean vidaEncontrada;
    private boolean vidaUsada;
    private int numMinas;
    private int dimX;
    private int dimY;
    private int height;
    private int width;
    private JMenuBar menuBar;
    private JButton abandonar;
    private JLabel ayuda;
            /**
     * Creates new form Interfaz
     */
    public Interfaz() {
        initComponents();
        this.buscaminas = new Buscaminas();
        buscaminas.cargarArchivoBinario();
        
    }
    
    private void mostrarTablero(Nivel nivel) {
        
        OpcionesNivel.show(false);
        tableroFrame = new JFrame("Tablero");
        
        menuBar = new JMenuBar();
        
        abandonar = new JButton();
        ayuda = new JLabel();
        
        abandonar.setText("Abandonar");
        ayuda.setText("     Ayuda");
        ayuda.setToolTipText("M -> mina    V -> vida cerca    :) -> vida    1V -> Una mina y vida cerca");
        
        menuBar.add(abandonar);
        menuBar.add(ayuda);
        
        tableroFrame.setJMenuBar(menuBar);
        
        abandonar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(tableroFrame, "Partida abandonada.");
                tableroFrame.setVisible(false);
                MenuInicioSesion.setVisible(true);
            }
            
        });
        
        
        partida = new Partida(jugador, nivel);
        switch (nivel) {
            case PRINCIPIANTE:
                dimX = 8;
                dimY = 8;
                numMinas = 10;
                height = 557;
                width = 693;
                break;
            case INTERMEDIO:
                dimX = 16;
                dimY = 16;
                numMinas = 40;
                height = 800;
                width = 800;
                break;
            case EXPERTO:
                dimX = 16;
                dimY = 30;
                numMinas = 99;
                height = 789;
                width = 1277;
                break;
            case PERSONALIZADO:
                height = 557;
                width = 693;
                break;
        }

        Tablero tablero = new Tablero(dimX, dimY, numMinas);
        partida.setTablero(tablero);
        layout = new GridLayout(dimX, dimY);
        contenedor = tableroFrame.getContentPane();
        contenedor.setLayout(layout);
        botones = new JButton[dimX][dimY];
        minas = new int[dimX][dimY];
        vidas = new int[dimX][dimY];
        numCasillasDescubiertas = 0;
        vidaEncontrada = false;
        vidaUsada = false;
        

        tableroFrame.setSize(width, height);

        inicializarMinasYVidas(numMinas);

        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                botones[i][j] = new JButton(" ");
                botones[i][j].addActionListener(new BotonClickListener(i, j));
                contenedor.add(botones[i][j]);
            }
        }
        MenuInicioSesion.setVisible(false);
        tableroFrame.setVisible(true);
        this.setVisible(false);
    }

    private void inicializarMinasYVidas(int numMinas) {
        colocarElementosAleatorios(minas, numMinas);
        colocarElementosAleatorios(vidas, 1);
    }

    private void colocarElementosAleatorios(int[][] matriz, int cantidad) {
        int filas = matriz.length;
        int columnas = matriz[0].length;

        for (int i = 0; i < cantidad; i++) {
            int fila, columna;
            do {
                fila = (int) (Math.random() * filas);
                columna = (int) (Math.random() * columnas);
            } while (minas[fila][columna] == 1 || vidas[fila][columna] == 1); 
            matriz[fila][columna] = 1;
        }
    }

    private class BotonClickListener implements ActionListener {
        private int fila;
        private int columna;

        public BotonClickListener(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (minas[fila][columna] == 1) {
                if (vidaEncontrada && !vidaUsada && numCasillasDescubiertas > 0) {
                    vidaUsada = true;
                    JOptionPane.showMessageDialog(Interfaz.this, "¡Has perdido una vida!");
                } else {
                    botones[fila][columna].setForeground(Color.RED); // Establecer el color rojo para las minas
                    botones[fila][columna].setText("M");
                    botones[fila][columna].setEnabled(false);
                    JOptionPane.showMessageDialog(Interfaz.this, "¡Has tocado una mina!");
                    partida.setResultado(Resultado.PERDIDA);
                    vidas[fila][columna] = 0;
                    buscaminas.actualizarPartida(partida, jugador);
                    tableroFrame.setVisible(false);
                    MenuInicioSesion.setVisible(true);
                }
                

            } else if (vidas[fila][columna] == 1) {
                JOptionPane.showMessageDialog(Interfaz.this, "¡Has encontrado una vida!");
                vidas[fila][columna] = 0;  // Vida Encontrada
                vidaEncontrada = true;
                partida.setVidaEncontrada(vidaEncontrada);
                botones[fila][columna].setForeground(Color.GREEN); // Establecer el color verde para las vidas
                botones[fila][columna].setText(":)");
                botones[fila][columna].setEnabled(false);
                
            } else {
                descubrirCasillasVacias(fila, columna);
            }
            verificarJuegoGanado();
        }
        
        private void descubrirCasillasVacias(int x, int y) {
            if (x >= 0 && x < dimX && y >= 0 && y < dimY && botones[x][y].isEnabled()) {
                int minasAdyacentes = contarMinasAdyacentes(x, y);
                boolean vidasAdyacentes = contarVidasAdyacentes(x,y);

                if(vidasAdyacentes){
                   botones[x][y].setForeground(Color.MAGENTA);
                   botones[x][y].setText(String.valueOf(minasAdyacentes + "V"));
                   
                } else {
                    botones[x][y].setText(String.valueOf(minasAdyacentes));
                    //System.out.println("Color antes: " + botones[x][y].getForeground());
                    switch (minasAdyacentes) {
                        case 1:
                            botones[x][y].setForeground(Color.BLUE); // Establecer el color azul para el número 1
                            break;
                        case 2:
                            botones[x][y].setForeground(Color.YELLOW); // Establecer el color amarillo para el número 2
                            break;
                        case 3:
                            botones[x][y].setForeground(Color.ORANGE); // Establecer el color naranja para el número 3
                             break;
                     } 
                    //System.out.println("Color después: " + botones[x][y].getForeground());
                    //botones[x][y].setText(String.valueOf(minasAdyacentes));
                }
                
                botones[x][y].setEnabled(false);
                numCasillasDescubiertas++;
                partida.setCasillasDescubiertas(numCasillasDescubiertas);

                if (minasAdyacentes == 0 && !vidasAdyacentes) {
                    for (int i = Math.max(0, x - 1); i <= Math.min(dimX - 1, x + 1); i++) {
                        for (int j = Math.max(0, y - 1); j <= Math.min(dimY - 1, y + 1); j++) {
                            descubrirCasillasVacias(i, j);
                        }
                    }
                }
                
            }
        }
        
        private void verificarJuegoGanado() {
            int casillasTotales = dimX * dimY;
            int casillasSinMinas = casillasTotales - numMinas;

            if (numCasillasDescubiertas  == casillasSinMinas || numCasillasDescubiertas  == casillasSinMinas-1) {
                JOptionPane.showMessageDialog(Interfaz.this, "¡Has ganado!");
                partida.setResultado(Resultado.GANADA);
                buscaminas.actualizarPartida(partida, jugador);
                tableroFrame.setVisible(false);
                MenuInicioSesion.setVisible(true);
            }
        }
        
        private int contarMinasAdyacentes(int fila, int columna) {
            int minasAdyacentes = 0;
            
            for (int i = fila - 1; i <= fila + 1; i++) {
                for (int j = columna - 1; j <= columna + 1; j++) {
                    if (i >= 0 && i < dimX && j >= 0 && j < dimY) {
                        if (minas[i][j] == 1) {
                            minasAdyacentes++;
                        }
                    }
                }
            }
            
            return minasAdyacentes;
        }
        
        private boolean contarVidasAdyacentes(int fila, int columna) {
            boolean vidasAdyacentes = false;
            
            for (int i = fila - 1; i <= fila + 1; i++) {
                for (int j = columna - 1; j <= columna + 1; j++) {
                    if (i >= 0 && i < dimX && j >= 0 && j < dimY) {
                        if (vidas[i][j] == 1) {
                            vidasAdyacentes = true;
                        }
                    }
                }
            }
            
            return vidasAdyacentes;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MenuInicioSesion = new javax.swing.JFrame();
        Titulo1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        IniciarPartida = new javax.swing.JButton();
        MiPerfil = new javax.swing.JButton();
        Calificacion = new javax.swing.JButton();
        Volver = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        OpcionesCalificacion = new javax.swing.JDialog();
        PorPuntuacion = new javax.swing.JButton();
        PorVictorias = new javax.swing.JButton();
        PorAlfabeto = new javax.swing.JButton();
        Tablero = new javax.swing.JFrame();
        jMenuBar4 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        OpcionesNivel = new javax.swing.JDialog();
        Principiante1 = new javax.swing.JButton();
        Intermedio1 = new javax.swing.JButton();
        Experto1 = new javax.swing.JButton();
        Personalizado1 = new javax.swing.JButton();
        Personalizado = new javax.swing.JDialog();
        Filas = new javax.swing.JLabel();
        numFilas = new javax.swing.JTextField();
        Columnas = new javax.swing.JLabel();
        numColumnas = new javax.swing.JTextField();
        Minas = new javax.swing.JLabel();
        minasP = new javax.swing.JTextField();
        Jugar = new javax.swing.JButton();
        MenuAdministrador = new javax.swing.JPanel();
        AltaBaja = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        IniciarSesion = new javax.swing.JButton();
        Listado = new javax.swing.JButton();
        InfoPartidas = new javax.swing.JButton();
        Ranking = new javax.swing.JButton();
        Titulo = new javax.swing.JLabel();
        Salir = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        MenuInicioSesion.setLocationByPlatform(true);
        MenuInicioSesion.setMinimumSize(new java.awt.Dimension(420, 367));

        Titulo1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Titulo1.setText("  MENÚ JUGADOR");
        Titulo1.setAlignmentX(0.5F);

        jLabel3.setText("Seleccione una opción:");

        IniciarPartida.setBackground(new java.awt.Color(255, 255, 255));
        IniciarPartida.setForeground(new java.awt.Color(0, 0, 0));
        IniciarPartida.setText("Iniciar Partida");
        IniciarPartida.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        IniciarPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IniciarPartidaActionPerformed(evt);
            }
        });

        MiPerfil.setBackground(new java.awt.Color(255, 255, 255));
        MiPerfil.setForeground(new java.awt.Color(0, 0, 0));
        MiPerfil.setText("Mi Perfil");
        MiPerfil.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        MiPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MiPerfilActionPerformed(evt);
            }
        });

        Calificacion.setBackground(new java.awt.Color(255, 255, 255));
        Calificacion.setForeground(new java.awt.Color(0, 0, 0));
        Calificacion.setText("Calificacion");
        Calificacion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Calificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalificacionActionPerformed(evt);
            }
        });

        Volver.setBackground(new java.awt.Color(255, 255, 255));
        Volver.setForeground(new java.awt.Color(0, 0, 0));
        Volver.setText("Salir");
        Volver.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Volver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VolverActionPerformed(evt);
            }
        });

        jMenu2.setText("Home");
        jMenuBar2.add(jMenu2);

        MenuInicioSesion.setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout MenuInicioSesionLayout = new javax.swing.GroupLayout(MenuInicioSesion.getContentPane());
        MenuInicioSesion.getContentPane().setLayout(MenuInicioSesionLayout);
        MenuInicioSesionLayout.setHorizontalGroup(
            MenuInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuInicioSesionLayout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addGroup(MenuInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuInicioSesionLayout.createSequentialGroup()
                        .addComponent(Titulo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(157, 157, 157))
                    .addGroup(MenuInicioSesionLayout.createSequentialGroup()
                        .addGroup(MenuInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(MenuInicioSesionLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(MenuInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(IniciarPartida, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                    .addComponent(MiPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(MenuInicioSesionLayout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addGroup(MenuInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Calificacion, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addComponent(Volver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        MenuInicioSesionLayout.setVerticalGroup(
            MenuInicioSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuInicioSesionLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(Titulo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(30, 30, 30)
                .addComponent(IniciarPartida)
                .addGap(18, 18, 18)
                .addComponent(MiPerfil)
                .addGap(18, 18, 18)
                .addComponent(Calificacion)
                .addGap(18, 18, 18)
                .addComponent(Volver)
                .addGap(111, 111, 111))
        );

        OpcionesCalificacion.setMinimumSize(new java.awt.Dimension(400, 215));

        PorPuntuacion.setText("Ordenada por Puntuación");
        PorPuntuacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PorPuntuacionActionPerformed(evt);
            }
        });

        PorVictorias.setText("Ordenada por Victorias");
        PorVictorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PorVictoriasActionPerformed(evt);
            }
        });

        PorAlfabeto.setText("Ordenada Alfabeticamente");
        PorAlfabeto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PorAlfabetoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout OpcionesCalificacionLayout = new javax.swing.GroupLayout(OpcionesCalificacion.getContentPane());
        OpcionesCalificacion.getContentPane().setLayout(OpcionesCalificacionLayout);
        OpcionesCalificacionLayout.setHorizontalGroup(
            OpcionesCalificacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PorPuntuacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PorVictorias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PorAlfabeto, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        OpcionesCalificacionLayout.setVerticalGroup(
            OpcionesCalificacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OpcionesCalificacionLayout.createSequentialGroup()
                .addComponent(PorPuntuacion, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PorVictorias, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PorAlfabeto, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenu3.setText("File");
        jMenuBar4.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar4.add(jMenu4);

        Tablero.setJMenuBar(jMenuBar4);

        javax.swing.GroupLayout TableroLayout = new javax.swing.GroupLayout(Tablero.getContentPane());
        Tablero.getContentPane().setLayout(TableroLayout);
        TableroLayout.setHorizontalGroup(
            TableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        TableroLayout.setVerticalGroup(
            TableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        OpcionesNivel.setMinimumSize(new java.awt.Dimension(400, 285));
        OpcionesNivel.setModal(true);

        Principiante1.setText("PRINCIPIANTE");
        Principiante1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Principiante1ActionPerformed(evt);
            }
        });

        Intermedio1.setText("INTERMEDIO");
        Intermedio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Intermedio1ActionPerformed(evt);
            }
        });

        Experto1.setText("EXPERTO");
        Experto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Experto1ActionPerformed(evt);
            }
        });

        Personalizado1.setText("PERSONALIZADO");
        Personalizado1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Personalizado1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout OpcionesNivelLayout = new javax.swing.GroupLayout(OpcionesNivel.getContentPane());
        OpcionesNivel.getContentPane().setLayout(OpcionesNivelLayout);
        OpcionesNivelLayout.setHorizontalGroup(
            OpcionesNivelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Principiante1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Intermedio1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Experto1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(Personalizado1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        OpcionesNivelLayout.setVerticalGroup(
            OpcionesNivelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OpcionesNivelLayout.createSequentialGroup()
                .addComponent(Principiante1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Intermedio1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Experto1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Personalizado1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Personalizado.setMinimumSize(new java.awt.Dimension(353, 250));
        Personalizado.setModal(true);

        Filas.setText("Introduzca el numero de filas:");

        numFilas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numFilasActionPerformed(evt);
            }
        });

        Columnas.setText("Introduzca el numero de columnas:");

        numColumnas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numColumnasActionPerformed(evt);
            }
        });

        Minas.setText("Introduzca el numero de minas:");

        minasP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minasPActionPerformed(evt);
            }
        });

        Jugar.setText("Jugar");
        Jugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JugarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PersonalizadoLayout = new javax.swing.GroupLayout(Personalizado.getContentPane());
        Personalizado.getContentPane().setLayout(PersonalizadoLayout);
        PersonalizadoLayout.setHorizontalGroup(
            PersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PersonalizadoLayout.createSequentialGroup()
                .addGroup(PersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PersonalizadoLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(PersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PersonalizadoLayout.createSequentialGroup()
                                .addComponent(Filas)
                                .addGap(56, 56, 56)
                                .addComponent(numFilas, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PersonalizadoLayout.createSequentialGroup()
                                .addComponent(Columnas)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(numColumnas, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PersonalizadoLayout.createSequentialGroup()
                                .addComponent(Minas)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(minasP, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(PersonalizadoLayout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(Jugar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        PersonalizadoLayout.setVerticalGroup(
            PersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PersonalizadoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(PersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Filas)
                    .addComponent(numFilas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(PersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Columnas)
                    .addComponent(numColumnas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(PersonalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Minas)
                    .addComponent(minasP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Jugar)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        AltaBaja.setBackground(new java.awt.Color(255, 255, 255));
        AltaBaja.setForeground(new java.awt.Color(0, 0, 0));
        AltaBaja.setText("Dar Alta/Baja");
        AltaBaja.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AltaBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AltaBajaActionPerformed(evt);
            }
        });

        jLabel1.setText("Seleccione una opción:");

        IniciarSesion.setBackground(new java.awt.Color(255, 255, 255));
        IniciarSesion.setForeground(new java.awt.Color(0, 0, 0));
        IniciarSesion.setText("Iniciar Sesión");
        IniciarSesion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        IniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IniciarSesionActionPerformed(evt);
            }
        });

        Listado.setBackground(new java.awt.Color(255, 255, 255));
        Listado.setForeground(new java.awt.Color(0, 0, 0));
        Listado.setText("Obtener Listado");
        Listado.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Listado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ListadoActionPerformed(evt);
            }
        });

        InfoPartidas.setBackground(new java.awt.Color(255, 255, 255));
        InfoPartidas.setForeground(new java.awt.Color(0, 0, 0));
        InfoPartidas.setText("Información Partidas");
        InfoPartidas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        InfoPartidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InfoPartidasActionPerformed(evt);
            }
        });

        Ranking.setBackground(new java.awt.Color(255, 255, 255));
        Ranking.setForeground(new java.awt.Color(0, 0, 0));
        Ranking.setText("Obtener Ranking");
        Ranking.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Ranking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RankingActionPerformed(evt);
            }
        });

        Titulo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Titulo.setText("  MENÚ PRINCIPAL");
        Titulo.setAlignmentX(0.5F);

        Salir.setBackground(new java.awt.Color(255, 255, 255));
        Salir.setForeground(new java.awt.Color(0, 0, 0));
        Salir.setText("Salir");
        Salir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuAdministradorLayout = new javax.swing.GroupLayout(MenuAdministrador);
        MenuAdministrador.setLayout(MenuAdministradorLayout);
        MenuAdministradorLayout.setHorizontalGroup(
            MenuAdministradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuAdministradorLayout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addGroup(MenuAdministradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(IniciarSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AltaBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(InfoPartidas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Ranking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Listado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Salir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(140, Short.MAX_VALUE))
        );
        MenuAdministradorLayout.setVerticalGroup(
            MenuAdministradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuAdministradorLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(Titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(IniciarSesion)
                .addGap(18, 18, 18)
                .addComponent(AltaBaja)
                .addGap(18, 18, 18)
                .addComponent(InfoPartidas)
                .addGap(18, 18, 18)
                .addComponent(Ranking)
                .addGap(18, 18, 18)
                .addComponent(Listado)
                .addGap(18, 18, 18)
                .addComponent(Salir)
                .addGap(52, 52, 52))
        );

        jMenu1.setText("Home");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(MenuAdministrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(MenuAdministrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void IniciarPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IniciarPartidaActionPerformed
        // TODO add your handling code here:
        OpcionesNivel.show(true);
        this.setVisible(false);
        
        
    }//GEN-LAST:event_IniciarPartidaActionPerformed

    private void MiPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MiPerfilActionPerformed
        // TODO add your handling code here:
        ArrayList<Partida> partidas = jugador.getPartidas();

        // Crear un panel para mostrar la información del jugador, incluida la foto
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Obtener la información del jugador
        StringBuilder infoJugador = new StringBuilder("");
        infoJugador.append("Puntos totales: ").append(jugador.getPuntos()).append("\n");
        infoJugador.append("Partidas ganadas: ").append(jugador.getGanadas()).append("\n");
        infoJugador.append("Partidas perdidas: ").append(jugador.getPerdidas()).append("\n");
        infoJugador.append("Partidas jugadas:\n");

        for (int i = 0; i < partidas.size(); i++) {
            Partida p = partidas.get(i);
            infoJugador.append("\tPartida ").append(i + 1).append(":\n");
            infoJugador.append("\t\tNivel: ").append(p.getNivel()).append(", Resultado: ").append(p.getResultado()).append("\n");
        }

        JTextArea textArea = new JTextArea(infoJugador.toString());
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        // Verificar si el jugador tiene una foto y mostrarla en un JLabel con un tamaño reducido
        String fotoPath = jugador.getFoto();
        if (fotoPath != null && !fotoPath.isEmpty()) {
            ImageIcon imagen = new ImageIcon(new ImageIcon(fotoPath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            JLabel labelFoto = new JLabel(imagen);
            panel.add(labelFoto, BorderLayout.WEST);
        }

        panel.add(scrollPane, BorderLayout.CENTER);

        // Mostrar el panel en un cuadro de diálogo
        JOptionPane.showMessageDialog(
            this,
            panel,
            "Perfil Jugador",
            JOptionPane.PLAIN_MESSAGE
        );
    }//GEN-LAST:event_MiPerfilActionPerformed

    private void CalificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CalificacionActionPerformed
        // TODO add your handling code here:
        OpcionesCalificacion.show(true);
        
    }//GEN-LAST:event_CalificacionActionPerformed

    private void VolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VolverActionPerformed
        // TODO add your handling code here:
        MenuInicioSesion.setVisible(false);
        this.setVisible(true);
    }//GEN-LAST:event_VolverActionPerformed

    private void PorVictoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PorVictoriasActionPerformed
        // TODO add your handling code here:
        
        OpcionesCalificacion.show(false);
        ArrayList<Jugador> jugadores = buscaminas.obtenerJugadores();
        buscaminas.ordenarPorGanadas();

        // mensaje con información de las partidas
        StringBuilder calificacionVictorias = new StringBuilder("Calificacion Ordenada por Victorias:\n\n");

        int i = 1;
        for (Jugador j : jugadores) {
            calificacionVictorias.append(i).append(". ").append(j.toString()).append("\n");
            i++;
        }
        JTextArea textArea = new JTextArea(calificacionVictorias.toString());
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Calificacion Ordenada por Victorias",
                JOptionPane.PLAIN_MESSAGE
        );
    }//GEN-LAST:event_PorVictoriasActionPerformed

    private void PorPuntuacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PorPuntuacionActionPerformed
        // TODO add your handling code here:
        OpcionesCalificacion.show(false);
         ArrayList<Jugador> jugadores = buscaminas.obtenerJugadores();
        buscaminas.ordenarPorPuntuacion();

        // mensaje con información de las partidas
        StringBuilder calificacionPuntos = new StringBuilder("Calificacion Ordenada por Puntos:\n\n");

        int i = 1;
        for (Jugador j : jugadores) {
            calificacionPuntos.append(i).append(". ").append(j.toString()).append("\n");
            i++;
        }
        JTextArea textArea = new JTextArea(calificacionPuntos.toString());
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Calificacion Ordenada por Puntos",
                JOptionPane.PLAIN_MESSAGE
        );
    }//GEN-LAST:event_PorPuntuacionActionPerformed

    private void PorAlfabetoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PorAlfabetoActionPerformed
        // TODO add your handling code here:
        OpcionesCalificacion.show(false);
         ArrayList<Jugador> jugadores = buscaminas.obtenerJugadores();
        buscaminas.ordenarPorNombre();

        StringBuilder calificacionNombre = new StringBuilder("Calificacion Ordenada por Nombre:\n\n");

        int i = 1;
        for (Jugador j : jugadores) {
            calificacionNombre.append(i).append(". ").append(j.toString()).append("\n");
            i++;
        }
        JTextArea textArea = new JTextArea(calificacionNombre.toString());
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Calificacion Ordenada por Nombre",
                JOptionPane.PLAIN_MESSAGE
        );
    }//GEN-LAST:event_PorAlfabetoActionPerformed

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        // TODO add your handling code here:
        buscaminas.guardarEnArchivoBinario();
        System.exit(0); // Salir de la aplicación
    }//GEN-LAST:event_SalirActionPerformed

    private void ListadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ListadoActionPerformed
        // TODO add your handling code here:
        buscaminas.ordenarPorNombre();

        JOptionPane.showMessageDialog(this, "Listado Obtenido", "Listado", JOptionPane.INFORMATION_MESSAGE);

        buscaminas.guardarEnArchivoTextoListado();
    }//GEN-LAST:event_ListadoActionPerformed

    private void RankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RankingActionPerformed
        // TODO add your handling code here:
        buscaminas.ordenarPorPuntuacion();

        JOptionPane.showMessageDialog(this, "Ranking obtenido.", "Ranking", JOptionPane.INFORMATION_MESSAGE);

        buscaminas.guardarEnArchivoTextoRanking();
    }//GEN-LAST:event_RankingActionPerformed

    private void InfoPartidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InfoPartidasActionPerformed
        // TODO add your handling code here:
        ArrayList<Partida> partidas = buscaminas.obtenerPartidas();

        // mensaje con información de las partidas
        StringBuilder infoPartidas = new StringBuilder("Partidas Jugadas:\n\n");

        int i = 1;
        for (Partida p : partidas) {
            infoPartidas.append("Partida ").append(i).append(":\n");
            infoPartidas.append("\t").append(p.toString()).append("\n\n");
            i++;
        }
        JTextArea textArea = new JTextArea(infoPartidas.toString());
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Información de Partidas",
            JOptionPane.PLAIN_MESSAGE
        );
    }//GEN-LAST:event_InfoPartidasActionPerformed

    private void AltaBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AltaBajaActionPerformed
        // TODO add your handling code here:
        String nombreJugador = JOptionPane.showInputDialog("Ingrese el nombre del jugador a dar de alta/baja:");
        if (nombreJugador != null && !nombreJugador.isEmpty()) {
            int indexJugador = buscaminas.existeJugador(nombreJugador);

            if (indexJugador == -1) {
                int opcionAltaBaja = JOptionPane.showOptionDialog(
                    null,
                    "¿Desea dar de alta al jugador?",
                    "Confirmar Alta",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null
                );

                if (opcionAltaBaja == JOptionPane.YES_OPTION) {
                    Jugador nuevoJugador = new Jugador(nombreJugador);
                    buscaminas.agregarJugador(nuevoJugador);
                    JOptionPane.showMessageDialog(null, "Jugador dado de alta exitosamente.");
                }
            } else {
                int opcionAltaBaja = JOptionPane.showOptionDialog(
                    null,
                    "El jugador ya existe. ¿Desea dar de baja al jugador?",
                    "Confirmar Baja",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null
                );

                if (opcionAltaBaja == JOptionPane.YES_OPTION) {
                    // Borrar al jugador existente
                    buscaminas.borraJugador(nombreJugador);
                    JOptionPane.showMessageDialog(null, "Jugador dado de baja exitosamente.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nombre de jugador no válido");
        }
    }//GEN-LAST:event_AltaBajaActionPerformed

    private void IniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IniciarSesionActionPerformed
        // TODO add your handling code here:
        String nombreJugador = JOptionPane.showInputDialog("Ingrese su nombre:");
        
        if (nombreJugador != null && !nombreJugador.isEmpty()) {
            // Verificar si el jugador existe
            int indexJugador = buscaminas.existeJugador(nombreJugador);

            if (indexJugador == -1) {
                // El jugador no existe asi que se crea
                JOptionPane.showMessageDialog(
                    this,
                    "El jugador no existe. Registrando jugador...",
                    "Iniciar Sesión",
                    JOptionPane.WARNING_MESSAGE
                );
                Jugador nuevoJugador = new Jugador(nombreJugador);
                buscaminas.agregarJugador(nuevoJugador);
                jugador = nuevoJugador;
                
                int opcionFoto = JOptionPane.showOptionDialog(
                            null,
                            "¿Desea añadir una foto de perfil?",
                            "Añadir Foto",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            null,
                            null
                );

                if (opcionFoto == JOptionPane.YES_OPTION) {
                    // Abrir cuadro de diálogo para seleccionar la foto
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.showOpenDialog(null);
                    File selectedFile = fileChooser.getSelectedFile();

                    if (selectedFile != null) {
                        // Obtener la ruta de la foto y asignarla al jugador
                        String fotoPath = selectedFile.getAbsolutePath();
                        jugador.setFoto(fotoPath);
                    }
                }
                if(jugador!=null && jugador.getNombre()!=null){
                    JOptionPane.showMessageDialog(MenuInicioSesion, "Bienvenido, " + nuevoJugador.getNombre() + "!");
                    Titulo1.setText("MENÚ JUGADOR - " + nuevoJugador.getNombre());
                    MenuInicioSesion.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(this,"Error al obtener el nombre del jugador","Error",JOptionPane.ERROR_MESSAGE);
                }
                

            }else {
                // hay que hacer que muestre el menu jugador en ambas
                Jugador jugadorActual = buscaminas.getJugadores().get(indexJugador);
                if(jugadorActual!=null && jugadorActual.getNombre()!=null){
                    JOptionPane.showMessageDialog(MenuInicioSesion, "Bienvenido, " + jugadorActual.getNombre() + "!");
                    jugador = jugadorActual;
                    Titulo1.setText("MENÚ JUGADOR - " + jugadorActual.getNombre());
                    MenuInicioSesion.setVisible(true);

                }else {
                     JOptionPane.showMessageDialog(this,"Error al obtener el nombre del jugador","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            // ocultar JFrame actual
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(null, "Nombre de jugador no válido");
        }
    }//GEN-LAST:event_IniciarSesionActionPerformed

    
    private void Principiante1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Principiante1ActionPerformed
        // TODO add your handling code here:
        mostrarTablero(Nivel.PRINCIPIANTE);
        
    }//GEN-LAST:event_Principiante1ActionPerformed

    private void Intermedio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Intermedio1ActionPerformed
        // TODO add your handling code here:
        mostrarTablero(Nivel.INTERMEDIO);
    }//GEN-LAST:event_Intermedio1ActionPerformed

    private void Experto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Experto1ActionPerformed
        // TODO add your handling code here:
        mostrarTablero(Nivel.EXPERTO);
    }//GEN-LAST:event_Experto1ActionPerformed

    private void Personalizado1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Personalizado1ActionPerformed
        // TODO add your handling code here:
        Personalizado.show(true);
    }//GEN-LAST:event_Personalizado1ActionPerformed

    private void numFilasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numFilasActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_numFilasActionPerformed

    private void numColumnasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numColumnasActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_numColumnasActionPerformed

    private void minasPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minasPActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_minasPActionPerformed

    private void JugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JugarActionPerformed
        // TODO add your handling code here:
        dimX = Integer.parseInt(numFilas.getText());
        dimY = Integer.parseInt(numColumnas.getText());
        numMinas = Integer.parseInt(minasP.getText());
        
        Personalizado.show(false);
        mostrarTablero(Nivel.PERSONALIZADO);
        
        //Despues de que se cree el tablero vaciar el contenido de los campos de texto
        numFilas.setText("");
        numColumnas.setText("");
        minasP.setText("");
    }//GEN-LAST:event_JugarActionPerformed

     
   
        
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AltaBaja;
    private javax.swing.JButton Calificacion;
    private javax.swing.JLabel Columnas;
    private javax.swing.JButton Experto1;
    private javax.swing.JLabel Filas;
    private javax.swing.JButton InfoPartidas;
    private javax.swing.JButton IniciarPartida;
    private javax.swing.JButton IniciarSesion;
    private javax.swing.JButton Intermedio1;
    private javax.swing.JButton Jugar;
    private javax.swing.JButton Listado;
    private javax.swing.JPanel MenuAdministrador;
    private javax.swing.JFrame MenuInicioSesion;
    private javax.swing.JButton MiPerfil;
    private javax.swing.JLabel Minas;
    private javax.swing.JDialog OpcionesCalificacion;
    private javax.swing.JDialog OpcionesNivel;
    private javax.swing.JDialog Personalizado;
    private javax.swing.JButton Personalizado1;
    private javax.swing.JButton PorAlfabeto;
    private javax.swing.JButton PorPuntuacion;
    private javax.swing.JButton PorVictorias;
    private javax.swing.JButton Principiante1;
    private javax.swing.JButton Ranking;
    private javax.swing.JButton Salir;
    private javax.swing.JFrame Tablero;
    private javax.swing.JLabel Titulo;
    private javax.swing.JLabel Titulo1;
    private javax.swing.JButton Volver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar4;
    private javax.swing.JTextField minasP;
    private javax.swing.JTextField numColumnas;
    private javax.swing.JTextField numFilas;
    // End of variables declaration//GEN-END:variables
}
