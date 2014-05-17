package com.ijustyce.androidlib;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Process;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.ijustyce.unit.LogCat;
import com.ijustyce.unit.toast;
import com.txh.Api.sqlite;

public class txApplication extends Application {

	private static String tag = "txTag";
	private String dbFile; 
	public String sharedName = "shared";
	public boolean pw = false;
	public static Intent Main;
	@Override
	public void onCreate() {
		
		firstUse();
		LogCat.i(tag, "Application onCreate , pid = " + Process.myPid());
	}
	
	public void setMainActivity(Intent intent){
		Main = intent;
	}
	
	/**
	 * set theme and return theme name
	 * @param context
	 */
	public String theme(Context context){
		
		String themeString = getPreferences("theme", getSharedPreferencesName());
		
		if(themeString.equals("")){
			return "";
		}
		LogCat.i(tag, themeString);
		
		if (themeString.equals("sky")) {
			
			context.setTheme(R.style.txTheme3);
		} else if(themeString.equals("beauty")){
			
			context.setTheme(R.style.txTheme);
		}else if(themeString.equals("pure")){
			context.setTheme(R.style.txTheme2);
		}
		
		return themeString;
	}
	
	/**
	 * read preferences file and return value if possible or return null
	 * @param value 
	 * @param fileName
	 * @return
	 */
	public String getPreferences(String key ,String fileName){
		
		SharedPreferences shared = getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		
		return shared.getString(key, "null");
	}
	
	/**
	 * read preferences file and return value if possible or return 0
	 * @param value 
	 * @param fileName
	 * @return
	 */
	public int getPreferencesInt(String key ,String fileName){
		
		SharedPreferences shared = getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		
		return shared.getInt(key, 0);
	}
	
	/**
	 *  set preferences file value
	 * @param key key of preference file 
	 * @param value value of key
	 * @param fileName preference file name
	 */
	public void setPreferences(String key , String value , String fileName){
		
		SharedPreferences shared = getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		
		shared.edit().putString(key, value).commit();
	}
	
	/**
	 *  set preferences file value
	 * @param key key of preference file 
	 * @param value value of key
	 * @param fileName preference file name
	 */
	public void setPreferencesInt(String key , int value , String fileName){
		
		SharedPreferences shared = getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		
		shared.edit().putInt(key, value).commit();
	}
	
	
	/**
	 * return PreferenceActivity's sharedPreferences
	 * @return sharedPreferences
	 */
	public SharedPreferences getSharedPreferences(){
		
		SharedPreferences shared = getSharedPreferences(getSharedPreferencesName(),
				Context.MODE_PRIVATE);
		return shared;
	}
	
	/**
	 * return PreferenceActivity's sharedPreferences
	 * @return pkgName_preferences
	 */
	public String getSharedPreferencesName(){
		
		return this.getPackageName() + "_preferences";
	}
	
	/**
	 * check if is first time , return true if is first time
	 * @return
	 */
	public boolean firstUse(){
		
		File dbFile = new File(getDbFile());
		if(!dbFile.exists()){
			createTable();
			LogCat.i(tag, "this is first time to use");
			return true;
		}
		
		LogCat.i(tag, "this is not the first time to use");
		return false;
	}
	
	/**
	 * return sqlite file path ,if parent directory not exist
	 * it will create !
	 * @return
	 */
	public String getDbFile(){
	
		String file = this.getFilesDir().getPath();
		File f = new File(file);
		if(!f.exists()){
			f.mkdir();
		}
		dbFile = file + "/contacts.db";
		LogCat.i(tag, dbFile);
		return dbFile;
	}
	
	
	/**
	 * return windows animation string
	 * @return
	 */
	
	public String getAnim(){
		
		SharedPreferences myshared = getSharedPreferences();
		String anim = myshared.getString("anim", "zoom");
		LogCat.i(tag, "anim: "+anim);
		return anim;
	}
	
