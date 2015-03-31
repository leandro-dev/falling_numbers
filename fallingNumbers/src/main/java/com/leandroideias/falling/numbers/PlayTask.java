package com.leandroideias.falling.numbers;

import java.util.Calendar;
import java.util.LinkedList;

import android.util.Log;

import com.leandroideias.falling.numbers.canvas.NumbersView;
import com.leandroideias.falling.numbers.tela.PlayGame;
import com.leandroideias.falling.numbers.util.Number;

/**
 * An Thread that is responsible to control the game. It was an AsyncTask, but since I didn't find a way to pause/resume, 
 * it was necessary to change to a simple Thread.
 * @author Leandro
 */
public class PlayTask extends Thread{
	public enum Difficulty{
		BEGGINER, EASY, MEDIUM, HARD, INSANE, IMPOSSIBLE
	}
	private static final int FPS = 25;
	private static final long DEFAULT_HEATING_TICKS = 10000/FPS; //10+2=12 seconds to warm up before it starts to get harder.
	private static final long DEFAULT_CONSTANT_INCREMENT_TICKS = 22900; //A good approximation to use.
	private static final long DEFAULT_CONSTANT_INCREMENT_VPT_UNIT = 700;
	private PlayGame playGame; //Instance of the game to post the results.
	private NumbersView view;
	private boolean finished;
	private boolean paused;
	private long heatingTicks;
	private long gameTicks;
	private long gameTicks2;
	private float vpt = 3.5f; //Velocity per tick - the number of pixels that will be added for each Number for each tick.
	private int defaultCreationRatio = 20;
	private int creationRatio = 20; //Numbers of ticks necessary to wait to create another number.
	private float screenHeight; //Height of the screen, used to calculate precisely the vpt without benefiting.
	private float tickIncrement; //Amount to increment on vpt everytime the game gets harder.
	private LinkedList<Number> list;
	private long score;
	private boolean silentMode; //To know when to show notifications to UI thread or not.
	private Difficulty mDifficulty;
	private long ticksToHarder;
	
	/**
	 * @param playGame The game instance to post progress and result on it.
	 * @param view The game view that will be refreshed everytime that needed.
	 * @param difficulty The initial difficulty of the game.
	 */
	public PlayTask(PlayGame playGame, NumbersView view, Difficulty difficulty){
		this.mDifficulty = difficulty;
		this.playGame = playGame;
		this.view = view;
		heatingTicks = DEFAULT_HEATING_TICKS;
		gameTicks = 0;
		gameTicks2 = 0;
		list = new LinkedList<Number>();
		view.setList(list);
	}
	
	private void createNumber(){
		long maxValue = view.getWidth() - view.getDigitoPixels();
		long posX = (long) (Math.random()*maxValue);
		int numero = (int) (Math.random()*10);
		list.add(new Number(numero, posX));

	}
	
	public void setView(NumbersView view) throws IllegalArgumentException{
		if(view == null) throw new IllegalArgumentException("The view can't be null.");
		this.view = view;
	}
	
	public void myPause(){
		this.paused = true;
	}
	public void myResume(){
		synchronized(this){
			this.notify();
		}
		this.paused = false;
	}
	public void myEnd(boolean silentMode){
		this.silentMode = silentMode;
		this.finished = true;
		if(this.paused) myResume();
	}
	public void clearGame(){
		list.clear();
		heatingTicks = DEFAULT_HEATING_TICKS;
		gameTicks = 0;
		gameTicks2 = 0;
		onPreExecute();
		view.invalidate();
	}
	
	@Override
	public void run(){
		onPreExecute();
		Boolean result = doInBackground();
		onPostExecute(result);
	}
	

	
	/**
	 * Makes the game a little more difficult.
	 */
	public void harder(){
		Log.i("Teste", "hardered");
		if(mDifficulty == Difficulty.EASY){
			vpt += 0.3f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT;
			ticksToHarder *= 2;
			
			gameTicks = creationRatio - gameTicks;
			creationRatio = (int) Math.ceil((defaultCreationRatio * (3.5f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT)) / vpt);
			gameTicks = creationRatio - gameTicks;
		} else if(mDifficulty == Difficulty.MEDIUM){
			vpt += 0.3f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT;
			ticksToHarder *= 1.6;

			gameTicks = creationRatio - gameTicks;
			creationRatio = (int) Math.ceil((defaultCreationRatio * (4.0f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT)) / vpt);
			gameTicks = creationRatio - gameTicks;
		} else if(mDifficulty == Difficulty.HARD){
			vpt += 0.3f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT;
			ticksToHarder *= 2;
			
			gameTicks = creationRatio - gameTicks;
			creationRatio = (int) Math.ceil((defaultCreationRatio * (4.0f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT)) / vpt);
			gameTicks = creationRatio - gameTicks;
		} else if(mDifficulty == Difficulty.INSANE){
			vpt += 0.3f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT;
			ticksToHarder *= 2;
			
			gameTicks = creationRatio - gameTicks;
			creationRatio = (int) Math.ceil((defaultCreationRatio * (3.5f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT)) / vpt);
			gameTicks = creationRatio - gameTicks;
		} else {
			vpt += 0.3f;
		}
	}
	
