package com.cheers.goal.service;


import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.DataFormat;


public class goalService extends Dao {
	
	public String add(){
		String flag="";
		List<String> upsql=new ArrayList<String>();
		DataBase db=new DataBase();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String now=df.format(new Date());
		List<Object> proList = (List<Object>) data.get("productList");//获取到目标对象集合
		for (int i = 0; i <proList.size(); i++) {
			//集合中存放的为LinkedHashMap对象，每次循环取出一个
			LinkedHashMap<String, String> maps = (LinkedHashMap<String, String>) proList.get(i);
			String sql = "insert into goal" +
					"(year,month,orgid,ywyid,salemoney,memo,creatorid,createtime,isenable) " +
					"values("+this.data.get("year")+","+ this.data.get("month")+","+ this.data.get("orgid")+","+ maps.get("ywyid")+","+ maps.get("salemoney")+",'"+ maps.get("memo")+"',"+((AccountBean)this.session.getAttribute("accountBean")).getId()+",'"+now+"',1)";
			upsql.add(sql);
		}
		try{
			db.updateBatch(upsql.toArray(new String[upsql.size()]));
			flag="true";
		}catch(Exception e){
			e.printStackTrace();
			flag="false";
		}finally{
			try {
				db.close();
				flag="true";
			} catch (DbException e) {
				e.printStackTrace();
				flag="false";
			}
		}
		
		 return super.getReturnString(flag);
	}
	public String update(){
		String flag="";
		List<String> upsql=new ArrayList<String>();
		DataBase db=new DataBase();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String now=df.format(new Date());
		List<Object> proList = (List<Object>) data.get("productList");//获取到目标对象集合
		
		for (int i = 0; i <proList.size(); i++) {
			//集合中存放的为LinkedHashMap对象，每次循环取出一个
			HashMap<String, String> maps = (HashMap<String, String>) proList.get(i);
			Object salemoney ;
			salemoney = maps.get("salemoney");
			String sql = "update goal set year="+this.data.get("year")+",month="+ this.data.get("month")+",orgid="+ this.data.get("orgid")+"," +
			        " ywyid="+ maps.get("ywyid")+",salemoney='"+salemoney+"',memo='"+ maps.get("memo")+"',modifyid="+((AccountBean)this.session.getAttribute("accountBean")).getId()+",modifytime='"+now+"',isenable=1 where id="+this.data.get("id");
			upsql.add(sql);
		}
		try{
			db.updateBatch(upsql.toArray(new String[upsql.size()]));
			flag="true";
		}catch(Exception e){
			e.printStackTrace();
			flag="false";
		}finally{
			try {
				db.close();
				flag="true";
			} catch (DbException e) {
				e.printStackTrace();
				flag="false";
			}
		}
		
		 return super.getReturnString(flag);
	}
	
	public String delete(){
		return getReturnString(super.delete());
	}
	
	public String updatefieldByIds(){
		return getReturnString(super.updatefieldByIds());
	}
			
	/**
	 *业务员月度目标管理显示方法 
	 * 
	 * @return
	 */
	public String getBeansSelf(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from goalView t where 1=1");
//		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.creatorid"));
        sql.append(" and t.creatorid ='"+selfId+"'");
		sql.append(super.getSearchStr(super.map));
		sql .append( " order by id");
		System.out.println("sql业务员月度目标管理--------:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}	
	
	/**
	 *业务员月度目标查看 
	 * 
	 * @return
	 */
	public String getBeansgoallook(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from goalView t where 1=1");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.ywyid"));
        sql.append(" or t.ywyid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		sql .append( " order by id");
		System.out.println("sql业务员月度目标查看 --------:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}	
	/**
	 *业务员月度目标达成率
	 * 
	 * @return
	 */
	public String getBeansgoalywydcl(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent * from (select  y.year,y.month,y.ywyid,y.ywyname,y.orgname,y.salemoney,y1.ordermoney,");
		sql .append( " (case when y.salemoney=0 then 0 ELSE y1.ordermoney/y.salemoney END) dcl,y.orgid,y.memo");
		sql .append( " from ");
		sql .append( " (select g.year year,g.month month,g.ywyid ,sysACCOUNT.name ywyname,g.orgid,sum(ISNULL(g.salemoney, 0)) salemoney,g.memo,SYSTEM_TREE.name orgname");
		sql .append( " from goal g");
		sql .append( " left join sysACCOUNT on sysACCOUNT.id=g.ywyid left join SYSTEM_TREE on SYSTEM_TREE.id=g.orgid where g.isenable=1");
		sql .append( " GROUP BY g.ywyid,g.[year],g.[month],sysACCOUNT.name,g.orgid,g.memo,SYSTEM_TREE.name ) y,");
		sql .append( " (select  o.accountid,convert(varchar,datepart(year,orderdate)) YEAR,convert(varchar,datepart(month,orderdate)) month, sum(ISNULL(o.ordermoney, 0)) orderMoney,o.orgid orgid");
		sql .append( " from Orders o ");
		sql .append( " where o.isenable=1 and o.spstate=1 ");//加审批条件
		sql .append( " group by o.accountid,convert(varchar,datepart(year,orderdate)),convert(varchar,datepart(month,orderdate)),orgid) y1");
		sql .append( " where y.year=y1.year and y.ywyid=y1.accountid and y.month=y1.month and y.orgid=y1.orgid");
		sql.append("  and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"y.ywyid"));
        sql.append(" or y.ywyid ='"+selfId+"'))");
        sql.append(" m where 1=1 ");
		sql.append(super.getSearchStr(super.map));
		sql .append( " order by ywyid");
		System.out.println("sql业务员月度目标查看 --------:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}	
	/**
	 *部门月度目标汇总/达成率
	 * 
	 * @return
	 */
	public String getBeansgoalbmdcl(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent *,(case when salemoney=0 then 0 ELSE ordermoney/salemoney END) dcl from (select  y.year,y.month,y.orgname,sum(ISNULL(y.salemoney,0)) salemoney,sum(ISNULL(y1.ordermoney,0)) orderMoney,");
		sql .append( " y.orgid");
		sql .append( " from ");
		sql .append( " (select g.ywyid,g.year year,g.month month,g.orgid,sum(ISNULL(g.salemoney, 0)) salemoney,SYSTEM_TREE.name orgname");
		sql .append( " from goal g");
		sql .append( " left join SYSTEM_TREE on SYSTEM_TREE.id=g.orgid where g.isenable=1");
		sql .append( " GROUP BY g.ywyid,g.[year],g.[month],g.orgid,SYSTEM_TREE.name ) y,");
		sql .append( " (select  o.accountid,convert(varchar,datepart(year,orderdate)) YEAR,convert(varchar,datepart(month,orderdate)) month, sum(ISNULL(o.ordermoney, 0)) orderMoney,o.orgid orgid");
		sql .append( " from Orders o ");
		sql .append( " where o.isenable=1 and o.spstate=1 ");//加审批条件
		sql .append( " group by o.accountid,convert(varchar,datepart(year,orderdate)),convert(varchar,datepart(month,orderdate)),orgid) y1");
		sql .append( " where y.year=y1.year  and y.month=y1.month and y.orgid=y1.orgid  and y.ywyid=y1.accountid ");
		sql.append("  and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"y.ywyid"));
        sql.append(" or y.ywyid ='"+selfId+"')");
		sql.append(" group by y.[year],y.[month],y.orgid,y.orgname");
		sql.append(" )");
        sql.append(" m where 1=1 ");
		sql.append(super.getSearchStr(super.map));
		sql .append( " order by orgid");
		System.out.println("sql部门业务员月度目标查看 --------:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}		
}
