package com.cheers.staffRecord.service;

import java.util.HashMap;
import java.util.Map;


import com.cheers.account.bean.AccountBean;
import com.cheers.dao.Dao;
import com.cheers.util.DateFormat;

public class StaffRecordService extends Dao {
	
	public String add(){//添加员工档案
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		m.put("idcardEQ", this.data.get("idcard"));
		if("true".equals(checkDuplicate(m))){
			return getReturnString("身份证号码重复请确认！");
		}else {
			return getReturnString(super.add());
		}
	}
	public String addwuliu(){//添加物流信息
		DateFormat dfd = new DateFormat();//设置日期格式
		String now=dfd.getNowTime();
		this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		this.data.put("createTime", now);
		return getReturnString(super.add());
	}
	public String updatewuliu(){//修改物流信息
		DateFormat dfd = new DateFormat();//设置日期格式
		String now=dfd.getNowTime();
		this.data.put("modifierId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		this.data.put("modifyTime", now);
		return getReturnString(super.update());
	}
	
	/**
	 * 获取物流下拉列表
	 * 2014-5-19 15:50:06
	 * @return
	 */
	public String searchWuliu(){
		String q=this.QUESTION;
		StringBuffer sql = new StringBuffer("select top 100 percent id value ,name text,isleaf FROM system_tree c where  categoryType=1 \n" +
				" and parentid!=0   and notetype=4 ");
		if(q!=null&&!"".equals(q)){
			 sql.append(" and  c.name like '%"+q+"%' ");
		 }
		sql.append(" order by vorder");
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
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
