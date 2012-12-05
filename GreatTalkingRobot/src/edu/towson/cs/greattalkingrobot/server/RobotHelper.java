package edu.towson.cs.greattalkingrobot.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.mortbay.log.Log;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;

import edu.towson.cs.greattalkingrobot.shared.ConsistantValues;


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
		Charset chaset=null;
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
			chaset =Charset.forName("UTF-8");
			in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), chaset));
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
	
	/**
	 * get post cotent from map
	 * @param data
	 * @return
	 * @throws Exception
	 */
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
	

	/**
	 * for example:
	 * <html><head><title>Bot Matilda</title><meta name="description" content="Build and host your very own chatbot. Include speech and images. Connect it to instant messaging services, multiplayer games and become part of a growing ecosystem of virtual personalities."><meta name="keywords" content="chatbot, chatterbot, chat bot, pandorabot, software robot, hosting, artificial intelligence, robot, AI, natural language, speech, voice, turing test, IM bot, NPC, game bot, Lisp"><script><!--function sf(){document.f.message.focus();}// --></script></head><body onLoad="sf()"><script type="text/javascript"><!-- google_ad_client = "pub-6845023912477022";google_ad_width = 728;google_ad_height = 90;google_ad_format = "728x90_as";google_ad_channel ="";google_color_border = "725B7F";google_color_bg = "F0F0F0";google_color_link = "000000";google_color_url = "00008B";google_color_text = "663366";//--></script><script type="text/javascript"  src="http://pagead2.googlesyndication.com/pagead/show_ads.js"></script><br>Tell Matilda:<br><form method="POST" name="f"><input type="HIDDEN" name="botcust2" value="b2ebacdc39d2e86e"><table border="0" cellspacing="0"><tr><td><input type="TEXT" autocomplete="off" name="message" size=60></td><td>&nbsp;</td><td><input type="SUBMIT" value="Say"></td></tr><tr><td align="right" valign="top"><font size="-1"><i>Powered by <a href="http://www.pandorabots.com" target="_blank">Pandorabots</a>.</i></font></td></tr></table></form>
	 * <br>
	 * <b>Human:</b> tell me about shape 3 
	 * <br>
	 * <b>Matilda:</b>  
	 * <br>  
	 * It looks stretched out. 
	 * <br></body></html>

	 * @param htmlContent
	 * @return
	 * @throws Exception
	 */
	public static String getResultFromHtml(String question,StringBuffer htmlContent) throws Exception {
		if(htmlContent==null||htmlContent.length()==0){
			throw new Exception("Invalid return html.");
		}
		if(question==null||question.isEmpty()){
			throw new Exception("Invalid question.");
		}
		
		htmlContent = new StringBuffer(StringEscapeUtils.unescapeHtml4(htmlContent.toString()));

		
		//get the answer part of html
		int startingIndex=0;
		
		String partString =null;
		
		//get to the answer
		startingIndex = htmlContent.indexOf("<b>"+ConsistantValues.ROBOT_NAME+":</b>");
		if(startingIndex<0){
			throw new Exception("Cannot find the <b>"+ConsistantValues.ROBOT_NAME+":</b>");
		}
		startingIndex=startingIndex+("<b>"+ConsistantValues.ROBOT_NAME+":</b>").length();
		partString=htmlContent.substring(startingIndex).trim();
		
		
		//remove the <br>
		String result ="";
		if(partString.startsWith("<br>")){
			partString = partString.substring(4);
		}
		result=partString.substring(0, partString.indexOf("<br>"));
		result=result.trim();
		result  = result.replaceAll("\\s+", " ");

		return result;
	}
	
	public static String getCurrentTimestamp(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		
		return "";
	}

	

	public static void flushfileToResponse(String content, HttpServletResponse response, String fileName)  throws Exception{
        OutputStream out=null;
        try{
            System.setProperty("java.awt.headless", "true");
    
            out = response.getOutputStream();
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-control", "private");
            if (content != null) {
                out.write(content.getBytes());
            } else {
                out.write("".getBytes());
            }
    
            out.flush();
        }
        catch(Exception e){
            throw e;
        }
        finally{
            if(out!=null){
                try{
                    out.close();
                }
                catch(Exception e){
                    Log.info("error"+e.getMessage(),e);
                }
            }
        }
    }
}
