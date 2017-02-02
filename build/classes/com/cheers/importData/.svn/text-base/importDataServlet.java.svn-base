package com.cheers.importData;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cheers.account.bean.AccountBean;
import com.cheers.filter.LoginFilter;
import com.cheers.news.service.NewsHandleThread;

/** 
 * 要启用 asyncSupported 需要定义一个【拦截该请求】的WebFilter， 
 * 否则将会在[request.startAsync(request,response);]时 
 * 抛java.lang.IllegalStateException: Not supported.的异常; 
 * 另外： 
 * 如何web.xml或者程序中其他地方定义了Filter，且没有指定asyncSupported=true  
 * 同样会跑出如上异常; 
 * @author apple 
 */  
@WebServlet(urlPatterns={"/servlet/importData"},name="importData",asyncSupported=true)
public class importDataServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public importDataServlet() {
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
		System.out.println("begin importData...");
		
		//读取配置文件中的地址信息
		Properties p = new Properties();
		InputStream fis;
		fis = LoginFilter.class.getClassLoader().getResourceAsStream("weburl.properties");
		try {
			p.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//获取前台的地址：http://localhost:8080/ldx
		String sendurl=p.getProperty("weburl");
		
		HttpSession se = request.getSession();  // Servlet 中获取 Session 对象 
		AccountBean account = new AccountBean();
		//获取当前登陆用户的id
		String creatid = ((AccountBean)se.getAttribute("accountBean")).getId();
		account.setId(creatid);
		String successUrl = sendurl+"/app/customer/customerinfo/infoimport/importDataSuccess.jsp";
	    String errorUrl = sendurl+"/app/customer/customerinfo/infoimport/importDataError.jsp?fileName="+account.getId()+".xls";
		String action = request.getParameter("action");
		System.out.println(action+"正在导入...");
		if ("test1".equals(action)) {
			String fileName = (String) request.getSession().getAttribute("filename");  //保存到服务器后的路径及文件名
			String upFileName = (String) request.getSession().getAttribute("upFileName");  //上传的原始文件名，不带扩展名
			ReadExcel_client red = new ReadExcel_client();
			boolean isok = red.readExcelAndInsDB(fileName, upFileName, account);
			if (isok) {
				response.sendRedirect(successUrl);
			}else{
				response.sendRedirect(errorUrl);
			}		
		}else if ("test2".equals(action)) {
			String fileName = (String) request.getSession().getAttribute("filename");  //保存到服务器后的路径及文件名
			String upFileName = (String) request.getSession().getAttribute("upFileName");  //上传的原始文件名，不带扩展名
			ReadExcel_lianxiren red = new ReadExcel_lianxiren();
			boolean isok = red.readExcelAndInsDB(fileName, upFileName, account);
			if (isok) {
				response.sendRedirect(successUrl);
			}else{
				response.sendRedirect(errorUrl);
			}		
		}
		
	}

}
