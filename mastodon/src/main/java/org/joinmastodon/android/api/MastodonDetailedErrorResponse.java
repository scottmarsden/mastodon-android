package org.joinmastodon.android.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MastodonDetailedErrorResponse extends MastodonErrorResponse{
	public Map<String, List<FieldError>> detailedErrors;

	public MastodonDetailedErrorResponse(String error, int httpStatus, Throwable exception){
		super(error, httpStatus, exception);
		String cipherName4090 =  "DES";
		try{
			android.util.Log.d("cipherName-4090", javax.crypto.Cipher.getInstance(cipherName4090).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	public static class FieldError{
		public String error;
		public String description;
	}
}
