import bagel.*;
import bagel.util.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Shadow Bounce game.
 *
 * @author Yihan Yao
 * 731698
 */
public class ShadowBounce extends AbstractGame{
    public final static int SCREEN_WIDTH = 1024;
    public final static int SCREEN_HEIGHT = 768;
    /** initial ball x coordinate */
    private final static double BALL_X = 512;
    /** initial ball y coordinate */
    private final static double BALL_Y = 32;
    /** initial ball speed */
    private final static double BALL_INITIAL_SPEED = 10;
    /** initial bucket x coordinate */
    private final static double BUCKET_X = 512;
    /** initial bucket y coordinate */
    private final static double BUCKET_Y = 744;
    /** initial bucket speed */
    private final static double BUCKET_INITIAL_SPEED = 4;
    /** initial power up speed */
    private final static double POWERUP_INITIAL_SPEED = 3;
    /** ball gravity */
    private final static double BALL_GRAVITY = 0.15;
    /** fireball explosion radius */
    private final static int EXPLOSION_RADIUS = 70;
    private final static Vector2 UP_LEFT = new Vector2(-1,-1).normalised();
    private final static Vector2 UP_RIGHT = new Vector2(1,-1).normalised();
    /** powerup generation chance */
    private final static double POWERUP_CHANCE = 0.1;
	/** random number generator for powerups */
    private static Random randomGenerator = new Random();
    private Point ballStartingPoint;
    private ArrayList<Peg> pegs = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Powerup> powerups = new ArrayList<>();
    private Bucket bucket;
    private int shotsLeft;
    

	public ShadowBounce() {
		super(SCREEN_WIDTH, SCREEN_HEIGHT);
		pegs = Loader.loadPegs();
		bucket = new Bucket(BUCKET_INITIAL_SPEED, BUCKET_X, BUCKET_Y);
		ballStartingPoint = new Point(BALL_X, BALL_Y);
		shotsLeft = 20;
	}
	
	/**
	 * Method to start the game.
	 */
    public static void main(String[] args) {
    	ShadowBounce game = new ShadowBounce();
        game.run();
    }
    
