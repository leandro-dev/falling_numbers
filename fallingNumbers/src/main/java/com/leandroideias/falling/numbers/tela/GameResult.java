package com.leandroideias.falling.numbers.tela;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leandroideias.falling.numbers.MainActivity;
import com.leandroideias.falling.numbers.MyApplication;
import com.leandroideias.falling.numbers.PlayTask.Difficulty;
import com.leandroideias.falling.numbers.R;

public class GameResult extends Fragment {
	private Typeface tf;
	private TextView youGotTV;
	private TextView scoreTV;
	private TextView pointsTV;
	private long score = -1;
	private Difficulty difficulty;
	private Button mainMenuButton;
	private Button sendScoreButton;
	private View buttonsBottom;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tf = ((MyApplication) getActivity().getApplication()).getCustomLayout().getTypeface();
		
		if(savedInstanceState != null){
			score = savedInstanceState.getLong("score");
			difficulty = (Difficulty) savedInstanceState.getSerializable("difficulty");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.game_result, container, false);

		youGotTV = (TextView) view.findViewById(R.id.you_got);
		scoreTV = (TextView) view.findViewById(R.id.scoreTV);
		pointsTV = (TextView) view.findViewById(R.id.points);
		mainMenuButton = (Button) view.findViewById(R.id.mainMenuButton);
		sendScoreButton = (Button) view.findViewById(R.id.sendScoreButton);
		buttonsBottom = view.findViewById(R.id.buttonsBottom);

		youGotTV.setTypeface(tf);
		scoreTV.setTypeface(tf);
		pointsTV.setTypeface(tf);
		mainMenuButton.setTypeface(tf);
		sendScoreButton.setTypeface(tf);
		
		if(savedInstanceState != null){
			score = savedInstanceState.getLong("score", -1);
			difficulty = (Difficulty) savedInstanceState.getSerializable("difficulty");
		}
		if(score != -1) scoreTV.setText("" + score);
		
		setListeners();
		
		return view;
	}
	
	private void setListeners(){
		mainMenuButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				((MainActivity) getActivity()).getGameControl().backToInitialMenu();
			}
		});
		sendScoreButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				((MainActivity) getActivity()).getGameControl().sendScore(difficulty, score);
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		new EnableButtonsAsyncTask().execute();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong("score", score);
		outState.putSerializable("difficulty", difficulty);
	}
	
	public void setScore(Difficulty difficulty, long score){
		this.score = score;
		this.difficulty = difficulty;
		if(scoreTV != null) scoreTV.setText("" + score);
	}
	
	private class EnableButtonsAsyncTask extends AsyncTask<Void, Void, Void>{
		protected Void doInBackground(Void... params){
			synchronized (this){
				try{
					wait(1500);
				} catch(Exception e){
					//Ignore
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			buttonsBottom.setVisibility(View.VISIBLE);
		}
	}
}
