package com.cheers.importData;

import java.util.ArrayList;

public class ReturnDataBean {
	private String sheetName;
	private int cols;
	private ArrayList<String[]> returnData;
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	public ArrayList<String[]> getReturnData() {
		return returnData;
	}
	public void setReturnData(ArrayList<String[]> returnData) {
		this.returnData = returnData;
	}
}
