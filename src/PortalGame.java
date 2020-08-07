import java.util.ArrayList;
import java.util.Collections;

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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/*
 * IDEAS/TODO
 * - Left/Right click should determine what type of portal is fired
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
	int WIDTH = 1000;
	int HEIGHT = 680;

	// Useful global variables
	Group root;
	Canvas canvas;
	Scene gameScene;
	double cameraZoom = 1;

	AnimationTimer animator;

	KeyboardHandler keyboardHandler;
	MouseHandler mouseHandler;
	
	GraphicsContext gc;
	
	Player player;
	ArrayList<Rectangle> platformObjects = new ArrayList<>();
	
	public static void main(String[] args) {
		launch(args);
	}

	public void init() throws Exception {
		
		player = new Player(WIDTH / 2, HEIGHT / 2, 40, 40, this);
		player.setColor(Color.AQUA);
		player.setxVelocity(0);
		player.setyVelocity(0);
		
	}

	public void start(Stage gameStage) throws Exception {
		
		prepareWindow(gameStage);	
		
		Rectangle floor = new Rectangle(0, HEIGHT - 50, WIDTH, 50);
		Rectangle leftWall = new Rectangle(0, 0, 50, HEIGHT);
		Rectangle rightWall = new Rectangle(WIDTH - 50, 0, 50, HEIGHT);
		Rectangle middlePlatform = new Rectangle(WIDTH / 2 - 150, 500, 300, 20);
		Rectangle ceiling = new Rectangle(0, 0, WIDTH, 50);
		
		Collections.addAll(platformObjects, floor, leftWall, rightWall, middlePlatform, ceiling);
		root.getChildren().addAll(platformObjects);
		
		
		animator = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				
				// NOTE: Game loop

				gc.clearRect(0, 0, WIDTH, HEIGHT); // clear the screen
				
				// draw the objects
				gc.setFill(Color.BLACK);
				
				player.update(gc);

				if (player.getPortals().size() > 1) {
				}
				
			}
		};
		
		animator.start();

		gameStage.setScene(gameScene); // stuff won't show up without this
		gameStage.show();
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
	

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}
	
	/**
	 * Gets the ArrayList of Rectangle objects which serve as platforms in the game 
	 * @return the ArrayList of platform Rectangles
	 */
	public ArrayList<Rectangle> getPlatformObjects() {
		return platformObjects;
	}

	public void prepareWindow(Stage theStage) {
		
		theStage.setTitle("PortalGame");

		root = new Group();

		gameScene = new Scene(root, WIDTH, HEIGHT);

		addKeyboardHandling(gameScene);

		addMouseHandling(gameScene, new MouseHandler());
		
		theStage.setWidth(WIDTH + 17); // adding these 2 lines fixes drawing errors
		theStage.setHeight(HEIGHT + 47);

		canvas = new Canvas(theStage.getWidth(), theStage.getHeight());
		// also, adding numbers moves the canvas down further on the screen or further
		// to the right

		root.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();
		
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
	
	public void addMouseHandling(Scene scene, MouseHandler handlerObject) {
		// adds mouseEvent handling to the given scene
		scene.setOnMouseMoved(handlerObject);
		scene.setOnMouseDragged(handlerObject);
		scene.setOnMousePressed(handlerObject);
		scene.setOnMouseClicked(handlerObject);
		scene.setOnMouseReleased(handlerObject);
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
					
					player.setyVelocity(player.getyVelocity() - 2);
					
				} else if (code.equals("A")) {
					
					player.setxVelocity(player.getxVelocity() - 2);
					
				} else if (code.equals("D")) {
					
					player.setxVelocity(player.getxVelocity() + 2);
					
				} else if (code.equals("S")) {
					
					player.setyVelocity(player.getyVelocity() + 2);
					
				}
				
				if (code.equals("P")) {
					
					animator.stop();
					
				}
				if (code.equals("R")) {
					
					animator.start();
					
				}
				
			} 
		}

	}
	

	
	class MouseHandler implements EventHandler<MouseEvent> {
		// A class for handling mouse input

		MouseHandler() {
		}

		public void handle(MouseEvent arg0) {
			
			if (arg0.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
				
				Point2D clickPoint = new Point2D(arg0.getX(), arg0.getY());
				
				double angle = Math.toDegrees(calculateAngleBetween(player.getCenterPoint(), clickPoint));
				
				if (arg0.getButton() == MouseButton.PRIMARY) {
					
					// If the left click button is pressed, launch a portal
					String clickType = "LEFT";
					
					
					player.launchPortal(clickType, angle);
					
				}
				if (arg0.getButton() == MouseButton.SECONDARY) {
					
					// If the right click button is pressed, launch a portal
					String clickType = "RIGHT";
					
					player.launchPortal(clickType, angle);
					
				}
			}
		}

	}

}