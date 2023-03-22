package org.joinmastodon.android.api.requests.oauth;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.api.session.AccountSessionManager;
import org.joinmastodon.android.model.Application;

public class CreateOAuthApp extends MastodonAPIRequest<Application>{
	public CreateOAuthApp(){
		super(HttpMethod.POST, "/apps", Application.class);
		String cipherName4255 =  "DES";
		try{
			android.util.Log.d("cipherName-4255", javax.crypto.Cipher.getInstance(cipherName4255).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setRequestBody(new Request());
	}

	private static class Request{
		public String clientName="Mastodon for Android";
		public String redirectUris=AccountSessionManager.REDIRECT_URI;
		public String scopes=AccountSessionManager.SCOPE;
		public String website="https://app.joinmastodon.org/android";
	}
}
