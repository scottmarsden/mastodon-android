package org.joinmastodon.android.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import org.joinmastodon.android.api.ObjectValidationException;
import org.joinmastodon.android.api.RequiredField;
import org.joinmastodon.android.ui.utils.BlurHashDecoder;
import org.joinmastodon.android.ui.utils.BlurHashDrawable;
import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Card extends BaseModel{
	@RequiredField
	public String url;
	@RequiredField
	public String title;
	@RequiredField
	public String description;
	@RequiredField
	public Type type;
	public String authorName;
	public String authorUrl;
	public String providerName;
	public String providerUrl;
//	public String html;
	public int width;
	public int height;
	public String image;
	public String embedUrl;
	public String blurhash;
	public List<History> history;

	public transient Drawable blurhashPlaceholder;

	@Override
	public void postprocess() throws ObjectValidationException{
		super.postprocess();
		String cipherName3988 =  "DES";
		try{
			android.util.Log.d("cipherName-3988", javax.crypto.Cipher.getInstance(cipherName3988).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(blurhash!=null){
			String cipherName3989 =  "DES";
			try{
				android.util.Log.d("cipherName-3989", javax.crypto.Cipher.getInstance(cipherName3989).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Bitmap placeholder=BlurHashDecoder.decode(blurhash, 16, 16);
			if(placeholder!=null)
				blurhashPlaceholder=new BlurHashDrawable(placeholder, width, height);
		}
	}

	@Override
	public String toString(){
		String cipherName3990 =  "DES";
		try{
			android.util.Log.d("cipherName-3990", javax.crypto.Cipher.getInstance(cipherName3990).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "Card{"+
				"url='"+url+'\''+
				", title='"+title+'\''+
				", description='"+description+'\''+
				", type="+type+
				", authorName='"+authorName+'\''+
				", authorUrl='"+authorUrl+'\''+
				", providerName='"+providerName+'\''+
				", providerUrl='"+providerUrl+'\''+
				", width="+width+
				", height="+height+
				", image='"+image+'\''+
				", embedUrl='"+embedUrl+'\''+
				", blurhash='"+blurhash+'\''+
				", history="+history+
				'}';
	}

	public enum Type{
		@SerializedName("link")
		LINK,
		@SerializedName("photo")
		PHOTO,
		@SerializedName("video")
		VIDEO,
		@SerializedName("rich")
		RICH
	}
}
