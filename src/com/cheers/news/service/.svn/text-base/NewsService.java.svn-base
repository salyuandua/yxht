package com.cheers.news.service;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.GetUrl;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.sp.service.SpService;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;
import com.cheers.util.JsonUtil;


import java.net.URLEncoder;
import java.sql.*;

import org.apache.commons.beanutils.DynaBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.cheers.database.*;
import com.ldsoft.send.SingleSend;
public class NewsService extends Dao{

	public String add(){
		this.session = request.getSession();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String now=df.format(new Date());
		this.data.put("createTime", now);
		this.data.put("accountId", ((AccountBean)this.session.getAttribute("accountBean")).getId());

		List<Object> proList = (List<Object>) data.get("accountList");//获取到联系人对象集合
		List<Object> newList = new ArrayList<Object>();//创建一个新的集合存放新的联系人数据
		for (int i = 0; i <proList.size(); i++) {
			//集合中存放的为LinkedHashMap对象，每次循环取出一个
			LinkedHashMap<String, String> maps = (LinkedHashMap<String, String>) proList.get(i);
			maps.put("table", "newsAccount");
			maps.put("mainTable", "news");
			maps.put("fk", "newsId");
			maps.put("hasReading", "0");
			newList.add(maps);//将新的map数据放入新的集合中
		}
		data.remove("accountList");
		//放入新的list集合
		data.put("accountList", newList);
		
		String retStr=super.add();
		if(retStr.equals("true")){
			Map sqlMap=new HashMap();
			sqlMap.put("createTime", now);
			int i=getMaxId((String)this.data.get("table"), sqlMap);
			return super.getReturnString(i+"");
		}else return super.getReturnString(retStr);
	}
	
	//----------------------------------短信发送--------------------
		
