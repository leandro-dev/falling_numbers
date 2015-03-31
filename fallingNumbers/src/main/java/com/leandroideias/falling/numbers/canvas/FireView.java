package com.leandroideias.falling.numbers.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class FireView extends View {
	private Paint paint;
	
	public FireView(Context context) {
		this(context, null);
	}

	public FireView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FireView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
		paint.setColor(Color.RED);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawLine(0, getHeight()-1, getWidth(), getHeight()-1, paint);
	}
	
}
