package pantallas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import principal.PanelJuego;

public class PantallaFinal implements Pantalla{
	
	private String tiempoFinal;
	private PanelJuego panel;
	private String ruta;
	private BufferedImage canvasFinal;
	private Image fondoEscalado;
	
	public PantallaFinal(PanelJuego panel,String tiempoFinal,String ruta) {
		this.ruta = ruta;
		this.panel=panel;
		this.tiempoFinal=tiempoFinal;
		inicializarPantalla(panel);
	}
	
	@Override
	public void inicializarPantalla(PanelJuego panel) {
		try {
			canvasFinal = ImageIO.read(new File(ruta));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}

	@Override
	public void pintarPantalla(Graphics g) {
		rellenarFondo(g);
//		fondoEscalado = canvasFinal.getScaledInstance(panel.getWidth(), panel.getHeight(),
//				BufferedImage.SCALE_SMOOTH);
		
		
		g.drawImage(canvasFinal.getScaledInstance(panel.getWidth(), panel.getHeight(), BufferedImage.SCALE_SMOOTH), 0, 0, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("fuente1",1,40));
		//Imagenes/darth-vader-guitar.jpg
		if(ruta.equalsIgnoreCase("Imagenes/darth-vader-guitar.jpg")) {
			g.drawString("�Derrota! Pulsa para jugar de nuevo", 20, 40);
		}else {
			g.drawString("�Victoria! Pulsa para jugar de nuevo", 20, 40);
		}
		
		g.setColor(Color.GREEN);
		g.drawString(tiempoFinal, 60, 100);
		
		g.dispose();
	}
	
	private void rellenarFondo(Graphics g) {
		g.drawImage(fondoEscalado, 0, 0, null);
	}

	@Override
	public void ejecutarFrame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pulsarRaton(MouseEvent e) {
		panel.setPantalla(new PantallaJuego(panel));
		
	}

	@Override
	public void moverRaton(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redimensionar() {
		fondoEscalado = canvasFinal.getScaledInstance(panel.getWidth(), panel.getHeight(),
				BufferedImage.SCALE_SMOOTH);
		
	}

}
