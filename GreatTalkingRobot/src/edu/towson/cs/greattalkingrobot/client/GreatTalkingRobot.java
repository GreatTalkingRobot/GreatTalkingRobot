package edu.towson.cs.greattalkingrobot.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.towson.cs.greattalkingrobot.shared.ConsistantValues;
import edu.towson.cs.greattalkingrobot.shared.FieldVerifier;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GreatTalkingRobot implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side talkingToRobot service.
	 */
	private final TalkingToRobotServiceAsync talkingToRobotService = GWT
			.create(TalkingToRobotService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		//say button
		final Button sayButton = new Button("     Say     ");
		sayButton.removeStyleName("gwt-Button");
		sayButton.addStyleName("btn btn-primary");

		
		//question filed
		final TextBox questionFiled = new TextBox();
		questionFiled.setText("");
		questionFiled.removeStyleName("gwt-TextBox");
		questionFiled.addStyleName("input-xxlarge");
		final Label errorLabel = new Label();
		
		//Form
		final FormPanel formPan = new FormPanel();
		formPan.setAction("/SaveToFile");
	    formPan.setMethod(FormPanel.METHOD_POST);
		final VerticalPanel dialogVPanel = new VerticalPanel();

		final Hidden hiddenDialog=new Hidden();
		hiddenDialog.setName(ConsistantValues.HISTROY_TEXT_AREA_NAME);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		HTML htmltitle = new HTML();
		htmltitle.setHTML("<h4>Chat History</h4>");
		horizontalPanel.add(htmltitle);
		
		HTML emptyHtml = new HTML();
		emptyHtml.setHTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		horizontalPanel.add(emptyHtml);
		
		 // Add a 'submit' button.
		final Button saveButton = new Button("Save To File", new ClickHandler() {
		      public void onClick(ClickEvent event) {
			        formPan.submit();
			      }
			    });
		saveButton.removeStyleName("gwt-Button");
		saveButton.addStyleName("btn btn-primary");
		horizontalPanel.add(saveButton);
		dialogVPanel.add(horizontalPanel);

		
		final HTML html = new HTML();
		html.setHTML("");
		dialogVPanel.add(html);
		dialogVPanel.add(hiddenDialog);
		formPan.add(dialogVPanel);
		
		
		dialogVPanel.setVisible(false);


		// Add the questionFiled and sayButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(questionFiled);
		RootPanel.get("sendButtonContainer").add(sayButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		RootPanel.get("historyContainer").add(formPan);
		//RootPanel.get("printButtonContainer").add(printButton);

		// Focus the cursor on the name field when the app loads
		questionFiled.setFocus(true);
		questionFiled.selectAll();
		
		

		
		// Create a handler for the sayButton and questionFiled
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sayButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the questionFiled.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the questionFiled to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				final String textToServer = questionFiled.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter some question");
					return;
				}

				// Then, we send the input to the server.
				sayButton.setEnabled(false);
				
				
				talkingToRobotService.askingRobot(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								
								// Create the popup dialog box
								final DialogBox dialogBox = new DialogBox();
								dialogBox.setAnimationEnabled(true);
								final Button closeButton = new Button("Close");
								// We can set the id of a widget by accessing its Element
								closeButton.getElement().setId("closeButton");
								final Label textToServerLabel = new Label();
								final HTML serverResponseLabel = new HTML();
								VerticalPanel dialogVPanel = new VerticalPanel();
								dialogVPanel.addStyleName("dialogVPanel");
								dialogVPanel.add(new HTML("<b>Human:</b>"));
								dialogVPanel.add(textToServerLabel);
								dialogVPanel.add(new HTML("<br><b>"+ConsistantValues.ROBOT_NAME+":</b>"));
								dialogVPanel.add(serverResponseLabel);
								dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
								dialogVPanel.add(closeButton);
								dialogBox.setWidget(dialogVPanel);

								// Add a handler to close the DialogBox
								closeButton.addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent event) {
										dialogBox.hide();
										sayButton.setEnabled(true);
										sayButton.setFocus(true);
									}
								});
								textToServerLabel.setText(textToServer);
								serverResponseLabel.setText("");
								dialogBox
										.setText("Unexpected error - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								boolean showingButton=true;
								if(result!=null){
									if(result.startsWith("0")){
										showingButton=false;
									}
								}
								if(result!=null&&result.length()>1){
									result = result.substring(1);
								}
								if(!dialogVPanel.isVisible()){
									dialogVPanel.setVisible(true);
									if(!showingButton){
										saveButton.setVisible(false);
									}
									
								}
								html.setHTML(
										"Human: "+textToServer+"<br>"
										+ConsistantValues.ROBOT_NAME+": "+result+"<br><br>"
										+html.getHTML()
										);
								
								if(hiddenDialog.getValue()==null||hiddenDialog.getValue().isEmpty()){
									hiddenDialog.setValue(
											"Human: "+textToServer+"\n"
											+ConsistantValues.ROBOT_NAME+": "+result
											);
								}
								else {
									hiddenDialog.setValue(hiddenDialog.getValue()+"\n\n"
										+"Human: "+textToServer+"\n"
										+ConsistantValues.ROBOT_NAME+": "+result
										);
								}
								//closeButton.setFocus(true);
								questionFiled.setText("");
								questionFiled.setFocus(true);
								sayButton.setEnabled(true);


							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sayButton.addClickHandler(handler);
		questionFiled.addKeyUpHandler(handler);
		
		
	}
}
