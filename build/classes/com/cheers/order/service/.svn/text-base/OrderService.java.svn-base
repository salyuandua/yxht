package com.cheers.order.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderService extends Dao {
	
	public  String getInitCode()  {
		return getReturnString(super.getCode());
	}
	
	public String add(){ //订单添加
		this.session = request.getSession();
		String accountId=((AccountBean) this.session.getAttribute("accountBean")).getId();//获取上报人
		this.data.put("creatorId",accountId);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//上报时间
		String createtime=sdf.format(new Date());
		this.data.put("createTime",createtime);
		data.put("isShip", 0);
		return getReturnString(super.add());
		
	}
	
	/**
	 * 添加 订单配货单
	 * @return
	 */
	public String addOrderShip(){ // 添加 订单配货单
		this.session = request.getSession();
		String accountId=((AccountBean) this.session.getAttribute("accountBean")).getId();//获取上报人
		this.data.put("creatorId",accountId);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//上报时间
		String createtime=sdf.format(new Date());
		this.data.put("createTime",createtime);
		data.put("isReceipt", 0);
		String falg = super.add();
		if("true".equals(falg)){
			DataBase db=new DataBase();
			String updateOrderShip = " update orders set isShip = 1 where id ="+data.get("orderId")+" ";
			try {
				db.update(updateOrderShip);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return getReturnString(falg);
		
	}
	
	public String update(){
//		this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		return getReturnString(super.update());
	}
	
	public String delete(){
		return getReturnString(super.delete());
	}
	
	public String doReceipt(){//确认收货
		data.put("isReceipt", 1);
		return getReturnString(super.update());
	}
	
	public String updatefieldByIds(){
		return getReturnString(super.updatefieldByIds());
	}
	
	public String getBeans2(){
		 return  super.getBeans();
	 }
	
	public String getOrdersList(){
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from Orderss t where 1=1");
//		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
//      sql.append(" or t.accountid ='"+selfId+"')");
      //=====================================审批=============================
        
        if(shenpi!=null){
             table="orders";
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
		sql.append(super.getSearchStr(super.map));
		sql .append( " order by id desc ");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();		
	}
	
	public String getOrdersListForShip(){
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from Orderss t where 1=1");
		sql.append(" and t.spstate = 1 and t.isShip = 0 ");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
        sql.append(" or t.accountid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		sql .append( " order by id desc ");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();		
	}
	
	public String getOrderShipList(){
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from OrderShips t where 1=1");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
        sql.append(" or t.accountid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		sql .append( " order by id desc ");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();		
	}
	
	//大区月度销量排名
	public String getBeansdqsale(){
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
//		StringBuffer sql =new StringBuffer("select top 100 percent h.dqid,h.accountid,h.year,h.month,isnull(h.orderMoneys,0) ordermoneys,a.name dqjl,t.name dqname from");
//        sql.append(" (select o.dqId,max(o.accountId) accountId,MAX(convert(varchar,datepart(year,o.orderdate))) YEAR,MAX(convert(varchar,datepart(MONTH,o.orderdate))) month,sum(o.orderMoney/10000) orderMoneys");
//        sql.append(" from Orders o ");
//        sql.append(" where 1=1 ");
//        if(super.map.get("yearEQ")!=null||super.map.get("yearEQ")!=""){
//        	sql.append(" and convert(varchar,datepart(year,o.orderdate))="+super.map.get("yearEQ"));
////        }else{
////        	sql.append(" and convert(varchar,datepart(year,o.orderdate))='2014' ");//加个默认
//        }
//        if(super.map.get("monthEQ")!=null||super.map.get("monthEQ")!=""){
//        	
//        	sql.append(" and convert(varchar,datepart(MONTH,orderdate))="+super.map.get("monthEQ"));
////        }else{
////        	sql.append(" and convert(varchar,datepart(MONTH,orderdate))='1' ");
//        }
//        sql.append(" group by o.dqId ");
//        sql.append(" ) h  ");
//        sql.append(" left join SYSTEM_TREE t on t.id=h.dqId left JOIN sysACCOUNT a on a.id=t.person  ");
//        sql.append(" where 1=1 ");
////		sql.append(" and "+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
////		sql.append(" or t.accountid ='"+selfId+"'");
//		sql .append( " order by h.ordermoneys desc ");
		
		if(super.map.get("yearEQ")==null||super.map.get("yearEQ")==""||super.map.get("monthEQ")==null||super.map.get("monthEQ")==""){
			return "";
		}
		
		StringBuffer sql =new StringBuffer(" select top 100 percent (\n" +
				"select count(*) from  dq_from_view b  where 1=1 ");

				if(super.map.get("yearEQ")!=null||super.map.get("yearEQ")!=""){
					sql.append(" and b.YEAR="+super.map.get("yearEQ"));
				}
				if(super.map.get("monthEQ")!=null||super.map.get("monthEQ")!=""){
					sql.append(" and b.MONTH ="+super.map.get("monthEQ"));
				}
				
				sql.append(" and b.ordermoneys >= a.ordermoneys) as rownumber,\n" +
				"* from \n" +
				"dq_from_view as a where 1=1 ");
				
				if(super.map.get("yearEQ")!=null||super.map.get("yearEQ")!=""){
					sql.append(" and a.YEAR="+super.map.get("yearEQ"));
				}
				if(super.map.get("monthEQ")!=null||super.map.get("monthEQ")!=""){
					sql.append(" and a.MONTH ="+super.map.get("monthEQ"));
				}
				
				sql.append("\n" +
				"order by a.ordermoneys desc");
		
		//System.out.println("大区查询sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	 }
	/**
	 * //大区月度销量排名 ---求和
	 * 
	 * @return
	 */
	public String totaldqsale(){ 
		this.session = request.getSession();
		StringBuffer sql =new StringBuffer("select top 100 percent isnull(sum(h.orderMoneys),0) total from");
        sql.append(" (select o.dqId,max(o.accountId) accountId,MAX(convert(varchar,datepart(year,o.orderdate))) YEAR,MAX(convert(varchar,datepart(MONTH,o.orderdate))) month,sum(o.orderMoney/10000) orderMoneys");
        sql.append(" from Orders o ");
        sql.append(" where 1=1 ");
        if(super.map.get("yearEQ")!=null||super.map.get("yearEQ")!=""){
        	sql.append(" and convert(varchar,datepart(year,o.orderdate))="+super.map.get("yearEQ"));
//        }else{
//        	sql.append(" and convert(varchar,datepart(year,o.orderdate))='2014' ");//加个默认
        }
        if(super.map.get("monthEQ")!=null||super.map.get("monthEQ")!=""){
        	
        	sql.append(" and convert(varchar,datepart(MONTH,orderdate))="+super.map.get("monthEQ"));
//        }else{
//        	sql.append(" and convert(varchar,datepart(MONTH,orderdate))='1' ");
        }
        sql.append(" group by o.dqId ");
        sql.append(" ) h  ");
        sql.append(" left join SYSTEM_TREE t on t.id=h.dqId left JOIN sysACCOUNT a on a.id=t.person  ");
        sql.append(" where 1=1 ");
		sql.append(super.getSearchStr(super.map));
	
		System.out.println(sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	//区域月度销量排名
		public String getBeansqysale(){
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			
			if(super.map.get("yearEQ")==null||super.map.get("yearEQ")==""||super.map.get("monthEQ")==null||super.map.get("monthEQ")==""){
				return "";
			}
			
			SqlUtil sqlUtil = new SqlUtil();
			
			StringBuffer sql =new StringBuffer(" select top 100 percent (\n" +
					"select count(*) from  qy_from_view b  where 1=1 ");

					if(super.map.get("yearEQ")!=null||super.map.get("yearEQ")!=""){
						sql.append(" and b.YEAR="+super.map.get("yearEQ"));
					}
					if(super.map.get("monthEQ")!=null||super.map.get("monthEQ")!=""){
						sql.append(" and b.MONTH ="+super.map.get("monthEQ"));
					}
					
					sql.append(" and b.ordermoneys >= a.ordermoneys) as rownumber,\n" +
					"* from \n" +
					"qy_from_view as a where 1=1 ");
					
					if(super.map.get("yearEQ")!=null||super.map.get("yearEQ")!=""){
						sql.append(" and a.YEAR="+super.map.get("yearEQ"));
					}
					if(super.map.get("monthEQ")!=null||super.map.get("monthEQ")!=""){
						sql.append(" and a.MONTH ="+super.map.get("monthEQ"));
					}
					
					sql.append("\n" +
					"order by a.ordermoneys desc");
			
			//" select top 100 percent t.* from qy_from_view t where 1=1 ");
			
//			if(super.map.get("yearEQ")!=null||super.map.get("yearEQ")!=""){
//	        	sql.append(" and t.YEAR="+super.map.get("yearEQ"));
//	        }
//	        if(super.map.get("monthEQ")!=null||super.map.get("monthEQ")!=""){
//	        	sql.append(" and t.MONTH ="+super.map.get("monthEQ"));
//	        }
//	        sql .append( " order by t.ordermoneys desc ");
	        
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		 }
		/**
		 * //区域月度销量排名 ---求和
		 * 
		 * @return
		 */
		public String totalqysale(){ 
			this.session = request.getSession();
			StringBuffer sql =new StringBuffer("select top 100 percent isnull(sum(h.orderMoneys),0) total from");
	        sql.append(" (select o.qyId,max(o.accountId) accountId,MAX(convert(varchar,datepart(year,o.orderdate))) YEAR,MAX(convert(varchar,datepart(MONTH,o.orderdate))) month,sum(o.orderMoney/10000) orderMoneys");
	        sql.append(" from Orders o ");
	        sql.append(" where 1=1 ");
	        if(super.map.get("yearEQ")!=null||super.map.get("yearEQ")!=""){
	        	sql.append(" and convert(varchar,datepart(year,o.orderdate))="+super.map.get("yearEQ"));
//	        }else{
//	        	sql.append(" and convert(varchar,datepart(year,o.orderdate))='2014' ");//加个默认
	        }
	        if(super.map.get("monthEQ")!=null||super.map.get("monthEQ")!=""){
	        	
	        	sql.append(" and convert(varchar,datepart(MONTH,orderdate))="+super.map.get("monthEQ"));
//	        }else{
//	        	sql.append(" and convert(varchar,datepart(MONTH,orderdate))='1' ");
	        }
	        sql.append(" group by o.qyId ");
	        sql.append(" ) h  ");
	        sql.append(" left join SYSTEM_TREE t on t.id=h.qyId left JOIN sysACCOUNT a on a.id=t.person  ");
	        sql.append(" where 1=1 ");
			sql.append(super.getSearchStr(super.map));
		
			System.out.println(sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		/**
		 * 月度销售报表 
		 * @return
		 */
		public String getBeansYDdsale(){
			if(super.map.get("yearEQ")!=null&&super.map.get("yearEQ")!=""&&super.map.get("monthEQ")!=null&&super.map.get("monthEQ")!=""){
				
				String searchYear = String.valueOf(super.map.get("yearEQ"));
				String searchMonth = String.valueOf(super.map.get("monthEQ"));
				
				StringBuffer sql =new StringBuffer();
				sql.append(" select t1.id,t1.quyuname,t1.daquid,t1.daquname,t1.qyaccountname,t1.levelname,t1.dqaccountname,t1.kefuaccountname,(t1.oldorderMonry/10000)as oldorderMonry,(t1.goal/10000) as goal,(t1.noworderMonry/10000) as noworderMonry ");
				sql.append(" ,convert(varchar(100),convert(decimal(10,2),((CASE when t1.goal = 0 then 0 else (t1.noworderMonry/10000)/(t1.goal/10000) end )*100)))+'%'  wanchenglv ");
				sql.append(" ,convert(varchar(100),convert(decimal(10,2),(((t1.noworderMonry/10000)-(t1.oldorderMonry/10000))/(CASE when t1.oldorderMonry = 0 then 1 else (t1.oldorderMonry/10000) end )*100)))+'%' zengzhanglv ");
				sql.append(" from ( ");
				sql.append(" select st.id,st.name quyuname,st1.id daquid,st1.name daquname ,a.name qyaccountname,bd.name levelname,a1.name dqaccountname,a2.name kefuaccountname ");
				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.qyid = st.id and DATENAME(YY,o.orderdate)='"+(Integer.parseInt(searchYear)-1)+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) oldorderMonry ");
				sql.append(" ,isnull((select top 1 g.salemoney from goal g where g.ywyid = st.person and g.year='"+searchYear+"' AND g.month = '"+searchMonth+"'),0) goal ");
				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.qyid = st.id and DATENAME(YY,o.orderdate)='"+searchYear+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) noworderMonry ");
				sql.append(" from SYSTEM_TREE st  ");
				sql.append(" LEFT JOIN SYSTEM_TREE st1 on SUBSTRING(st.vorder, 0, LEN(st.vorder)-3) = st1.vorder ");
				sql.append(" LEFT JOIN sysACCOUNT a on a.id = st.person ");
				sql.append(" LEFT JOIN sysACCOUNT a1 on a1.id = st1.person ");
 				sql.append(" LEFT JOIN BASE_DATA bd on a.levelid = bd.id ");
				sql.append(" LEFT JOIN SYSTEM_TREE st2 on st2.name = '客服内勤' ");
				sql.append(" LEFT JOIN sysACCOUNT_POST ap on ap.positionid = st2.id and orgid = st1.id ");
				sql.append(" LEFT JOIN sysACCOUNT a2 on a2.id = ap.accountid ");
				sql.append(" where  st.categorytype = 1 and st.notetype = 1  ");
				sql.append(" UNION ALL  ");
				sql.append(" select '','',st.id,st.name dqname,'','',a.name dyaccountname,a2.name kefuaccountname  ");
//				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.dqid = st.id and DATENAME(YY,o.orderdate)='"+(Integer.parseInt(searchYear)-1)+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) oldorderMonry  ");
//				sql.append(" ,isnull((select top 1 sum(g.salemoney) from goal g,SYSTEM_TREE st1 where g.ywyid = st1.person and g.year='"+searchYear+"' AND g.month = '"+searchMonth+"' and charindex(st.vorder,st1.vorder)=1 and st1.categorytype = 1  and st1.notetype = 1),0) goal  ");
//				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.dqid = st.id and DATENAME(YY,o.orderdate)='"+searchYear+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) noworderMonry  ");
				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.dqid = st.id and DATENAME(YY,o.orderdate)='"+(Integer.parseInt(searchYear)-1)+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.isEnable = '1'),0) oldorderMonry  ");
				sql.append(" ,isnull((select top 1 sum(g.salemoney) from goal g,SYSTEM_TREE st1 where g.ywyid = st1.person and g.year='"+searchYear+"' AND g.month = '"+searchMonth+"' and charindex(st.vorder,st1.vorder)=1 and st1.categorytype = 1  and st1.notetype = 1),0) goal  ");
				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.dqid = st.id and DATENAME(YY,o.orderdate)='"+searchYear+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.isEnable = '1'),0) noworderMonry  ");
				
				
				sql.append(" from SYSTEM_TREE st   ");
				sql.append(" LEFT JOIN sysACCOUNT a on a.id = st.person  ");
				sql.append(" LEFT JOIN SYSTEM_TREE st2 on st2.name = '客服内勤'  ");
				sql.append(" LEFT JOIN sysACCOUNT_POST ap on ap.positionid = st2.id and orgid = st.id  ");
				sql.append(" LEFT JOIN sysACCOUNT a2 on a2.id = ap.accountid  ");
				sql.append(" where  st.categorytype = 1 and st.notetype = 4  ");
				sql.append(" )t1 ");
			
				System.out.println("月度销售报表 sql:"+sql);
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
			 }else{
				 return getReturnString("sorry,请选择查询年、月");
			 }
		}
		/**
		 * 月度销售报表  柱状图
		 * @return
		 */
		public String getBeansYDdsalezzt(){
			if(super.map.get("yearEQ")!=null&&super.map.get("yearEQ")!=""&&super.map.get("monthEQ")!=null&&super.map.get("monthEQ")!=""){
				
				String searchYear = String.valueOf(super.map.get("yearEQ"));
				String searchMonth = String.valueOf(super.map.get("monthEQ"));
				
				StringBuffer sql =new StringBuffer();
				sql.append(" select t1.id,t1.quyuname,t1.daquid,t1.daquname,t1.qyaccountname,t1.dqaccountname,t1.kefuaccountname,(t1.oldorderMonry/10000)as oldorderMonry,(t1.goal/10000) as goal,(t1.noworderMonry/10000) as noworderMonry ");
				sql.append(" ,convert(varchar(10),convert(decimal(10,2),((CASE when t1.goal = 0 then 0 else (t1.noworderMonry/10000)/(t1.goal/10000) end )*100)))+'%'  wanchenglv ");
				sql.append(" ,convert(varchar(10),convert(decimal(10,2),(((t1.noworderMonry/10000)-(t1.oldorderMonry/10000))/(CASE when t1.oldorderMonry = 0 then 1 else (t1.oldorderMonry/10000) end )*100)))+'%' zengzhanglv ");
				sql.append(" from ( ");
				sql.append(" select st.id,st.name quyuname,st1.id daquid,st1.name daquname ,a.name qyaccountname,a1.name dqaccountname,a2.name kefuaccountname ");
				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.qyid = st.id and DATENAME(YY,o.orderdate)='"+(Integer.parseInt(searchYear)-1)+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) oldorderMonry ");
				sql.append(" ,isnull((select top 1 g.salemoney from goal g where g.ywyid = st.person and g.year='"+searchYear+"' AND g.month = '"+searchMonth+"'),0) goal ");
				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.qyid = st.id and DATENAME(YY,o.orderdate)='"+searchYear+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) noworderMonry ");
				sql.append(" from SYSTEM_TREE st  ");
				sql.append(" LEFT JOIN SYSTEM_TREE st1 on SUBSTRING(st.vorder, 0, LEN(st.vorder)-3) = st1.vorder ");
				sql.append(" LEFT JOIN sysACCOUNT a on a.id = st.person ");
				sql.append(" LEFT JOIN sysACCOUNT a1 on a1.id = st1.person ");
				sql.append(" LEFT JOIN SYSTEM_TREE st2 on st2.name = '客服内勤' ");
				sql.append(" LEFT JOIN sysACCOUNT_POST ap on ap.positionid = st2.id and orgid = st1.id ");
				sql.append(" LEFT JOIN sysACCOUNT a2 on a2.id = ap.accountid ");
				sql.append(" where  st.categorytype = 1 and st.notetype = 1  ");
				sql.append(" )t1 ");
			
				System.out.println("月度销售报表 sql:"+sql);
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
			 }else{
				 return getReturnString("sorry,请选择查询年、月");
			 }
		}
		/**
		 * 月度销售报表 合计
		 * @return
		 */
		public String totalYDsale(){ 
			
			if((super.map.get("yearEQ")!=null||super.map.get("yearEQ")!="")&&(super.map.get("monthEQ")!=null||super.map.get("monthEQ")!="")){
				String searchYear = String.valueOf(super.map.get("yearEQ"));
				String searchMonth = String.valueOf(super.map.get("monthEQ"));
				this.session = request.getSession();
				StringBuffer sql =new StringBuffer();
				
				sql.append(" select SUM(t.oldorderMonry)oldorderMonry,SUM(t.goal)goal,SUM(t.noworderMonry)noworderMonry ");
				sql.append(" ,convert(varchar(100),convert(decimal(10,2),(SUM(t.noworderMonry)/(CASE when SUM(t.goal) = 0 then 1 else SUM(t.goal) end )*100)))+'%'  wanchenglv ");
				sql.append(" ,convert(varchar(100),convert(decimal(10,2),((SUM(t.noworderMonry)-SUM(t.oldorderMonry))/(CASE when SUM(t.oldorderMonry) = 0 then 1 else SUM(t.oldorderMonry) end )*100)))+'%' zengzhanglv ");
				sql.append(" from ( ");
				sql.append(" select isnull((select SUM(o.ordermoney) from orders o where o.dqid = st.id and DATENAME(YY,o.orderdate)='"+(Integer.parseInt(searchYear)-1)+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) oldorderMonry ");
				sql.append(" ,isnull((select top 1 sum(g.salemoney) from goal g,SYSTEM_TREE st1 where g.ywyid = st1.person and g.year='"+searchYear+"' AND g.month = '"+searchMonth+"' and charindex(st.vorder,st1.vorder)=1 and st1.categorytype = 1  and st1.notetype = 1),0) goal ");
				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.dqid = st.id and DATENAME(YY,o.orderdate)='"+searchYear+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) noworderMonry ");
				sql.append(" from SYSTEM_TREE st  where  st.categorytype = 1 and st.notetype = 4  )t ");
				
				System.out.println(sql);
				excuteSQLPageQuery(sql.toString());
				result = getRows();
				return getReturn();
			}else{
				 return getReturnString("");
			}
		}
		
		/**
		 * 	一新数码
		 * 	销售业绩、费用一览表统计
		 * */
		public String getAchievementAndCost(){
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from salesreport_view where 1=1 ");
			/*sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"accountid"));
			sql.append(" or accountid='"+selfId+"')");*/
	        sql.append(super.getSearchStr(super.map));
	        System.out.println("--="+sql.toString());
	        excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
}
