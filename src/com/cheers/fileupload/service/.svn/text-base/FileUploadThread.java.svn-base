package com.cheers.fileupload.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.RowSetDynaClass;

import com.cheers.account.bean.AccountBean;
import com.cheers.dao.Dao;
import com.cheers.dao.Dao4Bean;
import com.cheers.database.DataBase;
import com.cheers.database.DbException;
import com.cheers.database.MsSqlPageBean;
import com.cheers.database.PageBean;
import com.cheers.fileupload.bean.FileBean;
import com.cheers.fileupload.servlet.FileServlet;
import com.cheers.util.JsonUtil;
import com.cheers.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
public class FileUploadThread  implements Runnable{
	// 异步操作的上下文对象,通过构造方法传进来
	private String callback="";
	private AsyncContext asyncContext;
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private final static Logger LOGGER = Logger.getLogger(FileServlet.class.getCanonicalName());
	private String fileNameExtractorRegex = "filename=\".+\"";
	public FileUploadThread() {
		request = (HttpServletRequest) (HttpServletRequest)asyncContext.getRequest();
	}
	public FileUploadThread(AsyncContext asyncContext) {
		this.asyncContext = asyncContext;
		request = (HttpServletRequest) (HttpServletRequest)asyncContext.getRequest();
	}
	public FileUploadThread(AsyncContext asyncContext,HttpSession session) {
		this.asyncContext = asyncContext;
		this.session = session;
		request = (HttpServletRequest) (HttpServletRequest)asyncContext.getRequest();
	}
	public void run() {
		try {
			response = (HttpServletResponse) asyncContext.getResponse();
			String mobile=null;
			mobile=request.getParameter("mobile");
			String action = request.getParameter("action");
			callback= request.getParameter("callback");
			List<String> fileNames = new LinkedList<String>();
			if("download".equals(action)){
				String folder = request.getParameter("folder");  //存放附件的文件夹
				String fileId = request.getParameter("fileId");
				String fileName = request.getParameter("fileName");
					if(fileName!=null && !"".equals(fileName)) {
						try {
//							if(mobile!=null){
								fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
//								}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
				    }	    
				    try
				    {
				      String zipFileName = fileId;

				      String dir1 = (String) session.getServletContext().getAttribute("path");
				      
				      String dirFileName = dir1+folder+"\\" + zipFileName;
				      response.setContentType("application/x-msdownload");
				      response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
				      File file = new File(dirFileName);
				      FileInputStream fis = new FileInputStream(file);
				      OutputStream os = response.getOutputStream();
				      byte[] b = new byte[1024];
				      int i = 0;
				      while ((i = fis.read(b)) >= 0)
				      {
				    	 // out.write(b, 0, i);
				         os.write(b, 0, i);
				      }
				      fis.close();
				      os.flush();
				    }
				    catch (Exception e)
				    {
				      e.printStackTrace();
				    }
			}else {
				PrintWriter out = response.getWriter();
				if(action.equals("add")){
				String tableName=request.getParameter("tableName");
				String tableId=request.getParameter("tableId");
	        	Collection<Part> parts = request.getParts();
	        	for (Iterator<Part> iterator = parts.iterator(); iterator.hasNext();) {
	    			Part part = iterator.next();
	    			// 从Part的content-disposition中提取上传文件的文件名
	    			String fileName = getFileName(part);
	    			if (fileName != null) {
	    				FileBean fileBean = new FileBean();
	                    String fileId = UUID.getUUID(0)+fileName.substring(fileName.indexOf("."));
	                    fileBean.setFileName(fileName);
	                    fileBean.setFileId(fileId);
	                    fileBean.setIsEnable("1");
						fileBean.setTableName(tableName);
						fileBean.setTableId(tableId);
						fileNames.add(fileId);
						new Dao4Bean().addBean(fileBean);
						System.out.println(request.getServletContext().getRealPath("/file/")+fileName);
	    				part.write(request.getServletContext().getRealPath("/file")+"/"+fileId);
	    			}
	    		}
			}else if(action.equals("modify")){//修改
				String tableName=request.getParameter("tableName");
				String tableId=request.getParameter("tableId");
				//删除 
				String ids=request.getParameter("deleteIds");
				if(ids!=null&&!ids.equals("")){
					ObjectMapper mapper = new ObjectMapper();
	            	Collection<Part> parts = request.getParts();
					String[] id = mapper.readValue(ids, String[].class);
						//String id[] = ids.split(",");
			        	delFiles(id, tableName);
	        	}
	        	Collection<Part> parts = request.getParts();
	        	for (Iterator<Part> iterator = parts.iterator(); iterator.hasNext();) {
	    			Part part = iterator.next();
	    			// 从Part的content-disposition中提取上传文件的文件名
	    			String fileName = getFileName(part);
	    			if (fileName != null) {
	    				FileBean fileBean = new FileBean();
	                    String fileId = UUID.getUUID(0)+fileName.substring(fileName.indexOf("."));
	                    fileBean.setFileName(fileName);
	                    fileBean.setFileId(fileId);
	                    fileBean.setIsEnable("1");
						fileBean.setTableName(tableName);
						fileBean.setTableId(tableId);
						fileNames.add(fileId);
						new Dao4Bean().addBean(fileBean);
						part.write(request.getServletContext().getRealPath("/file")+"/"+fileId);
	    			}
	    		}
			}
			else if(action.equals("getFiles")){
				String tableName=request.getParameter("tableName");
				String tableId=request.getParameter("tableId");
				out.print(getFiles(tableName,tableId));
				out.flush();
	    		out.close();
			}else if("uploadPhotos".equals(action)) {
				request.setCharacterEncoding("utf-8");
				String now=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				String clientId=request.getParameter("clientid");//客户id
				String lng=request.getParameter("lng");
				String lat=request.getParameter("lat");
				String accountId=((AccountBean)this.session.getAttribute("accountBean")).getId();
				String isios=request.getParameter("isios");
				DataBase db=new DataBase();
				Connection conn=null;					
				//开始一个事物			
				try{
				conn=db.getConnection();
				conn.setAutoCommit(false);
				Statement st=conn.createStatement();
				String tableName="photoupload";
	            Collection<Part> parts = request.getParts();
	            Iterator<Part> it= parts.iterator();
	            int flag=0;
	            int id=0;
	            while(it.hasNext()){
	            	Part part=it.next();
	            	if(flag==0){//获取memo,写入主表
	            		String memo=JSONObject.fromObject(part.getName()).getString("memo");
		            	String mainSql="insert into photoupload(customerid,createtime,accountid,latitude,longtitude,memo,isios) values("+
		        				"'"+clientId+"','"+now+"','"+accountId+"','"+lat+"','"+lng+"','"+memo+"',"+isios+");";
		            	System.out.println(mainSql);
		            	st.executeUpdate(mainSql);
        	            
        	            ResultSet rs=st.executeQuery("select max(id) id from photoupload");
        	            if(rs.next()){
        	                id = rs.getInt(1);
        	                System.out.println("最大id："+id);
        	            }
        	            flag++;
	            	}
	        		String tableId=""+id;
	            	String photoId=java.util.UUID.randomUUID().toString()+".jpg";//在服务器存储的id           
	            	String path=request.getServletContext().getRealPath("/file")+"/"+photoId;
	            	String sql="insert into uploadfile(tablename,tableid,fileid,isenable) values('"+tableName+"','"+tableId+"','"+photoId+"','1');";
	            	System.out.println("sql is "+sql);
	            	st.executeUpdate(sql);
	            	System.out.println(path);
	            	//写照片	            	
	            	 part.write(path);

	            }
           	 conn.commit();
           	 out.print("1");
				}catch(SQLException e){
					System.out.println("sqlexception rollback...");
					conn.rollback();
					out.print("0");
				}catch(IOException e){
					System.out.println("ioexception rollback...");
					conn.rollback();
					out.print("0");
				}finally{
					conn.setAutoCommit(true);
					conn.close();	
					db.close();
				}
				
			}
				out.flush();
				out.close();
			}
			asyncContext.complete();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
			asyncContext.complete();
		}
		
	}

	
	
	private boolean delFiles(String ids[],String tableName){
		if(ids!=null){
			 String deleteSql="";
			 deleteSql+="delete  from  uploadFILE where tableName = '"+tableName +"'	and id in (0";
			for (int i=0;i<ids.length;i++) {
				deleteSql+=","+ids[i];
			}
			deleteSql+="	)";
			DataBase db=new DataBase();
			try{
				db.connectDb();
				int i=db.update(deleteSql);
				 if(i!=0)return true;
				 else return false;
			}catch(Exception e){
				e.printStackTrace();
				return  false;
			}finally{
				try {
					db.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else return true;
	}
//	public String getReturnString(String str) {
//		if (str == null) {
//			return  callback + "(" + JsonUtil.object2Json("")+ ")";
//		} else {
//			return  callback + "(" +  JsonUtil.object2Json(str)+ ")";
//		}
//	}
//	public String getReturn(List result) {
//		if (result == null) {
//			return callback + "(" + JsonUtil.object2Json("") + ")";
//		} else  
//			return callback + "(" + JsonUtil.encodeObject2Json(result) + ")";
//		 
//	}
	public String getReturnString(String str) {
		if(callback!=null&&!callback.equals("null")){
			
		if (str == null) {
			return  callback + "(" + JsonUtil.object2Json("")+ ")";
		} else {
			return  callback + "(" +  JsonUtil.object2Json(str)+ ")";
		}
		}else{
			if (str == null) {
				return  JsonUtil.object2Json("");
			} else {
				return  JsonUtil.object2Json(str);
			}
		}
	}
	public String getReturn(List result) {
		if(callback!=null&&!callback.equals("null")){
			
		if (result == null) {
			return callback + "(" + JsonUtil.object2Json("") + ")";
		} else  
			return callback + "(" + JsonUtil.encodeObject2Json(result) + ")";
		}else{
			if (result == null) {
				return   JsonUtil.object2Json("") ;
			} else  
				return  JsonUtil.encodeObject2Json(result)  ;
		}
		
	}
	 private String getFiles(String tableName, String tableId) {
		 List result = new ArrayList();
		String sql="select top 10 * from uploadFILE where tableName='"+tableName
				+"'	and tableId ="+tableId+" and	isenable=1	 order by id  desc";
		DataBase db=new DataBase();
		try{
			db.connectDb();
			PageBean bean = new MsSqlPageBean();
			ResultSet rs = bean.listData(sql.toString(), "").getRsResult();
			RowSetDynaClass rsdc = new RowSetDynaClass(rs);
			result = rsdc.getRows();
			if (result == null) {
				return getReturnString("");
			} else {
				return  getReturn(result);
			}
		}catch(Exception e){
			e.printStackTrace();
			return getReturnString("");
		}finally{
			try {
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
			LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
			String fileName = null;
			Pattern pattern = Pattern.compile(fileNameExtractorRegex);
			Matcher matcher = pattern.matcher(partHeader);
			if (matcher.find()) {
				fileName = matcher.group();
				fileName = fileName.substring(10, fileName.length() - 1);
			}
			return fileName;
		}
		//上传照片终极方法
		private String uploadPhoto() throws Exception{
			String now=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String mainData=request.getParameter("data");//主表数据
			JSONObject sqlObj=JSONObject.fromObject(mainData);
			String mainSql="insert into photoupload(customerid,phototype,phototime,createtime,accountid,structid) values("+
			"'"+sqlObj.getString("customerid")+"','"+sqlObj.getString("phototype")+"','"+now+"','"+now+"','"+sqlObj.getString("accountid")+"','"+sqlObj.getString("structid")+"');";
			System.out.println(mainSql);
			DataBase db=new DataBase();
			Connection conn=null;					
			//开始一个事物			
			//try{
			conn=db.getConnection();
			conn.setAutoCommit(false);
			Statement st=conn.createStatement();
			st.executeUpdate(mainSql,st.RETURN_GENERATED_KEYS);
		
            ResultSet rs = st.getGeneratedKeys();  
            int id=0;
            if(rs.next()){
                id = rs.getInt(1);
                System.out.println("最大id："+id);
            }

			int flag=1;
			String tableId=""+id;
			String tableName=request.getParameter("tableName");
            Collection<Part> parts = request.getParts();
            Iterator<Part> it= parts.iterator();           
            while(it.hasNext()){
            	Part part=it.next();
            	String data=part.getName();
            	System.out.println("===="+data);
            	JSONObject o=JSONObject.fromObject(data);
            	String photoName=o.getString("photoname");//照片名称
            	String photoId=java.util.UUID.randomUUID().toString()+".jpg";//在服务器存储的id           
            	String path=request.getServletContext().getRealPath("/file")+"/"+photoId+".jpg";
            	String sql="insert into uploadfile(tablename,tableid,fileid,filename,isenable) values('"+tableName+"','"+tableId+"','"+photoId+"','"+photoName+"','1');";
            	System.out.println("sql is "+sql);
            	st.executeUpdate(sql);
            	System.out.println("write pic..");
            	//写照片
            	 part.write(path);             	
            }
            conn.commit();
			conn.setAutoCommit(true);
			conn.close();	
			db.close();
			return "1";
		}
		
		
		
		
		
		
}
