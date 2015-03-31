package com.leandroideias.falling.numbers.tela;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.leandroideias.falling.numbers.MainActivity;
import com.leandroideias.falling.numbers.MyApplication;
import com.leandroideias.falling.numbers.PlayTask.Difficulty;
import com.leandroideias.falling.numbers.R;

public class ForceLoginDialogFragment extends DialogFragment implements View.OnClickListener{
	private Typeface tf;
	private TextView explainingText;
	private SignInButton signInButton;
	private Button cancelButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tf = ((MyApplication) getActivity().getApplication()).getCustomLayout().getTypeface();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		getDialog().setTitle(R.string.falling_numbers);
		View view = inflater.inflate(R.layout.force_login_dialog_fragment, container, false);
		
		explainingText = (TextView) view.findViewById(R.id.explainingText);
		signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
		cancelButton = (Button) view.findViewById(R.id.cancelButton);
		
		explainingText.setTypeface(tf);
		cancelButton.setTypeface(tf);
		
		setListeners();
		
		return view;
	}
	
	private void setListeners(){
		signInButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v){
		if(v.getId() == R.id.sign_in_button){
			((MainActivity) getActivity()).getGameControl().signInUser();
		} else if(v.getId() == R.id.cancelButton){
			dismiss();
		}
	}
	
	public void onSignInSucceeded(){
		dismiss();
	}
}
