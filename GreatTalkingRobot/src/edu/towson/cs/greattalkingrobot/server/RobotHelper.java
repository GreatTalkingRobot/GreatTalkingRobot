package edu.towson.cs.greattalkingrobot.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class RobotHelper {
	
    private static final Logger log = Logger.getLogger(RobotHelper.class.getName());

	/**
	 * 
	 * @param url
	 * @param data
	 * @throws Exception
	 */
	public static StringBuffer doSubmit(String url, Map<String, String> data)
			throws Exception {
		String location = "robotHelper.doSubmit";
		log.info(location);
		
		URL siteUrl =null;
		URLConnection urlConnection =null;
		OutputStreamWriter out= null;
		BufferedReader in =null;
		String content =null;

		StringBuffer result = null;
		try {
			siteUrl = new URL(url);
			urlConnection = (URLConnection) siteUrl.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			try{
				out = new OutputStreamWriter(
						urlConnection.getOutputStream());
				content=getPostContent(data);
				log.info("The content is " + content);
				out.write(content);
				out.flush();
			}
			finally{
				if(out!=null){
					out.close();
				}
			}
			in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			try {

				result = new StringBuffer();
				String line = "";
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
			} finally {
				if(in!=null){
					in.close();
				}
			}
			return result;
		} catch (Exception e) {
			throw new Exception("Failed to submit:", e);
		}
	}
	
	
	private static String getPostContent(Map<String, String> data) throws Exception{
		Set<String> keys = data.keySet();
		Iterator<String> keyIter = keys.iterator();
		String content = "";
		for (int i = 0; keyIter.hasNext(); i++) {
			Object key = keyIter.next();
			if (i != 0) {
				content += "&";
			}
			content += key + "="
					+ URLEncoder.encode(data.get(key), "UTF-8");
		}
		return content;
	}

}
