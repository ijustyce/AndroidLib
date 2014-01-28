/**
 * date:013-06-02
 * settings */
package com.ijustyce.contacts;

import com.txh.Api.md5;

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

public class settings extends PreferenceActivity implements
		OnPreferenceChangeListener {
	/** Called when the activity is first created. */
	private txApplication tx;

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
		// TODO Auto-generated method stub

		if (preference.getKey().equals("lock")) {

			preference.getEditor().putString("lock", newValue.toString())
					.commit();
			
			if(!newValue.toString().equals("null")){
				
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
						tx.showToast(password);
						tx.setPreferences("lock", md5.afterMd5(password), "pass");
					}
				}).show();
			}
		}
		return true;
	}
}