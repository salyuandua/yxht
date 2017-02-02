package com.cheers.export.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.cheers.account.bean.AccountBean;
import com.cheers.commonMethod.SqlUtil;
import com.cheers.database.DataBase;
import com.cheers.export.service.ExportFileService;
import com.cheers.export.service.ExportFileThread;
import com.cheers.export.service.ExportThread;
import com.cheers.util.DataFormat;

/** 
 * 要启用 asyncSupported 需要定义一个【拦截该请求】的WebFilter， 
 * 否则将会在[request.startAsync(request,response);]时 
 * 抛java.lang.IllegalStateException: Not supported.的异常; 
 * 另外： 
 * 如何web.xml或者程序中其他地方定义了Filter，且没有指定asyncSupported=true 
 * 同样会跑出如上异常; 
 * @author apple 
 */  
@WebServlet(urlPatterns={"/servlet/exportFile"},name="exportFile",asyncSupported=true)
public class exportFileServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public exportFileServlet() {
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
		System.out.println("begin exportFile...");
		String action = request.getParameter("action");
		//下载excel至本地
		try {
			HttpSession session=request.getSession();
			String accountid=((AccountBean)session.getAttribute("accountBean")).getId();
			AccountBean accountBean = (AccountBean)session.getAttribute("accountBean");
			String canAccountIds = accountBean.get_canAccountIds();//用户的下级用户
			String selfId = accountBean.getId();//当前登录人id
			String[] ids= request.getParameterValues("ids");
			ExportFileService service=new ExportFileService();
//			service.getMap().put("accountId",((AccountBean)session.getAttribute("accountBean")).getId());
			if("ControllerInfo".equals(action)){
				List<DynaBean> ds=	service.getControllerInfoExcel(canAccountIds,selfId,ids);
				service.exportExcel(response,ds, "ControllerInfo");
			}else if("CusVisitInfo".equals(action)){
				List<DynaBean> ds=	service.getCusVisitInfoExcel(canAccountIds,selfId,ids);
				service.exportExcel(response,ds, "CusVisitInfo");
			}else if("CusInfo".equals(action)){
				List<DynaBean> ds=	service.getCusInfoExcel(canAccountIds,selfId,ids);
				service.exportExcel(response,ds, "CusInfo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
