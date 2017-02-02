/**
 * 
 */
package com.cheers.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author apple
 * 
 */
@WebListener()
public class UserListener implements  HttpSessionListener,
HttpSessionAttributeListener, ServletContextListener {
	final static Log LOG = LogFactory.getLog(UserListener.class);

	ServletContext context;

	// 用户信息
	private static Map userList;

	/**
	 * 当增加一个HttpSession时激发
	 * 
	 * @param evt
	 */
	
	public void sessionCreated(HttpSessionEvent evt) {
		long time = evt.getSession().getCreationTime();
		
		String sessionId = evt.getSession().getId();
		System.out.println("sessioncreated:::::"+sessionId+"::::::"+time);
		userList = (HashMap) context.getAttribute("userList");
		if (userList == null || userList.size() == 0) {
			userList = new HashMap();
		}
		//判断是否有session存储的用户信息map
		Map info = (Map) userList.get(sessionId);
		if(info==null){
			info = new HashMap();
		}
		info.put("session", evt.getSession());
		userList.put(sessionId, info);
		context.setAttribute("userList", userList);
	}
	/**
	 * 当一个session失效时激发
	 * 
	 * @param evt
	 */
	
	public void sessionDestroyed(HttpSessionEvent evt) {
		System.out.println("sessionDestroyed!");
		//获取用户列表
		userList = (HashMap) context.getAttribute("userList");
		if (userList == null || userList.size() == 0) {
			userList = new HashMap();
		} 
		//删除登陆的用户信息
		userList.remove(evt.getSession().getId());
		//注销session
		evt.getSession().invalidate();
		context.setAttribute("userList", userList);

	}
	/**
	 * 增加一个新的属性时激发
	 * 
	 * @param se
	 */
	
	public void attributeAdded(HttpSessionBindingEvent se) {
		String attributeName = (String) se.getName();
		String sessionId = se.getSession().getId();
		userList = (HashMap) context.getAttribute("userList");
		if (userList == null || userList.size() == 0) {
			userList = new HashMap();
		}
		//判断是否有session存储的用户信息map
		Map info = (Map) userList.get(sessionId);
		if(info==null){
			info = new HashMap();
		}
		if(attributeName != null && (attributeName.trim().equals("ip"))){
			info.put(attributeName, se.getValue());
			
		}else if(attributeName != null && (attributeName.trim().equals("account"))){
			info.put(attributeName, se.getValue());
			
		}else if(attributeName != null && (attributeName.trim().equals("loginTime"))){
			info.put(attributeName, se.getValue());
			
		}
		userList.put(sessionId, info);
		context.setAttribute("userList", userList);
	}
	
	public void attributeReplaced(HttpSessionBindingEvent se) {
		String attributeName = (String) se.getName();
		String sessionId = se.getSession().getId();
		userList = (HashMap) context.getAttribute("userList");
		if (userList == null || userList.size() == 0) {
			userList = new HashMap();
		}
		//判断是否有session存储的用户信息map
		Map info = (Map) userList.get(sessionId);
		if(info==null){
			info = new HashMap();
		}
		if(attributeName != null && (attributeName.trim().equals("ip"))){
			info.put(se.getName(), se.getValue());
			
		}else if(attributeName != null && (attributeName.trim().equals("account"))){
			info.put(attributeName, se.getValue());
			
		}else if(attributeName != null && (attributeName.trim().equals("loginTime"))){
			info.put(se.getName(), se.getValue());
			
		}
		userList.put(sessionId, info);
		context.setAttribute("userList", userList);
	}
	public void attributeRemoved(HttpSessionBindingEvent se) {
		
		
	}
	
	 public void contextDestroyed(ServletContextEvent sce) {
		    this.context = null;
		  }
	
		  public void contextInitialized(ServletContextEvent sce) {
		System.out.print("userlist初始化");
		    this.context = sce.getServletContext();
		  }
	
}
