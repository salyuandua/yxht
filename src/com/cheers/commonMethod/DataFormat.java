package com.cheers.commonMethod;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.cheers.dao.Dao;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;
import com.cheers.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;


public class DataFormat extends Dao{
	
	/**
	 * 判断字符串是否空，若是则返回"",否则返回str.trim();
	 * @param str
	 * @return
	 */
	public String stringCheckNull(String str){
		if(str == null || str.trim().equals("") || str.equals("null") || str.equals("NULL")){
			return "";
		}else {
			return str.trim();	
		}
	}
	/**
	 * 判断字符串是否空，若是则返回"",否则返回str.trim();
	 * @param str
	 * @return
	 */
	public static String objectCheckNull(Object str){
		if(str == null || str.toString().trim().equals("") || str.equals("null") || str.equals("NULL")){
			return "";
		}else {
			return str.toString().trim();	
		}
	}
	/**
	 * 判断字符串是否空，若是则返回true,否则返回false;
	 * @param str 
	 * @return
	 */
	public static boolean booleanCheckNull(Object str){
		if(str == null || str.toString().trim().equals("") || str.equals("null") || str.equals("NULL")){
			return true;
		}else {
			return false;	
		}
	}
	/**
	 * 字符串回车替换
	 * @param str  被替换字符串
	 * @param str2  替换字符串
	 * @return
	 */
	public String replaceNnR(String str,String str2){
		if(str == null || str.trim().equals("") || str.equals("null") || str.equals("NULL"))
			return "";
		else
			return str.replaceAll("\\r|\n",str2);
	}
	/**
	 * 判断字符串是否空，若是则返回"",否则返回str;
	 * @param str
	 * @param str2
	 * @return
	 */
	public String replaceNnRnTnS(String str,String str2){
		if(str == null || str.trim().equals("") || str.equals("null") || str.equals("NULL"))
			return "";
		else
			return str.replaceAll("\\s*|\t|\r|\n",str2);
	}
	/**
	 * 判断字符串是否空，若是则返回"",否则返回str;
	 * @param str
	 * @return
	 */
	public String stringCheckNullNoTrim(String str){
		if(str == null || str.trim().equals("") || str.equals("null") || str.equals("NULL")){
			return "";
		}else {
			return str;	
		}
	}
	
	/**
	 * 判断字符串是否空，若是则返回defaultValue,否则返回str.trim();
	 * @param str
	 * @return
	 */
	public String stringCheckNull(String str, String defaultValue){
		if(str == null || str.trim().equals("") || str.equals("null") || str.equals("NULL")){
			return defaultValue;
		}else {
			return str.trim();	
		}
	}
	
	/**
	 * 判断字符串是否空，若是则返回defaultValue,否则返回str;
	 * @param str
	 * @return
	 */
	public String stringCheckNullNoTrim(String str, String defaultValue){
		if(str == null || str.trim().equals("") || str.equals("null") || str.equals("NULL")){
			return defaultValue;
		}else {
			return str;	
		}
	}
	
	/**
	 * 判断字符串是否空，若是则返回"",否则返回str;
	 * @param str
	 * @return
	 */
	public String stringCheckNullNoTrim(Object obj){
		if(obj == null || obj.toString().equals("") || obj.equals("null") || obj.equals("NULL")){
			return "";
		}else {
			return obj.toString();	
		}
	}
	
	/**
	 * 判断字符串是否为空 返回true false
	 * @param str
	 * @return
	 */
	public boolean checkNull(String str){
		boolean boo = false;
		if(str == null || str.trim().equals("") || str.equals("null")){
			boo = true;
		}else if(str.length() == 0){
			boo = true;
		}
		return boo;
	}
	
	/**
	 * 将 日期字符串 转换为oracle识别的日期
	 * 参数 date 格式为yyyy-MM-dd HH:mm:ss
	 */
	public String databaseToTime(String date){		
		return "to_date('"+date+"','yyyy-mm-dd hh24:mi:ss')";
	}
	/**
	 * 返回数据库时间查询起始日期	
	 * @param date
	 * @return
	 */
	public String fromDatabaseToTime(String date){	//返回数据库时间查询起始日期	
		return " >= to_date('"+date+" 00:00:00','yyyy-mm-dd hh24:mi:ss') ";
	}
	/**
	 * 返回数据库时间查询截止日期
	 * @param date
	 * @return
	 */
	public static String toDatabaseToTime(String date){		//返回数据库时间查询截止日期
		return " <= to_date('"+date+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ";
	}
	/**
	 * 截取时间 9位，显示结果 2012-01-01
	 * @param date
	 * @return
	 */
	public String subTimeTo9(String date){		//截取时间 9位，显示结果 2012-01-01
		return stringCheckNull(date).length()>10?stringCheckNull(date).substring(0,10):"";
	}
	/**
	 * 截取时间 19位，显示结果 2012-01-01 12:12:12
	 * @param date
	 * @return
	 */
	public String subTimeTo19(String date){		//截取时间 19位，显示结果 2012-01-01 12:12:12
		return stringCheckNull(date).length()>19?stringCheckNull(date).substring(0,19):"";
	}
	
