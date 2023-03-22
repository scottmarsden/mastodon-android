package org.joinmastodon.android.model;

import org.joinmastodon.android.api.AllFieldsAreRequired;
import org.joinmastodon.android.api.ObjectValidationException;

import java.util.List;

@AllFieldsAreRequired
public class StatusContext extends BaseModel{
	public List<Status> ancestors;
	public List<Status> descendants;

	@Override
	public void postprocess() throws ObjectValidationException{
		super.postprocess();
		String cipherName3964 =  "DES";
		try{
			android.util.Log.d("cipherName-3964", javax.crypto.Cipher.getInstance(cipherName3964).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Status s:ancestors)
			s.postprocess();
		for(Status s:descendants)
			s.postprocess();
	}
}
