/**
 * 
 */
package com.cheers.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author apple
 * 
 */
@WebListener()
public class ApplicationListener implements ServletContextAttributeListener, ServletContextListener {
	final static Log LOG = LogFactory.getLog(ApplicationListener.class);
	private java.util.Timer timer = null;
	ServletContext context;
	String rootPath;

	public void contextDestroyed(ServletContextEvent arg0) {
		 System.out.println("Application销毁："+arg0.getServletContext());
		 timer.cancel();
	}

	public void contextInitialized(ServletContextEvent arg0) {
	
		  System.out.println("Application创建："+arg0.getServletContext());
		  Properties p = new Properties();
			Properties pv = new Properties();
		  context = arg0.getServletContext();
		  rootPath = context.getRealPath("/");
		  context.setAttribute("path", rootPath);
		  File file = new File(rootPath+"/WEB-INF/sql.properties");
		  try {
			InputStream is = new FileInputStream(file);
			p.load(is);
			for (Object o : p.values()) {
				is = new FileInputStream(rootPath+"/WEB-INF/classes/"+o.toString());
				pv.load(is);
			}
			for (Object o : pv.keySet()) {
				System.out.println(o + ":" + pv.getProperty(o.toString()));
				context.setAttribute(o.toString(), pv.getProperty(o.toString()));
			}
			is.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		  
		
		// TODO Auto-generated method stub
			timer = new java.util.Timer(true);
			//mcaddress=com.cheers.util.Sdcally.getMC();
			arg0.getServletContext().log("定时器已启动...");
			//System.out.println("定时器开始");
			//timer.scheduleAtFixedRate(new StockTask(), 0,  60*1000);
			//arg0.getServletContext().log("已经添加任务调度表...");	  
		  
		  
		  
		  
	}

	public void attributeAdded(ServletContextAttributeEvent arg0) {
			
	}

	public void attributeRemoved(ServletContextAttributeEvent arg0) {
		
	}

	public void attributeReplaced(ServletContextAttributeEvent arg0) {
		
	}

	
}
