package com.ispw.simplecalculator;

import javafx.geometry.Insets;
import javafx.scene.control.Button;

public class CalcButton extends Button {
	
	public CalcButton(String text) {
		super(text);
		
		this.setMinHeight(SimpleCalculator.size);
		this.setMaxHeight(SimpleCalculator.size);
		this.setMinWidth(SimpleCalculator.size);
		this.setMaxWidth(SimpleCalculator.size);
				
		this.setPadding(new Insets(0));
	}

	@Override
	public void requestFocus() {
		return;
	}
}
