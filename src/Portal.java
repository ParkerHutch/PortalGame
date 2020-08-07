
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Portal {

	private String portalType = "A"; // Types are A and B
	private Color color;
	private boolean placedOnWall = false;
	private double centerX;
	private double centerY;
	private double width = 100;
	private double height = 4;
	private double airborneRadius = 20;

	private double velocity = 10; 
	private double direction;

	private String openingDirection = null;

	private PortalGame gameObject; 

	private double horizontalPortalWidth = 120;
	private double verticalPortalWidth = 10;
	private double horizontalPortalHeight = verticalPortalWidth;
	private double verticalPortalHeight = horizontalPortalWidth;


	public Portal() {}

	/**
	 * Creates a portal of the given portal type centered at the given point (centerX, centerY) 
	 * and a given width, height, velocity, and velocity direction. 
	 * @param portalType used to determine the Portal's color and differentiate between the two
	 * portals the Player can use
	 * @param centerX the center x coordinate of the Portal
	 * @param centerY the center y coordinate of the Portal
	 * @param width the width of the Portal
	 * @param height the height of the Portal
	 * @param velocity the magnitude of the Portal's velocity
	 * @param direction the direction of the Portal's velocity(in degrees)
	 */
	public Portal(String portalType, double centerX, double centerY, double width, double height, double velocity,
			double direction, PortalGame gameObject) {

		setPortalType(portalType);
		setCenterX(centerX);
		setCenterY(centerY);
		setWidth(width);
		setHeight(height);
		setVelocity(velocity);
		setDirection(direction);

		this.gameObject = gameObject;

		if (getPortalType().equals("A")) {

			setColor(Color.ORANGE);

		} else {

			setColor(Color.BLUE);

		}

	}

	/**
	 * Creates a Portal centered on (centerX, centerY) with a default velocity, width, and height
	 * @param portalType used to determine the Portal's color and differentiate between the two
	 * portals the Player can use
	 * @param centerX the center x coordinate of the Portal
	 * @param centerY the center y coordinate of the Portal
	 * @param direction the direction of the Portal's velocity
	 */
	public Portal(String portalType, double centerX, double centerY, double direction, PortalGame gameObject) {

		setPortalType(portalType);
		setCenterX(centerX);
		setCenterY(centerY);
		setDirection(direction);

		this.gameObject = gameObject;

		if (getPortalType().equals("A")) {

			setColor(Color.ORANGE);

		} else {

			setColor(Color.BLUE);

		}

	}

	/**
	 * Updates the Portal's position if it is airborne, and checks if the Portal
	 * is colliding with any of the platforms in the current level.
	 */
	public void update() {

		if (getVelocity() > 0) {

			setCenterX(getCenterX() + (Math.cos(Math.toRadians(getDirection()))) * getVelocity() * -1);
			setCenterY(getCenterY() + (Math.sin(Math.toRadians(getDirection()))) * getVelocity() * -1);

			checkCollisions(gameObject.getLevelManager().getCurrentLevel().getPlatforms());

		}

	}

	/**
	 * Draws the Portal. If the Portal is airborne, a Circle is drawn, and if
	 * the Portal is stationary a Rectangle is drawn. 
	 * @param gc the GraphicsContext of the Canvas to draw the Portal on
	 */
	public void draw(GraphicsContext gc) {

		gc.setFill(getColor());

		if (velocity > 0) {

			gc.fillOval(getCenterX() - getAirborneRadius(), getCenterY() - getAirborneRadius(), getAirborneRadius() * 2, getAirborneRadius() * 2);

		} else if (isPlacedOnWall()) {
			gc.fillRect(getCenterX() - getWidth() / 2, getCenterY() - getHeight() / 2, getWidth(), getHeight());
		}

	}

	/**
	 * Checks for a collision between the airborne Portal object and one of the game platforms
	 * @param rectangles the platform objects in the game
	 * @return true if there is a collision, false otherwise
	 */
	public void checkCollisions(ArrayList<Rectangle> rectangles) {

		for (Rectangle rectangle : rectangles) {

			Circle centerPoint = new Circle(getCenterX(), getCenterY(), 1);

			if (centerPoint.intersects(rectangle.getLayoutBounds())) {

				stickToWall(rectangle);

			}

		}

	}

	/**
	 * Adjusts the position, dimensions, and velocity of the Portal
	 * so that it will be stationary on the wall it collided with
	 * @param rectangle the Rectangle of the wall to stick the Portal on
	 */
	public void stickToWall(Rectangle rectangle) {

		setVelocity(0);

		double leftVerticalX = rectangle.getX();
		double rightVerticalX = rectangle.getX() + rectangle.getWidth();

		double topHorizontalY = rectangle.getY();
		double bottomHorizontalY = rectangle.getY() + rectangle.getHeight();

		double distanceToLeftVertical = Math.abs(getCenterX() - leftVerticalX);
		double distanceToRightVertical = Math.abs(getCenterX() - rightVerticalX);

		double shortestDistanceToVerticalSide = Math.min(distanceToLeftVertical, distanceToRightVertical);

		double distanceToTopHorizontal = Math.abs(getCenterY() - topHorizontalY);
		double distanceToBottomHorizontal = Math.abs(getCenterY() - bottomHorizontalY);

		double shortestDistanceToHorizontalSide = Math.min(distanceToTopHorizontal, distanceToBottomHorizontal);

		if (shortestDistanceToVerticalSide < shortestDistanceToHorizontalSide) {

			// If the portal collided with one of the vertical(left or right) sides of the platform
			if (distanceToLeftVertical < distanceToRightVertical) {

				setCenterX(leftVerticalX);
				setOpeningDirection("LEFT");


			} else {

				setCenterX(rightVerticalX);
				setOpeningDirection("RIGHT");

			}


		} else {

			// If the portal collided with one of the horizontal(top or bottom) sides of the platform

			if (distanceToTopHorizontal < distanceToBottomHorizontal) {

				setCenterY(topHorizontalY);
				setOpeningDirection("UP");

			} else {

				setCenterY(bottomHorizontalY);
				setOpeningDirection("DOWN");

			}
		}

		adjustSize();
		
		boolean portalFitsOnPlatform = true;
		boolean isHorizontalPortal = getOpeningDirection().equals("UP") || getOpeningDirection().equals("DOWN");
		
		// Check if the Portal fits on the platform
		if (isHorizontalPortal) {
			
			if (getLeftX() < rectangle.getX() || getRightX() > rectangle.getX() + rectangle.getWidth()) {
				
				portalFitsOnPlatform = false;
				
			}
			
		} else {
			
			if (getTopY() < rectangle.getY() || getBottomY() > rectangle.getY() + rectangle.getHeight()) {
				
				portalFitsOnPlatform = false;
				
			}
			
			
		}
		
		if (portalFitsOnPlatform) {
			
			setPlacedOnWall(true);
			
		} else {
			
			setPlacedOnWall(false);
			
		}
		
		

	}

	/**
	 * Adjusts the width and height of the rectangle to match its opening orientation
	 * Example: A portal with a "DOWN" opening orientation should have a longer width
	 * than height since it is flat
	 */
	public void adjustSize() {

		if (getOpeningDirection().equals("UP") || getOpeningDirection().equals("DOWN")) {

			setWidth(horizontalPortalWidth);
			setHeight(horizontalPortalHeight);

		} else if (getOpeningDirection().equals("LEFT") || getOpeningDirection().equals("RIGHT")) {

			setWidth(verticalPortalWidth);
			setHeight(verticalPortalHeight);

		}

	}

	public Rectangle getRectangle() {

		return new Rectangle(getLeftX(), getTopY(), getWidth(), getHeight());

	}

	/**
	 * Gets the type of the Portal(A or B)
	 * @return the type of the Portal
	 */
	public String getPortalType() {
		return portalType;
	}

	/**
	 * Sets the type of the Portal(A or B)
	 * @param portalType the new type
	 */
	public void setPortalType(String portalType) {
		this.portalType = portalType;
	}

	/**
	 * Gets the Color of the Portal
	 * @return the color of the Portal
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets a the Color of the Portal
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Gets the center x coordinate of the Portal
	 * @return the center x coordinate
	 */
	public double getCenterX() {
		return centerX;
	}

	/**
	 * Sets the center x coordinate of the Portal
	 * @param x the new center x coordinate
	 */
	public void setCenterX(double x) {
		this.centerX = x;
	}

	/**
	 * Gets the left x coordinate of the Portal
	 * @return the left x coordinate
	 */
	public double getLeftX() {

		return getCenterX() - getWidth() / 2;

	}

	/**
	 * Gets the right x coordinate of the Portal
	 * @return the right x coordinate
	 */
	public double getRightX() {

		return getCenterX() + getWidth() / 2;

	}

	/**
	 * Gets the center y coordinate of the Portal
	 * @return the center y coordinate of the Portal
	 */
	public double getCenterY() {
		return centerY;
	}

	/**
	 * Sets the center y coordinate of the Portal
	 * @param y the center y coordinate
	 */
	public void setCenterY(double y) {
		this.centerY = y;
	}

	/**
	 * Gets the top y coordinate of the Portal
	 * @return the top y coordinate
	 */
	public double getTopY() {

		return getCenterY() - getHeight() / 2;

	}

	/**
	 * Gets the bottom y coordinate of the Portal
	 * @return the bottom y coordinate
	 */
	public double getBottomY() {

		return getCenterY() + getHeight() / 2;

	}

	/**
	 * Gets the width of the Portal
	 * @return the width of the Portal
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Sets the width of the Portal
	 * @param width the new width
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/** Gets the height of the Portal
	 * @return the height of the Portal
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Sets the height of the Portal
	 * @param height the new height
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * @return the airborneRadius
	 */
	public double getAirborneRadius() {
		return airborneRadius;
	}

	/**
	 * @param airborneRadius the airborneRadius to set
	 */
	public void setAirborneRadius(double airborneRadius) {
		this.airborneRadius = airborneRadius;
	}

	/**
	 * @return the velocity
	 */
	public double getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	/**
	 * Gets the direction of the Portal's velocity
	 * @return the direction
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(double direction) {
		this.direction = direction;
	}

	/**
	 * @return the placedOnWall
	 */
	public boolean isPlacedOnWall() {
		return placedOnWall;
	}

	/**
	 * @param placedOnWall the placedOnWall to set
	 */
	public void setPlacedOnWall(boolean placedOnWall) {
		this.placedOnWall = placedOnWall;
	}

	/** 
	 * Gets the direction that the Portal opens to(where the Player exits)
	 * @return the opening direction
	 */
	public String getOpeningDirection() {
		return openingDirection;
	}

	/**
	 * Sets the direction of the Portal's opening
	 * @param openingDirection the new opening direction
	 */
	public void setOpeningDirection(String openingDirection) {
		this.openingDirection = openingDirection;
	}

}
