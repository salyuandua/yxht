package com.cheers.commonMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class GetUrl
{
  public String getUrl(String urlString)
  {
    StringBuffer sb = new StringBuffer();
    try
    {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      if (conn == null)
        conn.connect();
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      for (String line = null; (line = reader.readLine()) != null; ) {
        sb.append(line);
      }
      reader.close();
    }
    catch (IOException e)
    {
      System.out.println(e.toString());
    }
    System.out.println("getUrlBack="+sb.toString());
    return sb.toString();
  }

  public ArrayList jix(String s)
  {
    ArrayList alist = new ArrayList();
    for (StringTokenizer st = new StringTokenizer(s, "&"); st.hasMoreTokens(); )
    {
      for (StringTokenizer sts = new StringTokenizer(st.nextToken(), "="); sts.hasMoreTokens(); alist.add(sts.nextToken()));
    }

    return alist;
  }

  public ArrayList jixr(String s)
  {
    ArrayList alist = new ArrayList();
    for (StringTokenizer st = new StringTokenizer(s, "&"); st.hasMoreTokens(); alist.add(st.nextToken()));
    return alist;
  }
}