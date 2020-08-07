
import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class Level {

	private PortalGame gameObject;
	private ArrayList<Rectangle> platforms;
	private ArrayList<Hazard> hazards; // NOTE may have to be ArrayList of Rectangles for collision checking purposes
	private ArrayList<Point2D> initialHazardPoints = new ArrayList<Point2D>();
	private ArrayList<Double> initialHazardXVelocities = new ArrayList<Double>();
	private ArrayList<Double> initialHazardYVelocities = new ArrayList<Double>();
	
	private Rectangle goal;
	private Point2D playerStartPoint;
	boolean playerTouchingGoal = false;
	boolean levelCompleted = false;
	
	public Level() {}
	
	/**
	 * Creates a Level with given values for the platforms, hazards, goal, playerStartPoint,
	 * and gameObject held by the Level
	 * @param platforms the platforms of the level
	 * @param hazards the Hazards of the Level
	 * @param goal the goal of the Level
	 */
	public Level(ArrayList<Rectangle> platforms, ArrayList<Hazard> hazards, Rectangle goal, Point2D playerStartPoint, PortalGame gameObject) {
		setPlatforms(platforms);
		setHazards(hazards);
		assignInitialHazardValues();
		setGoal(goal);
		setPlayerStartPoint(playerStartPoint);
		setGameObject(gameObject);
	}
	
	/**
	 * Draws the Level, including its Hazards, platforms, and goal
	 * @param gc the GraphicsContext of the Canvas to draw the Level on
	 */
	public void draw(GraphicsContext gc) {
		
		gc.save();

		for (Hazard hazard : getHazards()) {

			gc.setFill(hazard.getColor());
			hazard.draw(gc);

		}

		gc.setFill(getGoal().getFill());
		gc.fillRect(getGoal().getX(), getGoal().getY(), getGoal().getWidth(), getGoal().getHeight());

		for (Rectangle platform: getPlatforms()) {

			gc.setFill(platform.getFill());
			gc.fillRect(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());

		}

		gc.restore();
		
	}
	
	/**
	 * Updates the Hazards of the Level and checks if the Player is touching
	 * the goal
	 */
	public void update() {
		
		for (Hazard hazard : getHazards()) {
			
			hazard.update();
			
		}
		
		checkIfPlayerIsTouchingGoal();
		
		if (isPlayerTouchingGoal()) {
			
			getGameObject().getAnimator().stop();
			
			setLevelCompleted(true);
			
			getGameObject().getMenuManager().showLevelCompletedWindow();
			
		}
		
	}
	
	/**
	 * Resets the Hazards of the Level and sets the position of the Player to 
	 * the start point
	 */
	public void restartLevel() {
		
		resetHazards();
		getGameObject().getPlayer().reset(getPlayerStartPoint());
		
	}
	
	/**
	 * Sets the values of the ArrayLists which store information about the
	 * initial positions and velocities of the Hazards
	 */
	public void assignInitialHazardValues() {
		
		for (Hazard hazard : getHazards()) {
			
			getInitialHazardPoints().add(hazard.getCenterPoint());
			getInitialHazardXVelocities().add(hazard.getxVelocity());
			getInitialHazardYVelocities().add(hazard.getyVelocity());
			
		}
		
	}
	
	/**
	 * Resets the position and velocities of each Hazard to their initial values
	 */
	public void resetHazards() {
		
		for (int i = 0; i < getHazards().size(); i++) {
			
			getHazards().get(i).setCenterPoint(getInitialHazardPoints().get(i));
			getHazards().get(i).setxVelocity(getInitialHazardXVelocities().get(i));
			getHazards().get(i).setyVelocity(getInitialHazardYVelocities().get(i));
			
		}
		
	}
	
	/**
	 * Checks if the Player is touching the goal of the Level, and sets the 
	 * boolean playerTouchingGoal variable accordingly
	 */
	public void checkIfPlayerIsTouchingGoal() {
		
		if (getGameObject().getPlayer().getRectangle().intersects(getGoal().getLayoutBounds())) {
			
			setPlayerTouchingGoal(true);
			
		} else {
			
			setPlayerTouchingGoal(false);
			
		}
		
	}

	/**
	 * Gets the value of the playerTouchingGoal variable
	 * @return the playerTouchingGoal value
	 */
	public boolean isPlayerTouchingGoal() {
		return playerTouchingGoal;
	}

	/**
	 * Sets the value for the playerTouchingGoal variable
	 * @param playerTouchingGoal the new value for the playerTouchingGoal variable
	 */
	public void setPlayerTouchingGoal(boolean playerTouchingGoal) {
		this.playerTouchingGoal = playerTouchingGoal;
	}

	/**
	 * Gets the platforms of the Level
	 * @return the platforms ArrayList
	 */
	public ArrayList<Rectangle> getPlatforms() {
		return platforms;
	}
	
	/**
	 * Sets the platforms of the Level
	 * @param platforms the new platforms ArrayList
	 */
	public void setPlatforms(ArrayList<Rectangle> platforms) {
		this.platforms = platforms;
	}
	
	/**
	 * Gets the Hazards of the Level
	 * @return the ArrayList of Hazards
	 */
	public ArrayList<Hazard> getHazards() {
		return hazards;
	}
	
	/**
	 * Sets the Hazards of the Level
	 * @param hazards the new Hazards ArrayList
	 */
	public void setHazards(ArrayList<Hazard> hazards) {
		this.hazards = hazards;
	}

	/**
	 * Gets the initial center points of the Hazards
	 * @return the initial center points
	 */
	public ArrayList<Point2D> getInitialHazardPoints() {
		return initialHazardPoints;
	}

	/**
	 * Stores the initial Hazard center points
	 * @param initialHazardPoints the initial center points
	 */
	public void setInitialHazardPoints(ArrayList<Point2D> initialHazardPoints) {
		this.initialHazardPoints = initialHazardPoints;
	}

	/**
	 * Gets the initial x velocities of the Hazards of the Level
	 * @return the initial x velocities
	 */
	public ArrayList<Double> getInitialHazardXVelocities() {
		return initialHazardXVelocities;
	}

	/**
	 * Stores the initial x velocities of the Hazards of the Level
	 * @param initialHazardXVelocities the initial x velocities
	 */
	public void setInitialHazardXVelocities(ArrayList<Double> initialHazardXVelocities) {
		this.initialHazardXVelocities = initialHazardXVelocities;
	}

	/**
	 * Gets the initial y velocities of the Hazards of the Level
	 * @return the initial y velocities
	 */
	public ArrayList<Double> getInitialHazardYVelocities() {
		return initialHazardYVelocities;
	}

	/**
	 * Stores the initial y velocities of the Hazards of the Level
	 * @param initialHazardYVelocities the initial y velocities
	 */
	public void setInitialHazardYVelocities(ArrayList<Double> initialHazardYVelocities) {
		this.initialHazardYVelocities = initialHazardYVelocities;
	}

	/**
	 * Gets the goal of the Level
	 * @return the goal Rectangle
	 */
	public Rectangle getGoal() {
		return goal;
	}
	
	/**
	 * Sets the goal of the Level
	 * @param goal the goal to set
	 */
	public void setGoal(Rectangle goal) {
		this.goal = goal;
	}

	/**
	 * Gets the center point the Player starts the Level at
	 * @return the start point
	 */
	public Point2D getPlayerStartPoint() {
		return playerStartPoint;
	}

	/**
	 * Sets the center point for the Player to start the Level at
	 * @param playerStartPoint the playerStartPoint to set
	 */
	public void setPlayerStartPoint(Point2D playerStartPoint) {
		this.playerStartPoint = playerStartPoint;
	}

	/**
	 * Gets the value of the levelCompleted boolean
	 * @return the value of levelCompleted
	 */
	public boolean isLevelCompleted() {
		return levelCompleted;
	}

	/**
	 * Sets the value of the levelCompleted boolean
	 * @param levelCompleted the new value
	 */
	public void setLevelCompleted(boolean levelCompleted) {
		this.levelCompleted = levelCompleted;
	}

	/**
	 * Gets the PortalGame instance
	 * @return the instance
	 */
	public PortalGame getGameObject() {
		return gameObject;
	}

	/**
	 * Sets the PortalGame instance
	 * @param gameObject the new instance
	 */
	public void setGameObject(PortalGame gameObject) {
		this.gameObject = gameObject;
	}
	
	
	
}
