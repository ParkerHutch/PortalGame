import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Portal {

	String portalType = "A"; // Types are A and B
	Color color;
	boolean placedOnWall = false;
	double centerX;
	double centerY;
	double width = 100;
	double height = 4;
	double airborneRadius = 20;

	double velocity = 10; 
	double direction;

	String openingDirection = null;

	PortalGame gameObject; 

	double horizontalPortalWidth = 150;
	double verticalPortalWidth = 6;
	double horizontalPortalHeight = 6;
	double verticalPortalHeight = 150;


	public Portal() {}

	/**
	 * @param portalType
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param velocity
	 * @param direction direction of the velocity(in degrees)
	 */
	public Portal(String portalType, double centerX, double centerY, double width, double height, double velocity,
			double direction, PortalGame gameObject) {

		this.portalType = portalType;
		this.centerX = centerX;
		this.centerY = centerY;
		this.width = width;
		this.height = height;
		this.velocity = velocity;
		this.direction = direction;

		this.gameObject = gameObject;

		if (portalType.equals("A")) {

			setColor(Color.ORANGE);

		} else {

			setColor(Color.BLUE);

		}

	}

	/**
	 * Creates a Portal with a default velocity, width, and height
	 * @param portalType
	 * @param x
	 * @param y
	 * @param direction
	 */
	public Portal(String portalType, double x, double y, double direction, PortalGame gameObject) {

		this.portalType = portalType;
		this.centerX = x;
		this.centerY = y;
		this.direction = direction;

		this.gameObject = gameObject;

		if (portalType.equals("A")) {

			setColor(Color.ORANGE);

		} else {

			setColor(Color.BLUE);

		}

	}

	public void update() {

		if (velocity > 0) {

			setCenterX(getCenterX() + (Math.cos(Math.toRadians(getDirection()))) * getVelocity() * -1);
			setCenterY(getCenterY() + (Math.sin(Math.toRadians(getDirection()))) * getVelocity() * -1);

			checkCollisions(gameObject.getPlatformObjects());

		}

	}

	public void draw(GraphicsContext gc) {

		gc.setFill(getColor());

		if (velocity > 0) {

			gc.fillOval(getCenterX() - getAirborneRadius(), getCenterY() - getAirborneRadius(), getAirborneRadius() * 2, getAirborneRadius() * 2);

		} else {
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

	public void stickToWall(Rectangle rectangle) {

		setVelocity(0);
		setPlacedOnWall(true);

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

		return new Rectangle(getCenterX() - getWidth() / 2, getCenterY() - getHeight() / 2, getWidth(), getHeight());

	}

	/**
	 * @return the portalType
	 */
	public String getPortalType() {
		return portalType;
	}

	/**
	 * @param portalType the portalType to set
	 */
	public void setPortalType(String portalType) {
		this.portalType = portalType;
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
	 * @return the x
	 */
	public double getCenterX() {
		return centerX;
	}

	/**
	 * @param x the x to set
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
	 * @return the y
	 */
	public double getCenterY() {
		return centerY;
	}

	/**
	 * @param y the y to set
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
	 * @return the openingDirection
	 */
	public String getOpeningDirection() {
		return openingDirection;
	}

	/**
	 * @param openingDirection the openingDirection to set
	 */
	public void setOpeningDirection(String openingDirection) {
		this.openingDirection = openingDirection;
	}

}
