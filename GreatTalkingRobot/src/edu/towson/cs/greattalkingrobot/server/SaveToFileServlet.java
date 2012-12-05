package edu.towson.cs.greattalkingrobot.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SaveToFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3101819553323613500L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request.getAttribute(arg0)
		System.out.println("test");
		try{
			HttpSession s=request.getSession();
			
			RobotHelper.flushfileToResponse("test", response, "test.tex");
		}
		catch(Exception e){
			
		}
	}

}
