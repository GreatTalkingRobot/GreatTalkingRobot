package edu.towson.cs.greattalkingrobot.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveToFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3101819553323613500L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request.getAttribute(arg0)
		//System.out.println("test");
		try{
			String result =request.getParameter("histroy");
			result = reOrganize(result);
			RobotHelper.flushfileToResponse(result, response, "chatHistory.txt");
		}
		catch(Exception e){
			throw new ServletException("Failed to generate file:",e);
		}
	}
	
	private String reOrganize(String result) {
		try {
			if (result == null || result.isEmpty()) {
				return "";
			}
			String[] listOfResult = result.split("Human:");
			if (listOfResult == null || listOfResult.length == 0
					|| listOfResult.length == 1) {
				return result;
			}

			int count = listOfResult.length;
			String reverse = "Human:"+listOfResult[count-1]+"\n\n";
			count--;

			while (count >=1) {
				count--;
				if(!listOfResult[count].isEmpty()){
					reverse = reverse + "Human:" + listOfResult[count];
				}
			}
			return reverse;
		} catch (Exception e) {
			return result;
		}
	}

}
