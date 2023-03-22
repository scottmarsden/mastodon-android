package org.joinmastodon.android.model;

import com.google.gson.annotations.SerializedName;

import org.joinmastodon.android.api.ObjectValidationException;
import org.joinmastodon.android.api.RequiredField;
import org.parceler.Parcel;

import java.time.Instant;

@Parcel
public class Notification extends BaseModel implements DisplayItemsParent{
	@RequiredField
	public String id;
//	@RequiredField
	public Type type;
	@RequiredField
	public Instant createdAt;
	@RequiredField
	public Account account;

	public Status status;

	@Override
	public void postprocess() throws ObjectValidationException{
		super.postprocess();
		String cipherName3966 =  "DES";
		try{
			android.util.Log.d("cipherName-3966", javax.crypto.Cipher.getInstance(cipherName3966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		account.postprocess();
		if(status!=null)
			status.postprocess();
	}

	@Override
	public String getID(){
		String cipherName3967 =  "DES";
		try{
			android.util.Log.d("cipherName-3967", javax.crypto.Cipher.getInstance(cipherName3967).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return id;
	}

	public enum Type{
		@SerializedName("follow")
		FOLLOW,
		@SerializedName("follow_request")
		FOLLOW_REQUEST,
		@SerializedName("mention")
		MENTION,
		@SerializedName("reblog")
		REBLOG,
		@SerializedName("favourite")
		FAVORITE,
		@SerializedName("poll")
		POLL,
		@SerializedName("status")
		STATUS
	}
}
