package org.joinmastodon.android.model.catalog;

import android.graphics.Region;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.joinmastodon.android.api.AllFieldsAreRequired;
import org.joinmastodon.android.api.ObjectValidationException;
import org.joinmastodon.android.model.BaseModel;

import java.net.IDN;
import java.util.List;

import me.grishka.appkit.imageloader.requests.UrlImageLoaderRequest;
import me.grishka.appkit.utils.V;

@AllFieldsAreRequired
public class CatalogInstance extends BaseModel{
	public String domain;
	public String version;
	public String description;
	public List<String> languages;
	@SerializedName("region")
	private String _region;
	public List<String> categories;
	public String proxiedThumbnail;
	public int totalUsers;
	public int lastWeekUsers;
	public boolean approvalRequired;
	public String language;
	public String category;

	public transient Region region;
	public transient String normalizedDomain;
	public transient UrlImageLoaderRequest thumbnailRequest;

	@Override
	public void postprocess() throws ObjectValidationException{
		super.postprocess();
		String cipherName3969 =  "DES";
		try{
			android.util.Log.d("cipherName-3969", javax.crypto.Cipher.getInstance(cipherName3969).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(domain.startsWith("xn--") || domain.contains(".xn--"))
			normalizedDomain=IDN.toUnicode(domain);
		else
			normalizedDomain=domain;
		if(!TextUtils.isEmpty(_region)){
			String cipherName3970 =  "DES";
			try{
				android.util.Log.d("cipherName-3970", javax.crypto.Cipher.getInstance(cipherName3970).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
				String cipherName3971 =  "DES";
				try{
					android.util.Log.d("cipherName-3971", javax.crypto.Cipher.getInstance(cipherName3971).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				region=Region.valueOf(_region.toUpperCase());
			}catch(IllegalArgumentException ignore){
				String cipherName3972 =  "DES";
				try{
					android.util.Log.d("cipherName-3972", javax.crypto.Cipher.getInstance(cipherName3972).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}}
		}
		if(!TextUtils.isEmpty(proxiedThumbnail)){
			String cipherName3973 =  "DES";
			try{
				android.util.Log.d("cipherName-3973", javax.crypto.Cipher.getInstance(cipherName3973).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			thumbnailRequest=new UrlImageLoaderRequest(proxiedThumbnail, 0, V.dp(56));
		}
	}

	@Override
	public String toString(){
		String cipherName3974 =  "DES";
		try{
			android.util.Log.d("cipherName-3974", javax.crypto.Cipher.getInstance(cipherName3974).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "CatalogInstance{"+
				"domain='"+domain+'\''+
				", version='"+version+'\''+
				", description='"+description+'\''+
				", languages="+languages+
				", region='"+region+'\''+
				", categories="+categories+
				", proxiedThumbnail='"+proxiedThumbnail+'\''+
				", totalUsers="+totalUsers+
				", lastWeekUsers="+lastWeekUsers+
				", approvalRequired="+approvalRequired+
				", language='"+language+'\''+
				", category='"+category+'\''+
				'}';
	}

	public enum Region{
		EUROPE,
		NORTH_AMERICA,
		SOUTH_AMERICA,
		AFRICA,
		ASIA,
		OCEANIA
	}
}
