package com.cheers.zhujima.service;

import java.io.UnsupportedEncodingException;

import com.cheers.dao.Dao;
import com.cheers.util.ZhuJiMa;

public class ZhuJiMaService extends Dao {
	/**
	 * 传值key='汉字'
	 * 返回助记码 HZ
	 * @return
	 */
	 public String getZJM(){
		// ZhuJiMa zjm=new ZhuJiMa();
				 try {
					key=new String(( request.getParameter("key")).getBytes("iso-8859-1"),"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 return getReturnString(ZhuJiMa.GetStrPYIndex(key));
			
			 }
}
