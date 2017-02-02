package com.cheers.fee.service;


import java.sql.ResultSet;
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
import com.cheers.sp.service.SpService;
import com.cheers.util.DataFormat;


public class feeapplyService extends Dao {
	
	public String add(){
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String selfId = accountBean.getId();//当前登录人id
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String now=df.format(new Date());
		this.data.put("createTime", now);
		this.data.put("useapplyid", selfId);
		Map sqlMap=new HashMap();
		sqlMap.put("createTime", now);
		String add=super.add();
		if(add.equals("true")){
			int i=getMaxId((String)this.data.get("table"), sqlMap);
		return super.getReturnString(i+"");
		}else return super.getReturnString(add);
	}
	public String update(){
		return getReturnString(super.update());
	}
	
	public String delete(){
		return getReturnString(super.delete());
	}
	
	public String updatefieldByIds(){
		return getReturnString(super.updatefieldByIds());
	}
			
	/**
	 *费用管理信息列表显示方法 
	 * 
	 * @return
	 */
	public String getBeansSelf(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from feeView t where 1=1");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.purchaseApplyid"));
        sql.append(" or t.purchaseApplyid ='"+selfId+"')");
      //=====================================审批=============================
       
        if(shenpi!=null){
             table="fee";
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
		sql .append( " order by id");
		System.out.println("sql管理费用--------:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}	
	/**
	 * 费用管理批复信息列表显示方法
	 *  2013年12月16日16:32:50
	 * @return
	 */
	public String getBeansPf(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from feeApply t where 1=1");
		sql.append(" and "+sqlUtil.getSqlStrByArrays(canAccountIds,"t.purchaseApplyid"));
	//	sql.append(" or t.purchaseApplyid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		if (!DataFormat.booleanCheckNull(sort))
			sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
		else
			sql .append( " order by id");
		System.out.println("sql费用批复---------:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	/**
	 *用款管理信息列表显示方法 
	 * 
	 * @return
	 */
	public String getBeansuseSelf(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from usemoneyview t where 1=1");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.useapplyid"));
        sql.append(" or t.useapplyid ='"+selfId+"')");
        //=====================================审批=============================
        
        if(shenpi!=null){
             table="usemoney";
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
		sql .append( " order by id desc");
		System.out.println("sql用款费用--------:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}	
	
	/**
	 * 	通过借款人显示该部门
	 * */
	public String getUseapplyByDepartment() {
		String useapplyid = this.request.getParameter("useapplyid");
		String sql="SELECT b.id,b.name from sysACCOUNT_POST a LEFT JOIN SYSTEM_TREE b on a.orgId = b.id where a.accountId="+useapplyid;
		return super.getBeansBySql(sql);
	}
}
