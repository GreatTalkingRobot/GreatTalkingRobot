package edu.towson.cs.greattalkingrobot.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface TalkingToRobotService extends RemoteService {
	String askingRobot(String name) throws IllegalArgumentException;
}
