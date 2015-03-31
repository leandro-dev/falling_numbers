package com.leandroideias.falling.numbers.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LinesView extends View{
	private Paint paint;
	public LinesView(Context context) {
		this(context, null);
	}
	public LinesView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LinesView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
		paint.setColor(Color.GREEN);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float tam = getHeight();
		for(float a = 0; a <= tam; a += tam/5){
			canvas.drawLine(0, a, getWidth(), a, paint);
		}
	}
}
