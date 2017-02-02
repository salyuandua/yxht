package com.cheers.importData;
/**
 * @author sunjianfeng
 * 2013-10-29 14:36:11
 * 收款单导入 ，导入 功能 实现类
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.DataFormat;
import com.cheers.commonMethod.DateFormat;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;

public class ReadExcel_client {
	private String fileUrl; // 服务器存放导入数据excel的路径
	private String upFileName; // 上传的原始文件名，不带扩展名，用于匹配excel的格式，确定读取哪几列数据
	private List<String[]> data = new ArrayList<String[]>(); // 存放数据的list，list中的每个数据代表一行，数组中存放一行中每一个单元格的数据
	private ArrayList<String[]> returnData = new ArrayList<String[]>();
	boolean ifDownload = false;
	
	private int cols = 5; // 导入模板中列的个数 需要根据具体的功能修改参数
	private int startRow = 1; // 批量导入数据的表头在第几行开始 需要根据具体的功能修改参数 下标从0开始
	public ReadExcel_client() {
		
	}
/**
 * 
 * @param account
 * @return
 */
	private boolean InsDB(AccountBean account) {
		boolean flag1 = false;
		DateFormat def = new DateFormat();
		DataFormat df = new DataFormat();
		
		DataBase db=new DataBase();
		try {
			db.connectDb();
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<String> sqls = new ArrayList<String>();

		String operateTime = new DateFormat().getNowTime();

		int row = 0;
		String errorMes = "";
		
		for (String[] rowData : data) {
			errorMes = "";
			String isupdate = "false";
			if (row <= startRow) { // 表头
				if(row == startRow){
					if(!"客户名称".equals(rowData[0])){
						errorMes = errorMes + "第 1 列的列名,";
					}
					if(!"客户编码".equals(rowData[1])){
						errorMes = errorMes + "第 2 列的列名,";
					}
					if(!"客户地址".equals(rowData[2])){
						errorMes = errorMes + "第 3 列的列名,";
					}
					if(!"业务员".equals(rowData[3])){
						errorMes = errorMes + "第4列的列名,";
					}
					if(!"客户分类".equals(rowData[4])){
						errorMes = errorMes + "第 5 列的列名,";
					}
					
					if(!"".equals(errorMes)){
						errorMes = errorMes + "和指定模版中的列名不匹配!";
					}
					
					if (!"".equals(errorMes)){
						rowData[rowData.length-1] = errorMes;
						returnData.add(rowData);
						ifDownload = true;
						break;
					}
				}
				row++;
			} else { // 数据
				//校验客户名称 是否存在
				String customerId = df.getValueFromTable("id", "customer", "name", rowData[0],"");
				if(!"".equals(customerId) && customerId != null){
					//errorMes = errorMes + "已存在此名称的客户名称;";
					isupdate = "true";
				}
				//转化  业务员
				String xiaoshouId = df.getValueFromTable("id", "sysaccount", "name", rowData[3],"");
				if("".equals(xiaoshouId))
					errorMes = errorMes + "不存在此业务员;";
				
				//转化  客户分类
				String CusflId = df.getValueFromTable("id", "base_data", "name", rowData[4]," and type = 'Cusfl' ");
				if("".equals(CusflId))
					errorMes = errorMes + "不存在此客户分类;";
				
				//获得当前数据库中 这个客户 对应的 业务员 信息
				Map<String, String> map = new HashMap<String, String>();
//				map = df.getFzrIdValue(clientId);
				
				if ("".equals(errorMes)) {
					//插入的sql
					String accountId=account.getId();
					String insertIntoStoreCheck = "";
					
					if(isupdate == "true"){
						insertIntoStoreCheck="update Customer set name='"+rowData[0]+"',codes='"+rowData[1]+"',address='"+rowData[2]+"',accountId='"+xiaoshouId+"',customerFlId='"+CusflId+"',creatorid='"+accountId+"',createtime='"+def.getNowTime()+"' where  id='"+customerId+"' ";
					}else{
						insertIntoStoreCheck="insert into Customer(name,codes,address,accountId,customerFlId,creatorid,createtime) " +
								" values ('"+rowData[0]+"','"+rowData[1]+"','"+rowData[2]+"','"+xiaoshouId+"','"+CusflId+"','"+accountId+"','"+def.getNowTime()+"' )";
					}
					
					try {
						
						db.update(insertIntoStoreCheck);
						
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
//					StringBuffer sql = new StringBuffer("insert into Gathering_Import (entryDate,billsNumber,billsType,handlerName,department,clientName,totalNum,totalMoney,clientId,createrId,createTime,ywyid,ywybmid,qyid,bscid) " +
//					"values ('" + rowData[1] + "','"+ rowData[2] +"','"+ rowData[3] +"','"+ rowData[4] +"','"+ rowData[5] +"','"+ rowData[6]+"','"+ rowData[7]+"','"+ rowData[8] +"','"+ clientId +"','"+account.getId()+"','"+def.getNowDate()+"','"+ map.get("ywyid") +"','"+ map.get("ywybmid") +"','"+ map.get("qyid") +"','"+ map.get("bscid") +"')");
//					sqls.add(sql.toString());
				} else {
					rowData[rowData.length-1] = errorMes;
					returnData.add(rowData);
					ifDownload = true;
				}
				row++;
			}
			if(flag1){
//				return false;
			}
		}
		//ifDownload = true;
//		DataBase db = null;
		try {
//			db = new DataBase();
//			db.connectDb();
			db.updateBatch(sqls.toArray(new String[sqls.size()]));
			flag1 = true;
		} catch (DbException e) {
			e.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		return flag1;
	}

	/*
	 * 结束修改
	 */

	public boolean readExcelAndInsDB(String fileUrl, String upFileName, AccountBean accountbean) {
		boolean flag = true;
		this.fileUrl = fileUrl;
		this.upFileName = upFileName;

		// 读文件并填充
		flag = readExcel();
		if (flag){
			flag = InsDB(accountbean);
			if(ifDownload){  //如果需要下载 则说明导入数据有问题
				outWriteXls();
				flag = false;
			}
		}else{
			ifDownload = true;
			outWriteXls();
		}
		return flag;
	}

	// 读取excel中的数据
	private boolean readExcel() {
		boolean flag = true;
		ReadExcel xls = new ReadExcel();
		Vector dom = xls.getContent(fileUrl);

		for (int i = 0; i < dom.size(); i++) { // 从批量导入数据行的表头开始，读取数据
			Vector row = (Vector) dom.get(i); // 获取一行数据
			row.remove(row.lastElement());
			String[] rowData = new String[cols + 1];
			//如果列数小于模板列数，提示导入模板错误
			if(row.size()>= cols){
				for (int j = 0; j < cols; j++) { // 循环每行中的每一列
					rowData[j] = (String) (row.get(j)).toString().replace(" ", "");
				}
				if(i==0){
					rowData[cols] = "错误原因";
					returnData.add(rowData);
				}
				else 
					rowData[cols] = "  ";
			}else{
				cols = 0;
				rowData[0] = "导入数据的列数与模板不匹配";
				returnData.add(rowData);
				flag = false;
				break;
			}
			data.add(rowData);
		}
		return flag;
	}
	
	private void outWriteXls() {
		
		WriteExcle we = new WriteExcle();

		we.Write(returnData, fileUrl, "导入失败表", "门店表", cols+1);
	}

}
