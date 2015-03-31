package com.leandroideias.falling.numbers.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.leandroideias.falling.numbers.R;
import com.leandroideias.falling.numbers.tela.PlayGame;

public class KeyboardHandler {
	private PlayGame playGame;
	private Button bottomButtons[] = new Button[10];
	private Button sideButtons[] = new Button[10];
	private View leftFrag;
	private View rightFrag;
	private View bottomFrag;
	
	public KeyboardHandler(PlayGame playGame, View view){
		this.playGame = playGame;
		Context context = playGame.getActivity().getApplicationContext();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		String buttonsStyle = sharedPreferences.getString("keyboard_preference", context.getString(R.string.default_keyboard_preference));
		/*
		 *	<item>bottom</item>
		 *  <item>sides</item>
		 *  <item>invisible</item>
		 */

		leftFrag = view.findViewById(R.id.includeLeft);
		rightFrag = view.findViewById(R.id.includeRight);
		bottomFrag = view.findViewById(R.id.includeBottom);
		
		if(buttonsStyle.equals("invisible")){
			leftFrag.setVisibility(View.GONE);
			rightFrag.setVisibility(View.GONE);
			bottomFrag.setVisibility(View.GONE);
		} else if(buttonsStyle.equals("bottom")){
			leftFrag.setVisibility(View.GONE);
			rightFrag.setVisibility(View.GONE);
			bottomFrag.setVisibility(View.VISIBLE);
			bottomButtons[0] = (Button) view.findViewById(R.id.buttonbottom0);
			bottomButtons[1] = (Button) view.findViewById(R.id.buttonbottom1);
			bottomButtons[2] = (Button) view.findViewById(R.id.buttonbottom2);
			bottomButtons[3] = (Button) view.findViewById(R.id.buttonbottom3);
			bottomButtons[4] = (Button) view.findViewById(R.id.buttonbottom4);
			bottomButtons[5] = (Button) view.findViewById(R.id.buttonbottom5);
			bottomButtons[6] = (Button) view.findViewById(R.id.buttonbottom6);
			bottomButtons[7] = (Button) view.findViewById(R.id.buttonbottom7);
			bottomButtons[8] = (Button) view.findViewById(R.id.buttonbottom8);
			bottomButtons[9] = (Button) view.findViewById(R.id.buttonbottom9);
			
			for(int a = 0; a < 10; a++){
				bottomButtons[a].setOnClickListener(new MyOnClickListener(a));
			}
		} else if(buttonsStyle.equals("sides")){
			leftFrag.setVisibility(View.VISIBLE);
			rightFrag.setVisibility(View.VISIBLE);
			bottomFrag.setVisibility(View.GONE);
			sideButtons[0] = (Button) view.findViewById(R.id.buttonsides0);
			sideButtons[1] = (Button) view.findViewById(R.id.buttonsides1);
			sideButtons[2] = (Button) view.findViewById(R.id.buttonsides2);
			sideButtons[3] = (Button) view.findViewById(R.id.buttonsides3);
			sideButtons[4] = (Button) view.findViewById(R.id.buttonsides4);
			sideButtons[5] = (Button) view.findViewById(R.id.buttonsides5);
			sideButtons[6] = (Button) view.findViewById(R.id.buttonsides6);
			sideButtons[7] = (Button) view.findViewById(R.id.buttonsides7);
			sideButtons[8] = (Button) view.findViewById(R.id.buttonsides8);
			sideButtons[9] = (Button) view.findViewById(R.id.buttonsides9);
			
			for(int a = 0; a < 10; a++){
				sideButtons[a].setOnClickListener(new MyOnClickListener(a));
			}
		}
	}
	
	private class MyOnClickListener implements View.OnClickListener{
		private int number;
		public MyOnClickListener(int number){
			this.number = number;
		}
		public void onClick(View v){
			playGame.pressedNumber(number);
		}
	};
}
