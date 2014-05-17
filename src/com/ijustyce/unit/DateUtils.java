package com.ijustyce.unit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

	static final String YESTERDAY = "昨天";
	static final String TODAY = "今天";
	static final String BEFORE_YESTERDAY = "前天";
	static final String MONTH = "月";
	static final String DAY = "日";
	static final String HOUR = "时";
	static final String MINUTE = "分";
	
	/**
	 * get day count between date and now , format like yyyy/MM/dd HH:mm
	 * @param date date count
	 */
	public static String parseDate(Date date){
		
		String day = "";
		Date now = stringToDate(getDate("yyyy/MM/dd HH:mm") , "yyyy/MM/dd HH:mm" );
		long time = (now.getTime() - date.getTime())/86400000;
		switch ((int)time) {
		case 0:
			day = TODAY;
			break;
		case 1:
			day = YESTERDAY;
			break;
		case 2:
			day = BEFORE_YESTERDAY;
			break;
		default:
			day = dateToString(date, "MM" + MONTH + "dd" + DAY);
			break;
		}
		
		return day + dateToString(date, " HH" + HOUR + "mm" + MINUTE);
	}
	
	/**
	 * get current date like yyyy/MM/dd HH:mm
	 * @param formatter like yyyy/MM/dd HH:mm
	 * @return
	 */
	public static String getDate(String formatter) {
		SimpleDateFormat ft = new SimpleDateFormat(formatter, Locale.CHINA);
		Date dd = new Date();
		return ft.format(dd);
	}
	
	/**
	 * convert date to string
	 * @param date
	 * @param formatter
	 * @return String
	 */
	public static String dateToString(Date date , String formatter){
		SimpleDateFormat ft = new SimpleDateFormat(formatter, Locale.CHINA);
		return ft.format(date);
	}
	
	/**
	 * convert a date string to date 
	 * @param value value of date , like 2014/04/27 11:42:00
	 * @param format like yyyy/MM/dd HH:mm:ss 
	 * @return date
	 */
	
	public static Date stringToDate(String value , String format){
		
		SimpleDateFormat sdf=new SimpleDateFormat(format); 
		try {
			return sdf.parse(value);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
	}
	
	/**
	 * convert a certain date to Unix timestamp
	 * @param date , like 2014-04-27 11:42:00
	 * @return Unix timestamp
	 */
	public static String dateToTimesTamp(Date date) {
		
		String timesTamp = null;
		long l = date.getTime();
		String str = String.valueOf(l);
		timesTamp = str.substring(0, 10);
		return timesTamp;
	}
	
	/**
	 * convert Unix timestamp to a certain date
	 * @param timesTamp Unix timestamp
	 * @return date , like 2014-04-27 11:42:00
	 */
	public static String timesTampToDate(String timesTamp , String format) {
		
		String date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long lcc_time = Long.valueOf(timesTamp);
		date = sdf.format(new Date(lcc_time * 1000L));
		return date;
	}
	
	/**
	 * system.out.println(text)
	 * @param text
	 */
	public static void Jout(String text){
		
		System.out.println(text);
	}
}
