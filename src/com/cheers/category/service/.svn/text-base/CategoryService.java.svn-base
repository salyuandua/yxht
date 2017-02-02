package com.cheers.category.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.DataFormat;

public class CategoryService extends Dao {
	

	/**
	 * 
	 * @param data
	 * @return
	 * @author apple
	 */
	public String add() {
		Map map = new HashMap();
		map.put("table", this.data.get("table"));
		map.put("nameEQ", this.data.get("name"));
		map.put("categoryTypeEQ", this.data.get("categoryType"));
		//map.put("id", this.data.get("parentId"));
		if ("true".equals(super.checkDuplicate(map))) {
			return super.getReturnString("重复的名字！");
		} else {
			List<DynaBean> list = super.search("select * from " + this.data.get("table") + " where categoryType='"+this.data.get("categoryType")+"' and  id=" + this.data.get("parentId"));// 获取父节点信息
			String vorder = "";
			if (list.size() > 0) {
				vorder = DataFormat.objectCheckNull(list.get(0).get("vorder"));
			}
			// 查询子节点，按vorder倒序排列，获得最大值
			String sql = "select * FROM " + this.data.get("table") + " org  where  categoryType='"+this.data.get("categoryType")+"' and org.vorder like '" + vorder + "__' ";
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
			this.data.put("vorder", Long.parseLong(vorder) + 1);
			this.data.put("isleaf", "1");
			return super.getReturnString(super.add());
		}
	}
	
	/**
	 * add zuZhiJiGou one
	 * @param data
	 * @return
	 * @author apple
	 */
	public String addZu() {
		Map map = new HashMap();
		map.put("table", this.data.get("table"));
		map.put("nameEQ", this.data.get("name"));
		map.put("categoryTypeEQ", this.data.get("categoryType"));
//		map.put("noteType",this.data.get("notetype"));
//		map.put("personN",this.data.get("person"));
		
		//map.put("id", this.data.get("parentId"));
		if ("true".equals(super.checkDuplicate(map))) {
			return super.getReturnString("重复的名字！");
		} else {
			List<DynaBean> list = super.search("select * from " + this.data.get("table") + " where categoryType='"+this.data.get("categoryType")+"' and  id=" + this.data.get("parentId"));// 获取父节点信息
			String vorder = "";
			if (list.size() > 0) {
				vorder = DataFormat.objectCheckNull(list.get(0).get("vorder"));
			}
			// 查询子节点，按vorder倒序排列，获得最大值
			String sql = "select * FROM " + this.data.get("table") + " org  where  categoryType='"+this.data.get("categoryType")+"' and org.vorder like '" + vorder + "__' ";
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
			this.data.put("vorder", Long.parseLong(vorder) + 1);
			this.data.put("isleaf", "1");
			return super.getReturnString(super.add());
		}
	}
	
	/**
	 * 修改 人员分组 方法
	 * @param data
	 * @return
	 * @author apple
	 */
	public String update() {
		Map map = new HashMap();
		map.put("table", this.data.get("table"));
		map.put("nameEQ", this.data.get("name"));
		map.put("categoryTypeEQ", this.data.get("categoryType"));

		if ("true".equals(super.checkDuplicate(map))) {
			if (result != null & result.size() > 1) {
				return super.getReturnString("重复的名字！");
			} else {
				if (1 == super.update(" update " + this.data.get("table") + " set name= '" + this.data.get("name") + "' where id=" + this.data.get("id") + ""))
					return super.getReturnString("true");
				else {
					return super.getReturnString("false");
				}
			}
		} else {
			if (1 == super.update(" update " + this.data.get("table") + " set name= '" + this.data.get("name") + "' where id=" + this.data.get("id") + ""))
				return super.getReturnString("true");
			else {
				return super.getReturnString("false");
			}
		}

	}

