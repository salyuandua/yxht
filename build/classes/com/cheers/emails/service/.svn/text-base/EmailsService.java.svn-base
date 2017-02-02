package com.cheers.emails.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;

public class EmailsService extends Dao{

	public String add(){
		this.session = request.getSession();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式
		String now=df.format(new Date());
		this.data.put("createTime", now);
		this.data.put("sendTime", now);
		this.data.put("creatorId", ((AccountBean)this.session.getAttribute("accountBean")).getId());
		System.out.println(this.data.get("content"));
		String retStr=super.add();
		if(retStr.equals("true")){
			Map sqlMap=new HashMap();
			sqlMap.put("createTime", now);
			int i=getMaxId((String)this.data.get("table"), sqlMap);
			return super.getReturnString(i+"");
		}else return super.getReturnString(retStr);
	}
	
	public String update(){
		
		return super.getReturnString(super.update());
		
	}
	
	public String delete(){
		
		return super.getReturnString(super.delete());

	}
	
	public String checkDuplicate(){
		
		Map map = new HashMap();
		return super.getReturnString(super.checkDuplicate(map));

	}
	
	/*
	 * 收件箱  显示 收到邮件的 方法
	 * sunjianfeng 2013-12-18 15:31:40
	 */
	public String getReceiveMails(){
		this.session = request.getSession();
		
		String sendnameLIKE = (String) super.map.get("sendnameLIKE");
		String sendtimeGE   = (String) super.map.get("sendtimeGE");
		String sendtimeLE   = (String) super.map.get("sendtimeLE");
		String stateEQ      = (String) super.map.get("stateEQ");
		
		String sql = "select top 100 percent  sa.* from ( SELECT dbo.EMAIL.title, " +
				"dbo.EMAIL.content, " + //内容
				"dbo.EMAIL.sendTime, " +//发送时间
				"dbo.EMAIL.creatorId, " +
				"dbo.EMAIL_ACCOUNT.hasReading, " +
				"dbo.sysACCOUNT.name, " +
				"dbo.sysACCOUNT.accountNo," +
				"dbo.EMAIL.createTime, " +
				"dbo.EMAIL.id as email_id, " + //email 表的 id
				"dbo.EMAIL_account.id as ea_id, " + //email_account 表的 id ,删除使用 的id
				"ACCOUNT_1.name AS createName " +
				"FROM " +
				"dbo.EMAIL_ACCOUNT " +
				"INNER JOIN dbo.EMAIL ON " +
				"dbo.EMAIL_ACCOUNT.emailId = dbo.EMAIL.id " +
				"INNER JOIN dbo.sysACCOUNT ON dbo.EMAIL_ACCOUNT.accountId = dbo.sysACCOUNT.id " +
				"INNER JOIN dbo.sysACCOUNT AS ACCOUNT_1 ON dbo.EMAIL.creatorId = ACCOUNT_1.id " +
				"where dbo.email_account.enable='1' and dbo.email_account.accountid = '"+((AccountBean)this.session.getAttribute("accountBean")).getId()+"'";
		
			if(!"".equals((super.map.get("sendnameLIKE")))&&super.map.get("sendnameLIKE")!=null){
				sql += " and ACCOUNT_1.name Like '%"+ sendnameLIKE +"%'";
			}
			if(!"".equals((super.map.get("sendtimeGE")))&&super.map.get("saletimeGE")!=null){
				sql += " and dbo.email.sendtime >= '"+sendtimeGE+ " 00:00:00'";
			}
			if(!"".equals((super.map.get("sendtimeLE")))&&super.map.get("sendtimeLE")!=null){
				sql += " and dbo.email.sendtime <= '"+sendtimeLE+" 23:59:59'";
			}
			if(!"".equals((super.map.get("stateEQ")))&&super.map.get("stateEQ")!=null){
				sql += " and dbo.EMAIL_ACCOUNT.hasReading = '"+stateEQ+"'";
			}
			
			sql += ") sa";
			
			if (!DataFormat.booleanCheckNull(sort))
	            sql += ( " order by " + sort + " " + DataFormat.objectCheckNull(order));
	        else
	            sql += ( " order by sa.sendTime desc");
		
		excuteSQLPageQuery(sql);
		result = getRows();
		return getReturn();
	}
	
