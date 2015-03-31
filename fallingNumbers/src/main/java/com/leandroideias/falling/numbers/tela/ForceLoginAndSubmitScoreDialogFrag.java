package com.leandroideias.falling.numbers.tela;

import android.os.Bundle;

import com.leandroideias.falling.numbers.MainActivity;
import com.leandroideias.falling.numbers.PlayTask.Difficulty;

public class ForceLoginAndSubmitScoreDialogFrag extends ForceLoginDialogFragment {
	private long score;
	private Difficulty difficulty;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		score = getArguments().getLong("score", -1);
		difficulty = (Difficulty) getArguments().getSerializable("difficulty");
		if(score == -1) throw new IllegalArgumentException("You forgot to send the score..");
	}
	
	@Override
	public void onSignInSucceeded(){
		super.onSignInSucceeded();
		((MainActivity) getActivity()).getGameControl().sendScore(difficulty, score);
	}
}
