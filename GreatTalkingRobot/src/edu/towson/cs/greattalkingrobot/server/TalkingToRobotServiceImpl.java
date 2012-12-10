package edu.towson.cs.greattalkingrobot.server;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.towson.cs.greattalkingrobot.client.TalkingToRobotService;
import edu.towson.cs.greattalkingrobot.shared.ConsistantValues;
import edu.towson.cs.greattalkingrobot.shared.FieldVerifier;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TalkingToRobotServiceImpl extends RemoteServiceServlet implements
		TalkingToRobotService {
    private static final Logger log = Logger.getLogger(TalkingToRobotServiceImpl.class.getName());


	public String askingRobot(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		log.info("UserAgent:***"+userAgent);
		boolean noButtonShowing=false;
		if(userAgent!=null){
		    Pattern pIPad = Pattern.compile("iPad");
		    Pattern pIphone = Pattern.compile("iPhone");

		    Matcher m1 = pIPad.matcher(userAgent);
		    Matcher m2 = pIphone.matcher(userAgent);

			if(m1.find()||m2.find()){
				noButtonShowing=true;
			}
		}
		

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		Map<String, String> data = new HashMap<String, String>();
		data.put("message", input);
		String finalResult=null;
		try{
			StringBuffer result=RobotHelper.doSubmit(ConsistantValues.ROBOT_URL, data);
			finalResult = RobotHelper.getResultFromHtml(input, result);
		}
		catch(Exception e){
			throw new IllegalArgumentException("",e);
		}
		if(finalResult!=null){
			if(noButtonShowing){
				finalResult="0"+finalResult;
			}
			else{
				finalResult ="1"+finalResult;
			}
		}
		return  finalResult;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
