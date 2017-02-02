package com.cheers.client.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.jstl.sql.Result;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientVisitService extends Dao {
	
	
	public String add(){
		List<String> upsql=new ArrayList<String>();
		DataBase db=new DataBase();
		ResultSet rs=null;
		this.session = request.getSession();
		 String accountid=((AccountBean)session.getAttribute("accountBean")).getId();
		this.data.put("accountId", ((AccountBean)session.getAttribute("accountBean")).getId());
		DateFormat df = new DateFormat();//设置日期格式
		String now=df.getNowTime();
		this.data.put("createTime", now);
		this.data.put("visitDate", now);
		this.data.put("spStatus", 0);
		this.data.put("nextVisitType", 0);
		this.data.put("visitType", 0);
	    String updatedb="update Customer_visit set scheduleStatus='1' where customerId="+this.data.get("customerId")+" and accountId="+accountid;
		try {
			db.update(updatedb);
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		String add=super.add();
		if(add.equals("true")){
			
			String afterlevel="";
			try {
				 
				  //取得客户原来的客户级别
				    String selectlevel="select * from Customer where id="+this.data.get("customerId");
				     rs= db.queryAll(selectlevel);
				     if (rs.next()) { 
				    	 afterlevel= rs.getString("levelId");
				    	 if(!afterlevel.equals(this.data.get("levelId"))){//只有客户级别跟当前级别不一样的时候才更改
				    		 String updatesql="update Customer set levelId = '"+ this.data.get("levelId")+"' where id= "+ this.data.get("customerId") ;
				    		 upsql.add(updatesql);
				    		 String createtime=df.getNowTime();
				    		 String customerid=String.valueOf(this.data.get("customerId"));
				    		 String afterid=String.valueOf(this.data.get("levelId"));
				    		 String updatecuslvsql="insert into cuslvRecord(customerid,beforeid,afterid,accountid,createtime)" +
				    				 "values("+customerid+","+afterlevel+","+afterid+","+accountid+",'"+createtime+"')";
				    		 upsql.add(updatecuslvsql);
				    		 
				    	 }
				     }
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				db.updateBatch(upsql.toArray(new String[upsql.size()]));
			} catch (DbException e) {
				e.printStackTrace();
			}
			try {
				db.close();
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map sqlMap=new HashMap();
			sqlMap.put("createTime", now);
			int i=getMaxId((String)this.data.get("table"), sqlMap);
			return super.getReturnString(i+"");
		}else return super.getReturnString(add);
	}
	public String update(){
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		m.put("idEQ", this.data.get("id"));
		ResultSet rs=null;
		DateFormat df = new DateFormat();//设置日期格式
		List<String> upsql=new ArrayList<String>();
		DataBase db=new DataBase();
		String beforelevel="";
		if("true".equals(checkDuplicate(m))){
			if(this.result.size()>1){
				return getReturnString("已有重复数据");	
			}else {
				try {
					//取得客户原来的客户级别
					    String selectlevel="select * from Customer where id="+this.data.get("customerId");
					     rs= db.queryAll(selectlevel);
					     if (rs.next()) { 
					    	 beforelevel= rs.getString("levelId");
					    	 int beforeid=Integer.parseInt(beforelevel);
					    	 int customerid=(Integer) this.data.get("customerId");
					    	 String afterid= this.data.get("levelId").toString();
					    	 if(!beforelevel.equals(afterid)){//只有客户级别跟当前级别不一样的时候才更改
					    		 String updatesql="update Customer set levelId = '"+ this.data.get("levelId")+"' where id= "+ this.data.get("customerId") ;
					    		 upsql.add(updatesql);
					    		 String accountid=((AccountBean)session.getAttribute("accountBean")).getId();
					    		 String createtime=df.getNowTime();
					    		 
					    		 String updatecuslvsql="insert into cuslvRecord(customerid,beforeid,afterid,accountid,createtime)" +
					    				 "values("+customerid+","+beforelevel+","+afterid+","+accountid+",'"+createtime+"')";
					    		 upsql.add(updatecuslvsql);
					    		 
					    	 }
					     }
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					db.updateBatch(upsql.toArray(new String[upsql.size()]));
				} catch (DbException e) {
					e.printStackTrace();
				}
				try {
					db.close();
				} catch (DbException e) {
					e.printStackTrace();
				}
				DynaBean bean = (DynaBean) this.result.get(0);
				if(bean.get("id").toString().equals(this.data.get("id")+"")){
					return super.getReturnString(super.update());
				}else
				return getReturnString("信息丢失");
			}
		}else {
			return getReturnString("信息丢失");
		}
			
	}
	/**
	 * 跟踪拜访审批方法
	 * 
	 * 
	 * @return
	 */
	public String updateMod(){
		List donext=new ArrayList();
		this.session = request.getSession();
		DateFormat na = new DateFormat();
		String nowdate = na.getNowTime();
		this.data.put("replytime",nowdate);
		this.data.put("replyid", ((AccountBean)session.getAttribute("accountBean")).getId());//审批人的id
		this.data.put("replyname", ((AccountBean)session.getAttribute("accountBean")).getName());//审批人的id
		String retStr=super.add();
		if(retStr.equals("true")){
			String sql1="update Customer_visit  set spStatus=1  where id="+this.data.get("reportid");
			donext.add(sql1);
			 db=new DataBase();
			 try {
				db.updateBatch((String[])donext.toArray(new String[donext.size()]));
				 return getReturnString("true");
			 } catch (DbException e) {
				e.printStackTrace();
				return getReturnString("false");
			}
		}
		return  getReturnString(retStr);
	}
	
	//获得批复内容
			public String getAnswers(){
				String recordid=request.getParameter("id");
				String table=request.getParameter("table");
				String sql="select top 100 percent * from customervisitsp where reportid="+recordid ;
				sql+=" order by replytime asc ";
				excuteSQLPageQuery(sql);
				System.out.println("日志审批详情：======="+sql.toString());
				result = getRows();
				return getReturn();
			}
	/**
	 * 查看下级和自己的客户跟踪上报信息
	 *  2013年12月16日16:32:50
	 * @return
	 */
	public String getBeansSearch(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from customerVisitView t where 1=1");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
		sql.append(" or t.accountid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		if (!DataFormat.booleanCheckNull(sort))
			sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
		else
			sql .append( " order by id");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	/*
	 * 获得 拜访 客户的 定位 信息
	 * sun——jack 2014年6月27日 14:41:21
	 */
	public String getLocation(){ 
        StringBuffer sql =new StringBuffer("select top 100 percent  t.lontitude,t.latitude,t.customername,t.accountname,t.visitdate,t.createtime  from customerVisitView t where 1=1 ");
        sql.append(super.getSearchStr(super.map));
        if (!DataFormat.booleanCheckNull(sort))
            sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
        else
            sql .append( " order by id");
        excuteSQLPageQuery(sql.toString());
        result = getRows();
        return getReturn();
    }
	/**
	 * 批复下级客户跟踪上报信息
	 *  2013年12月16日16:32:50
	 * @return
	 */
	public String getBeansPf(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from customerVisitView t where 1=1");
		sql.append(" and "+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	//	sql.append(" or t.accountid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		if (!DataFormat.booleanCheckNull(sort))
			sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
		else
			sql .append( " order by id");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	
	/**
	 * 内勤回访上报列表页面
	 *  2013年12月16日16:32:50
	 * @return
	 */
	public String getBeansvisitreport(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from customerVisitView t where 1=1 and t.isvisit!=3 ");//老客户的不用回访
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
		sql.append(" or t.accountid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		if (!DataFormat.booleanCheckNull(sort))
			sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
		else
			sql .append( " order by id");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	/**
	 * 内勤未回访提醒页面
	 *  2013年12月16日16:32:50
	 * @return
	 */
	public String getBeansvisitreporttixing(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from clientvisittixingview t where 1=1 and t.isvisit!=3");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
		sql.append(" or t.accountid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		if (!DataFormat.booleanCheckNull(sort))
			sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
		else
			sql .append( " order by id");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	
	/**
	 *客户个人跟踪上报信息列表显示方法
	 * 
	 * @return
	 */
	public String getBeansSelf(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String id = accountBean.getId();//当前登录人的
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from customerVisitView t where 1=1");
		sql.append(" and t.accountid='"+id+"'");
		sql.append(super.getSearchStr(super.map));
		if (!DataFormat.booleanCheckNull(sort))
			sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
		else
			sql .append( " order by id");
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	public String delete(){ 
		return getReturnString(super.delete());
	}
	public  String getCode()  {
		return getReturnString(super.getCode());
	}
	public String getTables(){
		try {
			return getReturnString(super.getTables());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return getReturnString("");
		}
	}
	//内勤回访上报
    public String updateclientvisit(){
		
		return getReturnString(super.update());
	}
	public  String updatefieldByIds()  {
		return getReturnString(super.updatefieldByIds());
	}
	public  String getBeansSelf2()  {
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String id = accountBean.getId();//当前登录人的
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from Undowork t where 1=1");
		sql.append(" and t.accountid='"+id+"'");
		sql.append(" and t.ts1<=7");//7天之内的
		sql.append(super.getSearchStr(super.map));
		if (!DataFormat.booleanCheckNull(sort))
			sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
		else
			sql .append( " order by id");
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	
	//动态获取客户个级别客户
	public String geteachSQL(String startdate,String enddate,String accountid){
		String sql = "select id,name from base_data where type ='visitlv' and isenable ='1' ";
	    DataBase db = new DataBase();
	    ResultSet rs = null;
	    try
	    {
	      db.connectDb();
	      rs = db.queryAll(sql);
	      if (rs != null)
	      {
	  		this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
	    	int i=0;
	  		String sqlsc = "select sysaccount.id,";
	  		String sqllsc = "";
	        while (rs.next()) {
	        	sqlsc +="+ISNULL(a"+i+".lvname+':'+ISNULL(CAST(a"+i+".eachlvnum AS varchar),'0')+',','')";
	        	if (i>0){
	        		sqllsc+=" left join ";
	        	}
	        	sqllsc +="( select  sysaccount.id, '"+DataFormat.stringCheckNullNoTrim(rs.getString(2),"")+
	        			"' lvname , count( customer.levelId) eachlvnum  " +
	        			"from sysaccount left join (select accountid,levelid from  customer where customer.levelid=" +
	        			DataFormat.stringCheckNullNoTrim(rs.getString(1),"")+
	        			") customer  on sysaccount.id = customer.accountid left join base_data on base_data.id = customer.levelid" +
	        			" where "+ SqlUtil.getSqlStrByArrays(canAccountIds,"sysaccount.id")+
	        			" group by sysaccount.id ) a"+i+" on sysaccount.id = a"+i+".id ";
	        	i++;
	        }
	        sqlsc +=" lvinfo from sysaccount left join ";
	        sqlsc +=sqllsc;
			return sqlsc;
	      }
	      return "";
	    }catch (Exception e) {
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
	}
	//统计分析
	 public String getBeansBaiFang(){
		 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer();
		String startrime = (String) super.map.get("visitdateGE");//开始时间
		String endtime = (String) super.map.get("visitdateLE");//结束时间
		String accountid = (String)super.map.get("accountidEQ");//业务员id
		String str1 =""; 
		String str2 =""; 
		if(!DataFormat.booleanCheckNull(startrime)){
			str1 +=" and Customer_visit.visitDate >='"+startrime+"' ";
			str2 +=" and cuslvRecord.createtime >='"+startrime+"' ";
		}
		if(!DataFormat.booleanCheckNull(endtime)){
			str1 +=" and Customer_visit.visitDate <='"+endtime+"' ";
			str2 +=" and cuslvRecord.createtime <='"+endtime+"' ";
		}
		
		 sql.append("select a.id,a.name,ISNULL(visit.visitnum,0) visitnum,ISNULL(lvbg.lvbgnum,0) lvbgnum,ISNULL(lvs.lvsnum,0) lvsnum, clvinfo.lvinfo ,  ISNULL(notvisit.nvnum,0) nvnum,ISNULL(pvisit.pvnum,0) pvnum " +
				" from sysaccount a left join (select accountid , count(distinct customerid) visitnum from Customer_visit where 1=1 "+str1+ " group by accountid) visit on a.id = visit.accountid" +
				" left join (select accountid , count(distinct customerid) lvbgnum from cuslvRecord where 1=1 "+str2+" group by accountid) lvbg on a.id = lvbg.accountid " +
				" left join (select Customer_visit.accountId , count(distinct Customer_visit.customerid) lvsnum from Customer_visit left join Customer on Customer_visit.customerid = Customer.id " +
					" where Customer.levelId = Customer_visit.levelId and customerid not in (select distinct customerid from cuslvRecord where 1=1 "+str2+") group by Customer_visit.accountid) lvs on a.id =lvs.accountid " +
				" left join ("+	geteachSQL(startrime,endtime,accountid) +" ) clvinfo on a.id=clvinfo.id "+
				" left join (select Customer.accountid , count(distinct customerid) nvnum from  Customer left join Customer_visit on Customer_visit.customerid = Customer.id " +
					" where Customer.id not in (select distinct customerid from Customer_visit) group by Customer.accountid) notvisit on a.id = notvisit.accountid " +
				" left join (select accountid , count(distinct customerid) pvnum from Customer_visit group by accountid) pvisit on a.id = pvisit.accountid where 1=1");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"a.id"));
        sql.append(" or a.id ='"+selfId+"')");
        if(!"".equals(accountid)&&accountid!=null){
			sql.append("and  a.id="+accountid);
		}
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	 }
	
	 /**
		 * 查看下级和自己的客户级别变动
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchcuslvRecord(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from cuslvRecordView t where 1=1");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.accountid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
	
}
