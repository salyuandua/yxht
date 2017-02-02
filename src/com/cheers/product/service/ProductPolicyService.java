package com.cheers.product.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.account.bean.AccountBean;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ProductPolicyService extends Dao {
	
	
	public String add(){

		this.session = request.getSession();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		this.data.put("createtime", df.format(new Date()));
		this.data.put("accountid", this.session.getAttribute("account"));
		
		return getReturnString(super.add());
		
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

	public String setCustomer(){
		this.session = request.getSession();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String now=df.format(new Date());
		//this.data.put("createtime", now);
		//this.data.put("accountid", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		List ids=(List) this.data.get("customerIds");
		if(ids!=null&&ids.size()>0){
		String[] sqls=new String[ids.size()+1];
		 sqls[0]="delete from ProductPolicyCDetail where productpolicyid="+this.data.get("productpolicyid");
		for (int i = 1; i <= ids.size(); i++) {
			System.out.println(((AccountBean)this.session.getAttribute("accountBean")).getId());
			sqls[i]="insert into ProductPolicyCDetail(productpolicyid,customerId,createtime,accountid) values(" +
					this.data.get("productpolicyid")+","+ids.get(i-1)+",'"+now+"',"+((AccountBean)this.session.getAttribute("accountBean")).getId()+")";
		}
		try {
			DataBase db=new DataBase();
			db.connectDb();
			db.updateBatch(sqls);
			return getReturnString("true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		return getReturnString("操作失败，请稍候再试");
		}
}
