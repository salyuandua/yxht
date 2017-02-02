package com.cheers.system.logs.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.system.logs.bean.LogBean;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;
import com.cheers.util.JsonUtil;

public class LogsService extends Dao {

	final static Log LOG = LogFactory.getLog(LogsService.class);
	/**
	 * 写日志
	 * 
	 * @return 0不成功；1成功
	 * @throws DbException
	 */
	public static int write(LogBean bean) throws DbException {
		int result = 0;
		DataBase db = null;
		try {
			db = new DataBase();
			db.connectDb();
			PreparedStatement ps = db.getPreparedStatement("insert into syslog(content,createtime,grade,address,ip,title,oper) values (?,?,?,?,?,?,?)");
			ps.setString(1, bean.getContent());
			ps.setString(2, bean.getCreateTime());
			ps.setString(3, bean.getGrade());
			ps.setString(4, bean.getAddress());
			ps.setString(5, bean.getIp());
			ps.setString(6, bean.getTitle());
			ps.setString(7, bean.getOper());
			result = ps.executeUpdate();
		} catch (Exception e) {
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
		return result;
	}

	/**
	 * 写日志
	 * 
	 * @param content
	 *            日志内容
	 * @return 0不成功；1成功
	 * @throws DbException
	 */
	public static int write(String content) {
		int result = 0;
		try {
			LogBean bean = new LogBean();
			bean.setContent(content);
			result = write(bean);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 写日志
	 * 
	 * @param content
	 *            日志内容
	 * @param oper
	 *            操作人
	 * @return 0不成功；1成功
	 * @throws DbException
	 */
	public static int write(String content, String oper) {
		int result = 0;
		try {
			LogBean bean = new LogBean();
			bean.setContent(content);
			bean.setOper(oper);
			result = write(bean);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 写日志
	 * 
	 * @param content
	 *            日志内容
	 * @param oper
	 *            操作人
	 * @param ipaddress
	 *            ip地址
	 * @param title
	 *            题目
	 * @return 0不成功；1成功
	 * @throws DbException
	 */
	public static int write(String content, String oper, String ipAddress, String title) {
		int result = 0;
		try {
			LogBean bean = new LogBean();
			bean.setTitle(title);
			bean.setContent(content);
			bean.setOper(oper);
			bean.setIp(ipAddress);
			bean.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			result = write(bean);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 写日志
	 * 
	 * @param content
	 *            日志内容
	 * @param address
	 *            写日志的程序名
	 * @param oper
	 *            操作人
	 * @param ipaddress
	 *            ip地址
	 * @param title
	 *            题目
	 * @return 0不成功；1成功
	 * @throws DbException
	 */
	public static int write(String content, String oper, String ipAddress, String address, String title) {
		int result = 0;
		try {

			LogBean bean = new LogBean();
			bean.setTitle(title);
			bean.setContent(content);
			bean.setAddress(address);
			bean.setOper(oper);
			bean.setIp(ipAddress);
			result = write(bean);
		} catch (Exception e) {
		}
		return result;

	}

	/**
	 * 写日志
	 * 
	 * @param content
	 *            日志内容
	 * @param grade
	 *            日志级别. 0，info级别 1 error级别
	 * @param address
	 *            写日志的程序名
	 * @param oper
	 *            操作人
	 * @param ipaddress
	 *            ip地址
	 * @param title
	 *            题目
	 * @return 0不成功；1成功
	 * @throws DbException
	 */
	public static int write(String content, String oper, String ipAddress, String address, String grade, String title) {
		int result = 0;
		try {
			LogBean bean = new LogBean();
			bean.setTitle(title);
			bean.setContent(content);
			bean.setAddress(address);
			bean.setOper(oper);
			bean.setIp(ipAddress);
			bean.setGrade(grade);
			bean.setCreateTime(DateFormat.getNowTime());
			result = write(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public String getBeans(String rowsPerPage, String curPage, Map params) {
			StringBuffer sql = new StringBuffer("select top 100 percent a.* from syslog a where 1=1");
			sql.append(super.getLikeStr("title", DataFormat.objectCheckNull(params.get("title"))));
			sql.append(super.getEQStr("oper", DataFormat.objectCheckNull(params.get("oper"))));
			sql.append(super.getGEStr("createtime", DataFormat.objectCheckNull(params.get("startTime"))));
			sql.append(super.getLEStr("createtime", DataFormat.objectCheckNull(params.get("endTime"))));
			sql.append(" order by createtime desc");
			excuteSQLPageQuery(sql.toString());
			return getReturn();
	}

	/**
	 * 按时间段删除记录
	 * 
	 * @param obj
	 * @param startTime
	 * @param endTime
	 * 
	 * @param tableName
	 * @return true or false
	 * @author apple
	 */
	public boolean deleteBean(Object obj, String startTime, String endTime, String tableName) {
		if(delete(obj, startTime, endTime, tableName)>0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 按时间段和对象删除记录
	 * 
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param tableName
	 * @return 1 or -1
	 * @author apple
	 */
	public int delete(Object obj, String startTime, String endTime, String tableName) {

		DataBase db = new DataBase();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from " + tableName + " where 1=1");
		if (!DataFormat.booleanCheckNull(obj)) {
			Field[] fields = obj.getClass().getDeclaredFields();
			String[] fieldNames = new String[fields.length];
			String fieldName = "", value = "";
			for (int i = 0; i < fields.length; i++) {
				fieldNames[i] = fields[i].getName();
				fieldName = fieldNames[i];
				try {
					if (!"_".equals(fieldName.substring(0, 1)) && !fieldName.endsWith("List")) {
						String firstLetter = fieldName.substring(0, 1).toUpperCase();
						String getter = "get" + firstLetter + fieldName.substring(1);
						Type type = fields[i].getGenericType();
						Method method = obj.getClass().getMethod(getter, new Class[]{(Class) type});
						sql.append(" and ");
						sql.append(fieldName);
						sql.append(" =' ");
						sql.append(method.invoke(obj));
						sql.append("' ");
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		if (!DataFormat.checkNull(startTime)) {
			sql.append(" and createtime >=' ");
			sql.append(startTime + "'	");

		}
		if (!DataFormat.checkNull(endTime)) {
			sql.append(" and createtime <= '");
			sql.append(endTime + "'	");
		}
		try {
			db.connectDb();
			return db.update(sql.toString());

		} catch (DbException e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				db.close();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	}

	public static int write(String content, String oper, String title) {
		int result = 0;
		try {
			LogBean bean = new LogBean();
			bean.setTitle(title);
			bean.setContent(content);
			bean.setOper(oper);
			bean.setCreateTime(DateFormat.getNowTime());
			result = write(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;		
	}
	public String deleteByTime(){
		String begin=(String) this.data.get("startTime");
		String end=(String) this.data.get("endTime");
		String sql="delete from syslog where 1=1 ";
		if(begin!=null&&!begin.equals(""))
			sql+= " and createTime>='"+begin+"'  ";
		if(end!=null&&!end.equals(""))
		sql+=" and createTime<='"+end+"'";
		DataBase db = null;
		try {
			db = new DataBase();
			db.connectDb();
			db.update(sql);
			   return getReturnString("true");
		}catch(Exception e){
			e.printStackTrace();
			return getReturnString("false");
		}
	}
	
}
