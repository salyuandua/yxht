package com.cheers.system.role.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;

public class RoleService extends Dao{

	
	public String add(){
		Map map = new HashMap();
		map.put("table", "sysrole");
		//map.put("name", map.get("name"));
		map.put("nameEQ", this.data.get("name")); 
		if("true".equals(super.checkDuplicate(map))){
			return getReturnString("重复的名字！");
		}else{
			return getReturnString(super.add());
		}
	}
	
	public String update(){
		Map map = new HashMap();
		map.put("table", "sysrole");
		map.put("nameEQ", this.data.get("name")); 
		if("true".equals(super.checkDuplicate(map))){
			return getReturnString("重复的名字！");
		}else{
			return getReturnString(super.update());
		}
		
	}
	
	/**
	 * 修改时间2013年10月26日 17:01:41
	 * ljc
	 * role删除改为假删除，isenable===>0
	 * @param data
	 * @return
	 */
	public  String delete() {

		String sql="delete from sysrole  where id="+request.getParameter("id");
		db=new DataBase();
		try {
			db.connectDb();
			int i=db.update(sql);
			if(i>0)
				return getReturnString("true");
		} catch (DbException e) {
			e.printStackTrace();
		}
		return getReturnString("false");
	} 
	public String getRoleRight()   {
		String roleId=request.getParameter("roleId");
			String str = " select r.* from sysroleright rr,[sysright] r  where rr.rightid = r.id ";
			str+="and rr.roleId = '"+ roleId + "'";
			excuteSQLPageQuery(str);
			result = getRows();
			return getReturn();
	}
	/**
	 * 设置角色权限
	 * 
	 * @param roleId
	 * @param rightId
	 * @return
	 * @throws SQLException
	 */
	public String setRoleRight()  {
		String roleId=request.getParameter("roleId");
		String ids=request.getParameter("rightId");
		String rightId[]=null;
		if(ids!=null) {
			JSONArray jsonarray = JSONArray.fromObject(ids);
			rightId = new String[jsonarray.size()];
			for(int i=0;i<jsonarray.size();i++){
				rightId[i]=(String) jsonarray.get(i);
			}
		}
		DataBase db = new DataBase();
		try {
			db.connectDb();
			String str[] = new String[rightId.length+1];
			str[0]="delete from sysroleright where roleId="+roleId;
			for(int i = 1; i < str.length; i++){
				str[i] = "insert into sysroleright (roleid,rightId,OPERATIONRIGHT)	values ("+roleId+ ","+rightId[i-1]+",2) ";
			}
			db.updateBatch(str);
			return getReturnString("true");
		} catch (Exception de) {
			de.printStackTrace();
			return  getReturnString("false");
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
				return  getReturnString("false");
			}
		}
	}
	 
}
