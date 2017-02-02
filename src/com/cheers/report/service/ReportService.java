package com.cheers.report.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.sp.service.SpService;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReportService extends Dao {
	
	
	public String add(){
		HashMap m = new HashMap();
		this.session = request.getSession();
		DateFormat df = new DateFormat();
		/*m.put("reportdateEQ",  this.data.get("reportdate"));
		m.put("accountidEQ",  ((AccountBean)this.session.getAttribute("accountBean")).getId());
		if("true".equals(checkDuplicate(m))){	//鏍￠獙鏈棩鏃ユ姤鏄惁涓婃姤
			return getReturnString("sorry,鎮ㄥ凡涓婃姤锛�);
		}else {*/
			this.data.put("createtime", df.getNowTime());
			this.data.put("accountid", ((AccountBean)this.session.getAttribute("accountBean")).getId());
			this.data.put("creator", ((AccountBean)this.session.getAttribute("accountBean")).getId());
			String retStr=super.add();
			if(retStr.equals("true")){
				Map sqlMap=new HashMap();
				//sqlMap.put("createTime", now);
//				int i=getMaxId("specialReport", sqlMap);
				int i=getMaxId(String.valueOf(data.get("table")), sqlMap);
				return super.getReturnString(i+"");
			}else return super.getReturnString(retStr);
//		}
	}
	public String seenable(){//查看日报当前日期能否上报
		DataBase db=new DataBase();
		ResultSet rs=null;
		String isenable="";
		
		//校验本日日报是否上报
		String selenable="select *  from WorkReport_Weekday where weekday='"+ getRquireDay(key)+"'";
		try {
			rs= db.queryAll(selenable);
		} catch (DbException e) {
			e.printStackTrace();
		}
		
			try {
				if (rs.next()) {
				isenable=rs.getString("isenable");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(isenable.equals("0")){	
				return getReturnString("2");//不可以上报
			}else{
				return getReturnString("3");//可以上报
			}
	}
	public String update(){
		this.session = request.getSession();
		DateFormat df = new DateFormat();//设置日期格式
		String now=df.getNowTime();
		String accountid=((AccountBean)this.session.getAttribute("accountBean")).getId();
		//处理日志信息中一对多的更新
		List<String> upsqls =  new ArrayList<String>();
		if(this.data.containsKey("testList")){
		    List<Map> list = (List)this.data.get("testList");
		    for (Map map : list) {
		    	if(map.get("id")!=""){
		    		map.put("modifyTime", now);
		    		upsqls.addAll(super.updateSql(map));
		    	}else{
			    	//添加新增选项
		    		map.put("WorkReportId", (Integer)this.data.get("id"));
		    		map.put("createTime", now);
		    		map.put("creatorId", accountid);
		    		upsqls.addAll(addSql(map));
		    	}
		    }
		    data.remove("testList");//去除子表信息
	    }
		this.data.put("modifyTime", now);//
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
	
	public String delete(){
		return getReturnString(super.delete());
	}
	
	public String delete2(){
		try {
		      List donext=new ArrayList();
		      String id=String.valueOf(request.getParameter("id"));
			  String sql1="delete from WorkReport  where id="+id;
			  donext.add(sql1);
			 db=new DataBase();
			 db.updateBatch((String[])donext.toArray(new String[donext.size()]));
			 return getReturnString("true");
		} catch (Exception e) {
			e.printStackTrace();
			return getReturnString("false");
		}
	}
	
	/**
	 * 日报批复
	 * @return
	 */
	public String sp_update(){
		List donext=new ArrayList();
		this.session = request.getSession();
		DateFormat na = new DateFormat();
		String nowdate = na.getNowTime();
		this.data.put("replytime",nowdate);
		this.data.put("replyid", ((AccountBean)session.getAttribute("accountBean")).getId());//审批人的id
		this.data.put("replyname", ((AccountBean)session.getAttribute("accountBean")).getName());//审批人的id
		String retStr=super.add();
		if(retStr.equals("true")){
			String sql1="update WorkReport  set isreply=1  where id="+this.data.get("reportid");
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
			String sql="select top 100 percent * from reportsp where reportid='"+recordid+"' order by replytime " ;
			excuteSQLPageQuery(sql);
			System.out.println("日志审批详情：======="+sql.toString());
			result = getRows();
			return getReturn();
		}
	/**
	 * 个人日报查询列表显示方法
	 * @return
	 */
	public String getBeansSelf(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String id = accountBean.getId();//当前登录人的
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from WorkReports t where 1=1");
		sql.append(" and t.accountid='"+id+"'");
		sql.append(super.getSearchStr(super.map));
		if (!DataFormat.booleanCheckNull(sort))
			sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
		else
			sql .append( " order by id ");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	
	
	/**
     * 个人 特殊申请  查询列表显示方法
     * @return
     */
    public String getBeansSpecial(){ 
        this.session = request.getSession();
        AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
        StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from specialReportView t where 1=1");
       // sql.append(" and t.accountid='"+id+"'");
        
      //=====================================审批=============================
        if(shenpi!=null){
             table="specialReport";
             String spaccountid=((AccountBean)session.getAttribute("accountBean")).getId();
             SpService sps=new SpService();
             ArrayList recordid=sps.getTableIds(table);
             List<DynaBean> canUsps=sps.getSPrecordT(table,recordid,spaccountid);
             sql.append(" and t.id in(0");
             if (canUsps.size()>0) {
                 for (int i = 0; i < canUsps.size(); i++) {
                     sql.append(","+(canUsps.get(i)).get("recordid"));
                 }
             }
             sql.append(")");
         }else{
        	 sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid")+" or t.accountId="+selfId+")");
         }
       //=====================================审批=============================   
        
        sql.append(super.getSearchStr(super.map));
        if (!DataFormat.booleanCheckNull(sort))
            sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
        else
            sql .append( " order by id ");
        System.out.println("sql:"+sql);
        excuteSQLPageQuery(sql.toString());
        result = getRows();
        return getReturn();
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
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from WorkReports t where 1=1");
		sql.append(" and "+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
		sql.append(" and t.accountid !='"+selfId+"'");
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
	 * 日志查询列表显示方法
	 * @return
	 */
	public String getBeansSearch(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from WorkReports t where 1=1");
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
	 * 日志查询首页显示
	 * @return
	 */
	public String getHomePageReport(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 10  t.typename,convert(varchar(10),t.reportdate,121) reportdate,");  
		sql.append(" t.accountname,case when t.isreply=1 then '已批复' else '未批复' end isreply,convert(varchar(10),t.createtime,121) createtime  from WorkReports t where 1=1");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
		sql.append(" or t.accountid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		if (!DataFormat.booleanCheckNull(sort))
			sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
		else
			sql .append( " order by createtime desc");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	
	public String getSpecifiedDayBefore(String specifiedDay, int days)
	  {
	    Calendar c = Calendar.getInstance();
	    Date date = null;
	    try {
	      date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    c.setTime(date);
	    int day = c.get(5);
	    c.set(5, day - days);

	    String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
	      .getTime());
	    return dayBefore;
	  }

	/**
	 * 日志统计列表显示
	 * @return
	 */
	public String getReportStat(){
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		SqlUtil sqlUtil = new SqlUtil();
		String _canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String starttime = (String) this.data.get("starttime");//开始时间
		String endtime = (String) this.data.get("endtime");//结束时间
		String name = (String) this.data.get("account_name");
		DateFormat date = new DateFormat();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String now=df.format(new Date());
		String sql="";
		if("".equals(starttime)&&"".equals(endtime)){
			String beforeday = getSpecifiedDayBefore(now, 1);
			sql+="select a.name ,1 as yingbao ,shibao.shibao as shibao,1-ISNULL(shibao.shibao, 0) as quebao  from workreportinfo d \n" +
					"right join SYSACCOUNT a on a.id=d.accountId\n" +
					"left join (" +
					"	select count(DISTINCT(reportdate)) as shibao ,da.accountId from workreportinfo da \n" +
					"where da.reportdate>='"+beforeday+"' and da.reportdate<='"+beforeday+"'\n" +
					"GROUP BY da.accountId\n" +
					") shibao on shibao.accountId=d.accountId  where 1=1 \n" ;
		}else{
			String chazhi = getRquireDay(starttime, endtime);//应报天数
			sql += "select a.name ,"+chazhi+" as yingbao ,shibao.shibao as shibao,"+chazhi+"-ISNULL(shibao.shibao, 0) as quebao  from workreportinfo d \n" +
					"right join SYSACCOUNT a on a.id=d.accountId\n" +
					"left join (" +
					"	select 	count(DISTINCT(reportdate)) as shibao ,da.accountId from workreportinfo da \n" +
					"where da.reportdate>='"+starttime+"' and da.reportdate<='"+endtime+"'\n" +
					"GROUP BY da.accountId\n" +
					") shibao on shibao.accountId=d.accountId  where 1=1\n" ;
		}
		if(!"".equals(name)&&name!=null){
			sql+="and  a.name like'%"+name+"%'";
		}
		sql+=" and (" +sqlUtil.getSqlStrByArrays(_canAccountIds,"a.id")+" or d.accountId='"+accountBean.getId()+"')";
		sql+=" GROUP BY a.name ,shibao.shibao";
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	
	/**
	 * 获取指定日期是周几
	 * */
	public String getRquireDay(String dt){
		
		DateFormat dft = new DateFormat();
		return dft.getweekday(dt);
		
	}
	/**  
	* 根据开始时间和结束时间返回时间段内的时间集合  
	* @param beginDate  
	* @param endDate  
	* @return List  
	 * @throws ParseException 
	*/  
	public static List getDatesBetweenTwoDate(Date beginDate, Date endDate) {  
		List lDate = new ArrayList();  
		lDate.add(beginDate);//把开始时间加入集合  
		Calendar cal = Calendar.getInstance();  
		//使用给定的 Date 设置此 Calendar 的时间  
		cal.setTime(beginDate);  
		boolean bContinue = true;  
		while (bContinue) {  
			//根据日历的规则，为给定的日历字段添加或减去指定的时间量  
			cal.add(Calendar.DAY_OF_MONTH, 1);  
			// 测试此日期是否在指定日期之后  
			if (endDate.after(cal.getTime())) {  
			lDate.add(cal.getTime());  
			} else {  
			break;  
			}  
		}
		if (!lDate.contains(endDate)){
			lDate.add(endDate);//把结束时间加入集合  
		}
		return lDate;  
	}  
	
	
	/*
	 * 获取时间段内的应报天数
	 * */
public String getRquireDay(String start,String end){
	int num = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	Date beginDate = null;
	try {
		beginDate = sdf.parse(start);
	} catch (ParseException e) {
		e.printStackTrace();
	} 
	Date endDate = null;
	try {
		endDate = sdf.parse(end);
	} catch (ParseException e) {
		e.printStackTrace();
	} 
	if (beginDate != null && endDate != null){
	//获取指定日期内的日期
	List<Date> listDate = getDatesBetweenTwoDate(beginDate, endDate); 
		for(Date date:listDate){ 
			String weekday = getRquireDay(sdf.format(date));
			HashMap m = new HashMap();
			m.put("table", "WorkReport_Weekday");
			m.put("weekdayEQ", weekday);
			m.put("isenableEQ", "1");
			if("true".equals(checkDuplicate(m))){	//校验本日日报是否上报
				num++;
			}
		} 
	System.out.println("本段时间内应报"+num); 
	return String.valueOf(num);
	}
	return "";
	} 

	
}
