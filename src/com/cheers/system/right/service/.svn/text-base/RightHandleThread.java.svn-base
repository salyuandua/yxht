package com.cheers.system.right.service;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import com.cheers.account.bean.AccountBean;
import com.cheers.system.right.bean.RightBean;
import com.cheers.util.JsonUtil;
import com.cheers.util.MyDao;

public class RightHandleThread implements Runnable{

    //异步操作的上下文对象,通过构造方法传进来
    private AsyncContext asyncContext;
    private MyDao dao = new MyDao();
	private HttpSession session;
    
    public RightHandleThread() {
		
	}

	public RightHandleThread(AsyncContext asyncContext) {
        this.asyncContext =asyncContext;
    }
	public RightHandleThread(AsyncContext asyncContext,HttpSession session) {
        this.asyncContext =asyncContext;
        this.session = session;
    }
 /**
  *
  */
    public void run() {
        try {
            ServletResponse response = asyncContext.getResponse();
            ServletRequest request = asyncContext.getRequest();
            AccountBean bean = (AccountBean) session.getAttribute("accountBean");
            PrintWriter out = response.getWriter();
            String data = request.getParameter("data");
            String action = request.getParameter("action");
            callback= request.getParameter("callback");
            if("add".equals(action)){
            	out.write(getReturnString(addBean(data)));
            }else if("getBean".equals(action)){
            	String id = request.getParameter("id");
            	out.write(getReturnString(getBean(id)));
            }else if("getBeans".equals(action)){
            	
            	out.write(getReturnString(getBeans()));
            }else if("update".equals(action)){
            	//String id = request.getParameter("id");
            	out.write(getReturnString(updateBean( data)));
            }else if("delete".equals(action)){
            	String id = request.getParameter("id");
            	out.write(getReturnString(deleteBean(id)));
            }else if("getTree".equals(action)){
            	out.write(getReturnString(getTree()));
            }else if("getLeftTree".equals(action)){
            	out.write(getReturnString(getLeftTree(bean.getId())));
            }
            //告诉启动异步处理的Servlet异步处理已完成，Servlet就会提交请求
    		out.flush();
    		out.close();
            asyncContext.complete();
        } catch (Exception e) {
            e.printStackTrace();
            asyncContext.complete();
        }
    }
    public String callback;
	public String getReturnString(String str) {
		if (str == null) {
			return   JsonUtil.object2Json("");
		} else {
			return   JsonUtil.object2Json(str);
		}
	}
    
    private String getTree() {
    	List list = new ArrayList();
    	list = new RightService().getRightMap();
    	 if(list==null){
          	return JsonUtil.string2json("");
          }else{
          	return JsonUtil.encodeObject2Json(list);
          }
}
    private String getLeftTree(String accountId) {
    	List list = new ArrayList();
    	list = new RightService().getLeftTreeMap(accountId);
    	 if(list==null){
          	return JsonUtil.string2json("");
          }else{
          	return JsonUtil.encodeObject2Json(list);
          }
}

	/**
     * 获取id对应的部门bean json格式
     * @param id
     * @return bean or ""
     * @author apple
     */
    public String getBean(String id){
    	
    	if(id==null){
    		return "";
    	}
    	RightBean bean = (RightBean) dao.getBeanByField("Right", "com.cheers.right.bean.RightBean", "id", id);
    	  if(bean==null){
          	return JsonUtil.string2json("");
          }else{
          	return JsonUtil.encodeObject2Json(bean);
          }
    }
    
    /**
     * 
     * @return
     */
        public String getBeans(){
        	
        	List list= dao.getBeansByField("Right", "com.cheers.right.bean.RightBean",null,null,null);
            if(list==null){
            	return JsonUtil.string2json("");
            }else{
            	return JsonUtil.encodeObject2Json(list);
            }
        }
    /**
     * 
     * @param data
     * @return
     * @author apple
     */
    public String addBean(String data){
    	//if() 
    	try {
    		RightBean bean = (RightBean) JsonUtil.JSONToBeanCascade(data, "com.cheers.right.bean.RightBean");
    		if (bean == null) {
				return "false";
			}
			if (  dao.addBean(bean)  == 1)
				return "true";
			else
				return "false";
    		
		} catch (Exception e) {
			
			e.printStackTrace();
			return "false";
		}
    	
    }
    
    
    
    /**
     * 
     * @param id
     * @return
     * @author apple
     */
    public String deleteBean(String id){
    	
    	try {
    		
			if (dao.deleteById(id, "right")  == 1)
				return "true";
			else
				return "false";
		} catch (Exception e) {
			
			e.printStackTrace();
			return "false";
		}
    	
    }
    /**
     * 
     * @param id
     * @param data
     * @return
     * @author apple
     */
    public String updateBean( String data){
    	
    	try {
    		RightBean bean = (RightBean) JsonUtil.JSONToBeanCascade(data, "com.cheers.right.bean.RightBean");
    		
    		if (bean == null) {
				return "false";
			}
			if (dao.updateBean(bean)  == 1)
				return "true";
			else
				return "false";
    		
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
    	
    	
    }
 
}
