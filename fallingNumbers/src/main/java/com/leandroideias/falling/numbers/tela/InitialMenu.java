package com.leandroideias.falling.numbers.tela;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;
import com.leandroideias.falling.numbers.MainActivity;
import com.leandroideias.falling.numbers.MyApplication;
import com.leandroideias.falling.numbers.R;

public class InitialMenu extends Fragment{
	private Typeface tf;
	private Button playButton;
	private Button rankButton;
	private Button achievementsButton;
	private Button settingsButton;
	private SignInButton signInButton;
	private Button signOutButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tf = ((MyApplication) getActivity().getApplication()).getCustomLayout().getTypeface();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_initial, container, false);
		
		playButton = (Button) view.findViewById(R.id.playButton);
		rankButton = (Button) view.findViewById(R.id.rankButton);
		achievementsButton = (Button) view.findViewById(R.id.achievementsButton);
		settingsButton = (Button) view.findViewById(R.id.settingsButton);
		signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
		signOutButton = (Button) view.findViewById(R.id.sign_out_button);
		
		playButton.setTypeface(tf);
		rankButton.setTypeface(tf);
		achievementsButton.setTypeface(tf);
		settingsButton.setTypeface(tf);
		
		if(((MainActivity) getActivity()).isConnected()){
			signInCompleted();
		} else {
			showSignInButton();
		}
		
		setListeners();
		
		return view;
	}
	
	private void setListeners(){
		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				((MainActivity) getActivity()).getGameControl().goToSelectDifficulty();
			}
		});
		rankButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((MainActivity) getActivity()).getGameControl().goToRankFrag();
			}
		});
		achievementsButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				((MainActivity) getActivity()).getGameControl().showAchievements();
			}
		});
		settingsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				((MainActivity) getActivity()).getGameControl().goToSettingsScreen();
			}
		});
		signInButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				((MainActivity) getActivity()).getGameControl().signInUser();
			}
		});
		signOutButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				((MainActivity) getActivity()).getGameControl().signOutUser();
				showSignInButton();
			}
		});
	}
	
	public void signInCompleted(){
		signInButton.setVisibility(View.GONE);
		signOutButton.setVisibility(View.VISIBLE);
	}
	
	public void showSignInButton(){
		signInButton.setVisibility(View.VISIBLE);
		signOutButton.setVisibility(View.GONE);
	}
}
