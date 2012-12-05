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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
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
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final TalkingToRobotServiceAsync greetingService = GWT
			.create(TalkingToRobotService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Say");

		final TextBox nameField = new TextBox();
		nameField.setText("");
		nameField.setWidth("360px");
		final Label errorLabel = new Label();
		
		

		final TextArea resultArea=new TextArea();
		resultArea.setWidth("1100px");
		resultArea.setHeight("200px");
		resultArea.setName(ConsistantValues.HISTROY_TEXT_AREA_NAME);
		//resultArea.set
	
		resultArea.setReadOnly(true);
		//resultArea.setStyleName(style)
		final FormPanel formPan = new FormPanel();
		formPan.setAction("/SaveToFile");
	    formPan.setMethod(FormPanel.METHOD_POST);
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.add(resultArea);
		formPan.add(dialogVPanel);
		

	    // Add a 'submit' button.
		dialogVPanel.add(new Button("Save To File", new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        formPan.submit();
	      }
	    }));

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		RootPanel.get("historyContainer").add(formPan);
		//RootPanel.get("printButtonContainer").add(printButton);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		

		
		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				final String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter some question");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				
				
				greetingService.askingRobot(textToServer,
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
										sendButton.setEnabled(true);
										sendButton.setFocus(true);
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
								
								
								
								resultArea.setText(
										"Human: "+textToServer+"\n"
										+ConsistantValues.ROBOT_NAME+": "+result+"\n\n"
										+resultArea.getText()
										);
								//closeButton.setFocus(true);
								nameField.setText("");
								nameField.setFocus(true);
								sendButton.setEnabled(true);


							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
		
		
	}
}