	protected void onPreExecute() {
		finished = false;
		paused = false;
		score = 0;
		silentMode = false;
		screenHeight = playGame.getActivity().findViewById(R.id.numbersView).getHeight();
		tickIncrement = screenHeight/DEFAULT_CONSTANT_INCREMENT_TICKS;
		
		if(mDifficulty == Difficulty.EASY){
			ticksToHarder = 5000/FPS; //5 seconds.
			defaultCreationRatio = 1000/FPS; // 1 second.
			vpt = 3.5f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT;
		} else if(mDifficulty == Difficulty.MEDIUM){
			ticksToHarder = 4000/FPS;
			defaultCreationRatio = 700/FPS;
			vpt = 4.0f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT;
		} else if(mDifficulty == Difficulty.HARD){
			ticksToHarder = 15000/FPS;
			defaultCreationRatio = 450/FPS;
			vpt = 4.0f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT;
		} else if(mDifficulty == Difficulty.INSANE){
			ticksToHarder = 60000/FPS;
			defaultCreationRatio = 300/FPS;
			vpt = 3.5f*screenHeight/DEFAULT_CONSTANT_INCREMENT_VPT_UNIT;
		} else {
			ticksToHarder = Long.MAX_VALUE-1;
		}
		creationRatio = defaultCreationRatio;
		gameTicks = ticksToHarder;
	}
	
	protected Boolean doInBackground() {
		long tickPS = 1000/FPS;
		long startTime, sleepTime;
		Calendar calendar = Calendar.getInstance();
		while(true){
			startTime = calendar.getTimeInMillis();
			if(paused){
				try{
					synchronized(this){
						wait();
					}
				} catch(InterruptedException e){
					if(paused){
						//Oops, I think something wrong happened.. I must see the log
						finished = true;
						Log.e("Falling Numbers", "PlayTask thread unpaused", e);
					} else {
						//UI thread unpaused :)
					}
				}
			}
			if(finished){
				break;
			}
			
			/* Do the play here */
			int quant = list.size();
			Number number;
			if(quant > 0 && list.get(0).getPosY() + vpt >= NumbersView.QUANT_LINES*view.getTextSize()){
				//Para teste
				/*if(true){
					typedNumber(list.peek().getNumero());
					quant--;
				} else {*/
				//End the game. The player lost. You lost too.
				Log.i("Teste", "acabou. perdeu");
				return false;
			}
			for(int a = 0; a < quant; a++){
				number = list.get(a);
				number.setPosY(number.getPosY() + vpt);
			}
			view.postInvalidate();
			
			//Update the ticks
			if(gameTicks%creationRatio == 0 && gameTicks > 0){
				createNumber();
				gameTicks = 0;
			}
			if(heatingTicks > 0){
				heatingTicks--;
			} else {
				if(gameTicks2%ticksToHarder == 0){
					gameTicks2 = 0;
					harder();
				}
				gameTicks2++;
			}
			gameTicks++;
			
			/* Wait to complete the cycle */
			sleepTime = tickPS - (calendar.getTimeInMillis() - startTime);
			try{
				synchronized(this){
					if(sleepTime > 0){
						wait(sleepTime);
					} else {
						//The processor is too busy to play.. let's give him some more time.
						wait(10);
					}
				}
			} catch(InterruptedException e){
				//Oops, nobody should have stopped this guy..
				Log.e("Falling Numbers", "PlayTask thread interrupted brutally", e);
				//Let's pretend nothing happened :)
			}
		}
		return null;
	};
	
	protected void onPostExecute(Boolean result){
		if(!silentMode){
			playGame.getHandler().post(new Runnable() {
				public void run() {
					playGame.endGame(score);
				}
			});
		}
	}
	
	public void typedNumber(int numberValue){
		Number number = list.peek();
		if(number != null && number.getNumero() == numberValue){
			synchronized(list){
				list.poll();
			}
			score += number.getNumero();
//			if(list.size() == 0){
//				createNumber();
//			}
		}
	}
}