	/**
	 * 修改 组织机构 方法
	 * @param data
	 * @return
	 * @author apple
	 */
	public String updateZuZhi() {
		Map map = new HashMap();
		map.put("table", this.data.get("table"));
		map.put("nameEQ", this.data.get("name"));
		map.put("categoryTypeEQ", this.data.get("categoryType"));

		if ("true".equals(super.checkDuplicate(map))) {
			if (result != null & result.size() > 1) {
				return super.getReturnString("重复的名字！");
			} else {
				if (1 == super.update(" update " + this.data.get("table") + " set name= '" + this.data.get("name") + "',notetype = '"+ this.data.get("notetype") + "',person = '" + this.data.get("person") + "' where id=" + this.data.get("id") + ""))
					return super.getReturnString("true");
				else {
					return super.getReturnString("false");
				}
			}
		} else {
			if (1 == super.update(" update " + this.data.get("table") + " set name= '" + this.data.get("name") + "',notetype = '"+ this.data.get("notetype") + "',person = '" + this.data.get("person") + "' where id=" + this.data.get("id") + ""))
				return super.getReturnString("true");
			else {
				return super.getReturnString("false");
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
		//查询要删除节点信息
		map.put("table", this.data.get("table"));
		map.put("idEQ", this.data.get("id"));
		map.put("categoryType", this.data.get("categoryType"));
		List<DynaBean> list = super.search(super.searchSQL(map));
		int parentId = (Integer) list.get(0).get("parentid");
		map.put("parentId", parentId );
		map.remove("idEQ");
		list = super.search("select * from SYSTEM_TREE where parentId="+this.data.get("id"));
		if(list!=null&&list.size()>0){
			String str = "";
			//按不同情况检查是否可以删除
			if("productType".equals(map.get("categoryType"))){
				//查看当前节点是否有人
				str = "此类型仍有产品类型，不可删除！";
			}else{
				str = "参数错误";
			}
			return super.getReturnString(str);	
		}else{
			super.delete();
		}
		//删除后查询父节点是否有子节点
		list = super.search("select * from SYSTEM_TREE where parentId="+parentId);
		if (list!=null&&list.size() > 0) {

		} else {
			//修改叶子状态
			super.update("update " + this.data.get("table") + " set isleaf='1' where id=" + map.get("parentId"));
		}
		return super.getReturnString("true");
		}catch (Exception e) {
			return super.getReturnString("false");
		}
	
	}
	/**
	 * 
	 * @param data
	 * @return
	 * @author apple
	 */
	public String deleteCategory() {
		try{
		Map map = new HashMap();
		//查询要删除节点信息
		map.put("table", this.data.get("table"));
		map.put("idEQ", this.data.get("id"));
		map.put("categoryType", this.data.get("categoryType"));
		
		List<DynaBean> list = super.search(super.searchSQL(map));
		String parentId = (String) list.get(0).get("parentid");
		map.put("parentId", parentId );
		map.remove("idEQ");

		list = super.search("select * from SysCategory where parentId="+this.data.get("id"));
		if(list!=null&&list.size()>0){
			String str = "";
			//按不同情况检查是否可以删除
			if("productType".equals(map.get("categoryType"))){
				//查看当前节点是否有人
				str = "此类型仍有下级产品类型，不可删除！";
			}else if("productLeibie".equals(map.get("categoryType"))){
				//查看当前节点是否有人
				str = "此分类仍有下级产品分类，不可删除！";
			}else if("custType".equals(map.get("categoryType"))){
				//查看客户类型是否有子客户类型
				str = "此类型仍有下级客户类型，不可删除！";
			}else if("custLevel".equals(map.get("categoryType"))){
				//查看客户级别是否有下级
				str = "此级别仍有下级客户，不可删除！";
			}else if("newsType".equals(map.get("categoryType"))){
				//查看新闻类别是否有下级
				str = "此新闻类别仍有下级类别，不可删除！";
			}else if("feeType".equals(map.get("categoryType"))){
				//查看费用类别是否有下级
				str = "此费用类别仍有下级类别，不可删除！";
			}else if("feeSubject".equals(map.get("categoryType"))){
				//查看费用科目是否有下级
				str = "此费用科目仍有下级科目，不可删除！";
			}else{
				str = "参数错误";
			}
			return super.getReturnString(str);	
		}else{
			super.delete();
		}
		//删除后查询父节点是否有子节点
		list = super.search(super.searchSQL(map));
		System.out.println(list.size());
		if (list!=null&&list.size() > 0) {
			
		} else {
			//修改叶子状态
			super.update("update " + this.data.get("table") + " set isleaf='1' where id=" + map.get("parentId"));
		}
		return super.getReturnString("true");
		}catch (Exception e) {
			return super.getReturnString("false");
		}
	
	}
	public String getTreeRen()
	  {
	    this.result = new ArrayList();
	    String parentId = DataFormat.objectCheckNull(this.map.get("parentId"));
	    String sql = "";

	    if (DataFormat.checkNull(parentId)) {
	      sql = "select top 1 id,name,isleaf FROM " + this.map.get("table") + " c where 1=1";
	      if (!DataFormat.booleanCheckNull(this.map.get("categoryType")))
	        sql = sql + " and categoryType='" + this.map.get("categoryType") + "'";
	      sql = sql + "  order by vorder";
	    } else {
	      sql = "select id,name,isleaf FROM " + this.map.get("table") + " c  where 1=1 ";
	      if (!DataFormat.booleanCheckNull(this.map.get("categoryType")))
	        sql = sql + " and categoryType='" + this.map.get("categoryType") + "'";
	      sql = sql + " and c.vorder like (select vorder from " + this.map.get("table") + " o where id=" + parentId + 
	        ")+'__'   order by vorder";
	    }
	    System.out.println(sql);
	    DataBase db = new DataBase();
	    ResultSet rs = null;
	    try
	    {
	      db.connectDb();
	      rs = db.queryAll(sql);
	      if (rs != null)
	      {
	        while (rs.next()) {
	          Map node = new HashMap();
	          node.put("id", DataFormat.stringCheckNullNoTrim(rs.getString(1), ""));
	          node.put("text", DataFormat.stringCheckNullNoTrim(rs.getString(2),""));
	          String isLeaf = DataFormat.stringCheckNullNoTrim(rs.getString(3), "");
	          node.put("isLeaf", isLeaf);
	          if (isLeaf.equals("1")) {
	            node.put("state", "open");
	          } else if (isLeaf.equals("0")) {
	            node.put("state", "closed");
	            
	            node.put("children", getChildRen(DataFormat.stringCheckNullNoTrim(rs.getString(1), "")));
	          }
	          this.result.add(node);
	        }
	      }
	      if (this.result.size() < 1) {
	        Map parentNode = new HashMap();
	        parentNode.put("table", this.map.get("table"));
	        parentNode.put("id", parentId);
	        parentNode.put("isleaf", "1");
	        update((String)updateSql(parentNode).get(0));
	      }
	      String str1 = getReturn();
	      return str1;
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      return "";
	    } finally {
	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try
	      {
	        db.close();
	      } catch (DbException de) {
	        de.printStackTrace();
	      }
	    }
	    //throw localObject;
	  }
	 /**查询系统树的 人员分组 children 节点
	  * @author sunjianfeng
	  * 2013-11-04 16:54:11
	  * @return
	  */
	public List getChildRen(String id)
	  {
	    List list = new ArrayList();
	    String sql = "select id,name,isleaf FROM " + this.map.get("table") + " c where 1=1 ";

	    if (!DataFormat.booleanCheckNull(this.map.get("categoryType")))
	      sql = sql + " and categoryType='" + this.map.get("categoryType") + "'";
	    sql = sql + " and c.vorder like (select vorder from " + this.map.get("table") + " o where id=" + id + ")+'__' order by vorder";
	    DataBase db = new DataBase();
	    ResultSet rs = null;
	    System.out.println(sql);
	    try {
	      db.connectDb();
	      rs = db.queryAll(sql);
	      if (rs != null)
	      {
	        while (rs.next()) {
	          Map node = new HashMap();
	          node.put("id", DataFormat.stringCheckNullNoTrim(rs.getString(1), ""));
	          node.put("text", DataFormat.stringCheckNullNoTrim(rs.getString(2),""));
	          String isLeaf = DataFormat.stringCheckNullNoTrim(rs.getString(3), "");
	          node.put("isLeaf", isLeaf);
	          if (isLeaf.equals("1")) {
	            node.put("state", "open");
	          } else if (isLeaf.equals("0")) {
	            node.put("state", "closed");
	            node.put("children", getChildRen(DataFormat.stringCheckNullNoTrim(rs.getString(1))));
	          }
	          list.add(node);
	        }
	      }
	      if (list.size() < 1) {
	        Map map = new HashMap();
	        map.put("table", this.map.get("table"));
	        map.put("id", id);
	        map.put("isLeaf", "1");
	        update((String)updateSql(map).get(0));
	      }
	    } catch (DbException de) {
	      de.printStackTrace();

	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try
	      {
	        db.close();
	      } catch (DbException de1) {
	        de1.printStackTrace();
	      }
	    }
	    catch (SQLException e)
	    {
	      e.printStackTrace();

	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try
	      {
	        db.close();
	      } catch (DbException de) {
	        de.printStackTrace();
	      }
	    }
	    finally
	    {
	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try
	      {
	        db.close();
	      } catch (DbException de) {
	        de.printStackTrace();
	      }
	    }
	    return list;
	  }
	  /*
	  * 
	  * 获得组织机构的树状结构的 getTree方法
	  * 
	  */
	 public String getTreeZu()
	  {
	    this.result = new ArrayList();
	    String parentId = DataFormat.objectCheckNull(this.map.get("parentId"));
	    String sql = "";

	    if (DataFormat.checkNull(parentId)) {
	      sql = "select top 1 id,name,isleaf,notetype,person FROM " + this.map.get("table") + " c where 1=1";
	      if (!DataFormat.booleanCheckNull(this.map.get("categoryType")))
	        sql = sql + " and categoryType='" + this.map.get("categoryType") + "'";
	      sql = sql + "  order by vorder";
	    } else {
	      sql = "select id,name,isleaf,notetype,person FROM " + this.map.get("table") + " c  where 1=1 ";
	      if (!DataFormat.booleanCheckNull(this.map.get("categoryType")))
	        sql = sql + " and categoryType='" + this.map.get("categoryType") + "'";
	      sql = sql + " and c.vorder like (select vorder from " + this.map.get("table") + " o where id=" + parentId + 
	        ")+'__'   order by vorder";
	    }
	    System.out.println(sql);
	    DataBase db = new DataBase();
	    ResultSet rs = null;
	    try
	    {
	      db.connectDb();
	      rs = db.queryAll(sql);
	      if (rs != null)
	      {
	        while (rs.next()) {
	          Map node = new HashMap();
	          node.put("id", DataFormat.stringCheckNullNoTrim(rs.getString(1), ""));
	          node.put("text", DataFormat.stringCheckNullNoTrim(rs.getString(2)) + DataFormat.stringCheckNullNoTrim(rs.getString(4)) + DataFormat.stringCheckNullNoTrim(rs.getString(5)));
	          String isLeaf = DataFormat.stringCheckNullNoTrim(rs.getString(3), "");
	          node.put("isLeaf", isLeaf);
	          if (isLeaf.equals("1")) {
	            node.put("state", "open");
	          } else if (isLeaf.equals("0")) {
	            node.put("state", "closed");
	            node.put("children", getChildZu(DataFormat.stringCheckNullNoTrim(rs.getString(1), "")));
	          }
	          this.result.add(node);
	        }
	      }
	      if (this.result.size() < 1) {
	        Map parentNode = new HashMap();
	        parentNode.put("table", this.map.get("table"));
	        parentNode.put("id", parentId);
	        parentNode.put("isleaf", "1");
	        update((String)updateSql(parentNode).get(0));
	      }
	      String str1 = getReturn();
	      return str1;
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      return "";
	    } finally {
	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try
	      {
	        db.close();
	      } catch (DbException de) {
	        de.printStackTrace();
	      }
	    }
	    //throw localObject;
	  }
	 /**
		 * getChild 重写获得children子节点方法 
		 * 用于获得组织机构的 特殊结构
		 * @param data
		 * @return
		 * @author apple
		 */
	 public List getChildZu(String id)
	  {
	    List list = new ArrayList();
	    String sql = "select c.id,c.name,c.isleaf,c.notetype,ac.name as fzname FROM " + this.map.get("table") + " c  left join sysACCOUNT ac on ac.id =c.person where 1=1 ";

	    if (!DataFormat.booleanCheckNull(this.map.get("categoryType")))
	      sql = sql + " and categoryType='" + this.map.get("categoryType") + "'";
	    sql = sql + " and c.vorder like (select vorder from " + this.map.get("table") + " o where o.id=" + id + ")+'__' order by vorder";
	    DataBase db = new DataBase();
	    ResultSet rs = null;
	    System.out.println("***************"+sql);
	    try {
	      db.connectDb();
	      rs = db.queryAll(sql);
	      if (rs != null)
	      {
	        while (rs.next()) {
	          Map node = new HashMap();
	          node.put("id", DataFormat.stringCheckNullNoTrim(rs.getString(1), ""));
	          
//	          node.put("name",DataFormat.stringCheckNullNoTrim(rs.getString(2), ""));//
//	          node.put("notetype",DataFormat.stringCheckNullNoTrim(rs.getString(4), ""));//
//	          node.put("person",DataFormat.stringCheckNullNoTrim(rs.getString(5), ""));
	          
	          //String notetype = Integer.parseInt(DataFormat.stringCheckNullNoTrim(rs.getString(4))) == 1 ? "大区" : "办事处" ;
	          String notetype="";
	          if(DataFormat.stringCheckNullNoTrim(rs.getString(4))==""||DataFormat.stringCheckNullNoTrim(rs.getString(4))==null){
	        	  notetype = "";
	          }else{
	        	  //notetype = DataFormat.stringCheckNullNoTrim(rs.getString(4)).equals("1") ? "大区" : "办事处" ;
	        	  if(DataFormat.stringCheckNullNoTrim(rs.getString(4)).equals("1")){
	        		  notetype = "区域";
	        	  }else if(DataFormat.stringCheckNullNoTrim(rs.getString(4)).equals("2")){
	        		  notetype = "办事处";
	        	  }else if(DataFormat.stringCheckNullNoTrim(rs.getString(4)).equals("4")){
	        		  notetype = "大区";
	        	  }else if(DataFormat.stringCheckNullNoTrim(rs.getString(4)).equals("3")){
	        		  notetype = "部门";
	        	  }
	          }
	          
	          node.put("text", DataFormat.stringCheckNullNoTrim(rs.getString(2)) +"-【类型：["+ notetype +"]，负责人：["+ DataFormat.stringCheckNullNoTrim(rs.getString(5))+"]】");
	      
	          String isLeaf = DataFormat.stringCheckNullNoTrim(rs.getString(3), "");
	          node.put("isLeaf", isLeaf);
	          if (isLeaf.equals("1")) {
	            node.put("state", "open");
	          } else if (isLeaf.equals("0")) {
	            node.put("state", "closed");
	            node.put("children", getChildZu(DataFormat.stringCheckNullNoTrim(rs.getString(1))));
	          }
	          list.add(node);
	        }
	      }
	      if (list.size() < 1) {
	        Map map = new HashMap();
	        map.put("table", this.map.get("table"));
	        map.put("id", id);
	        map.put("isLeaf", "1");
	        update((String)updateSql(map).get(0));
	      }
	    } catch (DbException de) {
	      de.printStackTrace();

	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try
	      {
	        db.close();
	      } catch (DbException de1) {
	        de1.printStackTrace();
	      }
	    }
	    catch (SQLException e)
	    {
	      e.printStackTrace();

	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try
	      {
	        db.close();
	      } catch (DbException de) {
	        de.printStackTrace();
	      }
	    }
	    finally
	    {
	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try
	      {
	        db.close();
	      } catch (DbException de) {
	        de.printStackTrace();
	      }
	    }
	    return list;
	  }
	 
	 //组织结构 根据id获得  原名称 节点类型 负责人
	 public String getZuZhiDe(){
		 	String id = request.getParameter("id");
		 	//String key=request.getParameter("key");
		 	//String value=request.getParameter("key");
//		 	Map map = new HashMap();
//			map.put("id", this.data.get("id"));
//			
		 	String sql="select name,notetype,person  from system_tree where 1=1 and id='"+ id +"'";
		 	System.out.println("----------------"+sql);
		 	excuteSQLPageQuery(sql);
		 	result = getRows();
		 	return getReturn();
	 }
	//获取基础数据的下拉列表
		 public String getCombobox(){
		 	//String table=request.getParameter("table");
		 	//String key=request.getParameter("key");
		 	//String value=request.getParameter("key");
		 	String sql="select top 100 percent * from "+table+" where 1=1";
		 	if(key!=null && !key.equals(""))sql+=" and "+key+"= '"+value+"'	";
		 	System.out.println(sql+"========>");
		 	if (!DataFormat.booleanCheckNull(sort))
		 		sql += " order by " + sort + " " + DataFormat.objectCheckNull(order);
		 	else
		 		sql += " order by id";
		 	excuteSQLPageQuery(sql);
		 	result = getRows();
		 	return getReturn();
		 }
		 //校验是否存在对应负责人
		 public String check(){
			Map m= new HashMap();
			m.put("table", this.data.get("table"));
			m.put("idEQ", this.data.get("idEQ"));
			return getReturnString(super.checkDuplicate(m));
			
		}
		 
			/**
			 * 获取部门下拉列表
			 * 王欣  2014年1月8日14:31:45
			 * @return
			 */
			public String searchBMType(){
				String q=this.QUESTION;
				StringBuffer sql = new StringBuffer("select top 100 percent id value ,name text,isleaf FROM system_tree c where  categoryType=1 \n" +
						" and parentid!=0   and notetype=3 ");
				if(q!=null&&!"".equals(q)){
					 sql.append(" and  c.name like '%"+q+"%' ");
				 }
				sql.append(" order by vorder");
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
			}

			/**
			 * 获取区域下拉列表
			 * 王欣  2014年1月8日14:31:45
			 * @return
			 */
			public String searchQYType(){
				String q=this.QUESTION;
				StringBuffer sql = new StringBuffer("select top 100 percent id value ,name text,isleaf FROM system_tree c where  categoryType=1 \n" +
						" and parentid!=0   and notetype=1 or notetype=4 ");
				if(q!=null&&!"".equals(q)){
					 sql.append(" and  c.name like '%"+q+"%' ");
				 }
				sql.append(" order by vorder");
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
			}
			
			/**
			 * 仅 获取 大区 的 下拉列表
			 *  jianfeng 2014年1月8日14:31:45
			 * @return
			 */
			public String searchdaqv(){
				String q=this.QUESTION;
				StringBuffer sql = new StringBuffer("select top 100 percent id value ,name text,isleaf FROM system_tree c where  categoryType=1 \n" +
						" and parentid!=0   and  notetype=4 ");
				if(q!=null&&!"".equals(q)){
					 sql.append(" and  c.name like '%"+q+"%' ");
				 }
				sql.append(" order by vorder");
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
			}
			
			/**
			 * 仅 获取 区域 的 下拉列表
			 *  jianfeng 2014年1月8日14:31:45
			 * @return
			 */
			public String searchqvyu(){
				String q=this.QUESTION;
				StringBuffer sql = new StringBuffer("select top 100 percent id value ,name text,isleaf FROM system_tree c where  categoryType=1 \n" +
						" and parentid!=0   and  notetype=1 ");
				if(q!=null&&!"".equals(q)){
					 sql.append(" and  c.name like '%"+q+"%' ");
				}
				
				sql.append(" order by vorder");
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
			}
			
			
			/**
			 * 获取大区下拉列表
			 * 2014-5-16 08:37:06
			 * @return
			 */
			public String searchDQType(){
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
			
}
