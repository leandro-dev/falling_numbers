package com.leandroideias.falling.numbers.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.TextView;

public class CustomLayout {
	Typeface tf;
	
	public CustomLayout(Context context){
		tf = Typeface.createFromAsset(context.getAssets(), "fonts/Romy.ttf");
	}
	
	public Typeface getTypeface(){
		return tf;
	}
	
	public void customizeFont(TextView view){
		view.setTypeface(tf);
		view.setTextColor(Color.WHITE);
	}
	public void customizeFont(TextView[] views){
		int i = views.length;
		for(int a = 0; a < i; a++) customizeFont(views[a]);
	}
	
	public void setFontSize(float screenSize, TextView[] texts){
		float tam = screenSize/5;
		int i = texts.length;
		for(int a = 0; a < i; a++){
			texts[a].setTextSize(TypedValue.COMPLEX_UNIT_PX, tam);
		}
	}
}
