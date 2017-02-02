package com.cheers.system.org.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.dao.Dao;
import com.cheers.util.DataFormat;
import com.fasterxml.jackson.core.JsonProcessingException;

public class OrgService extends Dao {

	/**
	 * 
	 * @param data
	 * @return
	 * @author apple
	 */
	public String add() {
		Map map = new HashMap();
		map.put("table", this.data.get("table"));
		map.put("name", this.data.get("name"));
		if ("true".equals(super.checkDuplicate(map))) {
			return "重复的名字！";
		} else {

			List<DynaBean> list = super.search("select * from " + this.data.get("table") + " where id=" + this.data.get("parentId"));// 获取父节点信息
			String vorder = "";
			if (list.size() > 0) {
				vorder = DataFormat.objectCheckNull(list.get(0).get("vorder"));
			}
			// 查询子节点，按vorder倒序排列，获得最大值
			String sql = "select * FROM " + this.data.get("table") + " org  where org.vorder like '" + vorder + "__' ";
			sql += " order by vorder desc";
			List<DynaBean> orgList = super.search(sql);
			if (orgList.size() > 0)
				vorder = (String) orgList.get(0).get("vorder");
			else {
				vorder = vorder + "00";
			}
			// 父节点若原来是叶子则变节点
			super.update("update " + this.data.get("table") + " set isleaf='0' where id=" + this.data.get("parentId"));
			// 设置添加的叶子的vorder
			this.data.put("vorder", Integer.parseInt(vorder) + 1);
			this.data.put("isleaf", "1");
			return super.add();
		}
	}

	/**
	 * 
	 * @param data
	 * @return
	 * @author apple
	 */
	public String update() {
		Map map = new HashMap();
		map.put("table", this.data.get("table"));
		map.put("name", map.get("name"));
		if ("true".equals(super.checkDuplicate(map))) {
			if (result != null & result.size() > 1) {
				return "重复的名字！";
			} else {
				if (1 == super.update(" update " + this.data.get("table") + " set name= '" + this.data.get("name") + "' where id=" + this.data.get("id") + ""))
					return "true";
				else {
					return "false";
				}
			}
		} else {
			if (1 == super.update(" update " + this.data.get("table") + " set name= '" + this.data.get("name") + "' where id=" + this.data.get("id") + ""))
				return "true";
			else {
				return "false";
			}
		}

	}

	/**
	 * 
	 * @param data
	 * @return
	 * @author apple
	 */
	public String delete() {
		try{
		Map map = new HashMap();
		map.put("table", this.data.get("table"));
		map.put("id", this.data.get("id"));
		//查询要删除节点信息
		List<DynaBean> list = super.search(super.searchSQL(map));
		String parentId = (String) list.get(0).get("parentid");
		map.put("parentId", parentId);
		map.remove("id");
		//查看当前节点是否有人
		list = super.search("select * from account_struct where structid="+this.data.get("id"));
		if(list!=null&&list.size()>0){
			return "此部门仍有人员，不可删除！";
		}else{
			super.delete();
		}
		
		//删除后查询父节点是否有子节点
		list = super.search(super.searchSQL(map));
		if (list!=null&&list.size() > 0) {

		} else {
			//修改叶子状态
			super.update("update " + this.data.get("table") + " set isleaf='1' where id=" + this.data.get("parentId"));
		}
		return "true";
		}catch (Exception e) {
			return "false";
		}
	
	}

}
