package com.cheers.system.account.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.CnToSpell;
import com.cheers.commonMethod.HZSM;
import com.cheers.dao.BaseDao;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.exceltools.ExcelUtils;
import com.cheers.exceltools.JsGridReportBase;
import com.cheers.exceltools.TableData;
import com.cheers.util.DES;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;
import com.cheers.util.JsonUtil;

public class AccountService extends Dao{
	DES des = new DES();
/**
 * 修改密码
 * @return
 */
	public String updatePassword() {
	/*	Map map = new HashMap();
		map.put("table", this.data.get("table"));
		map.put("name", this.session.getAttribute("id"));
		List<DynaBean> list = super.search(super.searchSQL(map));*/
		//if (list != null && list.size() > 0) {
			/*String oldPwd = (String) list.get(0).get("password");
			//if (des.createDecryptor(oldPwd).equals(this.data.get("oldPassword"))) {
				String password = (String) this.data.get("password");
				password = des.createEncryptor(password);
				if (1 == super.update(" update " + this.data.get("table") + " set password= '" + password + "' where id=" + this.data.get("id") + ""))*/
		session=request.getSession();
		String pwd=des.createEncryptor(request.getParameter("password"));
		String sql="update sysaccount set password='"+pwd+"' where id ='"+((AccountBean)session.getAttribute("accountBean")).getId()+"'";
		if (1 == super.update(sql))
		return getReturnString("true");
				else {
					return getReturnString("false");
				}
			/*} else {
				return getReturnString("旧密码错误！");
			}*/
		/*} else {
			return getReturnString("false");
		}*/

	}

