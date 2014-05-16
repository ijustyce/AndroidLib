/**
 * date:2014-04-21
 * settings function
 */
package com.ijustyce.androidlib;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

public class settings extends PreferenceActivity implements
		OnPreferenceChangeListener {
	/** Called when the activity is first created. */
	private txApplication tx;
	
	boolean finish = false;  //   is password set finish  

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);

		tx = (txApplication) getApplication();

		ListPreference lp = (ListPreference) findPreference("lock");
		lp.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
		
			startActivity(new Intent(this, MainActivity.class));
			anim();
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void anim() {

		String anim = tx.getAnim();
		if (anim.equals("fade")) {
			overridePendingTransition(R.anim.fade, R.anim.hold);
		} else if (anim.equals("zoom")) {
			overridePendingTransition(R.anim.my_scale_action,
					R.anim.my_alpha_action);
		} else if (anim.equals("roll")) {
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		} else if (anim.equals("iphone")) {
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		} else if (anim.equals("Staggered")) {
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
		} else if (anim.equals("unfold")) {
			overridePendingTransition(R.anim.unfold_enter, R.anim.unfold_exit);
		}
		// default is ubuntu style
		else {
			overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		}
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {

		if (preference.getKey().equals("lock")) {

			String type = newValue.toString();
			if(!type.equals("null")&&!type.equals("password")
					&&!type.equals("gesture")){
				
				return false;
			}
			
			// unlock info saved in pass.xml , password or gesture can 
			// be accessed by key lock , if password can be accessed
			// by key password , and gesture too .
			
			tx.setPreferences("lock", type, "pass");
			if(type.equals("gesture")){
				
				tx.setPreferences("gesture", "null", "pass");
				tx.pw = false;
				startActivity(new Intent(this, MainActivity.class));
				this.finish();
			}
		}
		return true;
	}
}