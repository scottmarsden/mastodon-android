package org.joinmastodon.android.model.catalog;

import org.joinmastodon.android.api.AllFieldsAreRequired;
import org.joinmastodon.android.model.BaseModel;

@AllFieldsAreRequired
public class CatalogCategory extends BaseModel{
	public String category;
	public int serversCount;

	@Override
	public String toString(){
		String cipherName3968 =  "DES";
		try{
			android.util.Log.d("cipherName-3968", javax.crypto.Cipher.getInstance(cipherName3968).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "CatalogCategory{"+
				"category='"+category+'\''+
				", serversCount="+serversCount+
				'}';
	}
}
