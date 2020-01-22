package pantallas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import principal.PanelJuego;

public class PantallaInicio implements Pantalla{
	
	/**PINICIAL COLOR **/
	Color colorLetraInicio = Color.WHITE;
	PanelJuego panel;
	final Font fuenteInicio = new Font("", Font.BOLD, 30);
	
	//Constructor
	public PantallaInicio(PanelJuego panel) {
		inicializarPantalla(panel);
	}
	
	@Override
	public void inicializarPantalla(PanelJuego panel) {
		this.panel=panel;
		
	}

	@Override
	public void pintarPantalla(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		g.setFont(fuenteInicio);
		g.setColor(colorLetraInicio);
		g.drawString("BIENVENIDO", panel.getWidth()/2-120,  panel.getHeight()/2-10);
		
	}

	@Override
	public void ejecutarFrame() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {e.printStackTrace();}
		colorLetraInicio = colorLetraInicio == Color.WHITE ? Color.ORANGE : Color.WHITE;
		
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
		// TODO Auto-generated method stub
		
	}

}
