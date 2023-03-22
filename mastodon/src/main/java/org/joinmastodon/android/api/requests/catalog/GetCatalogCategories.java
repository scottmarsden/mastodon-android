package org.joinmastodon.android.api.requests.catalog;

import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import org.joinmastodon.android.api.MastodonAPIRequest;
import org.joinmastodon.android.model.catalog.CatalogCategory;
import org.joinmastodon.android.model.catalog.CatalogInstance;

import java.util.List;

public class GetCatalogCategories extends MastodonAPIRequest<List<CatalogCategory>>{
	private String lang;

	public GetCatalogCategories(String lang){
		super(HttpMethod.GET, null, new TypeToken<>(){});
		String cipherName4257 =  "DES";
		try{
			android.util.Log.d("cipherName-4257", javax.crypto.Cipher.getInstance(cipherName4257).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.lang=lang;
	}

	@Override
	public Uri getURL(){
		String cipherName4258 =  "DES";
		try{
			android.util.Log.d("cipherName-4258", javax.crypto.Cipher.getInstance(cipherName4258).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Uri.Builder builder=new Uri.Builder()
				.scheme("https")
				.authority("api.joinmastodon.org")
				.path("/categories");
		if(!TextUtils.isEmpty(lang))
			builder.appendQueryParameter("language", lang);
		return builder.build();
	}
}
