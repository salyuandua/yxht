package com.cheers.export.service;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.exceltools.ExcelUtils;
import com.cheers.exceltools.JsGridReportBase;
import com.cheers.exceltools.TableData;
import com.cheers.util.DataFormat;
public class ExportService extends Dao {
	public String add(){
		return getReturnString(super.add());
		
	}
	public String update(){
		return getReturnString(super.update());
	}
	
	public String delete(){
		return getReturnString(super.delete());
	}
	//销售总表 导出 操作
	 public void expExcel() throws Exception
		{
		//下载excel至本地
		 this.session = request.getSession();
		 String accountid=((AccountBean)this.session.getAttribute("accountBean")).getId();
		 String[] ids= request.getParameterValues("ids");
		List<DynaBean> ds=	getCustomerInfoExcel(ids,accountid);
		exportExcel(response,ds, "customer");
		}
	//导出Excel 
		public boolean exportExcel(HttpServletResponse response,List<DynaBean>  list,String type){   
			try{ 
				OutputStream os = response.getOutputStream();// 取得输出流   
				response.reset();// 清空输出流   
				response.setHeader("Content-disposition", "attachment; filename="+type+".xls");// 设定输出文件头   
				response.setContentType("application/msexcel");// 定义输出类型 

				WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件   
				String tmptitle="";
				if( type.equals("customer"))tmptitle= "客户信息导出";
				else if( type.equals("client"))tmptitle= "客户信息导出";
				else if( type.equals("abc"))tmptitle= "ABC信息导出";
				else if( type.equals("contract"))tmptitle= "合同信息导出";
				else return false;
				WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称
				// 设置excel标题   
				WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,WritableFont.BOLD, false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);   
				WritableCellFormat wcfFC = new WritableCellFormat(wfont); 
				wcfFC.setBackground(Colour.AQUA); 
				wsheet.addCell(new Label(6, 0, tmptitle, wcfFC));   
				//主体
				wfont = new WritableFont(WritableFont.ARIAL, 14,WritableFont.BOLD,false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);   
				wcfFC = new WritableCellFormat(wfont);  
				// 开始生成主体内容      
				if( type.equals("customer")){
					String[] title=new String[]{"客户类型","客户名称","法人", "客户联系人", "联系电话", "电子邮箱", "客户地址", "客户级别", "开票信息", "客户来源","业务人员","备注"};
					String[] body=new String[]{"typename","name", "connector", "prinicpalname", "phone", "email", "address", "levelname", "customerKpInfor", "sourcename","accountname","memo"};
					addTable(wsheet, list, title, body);
				}else if( type.equals("client")){
					String[] title=new String[]{"客户编号","客户名称", "业务员", "地区", "填表日期", "企业法人", "客户地址", "邮编", "客户电话（座机）", "客户传真", "网址","邮箱","成立日期", "开户行", "账号", "税号", "注册资本", "员工人数", "主要产品及服务", "所在行业", "同行业中地位", "企业性质","收货人",  "收货地点"};
					String[] body=new String[]{"code","name", "account", "place", "fillingtime", "legal", "address", "zip", "tel", "fax", "weburl","email","foundtime", "bank", "bankaccount", "taxid", "registeredcapital", "staffamount", "mainproduct", "industry", "status", "annualoutputvalue","consignee_name",  "shipping_address"};
					addTable(wsheet, list, title, body);	
				}else if( type.equals("abc")){
					String[] title=new String[]{"客户档案编号","产品类属", "分区报告时间", "具体分区", "所属地区", "创建人", "现业务负责人", "客户分类", "客户来源", "ABC信息客户名称", "详细地址","联系人姓名","联系人地址", "联系方式", "信息内容及实施时间", "信息等级"};
					String[] body=new String[]{"clientname","productcategory", "reporttime", "orgname", "place", "creator", "accountname", "clienttype", "clientsource", "name", "address","contactname","contactaddress", "contacttel", "content", "infolevel"};
					addTable(wsheet, list, title, body);	
				}else if( type.equals("contract")){
					
				}
				// 主体内容生成结束           
				wbook.write(); // 写入文件   
				wbook.close();  
				os.close(); // 关闭流
				return true; 
			} 
			catch(Exception ex) 
			{ 
				ex.printStackTrace(); 
				return false; 
			} 
		}
		public WritableSheet addTable(WritableSheet wsheet,List<DynaBean>  list,String[] title,String[] body){
			try {
				for (int i = 0; i < title.length; i++) {
					wsheet.addCell(new Label(i, 2,title[i]));
				}
				for (int k = 0; k < list.size(); k++) {
					DynaBean bean=list.get(k);
					for (int j = 0; j < body.length; j++) {
						String ipt=String.valueOf(bean.get(body[j])) ;
						wsheet.addCell(new Label(j, k+3, ipt==null?"":(ipt.equalsIgnoreCase("null")?"":ipt)  ));   
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}  
			return wsheet;
		}
	 
		public List<DynaBean> getCustomerInfoExcel(String[] ids,String accountid){
			List<DynaBean> ds=new ArrayList<DynaBean>();
			String sql="SELECT dbo.Customer.ID, dbo.Customer.accountId, dbo.Customer.province, dbo.Customer.city, dbo.Customer.country, dbo.Customer.customerTypeId, dbo.Customer.name,dbo.Customer.receivTypeId, " +
					"dbo.Customer.connector, dbo.Customer.address, dbo.BASE_DATA.name AS typename, BASE_DATA_1.name AS levelname,BASE_DATA_2.name AS sourcename, dbo.sysACCOUNT.name AS accountname, " +
					"dbo.Customer_Principal.name AS prinicpalname,BASE_DATA_3.name AS industryname, dbo.Customer.sourceId, dbo.Customer.customerKpInfor, dbo.Customer.companyProfile, " +
					"dbo.Customer.memo,dbo.Customer.levelId, dbo.Customer.balance, dbo.Customer.IndustryId, dbo.Customer_Principal.email, dbo.Customer_Principal.phone," +
					"dbo.Customer.enable,dbo.Customer_Principal.tel " +
					"FROM dbo.Customer " +
					" LEFT  JOIN  dbo.sysACCOUNT ON dbo.Customer.accountId = dbo.sysACCOUNT.id" +
					" LEFT  JOIN dbo.Customer_Principal ON dbo.Customer.ID = dbo.Customer_Principal.clientId" +
					" LEFT  JOIN dbo.BASE_DATA ON dbo.Customer.customerTypeId = dbo.BASE_DATA.id " +
					" LEFT  JOIN dbo.BASE_DATA AS BASE_DATA_1 ON dbo.Customer.levelId = BASE_DATA_1.id" +
					" LEFT  JOIN dbo.BASE_DATA AS BASE_DATA_2 ON dbo.Customer.sourceId = BASE_DATA_2.id" +
					" LEFT  JOIN dbo.BASE_DATA AS BASE_DATA_3 ON dbo.Customer.IndustryId = BASE_DATA_3.id";
			if (ids!=null&&ids.length>0) {
				sql+=" and dbo.Customer.ID in(0	";
			
			for (int i = 0; i < ids.length; i++) {
				 sql+=","+ids[i];
			}
			sql+=")";
			}
			DataBase db=new DataBase();
			try {
				db.connectDb();
				ResultSet rs=	db.queryAll(sql);
				ds=new RowSetDynaClass(rs).getRows();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ds;
		}
	
		 /**
		 * 查看下级和自己的客户资料信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearch(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from CusInfo t where 1=1");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.accountid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		/**
		 * 查看下级和自己的客户跟踪上报信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchvisit(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from CusVisitInfo t where 1=1");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
			sql.append(" or t.accountid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			if (!DataFormat.booleanCheckNull(sort))
				sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
			else
				sql .append( " order by id");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}
		/**
		 * 查看下级和自己的客户联系人信息
		 *  2013年12月16日16:32:50
		 * @return
		 */
		public String getBeansSearchprincipal(){ 
			this.session = request.getSession();
			AccountBean accountBean = (AccountBean)this.session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from ControllerInfo t where 1=1");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
			sql.append(" or t.accountid ='"+selfId+"')");
			sql.append(super.getSearchStr(super.map));
			if (!DataFormat.booleanCheckNull(sort))
				sql.append( " order by " + sort + " " + DataFormat.objectCheckNull(order));
			else
				sql .append( " order by id");
			System.out.println("sql:"+sql);
			excuteSQLPageQuery(sql.toString());
			result = getRows();
			return getReturn();
		}

}