	/*
     * 发件箱  显示 收到邮件的 方法
     * sunjianfeng 2013-12-19 09:04:37
     */
    public String getSendMails(){
        this.session = request.getSession();
        
        String sendnameLIKE = (String) super.map.get("sendnameLIKE");
        String sendtimeGE   = (String) super.map.get("sendtimeGE");
        String sendtimeLE   = (String) super.map.get("sendtimeLE");
        String stateEQ      = (String) super.map.get("stateEQ");
        
        String sql = "select top 100 percent  sa.* from ( SELECT dbo.EMAIL.title, " +
                "dbo.EMAIL.content, " + //内容
                "dbo.EMAIL.sendTime, " +//发送时间
                "dbo.EMAIL.creatorId, " +
                "dbo.EMAIL_ACCOUNT.hasReading, " +
                "dbo.sysACCOUNT.name, " +
                "dbo.sysACCOUNT.accountNo," +
                "dbo.EMAIL.createTime, " +
                "dbo.EMAIL.id, " +
                "ACCOUNT_1.name AS createName " +
                "FROM " +
                "dbo.EMAIL_ACCOUNT " +
                "INNER JOIN dbo.EMAIL ON " +
                "dbo.EMAIL_ACCOUNT.emailId = dbo.EMAIL.id " +
                "INNER JOIN dbo.sysACCOUNT ON dbo.EMAIL_ACCOUNT.accountId = dbo.sysACCOUNT.id " +
                "INNER JOIN dbo.sysACCOUNT AS ACCOUNT_1 ON dbo.EMAIL.creatorId = ACCOUNT_1.id " +
                "where dbo.email.enable='1' and " +
                "dbo.email.creatorid = '"+((AccountBean)this.session.getAttribute("accountBean")).getId()+"'";
        
            if(!"".equals((super.map.get("sendnameLIKE")))&&super.map.get("sendnameLIKE")!=null){
                sql += " and dbo.sysACCOUNT.name Like '%"+ sendnameLIKE +"%'";
            }
            if(!"".equals((super.map.get("sendtimeGE")))&&super.map.get("sendtimeGE")!=null){
                sql += " and dbo.email.sendtime >= '"+sendtimeGE+ " 00:00:00'";
            }
            if(!"".equals((super.map.get("sendtimeLE")))&&super.map.get("sendtimeLE")!=null){
                sql += " and dbo.email.sendtime <= '"+sendtimeLE+" 23:59:59'";
            }
            if(!"".equals((super.map.get("stateEQ")))&&super.map.get("stateEQ")!=null){
                sql += " and dbo.EMAIL_ACCOUNT.hasReading = '"+stateEQ+"'";
            }
            sql += ") sa";
            
            if (!DataFormat.booleanCheckNull(sort))
                sql += ( " order by " + sort + " " + DataFormat.objectCheckNull(order));
            else
                sql += ( " order by sa.sendTime desc");
            
            System.out.println("发件箱-------------"+sql);
        excuteSQLPageQuery(sql);
        result = getRows();
        return getReturn();
    }
	/*
	 * 假删除 更新 方法(non-Javadoc)
	 * @see com.cheers.dao.Dao#updatefieldByIds()
	 */
    public String updatefieldByIds() {
        return super.getReturnString(super.updatefieldByIds());
    }
    
	public String getCanReadNews(){
		session=request.getSession();
		
		String sql="select n.*,a.hasReading ,a.readTime,s.name typename,b.name creator from news n,newsaccount a,base_data s,account b where a.newsid=n.id and a.accountId=b.id and n.typeId=s.id and s.type = '6' and a.accountId="+((AccountBean)session.getAttribute("accountBean")).getId();
		if (this.map.containsKey("typeIdEQ")||this.map.containsKey("titleLIKE")||this.map.containsKey("createTimeGE")||this.map.containsKey("createTimeLE")){
		sql = "select * from ( "+sql+" ) na where 1=1 "+ getSearchStr(this.map);
		}
		System.out.println("11111111=="+sql);
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
	/**
	 * 新闻管理列表显示
	 * @author 王欣
	 * 2013年12月3日14:06:02
	 * @return
	 */
	public String getBeansNews(){
		this.session = request.getSession();
		AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
		String creatid = accountBean.getId();//获取当前登陆用户的id
		String canAccountIds = accountBean.get_canAccountIds();//获取用户的下级用户
		SqlUtil sqlUtil = new SqlUtil();
		StringBuffer sql = new StringBuffer("select top 100 percent  t.*  from newMessage t where 1=1 ");
		sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid")+"or t.accountid='"+creatid+"')");
		sql.append(super.getSearchStr(super.map));
		if (!DataFormat.booleanCheckNull(sort))
			sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
		else
			sql .append( " order by t.createtime desc");
		excuteSQLPageQuery(sql.toString());
		result = getRows();
		return getReturn();
	}
}
