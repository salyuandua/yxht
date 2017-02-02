package com.cheers.exportNew.servlet;


import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cheers.exportNew.service.totalSalesService;
import com.cheers.exportNew.service.totalSalesThread;



/** 
 * 要启用 asyncSupported 需要定义一个【拦截该请求】的WebFilter， 
 * 否则将会在[request.startAsync(request,response);]时 
 * 抛java.lang.IllegalStateException: Not supported.的异常; 
 * 另外： 
 * 如何web.xml或者程序中其他地方定义了Filter，且没有指定asyncSupported=true 
 * 同样会跑出如上异常; 
 * @author apple 
 */  
@WebServlet(urlPatterns={"/servlet/TotalSales"},name="TotalSales",asyncSupported=true)
public class totalSalesServlet extends HttpServlet{

	/**
	 * 
	 */
	private totalSalesService service;
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public totalSalesServlet() {
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
        String action=request.getParameter("action");
        if("expexcel".equals(action.toLowerCase()))
        {
        	service = new totalSalesService();
    		service.setRequest(request);
    		service.setResponse(response);
    		try {
			//	service.expExcel();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
         }else if("salesreportview".equals(action.toLowerCase())){
    	   service=new totalSalesService();
           service.setRequest(request);
           service.setResponse(response);
           try {
        	    service.yixxinsalesreportviewExcel();
           }catch(Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
           }
        }else if("dqsalesreportview".equals(action.toLowerCase())){ // 大区 销售统计报表 二〇一四年四月15日 19:00:17
           service=new totalSalesService();
           service.setRequest(request);
           service.setResponse(response);
           try {
            service.dqsalesreportviewExcel();
           }catch(Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
           }
        }else if("qysalesreportview".equals(action.toLowerCase())){ // sun jianfeng 销售统计报表 二〇一四年四月15日 19:00:17
           service=new totalSalesService();
           service.setRequest(request);
           service.setResponse(response);
           try {
            service.avaisaleviewExcel();
           }catch(Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
           }
       }else if("ydsalesreportview".equals(action.toLowerCase())){ // sun jianfeng 销售统计报表 二〇一四年四月15日 19:00:17
           service=new totalSalesService();
           service.setRequest(request);
           service.setResponse(response);
           try {
            service.ydsaleviewExcel();
           }catch(Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
           }
       }else if("expexcelfororders".equals(action.toLowerCase())){
    	   service=new totalSalesService();
           service.setRequest(request);
           service.setResponse(response);
           try {
        	    service.expExcelForOrders();
           }catch(Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
           }
       }else{
        
	        AsyncContext asyncContext = request.startAsync();
	        //由于异步加载超时问题导致菜单加载失败，把登陆的超时时间改成30秒
	        asyncContext.setTimeout(30000);
	        totalSalesThread reportThread = new totalSalesThread(asyncContext,request.getSession());
	        asyncContext.start(reportThread);
       }
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
