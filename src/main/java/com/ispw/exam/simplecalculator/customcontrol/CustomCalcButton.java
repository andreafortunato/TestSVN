package com.ispw.exam.simplecalculator.customcontrol;

import com.ispw.exam.simplecalculator.Main;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class CustomCalcButton extends Button {

	public CustomCalcButton() {
		super();
	}

	public CustomCalcButton(String text) {
		super(text);
		
		this.setMinHeight(Main.SIZE);
		this.setMaxHeight(Main.SIZE);
		this.setMinWidth(Main.SIZE);
		this.setMaxWidth(Main.SIZE);
				
		this.setPadding(new Insets(0));
		
		this.setFont(Font.font(27));

	}
	
	@Override
	public void requestFocus() {
	}
}
