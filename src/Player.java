import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Player {

	private double x;
	private double y;
	private double width;
	private double height;
	private Color color;

	private double xVelocity;
	private double yVelocity;
	
	private boolean insidePortal = false;

	private PortalGame gameObject;

	private ArrayList<Portal> portals = new ArrayList<>();
	
	private ArrayList<Player> instances = new ArrayList<>(); 

	/**
	 * Makes a Player with top-left coordinate (x, y) and a specified with and height
	 * @param x left x coordinate of the Player
	 * @param y top y coordinate of the Player
	 * @param width width of the Player
	 * @param height height of the Player
	 */
	public Player(double x, double y, double width, double height, PortalGame gameObject) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.gameObject = gameObject;
		
		instances.add(this);
	}

	/**
	 * Updates the position of the player and portals, and also draws them on 
	 * the canvas
	 * @param gc the GraphicsContext of the canvas to draw the player and portals on
	 */
	public void update(GraphicsContext gc) {
		
		//setxVelocity(getxVelocity() * 0.99);
		
		setX(getX() + getxVelocity());
		setY(getY() + getyVelocity());

		//setyVelocity(getyVelocity() + .1); // Gravity
		
		draw(gc);

		for (Portal portal : getPortals()) {

			portal.update();
			portal.draw(gc);

		}
		
		resolvePortalCollisions();
		
		// TODO: should only execute the below line if !isInsidePortal()
		if (!isInsidePortal()) {
			checkCollisions(gameObject.getPlatformObjects());
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
	public void draw(GraphicsContext gc) {

		
		// TODO I should modify the drawing method when the player is inside a portal
		// ex. only draw the rect up to the portal
		
		gc.save();
		for (Player clone : instances) {
			
			
			gc.setFill(clone.getColor());
			gc.fillRect(clone.getX(), clone.getY(), clone.getWidth(), clone.getHeight());
			
		}
		
		gc.restore();
		
		
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
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Sets the top left point of the Player
	 * @param x the new left x coordinate of the Player
	 * @param y the new top y coordinate of the Player
	 */
	public void setLocation(double x, double y) {
		
		setX(x);
		setY(y);
		
	}

	/**
	 * Gets the center x coordinate of the player
	 * @return the center x coordinate
	 */
	public double getCenterX() {

		return (getX() + getWidth() / 2);

	}

	/**
	 * Gets the center y coordinate of the player
	 * @return the center y coordinate
	 */
	public double getCenterY() {

		return (getY() + getHeight() / 2);

	}

	/**
	 * Gets the center (x, y) of the Player as a Point2D object
	 * @return the center point of the Player
	 */
	public Point2D getCenterPoint() {

		return new Point2D(getCenterX(), getCenterY());

	}

	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
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
	 * Returns a rectangle representing the containing boundaries of the tile
	 * @return a Rectangle2D object which contains the dimensions of the  Player
	 */
	public Rectangle getProximityBox() {
		return new Rectangle(getCenterX() - getWidth() * 2, getCenterY() - getHeight() * 2, getWidth() * 4, getHeight() * 4);
	}
	
	public Rectangle getRectangle() {
		
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
		
	}

	public Rectangle getTopBounds() {
		return new Rectangle(getX() + getWidth() / 4, getY(), getWidth() / 2, getHeight() / 8);
	}

	public Rectangle getRightBounds() {
		// old: return new Rectangle(getX() + (7 * getWidth()) / 8, getY() + getHeight() / 8, getWidth() / 8,
		//    		(6 * getHeight() / 8));
		return new Rectangle(getX() + (5 * getWidth()) / 6, getY() + getHeight() / 8, getWidth() / 6,
				(6 * getHeight() / 8));
	}

	public Rectangle getLeftBounds() {
		// old: return new Rectangle(getX(), getY() + getHeight() / 8, getWidth() / 8, (6 * getHeight() / 8));
		return new Rectangle(getX(), getY() + getHeight() / 8, getWidth() / 6, (6 * getHeight() / 8));
	}

	public Rectangle getBottomBounds() {
		return new Rectangle(getX() + getWidth() / 4, getY() + (7 * getHeight() / 8), getWidth() / 2,
				(getHeight() / 8) + 0);
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
			
			Portal enterPortal;
			Portal exitPortal;
			
			Portal portal1 = portals.get(0); 
			Portal portal2 = portals.get(1);
			
			Bounds portal1Bounds = portal1.getRectangle().getLayoutBounds();
			Bounds portal2Bounds = portal2.getRectangle().getLayoutBounds();
			
			boolean insidePortal1 = instances.get(0).getRectangle().intersects(portal1Bounds);
			boolean insidePortal2 = instances.get(0).getRectangle().intersects(portal2Bounds);
			
			if (insidePortal1) {
				
				enterPortal = portal1;
				exitPortal = portal2;
				
			} else {
				
				enterPortal = portal2;
				exitPortal = portal1;
				
			}
			
			if (insidePortal1 || insidePortal2) {

				setInsidePortal(true);
				
				bounceInsidePortal(enterPortal);
				
				placeClone(enterPortal, exitPortal);
				
				
			} else {
				
				// If the Player went completely through a portal and the collision is now over,
				// set the Player's location to the clone(which was placed during the collision)'s 
				// location and remove the clone
				if (getInstances().size() > 1) {
					
					Player clone = getInstances().get(1);
					
					instances.get(0).setLocation(clone.getX(), clone.getY());
					
					instances.get(0).adjustVelocities(enterPortal, exitPortal);
					
					instances.remove(1);
					
				}

				setInsidePortal(false);

			}

		}

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
				// TODO FIXME
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

			getInstances().add(new Player(-100, -100, getWidth(), getHeight(), gameObject));
			getInstances().get(1).setColor(Color.BLUE);

		}
		
		String enterPortalDirection = enterPortal.getOpeningDirection();
		String exitPortalDirection = exitPortal.getOpeningDirection();
		boolean isEnteringHorizontalPortal = enterPortalDirection.equals("UP") || enterPortalDirection.equals("DOWN");
		boolean isExitingHorizontalPortal = exitPortalDirection.equals("UP") || exitPortalDirection.equals("DOWN");
		
		double distanceIntoEnterPortal = calculateDistanceIntoPortal(enterPortal);
		
		double cloneX;
		double cloneY;
		
		if (isEnteringHorizontalPortal) {
			
			double distanceToEnterPortalLeftSide = Math.abs(enterPortal.getLeftX() - getX());
			
			// If the Player is entering a horizontal portal and exiting through a horizontal portal

			if (isExitingHorizontalPortal) {
				
				cloneX = exitPortal.getLeftX() + distanceToEnterPortalLeftSide;
				
				if (exitPortal.getOpeningDirection().equals("UP")) {
					
					cloneY = exitPortal.getTopY() - distanceIntoEnterPortal;
					
				} else {
					
					// If the Player enters a horizontal portal and exits through a down-opening portal
					
					cloneY = exitPortal.getTopY() + distanceIntoEnterPortal - getHeight() + 5;

				}
				
				
			} else {

				// If the Player is entering a horizontal portal and exiting through a vertical portal
				cloneY = exitPortal.getTopY() + distanceToEnterPortalLeftSide;

				if (exitPortal.getOpeningDirection().equals("RIGHT")) {
					
					cloneX = exitPortal.getRightX() + distanceIntoEnterPortal - getWidth();
					
				} else {
					
					cloneX = exitPortal.getLeftX() - distanceIntoEnterPortal;
					
				}
				
			}
			
		} else {
			
			// If the player entered through a vertical portal

			double distanceToEnterPortalTopSide = Math.abs(enterPortal.getTopY() - getY());

			
			if (isExitingHorizontalPortal) {

				cloneX = exitPortal.getLeftX() + distanceToEnterPortalTopSide;

				if (exitPortal.getOpeningDirection().equals("UP")) {

					cloneY = exitPortal.getTopY() - distanceIntoEnterPortal;

				} else {
					
					cloneY = exitPortal.getTopY() + distanceIntoEnterPortal - getHeight();

				}

			} else {
				
				// If the Player enters through a vertical portal and exits through a vertical portal
				
				cloneY = exitPortal.getTopY() + distanceToEnterPortalTopSide;

				if (exitPortal.getOpeningDirection().equals("RIGHT")) {

					cloneX = exitPortal.getRightX() + distanceIntoEnterPortal - getWidth();

				} else {

					cloneX = exitPortal.getLeftX() - distanceIntoEnterPortal;
					
				}

			}
			
		}
		
		getInstances().get(1).setLocation(cloneX, cloneY);
		
	}
	
	/**
	 * Adjusts the x and y velocities of the Player. The adjustment made
	 * depends on the opening directions of the Portals through which the 
	 * Player enters and exits.
	 * @param enterPortal the Portal which the Player entered
	 * @param exitPortal the Portal which the Player exited
	 */
	public void adjustVelocities(Portal enterPortal, Portal exitPortal) {
		
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
			double portalLeftX = portal.getCenterX() - portal.getWidth();
			double playerRightX = getX() + getWidth();
			
			distance = Math.abs(portalLeftX - playerRightX);
			
			
		} else if (portal.getOpeningDirection().equals("RIGHT")) {
			
			// | <- player
			double playerLeftX = getX();
			double portalRightX = portal.getCenterX() + portal.getWidth() / 2;
			
			distance = Math.abs(playerLeftX - portalRightX);
			
		} else if (portal.getOpeningDirection().equals("UP")) {
			
			double playerBottomY = getY() + getHeight();
			double portalTopY = portal.getCenterY() - portal.getHeight() / 2;
			
			distance = Math.abs(portalTopY - playerBottomY);
			
		} else if (portal.getOpeningDirection().equals("DOWN")) {
			
			double playerTopY = getY();
			double portalBottomY = portal.getCenterY() + portal.getHeight() / 2;
			
			distance = Math.abs(portalBottomY - playerTopY);
		}
		
		return distance;
		
	}
	
	/**
	 * Checks for collisions(intersections) between the Player and a
	 * given ArrayList of Rectangle objects, which represent platforms
	 * or boundaries in the game. If a collision is occurring, this method 
	 * determines what type of collision it is by determining what 
	 * part of the player is colliding with another object, and then calls 
	 * an appropriate method to resolve the collision.
	 * @param rectangles an ArrayList of Rectangles representing game objects
	 */
	public void checkCollisions(ArrayList<Rectangle> rectangles) {
		
		boolean topCollision;
		boolean bottomCollision;
		boolean leftCollision;
		boolean rightCollision;
		
		for (Rectangle rectangle : rectangles) {
			
			// If the rectangle intersects the player somewhere
			if (getProximityBox().intersects(rectangle.getLayoutBounds())) {
				
				Bounds otherRectangle = rectangle.getLayoutBounds();
				
				// Determine what kind of collision is occuring
				bottomCollision = getBottomBounds().intersects(otherRectangle);
				topCollision = getTopBounds().intersects(otherRectangle);
				leftCollision = getLeftBounds().intersects(otherRectangle);
				rightCollision = getRightBounds().intersects(otherRectangle);
				
				
				// Respond to the collision
				if (bottomCollision) {

					resolveBottomCollision(rectangle);
					
				}
				if (topCollision) {

					resolveTopCollision(rectangle);

				}
				if (leftCollision) {

					resolveLeftCollision(rectangle);
					
				} 
				if (rightCollision) {

					resolveRightCollision(rectangle);

				}
			}
			
		}
		
	}
	
	public void resolveTopCollision(Rectangle rectangle) {
		
		setyVelocity(getyVelocity() * -1);
		
	}
	
	public void resolveBottomCollision(Rectangle rectangle) {
		
		setyVelocity(getyVelocity() * -1);
		
	}
	
	public void resolveLeftCollision(Rectangle rectangle) {
		
		setxVelocity(getxVelocity() * -1);

	}
	
	public void resolveRightCollision(Rectangle rectangle) {
		
		setxVelocity(getxVelocity() * -1);
		
	}
	
	/**
	 * @return the xVelocity
	 */
	public double getxVelocity() {
		return xVelocity;
	}

	/**
	 * @param xVelocity the xVelocity to set
	 */
	public void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	/**
	 * @return the yVelocity
	 */
	public double getyVelocity() {
		return yVelocity;
	}

	/**
	 * @param yVelocity the yVelocity to set
	 */
	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

}
