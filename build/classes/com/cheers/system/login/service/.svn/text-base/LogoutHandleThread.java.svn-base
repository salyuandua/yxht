package com.cheers.system.login.service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.DynaBean;

import com.cheers.system.logs.service.LogsService;
import com.cheers.util.DES;
import com.cheers.util.DateFormat;
import com.cheers.util.JsonUtil;

public class LogoutHandleThread implements Runnable {

	// 异步操作的上下文对象,通过构造方法传进来
	private AsyncContext asyncContext;
	private HttpSession session;
	private HttpServletRequest request;
	private ServletResponse response;
	private LogoutService service;
	public LogoutHandleThread() {
		service = new LogoutService();
		request = (HttpServletRequest)asyncContext.getRequest();
		service.setRequest(request);
	}
	public LogoutHandleThread(AsyncContext asyncContext) {
		service = new LogoutService();
		this.asyncContext = asyncContext;
		request = (HttpServletRequest)asyncContext.getRequest();
		service.setRequest(request);
	}
	public LogoutHandleThread(AsyncContext asyncContext, HttpSession session) {
		service = new LogoutService();
		this.asyncContext = asyncContext;
		this.session = session;
		request = (HttpServletRequest)asyncContext.getRequest();
		service.setRequest(request);
	}
	public void run() {
		try {
			;
			response.setContentType(session.getServletContext().getInitParameter("content"));
				
				System.out.println("session destroy" + session.getId());
				request.getServletContext().removeAttribute(session.getId());
				if (session != null) {
					session.invalidate();
				}
				 PrintWriter out = response.getWriter();
			      out.print(true);
			      out.flush();
			      out.close();
		
			asyncContext.complete();
		} catch (Exception e) {
			e.printStackTrace();
			request.getServletContext().removeAttribute(session.getId());
			asyncContext.complete();
		}
	}

}
