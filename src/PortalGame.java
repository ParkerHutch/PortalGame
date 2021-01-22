
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*
 * IDEAS/TODO
 * 
 * - Private variables
 * - Making the left/right hitboxes bigger in the Player getBounds methods seemed to help
 * 		the computer detect more collisions. I think I have more unused space to expand
 * 		to(where boxes don't overlap) - maybe make them even bigger
 * - Should probably set a max velocity
 * 		- ex. in the player update method, if a velocity is greater than the max(ex. 30),
 * 			just set it to that max
 * 
 */
public class PortalGame extends Application {
	
	// Dimensions
	private int WIDTH = 700;
	private int HEIGHT = 600;

	// Useful global variables
	private Group root;
	private Stage stage;
	private Canvas canvas;
	private Scene gameScene;

	private AnimationTimer animator;

	private GraphicsContext gc;
	
	private Player player;
	
	private LevelManager levelManager;
	private MenuManager menuManager;
	
	public static void main(String[] args) {
		launch(args);
	}

	public void init() throws Exception {
		
		player = new Player(WIDTH / 2, HEIGHT / 2, 40, 40, Color.AQUA, this);
		player.setxVelocity(0);
		player.setyVelocity(0);
		
		animator = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				
				tickGame();
				
			}
		}; // animator is started by MenuManager
		
		//animator.start();
		
		levelManager = new LevelManager(this);
		menuManager = new MenuManager(this);
		
	}

	public void start(Stage stage) throws Exception {
		
		setStage(stage);
		prepareWindow();	
		
		menuManager.showMainMenu();
		
		
	}
	
	/**
	 * Updates objects involved in the game
	 */
	public void tickGame() {
		
		// NOTE: Game loop
		
		gc.clearRect(0, 0, WIDTH, HEIGHT); // clear the screen

		// draw the objects
		player.update(gc);
		
		levelManager.update(gc);
		
		
	}

	/**
	 * Gets the width of the window
	 * @return the width of the window
	 */
	public int getWidth() {
		return WIDTH;
	}

	/**
	 * Gets the height of the window
	 * @return the height
	 */
	public int getHeight() {
		return HEIGHT;
	}

	
	/**
	 * Gets the Stage
	 * @return the stage
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * Sets the Stage
	 * @param stage the new stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Gets the Scene used when the player is playing the game
	 * @return the gameScene
	 */
	public Scene getGameScene() {
		return gameScene;
	}

	/**
	 * Sets the gameScene
	 * @param gameScene the gameScene to set
	 */
	public void setGameScene(Scene gameScene) {
		this.gameScene = gameScene;
	}

	/**
	 * Gets the AnimationTimer which animates the game
	 * @return the AnimationTimer of the game
	 */
	public AnimationTimer getAnimator() {
		return animator;
	}

	/**
	 * Sets the AnimationTimer for the gameScene
	 * @param animator the new AnimationTimer
	 */
	public void setAnimator(AnimationTimer animator) {
		this.animator = animator;
	}

	/**
	 * Sets window variables such as the Stage and root Group and adds input
	 * detection
	 */
	public void prepareWindow() {
		
		getStage().setTitle("PortalGame");
		getStage().setResizable(false);

		root = new Group();

		gameScene = new Scene(root, WIDTH, HEIGHT);

		addKeyboardHandling(gameScene);

		addMouseHandling(gameScene);
		
		getStage().setWidth(WIDTH + 6);
		getStage().setHeight(HEIGHT + 35);

		canvas = new Canvas(getStage().getWidth(), getStage().getHeight());

		root.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();
		
		getStage().show();
		
	}
	
	/**
	 * Gets the LevelManager
	 * @return the LevelManager
	 */
	public LevelManager getLevelManager() {
		return levelManager;
	}

	/**
	 * Sets the LevelManager
	 * @param levelManager the new LevelManager
	 */
	public void setLevelManager(LevelManager levelManager) {
		this.levelManager = levelManager;
	}

	/**
	 * Gets the MenuManager
	 * @return the MenuManager
	 */
	public MenuManager getMenuManager() {
		return menuManager;
	}

	/**
	 * Sets the MenuManager
	 * @param menuManager the new MenuManager
	 */
	public void setMenuManager(MenuManager menuManager) {
		this.menuManager = menuManager;
	}

	/**
	 * Gets the Player
	 * @return the Player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Adds keyboard event(ex. key press) handling to a scene
	 * @param scene scene to add keyboard event handling to
	 */
	public void addKeyboardHandling(Scene scene) {
		
		KeyboardHandler keyboardHandler = new KeyboardHandler();
		scene.setOnKeyPressed(keyboardHandler);
		scene.setOnKeyReleased(keyboardHandler);
	}
	
	/**
	 * Adds mouse event handling to the given Scene
	 * @param scene the Scene to add mouse event handling to
	 */
	public void addMouseHandling(Scene scene) {
		// adds mouseEvent handling to the given scene
		MouseHandler mouseHandler = new MouseHandler();
		scene.setOnMouseMoved(mouseHandler);
		scene.setOnMouseDragged(mouseHandler);
		scene.setOnMousePressed(mouseHandler);
		scene.setOnMouseClicked(mouseHandler);
		scene.setOnMouseReleased(mouseHandler);
	}
	
	/**
	 * A class for handling keyboard input
	 */
	private class KeyboardHandler implements EventHandler<KeyEvent> {

		KeyboardHandler() {
		}

		public void handle(KeyEvent arg0) {
			if (arg0.getEventType() == KeyEvent.KEY_PRESSED) {
				
				String code = arg0.getCode().toString().toUpperCase();
				
				if (code.equals("W")) {
					
					if (getPlayer().isJumpReady() && !getPlayer().isInsidePortal()) {
						
						getPlayer().setyVelocity(-4);
						getPlayer().setJumpReady(false);
					}
					
					
				} else if (code.equals("A")) {
					
					player.setxVelocity(player.getxVelocity() - 2);
					
				} else if (code.equals("D")) {
					
					player.setxVelocity(player.getxVelocity() + 2);
					
				} else if (code.equals("DIGIT1")) {
					
					getLevelManager().switchLevel(0);
				}
				
				if (code.equals("F")) {
					
					System.out.println(player);
					
				}
				if (code.equals("P")) {
					
					menuManager.showMainMenu();
					//animator.stop();
					
				}
				if (code.equals("R")) {
					
					animator.start();
					
				} 
				if (code.equals("SPACE")) {
					
					getLevelManager().getCurrentLevel().restartLevel();
					
					
				}
				
			} 
		}

	}
	
	/**
	 * A class for handling mouse input
	 */
	class MouseHandler implements EventHandler<MouseEvent> {

		MouseHandler() {
		}

		public void handle(MouseEvent arg0) {
			
			if (arg0.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
				
				Point2D clickPoint = new Point2D(arg0.getX(), arg0.getY());
				
				double angle = Math.toDegrees(calculateAngleBetween(player.getCenterPoint(), clickPoint));
				
				if (arg0.getButton() == MouseButton.PRIMARY) {

					// If the left click button is pressed, launch a portal
					String clickType = "LEFT";

					if (!player.isInsidePortal()) {
						player.launchPortal(clickType, angle);
					}
				}
				if (arg0.getButton() == MouseButton.SECONDARY) {

					// If the right click button is pressed, launch a portal
					String clickType = "RIGHT";

					if (!player.isInsidePortal()) {
						player.launchPortal(clickType, angle);
					}
				}
			}
		}
		
		/**
		 * Calculates the angle between two bodies and gives it in radians
		 * @param body1 one of the bodies to calculate the angle between
		 * @param body2 one of the bodies to calculate the angle between
		 * @return the angle between the two bodies in radians
		 */
		public double calculateAngleBetween(Point2D point1, Point2D point2) {
			
			double angle; 
			
			double xDiff = point1.getX() - point2.getX();
			double yDiff = point1.getY() - point2.getY();
			
			angle = Math.atan2(xDiff, yDiff);
			
			// Translate the angle
			angle -= Math.PI / 2;
			angle *= -1;
			
			return angle;
			
		}
		
	}

}