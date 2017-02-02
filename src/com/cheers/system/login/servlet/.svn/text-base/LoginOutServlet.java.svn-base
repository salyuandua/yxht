package com.cheers.system.login.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cheers.util.JsonUtil;
/**
 * 要启用 asyncSupported 需要定义一个【拦截该请求】的WebFilter，
 * 否则将会在[request.startAsync(request,response);]时
 * 抛java.lang.IllegalStateException: Not supported.的异常; 另外：
 * 如何web.xml或者程序中其他地方定义了Filter，且没有指定asyncSupported=true 同样会跑出如上异常;
 * 
 * @author apple
 */
@WebServlet(urlPatterns = {"/LoginOut"}, name = "loginOut", initParams = {@WebInitParam(name = "url", value = "/index.html")}, asyncSupported = true)
public class LoginOutServlet extends HttpServlet {
	HttpSession session;
	String url;
	/**
	 * Constructor of the object.
	 */
	public LoginOutServlet() {
		super();
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */

	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

	// Initialize global variables
	public void init() throws ServletException {
		this.url = getServletConfig().getInitParameter("url");
	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strpath = request.getContextPath();
		response.setContentType(getServletContext().getInitParameter("content"));
		try {
			String mobile=null;
			mobile=request.getParameter("mobile");
			String callback=null;
			
				callback=request.getParameter("callback");
			
			session = request.getSession();
			System.out.println("session destroy" + session.getId());
			request.getServletContext().removeAttribute(session.getId());
			if (session != null) {
				session.invalidate();
			}
			 PrintWriter out = response.getWriter();
			 if(mobile!=null){
		      out.print(callback + "(" + JsonUtil.object2Json(callback) + ")");
			 }else{
				 out.print(JsonUtil.object2Json(callback));
			 }
		      out.flush();
		      out.close();
		} catch (Exception e) {
			e.printStackTrace();
			request.getServletContext().removeAttribute(session.getId());
		} 
	}
	// Process the HTTP Post request
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}
}
