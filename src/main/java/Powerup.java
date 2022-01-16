import java.util.Random;

import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

/**
 * The Powerup class, defines a single powerup.
 */

public class Powerup extends Sprite {
	/* destination coordinates */
	private double destination_x;
	private double destination_y;
	/* speed */
	private double vx;
	private double vy;
	/** moving speed */
	private double speed;
	/** unit vector of moving velocity */
	private Vector2 velocity;
	/** random number generator for destinations */
	private Random randomGenerator;
	

	public Powerup(double speed, double x, double y) {
		super(new Image("res/powerup.png"), x, y);
		this.speed = speed;
		randomGenerator = new Random();
		destination_x = 
				randomGenerator.nextDouble() * ShadowBounce.SCREEN_WIDTH;
		destination_y = 
				randomGenerator.nextDouble() * ShadowBounce.SCREEN_HEIGHT;
	}
	
	public void update() {
		Point destination = new Point(destination_x, destination_y);
		Point currentPosition = new Point(super.getX(), super.getY());
		// if within 5 pixels of the destination, pick new destination
		if (destination.asVector().sub(currentPosition.asVector()).length() < 5) {
			destination_x = 
					randomGenerator.nextDouble() * ShadowBounce.SCREEN_WIDTH;
			destination_y = 
					randomGenerator.nextDouble() * ShadowBounce.SCREEN_HEIGHT;
			destination = new Point(destination_x, destination_y);
		}
		velocity = destination.asVector().sub(currentPosition.asVector());
		velocity = velocity.normalised();
		vx = velocity.x * speed;
		vy = velocity.y * speed;
		super.setX(super.getX() + vx);
		super.setY(super.getY() + vy);
	}

}
