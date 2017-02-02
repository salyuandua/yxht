package com.cheers.fileupload.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.dao.Dao;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.util.UUID;
import com.fasterxml.jackson.core.JsonProcessingException;

public class FileUploadService extends Dao {
	
	/**
	 * 添加
	 * 
	 * @param e
	 *            map遵守以下规则 key: table value 表名 (String) Required Y key: fk
	 *            value 子表外键 (String) Required 存储一个表或是多个表的主表时可为空 ，子表为必填 key:
	 *            mainTable value 主表名 (String) Required 存储一个表或是多个表的主表时可为空，子表为必填
	 *            key: 表字段 value 表字段值 (String) Required N 。。。。。。 key: 表字段 value
	 *            表字段值 (String) Required N
	 * 
	 * @return
	 */
	public String add(){

		try {
	    	Collection<Part> parts = request.getParts();
	    	Map e = new HashMap();

			List<Map> list =new ArrayList<Map>();
	    	for (Iterator<Part> iterator = parts.iterator(); iterator.hasNext();) {
				Part part = iterator.next();
				// 从Part的content-disposition中提取上传文件的文件名
				String fileName = getFileName(part);
				if (fileName != null) {
	                String fileId = UUID.getUUID(0)+fileName.substring(fileName.indexOf("."));
	                e.put("fileName", fileName+fileId);
	                e.put("fileId", fileId);
	                e.put("IsEnable", "1");
	                e.put("table", data.get("table"));
	                e.put("id", data.get("id"));
	                list.add(e);
	                part.write(request.getServletContext().getRealPath("/file")+"/"+fileId);
				}
				for(Map map :list){
					return getReturnString( super.add(map));
				}
	    	}
	    	return getReturnString("false");
		} catch (IOException e1) {
			
			e1.printStackTrace();
			return getReturnString("false");
			
		} catch (ServletException e1) {
			
			e1.printStackTrace();
			return getReturnString("false");
		}
	}
	

	
	
	
	
	public String update(){

		return getReturnString(super.update());
	}
	
	public String delete(){
		
		return getReturnString(super.delete());
	}
	

	
	
	
	
	

	/**
		 * 从Part的Header信息中提取上传文件的文件名
		 * 
		 * @param part
		 * @return 上传文件的文件名，如果如果没有则返回null
		 */
		private String getFileName(Part part) {
			// 获取header信息中的content-disposition，如果为文件，则可以从其中提取出文件名
			String partHeader = part.getHeader("content-disposition");
			//LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
			String fileName = null;
			String fileNameExtractorRegex = "filename=\".+\"";
			Pattern pattern = Pattern.compile(fileNameExtractorRegex);
			Matcher matcher = pattern.matcher(partHeader);
			if (matcher.find()) {
				fileName = matcher.group();
				fileName = fileName.substring(10, fileName.length() - 1);
			}
			return fileName;
		}
}
