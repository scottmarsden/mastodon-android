package org.joinmastodon.android.api.requests.statuses;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.model.StatusContext;

public class GetStatusContext extends MastodonAPIRequest<StatusContext>{
	public GetStatusContext(String id){
		super(HttpMethod.GET, "/statuses/"+id+"/context", StatusContext.class);
		String cipherName4318 =  "DES";
		try{
			android.util.Log.d("cipherName-4318", javax.crypto.Cipher.getInstance(cipherName4318).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}
}
