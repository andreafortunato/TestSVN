package com.ispw.exam.simplecalculator.customcontrol;

import com.ispw.exam.simplecalculator.Main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class CustomResultTextField extends TextField {
	
	public CustomResultTextField() {
		super();
				
		this.setMinHeight(Main.SIZE);
		this.setMaxHeight(Main.SIZE);
		this.setMinWidth(Main.SIZE*4 + Main.GAP*3);
		this.setMaxWidth(Main.SIZE*4 + Main.GAP*3);
		
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
