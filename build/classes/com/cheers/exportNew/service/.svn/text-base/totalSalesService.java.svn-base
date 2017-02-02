package com.cheers.exportNew.service;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cheers.account.bean.AccountBean;

import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.exceltools.ExcelUtils;
import com.cheers.exceltools.JsGridReportBase;
import com.cheers.exceltools.TableColumn;
import com.cheers.exceltools.TableData;
import com.cheers.exceltools.TableHeaderMetaData;
import com.cheers.util.DataFormat;
import com.cheers.util.DateFormat;
/**
 * 销售总表报表服务类
 * 2013年11月6日14:03:33
 * 
 *
 */
public class totalSalesService extends Dao{
	public String add(){
		return getReturnString(super.add());
	}
	public String update(){

		return getReturnString(super.update());
	}
	
	public String delete(){
		return getReturnString(super.delete());
	}
		 
             /**
              * 大区销售统计报表  导出 操作---------
              * jack 
              *  
              */
             public void dqsalesreportviewExcel() throws Exception{
                 this.response.setContentType("application/msexcel;charset=GBK");
                 AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
                 String canAccountIds = accountBean.get_canAccountIds();//此账号所能看到的
                 SqlUtil sqlUtil = new SqlUtil();
                 
                 String yearEQ = (String) super.map.get("yearEQ");//年
                 String monthEQ = (String) super.map.get("monthEQ");//月
                 StringBuffer sql =new StringBuffer("select top 100 percent h.dqid,h.accountid,h.year,h.month,cast(round(h.orderMoneys/10000,4) as numeric(20,4)) ordermoneys,a.name dqjl,t.name dqname from");
                 sql.append(" (select o.dqId,max(o.accountId) accountId,MAX(convert(varchar,datepart(year,o.orderdate))) YEAR,MAX(convert(varchar,datepart(MONTH,o.orderdate))) month,sum(o.orderMoney) orderMoneys");
                 sql.append(" from Orders o ");
                 sql.append(" where 1=1 ");
                 if(yearEQ!=null||yearEQ!=""){
                 	sql.append(" and convert(varchar,datepart(year,o.orderdate))="+yearEQ);
//                 }else{
//                 	sql.append(" and convert(varchar,datepart(year,o.orderdate))='2014' ");//加个默认
                  }
                 if(monthEQ!=null||monthEQ!=""){
                 	
                 	sql.append(" and convert(varchar,datepart(MONTH,orderdate))="+monthEQ);
//                 }else{
//                 	sql.append(" and convert(varchar,datepart(MONTH,orderdate))='1' ");
                  }
                 sql.append(" group by o.dqId ");
                 sql.append(" ) h  ");
                 sql.append(" left join SYSTEM_TREE t on t.id=h.dqId left JOIN sysACCOUNT a on a.id=t.person  ");
                 sql.append(" where 1=1 ");
//         		 sql.append(" and "+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
//         		 sql.append(" or t.accountid ='"+selfId+"'");
         		 sql .append( " order by h.dqid");
                    List<Map<String,Object>> list = super.getListBean(sql.toString());//获取数据
                    this.session=request.getSession();
                    String name =((AccountBean)this.session.getAttribute("accountBean")).getName();

                    String title = "科发股份"+yearEQ+"年"+monthEQ+"月大区销量排名";
                    String[] hearders = new String[] {"销售大区","大区经理 ", "年度","月份","销量(万元)"};//表头数组
                    String[] fields = new String[] {"dqname","dqjl", "year", "month", "ordermoneys"};//People对象属性数组
                    TableData td = ExcelUtils.createTableData(list, getTableHeaderdqSalesRepor(hearders),fields);
                    td.compute(false);
                    JsGridReportBase report = null;
                    try {
                        report = new JsGridReportBase(request, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        report.exportToExcel(title, name, td);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
             private TableHeaderMetaData getTableHeaderdqSalesRepor(String[] hearders){
                 int spanCount = 3;//需要合并的列数。从第1列开始到指定列。如不需要合并，则将该行和237、238行去掉即可                                         
                 //创建表头对象
                 TableHeaderMetaData headMeta = new TableHeaderMetaData();
                 for(int i=0;i<hearders.length;i++){
                     TableColumn tc = new TableColumn();
                     tc.setDisplay(hearders[i]);
                     if(i==1)//按第1列进行
                         tc.setDisplaySummary(true);
                     
                     if( i==4)//求和
                         tc.setAggregateRule("sum");
                     
                     if(i<spanCount)//前3列行合并
                         tc.setGrouped(true);
                     headMeta.addColumn(tc);
                 }
                 return headMeta;
             }
             
            
             
             
             /**
              * 区域销量汇总表  导出 操作------------
              * 
              * 
              */
             public void avaisaleviewExcel() throws Exception{
            	 this.response.setContentType("application/msexcel;charset=GBK");
                 AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
                 String canAccountIds = accountBean.get_canAccountIds();//此账号所能看到的
                 SqlUtil sqlUtil = new SqlUtil();
                 
                 String yearEQ = (String) super.map.get("yearEQ");//年
                 String monthEQ = (String) super.map.get("monthEQ");//月
                 StringBuffer sql =new StringBuffer("select top 100 percent h.qyid,h.accountid,h.year,h.month,cast(round(h.orderMoneys/10000,4) as numeric(20,4)) ordermoneys,a.name qyjl,t.name qyname from");
                 sql.append(" (select o.qyid,max(o.accountId) accountId,MAX(convert(varchar,datepart(year,o.orderdate))) YEAR,MAX(convert(varchar,datepart(MONTH,o.orderdate))) month,sum(o.orderMoney) orderMoneys");
                 sql.append(" from Orders o ");
                 sql.append(" where 1=1 ");
                 if(yearEQ!=null||yearEQ!=""){
                 	sql.append(" and convert(varchar,datepart(year,o.orderdate))="+yearEQ);
//                 }else{
//                 	sql.append(" and convert(varchar,datepart(year,o.orderdate))='2014' ");//加个默认
                  }
                 if(monthEQ!=null||monthEQ!=""){
                 	
                 	sql.append(" and convert(varchar,datepart(MONTH,orderdate))="+monthEQ);
//                 }else{
//                 	sql.append(" and convert(varchar,datepart(MONTH,orderdate))='1' ");
                  }
                 sql.append(" group by o.qyid ");
                 sql.append(" ) h  ");
                 sql.append(" left join SYSTEM_TREE t on t.id=h.qyid left JOIN sysACCOUNT a on a.id=t.person  ");
                 sql.append(" where 1=1 ");
//         		 sql.append(" and "+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
//         		 sql.append(" or t.accountid ='"+selfId+"'");
         		 sql .append( " order by h.qyid");
                    List<Map<String,Object>> list = super.getListBean(sql.toString());//获取数据
                    this.session=request.getSession();
                    String name =((AccountBean)this.session.getAttribute("accountBean")).getName();

                    String title = "科发股份"+yearEQ+"年"+monthEQ+"月区域销量排名";
                    String[] hearders = new String[] {"销售区域","区域经理 ", "年度","月份","销量(万元)"};//表头数组
                    String[] fields = new String[] {"qyname","qyjl", "year", "month", "ordermoneys"};//People对象属性数组
                    TableData td = ExcelUtils.createTableData(list, getTableHeaderqySalesRepor(hearders),fields);
                    td.compute(false);
                    JsGridReportBase report = null;
                    try {
                        report = new JsGridReportBase(request, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        report.exportToExcel(title, name, td);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
             private TableHeaderMetaData getTableHeaderqySalesRepor(String[] hearders){
                 int spanCount = 3;//需要合并的列数。从第1列开始到指定列。如不需要合并，则将该行和237、238行去掉即可                                         
                 //创建表头对象
                 TableHeaderMetaData headMeta = new TableHeaderMetaData();
                 for(int i=0;i<hearders.length;i++){
                     TableColumn tc = new TableColumn();
                     tc.setDisplay(hearders[i]);
                     if(i==1)//按第1列进行汇总
                         tc.setDisplaySummary(true);
                     
                     if( i==4)//求和
                         tc.setAggregateRule("sum");
                     
                     if(i<spanCount)//前3列行合并
                         tc.setGrouped(true);
                     headMeta.addColumn(tc);
                 }
                 return headMeta;
             }
             
             
             /**
              * 月度报表  导出 操作------------
              * 
              * 
              */
             public void ydsaleviewExcel() throws Exception{
            	 this.response.setContentType("application/msexcel;charset=GBK");
                 AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
                 String canAccountIds = accountBean.get_canAccountIds();//此账号所能看到的
                 SqlUtil sqlUtil = new SqlUtil();
                 
                 String searchYear = String.valueOf(super.map.get("yearEQ"));
 				String searchMonth = String.valueOf(super.map.get("monthEQ"));
 				
 				StringBuffer sql =new StringBuffer();
 				sql.append(" select t1.id,t1.quyuname,t1.daquid,t1.daquname,t1.qyaccountname,t1.levelname,t1.dqaccountname,t1.kefuaccountname,(t1.oldorderMonry/10000)as oldorderMonry,cast(round(t1.goal/10000,4) as numeric(20,4)) as goal,(t1.noworderMonry/10000) as noworderMonry ");
 				sql.append(" ,convert(varchar(100),convert(decimal(10,2),((CASE when t1.goal = 0 then 0 else (t1.noworderMonry/10000)/(t1.goal/10000) end )*100)))+'%'  wanchenglv ");
 				sql.append(" ,convert(varchar(100),convert(decimal(10,2),(((t1.noworderMonry/10000)-(t1.oldorderMonry/10000))/(CASE when t1.oldorderMonry = 0 then 1 else (t1.oldorderMonry/10000) end )*100)))+'%' zengzhanglv ");
 				sql.append(" from ( ");
 				sql.append(" select st.id,st.name quyuname,st1.id daquid,st1.name daquname ,a.name qyaccountname,bd.name levelname,a1.name dqaccountname,a2.name kefuaccountname ");
 				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.qyid = st.id and DATENAME(YY,o.orderdate)='"+(Integer.parseInt(searchYear)-1)+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) oldorderMonry ");
 				sql.append(" ,isnull((select top 1 g.salemoney from goal g where g.ywyid = st.person and g.year='"+searchYear+"' AND g.month = '"+searchMonth+"'),0) goal ");
 				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.qyid = st.id and DATENAME(YY,o.orderdate)='"+searchYear+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) noworderMonry ");
 				sql.append(" from SYSTEM_TREE st  ");
 				sql.append(" LEFT JOIN SYSTEM_TREE st1 on SUBSTRING(st.vorder, 0, LEN(st.vorder)-3) = st1.vorder ");
 				sql.append(" LEFT JOIN sysACCOUNT a on a.id = st.person ");
 				sql.append(" LEFT JOIN sysACCOUNT a1 on a1.id = st1.person ");
 				sql.append(" LEFT JOIN BASE_DATA bd on a.levelid = bd.id ");
 				sql.append(" LEFT JOIN SYSTEM_TREE st2 on st2.name = '客服内勤' ");
 				sql.append(" LEFT JOIN sysACCOUNT_POST ap on ap.positionid = st2.id and orgid = st1.id ");
 				sql.append(" LEFT JOIN sysACCOUNT a2 on a2.id = ap.accountid ");
 				sql.append(" where  st.categorytype = 1 and st.notetype = 1  ");
 				//大区小计
// 				sql.append(" UNION ALL  ");
// 				sql.append(" select '','',st.id,st.name dqname,'','',a.name dyaccountname,a2.name kefuaccountname  ");
// 				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.dqid = st.id and DATENAME(YY,o.orderdate)='"+(Integer.parseInt(searchYear)-1)+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) oldorderMonry  ");
// 				sql.append(" ,isnull((select top 1 sum(g.salemoney) from goal g,SYSTEM_TREE st1 where g.ywyid = st1.person and g.year='"+searchYear+"' AND g.month = '"+searchMonth+"' and charindex(st.vorder,st1.vorder)=1 and st1.categorytype = 1  and st1.notetype = 1),0) goal  ");
// 				sql.append(" ,isnull((select SUM(o.ordermoney) from orders o where o.dqid = st.id and DATENAME(YY,o.orderdate)='"+searchYear+"' AND DATENAME(MM,o.orderdate) = '"+searchMonth+"' and o.spstate = '1' and o.isEnable = '1'),0) noworderMonry  ");
// 				sql.append(" from SYSTEM_TREE st   ");
// 				sql.append(" LEFT JOIN sysACCOUNT a on a.id = st.person  ");
// 				sql.append(" LEFT JOIN SYSTEM_TREE st2 on st2.name = '客服内勤'  ");
// 				sql.append(" LEFT JOIN sysACCOUNT_POST ap on ap.positionid = st2.id and orgid = st.id  ");
// 				sql.append(" LEFT JOIN sysACCOUNT a2 on a2.id = ap.accountid  ");
// 				sql.append(" where  st.categorytype = 1 and st.notetype = 4  ");
 				sql.append(" )t1 ");
         		 
                    List<Map<String,Object>> list = super.getListBean(sql.toString());//获取数据
                    this.session=request.getSession();
                    String name =((AccountBean)this.session.getAttribute("accountBean")).getName();

                    String title = "科发股份"+searchYear+"年"+searchMonth+"月销量统计报表";
                    String[] hearders = new String[] {"大区","大区经理 ", "客服内勤","销售区域","区域经理","级别","基数(万元)","任务(万元)","完成(万元)","任务完成率","增长率"};//表头数组
                    String[] fields = new String[] {"daquname","dqaccountname", "kefuaccountname", "quyuname", "qyaccountname","levelname", "oldorderMonry", "goal", "noworderMonry", "wanchenglv", "zengzhanglv"};//People对象属性数组
                    TableData td = ExcelUtils.createTableData(list, getTableHeaderYDSalesRepor(hearders),fields);
                    td.compute(false);
                    JsGridReportBase report = null;
                    try {
                        report = new JsGridReportBase(request, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        report.exportToExcel(title, name, td);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
             private TableHeaderMetaData getTableHeaderYDSalesRepor(String[] hearders){
                 int spanCount = 11;//需要合并的列数。从第1列开始到指定列。如不需要合并，则将该行和237、238行去掉即可                                         
                 //创建表头对象
                 TableHeaderMetaData headMeta = new TableHeaderMetaData();
                 for(int i=0;i<hearders.length;i++){
                     TableColumn tc = new TableColumn();
                     tc.setDisplay(hearders[i]);
                     if(i==1)//按第1列进行汇总
                         tc.setDisplaySummary(true);
                     
                     if( i==6)//求和
                         tc.setAggregateRule("sum");
                     if( i==7)//求和
                         tc.setAggregateRule("sum");
                     if( i==8)//求和
                         tc.setAggregateRule("sum");
                     
                     if(i<spanCount)//前3列行合并
                         tc.setGrouped(true);
                     headMeta.addColumn(tc);
                 }
                 return headMeta;
             }
             
             
             /**
              * 模块：订单管理
              * 功能：导出 功能
              * jack_sun:
             * @throws SQLException 
              */
             public void expExcelForOrders() throws SQLException{
            	this.response.setContentType("application/msexcel;charset=GBK");
                AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
                String canAccountIds = accountBean.get_canAccountIds();//此账号所能看到的
                SqlUtil sqlUtil = new SqlUtil();
                 
 				StringBuffer sql =new StringBuffer();
 				sql.append(" select top 100 percent  t.*  from Orderss t where 1=1 ");
 				sql.append(super.getSearchStr(super.map));
 				sql .append( " order by id desc ");
         		 
                    List<Map<String,Object>> list = super.getListBean(sql.toString());//获取数据
                    this.session=request.getSession();
                    String name =((AccountBean)this.session.getAttribute("accountBean")).getName();

                    String title = "订单管理导出表";
                    String[] hearders = new String[] {"订单号","订单日期 ","客户名称","订单金额","联系人 ","手机","电话","传真","业务员","大区 ","区域 ","收货地址","备注","审核状态"};//表头数组
                    String[] fields = new String[] {"code","orderdate", "customername", "ordermoney", "linkmanname","phone", "tel", "address", "accountname", "dqname", "qyname","shippingaddress","memo","spnodename"};//People对象属性数组
                    TableData td = ExcelUtils.createTableData(list, getTableHeaderOrderManage(hearders),fields);
                    td.compute(false);
                    JsGridReportBase report = null;
                    try {
                        report = new JsGridReportBase(request, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        report.exportToExcel(title, name, td);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
             }
             /**
              * 模块：订单管理
              * 功能：导出的汇总列 说明方法
              * 作者：jack_sun:1519891098
              */
             private TableHeaderMetaData getTableHeaderOrderManage(String[] hearders){
                 int spanCount = 3;//需要合并的列数。从第1列开始到指定列。如不需要合并，则将该行和237、238行去掉即可                                         
                 //创建表头对象
                 TableHeaderMetaData headMeta = new TableHeaderMetaData();
                 for(int i=0;i<hearders.length;i++){
                     TableColumn tc = new TableColumn();
                     tc.setDisplay(hearders[i]);
                     if(i==1)//按第1列进行汇总
                         tc.setDisplaySummary(true);
                     
                     if( i==3)//求和
                         tc.setAggregateRule("sum");
                     
                     if(i<spanCount)//前3列行合并
                         tc.setGrouped(true);
                     	 headMeta.addColumn(tc);
                 }
                 return headMeta;
             }
             
             /**
              * 报表管理    销售业绩    导出 操作---------
              * jack 2015年1月5日 10:44:39
              *  
              */
             public void yixxinsalesreportviewExcel() throws Exception{
                 this.response.setContentType("application/msexcel;charset=GBK");
                 AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
                 String canAccountIds = accountBean.get_canAccountIds();//此账号所能看到的
                 SqlUtil sqlUtil = new SqlUtil();
                 StringBuffer sql =new StringBuffer();
                 sql.append("select top 100 percent *  from salesreport_view where 1=1 ");
     	         sql.append(super.getSearchStr(super.map));
                    
                    List<Map<String,Object>> list = super.getListBean(sql.toString());//获取数据
                    this.session=request.getSession();
                    String name =((AccountBean)this.session.getAttribute("accountBean")).getName();

                    String title = "销售业绩、费用一览表";
                    String[] hearders = new String[] {"部门","业务员","合同签定日期","合同编号","项目名称","合同金额","回款金额","未收回货款","个人费用支出","借款余额"};//表头数组
                    String[] fields = new String[] {"orgname","accountname", "qddate", "contractnumber", "projectname","moneysum","returnmoney","noreturnmoney","gerenfeiyong","usemoney"};//People对象属性数组
                    TableData td = ExcelUtils.createTableData(list, getTableHeaderYXSalesRepor(hearders),fields);
                    td.compute(false);
                    JsGridReportBase report = null;
                    try {
                        report = new JsGridReportBase(request, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        report.exportToExcel(title, name, td);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
             private TableHeaderMetaData getTableHeaderYXSalesRepor(String[] hearders){
                 int spanCount = 3;//需要合并的列数。从第1列开始到指定列。如不需要合并，则将该行和237、238行去掉即可                                         
                 //创建表头对象
                 TableHeaderMetaData headMeta = new TableHeaderMetaData();
                 for(int i=0;i<hearders.length;i++){
                     TableColumn tc = new TableColumn();
                     tc.setDisplay(hearders[i]);
                     if(i==1)//按第1列进行
                         tc.setDisplaySummary(true);
                     
                     if(i==5)//求和
                         tc.setAggregateRule("sum");
                     if(i==6)//求和
                         tc.setAggregateRule("sum");
                     if(i==7)//求和
                         tc.setAggregateRule("sum");
                     if(i==8)//求和
                         tc.setAggregateRule("sum");
                     if(i==9)//求和
                         tc.setAggregateRule("sum");
                     
                     if(i<spanCount)//前3列行合并
                         tc.setGrouped(true);
                     headMeta.addColumn(tc);
                 }
                 return headMeta;
             }
}