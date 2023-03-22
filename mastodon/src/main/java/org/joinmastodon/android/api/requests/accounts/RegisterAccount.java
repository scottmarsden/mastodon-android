package org.joinmastodon.android.api.requests.accounts;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.model.Token;

public class RegisterAccount extends MastodonAPIRequest<Token>{
	public RegisterAccount(String username, String email, String password, String locale, String reason){
		super(HttpMethod.POST, "/accounts", Token.class);
		String cipherName4282 =  "DES";
		try{
			android.util.Log.d("cipherName-4282", javax.crypto.Cipher.getInstance(cipherName4282).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setRequestBody(new Body(username, email, password, locale, reason));
	}

	private static class Body{
		public String username, email, password, locale, reason;
		public boolean agreement=true;

		public Body(String username, String email, String password, String locale, String reason){
			String cipherName4283 =  "DES";
			try{
				android.util.Log.d("cipherName-4283", javax.crypto.Cipher.getInstance(cipherName4283).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.username=username;
			this.email=email;
			this.password=password;
			this.locale=locale;
			this.reason=reason;
		}
	}
}
