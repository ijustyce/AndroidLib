package com.ijustyce.contacts;

import com.txh.Api.md5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Password extends Activity {

	private txApplication tx;
	private int errorTime = 0;
	private String pw = "", password;
	private TextView tv, info;
	private boolean first = false, affirm = false;
	public static String lockPin = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tx = (txApplication) getApplication();

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {

			String lock = bundle.getString("lock");
			Log.i("lock", lock);
			if (lock.equals("password")) {
				setContentView(R.layout.lock);
				tv = (TextView) findViewById(R.id.pw);
				password = tx.getPreferences("password", "pass");
			} else if (lock.equals("gesture")) {
				setContentView(R.layout.lock_screen_layout);
				password = tx.getPreferences("gesture", "pass");
				if (password.equals("null")) {
					info = (TextView) findViewById(R.id.lock_user_info);
					init();
				}
			}
		}
	}

	private void init() {

		info.setText(tx.getStringValue(R.string.pass_first));
		affirm = false;
		password = "";
		first = true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			tx.setPreferences("lock", "null", "pass");
			exit("cancel");
		}
		return super.onKeyDown(keyCode, event);
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
			tv.setText(temp.subSequence(0, temp.length() - 1));
		}
	}

	private void exit(String value) {

		Intent intent = new Intent();
		intent.putExtra("result", value);
		setResult(RESULT_OK, intent);
		this.finish();
	}

	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (!lockPin.equals("")) {
				check(lockPin);
			}
		}
		return true;
	}

	private void check(String lockPin) {

		if (errorTime > 2) {
			exit("cancel");
		}

		if (!first && password.equals(md5.afterMd5(lockPin))) {

			exit("success");
			tx.showToast(R.string.pw_success);
		}

		if (!first && !password.equals(md5.afterMd5(lockPin))) {

			errorTime++;
			if (errorTime == 3) {
				tx.showToast(R.string.pw_wait);
			} else {
				tx.showToast(R.string.pw_error);
			}
		}

		if (first && affirm) {

			Log.i("---first---", "haha");
			if (password.equals(lockPin)) {

				tx.setPreferences("gesture", md5.afterMd5(password), "pass");
				exit("success");
			}

			else{
				init();
				tx.showToast(R.string.pass_affirm_error);
				return ;
			}
		}

		if (first && !affirm) {

			password = lockPin;
			info.setText(tx.getStringValue(R.string.pass_affirm));
			affirm = true;
		}
	}
}
