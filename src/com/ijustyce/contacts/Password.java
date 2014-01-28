package com.ijustyce.contacts;

import com.txh.Api.md5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Password extends Activity {

	private txApplication tx;
	private int errorTime = 0;
	private String pw = "" , password;
	private TextView tv;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {

			String lock = bundle.getString("lock");
			Log.i("lock", lock);
			if (lock.equals("password")) {
				setContentView(R.layout.lock);
				tv = (TextView)findViewById(R.id.pw);
			} else if (lock.equals("gesture")) {
				setContentView(R.layout.lock);
			}
		}
		
		tx = (txApplication) getApplication();
		password = tx.getPreferences("lock", "pass");
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		exit("cancel");
		return true;
	}

	public void btClick(View v) {

		switch (v.getId()) {

		case R.id.ok:
			check();
			break;
		case R.id.del:
			del();
			break;
		default:
			click(v);
			break;
		}
	}

	private void click(View v) {

		Button bt = (Button) findViewById(v.getId());
		pw += bt.getText().toString();
		
		tv.setText(tv.getText().toString() + "*");

		Log.i("text", pw);
	}

	private void check() {

		if (errorTime > 2) {
			exit("cancel");
		}

		if (md5.afterMd5(pw).equals(password)) {

			exit("success");
			tx.showToast(R.string.pw_success);
		} else {
			errorTime++;
			if (errorTime == 3) {
				tx.showToast(R.string.pw_wait);
			} else {
				tx.showToast(R.string.pw_error);
			}
		}
	}

	private void del() {

		String temp = tv.getText().toString();
		if (pw.length() > 0) {
			pw = pw.substring(0, pw.length() - 1);	
			tv.setText(temp.subSequence(0, temp.length()-1));
		}
	}

	private void exit(String value) {

		Intent intent = new Intent();
		intent.putExtra("result", value);
		setResult(RESULT_OK, intent);
		this.finish();
	}
}
