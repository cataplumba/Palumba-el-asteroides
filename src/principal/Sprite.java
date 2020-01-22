package principal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Representa un objeto en movimiento. Los sprites son colisionables. Un Sprite
 * se concibe como un BufferedImage con un ancho, alto y posición que tiene una
 * velocidad y que se puede estambar en un {@link Graphics}
 * 
 * @author jesusredondogarcia
 *
 */
public class Sprite {
	protected int posX;
	protected int posY;
	protected int ancho;
	protected int alto;
	protected int velX;
	protected int velY;
	protected BufferedImage buffer;

	/**
	 * Constructor privado para evitar repetición de código en los otros
	 * constructores.
	 */
	private Sprite(int posX, int posY, int ancho, int alto, int velX, int velY) {
		this.posX = posX;
		this.posY = posY;
		this.ancho = ancho;
		this.alto = alto;
		this.velX = velX;
		this.velY = velY;
	}

	/**
	 * Inicializa el {@link Sprite} con un color.
	 * 
	 * @param color Color del sprite.
	 */
	public Sprite(int posX, int posY, int ancho, int alto, int velX, int velY, Color color) {
		this(posX, posY, ancho, alto, velX, velY);
		pintarBuffer(color);
	}

	/**
	 * Inicializa el {@link Sprite} a partir de una ruta. Si hay varios sprites con
	 * la misma ruta, mejor evitar este constructor para evitar accesos a disco.
	 * 
	 * @param ruta Ruta de la imagen.
	 */
	public Sprite(int posX, int posY, int ancho, int alto, int velX, int velY, String ruta) {
		this(posX, posY, ancho, alto, velX, velY);
		pintarBuffer(ruta);
	}

	/**
	 * Inicizaliza el {@link Sprite} a partir de un Image ya creado.
	 * 
	 * @param imgConstructor
	 * @param redimensionar  Si vale verdadero es necesario redimensionar el buffer
	 *                       de entrada.
	 */
	public Sprite(int posX, int posY, int ancho, int alto, int velX, int velY, Image imgConstructor,
			boolean redimensionar) {
		this(posX, posY, ancho, alto, velX, velY);
		pintarBuffer(imgConstructor, redimensionar);
	}

	/**
	 * Pinta el buffer del color dado
	 * 
	 * @param color
	 */
	private void pintarBuffer(Color color) {
		buffer = new BufferedImage(this.ancho, this.alto, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, this.ancho, this.alto);
		g.dispose();
	}

	/**
	 * Estampa en el buffer la imagen de la ruta.
	 * 
	 * @param ruta
	 */
	private void pintarBuffer(String ruta) {
		buffer = new BufferedImage(this.ancho, this.alto, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		// Cargar la imagen, redimensionarla y estamparla:
		try {
			BufferedImage img = ImageIO.read(new File(ruta));
			g.drawImage(img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH), 0, 0, null);

		} catch (IOException e) {
			e.printStackTrace();
		}
		g.dispose();
	}

	/**
	 * Estampa la imagen en la ruta (a veces la redimensiona, a veces no).
	 * 
	 * @param imgConstructor
	 * @param redimensionar
	 */
	private void pintarBuffer(Image imgConstructor, boolean redimensionar) {
		buffer = new BufferedImage(this.ancho, this.alto, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		g.drawImage(redimensionar ? imgConstructor.getScaledInstance(this.ancho, this.alto, Image.SCALE_SMOOTH)
				: imgConstructor, 0, 0, null);
		g.dispose();
	}

	/**
	 * Actualiza la posición del Sprite para que siempre se mantega en el
	 * panelJuego.
	 * 
	 * @param panelJuego
	 */
	public void actualizarPosicion(PanelJuego panelJuego) {

		if (posX + ancho >= panelJuego.getWidth()) { // Si te vas por la derecha!
			velX = -Math.abs(velX);
		}
		if (posX < 0) {
			velX = Math.abs(velX);
		}

		// Eje vertical:
		if (posY + alto >= panelJuego.getHeight()) { // Si te vas por abajo!!
			velY = -Math.abs(velY);
		}
		if (posY < 0) {
			velY = Math.abs(velY);
		}

		posX = posX + velX;
		posY = posY + velY;
	}

	public void actualizarPosicionJefe(PanelJuego panelJuego) {

		if (posX + ancho >= panelJuego.getWidth()) { // Si te vas por la derecha!
			velX = -Math.abs(velX);
		}
		if (posX < 0) {
			velX = Math.abs(velX);
		}

		// Eje vertical:
		if (posY + alto >= panelJuego.getHeight() / 2) { // Si te vas por abajo!!
			velY = -Math.abs(velY);
		}
		if (posY < 0) {
			velY = Math.abs(velY);
		}

		posX = posX + velX;
		posY = posY + velY;
	}

	/**
	 * Comprueba si hay colisión entre este Sprite y otro que viene por parámetros.
	 * La colisión es cuadrada.
	 * 
	 * @param otro
	 * @return
	 */
	public boolean colisiona(Sprite otro) {
		boolean colisionX = posX < otro.posX ? (posX + ancho >= otro.posX) : (otro.posX + otro.ancho >= posX);

		boolean colisionY = posY < otro.posY ? (posY + alto >= otro.posY) : (otro.posY + otro.alto >= posY);

		return colisionX && colisionY;
	}

	/**
	 * Estampa el {@link Sprite#buffer} del {@link Sprite} en el gráficos de
	 * entrada.
	 * 
	 * @param g
	 */
	public void pintarEnMundo(Graphics g) {
		g.drawImage(buffer, posX, posY, null);
	}

	/**
	 * Actualiza la posición teniendo en cuenta la velocidad. No tiene en cuenta
	 * márgenes de pantalla.
	 */
	public void aplicarVelocidad() {
		posX += velX;
		posY += velY;

	}

	/** GETTERS && SETTERS **/

	public int getVelX() {
		return velX;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public int getVelY() {
		return velY;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public BufferedImage getBuffer() {
		return buffer;
	}

	public void setBuffer(BufferedImage buffer) {
		this.buffer = buffer;
	}
}
