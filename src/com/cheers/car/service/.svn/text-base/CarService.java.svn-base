package com.cheers.car.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.account.bean.AccountBean;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;

public class CarService extends Dao {
	
	public String addcar(){//添加车辆
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		m.put("carnoEQ", this.data.get("carno"));
		if("true".equals(checkDuplicate(m))){
			return getReturnString("该车牌号车辆已存在");
		}else {
			return getReturnString(super.add());
		}
	}
	
	/*
	 * 出车情况管理  添加  保存  方法
	 */
	public String addChuCar(){
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		m.put("carnoEQ", this.data.get("carno"));
		
		this.session = request.getSession();
		DateFormat df = new DateFormat();//设置日期格式
		String now=df.getNowTime();
		this.data.put("createtime", now);
		this.data.put("creatorid", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		
		return getReturnString(super.add());
//		if("true".equals(checkDuplicate(m))){
//			return getReturnString("该车牌号车辆已存在");
//		}else {
//			return getReturnString(super.add());
//		}
	}
	
	
	public String update(){
		return getReturnString(super.update());
	}
	
	public String delete(){
		return getReturnString(super.delete());
	}
	
	public String updatefieldByIds(){
		return getReturnString(super.updatefieldByIds());
	}
}