    /**
     * Delete the ball if it is no longer on the screen,
     * exit game when the escape key is pressed, spawn the ball when left mouse
     * button is clicked, then update the sprites and draw them.
     */
	@Override
	public void update(Input input) {
		// check for out of bounds, and 
		ArrayList<Ball> ballsToDelete = new ArrayList<>();
		for (Ball ball: balls) {
			if (ball.getY() > SCREEN_HEIGHT) {
				ballsToDelete.add(ball);
			}
		}
		
		// generate a new ball if old one is gone and LMB clicked
		if (input.wasPressed(MouseButtons.LEFT) && balls.isEmpty()) {
			balls.add(new Ball(BALL_X, BALL_Y, BALL_INITIAL_SPEED, 
					BALL_GRAVITY, input.directionToMouse(ballStartingPoint)));
        }
		
		// exit when ESC is pressed
		if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
		
		// update balls if any exists, delete collided pegs, 
		// add balls if green peg got destroyed
		ArrayList<Ball> ballsToAdd = new ArrayList<>();
		for (Ball ball : balls) {
			ball.update();
			Rectangle rectBall = ball.getRectangle();
			
			// Collision detection with the bucket, add shots afterwards
			Rectangle rectBucket = bucket.getRectangle();
			if (rectBucket.intersects(rectBall)) {
				shotsLeft++;
				ballsToDelete.add(ball);
				break;
			}
			
			// Collision detection with power ups, activate fire afterwards
			ArrayList<Powerup> powerupsToDelete = new ArrayList<>();
			for (Powerup powerup : powerups) {
				Rectangle rectPowerup = powerup.getRectangle();
				if (rectPowerup.intersects(rectBall)) {
					powerupsToDelete.add(powerup);
					ball.activateFire();
				}
			}
			powerups.removeAll(powerupsToDelete);
			
			// Collision detection with pegs
			ArrayList<Peg> pegsToDelete = new ArrayList<>();
			for (Peg peg : pegs) {
				Rectangle rectPeg = peg.getRectangle();
				if (rectPeg.intersects(rectBall)) {
					if (ball.isFire()) { // if the ball is on fire
						Point ballP = new Point(ball.getX(),ball.getY());
						for (Peg peg1 : pegs) {
							Point peg1P = new Point(peg1.getX(),peg1.getY());
							if (peg1P.asVector().sub(ballP.asVector()).length()
									< EXPLOSION_RADIUS) {
								switch (peg1.getType()) {
								case grey_peg:
								case grey_horizontal_peg:
								case grey_vertical_peg:
									break;
								case green_peg:
								case green_horizontal_peg:
								case green_vertical_peg:
									Ball ballToAdd1 = new Ball(peg.getX(), peg.getY(), 
											BALL_INITIAL_SPEED, BALL_GRAVITY, UP_LEFT);
									Ball ballToAdd2 = new Ball(peg.getX(), peg.getY(), 
											BALL_INITIAL_SPEED, BALL_GRAVITY, UP_RIGHT);
									ballToAdd1.activateFire();
									ballToAdd2.activateFire();
									ballsToAdd.add(ballToAdd1);
									ballsToAdd.add(ballToAdd2);
									shotsLeft += 2;
									peg1.setType(PegType.blue_peg);
								default:
									pegsToDelete.add(peg1);
									break;
								}
							}
						}
					}
					switch (peg.getType()) {
						case grey_peg:
						case grey_horizontal_peg:
						case grey_vertical_peg:
							break;
						case green_peg:
						case green_horizontal_peg:
						case green_vertical_peg:
							ballsToAdd.add(new Ball(peg.getX(), peg.getY(), 
									BALL_INITIAL_SPEED, BALL_GRAVITY, UP_LEFT));
							ballsToAdd.add(new Ball(peg.getX(), peg.getY(), 
									BALL_INITIAL_SPEED, 
									BALL_GRAVITY, UP_RIGHT));
							shotsLeft += 2;
						default:
							pegsToDelete.add(peg);
							break;
					}
					// generate power up at set chance
					if (randomGenerator.nextInt(
							(int) Math.round(1/POWERUP_CHANCE)) == 0) {
						powerups.add(new Powerup(
								POWERUP_INITIAL_SPEED, 
								randomGenerator.nextDouble() * ShadowBounce.SCREEN_WIDTH, 
								randomGenerator.nextDouble() * ShadowBounce.SCREEN_HEIGHT));
					}
					// bounce the ball according to side of collision
					Point point = new Point(ball.getX(), ball.getY());
					Vector2 vector = new Vector2(ball.getVx(), ball.getVy());
					Side side = rectBall.intersectedAt(point, vector);
					switch (side) {
						case LEFT:
						case RIGHT:
							ball.bounceH();
							break;
						case TOP:
						case BOTTOM:
							ball.bounceV();
							break;
						default:
							break;
					}
				}
			}
			pegs.removeAll(pegsToDelete);
		}
		balls.addAll(ballsToAdd);

		if (balls.removeAll(ballsToDelete)) {
			shotsLeft--;
			// if out of shots, game ends
			if (shotsLeft == 0) {
				Window.close();
			}
		}
		
		// update power ups
		for (Powerup powerup : powerups) {
			powerup.update();
		}
		
		// update bucket
		bucket.update();
		
		// check if all red pegs are destroyed, if so advance level
		boolean cleared = true;
		for (Peg peg: pegs) {
			if (peg.getType() == PegType.red_peg || 
					peg.getType() == PegType.red_vertical_peg ||
					peg.getType() == PegType.red_horizontal_peg) {
				cleared = false;
			}
		}
		if (cleared) {
			pegs = Loader.loadNextLvl();
		}
		
		// draw the pegs
		for (Peg peg : pegs) {
			peg.draw();
		}
		
		// draw the balls
		for (Ball ball : balls) {
			ball.draw();
		}
		
		// draw the power ups
		for (Powerup powerup : powerups) {
			powerup.draw();
		}
		
		// draw the bucket
		bucket.draw();
	}
}
