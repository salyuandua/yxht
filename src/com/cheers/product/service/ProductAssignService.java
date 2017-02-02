package com.cheers.product.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.DataFormat;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ProductAssignService extends Dao {
	
	
	public String add(){
		
		return getReturnString(super.add());
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
		
	public String addBomPick(){
		List<Map<String,String>> productList=(List<Map<String,String>>)(this.data.get("productList"));		
		String storeId=(String) this.data.get("outstoreid");
		List<String>upsql=new ArrayList<String>();
		DataBase db=new DataBase();
		for(int i=0;i<productList.size();i++){
			String productId=String.valueOf(productList.get(i).get("productId"));
			String batch=String.valueOf(productList.get(i).get("batch"));
			String number=String.valueOf(productList.get(i).get("number"));
			String surplusNumber=String.valueOf(productList.get(i).get("surplusNumber")).trim();
			if(Integer.valueOf(number)>Integer.valueOf(surplusNumber)){
				return super.getReturnString("大于最大库存！");					
			}
			String updateSql="update storeProduct set number =number -"
					+ productList.get(i).get("number")
					+ " where productid="
					+ productId
					+ " and storeid=" + storeId + " and batch='" + batch + "'";
			upsql.add(updateSql);
		}
		String add=super.add();
		
		if(add.equals("true")){
			try {
				db.updateBatch(upsql.toArray(new String[upsql.size()]));
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return getReturnString(add);
	}
	
	public String addBomProduct(){
		String add=super.add();
		int returni = 0;
		List<Map<String,String>> productList=(List<Map<String,String>>)this.data.get("productList");
		String storeId=(String) this.data.get("storeId");
		String code=(String)this.data.get("bompickmainnumber");
		if (productList != null) {
			List<String> upsql = new ArrayList<String>();
			List<String> sunsql=new ArrayList<String>();
			DataBase db = new DataBase();
			for (int i = 0; i < productList.size(); i++) {					
				String productId = productList.get(i).get("productId");
				String batch = productList.get(i).get("batch");
				String number=productList.get(i).get("number");
				String bomChildSql="SELECT   distinct  dbo.BomChild.productId, dbo.BomChild.number,dbo.BomChild.number*"+number+" " +//获得相应产品原料的用量
									"yiyong FROM         dbo.BomMain INNER JOIN  dbo.BomChild ON dbo.BomMain.ID = dbo.BomChild.bomMainId and bommain.productid="+productId;
				ResultSet rsSunHao;
				try {
					rsSunHao = db.queryAll(bomChildSql);
					while(rsSunHao.next()){
						String chanPinId=rsSunHao.getString(1);
						String yiyong=rsSunHao.getString(3);
						String computeSun="update bompickchild set sunhao=sunhao-"+yiyong+" " +//更新损耗字段
								"where productid="+chanPinId+" and bompickmainid=(select id from bompickmain where code='"+code+"')";
						sunsql.add(computeSun);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String sql = "select count(id) id from storeProduct where 1=1 ";
				if (!DataFormat.checkNull(productId))
					sql += " and productid=" + productId;
				if (!DataFormat.checkNull(storeId))
					sql += " and storeid=" + storeId;
				if (!DataFormat.checkNull(batch))
					sql += " and batch='" + batch + "'";
				try {
					ResultSet rs = db.queryAll(sql);
					if (rs.next())
						returni = rs.getInt("id");
					if (returni > 0) {
						String updatesql = "update storeProduct set number =number +"
								+ productList.get(i).get("number")
								+ " where productid="
								+ productId
								+ " and storeId=" + storeId;
						upsql.add(updatesql);
					} else {
						String insertsql = "insert into storeProduct(storeid,productid,number,batch) values("
								+ storeId
								+ ","
								+ productId
								+ ","
//								+ productList.get(i).get("productPrice")//yichang
//								+ ","
								+ productList.get(i).get("number")
								+ ",'"
								+ batch
								+ "')";
						upsql.add(insertsql);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			try {
				db.updateBatch(upsql.toArray(new String[upsql.size()]));
				db.updateBatch(sunsql.toArray(new String[sunsql.size()]));
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return getReturnString(add);
	}
	public String getSurplusNumber(){
		DataBase db=new DataBase();
		String productid=request.getParameter("productid");
		String batch=request.getParameter("batch");
		String sql="select number from storeProducts where productid="+productid+" and batch="+batch;
		try {
			ResultSet rs=db.queryAll(sql);
			if(rs.next()){
				return super.getReturnString(rs.getString(1));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}
	public String getProducts(){
		String storeid=request.getParameter("storeid");
		String sql="select distinct storeid,productid,name from storeProducts where storeid="+storeid;
		return super.getBeansBySql(sql);
	}
	public String getBatchs(){
		String storeid=request.getParameter("storeid");
		String productid=request.getParameter("productid");
		String sql="select * from storeproducts where storeid="+storeid+" and productid="+productid;		
		return super.getBeansBySql(sql);
	}
	public String getCode(){
		String sql="select distinct code from bompickmain a,bompickchild b where a.id=b.bompickmainid and b.number=b.sunhao";
		return super.getBeansBySql(sql);
	}
}	
