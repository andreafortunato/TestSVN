package com.bikersland.controller.graphics;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.SearchableComboBox;

import com.bikersland.bean.EventBean;
import com.bikersland.controller.application.HomepageControllerApp;
import com.bikersland.exception.InternalDBException;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.utility.ConvertMethods;
import com.bikersland.utility.CustomGridPane;
import com.bikersland.utility.TagsListCell;
import com.bikersland.utility.TimedAlert;
import com.bikersland.Main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class HomepageControllerView 
{
	
	@FXML
    private AnchorPane pnlHeader;

    @FXML
    private ImageView imgBackground;

    @FXML
    private StackPane pnlMain;

    @FXML
    private RadioButton radioPartenzaCitta;

    @FXML
    private ToggleGroup tgPartenza;

    @FXML
    private RadioButton radioPartenzaDistanza;

    @FXML
    private Slider sliderPartenzaDistanza;

    @FXML
    private Label lblPartenzaDistanza;

    @FXML
    private SearchableComboBox<String> comboPartenzaCitta;

    @FXML
    private SearchableComboBox<String> comboPartenzaDistanza;

    @FXML
    private RadioButton radioArrivoCitta;

    @FXML
    private ToggleGroup tgArrivo;

    @FXML
    private RadioButton radioArrivoDistanza;

    @FXML
    private Slider sliderArrivoDistanza;

    @FXML
    private Label lblArrivoDistanza;

    @FXML
    private SearchableComboBox<String> comboArrivoCitta;

    @FXML
    private SearchableComboBox<String> comboArrivoDistanza;
    
    @FXML
    private HeaderControllerView headerController; /* IMPORTANTE: questa variabile deve avere come nome <fx:id value>Controller, dove
     											  <fx:id value> equivale in questo caso a "header", ovvero l'id del file fxml incluso */
    @FXML
    private Button btnSearch;
    
    @FXML
    private ScrollPane spEventList;
    
    @FXML
    private CustomGridPane gridViaggi;
    
    @FXML
    private ListView<String> lvTags;
    
    private int viaggioBoxWidth = 420;
    
    private List<EventBean> eventListBean;
    private List<Node> eventNodeList;
    
    private Integer gridPaneColumns = 2;
    
    
    public void initialize() 
    {
    	
    	imgBackground.setVisible(false);
    	pnlMain.setVisible(false);
    	spEventList.setVisible(false);
    	
    	sliderPartenzaDistanza.setMajorTickUnit(25);
    	sliderPartenzaDistanza.setMinorTickCount(2);
    	sliderPartenzaDistanza.setShowTickLabels(true);
    	sliderPartenzaDistanza.valueProperty().addListener((observable, oldValue, newValue) -> {
    		sliderPartenzaDistanza.setValue(newValue.intValue());
            lblPartenzaDistanza.setText(Integer.toString(newValue.intValue()) + " Km " + Main.getBundle().getString("from"));
        });
    	sliderPartenzaDistanza.setOnMousePressed(event -> imgBackground.requestFocus());
    	
    	sliderArrivoDistanza.setMajorTickUnit(25);
    	sliderArrivoDistanza.setMinorTickCount(2);
    	sliderArrivoDistanza.setShowTickLabels(true);
    	sliderArrivoDistanza.valueProperty().addListener((observable, oldValue, newValue) -> {
    		sliderArrivoDistanza.setValue(newValue.intValue());
            lblArrivoDistanza.setText(Integer.toString(newValue.intValue()) + " Km " + Main.getBundle().getString("from"));
        });
    	sliderArrivoDistanza.setOnMousePressed(event -> imgBackground.requestFocus());    	
    	
    	
    	ObservableList<String> cities = FXCollections.observableArrayList(Main.getCities());
    	cities.add(0, Main.getBundle().getString(ConstantStrings.ALL_FEMALE));
    	
        comboPartenzaCitta.setItems(cities);
        comboPartenzaDistanza.setItems(FXCollections.observableArrayList(Main.getCities()));
        comboArrivoCitta.setItems(cities);
        comboArrivoDistanza.setItems(FXCollections.observableArrayList(Main.getCities()));
        
        comboPartenzaCitta.getSelectionModel().select(0);
        comboArrivoCitta.getSelectionModel().select(0);
        
        ObservableList<String> tags = FXCollections.observableArrayList(Main.getTags());
        lvTags.setItems(tags);
        lvTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        populateTagsListCell();
        
        Platform.runLater(() -> {
        	
        	try {
	        	imgBackground.fitWidthProperty().bind(Main.getCurrentWindow().widthProperty());
	        	imgBackground.setFitHeight(0.0);
	        	imgBackground.setPreserveRatio(true);
        	} catch (NullPointerException e) {
        		e.printStackTrace();
				System.exit(-1);
			}
        	
        	
        	gridPaneColumns = (((Double)Main.getCurrentWindow().getWidth()).intValue()-16-(getNumViaggi())*20)/420;			
        	
        	
        	try {
        		HomepageControllerApp homepageControllerApp = new HomepageControllerApp();
				this.eventListBean = homepageControllerApp.getEventByCities(Main.getBundle().getString(ConstantStrings.ALL_FEMALE), Main.getBundle().getString(ConstantStrings.ALL_FEMALE));
				
        	} catch (InternalDBException idbe) {
        		TimedAlert.show(AlertType.ERROR,
    					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
    					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
    					idbe.getMessage(), Main.getLogFile());
    			
    			Main.setRoot("Homepage");
			}
        	
        	this.eventNodeList = ConvertMethods.eventsToNodeList(eventListBean);
        	gridViaggi.populateGrid(eventNodeList, gridPaneColumns);
        	
        	Main.getCurrentWindow().widthProperty().addListener((obs, oldVal, newVal) -> {            	
		    	int o = oldVal.intValue()-16-getNumViaggi()*20;
		    	int n = newVal.intValue()-16-getNumViaggi()*20;
		    	            	
		    	if(o/viaggioBoxWidth != n/viaggioBoxWidth) {
		    		gridPaneColumns = n/viaggioBoxWidth;
		    		try {
		    			gridViaggi.populateGrid(eventNodeList, gridPaneColumns);
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	}
            });
        	
        	imgBackground.setVisible(true);
        	pnlMain.setVisible(true);
        	spEventList.setVisible(true);
        });
    }
    
    private void populateTagsListCell() 
    {
    	lvTags.setCellFactory(lv -> {
    		
    		TagsListCell cell = new TagsListCell();

            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                if (cell.isEmpty()) {
                    return;
                }

                int index = cell.getIndex();
                if (lvTags.getSelectionModel().getSelectedIndices().contains(index))
                	lvTags.getSelectionModel().clearSelection(index);
                else
                	lvTags.getSelectionModel().select(index);

                lvTags.requestFocus();

                e.consume();
            });
            return cell ;
        });
    }
    
    public int getNumViaggi() 
    {
    	return gridViaggi.getColumnCount();
    }
    
    @FXML
    private void enableDepartureCitta()
    {   	
    	if(radioPartenzaCitta.isSelected()) {
	    	sliderPartenzaDistanza.setDisable(true);
	    	lblPartenzaDistanza.setDisable(true);
	    	comboPartenzaDistanza.setDisable(true);
	    	comboPartenzaCitta.setDisable(false);
	    	
	    	if(!radioArrivoDistanza.isSelected())
	    		btnSearch.setDisable(false);
    	}
    }
    
    @FXML
    private void enableDepartureDistance()
    {
    	if(radioPartenzaDistanza.isSelected()) {
        	sliderPartenzaDistanza.setDisable(false);
        	lblPartenzaDistanza.setDisable(false);
        	comboPartenzaDistanza.setDisable(false);
        	comboPartenzaCitta.setDisable(true);
        	
        	btnSearch.setDisable(true);
    	}
    }
    
    @FXML
    private void enableDestinationCitta() 
    {
    	if(radioArrivoCitta.isSelected()) {
	    	sliderArrivoDistanza.setDisable(true);
	    	lblArrivoDistanza.setDisable(true);
	    	comboArrivoDistanza.setDisable(true);
	    	comboArrivoCitta.setDisable(false);
	    	
	    	if(!radioPartenzaDistanza.isSelected())
	    		btnSearch.setDisable(false);
    	}
    }
    
    @FXML
    private void enableDestinationDistance()
    {
    	if(radioArrivoDistanza.isSelected()) {
        	sliderArrivoDistanza.setDisable(false);
        	lblArrivoDistanza.setDisable(false);
        	comboArrivoDistanza.setDisable(false);
        	comboArrivoCitta.setDisable(true);
        	btnSearch.setDisable(true);
    	}
    }
    
    @FXML
    private void search()
    {
    	List<EventBean> searchedEventList = new ArrayList<>();
    	
    	try {
    		HomepageControllerApp homepageControllerApp = new HomepageControllerApp();
			searchedEventList = homepageControllerApp.getEventByCitiesAndTags(comboPartenzaCitta.getValue(), comboArrivoCitta.getValue(), lvTags.getSelectionModel().getSelectedItems());
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
			
			Main.setRoot("Homepage");
		}
    	
    	
    	this.eventNodeList = ConvertMethods.eventsToNodeList(searchedEventList);
    	gridViaggi.populateGrid(eventNodeList, gridPaneColumns);
    }

}
