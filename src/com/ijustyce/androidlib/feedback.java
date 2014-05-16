/**
 * feedback function of app , based on package com.ijustyce.mail
 */
package com.ijustyce.androidlib;

import com.ijustyce.unit.toast;
import com.txh.Api.JavaMail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class feedback extends Activity{
	
	private static ProgressDialog dialog;
	private static EditText contact;
	private static EditText content;
	private Thread thread;
	private String msg , info;
	private txApplication tx;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.androidlib_feedback);
		
		init();
	}
	
	/**
	 * init app's layout
	 */
	private void init(){
		
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        
        int height = metric.heightPixels;
        
        EditText content = (EditText)findViewById(R.id.msg);
        content.setHeight(7*height/12);
       
        tx = (txApplication)getApplication();
	}
	
	/**
	 * mostly we should judge which view is clicked , but there only has one
	 * so it's need't to judge , can call function directly 
	 * @param v
	 */
	public void btClick(View v){
		
		sendFeedBack();
	}
	
	/**
	 * send feedback info to email
	 */
	private void sendFeedBack(){

		String dialog_title = getResources().getString(R.string.dialog_title);
		String dialog_content = getResources().getString(R.string.dialog_content);
		
		content = (EditText)findViewById(R.id.msg);
		contact = (EditText)findViewById(R.id.contact);
		
		msg = content.getText().toString();
		info = " qq: "+contact.getText().toString();
		
		if(msg.equals("")){
			
			toast.show(R.string.error_feedback , this);
			return ;
		}
		
		dialog = ProgressDialog.show(this, dialog_title, dialog_content, true);
		thread = new Thread(null, domywork, "Background");
		thread.start();
	}

	/**
	 * must call Looper.prepare(); first and call Looper.loop(); finally .
	 */
	private Runnable domywork = new Runnable(){

		public void run(){
			
			Looper.prepare();

			String phone = "apk: "+tx.getVersion()+" phone: "+tx.getNumber();		
			String[] args = {"justyce2013@163.com","MM|2013@163.com",
					"1558789954@qq.com",phone+info,msg};
			JavaMail.sendMail(args);			
			handler.sendEmptyMessage(0);			
			
			Looper.loop();
		}
	};
	
	/**
	 * deal dialog
	 * handler should be static !
	 */
	static Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){

			/* because there just a message so can deal this way */
			dialog.dismiss();
			empty();
		}
	};
	
	/**
	 * empty editText , content and contacts
	 */
	private static void empty(){
		
		content.setText("");
		contact.setText("");
	}
	
	/**
	 * deal key down event
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if (keyCode == KeyEvent.KEYCODE_BACK){
			
			startActivity(new Intent(this , MainActivity.class));
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
