package com.cheers.client.service;

import java.util.ArrayList;
import java.util.List;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;

public class ClientAssignService extends Dao {
	
	public String add(){
		return getReturnString(super.add());
	}
	public String update(){
		DateFormat df = new DateFormat();//设置日期格式
		String now=df.getNowTime();
		DataBase db = new DataBase();
		String accountid = ((AccountBean)this.session.getAttribute("accountBean")).getId();
		String table = (String) this.data.get("table");
		String receiver = (String) this.data.get("receiver");
		String memo=(String)this.data.get("memo");
		List<String> sqls = new ArrayList<String>();
		if(this.data.get("id") instanceof String){//单个删除
			StringBuffer sql = new StringBuffer();
			sql.append("update "+table+" set modifierid = "+accountid+" , accountid = "+receiver+" , startdate = '"+now+"'"+" , memo = '"+memo+"'");
			sql.append(" where clientid="+this.data.get("id"));
			String sqlc ="update  Customer set accountId = "+receiver+" , modifierId = "+accountid+" , modifyTime = '"+now+"' where id = "+this.data.get("id");
			sqls.add(sql.toString());
			sqls.add(sqlc.toString());
		}else if(this.data.get("id") instanceof ArrayList){//批量删除
			@SuppressWarnings("unchecked")
			List<Object> ids = (List<Object>)this.data.get("id");
			for(Object str : ids){
				StringBuffer sql = new StringBuffer();
				sql.append("update "+table+" set modifierid = "+accountid+" , accountid = "+receiver+" , startdate = '"+now+"'"+" , memo = '"+memo+"'");
				sql.append(" where clientid="+str);
				String sqlc ="update  Customer set accountId = "+receiver+" , modifierId = "+accountid+" , modifyTime = '"+now+"' where id = "+str;
				sqls.add(sql.toString());
				sqls.add(sqlc.toString());
			}
		}
		try {
			db.connectDb();
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
	
	public String delete(){
		return getReturnString(super.delete());
	}

	/**
	 * 客户分配列表信息
	 * @return
	 */

	public String getBeans(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from customerView t where 1=1");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
        sql.append(" or t.accountid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		sql .append( " order by id");
		System.out.println("sql:"+sql);
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
	
	
}
