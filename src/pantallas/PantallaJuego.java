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
import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sun.tools.javac.Main;

import principal.PanelJuego;
import principal.Sprite;

public class PantallaJuego implements Pantalla {

	final Font fuenteInicio = new Font("", Font.BOLD, 30);

	/** SPRITES **/
	Image imagenAsteroide;
	ArrayList<Sprite> asteroides;
	ArrayList<Sprite> disparos;
	private static final int ASTEROIDES_INICIALES = 2;
	private static final int LADO_ASTEROIDE = 40;

	Sprite nave;
	Image imagenNave;
	private static final int ANCHO_NAVE = 40;

	// Sprite disparo = null;
	private static final int ANCHO_DISPARO = 16;
	private static final int ALTO_DISPARO = 40;
	private static final int VELY_DISPARO = -10;

	/** VARIABLES PARA TIEMPO **/
	private double tiempoInicial = 0;
	private double tiempoTranscurrido;
	private DecimalFormat formato = new DecimalFormat("#.##");

	/** FONDO **/
	private BufferedImage fondo;
	private Image fondoEscalado;

	PanelJuego panelJuego;

	public PantallaJuego(PanelJuego panel) {
		panelJuego = panel;
		inicializarPantalla(panel);
	}

	@Override
	public void inicializarPantalla(PanelJuego panel) {
		panelJuego = panel;
		// IM�?GENES
		try {
			fondo = ImageIO.read(new File("Imagenes/galaxia.jpg"));
			imagenNave = ImageIO.read(new File("Imagenes/nave.png"));
			imagenAsteroide = ImageIO.read(new File("Imagenes/asteroide.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("PROBLEMAS AL CARGAR LAS IM�?GENES. FIN DEL PROGRAMA");
			System.exit(1);
		}

		// CREACIÓN NAVE
		nave = new Sprite(-ANCHO_NAVE - 10, -ANCHO_NAVE - 10, ANCHO_NAVE, ANCHO_NAVE, -50, -50, imagenNave, true);

		// ASTEROIDES:
		imagenAsteroide = imagenAsteroide.getScaledInstance(LADO_ASTEROIDE, LADO_ASTEROIDE, Image.SCALE_SMOOTH);
		asteroides = new ArrayList<>();
		Random rd = new Random();
		for (int i = 0; i < ASTEROIDES_INICIALES; i++) {
			asteroides.add(new Sprite(10, 10, LADO_ASTEROIDE, LADO_ASTEROIDE, rd.nextInt(14) + 1, rd.nextInt(14) + 1,
					imagenAsteroide, false));
		}

		// DISPAROS
		disparos = new ArrayList<Sprite>();

		tiempoInicial = System.nanoTime();
		fondoEscalado = fondo.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(),
				BufferedImage.SCALE_SMOOTH);
	}

	@Override
	public void pintarPantalla(Graphics g) {
		rellenarFondo(g);

		for (int i = 0; i < asteroides.size(); i++) {
			asteroides.get(i).pintarEnMundo(g);
		}

		nave.pintarEnMundo(g);

//		if (disparo != null) {
//			disparo.pintarEnMundo(g);
//		}

		for (int i = 0; i < disparos.size(); i++) {
			disparos.get(i).pintarEnMundo(g);
		}

		g.setFont(fuenteInicio);
		g.setColor(Color.ORANGE);
		tiempoTranscurrido = System.nanoTime() - tiempoInicial;
		g.drawString(formato.format((System.nanoTime() - tiempoInicial) / 1e9), 50, 50);

	}

	@Override
	public void ejecutarFrame() {
		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (asteroides.isEmpty()) {
			panelJuego.setPantalla(new PantallaJefe(tiempoTranscurrido, panelJuego));
		}
		comprobarColisiones();
		moverSprites();

	}

	public void reproducirSonidoDisparo() {
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
		// audioClip.close();
		try {
			audio.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void pulsarRaton(MouseEvent e) {
		reproducirSonidoDisparo();
//		if (disparo == null) {
//			disparo = new Sprite(e.getX() - ANCHO_DISPARO / 2, e.getY() - ALTO_DISPARO / 2, ANCHO_DISPARO, ALTO_DISPARO,
//					0, VELY_DISPARO, Color.GREEN);
//		}

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

	private void rellenarFondo(Graphics g) {
		g.drawImage(fondoEscalado, 0, 0, null);
	}

	/**
	 * Mueve todos los sprites del PanelJuego
	 */
	private void moverSprites() {
		// Asteroides:
		for (int i = 0; i < asteroides.size(); i++) {
			asteroides.get(i).actualizarPosicion(panelJuego);
		}

		for (int i = 0; i < disparos.size(); i++) {
		
			disparos.get(i).aplicarVelocidad();
		}
	}

	private void comprobarColisiones() {
		/*
		 * BORRADO, LOS ASTEROIDES NO CHOCAN ENTRE SI for(int i = 0; i <
		 * asteroides.size()-1;i++) { for(int j = i+1; j < asteroides.size();j++) {
		 * if(asteroides.get(i).colisiona(asteroides.get(j))) { asteroides.remove(j);
		 * asteroides.remove(i); j = i; } } }
		 */

		// Comprobamos colisión del disparo con asteroides:
//		for (int i = 0; i < asteroides.size() && disparo != null; i++) {
//			if (disparo.colisiona(asteroides.get(i))) {
//				disparo = null;
//				asteroides.remove(i);
//			}
//		}

		// Comprobamos si se sale por arriba:
		for (int i = 0; i < disparos.size(); i++) {
			if (disparos.get(i) != null && disparos.get(i).getPosY() + disparos.get(i).getAlto() < 0) {
				disparos.remove(i);
			}
		}

		if (!disparos.isEmpty()) {
			for (int i = disparos.size() - 1; i >= 0; i--) {
				for (int j = asteroides.size() - 1; j >= 0; j--) {
					if (disparos.get(i).colisiona(asteroides.get(j))) {
						asteroides.remove(j);
						disparos.remove(i);
						break;
					}
				}
			}
		}

		for (int i = 0; i < asteroides.size() && nave != null; i++) {
			if (nave.colisiona(asteroides.get(i))) {
				panelJuego.setPantalla(new PantallaFinal(panelJuego,
						formato.format((System.nanoTime() - tiempoInicial) / 1e9), "Imagenes/darth-vader-guitar.jpg"));
			}
		}

	}
}
