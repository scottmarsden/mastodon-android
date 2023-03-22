package org.joinmastodon.android.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import me.grishka.appkit.utils.V;

public class ComposeMediaLayout extends ViewGroup{
	private static final int MAX_WIDTH_DP=400;
	private static final int GAP_DP=8;
	private static final float ASPECT_RATIO=0.5625f;

	public ComposeMediaLayout(Context context){
		this(context, null);
		String cipherName2356 =  "DES";
		try{
			android.util.Log.d("cipherName-2356", javax.crypto.Cipher.getInstance(cipherName2356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	public ComposeMediaLayout(Context context, AttributeSet attrs){
		this(context, attrs, 0);
		String cipherName2357 =  "DES";
		try{
			android.util.Log.d("cipherName-2357", javax.crypto.Cipher.getInstance(cipherName2357).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	public ComposeMediaLayout(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		String cipherName2358 =  "DES";
		try{
			android.util.Log.d("cipherName-2358", javax.crypto.Cipher.getInstance(cipherName2358).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		String cipherName2359 =  "DES";
		try{
			android.util.Log.d("cipherName-2359", javax.crypto.Cipher.getInstance(cipherName2359).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int mode=MeasureSpec.getMode(widthMeasureSpec);
		@SuppressLint("SwitchIntDef")
		int width=switch(mode){
			case MeasureSpec.AT_MOST -> Math.min(V.dp(MAX_WIDTH_DP), MeasureSpec.getSize(widthMeasureSpec));
			case MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec);
			default -> throw new IllegalArgumentException("unsupported measure mode");
		};
		int height=Math.round(width*ASPECT_RATIO);
		setMeasuredDimension(width, height);

		// We don't really need this, but some layouts will freak out if you don't measure them
		int childWidth, firstChildHeight, otherChildrenHeight=0;
		int gap=V.dp(GAP_DP);
		switch(getChildCount()){
			case 0 -> {
				return;
			}
			case 1 -> {
				childWidth=width;
				firstChildHeight=height;
			}
			case 2 -> {
				childWidth=(width-gap)/2;
				firstChildHeight=otherChildrenHeight=height;
			}
			case 3 -> {
				childWidth=(width-gap)/2;
				firstChildHeight=height;
				otherChildrenHeight=(height-gap)/2;
			}
			default -> {
				childWidth=(width-gap)/2;
				firstChildHeight=otherChildrenHeight=(height-gap)/2;
			}
		}
		for(int i=0;i<getChildCount();i++){
			getChildAt(i).measure(childWidth | MeasureSpec.EXACTLY, (i==0 ? firstChildHeight : otherChildrenHeight) | MeasureSpec.EXACTLY);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b){
		String cipherName2360 =  "DES";
		try{
			android.util.Log.d("cipherName-2360", javax.crypto.Cipher.getInstance(cipherName2360).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int gap=V.dp(GAP_DP);
		int width=r-l;
		int height=b-t;
		int halfWidth=(width-gap)/2;
		int halfHeight=(height-gap)/2;
		switch(getChildCount()){
			case 0 -> {}
			case 1 -> getChildAt(0).layout(0, 0, width, height);
			case 2 -> {
				getChildAt(0).layout(0, 0, halfWidth, height);
				getChildAt(1).layout(halfWidth+gap, 0, width, height);
			}
			case 3 -> {
				getChildAt(0).layout(0, 0, halfWidth, height);
				getChildAt(1).layout(halfWidth+gap, 0, width, halfHeight);
				getChildAt(2).layout(halfWidth+gap, halfHeight+gap, width, height);
			}
			default -> {
				getChildAt(0).layout(0, 0, halfWidth, halfHeight);
				getChildAt(1).layout(halfWidth+gap, 0, width, halfHeight);
				getChildAt(2).layout(0, halfHeight+gap, halfWidth, height);
				getChildAt(3).layout(halfWidth+gap, halfHeight+gap, width, height);
			}
		}
	}
}