	//测试时使用
	public void Out(String message){
		if(true){
			System.out.println(message);
			//new WriteLog().writeLog(message);
		}else{
			//new WriteLog().writeLog(message);
		}
	}
	
	//需要保存到日志文件里的输出
	public void OutSave(String message){
		if(true){
			//new WriteLog().writeLog(message);
		}			
	}
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String removeSignChar(String s) {
    String[] result = s.split("－");
    String t = "";
    for (int i = 0; i < result.length; i++)
     t += result[i];    
    return t;
	}
	
	
	/**
	 * 使长字符串（oldstr）自动换行
	 * @param oldstr
	 * @param rowLength:指定的每行的长度
	 * @return
	 */
	public static String wordWrap(String oldstr,int rowLength) {
		String newstr = "";
		if(!"".equals(new DataFormat().stringCheckNullNoTrim(oldstr))) {
			int h=oldstr.length()/rowLength+1;
			for(int i=1;i<=h;i++) {
				if(oldstr.length()/(i*rowLength)>0) {
					newstr += oldstr.substring((i-1)*rowLength, i*rowLength)+"<br/>";
				}else {
					newstr += oldstr.substring((i-1)*rowLength, oldstr.length());
					break;
				}
			}
		}else {
			newstr = "";
		}
		return newstr;
	}
	
	/** * 提供精确的乘法运算。 
   * @param v1  
   * @param v2  
   * @return 两个参数的积 */ 
  public static double multiply(double v1, double v2) { 
  	BigDecimal b1 = new BigDecimal(Double.toString(v1)); 
  	BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
  	return b1.multiply(b2).doubleValue(); 
  }
  /**
   * 提供精确的乘法运算
   * @param v1
   * @param v2
   * @return 两个参数的数学积，以字符串格式返回
   */
  public static String multiply(String v1,String v2){
  	BigDecimal b1=new BigDecimal(v1);
  	BigDecimal b2=new BigDecimal(v2);
  	return b1.multiply(b2).toString();
  }
  
  /**
   * 提供精确的减法运算
   * @param v1
   * @param v2
   * @return 两个参数数学差，以字符串格式返回
   */
  public static String subtract(String v1,String v2){
  	BigDecimal b1=new BigDecimal(v1);
  	BigDecimal b2=new BigDecimal(v2);
  	return b1.subtract(b2).toString();
  }
  
  /** 提供精确的减法运算。
   *  @param v1 
   *  @param v2 
   *  @return 两个参数的差 */ 
  public static double subtract(double v1, double v2) { 
  	BigDecimal b1 = new BigDecimal(Double.toString(v1)); 
  	BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
  	return b1.subtract(b2).doubleValue();
  }
  
  /** * 提供精确的加法运算
   *  @param v1  
   *  @param v2  
   *  @return 两个参数数学加和，以字符串格式返回 
   *  */ 
  public static String add(String v1, String v2) { 
  	BigDecimal b1 = new BigDecimal(v1); 
  	BigDecimal b2 = new BigDecimal(v2); 
  	return b1.add(b2).toString(); 
  } 
  /** 提供精确的加法运算。
   *  @param v1 
   *  @param v2
   *   @return 两个参数的和 */ 
  public static double add(double v1, double v2) { 
  	BigDecimal b1 = new BigDecimal(Double.toString(v1)); 
  	BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
  	return b1.add(b2).doubleValue(); 
  }
  
