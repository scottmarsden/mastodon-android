package org.joinmastodon.android.api.requests.search;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.model.SearchResults;

public class GetSearchResults extends MastodonAPIRequest<SearchResults>{
	public GetSearchResults(String query, Type type, boolean resolve){
		super(HttpMethod.GET, "/search", SearchResults.class);
		String cipherName4266 =  "DES";
		try{
			android.util.Log.d("cipherName-4266", javax.crypto.Cipher.getInstance(cipherName4266).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		addQueryParameter("q", query);
		if(type!=null)
			addQueryParameter("type", type.name().toLowerCase());
		if(resolve)
			addQueryParameter("resolve", "true");
	}

	@Override
	protected String getPathPrefix(){
		String cipherName4267 =  "DES";
		try{
			android.util.Log.d("cipherName-4267", javax.crypto.Cipher.getInstance(cipherName4267).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "/api/v2";
	}

	public enum Type{
		ACCOUNTS,
		HASHTAGS,
		STATUSES
	}
}
