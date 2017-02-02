package com.cheers.contract.servlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cheers.contract.service.ContractThread;
/** 
 * 要启用 asyncSupported 需要定义一个【拦截该请求】的WebFilter， 
 * 否则将会在[request.startAsync(request,response);]时 
 * 抛java.lang.IllegalStateException: Not supported.的异常; 
 * 另外： 
 * 如何web.xml或者程序中其他地方定义了Filter，且没有指定asyncSupported=true 
 * 同样会跑出如上异常; 
 * @author apple 
 */  
@WebServlet(urlPatterns={"/servlet/Contract"},name="contract",asyncSupported=true)
public class ContractServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ContractServlet() {
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
		//进入异步模式,调用业务处理线程进行业务处理
        //Servlet不会被阻塞,而是直接往下执行
        //业务处理完成后的回应由AsyncContext管理
        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(30000);
        ContractThread productHandleThread = new ContractThread(asyncContext,request.getSession());
        asyncContext.start(productHandleThread);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
