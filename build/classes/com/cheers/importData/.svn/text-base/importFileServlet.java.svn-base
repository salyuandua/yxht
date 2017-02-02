package com.cheers.importData;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

import com.cheers.account.bean.AccountBean;
import com.cheers.dao.Path;

/** 
 * 要启用 asyncSupported 需要定义一个【拦截该请求】的WebFilter， 
 * 否则将会在[request.startAsync(request,response);]时 
 * 抛java.lang.IllegalStateException: Not supported.的异常; 
 * 另外： 
 * 如何web.xml或者程序中其他地方定义了Filter，且没有指定asyncSupported=true 
 * 同样会跑出如上异常; 
 * @author apple 
 */  
@WebServlet(urlPatterns={"/servlet/importFile"},name="importFile",asyncSupported=true)
public class importFileServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public importFileServlet() {
		super();
	}
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
	    response.setContentType(getServletContext().getInitParameter("content"));
		System.out.println("begin importFile...");
		String servletName = request.getParameter("servletName");
		System.out.println("servletName:"+servletName);
		//上传excel至服务器
		try {
			this.upload(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//跳转到读取excel数据的servlet
		RequestDispatcher dispatcher = request.getRequestDispatcher(servletName);
		dispatcher.forward(request, response);
	}
	
	public void upload(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		AccountBean account = (AccountBean) session.getAttribute("accountBean");

		DiskFileUpload fu = new DiskFileUpload();
		fu.setHeaderEncoding("utf-8");
		
		// 设置允许用户上传文件大小,单位:字节，这里设为5m
		fu.setSizeMax(5 * 1024 * 1024);
		
		// 设置最多只允许在内存中存储的数据,单位:字节
		fu.setSizeThreshold(4096);
		
		// 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
		Path path = new Path();
		path.servletInitialize(getServletConfig().getServletContext());
		String dir1 = path.getPhysicalPath("/importDataFile/", 0); // 传参数
		fu.setRepositoryPath(dir1);
		
		request.setCharacterEncoding("UTF-8");
		// 开始读取上传信息
		List fileItems = fu.parseRequest(request);
		
		// 依次处理每个上传的文件
		Iterator iter = fileItems.iterator();
		
		// 正则匹配，过滤路径取文件名
		String regExp = ".+\\\\(.+)$";
		
		// 过滤掉的文件类型
		String[] errorType={".exe",".com",".cgi",".asp"};
		
		Pattern p = Pattern.compile(regExp);
		
		String zipname = null;
		File file = null;
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			// 忽略其他不是文件域的所有表单信息
			if (!item.isFormField()) {
				String name = item.getName();
				long size = item.getSize();
				if ((name == null || name.equals("")) && size == 0) {
					continue;
				}
				Matcher m = p.matcher(name);
				int name_index=name.lastIndexOf("\\");
				if(name_index==-1){
					name_index=0;
				}else{
					name_index = name_index+1;
				}
				//System.out.println("<>"+name);
				String upFileFullName=name.substring(name_index);
				//System.out.println(upFileFullName+"<>"+name);
				String upFileName = upFileFullName.substring(0, upFileFullName.lastIndexOf("."));
				if (1 == 1) {
					try {
						// 保存上传的文件到指定的目录
						String fileType = ".xls";
						zipname = account.getId() + fileType;
						session.setAttribute("filename", dir1 + zipname); //保存到服务器后的路径及文件名 
						session.setAttribute("upFileName", upFileName);  //上传的原始文件名，不带扩展名
						file = new File(dir1 + zipname);
						item.write(file);
					} catch (Exception e) {
						// out.println(e);
						e.printStackTrace();
					}

				} else {
					throw new IOException("fail to upload");
				}
			}
		}
	}

}
