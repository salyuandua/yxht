package com.cheers.location.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.dao.Dao;
import com.fasterxml.jackson.core.JsonProcessingException;

public class LocationService extends Dao {
	
	
	public String add(){
		
			return getReturnString(super.add());
	}
	public String update(){

					return getReturnString(super.update());
	}
	
	public String delete(){
		return getReturnString(super.delete());
	}
	
	public String getLocation(){
		String sql="select * from (select a.id accountid, a.name,a.telephone,l.latitude,l.longtitude,l.createtime from PhotoUpload l left join sysaccount a on a.id=l.accountid ) m where 1=1 " ;
		return getBeansBySql(sql);
	}
	/*
	public String getLocation(){
		String accountid=(String)this.map.get("accountidEQ");
		String createtimeGE=(String)this.map.get("createTimeGE");
		String createtimeLE=(String)this.map.get("createTimeLE");
		String sql="select top 100 percent* from " +
				"(select top 100 percent a.id accountid, a.name,a.telephone mobile," +
				"l.signinlatitude latitude," +
				"l.signinlontitude longitude," +
				"l.signintime screatetime," +
				"l.rqtime createtime,'0' sflag " +
				"from " +
				"businessplan_kq l,sysaccount a  " +
				"where " +
				"l.accountid = a.id  order by screatetime  " +
				"union all " +
				"select top 100 percent a.id accountid, a.name,a.telephone mobile,l.signoutlatitude latitude," +
				"l.signoutlontitude longitude,l.signouttime screatetime,l.rqtime createtime," +
				"'1' sflag from businessplan_kq l,sysaccount a  " +
				"where  " +
				"l.accountid = a.id  order by screatetime) m " +
				"where " +
				"1=1 and latitude is not null and longitude is not null and accountid="+accountid+" " +
				"and screatetime >='"+createtimeGE+" 00:00:00:000'  " +
				"and screatetime<='"+createtimeLE+" 23:59:59:000' order by screatetime,sflag" ;
				//"where 1=1 a.id=l.accountid and a.id=? and l.createtime>=? and l.createtime<=?";
		 System.out.println("---------------------"+sql);
		excuteSQLPageQuery(sql);
		result = getRows();
		return getReturn();
		
		
		//return getBeansBySql(sql);
	}
	*/
	/*public String getLocation(){
		try {
			super.getTablesByParams();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("aaaaaa"+getReturn());
		return getReturn();
	}*/
}
