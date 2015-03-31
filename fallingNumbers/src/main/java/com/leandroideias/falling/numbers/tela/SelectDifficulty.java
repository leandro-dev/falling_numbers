package com.leandroideias.falling.numbers.tela;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.leandroideias.falling.numbers.MainActivity.GameControl;
import com.leandroideias.falling.numbers.PlayTask.Difficulty;
import com.leandroideias.falling.numbers.MainActivity;
import com.leandroideias.falling.numbers.MyApplication;
import com.leandroideias.falling.numbers.R;

public class SelectDifficulty extends Fragment {
	private Typeface tf;
	private Button easyButton;
	private Button mediumButton;
	private Button hardButton;
	private Button insaneButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tf = ((MyApplication) getActivity().getApplication()).getCustomLayout().getTypeface();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.menu_select_difficulty, container, false);

		easyButton = (Button) view.findViewById(R.id.easyButton);
		mediumButton = (Button) view.findViewById(R.id.mediumButton);
		hardButton = (Button) view.findViewById(R.id.hardButton);
		insaneButton = (Button) view.findViewById(R.id.insaneButton);

		easyButton.setTypeface(tf);
		mediumButton.setTypeface(tf);
		hardButton.setTypeface(tf);
		insaneButton.setTypeface(tf);
		
		setListeners();
		
		return view;
	}
	
	private void setListeners(){
		easyButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((MainActivity) getActivity()).getGameControl().playGame(Difficulty.EASY);
			}
		});
		mediumButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((MainActivity) getActivity()).getGameControl().playGame(Difficulty.MEDIUM);
			}
		});
		hardButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((MainActivity) getActivity()).getGameControl().playGame(Difficulty.HARD);
			}
		});
		insaneButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((MainActivity) getActivity()).getGameControl().playGame(Difficulty.INSANE);
			}
		});
	}
}
