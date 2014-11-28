package pl.polsl.dotnet.itacademicday.core.signalr;

import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class SignalRClient {

	protected HubProxy hub;

	protected HubConnection connection;

	private SignalRFuture<Void> awaitConnection;

	private Logger logger = new Logger() {

		@Override
		public void log(String message, LogLevel level){
			System.out.println(message);
		}
	};

	public SignalRClient(final OnMessage onMsg) {
		Platform.loadPlatformComponent(new AndroidPlatformComponent());
		// Change to the IP address and matching port of your SignalR server.
		String host = "http://itadpolsl.pl/signalr";
		HubConnection connection = new HubConnection(host, "", true, logger);

		hub = connection.createHubProxy("wallhub");
		awaitConnection = connection.start();
		hub.on("broadcastMessage", new SubscriptionHandler1<String>() {
			@Override
			public void run(String msg){
				onMsg.onMessage(msg);
			}
		}, String.class);
	}

	public boolean sendMessage(String message){
		hub.invoke("Post", message); //TODO zwracanie false w przypadku niepowodzenia, albo dodanie listenera w WallPage
		return true;
	}

	public interface OnMessage {
		public void onMessage(String message);
	}

}
