package com.leandroideias.falling.numbers.canvas;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.View;

import com.leandroideias.falling.numbers.util.Number;

public class NumbersView extends View {
	public static final int QUANT_LINES = 20;
	private int digitoPixels;
	private float textSize;
	private LinkedList<Number> list = null;
	private Paint paint;
	float proportion = 0;

	public NumbersView(Context context) {
		this(context, null);
	}
	public NumbersView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NumbersView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
		paint.setTypeface(Typeface.MONOSPACE);
		list = new LinkedList<Number>();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);
		
		if(changed){
			/* Calculando o tamanho do texto */
			textSize = ((float)getHeight())/QUANT_LINES;
			paint.setTextSize(textSize);
			digitoPixels = (int) FloatMath.ceil(paint.measureText("8"));
		}
	};
	
	public int getDigitoPixels(){
		return digitoPixels;
	}
	
	public float getTextSize(){
		return textSize;
	}
	
	public void setList(LinkedList<Number> list){
		this.list = list;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		synchronized(list){
			int tam = list.size();
			Number numero;
			for(int a = 0; a < tam; a++){
				numero = list.get(a);
				canvas.drawText("" + numero.getNumero(), numero.getPosX(), numero.getPosY(), paint);
			}
		}
	}
}
