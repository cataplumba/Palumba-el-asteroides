package pantallas;

/**
 * @author Alejandro Pascual Clemente, Álvaro Sánchez Hernández, Daniel Simón Mateo
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sun.tools.javadoc.main.Start;

import principal.PanelJuego;
import principal.Sprite;

public class PantallaJefe implements Pantalla {

	final Font fuenteInicio = new Font("", Font.BOLD, 30);

	/** SPRITES **/
	Image imagenJefe;
	Sprite jefe;
	ArrayList<Sprite> disparos;
	ArrayList<Sprite> disparosJefe;
	private static final int LADO_JEFE = 40;
	private int vidaJefe = 75;

	Sprite nave;
	Image imagenNave;
	Image imagenBolaFuego;
	private static final int ANCHO_NAVE = 40;

	Sprite disparo = null;
	private static final int ANCHO_DISPARO = 16;
	private static final int ALTO_DISPARO = 40;
	private static final int VELY_DISPARO = -10;

	Runnable runDisparosJefe;
	Runnable disparar;
	

	/** VARIABLES PARA TIEMPO **/
	private double tiempoInicial;
	private DecimalFormat formato = new DecimalFormat("#.##");

	/** FONDO **/
	private BufferedImage fondo;
	private Image fondoEscalado;

	PanelJuego panelJuego;

	PantallaJefe(double tiempoInicial, PanelJuego panel) {
		this.tiempoInicial = (float) tiempoInicial;
		this.panelJuego = panel;
		inicializarPantalla(panel);

	}

	/**
	 * Inicializa los elementos del juego
	 */
	@Override
	public void inicializarPantalla(PanelJuego panel) {
		Random rd = new Random();
		panelJuego = panel;
		// IM�?GENES
		try {
			fondo = ImageIO.read(new File("Imagenes/galaxiaJefe.jpg"));
			imagenNave = ImageIO.read(new File("Imagenes/nave.png"));
			imagenJefe = ImageIO.read(new File("Imagenes/sinistar.png"));
			imagenBolaFuego = ImageIO.read(new File("Imagenes/fireball.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("PROBLEMAS AL CARGAR LAS IM�?GENES. FIN DEL PROGRAMA");
			System.exit(1);
		}

		// CREACIÓN NAVE
		nave = new Sprite(-ANCHO_NAVE - 10, -ANCHO_NAVE - 10, ANCHO_NAVE, ANCHO_NAVE, -50, -50, imagenNave, true);

		jefe = new Sprite(10, 10, LADO_JEFE * 8, LADO_JEFE * 8, 8, 8, imagenJefe, true);

		disparos = new ArrayList<Sprite>();
		disparosJefe = new ArrayList<Sprite>();
		
		iniciarHiloDisparosJefe();
		
		tiempoInicial = tiempoInicial;
		fondoEscalado = fondo.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(),
				BufferedImage.SCALE_SMOOTH);

	}

	/**
	 * Método que controla el hilo de los disparos del jefe
	 */
	public void iniciarHiloDisparosJefe() {
		runDisparosJefe = new Runnable() {

			@Override
			public void run() {
				while (true) {
					if ((nave.getPosX() <= jefe.getPosX() + (jefe.getAncho() + 400) && (nave.getPosX() >= jefe.getPosX() + (jefe.getAncho() - 600)))) {
						disparosJefe.add(new Sprite(jefe.getPosX() + (jefe.getAncho() / 4),
								jefe.getPosY() + (jefe.getAlto() / 2), jefe.getAncho() / 2, jefe.getAncho() / 2, 0, 20,
								imagenBolaFuego, true));

						disparosJefe.add(new Sprite(jefe.getPosX() + (jefe.getAncho() / 4),
								jefe.getPosY() + (jefe.getAlto() / 2), jefe.getAncho() / 2, jefe.getAncho() / 2, -20,
								20, imagenBolaFuego, true));

						disparosJefe.add(new Sprite(jefe.getPosX() + (jefe.getAncho() / 4),
								jefe.getPosY() + (jefe.getAlto() / 2), jefe.getAncho() / 2, jefe.getAncho() / 2, 20, 20,
								imagenBolaFuego, true));
					}

//					try {
//						Thread.sleep(850);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
				}

			}
		};

		Thread hiloDispararJefe = new Thread(runDisparosJefe);
		hiloDispararJefe.start();
	}
	
	

	/**
	 * Pinta los elementos de la pantalla
	 */
	@Override
	public void pintarPantalla(Graphics g) {
		rellenarFondo(g);

		jefe.pintarEnMundo(g);
		nave.pintarEnMundo(g);

		for (int i = 0; i < disparos.size(); i++) {
			disparos.get(i).pintarEnMundo(g);
		}

		for (int i = 0; i < disparosJefe.size(); i++) {
			disparosJefe.get(i).pintarEnMundo(g);
		}

		if (disparo != null) {
			disparo.pintarEnMundo(g);
		}

		g.setFont(fuenteInicio);
		g.setColor(Color.ORANGE);
		g.drawString(formato.format((System.nanoTime() - tiempoInicial) / 1e9), 50, 50);

	}

	/**
	 * Pinta el fondo de la partida
	 * 
	 * @param g
	 */
	private void rellenarFondo(Graphics g) {
		g.drawImage(fondoEscalado, 0, 0, null);

	}

	@Override
	public void ejecutarFrame() {
		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (vidaJefe <= 0) {
			panelJuego.setPantalla(new PantallaFinal(panelJuego,
					formato.format((System.nanoTime() - tiempoInicial) / 1e9), "Imagenes/fotoPantallaFinal.jpg"));
		}

		comprobarColisiones();
		moverSprites();

	}

	/**
	 * Actualiza la posición de los sprites de la pantalla
	 */
	private void moverSprites() {
		// Jefe
		jefe.actualizarPosicionJefe(panelJuego);

		// Disparos
		for (int i = 0; i < disparos.size(); i++) {
			disparos.get(i).aplicarVelocidad();
		}

		for (int i = 0; i < disparosJefe.size(); i++) {
			// disparosJefe.get(i).actualizarPosicion(panelJuego);
			disparosJefe.get(i).aplicarVelocidad();
		}

	}

	/**
	 * Reproduce el sonido del disparo de la nave
	 */
	public void reproducirSonidoDisparo() {
		new Runnable() {

			@Override
			public void run() {
				File fich = new File("Sonidos/laser.wav");
				AudioInputStream audio = null;
				try {
					audio = AudioSystem.getAudioInputStream(fich);
				} catch (UnsupportedAudioFileException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				AudioFormat format = audio.getFormat();

				DataLine.Info info = new DataLine.Info(Clip.class, format);

				Clip audioClip = null;

				try {
					audioClip = (Clip) AudioSystem.getLine(info);
				} catch (LineUnavailableException e1) {
					e1.printStackTrace();
				}

				try {
					audioClip.open(audio);
				} catch (LineUnavailableException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				audioClip.start();
				try {
					audio.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				new Thread(this).start();
			}
		};
	}

	@Override
	public void pulsarRaton(MouseEvent e) {
		reproducirSonidoDisparo();

		disparos.add(new Sprite(e.getX() - ANCHO_DISPARO / 2, e.getY() - ALTO_DISPARO / 2, ANCHO_DISPARO, ALTO_DISPARO,
				0, VELY_DISPARO, Color.GREEN));

	}

	@Override
	public void moverRaton(MouseEvent e) {
		nave.setPosX(e.getX() - nave.getAncho() / 2);
		nave.setPosY(e.getY() - nave.getAlto() / 2);

	}

	@Override
	public void redimensionar() {
		fondoEscalado = fondo.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(),
				BufferedImage.SCALE_SMOOTH);

	}

	private void comprobarColisiones() {

		if (nave.colisiona(jefe)) {
			panelJuego.setPantalla(new PantallaFinal(panelJuego,
					formato.format((System.nanoTime() - tiempoInicial) / 1e9), "Imagenes/darth-vader-guitar.jpg"));
		}

		for (int i = 0; i < disparos.size(); i++) {
			if (disparos.get(i).colisiona(jefe)) {
				vidaJefe--;
				disparos.remove(i);
			}
		}

		for (int i = 0; i < disparosJefe.size(); i++) {
			if(nave.colisiona(disparosJefe.get(i))) {
				panelJuego.setPantalla(new PantallaFinal(panelJuego,
						formato.format((System.nanoTime() - tiempoInicial) / 1e9), "Imagenes/darth-vader-guitar.jpg"));
			}
		}

		// Comprobamos si se sale por arriba:
		for (int i = 0; i < disparos.size(); i++) {
			if ((disparos.get(i).getPosY() + disparos.get(i).getAlto()) < 0) {
				disparos.remove(i);
			}
		}
	}

}
