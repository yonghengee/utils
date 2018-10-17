package com.yqh.shop.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期常用方法
 *
 * @author
 */
public class DateUtils {

    /**
     * 常用变量
     */
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_HMS = "HH:mm:ss";
    public static final String DATE_FORMAT_HM = "HH:mm";
    public static final String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YMDHMS = "yyyyMMddHHmmss";
    public static final long ONE_DAY_MILLS = 3600000 * 24;
    public static final int WEEK_DAYS = 7;
    private static final int dateLength = DATE_FORMAT_YMDHM.length();


    /**
     * 日期转换为制定格式字符串
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatDateToString(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }
    
    public static String getTomorrowDate(Date time, String format){
    	Date date=new Date();//取时间
    	Calendar calendar = new GregorianCalendar();
    	calendar.setTime(date);
    	calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
    	date=calendar.getTime(); //这个时间就是日期往后推一天的结果 
    //	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter = new SimpleDateFormat(format);
    	String dateString = formatter.format(date);
    	return dateString;
    }

    /**
     * 字符串转换为制定格式日期
     * (注意：当你输入的日期是2014-12-21 12:12，format对应的应为yyyy-MM-dd HH:mm
     * 否则异常抛出)
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     * @
     */
    public static Date formatStringToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        }
    }

    /**
     * 判断一个日期是否属于两个时段内
     *
     * @param time
     * @param timeRange
     * @return
     */
    public static boolean isTimeInRange(Date time, Date[] timeRange) {
        return (!time.before(timeRange[0]) && !time.after(timeRange[1]));
    }

    /**
     * 从完整的时间截取精确到分的时间
     *
     * @param fullDateStr
     * @return
     */
    public static String getDateToMinute(String fullDateStr) {
        return fullDateStr == null ? null
                : (fullDateStr.length() >= dateLength ? fullDateStr.substring(
                0, dateLength) : fullDateStr);
    }

    /**
     * 返回指定年度的所有周。List中包含的是String[2]对象 string[0]本周的开始日期,string[1]是本周的结束日期。
     * 日期的格式为YYYY-MM-DD 每年的第一个周，必须包含星期一且是完整的七天。
     * 例如：2009年的第一个周开始日期为2009-01-05，结束日期为2009-01-11。 星期一在哪一年，那么包含这个星期的周就是哪一年的周。
     * 例如：2008-12-29是星期一，2009-01-04是星期日，哪么这个周就是2008年度的最后一个周。
     *
     * @param year 格式 YYYY ，必须大于1900年度 小于9999年
     * @return @
     */
    public static List<String[]> getWeeksByYear(final int year) {
        int weeks = getWeekNumOfYear(year);
        List<String[]> result = new ArrayList<String[]>(weeks);
        int start = 1;
        int end = 7;
        for (int i = 1; i <= weeks; i++) {
            String[] tempWeek = new String[2];
            tempWeek[0] = getDateForDayOfWeek(year, i, start);
            tempWeek[1] = getDateForDayOfWeek(year, i, end);
            result.add(tempWeek);
        }
        return result;
    }

    /**
     * 计算指定年、周的上一年、周
     *
     * @param year
     * @param week
     * @return @
     */
    public static int[] getLastYearWeek(int year, int week) {
        if (week <= 0) {
            throw new IllegalArgumentException("周序号不能小于1！！");
        }
        int[] result = {week, year};
        if (week == 1) {
            // 上一年
            result[1] -= 1;
            // 最后一周
            result[0] = getWeekNumOfYear(result[1]);
        } else {
            result[0] -= 1;
        }
        return result;
    }

    /**
     * 下一个[周，年]
     *
     * @param year
     * @param week
     * @return @
     */
    public static int[] getNextYearWeek(int year, int week) {
        if (week <= 0) {
            throw new IllegalArgumentException("周序号不能小于1！！");
        }
        int[] result = {week, year};
        int weeks = getWeekNumOfYear(year);
        if (week == weeks) {
            // 下一年
            result[1] += 1;
            // 第一周
            result[0] = 1;
        } else {
            result[0] += 1;
        }
        return result;
    }

    /**
     * 计算指定年度共有多少个周。(从周一开始)
     *
     * @param year
     * @return @
     */
    public static int getWeekNumOfYear(final int year) {
        return getWeekNumOfYear(year, Calendar.MONDAY);
    }

    /**
     * 计算指定年度共有多少个周。
     *
     * @param year yyyy
     * @return @
     */
    public static int getWeekNumOfYear(final int year, int firstDayOfWeek) {
        // 每年至少有52个周 ，最多有53个周。
        int minWeeks = 52;
        int maxWeeks = 53;
        int result = minWeeks;
        int sIndex = 4;
        String date = getDateForDayOfWeek(year, maxWeeks, firstDayOfWeek);
        // 判断年度是否相符，如果相符说明有53个周。
        if (date.substring(0, sIndex).equals(year)) {
            result = maxWeeks;
        }
        return result;
    }

    public static int getWeeksOfWeekYear(final int year) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(WEEK_DAYS);
        cal.set(Calendar.YEAR, year);
        return cal.getWeeksInWeekYear();
    }

    /**
     * 获取指定年份的第几周的第几天对应的日期yyyy-MM-dd(从周一开始)
     *
     * @param year
     * @param weekOfYear
     * @param dayOfWeek
     * @return yyyy-MM-dd 格式的日期 @
     */
    public static String getDateForDayOfWeek(int year, int weekOfYear,
                                             int dayOfWeek) {
        return getDateForDayOfWeek(year, weekOfYear, dayOfWeek, Calendar.MONDAY);
    }

    /**
     * 获取指定年份的第几周的第几天对应的日期yyyy-MM-dd，指定周几算一周的第一天（firstDayOfWeek）
     *
     * @param year
     * @param weekOfYear
     * @param dayOfWeek
     * @param firstDayOfWeek 指定周几算一周的第一天
     * @return yyyy-MM-dd 格式的日期
     */
    public static String getDateForDayOfWeek(int year, int weekOfYear,
                                             int dayOfWeek, int firstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        cal.setMinimalDaysInFirstWeek(WEEK_DAYS);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        return formatDateToString(cal.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获取指定日期星期几
     *
     * @param datetime
     * @throws ParseException
     * @
     */
    public static int getWeekOfDate(String datetime) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(WEEK_DAYS);
        Date date = formatStringToDate(datetime, DATE_FORMAT_YMD);
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);

    }

    /**
     * 计算某年某周内的所有日期(从周一开始 为每周的第一天)
     *
     * @param yearNum
     * @param weekNum
     * @return @
     */
    public static List<String> getWeekDays(int yearNum, int weekNum) {
        return getWeekDays(yearNum, weekNum, Calendar.MONDAY);
    }

    /**
     * 计算某年某周内的所有日期(七天)
     *
     * @param year
     * @param weekOfYear
     * @param firstDayOfWeek
     * @return yyyy-MM-dd 格式的日期列表
     */
    public static List<String> getWeekDays(int year, int weekOfYear,
                                           int firstDayOfWeek) {
        List<String> dates = new ArrayList<String>();
        int dayOfWeek = firstDayOfWeek;
        for (int i = 0; i < WEEK_DAYS; i++) {
            dates.add(getDateForDayOfWeek(year, weekOfYear, dayOfWeek++,
                    firstDayOfWeek));
        }
        return dates;
    }

    /**
     * 获取目标日期的上周、或本周、或下周的年、周信息
     *
     * @param queryDate      传入的时间
     * @param weekOffset     -1:上周 0:本周 1:下周
     * @param firstDayOfWeek 每周以第几天为首日
     * @return
     * @throws ParseException
     */
    public static int[] getWeekAndYear(String queryDate, int weekOffset,
                                       int firstDayOfWeek) throws ParseException {

        Date date = formatStringToDate(queryDate, DATE_FORMAT_YMD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        calendar.setMinimalDaysInFirstWeek(WEEK_DAYS);
        int year = calendar.getWeekYear();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int[] result = {week, year};
        switch (weekOffset) {
            case 1:
                result = getNextYearWeek(year, week);
                break;
            case -1:
                result = getLastYearWeek(year, week);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 计算个两日期的天数
     *
     * @param startDate 开始日期字串
     * @param endDate   结束日期字串
     * @return
     * @throws ParseException
     */
    public static int getDaysBetween(String startDate, String endDate)
            throws ParseException {
        int dayGap = 0;
        if (startDate != null && startDate.length() > 0 && endDate != null
                && endDate.length() > 0) {
            Date end = formatStringToDate(endDate, DATE_FORMAT_YMD);
            Date start = formatStringToDate(startDate, DATE_FORMAT_YMD);
            dayGap = getDaysBetween(start, end);
        }
        return dayGap;
    }

    private static int getDaysBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / ONE_DAY_MILLS);
    }

    /**
     * 计算两个日期之间的天数差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysGapOfDates(Date startDate, Date endDate) {
        int date = 0;
        if (startDate != null && endDate != null) {
            date = getDaysBetween(startDate, endDate);
        }
        return date;
    }

    /**
     * 计算两个日期之间的年份差距
     *
     * @param firstDate
     * @param secondDate
     * @return
     */

    public static int getYearGapOfDates(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return 0;
        }
        Calendar helpCalendar = Calendar.getInstance();
        helpCalendar.setTime(firstDate);
        int firstYear = helpCalendar.get(Calendar.YEAR);
        helpCalendar.setTime(secondDate);
        int secondYear = helpCalendar.get(Calendar.YEAR);
        return secondYear - firstYear;
    }

    /**
     * 计算两个日期之间的月份差距
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static int getMonthGapOfDates(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return 0;
        }

        return (int) ((secondDate.getTime() - firstDate.getTime())
                / ONE_DAY_MILLS / 30);

    }

    /**
     * 计算是否包含当前日期
     *
     * @param dates
     * @return
     */
    public static boolean isContainCurrent(List<String> dates) {
        boolean flag = false;
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT_YMD);
        Date date = new Date();
        String dateStr = fmt.format(date);
        for (int i = 0; i < dates.size(); i++) {
            if (dateStr.equals(dates.get(i))) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 从date开始计算time天后的日期
     *
     * @param startDate
     * @param time
     * @return
     * @throws ParseException
     */
    public static String getCalculateDateToString(String startDate, int time)
            throws ParseException {
        String resultDate = null;
        if (startDate != null && startDate.length() > 0) {
            Date date = formatStringToDate(startDate, DATE_FORMAT_YMD);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, time);
            date = c.getTime();
            resultDate = formatDateToString(date, DATE_FORMAT_YMD);
        }
        return resultDate;
    }

    /**
     * 获取从某日期开始计算，指定的日期所在该年的第几周
     *
     * @param date
     * @param admitDate
     * @return
     * @throws ParseException
     * @
     */
    public static int[] getYearAndWeeks(String date, String admitDate)
            throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(formatStringToDate(admitDate, DATE_FORMAT_YMD));
        int time = c.get(Calendar.DAY_OF_WEEK);
        return getWeekAndYear(date, 0, time);
    }

    /**
     * 获取指定日期refDate，前或后一周的所有日期
     *
     * @param refDate    参考日期
     * @param weekOffset -1:上周 0:本周 1:下周
     * @param startDate  哪天算一周的第一天
     * @return yyyy-MM-dd 格式的日期
     * @throws ParseException
     * @
     */
    public static List<String> getWeekDaysAroundDate(String refDate,
                                                     int weekOffset, String startDate) throws ParseException {
        // 以startDate为一周的第一天
        Calendar c = Calendar.getInstance();
        c.setTime(formatStringToDate(startDate, DATE_FORMAT_YMD));
        int firstDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        // 获取相应周
        int[] weekAndYear = getWeekAndYear(refDate, weekOffset, firstDayOfWeek);
        // 获取相应周的所有日期
        return getWeekDays(weekAndYear[1], weekAndYear[0], firstDayOfWeek);
    }

    /**
     * 根据时间点获取时间区间
     *
     * @param hours
     * @return
     */
    public static List<String[]> getTimePointsByHour(int[] hours) {
        List<String[]> hourPoints = new ArrayList<String[]>();
        String sbStart = ":00:00";
        String sbEnd = ":59:59";
        for (int i = 0; i < hours.length; i++) {
            String[] times = new String[2];
            times[0] = hours[i] + sbStart;
            times[1] = (hours[(i + 1 + hours.length) % hours.length] - 1)
                    + sbEnd;
            hourPoints.add(times);
        }
        return hourPoints;
    }

    /**
     * 根据指定的日期，增加或者减少天数
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    /**
     * 根据指定的日期，增加或者减少分钟数
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addMinutes(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    /**
     * 根据指定的日期，增加或者减少小时数
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addHours(Date date, int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    /**
     * 根据指定的日期，类型，增加或减少数量
     *
     * @param date
     * @param calendarField
     * @param amount
     * @return
     */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * 获取当前日期的最大日期 时间2014-12-21 23:59:59
     *
     * @return
     */
    public static Date getCurDateWithMaxTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    /**
     * 获取当前日期的最小日期时间 2014-12-21 00:00:00
     *
     * @return
     */
    public static Date getCurDateWithMinTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
	/**
	 * 时间格式为 EEE MMM dd HH:mm:ss zzz yyyy
	 * @param data
	 * @return
	 */
	public static Date getENGLISHData(String data){
		try {
			SimpleDateFormat sfStart = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH) ;
			Date datas = sfStart.parse(data);
			return datas;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * String ->Date
	 * @param format 格式
	 * @param data 时间
	 * @return
	 */
	public static Date getDate(String format,String data){
		try {
			SimpleDateFormat sfStart = new SimpleDateFormat(format,Locale.ENGLISH) ;
			Date datas = sfStart.parse(data);
			return datas;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 这个方法用来判断该日期是否是一个时间
	 * @param format 传入判断日期的格式
	 * @param dataTime 传入需要判断的日期
	 * @return true表示是一个日期 false表示不是一个日期
	 */
	public static boolean isDateTime(String format,String dataTime){
		boolean returnBolean = true;
		try {
			SimpleDateFormat sfStart = new SimpleDateFormat(format,Locale.ENGLISH) ;
			sfStart.parse(dataTime);
		} catch (Exception e) {
			returnBolean = false;
		}
		return returnBolean;
	}
	
	/**
	 * 获取修改后的时间
	 * @author HouShuPeng
	 * @param date
	 * @param calendarVal
	 *            Calendar的参数， 如 Calendar.MINUTE
	 * @param addValue
	 * @return Date   
	 */
	public static Date getEditDate(Date date, Integer calendarVal,
			Integer addValue) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(calendarVal, addValue);
		Date editDate = cal.getTime();
		return editDate;
	}
	
    /**
     * 获取本月的最小日期 时间2014-12-21 00:00:00
     * @return
     */
    public static String getThisMonthWithMinTime() {
    	SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_FULL); 
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        c.set(Calendar.HOUR_OF_DAY, 00);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        String first = format.format(c.getTime());
        return first;
    }
    
    /**
     * 获取本月的最大日期 时间2014-12-21 23:59:59
     * @return
     */
    public static String getThisMonthWithMaxTime() {
    	SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_FULL); 
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));//设置为1号,当前日期既为本月第一天 
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        String end = format.format(c.getTime());
        return end;
    }
    
    /**
     * 获取本月的最大日期 时间2014-12-21 23:59:59
     * @return
     */
    public static String getThisYearWithMaxTime() {
    	SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_FULL); 
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));//设置为1号,当前日期既为本月第一天 
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        String end = format.format(c.getTime());
        return end;
    }
	
    
    /** 
     * 获取当年的第一天 
     * @param year 
     * @return 
     */  
    public static String getCurrYearFirst(){  
    	SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_FULL); 
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
       
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, currentYear);  
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        Date currYearFirst = calendar.getTime();  
        String first = format.format(currYearFirst);
        return first;  
    }  
      
    /** 
     * 获取当年的最后一天 
     * @param year 
     * @return 
     */  
    public static String getCurrYearLast(){  
    	SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_FULL); 
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, currentYear); 
        calendar.set(Calendar.YEAR, currentYear);  
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();  
        String last = format.format(currYearLast);
        return last;  
    }  
    
    /**
     * 获取一个日期到现在的时间差
     * @param date
     * @return 秒数
     */
    public static int getNowToDateOfTme(String date){
    	int time = 0;
    	try {
			SimpleDateFormat sfStart = new SimpleDateFormat(DATE_FORMAT_FULL,Locale.ENGLISH) ;
			Date dateTime = sfStart.parse(date);
			time = (int) ((dateTime.getTime() - new Date().getTime())/1000);
		} catch (Exception e) {
			return time;
		}
    	return time;
    }
    
    public static String getNextYearToday(String beginDate,String format){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format,Locale.ENGLISH) ;
        Date date = formatStringToDate(beginDate, format);
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
       // calendar.add(Calendar.DAY_OF_WEEK, -1);
        date = calendar.getTime();
        return dateFormat.format(date);
    }
    
    /***
     * 获取那年那月的最小日期日期
     * @param year 2017
     * @param month 6
     * @return
     */
    public static String getFristDayOfMonth(int year,int month)  
    {
    	String temp = "";
    	if(month < 10){
    		temp =  "0" + month;
    	}else {
    		temp = month + "";
    	}
    	String fristDayOfMonth = year + "-" + temp + "-" + "01";
        return fristDayOfMonth;  
    } 
    
    /***
     * 获取那年那月的最大日期
     * @param year 2017
     * @param month 6
     * @return
     */
    public static String getLastDayOfMonth(int year,int month)  
    {  
        Calendar cal = Calendar.getInstance();  
        //设置年份  
        cal.set(Calendar.YEAR,year);  
        //设置月份  
        cal.set(Calendar.MONTH, month-1);  
        //获取某月最大天数  
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
        //设置日历中月份的最大天数  
        cal.set(Calendar.DAY_OF_MONTH, lastDay);  
        //格式化日期  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String lastDayOfMonth = sdf.format(cal.getTime());  
          
        return lastDayOfMonth;  
    }  
    
    public static List<Date> dateSplit(Date startDate, Date endDate) throws Exception {
        if (!startDate.before(endDate))
            throw new Exception("开始时间应该在结束时间之后");
        Long spi = endDate.getTime() - startDate.getTime();
        Long step = spi / (24 * 60 * 60 * 1000);// 相隔天数

        List<Date> dateList = new ArrayList<Date>();
        dateList.add(startDate);
        for (int i = 1; i <= step; i++) {
            dateList.add(new Date(dateList.get(i - 1).getTime()
                    + (24 * 60 * 60 * 1000)));// 比上一天加一
        }
        return dateList;
    }
    
	public static void main(String[] args) {
		try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date start = sdf.parse("2015-4-20");
	        Date end = sdf.parse("2015-5-20");
	        List<Date> lists = dateSplit(start, end);
	        StringBuilder sb = new StringBuilder();
	        if (!lists.isEmpty()) {
	        	int i = 0;
	            for (Date date : lists) {
	                System.out.println(sdf.format(date));
	                if(lists.size() - i == 1){
	                	sb.append(sdf.format(date));
	                }else {
	                	sb.append(sdf.format(date) + ",");
	                }
	                i ++;
	            }
	        }
	        System.out.println(sb.toString());
	    } catch (Exception e) {
	    }
	}
}
