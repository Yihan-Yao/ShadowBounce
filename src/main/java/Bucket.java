import bagel.Image;

public class Bucket extends Sprite {
	/** moving speed */
	private double speed;

	public Bucket(double speed, double x, double y) {
		super(new Image("res/bucket.png"), x, y);
		this.speed = speed;
	}
	
	public void update() {
		// reverse if reaches side of screen
		if (super.getX() < 0 || super.getX() > ShadowBounce.SCREEN_WIDTH) {
			speed = -speed;
		}
		super.setX(super.getX() + speed);
	}

}