	public String add2(){ //短信添加
		int iRet = 0;
		String account="kefa";
		String password="kefa123456";
		String type="0";
		String message=(String) this.data.get("content");
		String title=(String) this.data.get("title");
		if (message == null || "".equals(message.trim())) {
			return super.getReturnString(iRet+"");
		}
		this.session = request.getSession();
		String account_id=((AccountBean) this.session.getAttribute("accountBean")).getId();//获取上报人
		String auto=((AccountBean) this.session.getAttribute("accountBean")).getName();//获取上报人
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//上报时间
		String createtime=sdf.format(new Date());
		List<Object> proList = (List<Object>) data.get("accountList");//获取到联系人对象集合
		List<Object> newList = new ArrayList<Object>();//创建一个新的集合存放新的联系人数据
		int count = newList.size();
		if (count > 1000) {
			return super.getReturnString((-1)+"");
		}
		for (int i = 0; i <proList.size(); i++) {
			//集合中存放的为LinkedHashMap对象，每次循环取出一个
			LinkedHashMap<String, String> maps = (LinkedHashMap<String, String>) proList.get(i);
			String number=maps.get("phone");
			//String strUrl = "http://www.china-sms.com/send/ssend.asp?name=" + account + "&pwd=" + password + "&dst=" + number + "&msg=" + message;
			  String strUrl = "http://www.139000.com/send/gsend.asp?txt=ccdx&name=" + account + "&pwd=" + password + "&dst=" + number + "&msg=" + URLEncoder.encode(message);
		      GetUrl gu = new GetUrl();
		      String s = gu.getUrl(strUrl);
		      String id = (String)gu.jix(s).get(1);
		      if (Integer.parseInt(id) == 0) {
		        iRet = -1;
		        addSms(number, message, "0", account_id, auto, type,title);//type默认是0
		        break;
		      }
		      iRet = 1;
		      addSms(number, message, "1", account_id, auto, type,title);
		}
		return super.getReturnString(iRet+"");
		
	}
	private synchronized int addSms(
			String phone_number, 
			String message, 
			String success, 
			String account_id, 
			String auto, 
			String type,
			String title) {
		int iRet = 0;
		if (phone_number == null || "".equals(phone_number.trim())) {
			return iRet;
		}
		if (message == null || "".equals(message.trim())) {
			return iRet;
		}
		if (account_id == null || "".equals(account_id.trim())) {
			return iRet;
		}
		if (auto == null || "".equals(auto.trim())) {
			return iRet;
		}
		if (type == null || "".equals(type.trim())) {
			return iRet;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("insert into SMS_HISTORY(MOBILE, MESSAGE, SEND_TIME, SUCCESS, ACCOUNT_ID, AUTO, TYPE,title) values('");
		sql.append(phone_number);
		sql.append("','");
		sql.append(message);
		sql.append("', getDate(),'");
		sql.append(success);
		sql.append("','");
		sql.append(account_id);
		sql.append("','");
		sql.append(auto);
		sql.append("','");
		sql.append(type);
		sql.append("','");
		sql.append(title);
		sql.append("')");
		
		DataBase db = null;
		try {
			db = new DataBase();
			db.connectDb();
			iRet = db.update(sql.toString());
		} catch (Exception e) {
			iRet = -1;
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return iRet;
	}
	//查询余额
	public String getmoney(){
		SingleSend singleSend = new SingleSend("kefa","kefa123456");
		  float balance = 0;
		  String balance2="";
		  try{
			  DecimalFormat df = new DecimalFormat("#.00");
		    balance = singleSend.getbalance()/10; //短信条数
		    balance2=df.format(balance*0.1);  //短信余额
		  }catch(Exception e){
		    System.out.println(e);
		  }
		    JSONArray jsonArray=new JSONArray();  //json数据集
			JSONObject json=new JSONObject();
			json.accumulate("balance", balance);
			json.accumulate("balance2", balance2);
			jsonArray.add(json);
			result = jsonArray;
			return getReturn();	
	}
	//-------------------------------------------------短信结束------------------------
	public String update(){

		List<Object> proList = (List<Object>) data.get("accountList");//获取到联系人对象集合
		List<Object> newList = new ArrayList<Object>();//创建一个新的集合存放新的联系人数据
		for (int i = 0; i <proList.size(); i++) {
			//集合中存放的为LinkedHashMap对象，每次循环取出一个
			LinkedHashMap<String, String> maps = (LinkedHashMap<String, String>) proList.get(i);
			maps.put("table", "newsAccount");
			maps.put("mainTable", "news");
			maps.put("fk", "newsId");
			maps.put("hasReading", "0");
			newList.add(maps);//将新的map数据放入新的集合中
		}
		data.remove("accountList");
		//放入新的list集合
		data.put("accountList", newList);
		
		return super.getReturnString(super.update());
		
	}
	
	public String delete(){
		
		return super.getReturnString(super.delete());

	}
	
	public String checkDuplicate(){
		
		Map map = new HashMap();
		return super.getReturnString(super.checkDuplicate(map));

	}
	public String getCanReadNews(){
		session=request.getSession();
		String sql="select top 100 percent n.*,a.hasReading ,a.readTime,s.name typename,b.name creator from news n,newsaccount a,syscategory s,sysaccount b where a.newsid=n.id and n.accountId=b.id and n.typeId=s.id and a.accountId="+((AccountBean)session.getAttribute("accountBean")).getId()
				+"	order by n.id desc";
		if (this.map.containsKey("typeIdTREE")||this.map.containsKey("titleLIKE")||this.map.containsKey("createTimeGE")||this.map.containsKey("createTimeLE")){
			sql = "select * from ( "+sql+" ) na where 1=1 "+ getSearchStr(this.map);
			}
		excuteSQLPageQuery(sql);
		result = getRows();
		return getReturn();
	}
	public void doRead(){
		String newsId=request.getParameter("newsId");
		session=request.getSession();
		String sql="update newsAccount set hasReading=1 ,readTime='"+DateFormat.getNowTime()+"' where accountId="+((AccountBean)session.getAttribute("accountBean")).getId()+" and newsId="+newsId ;
		//System.out.println(sql);
		super.update(sql);
	}

	public String getNewBeans(){ 
		this.session = request.getSession();
		//获取当前登陆用户的id
		String creatid = ((AccountBean)session.getAttribute("accountBean")).getId();
		String sql = "select top 100 percent s.*,ns.hasReading from News s  JOIN \n" +
				"newsAccount ns on s.ID=ns.newsId where s.ID \n" +
				"in(select n.newsId from newsAccount n where n.accountid='"+creatid+"') and ns.accountId='"+creatid+"'";
//		String sql = super.search(table, view, top, map);
		if (!DataFormat.booleanCheckNull(sort))
			sql += " order by " + sort + " " + DataFormat.objectCheckNull(order);
		else
			sql += " order by s.id desc";
		excuteSQLPageQuery(sql);
		result = getRows();
		return getReturn();
	}
	public String getNews(){
		String id = (String) this.data.get("id");
		String sql ="SELECT s.*,a.name ,b.name as newname from newsmessage s JOIN sysACCOUNT a on a.id=s.accountId JOIN  SysCategory b on b.id=s.typeId where s.ID='"+id+"'";
		DataBase dbs = null;
		//创建map集合用于存放查询出的数据
		Map<String, String> maps = new HashMap<String, String>();
		try {
			dbs = new DataBase();
			dbs.connectDb();
			ResultSet rs = dbs.queryAll(sql.toString());
			//遍历结果集，取出相应的数据
			while((rs!=null)&&(rs.next())){
				maps.put("title", rs.getString(2));
				maps.put("content", rs.getString(4));
				String time = rs.getString(5).split(" ")[0];
				maps.put("createtime",time);
				maps.put("creatname", rs.getString(10));
				maps.put("type", rs.getString(8));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		//将map转为json字符串返回给前台
				return callback + "(" + JsonUtil.encodeObject2Json(maps) + ")";
	}
	

	public String getNewsMessageSearch(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from newsmessage t where 1=1");
    	sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountId"));
        sql.append(" or t.accountId ='"+selfId+"')");

		sql.append(super.getSearchStr(super.map));
		sql .append( " order by id desc");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	
	/**
	 * 	查出最新10条新闻记录首页显示
	 * */
	public String getHomePageNewsContent(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 10  t.id,t.title,t.typename,t.account,convert(varchar(10),t.createtime,121) createtime  from newsmessage t where 1=1");
    	sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountId"));
        sql.append(" or t.accountId ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		sql .append( " order by createTime desc");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	
}
