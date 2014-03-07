/**
 * date:013-06-02
 * settings */
package com.ijustyce.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;
import android.widget.EditText;

import com.txh.Api.md5;

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
			tx.setPreferences("lock", "null", "pass");
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
		// TODO Auto-generated method stub

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
			
			else if(!newValue.toString().equals("null")){
				
				setPassword();
			}
		}
		return true;
	}
	
	private void setPassword(){
		
		String pw = tx.getStringValue(R.string.pw_set);
		String ok = tx.getStringValue(R.string.ok);
		final EditText et = new EditText (this);
		et.setInputType(2);
		new AlertDialog.Builder(settings.this).setTitle(pw)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(et)
		.setPositiveButton(ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String password = et.getText().toString();
				if(password.length()<5){
					tx.showToast(R.string.pass_short);
					setPassword();
				}
				else{
					tx.showToast(password);
					tx.setPreferences("password", md5.afterMd5(password), "pass");
				}
			}
		}).show();
	}
}