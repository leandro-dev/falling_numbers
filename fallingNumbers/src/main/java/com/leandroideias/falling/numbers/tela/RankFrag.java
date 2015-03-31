package com.leandroideias.falling.numbers.tela;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leandroideias.falling.numbers.MainActivity;
import com.leandroideias.falling.numbers.MyApplication;
import com.leandroideias.falling.numbers.R;

public class RankFrag extends Fragment {
	private Typeface tf;
	private Button leftButton;
	private Button rightButton;
	private String leftButtonText;
	private String rightButtonText;
	private TextView rankTitle;
	private Button seeRankButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tf = ((MyApplication) getActivity().getApplication()).getCustomLayout().getTypeface();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.ranking_fragment, container, false);

		leftButton = (Button) view.findViewById(R.id.leftButton);
		rightButton = (Button) view.findViewById(R.id.rightButton);
		rankTitle = (TextView) view.findViewById(R.id.rankTitle);
		seeRankButton = (Button) view.findViewById(R.id.seeRankButton);

		leftButton.setTypeface(tf);
		rightButton.setTypeface(tf);
		rankTitle.setTypeface(tf);
		seeRankButton.setTypeface(tf);
		
		setListeners();
		
		return view;
	}
	
	private void setListeners(){
		leftButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				MyDialogFragment dialogFragment = MyDialogFragment.newInstance(R.string.see_rank_by, R.array.categoryTimePeriod);
				dialogFragment.show(getActivity().getSupportFragmentManager(), "rankSelectTimePeriod");
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				MyDialogFragment dialogFragment = MyDialogFragment.newInstance(R.string.select_difficulty, R.array.categoryDifficulty);
				dialogFragment.show(getActivity().getSupportFragmentManager(), "rankSelectDifficulty");
			}
		});
		seeRankButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				((MainActivity) getActivity()).getGameControl().showRank(leftButton.getText().toString(), rightButton.getText().toString());
			}
		});
	}
	
	public void dialogFragmentItemClicked(int dialogTitleId, int itemSelected){
		if(dialogTitleId == R.string.see_rank_by){
			leftButton.setText(getResources().getStringArray(R.array.categoryTimePeriod)[itemSelected]);
		} else {
			rightButton.setText(getResources().getStringArray(R.array.categoryDifficulty)[itemSelected]);
		}
	}
}
