package com.cheers.returnmoney.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

public class ReturnmoneyService extends Dao {
	
	public String add(){//添加回款
		DateFormat dfd = new DateFormat();//设置日期格式
		String now=dfd.getNowTime();
		this.data.put("creatorid", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		this.data.put("createtime", now);
		return getReturnString(super.add());
	}
		 public String getBeansordercode(){
			 this.session = request.getSession();
				String accountid = ((AccountBean)this.session.getAttribute("accountBean")).getId();
				String canAccountIds = ((AccountBean)this.session.getAttribute("accountBean")).get_canAccountIds();//当前用户的下级用户
				SqlUtil sqlUtil = new SqlUtil();
				StringBuffer sql = new StringBuffer("select top 100 percent id value,code text from orders where 1=1 ");
				sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"creatorid")+"or creatorid='"+accountid);
				sql.append("') order by id desc");
				excuteSQLPageQuery(sql.toString());
				System.out.println("searchOrders     "+sql);
				result = getRows();
				return getReturn();
			}
	public String update(){
		DateFormat dfd = new DateFormat();//设置日期格式
		String now=dfd.getNowTime();
		this.data.put("modifierid", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		this.data.put("modifttime", now);
		return getReturnString(super.update());
	}
	
	public String delete(){ 
		return getReturnString(super.delete());
	}
	
	public String updatefieldByIds(){
		return getReturnString(super.updatefieldByIds());
	}
}
