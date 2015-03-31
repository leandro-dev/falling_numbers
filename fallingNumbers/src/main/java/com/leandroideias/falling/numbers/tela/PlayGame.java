package com.leandroideias.falling.numbers.tela;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.leandroideias.falling.numbers.MainActivity;
import com.leandroideias.falling.numbers.MyApplication;
import com.leandroideias.falling.numbers.PlayTask;
import com.leandroideias.falling.numbers.PlayTask.Difficulty;
import com.leandroideias.falling.numbers.R;
import com.leandroideias.falling.numbers.canvas.AnimateStartView;
import com.leandroideias.falling.numbers.canvas.NumbersView;
import com.leandroideias.falling.numbers.util.KeyboardHandler;

public class PlayGame extends Fragment implements View.OnKeyListener{
	private enum State{
		BEFORE, PLAYING, PAUSED, STOPPED
	};
	private State state;
	private AnimateStartView animateStartView;
	private View gameLayout;
	private Handler handler;
	private PlayTask playTask;
	private NumbersView numbersView;
	private Difficulty difficulty;
	private KeyboardHandler keyboardHandler;
	
	//Menu in-game
	private View menuInGameView;
	private Typeface tf;
	private Button resumeButton;
	private Button restartButton;
	private Button giveupButton;
	
	public void setDifficulty(Difficulty difficulty){
		this.difficulty = difficulty;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = State.BEFORE;
		handler = new Handler();
		tf = ((MyApplication) getActivity().getApplication()).getCustomLayout().getTypeface();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.game_play, container, false);
		
//		view.setOnKeyListener(this);
		
		animateStartView = (AnimateStartView) view.findViewById(R.id.animateStartView);
		gameLayout = view.findViewById(R.id.gameLayout);
		numbersView = (NumbersView) view.findViewById(R.id.numbersView);

		menuInGameView = view.findViewById(R.id.menuInGame);
		resumeButton = (Button) view.findViewById(R.id.resumeButton);
		restartButton = (Button) view.findViewById(R.id.restartButton);
		giveupButton = (Button) view.findViewById(R.id.giveupButton);

		resumeButton.setTypeface(tf);
		restartButton.setTypeface(tf);
		giveupButton.setTypeface(tf);
		
		keyboardHandler = new KeyboardHandler(this, view);
		
		setListeners();
		
		return view;
	}
	
	private void setListeners(){
		resumeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				resume();
			}
		});
		restartButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				playTask.clearGame();
				gameLayout.setVisibility(View.INVISIBLE);
				animateStartView.setVisibility(View.VISIBLE);
				menuInGameView.setVisibility(View.INVISIBLE);
				animateStartView.startTimer(PlayGame.this);
			}
		});
		giveupButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				playTask.myEnd(true);
				((MainActivity) getActivity()).getGameControl().backToInitialMenu();
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(state == State.BEFORE){
			animateStartView.startTimer(this);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(state == State.BEFORE){
			animateStartView.cancelTimer();
		} else if(state == State.PLAYING){
			pause();
		}
	}
	
	public void play(){
		if(state == State.BEFORE){
			animateStartView.setVisibility(View.GONE);
			gameLayout.setVisibility(View.VISIBLE);
			if(state == State.BEFORE){
				animateStartView.setVisibility(View.VISIBLE);
				playTask = new PlayTask(this, numbersView, difficulty);
				playTask.start();
			}
		} else if(state == State.PAUSED){
			resume();
		}
		state = State.PLAYING;
	}
	
	public void pause(){
		gameLayout.setVisibility(View.INVISIBLE);
		animateStartView.setVisibility(View.INVISIBLE);
		menuInGameView.setVisibility(View.VISIBLE);
		playTask.myPause();
		state = State.PAUSED;
	}
	
	public void resume(){
		if(state == State.PAUSED){
			playTask.myResume();
			gameLayout.setVisibility(View.VISIBLE);
			animateStartView.setVisibility(View.INVISIBLE);
			menuInGameView.setVisibility(View.INVISIBLE);
			state = State.PLAYING;
		}
	}
	
	public Handler getHandler(){
		return handler;
	}
	
	public void endGame(long score){
		state = State.STOPPED;
		((MainActivity) getActivity()).getGameControl().endedGame(difficulty, score);
	}
	
	
	public void menuPressed(){
		if(state == State.PLAYING) pause();
	}
	
	public void backPressed(){
		if(state == State.BEFORE) ((MainActivity) getActivity()).getGameControl().backToSelectDifficulty();
		else if(state == State.PLAYING) pause();
	}
	
	public void pressedNumber(int number){
		if(state == State.PLAYING) playTask.typedNumber(number);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event){
		Log.i("Teste", "ae: " + keyCode + "...");
		int numberPressed;
		switch(keyCode){
		case KeyEvent.KEYCODE_0: numberPressed = 0; break;
		case KeyEvent.KEYCODE_1: numberPressed = 1; break;
		case KeyEvent.KEYCODE_2: numberPressed = 2; break;
		case KeyEvent.KEYCODE_3: numberPressed = 3; break;
		case KeyEvent.KEYCODE_4: numberPressed = 4; break;
		case KeyEvent.KEYCODE_5: numberPressed = 5; break;
		case KeyEvent.KEYCODE_6: numberPressed = 6; break;
		case KeyEvent.KEYCODE_7: numberPressed = 7; break;
		case KeyEvent.KEYCODE_8: numberPressed = 8; break;
		case KeyEvent.KEYCODE_9: numberPressed = 9; break;
		default: numberPressed = -1;
		}
		if(numberPressed != -1){
			pressedNumber(numberPressed);
			return true;
		}
		return false;
	}
}
