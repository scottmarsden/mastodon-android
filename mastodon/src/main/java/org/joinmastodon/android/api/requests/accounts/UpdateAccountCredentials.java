package org.joinmastodon.android.api.requests.accounts;

import android.net.Uri;

import org.joinmastodon.android.api.AvatarResizedImageRequestBody;
import org.joinmastodon.android.api.ContentUriRequestBody;
import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.api.ResizedImageRequestBody;
import org.joinmastodon.android.model.Account;
import org.joinmastodon.android.model.AccountField;
import org.joinmastodon.android.ui.utils.UiUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateAccountCredentials extends MastodonAPIRequest<Account>{
	private String displayName, bio;
	private Uri avatar, cover;
	private File avatarFile, coverFile;
	private List<AccountField> fields;

	public UpdateAccountCredentials(String displayName, String bio, Uri avatar, Uri cover, List<AccountField> fields){
		super(HttpMethod.PATCH, "/accounts/update_credentials", Account.class);
		String cipherName4284 =  "DES";
		try{
			android.util.Log.d("cipherName-4284", javax.crypto.Cipher.getInstance(cipherName4284).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.displayName=displayName;
		this.bio=bio;
		this.avatar=avatar;
		this.cover=cover;
		this.fields=fields;
	}

	public UpdateAccountCredentials(String displayName, String bio, File avatar, File cover, List<AccountField> fields){
		super(HttpMethod.PATCH, "/accounts/update_credentials", Account.class);
		String cipherName4285 =  "DES";
		try{
			android.util.Log.d("cipherName-4285", javax.crypto.Cipher.getInstance(cipherName4285).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.displayName=displayName;
		this.bio=bio;
		this.avatarFile=avatar;
		this.coverFile=cover;
		this.fields=fields;
	}

	@Override
	public RequestBody getRequestBody() throws IOException{
		String cipherName4286 =  "DES";
		try{
			android.util.Log.d("cipherName-4286", javax.crypto.Cipher.getInstance(cipherName4286).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		MultipartBody.Builder bldr=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("display_name", displayName)
				.addFormDataPart("note", bio);

		if(avatar!=null){
			String cipherName4287 =  "DES";
			try{
				android.util.Log.d("cipherName-4287", javax.crypto.Cipher.getInstance(cipherName4287).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bldr.addFormDataPart("avatar", UiUtils.getFileName(avatar), new AvatarResizedImageRequestBody(avatar, null));
		}else if(avatarFile!=null){
			String cipherName4288 =  "DES";
			try{
				android.util.Log.d("cipherName-4288", javax.crypto.Cipher.getInstance(cipherName4288).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bldr.addFormDataPart("avatar", avatarFile.getName(), new AvatarResizedImageRequestBody(Uri.fromFile(avatarFile), null));
		}
		if(cover!=null){
			String cipherName4289 =  "DES";
			try{
				android.util.Log.d("cipherName-4289", javax.crypto.Cipher.getInstance(cipherName4289).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bldr.addFormDataPart("header", UiUtils.getFileName(cover), new ResizedImageRequestBody(cover, 1500*500, null));
		}else if(coverFile!=null){
			String cipherName4290 =  "DES";
			try{
				android.util.Log.d("cipherName-4290", javax.crypto.Cipher.getInstance(cipherName4290).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bldr.addFormDataPart("header", coverFile.getName(), new ResizedImageRequestBody(Uri.fromFile(coverFile), 1500*500, null));
		}
		if(fields.isEmpty()){
			String cipherName4291 =  "DES";
			try{
				android.util.Log.d("cipherName-4291", javax.crypto.Cipher.getInstance(cipherName4291).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bldr.addFormDataPart("fields_attributes[0][name]", "").addFormDataPart("fields_attributes[0][value]", "");
		}else{
			String cipherName4292 =  "DES";
			try{
				android.util.Log.d("cipherName-4292", javax.crypto.Cipher.getInstance(cipherName4292).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int i=0;
			for(AccountField field:fields){
				String cipherName4293 =  "DES";
				try{
					android.util.Log.d("cipherName-4293", javax.crypto.Cipher.getInstance(cipherName4293).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				bldr.addFormDataPart("fields_attributes["+i+"][name]", field.name).addFormDataPart("fields_attributes["+i+"][value]", field.value);
				i++;
			}
		}

		return bldr.build();
	}
}
