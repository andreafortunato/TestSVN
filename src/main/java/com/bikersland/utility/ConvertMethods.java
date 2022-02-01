package com.bikersland.utility;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.bikersland.bean.EventBean;
import com.bikersland.controller.graphics.EventCardControllerView;
import com.bikersland.Main;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class ConvertMethods {
	
	private ConvertMethods() {}
	
	public static List<Node> eventsToNodeList(List<EventBean> eventList)
	{
		FXMLLoader fxmlLoader;
    	
    	List<Node> nodeList = FXCollections.observableArrayList();
    	
    	for(EventBean eventBean: eventList) {
    		fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("EventCard.fxml"));
            fxmlLoader.setController(new EventCardControllerView(eventBean));
            fxmlLoader.setResources(Main.getBundle());
            
            StackPane viaggioBox = null;
			try {
				viaggioBox = fxmlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			            
            if(viaggioBox != null)
            	nodeList.add(viaggioBox);
		}
    	
    	return nodeList;
	}
	
	public static String dateToLocalFormat(Date date)
	{
		SimpleDateFormat datePattern;
		if(Main.getLocale() == Locale.ITALIAN)
			datePattern = new SimpleDateFormat("dd-MM-yyyy");
		else
			datePattern = new SimpleDateFormat("MM-dd-yyyy");
		
		return datePattern.format(date);
	}
	
	/* OFFICIAL COMMENT BLOCK */
	/* Limita il massimo numero di caratteri di un TextField al valore "maxLenght" */
	/*
	public static void addTextLimiter(Control control, int maxLength) throws IOException {
		if(control instanceof TextField) {
		    ((TextField)control).textProperty().addListener(new ChangeListener<String>() {
		        @Override
		        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
		            if (((TextField)control).getText().length() > maxLength) {
		                String s = ((TextField)control).getText().substring(0, maxLength);
		                ((TextField)control).setText(s);
		            }
		        }
		    });
		} else {
			throw new IOException("The control is not a kind-of TextField");
		}
	}
	*/
}
