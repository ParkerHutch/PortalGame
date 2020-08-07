
import java.util.ArrayList;
import java.util.Collections;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LevelManager {
	
	private Level [] levels;
	
	private int currentLevelIndex;
	
	private PortalGame gameObject;
	
	public LevelManager() {}
	
	/**
	 * Creates a LevelManager for a given PortalGame
	 * @param gameObject the PortalGame instance
	 */
	public LevelManager(PortalGame gameObject) {
		
		setGameObject(gameObject);
		
		generateLevels();
		
	}
	
	/**
	 * Updates and draws the current level
	 * @param gc the GraphicsContext of the Canvas to draw the Level on
	 */
	public void update(GraphicsContext gc) {
		
		updateLevel();
		drawCurrentLevel(gc);

	}

	/**
	 * Updates the current Level
	 */
	public void updateLevel() {

		getCurrentLevel().update();

	}
	
	/**
	 * Draws the current Level
	 * @param gc the GraphicsContext of the Canvas to draw the Level on
	 */
	public void drawCurrentLevel(GraphicsContext gc) {
		
		gc.save();
		
		gc.setFill(Color.BLACK);
		
		getCurrentLevel().draw(gc);
		
		gc.restore();
		
	}
	
	/**
	 * Switches the current Level to the Level at the given index of the
	 * "levels" array in this LevelManager
	 * @param levelIndex the index of the Level in the levels array
	 */
	public void switchLevel(int levelIndex) {
		
		setCurrentLevelIndex(levelIndex);
		getGameObject().getPlayer().reset(getCurrentLevel().getPlayerStartPoint());
		getGameObject().getAnimator().start();
		getGameObject().getStage().setScene(getGameObject().getGameScene());
		getCurrentLevel().restartLevel();
		
	}
	
	/**
	 * Gets the Level at the currentLevelIndex of the levels array
	 * @return the current Level
	 */
	public Level getCurrentLevel() {
		
		return getLevels()[getCurrentLevelIndex()];
		
	}
	
	/**
	 * Gets the index(in the levels array) of the current Level
	 * @return the current level's index
	 */
	public int getCurrentLevelIndex() {
		return currentLevelIndex;
	}

	/**
	 * Sets the index(in the levels array) of the current level
	 * @param currentLevelIndex the new current level index
	 */
	public void setCurrentLevelIndex(int currentLevelIndex) {
		this.currentLevelIndex = currentLevelIndex;
	}

	/**
	 * Instantiate the Levels in the levels array
	 */
	public void generateLevels() {
		
		setLevels(new Level [] {
			
			getLevel1(), 
			getLevel2(),
			getLevel3(),
			getLevel4(),
			getLevel5(),
			getLevel6(),
			getLevel7(),
			getLevel8()
			
			
		});
		
	}
	
	/**
	 * Gets the array of Level objects
	 * @return the levels array
	 */
	public Level[] getLevels() {
		return levels;
	}

	/**
	 * Sets the levels array
	 * @param levels the new levels array
	 */
	public void setLevels(Level[] levels) {
		this.levels = levels;
	}

	/**
	 * Creates the Level object for Level 1
	 * @return the Level object for Level 1
	 */
	public Level getLevel1() {
		
		ArrayList<Rectangle> platforms = new ArrayList<>();

		Rectangle floor = new Rectangle(0, gameObject.getHeight() - 50, gameObject.getWidth(), 50);
		Rectangle leftWall = new Rectangle(0, 0, 50, gameObject.getHeight());
		Rectangle rightWall = new Rectangle(gameObject.getWidth() - 50, 0, 50, gameObject.getHeight());
		Rectangle ceiling = new Rectangle(0, 0, gameObject.getWidth(), 50);

		Collections.addAll(platforms, floor, leftWall, rightWall, ceiling);

		ArrayList<Hazard> hazards = new ArrayList<>();

		Rectangle goal = new Rectangle(rightWall.getX() - 50, floor.getY() - 80, 50, 80);
		goal.setFill(Color.GREEN);

		Point2D playerStartPoint = new Point2D(leftWall.getX() + leftWall.getWidth() + 100, getGameObject().getHeight() / 2);

		Level level1 = new Level(platforms, hazards, goal, playerStartPoint, getGameObject());

		return level1;
		
		
	}
	
	/** 
	 * Creates the Level object for Level 2
	 * @return the Level object for Level 2
	 */
	public Level getLevel2() {

		ArrayList<Rectangle> platforms = new ArrayList<>();

		Rectangle floor = new Rectangle(0, gameObject.getHeight() - 50, gameObject.getWidth(), 50);
		Rectangle leftWall = new Rectangle(0, 0, 50, gameObject.getHeight());
		Rectangle rightWall = new Rectangle(gameObject.getWidth() - 50, 0, 50, gameObject.getHeight());
		Rectangle ceiling = new Rectangle(0, 0, gameObject.getWidth(), 50);
		Rectangle goalPlatform = new Rectangle(rightWall.getX() - 200, ceiling.getY() + ceiling.getHeight() + 200, 200, 20);
		
		Collections.addAll(platforms, floor, leftWall, rightWall, goalPlatform, ceiling);
		
		ArrayList<Hazard> hazards = new ArrayList<>();

		Rectangle goal = new Rectangle(rightWall.getX() - 50, goalPlatform.getY() - 80, 50, 80);
		goal.setFill(Color.GREEN);

		Point2D playerStartPoint = new Point2D(leftWall.getX() + leftWall.getWidth() + 100, floor.getY() - 80);

		Level level2 = new Level(platforms, hazards, goal, playerStartPoint, getGameObject());

		return level2;

	}
	
	/** 
	 * Creates the Level object for Level 3
	 * @return the Level object for Level 3
	 */
	public Level getLevel3() {

		ArrayList<Rectangle> platforms = new ArrayList<>();

		Rectangle leftFloor = new Rectangle(0, gameObject.getHeight() - 100, gameObject.getWidth() / 4, 100);
		Rectangle rightFloor = new Rectangle((gameObject.getWidth() * 3) / 4, gameObject.getHeight() - 100, gameObject.getWidth() / 4, 100);
		Rectangle leftWall = new Rectangle(0, 0, 50, gameObject.getHeight());
		Rectangle rightWall = new Rectangle(gameObject.getWidth() - 50, 0, 50, gameObject.getHeight());
		Rectangle ceiling = new Rectangle(0, 0, gameObject.getWidth(), 50);
		
		Collections.addAll(platforms, leftFloor, rightFloor, leftWall, rightWall, ceiling);

		ArrayList<Hazard> hazards = new ArrayList<>();
		Hazard hazard = new Hazard(leftFloor.getX() + leftFloor.getWidth(), getGameObject().getHeight() - 40, 
				rightFloor.getX() - (leftFloor.getX() + leftFloor.getWidth()), 40, Color.RED, getGameObject());
		hazards.add(hazard);
				
		Rectangle goal = new Rectangle(rightWall.getX() - 50, rightFloor.getY() - 80, 50, 80);
		goal.setFill(Color.GREEN);

		Point2D playerStartPoint = new Point2D(leftWall.getX() + 60, leftFloor.getY() - 50);

		Level level3 = new Level(platforms, hazards, goal, playerStartPoint, getGameObject());

		return level3;
		
	}
	
	/** 
	 * Creates the Level object for Level 4
	 * @return the Level object for Level 4
	 */
	public Level getLevel4() {

		ArrayList<Rectangle> platforms = new ArrayList<>();
		
		Rectangle floor = new Rectangle(0, gameObject.getHeight() - 50, gameObject.getWidth(), 50);
		Rectangle leftWall = new Rectangle(0, 0, 50, gameObject.getHeight());
		Rectangle rightWall = new Rectangle(gameObject.getWidth() - 50, 0, 50, gameObject.getHeight());
		Rectangle ceiling = new Rectangle(0, 0, gameObject.getWidth(), 50);
		
		Collections.addAll(platforms, floor, leftWall, rightWall, ceiling);
		
		ArrayList<Hazard> hazards = new ArrayList<>();
		
		Hazard testHazard = new Hazard(rightWall.getX() - 200, floor.getY() - 300, 20, 300, Color.RED, getGameObject());
		testHazard.setxVelocity(-2);
		hazards.add(testHazard);

		Rectangle goal = new Rectangle(rightWall.getX() - 50, floor.getY() - 80, 50, 80);
		goal.setFill(Color.GREEN);

		Point2D playerStartPoint = new Point2D(leftWall.getX() + leftWall.getWidth() + 70, floor.getY() - 80);

		Level level4 = new Level(platforms, hazards, goal, playerStartPoint, getGameObject());

		return level4;
		
		
	}
	
	/** 
	 * Creates the Level object for Level 5
	 * @return the Level object for Level 5
	 */
	public Level getLevel5() {

		ArrayList<Rectangle> platforms = new ArrayList<>();
		
		Rectangle leftWall = new Rectangle(0, 0, 50, gameObject.getHeight());
		Rectangle rightWall = new Rectangle(gameObject.getWidth() - 50, 0, 50, gameObject.getHeight());
		Rectangle leftFloor = new Rectangle(leftWall.getX() + leftWall.getWidth(), gameObject.getHeight() - 100, gameObject.getWidth() / 4, 100);
		Rectangle rightFloor = new Rectangle((gameObject.getWidth() * 3) / 4, gameObject.getHeight() - 100, gameObject.getWidth() / 4, 100);
		Rectangle ceiling = new Rectangle(0, 0, gameObject.getWidth(), 50);
		Rectangle rightFloorBar = new Rectangle(rightFloor.getX(), rightFloor.getY() - 100, 10, 100);
		
		Collections.addAll(platforms, leftFloor, rightFloor, leftWall, rightWall, ceiling, rightFloorBar);

		ArrayList<Hazard> hazards = new ArrayList<>();
		
		Hazard floorHazard = new Hazard(leftFloor.getX() + leftFloor.getWidth(), getGameObject().getHeight() - 40, 
				rightFloor.getX() - (leftFloor.getX() + leftFloor.getWidth()), 40, Color.RED, getGameObject());
		
		Hazard leftCeilingHazard = new Hazard(leftWall.getX() + leftWall.getWidth(),
				ceiling.getY() + ceiling.getHeight() + 50, leftFloor.getWidth(), 50, 
				Color.RED, getGameObject());
		leftCeilingHazard.setyVelocity(0.85);
		
		hazards.add(floorHazard);
		hazards.add(leftCeilingHazard);
				
		Rectangle goal = new Rectangle(rightWall.getX() - 50, rightFloor.getY() - 80, 50, 80);
		goal.setFill(Color.GREEN);

		Point2D playerStartPoint = new Point2D(leftWall.getX() + 60, leftFloor.getY() - 50);

		Level level5 = new Level(platforms, hazards, goal, playerStartPoint, getGameObject());

		return level5;
		
	}
	
	/** 
	 * Creates the Level object for Level 6
	 * @return the Level object for Level 6
	 */
	public Level getLevel6() {

		ArrayList<Rectangle> platforms = new ArrayList<>();

		Rectangle leftWall = new Rectangle(0, 0, 50, gameObject.getHeight());
		Rectangle rightWall = new Rectangle(gameObject.getWidth() - 50, 0, 50, gameObject.getHeight());
		Rectangle floor = new Rectangle(0, gameObject.getHeight() - 50, gameObject.getWidth(), 50);
		Rectangle ceiling = new Rectangle(0, 0, gameObject.getWidth(), 50);
		Rectangle tower = new Rectangle(leftWall.getX() + leftWall.getWidth() + 200, 
				ceiling.getY() + ceiling.getHeight() + 80, 40, floor.getY() - (ceiling.getY() + ceiling.getHeight()));
		Rectangle towerLanding = new Rectangle(tower.getX() + tower.getWidth(), tower.getY() + 120,
				200, tower.getHeight() - 120);
		Rectangle ceilingBar = new Rectangle(towerLanding.getX() + towerLanding.getWidth() - 20, 
				ceiling.getY() + ceiling.getHeight(), 20, 85);
		Rectangle towerLandingBar = new Rectangle(ceilingBar.getX(), towerLanding.getY() - ceilingBar.getHeight(),
				ceilingBar.getWidth(), ceilingBar.getHeight());
		Collections.addAll(platforms, floor, leftWall, rightWall, ceiling, tower, towerLanding, ceilingBar, towerLandingBar);
		
		ArrayList<Hazard> hazards = new ArrayList<>();

		Rectangle goal = new Rectangle(rightWall.getX() - 50, floor.getY() - 80, 50, 80);
		goal.setFill(Color.GREEN);

		Point2D playerStartPoint = new Point2D(leftWall.getX() + 60, floor.getY() - 50);

		Level level6 = new Level(platforms, hazards, goal, playerStartPoint, getGameObject());

		return level6;
		
	}
	
	/** 
	 * Creates the Level object for Level 7
	 * @return the Level object for Level 7
	 */
	public Level getLevel7() {

		ArrayList<Rectangle> platforms = new ArrayList<>();

		Rectangle leftWall = new Rectangle(0, 0, 50, gameObject.getHeight());
		Rectangle rightWall = new Rectangle(gameObject.getWidth() - 50, 0, 50, gameObject.getHeight());
		Rectangle floor = new Rectangle(0, gameObject.getHeight() - 50, gameObject.getWidth(), 50);
		Rectangle leftPlatform = new Rectangle(leftWall.getX() + leftWall.getWidth(), floor.getY() - 300, 200, 300);
		Rectangle ceiling = new Rectangle(0, 0, gameObject.getWidth(), 50);
		Collections.addAll(platforms, floor, leftPlatform, leftWall, rightWall, ceiling);
		
		Rectangle goal = new Rectangle(rightWall.getX() - 400, ceiling.getY() + ceiling.getHeight(), 120, 100);
		goal.setFill(Color.GREEN);
		
		ArrayList<Hazard> hazards = new ArrayList<>();
		Hazard leftGoalHazard = new Hazard(goal.getX() - 10, ceiling.getY() + ceiling.getHeight(), 10, 500, Color.RED, getGameObject());
		Hazard rightGoalHazard = new Hazard(goal.getX() + goal.getWidth(), leftGoalHazard.getY(), 
				leftGoalHazard.getWidth(), leftGoalHazard.getHeight(), Color.RED, getGameObject());
		hazards.add(leftGoalHazard);
		hazards.add(rightGoalHazard);
		Point2D playerStartPoint = new Point2D(leftWall.getX() + 60, leftPlatform.getY() - 50);

		Level level7 = new Level(platforms, hazards, goal, playerStartPoint, getGameObject());

		return level7;
		
	}
	
	/** 
	 * Creates the Level object for Level 8
	 * @return the Level object for Level 8
	 */
	public Level getLevel8() {

		ArrayList<Rectangle> platforms = new ArrayList<>();

		Rectangle leftWall = new Rectangle(0, 0, 50, gameObject.getHeight());
		Rectangle rightWall = new Rectangle(gameObject.getWidth() - 50, 0, 50, gameObject.getHeight());
		Rectangle floor = new Rectangle(0, gameObject.getHeight() - 50, gameObject.getWidth(), 50);
		Rectangle leftPlatform = new Rectangle(leftWall.getX() + leftWall.getWidth(), floor.getY() - 300, 200, 300);
		Rectangle rightPlatform = new Rectangle(rightWall.getX() - 200, floor.getY() - 300, 200, 300);
		Rectangle rightPlatformBlocker = new Rectangle(rightPlatform.getX(), rightWall.getY() - 40, 20, 40);
		Rectangle ceiling = new Rectangle(0, 0, gameObject.getWidth(), 50);
		Collections.addAll(platforms, floor, leftPlatform, rightPlatform, rightPlatformBlocker, leftWall, rightWall, ceiling);
		
		Rectangle goal = new Rectangle(rightWall.getX() - 50, rightPlatform.getY() - 80, 50, 80);
		goal.setFill(Color.GREEN);
		
		ArrayList<Hazard> hazards = new ArrayList<>();
		
		Point2D playerStartPoint = new Point2D(leftWall.getX() + 60, leftPlatform.getY() - 50);

		Level level8 = new Level(platforms, hazards, goal, playerStartPoint, getGameObject());

		return level8;
		
	}
	
	/**
	 * Gets the PortalGame instance
	 * @return the PortalGame instance
	 */
	public PortalGame getGameObject() {
		return gameObject;
	}

	/**
	 * Sets the PortalGame instance
	 * @param gameObject the new PortalGame instance
	 */
	public void setGameObject(PortalGame gameObject) {
		this.gameObject = gameObject;
	}
	
}
