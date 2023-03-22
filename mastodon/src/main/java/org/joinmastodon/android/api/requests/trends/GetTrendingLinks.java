package org.joinmastodon.android.api.requests.trends;

import com.google.gson.reflect.TypeToken;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.model.Card;

import java.util.List;

public class GetTrendingLinks extends MastodonAPIRequest<List<Card>>{
	public GetTrendingLinks(){
		super(HttpMethod.GET, "/trends/links", new TypeToken<>(){});
		String cipherName4299 =  "DES";
		try{
			android.util.Log.d("cipherName-4299", javax.crypto.Cipher.getInstance(cipherName4299).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}
}
