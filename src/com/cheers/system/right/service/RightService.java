package com.cheers.system.right.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.DataFormat;
import com.cheers.util.MyDao;

public class RightService extends Dao{
	/**
	 * 角色权限分配 获取全部权限
	 * @return
	 */
	public List getLeftTreeMap(String accountId){
	MyDao dao = new MyDao();
	DataBase db = new DataBase();
	List<Object> list = new ArrayList<Object>();
	try {
		db.connectDb();
		String sql=" select r.id from sysaccount ar,sysroleright rr,sysright r  where ar.roleid = rr.roleid and rr.rightid = r.id and ar.id = "
				+ accountId + " and r.isenable=1";
		ResultSet rs = db.queryAll(sql);
		List<String> right = new ArrayList<String>();
		if(rs!=null){
			while(rs.next()){
				right.add(rs.getString(1));
			}
		}
		//right.add("00000");//[10100,10101...,0000]
		rs=null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("text", "");
		map.put("id", "");
		for(int i = 0; i< 99; i++){
			String temp = "1"+String.format("%02d", i)+"00";
			if(right.contains(temp)){
			String str = " select  r.* from sysright r where id = '1"+String.format("%02d", i)+"00'  order by id asc";
			 rs = db.queryAll(str);
			if(rs!=null)
			 while(rs.next()){
				 Map<String ,Object> map2 = new HashMap<String ,Object>();
				 map2.put("text",DataFormat.stringCheckNullNoTrim(rs.getString(2), ""));
				 map2.put("id",DataFormat.stringCheckNullNoTrim(rs.getString(3), ""));
				 map2.put("items",this.getLeftTreeChild("1"+String.format("%02d", i),right));
				 list.add(map2);
			 }
			}
		}
		System.out.println("hello"+map);
		map.put("items", list);
		return list;
	} catch (DbException de) {
		de.printStackTrace();
		return null;
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	} finally {
		try {
			db.close();
		} catch (Exception de) {
			de.printStackTrace();
		}
	}
	}
	/**
	 * 角色权限分配 获取全部权限
	 * @return
	 */
	public List getRightMap(){
	MyDao dao = new MyDao();
	DataBase db = new DataBase();
	List list = new ArrayList();
	try {
		db.connectDb();
		
		Map map = new HashMap();
		map.put("id", "");
		map.put("text", "");
		for(int i = 0; i< 30; i++){
			String str = " select  r.* from sysright r where id = '1"+String.format("%02d", i)+"00'  order by id asc";
			System.out.println(str);
			ResultSet rs = db.queryAll(str);
			if(rs!=null)
			 while(rs.next()){
				 Map map2 = new HashMap();
				 map2.put("id",DataFormat.stringCheckNullNoTrim(rs.getString(1), ""));
				 map2.put("text",DataFormat.stringCheckNullNoTrim(rs.getString(2), ""));
				 List children = this.getchild("1"+String.format("%02d", i));
				 if(children.size()>0)
				 	map2.put("state", "closed");
				 map2.put("children",children);
				 list.add(map2);
			 }
			
		}
		System.out.println("hello"+map);
		map.put("children", list);
		return list;
	} catch (DbException de) {
		de.printStackTrace();
		return null;
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	} finally {
		try {
			db.close();
		} catch (Exception de) {
			de.printStackTrace();
		}
	}
	}
	public List getchild(String profix){
		DataBase db = new DataBase();
		List list = new ArrayList();
		try {
		String str = " select  r.* from sysright r where id like '"+profix+"__' and id !='"+profix+"00"+"' order by id asc";
		System.out.println(str);
		ResultSet rs = db.queryAll(str);
		if(rs!=null)
		 while(rs.next()){
			 Map map = new HashMap();
			 map.put("id",DataFormat.stringCheckNullNoTrim(rs.getString(1), ""));
			 map.put("text",DataFormat.stringCheckNullNoTrim(rs.getString(2), ""));
			 /* 大于二级的时候打开
			 List children = this.getchild(rs.getString(1));
			 if(children.size()>0)
			 map.put("state", "closed");
			 map.put("children",children);
			  * */
			 list.add(map);
		 }
		 return list;
	} catch (DbException de) {
		de.printStackTrace();
		return null;
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	} finally {
		try {
			db.close();
		} catch (Exception de) {
			de.printStackTrace();
		}
	}
		
	}
	

	public List getLeftTreeChild(String profix,List right){
		DataBase db = new DataBase();
		List list = new ArrayList();
		try {
			
		String str = " select  r.* from sysright r where id like '"+profix+"__' and id !='"+profix+"00"+"' order by id asc";
		ResultSet rs = db.queryAll(str);
		if(rs!=null)
		 while(rs.next()){
			 if(right.contains(DataFormat.stringCheckNullNoTrim(rs.getString(1), ""))){
			 Map map = new HashMap();
			 map.put("text",DataFormat.stringCheckNullNoTrim(rs.getString(2), ""));
			 map.put("id",DataFormat.stringCheckNullNoTrim(rs.getString(3), ""));
			 map.put("items",this.getLeftTreeChild(rs.getString(1),right)); 
			 list.add(map);
		 }
			}
		System.out.println("list:"+list);
		 return list;
	} catch (DbException de) {
		de.printStackTrace();
		return null;
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	} finally {
		try {
			db.close();
		} catch (Exception de) {
			de.printStackTrace();
		}
	}
		
	}
}
