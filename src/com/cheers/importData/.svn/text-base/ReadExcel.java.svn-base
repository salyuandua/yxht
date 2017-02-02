package com.cheers.importData;

//读取Excel的类 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;


import com.cheers.exceltools.ReadExcelUtil;
import com.cheers.util.DateFormat;

public class ReadExcel implements ReadFile {
	public static void main(String args[]) {
		ReadExcel xls = new ReadExcel();
		System.out.println(xls.getColumns("测试.xls"));
	}

	public Vector getColumns(String fileName) {
		Vector columns = new Vector();
		try {
			Workbook book = Workbook.getWorkbook(new File(fileName));

			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);

			// 得到第一列第一行的单元格
			for (int j = 0; j < sheet.getColumns(); j++) {
				Cell cell1 = sheet.getCell(j, 0);
				String result = cell1.getContents();
				columns.add(result);
			}

			book.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return columns;

	}

	public Vector getContent(String fileName) {
		Vector content = new Vector();
		try {
			ReadExcelUtil readExcel = new ReadExcelUtil();
			readExcel.setWb(WorkbookFactory.create(new FileInputStream(fileName)));
			// 获得第一个工作表对象
			readExcel.setSheetNum(0);
			int count = readExcel.getRowCount();
			if (count > 0)
				for (int i = 0; i <= count; i++) {
					String[] rows = readExcel.readExcelLine(i, null);
					// 这里面写方法
					if (rows != null) {
						Vector rows1 = new Vector();
						boolean isNull = true;
						for (int j = 0; j < rows.length; j++) {
							String cell1 = rows[j];
							rows1.add(cell1);
							if (cell1 != null && !cell1.equals(""))
								isNull = false;
						}
						if (!isNull) content.add(rows1);
					}
				}

		} catch (Exception e) {
			System.out.println(e);
		}
		return content;
	}

	public Map getContents(String fileName, String sheetNames) {
		Map ret = new HashMap();

		try {
			Workbook book = Workbook.getWorkbook(new File(fileName));

			for (int ii = 0; ii < book.getNumberOfSheets(); ii++) {
				// 获得第一个工作表对象
				Sheet sheet = book.getSheet(ii);
				String sheetName = sheet.getName();
				if (!sheetNames.contains(sheetName))
					continue;

				Vector content = new Vector();
				// 得到第一列第一行的单元格
				for (int i = 0; i < sheet.getRows(); i++) {
					Vector rows = new Vector();
					boolean isNull = true;
					for (int j = 0; j < sheet.getColumns(); j++) {
						Cell cell1 = sheet.getCell(j, i);
						String result = "";
						if (cell1.getType() == CellType.DATE
								|| cell1.getType() == CellType.DATE_FORMULA) {
							DateCell dateCell = (DateCell) cell1;
							Date date = dateCell.getDate();
							result = new DateFormat().getStringDate(date);
						} else
							result = cell1.getContents();
						rows.add(result);
						if (result != null && !result.equals(""))
							isNull = false;
					}
					if (!isNull)
						content.add(rows);
				}
				ret.put(sheetName, content);
			}

			book.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return ret;
	}

}
