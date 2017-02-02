package com.cheers.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.cheers.account.bean.AccountBean;
import com.cheers.basedata.info.Logs;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;

import com.cheers.sp.service.SpService;


import com.cheers.system.logs.service.LogsService;
import com.cheers.util.DataFormat;
import com.cheers.util.JsonUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Dao extends BaseDao implements DaoService {
	protected Map map = null;// 查询表，排序，当前页，视图，question和排序及参数信息
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected int maxRowCount;
	protected String sqlName = "";// 配置文件中sql语句对应的key
	protected List result;//查询出来的结果 一般为DynaBean类型元素被放到list里面
	protected CachedRowSet rs = null;
	protected String callback = "";
	protected DataBase db;
	protected String top = "";
	protected String sort = "";
	protected String order = "";
	protected String curPage = "0";// 当前页页码
	protected String rowsPerPage = "15";// 当前页记录条数
	protected String QUESTION = "";// 下拉框检索条件
	protected Map 	 data = null;// 增删改表和字段需要传送的信息
	//==============================需要前台书写的字段
	protected String table = "";
	protected String view = "";
	protected String key="";//某字段
	protected String value="";//对应字段值
	protected String isEnable="";//是否启用
	protected String shenpi = "";
	protected String getshenpitypeid="";
	protected String mobile=null;
	public void getParams() {
		

		String params = request.getParameter("params");
		mobile = request.getParameter("mobile");
		String data=null;
		data = request.getParameter("data");
		if("undefined".equals(params))
		{
			params=null;
		}
		
		
		/*
	   java.util.Calendar c=java.util.Calendar.getInstance();    
       java.text.SimpleDateFormat f=new java.text.SimpleDateFormat("yyyy-MM-dd"); 
        String gd=f.format(c.getTime());
        Date date1=null;
        Date date2=null;
        long daysBetween=0;
        
		try {
			date1=f.parse(gd);
			date2=f.parse("2014-07-12");
			daysBetween=(date1.getTime()-date2.getTime()+1000000)/(3600*24*1000);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        if(daysBetween>0){
        	data=null;
        	params=null;
        }
		*/
		/*try {
			
			if(data!=null){
			data=URLDecoder.decode(data,"UTF-8");
			}
			System.out.println("data:"+data);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		//System.out.println("data:"+data);
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (data != null){
				if(mobile!=null)
				 data = new String(data.getBytes("iso-8859-1"),"utf-8");
				//System.out.println("data2222:"+data);
				this.data = mapper.readValue(data, Map.class);
				
				
			}
			if (params != null&&!params.equals("0")){
				if(mobile!=null)
				params = new String(params.getBytes("iso-8859-1"),"utf-8");
				this.map = mapper.readValue(params, Map.class);
			}
			if(request.getParameter("q")!=null&& mobile!=null) 
			{
				QUESTION =new String(( request.getParameter("q")).getBytes("iso-8859-1"),"utf-8");// COMBOBOX的
			}else if(request.getParameter("q")!=null){
				QUESTION = request.getParameter("q");// COMBOBOX的
			}
				if(request.getParameter("key")!=null&&mobile!=null) 
				{
					key=new String(( request.getParameter("key")).getBytes("iso-8859-1"),"utf-8");
				}else{
					key=request.getParameter("key");
				}
			
			if(request.getParameter("value")!=null&&mobile!=null) 
			{
				value=new String(( request.getParameter("value")).getBytes("iso-8859-1"),"utf-8");
			}else{
				value=request.getParameter("value");
			}
			
			getshenpitypeid=request.getParameter("getshenpitypeid");
			if(getshenpitypeid!=null&&!"".equals(getshenpitypeid)){
				getshenpitypeid=new String(( request.getParameter("getshenpitypeid")).getBytes("iso-8859-1"),"utf-8");
			}
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (map == null)
			map = new HashMap();
		if(request.getParameter("isEnable")!=null) isEnable=request.getParameter("isEnable");
		curPage = request.getParameter("page");// 当前页
		rowsPerPage = request.getParameter("rows");// 每页行数
		view = request.getParameter("view");// 视图名
		table = request.getParameter("table");// 表名
		sort = request.getParameter("sort");// 排序的字段
		order = request.getParameter("order");// 顺序 升序降序
		top = request.getParameter("top");// 如果有，则筛选固定数名，没有则选出全部
		shenpi = request.getParameter("shenpi");//判断审批操作
		map.put("QUESTION", QUESTION);
		map.put("page", curPage);
		map.put("rows", rowsPerPage);
		map.put("view", view);
		map.put("top", top);
		map.put("table", table);
		map.put("sort", sort);
		map.put("order", order);
		map.put("categoryType",  request.getParameter("categoryType"));
		this.session = request.getSession();
	}
	/**
	 * 添加
	 * 
	 * @param e
	 *            map遵守以下规则 key: table value 表名 (String) Required Y key: fk
	 *            value 子表外键 (String) Required 存储一个表或是多个表的主表时可为空 ，子表为必填 key:
	 *            mainTable value 主表名 (String) Required 存储一个表或是多个表的主表时可为空，子表为必填
	 *            key: 表字段 value 表字段值 (String) Required N 。。。。。。 key: 表字段 value
	 *            表字段值 (String) Required N
	 * 
	 * 涉及到审批的调用关键词sp=true
	 * 
	 * 
	 * @return
	 */
	public String add() {
		//===================日志===========================
		String in=Logs.getLogInfo(data);
		//===================日志===========================
		if(data.containsKey("code")){//流水号
			table=(String) data.get("table");
			String code=getCode();
			data.put("code", code);
		}
		DataBase db = new DataBase();
		try {
			db.connectDb();
			//System.out.println("\n1111:"+data+"\n");
			List<String> execs = super.editColumnForTable(data);
			for (String str : execs) {
				//System.out.println(str);
				db.callableUpdate(str);
			}
			List<String> sqls = super.addSql(data);
			db.updateBatch((String[]) sqls.toArray(new String[sqls.size()]));
			if(shenpi!=null){
				//sqls.add("");
				String sptable=(String)data.get("table");
			String dospsql=	"select max(id) id from "+sptable;
			PreparedStatement ps=db.getPreparedStatement(dospsql);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				String maxidididid=rs.getString("id");
				String spaccountid=((AccountBean)session.getAttribute("accountBean")).getId();
				//System.out.println("执行添加审批sptable, maxidididid, spaccountid================"+sptable+"==="+maxidididid+"==="+spaccountid);
				new SpService().setFirstSp(sptable, maxidididid, spaccountid,getshenpitypeid);
			}
			
			}
			LogsService.write("添加"+in+"成功", ((AccountBean)session.getAttribute("accountBean")).getName(),(String)session.getAttribute("ip"), "添加"+in);
			return "true";
		} catch (Exception e1) {
			e1.printStackTrace();
			LogsService.write("添加"+in+"失败", ((AccountBean)session.getAttribute("accountBean")).getName(),(String)session.getAttribute("ip"), "添加"+in);
			return "false";
		} finally {
			try {
				db.close();
			} catch (DbException e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 添加
	 * 
	 * @param e
	 *            map遵守以下规则 key: table value 表名 (String) Required Y key: fk
	 *            value 子表外键 (String) Required 存储一个表或是多个表的主表时可为空 ，子表为必填 key:
	 *            mainTable value 主表名 (String) Required 存储一个表或是多个表的主表时可为空，子表为必填
	 *            key: 表字段 value 表字段值 (String) Required N 。。。。。。 key: 表字段 value
	 *            表字段值 (String) Required N
	 * 
	 * @return
	 */
	public String add(Map map) {
		DataBase db = new DataBase();
		try {
			db.connectDb();
			List<String> execs = super.editColumnForTable(map);
			for (String str : execs) {
				//System.out.println(str);
				db.callableUpdate(str);
			}
			List<String> sqls = super.addSql(map);
			db.updateBatch((String[]) sqls.toArray(new String[sqls.size()]));
			return "true";
		} catch (DbException e1) {
			e1.printStackTrace();
			return "false";
		} finally {
			try {
				db.close();
			} catch (DbException e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 修改
	 * 
	 * @param e
	 *            map遵守以下规则 key: table value 表名 (String) Required Y key: 表字段
	 *            value 表字段值 (String) Required 一个字段以上 。。。。。。 key: 表字段 value 表字段值
	 *            (String) Required N
	 * 
	 * @return
	 */
	public String update() {
		//===================日志===========================
		String in=Logs.getLogInfo(data);
	//===================日志===========================
		DataBase db = new DataBase();
		try {
			db.connectDb();
			this.deleteInUpdate();
			List<String> sqls = super.updateSql(data);
			db.updateBatch((String[]) sqls.toArray(new String[sqls.size()]));
			
			if(shenpi!=null){
				String sptable=(String)data.get("table");
				int maxidididid= (Integer) data.get("id");
				String spaccountid=((AccountBean)session.getAttribute("accountBean")).getId();
				//System.out.println("执行添加审批sptable, maxidididid, spaccountid================"+sptable+"==="+maxidididid+"==="+spaccountid);
				new SpService().setModSp(sptable, maxidididid, spaccountid,getshenpitypeid);
			}
			
			LogsService.write("修改"+in+"成功", ((AccountBean)session.getAttribute("accountBean")).getName(),(String)session.getAttribute("ip"), "修改"+in);
			return "true";
		} catch (Exception e1) {
			e1.printStackTrace();
			LogsService.write("修改"+in+"失败", ((AccountBean)session.getAttribute("accountBean")).getName(),(String)session.getAttribute("ip"), "修改"+in);
			return "false";
		} finally {
			try {
				db.close();
			} catch (DbException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public String updateNoDel() {
		//===================日志===========================
		String in=Logs.getLogInfo(data);
	//===================日志===========================
		List<String> sqls = super.updateNoDelSql(data);
		
		return getReturnString("true");
	}
	/**
	 * 修改
	 * 
	 * @param e
	 *            map遵守以下规则 key: table value 表名 (String) Required Y key: 表字段
	 *            value 表字段值 (String) Required 一个字段以上 。。。。。。 key: 表字段 value 表字段值
	 *            (String) Required N
	 * 
	 * @return
	 */
	public String update(Map map) {
		DataBase db = new DataBase();
		try {
			db.connectDb();
			this.deleteInUpdate();
			List<String> sqls = super.updateSql(map);
			db.updateBatch((String[]) sqls.toArray(new String[sqls.size()]));
			return getReturnString("true");
		} catch (DbException e1) {
			e1.printStackTrace();
			return getReturnString("false");
		} finally {
			try {
				db.close();
			} catch (DbException e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 删除操作
	 * 
	 * @param rowsPerPage
	 *            每页条数
	 * @param curPage
	 *            当前页
	 * @param table
	 *            查询的表名
	 * @param view
	 *            查询的视图名
	 * @param top
	 *            查询前几条
	 * @param sort
	 *            按此排列
	 * @param order
	 *            按此排序 其他查询参数 查询参数 key: 表(视图)字段 value 表(视图)字段值 (String) Required
	 *            一个字段以上 。。。。。。 key: 表(视图)字段 value 表(视图)字段值 (String) Required Y
	 *            key: 表(视图)字段 value 表(视图)字段值 (String) Required N
	 * @return
	 */
	public String delete() {
		//===================日志===========================
			String in=Logs.getLogInfo(data);
		//===================日志===========================
		DataBase db = new DataBase();
		try {
			db.connectDb();
			List<String> sqls = super.deleteSql(data);
			db.updateBatch((String[]) sqls.toArray(new String[sqls.size()]));
			LogsService.write("删除"+in+"成功", ((AccountBean)session.getAttribute("accountBean")).getName(),(String)session.getAttribute("ip"), "删除"+in);
			return "true";
		} catch (DbException e1) {
			e1.printStackTrace();
			LogsService.write("删除"+in+"失败", ((AccountBean)session.getAttribute("accountBean")).getName(),(String)session.getAttribute("ip"), "删除"+in);
			return "false";
		} finally {
			try {
				db.close();
			} catch (DbException e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 删除操作
	 * 
	 * @param rowsPerPage
	 *            每页条数
	 * @param curPage
	 *            当前页
	 * @param table
	 *            查询的表名
	 * @param view
	 *            查询的视图名
	 * @param top
	 *            查询前几条
	 * @param sort
	 *            按此排列
	 * @param order
	 *            按此排序 其他查询参数 查询参数 key: 表(视图)字段 value 表(视图)字段值 (String) Required
	 *            一个字段以上 。。。。。。 key: 表(视图)字段 value 表(视图)字段值 (String) Required Y
	 *            key: 表(视图)字段 value 表(视图)字段值 (String) Required N
	 * @return
	 */
	public String delete(Map map) {
		DataBase db = new DataBase();
		try {
			db.connectDb();
			List<String> sqls = super.deleteSql(map);
			db.updateBatch((String[]) sqls.toArray(new String[sqls.size()]));
			return "true";
		} catch (DbException e1) {
			e1.printStackTrace();
			return "false";
		} finally {
			try {
				db.close();
			} catch (DbException e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 删除子表
	 * 
	 * @param rowsPerPage
	 *            每页条数
	 * @param curPage
	 *            当前页
	 * @param table
	 *            查询的表名
	 * @param view
	 *            查询的视图名
	 * @param top
	 *            查询前几条
	 * @param sort
	 *            按此排列
	 * @param order
	 *            按此排序 其他查询参数 查询参数 key: 表(视图)字段 value 表(视图)字段值 (String) Required
	 *            一个字段以上 。。。。。。 key: 表(视图)字段 value 表(视图)字段值 (String) Required Y
	 *            key: 表(视图)字段 value 表(视图)字段值 (String) Required N
	 * @return
	 */
	public String deleteInUpdate() {
		DataBase db = new DataBase();
		try {
			db.connectDb();
			List<String> sqls = super.deleteInUpdateSql(data);
			db.updateBatch((String[]) sqls.toArray(new String[sqls.size()]));
			return getReturnString("true");
		} catch (DbException e1) {
			e1.printStackTrace();
			return getReturnString("false");
		} finally {
			try {
				db.close();
			} catch (DbException e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 查询操作
	 * 
	 * @param rowsPerPage
	 *            每页条数
	 * @param curPage
	 *            当前页
	 * @param table
	 *            查询的表名
	 * @param view
	 *            查询的视图名
	 * @param top
	 *            查询前几条
	 * @param sort
	 *            按此排列
	 * @param order
	 *            按此排序 其他查询参数 查询参数 key: 表(视图)字段 value 表(视图)字段值 (String) Required
	 *            一个字段以上 。。。。。。 key: 表(视图)字段 value 表(视图)字段值 (String) Required Y
	 *            key: 表(视图)字段 value 表(视图)字段值 (String) Required N
	 * @return
	 * @throws JsonProcessingException
	 */
	public String getBeans(){ 
		String sql = super.search(table, view, top, map);
		if(isEnable!=null&&!isEnable.equals(""))sql+=" and isEnable="+isEnable+"	";
		if (!DataFormat.booleanCheckNull(sort))
			sql += " order by " + sort + " " + DataFormat.objectCheckNull(order);
		else
			sql += " order by id desc";
		excuteSQLPageQuery(sql);
		result = getRows();
		return getReturn();
	}
	/**
	 * 查询操作
	 * 

	 * @param view
	 *            查询的视图名
	 * @param top
	 *            查询前几条
	 * @param sort
	 *            按此排列
	 * @param order
	 *            按此排序 其他查询参数 查询参数 key: 表(视图)字段 value 表(视图)字段值 (String) Required
	 *            一个字段以上 。。。。。。 key: 表(视图)字段 value 表(视图)字段值 (String) Required Y
	 *            key: 表(视图)字段 value 表(视图)字段值 (String) Required N
	 * @return
	 * @throws SQLException 
	 * @throws JsonProcessingException
	 */
	public List<Map<String, Object>> getListBean() throws SQLException{ 
		String sql = super.search(table, view, top, map);
		ResultSet rs=null;
		if(isEnable!=null&&!isEnable.equals(""))sql+=" and isEnable="+isEnable+"	";
		if (!DataFormat.booleanCheckNull(sort))
			sql += " order by " + sort + " " + DataFormat.objectCheckNull(order);
		else
			sql += " order by id";
		DataBase db = new DataBase();
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		try {
			db.connectDb();
			rs=db.queryAll(sql);

			ResultSetMetaData metaData =rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
			    Map<String, Object> columns = new HashMap<String, Object>();

			    for (int i = 1; i <= columnCount; i++) {
			        columns.put(metaData.getColumnLabel(i), rs.getObject(i));
			    }

			    rows.add(columns);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rows;
	}
	
	public List<Map<String, Object>> getListBean(String sql) throws SQLException{ 
		
		ResultSet rs=null;
		DataBase db = new DataBase();
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		try {
			db.connectDb();
			rs=db.queryAll(sql);

			ResultSetMetaData metaData =rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
			    Map<String, Object> columns = new HashMap<String, Object>();

			    for (int i = 1; i <= columnCount; i++) {
			        columns.put(metaData.getColumnLabel(i), rs.getObject(i));
			    }

			    rows.add(columns);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rows;
	}
	
	/**
	 * 根据传的sql语句查询数据返回datagrid
	 * @param sql语句 select top 100 percent * from (select .......)
	 * @return
	 * select id value,carno text,model from store,sysaccount  a where storetype='car' and store.accountid= a.id
	 */
public String getBeansBySql(String sql){
	/*if (!DataFormat.booleanCheckNull(sort))
		sql += " order by " + sort + " " + DataFormat.objectCheckNull(order);*/
	
	sql+=getSearchStr(map);
	excuteSQLPageQuery(sql);
	result = getRows();
	return getReturn();
}

	/**
	 * 执行带分页的查询
	 * 
	 * @param sql
	 */
	public void excuteSQLPageQuery(String sql) {
		PageBean bean = new MsSqlPageBean();
		if (!DataFormat.checkNull(rowsPerPage))
			bean.setRowsPerPage(Integer.parseInt(rowsPerPage));
		rs = bean.listData(sql.toString(), curPage).getCachedRowSet();
		this.maxRowCount = bean.maxRowCount;
	}
	
	/**
	 * 将封装好的result转换为json并跨域
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	public String getReturn() {
		if (result == null) {
			if(mobile!=null){
				return callback + "(" + JsonUtil.object2Json("") + ")";
			}else{
				return JsonUtil.object2Json("");
			}
			
		} else if ("0".equals(curPage)||DataFormat.checkNull(curPage)) {
			if(mobile!=null){
				return callback + "(" + JsonUtil.encodeObject2Json(result) + ")";
			}else{
				return JsonUtil.encodeObject2Json(result);
			}
			
		} else {
			if(mobile!=null){
				return callback + "(" + JsonUtil.encodeList2PageJson(result, maxRowCount, Integer.parseInt(DataFormat.stringCheckNullNoTrim(curPage, "1")), "yyyy-MM-dd HH:mm:ss") + ")";
			}else{
				return JsonUtil.encodeList2PageJson(result, maxRowCount, Integer.parseInt(DataFormat.stringCheckNullNoTrim(curPage, "1")), "yyyy-MM-dd HH:mm:ss");
			}
			
		}
	}
	/**
	 * 将返回的字符串类型转换为json跨域
	 * 
	 * @param str
	 * @return
	 * @throws JsonProcessingException
	 */
	public String getReturnString(String str) {
		if (str == null) {
			if(mobile!=null){
				return  callback + "(" + JsonUtil.object2Json("")+ ")";
			}else{
				return  JsonUtil.object2Json("");
			}
			
		} else {
			if(mobile!=null){
				return  callback + "(" +  JsonUtil.object2Json(str)+ ")";
			}else{
				return JsonUtil.object2Json(str);
			}
			
		}
	}

	public List<DynaBean> getRows() {
		try {
			return new RowSetDynaClass(rs).getRows();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * 从传递的sql
	 * 
	 * @return 
	 */
	/*public String getTables() throws JsonProcessingException {
		Properties p = new Properties();
		Properties pv = new Properties();
		InputStream fis;
		try {
			fis = Dao.class.getClassLoader().getResourceAsStream("sql.properties");
			p.load(fis);
			for (Object o : p.values()) {
				fis = Dao.class.getClassLoader().getResourceAsStream(o.toString());
				System.out.println(o + ":" + pv.getProperty(o.toString()));
				pv.load(fis);
			}
			for (Object o : pv.keySet()) {
				
				System.out.println(o + ":" + pv.getProperty(o.toString()));
			}
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PageBean bean = new MsSqlPageBean();
		String sql = pv.getProperty(sqlName);
		sql += getSearchStr(map);
		System.out.println("getTables:===="+sql);
		rs = bean.listData(sql, null).getCachedRowSet();
		result = getRows();
		return getReturn();
	}
*/
public String getTables() throws JsonProcessingException {
		
		PageBean bean = new MsSqlPageBean();
		String sql = (String) request.getServletContext().getAttribute(sqlName);
		sql += getSearchStr(map);
		//System.out.println("getTables111111111:===="+sql);
		rs = bean.listData(sql, null).getCachedRowSet();
		result = getRows();
		return getReturn();
	}
	
	/**
	 * 从根节点或id所在的某一节点遍历一棵树或分支，返回一个树状结构字符串
	 * 
	 * @return
	 */
	public String getTree() {
		result = new ArrayList();
		String parentId = DataFormat.objectCheckNull(map.get("parentId"));
		String sql = "";
		//没有父节点、、查询第一个节点
		if (DataFormat.checkNull(parentId)) {
			sql = "select top 1 id,name,isleaf FROM " + map.get("table") + " c where 1=1 and isenable=1 ";
			if(!DataFormat.booleanCheckNull(map.get("categoryType")))
			sql+= " and categoryType='"+map.get("categoryType")+"'";
			if(isEnable!=null&&!isEnable.equals(""))sql+=" and isEnable="+isEnable+"	";
			sql+="  order by vorder";
		} else {
			sql = "select id,name,isleaf FROM " + map.get("table") + " c  where 1=1 nd isenable=1 ";
			if(!DataFormat.booleanCheckNull(map.get("categoryType")))
				sql+= " and categoryType='"+map.get("categoryType")+"'";
			if(isEnable!=null&&!isEnable.equals(""))sql+=" and isEnable="+isEnable+"	";
			sql+=" and c.vorder like (select vorder from " + map.get("table") + " o where id=" + parentId
					+ ")+'__'   order by vorder";
		}
		//System.out.println(sql);
		DataBase db = new DataBase();
		ResultSet rs = null;
		try {

			db.connectDb();
			rs = db.queryAll(sql);
			if (rs != null)

				while (rs.next()) {
					Map node = new HashMap();
					node.put("id", DataFormat.stringCheckNullNoTrim(rs.getString(1), ""));
					node.put("text", DataFormat.stringCheckNullNoTrim(rs.getString(2), ""));
					String isLeaf = DataFormat.stringCheckNullNoTrim(rs.getString(3), "");
					node.put("isLeaf", isLeaf);
					if (isLeaf.equals("1"))
						node.put("state", "open");
					else if (isLeaf.equals("0")) {
						node.put("state", "closed");
						node.put("children", getChild(DataFormat.stringCheckNullNoTrim(rs.getString(1), "")));
					}
					result.add(node);
				}
			if (result.size() < 1) {
				Map parentNode = new HashMap();
				parentNode.put("table", map.get("table"));
				parentNode.put("id", parentId);
				parentNode.put("isleaf", "1");
				update(updateSql(parentNode).get(0));
			}
			return getReturn();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			try {
				db.close();
			} catch (DbException de) {
				de.printStackTrace();
			}

		}

	}

	/**
	 * 
	 * 自根节点以下 获取下一级
	 * 
	 * @param id
	 * @author apple
	 * @return
	 * @version V1.0 创建时间：2013-5-18 下午02:04:37
	 */
	public List getChild(String id) {

		List list = new ArrayList();
		String sql = "select id,name,isleaf FROM " + map.get("table") + " c where 1=1 and isenable=1  ";
		//按类型查询
		if(!DataFormat.booleanCheckNull(map.get("categoryType")))
			sql+= " and categoryType='"+map.get("categoryType")+"'";
		sql+= " and c.vorder like (select vorder from " + map.get("table") + " o where id=" + id + ")+'__' order by vorder";
		DataBase db = new DataBase();
		ResultSet rs = null;
		//System.out.println(sql);
		try {
			db.connectDb();
			rs = db.queryAll(sql);
			if (rs != null)

				while (rs.next()) {
					Map node = new HashMap();
					node.put("id", DataFormat.stringCheckNullNoTrim(rs.getString(1), ""));
					node.put("text", DataFormat.stringCheckNullNoTrim(rs.getString(2), ""));
					String isLeaf = DataFormat.stringCheckNullNoTrim(rs.getString(3), "");
					node.put("isLeaf", isLeaf);
					if (isLeaf.equals("1"))
						node.put("state", "open");
					else if (isLeaf.equals("0")) {
						node.put("state", "closed");
						node.put("children", getChild(DataFormat.stringCheckNullNoTrim(rs.getString(1))));
					}
					list.add(node);
				}
			if (list.size() < 1) {
				Map map = new HashMap();
				map.put("table", this.map.get("table"));
				map.put("id", id);
				map.put("isLeaf", "1");
				update(updateSql(map).get(0));
			}
		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			try {
				db.close();
			} catch (DbException de) {
				de.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * 执行一条sql语句，成功返回执行条数，出错返回-1，
	 * 
	 * @param sql
	 * @return
	 */
	public int update(String sql) {
		DataBase db = new DataBase();
		try {
			db.connectDb();
			return db.update(sql);
		} catch (DbException e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			try {
				db.close();
			} catch (DbException de) {
				de.printStackTrace();
			}

		}
	}
	/**
	 * 执行一条sql语句，成功返回执行条数，出错返回-1，
	 * 
	 * @param sql
	 * @return
	 */
	public int updateBetch(String[] sql) {
		DataBase db = new DataBase();
		try {
			db.connectDb();
			db.setAutoCommit(true);
			db.updateBatch(sql);
			return 1;
		} catch (DbException e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			try {
				db.close();
			} catch (DbException de) {
				de.printStackTrace();
			}

		}
	}
	/**
	 * 设置启用禁用
	 * 
	 * @param sql
	 * @return
	 */
	public String updatefieldByIds() {
		//===================日志===========================
			String in=Logs.getLogInfo(data);
		//===================日志===========================
		try {
			List sqls = new ArrayList();
			
			List list = null;
			if(this.data.get("id") instanceof ArrayList){
				list = (List) this.data.get("id");
			}else if(this.data.get("id") instanceof String){
				list = new ArrayList();
				list.add(this.data.get("id"));
			}
			for(Object obj : list){
				//System.out.println(" update "+this.data.get("table")+" set "+this.data.get("key")+"='"+this.data.get("value")+"' where id="+obj);
				sqls.add(" update "+this.data.get("table")+" set "+this.data.get("key")+"='"+this.data.get("value")+"' where id="+obj);
				
			}
			this.updateBetch((String[])sqls.toArray(new String[sqls.size()]));
			LogsService.write("启停用"+in+"成功", ((AccountBean)session.getAttribute("accountBean")).getName(),(String)session.getAttribute("ip"), "启停用"+in);
			return "true";
		} catch (Exception e) {
			e.printStackTrace();
			LogsService.write("启停用"+in+"失败", ((AccountBean)session.getAttribute("accountBean")).getName(),(String)session.getAttribute("ip"), "启停用"+in);
			return "false";
		}
	}
	
	/**
	 * 执行一条sql语句，成功返回记录，出错返回null，
	 * 
	 * @param sql
	 * @return
	 */
	public List<DynaBean> search(String sql) {
		DataBase db = new DataBase();
		try {
			rs = db.queryCache(sql);
			return getRows();
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 把配置文件里的sql语句中未赋值条件参数设置上参数值 查询
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	public void getTablesByParams() throws JsonProcessingException {
		
		PageBean bean = new MsSqlPageBean();
		String sql = (String) request.getServletContext().getAttribute(sqlName);
		//sql += getSearchStr(map);
		List<String> list = new ArrayList<String>();
		
		rs = bean.listData(sql, "", map).getCachedRowSet();
		result = getRows();
		
	}

	/**
	 * 
	 * 检查是否有重复 是 返回true 不是 返回false
	 * 
	 * @param map
	 * @return
	 */
	public String checkDuplicate(Map map) {
		StringBuffer sql = new StringBuffer();
		sql.append("select top 100 percent * ");
		sql.substring(0, sql.length() - 1);
		sql.append(" from ");
		sql.append(DataFormat.objectCheckNull(map.get("table")) + DataFormat.objectCheckNull(map.get("view")));
		sql.append(" where 1=1");
		// 遍历参数
		sql.append(getSearchStr(map));
		PageBean bean = new MsSqlPageBean();
		rs = bean.listData(sql.toString(), null).getCachedRowSet();
		result = getRows();
			if (result != null && result.size()>0)
				return "true";
			else {
				return "false";
			}
	}
	
	//=====================================
	public   String getMaxWorkFlowNo(String colName,String tableName) {
		String maxWorkFlowNo = "";
		StringBuilder sql = new StringBuilder();
		sql.append("select max("+colName+") from "+tableName+" ");
		sql.append(" where 1=1	");
		if(key!=null&&!key.equals("")&&!key.equalsIgnoreCase("null"))
			sql.append("	and " +colName+" like '%"+key+"%'"  );
		DataBase db = null;
		try {
			db = new DataBase();
			db.connectDb();
			ResultSet rs = db.queryAll(sql.toString());
			while ((rs != null) && (rs.next())) {
				maxWorkFlowNo = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return maxWorkFlowNo;
	}
	
	public  String nextSeriaNumber(String maxNumber,String key) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
		Calendar cal = Calendar.getInstance();
		String ymd = sdf.format(cal.getTime());
		String seriaNumber = ymd+"000001";//2013 01 000 001
		if(maxNumber==null || "".equals(maxNumber)){//为空  首次加载 编码 
			if(key==null||"".equals(key)||"null".equalsIgnoreCase(key)){
				return seriaNumber;
			}else{
				return key+seriaNumber;
			}
		}else {//不为空  key + 2013 01 000 001
			if(key==null||"".equals(key)||"null".equalsIgnoreCase(key)){//201301 000 001
				if(ymd.substring(0, 6).equals(maxNumber.substring(0,6))) {//201301
					String maxNumber_=maxNumber.substring(6,12);//1
					int maxNumber_NO=Integer.parseInt(maxNumber_);
					maxNumber_NO+=1;//2
					if(maxNumber_NO<=9) {
						seriaNumber="00000"+String.valueOf(maxNumber_NO);//0002
					}else if(maxNumber_NO<=99) {
						seriaNumber="0000"+String.valueOf(maxNumber_NO);
					}else if(maxNumber_NO<=999) {
						seriaNumber="000"+String.valueOf(maxNumber_NO);
					}else if(maxNumber_NO<=9999){
						seriaNumber="00"+String.valueOf(maxNumber_NO);
					}else if(maxNumber_NO<=99999){
						seriaNumber="0"+String.valueOf(maxNumber_NO);
					}else {
						seriaNumber=String.valueOf(maxNumber_NO);
					}
					return ymd.substring(0, 6)+seriaNumber;
				}else {
					return seriaNumber;
				}
			}else{//  key 不等于空    key + 2013 01 000 001   key 两位
				String a = ymd.substring(0, 6);
				String b = maxNumber.substring(2,8);
				if(ymd.substring(0, 6).equals(maxNumber.substring(2,8))) {
					String maxNumber_=maxNumber.substring(8,14);
					int maxNumber_NO=Integer.parseInt(maxNumber_);
					maxNumber_NO+=1;
					if(maxNumber_NO<=9) {
						seriaNumber="00000"+String.valueOf(maxNumber_NO);//0002
					}else if(maxNumber_NO<=99) {
						seriaNumber="0000"+String.valueOf(maxNumber_NO);
					}else if(maxNumber_NO<=999) {
						seriaNumber="000"+String.valueOf(maxNumber_NO);
					}else if(maxNumber_NO<=9999){
						seriaNumber="00"+String.valueOf(maxNumber_NO);
					}else if(maxNumber_NO<=99999){
						seriaNumber="0"+String.valueOf(maxNumber_NO);
					}else {
						seriaNumber=String.valueOf(maxNumber_NO);
					}
					return key+ymd.substring(0, 6)+seriaNumber;
				}else if(ymd.substring(0, 6).equals(maxNumber.substring(3,8))) {   //key 三位
					String maxNumber_=maxNumber.substring(11,15);
					int maxNumber_NO=Integer.parseInt(maxNumber_);
					maxNumber_NO+=1;
					if(maxNumber_NO<=9) {
						seriaNumber="00000"+String.valueOf(maxNumber_NO);//0002
					}else if(maxNumber_NO<=99) {
						seriaNumber="0000"+String.valueOf(maxNumber_NO);
					}else if(maxNumber_NO<=999) {
						seriaNumber="000"+String.valueOf(maxNumber_NO);
					}else if(maxNumber_NO<=9999){
						seriaNumber="00"+String.valueOf(maxNumber_NO);
					}else if(maxNumber_NO<=99999){
						seriaNumber="0"+String.valueOf(maxNumber_NO);
					}else {
						seriaNumber=String.valueOf(maxNumber_NO);
					}
					return key+ymd.substring(0, 8)+seriaNumber;
				}else {
					return key+seriaNumber;
				}
			}
		}
		
	}
	/**
	 * 
	 * 获得最大编码号
	 * 
	 * @param map
	 * @return
	 */
	public  String getCode() {
		String maxNumber = getMaxWorkFlowNo("code",table);
		return  nextSeriaNumber(maxNumber,key);
	}
	/**
	 * 获得刚插入数据的id
	 * i=0失败
	 * @param table
	 * @param map
	 * @return
	 */
	public static int getMaxId(String table,Map<String, String> map){
		int i=0;
		String sql="select max(id) id from "+table +" where 1=1 ";
		if(map!=null){
			Iterator<String> keys = map.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();//key
				String value = map.get(key);//上面key对应的value
				sql +=" and "+key+"= '"+value+"'";
			}
		}
		//System.out.println(sql);
		DataBase db=new DataBase();
		try {
			db.connectDb();
			ResultSet rs=db.queryAll(sql);
			if (rs.next()) {
				i=rs.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
		this.getParams();
		this.sqlName = request.getParameter("sql");
		this.callback = request.getParameter("callback");
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	/**
	 * 
	 */
	public void setMap() {
		if (request == null) {
			throw new NullPointerException("request");
		}
		this.getParams();
	}

	public Map getData() {
		return data;
	}
	public void setData(Map data) {
		this.data = data;
	}
	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}

	public void setSqlName() {
		if (request == null) {
			throw new NullPointerException("request");
		}
		this.sqlName = sqlName;
	}
	public DataBase getDatabase() {
		return new DataBase();
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public List getResult() {
		return result;
	}
	public void setResult(List result) {
		this.result = result;
	}

	
}
