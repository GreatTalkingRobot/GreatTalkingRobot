package edu.towson.cs.greattalkingrobot.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.towson.cs.greattalkingrobot.shared.ConsistantValues;

public class SaveToFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3101819553323613500L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request.getAttribute(arg0)
		//System.out.println("test");
		try{
			String result =request.getParameter(ConsistantValues.HISTROY_TEXT_AREA_NAME);
			//result = RobotHelper.reorganize(result);
			RobotHelper.flushfileToResponse(result, response, ConsistantValues.HISTROY_FILE_NAME);
		}
		catch(Exception e){
			throw new ServletException("Failed to generate file:",e);
		}
	}
	
	
}
