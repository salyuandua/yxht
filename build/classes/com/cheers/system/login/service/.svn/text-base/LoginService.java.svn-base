package com.cheers.system.login.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.BusinessMethod;
import com.cheers.commonMethod.GetProperty;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.DataFormat;

public class LoginService extends Dao{

	DataFormat df = new DataFormat();
	BusinessMethod bm = new BusinessMethod();
	
	public String add(){

		return getReturnString(super.add());
		
	}
	
	public String update(){

		return getReturnString(super.update());
		
	}
	
	public String delete(){
		
			return getReturnString(super.delete());
		
	}
	
	public String checkDuplicate(){
		
		Map map = new HashMap();
		
		return getReturnString(super.checkDuplicate(map));
	}
	public AccountBean getAccount(String accountCode){
		AccountBean account =new AccountBean();
		DataBase db = null;
		try {
			db = new DataBase();
			db.connectDb();
			String sql="select * from sysaccount where account='"+accountCode+"'";
			ResultSet rs =db.queryAll(sql);
			if (rs.next()) {
				account.setAccount(accountCode);
				account.setId(rs.getString("id"));
				account.setName(rs.getString("name"));
				account.setRoleid(rs.getString("roleid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		return account;
	}
	
	//数据权限设置
	public AccountBean setDataRight(AccountBean accountBean){
		//此账号所能看到的账号ids 下级
				String _canAccountIds = this.getAccountIdsByAccountId(accountBean);
				accountBean.set_canAccountIds(_canAccountIds);
				
				/*//此账号上级的账号ids 上级
				String upAccountIds = this.getUpAccountIdsByAccountId(accountBean);
				upAccountIds = (upAccountIds==null || "".equals(upAccountIds))?"-1":upAccountIds;
				System.out.println("_upAccountIds>"+upAccountIds);
				accountBean.set_upAccountIds(upAccountIds); */
				
				//此账号所能看到的客户ids 自己设置的加上下级业务员所包含的
				String canClientIds = this.getCanClientIdsByAccountId(accountBean);
				canClientIds = (canClientIds==null || "".equals(canClientIds))?"-1":canClientIds;
				//System.out.println("_canClientIds>"+canClientIds);
				accountBean.set_canClientIds(canClientIds);
				
				return accountBean;
	}
	
	public String getAccountIdsByAccountId(AccountBean accountBean){
        String accountId = accountBean.getId();
        String ret = accountId;
        
        ArrayList<String> list = new ArrayList<String>();
        DataBase db = new DataBase();
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        ArrayList<String> selectList = new ArrayList<String>();
        sql.append("select st1.vorder treeOrder1, aa.level treeOrder2 from sysaccount_post aa, system_tree st1 ");
        sql.append("where st1.id=aa.orgId ");
        sql.append("and aa.accountId = ");
        sql.append(accountId);
        try {
            db.connectDb();
            rs = db.queryAll(sql.toString());
            while(rs!=null&&rs.next()){
                String treeOrder1 = df.stringCheckNull(rs.getString("treeOrder1"));
                String treeOrder2 = df.stringCheckNull(rs.getString("treeOrder2"));
                list.add(treeOrder1+","+treeOrder2);  //部门treeOrder+“,”+级别treeOrder
            }
            
            //boolean isFirst = true;
            if(list.size()>0){
                //sql.setLength(0);
                //sql.append("select distinct accountId from (");
                for(String st : list){
                    String treeOrder1 = st.split(",")[0];  //部门  vorder
                    String treeOrder2 = st.split(",")[1];  //级别数字
                    
//                    if(isFirst)
//                        isFirst = false;
//                    else
//                        sql.append(" union ");
                    
                    StringBuffer selectsql = new StringBuffer();
                    ResultSet selectrs = null;
                   
                    selectsql.append(" select distinct aa.accountid from sysaccount_post aa where aa.orgid in ( select id from System_tree where categoryType='1' and vorder like '"+treeOrder1+"%' ) ");
                    selectrs = db.queryAll(selectsql.toString());
                    //System.out.println("+++++++++++++++++"+selectsql);
                    while(selectrs!=null&&selectrs.next()){
                    	selectList.add(selectrs.getString("accountId"));
                    }
                    
                    ResultSet removetrs = null;
                    String removesql = " select distinct aa.accountid from sysaccount_post aa where aa.orgid = (select id from System_tree where categoryType='1' and vorder = '"+ treeOrder1 + "') and aa.level >= '"+ treeOrder2 +"'  ";
                    removetrs = db.queryAll(removesql.toString());
                    //System.out.println("-------------------------"+removesql);
                    while(removetrs!=null&&removetrs.next()){
                    	selectList.remove(removetrs.getString("accountId"));
                    }
                    
                    
                    
//                    sql.append("select distinct aa.accountId from sysaccount_post aa where ");
//                    sql.append("aa.orgId in () ");
//                    sql.append("and aa.level < "+treeOrder2 );
                }
//                sql.append(") ac");
                //rs = db.queryAll(sql.toString());
                //isFirst = true;
                //System.out.println("~~~~~~~~~~~"+sql);
                //根据组织机构上下级和人员分组上下级获得下级accountIds
//                while(rs!=null&&rs.next()){
//                    ret = ret + "," + df.stringCheckNull(rs.getString("accountId"));
//                }
                Iterator<String> it=selectList.iterator();
                while(it.hasNext()){
                    ret = ret + "," + df.stringCheckNull(it.next());
                }
            }
            
            
        } catch (DbException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                db.close();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        
        
        return ret;
    }
	
	/*
	 * 根据accountId返回该账号上级的人员ids，用“,”隔开
	 * 
	 * */
	public String getUpAccountIdsByAccountId(AccountBean accountBean){
		String accountId = accountBean.getId();
		String ret = accountId;
		
		ArrayList<String> list = new ArrayList<String>();
		DataBase db = new DataBase();
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select st1.vorder treeOrder1, st2.vorder treeOrder2 from sysaccount_post aa, SYSTEM_TREE st1, SYSTEM_TREE st2 ");
		sql.append("where 1=1 and aa.isenable='1' and st1.id=aa.orgId and st2.id=aa.positionId ");
		sql.append("and aa.accountId = ");
		sql.append(accountId);
		try {
			db.connectDb();
			rs = db.queryAll(sql.toString());
			while(rs!=null&&rs.next()){
				String treeOrder1 = df.stringCheckNull(rs.getString("treeOrder1"));
				String treeOrder2 = df.stringCheckNull(rs.getString("treeOrder2"));
				list.add(treeOrder1+","+treeOrder2);  //部门treeOrder+“,”+人员分组treeOrder
			}
			
			boolean isFirst = true;
			if(list.size()>0){
				sql.setLength(0);
				sql.append("select distinct accountId from (");
				for(String st : list){
					String treeOrder1 = st.split(",")[0];  //部门treeOrder
					String treeOrder2 = st.split(",")[1];  //人员分组treeOrder
					if(isFirst)
						isFirst = false;
					else
						sql.append(" union ");
					sql.append("select distinct aa.accountId from sysaccount_post aa where ");
					sql.append("aa.orgId in (select id from system_tree where categoryType='1' and '"+treeOrder1+"' like vorder+'%' ) ");
					sql.append("and aa.positionId in (select id from system_tree where categoryType='2' and '"+treeOrder2+"' like vorder+'%' and vorder != '"+treeOrder2+"')");
				}
				sql.append(") ab");
				rs = db.queryAll(sql.toString());
				isFirst = true;
				//根据组织机构上下级和人员分组上下级获得下级accountIds
				while(rs!=null&&rs.next()){
					ret = ret + "," + df.stringCheckNull(rs.getString("accountId"));
				}
			}
			
//			GetProperty getProperty = new GetProperty();
//			String zzjgnb = getProperty.getZzjgnb();  //内部组织机构的treeOrder
//			String zzjgwb = getProperty.getZzjgwb();  //外部组织机构的treeOrder
			//组织机构为公司外部的登入人，公司内部所有人都是他的上级
//			df.Out(accountBean.get_departTreeOrder()+"<>"+zzjgwb);
//			if(accountBean.get_departTreeOrder().indexOf(zzjgwb)==0){
//				//上述代码已根据上下级获得上级accountIds
//				//现在只需要将公司内部的accountIds加入就可以了
//				sql.setLength(0);
//				sql.append("select act.id accountId from ACCOUNT act,SYSTEM_TREE st ");
//				sql.append("where st.id=act.departId and st.treeOrder like '"+zzjgnb+"%'");
//				rs = db.queryAll(sql.toString());
//				isFirst = true;
//				while(rs!=null&&rs.next()){
//					ret = ret + "," + df.stringCheckNull(rs.getString("accountId"));
//				}
//			}else{  //组织机构为公司内部的登入人，上级就是根据上下级获得的
//				//上述代码已根据上下级获得上级accountIds
//				
//			}
			
		} catch (DbException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				db.close();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		
		
		return ret;
	}
	
	/*
	 * 根据accountId返回 此账号所能看到的客户ids 自己设置的加上下级业务员所包含的 ids，用“,”隔开
	 * 
	 * */
	public String getCanClientIdsByAccountId(AccountBean accountBean){
		String ret = "";
		DataBase db = new DataBase();
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		
		GetProperty gp = new GetProperty();
		if(gp.getId_Gly().equals(accountBean.getId())) {//管理员能看见所有客户
			sql.append("select id clientId from customer ");
		}else {
			sql.append("select clientId from Customer_account ac ");
			sql.append("where 1=1 and ac.enable='1' and ac.accountId in (");
			sql.append(accountBean.get_canAccountIds()+")");
		}
		
		try {
			db.connectDb();
			rs = db.queryAll(sql.toString());
			boolean isFirst = true;
			while(rs!=null&&rs.next()){
				if(isFirst){
					isFirst = false;
					ret = df.stringCheckNull(rs.getString("clientId"));
				}else{
					ret = ret + "," + df.stringCheckNull(rs.getString("clientId"));
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				db.close();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
}
