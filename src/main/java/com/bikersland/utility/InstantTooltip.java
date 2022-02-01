package com.bikersland.utility;

import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class InstantTooltip extends Tooltip {
	public InstantTooltip() 
	{
		super();
		
		this.instant();
	}
	public InstantTooltip(String text) 
	{
		super(text);
		
		this.instant();
	}
	
	private void instant() 
	{
		setShowDelay(Duration.ZERO);
		setHideDelay(Duration.ZERO);
		setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, 13));
	}
}
