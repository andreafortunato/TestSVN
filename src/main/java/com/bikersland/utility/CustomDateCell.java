package com.bikersland.utility;

import java.time.LocalDate;

import javafx.scene.control.DateCell;

@java.lang.SuppressWarnings("squid:MaximumInheritanceDepth")
public class CustomDateCell extends DateCell {
	
	private LocalDate firstDate;
	
	public CustomDateCell(LocalDate firstDate) 
	{
		this.firstDate = firstDate;
	}

	@Override
	public void updateItem(LocalDate date, boolean empty)
	{
		super.updateItem(date, empty);
		
		setDisable(empty || date.compareTo(firstDate) < 0 );
	}
	
}
