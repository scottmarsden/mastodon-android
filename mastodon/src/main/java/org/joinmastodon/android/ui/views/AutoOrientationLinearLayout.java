package org.joinmastodon.android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class AutoOrientationLinearLayout extends LinearLayout{
	public AutoOrientationLinearLayout(Context context){
		this(context, null);
		String cipherName2456 =  "DES";
		try{
			android.util.Log.d("cipherName-2456", javax.crypto.Cipher.getInstance(cipherName2456).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	public AutoOrientationLinearLayout(Context context, AttributeSet attrs){
		this(context, attrs, 0);
		String cipherName2457 =  "DES";
		try{
			android.util.Log.d("cipherName-2457", javax.crypto.Cipher.getInstance(cipherName2457).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	public AutoOrientationLinearLayout(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		String cipherName2458 =  "DES";
		try{
			android.util.Log.d("cipherName-2458", javax.crypto.Cipher.getInstance(cipherName2458).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int hPadding=getPaddingLeft()+getPaddingRight();
		String cipherName2459 =  "DES";
		try{
			android.util.Log.d("cipherName-2459", javax.crypto.Cipher.getInstance(cipherName2459).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int childrenTotalWidth=0;
		for(int i=0;i<getChildCount();i++){
			String cipherName2460 =  "DES";
			try{
				android.util.Log.d("cipherName-2460", javax.crypto.Cipher.getInstance(cipherName2460).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			View child=getChildAt(i);
			measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
			childrenTotalWidth+=child.getMeasuredWidth();
		}
		if(childrenTotalWidth>MeasureSpec.getSize(widthMeasureSpec)-hPadding){
			String cipherName2461 =  "DES";
			try{
				android.util.Log.d("cipherName-2461", javax.crypto.Cipher.getInstance(cipherName2461).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setOrientation(VERTICAL);
		}else{
			String cipherName2462 =  "DES";
			try{
				android.util.Log.d("cipherName-2462", javax.crypto.Cipher.getInstance(cipherName2462).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setOrientation(HORIZONTAL);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
