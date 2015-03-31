package com.leandroideias.falling.numbers.canvas;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.leandroideias.falling.numbers.MyApplication;
import com.leandroideias.falling.numbers.R;
import com.leandroideias.falling.numbers.tela.PlayGame;

public class AnimateStartView extends FrameLayout {
	private AnimateStartAsyncTask animateStartAsyncTask;
	private TextView topText;
	private TextView mainText;
	private boolean cancelCountdownTimer;
	private Typeface tf;
	
	public AnimateStartView(Context context) {
		super(context);
		initView(context);
	}
	
	public AnimateStartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public AnimateStartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.get_ready, this);
		
		topText = (TextView) findViewById(R.id.topText);
		mainText = (TextView) findViewById(R.id.mainText);
		tf = ((MyApplication) context.getApplicationContext()).getCustomLayout().getTypeface();
		
		topText.setTypeface(tf);
		mainText.setTypeface(tf);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		invalidate();
	}
	
	public void startTimer(PlayGame playGame){
		cancelCountdownTimer = false;
		animateStartAsyncTask = new AnimateStartAsyncTask(playGame);
		animateStartAsyncTask.execute();
	}
	
	public void cancelTimer(){
		cancelCountdownTimer = true;
		animateStartAsyncTask.cancel(true);
	}
	
	/*
	 * CountDownTimer didn't seem to work well... so I replaced it for something better :)
	 */
	private class AnimateStartAsyncTask extends AsyncTask<Void, Integer, Void>{
		private PlayGame playGame;
		private Animation animation;
		AnimateStartAsyncTask(PlayGame playGame){
			this.playGame = playGame;
		}
		@Override
		protected void onPreExecute() {
			topText.setVisibility(View.VISIBLE);
			mainText.setText("");
//			playGame.clearGame();
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			mainText.setText(""+values[0]);
			animation = new AlphaAnimation(1, 0);
			animation.setStartOffset(0);
			animation.setDuration(1000);
			mainText.setAnimation(animation);
		}
		@Override
		protected Void doInBackground(Void... params) {
			int a;
			for(a = 0; a < 3; a++){
				publishProgress(3-a);
				synchronized(this){
					try{
						wait(1000);
					} catch(InterruptedException e){}
				}
				if(cancelCountdownTimer){
					cancel(true);
					break;
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			topText.setVisibility(View.INVISIBLE);
			mainText.setText(R.string.go);
			animation = new AlphaAnimation(1, 0);
			animation.setStartOffset(0);
			animation.setDuration(1000);
			animation.setFillAfter(true);
			mainText.setAnimation(animation);
			playGame.play();
		}
	};
}