	/**
	 * find name by phone number , return phone number 
	 * and false if not exist , or return name and true
	 * @return
	 */
	public String[] getName(String number){
		
		String name = "";
		String temp = number;
		String[] result = new String[2];
		result[1] = "true";
		int error = 0;
		while(name.equals("")&&error<3){
			if(error ==1&&number.length()>10){
				temp = number.substring(0,3)+" "+number.substring(3,7)+
						" "+number.substring(7,11);
			}
			if(error ==2&&number.length()>10){
				temp = number.substring(0,3)+" "+number.substring(3,7)+" "+
			number.substring(7,11);
			}
			
			String[] projection = {
					ContactsContract.PhoneLookup.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Phone.NUMBER };
			Cursor cur = this.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					projection, // Which columns to return.
					ContactsContract.CommonDataKinds.Phone.NUMBER
					+ " = '" + temp + "'", // WHERE clause.
					null, // WHERE clause value substitution
					null); // Sort order.
			if (cur == null){
				result[0] = number;
				result[1] = "false";
				return result;
			}
			for (int i = 0; i < cur.getCount(); i++){
				cur.moveToPosition(i);
				int nameFieldColumnIndex = cur
				.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
				name = cur.getString(nameFieldColumnIndex);
			}
			cur.close();
			error++;
		}
		if (name.equals("")){
			name = number;
			result[1] = "false";
		}
		result[0] = name;
		return result;
	}
	
	/**
	 * return pkgName+" "+versionName , if fail return ""
	 * @return
	 */
	public String getVersion() {
	    
		try {
	        PackageManager manager = this.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
	        String version = info.versionName;
	        String pkgName = info.applicationInfo.loadLabel
	        		(getPackageManager()).toString();
	        return pkgName+" "+version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "";
	    }
	}
	
	/**
	 * return date by your own formatter , like yyyy/MM/dd/HH/mm
	 * @return
	 */
	public String getDate(String formatter){
		
		SimpleDateFormat ft = new SimpleDateFormat(formatter,Locale.CHINA);
		 Date dd = new Date();
		 return ft.format(dd);
	}
	
	
	/**
	 * @return phone number if success or return null 
	 */
	
	public String getNumber(){
	
		TelephonyManager mngr = (TelephonyManager)this.
				getSystemService(Context.TELEPHONY_SERVICE); 
		String number = mngr.getLine1Number();
	    
	    LogCat.i("---justyce---", "phone number :"+number);
	    return number;
	}
	
	/**
	 * show toast
	 * @param id name of string in string.xml , such as R.string.add
	 */
	public void showToast(int id) {

		String s = getResources().getString(id).toString();
		showToast(s);
	}
	
	/**
	 * get string of strings.xml
	 * @param id string id like R.string.hello
	 * @return string value
	 */
	public String getStringValue(int id){
		
		return getResources().getString(id).toString();
	}

	/**
	 * show toast
	 * @param s String to show  
	 */
	public void showToast(String s) {

		toast.show(getBaseContext(), s);
	}
	
	/**
	 * set text to clipboard 
	 * @param text the text set to clipboard 
	 */
	public void setClipboard(String text){
		
		ClipboardManager clipboard = (ClipboardManager)getSystemService
				(Context.CLIPBOARD_SERVICE);
		
		clipboard.setText(text);
		showToast(R.string.copy_success);
	}
	
	/**
	 * get clipboard text
	 * @return clipboard text
	 */
	public String getClipboard(){
		
		ClipboardManager clipboard = (ClipboardManager)getSystemService
				(Context.CLIPBOARD_SERVICE);
		
		return clipboard.getText().toString();
	}
	
	/**
	 * create table for app
	 */
	
	private void createTable(){
		
		sqlite api = new sqlite();
		String dbFile = getDbFile();	
		String[] intercept = {"value"};
		String[] sms = {"phone" , "content" , "ismy"};
		String[] phone = {"phone" , "content" , "ismy" , "total"};
		String[] recent = {"phone"};
		String[] timing = {"phone" , "content" , "year" ,"month" , 
				"day" ,"hour" , "minute"};
		
		api.createTable("intercept", intercept , dbFile);		
		api.createTable("words", intercept , dbFile);	
		api.createTable("sms", sms , dbFile);
		api.createTable("phone", phone , dbFile);
		api.createTable("recent", recent , dbFile);	
		api.createTable("timing", timing, dbFile);
		
	}
}