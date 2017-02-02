package com.cheers.commonMethod;

import java.sql.ResultSet;
import java.util.Map;

import com.cheers.database.DataBase;


public class BusinessMethod {

	/**
	 * 判断是否根据此属性进行查询语句的筛选 返回true false
	 * params中的key是否是null，如果不是null，判断是否是noSearchString
	 */
	public static boolean checkIsSearch(Map params, String key, String noSearchString){
		boolean boo = false;
		if(params.get(key) != null && !"null".equals(params.get(key)) && !"NULL".equals(params.get(key)) && !noSearchString.equals(params.get(key))){
			boo = true;
		}
		return boo;
	}
	
	//返回数据库中是否存在该记录  dataType包括:number、string、date
	public static boolean isExist(String tableName, String columName, String value, String dataType){
		boolean ret = false;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from "+tableName+" ");
		if("string".equals(dataType)){
			sql.append("where "+columName+" = '"+value+"'");
		}
		if("number".equals(dataType)){
			sql.append("where "+columName+" = "+value+"");
		}
		sql.append("");
		sql.append("");
		DataBase db = null;
		ResultSet rs = null;
		try {
			db = new DataBase();
			db.connectDb();
			rs = db.queryAll(sql.toString());
			if(rs!=null&&rs.next()){
				ret = true;
			}
			
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ret;
	}
	public static boolean isExistForBsc(String tableName,String clientName, String lxBscId){
		boolean ret = false;
		StringBuffer sql = new StringBuffer();
		if("CLIENT_NEW".equals(tableName)) {
			sql.append("select * from CLIENT_NEW where clientName = '"+clientName+"' and lxBscId = '"+lxBscId+"'");
		}
		if("CLIENT_MATCH".equals(tableName)) {
			sql.append("select cm.* from CLIENT_MATCH cm,CLIENT_NEW cn where cm.clientid = cn.id ");
			sql.append(" and cm.importClientName = '"+clientName+"' and cn.lxBscId = '"+lxBscId+"'");
		}
		sql.append("");
		sql.append("");
		DataBase db = null;
		ResultSet rs = null;
		try {
			db = new DataBase();
			db.connectDb();
			rs = db.queryAll(sql.toString());
			if(rs!=null&&rs.next()){
				ret = true;
			}
			
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ret;
	}
}
