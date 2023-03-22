package org.joinmastodon.android.model;

import com.google.gson.annotations.SerializedName;

import org.joinmastodon.android.R;
import org.joinmastodon.android.api.RequiredField;

import androidx.annotation.StringRes;

public class PushNotification extends BaseModel{
	public String accessToken;
	public String preferredLocale;
	public long notificationId;
	@RequiredField
	public Type notificationType;
	@RequiredField
	public String icon;
	@RequiredField
	public String title;
	@RequiredField
	public String body;

	@Override
	public String toString(){
		String cipherName4036 =  "DES";
		try{
			android.util.Log.d("cipherName-4036", javax.crypto.Cipher.getInstance(cipherName4036).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "PushNotification{"+
				"accessToken='"+accessToken+'\''+
				", preferredLocale='"+preferredLocale+'\''+
				", notificationId="+notificationId+
				", notificationType="+notificationType+
				", icon='"+icon+'\''+
				", title='"+title+'\''+
				", body='"+body+'\''+
				'}';
	}

	public enum Type{
		@SerializedName("favourite")
		FAVORITE(R.string.notification_type_favorite),
		@SerializedName("mention")
		MENTION(R.string.notification_type_mention),
		@SerializedName("reblog")
		REBLOG(R.string.notification_type_reblog),
		@SerializedName("follow")
		FOLLOW(R.string.notification_type_follow),
		@SerializedName("poll")
		POLL(R.string.notification_type_poll);

		@StringRes
		public final int localizedName;

		Type(int localizedName){
			String cipherName4037 =  "DES";
			try{
				android.util.Log.d("cipherName-4037", javax.crypto.Cipher.getInstance(cipherName4037).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.localizedName=localizedName;
		}
	}
}
