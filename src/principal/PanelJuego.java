package principal;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import pantallas.Pantalla;
import pantallas.PantallaInicio;
import pantallas.PantallaJuego;

public class PanelJuego extends JPanel implements Runnable {

	/** PANTALLAS **/
	Pantalla pantallaEjecucion;

	// El contructor
	public PanelJuego() {
		pantallaEjecucion = new PantallaInicio(this);

		// HILO
		;
		new Thread(this).start();

		// L�?STENERS
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				pantallaEjecucion.pulsarRaton(e);
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				pantallaEjecucion.redimensionar();

			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				pantallaEjecucion.moverRaton(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mouseMoved(e);
			}
		});
	}

	// Método que se llama automáticamente
	@Override
	public void paintComponent(Graphics g) {
		pantallaEjecucion.pintarPantalla(g);

	}

	@Override
	public void run() {
		while (true) {
			repaint();
			Toolkit.getDefaultToolkit().sync();
			pantallaEjecucion.ejecutarFrame();
			// Siempre repinto.

		}

	}

	public void setPantalla(Pantalla pantalla) {
		this.pantallaEjecucion = pantalla;
	}

}
