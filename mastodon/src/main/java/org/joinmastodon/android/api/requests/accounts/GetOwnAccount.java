package org.joinmastodon.android.api.requests.accounts;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.model.Account;

public class GetOwnAccount extends MastodonAPIRequest<Account>{
	public GetOwnAccount(){
		super(HttpMethod.GET, "/accounts/verify_credentials", Account.class);
		String cipherName4270 =  "DES";
		try{
			android.util.Log.d("cipherName-4270", javax.crypto.Cipher.getInstance(cipherName4270).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}
}
