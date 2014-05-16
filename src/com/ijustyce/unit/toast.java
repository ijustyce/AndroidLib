/**
 * date:2014-04-21
 * rewrite toast
 */
package com.ijustyce.unit;
import android.content.Context;
import android.widget.Toast;

public class toast {
	
	public static void show(String text , Context context){
		
		Toast.makeText(context , text , Toast.LENGTH_LONG).show();
	}
	
	public static void show(int id , Context context){
		
		Toast.makeText(context, id, Toast.LENGTH_LONG).show();
	}
}
