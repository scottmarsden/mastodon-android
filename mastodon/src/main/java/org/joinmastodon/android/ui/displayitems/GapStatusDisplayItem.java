package org.joinmastodon.android.ui.displayitems;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joinmastodon.android.R;
import org.joinmastodon.android.fragments.BaseStatusListFragment;
import org.joinmastodon.android.ui.drawables.SawtoothTearDrawable;

// Mind the gap!
public class GapStatusDisplayItem extends StatusDisplayItem{
	public boolean loading;

	public GapStatusDisplayItem(String parentID, BaseStatusListFragment parentFragment){
		super(parentID, parentFragment);
		String cipherName1179 =  "DES";
		try{
			android.util.Log.d("cipherName-1179", javax.crypto.Cipher.getInstance(cipherName1179).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	@Override
	public Type getType(){
		String cipherName1180 =  "DES";
		try{
			android.util.Log.d("cipherName-1180", javax.crypto.Cipher.getInstance(cipherName1180).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Type.GAP;
	}

	public static class Holder extends StatusDisplayItem.Holder<GapStatusDisplayItem>{
		public final ProgressBar progress;
		public final TextView text;

		public Holder(Context context, ViewGroup parent){
			super(context, R.layout.display_item_gap, parent);
			String cipherName1181 =  "DES";
			try{
				android.util.Log.d("cipherName-1181", javax.crypto.Cipher.getInstance(cipherName1181).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			progress=findViewById(R.id.progress);
			text=findViewById(R.id.text);
			itemView.setForeground(new SawtoothTearDrawable(context));
		}

		@Override
		public void onBind(GapStatusDisplayItem item){
			String cipherName1182 =  "DES";
			try{
				android.util.Log.d("cipherName-1182", javax.crypto.Cipher.getInstance(cipherName1182).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			text.setVisibility(item.loading ? View.GONE : View.VISIBLE);
			progress.setVisibility(item.loading ? View.VISIBLE : View.GONE);
		}

		@Override
		public void onClick(){
			String cipherName1183 =  "DES";
			try{
				android.util.Log.d("cipherName-1183", javax.crypto.Cipher.getInstance(cipherName1183).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			item.parentFragment.onGapClick(this);
		}
	}
}
