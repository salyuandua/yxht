package com.cheers.system.baseData.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.dao.Dao;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;
import com.fasterxml.jackson.core.JsonProcessingException;

public class BaseDataService extends Dao {
	
	
	public String add(){
		Map map = new HashMap();
		map.put("table", this.data.get("table"));
		map.put("nameEQ", this.data.get("name"));
		map.put("typeEQ", this.data.get("type"));
		
		
		if("true".equals(checknametype(map))){
			return getReturnString("重复项");
		}else {
			return getReturnString(super.add());
		}	
	}
	/**检验添加数据 是否重复的 方法
	 * @author sunjianfeng
	 * 2013-11-04 14:26:34 
	 * @param map
	 * @return
	 */
	 public String checknametype(Map map)
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append("select top 100 percent * from "+ map.get("table") +" where name ='" + map.get("nameEQ") + "' and type='" + map.get("typeEQ") +"'");
	    PageBean bean = new MsSqlPageBean();
	    this.rs = bean.listData(sql.toString(), null).getCachedRowSet();
	    this.result = getRows();
	    if ((this.result != null) && (this.result.size() > 0)) {
	      return "true";
	    }
	    return "false";
	  }
	public String update(){
		
		return  getReturnString(super.update());
			
	}
	
	public String delete(){
		
		return super.getReturnString(super.delete());

	}
}
