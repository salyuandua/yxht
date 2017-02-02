package com.cheers.system.logs.service;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import com.cheers.account.bean.AccountBean;
import com.cheers.util.DataFormat;
import com.cheers.util.JsonUtil;
import com.cheers.util.ReflectionUtil;

public class LogsHandleThread implements Runnable{

	// 异步操作的上下文对象,通过构造方法传进来
	private AsyncContext asyncContext;
	private HttpSession session;
	private HttpServletRequest request;
	private ServletResponse response;
	private LogsService service;
	public LogsHandleThread() {
		service = new LogsService();
		request = (HttpServletRequest)asyncContext.getRequest();
		service.setRequest(request);
	}
	public LogsHandleThread(AsyncContext asyncContext) {
		service = new LogsService();
		this.asyncContext = asyncContext;
		request = (HttpServletRequest)asyncContext.getRequest();
		service.setRequest(request);
	}
	public LogsHandleThread(AsyncContext asyncContext,HttpSession session) {
		service = new LogsService();
		this.asyncContext = asyncContext;
		this.session = session;
		request = (HttpServletRequest)asyncContext.getRequest();
		service.setRequest(request);
	}
	public void run() {
		try {
			response = asyncContext.getResponse();
			PrintWriter out = response.getWriter();
			//if(!request.getParameter("action").equals("add"))
			AccountBean accountBean=(AccountBean) session.getAttribute("accountBean");
			service.getMap().put("accountBean", accountBean);
			
			service.getMap().put("accountId", session.getAttribute("accountId"));
			List<Object[]> list = new ArrayList<Object[]>();
			try {
				///ReflectionUtil reflect = new ReflectionUtil();
				Method method = ReflectionUtil.getDeclaredMethod(service,DataFormat.objectCheckNull(request.getParameter("action")),null);
				//Object obj =reflect.getClazz().newInstance();
				//method.setAccessible(true);
				String result = (String) method.invoke(service);
				//String result = (String) reflect.invokeMethod(service,method.getName(),null,null);
				out.print(result);
			}catch (SecurityException e) {
				e.printStackTrace();
				out.print(JsonUtil.object2Json(""));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				out.print(JsonUtil.object2Json(""));
			}
			// 告诉启动异步处理的Servlet异步处理已完成，Servlet就会提交请求
			out.flush();
			out.close();
			asyncContext.complete();
		} catch (Exception e) {
			e.printStackTrace();
			asyncContext.complete();
		}
	}
	 
}
