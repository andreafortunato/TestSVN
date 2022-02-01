package com.bikersland.utility;

import com.bikersland.Main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class TimedAlert {
	private TimedAlert() {}
	
	public static void show(AlertType alertType, String title, String header, String content, String contentRed)
	{
		show(3, alertType, title, header, content, contentRed);
	}
	
	public static void show(Integer seconds, AlertType alertType, String title, String header, String content, String contentRed) 
	{
		TextFlow flow = new TextFlow();

		Text txt1=new Text(content);
		txt1.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-fill: white;");
		txt1.setWrappingWidth(100);
		txt1.setFontSmoothingType(FontSmoothingType.LCD);

		Text txt2=new Text(contentRed);
		txt2.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: red; -fx-fill: red;");

		flow.getChildren().addAll(txt1, txt2);
		
		Alert alert = new Alert(alertType);
		alert.getDialogPane().getScene().getWindow().setOnCloseRequest(WindowEvent::consume);
		alert.getDialogPane().getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
		alert.getDialogPane().getStyleClass().add("errorDialog");
		alert.setTitle(" " + title);
		alert.setHeaderText(header);
		flow.setPrefWidth(400);
		alert.getDialogPane().setContent(flow);
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setStyle("-fx-font-size: 15px;");
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("(" + seconds + ") OK");
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setDisable(true);
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setFocusTraversable(false);
						
		Timeline idlestage = new Timeline( new KeyFrame( Duration.seconds(1), new EventHandler<ActionEvent>()
	    {
			int i=seconds-1;

	        @Override
	        public void handle( ActionEvent event )
	        {
	        	((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText(String.format("(%d) OK", i--));
	        }
	    } ) );
	    idlestage.setCycleCount(seconds);
	    idlestage.setOnFinished(event -> {
				((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("OK");
				((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setDisable(false);
				alert.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> alert.hide());
		});
	    
	    idlestage.play();
		alert.showAndWait();
	}
}
