package com.cheers.product.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.account.bean.AccountBean;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.DateFormat;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ProductService extends Dao {
	
	
	public String add(){
		
		Map m = new HashMap();
		DateFormat df = new DateFormat();//设置日期格式
		String now=df.getNowTime();
		m.put("table", this.data.get("table"));
		m.put("codesEQ", this.data.get("codes"));
		this.data.put("createTime", now);
		this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		/*if("true".equals(checkDuplicate(m))){
			return getReturnString("同种产品编码的产品已存在");
		}else{*/
			return getReturnString(super.add());
		//}
			} 
		
	
	public String update(){
		DateFormat df = new DateFormat();//设置日期格式
		String now=df.getNowTime();
		this.data.put("createTime", now);
		this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		return getReturnString(super.update());
		/*Map m = new HashMap();
		m.put("table", this.data.get("table"));
		
		if("true".equals(checkDuplicate(m))){
			if(this.result.size()>1){
				return getReturnString("已有重复数据");	
			}else {
				DynaBean bean = (DynaBean) this.result.get(0);
				if(bean.get("id").toString().equals(this.data.get("id")+"")){
					List sqls = super.updateSql(this.data);
					super.updateBetch((String[])sqls.toArray(new String[sqls.size()]));
					
					return getReturnString("true");
				}else
				return getReturnString("信息丢失");
			}
		}else {
			return getReturnString("信息丢失");
		}*/
	}
	
	public String delete(){
		return getReturnString(super.delete());
	}
	
	public String updatefieldByIds() {
		
		return super.getReturnString(super.updatefieldByIds());
	}
	public String addcode(){//生成箱码
		
        String update="false";
		Map m = new HashMap();
		String time=(String)request.getParameter("productTime");		
        String  strSplit  = "-";   
        String[]  time1   = time.split(strSplit); 
        String t="";
        for (int i =0;i < time1.length;i++)   
        {   
               t+=time1[i]; 
        }
        //"20130910"    8  9  10
        if(t.length()==10){
        t=t.substring(3,9);
        }else if(t.length()==9){
        	t=t.substring(3,8);
        }else{
        	t=t.substring(3,7);
        }
  
		String mnemonicCode="'"+request.getParameter("stripmark")+t+request.getParameter("packagespec")+"'";
		m.put("table", this.data.get("table"));
			DataBase db = new DataBase();
			try {				
				db.connectDb();
				String sql="update Product set mnemonicCode="+mnemonicCode+" where id="+request.getParameter("id")+"";
				db.update(sql);
				update="true";
			} catch (DbException e) {
				e.printStackTrace();
			}				
			return getReturnString(update);
	
	}
	public String getActualprice(){                  //通过价格政策查询产品价格
		int customerid=Integer.valueOf(String.valueOf(request.getParameter("customerid")));
		int productid =Integer.valueOf(String.valueOf(request.getParameter("productid")));
		boolean flag=false;
		String price = null;
		DataBase db = new DataBase();
		try {				
			db.connectDb();
			String sql="select actualprice from policys where getdate() between  (select convert(datetime,startdate) from policys where customerid="+customerid+" and productid="+productid+") and (select convert(datetime,enddate) from policys where customerid="+customerid+" and productid="+productid+") and customerid="+customerid+" and productid="+productid+" and isEnable=1";
			ResultSet rs=db.queryAll(sql);
			try {
				if(rs.next()){
					price=rs.getString("actualprice");
					flag=true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (DbException e) {
			e.printStackTrace();
		}				
		if(flag){
			return super.getReturnString(price);
		}
		return getReturnString("0");
	}
	/**
	 * 入库下拉框
	 * @return
	 */
	public String inStoreProC(){
		String sql="select top 50 * from (select p.id,+'['+p.name+']--['+p.specification+']--['+s.name+']' name,p.name productname,p.codes,p.specification,s.name measurename,p.factoryprice,p.tradeprice,p.stripmark from product p," +
				"	sysbasedata s where p.isenable=1  and s.id=p.productmeasureid )a  ";
		if(super.QUESTION!=null&&!super.QUESTION.equals("")){
			sql+="	where 	  name like '%"+super.QUESTION+"%' 	or measurename like '%"+super.QUESTION+"%'";
		}
		sql+="	order by name";
		excuteSQLPageQuery(sql);
		result = getRows();
		return getReturn();
	}
	/**
	 * 出库下拉框
	 * @return
	 */
	public String outStoreProC(){
		String storeid=String.valueOf(request.getParameter("storeid"));
		String sql="select top 100 percent * from (select sp.id,+'['+p.name+']--['+sp.specification+']--['+sp.measurename+']'  name " +
				"	from storeproduct sp,product p where storeid="+storeid+"	and sp.productid=p.id)a  ";
		/*if(super.QUESTION!=null&&!super.QUESTION.equals("")){
			sql+="	where 	  name like '%"+super.QUESTION+"%' 	or measurename like '%"+super.QUESTION+"%'";
		}*/
		sql+="	order by name";
		excuteSQLPageQuery(sql);
		result = getRows();
		return getReturn();
	}
	/**
	 * 车辆产品下拉框
	 * @return
	 */
	public String carProC(){
		String storeid=String.valueOf(request.getParameter("storeid"));
		String sql="select top 100 percent * from (select sp.id,p.name+'	\n\b'+sp.codes  name ,sp.productid " +
				"	from storeproduct sp,product p where storeid="+storeid+"	and sp.productid=p.id)a  ";
		/*if(super.QUESTION!=null&&!super.QUESTION.equals("")){
			sql+="	where 	  name like '%"+super.QUESTION+"%' 	or measurename like '%"+super.QUESTION+"%'";
		}*/
		sql+="	order by name";
		excuteSQLPageQuery(sql);
		result = getRows();
		return getReturn();
	}
	
	
	public String combPro(){
		String sql="select top 50 * from (select p.id,+'['+p.name+']--['+p.specification+']--['+s.name+']' name,p.codes,p.name xname,p.specification,s.name measurename from product p," +
				"	sysbasedata s where p.isenable=1  and s.id=p.productmeasureid )a  ";
		if(super.QUESTION!=null&&!super.QUESTION.equals("")){
			sql+="	where 	  name like '%"+super.QUESTION+"%' 	or measurename like '%"+super.QUESTION+"%'";
		}
		sql+="	order by name";
		excuteSQLPageQuery(sql);
		result = getRows();
		return getReturn();
	}
	public String getBeans(){
		 return  super.getBeans();
	 }
	/**
	 * 订单产品下拉 
	 * @return
	 */
	public String searchOrderProduct(){
//		String productcategory = request.getParameter("productcategory")==null?"":request.getParameter("productcategory");
		String q= this.QUESTION.replace("'","");
		
		String id = request.getParameter("id");
		
		System.out.println("_____"+q+"______");
		StringBuffer sql = new StringBuffer("SELECT top 100 t.* FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			p.id VALUE,\n" +
				"			(\n" +
				"				p.name + ' （规格：' + p.specification + ')'\n" +
				"			) AS TEXT,\n" +
				"           p.name," +
				"			p.specification,\n" +
				"			p.codes,\n" +
				"			p.price,\n" +
				"			bd.name productunit,\n" +
				"      p.zhuji \n" +
				"		FROM\n" +
				"			product p\n" +
				"		LEFT JOIN BASE_DATA bd ON p.unitid = bd.id\n" +
				"		WHERE\n" +
				"			1 = 1\n" +
				"		AND p.isEnable = 1)  t ");
		if(q!=null&&!"".equals(q)){
			sql.append(" WHERE 1=1  and (replace(t. TEXT,' ','') like '%"+q+"%' or t.zhuji like '%"+q+"%')");
		}else if(id!=null&&!"".equals(id)){
			sql.append(" WHERE 1=1  and cast(t.value as varchar) ='"+id+"' ");
		}
		
		/*if(!"".equals(productcategory)){//产品类型
			sql.append(" and (pr.productcategory !=  '"+productcategory+"') ");
		}*/
//		System.out.println("@@@@@@@@@@@@@"+sql.toString()+"!!!!!!!!!!!!!!!!!!!!");
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
}
