package pl.polsl.dotnet.itacademicday.core.signalr;

import java.util.ArrayList;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

public class SignalRClient {

	protected HubProxy hub;

	protected HubConnection connection;
	private static SignalRClient Instance = null;

	private ArrayList<String> wallInfo = new ArrayList<String>();

	private Logger logger = new Logger() {

		@Override
		public void log(String message, LogLevel level) {
			System.out.println(message);
		}
	};

	private SignalRClient() {
		Platform.loadPlatformComponent(new AndroidPlatformComponent());
		// Change to the IP address and matching port of your SignalR server.
		String host = "http://itadpolsl.pl/signalr";
		HubConnection connection = new HubConnection(host, "", true, logger);

		hub = connection.createHubProxy("wallHub");
		hub.subscribe(new Object() {
			@SuppressWarnings("unused")
			public void posted(String message) {
				wallInfo.add(message);
			}
		});

		connection.start().done(new Action<Void>() {
			@Override
			public void run(Void obj) throws Exception {
				hub.invoke("claimParticipant");
			}
		});
	}

	public boolean sendMessage(String message) {
		return hub.invoke("Post", message).isDone();
	}

	public static SignalRClient getInstance() {
		if (Instance == null) {
			Instance = new SignalRClient();
		}
		return Instance;
	}

	public ArrayList<String> getWallInfo() {
		return wallInfo;
	}
}
