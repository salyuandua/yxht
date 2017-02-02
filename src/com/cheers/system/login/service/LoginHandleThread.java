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

import com.cheers.account.bean.AccountBean;
import com.cheers.system.logs.service.LogsService;
import com.cheers.util.DES;
import com.cheers.util.DateFormat;
import com.cheers.util.JsonUtil;

public class LoginHandleThread implements Runnable {

	// 异步操作的上下文对象,通过构造方法传进来
	private AsyncContext asyncContext;
	private HttpSession session;
	private HttpServletRequest request;
	private ServletResponse response;
	private LoginService service;
	public LoginHandleThread() {
		service = new LoginService();
		request = (HttpServletRequest)asyncContext.getRequest();
		service.setRequest(request);
	}
	public LoginHandleThread(AsyncContext asyncContext) {
		service = new LoginService();
		this.asyncContext = asyncContext;
		request = (HttpServletRequest)asyncContext.getRequest();
		service.setRequest(request);
	}
	public LoginHandleThread(AsyncContext asyncContext, HttpSession session) {
		service = new LoginService();
		this.asyncContext = asyncContext;
		this.session = session;
		request = (HttpServletRequest)asyncContext.getRequest();
		service.setRequest(request);
	}
	public void run() {
		try {
			response = asyncContext.getResponse();
			PrintWriter out = response.getWriter();
			DES des = new DES();
			service.getMap().put("accountId", session.getAttribute("accountId"));
			String table = request.getParameter("table");
			String account = request.getParameter("account");
			String password = request.getParameter("password");
			AccountBean accountBean=service.getAccount(account);
			Map map = new HashMap();
			map.put("table", table);
			map.put("accountEQ", account);
			Map returnMap = new HashMap();
			if ("false".equals(service.checkDuplicate(map))) {
				returnMap.put("isPass", "false");
				returnMap.put("info", "账号不存在");
				out.print(service.getReturnString(JsonUtil.object2Json(returnMap)));
				
				// 告诉启动异步处理的Servlet异步处理已完成，Servlet就会提交请求
                out.flush();
                out.close();
                asyncContext.complete();
			}
			else {
				map.put("isenableEQ", 1);
				if ("false".equals(service.checkDuplicate(map))) {
					returnMap.put("isPass", "false");
					returnMap.put("info", "账号已停用，请联系管理员启用");
					out.print(service.getReturnString(JsonUtil.object2Json(returnMap)));
				}
				else {
					boolean ok = false;
					List<DynaBean> list = service.getResult();
					for (int i = 0; i < list.size(); i++) {
						DynaBean acc = list.get(i);
						if (acc.get("password").equals(des.createEncryptor(password))) {
							ok = true;
						}
					}
					if (ok) {
						String sid = session.getId();
						ServletContext context = session.getServletContext();
						HashMap hs = (HashMap) context.getAttribute("userList");
						String strcon = "";
						if (hs != null) {
							Iterator iterater = hs.keySet().iterator();
							Iterator myvalues = hs.values().iterator();
	
							Object key = null;
							Object value = null;
							while (iterater.hasNext()) {
								key = iterater.next();
								value = myvalues.next();
								Map info = (Map) value;
	
								if (!key.equals(sid) && account.equals(info.get("account"))) {
									strcon = "你的账号正在别处登录，您的密码可能泄漏。请及时修改密码！\\n" + "\\n登录IP:" + info.get("ip") + "\\n登录时间：" + info.get("loginTime");
									if(((HttpSession) info.get("session"))!=null)
										((HttpSession) info.get("session")).invalidate();

									break;
	
								}
							}
						}
						
						//权限控制
						service.setDataRight(accountBean);
						
						service.checkDuplicate(map);
						session.setAttribute("account", account);
						session.setAttribute("ip", request.getRemoteHost());
						session.setAttribute("loginTime", DateFormat.getNowTime());
						session.setAttribute("accountBean", accountBean);
						LogsService.write("登陆成功", accountBean.getName(),(String)session.getAttribute("ip"), "登陆");
						out.print(service.getReturnString("{\"isPass\":\"true\",\"info\":\""+strcon+"\",\"account\":"+JsonUtil.encodeObject2Json(service.getResult().get(0), "yyyy/MM/dd")+"}"));
						out.flush();
						out.close();
					} else {
						out.print(service.getReturnString("{\"isPass\":\"false\",\"info\":\"密码错误\"}"));
					}
				}
				// 告诉启动异步处理的Servlet异步处理已完成，Servlet就会提交请求
				out.flush();
				out.close();
				asyncContext.complete();
				}
			} catch (Exception e) {
			e.printStackTrace();
			asyncContext.complete();
		}
	}

}
