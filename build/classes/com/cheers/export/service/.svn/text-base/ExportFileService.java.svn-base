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
public class ExportFileService extends Dao {
	//联系人
		public List<DynaBean> getControllerInfoExcel(String canAccountIds,String selfId,String[] ids){
			List<DynaBean> ds=new ArrayList<DynaBean>();
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from ControllerInfo t where 1=1");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
			sql.append(" or t.accountid ='"+selfId+"')");
			if (ids!=null&&ids.length>0) {
				sql.append(" and t.id in(0	");
				for (int i = 0; i < ids.length; i++) {
					 sql.append(","+ids[i]);
				}
				sql.append(")");
			}
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			System.out.println("联系人sql"+sql.toString());
			DataBase db=new DataBase();
			try {
				db.connectDb();
				ResultSet rs=	db.queryAll(sql.toString());
				ds=new RowSetDynaClass(rs).getRows();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ds;
		}
		//客户拜访
		public List<DynaBean> getCusVisitInfoExcel(String canAccountIds,String selfId,String[] ids){
			
			List<DynaBean> ds=new ArrayList<DynaBean>();
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from CusVisitInfo t where 1=1");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
			sql.append(" or t.accountid ='"+selfId+"')");
			if (ids!=null&&ids.length>0) {
				sql.append(" and t.id in(0	");
			for (int i = 0; i < ids.length; i++) {
				 sql.append(","+ids[i]);
			}
			sql.append(")");
			}
			sql.append(super.getSearchStr(super.map));
			sql .append( " order by id");
			System.out.println("客户拜访sal"+sql.toString());
			DataBase db=new DataBase();
			try {
				db.connectDb();
				ResultSet rs=	db.queryAll(sql.toString());
				ds=new RowSetDynaClass(rs).getRows();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ds;
		}
		//客户
		public List<DynaBean> getCusInfoExcel(String canAccountIds,String selfId,String[] ids){
			List<DynaBean> ds=new ArrayList<DynaBean>();
			SqlUtil sqlUtil = new SqlUtil();
			StringBuffer sql =new StringBuffer("select top 100 percent  t.*  from CusInfo t where 1=1");
			sql.append(" and ("+sqlUtil.getSqlStrByArrays(canAccountIds,"t.accountid"));
	        sql.append(" or t.accountid ='"+selfId+"')");
			if (ids!=null&&ids.length>0) {
				sql.append(" and t.id in(0	");
			
			for (int i = 0; i < ids.length; i++) {
				 sql.append(","+ids[i]);
			}
			sql.append(")");
			}
			sql.append(super.getSearchStr(this.map));
			sql .append( " order by id");
			System.out.println("客户sql"+sql.toString());
			DataBase db=new DataBase();
			try {
				db.connectDb();
				ResultSet rs=	db.queryAll(sql.toString());
				ds=new RowSetDynaClass(rs).getRows();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ds;
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
				if( type.equals("ControllerInfo"))tmptitle= "客户负责人职务表";
				else if( type.equals("CusVisitInfo"))tmptitle= "客户追踪表";
				else if( type.equals("CusInfo"))tmptitle= "客户资料表";
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
				if( type.equals("ControllerInfo")){
					String[] title=new String[]{"客户名称","负责人", "职务", "联系手机", "联系电话", "电子邮箱", "分管业务", "备注"};
					String[] body=new String[]{"customername","name", "postionname", "phone", "tel", "email", "fgyw", "memo"};
					addTable(wsheet, list, title, body);
				}else if( type.equals("CusVisitInfo")){
					String[] title=new String[]{"日期","客户名称", "联系人", "联系电话", "追踪情况", "下次追踪时间", "下次追踪目的", "业务人员", "备注"};
					String[] body=new String[]{"visitdate","customername", "principalname", "phone", "content", "nextvisitdate", "nextpurpose", "accountname", "memo"};
					addTable(wsheet, list, title, body);	
				}else if( type.equals("CusInfo")){
					String[] title=new String[]{"客户类型","客户名称", "法人", "客户联系人", "联系电话", "电子邮箱", "客户地址", "客户级别", "开票信息", "客户来源", "业务人员","备注"};
					String[] body=new String[]{"typename","name", "connector", "prinicpalname", "tel", "email", "address", "levelname", "customerkpinfor", "sourcename", "accountname","memo"};
					addTable(wsheet, list, title, body);	
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

}
