package com.cheers.client.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.BaseDao;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientService extends Dao {
	//客户信息的添加
	public String add(){
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		m.put("codesEQ", this.data.get("codes"));//客户编码不能重复
		/*if("true".equals(checkDuplicate(m))){
			return getReturnString("sorry,已有重复客户编码");
		}else {*/
			this.session = request.getSession();
			DateFormat df = new DateFormat();//设置日期格式
			String now=df.getNowTime();
			List<Object> proList = (List<Object>) data.get("productList");//获取到联系人对象集合
			List<Object> newList = new ArrayList<Object>();//创建一个新的集合存放新的联系人数据
			//循环联系人集合，将创建时间创建人，最后一次修改时间，最后一次修改人，存放到集合中
			for (int i = 0; i <proList.size(); i++) {
				//集合中存放的为LinkedHashMap对象，每次循环取出一个
				LinkedHashMap<String, String> maps = (LinkedHashMap<String, String>) proList.get(i);
//				maps.remove("id");
				maps.put("createTime", now);
				maps.put("modifyTime", now);
				maps.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
				maps.put("modifierId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
				newList.add(maps);//将新的map数据放入新的集合中
			}
			//根据key删除之前的list集合
			data.remove("productList");
			//放入新的list集合
			data.put("productList", newList);
			this.data.put("createTime", now);
			this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
			this.data.put("modifyTime", now);//
			this.data.put("modifierId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
			
			this.data.put("locationtime", now);
			this.data.put("locationmanid", ((AccountBean)this.session.getAttribute("accountBean")).getId());
			
			String retStr = super.add();
			//**************添加客户业务员信息表数据*******
			DataBase db=new DataBase();
			if(this.data.containsKey("accountId")){
				try{
					Map sqlMaps=new HashMap();
					sqlMaps.put("createTime", now);
					//创建一个存放sql语句的集合
					List<String> upsql=new ArrayList<String>();
					int is=getMaxId((String)this.data.get("table"), sqlMaps);
					String sql = "insert into Customer_account " +
							"(clientid,accountid,creatorid,createtime,modifierid,modifytime,enable,startdate) " +
							"values("+is+","+ this.data.get("accountId")+","+((AccountBean)this.session.getAttribute("accountBean")).getId()+",'"+now+"',"+((AccountBean)this.session.getAttribute("accountBean")).getId()+",'"+now+"',1,'"+now+"')";
					upsql.add(sql);
					db.updateBatch(upsql.toArray(new String[upsql.size()]));
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try {
						db.close();
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}
			//***********************************************************
			if(retStr.equals("true")){
				Map sqlMap=new HashMap();
				sqlMap.put("createTime", now);
				int i=getMaxId((String)this.data.get("table"), sqlMap);
				return super.getReturnString(i+"");
			}else return super.getReturnString(retStr);
		//}
	}
	//客户修改
	public String update(){
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		StringBuffer sql2 = new StringBuffer();
		sql2.append("select top 100 percent * ");
		sql2.substring(0, sql2.length() - 1);
		sql2.append(" from ");
		sql2.append(DataFormat.objectCheckNull(this.data.get("table")) + DataFormat.objectCheckNull(map.get("view")));
		sql2.append(" where 1=1 and id<>"+this.data.get("id"));
		// 遍历参数
		sql2.append(getSearchStr(m));
		PageBean bean = new MsSqlPageBean();
		rs = bean.listData(sql2.toString(), null).getCachedRowSet();
		result = getRows();
				this.session = request.getSession();
				DateFormat df = new DateFormat();//设置日期格式
				String now=df.getNowTime();
				String accountid=((AccountBean)this.session.getAttribute("accountBean")).getId();
				//处理客户信息中一对多的更新
				List<String> upsqls =  new ArrayList<String>();
				//删除所有去除项
				this.map.put("table", "CUSTOMER_PRODUCT");
				upsqls = deleteSql(this.map);
			    if(this.data.containsKey("productList")){
				    List<Map> list = (List)this.data.get("productList");
				    for (Map map : list) {
				    	if(map.get("id")!=""){
				    		map.put("modifyTime", now);
				    		map.put("modifierId",accountid);
				    		upsqls.addAll(super.updateSql(map));
				    	}else{
				    	//添加新增选项
				    		map.put("customerId", (Integer)this.data.get("id"));
				    		map.put("createTime", now);
				    		map.put("creatorId", accountid);
				    		upsqls.addAll(addSql(map));
				    	}
				    }
				    data.remove("productList");//去除子表信息
			    }
				this.data.put("modifyTime", now);//
				this.data.put("modifierId", accountid);
				//*********************修改更新客户业务员表*****************************
				int id =  (Integer) (this.data.containsKey("id")?this.data.get("id"):"");
				String sqlkh = "select * from customer t where t.id='"+id+"'";
				DataBase dbs=new DataBase();
				try {
					//若已存在业务员信息，则删除
					dbs.connectDb();
					ResultSet rs = dbs.queryAll(sqlkh.toString());
					String ywyid ="";
					while((rs!=null)&&(rs.next())){
						String sqldel = "delete  from  Customer_account  where clientId='"+id+"' and accountid='"+rs.getString("accountid")+"'";
						upsqls.add(sqldel);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return getReturnString("flase");
				}finally{
					try {
						dbs.close();
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				
				String sql = "insert into Customer_account " +
						"(clientid,accountid,creatorid,createtime,modifierid,modifytime,enable,startdate) " +
						"values("+id+","+ this.data.get("accountid")+","+accountid+",'"+now+"',"+accountid+",'"+now+"',1,'"+now+"')";
				upsqls.add(sql);
			    String flag =super.update();//更新主表信息
			    if (flag.equals("true")){
					DataBase db=new DataBase();
				    try {
						db.updateBatch(upsqls.toArray(new String[upsqls.size()]));
					} catch (DbException e) {
						e.printStackTrace();
						return getReturnString("flase");
					}finally{
						try {
							db.close();
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
			    }
				return getReturnString(flag);
			
	}
	//合作客户的修改-----只允许管理员角色修改
	public String updateWorkCustomer(){
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String selfroleid = accountBean.getRoleid();//当前登录人角色
		System.out.println("selfroleid====================="+selfroleid);
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		m.put("codesEQ", this.data.get("codes"));
		StringBuffer sql2 = new StringBuffer();
		sql2.append("select top 100 percent * ");
		sql2.substring(0, sql2.length() - 1);
		sql2.append(" from ");
		sql2.append(DataFormat.objectCheckNull(this.data.get("table")) + DataFormat.objectCheckNull(map.get("view")));
		sql2.append(" where 1=1 and id<>"+this.data.get("id"));
		// 遍历参数
		sql2.append(getSearchStr(m));
		PageBean bean = new MsSqlPageBean();
		rs = bean.listData(sql2.toString(), null).getCachedRowSet();
		result = getRows();
		if(!(selfroleid.equals("3"))){
			return getReturnString("sorry,只有管理员才可以修改!");
		}else if (result != null && result.size()>0){
			return getReturnString("sorry,已有重复客户编码!");
		}else {
				this.session = request.getSession();
				DateFormat df = new DateFormat();//设置日期格式
				String now=df.getNowTime();
				String accountid=((AccountBean)this.session.getAttribute("accountBean")).getId();
				//处理客户信息中一对多的更新
				List<String> upsqls =  new ArrayList<String>();
				//删除所有去除项
				this.map.put("table", "CUSTOMER_PRINCIPAL");
//				upsqls = deleteSql(this.map);
			    if(this.data.containsKey("productList")){
				    List<Map> list = (List)this.data.get("productList");
				    for (Map map : list) {
				    	if(map.get("id")!=""){
				    		map.put("modifyTime", now);
				    		map.put("modifierId",accountid);
				    		upsqls.addAll(super.updateSql(map));
				    	}else{
				    	//添加新增选项
				    		map.put("clientId", (Integer)this.data.get("id"));
				    		map.put("createTime", now);
				    		map.put("creatorId", accountid);
				    		upsqls.addAll(addSql(map));
				    	}
				    }
				    data.remove("productList");//去除子表信息
			    }
				this.data.put("modifyTime", now);//
				this.data.put("modifierId", accountid);
				//*********************修改更新客户业务员表*****************************
				int id =  (Integer) (this.data.containsKey("id")?this.data.get("id"):"");
				String sqlkh = "select * from customer t where t.id='"+id+"'";
				DataBase dbs=new DataBase();
				try {
					//若已存在业务员信息，则删除
					dbs.connectDb();
					ResultSet rs = dbs.queryAll(sqlkh.toString());
					String ywyid ="";
					while((rs!=null)&&(rs.next())){
						String sqldel = "delete  from  Customer_account  where clientId='"+id+"' and accountid='"+rs.getString("accountid")+"'";
						upsqls.add(sqldel);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return getReturnString("flase");
				}finally{
					try {
						dbs.close();
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				
				String sql = "insert into Customer_account " +
						"(clientid,accountid,creatorid,createtime,modifierid,modifytime,enable,startdate) " +
						"values("+id+","+ this.data.get("accountid")+","+accountid+",'"+now+"',"+accountid+",'"+now+"',1,'"+now+"')";
				upsqls.add(sql);
			    String flag =super.update();//更新主表信息
			    if (flag.equals("true")){
					DataBase db=new DataBase();
				    try {
						db.updateBatch(upsqls.toArray(new String[upsqls.size()]));
					} catch (DbException e) {
						e.printStackTrace();
						return getReturnString("flase");
					}finally{
						try {
							db.close();
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
			    }
				return getReturnString(flag);
			}
			
	}
	
	public String delete(){
		DataBase dbs=new DataBase();
		List<Object>  listob = (List<Object>) this.data.get("id");
		System.out.println();
		
		String str = "";
		for(int i = 0 ; i <listob.size();i++){
			str += Integer.toString((Integer) listob.get(i))+",";
		}
			String sql = "delete from Customer_Product where customerid in("+str.substring(0, str.length()-1)+")";
			//若已存在业务员信息，则删除
			System.out.println(sql.toString());
			try {
				dbs.connectDb();
				dbs.update(sql.toString());
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return getReturnString(super.delete());
	}

	public String updatefieldByIds(){
		
		return getReturnString(super.updatefieldByIds());
	}
	public String getBeans(){
		 return  super.getBeans();
	 }
	//得到权限下的客户
	 public String getBeansHKQX(){
//			String q=request.getParameter("q");
		 String q = this.QUESTION;
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//自己可以看到的客户信息
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			String sql ="select distinct top 100 percent c.id value,c.name text,c.levelid from customer c \n" +
					"where 1=1 and c.enable=1 ";
			//if(q!=null&&!"".equals(q)){
				 sql+=" and  c.name like '%"+q+"%' ";
			//}
				 
			sql+=" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"c.accountid")+" or c.accountId="+selfId+")";
			 if (!DataFormat.booleanCheckNull(sort))
					sql+= " order by " + sort + " " + DataFormat.objectCheckNull(order);
				else
					sql += " order by c.id";
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
		}
	
	 public String getBeansAllQXNew(){ 
		 	String q = this.QUESTION;
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//自己可以看到的客户信息
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuilder sql = new StringBuilder();
			StringBuilder sql2 = new StringBuilder();
			
			sql2.append(" select distinct top 100 percent t.id value,t.name text,t.levelid from customer t ");
			
//			String selectSQL = "SELECT tree.name ,tree.vorder FROM sysACCOUNT_POST sap ,SYSTEM_TREE tree WHERE sap.orgid = tree.id AND sap.accountid = "+selfId+" ";
//			DataBase db = new DataBase();
			try {
//				ResultSet rs = db.queryAll(selectSQL);
//				while(rs.next()){
//					StringBuilder sql1 = new StringBuilder();
//					sql1.append(" SELECT c.id value,c.name text,c.levelid FROM Customer c,Customer_account ca  ");
//					sql1.append(" WHERE c.id = ca.clientid AND ca.accountid IN ");
//					sql1.append(" (SELECT sap.accountid FROM sysACCOUNT_POST sap WHERE sap.orgid IN ( ");
//					sql1.append(" SELECT tree.id FROM SYSTEM_TREE tree WHERE tree.vorder LIKE '"+rs.getString("vorder")+"%' ");
//					sql1.append(" )) AND c.enable=1 ");
//					sql1.append(" UNION ALL");
//					
//					sql.append(sql1.toString());
//				}
//				
//				String sqlstr = sql.toString();
//				sqlstr = sqlstr.substring(0, sqlstr.length()-10);
//				sql2.append(sqlstr);
				
				
				sql2.append("  where 1=1 and  t.name like '%"+q+"%' ");
				
				sql2.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid")+" or t.accountId="+selfId+") ");
				
				 if (!DataFormat.booleanCheckNull(sort)){
					 sql2.append(" order by " + sort + " " + DataFormat.objectCheckNull(order));
				 }else{
					 sql2.append(" order by t.value");
				 }
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(rs!=null){
						rs.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(db!=null){
						db.close();
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//System.out.println("----------------------------------"+sql2);
			excuteSQLPageQuery(sql2.toString());
			result = getRows();
			return getReturn();
	 }
	 
	 /**
	  * 
	  * 模块：订单管理
	  * 功能：订单选择客户只能选择 “合作客户”  
	  * 作者：jack_sun:1519891098
	  */
	 public String getCustomerForOrder(){
		 	String q = this.QUESTION;
			this.session = request.getSession();
			
			//String customerid = request.getParameter("customerid");
			
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//自己可以看到的客户信息
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuilder sql = new StringBuilder();
			StringBuilder sql2 = new StringBuilder();
			
			sql2.append(" select distinct top 100 percent t.id value,t.name text,t.levelid,t.areaid from customer t ");
			
			try {
				//sql2.append("  where 1=1 and t.customerflid='139' and t.name like '%"+q+"%' or t.id LIKE '%"+customerid+"%' ");
				sql2.append("  where 1=1 and t.customerflid='139' and t.name like '%"+q+"%' ");
				sql2.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid")+" or t.accountId="+selfId+") ");
				 if (!DataFormat.booleanCheckNull(sort)){
					 sql2.append(" order by " + sort + " " + DataFormat.objectCheckNull(order));
				 }else{
					 sql2.append(" order by t.value");
				 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(rs!=null){
						rs.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(db!=null){
						db.close();
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			excuteSQLPageQuery(sql2.toString());
			result = getRows();
			return getReturn();
	 }
	 
	//得到权限下的客户
		 public String getBeansAllKH(){
//				String q=request.getParameter("q");
			 String q = this.QUESTION;
				this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
//				String canAccountIds = accountBean.get_canClientIds();//自己可以看到的客户信息
//				String selfId = accountBean.getId();//当前登录人id
				SqlUtil sqlUtil = new SqlUtil();
				String sql ="select distinct top 100 c.id value,c.name text,c.levelid from customer c \n" +
						"where 1=1 and c.enable=1 ";
				//if(q!=null&&!"".equals(q)){
					 sql+=" and  c.name like '%"+q+"%' ";
				 //}
//				sql+=" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"c.id")+" or c.accountId="+selfId+")";
				 if (!DataFormat.booleanCheckNull(sort))
						sql+= " order by " + sort + " " + DataFormat.objectCheckNull(order);
					else
						sql += " order by c.id";
					excuteSQLPageQuery(sql.toString());
					result = getRows();
					return getReturn();
			}
	 /*
	  * 获得 收货地址
	  * jack_sun 2014年6月27日 13:08:28
	  * */
	 public String getCusAddress(){
         StringBuffer sql =new StringBuffer("select c.address from customer c where 1=1 and c.enable=1 ");
         sql.append(super.getSearchStr(super.map));
             excuteSQLPageQuery(sql.toString());
             result = getRows();
             return getReturn();
     }
	 
	 //获取联系人
	 public String getBeanslxr(){
//			String q=request.getParameter("q");
//		 String q = this.QUESTION;
//			this.session = request.getSession();
//			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
//			String canAccountIds = accountBean.get_canClientIds();//自己可以看到的客户信息
//			String selfId = accountBean.getId();//当前登录人id
//			SqlUtil sqlUtil = new SqlUtil();
			String sql ="select distinct top 100 percent c.id value,c.name text from customer_visit c \n" +
					"where 1=1 and c.enable=1 and c.clientid="+this.data.get("clientidEQ");
//			if(q!=null&&!"".equals(q)){
//				 sql+=" and  c.name like '%"+q+"%' ";
//			 }
//			sql+=" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"c.id")+" or c.accountId="+selfId+")";
//			 if (!DataFormat.booleanCheckNull(sort))
//					sql+= " order by " + sort + " " + DataFormat.objectCheckNull(order);
//				else
					sql += " order by c.id";
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
		}
	 /**
		 * 查看下级和自己的客户资料信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearch(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			String cid = this.request.getParameter("cid");
			SqlUtil sqlUtil = new SqlUtil();
			
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from CustomerView t where 1=1");
			
			if(!"".equals(cid)&&!"null".equals(cid)&&cid!=null){
				sql.append(" and t.id = "+cid);
			}
			
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.creatorid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id desc");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		
		 /**
		 * 查看下级和自己的潜在客户信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchQz(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from CustomerView t where 1=1 and t.customerflid=138 ");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.creatorid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		
		 /**
		 * 查看下级和自己的日常拜访客户资料信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchVisit(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from CustomerView t where 1=1 and t.customerflid=137 ");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.creatorid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		 /**
		 * 查看下级和自己的合作客户资料信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchWork(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from CustomerView t where 1=1 and t.customerflid=139 ");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.creatorid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		//客户主联人生日提醒
		public  String  getBeansCustomerBirth(){
			this.session = request.getSession();
			SqlUtil sqlUtil = new SqlUtil();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String accountid = accountBean.getId();//当前登录人的
			String _AccountIds = ((AccountBean)this.session.getAttribute("accountBean")).get_canAccountIds();//用户权限下的客户
			
			String nameLIKE=(String) super.map.get("nameLIKE");
			String customernameLIKE=(String) super.map.get("customernameLIKE");
			String bumenLIKE=(String) super.map.get("bumenLIKE");
			String accountIdEQ=(String) super.map.get("accountIdEQ");
			String birthGE=(String) super.map.get("birthGE");
			String birthLE=(String) super.map.get("birthLE");
			
			String accountidlist = "0,";//
			String sql ="select top 100 percent t.* from (SELECT \n" +
									"dbo.Customer_Principal.id,\n" +
									"dbo.Customer_Principal.clientId,\n" +
									"dbo.Customer_Principal.name,\n" +
									"dbo.Customer_Principal.phone,\n" +
									"dbo.Customer_Principal.tel,\n" +
									"dbo.Customer_Principal.email,\n" +
									"dbo.Customer_Principal.positionId,\n" +
									"dbo.Customer_Principal.isMain,\n" +
									"dbo.Customer_Principal.enable,\n" +
									"dbo.Customer_Principal.memo,\n" +
									"dbo.Customer_Principal.creatorId,\n" +
									"dbo.Customer_Principal.createTime,\n" +
									"dbo.Customer_Principal.modifierId,\n" +
									"dbo.Customer_Principal.modifyTime,\n" +
									"dbo.Customer_Principal.address,\n" +
									"dbo.Customer_Principal.fgyw,\n" +
									"dbo.Customer_Principal.positionIds,\n" +
									"dbo.Customer.name AS customername,\n" +
									"dbo.Customer.accountId,\n" +
									"dbo.sysACCOUNT.name AS accountname,\n" +
									"dbo.BASE_DATA.name AS positionname,\n" +
									//"dbo.Customer_Principal.birth," +
									"CONVERT (VARCHAR (5),dbo.Customer_Principal.birth,110) as birth,\n" +
									"month(dbo.Customer_Principal.birth) as birth2,\n" +
									"day(dbo.Customer_Principal.birth) as birth3,\n" +
									"CAST(month(dbo.Customer_Principal.birth) as VARCHAR(2) )+' 月 '+CAST(day(dbo.Customer_Principal.birth) AS VARCHAR(2))+' 日' as birth4,\n" +
									"datediff(day,dateadd(year,datediff(year,dbo.Customer_Principal.birth,getdate()),dbo.Customer_Principal.birth),getdate()) as ts\n" +
									"FROM\n" +
									"dbo.Customer_Principal\n" +
									"LEFT JOIN dbo.Customer ON dbo.Customer_Principal.clientId = dbo.Customer.ID \n" +
									"LEFT JOIN dbo.sysACCOUNT ON dbo.Customer_Principal.creatorId = dbo.sysACCOUNT.id\n" +
									"LEFT JOIN dbo.BASE_DATA ON dbo.Customer_Principal.positionId = dbo.BASE_DATA.id \n" +
									"WHERE\n" +
									"--dbo.Customer_Principal.isMain = 1 AND\n" +
									"dbo.Customer_Principal.enable = 1 \n";
									
			if(birthGE!=null || birthLE!=null){
				if(birthGE!=null&&!"".equals(birthGE)){
					sql +=" and CONVERT (VARCHAR (5),dbo.Customer_Principal.birth,110) >= '"+birthGE+"' ";
				}
				
				if(birthLE!=null&&!"".equals(birthLE)){
					sql +=" and CONVERT (VARCHAR (5),dbo.Customer_Principal.birth,110) <= '"+birthLE+"' ";
				}
			}
			
			if((birthGE==null||"".equals(birthGE))  && (birthLE==null||"".equals(birthLE))){
				sql +=" and datediff(day,dateadd(year,datediff(year,dbo.Customer_Principal.birth,getdate()),dbo.Customer_Principal.birth),getdate()) between -15 and 1 ";
			}
								
			sql += " ) t where 1=1  ";
			
			 if(nameLIKE!=null&&!"".equals(nameLIKE)){
				 sql+=" and t.name like '%"+nameLIKE+"%' ";
			 }
			 if(customernameLIKE!=null&&!"".equals(customernameLIKE)){
				 sql+=" and t.customername like '%"+customernameLIKE+"%' ";
			 }
			 if(accountIdEQ!=null&&!"".equals(accountIdEQ)){
				 sql+=" and t.accountId = '"+accountIdEQ+"' ";
			 }
			 
			 if(bumenLIKE!=null&&!"".equals(bumenLIKE)){
				 BaseDao bd = new  BaseDao();
				 String idslist = bd.getidList(bumenLIKE);
				 
				 String[] ids = idslist.split(",");
				 List<String> tempList = Arrays.asList(ids);
				 
				 DataBase db = new DataBase();
				    try
				    {
				      db.connectDb();
				      ResultSet rs = db.queryAll(" select  *  from AccountOrg ");
				      while (rs.next()){
				    	  String orgid = rs.getString("orgid");
				    	  
				    	  if(tempList.contains(orgid)){
				    		  accountidlist = accountidlist + rs.getString("accountid") + ",";
				          } 
				      }
				    }catch (Exception de) {
				       de.printStackTrace();
				       try{db.close();} catch (Exception de1) {de1.printStackTrace();}
				    }finally{
					   try{db.close();} catch (Exception de) {de.printStackTrace();}
					}
				    
				    sql = " select top 100 percent * from  ("+sql+") t where t.accountid in ( "+ accountidlist.substring(0, accountidlist.length() - 1) +") ";
			}
			
			sql +=" and ("+sqlUtil.getSqlStrByArrays(_AccountIds,"t.accountid")+"or t.accountid='"+accountid ;
			sql +="') ";
			
			if (!DataFormat.booleanCheckNull(sort))
				 sql+=" order by " + sort + " " + DataFormat.objectCheckNull(order);
	 		else
	 			 sql+=" order by t.createtime desc ";
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		
		
		//客户库存上报
		public String addStore(){
			DateFormat df = new DateFormat();//设置日期格式
			String now=df.getNowTime();
			this.data.put("accountId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
			this.data.put("createTime",now);
			return getReturnString(super.add());
		}
		//客户库存修改
		public String updateStore(){
			DateFormat df = new DateFormat();//设置日期格式
			String now=df.getNowTime();
			this.data.put("modifyId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
			this.data.put("modifyTime",now);
			return getReturnString(super.update());
		}
		
		
		 /**
		 * 查看下级和自己的拍照信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchPhoto(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from photouploads t where 1=1  ");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.accountid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by t.createtime desc");
			//System.out.println("照片查看sql===========:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		
		
		//获取当前登录人的id
		public String getId(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select distinct top 100 percent t.id value,t.name text from sysaccount t where 1=1  ");
	        sql.append(" and t.id ='"+selfId+"'");
			excuteSQLPageQuery(sql.toString());
			System.out.println("当前登录人的Id==============="+sql.toString());
			result = getRows();
			return getReturn();
		}
		

		//获取客户的客户跟踪级别
		public String getlevelId(){ 
			this.session = request.getSession();
			StringBuffer sql =new StringBuffer("select distinct top 100 percent t.levelid levelid from customer t where 1=1  ");
	        sql.append(" and t.id ='"+this.data.get("id")+"'");
			excuteSQLPageQuery(sql.toString());
			System.out.println("客户的跟踪级别Id===============++++++++++"+sql.toString());
			result = getRows();
			return getReturn();
		}
		
		//获取当前登录人的id
	     public String getAccountId(){
	        this.session = request.getSession();
	        String accountid =  ((AccountBean)this.session.getAttribute("accountBean")).getId();
	        return getReturnString(accountid);
	     }
	     
	     /*
	      * 簽到管理  签到 签退  定位 信息
	      * jack_sun 2014年6月4日 09:01:29
	      */
	     public String getCVLocations(){
	         this.session = request.getSession();
	         AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
	         //String _canAccountIds = accountBean.get_canAccountIds();//登录人权限下的用户
	         
	         SqlUtil sqlUtil = new SqlUtil();
	         StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from  kqview  t where 1=1 ");
	        // sql.append(" and ("+sqlUtil.getSqlStrByArrays(_canAccountIds,"t.accountid")+"or t.accountid='"+accountBean.getId());
	         //sql.append("')");
	         sql.append(super.getSearchStr(super.map));
	         if (!DataFormat.booleanCheckNull(sort))
	             sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
	         else
	             sql .append( " order by t.signintime desc ");
	         excuteSQLPageQuery(sql.toString());
	         result = getRows();
	         return getReturn();
	     }
	     
	     /*
	      * 客户信息管理  客户  定位 信息
	      * jack_sun 2014年6月4日 09:01:29
	      */
	     public String getClientLocations(){
	         this.session = request.getSession();
	         AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
	         //String _canAccountIds = accountBean.get_canAccountIds();//登录人权限下的用户
	         
	         SqlUtil sqlUtil = new SqlUtil();
	         StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from  clientLoc_view  t where 1=1 ");
	        // sql.append(" and ("+sqlUtil.getSqlStrByArrays(_canAccountIds,"t.accountid")+"or t.accountid='"+accountBean.getId());
	         //sql.append("')");
	         sql.append(super.getSearchStr(super.map));
	         if (!DataFormat.booleanCheckNull(sort))
	             sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
	         else
	             sql .append( " order by t.locationtime desc ");
	         excuteSQLPageQuery(sql.toString());
	         result = getRows();
	         return getReturn();
	     }
	     
	     
	   //照片 管理，中的  终端拜访 照片 查看
	 	public String getPictures(){
	         String accountid=((AccountBean)this.session.getAttribute("accountBean")).getId();
	         String sql="SELECT\n" +
	                 "   TOP 100 PERCENT *\n" +
	                 "FROM\n" +
	                 "   (\n" +
	                 "       SELECT\n" +
	                 "           p.id," +
	                 "           p.fileid,\n" +
	                 "           p.tableid,\n" +
	                 "           p.fileName,\n" +
	                 "           p.filetype,\n" +
	                 "           b.name as phototypename,\n" +
	                 "      p.phototype, \n" +
	                 "      cv.clientId, \n" +
	                 "      c.clientname,\n" +
	                 "      cv.creatorId,\n" +
	                 "      cv.createTime,\n" +
	                 "      a.name as accountName\n" +
	                 "       \n" +
	                 "       FROM \n" +
	                 "           Uploadfile p\n" +
	                 "       LEFT JOIN CLIENT_VISIT cv ON p.tableid = cv.id\n" +
	                 "    LEFT JOIN client c ON c.id = cv.clientId\n" +
	                 "    LEFT JOIN ACCOUNT a ON a.id = cv.creatorid \n" +
	                 "    left JOIN BASE_DATA b ON b.id = p.phototype\n" +
	                 "       WHERE p.tableName = 'client_visit' and p.filetype='2'\n" +
	                 "   ) a\n" +
	                 "WHERE\n" +
	                 "   1 = 1";
	         return getBeansBySql(sql);
	     }
	     
	 	
	 	public String getCutomerRecordsCount(){
			StringBuffer sqlstr=new StringBuffer();
			String endtime = (String) this.map.get("inforeportdateLE");
			String starttime = (String) this.map.get("inforeportdateGE");
			String accountid = (String) this.map.get("accountIdEQ");
			
			sqlstr.append("select sa.name accountname,c.accountId, " +
	        		"(" +
	        		" select count(c1.accountId) FROM customer c1 where  c1.accountId = c.accountId  and c1.enable!=0   ");
	        if(!"".equals(starttime) && starttime != null){
				sqlstr.append(" and CONVERT(varchar(10), c1.inforeportdate, 23) >= '"+starttime+"'");
			}
	        if(!"".equals(endtime) && endtime != null){
	        	sqlstr.append(" and CONVERT(varchar(10), c1.inforeportdate, 23) <= '"+endtime+"'");
	        }
	        	sqlstr.append(" group by c1.accountId ) recordscount from customer c left join sysACCOUNT sa on c.accountId =sa.id ");
	        if(!"".equals(accountid) && accountid != null){					
	        	sqlstr.append(" where c.accountId = '"+accountid+"'");
	        }
	        	sqlstr.append(" group by sa.name,c.accountId");
	        	
	        	excuteSQLPageQuery(sqlstr.toString());
		         result = getRows();
		         return getReturn();		
	 	}
	 	
	 	public String getCutomerAndProject(){
	 		String accountid = this.request.getParameter("accountid");
	 		StringBuffer sqlstr=new StringBuffer();
	 		
	 		sqlstr.append("select c.ID,c.name customername,c.projectname,sa.name accountname from customer c,sysaccount sa where c.accountId=sa.id and c.enable!=0 and c.accountId="+accountid);
	 		
        	excuteSQLPageQuery(sqlstr.toString());
	         result = getRows();
	         return getReturn();
	 	}
}
