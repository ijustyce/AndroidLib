/**
 * app locker Activity , if not must , please keep it what it is .
 * Or app will not work , and support by : https://github.com/ijustyce/AndroidLib
 */

package com.ijustyce.androidlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ijustyce.unit.toast;
import com.txh.Api.md5;

public class Password extends Activity {

	private txApplication tx;
	private int errorTime = 0;
	private String password;
	private TextView info;
	private boolean first = false, affirm = false;
	private boolean setPassword = false , delPassword = true;
	public static String lockPin = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();
	}
	
	private void init(){
		
		tx = (txApplication) getApplication();
		Bundle bundle = this.getIntent().getExtras();
		
		// must judge whether is null or app will crash 
		if (bundle != null) {

			String lock = bundle.getString("lock");
			if (lock.equals("gesture")) {
				setContentView(R.layout.androidlib_locker);
				password = tx.getPreferences("gesture", "pass");
				if (password.equals("null")) {
					setPassword = true;
					info = (TextView) findViewById(R.id.lock_user_info);
					setPasswordInit();
				}
			}
		}
	}

	/**
	 * if is set password , will call this function .
	 */
	private void setPasswordInit() {

		info.setText(tx.getStringValue(R.string.pass_first));
		affirm = false;
		password = "";
		first = true;
	}
	
	/**
	 * deal key down event , cancel set password or cancel pass
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (setPassword && delPassword) {

				tx.setPreferences("lock", "null", "pass");
			}
			exit("cancel");
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * exit cancel or success
	 * @param value
	 */
	private void exit(String value) {

		Intent intent = new Intent();
		intent.putExtra("result", value);
		setResult(RESULT_OK, intent);
		this.finish();
	}

	/**
	 * listen view touch event , for judge whether can pass
	 */
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			dealTouchEvent();
		}
		return true;
	}
	
	/**
	 * deal view touch event call setPass or checkPass or just return 
	 */
	private void dealTouchEvent(){
		
		if (lockPin.equals("")) {
			
			return ;
		}
		
		if (first) {
			setPass(lockPin);
		}
		
		else {
			checkPass(lockPin);
		}
	}
	
	/**
	 * set password function , suggest add return in if statement , if next
	 * statement will not call , or just use switch-case . 
	 * @param lockPin
	 */
	private void setPass(String lockPin) {
		
		if (!affirm) {

			password = lockPin;
			info.setText(tx.getStringValue(R.string.pass_affirm));
			affirm = true;
			return ;
		}

		if (password.equals(lockPin)) {
			
			delPassword = false;
			tx.setPreferences("gesture", md5.afterMd5(password), "pass");
			exit("success");
			return ;
		}
		else {
			init();
			toast.show(R.string.pass_affirm_error, this);
		}		
	}

	/**
	 * check password function . If error than three times will exit
	 * @param lockPin
	 */
	private void checkPass(String lockPin) {

		if (errorTime > 2) {
			exit("cancel");
		}

		if (password.equals(md5.afterMd5(lockPin))) {

			exit("success");
			toast.show(R.string.pw_success, this);
		}

		if (!password.equals(md5.afterMd5(lockPin))) {

			errorTime++;
			if (errorTime == 3) {
				toast.show(R.string.pw_wait, this);
			} else {
				toast.show(R.string.pw_error, this);
			}
		}
	}
}
