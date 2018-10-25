package com.ysw.Util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 * @author ysw
 *
 */
public class DateUtil {
	public static final String YYYYMMDD = "yyyy-mm-dd";
	public static final String TIMEMonth = "1 3 5 7 8 10 12";
	
	public static void main(String[] args) {
		getIncreaseDate();
	}
	
	/***
	 * get yyyy-mm-dd date
	 * @return String
	 */
	public static String getIncreaseDate() {
		Date d = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		Integer year = c.get(1);
		Integer month = c.get(2)+1;
		Integer day = c.get(5);//使得每次获取的day都增加一天
		
//		if(TIMEMonth.indexOf(month.toString()) != -1 ) {
//			if(day >= 32) {
//				month += 1;
//				day = 1;
//			}
//		}
		
		String strZero = "0";
		
		//拼接日期
		StringBuffer date = new StringBuffer(year+"-");
		
		//个位数日期拼接0	08-21
		if(month < 10) {
			strZero = strZero.concat(month.toString());
			date.append(strZero);
		}else {
			date.append(month.toString());
		}
		
		date.append("-");
		strZero = "0";
		
		if(day < 10) {
			strZero = strZero.concat(day.toString());
			date.append(strZero);
		}else {
			date.append(day.toString());
		}
		
//		System.out.println(date);
		return date.toString();
	}
}
