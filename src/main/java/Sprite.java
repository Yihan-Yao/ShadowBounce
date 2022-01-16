import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The Sprite class, defines a single sprite.
 */

public class Sprite {
	private double x;
	private double y;
	private Image image;
	
	public Sprite(Image image, double x, double y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	/**
     * Returns the bounding box of class Rectangle of this peg.
     */
	public Rectangle getRectangle() {
		Point p = new Point(x, y);
		return image.getBoundingBoxAt(p);
	}

	public void draw() {
		image.draw(x, y);
	}

}
