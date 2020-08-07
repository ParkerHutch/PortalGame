
import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Entity {
	
	private Color color;
	private double x;
	private double y;
	private double width;
	private double height;
	private double xVelocity;
	private double yVelocity;
	
	public Entity() {}
	
	/**
	 * Creates an Entity with a top-left point of the point given
	 * @param point the top-left point of the Entity
	 */
	public Entity(Point2D point) {
		
		setLocation(point);
		
	}
	
	/**
	 * Creates an Entity with a top-left point of (x, y), and a defined width,
	 * height, and color
	 * @param x the left x coordinate of the Entity
	 * @param y the top y coordinate of the Entity
	 * @param width the width of the Entity
	 * @param height the height of the Entity
	 * @param color the color of the Entity
	 */
	public Entity(double x, double y, double width, double height, Color color) {
		
		setLocation(x, y);
		setWidth(width);
		setHeight(height);
		setColor(color);
		
	}
	
	/**
	 * Draws the Entity on a canvas
	 * @param gc the GraphicsContext of the Canvas to draw the Entity on
	 */
	public void draw(GraphicsContext gc) {
		
		gc.save();
		gc.setFill(getColor());
		gc.fillRect(getX(), getY(), getWidth(), getHeight());
		gc.restore();
		
	}
	
	/**
	 * Gets the left x coordinate of the Entity
	 * @return the left x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the left x coordinate of the Entity
	 * @param x the new left x coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the top y coordinate of the Entity
	 * @return the top y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the top y coordinate of the Entity
	 * @param y the new top y coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Gets the center x coordinate of the Entity
	 * @return the center x coordinate
	 */
	public double getCenterX() {

		return (getX() + getWidth() / 2);

	}

	/**
	 * Gets the center y coordinate of the Entity
	 * @return the center y coordinate
	 */
	public double getCenterY() {

		return (getY() + getHeight() / 2);

	}
	
	/**
	 * Gets the center point (x, y) of the Entity as a Point2D object
	 * @return the center point of the Entity
	 */
	public Point2D getCenterPoint() {

		return new Point2D(getCenterX(), getCenterY());

	}
	
	/**
	 * Sets the center point of the Entity to the point given
	 * @param point the new center point of the Entity
	 */
	public void setCenterPoint(Point2D point) {
		
		setX(point.getX() - (getWidth() / 2));
		setY(point.getY() - (getHeight() / 2));
		
	}
	
	/**
	 * Sets the top left point x and y coordinates of the Entity
	 * @param x the new left x coordinate of the Entity
	 * @param y the new top y coordinate of the Entity
	 */
	public void setLocation(double x, double y) {

		setX(x);
		setY(y);

	}
	
	/**
	 * Sets the top left point of the Entity
	 * @param point the new top left point of the Entity
	 */
	public void setLocation(Point2D point) {

		setX(point.getX());
		setY(point.getY());

	}

	/**
	 * Gets the width of the Entity
	 * @return the width of the Entity
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * Sets the width of the Entity
	 * @param width the new width of the Entity
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Gets the height of the Entity
	 * @return the height of the Entity
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Sets the Height of the Entity
	 * @param height the new height of the Entity
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
	 * Gets a Rectangle object which contains the dimensions of the Entity
	 * @return a Rectangle representation of the Entity
	 */
	public Rectangle getRectangle() {

		return new Rectangle(getX(), getY(), getWidth(), getHeight());

	}

	/**
	 * Gets a Rectangle representing a portion of the top side of the Entity's Rectangle, in which
	 * the left and right corners of the top of the Entity's Rectangle have been removed for 
	 * collision handling purposes
	 * @return a Rectangle representing the top collision area of the Entity
	 */
	public Rectangle getTopBounds() {
		return new Rectangle(getX() + getWidth() / 4, getY(), getWidth() / 2, getHeight() / 8);
	}

	/**
	 * Gets a Rectangle representing a portion of the right side of the Entity's Rectangle, in which
	 * the top and bottom corners of the right side of the Entity's Rectangle have been removed for 
	 * collision handling purposes
	 * @return a Rectangle representing the right collision area of the Entity
	 */
	public Rectangle getRightBounds() {
		return new Rectangle(getX() + (5 * getWidth()) / 6, getY() + getHeight() / 8, getWidth() / 6,
				(6 * getHeight() / 8));
	}

	/**
	 * Gets a Rectangle representing a portion of the left side of the Entity's Rectangle, in which
	 * the top and bottom corners of the left side of the Entity's Rectangle have been removed for 
	 * collision handling purposes
	 * @return a Rectangle representing the left collision area of the Entity
	 */
	public Rectangle getLeftBounds() {
		// old: return new Rectangle(getX(), getY() + getHeight() / 8, getWidth() / 8, (6 * getHeight() / 8));
		return new Rectangle(getX(), getY() + getHeight() / 8, getWidth() / 6, (6 * getHeight() / 8));
	}

	/**
	 * Gets a Rectangle representing a portion of the bottom side of the Entity's Rectangle, in which
	 * the left and right corners of the bottom side of the Entity's Rectangle have been removed for 
	 * collision handling purposes
	 * @return a Rectangle representing the bottom collision area of the Entity
	 */
	public Rectangle getBottomBounds() {
		return new Rectangle(getX() + getWidth() / 4, getY() + (7 * getHeight() / 8), getWidth() / 2,
				(getHeight() / 8) + 0);
	}
	
	/**
	 * Checks for collisions(intersections) between the Entity and a
	 * given ArrayList of Rectangle objects, which represent platforms
	 * or boundaries in the game. If a collision is occurring, this method 
	 * determines what type of collision it is by determining what 
	 * part of the Entity is colliding with another object, and then calls 
	 * an appropriate method to resolve the collision.
	 * @param rectangles an ArrayList of Rectangles representing game objects
	 */
	public void checkCollisions(ArrayList<Rectangle> rectangles) {

		boolean topCollision;
		boolean bottomCollision;
		boolean leftCollision;
		boolean rightCollision;

		for (Rectangle rectangle : rectangles) {

			// If the rectangle intersects the Entity somewhere
			if (getRectangle().intersects(rectangle.getLayoutBounds())) {

				Bounds otherRectangle = rectangle.getLayoutBounds();

				// Determine what kind of collision is occurring
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

	/**
	 * Determines how the Entity should react to a collision on its top side
	 * @param rectangle the Rectangle which collided with the top side of the Entity
	 */
	public abstract void resolveTopCollision(Rectangle rectangle);

	/**
	 * Determines how the Entity should react to a collision on its bottom side
	 * @param rectangle the Rectangle which collided with the bottom of the Entity
	 */
	public abstract void resolveBottomCollision(Rectangle rectangle);

	/**
	 * Determines how the Entity should react to a collision on its left side
	 * @param rectangle the Rectangle which collided with the left side of the Entity
	 */
	public abstract void resolveLeftCollision(Rectangle rectangle);

	/**
	 * Determines how the Entity should react to a collision on its right side
	 * @param rectangle the Rectangle which collided with the right side of the Entity
	 */
	public abstract void resolveRightCollision(Rectangle rectangle);
	
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

	/**
	 * Sets the x and y velocities of the Player to 0
	 */
	public void stopVelocities() {
		
		setxVelocity(0);
		setyVelocity(0);
		
	}
	
	/**
	 * Updates the Entity's x and y coordinates by adding the x and y velocities
	 * of the Entity to their respective coordinates
	 */
	public void applyVelocities() {
		
		setX(getX() + getxVelocity());
		setY(getY() + getyVelocity());
		
	}
	
	

}
