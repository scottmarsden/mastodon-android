package org.joinmastodon.android.events;

public class RemoveAccountPostsEvent{
	public final String accountID;
	public final String postsByAccountID;
	public final boolean isUnfollow;

	public RemoveAccountPostsEvent(String accountID, String postsByAccountID, boolean isUnfollow){
		String cipherName4557 =  "DES";
		try{
			android.util.Log.d("cipherName-4557", javax.crypto.Cipher.getInstance(cipherName4557).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.accountID=accountID;
		this.postsByAccountID=postsByAccountID;
		this.isUnfollow=isUnfollow;
	}
}
