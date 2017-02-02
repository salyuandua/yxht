package com.cheers.commonMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateFormat {

	DataFormat dataFormat = new DataFormat();
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dfNo_ = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat dfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 将date类型转换为String类型 yyyy-MM-dd
	 * @param date 要转换的date类型
	 * @return
	 */
	public String getStringDate(Date date){
		String dateOne = "";
		try {
			//转换
			dateOne = df.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateOne;
	}
	/**
	 *  将date类型转换为String类型yyyyMMdd
	 * @param date
	 * @return
	 */
	public String getStringDateNo_(Date date){
		String dateOne = "";
		try {
			//转换
			dateOne = dfNo_.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateOne;
	}
	/**
	 *  将date类型转换为String类型yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public String getStringTime(Date date){
		String dateOne = "";
		try {
			//转换
			if(date!=null) {
				dateOne = dfLong.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateOne;
	}
	/**
	 * 根据String字符串获取date
	 * @param date
	 * @return
	 */
	public Date getDateByString(Object date){
		Date dateOne = null;
		try {
			if(date != null){
				if(!dataFormat.checkNull(date.toString())){
					dateOne = df.parse(date.toString());
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateOne;
	}
	
	public Date getLongDateByString(Object date){
		Date dateOne = null;
		try {
			if(date != null){
				if(!dataFormat.checkNull(date.toString())){
					dateOne = dfLong.parse(date.toString());
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateOne;
	}
	
	
	
	/**
	 * 获取String类型的当前时间 返回格式为yyyy-MM-dd
	 * @return
	 */
	public String getNowDate(){
		String dateOne = null;
		try {
			dateOne = getStringDate(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateOne;
	}
	
	public String getNowDateNo_(){
		String dateOne = null;
		try {
			dateOne = getStringDateNo_(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateOne;
	}
	
	/**
	 * 获取String类型的当前时间 返回格式为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public String getNowTime(){
		String dateOne = null;
		try {
			dateOne = getStringTime(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateOne;
	}
	
	/**
	 * 将时间格式化为yyymmdd的形式
	 * @return
	 */
	public String getFormatDateOne(Date date){
		String dateFormatOne = "";
		try {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
			dateFormatOne = dateFormat1.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateFormatOne;
	}
	
	/**
	 * 将 日期字符串 转换为oracle识别的日期
	 * 参数 date 格式为yyyy-MM-dd HH:mm:ss
	 */
	public String databaseToTime(String date){		
		return "to_date('"+date+"','yyyy-mm-dd hh24:mi:ss')";
	}
	public String fromDatabaseToTime(String date){	//返回数据库时间查询起始日期	
		return " >= to_date('"+date+" 00:00:00','yyyy-mm-dd hh24:mi:ss') ";
	}
	public String toDatabaseToTime(String date){		//返回数据库时间查询截止日期
		return " <= to_date('"+date+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ";
	}
	
	public String subTimeTo9(String date){		//截取时间 9位，显示结果 2012-01-01
		return dataFormat.stringCheckNull(date).length()>9?dataFormat.stringCheckNull(date).substring(0,10):"";
	}
	public String subTimeTo19(String date){		//截取时间 19位，显示结果 2012-01-01 12:12:12
//		DataFormat.Out(date);
		return dataFormat.stringCheckNull(date).length()>18?dataFormat.stringCheckNull(date).substring(0,19):"";
	}
	
	/**
	 * 获得某年某月的最大日期
	 */
	public int getMonthDays(int year, int month) {
		int iDays = 0;
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				iDays = 31;
				break;
			
			case 4:
			case 6:
			case 9:
			case 11:
				iDays = 30;
				break;
			case 2:
				iDays = getFebDays(year);
				break;
			default:
				iDays = 0;
				break;
		}
		return iDays;
	}
	/**	
	 * 获得某一年二月份最大日期
	 */
	public int getFebDays(int year) {
		if (isLeapYear(year)) {
			return 29;
		} else {
			return 28;
		}
	}
	/**
	 * 判断是否是闰年
	 */
	public boolean isLeapYear(int year) {
		if (year % 400 == 0) {
			return true;
		} else if ((year % 4 == 0) && (year % 100 != 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	

	/** 
	 * 获得指定日期的前n天 
	 *  
	 * @param specifiedDay 
	 * @return 
	 * @throws Exception 
	 */
	public String getSpecifiedDayBefore(String specifiedDay, int days) {//可以用new Date().toLocalString()传递参数   
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - days);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/** 
	 * 注释部分的功能是 查询获得指定日期的前n天 
	 *  当前功能是 返回本月1号
	 * @param specifiedDay 
	 * @return 
	 * @throws Exception 
	 */
	public String getFromDateForSearch(String specifiedDay) {
		String dayBefore = specifiedDay.substring(0, 8) + "01";
		return dayBefore;
	}

	public String getToDateForSearch(String specifiedDay) {
		return specifiedDay;
	}

	public String getweekday(String date) //获取当前日期是周几
	{
		String weekday = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt = sdf.parse(date);
			String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
					"星期六" };
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0) {
				w = 0;
			}
			weekday = weekDays[w];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weekday;

	}

	public int numberWeekday(String weekday) {
		int num = 0;
		if (weekday != null) {
			if ("星期一".equals(weekday)) {
				num = 1;
			} else if ("星期二".equals(weekday)) {
				num = 2;
			} else if ("星期三".equals(weekday)) {
				num = 3;
			} else if ("星期四".equals(weekday)) {
				num = 4;
			} else if ("星期五".equals(weekday)) {
				num = 5;
			} else if ("星期六".equals(weekday)) {
				num = 6;
			} else if ("星期日".equals(weekday)) {
				num = 7;
			}
		}
		return num;
	}

	/*
	 * 计算俩日期之间相隔的天数
	 * */
	public long getIntervalDays(String startday, String endday) {
		long quot = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = sdf.parse(startday);
			Date date2 = sdf.parse(endday);
			quot = date2.getTime() - date1.getTime();
			quot = quot / (1000 * 60 * 60 * 24);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quot;
	}

	/*
	 * 获取与某一日期相隔一段时间天的日期
	 * @startdate  基准时间
	 * @daynum     相隔天数
	 * @beforeorafter  符号  之前日期为-1  之后日期为1
	 */
	public String getDateFromTheDay(String startdate, int daynum,
			int beforeorafter) {
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		double quot = 0;
		try {

			while (daynum > 20) {
				startdate = sdf.format(sdf.parse(startdate).getTime()
						+ beforeorafter * 20 * (1000 * 60 * 60 * 24));
				daynum = daynum - 20;
			}
			Date date1 = sdf.parse(startdate);
			quot = date1.getTime() + beforeorafter * daynum
					* (1000 * 60 * 60 * 24);
			date = sdf.format(quot);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 查询2个年月之间的年月
	 * @param year1
	 * @param month1
	 * @param year2
	 * @param month2
	 * @return
	 */
	public ArrayList<String[]> getYearMonth(int year1,int month1,int year2,int month2) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		String[] str_ = new String[2];
		str_[0] = year1+"";
		str_[1] = month1+"";
		result.add(str_);
		if(year1>year2 || (year1 ==year2 && month1>month2)) {
			return result;
		}else {
			while(hasNext(year1,month1,year2,month2)) {
				String[] str = next(year1,month1);
				year1= Integer.parseInt(str[0]);
				month1= Integer.parseInt(str[1]);
				result.add(str);
			}
		}
		return result;
	}
	public static boolean hasNext(int year1,int month1,int year2,int month2){
        int Nextmonth ;
        if(month1==12) {
        	Nextmonth=1;
        	year1 ++;
        } else {
        	Nextmonth=month1+1;
        }
        if(year1<year2) {
        	return true;
        }else if(year1==year2){
        	if(Nextmonth<=month2) {
        		return true;
        	}else {
        		return false;
        	}
        }else {
        	return false;
        }
	}
	public static String[] next(int year1,int month1){
		String[] str = new String[2];
		str[0] = year1+"";
		str[1] = month1+"";
        int Nextmonth ;
        if(month1==12) {
        	Nextmonth=1;
        	year1 ++;
        } else {
        	Nextmonth=month1+1;
        }
        str[0] = year1+"";
		str[1] = Nextmonth+"";
		return str;
	}
	
	//将字符串转换为日期格式2013-06-06，如果不符合则返回空
	public String getDate(String val){
		String ret = "";
		
		return ret;
	}
	
	public static void main(String args[]) {
		DateFormat def = new DateFormat();
//		System.out.println("getIntervalDays::"
//				+ def.getIntervalDays("2012-09-01", "2012-08-09"));
		System.out.println("2012-09-17:::"+def.subTimeTo9("2012-09-17"));
	}
	public String getTopDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月dd");
	    Calendar calendar = Calendar.getInstance();
	    String curr_date = sdf.format(calendar.getTime());   
		String weekday = "";
		try {
			Date dt = sdf.parse(curr_date);
			String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
					"星期六" };
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0) {
				w = 0;
			}
			weekday = weekDays[w];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curr_date + " " + weekday;

	}
}
