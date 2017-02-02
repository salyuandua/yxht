package com.cheers.sp.service;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cheers.util.DataFormat;
import com.cheers.util.JsonUtil;
import com.cheers.util.ReflectionUtil;

public class SpHandleThread  implements Runnable{
	// 异步操作的上下文对象,通过构造方法传进来
		private AsyncContext asyncContext;
		private HttpSession session;
		private HttpServletRequest request;
		private ServletResponse response;
		private SpService service;
		public SpHandleThread() {
			service = new SpService();
			request = (HttpServletRequest)asyncContext.getRequest();
			service.setRequest(request);
		}
		public SpHandleThread(AsyncContext asyncContext) {
			service = new SpService();
			this.asyncContext = asyncContext;
			request = (HttpServletRequest)asyncContext.getRequest();
			service.setRequest(request);
		}
		public SpHandleThread(AsyncContext asyncContext,HttpSession session) {
			service = new SpService();
			this.asyncContext = asyncContext;
			this.session = session;
			request = (HttpServletRequest)asyncContext.getRequest();
			service.setRequest(request);
		}
		public void run() {
			try {
				response = asyncContext.getResponse();
				PrintWriter out = response.getWriter();
				try {
					Method method = ReflectionUtil.getDeclaredMethod(service,DataFormat.objectCheckNull(request.getParameter("action")),null);
					String result = (String) method.invoke(service);
					out.print(result);
				}catch (SecurityException e) {
					e.printStackTrace();
					out.print(JsonUtil.object2Json(""));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					out.print(JsonUtil.object2Json(""));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					out.print(JsonUtil.object2Json(""));
				} catch (InvocationTargetException e) {
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
