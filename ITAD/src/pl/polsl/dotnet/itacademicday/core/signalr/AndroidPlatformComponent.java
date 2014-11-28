package pl.polsl.dotnet.itacademicday.core.signalr;

import android.os.Build;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.PlatformComponent;
import microsoft.aspnet.signalr.client.http.HttpConnection;
import microsoft.aspnet.signalr.client.http.android.AndroidHttpConnection;

public class AndroidPlatformComponent implements PlatformComponent {

	@Override
	public HttpConnection createHttpConnection(Logger logger) {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
			return new AndroidHttpConnection(logger);
		} else {
			return Platform.createDefaultHttpConnection(logger);
		}
	}

	@Override
	public String getOSName() {
		return "android";
	}

}
