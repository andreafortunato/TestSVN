package com.bikersland.utility;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

@java.lang.SuppressWarnings("squid:MaximumInheritanceDepth")
public class CustomGridPane extends GridPane {

	public CustomGridPane() {
		super();
	}
	
	public void populateGrid(List<Node> nodeList, int columns) 
	{
		this.getChildren().clear();
		
    	int totalElements = nodeList.size();
    	
    	int row = 0;
    	int col = 0;
    	
    	while(totalElements > 0) {
            this.add(nodeList.get(nodeList.size()-totalElements), col, row);
            
            if(++col == columns) {
            	col = 0;
            	row++;
            }
            totalElements--;            
    	}
	}

}
