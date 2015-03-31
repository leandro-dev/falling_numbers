package com.leandroideias.falling.numbers.tela;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.leandroideias.falling.numbers.MainActivity;
import com.leandroideias.falling.numbers.R;

public class MyDialogFragment extends DialogFragment {
	private int dialogTitleId;
	private int stringArrayId;
	private ListView listView;
	
	public static MyDialogFragment newInstance(int dialogTitleId, int stringArrayId){
		MyDialogFragment fragment = new MyDialogFragment();
		
		Bundle args = new Bundle();
		args.putInt("stringArrayId", stringArrayId);
		args.putInt("dialogTitleId", dialogTitleId);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stringArrayId = getArguments().getInt("stringArrayId");
		dialogTitleId = getArguments().getInt("dialogTitleId");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		getDialog().setTitle(dialogTitleId);
		View view = inflater.inflate(R.layout.my_dialog_fragment, container, false);
		
		listView = (ListView) view.findViewById(R.id.listView);
		String[] values = getActivity().getResources().getStringArray(stringArrayId);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),  android.R.layout.simple_list_item_1, values);
		listView.setAdapter(adapter);
		
		setListeners();
		
		return view;
	}
	
	private void setListeners(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				((MainActivity) getActivity()).getGameControl().myDialogFragmentItemSelected(dialogTitleId, position);
				dismiss();
			}
		});
	}
}
