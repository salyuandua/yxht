package com.cheers.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsonUtil
{
  private static Log log = LogFactory.getLog(JsonUtil.class);

  public static final String encodeObject2Json(Object pObject)
  {
    String jsonString = "";
    if (DataFormat.booleanCheckNull(pObject)) {
      log.warn("传入的Java对象为空,不能将其序列化为Json资料格式.请检查!");
    }
    else if (((pObject instanceof ArrayList)) || (JSONUtils.isArray(pObject))) {
      //System.out.println(pObject.getClass());
      JSONArray jsonArray = JSONArray.fromObject(pObject);
      jsonString = jsonArray.toString();
    } else {
      JSONObject jsonObject = JSONObject.fromObject(pObject);
      jsonString = jsonObject.toString();
    }

    if (log.isInfoEnabled()) {
      //log.info("序列化后的JSON资料输出:\n" + jsonString);
    }
    return jsonString;
  }

  public static final String encodeObject2Json(Object pObject, String pFormatString)
  {
    String jsonString = "";
    if (!DataFormat.isEmpty(pObject))
    {
      JsonConfig cfg = new JsonConfig();
      cfg.registerJsonValueProcessor(Timestamp.class, new JsonValueProcessorImpl(pFormatString));
      cfg.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessorImpl(pFormatString));
      cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl(pFormatString));
      if ((pObject instanceof ArrayList)) {
        JSONArray jsonArray = JSONArray.fromObject(pObject, cfg);
        jsonString = jsonArray.toString();
      } else {
        JSONObject jsonObject = JSONObject.fromObject(pObject, cfg);
        jsonString = jsonObject.toString();
      }
    }
    if (log.isInfoEnabled()) {
      //log.info("序列化后的JSON资料输出:\n" + jsonString);
    }
    return jsonString;
  }

  private static String encodeJson2PageJson(String jsonString, Integer totalCount)
  {
    jsonString = "{TOTALCOUNT:" + totalCount + ", ROOT:" + jsonString + "}";
    if (log.isInfoEnabled()) {
      //log.info("合并后的JSON资料输出:\n" + jsonString);
    }
    return jsonString;
  }

  public static final String encodeList2PageJson(List list, int totalCount, int page_number, String dataFormat)
  {
    String subJsonString = "";
    if (DataFormat.isEmpty(dataFormat))
      subJsonString = encodeObject2Json(list);
    else {
      subJsonString = encodeObject2Json(list, dataFormat);
    }
    String jsonString = "{\"total\":" + totalCount + ",\"page_number\":" + page_number + " ,\"rows\":" + subJsonString + "}";
    if (log.isInfoEnabled()) {
      //log.info("序列化后的JSON资料输出:\n" + jsonString);
    }
    return jsonString;
  }

  public static final String encodeList2PageJson(List list, Integer totalCount, String dataFormat)
  {
    String subJsonString = "";
    if (DataFormat.checkNull(dataFormat))
      subJsonString = encodeObject2Json(list);
    else {
      subJsonString = encodeObject2Json(list, dataFormat);
    }
    String jsonString = "{\"total\":" + totalCount + ", \"rows\":" + subJsonString + "}";
    //System.out.println(jsonString + "::::::::::::");
    return jsonString;
  }

  public static Object JSONToBeanCascade(String json, String beanName)
    throws Exception
  {
    if ((DataFormat.checkNull(json)) || (DataFormat.checkNull(beanName))) {
      return null;
    }
    ObjectMapper om = new ObjectMapper();
    return om.readValue(json, Class.forName(beanName));
  }

  public static final String object2Json(Object obj)
  {
    ObjectMapper om = new ObjectMapper();
    try {
      return om.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }return "";
  }

  public static String string2json(String s)
  {
    if (s == null)
      return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      switch (ch) {
      case '"':
        sb.append("\\\"");
        break;
      case '\\':
        sb.append("\\\\");
        break;
      case '\b':
        sb.append("\\b");
        break;
      case '\f':
        sb.append("\\f");
        break;
      case '\n':
        sb.append("\\n");
        break;
      case '\r':
        sb.append("\\r");
        break;
      case '\t':
        sb.append("\\t");
        break;
      case '/':
        sb.append("\\/");
        break;
      default:
        if ((ch >= 0) && (ch <= '\037')) {
          String ss = Integer.toHexString(ch);
          sb.append("\\u");
          for (int k = 0; k < 4 - ss.length(); k++) {
            sb.append('0');
          }
          sb.append(ss.toUpperCase());
        } else {
          sb.append(ch);
        }
      }
    }
    return sb.toString();
  }

  public static void main(String[] args)
  {
    Map map = new HashMap();
    map.put("name", "hello world");
    map.put("neck", "baby");
    List list = new ArrayList();
    list.add("good");
    list.add("bad");
    list.add("");
    list.add(Integer.valueOf(1));
    map.put("list", list);
    String a = "{a:123}";
    //System.out.println(object2Json(a));
    //System.out.println(encodeObject2Json(a));
  }
}