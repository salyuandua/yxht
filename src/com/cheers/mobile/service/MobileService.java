package com.cheers.mobile.service;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Part;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;

public class MobileService extends Dao {
	
	public String add(){
		HashMap m = new HashMap();
		this.session = request.getSession();
		DateFormat df = new DateFormat();
			this.data.put("createTime", df.getNowTime());
			String retStr=super.add();
			if(retStr.equals("true")){
				Map sqlMap=new HashMap();
				int i=getMaxId("PhotoUpload", sqlMap);
				return super.getReturnString(i+"");
			}else return super.getReturnString(retStr);
//		}
}
	/**
	 * 待办事项查询
	 * 
	 */
	 public  String getBeansSelf2()  {
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String id = accountBean.getId();//当前登录人的
			StringBuffer sql =new StringBuffer("select  top 100 PERCENT id,clientname,clientprincipalname,nextvisitdate,nextpurpose  from Undowork t where 1=1");
			sql.append(" and t.accountid='"+id+"'");
			sql.append(" and t.ts1<=7");//7天之内的
			sql.append(super.getSearchStr(super.map));
			if (!DataFormat.booleanCheckNull(sort))
				sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
			else
				sql .append( " order by id");
			System.out.println("-------------:"+sql.toString());
			 return super.getBeansBySql(sql.toString());
		}
	 /**
		 * 个人日报查询列表显示方法
		 * @return
		 */
		public String getBeansSelf(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String id = accountBean.getId();//当前登录人的
			StringBuffer sql =new StringBuffer("select top 100 percent id,reportdate,typename,accountname,isreply from WorkReports t where 1=1");
			sql.append(" and t.accountid='"+id+"'");
			sql.append(super.getSearchStr(super.map));
			if (!DataFormat.booleanCheckNull(sort))
				sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
			else
				sql .append( " order by reportdate desc ");
			System.out.println("sql:============"+sql);
			 return super.getBeansBySql(sql.toString());
		}
		/**
		 * 个人日志审批列表显示方法
		 * @return
		 */
		public String getBeansReport(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  id,reportdate,typename,accountname,isreply  from WorkReports t where 1=1");
			sql.append(" and "+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
			sql.append(" and t.accountid !='"+selfId+"'");
			sql.append(super.getSearchStr(super.map));
			if (!DataFormat.booleanCheckNull(sort))
				sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
			else
				sql .append( " order by id desc ");
			System.out.println("sql:"+sql);
			 return super.getBeansBySql(sql.toString());
		}
		/**
		 * 订单查询
		 * 
		 */
		public String getBeansOrder(){
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent id,customername,tel,linkmanname,memo,dqname,qyname,orderdate,phone,code,shippingaddress,accountname,createtime,spnodename from orderss t where 1=1");
			sql.append(" and "+sqlUtil.getSqlStrByArrays(canAccountIds,"t.creatorId"));
			sql.append(" or t.creatorId ='"+selfId+"'");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id desc ");
			System.out.println("sql:"+sql);
			return super.getBeansBySql(sql.toString());
		 }
		/**
		 * 查看下级和自己的客户拜访上报信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearch(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  id,visitdate,customername,customerprincipalname,visittypename,content,isvisit  from customerVisitView t where 1=1 and t.isvisit!=3");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
			sql.append(" or t.accountid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			if (!DataFormat.booleanCheckNull(sort))
				sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
			else
				sql .append( " order by id desc ");
			System.out.println("sql:"+sql);
			 return super.getBeansBySql(sql.toString());
		}
		/**
		 * 新闻浏览
		 * @return
		 */
		public String getNewBeans(){ 
			this.session = request.getSession();
			//获取当前登陆用户的id
			String creatid = ((AccountBean)session.getAttribute("accountBean")).getId();
			String sql = "select top 100 percent s.id,s.title,s.content,ns.hasReading from News s  JOIN \n" +
					"newsAccount ns on s.ID=ns.newsId where s.ID \n" +
					"in(select n.newsId from newsAccount n where n.accountid='"+creatid+"') and ns.accountId='"+creatid+"'";
			if (!DataFormat.booleanCheckNull(sort))
				sql += " order by " + sort + " " + DataFormat.objectCheckNull(order);
			else
				sql += " order by s.id desc";
			System.out.println("sql:"+sql);
			return super.getBeansBySql(sql.toString());
		}
		/**
		 * 查看下级和自己的客户信息
		 * 
		 */
		public String getBeansSearch2(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  id,name,customertypename,customerflname,accountname,levelname  from customerView t where 1=1");
			if(super.map.containsKey("name"))
				sql.append(" and t.name like '%"+super.map.get("name")+"%' ");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.accountid ='"+selfId+"')");
			sql .append( " order by id desc ");
			System.out.println("sql:"+sql);
			return super.getBeansBySql(sql.toString());
		}
		/**
		 * 查看下级和自己的潜在客户信息
		 * 
		 */
		public String getBeansSearchqz(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  id,name,customertypename,customerflname,accountname,levelname  from customerView t where 1=1 and t.customerflid=138 ");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.accountid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id desc ");
			System.out.println("sql:"+sql);
			return super.getBeansBySql(sql.toString());
		}
		/**
		 * 查看下级和自己的合作客户信息
		 * 
		 */
		public String getBeansSearchwork(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  id,name,customertypename,customerflname,accountname,levelname  from customerView t where 1=1 and t.customerflid=139 ");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.accountid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id desc ");
			System.out.println("sql:"+sql);
			return super.getBeansBySql(sql.toString());
		}
		/**
		 * 查看下级和自己的日常拜访客户信息
		 * 
		 */
		public String getBeansSearchvisit(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  id,name,customertypename,customerflname,accountname,levelname  from customerView t where 1=1 and t.customerflid=137 ");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.accountid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id desc ");
			System.out.println("sql:"+sql);
			return super.getBeansBySql(sql.toString());
		}
		//得到价格
		public String getBeansPri(){ 
			StringBuffer sql =new StringBuffer("select top 100 percent  id,name  from CUSTOMER_PRINCIPAL t where 1=1");
			sql.append(super.getSearchStr(super.map));
			return super.getBeansBySql(sql.toString());
		}
		//得到权限下的额客户
		 public String getBeansHKQX(){
				this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
				String canAccountIds = accountBean.get_canClientIds();//自己可以看到的客户信息
				String selfId = accountBean.getId();//当前登录人id
				SqlUtil sqlUtil = new SqlUtil();
				String sql ="select distinct top 50  c.id value,c.name text from customer c \n" +
						"where 1=1 and c.enable=1 ";
				sql+=" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"c.id")+" or c.creatorId="+selfId+")";
				sql += " order by c.id";
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
			}

		 //更新手机端本地存储的客户
		 public String updateCustomer(){
			 this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
				String canAccountIds = accountBean.get_canClientIds();//自己可以看到的客户信息
				String selfId = accountBean.getId();//当前登录人id
				SqlUtil sqlUtil = new SqlUtil();
				String maxid=request.getParameter("id");
			 if(maxid==null||maxid.equals("null"))
				 maxid="0";	
			 String sql="select id,name,address,taxNumber tele,balance from Customer where enable=1  and id >"+maxid;
			 sql+=" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"id")+" or creatorId="+selfId+")";
			 System.out.println("sql+=======:"+sql);
			return super.getBeansBySql(sql);
		 }
		 
			public String getLocalCustomer(){
				this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
				String canAccountIds = accountBean.get_canClientIds();//自己可以看到的客户信息
				String selfId = accountBean.getId();//当前登录人id
				SqlUtil sqlUtil = new SqlUtil();
				String sql="select customer.id id,  customer.name clientname, customer.codes clientcode," +
						"  customer.province ,  customer.city ,  customer.country ,taxNumber ,balance from Customer where enable=1";
				sql+=" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"id")+" or accountId="+selfId+")";
				 System.out.println("sql 更新本地 客户  数据 +-------:"+sql);
				return super.getBeansBySql(sql);
			}
			//照片查询列表
			public String getBeansPhoto(){ 
				this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
				String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
				String selfId = accountBean.getId();//当前登录人id
				SqlUtil sqlUtil = new SqlUtil();
				StringBuffer sql =new StringBuffer("select top 100 percent  id,customername,structname,phototype,typename,phototime2,memo,accountname,createtime  from photouploads t where 1=1");
				sql.append(" and "+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
				sql.append(" or t.accountid ='"+selfId+"'");
				sql.append(super.getSearchStr(super.map));
				sql .append( " order by createtime desc ");
				System.out.println("照片查看============"+sql.toString());
				return super.getBeansBySql(sql.toString());
			}
			//拍照删除
			public String delete2(){
				List donext=new ArrayList();
				String id=String.valueOf(request.getParameter("id"));
				
				this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
				String selfId = accountBean.getId();//当前登录人id
				
				StringBuffer sql2 = new StringBuffer();
				sql2.append("select accountid ");
				sql2.append(" from photoupload ");
				sql2.append(" where 1=1 and id="+id);
				PageBean bean = new MsSqlPageBean();
				DataBase dbs=new DataBase();
				try {
					dbs.connectDb();
					ResultSet rs = dbs.queryAll(sql2.toString());
					while ((rs!=null)&&(rs.next())){
						String accountid=rs.getString("accountid");
						if(!(accountid.equals(selfId))){//领导查看
							return getReturnString("false2");
						}else{//业务员自己可删除
								 String sql1="delete from photoupload  where id="+id;
								 donext.add(sql1);
								 db=new DataBase();
								 db.updateBatch((String[])donext.toArray(new String[donext.size()]));
								 return getReturnString("true");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return getReturnString("false");
				}
				
				
				return getReturnString("true");
				
			}
			
			//客户主联人生日提醒
			public  String getBeansCustomerBirth()  {
				this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
				String id = accountBean.getId();//当前登录人的
				StringBuffer sql =new StringBuffer("select top 100 percent t.id,t.customername,t.name,t.positionname,t.phone,t.tel,t.email,t.createtime,t.accountname,t.ts  from customerbirthView t where 1=1");
				sql.append(" and t.creatorid='"+id+"'");
				sql.append(" and t.ts>=-15 and t.ts<=0 ");//提前15天提醒
				sql.append(super.getSearchStr(super.map));
			    sql .append( " order by id");
			    return super.getBeansBySql(sql.toString());
			}
			
			/**
			 * 客户库存查询
			 * 
			 */
			public String getBeansStore(){
				this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
				String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
				String selfId = accountBean.getId();//当前登录人id
				SqlUtil sqlUtil = new SqlUtil();
				StringBuffer sql =new StringBuffer("select top 100 percent id,customername,createtime,accountname from storesView t where 1=1");
				sql.append(" and "+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
				sql.append(" or t.accountid ='"+selfId+"'");
				sql.append(super.getSearchStr(super.map));
				sql .append( " order by id desc ");
				System.out.println("sql:"+sql);
				return super.getBeansBySql(sql.toString());
			 }
			//库存删除
			public String deleteStore(){
				try {
				      List donext=new ArrayList();
				      String id=String.valueOf(request.getParameter("id"));
					  String sql1="delete from store  where id="+id;
					  String sql2="delete from storeDetail  where storeid="+id;
					 donext.add(sql1);
					 donext.add(sql2);
					 db=new DataBase();
					 db.updateBatch((String[])donext.toArray(new String[donext.size()]));
					 return getReturnString("true");
				} catch (Exception e) {
					e.printStackTrace();
					return getReturnString("false");
				}
			}
			//日常，潜在客户的修改
			public String update(){
				String accountid=((AccountBean)this.session.getAttribute("accountBean")).getId();
				StringBuffer sqlpd = new StringBuffer();
				sqlpd.append("select accountid ");
				sqlpd.append(" from customer ");
				sqlpd.append(" where 1=1 and id="+this.data.get("id"));
				DataBase dbs2=new DataBase();
				try {
					dbs2.connectDb();
					ResultSet rs = dbs2.queryAll(sqlpd.toString());
					while ((rs!=null)&&(rs.next())){
						String accountid2=rs.getString("accountid");
						if(!(accountid2.equals(accountid))){//领导查看
							return getReturnString("业务员只能修改自己的客户信息！");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return getReturnString("false");
				}
				
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
				if (result != null && result.size()>0){
					return getReturnString("sorry,已有重复客户编码");
				}else {
						this.session = request.getSession();
						DateFormat df = new DateFormat();//设置日期格式
						String now=df.getNowTime();
						
						//处理客户信息中一对多的更新
						List<String> upsqls =  new ArrayList<String>();
						//删除所有去除项
						this.map.put("table", "CUSTOMER_PRINCIPAL");
//						upsqls = deleteSql(this.map);
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
			
			//合作客户的修改------只允许管理员角色的修改
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
//						upsqls = deleteSql(this.map);
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
			
			
			
			
			  /*
	         * 手机端 得到 客户信息 client, 
	         *
	         */
	        public String getCliMobile(){
	    		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
	    		//String canClientIds = accountBean.get_canClientIds();//用户的下级
	    		String _canAccountIds = accountBean.get_canAccountIds();//登录人权限下的用户
	    		SqlUtil sqlUtil = new SqlUtil();
	            String maxid=request.getParameter("id");
	            if(maxid==null||maxid.equals("null"))
	                maxid="0"; 
	            String sql = "SELECT\n" +
	                    " TOP 100 PERCENT \n" +
	                    "   customer.id id,\n" +
	                    "   customer.name clientname,\n" +
	                    "   customer.codes clientcode,\n" +
//	                    "   customer.zhuji ,\n" +
	                    "   customer.province ,\n" +
	                    "   customer.city ,\n" +
	                    "   customer.country ,\n" +
//	                    "   customer.accountid, \n" +
//	                    "   a.name accountname, \n" +
	                    "   customer.customertypeid,\n" +
	                    "   customer.receivtypeid,\n" +
	                    "   customer.connector,\n" +
	                    "   customer.address,\n" +
	                    "   customer.productcategoryid,\n" +
//	                    "   customer.sourceid,\n" +
	                    "   customer.customerkpinfor,\n" +
//	                    "   customer.creatorid,\n" +
//	                    "   customer.createtime,\n" +
//	                    "   customer.modifierid,\n" +
//	                    "   customer.modifytime,\n" +
//	                    "   customer.companyprofile,\n" +
//	                    "   customer.memo,\n" +
//	                    "   customer.levelid,\n" +
//	                    "   customer.balance,\n" +
//	                    "   customer.industryid,\n" +
	                    "   customer.taxnumber,\n" +
//	                    "   customer.customerflid,\n" +
//	                    "   customer.bnljfhje,\n" +
//	                    "   customer.bnljfyje,\n" +
//	                    "   customer.bnljhkje,\n" +
	                    "   customer.enable  FROM\n" +
	                    "   customer \n" +
	                    " LEFT JOIN SYSACCOUNT a on a.id = customer.accountid \n" +
	                    " WHERE\n" +
	                    "   1 = 1 \n" +
	                    " AND dbo.customer.enable = 1  and customer.id >"+maxid;

	    		//sql+=" and ("+sqlUtil.getSqlStrByArrays(canClientIds,"customer.id")+" or customer.accountId = '" +accountBean.getId()+"' )";
	            sql +=" and ("+sqlUtil.getSqlStrByArrays(_canAccountIds,"customer.accountid")+"or customer.accountid='"+accountBean.getId()+"' ) ";
	            
	            if (!DataFormat.booleanCheckNull(sort))
	                   sql+= " order by " + sort + " " + DataFormat.objectCheckNull(order);
	               else
	                   sql += " order by id desc ";
	            //System.out.println("******getCliMobile******"+sql);
	               excuteSQLPageQuery(sql.toString());
	               result = getRows();
	               return getReturn();
	        }
	        
	        
	        /*
			 * 手机端 过的 订单上报 中的 产品 信息 （由 id 和 text 组成，便于显示 使用）
			 * jianfeng 二〇一四年三月十三日 11:24:462
			 */
			public String getProMobile(){
			    String maxid=request.getParameter("id");
	            if(maxid==null||maxid.equals("null"))
	                maxid="0"; 
		         String sql = "select top 100 PERCENT  " + 
		         		"                     p.id id," + 
		         		"                     p.name productname," + 
		         		"                     p.codes productcode," + 
		         		"                     p.shortname shortname," + 
		         		"                     p.producttypeid producttypeid," + 
		         		"                     p.specification  specification," + 
		         		"                     p.tecstandard tecstandard," + 
		         		"                     p.memo memo," + 
		         		"                     p.isenable isenable," + 
		         		"                     p.zhuji zhuji," + 
		         		"                     p.productunit productunit," + 
		         		"                     p.price price " + 
		         		"                     from PRODUCT p" + 
		         		"                     where 1=1 and p.id >"+maxid;
		         
		         if (!DataFormat.booleanCheckNull(sort))
		                sql+= " order by " + sort + " " + DataFormat.objectCheckNull(order);
		            else
		                sql += " order by id";
		         System.out.println("******getProMobile******"+sql);
		            excuteSQLPageQuery(sql.toString());
		            result = getRows();
		            return getReturn();
		    }
			 	
			
			
			/*
		     * 考勤管理 获得 上报人 方法
		     */
		    public String getAccBean(){
		        this.session = request.getSession();
		        String accountid =  ((AccountBean)this.session.getAttribute("accountBean")).getId();
		        
		        String sql ="select id,name from sysaccount where id ='"+ accountid +"'";
		        System.out.println(" getAccBean  sql:"+sql);
		        return super.getBeansBySql(sql.toString());
		    }
		    
		    //手机端  获得 签到 和 签退 信息的 方法
	        public String getSignInfo(){
	                 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	                 String now=df.format(new Date());
	                 this.session = request.getSession();
	                 AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
	                 String selfId = accountBean.getId();//当前登录人id
	                 //获得 当天 已经 签到的 信息
	                 StringBuffer sql =new StringBuffer("select top 100 percent  c.id," +
	                        " CONVERT(varchar(100), c.signintime, 20) as signintime," +
	                        " c.signoutlontitude," +
	                        " CONVERT(varchar(100), c.signouttime, 20) as signouttime," +
	                        " c.signinlontitude," +
	                        " c.isin," +
	                        " c.isout," +
	                        " c.signtype," +
	                        " c.memo," +
	                        " c.outmemo   " +
	                        " from " +
	                        " businessplan_kq c where 1=1 and convert(varchar(10),signintime,121)='"+ now +"'" );
	                 sql.append(" and c.accountid="+((AccountBean)this.session.getAttribute("accountBean")).getId());
	                 sql.append(super.getSearchStr(super.map));
	                 sql .append( " order by c.signInTime desc");
	                 
	                 //System.out.println("getSignInfo  sql:"+sql);
	                 
	                 return super.getBeansBySql(sql.toString());
	         }
	      //手机端 签到
	        public String signIn(){
	             
	                 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	                 String now=df.format(new Date());
	                 this.data.put("signInTime", now);
	                 this.data.put("createTime", now);
	                 this.data.put("accountid", ((AccountBean)this.session.getAttribute("accountBean")).getId());
	                 return getReturnString(super.add());
	             }
	             
	         //手机端 签退
	         public String signOut(){
	                 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	                 String now=df.format(new Date());
	                 this.data.put("signOutTime", now);
	                 this.data.put("accountid", ((AccountBean)this.session.getAttribute("accountBean")).getId());
	                 return getReturnString(super.update());
	         }
			
	       //手机端  出差考勤  获取 出差计划 日期的 方法
	            public String getKQdate(){
	                     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	                     String now=df.format(new Date());
	                     this.session = request.getSession();
	                     AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
	                     String selfId = accountBean.getId();//当前登录人id
	                     //获得 当天 已经 签到的 信息
	                     StringBuffer sql =new StringBuffer("SELECT top 100 percent \n" +
	                             "   b.id,\n" +
	                             "   b.starttime," +
	                             "   b.accountid,\n" +
	                             "   b.endtime   \n" +
	                             "FROM  \n" +
	                             "   businessplan b \n" +
	                             "WHERE CONVERT(varchar(11),'"+ now +"', 120) BETWEEN CONVERT(varchar(11), b.starttime, 120) AND CONVERT(varchar(11), b.endtime, 120) ");
	                     sql.append(" and b.accountid="+((AccountBean)this.session.getAttribute("accountBean")).getId());
	                     sql.append(super.getSearchStr(super.map));
	                     sql .append( " order by b.createtime desc");
	                     //System.out.println("getSignInfo  sql:"+sql);
	                     return super.getBeansBySql(sql.toString());
	             }
	            
	          //手机端  獲得版本號 信息，對 低版本提示更新！
                public String getVersion(){
                         this.session = request.getSession();
                         AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
                         String selfId = accountBean.getId();//当前登录人id
                         //获得 当天 已经 签到的 信息
                         StringBuffer sql =new StringBuffer(" select top 1 * from version order by id desc ");
                         //System.out.println("getSignInfo  sql:"+sql);
                         return super.getBeansBySql(sql.toString());
                 }
                
            /*
             * 手机 客户 定位 功能 ，定位 方法
             */
            public String updateCustomerLoc(){
            	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String now=df.format(new Date());
            	
                    this.session = request.getSession();
                    AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
                    String selfId = accountBean.getId();//当前登录人id
                    
                    String tablename = (String) this.data.get("table");
                    String id = this.data.get("id").toString();
                    
                    if("".equals(this.data.get("lontitude"))||this.data.get("lontitude") == null){
                    	 return getReturnString("false");
                    }
                    
                    DataBase db = new DataBase();
                    try {
						db.connectDb();
						String sql = " update "+ tablename +" set locationtime='"+ now +"',locationmanid='"+ accountBean.getId() +"', lontitude ='"+this.data.get("lontitude")+"', latitude='"+this.data.get("latitude")+"', accuracy='"+this.data.get("accuracy")+"', isios='"+(String) this.data.get("isios")+"' where id = '"+id+"' ";
						db.update(sql);
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "false";
					}finally {
						try {
							db.close();
						} catch (DbException e2) {
							e2.printStackTrace();
						}
					}
                    
                    return getReturnString("true");
            }
            //手机版本更新
            public String appUpdate(){
           	 String appVersion=request.getParameter("appVersion"); //获取应用的version
           	 String iosVersion=request.getParameter("iosVersion");//获取操作系统的version
           	 String isios=request.getParameter("isios");
           	 iosVersion=iosVersion.substring(0, 3);
           	 System.out.println("iosverios"+iosVersion);
           	 int maxId=getMaxId("version", new HashMap());
           	 if(maxId!=Integer.parseInt(appVersion)){//如果不相等，有更新
           		 String sql="";
           		if(Double.parseDouble(iosVersion)>=7.1){//ios7.1以上
           			sql="select versionName from version where id='"+maxId+"'";
           			System.out.println(super.getBeansBySql(sql));
           			return super.getBeansBySql(sql);
           		}else{//其他版本ios
           			sql="select versionName from version where id='"+maxId+"'";
           			System.out.println(super.getBeansBySql(sql));
           			return super.getBeansBySql(sql);
           		}
           	}
           	//如果相等，无更新
           	 if(isios!=null){
           		 JSONObject obj=new JSONObject();
           		 obj.put("versionname", "");
           		 JSONArray js=new JSONArray();
           		 js.add(obj);
           		 return getReturnString(js.toString());
           	 }
           	return "";
            }
            //获得照片上报数据的uuid
            public String getUUID(){
            	return UUID.randomUUID().toString().replace("-", "");
            }

            //获得当前登录人的所有信息
            public String getSelfOrg(){
            	String accountId=((AccountBean)this.session.getAttribute("accountBean")).getId();
            	String sql="SELECT b.* from SysAccount_post a LEFT JOIN SYSTEM_TREE b on a.orgId = b.id where a.accountId="+accountId;
            	System.out.println(sql);
            	
            	return getBeansBySql(sql);
            }
            
            //获得客户的产品
            public String getProByCustomer(){
            	String customerId=request.getParameter("customerId");
            	String sql="SELECT b.name,a.memo,a.createtime from Customer_Product a LEFT JOIN BASE_DATA b on a.productid=b.id where a.customerid="+customerId;
            	return super.getBeansBySql(sql);
            }
            
            //问题反馈
            public String question(){
            	String accountId=((AccountBean)this.session.getAttribute("accountBean")).getId();
            	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String now=df.format(new Date());
                String question=data.get("question").toString();
                String id=UUID.randomUUID().toString().replace("-", "");
                String sql="insert into mobilequestion(id,account,createtime,question) values('"+id+"','"+accountId+"','"+now+"','"+question+"');";
                int i=super.update(sql);
                return super.getReturnString(""+i);
            }
            //获得借款余额
            public String getBalance(){
            	String userId=request.getParameter("id");
            	String sql="select sa.id,sa.name,ISNULL(um.usemoney - ISNULL(f.moneysum,0), 0) usemoney from sysACCOUNT sa LEFT JOIN (SELECT sum(CONVERT(numeric(18,4),moneysum)) moneysum,purchaseApplyId FROM fee where spstate=1 and enable=1 group by purchaseApplyId) f ON f.purchaseApplyId = sa.id LEFT JOIN (SELECT sum(CONVERT(numeric(18,4),usemoney)) usemoney,useapplyid FROM usemoney where spstate=1 and isenable=1 group by useapplyid) um ON um.useapplyid = sa.id where sa.id="+userId;
            	return super.getBeansBySql(sql);
            }
            
            
            
            
}
