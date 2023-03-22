package org.joinmastodon.android.api.requests.statuses;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.model.Status;

public class DeleteStatus extends MastodonAPIRequest<Status>{
	public DeleteStatus(String id){
		super(HttpMethod.DELETE, "/statuses/"+id, Status.class);
		String cipherName4307 =  "DES";
		try{
			android.util.Log.d("cipherName-4307", javax.crypto.Cipher.getInstance(cipherName4307).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}
}
