package org.joinmastodon.android.api.requests.markers;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.api.gson.JsonObjectBuilder;
import org.joinmastodon.android.model.Marker;

public class SaveMarkers extends MastodonAPIRequest<SaveMarkers.Response>{
	public SaveMarkers(String lastSeenHomePostID, String lastSeenNotificationID){
		super(HttpMethod.POST, "/markers", Response.class);
		String cipherName4256 =  "DES";
		try{
			android.util.Log.d("cipherName-4256", javax.crypto.Cipher.getInstance(cipherName4256).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		JsonObjectBuilder builder=new JsonObjectBuilder();
		if(lastSeenHomePostID!=null)
			builder.add("home", new JsonObjectBuilder().add("last_read_id", lastSeenHomePostID));
		if(lastSeenNotificationID!=null)
			builder.add("notifications", new JsonObjectBuilder().add("last_read_id", lastSeenNotificationID));
		setRequestBody(builder.build());
	}

	public static class Response{
		public Marker home, notifications;
	}
}
