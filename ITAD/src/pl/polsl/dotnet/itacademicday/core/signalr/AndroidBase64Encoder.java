package pl.polsl.dotnet.itacademicday.core.signalr;

import android.util.Base64;
import microsoft.aspnet.signalr.client.http.BasicAuthenticationCredentials.Base64Encoder;

public class AndroidBase64Encoder implements Base64Encoder {

	@Override
	public String encodeBytes(byte[] bytes) {
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

}
