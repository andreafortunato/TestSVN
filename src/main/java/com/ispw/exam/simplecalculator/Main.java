package com.ispw.exam.simplecalculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.ispw.exam.simplecalculator.customcontrol.CustomCalcButton;
import com.ispw.exam.simplecalculator.customcontrol.CustomResultTextField;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {
	
	public static final Integer SIZE = 50;
	public static final Integer GAP = 5;
	public static final String ERROR = "ERROR! Division by 0!";
	private static final Double DEFAULT_RESULT_TEXT_SIZE = 33.0;

		
	private CustomResultTextField resultTextField;
	
	private Double result = 0.0;
	private String lastOperation = null;


    @Override
    public void start(Stage primaryStage) {
    	Map<String, Button> calcButtons;
    	Scene mainScene;
    	VBox mainVBox;
    	GridPane mainPane;


    	primaryStage.setTitle("Simple Calc");
    	primaryStage.setResizable(false);
    	
    	mainPane = new GridPane();
    	mainVBox = new VBox();
    	
		String[][] calcLayout = {
				{"C", "/", "*", "<--"},
				{"7", "8", "9", "-"},
				{"4", "5", "6", "+"},
				{"1", "2", "3", "="},
				{   "0",   ".",    }};

		calcButtons = new HashMap<>();
		
		resultTextField = new CustomResultTextField();
		
		resultTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Text tmpText = new Text(newValue);
            tmpText.setFont(Font.font(DEFAULT_RESULT_TEXT_SIZE));

            double textWidth = tmpText.getLayoutBounds().getWidth();

            if (textWidth <= 208) {
                resultTextField.setFont(Font.font(DEFAULT_RESULT_TEXT_SIZE));
            } else {
                double newFontSize = DEFAULT_RESULT_TEXT_SIZE * 208 / textWidth;
                resultTextField.setFont(Font.font(newFontSize));
            }

        });

		
		mainPane.setHgap(GAP);
		mainPane.setVgap(GAP);
		mainPane.setPadding(new Insets(GAP*3f));
		
		mainPane.add(resultTextField, 0, 0, 4, 1);

		int row;
		int col;
		String buttonText;
		
		for(row = 1; row <= calcLayout.length; row++) {
			for(col = 0; col < calcLayout[row-1].length; col++) {
				buttonText = calcLayout[row-1][col];
				calcButtons.put(buttonText, new CustomCalcButton(buttonText));
					    		
	    		if(buttonText.matches("[0-9]")) {
	    			calcButtons.get(buttonText).setOnAction(this::numberButtonHandler);
	    		}
	    		
	    		switch(buttonText) {
	    			case "C":
	    				calcButtons.get(buttonText).setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: red;");
	    				calcButtons.get(buttonText).setOnAction(event -> resetButtonHandler());
	    				mainPane.add(calcButtons.get(buttonText), col, row);
	    				break;
	    			case "<--":
	    				calcButtons.get(buttonText).setOnAction(event -> backButtonHandler());
	    				mainPane.add(calcButtons.get(buttonText), col, row);
	    				break;
	    			case "=":
	    				calcButtons.get(buttonText).setMaxHeight(Double.MAX_VALUE);
	    				calcButtons.get(buttonText).setOnAction(event -> equalsButtonHandler());
		    			mainPane.add(calcButtons.get(buttonText), col, row, 1, 2);
		    			break;
	    			case "0":
	    				calcButtons.get(buttonText).setMaxWidth(Double.MAX_VALUE);
		    			mainPane.add(calcButtons.get(buttonText), col, row, 2, 1);
		    			break;
	    			case ".":
	    				calcButtons.get(buttonText).setOnAction(event -> dotButtonHandler());
		    			mainPane.add(calcButtons.get(buttonText), col+1, row);
		    			break;
	    			case "+":
	    			case "-":
	    			case "*":
	    			case "/":
	    				calcButtons.get(buttonText).setOnAction(this::operButtonHandler);
	    				mainPane.add(calcButtons.get(buttonText), col, row);
	    				break;
		    		default:
		    			mainPane.add(calcButtons.get(buttonText), col, row);
	    		}
	    	}
		}

		mainVBox.getChildren().addAll(mainPane);

		mainScene = new Scene(mainVBox);
		primaryStage.setScene(mainScene);
		primaryStage.show();
				
		mainPane.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
    
	private void numberButtonHandler(ActionEvent event) {
		if (resultTextField.getText().equalsIgnoreCase(ERROR)) {
			resetButtonHandler();
		}

		if(resultTextField.getLength() > 0 && lastCharIsOper(resultTextField.getText())) {
				doOper(resultTextField.getText());
		}
		
		resultTextField.appendText(((Button)event.getSource()).getText());
	}
	
	private void equalsButtonHandler() {
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
		
		if(Double.isInfinite(result) || Double.isNaN(result)) {
			resultTextField.setStyle("-fx-text-fill: #FF0000;");
			resultTextField.setText(ERROR);
			resultTextField.setFont(Font.font(21.5));
			lastOperation = null;
			result = 0.0;
		} else {
			if(String.valueOf(result).endsWith(".0"))
				resultTextField.setText(String.valueOf(result).substring(0, String.valueOf(result).length()-2));
			else
				resultTextField.setText(String.valueOf(result));
		}
	}

	
	private void operButtonHandler(ActionEvent event) {
		if (resultTextField.getText().equalsIgnoreCase(ERROR)) {
			resetButtonHandler();
		}
		
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
	
	private void dotButtonHandler() {
		if (resultTextField.getText().equalsIgnoreCase(ERROR)) {
			resetButtonHandler();
		}

		if(resultTextField.getLength() == 0)
			resultTextField.appendText("0.");
		else if(lastCharIsOper(resultTextField.getText())) {
			doOper(resultTextField.getText());
			resultTextField.appendText("0.");
		}
		else if(!resultTextField.getText().contains("."))
			resultTextField.appendText(".");
			
	}
	
	private void resetButtonHandler() {
		resultTextField.setStyle(null);
		resultTextField.setFont(Font.font(33));
		resultTextField.setText("");
		result = 0.0;
		lastOperation = null;
	}
	
	private void backButtonHandler() {
		if (resultTextField.getText().equalsIgnoreCase(ERROR)) {
			resetButtonHandler();
		}
		
		switch (resultTextField.getLength()) {
		case 0:
			break;
		case 1:
			resultTextField.setText("");
			break;
		default:
			resultTextField.setText(resultTextField.getText(0, resultTextField.getLength()-1));
			break;
		}			
	}



	private boolean lastCharIsOper(String string) {
		String[] oper = {"+", "-", "*", "/"};
		return Arrays.stream(oper).anyMatch(string::endsWith);
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
					result = result + Double.valueOf(num);
					break;
				case "-":
					result = result - Double.valueOf(num);
					break;
				case "*":
					result = result * Double.valueOf(num);
					break;
				case "/":
					result = result / Double.valueOf(num);
					break;
				default:
					break;
			}
		} else
			result = Double.valueOf(num);
		
		lastOperation = nextOper;
		
		resultTextField.setText("");
	}


}