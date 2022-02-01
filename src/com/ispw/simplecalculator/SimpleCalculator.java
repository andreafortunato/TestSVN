package com.ispw.simplecalculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimpleCalculator extends Application {

	public static final Integer size = 50;
	
	private final Double defaultResultTextSize = 33.0;
	
	private Stage primaryStage;
	private Scene mainScene;
	private VBox mainVBox;
	private GridPane mainPane;
	
	private MenuBar calcMenuBar = new MenuBar();
		private Menu fileMenu = new Menu("File");
			private Menu settingsMenu = new Menu("Settings");
				private Menu mulSettingsMenu = new Menu("Choose Multiplication Sign");
					private ToggleGroup mulToggleGroup = new ToggleGroup();
						private RadioMenuItem mulItem1 = new RadioMenuItem("\u00D7   ");
						private RadioMenuItem mulItem2 = new RadioMenuItem("*   ");
						private RadioMenuItem mulItem3 = new RadioMenuItem("\u2022   ");
				private Menu divSettingsMenu = new Menu("Choose Division Sign");
					private ToggleGroup divToggleGroup = new ToggleGroup();
						private RadioMenuItem divItem1 = new RadioMenuItem("\u00F7   ");
						private RadioMenuItem divItem2 = new RadioMenuItem(" /  ");
			private SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
			private MenuItem exitMenuItem = new MenuItem("Exit");
		private Menu infoMenu = new Menu("Info");
			private MenuItem creditsMenuItem = new MenuItem("Credits");
	
	private CalcResultTextField resultTextField;
	private Map<String, Button> calcButtons;
	
	private Double result = 0.0;
	private String lastOperation = null;
			
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.getIcons().add(new Image(SimpleCalculator.class.getResourceAsStream("calc_icon.png")));
		primaryStage.setTitle(" Simple Calc");
		primaryStage.setResizable(false);
		
		
		mainPane = new GridPane();
		mainVBox = new VBox();
		
		initializeMenuBar();
						
		String[][] calcLayout = {
				{"C", "/", "*", "<--"},
				{"7", "8", "9", "-"},
				{"4", "5", "6", "+"},
				{"1", "2", "3", "="},
				{   "0",   ".",    }};
		
		calcButtons = new HashMap<String, Button>();
		
		resultTextField = new CalcResultTextField();
		
		resultTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Text tmpText = new Text(newValue);
            tmpText.setFont(Font.font(defaultResultTextSize));

            double textWidth = tmpText.getLayoutBounds().getWidth();

            if (textWidth <= 208) {
                resultTextField.setFont(Font.font(defaultResultTextSize));
            } else {
                double newFontSize = defaultResultTextSize * 208 / textWidth;
                resultTextField.setFont(Font.font(newFontSize));
            }

        });
		
		mainPane.setHgap(5);
		mainPane.setVgap(5);
		mainPane.setPadding(new Insets(15));
		
		mainPane.add(resultTextField, 0, 0, 4, 1);
		
		int row, col;
		String buttonText;
		
		for(row = 1; row <= calcLayout.length; row ++) {
			for(col = 0; col < calcLayout[row-1].length; col++) {
				buttonText = calcLayout[row-1][col];
				calcButtons.put(buttonText, new CalcButton(buttonText));
					    		
	    		if(buttonText.matches("[0-9]")) {
	    			calcButtons.get(buttonText).setOnAction(event -> numberButtonHandler(event));
	    		}
	    		
	    		switch(buttonText) {
	    			case "C":
	    				calcButtons.get(buttonText).setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: red;");
	    				calcButtons.get(buttonText).setOnAction(event -> resetButtonHandler(event));
	    				mainPane.add(calcButtons.get(buttonText), col, row);
	    				break;
	    			case "<--":
	    				calcButtons.get(buttonText).setText("\u2190");
	    				calcButtons.get(buttonText).setStyle("-fx-font-size: 30px;");;
	    				calcButtons.get(buttonText).setOnAction(event -> backButtonHandler(event));
	    				mainPane.add(calcButtons.get(buttonText), col, row);
	    				break;
	    			case "=":
	    				calcButtons.get(buttonText).setMaxHeight(Double.MAX_VALUE);
	    				calcButtons.get(buttonText).setOnAction(event -> equalsButtonHandler(event));
	    				calcButtons.get(buttonText).getStyleClass().add("equals");
		    			mainPane.add(calcButtons.get(buttonText), col, row, 1, 2);
		    			break;
	    			case "0":
	    				calcButtons.get(buttonText).setMaxWidth(Double.MAX_VALUE);
		    			mainPane.add(calcButtons.get(buttonText), col, row, 2, 1);
		    			break;
	    			case ".":
	    				calcButtons.get(buttonText).setOnAction(event -> dotButtonHandler(event));
		    			mainPane.add(calcButtons.get(buttonText), col+1, row);
		    			break;
	    			case "+":
	    				calcButtons.get(buttonText).setOnAction(event -> operButtonHandler(event));
	    				mainPane.add(calcButtons.get(buttonText), col, row);
	    				break;
	    			case "-":
	    				calcButtons.get(buttonText).setOnAction(event -> operButtonHandler(event));
	    				calcButtons.get(buttonText).setText("\u2212");
	    				mainPane.add(calcButtons.get(buttonText), col, row);
	    				break;
	    			case "*":
	    				calcButtons.get(buttonText).setOnAction(event -> operButtonHandler(event));
	    				calcButtons.get(buttonText).setText("\u00D7");
	    				mainPane.add(calcButtons.get(buttonText), col, row);
	    				break;
	    			case "/":
	    				calcButtons.get(buttonText).setOnAction(event -> operButtonHandler(event));
	    				calcButtons.get(buttonText).setText("\u00F7");
	    				mainPane.add(calcButtons.get(buttonText), col, row);
	    				break;
		    		default:
		    			mainPane.add(calcButtons.get(buttonText), col, row);
	    		}
	    	}
		}
		
		calcButtons.get("+").getStyleClass().add("util");
		calcButtons.get("-").getStyleClass().add("util");
		calcButtons.get("*").getStyleClass().add("util");
		calcButtons.get("/").getStyleClass().add("util");
		calcButtons.get("<--").getStyleClass().add("util");
		calcButtons.get("C").getStyleClass().add("util");
		
		mainPane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {				
				if(event.getCode().isDigitKey() && event.isShiftDown()==false)
					calcButtons.get(event.getText()).arm();
				else if(event.getCode() == KeyCode.DELETE)
					calcButtons.get("C").arm();
				else if(event.getCode() == KeyCode.BACK_SPACE)
					calcButtons.get("<--").arm();
			}
		});
		
		mainPane.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().isDigitKey() && event.isShiftDown()==false) {
					calcButtons.get(event.getText()).disarm();
					calcButtons.get(event.getText()).fire();
				}
				else if(event.getCode() == KeyCode.DELETE) {
					calcButtons.get("C").disarm();
					calcButtons.get("C").fire();
				}
				else if(event.getCode() == KeyCode.BACK_SPACE) {
					calcButtons.get("<--").disarm();
					calcButtons.get("<--").fire();
				}
			}
		});
		
		mainVBox.getChildren().addAll(calcMenuBar, mainPane);
		
		mainScene = new Scene(mainVBox);
		mainScene.getStylesheets().addAll(getClass().getResource("style.css").toExternalForm());
		primaryStage.setScene(mainScene);
		primaryStage.show();
		
		this.primaryStage = primaryStage;
		
		mainPane.requestFocus();
	}
	
	private void numberButtonHandler(ActionEvent event) {
		if(resultTextField.getLength() > 0 && lastCharIsOper(resultTextField.getText())) {
			doOper(resultTextField.getText());
		}
		
		resultTextField.appendText(((Button)event.getSource()).getText());
	}
	
	private void equalsButtonHandler(ActionEvent event) {
		if(resultTextField.getLength() == 0)
			result = 0.0;
		else {
			if(lastCharIsOper(resultTextField.getText())) {
				doOper(resultTextField.getText().substring(0, resultTextField.getText().length()-1));
				lastOperation = null;
			} else {
				doOper(resultTextField.getText());
				lastOperation = null;
			}
		}
		
		if(String.valueOf(result).endsWith(".0"))
			resultTextField.setText(String.valueOf(result).substring(0, String.valueOf(result).length()-2));
		else
			resultTextField.setText(String.valueOf(result));

	}
	
	private void operButtonHandler(ActionEvent event) {
		Button source = (Button)event.getSource();
		String oper = source.getText();
		
		if(resultTextField.getLength() == 0)
			resultTextField.appendText("0" + oper);	
		else {
			if(lastCharIsOper(resultTextField.getText()))
				resultTextField.replaceText(resultTextField.getLength()-1, resultTextField.getLength(), oper);
			else {
				resultTextField.appendText(oper);
			}
		}
	}
	
	private void dotButtonHandler(ActionEvent event) {
		if(resultTextField.getLength() == 0)
			resultTextField.appendText("0.");
		else if(lastCharIsOper(resultTextField.getText())) {
			doOper(resultTextField.getText());
			resultTextField.appendText("0.");
		}
		else if(!resultTextField.getText().contains("."))
			resultTextField.appendText(".");
			
	}
	
	private void resetButtonHandler(ActionEvent event) {
		resultTextField.setText(null);
		result = 0.0;
		lastOperation = null;
	}
	
	private void backButtonHandler(ActionEvent event) {
		switch (resultTextField.getLength()) {
		case 0:
			break;
		case 1:
			resultTextField.setText(null);
			break;
		default:
			resultTextField.setText(resultTextField.getText(0, resultTextField.getLength()-1));
			break;
		}			
	}
	
	private boolean lastCharIsOper(String string) {
		String[] oper = {"+", "-", "\u2212", "\u00D7", "*", "\u2022", "\u00F7", "/"};
		return Arrays.stream(oper).anyMatch(entry -> string.endsWith(entry));
	}
	
	private void doOper(String newOperation) {
		String num;
		String nextOper = null;
		
		if(lastCharIsOper(newOperation)) {
			num = newOperation.substring(0, newOperation.length()-1);
			nextOper = String.valueOf(newOperation.charAt(newOperation.length()-1));
		}
		else 
			num = newOperation;
		
		if (lastOperation != null) {
			switch (lastOperation) {
				case "+":
					result = Double.valueOf(result) + Double.valueOf(num);
					break;
				case "-":
				case "\u2212":
					result = Double.valueOf(result) - Double.valueOf(num);
					break;
				case "\u00D7":
				case "*":
				case "\u2022":
					result = Double.valueOf(result) * Double.valueOf(num);
					break;
				case "\u00F7":
				case "/":
					if(Double.valueOf(num).equals(Double.valueOf("0"))) {
						showTimedAlert();
												
						resetButtonHandler(null);
					} else
						result = Double.valueOf(result) / Double.valueOf(num);
					break;
				default:
					break;
			}
		} else
			result = Double.valueOf(num);
		
		lastOperation = nextOper;
		
		resultTextField.setText(null);
	}
	
	private void showTimedAlert() {
		TextFlow flow = new TextFlow();

		Text txt1=new Text("Ooops, you tried to divide a number by zero but this is not possible!\n\nPrevious result: ");
		txt1.setStyle("-fx-font-size: 15px;");
		txt1.setWrappingWidth(100);
		txt1.setFontSmoothingType(FontSmoothingType.LCD);

		Text txt2=new Text(String.valueOf(result));
		txt2.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: red;");

		flow.getChildren().addAll(txt1, txt2);
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.getDialogPane().getStyleClass().add("errorDialog");
		alert.initOwner(primaryStage);
		alert.setTitle(" Operation Error");
		alert.setHeaderText("Division by Zero!");
		flow.setPrefWidth(350);
		alert.getDialogPane().setContent(flow);
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setStyle("-fx-font-size: 15px;");;
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("(3) OK");
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setDisable(true);
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setFocusTraversable(false);
						
		Timeline idlestage = new Timeline( new KeyFrame( Duration.seconds(1), new EventHandler<ActionEvent>()
	    {
			int i=2;

	        @Override
	        public void handle( ActionEvent event )
	        {
	        	((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText(String.format("(%d) OK", i--));
	        }
	    } ) );
	    idlestage.setCycleCount(3);
	    idlestage.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("OK");
				((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setDisable(false);
			}
		});
	    idlestage.play();
		alert.showAndWait();
	}
	
	private void initializeMenuBar() {
		mulToggleGroup.getToggles().addAll(mulItem1, mulItem2, mulItem3);
		divToggleGroup.getToggles().addAll(divItem1, divItem2);
		
		mulSettingsMenu.getItems().addAll(mulItem1, mulItem2, mulItem3);
		divSettingsMenu.getItems().addAll(divItem1, divItem2);
		
		settingsMenu.getItems().addAll(mulSettingsMenu, divSettingsMenu);
		
		fileMenu.getItems().addAll(settingsMenu, separatorMenuItem, exitMenuItem);
		infoMenu.getItems().add(creditsMenuItem);
		
		calcMenuBar.getMenus().addAll(fileMenu, infoMenu);
		
		exitMenuItem.setOnAction((event) -> primaryStage.close());
		
		mulItem1.setSelected(true);
		divItem1.setSelected(true);
		
		mulToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> menuItemHandler((RadioMenuItem)newVal));
		divToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> menuItemHandler((RadioMenuItem)newVal));
		
		infoMenu.setOnAction((event) -> showInfoAlert());
	}
	
	private void menuItemHandler(RadioMenuItem item) {
		String resultString = resultTextField.getText();
		
		String newOper = item.getText().strip();
		if(item.getParentMenu().equals(mulSettingsMenu)) {
			calcButtons.get("*").setText(newOper);
			if(resultString.endsWith("\u2022") || resultString.endsWith("*") || resultString.endsWith("\u00D7"))
				resultTextField.replaceText(resultTextField.getLength()-1, resultTextField.getLength(), newOper);
		} else {
			calcButtons.get("/").setText(newOper);
			if(resultString.endsWith("\u00F7") || resultString.endsWith("/")) 
				resultTextField.replaceText(resultTextField.getLength()-1, resultTextField.getLength(), newOper);
		}
		
		
	}
	
	private void showInfoAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(primaryStage);
		alert.setTitle(" Credits");
		alert.setHeaderText("Credits info");
		alert.setContentText("This \"Simple Calculator\" (v2.0) was made by Andrea Fortunato as an exercise for the ISPW course.");
		alert.getDialogPane().setStyle("-fx-font-size: 15px;");
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setStyle("-fx-font-size: 15px;");;
						
		alert.showAndWait();		
	}
}
