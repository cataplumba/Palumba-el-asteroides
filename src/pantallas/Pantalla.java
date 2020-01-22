package pantallas;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import principal.PanelJuego;

public interface Pantalla{
	
	public void inicializarPantalla(PanelJuego panel);
	public void pintarPantalla(Graphics g);
	public void ejecutarFrame();
	
	//Listeners
	public void pulsarRaton(MouseEvent e);
	public void moverRaton(MouseEvent e);
	public void redimensionar();
	
}
