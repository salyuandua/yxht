package com.cheers.contract.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;

public class ContractService extends Dao {
	/**
	 * 合同信息的添加 2014-12-25
	 * */
	public String add() {
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		this.session = request.getSession();
		DateFormat df = new DateFormat();// 设置日期格式
		String now = df.getNowTime();
		List<Object> orList = (List<Object>) data.get("occupationRatioList");// 获取到占合同金额比业务员信息对象集合
		List<Object> neworList = new ArrayList<Object>();// 创建一个新的集合存放新的合同金额比业务员数据

		List<Object> bdList = (List<Object>) data.get("biaodiList");// 获取到标的信息对象集合
		List<Object> newbdList = new ArrayList<Object>();// 创建一个新的集合存放新的标的数据

		// 循环联系人集合，将创建时间创建人，最后一次修改时间，最后一次修改人，存放到集合中
		for (int i = 0; i < orList.size(); i++) {
			// 集合中存放的为LinkedHashMap对象，每次循环取出一个
			LinkedHashMap<String, String> maps = (LinkedHashMap<String, String>) orList
					.get(i);
			// maps.remove("id");
			maps.put("createTime", now);
			maps.put("modifyTime", now);
			maps.put("creatorId", ((AccountBean) this.session
					.getAttribute("accountBean")).getId());
			maps.put("modifierId", ((AccountBean) this.session
					.getAttribute("accountBean")).getId());
			neworList.add(maps);// 将新的map数据放入新的集合中
		}
		for (int i = 0; i < bdList.size(); i++) {
			// 集合中存放的为LinkedHashMap对象，每次循环取出一个
			LinkedHashMap<String, String> maps = (LinkedHashMap<String, String>) bdList
					.get(i);
			// maps.remove("id");
			maps.put("createTime", now);
			maps.put("modifyTime", now);
			maps.put("creatorId", ((AccountBean) this.session
					.getAttribute("accountBean")).getId());
			maps.put("modifierId", ((AccountBean) this.session
					.getAttribute("accountBean")).getId());
			newbdList.add(maps);// 将新的map数据放入新的集合中
		}
		// 根据key删除之前的list集合
		data.remove("occupationRatioList");
		data.remove("biaodiList");
		// 放入新的list集合
		data.put("occupationRatioList", neworList);
		data.put("biaodiList", newbdList);

		this.data.put("createTime", now);
		this.data.put("creatorId", ((AccountBean) this.session
				.getAttribute("accountBean")).getId());
		this.data.put("modifyTime", now);//
		this.data.put("modifierId", ((AccountBean) this.session
				.getAttribute("accountBean")).getId());

		String retStr = super.add();
		// ***********************************************************
		if (retStr.equals("true")) {
			Map sqlMap = new HashMap();
			sqlMap.put("createTime", now);
			int i = getMaxId((String) this.data.get("table"), sqlMap);
			return super.getReturnString(i + "");
		} else
			return super.getReturnString(retStr);
		// }
	}

	// 客户修改
	public String update() {
		Map m = new HashMap();
		m.put("table", this.data.get("table"));
		StringBuffer sql2 = new StringBuffer();
		sql2.append("select top 100 percent * ");
		sql2.substring(0, sql2.length() - 1);
		sql2.append(" from ");
		sql2.append(DataFormat.objectCheckNull(this.data.get("table"))
				+ DataFormat.objectCheckNull(map.get("view")));
		sql2.append(" where 1=1 and id<>" + this.data.get("id"));
		// 遍历参数
		sql2.append(getSearchStr(m));
		PageBean bean = new MsSqlPageBean();
		rs = bean.listData(sql2.toString(), null).getCachedRowSet();
		result = getRows();
		this.session = request.getSession();
		DateFormat df = new DateFormat();// 设置日期格式
		String now = df.getNowTime();
		String accountid = ((AccountBean) this.session
				.getAttribute("accountBean")).getId();
		// 处理合同信息中一对多的更新
		List<String> upsqls = new ArrayList<String>();
		List<String> upsqls2 = new ArrayList<String>();
		
		
		// 删除所有去除项
		this.map.put("table", "contract_accountor");
		this.map.put("id",this.map.get("id1"));
		upsqls = deleteSql(this.map);
		if (this.data.containsKey("occupationRatioList")) {
			List<Map> list = (List) this.data.get("occupationRatioList");
			for (Map map : list) {
				if (map.get("id") != "") {
					map.put("modifyTime", now);
					map.put("modifierId", accountid);
					upsqls.addAll(super.updateSql(map));
				} else {
					// 添加新增选项
					map.put("contractId", (Integer) this.data.get("id"));
					map.put("createTime", now);
					map.put("creatorId", accountid);
					upsqls.addAll(addSql(map));
				}
			}
			data.remove("occupationRatioList");// 去除子表信息
		}
		//--------
		
		
		// 删除所有去除项
				this.map.put("table", "contract_product");
				this.map.put("id",this.map.get("id2"));
				upsqls2 = deleteSql(this.map);
				if (this.data.containsKey("biaodiList")) {
					List<Map> list = (List) this.data.get("biaodiList");
					for (Map map : list) {
						if (map.get("id") != "") {
							map.put("modifyTime", now);
							map.put("modifierId", accountid);
							upsqls2.addAll(super.updateSql(map));
						} else {
							// 添加新增选项
							map.put("contractId", (Integer) this.data.get("id"));
							map.put("createTime", now);
							map.put("creatorId", accountid);
							upsqls2.addAll(addSql(map));
						}
					}
					data.remove("biaodiList");// 去除子表信息
				}

				this.data.put("modifyTime", now);//
				this.data.put("modifierId", accountid);
		String flag = super.update();// 更新主表信息
		if (flag.equals("true")) {
			DataBase db = new DataBase();
			try {
				db.updateBatch(upsqls.toArray(new String[upsqls.size()]));
				db.updateBatch(upsqls2.toArray(new String[upsqls2.size()]));
			} catch (DbException e) {
				e.printStackTrace();
				return getReturnString("flase");
			} finally {
				try {
					db.close();
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}
		return getReturnString(flag);

	}

	public String delete() {
		
		return getReturnString(super.delete());
	}

	public String updatefieldByIds() {

		return getReturnString(super.updatefieldByIds());
	}

	public String getBeans() {
		return super.getBeans();
	}

	// 获取当前登录人的id
	public String getAccountId() {
		this.session = request.getSession();
		String accountid = ((AccountBean) this.session
				.getAttribute("accountBean")).getId();
		return getReturnString(accountid);
	}

	// 获取项目名称
	public String getProjectNames() {
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		String cid = this.request.getParameter("cid");
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select top 100 percent  t.* from(");
		
		sql.append("select id value, projectname text,accountid from customer where enable= '1' )t where 1=1");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
        sql.append(" or t.accountid ='"+selfId+"')");
		
		return super.getBeansBySql(sql.toString());
	}

	// 根据项目获取客户名称
	public String getProjectByCustomerName() {
		String sql = "select id,name customername from customer where 1=1 ";
		return super.getBeansBySql(sql);
	}
	
	/**
	 * 查看下级和自己的合同资料信息
	 *  2014年12月26日 09:50:35
	 * @return
	 */
	public String getBeansSearch(){ 
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
		String selfId = accountBean.getId();//当前登录人id
		SqlUtil sqlUtil = new SqlUtil();
		
		StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from contract t where 1=1");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.creatorid"));
        sql.append(" or t.creatorid ='"+selfId+"')");
		sql.append(super.getSearchStr(super.map));
		sql .append( " order by id desc");
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
}
