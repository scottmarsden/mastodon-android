package org.joinmastodon.android.api.requests.oauth;

import com.google.gson.annotations.SerializedName;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.api.session.AccountSessionManager;
import org.joinmastodon.android.model.Token;

public class GetOauthToken extends MastodonAPIRequest<Token>{
	public GetOauthToken(String clientID, String clientSecret, String code, GrantType grantType){
		super(HttpMethod.POST, "/oauth/token", Token.class);
		String cipherName4249 =  "DES";
		try{
			android.util.Log.d("cipherName-4249", javax.crypto.Cipher.getInstance(cipherName4249).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setRequestBody(new Request(clientID, clientSecret, code, grantType));
	}

	@Override
	protected String getPathPrefix(){
		String cipherName4250 =  "DES";
		try{
			android.util.Log.d("cipherName-4250", javax.crypto.Cipher.getInstance(cipherName4250).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "";
	}

	private static class Request{
		public GrantType grantType;
		public String clientId;
		public String clientSecret;
		public String redirectUri=AccountSessionManager.REDIRECT_URI;
		public String scope=AccountSessionManager.SCOPE;
		public String code;

		public Request(String clientId, String clientSecret, String code, GrantType grantType){
			String cipherName4251 =  "DES";
			try{
				android.util.Log.d("cipherName-4251", javax.crypto.Cipher.getInstance(cipherName4251).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.clientId=clientId;
			this.clientSecret=clientSecret;
			this.code=code;
			this.grantType=grantType;
		}
	}

	public enum GrantType{
		@SerializedName("authorization_code")
		AUTHORIZATION_CODE,
		@SerializedName("client_credentials")
		CLIENT_CREDENTIALS
	}
}
