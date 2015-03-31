package com.leandroideias.falling.numbers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.leandroideias.falling.numbers.PlayTask.Difficulty;
import com.leandroideias.falling.numbers.tela.ForceLoginAndSubmitScoreDialogFrag;
import com.leandroideias.falling.numbers.tela.ForceLoginDialogFragment;
import com.leandroideias.falling.numbers.tela.GameResult;
import com.leandroideias.falling.numbers.tela.InitialMenu;
import com.leandroideias.falling.numbers.tela.PlayGame;
import com.leandroideias.falling.numbers.tela.RankFrag;
import com.leandroideias.falling.numbers.tela.SelectDifficulty;
import com.leandroideias.falling.numbers.tela.UserPreferences;

public class MainActivity extends BaseGameActivity{
	final int RC_UNUSED = 5001;
	public GameControl gameControl = new GameControl();
	private AdView adView;
	private FragmentManager fm;
	private InitialMenu initialMenu;
	private PlayGame playGameFrag;
	private RankFrag rankFrag;
	private ForceLoginDialogFragment forceLoginDialogFrag;
	private ViewGroup linearLayoutParent;
	private ViewGroup frameLayoutParent;
	
	//Google+ sign-in
	private boolean mExplicitSignOut = true;
	private boolean mInSignInFlow = false;
	private enum Screen{
		INITIAL, SELECT_DIFFICULTY, PLAYING, RANKING, CONFIG, GAME_RESULT
	}
	private Screen screen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_activity);

		linearLayoutParent = (ViewGroup) findViewById(R.id.linearLayoutParent);
		frameLayoutParent = (ViewGroup) findViewById(R.id.frameLayoutParent);
