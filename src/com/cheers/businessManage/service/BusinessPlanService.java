package com.cheers.businessManage.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;
import com.cheers.sp.service.SpService;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;
import com.fasterxml.jackson.core.JsonProcessingException;

public class BusinessPlanService extends Dao {
	
	public String add(){
		Map m = new HashMap();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String startTimestr = (String) this.data.get("startTime");
		String endTimestr = (String) this.data.get("endTime");
	    Date startTimedate=new Date();
	    Date endTimedate=new Date();
		try {
			startTimedate = format.parse(startTimestr);
			endTimedate = format.parse(endTimestr);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		//获取当前月第一天：
        Calendar c = Calendar.getInstance();   
        c.setTime(startTimedate);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String first = format.format(c.getTime());
        System.out.println("===============first:"+first);
        
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();    
        ca.setTime(endTimedate);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        String last = format.format(ca.getTime());
        System.out.println("===============last:"+last);
		m.put("table", this.data.get("table"));
		m.put("startTimeGE",first );
		m.put("endTimeLE",last );
		m.put("accountIdEQ", this.data.get("accountId"));//业务员一个月只能上报一次
		if("true".equals(checkDuplicate(m))){
			return getReturnString("sorry,业务员一个月只能上报一次出差计划！");
		}else {
			this.session = request.getSession();
			DateFormat df = new DateFormat();//设置日期格式
			String now=df.getNowTime();
			int ts=0;
			List<Object> proList = (List<Object>) data.get("productList");//获取到出差计划集合
			List<Object> newList = new ArrayList<Object>();//创建一个新的集合存放新的出差计划数据
			//循环出差计划集合集合，将创建时间创建人，最后一次修改时间，最后一次修改人，存放到集合中
			for (int i = 0; i <proList.size(); i++) {
				//集合中存放的为LinkedHashMap对象，每次循环取出一个
				LinkedHashMap<String, String> maps = (LinkedHashMap<String, String>) proList.get(i);
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
		    
			String starttime=(String)this.data.get("startTime");
			String endtime=(String)this.data.get("endTime");
			String accountId=(String)this.data.get("accountId");
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		    long to;
		    long from;
			try {
				to = df2.parse(endtime).getTime();
			    from = df2.parse(starttime).getTime();
			    ts=(int) ((to - from) / (1000 * 60 * 60 * 24));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    Calendar cal = Calendar.getInstance();
			List<Object> rqnewList = new ArrayList<Object>();//创建一个新的集合存放新的出差计划数据
			LinkedHashMap<String, String> maps2 ;
			//循环出差计划集合集合，将创建时间创建人，最后一次修改时间，最后一次修改人，存放到集合中
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			ResultSet rs2=null;
            DataBase dbs = new DataBase();
			
			List donext=new ArrayList();
			List<Map> lm = new ArrayList();
			
//			for (int j = 0; j <=ts; j++) {
//				maps2 =new LinkedHashMap<String, String>();
//				String rqtime="";
//				try {
//					 cal.setTime(sdf.parse(starttime));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				cal.add(Calendar.DATE, j);
//				rqtime = sdf.format(cal.getTime());//往后加一天
//				
//				try {
//				    dbs.connectDb();
//	                String sqlquery = "select  id  from businessplan_kq where accountid = '"+ accountId +"' and  rqtime ='"+ rqtime +"'  and  starttime >= '"+ starttime +"' and endtime <= '"+ endtime +"' ";
//	                rs2=dbs.queryAll(sqlquery);
//	            } catch (DbException e1) {
//	                // TODO Auto-generated catch block
//	                e1.printStackTrace();
//	            }
//				String sql1 = "";
//				try {
//                    if((rs2!=null)&&(rs2.next())){
//                            String i =rs2.getString("id");
//                            Map sMap=new HashMap();
//                            sMap.put("starttime", starttime);
//                            sMap.put("endtime", endtime);
//                            sMap.put("id", i);
//                            lm.add(sMap);
//                    }else{
//                        maps2.put("table", "businessplan_kq");
//                        maps2.put("mainTable", "businessplan");
//                        maps2.put("fk", "businessplanid");
//                        maps2.put("rqtime", rqtime);
//                        maps2.put("starttime", starttime);
//                        maps2.put("endtime", endtime);
//                        maps2.put("accountid", accountId);
//                        rqnewList.add(maps2);//将新的map数据放入新的集合中
//                    }
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//			}
			
			//根据key删除之前的list集合
//			data.remove("rqList");
			//放入新的list集合
//			data.put("rqList", rqnewList);
			String retStr = super.add();
			
//			Map sqlMap2=new HashMap();
//            sqlMap2.put("createTime", now);
//			int maxidvalue = this.getMaxId("businessplan",sqlMap2);
//			
//			for(int a=0;a<lm.size();a++) {
//			   String starttime2 = (String) lm.get(a).get("starttime");
//			   String endtime2= (String) lm.get(a).get("endtime");
//			   String id2 = (String) lm.get(a).get("id");
//			   
//			   String sql1="update businessplan_kq  set  starttime='"+ starttime2 +"' , endtime ='"+ endtime2 +"', businessplanid ='"+ maxidvalue +"'  where id='"+ id2 +"'";
//               donext.add(sql1);
//			}
//			
//			try {
//                dbs.updateBatch((String[])donext.toArray(new String[donext.size()]));
//            } catch (DbException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }finally{
//                try {
//                    dbs.close();
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
//            }
			

		//***********************************************************
			if(retStr.equals("true")){
				Map sqlMap=new HashMap();
				sqlMap.put("createTime", now);
				int i=getMaxId((String)this.data.get("table"), sqlMap);
				return super.getReturnString(i+"");
			}else return super.getReturnString(retStr);
			}
	}
	
	public String update2(){
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		DateFormat dfd = new DateFormat();//设置日期格式
		String now=dfd.getNowTime();
		this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		this.data.put("modifierId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		this.data.put("modifyTime", now);
		this.data.put("createTime", now);
		return getReturnString(super.update());
	}
	public String update(){
		Map m = new HashMap();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String startTimestr = (String) this.data.get("startTime");
		String endTimestr = (String) this.data.get("endTime");
		String oldstartTime = (String) this.data.get("oldstartTime");
        String oldendTime = (String) this.data.get("oldendTime");
	    Date startTimedate=new Date();
	    Date endTimedate=new Date();
	    Date oldstartTimedate=new Date();
        Date oldendTimedate=new Date();
		try {
			startTimedate = format.parse(startTimestr);
			endTimedate = format.parse(endTimestr);
			oldstartTimedate = format.parse(oldstartTime);
            oldendTimedate = format.parse(oldendTime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		  //获取当前月第一天：
        Calendar c = Calendar.getInstance();   
        c.setTime(startTimedate);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String first = format.format(c.getTime());
        System.out.println("===============first:"+first);
        
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();    
        ca.setTime(endTimedate);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        String last = format.format(ca.getTime());
        System.out.println("===============last:"+last);
		m.put("table", this.data.get("table"));
		m.put("startTimeGE",first );
		m.put("endTimeLE",last );
		m.put("accountIdEQ", this.data.get("accountId"));//业务员一个月只能上报一次
		

		StringBuffer sql = new StringBuffer();
		sql.append("select top 100 percent * ");
		sql.substring(0, sql.length() - 1);
		sql.append(" from ");
		sql.append(DataFormat.objectCheckNull(this.data.get("table")) + DataFormat.objectCheckNull(map.get("view")));
		sql.append(" where 1=1 and id<>"+this.data.get("id"));
		// 遍历参数
		sql.append(getSearchStr(m));
		PageBean bean = new MsSqlPageBean();
		rs = bean.listData(sql.toString(), null).getCachedRowSet();
		result = getRows();
			if (result != null && result.size()>0){
			return getReturnString("sorry,业务员一个月只能上报一次出差计划！");
		}else {
		    
		int ts=0;
		String starttime=(String)this.data.get("startTime");
		String endtime=(String)this.data.get("endTime");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
	    long to;
	    long from;
		try {
			to = df2.parse(endtime).getTime();
		    from = df2.parse(starttime).getTime();
		    ts=(int) ((to - from) / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    Calendar cal = Calendar.getInstance();
		List<Object> rqnewList = new ArrayList<Object>();//创建一个新的集合存放新的出差计划数据
		LinkedHashMap<String, String> maps2 ;
		//循环出差计划集合集合，将创建时间创建人，最后一次修改时间，最后一次修改人，存放到集合中
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
		
		List donext=new ArrayList();
		
//		for (int j = 0; j <=ts; j++) {
//			maps2 =new LinkedHashMap<String, String>();
//			String rqtime="";
//			try {
//				 cal.setTime(sdf.parse(starttime));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			cal.add(Calendar.DATE, j);
//			rqtime = sdf.format(cal.getTime());//往后加一天
//			
//			try {
//                if((format.parse(rqtime).getTime() >= oldstartTimedate.getTime()) && (format.parse(rqtime).getTime() <= oldendTimedate.getTime())) {//在 已上报的 时间段内   用 update 跟新
//                    String sql1="update businessplan_kq  set  starttime='"+ starttime +"' , endtime ='"+ endtime +"'  where  DATEDIFF (day,'"+rqtime+"',rqtime) = '0'  and businessplanid='"+Integer.parseInt(this.data.get("id").toString())+"'";
//                    donext.add(sql1);
//                }else {// 还未上报过的 日期  用 insert  
//                    String sql2="insert into  " +
//                    		" businessplan_kq (accountid,businessplanid,starttime,endtime,rqtime) " +
//                    		" values('"+ Integer.parseInt(this.data.get("accountId").toString()) +"','"+ Integer.parseInt(this.data.get("id").toString()) +"','"+ starttime +"','"+ endtime +"','"+ rqtime +"')";
//                    donext.add(sql2);
//                }
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
////			maps2.put("table", "businessplan_kq");
////			maps2.put("mainTable", "businessplan");
////			maps2.put("fk", "businessplanid");
////			maps2.put("accountid", (String) this.data.get("accountId"));
////			maps2.put("rqtime", rqtime);
////			maps2.put("starttime", starttime);
////			maps2.put("endtime", endtime);
////			rqnewList.add(maps2);//将新的map数据放入新的集合中
//		}
		//根据key删除之前的list集合
		//data.remove("rqList");
		//放入新的list集合
		//data.put("rqList", rqnewList);
		this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		
//        try {
//            db=new DataBase();
//            db.updateBatch((String[])donext.toArray(new String[donext.size()]));
//        } catch (DbException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
		
//		DataBase db = new DataBase();
//		ResultSet rs;
//		rs = db.queryAll("select * from businessplan where id = '"+ this.data.get(id) +"'");
		
		return getReturnString(super.update());
		
		}
	}
	

	public String delete(){
		return getReturnString(super.delete());
	}
	
	public String deleteByIds(){
	    DataBase db = new DataBase();
	    try {
            db.connectDb();
            List<String> sqls = super.deleteSql(data);
            db.updateBatch((String[]) sqls.toArray(new String[sqls.size()]));
            
            List result1 = new ArrayList();
             
            //遍历  选中的  所有 主表 的  id 
             if ((this.data.get("id") instanceof String)) {
                 StringBuffer delesql = new StringBuffer();
                 delesql.append("delete from  businessplan_kq ");
                 delesql.append(" where businessplanid='"+ this.data.get("id") +"'");
                 delesql.append(this.data.get("id"));
                 delesql.append(" and signInTime is null and signOutTime is null ");
                 
                 result1.add(delesql.toString());
               } 
               else if ((this.data.get("id") instanceof ArrayList))
               {
                 List ids = (List)this.data.get("id");
                 for (Iterator localIterator = ids.iterator(); localIterator.hasNext(); ) { 
                     Object str = localIterator.next();
                     StringBuffer delesql = new StringBuffer();
                     delesql.append("delete from  businessplan_kq ");
                     delesql.append(" where businessplanid='"+ str +"'");
                     delesql.append(" and signInTime is null and signOutTime is null ");
                     
                     result1.add(delesql.toString());
                 }
               }
            
             db.updateBatch((String[]) result1.toArray(new String[result1.size()]));
             
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	    return "true";
	}
   
	public String updatefieldByIds(){
		return getReturnString(super.updatefieldByIds());
	}

	 public String getBeansHKQX(){
//			String q=request.getParameter("q");
		    String q = this.QUESTION;
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canClientIds();//自己可以看到的客户信息
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			String sql ="select distinct top 100 percent c.id value,c.name text from customer c \n" +
					"where 1=1 and c.enable=1 ";
			if(q!=null&&!"".equals(q)){
				 sql+=" and  c.name like '%"+q+"%' ";
			 }
			sql+=" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"c.id")+" or c.accountId="+selfId+")";
			 if (!DataFormat.booleanCheckNull(sort))
					sql+= " order by " + sort + " " + DataFormat.objectCheckNull(order);
				else
					sql += " order by c.id";
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
		}
	 
	 /**
		 * 查看下级和自己的出差计划信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearch(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from businessplanView t where 1=1");
			
	        //=====================================审批=============================
	        if(shenpi!=null){
	             table="businessplan";
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
	         }
	       //=====================================审批=============================
	        else{
				sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountId"));
		        sql.append(" or t.creatorid ='"+selfId+"')");
	        }
	        
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by t.createtime desc ");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
	 
	 /**
		 * 查看下级和自己的出差计划信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchQvYu(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from businessplanView t where 1=1 and t.typeid=2 ");
			
	        //=====================================审批=============================
	        if(shenpi!=null){
	             table="businessplan";
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
	         }
	       //=====================================审批=============================
	        else{
				sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountId"));
		        sql.append(" or t.creatorid ='"+selfId+"')");
	        }
	        
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by t.createtime desc ");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		
		
	 
	 /**
		 * 查看下级和自己的出差计划信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchDaQv(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from businessplanView t where 1=1 and t.typeid=1 ");
			
	        //=====================================审批=============================
	        if(shenpi!=null){
	             table="businessplan";
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
	         }
	       //=====================================审批=============================
	        else{
				sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountId"));
		        sql.append(" or t.creatorid ='"+selfId+"')");
	        }
	        
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by t.createtime desc ");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		public String getBeans(){
			 return  super.getBeans();
		 }
		
		 /**
		 * 查看下级和自己的出差总结
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchZj(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from businesssummaryView t where 1=1");
			
			//=====================================审批=============================
	        if(shenpi!=null){
	             table="businesssummary";
	             String spaccountid=((AccountBean)session.getAttribute("accountBean")).getId();
	             SpService sps=new SpService();
	             ArrayList recordid=sps.getTableIds(table);
	             List<DynaBean> canUsps=sps.getSPrecord(table,recordid,spaccountid);
	             sql.append(" and t.id in(0");
	             if (canUsps.size()>0) {
	                 for (int i = 0; i < canUsps.size(); i++) {
	                     sql.append(","+(canUsps.get(i)).get("recordid"));
	                 }
	             }
	             sql.append(")");
	         }
	       //=====================================审批=============================
	        else {
	        	sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountId"));
		        sql.append(" or t.creatorid ='"+selfId+"')");
	        }

			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		
		/*
		 * 领导出差总结管理--》页面查询
		 * 2014年10月21日14:49:53 sun_jianfeng
		 */
		public String getLDBeansSearchZj(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from ldbusinesssummaryView t where 1=1");
			
			//=====================================审批=============================
	        if(shenpi!=null){
	             table="businesssummary";
	             String spaccountid=((AccountBean)session.getAttribute("accountBean")).getId();
	             SpService sps=new SpService();
	             ArrayList recordid=sps.getTableIds(table);
	             List<DynaBean> canUsps=sps.getSPrecord(table,recordid,spaccountid);
	             sql.append(" and t.id in(0");
	             if (canUsps.size()>0) {
	                 for (int i = 0; i < canUsps.size(); i++) {
	                     sql.append(","+(canUsps.get(i)).get("recordid"));
	                 }
	             }
	             sql.append(")");
	         }
	       //=====================================审批=============================
	        else {
	        	sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountId"));
		        sql.append(" or t.creatorid ='"+selfId+"')");
	        }

			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			//System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		
		public String addsummary(){
			Map m = new HashMap();
			DateFormat df = new DateFormat();//设置日期格式
			String now=df.getNowTime();
			m.put("table", this.data.get("table"));
			this.data.put("createTime", now);
			this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
			this.data.put("modifyTime", now);
			this.data.put("modifierId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
				return getReturnString(super.add());
				}
		
		//签到管理
		public String getBusinessPlantkq(){
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			
			String signintimeGE = (String) this.map.get("signintimeGE");//开始时间
	        String signintimeLE = (String) this.map.get("signintimeLE");//结束时间
	        String orgIdEQ = (String) this.map.get("orgIdEQ");//部门
	        String accountIdEQ = (String) this.map.get("accountIdEQ");//业务员
			
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sqlstr=new StringBuffer();
			sqlstr.append("select  * from (" +
					"select top 100 percent " +
					"bkq.id as id," +
					"bkq.id as id2," +
					"bkq.outmemo as memo," +
					"s.name orgname," +
					"s.id orgid," +
					"c.id accountid," +
					"c.name accountname," +
					"bkq.starttime starttime," +
					"bkq.signintime createtime," +
					"bkq.endtime endtime," +
					"bkq.isvisited," +
					//"CONVERT(VARCHAR(10),bkq.createTime,121) createTime," +
					"bkq.rqtime rqtime," +
					"bkq.signintime," +
					"bkq.signouttime," +
					"DATEDIFF(minute, bkq.signintime, bkq.signouttime) alltime  " +
					"from  " +
					"businessplan b " +
					"left join businessplan_kq bkq on b.id=bkq.businessplanid" +
					"  left join sysaccount c on c.id=b.accountid left join system_tree s on b.orgid=s.id where 1=1 and bkq.signInTime IS not null ");
					
			//sqlstr.append(super.getSearchStr(super.map));
			        
			        
			        if (!DataFormat.booleanCheckNull(sort))
					sqlstr.append(" order by " + sort + " " + DataFormat.objectCheckNull(order));
					else
					sqlstr.append(" ORDER BY  bkq.signintime DESC,bkq.accountid DESC  ");
			        
					sqlstr.append(" ) a where 1=1  ");
					
			sqlstr.append(" and ("+SqlUtil.getSqlStrByArrays(canAccountIds,"accountid"));
			sqlstr.append(" or accountid='"+selfId+"') ");
			
			if(!"".equals(signintimeGE) && signintimeGE != null){
			    sqlstr.append(" AND a.signintime >= '"+ signintimeGE +"' ");
	        }
			if(!"".equals(signintimeLE) && signintimeLE != null){
			    sqlstr.append(" AND a.signintime <= '"+ signintimeLE +"'  "); 
            }
			if(!"".equals(orgIdEQ) && orgIdEQ != null){
                sqlstr.append(" AND a.orgid = '"+ orgIdEQ +"'  "); 
            }
			if(!"".equals(accountIdEQ) && accountIdEQ != null){
                sqlstr.append(" AND a.accountid = '"+ accountIdEQ +"'  "); 
            }
			
			System.out.println(" ---------------------------------------- "+sqlstr);
			
//			sql+=getSearchStr(map);
			excuteSQLPageQuery(sqlstr.toString());
			result = getRows();
//			return super.getBeansBySql(sqlstr.toString());
			return getReturn();
		}

		//在岗情况统计
		public String getBusinessPlanttj(){
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sqlstr=new StringBuffer();
			sqlstr.append("SELECT\n" +
			        "   *\n" +
			        "FROM\n" +
			        "   (\n" +
			        "       SELECT\n" +
			        "           ccts.accountid,\n" +
			        "           ccts.accountname," +
			        "           ccts.starttime,\n" +
			        "           ccts.endtime," +
			        "           ccts.orgid,\r\n" + 
			        "           st.name," +
			        "           ((convert(varchar,starttime,23))+' / '+(convert(varchar,endtime,23))) as dateroute,\n" +
			        "           ISNULL(ccts.jhccts, 0) AS jhccts,\n" +
			        "           ISNULL(c2.sjccts, 0) AS sjccts,\n" +
			        "           ISNULL(c3.sjqdts, 0) AS sjqdts,\n" +
			        "           ISNULL(c4.sjqtts, 0) AS sjqtts\n" +
			        "       FROM\n" +
			        "           (\n" +
			        "               SELECT DISTINCT\n" +
			        "                   b.id,\n" +
			        "                   sa.name AS accountname,\n" +
			        "                   sa.id AS accountid," +
			        "                   b.orgid,\n" +
			        "                   b.endTime,\n" +
			        "                   b.startTime,\n" +
			        "                   DATEDIFF(dd, b.starttime, b.endtime) + 1 AS jhccts\n" +
			        "               FROM\n" +
			        "                   businessplan b\n" +
			        "               LEFT JOIN sysACCOUNT sa ON b.accountId = sa.id\n" +
			        "           ) ccts\n" +
			        "       LEFT JOIN (\n" +
			        "           SELECT\n" +
			        "               COUNT (*) AS sjccts,\n" +
			        "               kq.businessplanid \n" +
			        "           FROM\n" +
			        "               businessplan_kq kq\n" +
			        "           GROUP BY\n" +
			        "               kq.businessplanid \n" +
			        "       ) c2 ON ccts.id = c2.businessplanid\n" +
			        "       LEFT JOIN (\n" +
			        "           SELECT\n" +
			        "               COUNT (kq.signInTime) AS sjqdts,\n" +
			        "               kq.businessplanid businessplanid \n" +
			        "           FROM\n" +
			        "               businessplan_kq kq\n" +
			        "           GROUP BY\n" +
			        "               kq.businessplanid\n" +
			        "       ) c3 ON ccts.id = c3.businessplanid\n" +
			        "       LEFT JOIN (\n" +
			        "           SELECT\n" +
			        "               COUNT (kq.signOutTime) AS sjqtts,\n" +
			        "               kq.businessplanid businessplanid\n" +
			        "           FROM\n" +
			        "               businessplan_kq kq\n" +
			        "           GROUP BY\n" +
			        "               kq.businessplanid \n" +
			        "       ) c4 ON ccts.id = c4.businessplanid\n" +
			        "       LEFT JOIN SYSTEM_TREE st ON st.id = ccts.orgid  " +
			        "   ) zb\n" +
			        "WHERE\n" +
			        "   1 = 1 ");
			sqlstr.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"accountid"));
			sqlstr.append(" or accountid='"+selfId+"')");
	        sqlstr.append(super.getSearchStr(super.map));
	        
	        //System.out.println("*************************************************"+sqlstr);
	        
			return super.getBeansBySql(sqlstr.toString());
		}
			
		 /**
		  * 根据出差人获取出差人所在的部门
		  */
		 public String getBeansAccountOrg(){
			 String sql ="select top 100 percent s.name,s.id from SYSTEM_TREE s\n" +
					 "join sysaccount_post a on s.id=a.orgid\n" +
					 " where a.accountid='"+this.data.get("idEQ")+"'";
			 if (!DataFormat.booleanCheckNull(sort))
					sql+= " order by " + sort + " " + DataFormat.objectCheckNull(order);
				else
					sql += " order by a.id";
			 excuteSQLPageQuery(sql.toString());
			 System.out.println("查询出差人部门======="+sql.toString());
				result = getRows();
				return getReturn();
		 }
		 public String getsearchQYType(){
				StringBuffer sql = new StringBuffer("select top 100 percent id id ,name name FROM system_tree c where  categoryType=1 ");
				sql.append(" and notetype=1  and parentid='"+this.data.get("parentidEQ")+"' ");
				sql.append(" order by vorder");
				System.out.println("部门联动==============="+sql.toString());
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
			}
			//手机端获取业务员 签到日期
			public String getBusinessPlants(){
				StringBuffer sqlstr=new StringBuffer();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				  //获取当前月第一天：
		        Calendar c = Calendar.getInstance();   
		        c.add(Calendar.MONTH, 0);
		        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		        String first = format.format(c.getTime());
		        System.out.println("===============first:"+first);
		        
		        //获取当前月最后一天
		        Calendar ca = Calendar.getInstance();    
		        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
		        String last = format.format(ca.getTime());
		        System.out.println("===============last:"+last);
		        sqlstr.append("select top 100 " +
		        		"percent b.id id," +
		        		"c.id accountid," +
		        		"c.name accountname," +
		        		"b.starttime starttime," +
		        		"b.endtime endtime," +
		        		"b.isvisited," +
						"b.rqTime,b.signintime," +
						"b.signouttime,DATEDIFF(dd, b.starttime, b.endtime) ts  " +
						"from " +
						"sysaccount c,businessplan_kq b left join businessplan bp " +
						" on bp.id = b.businessplanid  " +
						"  where    " +
						"c.id=b.accountid " +
						" and  b.accountid="+((AccountBean)session.getAttribute("accountBean")).getId());
		        sqlstr.append(" and CONVERT(varchar(10), b.rqtime, 23) <='"+last+"'");
		        sqlstr.append(" and CONVERT(varchar(10), b.rqtime, 23) >='"+first+"'");
				return super.getBeansBySql(sqlstr.toString());
			}
		    //手机端 签到
		   public String signIn(){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					String now=df.format(new Date());
					this.data.put("signInTime", now);
					this.data.put("isVisited", "2");
					this.data.put("accountid", ((AccountBean)this.session.getAttribute("accountBean")).getId());
					return getReturnString(super.update());
				}
				
				//手机端 签退
			public String signOut(){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				    String now=df.format(new Date());
					this.data.put("signOutTime", now);
					this.data.put("isVisited", "1");
					this.data.put("accountid", ((AccountBean)this.session.getAttribute("accountBean")).getId());
					return getReturnString(super.update());
			}
}
