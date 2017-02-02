package com.cheers.talk.service;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.DateFormat;

public class TalkService extends Dao {
	/**
	 * 添加 发送
	 */
	public String add(){
		try {
			this.session = request.getSession();
			String accountId=((AccountBean) this.session.getAttribute("accountBean")).getId();//获取上报人
	//		this.data.put("creatorId",accountId);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//上报时间
			String createtime=sdf.format(new Date());
	//		this.data.put("createTime",createtime);
			List donext=new ArrayList();
			String insertSQL = " insert into Talk(talkto,talkcontent,creatorId,createTime,talktype)" +
					"values('"+data.get("talkto")+"','"+data.get("talkcontent")+"','"+accountId+"','"+createtime+"','"+data.get("talktype")+"')";
			donext.add(insertSQL);
			String talktype = String.valueOf(data.get("talktype"));
			if("1".equals(talktype)){
				String talkto = String.valueOf(data.get("talkto"));
				String arr[] = talkto.split(",");
				for(int i =0;i<arr.length;i++){
					String insertSQL2 = " insert into TalkTo(talkid,accountid)values(ident_current('Talk'),'"+arr[i]+"')";
					donext.add(insertSQL2);
				}
			}
			db=new DataBase();
			db.updateBatch((String[])donext.toArray(new String[donext.size()]));
			return getReturnString("true");
		} catch (Exception e) {
			e.printStackTrace();
			return getReturnString("false");
		}finally{
			try {
				db.close();
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		return getReturnString(super.add());
	}
	/**
	 * 历史记录
	 * @return
	 */
	public String getTalkList(){
		this.session = request.getSession();
		String accountId=((AccountBean) this.session.getAttribute("accountBean")).getId();//当前登录人id
		
		StringBuffer sql =new StringBuffer();
		sql.append(" SELECT TOP 100 PERCENT t.id,t.talkto,t.talktype,t.talkcontent,t.creatorId,a.name accountname,t.createTime  ");
		sql.append(" from Talk t LEFT JOIN sysACCOUNT a on t.creatorId = a.id ");
        sql.append(" where (t.talktype = '0' or ( t.talktype = '1' and t.id in (select tt.talkid from TalkTo tt where tt.accountid = '"+accountId+"' ) ) or t.creatorId = '"+accountId+"') ");
		sql.append( " order by t.id desc ");
		
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();		
	}
	/**
	 * 交流框  只看 点开后 的
	 * @return
	 * @throws Exception
	 */
	public String getTalkWindow()throws Exception{
		
		String openTime = request.getParameter("openTime");
		if(openTime!=null&&"".equals(openTime)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//上报时间
			openTime=sdf.format(new Date());
//			System.out.println("首次点开页面....锁定时间"+openTime);
//		}else{
//			System.out.println("停留页面....首次时间"+openTime);
		}
		
//		System.out.println("getTalkWindow...");
		this.session = request.getSession();
		String accountId=((AccountBean) this.session.getAttribute("accountBean")).getId();//当前登录人id
		String talkwindow = "";
		ResultSet rs = null;
		DataBase db = new DataBase();
		
		StringBuffer sql =new StringBuffer();
		sql.append(" SELECT t.id,t.talkto,t.talktype,t.talkcontent,t.creatorId,a.name accountname,t.createTime  ");
		sql.append(" from Talk t LEFT JOIN sysACCOUNT a on t.creatorId = a.id ");
        sql.append(" where (t.talktype = '0' or ( t.talktype = '1' and t.id in (select tt.talkid from TalkTo tt where tt.accountid = '"+accountId+"' ) ) or t.creatorId = '"+accountId+"') and t.createTime > '"+openTime+"' ");
		sql.append( " order by t.id ");
		
//		System.out.println("sql..."+sql.toString());
		
		try {
			rs = db.queryAll(sql.toString());
			while(rs.next()){
				talkwindow += rs.getString("accountname")+" -- "+rs.getString("createTime")+"\r\n";
				talkwindow += "    "+rs.getString("talkcontent")+"\r\n\r\n";
			}
//			System.out.println("talkwindow..."+talkwindow);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(db!=null){
				db.close();
			}
		}
		
		JSONArray jsonArray=new JSONArray();  //json数据集
		JSONObject json=new JSONObject();
		json.accumulate("talkwindow", talkwindow);
		json.accumulate("opentime", openTime);
		jsonArray.add(json);
		result = jsonArray;
		return getReturn();	
	}
	
	
	
}
