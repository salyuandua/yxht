package com.cheers.basedata.service;



import com.cheers.account.bean.AccountBean;
import com.cheers.dao.Dao;

public class BaseDataService extends Dao {
	
	
	public String add(){
			return getReturnString(super.add());
	}
	public String update(){
		return getReturnString(super.update());
	}
	
	public String delete(){
		return getReturnString(super.delete());
	}
	public String getBeans(){
		 String type= request.getParameter("type");
		 if(type!=null&&!type.equals(""))this.map.put("typeEQ",type);
		 return  super.getBeans();
	 }
	public String getPlaneFiance(){
		String id=request.getParameter("accountid");
		String sql="select sum(totalmoney) allmoney from plantoperson where convert(varchar(10),timebegin,121)>=(select   convert(varchar(10),(dateadd(d,-day(getdate())+1,getdate())),121)) and convert(varchar(10),timeend,121)<=(select   convert(varchar(10),(dateadd(d,-day(getdate()),dateadd(m,1,getdate()))),121)) and accountid="+id+" group by accountid";
		return super.getBeansBySql(sql);
	}
	public String getStockFiance(){
		String id=request.getParameter("accountid");
		String sql="SELECT     sum(ordermoney) allmoney FROM         dbo.Orders AS a INNER JOIN dbo.Customer AS b ON a.customerId = b.ID INNER JOIN dbo.SysAccount AS c ON a.accountId = c.ID where  a.createtime between (select   convert(varchar(10),(dateadd(d,-day(getdate())+1,getdate())),121))and (select   convert(varchar(10),(dateadd(d,-day(getdate()),dateadd(m,1,getdate()))),121))  and a.accountid="+id+" group by a.accountid";
		return super.getBeansBySql(sql);
	}
	public String getCarFiance(){
		String id=request.getParameter("accountid");
		String sql="SELECT     sum(a.allmoney) allmoney FROM         dbo.SaleStock AS a INNER JOIN dbo.Customer AS b ON a.customerid = b.ID INNER JOIN dbo.SysAccount AS c ON a.saleaccountId = c.ID where  a.submittime between (select   convert(varchar(10),(dateadd(d,-day(getdate())+1,getdate())),121))and (select   convert(varchar(10),(dateadd(d,-day(getdate()),dateadd(m,1,getdate()))),121))and  a.saleaccountid="+id+"  group by a.saleaccountid ";
		return super.getBeansBySql(sql);
	}
	
	public String getCustomFenLei(){
		String id=request.getParameter("accountid");
		String sql="";
		
		//判断 账户的 角色 设置
		String roleid=((AccountBean)this.session.getAttribute("accountBean")).getRoleid();
		
		if("3".equals(roleid)){
			sql=" select  id value, name text from BASE_DATA where type = 'Cusfl' and isenable ='1' ";
		}else if("53".equals(roleid)){
			sql=" select  id value, name text from BASE_DATA where type = 'Cusfl' and isenable ='1' ";
		}else{
			sql=" select  id value, name text from BASE_DATA where type = 'Cusfl' and isenable ='1' and fenleikey = '0' ";
		}
		
		return super.getBeansBySql(sql);
	}
	
	public String getAreas(){
		String id=request.getParameter("accountid");
		String sql=" select  id value,name text from system_tree where notetype ='1'  ";
		return super.getBeansBySql(sql);
	}
}
