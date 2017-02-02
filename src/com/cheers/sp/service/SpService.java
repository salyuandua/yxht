package com.cheers.sp.service;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.account.bean.AccountBean;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.DateFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 自定义审批的tablename为zdysp
 * @author ljc
 *
 */
public class SpService  extends Dao implements Serializable{
	
	
	public Object addForm;
	@SuppressWarnings("unchecked")
	/*
	 * 审批流添加
	 * 
	 */
	public String add() {
		if(((String)data.get("table")).equalsIgnoreCase("shenpinode")){//审批流
			data.put("creatorid", ((AccountBean)session.getAttribute("accountBean")).getId());
			
		}
		return getReturnString(super.add());
	}
	
    /*
     * 审批流节点添加
     */
	public String addSPNode(){
	  StringBuffer sqls=new StringBuffer();
	  sqls.setLength(0);
	  String typeid=(String) this.data.get("typeId");
	  String ruleid=(String) this.data.get("ruleId");
	  String nodename=(String) this.data.get("nodeName");
	  String sprank=(String) this.data.get("spRank");
	  if(typeid==null||ruleid==null||nodename==null||sprank==null){
		  return getReturnString("false");
	  }
	  ArrayList accountlist=new ArrayList();
	  accountlist=(ArrayList) this.data.get("accountList");
	  sqls.append("select sprank from shenpinode where typeid='"+typeid+"' and ruleid='"+ruleid+"'and sprank='"+sprank+"'");
	  DataBase db= new DataBase();
	  ResultSet rs=null;
	  try {
		db.getConnection();
		rs=db.queryAll(sqls.toString());
		try {
			if(rs!=null&&rs.next()){
				
			}else{
				super.add();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} catch (DbException e) {
		e.printStackTrace();
		try {
			db.close();
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		return getReturnString("false");
	}finally{
		try {
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		
	}
		 return getReturnString("true");
	 
	}
	/*接口作用：如果本记录内的字段accountid=0然后根据业务名+记录获取当前的审批节点以及审批人员写入到审批表
	 * tablename业务表名
	 * recordid 审批记录的ID
	 * bspaccountid被审批人账号的ID
	 * 
	 * 使用规则:如果业务表内的accountid
	 */
	public String  setFirstSp(String tablename,String recordid,String bspaccountid,String getshenpitypeid) throws SQLException{
		String re="false";
		if(tablename==null||recordid==null||bspaccountid==null){
			return getReturnString(re);
		}
		
		DataBase db = new DataBase();
		ResultSet rs= null;
		ResultSet rs2= null;
		try {
			db.connectDb();
			String sqlstr="";
			String sptypestr="";
			if(getshenpitypeid!=null&&!"".equals(getshenpitypeid)){
				sptypestr="and t.fromid='"+getshenpitypeid+"'";
			}
			sqlstr="select n.sprank from shenpitype t,shenpirule r, shenpinode n, shenpiflow f where t.id=r.typeid and n.ruleid=r.id and f.typeid=t.id and f.ruleid=r.id and f.sprank=n.sprank and t.tablename='"+tablename+"' and n.sprank='1' and r.isenable='1' and f.accountid='"+bspaccountid+"' "+ sptypestr+"";
			rs=db.queryAll(sqlstr);
			if(rs!=null&&rs.next()){//他参与审批
					sqlstr="select t.id as typeid,r.id as ruleid,n.sprank,n.nodename from shenpitype t,shenpirule r, shenpinode n where t.id=r.typeid and n.ruleid=r.id and r.isenable='1' and t.tablename='"+tablename+"' and n.sprank='2' "+ sptypestr+"";
				rs2=db.queryAll(sqlstr);
                if(rs2!=null && rs2.next()){
                	String[] sqlstr2=new String[2];
                	sqlstr2[0]="update "+tablename+" set spnodename='等待"+rs2.getString("nodename")+"审批',spstate='0' where id='"+recordid+"'";
                	sqlstr2[1]="insert into shenpiywsearch(typeid,ruleid,recordid,sprank,spstate)values("+rs2.getInt("typeid")+","+rs2.getInt("ruleid")+",'"+recordid+"',2,0)";
                	db.updateBatch(sqlstr2);
                }else{
                	sqlstr="update "+tablename+" set spnodename='审批结束',spstate='1' where id='"+recordid+"'";
                	db.update(sqlstr);
                }
  			    
			}else{
			
					sqlstr="select t.id as typeid,r.id as ruleid,n.sprank,n.nodename from shenpitype t,shenpirule r, shenpinode n where t.id=r.typeid and n.ruleid=r.id and r.isenable='1' and t.tablename='"+tablename+"' and n.sprank='1' "+ sptypestr+"";
				
				
				rs=db.queryAll(sqlstr);
				if(rs!=null&&rs.next()){
					String[] sqlstr2=new String[2];
	            	sqlstr2[0]="update "+tablename+" set spnodename='等待"+rs.getString("nodename")+"审批',spstate='0' where id='"+recordid+"'";
	            	sqlstr2[1]="insert into shenpiywsearch(typeid,ruleid,recordid,sprank,spstate)values("+rs.getInt("typeid")+","+rs.getInt("ruleid")+",'"+recordid+"',1,0)";
	            	db.updateBatch(sqlstr2);	
				}
				
			}
			re="true";	
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			try {
				db.close();
			} catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				db.close();
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	  return re;
	}
	 
	/*
	 * 作用：拒绝修改后重新发起审批流
	 * tablename业务表名
	 * recordid 审批记录的ID
	 * bspaccountid 被审批人ID
	 * 
	 * 
	 */
	 public String  setModSp(String tablename,int recordid,String bspaccountid,String getshenpitypeid) throws SQLException{
		String re="false";
		if(tablename==null||("").equals(recordid)||bspaccountid==null){
			return getReturnString(re);
		}
		
		DataBase db = new DataBase();
		ResultSet rs= null;
		ResultSet rs2= null;
		try {
			db.connectDb();
			String sqlstr="";
			String sptypestr="";
			if(getshenpitypeid!=null&&!"".equals(getshenpitypeid)){
				sptypestr="and t.fromid='"+getshenpitypeid+"'";
			}
				sqlstr="select n.sprank from shenpitype t,shenpirule r, shenpinode n, shenpiflow f where t.id=r.typeid and n.ruleid=r.id and f.typeid=t.id and f.ruleid=r.id and f.sprank=n.sprank and t.tablename='"+tablename+"' and n.sprank='1' and r.isenable='1' and f.accountid='"+bspaccountid+"'  "+ sptypestr+"";
			
			
			rs=db.queryAll(sqlstr);
			if(rs!=null&&rs.next()){//他参与审批

					sqlstr="select t.id as typeid,r.id as ruleid,n.sprank,n.nodename from shenpitype t,shenpirule r, shenpinode n where t.id=r.typeid and n.ruleid=r.id and r.isenable='1' and t.tablename='"+tablename+"' and n.sprank='2'  "+ sptypestr+"";
				
				
				rs2=db.queryAll(sqlstr);
                if(rs2!=null && rs2.next()){
                	String[] sqlstr2=new String[2];
                	sqlstr2[0]="update "+tablename+" set spnodename='等待"+rs2.getString("nodename")+"审批',spstate='0' where id='"+recordid+"'";
                	sqlstr2[1]="update shenpiywsearch set spstate='0',sprank='2' where typeid="+rs2.getInt("typeid")+" and ruleid="+rs2.getInt("ruleid")+" and recordid='"+recordid+"'";
                	db.updateBatch(sqlstr2);
                }else{
                	sqlstr="update "+tablename+" set spnodename='审批结束',spstate='1' where id='"+recordid+"'";
                	db.update(sqlstr);
                }
  			    
			}else{
				sqlstr="select t.id as typeid,r.id as ruleid,n.sprank,n.nodename from shenpitype t,shenpirule r, shenpinode n where t.id=r.typeid and n.ruleid=r.id and r.isenable='1' and t.tablename='"+tablename+"' and n.sprank='1'";
				rs=db.queryAll(sqlstr);
				if(rs!=null&&rs.next()){
					String[] sqlstr2=new String[2];
	            	sqlstr2[0]="update "+tablename+" set spnodename='等待"+rs.getString("nodename")+"审批',spstate='0' where id='"+recordid+"'";
	            	sqlstr2[1]="update shenpiywsearch set spstate='0',sprank='1' where typeid="+rs.getInt("typeid")+" and ruleid="+rs.getInt("ruleid")+" and recordid='"+recordid+"'";
	            	db.updateBatch(sqlstr2);	
				}
				
			}
			re="true";	
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			try {
				db.close();
			} catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				db.close();
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	  return re;
	 }
	 
	 /*
	  * 作用：撤销审核
	  */
	 	 	 
	 
	/*
	 * 获取需要审批的recordid
	 * tablename、业务表明
	 * spaccountid 审批人accountid
	 * 返回list记录recordid
	 */
	 public List<DynaBean> getSPrecord(String tablename,ArrayList<String> recordid,String spaccountid){
			List nodes=new ArrayList();
			
			try {
				if(tablename==null||recordid==null||spaccountid==null){
					return (nodes);
				}
				recordid.add("0");
				String als=recordid.toString().replace("[", "(").replace("]",")");
				String sqlstr="select DISTINCT(s.recordid) recordid from shenpitype t,shenpirule r,shenpiflow f, shenpiywsearch s where t.id=r.typeid and r.id=f.ruleid and t.id=s.typeid and r.id=s.ruleid and f.sprank=s.sprank and s.recordid in "+als+" and t.tablename = '"+tablename+"' and f.accountid="+ spaccountid +" and r.isenable='1' and spstate=0 union select DISTINCT(s.recordid) recordid from shenpitype t,shenpirule r, shenpiywsearch s where t.id=r.typeid and t.id=s.typeid and r.id=s.ruleid  and s.recordid in "+als+" and t.tablename = '"+tablename+"' and s.tpaccountid="+ spaccountid +" and r.isenable='1' and s.spstate='2'";
				excuteSQLPageQuery(sqlstr);
				nodes= getRows();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
			}
			return nodes;
			
		}	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 /*特殊事项审批调用
		 * 获取需要审批的recordid
		 * tablename、业务表明
		 * spaccountid 审批人accountid
		 * 返回list记录recordid
		 */
		public List<DynaBean> getSPrecordT(String tablename,ArrayList<String> recordid,String spaccountid){
			List nodes=new ArrayList();
			
			try {
				if(tablename==null||recordid==null||spaccountid==null){
					return (nodes);
				}
				recordid.add("0");
				String als=recordid.toString().replace("[", "(").replace("]",")");
				String sqlstr="";
				
					sqlstr="select DISTINCT(s.recordid) recordid from " +
							"shenpitype t," +
							"shenpirule r," +
							"shenpiflow f, " +
							"shenpiywsearch s," +
							""+tablename+" " +
									"where t.id=r.typeid " +
									"and r.id=f.ruleid " +
									"and t.id=s.typeid " +
									"and r.id=s.ruleid " +
									"and f.sprank=s.sprank " +
									"and s.recordid in "+als+" " +
											"and t.tablename = '"+tablename+"' " +
											"and t.fromid="+tablename+".typeid " +
											"and s.recordid="+tablename+".id " +
											"and f.accountid="+ spaccountid +" " +
											"and r.isenable='1' " +
											"and s.spstate=0 " +
											"union " +
											"select DISTINCT(s.recordid) recordid from shenpitype t,shenpirule r, shenpiywsearch s,"+tablename+" where t.id=r.typeid and t.id=s.typeid and r.id=s.ruleid  and s.recordid in "+als+" and t.tablename = '"+tablename+"'   and t.fromid="+tablename+".typeid and s.recordid="+tablename+".id  and s.tpaccountid="+ spaccountid +" and r.isenable='1' and s.spstate='2'";
				
				excuteSQLPageQuery(sqlstr);
				nodes= getRows();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
			}
			return nodes;
			
		}
		
		
		//查询我审批过的
		
		public List<DynaBean> getMySP(String tablename, String spaccountid)
		  {
		    List nodes = new ArrayList();
		    try
		    {
		      if ((tablename == null) || (spaccountid == null)) {
		        return nodes;
		      }

		      String sqlstr = "select DISTINCT(a.recordid) recordid from shenpitype t,shenpiywanswer a where t.id=a.typeid and t.tablename = '" + tablename + "' and a.accountid=" + spaccountid;
		      excuteSQLPageQuery(sqlstr);
		      nodes = getRows();
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }

		    return nodes;
		  }
		
		
		
		
		
	/*作用:获取审批内容信息
	 * tablename 表名,recordid当前记录
	 * 返回list内容 第一列代表审批状态,第二列代表批复内容
	 */
		public List<DynaBean> getSPcontent(String tablename,String recordid){
			
			List nodes=new ArrayList();

			  if(tablename==null||recordid==null){
				  return nodes;
			  }
			  //order by sptime加上报错
			String sqlstr="select top 100 percent * from (select a.answerstatedesc,a.spmatter,a.sptime from shenpiywanswer a,shenpitype t where a.typeid=t.id and a.recordid='"+recordid+"' and t.tablename='"+tablename+"'  union select '等待'+n.nodename+'审批','','' from shenpinode n,shenpitype t,shenpirule r where n.typeid=t.id  and n.ruleid=r.id and t.id=r.typeid and r.isenable='1' and sprank>=(select s.sprank from shenpiywsearch s,shenpitype t where s.typeid=t.id and t.tablename='"+tablename+"' and s.recordid='"+recordid+"'and s.spstate='0') ) a ORDER BY sptime DESC ";
			//excuteSQLPageQuery(sqlstr);
			sqlstr+=getSearchStr(map);
			excuteSQLPageQuery(sqlstr);
			nodes=getRows();
			return nodes;
		}
	
	
	
	/* 审批流提交接口
	 * tablename业务表名
	 * recordid 业务id
	 * spaccountid 审批人的id
	 * fhaccountid 拒绝返回人ID默认值0
	 * tpaccountid 特批人id
	 * shenpiresult  -1审批不通过、1审批通过、2转特批、 -11特批拒绝、11特批通过继续审批流、12特批再转特批、13特批结束审批流
	 * shenpicontent 审批内容
	 * tpname 特批人
	 * 
	 */
	
		public String addShenPi(String tablename,String recordid,String spaccountid,String spname,String tpaccountid,String tpname,String shenpiresult,String shenpicontent,String getshenpitypeid) throws DbException, SQLException{
			DataBase db= new DataBase();
			String result="审批失败";
			if(tablename==null||recordid==null||spaccountid==null||spname==null||tpaccountid==null||tpname==null||shenpiresult==null||shenpicontent==null){
				return "审批失败！请检查数据是否完整";
			}
			try {
				db.connectDb();
				ResultSet rs=null;
				ResultSet rs2=null;
				String shenpitypestr="";
				if("1".equals(shenpiresult))//审批通过
				{
					if(getshenpitypeid!=null&&!"".equals(getshenpitypeid)&&!"null".equals(getshenpitypeid)&&!"NULL".equals(getshenpitypeid)){
						shenpitypestr="and t.fromid='"+getshenpitypeid+"'";
					}
					String sqlstr=" select s.id,s.sprank,s.recordid,s.typeid,s.ruleid from shenpitype t,shenpirule r,shenpiflow f, shenpiywsearch s where t.id=r.typeid and r.id=f.ruleid and t.id=s.typeid and r.id=s.ruleid and f.sprank=s.sprank and s.recordid="+recordid+" and t.tablename = '"+tablename+"' and f.accountid="+ spaccountid +" and r.isenable='1'";
					rs=db.queryAll(sqlstr);
					
					if(rs!=null && rs.next()){//判断是否存在
							sqlstr="select isnull(max(sprank),0) sprank from shenpinode where ruleid in(select r.id from shenpirule r, shenpitype t where r.typeid=t.id and r.isenable='1' and t.tablename='"+tablename+"' "+shenpitypestr+")";
							rs2=db.queryAll(sqlstr);
							if(rs2!=null&& rs2.next()&&
									rs2.getInt("sprank")>0){//判断是否审批结束
								if(rs.getInt("sprank")==rs2.getInt("sprank")){
									
									String[] sqlstr2=new String[2];
									sqlstr2[0]="update shenpiywsearch set spstate='1' where id='"+rs.getString("id")+"'";
									sqlstr2[1]="update "+tablename+" set spstate='1',spnodename='审批结束' where id="+recordid;
									db.updateBatch(sqlstr2);
								}else{
									sqlstr="select nodename from shenpitype t,shenpirule r,shenpinode n where t.id=r.typeid and r.id=n.ruleid and r.isenable='1' and t.tablename='"+tablename+"' "+shenpitypestr+" and n.sprank="+rs.getInt("sprank")+"+1";
									rs2=db.queryAll(sqlstr);
									String[] sqlstr2=new String[2];
									sqlstr2[0]="update shenpiywsearch set sprank=sprank+1 where id='"+rs.getString("id")+"'";
									if(rs2!=null&& rs2.next())
									sqlstr2[1]="update "+tablename+" set spnodename='等待"+rs2.getString("nodename")+"' where id="+recordid;
									db.updateBatch(sqlstr2);;
								}
							}
							sqlstr="insert into shenpiywanswer(accountid,typeid,ruleid,recordid,answerstate,answerstatedesc,spmatter,sptime)values"+
							"('"+spaccountid+"','"+rs.getString("typeid")+"','"+rs.getString("ruleid")+"','"+recordid+"','1','"+spname+"审批通过','"+shenpicontent+"','"+DateFormat.getNowTime()+"')";
							db.update(sqlstr);
							result="审批成功";
					}else{
						result="不应该到您审批";
					}
				}else if("-1".equals(shenpiresult)){//审批拒绝
					String sqlstr=" select s.id,s.sprank,s.recordid,s.typeid,s.ruleid from shenpitype t,shenpirule r,shenpiflow f, shenpiywsearch s where t.id=r.typeid and r.id=f.ruleid and t.id=s.typeid and r.id=s.ruleid and f.sprank=s.sprank and s.recordid="+recordid+" and t.tablename = '"+tablename+"' and f.accountid="+ spaccountid +" and r.isenable='1'"+shenpitypestr;
					rs=db.queryAll(sqlstr);
					
					if(rs!=null && rs.next()){//判断是否存在该此人审批
							
							String[] sqlstr2= new String[3];
							sqlstr2[0]="insert into shenpiywanswer(accountid,typeid,ruleid,recordid,answerstate,answerstatedesc,spmatter,sptime)values"+
							"('"+spaccountid+"','"+rs.getString("typeid")+"','"+rs.getString("ruleid")+"','"+recordid+"','-1','"+spname+"审批拒绝','"+shenpicontent+"','"+DateFormat.getNowTime()+"')";
							sqlstr2[1]="update shenpiywsearch set spstate='-1' where id='"+rs.getString("id")+"'";
							sqlstr2[2]="update "+tablename+" set spstate='-1',spnodename='"+spname+"审批拒绝' where id="+recordid;
							db.updateBatch(sqlstr2);
							result="审批成功";
					}else{
						result="不应该到您审批";
					}
				}else if("2".equals(shenpiresult)){//转特批
					String sqlstr="select s.id,s.sprank,s.recordid,s.typeid,s.ruleid from shenpitype t,shenpirule r,shenpiflow f, shenpiywsearch s where t.id=r.typeid and r.id=f.ruleid and t.id=s.typeid and r.id=s.ruleid and f.sprank=s.sprank and s.recordid="+recordid+" and t.tablename = '"+tablename+"' and f.accountid="+ spaccountid +" and r.isenable='1' and s.spstate='0' union select s.id,s.sprank,s.recordid,s.typeid,s.ruleid from shenpitype t,shenpirule r, shenpiywsearch s where t.id=r.typeid and t.id=s.typeid and r.id=s.ruleid  and s.recordid="+recordid+" and t.tablename = '"+tablename+"' and s.tpaccountid="+ spaccountid +" and r.isenable='1' and s.spstate='2'";
					rs=db.queryAll(sqlstr);
					
					if(rs!=null&& rs.next()){//判断是否存在
							
						String[] sqlstr2= new String[3];
						sqlstr2[0]="insert into shenpiywanswer(accountid,typeid,ruleid,recordid,answerstate,answerstatedesc,spmatter,sptime)values"+
						"('"+spaccountid+"','"+rs.getString("typeid")+"','"+rs.getString("ruleid")+"','"+recordid+"','2','"+spname+"转"+tpname+"特批','"+shenpicontent+"','"+DateFormat.getNowTime()+"')";
						sqlstr2[1]="update shenpiywsearch set spstate='2',tpaccountid='"+tpaccountid+"',tpname='"+tpname+"' where id='"+rs.getString("id")+"'";
						sqlstr2[2]="update "+tablename+" set spstate='2',spnodename='等待"+tpname+"特批' where id="+recordid;
						db.updateBatch(sqlstr2);	
							result="转特批成功";
					}else{
						result="不应该到您审批";
					}
				}else if("11".equals(shenpiresult)){//特批通过返回
					String sqlstr="select s.id,s.sprank,s.recordid,s.typeid,s.ruleid from shenpitype t,shenpirule r, shenpiywsearch s where t.id=r.typeid  and t.id=s.typeid and r.id=s.ruleid and s.recordid="+recordid+" and t.tablename = '"+tablename+"' and s.tpaccountid='"+spaccountid+"' and s.spstate='2'  and r.isenable='1'";
					rs=db.queryAll(sqlstr);
					
					if(rs!=null && rs.next()){//判断是否存在
						sqlstr="select nodename from shenpitype t,shenpirule r,shenpinode n where t.id=r.typeid and r.id=n.ruleid and r.isenable='1' and t.tablename='"+tablename+"'and n.sprank="+rs.getInt("sprank")+" "+shenpitypestr+"";
						rs2=db.queryAll(sqlstr);	
						String[] sqlstr2= new String[3];
						sqlstr2[0]="insert into shenpiywanswer(accountid,typeid,ruleid,recordid,answerstate,answerstatedesc,spmatter,sptime)values"+
						"('"+spaccountid+"','"+rs.getString("typeid")+"','"+rs.getString("ruleid")+"','"+recordid+"','2','"+spname+"特批通过','"+shenpicontent+"','"+DateFormat.getNowTime()+"')";
						sqlstr2[1]="update shenpiywsearch set spstate='0',tpaccountid='"+spaccountid+"',tpname='"+spname+"' where id='"+rs.getString("id")+"'";
						if(rs2.next())
						sqlstr2[2]="update "+tablename+" set spstate='0',spnodename='"+rs2.getString("nodename")+"' where id="+recordid;
						
						db.updateBatch(sqlstr2);	
							result="审批成功";
					}else{
						result="不应该到您审批";
					}
				}else if("12".equals(shenpiresult)){//特批转特批
					String sqlstr="select s.id,s.sprank,s.recordid,s.typeid,s.ruleid from shenpitype t,shenpirule r, shenpiywsearch s where t.id=r.typeid and r.id=f.ruleid and t.id=s.typeid and r.id=s.ruleid and s.recordid="+recordid+" and t.tablename = '"+tablename+"' and s.tpaccountid='"+tpaccountid+"' and s.spstate='2'  and r.isenable='1'";
					rs=db.queryAll(sqlstr);
					
					if(rs!=null && rs.next()){//判断是否存在
							
						String[] sqlstr2= new String[3];
						sqlstr2[0]="insert into shenpiywanswer(accountid,typeid,ruleid,recordid,answerstate,answerstatedesc,spmatter,sptime)values"+
						"('"+spaccountid+"','"+rs.getString("typeid")+"','"+rs.getString("ruleid")+"','"+recordid+"','2','特批"+spname+"转"+tpname+"特批','"+shenpicontent+"','"+DateFormat.getNowTime()+"')";
						sqlstr2[1]="update shenpiywsearch set spstate='2',tpaccountid='"+tpaccountid+"',tpname='"+tpname+"' where id='"+rs.getString("id")+"'";
						sqlstr2[2]="update "+tablename+" set spstate='2',spnodename='转"+tpname+"特批' where id="+recordid;
						db.updateBatch(sqlstr2);	
							result="转特批成功";
					}else{
						result="不应该到您审批";
					}
				}else if("13".equals(shenpiresult)){//特批结束审批
					String sqlstr="select s.id,s.sprank,s.recordid,s.typeid,s.ruleid from shenpitype t,shenpirule r, shenpiywsearch s where t.id=r.typeid  and t.id=s.typeid and r.id=s.ruleid and s.recordid="+recordid+" and t.tablename = '"+tablename+"' and s.tpaccountid='"+spaccountid+"' and s.spstate='2'  and r.isenable='1'";
					rs=db.queryAll(sqlstr);
					
					if(rs!=null && rs.next()){//判断是否存在该此人审批
							
							String[] sqlstr2= new String[3];
							sqlstr2[0]="insert into shenpiywanswer(accountid,typeid,ruleid,recordid,answerstate,answerstatedesc,spmatter,sptime)values"+
							"('"+spaccountid+"','"+rs.getString("typeid")+"','"+rs.getString("ruleid")+"','"+recordid+"','-11','"+tpname+"特批结束审批流','"+shenpicontent+"','"+DateFormat.getNowTime()+"')";
							sqlstr2[1]="update shenpiywsearch set spstate='1' where id='"+rs.getString("id")+"'";
							sqlstr2[2]="update "+tablename+" set spstate='1',spnodename='审批结束' where id="+recordid;
							db.updateBatch(sqlstr2);
							result="审批成功";
					}else{
						result="不应该到您审批";
					}
				}
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				db.close();
			}
			
			return result;
		}
	
	

	
	/**
	 * 获得某一类型下所有流程
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getFlows(){
		
		String typeId=request.getParameter("typeId");
		if(typeId==null){
			return getReturnString("false");
		}
		String sql="select top 100 percent * from SHENPIRULE where   typeId="+typeId +"";
		excuteSQLPageQuery(sql);
		List end=new ArrayList();
		result=new ArrayList();
		end = getRows();
		if(end!=null){
			 for (int i = 0; i < end.size(); i++) {
				 System.out.println(end.get(i).getClass());
				DynaBean obj= (DynaBean) end.get(i);
				String ruleid= String.valueOf( obj.get("id"));
				
				String sqlnode="select top 100 percent * from shenpinode where typeid='"+typeId+"' and ruleid ='"+ruleid+"' order by sprank";
				excuteSQLPageQuery(sqlnode);
				List nodes=new ArrayList();
				nodes= getRows();
				ArrayList nodelist = new ArrayList();
				Map m=new HashMap(); 
				if(nodes!=null){
					for(int j=0;j<nodes.size();j++){
						Map m2=new HashMap(); 
						DynaBean nodeobj= (DynaBean) nodes.get(j);
						String nodeid= String.valueOf(nodeobj.get("id"));
						String sqlflow="select top 100 percent a.id,s.ruleid, a.name,s.orgid from shenpiflow s,sysaccount a where s.accountid=a.id and  nodeid='"+nodeid+"'";
						excuteSQLPageQuery(sqlflow);
						List flows=new ArrayList();
						flows= getRows();
						
						m2.put("nodes", nodeobj);
						m2.put("flows", flows);
						nodelist.add(m2);					}
				}
				m.put("rule", obj);
				m.put("node",nodelist);
				
				
				result.add(m);
				
			}
		}
		curPage="1";
		maxRowCount=20;
		return getReturn();
	}
	/**
	 * 删除某一审批流
	 * ruleid
	 * @return
	 */
	public String delSPL(){
		String ruleid=request.getParameter("ruleid");
		if(ruleid==null){
			return getReturnString("false");
		}
		String[] sql= new String[3];
		sql[0]="delete from ShenpiRule where id ="+ruleid;
		sql[1]="delete from shenpinode where ruleid="+ruleid;
		sql[2]="delete from shenpiflow where ruleid="+ruleid;
		db=new DataBase();
		try {
			db.connectDb();
			db.updateBatch(sql);;
			return getReturnString("true");
		} catch (Exception e) {
			e.printStackTrace();
			return getReturnString("false");
		}finally{
			try {
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	public String update() {
		return getReturnString(super.update());
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*
 * 添加审批记录接口	
 * tablename,recordid,searchid
 */
 public void addContent(String tablename,String recordid,String spaccountid,String fhaccountid,String tpaccountid,String shenpiresult,String shenpicontent){
	 DataBase db = new DataBase();
	 try {
		db.connectDb();
		
	} catch (DbException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		
	}
 }
	
/*功能审批结束方法
 * tablename,recordid,searchid
 * 
 * 
 */
	public void finishFlow(String tablename,String recordid,String searchid){
		 DataBase db = new DataBase();
		 try {
			db.connectDb();
			String[] sqlstr= new String[2];
			sqlstr[0]="update "+tablename+" set spfinished='1' where id="+recordid;
			sqlstr[1]="update shenpiywsearch set spfinished='1' where id="+searchid;
			db.updateBatch(sqlstr);
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.close();
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	//获得需要审批的
	public   String getCanSp(){
		String isenable = request.getParameter("key");
		String searchsql="select distinct id from "+table +" where id not in (select recordid from ShenpiYWRecord where " +
				"typeid=(select id from shenpitype where tablename='"+table+"'))"; 
		excuteSQLPageQuery(searchsql);
		result = getRows();
		if(result!=null&&result.size()>0){
			String[] insql=new String[result.size()*2];
			for (int i = 0; i < result.size(); i++) {
				DynaBean obj=(DynaBean) result.get(i);
				insql[i*2]="insert into SHENPIywRECORD(typeid,ruleid,accountid,recordid)  " +
						"select distinct typeId,ruleId,accountId,"+String.valueOf(obj.get("id"))+" from dbo.ShenpiFlow where ruleid=(  " +
						"	select id from dbo.ShenpiRule where isenable=1 and typeId=(  " +
						"	select id from dbo.ShenpiType where tableName='"+table+"'  " +
						"	)) and spRank=1";
				insql[i*2+1]="insert into SHENPIywSEARCH (TYPEID,ruleid,ACCOUNTID,RECORDID,SPRANK) " +
						"select distinct typeId,ruleId,accountId,"+String.valueOf(obj.get("id"))+",1 from dbo.ShenpiFlow where ruleid=(  " +
						"	select id from dbo.ShenpiRule where isenable=1 and typeId=(  " +
						"	select id from dbo.ShenpiType where tableName='"+table+"'  " +
						"	)) and spRank=1";
			}
			try {
				db=new DataBase();
				db.updateBatch(insql);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					db.close();
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}
		String sql_____="";
		
		//获取视图信息
		if(table.equalsIgnoreCase("ordernew")){
			//订单审批视图
			table="orderSearchView";
		}else if(table.equalsIgnoreCase("orderprice")){
			//订单退补差价审批视图
			table="orderPriceView";
		}else if(table.equalsIgnoreCase("purchaserequisition")){
			//采购申请审批视图
			table="PurchaseRequisitionView";
		}else if(table.equalsIgnoreCase("purchasespreadtuibu")){
			//采购退补差价审批视图
			table="PurchaseTuibuView";
		}else if(table.equalsIgnoreCase("paymentapply")){
			//付款申请审批视图
			table="PaymentApplys";
		}else if(table.equalsIgnoreCase("salesreturn")){
			//销售退货审批视图
			table="SalesReturnView";
		}else if(table.equalsIgnoreCase("purchasereturn")){
			//采购销售退货审批视图
			table="PurchaseReturnView";
		}else if(table.equalsIgnoreCase("stocktransfermain")){
			//库间调拨单审核视图
			table="StockTransferMainView";
		}
		
		if(isenable!=null&&!"".equals(isenable)){
			sql_____="select b.*,a.sprank,a.typeid,a.ruleid,a.recordid,c.name  spaccountname from dbo.ShenpiYWSearch a,"+table+
					" b,sysaccount c where a.recordid=b.id and a.accountid=c.id and b." +isenable+"=1"+
					" and b.spstatus not in(9,-1)";//审批结束的
		}else{
			sql_____="select b.*,a.sprank,a.typeid,a.ruleid,a.recordid,c.name  spaccountname from dbo.ShenpiYWSearch a,"+table+
					" b,sysaccount c where a.recordid=b.id and a.accountid=c.id "+
					" and b.spstatus not in(9,-1)";//审批结束的
		}
		sql_____+=super.getSearchStr(super.map);
		excuteSQLPageQuery(sql_____);
		result = getRows();
		return getReturn();
	}
	
	//获得选中条目审批记录
	public String getAnswers(){
		String table=request.getParameter("table");
		String recordid=request.getParameter("id");
		result=getSPcontent(table,recordid);
		/*
		String sql="select top 100 percent * from(" +
				"	select  a.*,sa.name accountname from dbo.ShenpiYWAnswer a,sysaccount sa	"+
				"	where recordid="+recordid+ 
				"	and typeid=(select id from dbo.ShenpiType where tableName='"+table+"')"+
				"	and a.accountid=sa.id)	a	order by SPTIME ";
		excuteSQLPageQuery(sql);
		result = getRows();*/
		return getReturn();
	}
	/**
	 * 执行审批过程
	 * @return
	 * tpaccountid特批人
	 */
	public String doSp(){
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		

		String table=request.getParameter("table");
		String recordid=String.valueOf(data.get("recordid"));
		String spaccountid=accountBean.getId();
		String spname=accountBean.getName();
		String tpaccountid=String.valueOf(data.get("tpaccountid"));
		String tpname=String.valueOf(data.get("tpname"));
		String shenpiresult=String.valueOf(data.get("shenpiresult"));
		String shenpicontent=String.valueOf(data.get("shenpicontent"));
		String typeid=String.valueOf(data.get("typeid"));
		String result="false";
		try{
			result=addShenPi(table, recordid, spaccountid, spname, tpaccountid, tpname, shenpiresult, shenpicontent,typeid);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return getReturnString(result);
		
		/*String[] sql=new String[3];
		if(answerstate.equals("1")){//批复状态审批通过
			//1、spsearch表
			String s="(select accountid from shenpiflow where sprank=" +
					"(select SPRANK from SHENPIYWSEARCH where typeid="+typeid+
					"	and RULEID="+ruleid+" and recordid="+recordid+")+1 and  typeid="+typeid+"	and RULEID="+ruleid+")";
			
			 sql[0]="update SHENPIYWSEARCH set SPRANK=SPRANK+1,ACCOUNTID="+s+
						 " where typeid="+typeid+
					"	and RULEID="+ruleid+" and recordid="+recordid+"";
			 //2、spanswer表
			 
			  如果accountid为空,则表示已经审批结束,则将订单等表中的spstatus改为0,代表审批通过
			  
			 sql[2]="update "+table +" 	set spstatus=9 where id="+recordid+" and id in(select recordid from SHENPIYWSEARCH where accountid is null and recordid="+recordid+")";
		}else if(answerstate.equals("-1")){//审批未通过
			//1、spsearch表
			sql[0]="update SHENPIYWSEARCH set SPRANK=0,ACCOUNTID=NULL where typeid="+typeid+
				"	and RULEID="+ruleid+" and recordid="+recordid+"";
			sql[2]="update "+table +"   set spstatus=-1 where id="+recordid;
		}else if(answerstate.equals("2")){//转特批
			String tpaccountid=String.valueOf(data.get("tpaccountid"));
			 sql[0]="update SHENPIYWSEARCH set ACCOUNTID="+tpaccountid+
						 " where typeid="+typeid+
					"	and RULEID="+ruleid+" and recordid="+recordid+"";
			 sql[2]="update "+table +" 	set spstatus=9 where id="+recordid+" and id in(select recordid from SHENPIYWSEARCH where accountid is null and recordid="+recordid+")";
		}
		sql[1]="insert into SHENPIYWANSWER (accountid,typeid,ANSWERSTATE,SPMATTER,SPTIME,RULEID,RECORDID)values(" +
		 		((AccountBean)session.getAttribute("accountBean")).getId()+","+typeid+","+answerstate+",'"+spmatter+"','"+DateFormat.getNowTime()+"'," +
		 				ruleid+","+recordid+")";
		db=new DataBase();
		try {
			db.updateBatch(sql);
			 //审批成功后 执行操作
			if("salesreturn".equals(table)){//销售退货（入库）
				Map addForm = null;
				String addForms = request.getParameter("addForm");
				ObjectMapper mapper = new ObjectMapper();
				if (addForms != null){
					addForms = new String(addForms.getBytes("iso-8859-1"),"utf-8");
					addForm = mapper.readValue(addForms, Map.class);
				}
				String flag="false" ;
				String storeid = String.valueOf(addForm.get("storeid"));//退货仓库
				List<Map<String,String>> salesList=(List<Map<String,String>>)(addForm.get("returngoodsList"));
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				String now=df.format(new Date());
				this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
				try{
					List sqllist=new ArrayList();
					//循环字表信息
					for (int i = 0; i < salesList.size(); i++) {
						//取出字表信息字段
						String childid= String.valueOf(salesList.get(i).get("id"));//字表id 
						String proid=String.valueOf(salesList.get(i).get("proid"));//产品id
						String supplierid= String.valueOf(salesList.get(i).get("supplierid"));//供应商
						String productcode=String.valueOf(salesList.get(i).get("productcode"));//产品编码
						String specification=String.valueOf(salesList.get(i).get("specification"));//规格
						String productMeasurename=String.valueOf(salesList.get(i).get("productmeasurename"));//单位
						String batch=String.valueOf(salesList.get(i).get("batch"));//批次
						String price=String.valueOf(salesList.get(i).get("price"));
						String producedate=String.valueOf(salesList.get(i).get("procretime"));
						String validdate=String.valueOf(salesList.get(i).get("validdate"));
						String quantity=String.valueOf(salesList.get(i).get("returncount"));
						String searchsql="select * from stock where warehouseid='"+storeid+"' and supplierid='"+supplierid+"' and productid='"+proid+"' and batch='"+batch+"' and producedate='"+producedate+"' and validdate='"+validdate+"' and price='"+price+"'";
						db=new DataBase();
						db.connectDb();//打开连接
						ResultSet rs = db.queryAll(searchsql.toString());
						if((rs!=null)&&(rs.next())){//如果stock有数据,则更新对应的数据,并在iostock中插入一条新数据
							String sqlio1="insert into iostock(outinwarehousedate,outinwarehousemarked,stockid," +
									"outinwarehousetype,businesschildid,creatorid,createtime,modifierid,modifytime,quantity) " +
									" values('"+now+"','1','"+rs.getString("id")+"','16','"+childid+"','"+accountBean.getId()+"','"+now+"','"+accountBean.getId()+"','"+now+"','"+quantity+"')";
							sqllist.add(sqlio1);
							System.out.println("sqlio1"+sqlio1);
							String qu = rs.getString("quantity");//获取库存中已有数量
							String sums = (Integer.parseInt(qu)+Integer.parseInt(quantity))+"";
							String sqlsto1=" update stock set quantity='"+sums+"' where id="+rs.getString("id");
							System.out.println("sqlsto1"+sqlsto1);
							sqllist.add(sqlsto1);
						}else{//如果stock没有查询到数据,则在stock表中插入一条新数据,并在iostock中插入一条新数据
							String 	sqlsto2=" insert into stock(warehouseid,supplierid,productid,batch,price,producedate,validdate,quantity,creatorid,createtime,modifierid,modifytime) values('"+storeid+"','"+supplierid+"','"+proid+"','"+batch+"','"+price+"','"+producedate+"','"+validdate+"','"+quantity+"'," +
									" '"+accountBean.getId()+"','"+now+"','"+accountBean.getId()+"','"+now+"')";	
							sqllist.add(sqlsto2);
							System.out.println("elsqlsto2"+sqlsto2);
							String sqlio2 = "insert into iostock(outinwarehousedate,outinwarehousemarked,stockid," +
									"outinwarehousetype,businesschildid,creatorid,createtime,modifierid,modifytime,quantity) " +
									" values('"+now+"','1',ident_current('stock'),'16','"+childid+"','"+accountBean.getId()+"','"+now+"','"+accountBean.getId()+"','"+now+"','"+quantity+"')";
							sqllist.add(sqlio2);
							System.out.println("elsqlio2"+sqlio2);
						}
					}
					 db.updateBatch((String[])sqllist.toArray(new String[sqllist.size()]));
					 flag = "true";
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					try {
						db.close();
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				return super.getReturnString(flag);
				
			}else if("purchasereturn".equals(table)){//采购退货（出库）
				Map addForm = null;
				String addForms = request.getParameter("addForm");
				System.out.println("!!!!!"+addForms);
				ObjectMapper mapper = new ObjectMapper();
				if (addForms != null){
					addForms = new String(addForms.getBytes("iso-8859-1"),"utf-8");
					addForm = mapper.readValue(addForms, Map.class);
				}
				String flag="false" ;
				String warehouseid = String.valueOf(addForm.get("warehouseid"));//退货仓库
				String supplierid = String.valueOf(addForm.get("supplierid"));//供应商
				List<Map<String,String>> salesList=(List<Map<String,String>>)(addForm.get("returngoodsList"));
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				String now=df.format(new Date());
				this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
				try {
					List sqllist=new ArrayList();
					for (int i = 0; i < salesList.size(); i++) {
						//取出字表信息字段
						String childid= String.valueOf(salesList.get(i).get("id"));//字表id 
						String productid=String.valueOf(salesList.get(i).get("productid"));//产品id
						String batch=String.valueOf(salesList.get(i).get("batch"));//批次
						String price=String.valueOf(salesList.get(i).get("price"));
						String producedate=String.valueOf(salesList.get(i).get("producedate"));
						String validdate=String.valueOf(salesList.get(i).get("validdate"));
						String quantity=String.valueOf(salesList.get(i).get("quantity"));
						String searchsql="select * from stock where warehouseid='"+warehouseid+"' and supplierid='"+supplierid+"' and productid='"+productid+"' and batch='"+batch+"' and producedate='"+producedate+"' and validdate='"+validdate+"' and price='"+price+"'";
						db=new DataBase();
						db.connectDb();//打开连接
						ResultSet rs = db.queryAll(searchsql.toString());
						if((rs!=null)&&(rs.next())){//如果stock有数据,则更新对应的数据,并在iostock中插入一条新数据
							System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
							String sqlio1="insert into iostock(outinwarehousedate,outinwarehousemarked,stockid," +
									"outinwarehousetype,businesschildid,creatorid,createtime,modifierid,modifytime,quantity) " +
									" values('"+now+"','2','"+rs.getString("id")+"','26','"+childid+"','"+accountBean.getId()+"','"+now+"','"+accountBean.getId()+"','"+now+"','"+quantity+"')";
							sqllist.add(sqlio1);
							String qu = rs.getString("quantity");//获取库存中已有数量
							String sums = (Double.parseDouble(qu)-Double.parseDouble(quantity))+"";
							String sqlsto1=" update stock set quantity='"+sums+"' where id="+rs.getString("id");
							sqllist.add(sqlsto1);
						}
					}
					db.updateBatch((String[])sqllist.toArray(new String[sqllist.size()]));
					 flag = "true";
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					try {
						db.close();
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				return super.getReturnString(flag);
			}else if("stocktransfermain".equals(table)){
				Map addForm = null;
				String addForms = request.getParameter("addForm");
				System.out.println("!!!!!"+addForms);
				ObjectMapper mapper = new ObjectMapper();
				if (addForms != null){
					addForms = new String(addForms.getBytes("iso-8859-1"),"utf-8");
					addForm = mapper.readValue(addForms, Map.class);
				}
				String flag="false" ;
				String mainid = String.valueOf(addForm.get("id"));//主表id
				String outwarehouseid = String.valueOf(addForm.get("outwarehouseid"));//调出仓库
				String inwarehouseid = String.valueOf(addForm.get("inwarehouseid"));//调入仓库
				String allocationdate = String.valueOf(addForm.get("allocationdate"));//调拨日期
				String ywyid = String.valueOf(addForm.get("ywyid"));//业务员id
				String audittype =  String.valueOf(addForm.get("spstatus"));//审核状态
				List<Map<String,String>> stockTransferList=(List<Map<String,String>>)(addForm.get("stockTransferList"));
				String memo = String.valueOf(addForm.get("memo"));//备注
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				String now=df.format(new Date());
				this.session = request.getSession();
				AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
				try{
					List sqllist=new ArrayList();
//					//在出入库主表中插入数据
//					String sqladdmain="insert into stockTransferMain" +
//							"(outwarehouseid,inwarehouseid,allocationdate,ywyid,audittype,memo,creatorid,createtime,modifierid,modifytime)" +
//							"values ('"+outwarehouseid+"','"+inwarehouseid+"','"+allocationdate+"','"+ywyid+"',0,'"+memo+"','"+accountBean.getId()+"','"+now+"','"+accountBean.getId()+"','"+now+"')";
//					sqllist.add(sqladdmain);
					
					//更新调拨主表
					String sqladdmain = "update stockTransferMain set outwarehouseid='"+outwarehouseid+
							"',inwarehouseid='"+inwarehouseid+"',allocationdate='"+allocationdate+"',ywyid='"+ywyid+
							"',spstatus='"+audittype+"',modifierid='"+accountBean.getId()+"',modifytime='"+now+"' where id = "+mainid;
					sqllist.add(sqladdmain);
					for (int i = 0; i < stockTransferList.size(); i++) {
						//取出字表信息字段
						String childid= String.valueOf(stockTransferList.get(i).get("id"));//字表id
						String stockid=String.valueOf(stockTransferList.get(i).get("stockid"));//库存表id
						String allocationno=String.valueOf(stockTransferList.get(i).get("allocationno"));//调拨数量
						String supplierid=String.valueOf(stockTransferList.get(i).get("supplierid"));//供应商0
						String productid=String.valueOf(stockTransferList.get(i).get("productid"));//产品id
						String batch=String.valueOf(stockTransferList.get(i).get("batch"));//库存
						String price=String.valueOf(stockTransferList.get(i).get("price"));//价格
						String producedate=String.valueOf(stockTransferList.get(i).get("producedate"));//生产日期
						String validdate=String.valueOf(stockTransferList.get(i).get("validdate"));//有效期至
						//在字表中循环插入字表信息
//						String sqladdchild="insert into stockTransferChild(stocktransfermainid,stockid,allocationno)  " +
//								"values (ident_current('stockTransferMain'),'"+stockid+"','"+allocationno+"')";
						
						//更新字表信息
						String sqladdchild = "update stockTransferChild set stockid='"+stockid+"',allocationno='"+allocationno+"' where id = "+childid;
						sqllist.add(sqladdchild);
						String stocksql = "select * from stock where id="+stockid;
						db=new DataBase();
						db.connectDb();//打开连接
						ResultSet rs = db.queryAll(stocksql.toString());
						String qu = "";
						while((rs!=null)&&(rs.next())){
							//出库sql
							String sqlio1="insert into iostock(outinwarehousedate,outinwarehousemarked,stockid," +
									"outinwarehousetype,businesschildid,memo,creatorid,createtime,modifierid,modifytime,quantity) " +
									" values('"+now+"','2','"+rs.getString("id")+"','24','"+childid+"','"+memo+"','"+accountBean.getId()+"','"+now+"','"+accountBean.getId()+"','"+now+"','"+allocationno+"')";
							sqllist.add(sqlio1);
							qu = rs.getString("quantity");
							 String outNo = (Double.parseDouble(qu)-Double.parseDouble(allocationno))+"";//出库后的数量
							 String upsqteoutsql = "update stock set quantity= "+outNo+" where id="+stockid;//修改出库仓库库存数量
							 sqllist.add(upsqteoutsql);
						}
						String searchsql="select * from stock where warehouseid='"+inwarehouseid+"' and supplierid='"+supplierid+"' and productid='"+productid+"' and batch='"+batch+"' and producedate='"+producedate+"' and validdate='"+validdate+"' and price='"+price+"'";
						ResultSet rss = db.queryAll(searchsql.toString());
						if((rss!=null)&&(rss.next())){//插入入库记录
							String sqlio2="insert into iostock(outinwarehousedate,outinwarehousemarked,stockid," +
									"outinwarehousetype,businesschildid,memo,creatorid,createtime,modifierid,modifytime,quantity) " +
									" values('"+now+"','1','"+rs.getString("id")+"','14','"+childid+"','"+memo+"','"+accountBean.getId()+"','"+now+"','"+accountBean.getId()+"','"+now+"','"+allocationno+"')";
							System.out.println("sqlio2+++++++++++"+sqlio2);
							sqllist.add(sqlio2);
							String qus = rs.getString("quantity");//获取库存中已有数量
							String sums = (Double.parseDouble(qus)+Double.parseDouble(allocationno))+"";
							String sqlsto2=" update stock set quantity='"+sums+"' where id="+rs.getString("id");
							sqllist.add(sqlsto2);
						}else{
							String 	sqlsto3=" insert into stock(warehouseid,supplierid,productid,batch,price,producedate,validdate,quantity,memo,creatorid,createtime,modifierid,modifytime) values('"+inwarehouseid+"','"+supplierid+"','"+productid+"','"+batch+"','"+price+"','"+producedate+"','"+validdate+"','"+allocationno+"','"+memo+"'," +
									" '"+accountBean.getId()+"','"+now+"','"+accountBean.getId()+"','"+now+"')";	
							sqllist.add(sqlsto3);
							String sqlio3 = "insert into iostock(outinwarehousedate,outinwarehousemarked,stockid," +
									"outinwarehousetype,businesschildid,memo,creatorid,createtime,modifierid,modifytime,quantity) " +
									" values('"+now+"','1',ident_current('stock'),'14','"+childid+"','"+memo+"','"+accountBean.getId()+"','"+now+"','"+accountBean.getId()+"','"+now+"','"+allocationno+"')";
							sqllist.add(sqlio3);
						}
					}
					 db.updateBatch((String[])sqllist.toArray(new String[sqllist.size()]));
					 flag = "true";
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					try {
						db.close();
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				return super.getReturnString(flag);
			}
			if(answerstate.equals("1")){
				
			}
			return getReturnString("true");			
		} catch (Exception e) {
			e.printStackTrace();
			return getReturnString("false");
		}finally{
			try {
				db.close();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}*/
	
	}
	
	/*====================================================================
	====================================================================
	====================================================================*/
	//获得审批类型
	public String getType(){
		String sql="select top 100 percent * from SHENPITYPE where isenable!=0 ";
		return getBeansBySql(sql);
	}
	
	/**
	 * 启停用
	 */
	public String updown(){
		String id=String.valueOf(data.get("id"));
		String typeid=String.valueOf(data.get("typeid"));
		int isenable=(Integer) data.get("value");
		String[] bats = new String[isenable+1];
		if(isenable==1){
			//如果是启用,先禁用全部,在启用
			String sql1="update shenpirule set isenable=0 where typeid="+typeid;
			String sql2="update shenpirule set isenable=1 where id="+id;
			bats=new String[2];
			bats[0]=sql1;bats[1]=sql2;
		}else if(isenable==0){
			//如果是禁用,只禁用
			String sql="update shenpirule set isenable= 0 where id="+id;
			bats=new String[1];bats[0]=sql;
		}
		db=new DataBase();
		try {
			db.updateBatch(bats);
			return getReturnString("true");
		} catch (DbException e) {
			e.printStackTrace();
			return getReturnString("false");
		}
	}
	
	//获得每条的审批状态
	public String getSpStatus(){
	 
		return "";
	}
	/**
	 * 添加自定义审批
	 * table
	 * spaccount
	 * @return
	 */
	public String addZDYSP(){/*
		
		//String[] sqls=new String[4];
		String tablename=request.getParameter("table");//传值
		
		try {
			String  liststr = new String(request.getParameter("spaccountList").getBytes("iso-8859-1"),"utf-8");
		ObjectMapper mapper = new ObjectMapper();
		List  spaccounts=mapper.readValue(liststr, ArrayList.class);
		Map map= (Map) spaccounts.get(0);
		String spaccountid=(String) map.get("spaccount");
		String accountid= ((AccountBean)session.getAttribute("accountBean")).getId();
		String nowtime=DateFormat.getNowTime();
		List<String> lists=new ArrayList<String>();
		String sqls0="insert into ShenpiRule(spFlow ,isenable,typeid,accountid,createtime )" +
				"select '"+tablename+"自定义审批',0,id, "+accountid+",'"+nowtime+"' from shenpitype where tablename='"+tablename+"'";
		String sqls1="insert into shenpiflow(accountid,ruleid,typeid,sprank)select top 1 "+spaccountid+"," +
				"id,typeid,1 from ShenpiRule  order by id desc";
		String sqls2="insert into SHENPIywRECORD (typeid,accountid,recordid,ruleid )" +
				"select top 1 a.typeid,"+spaccountid+",b.id,a.ruleid from shenpiflow a,"+table+" b  order by a.id desc,b.id desc";
		String sqls3="insert into SHENPIywSEARCH (TYPEID,ruleid,ACCOUNTID,RECORDID,SPRANK) " +
				"	select top 1 a.typeid,a.ruleid,"+spaccountid+",b.id,1 from shenpiflow a,"+table+" b  order by a.id desc,b.id desc";
		lists.add(sqls0);lists.add(sqls1);lists.add(sqls2);lists.add(sqls3);
		for (int i = 1; i < spaccounts.size(); i++) {
			Map map1= (Map) spaccounts.get(i);
			String spaccountidi=(String) map1.get("spaccount");
			String sql="insert into shenpiflow(accountid,ruleid,typeid,sprank)select top 1 "+spaccountidi+"," +
					"id,typeid,"+(i+1)+" from ShenpiRule  order by id desc";
			lists.add(sql);
		}
		db=new DataBase();
			db.connectDb();
			 db.updateBatch((String[])lists.toArray(new String[lists.size()]));
			return getReturnString("true");
		} catch (Exception e) {
			e.printStackTrace();
			return getReturnString("false");
		}finally{
			try {
				db.close();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	*/

		
		//String[] sqls=new String[4];
		String tablename=request.getParameter("table");//传值
		
		try {
		String  liststr = new String(request.getParameter("spaccountList").getBytes("iso-8859-1"),"utf-8");
		ObjectMapper mapper = new ObjectMapper();
		List  spaccounts=mapper.readValue(liststr, ArrayList.class);
		Map map= (Map) spaccounts.get(0);
		String spaccountid=(String) map.get("spaccount");
		String accountid= ((AccountBean)session.getAttribute("accountBean")).getId();
		String nowtime=DateFormat.getNowTime();
		List<String> lists=new ArrayList<String>();
		String sqls0="insert into ShenpiRule(spFlow ,isenable,typeid,accountid,createtime )" +
				"select 'zdyspspspspspspsp',0,id, "+accountid+",'"+nowtime+"' from shenpitype where tablename='"+tablename+"'";
		String sqls1="insert into shenpiflow(accountid,ruleid,typeid,sprank)select top 1 "+spaccountid+"," +
				"id,typeid,1 from ShenpiRule  order by id desc";
		String sqls2="insert into SHENPIywRECORD (typeid,accountid,recordid,ruleid )" +
				"select top 1 a.typeid,"+spaccountid+",b.id,a.ruleid from shenpiflow a,"+table+" b  order by a.id desc,b.id desc";
		String sqls3="insert into SHENPIywSEARCH (TYPEID,ruleid,ACCOUNTID,RECORDID,SPRANK) " +
				"	select top 1 a.typeid,a.ruleid,"+spaccountid+",b.id,1 from shenpiflow a,"+table+" b  order by a.id desc,b.id desc";
		lists.add(sqls0);lists.add(sqls1);lists.add(sqls2);lists.add(sqls3);
		for (int i = 1; i < spaccounts.size(); i++) {
			Map map1= (Map) spaccounts.get(i);
			String spaccountidi=(String) map1.get("spaccount");
			String sql="insert into shenpiflow(accountid,ruleid,typeid,sprank)select top 1 "+spaccountidi+"," +
					"id,typeid,"+(i+1)+" from ShenpiRule  order by id desc";
			lists.add(sql);
		}
		    db=new DataBase();
			db.connectDb();
			db.updateBatch((String[])lists.toArray(new String[lists.size()]));
			return getReturnString("true");
		} catch (Exception e) {
			e.printStackTrace();
			return getReturnString("false");
		}finally{
			try {
				db.close();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	
		}
	
	public ArrayList getTableIds(String table){
		ArrayList recordid =new ArrayList();
		String sql="select id from "+table+" where spstate in(0,2) ";
		DataBase db=new DataBase();
		try {
			PreparedStatement ps = db.getPreparedStatement(sql);
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				recordid.add(rs.getString("id"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.close();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		return recordid;
	}
	
}