//		
		fm = getSupportFragmentManager();
		if(fm.getBackStackEntryCount() == 0){
			initialMenu = new InitialMenu();
			fm.beginTransaction().replace(R.id.fragmentPlaceholder, initialMenu, "initialFrag").commit();
			screen = Screen.INITIAL;
		}

        super.getGameHelper().setMaxAutoSignInAttempts(0);
		
		//Cara, você reparou que ali em cima tem uma linha em branco comentada? Purkê cê fás içu cumigo?
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String preferenceOrientationString = sharedPreferences.getString("orientation_preference", getString(R.string.default_screen_orientation));
		mExplicitSignOut = sharedPreferences.getBoolean("mExplicitSignOut", true);
		int preferenceOrientation;
		if(preferenceOrientationString.equals("portrait")) preferenceOrientation = Configuration.ORIENTATION_PORTRAIT;
		else preferenceOrientation = Configuration.ORIENTATION_LANDSCAPE;
		int currentOrientation = getResources().getConfiguration().orientation;
		if(preferenceOrientation != currentOrientation){
			if(preferenceOrientation == Configuration.ORIENTATION_PORTRAIT) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			if(preferenceOrientation == Configuration.ORIENTATION_LANDSCAPE) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(!mInSignInFlow && !mExplicitSignOut){
			mInSignInFlow = true;
//			beginUserInitiatedSignIn();
			getApiClient().connect();
//			getGamesClient().connect();
		}

        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("40F2B77E24B60E1F6AFA4AC2D0ADD142")
                .build();
        adView.loadAd(adRequest);
        adView.setEnabled(true);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		sharedPreferences.edit().putBoolean("mExplicitSignOut", mExplicitSignOut);
        adView.setEnabled(false);
	}
	
	
	/*
	 * Classe que permite que os fragmentos realizem transações
	 * na atividade, ocultando a propaganda quando necessário.
	 */
	public class GameControl{
		public void backToInitialMenu(){
//			enableAdView();
			changeAdViewToLinearLayout();
			int count = fm.getBackStackEntryCount();
			for(int a = 0; a < count; a++){
				fm.popBackStack();
			}
//			fm.popBackStack();
			screen = Screen.INITIAL;
		}
		
		public void goToSelectDifficulty(){
			SelectDifficulty fragment = new SelectDifficulty();
			fm.beginTransaction().replace(R.id.fragmentPlaceholder,  fragment, "selectDifficultyFrag").addToBackStack("selectDifficultyFrag").commit();
			screen = Screen.SELECT_DIFFICULTY;
		}
		
		public void playGame(Difficulty difficulty){
//			disableAdView();
			changeAdViewToFrameLayout();
			playGameFrag = new PlayGame();
			playGameFrag.setDifficulty(difficulty);
			fm.beginTransaction().replace(R.id.fragmentPlaceholder, playGameFrag, "playGameFrag").addToBackStack("playGameFrag").commit();
			screen = Screen.PLAYING;
		}
		
		public void backToSelectDifficulty(){
//			enableAdView();
			changeAdViewToLinearLayout();
			fm.popBackStack("selectDifficultyFrag", 0);
			screen = Screen.SELECT_DIFFICULTY;
		}
		
		public void endedGame(Difficulty difficulty, long score){
//			enableAdView();
			changeAdViewToLinearLayout();
			GameResult fragment = new GameResult();
			fragment.setScore(difficulty, score);
			fm.beginTransaction().replace(R.id.fragmentPlaceholder, fragment, "gameResultFrag").addToBackStack("gameResultFrag").commit();
			screen = Screen.GAME_RESULT;
		}
		
		public void sendScore(Difficulty difficulty, long score){
//			if(!getGamesClient().isConnected()){
			if(!getApiClient().isConnected()){
				forceLoginDialogFrag = new ForceLoginAndSubmitScoreDialogFrag();
				Bundle args = new Bundle();
				args.putSerializable("difficulty", difficulty);
				args.putLong("score", score);
				forceLoginDialogFrag.setArguments(args);
				forceLoginDialogFrag.show(getSupportFragmentManager(), "forceLoginDialogFrag");
			} else {
				int leaderboardId = -1;
				if(difficulty == Difficulty.EASY) leaderboardId = R.string.scoreboard_easy;
				else if(difficulty == Difficulty.MEDIUM) leaderboardId = R.string.scoreboard_medium;
				else if(difficulty == Difficulty.HARD) leaderboardId = R.string.scoreboard_hard;
				else if(difficulty == Difficulty.INSANE) leaderboardId = R.string.scoreboard_insane;
//				getGamesClient().submitScore(getString(leaderboardId), score);
				Games.Leaderboards.submitScore(getApiClient(), getString(leaderboardId), score);
				showToast(R.string.score_submitted);
				backToInitialMenu();
			}
		}
		
		public void showRank(String periodOfTime, String difficulty){
			int leaderboardId = -1;
			if(difficulty.equals(getString(R.string.easy))) leaderboardId = R.string.scoreboard_easy;
			else if(difficulty.equals(getString(R.string.medium))) leaderboardId = R.string.scoreboard_medium;
			else if(difficulty.equals(getString(R.string.hard))) leaderboardId = R.string.scoreboard_hard;
			else if(difficulty.equals(getString(R.string.insane))) leaderboardId = R.string.scoreboard_insane;
			
//			Intent intent = getGamesClient().getLeaderboardIntent(getString(leaderboardId));
			Intent intent = Games.Leaderboards.getLeaderboardIntent(getApiClient(), getString(leaderboardId));
			startActivityForResult(intent, RC_UNUSED);
		}

		public void goToSettingsScreen() {
			startActivity(new Intent(getApplicationContext(), UserPreferences.class));
		}
		
		public void goToRankFrag(){
			//Old rank..
			/*rankFrag = new RankFrag();
			fm.beginTransaction().replace(R.id.fragmentPlaceholder, rankFrag, "rankFrag").addToBackStack("rankFrag").commit();
			screen = Screen.RANKING;*/
//			if(!getGamesClient().isConnected()){
			if(!getApiClient().isConnected()){
				forceLoginDialogFrag = new ForceLoginDialogFragment();
				forceLoginDialogFrag.show(getSupportFragmentManager(), "forceLoginDialogFrag");
			} else {
//				Intent intent = getGamesClient().getAllLeaderboardsIntent();
				Intent intent = Games.Leaderboards.getAllLeaderboardsIntent(getApiClient());
				startActivityForResult(intent, RC_UNUSED);
			}
		}
		
		public void myDialogFragmentItemSelected(int dialogTitleId, int itemSelected){
			rankFrag.dialogFragmentItemClicked(dialogTitleId, itemSelected);
		}
		
		public void showAchievements(){
			showToast(R.string.achievements_under_construction);
//			if(!getGamesClient().isConnected()){
			if(!getApiClient().isConnected()){
				forceLoginDialogFrag = new ForceLoginDialogFragment();
				forceLoginDialogFrag.show(getSupportFragmentManager(), "forceLoginDialogFrag");
			} else {
//				startActivityForResult(getGamesClient().getAchievementsIntent(), RC_UNUSED);
				startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), RC_UNUSED);
			}
		}
		
		public void signInUser(){
			if(!mInSignInFlow){
				mInSignInFlow = true;
				beginUserInitiatedSignIn();
			}
		}
		
		public void signOutUser(){
			signOut();
			mExplicitSignOut = false;
			mInSignInFlow = false;
		}
	}
	
	public GameControl getGameControl(){
		return gameControl;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(screen == Screen.PLAYING){
			if(keyCode == KeyEvent.KEYCODE_BACK){
				playGameFrag.backPressed();
				return true;
			} else if(keyCode == KeyEvent.KEYCODE_MENU){
				playGameFrag.menuPressed();
				return true;
			} else if(event.getAction() == KeyEvent.ACTION_DOWN){
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
		        	playGameFrag.pressedNumber(numberPressed);
		        	return true;
		        } else {
		        	return super.onKeyDown(keyCode, event);
		        }
			} else {
				return super.onKeyDown(keyCode, event);
			}
		} else if(screen == Screen.GAME_RESULT){
			if(keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK){
				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}
//    	if(keyCode == KeyEvent.KEYCODE_MENU||keyCode == KeyEvent.KEYCODE_BACK){
//    		
//    	} else {
//    		return super.onKeyDown(keyCode, event);
//    	}
    }
	
	public boolean isConnected(){
//		return getGamesClient().isConnected();
		return getApiClient().isConnected();
	}
	
	
	@Override
	public void onSignInFailed() {
		//Ignored for a while because sometimes it goes here when the user don't want to sign in, and it already show an error alert if anything goes wrong.
		/*showToast(R.string.login_failed);
		if(screen == Screen.INITIAL){
			initialMenu.showSignInButton();
		}*/
		mInSignInFlow = false;
	}
	
	@Override
	public void onSignInSucceeded() {
		if(forceLoginDialogFrag != null){
			forceLoginDialogFrag.onSignInSucceeded();
			//The dialog should destroy itself, so I'll remove it's reference to don't call this method twice.
			forceLoginDialogFrag = null;
		}
		if(screen == Screen.INITIAL){
			initialMenu.signInCompleted();
		}
		mInSignInFlow = false;
	}
	
	public void showToast(int textResId){
		Toast.makeText(this, textResId, Toast.LENGTH_LONG).show();
	}
	
//	private void disableAdView(){
//		adView.setEnabled(false);
//		adView.setVisibility(View.GONE);
//	}
//	
//	private void enableAdView(){
//		adView.setEnabled(true);
//		adView.setVisibility(View.VISIBLE);
//	}
	
	private void changeAdViewToFrameLayout(){
		if(linearLayoutParent.findViewById(R.id.adView) == null) return;
//		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//		params.gravity = Gravity.CENTER_HORIZONTAL;
//		ViewGroup.LayoutParams params = adView.getLayoutParams();
		adView.pause();
		adView.setVisibility(View.GONE);
//		linearLayoutParent.removeView(adView);
//		frameLayoutParent.addView(adView, params);
	}
	
	private void changeAdViewToLinearLayout(){
		if(frameLayoutParent.findViewById(R.id.adView) == null) return;
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		params.gravity = Gravity.CENTER_HORIZONTAL;
//		ViewGroup.LayoutParams params = adView.getLayoutParams();
		adView.setVisibility(View.VISIBLE);
		adView.resume();
//		frameLayoutParent.removeView(adView);
//		linearLayoutParent.addView(adView, 0, params);
	}
}