  /**
   * 
   * @param parameterName 需要转码的字符串在input中的name
   * @param request
   * @param isNeedChangeCode是否需要转码，false不需要，true需要
   * @return
   */
  public String changeCode(String parameterName,HttpServletRequest request,boolean isNeedChangeCode){
  	String str=request.getParameter(parameterName);
  	if(isNeedChangeCode && !checkNull(str)){
  		try {
  			str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
  	}
  	return str;
  }
  
  /**
   * 将数字保留规定的小数位
   * @param format
   * @param num
   * @return
   */
  public String numberFormat(String format,double num){
	  DecimalFormat qdf = new DecimalFormat(format);
	  return qdf.format(num);
  }
  /**
   * 将数字保留规定的小数位
   * @param format
   * @param num
   * @return
   */
  public String numberFormat(String format,String num){
	  DecimalFormat qdf = new DecimalFormat(format);
	  return qdf.format(Double.parseDouble(num));
  }
  public static String format(String input,DecimalFormat df) {
	  return noE(df.format(Double.parseDouble(input)));
  }
  public static String format(Double input,DecimalFormat df) {
	  return noE(df.format(input));
  }
  public static String format(Float input,DecimalFormat df) {
	  return noE(df.format(input));
  }
  public static DecimalFormat getDecimalFormat(int d) {
		String f = "";
		if(d!=0) {
			f += ".";
			for(int i=0;i<d;i++) {
				f += "0";
			}
		}
		DecimalFormat df =  new DecimalFormat("###############0"+f);
		return df;
	}
  
    //保留几位小数 末尾0不删除
	public static String getNumber(String number,int d) {
		return DataFormat.format(number, DataFormat.getDecimalFormat(d));
	}
	//保留几位小数 末尾0不删除
	public static String getNumber(double number,int d) {
		return DataFormat.format(number, DataFormat.getDecimalFormat(d));
	}
	//保留几位小数 末尾0删除
	public static String getTruncateZeroNumber(String number,int d) {
		return DataFormat.truncateZero(DataFormat.format(number, DataFormat.getDecimalFormat(d)));
	}
	//保留几位小数 末尾0删除
	public static String getTruncateZeroNumber(double number,int d) {
		return DataFormat.truncateZero(DataFormat.format(number, DataFormat.getDecimalFormat(d)));
	}
	public static String noE(String s) {
		BigDecimal bg=new BigDecimal(s);
		return bg.toPlainString();
	}
	public static String noE(double s) {
		BigDecimal bg=new BigDecimal(s);
		return bg.toPlainString();
	}
	public static String truncateZero(double a) {
		String s = "";
		int i; 
		boolean flag=true; 
		Double d = new Double(a);
		String b=(new Double(d.doubleValue())).toString(); 
		if(b.indexOf( ".")!=-1) { 
			i=b.indexOf( ".")+1; 
			for(;i <b.length();i++) { 
				if(b.charAt(i)!= '0') { 
					flag=false; 
					break; 
				} 
			} 
		} 
		if(flag==true) { 
			s = String.valueOf(d.intValue());
		} else { 
			s = String.valueOf(d.doubleValue());
		} 
		return noE(s);
	}
	public static String truncateZero(String a) {
		String s = "";
		int i; 
		boolean flag=true; 
		Double d = new Double(Double.parseDouble(a));
		String b=(new Double(d.doubleValue())).toString(); 
		if(b.indexOf( ".")!=-1) { 
			i=b.indexOf( ".")+1; 
			for(;i <b.length();i++) { 
				if(b.charAt(i)!= '0') { 
					flag=false; 
					break; 
				} 
			} 
		} 
		if(flag==true) { 
			s = String.valueOf(d.intValue());
		} else { 
			s = String.valueOf(d.doubleValue());
		} 
		return noE(s);

	}
	/**
	 * 完成率＝（2－实际完成数/目标完成数）*100% 
	   注意，这个公式只适用于目标数值是负值的情况下！
	 * @param lrSJ 实际利润
	 * @param lrMB 目标利润
	 * @return liRunRate 完成率
	 */
	public static String getLirunRate(String lrSJ,String lrMB) {
		String liRunRate = "";
		double lrSJDouble = Double.parseDouble(lrSJ);
		double lrMBDouble = Double.parseDouble(lrMB);
		//方法
		if(lrMBDouble>0) {//目标利润大于0
			liRunRate = lrSJDouble/lrMBDouble+"";
 		}else if(lrMBDouble==0) {//目标利润等于0
 			//liRunRate = lrSJDouble+1 + "";
 			liRunRate = "0";
 		}else if(lrMBDouble<0) {//目标利润为负数
 			liRunRate = (2-lrSJDouble/lrMBDouble) + "";
 		}
		return liRunRate;
	}
	/**
	 * 合计分值 封顶计算
	 * @param sumScore
	 * @param yearProfit
	 * @return
	 */
	public static String getScoreFD(String sumScore,String yearProfit) {
		String sumScoreFD = sumScore;
		//封顶验证  年度指标(新华)小于100 200封顶  大于等于100 300封顶
 		if(Double.parseDouble(yearProfit)<100) {
 			if(Double.parseDouble(sumScore)>200) {
 				sumScoreFD = "200";
 			}
 		}else {
 			if(Double.parseDouble(sumScore)>300) {
 				sumScoreFD = "300";
 			}
 		}
 		
		return sumScoreFD;
	}
	/**
	 * 利润分值 招商普药
	 * <0 设置为0  大于100 设置为100
	 * @param sumScore
	 * @param yearProfit
	 * @return
	 */
	public static String getLrScore(String lrScore) {
		String lrScoreFD = lrScore;
 		if(Double.parseDouble(lrScore)<0) {
 			lrScoreFD = "0";
 		}
 		if(Double.parseDouble(lrScore)>100) {
 			lrScoreFD = "100";
 		}
		return lrScoreFD;
	}
	
	public boolean isInt(String num){
		boolean ret = true;
		try{
			Integer.parseInt(num);
		}catch(Exception e){
			ret = false;
		}
		return ret;
	}
	
	public boolean isFloat(String num){
		boolean ret = true;
		try{
			Float.parseFloat(num);
		}catch(Exception e){
			ret = false;
		}
		return ret;
	}
	
	public boolean isDate(String date){
		boolean ret = true;
		try{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.parse(date);
		}catch(Exception e){
			ret = false;
		}
		return ret;
	}
	
	public String isDateReturn(String date){
		String ret = date;
		try{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.parse(date);
		}catch(Exception e){
			ret = new DateFormat().getNowDate();
		}
		return ret;
	}
	
	/**验证导入数据 是否 存在 方法
	 * @author sunjianfeng 2013-11-09 10:07:20
	 * @param valueColumn 
	 * @param tableName
	 * @param key
	 * @param value
	 * @param addSql 可以 拼接的sql语句
	 * @return
	 */
	public String getValueFromTable(String valueColumn,String tableName,String key,String value,String addSql){

		String sql = "select "+valueColumn+" from "+tableName+" where "+key+"='"+value+"'"+ addSql;
		//System.out.println("********************"+sql+"");
		DataBase dbs = null;
		
		String str = "";
		try {
			dbs = new DataBase();
			dbs.connectDb();
			
			ResultSet rs = dbs.queryAll(sql);
			while((rs!=null)&&(rs.next())){
				str = rs.getString(1);
			}
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbs.close();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}

	 	return str;
		
//		ret = "11";
//		return ret;
	}
	
	/**
	 * 导入时 根据 客户的 id 获取 对应的 业务员 id 部门、办事处、大区
	 */
	public Map<String,String> getFzrIdValue(String clientid){
		String sql = "SELECT     dbo.CLIENT.fzrId, dbo.ACCOUNT_POST.orgId, dbo.SYSTEM_TREE.name AS ywydepartment, CLIENT_1.qyId, SYSTEM_TREE_1.name AS qyname,"+ 
                " CLIENT_1.bscId AS bscid, SYSTEM_TREE_2.name AS bscname "+
                " FROM         dbo.SYSTEM_TREE AS SYSTEM_TREE_1 INNER JOIN "+
                " dbo.CLIENT AS CLIENT_1 ON SYSTEM_TREE_1.id = CLIENT_1.qyId INNER JOIN "+
                " dbo.SYSTEM_TREE AS SYSTEM_TREE_2 ON CLIENT_1.bscId = SYSTEM_TREE_2.id CROSS JOIN "+
                " dbo.CLIENT INNER JOIN "+
                " dbo.ACCOUNT_POST ON dbo.CLIENT.fzrId = dbo.ACCOUNT_POST.accountId INNER JOIN "+
                " dbo.SYSTEM_TREE ON dbo.ACCOUNT_POST.orgId = dbo.SYSTEM_TREE.id "+
                "WHERE     (dbo.CLIENT.id ='" + clientid + "')";
		System.out.println("********************getFzrIdValue"+sql+"");
		DataBase dbs = null;
		String str = "";
		Map<String, String> map = new HashMap<String, String>();
		try {
			dbs = new DataBase();
			dbs.connectDb();
			
			ResultSet rs = dbs.queryAll(sql);
			while((rs!=null)&&(rs.next())){
				String str1 = rs.getString(1);//业务员 id
				map.put("ywyid", str1);
				String str2 = rs.getString(2);//业务员 部门 id
				map.put("ywybmid", str2);
				String str4 = rs.getString(4);//大区 id
				map.put("qyid", str4);
				String str6 = rs.getString(6);//办事处 id
				map.put("bscid", str6);
			}
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
//	/** 导入客户数据时  验证大区是否存在  方法
//	 * @author sunjianfeng 2013-11-09 09:30:12 
//	 */
//	public String getValueFromTable
	
	
}
