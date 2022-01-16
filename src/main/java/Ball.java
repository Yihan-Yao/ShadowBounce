import bagel.*;
import bagel.util.*;

/**
 * The Ball class, defines a single ball.
 */
public class Ball extends Sprite {
	private double vx;
	private double vy;
	private Image fireball_image;
	private double fallingSpeed;
	private boolean fire;
	
	public Ball(double x, double y, double initialSpeed, double fallingSpeed, Vector2 vector) {
		super(new Image("res/ball.png"), x, y);
		this.fireball_image = new Image("res/fireball.png");
		this.fallingSpeed = fallingSpeed;
		vx = vector.x * initialSpeed;
		vy = vector.y * initialSpeed;
		fire = false;
	}
	
	public void bounceV() {
		vx = -vx;
	}
	
	public void bounceH() {
		vy = -vy;
	}
	
	public double getVx() {
		return vx;
	}

	public double getVy() {
		return vy;
	}

	public boolean isFire() {
		return fire;
	}
	
	public void activateFire() {
		fire = true;
		super.setImage(fireball_image);
	}
	
	/**
     * Gravity effect, and bounce off the walls.
     */
	public void update() {
		vy += fallingSpeed;
		super.setX(super.getX() + vx);
		super.setY(super.getY() + vy);
		if (super.getX() < 0 || super.getX() > ShadowBounce.SCREEN_WIDTH) {
			vx = -vx;
		}
	}
}