	/**
	 * 
	 * 重置密码
	 * @return
	 */
	public String resetPassword() {
		String password = (String) this.data.get("value");
		password = des.createEncryptor(password);
		this.data.put("value", password);
		return getReturnString(super.updatefieldByIds()) ;
	}
	/**
	 * 
	 * 密码重置
	 * @return
	 */
	public String doreset() {    //将业务员的密码进行重置
		DataBase db=new DataBase();
		String id =String.valueOf(request.getParameter("id"));
		String password =des.createEncryptor("123");
		String sql="update Sysaccount set password='"+password+"' where id='"+id+"'";
		try {
			db.update(sql);
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		try {
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		return getReturnString("true") ;
	}
	
	/**
	 * 获取登录人
	 * @return
	 */
	public String getLoginer() {
		Map params = new HashMap();
		params.put("table", "sysaccount");
		session=request.getSession();
		params.put("accountEQ", session.getAttribute("account"));
		String sql=super.searchSQL(params);
		excuteSQLPageQuery(sql);
		result = getRows();
		return getReturn();
	}
	
	
	public String getSelfId(){
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String selfId = accountBean.getId();//当前登录人id
	String sql = "select top 100 percent id value ,name text from sysaccount where isenable = '1' and id='"+selfId+"'";
			excuteSQLPageQuery(sql);
	result = getRows();
	return getReturn();
	}
	public String getPwd(){
		session=request.getSession();
		String sql="select * from sysaccount where account ='"+session.getAttribute("account")+"'";
		
		excuteSQLPageQuery(sql);
		result = getRows();
	    if(result!=null){
	    	for( Object obj:result){
	    		DynaBean bean=(DynaBean)obj;
	    		bean.set("password", des.createDecryptor((String) bean.get("password")))  ;
	    	}
	    }
		return getReturn();
	}
	
	/**
	 * 
	 * 设置启用禁用
	 * @return
	 */
	public String updateEnable() {
		return super.updatefieldByIds();
	}
	public String updateAccount() {
		Map map = new HashMap();
		map.put("table", this.data.get("table"));
		map.put("accountEQ", this.data.get("account"));
		if ("true".equals(super.checkDuplicate(map))) {
			if (result != null && result.size() > 1)
				return "false";
			else {
				super.update(super.updateSQL(this.data));
				return getReturnString("true");
			}
		} else {
			super.update(super.updateSQL(this.data));
			return getReturnString("true");
		}

	}
	
	public String checkDuplicate(){
		return getReturnString(super.checkDuplicate(this.map));
	}

	public String delete(){
		return getReturnString(super.delete());
	}
 
	public String updatefieldByIds() {
		
		return super.getReturnString(super.updatefieldByIds());
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 * @author apple
	 */
	public String add() {
		try {
			Map map = new HashMap();
			map.put("accountEQ", this.data.get("account")); 
			map.put("table", this.data.get("table"));
			if ("true".equals(super.checkDuplicate(map))) {
				return getReturnString("重复的账号！");
			} else {
			if (this.data.get("password") != null && !"".equals(this.data.get("password"))) {
				DES des = new DES();
				data.put("password", des.createEncryptor(this.data.get("password").toString()));
			}
			this.session = request.getSession();
			this.data.put("createTime", DateFormat.getNowTime());
			this.data.put("createrId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
			if ("true".equals(super.add())) {
				String maxaccountid = getMaxAccountId();
				List<Map> orgList = (List<Map>) data.get("orgList");
				if (orgList != null && orgList.size() > 0) {
					for (int i = 0; i < orgList.size(); i++) {
						List<DynaBean> org = super.search("select * from " + orgList.get(i).get("table") + " where id=" + orgList.get(i).get("id"));
						getAllParentAccount(orgList.get(i).get("table").toString(), org.get(0).get("parentId").toString(), maxaccountid); // 高部门
						getAllParentLevelOrg(org.get(0).get("id").toString(), org.get(0).get("level").toString(), maxaccountid);// 高级别
					}
					for (int j = 0; j < orgList.size(); j++) {
						getAllChildAccountID(orgList.get(j).get("id").toString(), maxaccountid);
						getAllChildLevelOrg(orgList.get(j).get("id").toString(), orgList.get(j).get("level").toString(), maxaccountid);
					}
					// 将自己放入权限中
					addAccountRight(maxaccountid, maxaccountid);
				}
				return getReturnString("true");
			} else
				return getReturnString("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getReturnString("false");
		}
	}

	/**
	 * 获取所有的父部门对应的权限用户并为这些用户添加权限信息
	 * 
	 * @param parentOrgID
	 */
	public void getAllParentAccount(String table, String parentOrgID, String accountID) {
		List<DynaBean> org = super.search("select * from " + table + " where id=" + parentOrgID);
		if (org != null && null != org.get(0).get("id") && !"".equals(org.get(0).get("id"))) {
			// 获取此部门对应的所有的用户
			ArrayList<String> allUserDept = getAccountByOrg(org.get(0).get("id").toString());
			// 添加权限信息
			for (int i = 0; i < allUserDept.size(); i++) {
				String parentid = allUserDept.get(i);

				List<String> list = getAccountList(parentid);
				if (list.size() != 0) {
					// 权限添加
					addAccountRight(accountID, parentid);
				}
			}
			getAllParentAccount(table, org.get(0).get("parentid").toString(), accountID);
		}
	}
	/**
	 * 获取在同一级别的部门但优先级高的部门信息
	 * 
	 */
	public void getAllParentLevelOrg(String orgId, String level, String accountID) {
		// 高级别同部门的权限信息
		ArrayList<String> alllevelAccount = getLowLevel(orgId, level, "height");
		for (int t = 0; t < alllevelAccount.size(); t++) {
			String accid = alllevelAccount.get(t);
			List<String> list = getAccountList(accid);
			if (list.size() != 0) {
				// 为高级别部门赋值
				addAccountRight(accountID, accid);
			}
		}
	}
	public List<String> getAccountList(String accountid) {
		List<String> accountRights = new ArrayList<String>();
		List<DynaBean> list = super.search("select * from sysaccountright where accountid=" + accountid);
		for (DynaBean obj : list) {
			accountRights.add(obj.get("accountId").toString());
		}
		return accountRights;

	}
	public String getMaxAccountId() {
		String maxAccountId = "";
		DataBase db = new DataBase();
		try {
			db.connectDb();
			ResultSet rs = db.queryAll(" select max(id) from sysaccount");
			while (rs.next()) {
				maxAccountId = rs.getString(1);
			}

		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

		return maxAccountId;
	}

	/**
	 * 
	 * @param id
	 * @param data
	 * @return
	 * @author apple
	 */
	public String update() {
		try {
			/*if (this.data.get("password") != null && !"".equals(this.data.get("password"))) {
				DES des = new DES();
				this.data.put("password", des.createEncryptor(this.data.get("password").toString()));
			}*/
			if ("true".equals(super.update())) {
				List<Map> orgList = (List<Map>) this.data.get("orgList");
				if (orgList != null) {
					super.update("delete from sysAccountStruct where  accountid='" + this.data.get("id") + "'");

					for (Map org : orgList) {

						super.update("insert into sysAccountStruct(accountid,structid,level) values ('" + this.data.get("id") + "','" + org.get("id") + "','" + org.get("level")
								+ "')");
					}
					String maxaccountid = this.data.get("id").toString();
					if (deleteAccountRight(maxaccountid)) {
						if (orgList != null && orgList.size() >= 1) {
							for (int i = 0; i < orgList.size(); i++) {
								List<DynaBean> org = super.search("select * from " + orgList.get(i).get("table") + " where id=" + orgList.get(i).get("id"));
								getAllParentAccount(orgList.get(i).get("table").toString(), org.get(0).get("parentId").toString(), maxaccountid); // 高部门
								getAllParentLevelOrg(org.get(0).get("id").toString(), org.get(0).get("level").toString(), maxaccountid);// 高级别
							}
							for (int j = 0; j < orgList.size(); j++) {
								getAllChildAccountID(orgList.get(j).get("id").toString(), maxaccountid);
								getAllChildLevelOrg(orgList.get(j).get("id").toString(), orgList.get(j).get("level").toString(), maxaccountid);
							}
							// 将自己放入权限中
							addAccountRight(maxaccountid, maxaccountid);

						}
					}
				}
				return getReturnString("true");
			} else
				return getReturnString("false");

		} catch (Exception e) {

			e.printStackTrace();
			return getReturnString("false");
		}

	}
	public synchronized boolean deleteAccountRight(String accountid) {
		boolean sign = false;
		DataBase db = new DataBase();
		try {
			db.connectDb();
			String sql = "delete from SysAccountRight where accountid=" + accountid + "";
			String sql_ = "delete from SysAccountRight where parentid=" + accountid + "";
			db.update(sql);
			db.update(sql_);
			sign = true;

		} catch (DbException de) {
			try {
				db.rollback();
			} catch (DbException dee) {
				dee.printStackTrace();
			}
			de.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

		return sign;
	}

	public String getRight(String id) {
		List list;
		try {
			list = getRightList(id);
			if (list == null || list.size() < 1) {
				return JsonUtil.object2Json("");
			} else {
				System.out.println(JsonUtil.encodeObject2Json(list));
				return JsonUtil.encodeObject2Json(list);
			}
		} catch (SQLException e) {

			e.printStackTrace();
			return JsonUtil.object2Json("");
		}
	}
	public List getRightList(String accountId) throws SQLException {
		DataBase db = new DataBase();
		try {
			db.connectDb();
			rs = db.queryCache(" select r.* from SysAccountRole ar,SysRoleRight rr,[right] r  where ar.roleid = rr.roleid and rr.rightid = r.id and ar.accountid = " + accountId + "");
			return getRows();
		} catch (DbException de) {
			de.printStackTrace();
			return null;
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}
	}
	public ArrayList<String[]> getStructAndLevel(String accountid) {
		ArrayList<String[]> structLevel = new ArrayList<String[]>();
		DataBase db = new DataBase();
		try {
			db.connectDb();
			ResultSet rs = db.queryAll(" select s.structid,s.[level] from sysAccountStruct s where s.accountid = " + accountid + "");
			while (rs.next()) {
				String[] str = new String[2];
				str[0] = rs.getString("structid");
				str[1] = rs.getString("level");
				structLevel.add(str);
			}

		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

		return structLevel;
	}
	/**
	 * 获取在同一级别的部门但优先级低的部门信息
	 * 
	 */
	public void getAllChildLevelOrg(String orgId, String level, String parentAccountID) {
		// 低级别同部门的权限信息
		ArrayList<String> alllevelAccount = this.getLowLevel(orgId, level, "low");
		for (int t = 0; t < alllevelAccount.size(); t++) {
			String accid = alllevelAccount.get(t);
			// 相数据权限表中赋值
			this.addAccountRight(accid, parentAccountID);
		}
	}
	public ArrayList<String> getLowLevel(String orgID, String level, String flag) {
		ArrayList<String> accounts = new ArrayList<String>();
		DataBase db = new DataBase();
		try {
			db.connectDb();
			String sql = "";
			if ("low".equals(flag)) {
				sql += "select accountid from sysAccountStruct where structid = " + orgID + " and level < " + level + "";
			}
			if ("height".equals(flag)) {
				sql += "select accountid from sysAccountStruct where structid = " + orgID + " and level > " + level + "";
			}
			ResultSet rs = db.queryAll(sql);
			while (rs.next()) {
				accounts.add(rs.getString("accountid"));
			}

		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

		return accounts;
	}
	/**
	 * 获取所有的上级Id
	 * 
	 * @param parentID
	 * @return
	 */
	public void getAllChildAccountID(String parentOrgID, String parentAccountID) {
		try {
			ArrayList<String> list = this.getOrgList(parentOrgID);
			for (int i = 0; i < list.size(); i++) {
				String orgid = list.get(i);
				// 根据部门获取所有此部门下的用户并向权限表中赋值
				ArrayList<String> allUserDept = this.getAccountByOrg(orgid);
				for (int q = 0; q < allUserDept.size(); q++) {
					String accid = allUserDept.get(q);
					// 相数据权限表中赋值
					this.addAccountRight(accid, parentAccountID);
				}
				getAllChildAccountID(orgid, parentAccountID);// 递归
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ArrayList<String> getOrgList(String orgid) {
		ArrayList<String> orgs = new ArrayList<String>();
		DataBase db = new DataBase();
		try {
			db.connectDb();
			ResultSet rs = db.queryAll(" select o.id from organization o where o.vorder like (select vorder from organization where id=" + orgid
					+ ") +'%' and o.vorder <> (select vorder from organization where id=" + orgid + ")");
			while (rs.next()) {
				orgs.add(rs.getString("id"));
			}

		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

		return orgs;
	}
	
	
	public synchronized boolean queryInfo(String sql, DataBase db) {
		boolean sign = false;
		ResultSet rs = null;
		try {
			rs = db.queryAll(sql);
			while (rs.next()) {
				return true;
			}
		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return sign;
	}
	
	
	public void addAccountRight(String accid, String parentid) {
		DataBase db = new DataBase();
		try {
			db.connectDb();

			if (!queryInfo("select accountid from SysAccountRight where accountid=" + accid + " and parentid=" + parentid + "", db)) {
				db.update(" insert into SysAccountRight (accountid,parentid) values (" + accid + "," + parentid + ")");
			}

		} catch (DbException de) {
			de.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

	}
	public ArrayList<String> getAccountByOrg(String orgid) {
		ArrayList<String> accounts = new ArrayList<String>();
		DataBase db = new DataBase();
		try {
			db.connectDb();
			ResultSet rs = db.queryAll(" select accountid from sysAccountStruct where structid = " + orgid + "");
			while (rs.next()) {
				accounts.add(rs.getString("accountid"));
			}

		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

		return accounts;
	}

	public String getRoleNames(String accountid) {
		String rolenames = "";
		DataBase db = new DataBase();
		try {
			db.connectDb();
			ResultSet rs = db.queryAll(" select r.name from SysAccountRole ar,role r where ar.roleid = r.id and ar.accountid=" + accountid + "");

			Set set = new HashSet();
			if (rs != null)
				while (rs.next()) {
					set.add(rs.getString("name"));
				}
			Iterator it = set.iterator();
			while (it.hasNext()) {
				rolenames = it.next() + ",";
			}
			if (!"".equals(rolenames)) {
				rolenames = rolenames.substring(0, rolenames.length() - 1);
			}
		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

		return rolenames;
	}
	public String getRoleids(String accountid) {
		String roleids = "";
		DataBase db = new DataBase();
		try {
			db.connectDb();
			ResultSet rs = db.queryAll(" select ar.roleid from SysAccountRole ar,role r where ar.roleid = r.id and ar.accountid=" + accountid + "");

			Set set = new HashSet();
			if (rs != null)
				while (rs.next()) {

					set.add(rs.getString("roleid"));
				}
			Iterator it = set.iterator();
			while (it.hasNext()) {
				roleids = it.next() + ",";
			}
			if (!"".equals(roleids)) {
				roleids = roleids.substring(0, roleids.length() - 1);
			}
		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

		return roleids;
	}

	public String getStructNames(String accountid) {
		String structnames = "";
		DataBase db = new DataBase();
		try {
			db.connectDb();
			ResultSet rs = db.queryAll(" select o.name,s.level from sysAccountStruct s,organization o where s.structid = o.id and s.accountid = " + accountid + "");

			Set<String[]> set = new HashSet<String[]>();
			if (rs != null)
				while (rs.next()) {
					String[] str = new String[2];
					str[0] = rs.getString("name");
					str[1] = rs.getString("level");
					set.add(str);
				}
			Iterator<String[]> it = set.iterator();
			while (it.hasNext()) {
				String str[] = it.next();
				structnames = str[0] + "(" + str[1] + "),";
			}
			if (!"".equals(structnames)) {
				structnames = structnames.substring(0, structnames.length() - 1);
			}
		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

		return structnames;
	}

	public String getStructIds(String accountid, String haslevel) {
		String structIds = "";
		DataBase db = new DataBase();
		try {
			db.connectDb();

			ResultSet rs = db.queryAll(" select o.id from sysAccountStruct s,organization o where s.structid = o.id and s.accountid = " + accountid + "");
			Set set = new HashSet();
			if (rs != null)
				while (rs.next()) {
					set.add(rs.getString("id"));
				}
			Iterator it = set.iterator();
			while (it.hasNext()) {
				structIds = it.next() + ",";
			}
			if (!"".equals(structIds)) {
				structIds = structIds.substring(0, structIds.length() - 1);
			}
		} catch (DbException de) {
			de.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}

		return structIds;
	}

	public String isEnable(String[] ids, String oOrs) {
		StringBuffer sql = new StringBuffer();
		sql.append("update account set enable ='");
		sql.append(oOrs.equals("open") ? "1" : "0");
		sql.append("' where id in		(");
		for (int i = 0; i < ids.length - 1; i++) {
			sql.append(ids[i] + ",");
		}
		sql.append(ids[ids.length - 1] + ")");
		DataBase db = new DataBase();
		try {
			db.connectDb();
			if (db.update(sql.toString()) > 0)
				return "true";
			else
				return "false";
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}
	}

	public String resertPwd(String[] ids) {
		StringBuffer sql = new StringBuffer();
		DES des = new DES();
		sql.append("update account set password ='");
		sql.append(des.createEncryptor("123456"));
		sql.append("' where id in		(");
		for (int i = 0; i < ids.length - 1; i++) {
			sql.append(ids[i] + ",");
		}
		sql.append(ids[ids.length - 1] + ")");
		DataBase db = new DataBase();
		try {
			db.connectDb();
			if (db.update(sql.toString()) > 0)
				return "true";
			else
				return "false";
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		} finally {
			try {
				db.close();
			} catch (Exception de) {
				de.printStackTrace();
			}
		}
	}
	/**
	 * 获取全拼助记码
	 * 王欣  2013年11月11日20:14:33
	 * @return
	 */
	public String getQuan(){
		String name = (String) this.data.get("vals");
		System.out.println(CnToSpell.getSpell(name, false));
		return getReturnString( CnToSpell.getSpell(name, false));
	}
	/**
	 * 简拼助记码
	 * 王欣  2013年11月11日20:14:37
	 * @return
	 */
	public String getJian(){
		String name = (String) this.data.get("vals");
		return getReturnString(HZSM.GetStrPYIndex(name));
	}
	/**
	 * 结构树设置 组织结构 负责人 查询 方法
	 * @author sunjianfeng 2013-11-14 09:24:34
	 */
	public String getFuZenRen(){
		 String q=request.getParameter("q");
		 String sql = "select top 100 percent dbo.sysaccount.id value,dbo.sysaccount.name text from dbo.sysaccount ";
		 if(q!=null&&!"".equals(q)){
			 sql+="where dbo.sysaccount.zjmjian like '%"+q+"%' or dbo.sysaccount.zjmquan like '%"+q+"%'";
		 }
		 if (!DataFormat.booleanCheckNull(sort))
				sql+= " order by " + sort + " " + DataFormat.objectCheckNull(order);
			else
				sql += " order by id";
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
	 }
	/**
	 * 账户导出
	 * 王欣  2013年11月19日14:48:04
	 */
	public void expExcel() throws Exception{
		 this.response.setContentType("application/msexcel;charset=GBK");
		 String name = (String) super.map.get("nameLIKE");
		 String roleidEQ = (String) super.map.get("roleidEQ");
		 String accountEQ = (String) super.map.get("accountEQ");
		 String entrydateGE = (String) super.map.get("entrydateGE");
		 String entrydateLE = (String) super.map.get("entrydateLE");
		 String enableEQ = (String) super.map.get("enableEQ");
		 String sql = "select aa.*,st.name as bumen, sts.name as zhiwei ,bs.name juese from " +
		 		" (select a.*,ap.orgId,ap.positionId from sysaccountview a left join sysACCOUNT_POST ap on a.id=ap.accountId) aa"+
				 " left join SYSTEM_TREE st on st.id=aa.orgId LEFT JOIN SYSTEM_TREE sts on sts.id=aa.positionId " +
				 " LEFT JOIN BASE_DATA bs on bs.id=aa.roleid where 1=1";
		 if(name!=null&!"".equals(name)){
			 sql +=" and aa.name like '%"+name+"%'";
		 }
		 if(roleidEQ!=null&&!"".equals(roleidEQ)){
			 sql +=" and aa.roleId='"+roleidEQ+"'";
		 }
		 if(accountEQ!=null&&!"".equals(accountEQ)){
			 sql +=" and aa.account='"+accountEQ+"'";
		 }
		 if(entrydateGE!=null&&!"".equals(entrydateGE)){
			 sql+=" and aa.entrydate>='"+entrydateGE+"'";
		 }
		 if(entrydateLE!=null&&!"".equals(entrydateLE)){
			 sql+=" and aa.entrydate<='"+entrydateLE+"'";
		 }
		 if(enableEQ!=null&&!"".equals(enableEQ)){
			 sql+=" and aa.enable='"+enableEQ+"'";
		 }
		 List<Map<String,Object>> list = super.getListBean(sql);//获取数据
		 String title = "账户信息表";
		 String[] hearders = new String[] {"姓名", "性别","账号","工号","部门","职位","角色","身份证号","手机","电话","邮箱","出生日期","入职日期"};//表头数组
         String[] fields = new String[] {"name", "sex","account","accountno","bumen","zhiwei","juese","idcardno","telephone","phone","email","birthday","entrydate"};//People对象属性数组
         TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders),fields);
         JsGridReportBase report = new JsGridReportBase(request, response);
         report.exportToExcel(title, name, td);
	}
	
	
	public String getMaxNum(){
	    this.session = this.request.getSession();
	    String num = "100";
	    DataBase db = new DataBase();
	    try
	    {
	      db.connectDb();
	      ResultSet rs = db.queryAll(" select count(*) as num from sysACCOUNT ");
	      while (rs.next())
	        num = rs.getString("num");
	    }
	    catch (DbException de)
	    {
	      de.printStackTrace();
	      try
	      {
	        db.close();
	      } catch (Exception de1) {
	        de1.printStackTrace();
	      }
	    }
	    catch (SQLException se)
	    {
	      se.printStackTrace();
	      try
	      {
	        db.close();
	      } catch (Exception de) {
	        de.printStackTrace();
	      }
	    }
	    finally
	    {
	      try
	      {
	        db.close();
	      } catch (Exception de) {
	        de.printStackTrace();
	      }
	    }

	    if (Integer.parseInt(num) >= 100) {
	      return "false";
	    }
	    return "true";
	  }
	
	public String getsysaccountview(){
		 String nameLIKE=(String) super.map.get("nameLIKE");
		 String rolenameLIKE=(String) super.map.get("rolenameLIKE");
		 String bumenLIKE=(String) super.map.get("bumenLIKE");
		 
		 String sql = "select top 100 percent * from sysaccountview s where isenable = 1 ";
		 String accountidlist = "0,";//
		 
		 if(nameLIKE!=null&&!"".equals(nameLIKE)){
			 sql+=" and s.zjmjian like '%"+nameLIKE+"%' or s.zjmquan like '%"+nameLIKE+"%' or s.name like '%"+nameLIKE+"%' ";
		 }
		 if(rolenameLIKE!=null&&!"".equals(rolenameLIKE)){
			 sql+=" and s.rolename like '%"+rolenameLIKE+"%' ";
		 }
		 if(bumenLIKE!=null&&!"".equals(bumenLIKE)){
			 BaseDao bd = new  BaseDao();
			 String idslist = bd.getidList(bumenLIKE);
			 
			 String[] ids = idslist.split(",");
			 List<String> tempList = Arrays.asList(ids);
			 
			 DataBase db = new DataBase();
			    try
			    {
			      db.connectDb();
			      ResultSet rs = db.queryAll(" select  *  from AccountOrg ");
			      while (rs.next()){
			    	  String orgid = rs.getString("orgid");
			    	  
			    	  if(tempList.contains(orgid)){
			    		  accountidlist = accountidlist + rs.getString("accountid") + ",";
			          } 
			      }
			    }catch (Exception de) {
			       de.printStackTrace();
			       try{db.close();} catch (Exception de1) {de1.printStackTrace();}
			    }finally{
				   try{db.close();} catch (Exception de) {de.printStackTrace();}
				}
			    
			    sql = " select top 100 percent * from  ("+sql+") t where t.id in ( "+ accountidlist.substring(0, accountidlist.length() - 1) +") ";
		 }
		 
		 if (!DataFormat.booleanCheckNull(sort))
			 sql+= " order by " + sort + " " + DataFormat.objectCheckNull(order);
		 else
			 sql += " order by id";
		 
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
	 }
	
		/**
		 * 
		 * 模块：
		 * 功能：
		 * 作者：jack_sun:1519891098
		 */
		public String updateVersion() {
			    session=request.getSession();
				String version=request.getParameter("version");
				String sql="update sysaccount set version='"+version+"' where id ='"+((AccountBean)session.getAttribute("accountBean")).getId()+"'";
				if (1 == super.update(sql))
					return getReturnString("true");
							else {
								return getReturnString("false");
							}
		}
}
