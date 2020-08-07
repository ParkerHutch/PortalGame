
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Hazard extends Entity {

	private PortalGame gameObject;
	
	private boolean touchingPlayer;
	
	
	/**
	 * Makes a Hazard with a top-left coordinate (x, y) and a specified with and height
	 * @param x the left x coordinate of the Hazard
	 * @param y the top y coordinate of the Hazard
	 * @param width the width of the Hazard
	 * @param height the height of the Hazard
	 * @param gameObject the PortalGame object with data about levels and other objects
	 */
	public Hazard(double x, double y, double width, double height, Color color, PortalGame gameObject) {
		
		super(x, y, width, height, color);
		setGameObject(gameObject);
		
	}
	
	/**
	 * Updates the position of the Hazard and checks for collisions between it
	 * and any platforms in the level, and responds to those collisions 
	 * appropriately
	 */
	public void update() {
		
		applyVelocities();
		
		checkCollisions(getGameObject().getLevelManager().getCurrentLevel().getPlatforms());
		
		checkIfTouchingPlayer();
		
		if (isTouchingPlayer()) {
			
			getGameObject().getAnimator().stop();
			getGameObject().getMenuManager().showFailScreen();
			
		}
		
		
	}

	/**
	 * Checks if the Player's Rectangle intersects this Hazard's Rectangle,
	 * then sets the touchingPlayer variable accordingly
	 */
	public void checkIfTouchingPlayer() {
		
		if (getRectangle().intersects(getGameObject().getPlayer().getRectangle().getLayoutBounds())) {
			
			setTouchingPlayer(true);
			
		} else {
			
			setTouchingPlayer(false);
			
		}
		
	}
	
	/**
	 * @return the touchingPlayer
	 */
	public boolean isTouchingPlayer() {
		return touchingPlayer;
	}

	/**
	 * @param touchingPlayer the touchingPlayer to set
	 */
	public void setTouchingPlayer(boolean touchingPlayer) {
		this.touchingPlayer = touchingPlayer;
	}

	@Override
	public void resolveTopCollision(Rectangle rectangle) {
		
		setyVelocity(getyVelocity() * -1);
		
	}

	@Override
	public void resolveBottomCollision(Rectangle rectangle) {
		
		setyVelocity(getyVelocity() * -1);
		
	}

	@Override
	public void resolveLeftCollision(Rectangle rectangle) {
		
		setxVelocity(getxVelocity() * -1);
		
	}

	@Override
	public void resolveRightCollision(Rectangle rectangle) {
		
		setxVelocity(getxVelocity() * -1);
		
	}

	/**
	 * Gets the PortalGame instance held by the Hazard
	 * @return the PortalGame instance of the Hazard
	 */
	public PortalGame getGameObject() {
		return gameObject;
	}

	/**
	 * Sets the PortalGame instance of the Hazard
	 * @param gameObject the new PortalGame instance
	 */
	public void setGameObject(PortalGame gameObject) {
		this.gameObject = gameObject;
	}

	
	
	
}
