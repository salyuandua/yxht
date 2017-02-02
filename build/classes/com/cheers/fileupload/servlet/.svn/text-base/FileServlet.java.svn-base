package com.cheers.fileupload.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cheers.fileupload.service.FileUploadThread;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet(description = "文件上传", urlPatterns = {"/servlet/Upload"},asyncSupported=true)
@MultipartConfig(// 文件存放路径，必须指定，指定的目录必须存在，否则会抛异常

maxFileSize = 10*1024*1024,// 最大上传文件大小,经测试应该是字节为单位
fileSizeThreshold = 819200// 当数据量大于该值时，内容将被写入文件。（specification中的解释的大概意思，不知道是不是指Buffer
							// size），大小也是已字节单位
// maxRequestSize = 8*1024*1024*6 //针对该 multipart/form-data 请求的最大数量，默认值为
// -1，表示没有限制。以字节为单位。
)


public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(FileServlet.class.getCanonicalName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType(getServletContext().getInitParameter("content"));
		//进入异步模式,调用业务处理线程进行业务处理
        //Servlet不会被阻塞,而是直接往下执行
        //业务处理完成后的回应由AsyncContext管理
        AsyncContext asyncContext = request.startAsync();
        FileUploadThread productHandleThread = new FileUploadThread(asyncContext,request.getSession());
        asyncContext.start(productHandleThread);
	}
	
}
