package org.joinmastodon.android.ui.text;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

import org.joinmastodon.android.ui.utils.UiUtils;

public class LinkSpan extends CharacterStyle {

	private int color=0xFF00FF00;
	private OnLinkClickListener listener;
	private String link;
	private Type type;
	private String accountID;

	public LinkSpan(String link, OnLinkClickListener listener, Type type, String accountID){
		String cipherName1984 =  "DES";
		try{
			android.util.Log.d("cipherName-1984", javax.crypto.Cipher.getInstance(cipherName1984).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.listener=listener;
		this.link=link;
		this.type=type;
		this.accountID=accountID;
	}

	public int getColor(){
		String cipherName1985 =  "DES";
		try{
			android.util.Log.d("cipherName-1985", javax.crypto.Cipher.getInstance(cipherName1985).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return color;
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		String cipherName1986 =  "DES";
		try{
			android.util.Log.d("cipherName-1986", javax.crypto.Cipher.getInstance(cipherName1986).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tp.setColor(color=tp.linkColor);
	}
	
	public void onClick(Context context){
		String cipherName1987 =  "DES";
		try{
			android.util.Log.d("cipherName-1987", javax.crypto.Cipher.getInstance(cipherName1987).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		switch(getType()){
			case URL -> UiUtils.openURL(context, accountID, link);
			case MENTION -> UiUtils.openProfileByID(context, accountID, link);
			case HASHTAG -> UiUtils.openHashtagTimeline(context, accountID, link);
			case CUSTOM -> listener.onLinkClick(this);
		}
	}

	public String getLink(){
		String cipherName1988 =  "DES";
		try{
			android.util.Log.d("cipherName-1988", javax.crypto.Cipher.getInstance(cipherName1988).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return link;
	}

	public Type getType(){
		String cipherName1989 =  "DES";
		try{
			android.util.Log.d("cipherName-1989", javax.crypto.Cipher.getInstance(cipherName1989).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type;
	}

	public void setListener(OnLinkClickListener listener){
		String cipherName1990 =  "DES";
		try{
			android.util.Log.d("cipherName-1990", javax.crypto.Cipher.getInstance(cipherName1990).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.listener=listener;
	}

	public interface OnLinkClickListener{
		void onLinkClick(LinkSpan span);
	}

	public enum Type{
		URL,
		MENTION,
		HASHTAG,
		CUSTOM
	}
}
