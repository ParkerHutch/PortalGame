import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class MenuManager {

	private PortalGame gameObject;
	
	public MenuManager() {}
	
	/**
	 * Creates a MenuManager for a given PortalGame instance
	 * @param gameObject
	 */
	public MenuManager(PortalGame gameObject) {
		
		setGameObject(gameObject);
		
	}
	
	/**
	 * Shows the MainMenu screen on the PortalGame's Scene
	 */
	public void showMainMenu() {

		// Pause the animator if it exists
		if (getGameObject().getAnimator() != null) {
			
			getGameObject().getAnimator().stop();
			
		}
		
		Pane mainMenuPane = new Pane();

		Canvas mainMenuCanvas = new Canvas(getGameObject().getWidth(), getGameObject().getHeight());
		mainMenuPane.getChildren().add(mainMenuCanvas);
		GraphicsContext gc = mainMenuCanvas.getGraphicsContext2D();
		
		Scene mainMenuScene = new Scene(mainMenuPane, getGameObject().getWidth(), getGameObject().getHeight());
		getGameObject().getStage().setScene(mainMenuScene);
		

		gc.setFill(Color.GOLD);
		gc.fillRect(0, 0, getGameObject().getWidth(), getGameObject().getHeight());

		gc.setFill(Color.BLACK);
		
		gc.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 42));
		gc.fillText("Portals", getGameObject().getWidth() / 2 - 75, 200, 300); // TODO should store these as Text objects
		gc.fillText("Parker Hutchinson", getGameObject().getWidth() / 2 - 140, 275, 300);

		Button startButton = new Button("Levels");
		startButton.setPrefSize(200, 50);
		startButton.setLayoutX(200);
		startButton.setLayoutY(getGameObject().getHeight() - 100);
		startButton.setOnAction(event -> showLevels());
		
		Button instructionsButton = new Button("Instructions");
		instructionsButton.setPrefSize(200, 50);
		instructionsButton.setLayoutX(startButton.getLayoutX() + 400);
		instructionsButton.setLayoutY(startButton.getLayoutY());
		instructionsButton.setOnAction(event -> showInstructions());
		
		mainMenuPane.getChildren().add(startButton);
		mainMenuPane.getChildren().add(instructionsButton);

	}

	/**
	 * Shows the level summary screen on the PortalGame's Scene
	 */
	public void showLevels() {
		
		
		Pane levelScreenPane = new Pane();

		Canvas levelScreenCanvas = new Canvas(getGameObject().getWidth(), getGameObject().getHeight());
		GraphicsContext gc = levelScreenCanvas.getGraphicsContext2D();
		levelScreenPane.getChildren().add(levelScreenCanvas);
		
		Scene scene = new Scene(levelScreenPane, getGameObject().getWidth(), getGameObject().getHeight());
		getGameObject().getStage().setScene(scene);

		
		gc.setFill(Color.GOLD);
		gc.fillRect(0, 0, getGameObject().getWidth(), getGameObject().getHeight());
		
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 50));
		gc.fillText("Levels", getGameObject().getWidth() / 2 - 75, 100);
		
		// There will be 1 button for every level
		Button [] levelButtons = getLevelButtons();
		
		Button backButton = new Button("Back To Main Menu");
		backButton.setPrefSize(400, 50);
		backButton.setLayoutX(gameObject.getWidth() / 2 - 200);
		backButton.setLayoutY(getGameObject().getHeight() - 100);
		backButton.setOnAction(event -> showMainMenu());
		
		levelScreenPane.getChildren().addAll(levelButtons);
		levelScreenPane.getChildren().add(backButton);

	}
	
	/**
	 * Generates an array of formatted Buttons for use in the level summary window
	 * @return an array of Buttons
	 */
	public Button [] getLevelButtons() {
		
		Button [] levelButtons = new Button[getGameObject().getLevelManager().getLevels().length];
		
		String buttonText;
		
		int buttonX;
		int buttonY;
		int buttonWidth = 200;
		int buttonHeight = 100;
		
		int buttonHorizontalSpacing = 10; // Spacing between each button
		int buttonVerticalSpacing = 10;
		int buttonsPerRow = (getGameObject().getWidth()) / (buttonWidth + buttonHorizontalSpacing); 
		int buttonsPerColumn = (getGameObject().getHeight()) / (buttonHeight + buttonVerticalSpacing);
		
		int xMargin; // Horizontal distance between left side of screen and left side of first button
		int yMargin; // Vertical distance between top of screen and top of first button
		xMargin = (getGameObject().getWidth() - (buttonsPerRow * buttonWidth) - (buttonsPerRow - 1) * buttonHorizontalSpacing) / 2;
		yMargin = (getGameObject().getHeight() - (buttonsPerColumn * buttonHeight) - (buttonsPerColumn - 1) * buttonVerticalSpacing) / 2;
		
		int buttonNumber = 0;
		for (int i = 1; i <= buttonsPerColumn; i++) {

			for (int j = 0; j < buttonsPerRow; j++) {

				if (buttonNumber < getGameObject().getLevelManager().getLevels().length) {
					
					buttonText = Integer.toString(buttonNumber + 1);

					buttonX = xMargin + (j) * (buttonWidth + buttonHorizontalSpacing);

					buttonY = yMargin + (i) * (buttonHeight + buttonVerticalSpacing);


					levelButtons[buttonNumber] = new Button(buttonText);
					levelButtons[buttonNumber].setLayoutX(buttonX);
					levelButtons[buttonNumber].setLayoutY(buttonY);
					levelButtons[buttonNumber].setPrefSize(buttonWidth, buttonHeight);
					levelButtons[buttonNumber].setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 50));
					
					int levelAssociatedIndex = buttonNumber;
					levelButtons[buttonNumber].setOnAction(event -> getGameObject().getLevelManager().switchLevel(levelAssociatedIndex));
					
					// If the level was completed, make the button that accesses it have a green color
					if (getGameObject().getLevelManager().getLevels()[levelAssociatedIndex].isLevelCompleted()) {
						
						levelButtons[buttonNumber].setTextFill(Color.GREEN);
						
					}
					
					buttonNumber += 1;
					
					
				} 
				
			}
		}

		return levelButtons;

	}

	/**
	 * Shows the instructions screen on the PortalGame's Scene
	 */
	public void showInstructions() {

		Pane pane = new Pane();

		Canvas canvas = new Canvas(getGameObject().getWidth(), getGameObject().getHeight());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		pane.getChildren().add(canvas);
		
		Scene scene = new Scene(pane, getGameObject().getWidth(), getGameObject().getHeight());
		getGameObject().getStage().setScene(scene);
		
		gc.setFill(Color.GOLD);
		gc.fillRect(0, 0, getGameObject().getWidth(), getGameObject().getHeight());
		
		gc.setFill(Color.BLACK);
		
		// Header
		gc.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 50));
		gc.fillText("Instructions", getGameObject().getWidth() / 2 - 150, 75);
		
		
		gc.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 24));
		
		// Main Goal
		gc.fillText("Goal: Get the Player to the green Rectangle", getGameObject().getWidth() / 3 - 50, 150);

		// Keys

		// Left Click
		gc.fillText("Left Click: Launch an orange Portal", getGameObject().getWidth() / 3 - 50, 200);
		
		// Right Click
		gc.fillText("Right Click: Launch a blue Portal", getGameObject().getWidth() / 3 - 50, 250);
		
		// W
		gc.fillText("W: Jump", getGameObject().getWidth() / 3 - 50, 300);
		
		// A/D
		gc.fillText("A/D: Move left/right", getGameObject().getWidth() / 3 - 50, 350);

		
		
		Button backButton = new Button("Back To Main Menu");
		backButton.setPrefSize(400, 50);
		backButton.setLayoutX(gameObject.getWidth() / 2 - 200);
		backButton.setLayoutY(getGameObject().getHeight() - 100);
		backButton.setOnAction(event -> showMainMenu());
		
		
		pane.getChildren().add(backButton);

	}
	
	/**
	 * Shows a "congratulations" window on the PortalGame's Scene
	 */
	public void showLevelCompletedWindow() {
		
		Pane pane = new Pane();

		Canvas canvas = new Canvas(getGameObject().getWidth(), getGameObject().getHeight());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		pane.getChildren().add(canvas);
		
		Scene scene = new Scene(pane, getGameObject().getWidth(), getGameObject().getHeight());
		getGameObject().getStage().setScene(scene);
		
		gc.setFill(Color.GREEN);
		gc.fillRect(0, 0, getGameObject().getWidth(), getGameObject().getHeight());
		
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 24));
		gc.fillText("Congratulations! You completed the level.", getGameObject().getWidth() / 2 - 250, 100);
		
		
		Button backButton = new Button("Back To Levels");
		backButton.setPrefSize(400, 50);
		backButton.setLayoutX(gameObject.getWidth() / 2 - 200);
		backButton.setLayoutY(getGameObject().getHeight() / 2);
		backButton.setOnAction(event -> showLevels());
		
		
		pane.getChildren().add(backButton);
		
	}

	/**
	 * Shows a "fail" window on the PortalGame's Scene
	 */
	public void showFailScreen() {

		Pane pane = new Pane();

		Canvas canvas = new Canvas(getGameObject().getWidth(), getGameObject().getHeight());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		pane.getChildren().add(canvas);

		Scene scene = new Scene(pane, getGameObject().getWidth(), getGameObject().getHeight());
		getGameObject().getStage().setScene(scene);

		gc.setFill(Color.RED);
		gc.fillRect(0, 0, getGameObject().getWidth(), getGameObject().getHeight());

		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 24));
		gc.fillText("You failed the level. Try again?", getGameObject().getWidth() / 2 - 200, 100);


		Button backButton = new Button("Back To Levels");
		backButton.setPrefSize(400, 50);
		backButton.setLayoutX(gameObject.getWidth() / 2 - 200);
		backButton.setLayoutY(getGameObject().getHeight() / 2);
		backButton.setOnAction(event -> showLevels());


		pane.getChildren().add(backButton);

	}

	/**
	 * Sets the Scene of the PortalGame's Stage and starts the 
	 * PortalGame's Animator
	 */
	public void startGame() {
		
		getGameObject().getStage().setScene(getGameObject().getGameScene());
		getGameObject().getAnimator().start();
		
		
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
