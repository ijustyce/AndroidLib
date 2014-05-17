/**
 * date:2014-04-21
 * rewrite toast
 */
package com.ijustyce.unit;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ijustyce.androidlib.R;

public class toast {

	public static void show(int id, Context context) {

		LayoutInflater mInflater = LayoutInflater.from(context);
		View toastRoot = mInflater.inflate(R.layout.toast, null);
		TextView message = (TextView) toastRoot.findViewById(R.id.message);
		message.setText(id);

		Toast toastStart = new Toast(context);
		toastStart.setGravity(Gravity.BOTTOM , 0, 90);
		toastStart.setDuration(Toast.LENGTH_LONG);
		toastStart.setView(toastRoot);
		toastStart.show();
	}

	public static void show(Context context, String text) {

		LayoutInflater mInflater = LayoutInflater.from(context);
		View toastRoot = mInflater.inflate(R.layout.toast, null);
		TextView message = (TextView) toastRoot.findViewById(R.id.message);
		message.setText(text);

		Toast toastStart = new Toast(context);
		toastStart.setGravity(Gravity.BOTTOM , 0, 90);
		toastStart.setDuration(Toast.LENGTH_LONG);
		toastStart.setView(toastRoot);
		toastStart.show();
	}
}
