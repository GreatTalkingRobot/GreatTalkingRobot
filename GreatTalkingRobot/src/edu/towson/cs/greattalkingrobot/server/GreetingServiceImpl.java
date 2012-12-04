package edu.towson.cs.greattalkingrobot.server;

import java.util.HashMap;
import java.util.Map;

import edu.towson.cs.greattalkingrobot.client.GreetingService;
import edu.towson.cs.greattalkingrobot.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		Map<String, String> data = new HashMap<String, String>();
		data.put("message", input);
		String finalResult=null;
		try{
			StringBuffer result=RobotHelper.doSubmit("http://www.pandorabots.com/pandora/talk?botid=c340d2ec6e348c5f", data);
			System.out.println(result);
			
			 finalResult = RobotHelper.getResultFromHtml(input, result);
			System.out.println(finalResult);

		}
		catch(Exception e){
			throw new IllegalArgumentException("",e);
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
