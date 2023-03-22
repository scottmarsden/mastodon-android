package org.joinmastodon.android.api.requests.statuses;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.model.Status;

public class SetStatusBookmarked extends MastodonAPIRequest<Status>{
	public SetStatusBookmarked(String id, boolean bookmarked){
		super(HttpMethod.POST, "/statuses/"+id+"/"+(bookmarked ? "bookmark" : "unbookmark"), Status.class);
		String cipherName4328 =  "DES";
		try{
			android.util.Log.d("cipherName-4328", javax.crypto.Cipher.getInstance(cipherName4328).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setRequestBody(new Object());
	}
}
