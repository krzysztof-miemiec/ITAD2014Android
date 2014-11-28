package pl.polsl.dotnet.itacademicday.core.signalr;

import java.util.ArrayList;

import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

import com.google.gson.JsonElement;

public class SignalRClient {
	
	protected HubProxy hub;
	
	protected HubConnection connection;

	private SignalRFuture<Void> awaitConnection;
	
	private Logger logger = new Logger() {

		@Override
		public void log(String message, LogLevel level) {
			System.out.println(message);
		}
	};
	public SignalRClient(){
		Platform.loadPlatformComponent( new AndroidPlatformComponent() );
		// Change to the IP address and matching port of your SignalR server.
		String host = "http://itadpolsl.pl/signalr";
		HubConnection connection = new HubConnection( host, "", true,  logger);
		
		hub = connection.createHubProxy( "wallhub" );
		awaitConnection = connection.start();
		connection.received(new MessageReceivedHandler() {
			
			@Override
			public void onMessageReceived(JsonElement json) {
				//list.add(json.toString());
			}
		});
	}
	
	
	public void sendMessage(String message){
		hub.invoke("Post", message);
	}

}
