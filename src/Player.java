
import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Entity {
	
	private Rectangle drawingRectangle;
	
	private double maxVelocity = 15;

	private boolean insidePortal = false;
	
	private Portal lastEnteredPortal = null;
	private Portal lastExitedPortal = null;
	
	private boolean jumpReady = false;

	private PortalGame gameObject;

	private ArrayList<Portal> portals = new ArrayList<>();

	private ArrayList<Player> instances = new ArrayList<>(); 

	/**
	 * Creates a Player with the top-left coordinates given by a Point2D and a width
	 * and height of 40
	 * @param point the initial top-left (x, y) point of the player
	 * @param gameObject the PortalGame object with data about levels and other objects
	 */
	public Player(Point2D point, PortalGame gameObject) {
		
		super(point);
		setWidth(40);
		setHeight(40);
		
		setDrawingRectangle(new Rectangle(getX(), getY(), getWidth(), getHeight()));
		getDrawingRectangle().setFill(getColor());
		
		setGameObject(gameObject);
		
		instances.add(this);
		
	}
	
	/**
	 * Makes a Player with a top-left coordinate (x, y) and a specified with and height
	 * @param x the left x coordinate of the Player
	 * @param y the top y coordinate of the Player
	 * @param width the width of the Player
	 * @param height the height of the Player
	 * @param gameObject the PortalGame object with data about levels and other objects
	 */
	public Player(double x, double y, double width, double height, Color color, PortalGame gameObject) {
		
		super(x, y, width, height, color);
		setDrawingRectangle(new Rectangle(getX(), getY(), getWidth(), getHeight()));
		getDrawingRectangle().setFill(getColor());
		
		setGameObject(gameObject);

		instances.add(this);
	}

	/**
	 * Updates the position of the Player and its Portals, then draws them on 
	 * the Canvas
	 * @param gc the GraphicsContext of the canvas to draw the Player and Portals on
	 */
	public void update(GraphicsContext gc) {

		//setxVelocity(getxVelocity() * 0.98); // Friction
		applyVelocities();

		setyVelocity(getyVelocity() + .1); // Gravity

		capVelocities(maxVelocity);
		resolvePortalCollisions();

		if (!isInsidePortal()) {
			checkCollisions(gameObject.getLevelManager().getCurrentLevel().getPlatforms());
		}
		
		draw(gc);

		for (Portal portal : getPortals()) {

			portal.update();
			portal.draw(gc);

		}
		
	}

	/**
	 * Launches a portal in the direction given
	 * @param clickType
	 * @param direction the direction to launch the portal(in degrees)
	 */
	public void launchPortal(String clickType, double direction) {

		String portalType;
		if (clickType.equals("LEFT")) {

			portalType = "A";

		} else {

			portalType = "B";

		}



		int portalTypeIndex = -1;
		for (int i = 0; i < getPortals().size(); i++) {

			if (getPortals().get(i).getPortalType().equals(portalType)) {

				portalTypeIndex = i;

			}

		}

		Portal newPortal = new Portal(portalType, getCenterX(), getCenterY(), direction, gameObject);
		if (portalTypeIndex != -1) {

			getPortals().set(portalTypeIndex, newPortal);

		} else {

			getPortals().add(newPortal);

		}

	}

	/**
	 * Draws the player's rectangle on the canvas
	 * @param gc the GraphicsContext of the canvas to draw the player on
	 */
	@Override
	public void draw(GraphicsContext gc) {

		gc.save();
		
		gc.setFill(getColor());

		if (!isInsidePortal()) {
			
			easyFillRect(gc, getRectangle());
			
		} else {
			
			setDrawingRectangle(generatePlayerDrawingRectangle(getInstances().get(0)));
			
			easyFillRect(gc, getDrawingRectangle());
			
			if (getInstances().size() > 1) {
				
				easyFillRect(gc, getInstances().get(1).getRectangle());
				
			}
		}
		
		gc.restore();
		
	}
	
	/**
	 * Fills the Rectangle given on the Canvas
	 * @param gc the GraphicsContext for the Canvas to fill the Rectangle on
	 * @param rect the Rectangle to fill
	 */
	public void easyFillRect(GraphicsContext gc, Rectangle rect) {
		
		gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		
	}
	
	/**
	 * @return the drawingRectangle
	 */
	public Rectangle getDrawingRectangle() {
		return drawingRectangle;
	}

	/**
	 * @param drawingRectangle the drawingRectangle to set
	 */
	public void setDrawingRectangle(Rectangle drawingRectangle) {
		this.drawingRectangle = drawingRectangle;
	}

	/**
	 * Gets a Rectangle which represents only the Player's Rectangle 
	 * that is outside of a Portal
	 * @param player
	 * @return
	 */
	public Rectangle generatePlayerDrawingRectangle(Player player) {
		
		// TODO drawingRectangle should be a class variable in Player
		// then the resolvePortalCollisionsMethod should change drawingRectangle
		// when the Player is in a portal
		
		Rectangle newDrawingRectangle;
		
		if (getLastEnteredPortal().getOpeningDirection().equals("LEFT")) {
			
			newDrawingRectangle = new Rectangle(player.getX(), player.getY(), getLastEnteredPortal().getLeftX() - player.getX(), player.getHeight());
			
		} else if (getLastEnteredPortal().getOpeningDirection().equals("RIGHT")) {
			
			double playerRightX = player.getX() + player.getWidth();
			
			newDrawingRectangle = new Rectangle(getLastEnteredPortal().getRightX(), player.getY(), playerRightX - getLastEnteredPortal().getRightX(), player.getHeight());
			
		} else if (getLastEnteredPortal().getOpeningDirection().equals("UP")) {
			
			newDrawingRectangle = new Rectangle(player.getX(), player.getY(), player.getWidth(), getLastEnteredPortal().getTopY() - player.getY());
			
		} else {
			
			newDrawingRectangle = new Rectangle(player.getX(), getLastEnteredPortal().getBottomY(), player.getWidth(), player.getHeight());
			
		}
		
		newDrawingRectangle.setFill(getColor());
		
		return newDrawingRectangle;
		
	}

	/**
	 * @return the portals
	 */
	public ArrayList<Portal> getPortals() {
		return portals;
	}

	/**
	 * @return the instances
	 */
	public ArrayList<Player> getInstances() {
		return instances;
	}

	/**
	 * @return the insidePortal
	 */
	public boolean isInsidePortal() {
		return insidePortal;
	}

	/**
	 * @param insidePortal the insidePortal to set
	 */
	public void setInsidePortal(boolean insidePortal) {
		this.insidePortal = insidePortal;
	}

	/**
	 * Handles collisions between the Player and any of the placed Portals.
	 * If the Player is inside of a Portal, a "clone" is placed at the 
	 * other Portal to give the illusion of the Player going through one Portal
	 * and out the other. Once the Player has completely gone through the Portal
	 * they entered, the Player takes the x and y coordinates of the clone and 
	 * the velocities of the Player are adjusted.
	 */
	public void resolvePortalCollisions() {

		if (getPortals().size() == 2 && getPortals().get(0).isPlacedOnWall() && getPortals().get(1).isPlacedOnWall()) {

			Portal portal1 = portals.get(0); 
			Portal portal2 = portals.get(1);

			Bounds portal1Bounds = portal1.getRectangle().getLayoutBounds();
			Bounds portal2Bounds = portal2.getRectangle().getLayoutBounds();

			boolean insidePortal1 = instances.get(0).getRectangle().intersects(portal1Bounds);
			boolean insidePortal2 = instances.get(0).getRectangle().intersects(portal2Bounds);
			
			if (insidePortal1 || insidePortal2) {
				
				if (insidePortal1) {
					
					setLastEnteredPortal(portal1);
					setLastExitedPortal(portal2);
					
				} else {
					
					setLastEnteredPortal(portal2);
					setLastExitedPortal(portal1);
					
				}
				
				setInsidePortal(true);

				bounceInsidePortal(getLastEnteredPortal());
				
				placeClone(getLastEnteredPortal(), getLastExitedPortal());
				
				
			} else {

				swapCloneForPlayer(getLastEnteredPortal(), getLastExitedPortal());
				
			}

		}


	}

	public void swapCloneForPlayer(Portal enterPortal, Portal exitPortal) {

		// If the Player went completely through a portal and the collision is now over,
		// set the Player's location to the clone(which was placed during the collision)'s 
		// location and remove the clone
		if (getInstances().size() > 1) {

			Player clone = getInstances().get(1);

			
			getInstances().get(0).translateVelocities(enterPortal, exitPortal);
			
			getInstances().get(0).setLocation(clone.getX(), clone.getY());

			getInstances().remove(1);
			
		}

		setInsidePortal(false);

	}

	/**
	 * Checks if the Player should be able to enter a Portal. The player should be able to enter
	 * a Portal if it is within the sides of the Portal which run parallel to its opening.
	 * @param enterPortal the Portal which the Player is entering
	 * @return true if the Player should be able to enter the Portal, false otherwise
	 */
	public boolean shouldEnterPortal(Portal enterPortal) {

		boolean shouldEnterPortal = false;

		boolean isEnteringHorizontalPortal = enterPortal.getOpeningDirection().equals("UP") || enterPortal.getOpeningDirection().equals("DOWN");

		if (isEnteringHorizontalPortal) {

			if (getX() > enterPortal.getLeftX() && getX() + getWidth() < enterPortal.getRightX()) {

				shouldEnterPortal = true;

			}

		} else {

			if (getY() > enterPortal.getTopY() && getY() + getHeight() < enterPortal.getBottomY()) {

				shouldEnterPortal = true;

			}

		}

		return shouldEnterPortal;

	}

	/**
	 * Makes the Player bounce off the sides of the Portal
	 * Ex. if the player is in an upward-opening Portal, this method
	 * will make the Player bounce of the left and right edges if the Player
	 * hits them(keeping the Player inside those edges)
	 * @param enterPortal the Portal the Player is entering
	 */
	public void bounceInsidePortal(Portal enterPortal) {

		boolean isEnteringHorizontalPortal = enterPortal.getOpeningDirection().equals("UP") || enterPortal.getOpeningDirection().equals("DOWN");

		if (isEnteringHorizontalPortal) {

			if (getX() < enterPortal.getLeftX()) {

				setX(enterPortal.getLeftX());
				setxVelocity(getxVelocity() * -1);

			} else if (getX() + getWidth() > enterPortal.getRightX()) {

				setX(enterPortal.getRightX() - getWidth());
				setxVelocity(getxVelocity() * -1);

			}

		} else {

			if (getY() < enterPortal.getTopY()) {

				setY(enterPortal.getTopY());
				setyVelocity(getyVelocity() * -1);

			} else if (getY() + getHeight() > enterPortal.getBottomY()) {

				setY(enterPortal.getBottomY() - getHeight());
				setyVelocity(getyVelocity() * -1);

			}


		}

	}

	/**
	 * Sets the x and y coordinates of the Player's "clone", which is stored at index 1 of the
	 * instances ArrayList(this method will create the clone if it didn't exist already). When 
	 * this clone is drawn, it will appear as if the Player is entering through the enterPortal 
	 * while simultaneously exiting through the exitPortal. The x and y coordinates of the clone
	 * are calculated using the distance the Player has traveled into the enterPortal as well as 
	 * the location and opening direction of the exitPortal. 
	 * @param enterPortal the Portal which the Player is entering
	 * @param exitPortal the Portal which the Player is exiting
	 */
	public void placeClone(Portal enterPortal, Portal exitPortal) {

		// NOTE: the Player at index 0 of instances is the Player, index 1 is the clone(which doesn't collide)

		if (getInstances().size() < 2) {

			getInstances().add(new Player(-100, -100, getWidth(), getHeight(), getColor(), gameObject));
			getInstances().get(1).setColor(Color.BLUE);

		}

		String enterPortalDirection = enterPortal.getOpeningDirection();
		String exitPortalDirection = exitPortal.getOpeningDirection();

		boolean isEnteringHorizontalPortal = enterPortalDirection.equals("UP") || enterPortalDirection.equals("DOWN");
		boolean isExitingHorizontalPortal = exitPortalDirection.equals("UP") || exitPortalDirection.equals("DOWN");

		double distanceIntoEnterPortal = calculateDistanceIntoPortal(enterPortal);

		double cloneX;
		double cloneY;

		int safetyMargin = 0; // TODO Rename/label

		if (isEnteringHorizontalPortal) {

			double distanceToEnterPortalLeftSide = Math.abs(enterPortal.getLeftX() - getX());

			// If the Player is entering a horizontal portal and exiting through a horizontal portal

			if (isExitingHorizontalPortal) {

				cloneX = exitPortal.getLeftX() + distanceToEnterPortalLeftSide;

				if (exitPortal.getOpeningDirection().equals("UP")) {

					cloneY = exitPortal.getTopY() - distanceIntoEnterPortal - safetyMargin;

				} else {

					// If the Player enters a horizontal portal and exits through a down-opening portal

					cloneY = exitPortal.getBottomY() + distanceIntoEnterPortal + safetyMargin - getHeight();

				}


			} else {

				// If the Player is entering a horizontal portal and exiting through a vertical portal
				cloneY = exitPortal.getTopY() + distanceToEnterPortalLeftSide;

				if (exitPortal.getOpeningDirection().equals("RIGHT")) {

					cloneX = exitPortal.getRightX() + distanceIntoEnterPortal - getWidth() + safetyMargin;

				} else {

					cloneX = exitPortal.getLeftX() - distanceIntoEnterPortal - safetyMargin;

				}

			}

		} else {

			// If the player entered through a vertical portal

			double distanceToEnterPortalTopSide = Math.abs(enterPortal.getTopY() - getY());

			if (isExitingHorizontalPortal) {

				cloneX = exitPortal.getLeftX() + distanceToEnterPortalTopSide;

				if (exitPortal.getOpeningDirection().equals("UP")) {

					cloneY = exitPortal.getTopY() - distanceIntoEnterPortal - safetyMargin;

				} else {

					cloneY = exitPortal.getBottomY() + distanceIntoEnterPortal + safetyMargin - getHeight();

				}

			} else {

				// If the Player enters through a vertical portal and exits through a vertical portal

				cloneY = exitPortal.getTopY() + distanceToEnterPortalTopSide;

				if (exitPortal.getOpeningDirection().equals("RIGHT")) {

					cloneX = exitPortal.getRightX() + distanceIntoEnterPortal - getWidth() + safetyMargin ;

				} else {

					cloneX = exitPortal.getLeftX() - distanceIntoEnterPortal - safetyMargin;

				}

			}

		}

		getInstances().get(1).setLocation(cloneX, cloneY);

	}

	/**
	 * Translates the x and y velocities of the Player. The translation made
	 * depends on the opening directions of the Portals through which the 
	 * Player enters and exits.
	 * @param enterPortal the Portal which the Player entered
	 * @param exitPortal the Portal which the Player exited
	 */
	public void translateVelocities(Portal enterPortal, Portal exitPortal) {

		String enterPortalDirection = enterPortal.getOpeningDirection();
		String exitPortalDirection = exitPortal.getOpeningDirection();
		boolean isEnteringHorizontalPortal = enterPortalDirection.equals("UP") || enterPortalDirection.equals("DOWN");
		boolean isExitingHorizontalPortal = exitPortalDirection.equals("UP") || exitPortalDirection.equals("DOWN");

		if (enterPortalDirection.equals(exitPortalDirection)) {

			if (isEnteringHorizontalPortal) {

				setyVelocity(getyVelocity() * -1);

			} else {

				setxVelocity(getxVelocity() * -1);

			}

		} else {

			if (isEnteringHorizontalPortal == isExitingHorizontalPortal) {

				// If the player is entering and exiting through Portals of the same
				// alignment(horizontal/vertical) but different opening directions,
				// leave the Player's velocities unchanged
				// ex. If the player enters a right-opening Portal and exits a 
				// left-opening Portal, the player's velocities do not need to be
				// changed.

			} else {

				// If the Player enters a horizontal Portal and exits through a vertical Portal,
				// or if the Player enters a vertical Portal and exits a horizontal Portal

				String combination = enterPortalDirection + exitPortalDirection;

				boolean simpleSwap = ( combination.contains("LEFT") && combination.contains("DOWN") ) || 
						( combination.contains("RIGHT") && combination.contains("UP") );

				double oldXVelocity = getxVelocity();
				double oldYVelocity = getyVelocity();


				if (simpleSwap) {

					setxVelocity(oldYVelocity);
					setyVelocity(oldXVelocity);

				} else {

					if (enterPortalDirection.equals("LEFT") || enterPortalDirection.equals("RIGHT")) {

						setxVelocity(oldYVelocity);
						setyVelocity(oldXVelocity * -1);

					} else {

						setxVelocity(oldYVelocity * -1);
						setyVelocity(oldXVelocity);

					}

				}


			}
		}

	}

	/**
	 * Calculates how far into a Portal the Player has traveled. The distance
	 * is calculated by getting the absolute difference between the x/y coordinate
	 * of the non-opening side of the Portal and the Player's respective x/y
	 * coordinate closest to that side. For example, if the Player enters
	 * a left-opening Portal, the distance between the Portal's left x coordinate
	 * and the Player's right x coordinate is calculated and returned. 
	 * @param portal the Portal the Player is entering
	 * @return the distance the Player has traveled into the Portal, in pixels
	 */
	public double calculateDistanceIntoPortal(Portal portal) {

		double distance = -1;
		if (portal.getOpeningDirection().equals("LEFT")) {

			// player -> | 

			double playerRightX = getX() + getWidth();

			distance = Math.abs(portal.getLeftX() - playerRightX);


		} else if (portal.getOpeningDirection().equals("RIGHT")) {

			// | <- player
			double playerLeftX = getX();

			distance = Math.abs(playerLeftX - portal.getRightX());

		} else if (portal.getOpeningDirection().equals("UP")) {

			double playerBottomY = getY() + getHeight();

			distance = Math.abs(portal.getTopY() - playerBottomY);

		} else if (portal.getOpeningDirection().equals("DOWN")) {

			double playerTopY = getY();

			distance = Math.abs(portal.getBottomY() - playerTopY);
		}

		return distance;

	}

	/**
	 * @return the lastEnteredPortal
	 */
	public Portal getLastEnteredPortal() {
		return lastEnteredPortal;
	}

	/**
	 * @param lastEnteredPortal the lastEnteredPortal to set
	 */
	public void setLastEnteredPortal(Portal lastEnteredPortal) {
		this.lastEnteredPortal = lastEnteredPortal;
	}

	/**
	 * @return the lastExitedPortal
	 */
	public Portal getLastExitedPortal() {
		return lastExitedPortal;
	}

	/**
	 * @param lastExitedPortal the lastExitedPortal to set
	 */
	public void setLastExitedPortal(Portal lastExitedPortal) {
		this.lastExitedPortal = lastExitedPortal;
	}

	/**
	 * Checks that the magnitude of the Player's velocities
	 * are less than the given maximumMagnitude. If a velocity
	 * is greater than this threshold, it will be set to equal
	 * the value of the threshold.
	 */
	public void capVelocities(double maxMagnitude) {
		
		if (Math.abs(getxVelocity()) > maxMagnitude) {
			
			if (getxVelocity() > 0) {
				
				setxVelocity(maxMagnitude);
				
			} else {
				
				setxVelocity(-maxMagnitude);
				
			}
			
		}
		
		if (Math.abs(getyVelocity()) > maxMagnitude) {
			
			if (getyVelocity() > 0) {
				
				setyVelocity(maxMagnitude);
				
			} else {
				
				setyVelocity(-maxMagnitude);
				
			}
			
		}
		
	}
	
	@Override
	public void resolveTopCollision(Rectangle rectangle) {

		setyVelocity(getyVelocity() * -1);

	}

	@Override
	public void resolveBottomCollision(Rectangle rectangle) {

		setY(rectangle.getY() - getHeight());
		setyVelocity(0);
		setxVelocity(getxVelocity() * .95);
		setJumpReady(true);

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
	 * @return the jumpReady
	 */
	public boolean isJumpReady() {
		return jumpReady;
	}

	/**
	 * @param jumpReady the jumpReady to set
	 */
	public void setJumpReady(boolean jumpReady) {
		this.jumpReady = jumpReady;
	}

	/**
	 * Clears many of the objects associated with the Player, such as Portals
	 * and instances, and then sets the location of the Player based on the
	 * Point2D given. This method is useful for when the user is starting
	 * or restarting a level.
	 * @param startPoint a Point2D containing the new location for the Player
	 */
	public void reset(Point2D startPoint) {
		
		stopVelocities();
		getPortals().clear();
		
		if (getInstances().size() > 1) {
			
			getInstances().remove(1);
			
		}
		
		setInsidePortal(false);
		
		setLocation(startPoint);
		
		
	}

	/**
	 * Gets the PortalGame instance held by the Player
	 * @return the PortalGame instance
	 */
	public PortalGame getGameObject() {
		return gameObject;
	}

	/**
	 * Sets the PortalGame instance of the Player
	 * @param gameObject new PortalGame instance
	 */
	public void setGameObject(PortalGame gameObject) {
		this.gameObject = gameObject;
	}
	
}
