package com.cheers.commonMethod;
import java.io.IOException;

import java.io.InputStream;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;


public class GetProperty {
	private  InputStream configFile;
	private  Properties prop;
	
	//无参构造函数
	public GetProperty(){
		getConfig();
	}
	//初始化
	public void getConfig(){
		try {
			configFile = getClass().getResourceAsStream("workflow.properties");
			prop = new Properties();
			prop.load(configFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//获得指定参数的值
	public String getValue(String key){
		return prop.getProperty(key);
	}
	
	//获取 系统 账号id
	public String getId_System() {
		return this.getValue("ID_SYSTEM");
	}
	
	//获取 组织机构内部
	public String getZzjgnb() {
		return this.getValue("zzjgnb");
	}
	
	//获取 组织机构外部
	public String getZzjgwb() {
		return this.getValue("zzjgwb");
	}
	//获取 人员分组内部
	public String getRyfznb() {
		return this.getValue("ryfznb");
	}
	//获取 人员分组外部
	public String getRyfzwb() {
		return this.getValue("ryfzwb");
	}
	//获取 管理员的ID
	public String getId_Gly() {
		return this.getValue("ID_GLY");
	}
	
}
