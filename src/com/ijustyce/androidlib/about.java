/**
 * function : show a about Activity of app , change it at your will
 */
package com.ijustyce.androidlib;

import android.os.Bundle;
import android.widget.TextView;

public class about extends baseclass{
	
	private txApplication tx;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.androidlib_about);
		
		tx = (txApplication)getApplication();
		
		TextView version = (TextView)findViewById(R.id.version);
		String text = tx.getVersion();
		if(!text.equals("")){
			version.setText(text);
		}
	}
}
