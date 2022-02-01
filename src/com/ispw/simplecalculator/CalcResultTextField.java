package com.ispw.simplecalculator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class CalcResultTextField extends TextField {

	public CalcResultTextField() {
		super();
				
		this.setMinHeight(SimpleCalculator.size);
		this.setMaxHeight(SimpleCalculator.size);
		this.setMinWidth(SimpleCalculator.size*4 + 5*3);
		this.setMaxWidth(SimpleCalculator.size*4 + 5*3);
		
		this.setFont(Font.font(33.0));
		
		this.setPadding(new Insets(2));

		this.setEditable(false);
		this.setAlignment(Pos.CENTER_RIGHT);
	}
	
	@Override
	public void requestFocus() {
		return;
	}
}
