package com.cheers.rest.service;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;
import com.fasterxml.jackson.core.JsonProcessingException;

public class RestService extends Dao {
	
	public String add(){
		DateFormat df = new DateFormat();//设置日期格式
		String now=df.getNowTime();
		this.data.put("createTime", now);
		this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		return getReturnString(super.add());
	}
	public String update(){
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		DateFormat dfd = new DateFormat();//设置日期格式
		String now=dfd.getNowTime();
		this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		this.data.put("modifierId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		this.data.put("modifyTime", now);
		this.data.put("createTime", now);
		return getReturnString(super.update());
	}

	public String delete(){
		return getReturnString(super.delete());
	}
   
	public String updatefieldByIds(){
		
		return getReturnString(super.updatefieldByIds());
	}

	 /**
		 * 查看下级和自己的休班申请
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearch(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from restView t where 1=1");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.creatorid"));
	        sql.append(" or t.creatorid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		public String getBeans(){
			 return  super.getBeans();
		 }
		
		 /**
		 * 查看下级和自己的出差总结
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchZj(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from businesssummaryView t where 1=1");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.creatorid"));
	        sql.append(" or t.creatorid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
}
