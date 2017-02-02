package com.cheers.importData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriteExcle {
	public void Write(Vector returnData, String fileName, String sheetName, String title, int cols) {}

	public void Write(List<String[]> returnData, String fileName, String sheetName,
			String title, int cols) {
		try {
			// 创建可写入的Excel工作薄
			WritableWorkbook wwb = Workbook.createWorkbook(new File(fileName));
			
			// 创建Excel工作表 样式
			jxl.write.WritableFont wfc = new jxl.write.WritableFont(
					WritableFont.ARIAL, 15, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE2);
			jxl.write.WritableCellFormat wcfFc = new jxl.write.WritableCellFormat(
					wfc);
			wcfFc.setAlignment(jxl.format.Alignment.CENTRE);
			
			// 创建Excel工作表 样式1
			jxl.write.WritableFont wfc1 = new jxl.write.WritableFont(
					WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
					false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
			jxl.write.WritableCellFormat wcfFc1 = new jxl.write.WritableCellFormat(
					wfc1);
			wcfFc1.setAlignment(jxl.format.Alignment.CENTRE);
			wcfFc1.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.MEDIUM);
			
			// 创建Excel工作表 样式2
			jxl.write.WritableFont wfc2 = new jxl.write.WritableFont(
					WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
					false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
			jxl.write.WritableCellFormat wcfFc2 = new jxl.write.WritableCellFormat(
					wfc2);
			wcfFc2.setAlignment(jxl.format.Alignment.CENTRE);
			wcfFc2.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.MEDIUM);
			wcfFc2.setBackground(jxl.format.Colour.RED);// 背景红色
			
			// 创建Excel工作表
			WritableSheet ws = wwb.createSheet(sheetName, 0);
			
			Label label;
			//ws.mergeCells(0, 0, 0, 1);
			
			int rowNo = 0;
			for(String[] rowData : returnData){
				for(int colNo=0; colNo<cols; colNo++){  //循环每行中的每一列
					String cellData = rowData[colNo];
					if(cellData.contains("(error:"))  //无效数据单元格标注底色
						label = new Label(colNo, rowNo, cellData, wcfFc2);
					else
						label = new Label(colNo, rowNo, cellData, wcfFc1);
					ws.addCell(label);
				}
				rowNo ++;
			}
			
			
			// 写入Exel工作表
			wwb.write();
			// 关闭Excel工作薄对象
			wwb.close();
			System.out.println("成功写入EXCEL文件！");

		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Writes(ArrayList<ReturnDataBean> list, String fileUrl) {
		try {
			// 创建可写入的Excel工作薄
			WritableWorkbook wwb = Workbook.createWorkbook(new File(fileUrl));
			
			// 创建Excel工作表 样式
			jxl.write.WritableFont wfc = new jxl.write.WritableFont(
					WritableFont.ARIAL, 15, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE2);
			jxl.write.WritableCellFormat wcfFc = new jxl.write.WritableCellFormat(
					wfc);
			wcfFc.setAlignment(jxl.format.Alignment.CENTRE);
			
			// 创建Excel工作表 样式1
			jxl.write.WritableFont wfc1 = new jxl.write.WritableFont(
					WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
					false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
			jxl.write.WritableCellFormat wcfFc1 = new jxl.write.WritableCellFormat(
					wfc1);
			wcfFc1.setAlignment(jxl.format.Alignment.CENTRE);
			wcfFc1.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.MEDIUM);
			
			// 创建Excel工作表 样式2
			jxl.write.WritableFont wfc2 = new jxl.write.WritableFont(
					WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
					false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
			jxl.write.WritableCellFormat wcfFc2 = new jxl.write.WritableCellFormat(
					wfc2);
			wcfFc2.setAlignment(jxl.format.Alignment.CENTRE);
			wcfFc2.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.MEDIUM);
			wcfFc2.setBackground(jxl.format.Colour.RED);// 背景红色
			
			int sheetIndex = 0;
			for(ReturnDataBean bean : list){
				// 创建Excel工作表
				WritableSheet ws = wwb.createSheet(bean.getSheetName(), sheetIndex++);
				
				Label label;
				
				int rowNo = 0;
				for(String[] rowData : bean.getReturnData()){
					for(int colNo=0; colNo<bean.getCols(); colNo++){  //循环每行中的每一列
						String cellData = rowData[colNo];
						if(cellData.contains("(error:"))  //无效数据单元格标注底色
							label = new Label(colNo, rowNo, cellData, wcfFc2);
						else
							label = new Label(colNo, rowNo, cellData, wcfFc1);
						ws.addCell(label);
					}
					rowNo ++;
				}
			}
			
			// 写入Exel工作表
			wwb.write();
			// 关闭Excel工作薄对象
			wwb.close();
			System.out.println("成功写入EXCEL文件！");
			
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
