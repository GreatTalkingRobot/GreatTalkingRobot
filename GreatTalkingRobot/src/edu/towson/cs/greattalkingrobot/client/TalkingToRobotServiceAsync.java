package edu.towson.cs.greattalkingrobot.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>TalkingToRobotService</code>.
 */
public interface TalkingToRobotServiceAsync {
	void askingRobot(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
