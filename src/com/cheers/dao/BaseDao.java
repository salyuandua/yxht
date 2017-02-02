package com.cheers.dao;

import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;
import com.cheers.util.DataFormat;
import com.cheers.util.PageMethod;
import com.cheers.util.Type2Class;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseDao
{
  protected List<String> editColumnForTable(Map<String, ?> e)
  {
    List<String> result = new ArrayList();
    Set<String> set = e.keySet();

    for (String fieldName : set) {
      if (isValidAdd(fieldName)) {
        StringBuffer sql = new StringBuffer();
        sql.append("call EditcolumnForTable('");
        sql.append(e.get("table"));
        sql.append("','");
        sql.append(fieldName);
        sql.append("','nvarchar',200,3,null,null,null)");
        result.add(sql.toString());
      }
      if ("fk".equals(fieldName)) {
        StringBuffer sql = new StringBuffer();
        sql.append("call EditcolumnForTable('");
        sql.append(e.get("table"));
        sql.append("','");
        sql.append(e.get(fieldName));
        sql.append("','nvarchar',200,3,null,null,null)");
        result.add(sql.toString());
      }
    }

    for (String str : set) {
      if (str.endsWith("List")) {
        List<Map> list = (List<Map>)e.get(str);
        for (Map map : list) {
          result.addAll(editColumnForTable(map));
        }
      }
    }
    return result;
  }

  protected List<String> addSql(Map<String, ?> e)
  {
    List result = new ArrayList();
    StringBuffer sql = new StringBuffer();
    StringBuffer text = new StringBuffer();
    sql.append("insert into ");
    sql.append(e.get("table"));
    sql.append(" (");
    Set<String> set = e.keySet();
    text.setLength(0);
    for (String fieldName : set) {
      if (isValidAdd(fieldName, "id")) {
        text.append(fieldName);
        text.append(",");
      }
    }
    sql.append(text.substring(0, text.length() - 1));
    sql.append(") values (");
    text.setLength(0);
    for (String fieldName : set) {
      if (isValidAdd(fieldName, "id")) {
        text.append("'");
        text.append(e.get(fieldName));
        text.append("',");
      }
    }
    sql.append(text.substring(0, text.length() - 1));
    sql.append(")");
    System.out.println(sql);
    result.add(sql.toString());

    for (String str : set) {
      if (str.endsWith("List")) {
        List<Map> list = (List<Map>)e.get(str);
        for (Map map : list) {
          result.addAll(addChild(map));
        }
      }
    }
    return result;
  }

  protected List<String> addChildForUpdate(Map<String, ?> e, String mainTableId)
  {
    List result = new ArrayList();
    StringBuffer sql = new StringBuffer();
    Set<String> set = e.keySet();
    sql.append("insert into ");
    sql.append(e.get("table"));
    sql.append(" (");
    for (String fieldName : set) {
      if (isValidAdd(fieldName, "id")) {
        sql.append(fieldName);
        sql.append(",");
      }
    }

    sql.append(e.get("fk"));
    sql.append(") values (");
    for (String fieldName : set) {
      if (isValidAdd(fieldName)) {
        sql.append("'");
        sql.append(e.get(fieldName));
        sql.append("',");
      }
    }
    sql.append("  '" + mainTableId + "')");
    result.add(sql.toString());

    for (String str : set) {
      if (str.endsWith("List")) {
        List<Map> list = (List<Map>)e.get(str);
        for (Map map : list) {
          result.addAll(addChild(map));
        }
      }
    }
    return result;
  }

  protected List<String> addChild(Map<String, ?> e)
  {
    List result = new ArrayList();
    StringBuffer sql = new StringBuffer();
    Set<String> set = e.keySet();
    sql.append("insert into ");
    sql.append(e.get("table"));
    sql.append(" (");
    for (String fieldName : set) {
      if (isValidAdd(fieldName, "id")) {
        sql.append(fieldName);
        sql.append(",");
      }
    }

    sql.append(e.get("fk"));
    sql.append(") values (");
    for (String fieldName : set) {
      if (isValidAdd(fieldName)) {
        sql.append("'");
        sql.append(e.get(fieldName));
        sql.append("',");
      }
    }
    sql.append("  Ident_Current('" + e.get("mainTable") + "'))");
    result.add(sql.toString());

    for (String str : set) {
      if (str.endsWith("List")) {
        List<Map> list = (List<Map>)e.get(str);
        for (Map map : list) {
          result.addAll(addChild(map));
        }
      }
    }
    return result;
  }

  protected List<String> updateSql(Map<String, ?> e)
  {
    List result = new ArrayList();
    StringBuffer sql = new StringBuffer();
    StringBuffer text = new StringBuffer();
    sql.append("update ");
    sql.append(e.get("table"));
    sql.append(" set ");
    Set<String> set = e.keySet();
    for (String fieldName : set) {
      if (isValidAdd(fieldName, "id")) {
        text.append(fieldName + "='" + e.get(fieldName));
        text.append("',");
      }
    }

    sql.append(text.substring(0, text.length() - 1));
    sql.append(" where id=");
    sql.append(e.get("id"));
    result.add(sql.toString());
    System.out.println(sql);

    for (String str : set) {
      if (str.endsWith("List")) {
        List<Map> list = (List<Map>)e.get(str);
        for (Map map : list) {
          result.addAll(addChildForUpdate(map, e.get("id").toString()));
        }
      }
    }
    System.out.println(result);
    return result;
  }

  protected List<String> updateNoDelSql(Map<String, ?> e)
  {
    List result = new ArrayList();
    StringBuffer sql = new StringBuffer();
    StringBuffer text = new StringBuffer();
    sql.append("update ");
    sql.append(e.get("table"));
    sql.append(" set ");
    Set<String> set = e.keySet();
    for (String fieldName : set) {
      if (isValidAdd(fieldName, "id")) {
        text.append(fieldName + "='" + e.get(fieldName));
        text.append("',");
      }
    }
    sql.append(text.substring(0, text.length() - 1));
    sql.append(" where id=");
    sql.append(e.get("id"));
    result.add(sql.toString());
    System.out.println(sql);

    for (String str : set) {
      if (str.endsWith("List")) {
        List<Map> list = (List<Map>)e.get(str);
        for (Map map : list) {
          result.addAll(updateNoDelSql(map));
        }
      }
    }
    System.out.println("↓↓↓↓↓↓↓" + result);
    return result;
  }

  protected List<String> deleteSql(Map<String, ?> e)
  {
    List result = new ArrayList();
    String table = (String)e.get("table");
    if ((e.get("id") instanceof String)) {
      StringBuffer sql = new StringBuffer();
      sql.append("delete from ");
      sql.append(table);
      sql.append(" where id='");
      sql.append(e.get("id"));
      sql.append("'");
      result.add(sql.toString());
    } else if ((e.get("id") instanceof ArrayList))
    {
      List ids = (List)e.get("id");
      for (Iterator localIterator = ids.iterator(); localIterator.hasNext(); ) { Object str = localIterator.next();
        StringBuffer sql = new StringBuffer();
        sql.append("delete from ");
        sql.append(table);
        sql.append(" where id='");
        sql.append(str);
        sql.append("'");
        result.add(sql.toString());
      }
    }

    return result;
  }

  protected List<String> deleteInUpdateSql(Map<String, ?> e) {
    List result = new ArrayList();
    StringBuffer sql = new StringBuffer();

    Set<String> set = e.keySet();

    for (String str : set) {
      if (str.endsWith("List")) {
        List<Map> list = (List<Map>)e.get(str);
        for (Map map : list) {
          result.addAll(deleteChildSql(map, e.get("id").toString()));
        }
      }
    }
    return result;
  }
  protected List<String> deleteChildSql(Map<String, ?> e, String id) {
    List result = new ArrayList();
    StringBuffer sql = new StringBuffer();

    sql.append("delete from ");
    sql.append(e.get("table"));
    sql.append(" where " + e.get("fk") + "=");
    sql.append(id);
    System.out.println(sql + "=========");
    result.add(sql.toString());

    Set<String> set = e.keySet();

    for (String str : set) {
      if (str.endsWith("List")) {
        List<Map> list = (List<Map>)e.get(str);
        for (Map map : list) {
          result.addAll(deleteChildSql(map, e.get("id").toString()));
        }
      }
    }
    return result;
  }

  protected String search(String table, String view, String top, Map<String, ?> e)
  {
    StringBuffer sql = new StringBuffer();
    if (DataFormat.checkNull(top))
      sql.append("select top 100 percent ");
    else {
      sql.append("select top  " + top);
    }

    if (DataFormat.isEmpty(e.get("columns"))) {
      sql.append(" * ");
    } else {
      Map columns = (Map)e.get("columns");
      for (Iterator localIterator = columns.values().iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
        sql.append("   " + obj + ","); }
    }
    sql.substring(0, sql.length() - 1);
    sql.append(" from ");
    sql.append(DataFormat.objectCheckNull(table) + DataFormat.objectCheckNull(view));
    sql.append(" where 1=1");

    sql.append(getSearchStr(e));
    return sql.toString();
  }

  protected String searchSQL(Map<String, ?> e)
  {
    StringBuffer sql = new StringBuffer();
    if (DataFormat.booleanCheckNull(e.get("top")))
      sql.append("select top 100 percent ");
    else {
      sql.append("select top  " + e.get("top"));
    }

    if (DataFormat.isEmpty(e.get("columns"))) {
      sql.append(" * ");
    } else {
      Map columns = (Map)e.get("columns");
      for (Iterator localIterator = columns.values().iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
        sql.append("   " + obj + ","); }
    }
    sql.substring(0, sql.length() - 1);
    sql.append(" from ");
    sql.append(DataFormat.objectCheckNull(e.get("table")) + DataFormat.objectCheckNull(e.get("view")));
    sql.append(" where 1=1");

    sql.append(getSearchStr(e));
    return sql.toString();
  }

  protected String updateSQL(Map<String, ?> e)
  {
    StringBuffer sql = new StringBuffer();
    StringBuffer text = new StringBuffer();
    sql.append("update ");
    sql.append(DataFormat.objectCheckNull(e.get("table")));
    sql.append(" set ");
    Set<String> set = e.keySet();
    for (String fieldName : set) {
      if (isValidAdd(fieldName, "id")) {
        text.append(fieldName + "='" + e.get(fieldName));
        text.append("',");
      }
    }
    sql.append(text.substring(0, text.length() - 1));
    sql.append(" where id=");
    sql.append(e.get("id"));
    return sql.toString();
  }

  public String getSearchStr(Map params)
  {
    StringBuffer result = new StringBuffer();
    if (!DataFormat.booleanCheckNull(params)) {
      Set keys = params.keySet();
      Iterator it = keys.iterator();
      while (it.hasNext()) {
        String key = (String)it.next();
        if (key.endsWith("LIKE")) {
          result.append(getLikeStr(key.substring(0, key.length() - 4), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("RL")) {
          result.append(getEQStr(key.substring(0, key.length() - 2), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("LL")) {
          result.append(getEQStr(key.substring(0, key.length() - 2), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("EQ")) {
          result.append(getEQStr(key.substring(0, key.length() - 2), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("GE")) {
          result.append(getGEStr(key.substring(0, key.length() - 2), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("LE")) {
          result.append(getLEStr(key.substring(0, key.length() - 2), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("NG")) {
          result.append(getNGStr(key.substring(0, key.length() - 2), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("NL")) {
          result.append(getNLStr(key.substring(0, key.length() - 2), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("GT")) {
          result.append(getGTStr(key.substring(0, key.length() - 2), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("LT")) {
          result.append(getLTStr(key.substring(0, key.length() - 2), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("NE")) {
          result.append(getNEStr(key.substring(0, key.length() - 2), DataFormat.objectCheckNull(params.get(key))));
        } else if (key.equals("QUESTION")) {
          result.append(getLikeStr("name", DataFormat.objectCheckNull(params.get(key))));
        } else if (key.endsWith("LIKER")){
        	result.append(getrRLikeStr(key.substring(0, key.length() - 5), DataFormat.objectCheckNull(params.get(key))));
        }else if (key.endsWith("TREE")) {
          if (DataFormat.objectCheckNull(params.get(key)).equals("")) {
            continue;
          }
          String sql = "select id from dbo.SysCategory where id in( select id from dbo.SysCategory where id=" + 
            params.get(key) + " or parentid=" + params.get(key) + 
            " )or parentid in( " + 
            " select id from dbo.SysCategory " + 
            " where id=" + params.get(key) + " or parentid=" + params.get(key) + ")";

          result.append(" and " + key.substring(0, key.length() - 4) + " in (" + sql + ")");
        }
        else if (key.endsWith("TREES")) {//单部门 树 下级关系！
          result.append(getTREES(key.substring(0, key.length() - 5), DataFormat.objectCheckNull(params.get(key))));
        }else if (key.endsWith("TREESM")) {//选中单个部门 树 下级关系！处理 账户管理 里的多个 部门的 方法
            result.append(getTREESM(key.substring(0, key.length() - 6), DataFormat.objectCheckNull(params.get(key))));
        }
      }
    }
    return result.toString();
  }

  public String getTREES(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      String val = getidList(value);
      result.append(" and ");
      result.append(key);
      result.append(" in (");
      result.append(val.substring(0, val.length() - 1));
      result.append(" ) ");
    }
    return result.toString();
  }
  
  public String getTREESM(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      String val = getidListM(value);
      result.append(" and ");
      result.append(key);
      result.append(" in (");
      String a = val.substring(0, val.length() - 1);
      result.append(val.substring(0, val.length() - 1));
      result.append(" ) ");
    }
    return result.toString();
  }
  
  public String getidList(String val) {
    String ids = val + ",";
    String sql = "select id,isleaf FROM SYSTEM_TREE c where  parentid = " + val;
    DataBase db = new DataBase();
    ResultSet rs = null;
    try {
      db.connectDb();
      rs = db.queryAll(sql);
      if (rs != null)
        while (rs.next()) {
          String id = DataFormat.stringCheckNullNoTrim(rs.getString(1), "");
          String isLeaf = DataFormat.stringCheckNullNoTrim(rs.getString(2), "");
          if (isLeaf.equals("1"))
            ids = ids + id + ",";
          else if (isLeaf.equals("0"))
            ids = ids + getidList(id);
        }
    }
    catch (Exception de)
    {
      de.printStackTrace();
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException se) {
          se.printStackTrace();
        }
      try {
        db.close();
      } catch (DbException dbe) {
        dbe.printStackTrace();
      }

      if (rs != null)
        try {
          rs.close();
        } catch (SQLException se) {
          se.printStackTrace();
        }
      try {
        db.close();
      } catch (DbException de1) {
        de1.printStackTrace();
      }
    }
    finally
    {
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException se) {
          se.printStackTrace();
        }
      try {
        db.close();
      } catch (DbException de) {
        de.printStackTrace();
      }
    }
    return ids;
  }
  
  //获得 节点下的 所有 分公司
  public String getFgsIdsList(String val) {
	    String ids = val + ",";
	    String sql = "select id,isleaf,notetype FROM SYSTEM_TREE c where  parentid = " + val;
	    DataBase db = new DataBase();
	    ResultSet rs = null;
	    try {
	      db.connectDb();
	      rs = db.queryAll(sql);
	      if (rs != null)
	        while (rs.next()) {
	          String id = DataFormat.stringCheckNullNoTrim(rs.getString(1), "");
	          String isLeaf = DataFormat.stringCheckNullNoTrim(rs.getString(2), "");
	          String notetype = DataFormat.stringCheckNullNoTrim(rs.getString(3), "");
	          
	          if (isLeaf.equals("1")){
	        	if(notetype.equals("1")){
	        		ids = ids + id + ",";
	        	}
	          }else if (isLeaf.equals("0")){
	        	if(notetype.equals("1")){
	        		ids = ids + getidList(id);
		        }
	          }
	        }
	    }
	    catch (Exception de)
	    {
	      de.printStackTrace();
	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try {
	        db.close();
	      } catch (DbException dbe) {
	        dbe.printStackTrace();
	      }

	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try {
	        db.close();
	      } catch (DbException de1) {
	        de1.printStackTrace();
	      }
	    }
	    finally
	    {
	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try {
	        db.close();
	      } catch (DbException de) {
	        de.printStackTrace();
	      }
	    }
	    return ids;
	  }
  
  public String getidListM(String val) {
	    String ids = val + ",";
	    String sql = "select id,name,isleaf FROM SYSTEM_TREE c where  parentid = " + val;
	    DataBase db = new DataBase();
	    ResultSet rs = null;
	    try {
	      db.connectDb();
	      rs = db.queryAll(sql);
	      if (rs != null)
	        while (rs.next()) {
	          String id = DataFormat.stringCheckNullNoTrim(rs.getString(1), "");
	          String name = DataFormat.stringCheckNullNoTrim(rs.getString(2), "");
	          String isLeaf = DataFormat.stringCheckNullNoTrim(rs.getString(3), "");
	          if (isLeaf.equals("1"))
	        	  ids = ids + name + ",";
	          else if (isLeaf.equals("0"))
	        	  ids = ids + getidList(id);
	        }
	    }
	    catch (Exception de)
	    {
	      de.printStackTrace();
	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try {
	        db.close();
	      } catch (DbException dbe) {
	        dbe.printStackTrace();
	      }

	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try {
	        db.close();
	      } catch (DbException de1) {
	        de1.printStackTrace();
	      }
	    }
	    finally
	    {
	      if (rs != null)
	        try {
	          rs.close();
	        } catch (SQLException se) {
	          se.printStackTrace();
	        }
	      try {
	        db.close();
	      } catch (DbException de) {
	        de.printStackTrace();
	      }
	    }
	    return ids;
	  }
  
  public String getLikeStr(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      result.append(" like '%");
      result.append(value);
      result.append("%'");
    }
    return result.toString();
  }
  
  public String getrRLikeStr(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      result.append(" like '");
      result.append(value);
      result.append("%'");
    }
    return result.toString();
  }

  public String getLikeStr(String key, String value, String type)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      if ("".equals(type)) {
        result.append(" like '%");
        result.append(value);
        result.append("%'");
      } else if ("left".equals(type)) {
        result.append(" like '%");
        result.append(value);
        result.append("'");
      } else if ("right".equals(type)) {
        result.append(" like '");
        result.append(value);
        result.append("%'");
      }
    }
    return result.toString();
  }

  public String getEQStr(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      result.append(" = '");
      result.append(value);
      result.append("'");
    }
    return result.toString();
  }

  public String getNEStr(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      result.append(" != '");
      result.append(value);
      result.append("'");
    }
    return result.toString();
  }

  public String getGTStr(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      result.append(" > '");
      result.append(value);
      result.append("'");
    }
    return result.toString();
  }

  public String getGEStr(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      result.append(" >= '");
      result.append(value);
      result.append("'");
    }
    return result.toString();
  }

  public String getNGStr(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      result.append(" >= '");
      result.append(value);
      result.append("'");
    }
    return result.toString();
  }

  public String getLTStr(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      result.append(" < '");
      result.append(value);
      result.append("'");
    }
    return result.toString();
  }

  public String getLEStr(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      result.append(" <= '");
      result.append(value);
      result.append("'");
    }
    return result.toString();
  }

  public String getNLStr(String key, String value)
  {
    StringBuffer result = new StringBuffer();
    if ((!DataFormat.checkNull(key)) && (!DataFormat.checkNull(value))) {
      result.append(" and ");
      result.append(key);
      result.append(" <= ");
      result.append(value);
      result.append("");
    }
    return result.toString();
  }

  private boolean isValid(String fieldName, String pk)
  {
    return (!"mainTable".equals(fieldName)) && (!"table".equals(fieldName)) && (!pk.equals(fieldName)) && (!"fk".equals(fieldName)) && (!fieldName.startsWith("creat")) && (!fieldName.endsWith("List"));
  }

  private boolean isValidAdd(String fieldName, String pk)
  {
    return (!"mainTable".equals(fieldName)) && (!"table".equals(fieldName)) && (!pk.equals(fieldName)) && (!"fk".equals(fieldName)) && (!fieldName.endsWith("List"));
  }

  private boolean isValidAdd(String fieldName)
  {
    return (!"mainTable".equals(fieldName)) && (!"table".equals(fieldName)) && (!"fk".equals(fieldName)) && (!fieldName.endsWith("List"));
  }

  private boolean isValid(String fieldName)
  {
    return (!"mainTable".equals(fieldName)) && (!"table".equals(fieldName)) && (!"fk".equals(fieldName)) && (!fieldName.startsWith("creat")) && (!fieldName.endsWith("List"));
  }

  public List getList(String sql, String tableName) {
    List result = new ArrayList();
    PageBean bean = new MsSqlPageBean();
    try
    {
      ResultSet rs = bean.listData(sql.toString(), "").getRsResult();

      ResultSetMetaData meta = rs.getMetaData();

      result = SetBean(tableName, rs);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public List SetBean(String beanName, ResultSet rs)
  {
    List result = new ArrayList();
    try {
      while (rs.next()) {
        Object o = Class.forName(beanName).newInstance();
        o = setBeanValues(o, rs);
        result.add(o);
      }
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public Object setBeanValues(Object o, ResultSet rs)
  {
    PageMethod pm = new PageMethod();

    Field[] fields = o.getClass().getDeclaredFields();
    String fieldName = ""; String value = "";

    Field[] fieldsParent = o.getClass().getSuperclass().getDeclaredFields();
    String[] fieldParentNames = new String[fieldsParent.length];
    String fieldParentName = ""; String Parentvalue = "";
    try {
      for (int i = 0; i < fields.length; i++) {
        fieldName = fields[i].getName();
        if (isValidAdd(fieldName)) {
          String firstLetter = fieldName.substring(0, 1).toUpperCase();
          String setter = "set" + firstLetter + fieldName.substring(1);
          Type type = fields[i].getGenericType();
          Method method = o.getClass().getMethod(setter, new Class[] { Type2Class.getClass(type, 0) });
          value = pm.getDataFromResultSet(rs, "string", fieldName);
          method.invoke(o, new Object[] { new String(value) });
        }
      }
      for (int i = 0; i < fieldsParent.length; i++) {
        fieldParentName = fieldsParent[i].getName();
        if (isValidAdd(fieldParentName)) {
          String firstLetter = fieldParentName.substring(0, 1).toUpperCase();
          String setter = "set" + firstLetter + fieldParentName.substring(1);
          Type type = fieldsParent[i].getGenericType();
          Method method = o.getClass().getMethod(setter, new Class[] { (Class)type });
          value = pm.getDataFromResultSet(rs, "string", fieldParentName);
          method.invoke(o, new Object[] { new String(value) });
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return o;
  }
}