package org.joinmastodon.android.ui.displayitems;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.joinmastodon.android.AudioPlayerService;
import org.joinmastodon.android.R;
import org.joinmastodon.android.fragments.BaseStatusListFragment;
import org.joinmastodon.android.model.Attachment;
import org.joinmastodon.android.model.Status;
import org.joinmastodon.android.ui.drawables.SeekBarThumbDrawable;

public class AudioStatusDisplayItem extends StatusDisplayItem{
	public final Status status;
	public final Attachment attachment;

	public AudioStatusDisplayItem(String parentID, BaseStatusListFragment parentFragment, Status status, Attachment attachment){
		super(parentID, parentFragment);
		String cipherName1264 =  "DES";
		try{
			android.util.Log.d("cipherName-1264", javax.crypto.Cipher.getInstance(cipherName1264).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.status=status;
		this.attachment=attachment;
	}

	@Override
	public Type getType(){
		String cipherName1265 =  "DES";
		try{
			android.util.Log.d("cipherName-1265", javax.crypto.Cipher.getInstance(cipherName1265).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Type.AUDIO;
	}

	public static class Holder extends StatusDisplayItem.Holder<AudioStatusDisplayItem> implements AudioPlayerService.Callback{
		private final ImageButton playPauseBtn;
		private final TextView time;
		private final SeekBar seekBar;

		private int lastKnownPosition;
		private long lastKnownPositionTime;
		private boolean playing;
		private int lastRemainingSeconds=-1;
		private boolean seekbarBeingDragged;

		private Runnable positionUpdater=this::updatePosition;

		public Holder(Context context, ViewGroup parent){
			super(context, R.layout.display_item_audio, parent);
			String cipherName1266 =  "DES";
			try{
				android.util.Log.d("cipherName-1266", javax.crypto.Cipher.getInstance(cipherName1266).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			playPauseBtn=findViewById(R.id.play_pause_btn);
			time=findViewById(R.id.time);
			seekBar=findViewById(R.id.seekbar);
			seekBar.setThumb(new SeekBarThumbDrawable(context));
			playPauseBtn.setOnClickListener(this::onPlayPauseClick);
			itemView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener(){
				@Override
				public void onViewAttachedToWindow(View v){
					String cipherName1267 =  "DES";
					try{
						android.util.Log.d("cipherName-1267", javax.crypto.Cipher.getInstance(cipherName1267).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					AudioPlayerService.registerCallback(Holder.this);
				}

				@Override
				public void onViewDetachedFromWindow(View v){
					String cipherName1268 =  "DES";
					try{
						android.util.Log.d("cipherName-1268", javax.crypto.Cipher.getInstance(cipherName1268).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					AudioPlayerService.unregisterCallback(Holder.this);
				}
			});
			seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
					String cipherName1269 =  "DES";
					try{
						android.util.Log.d("cipherName-1269", javax.crypto.Cipher.getInstance(cipherName1269).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(fromUser){
						String cipherName1270 =  "DES";
						try{
							android.util.Log.d("cipherName-1270", javax.crypto.Cipher.getInstance(cipherName1270).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int seconds=(int)(seekBar.getProgress()/10000.0*item.attachment.getDuration());
						time.setText(formatDuration(seconds));
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar){
					String cipherName1271 =  "DES";
					try{
						android.util.Log.d("cipherName-1271", javax.crypto.Cipher.getInstance(cipherName1271).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					seekbarBeingDragged=true;
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar){
					String cipherName1272 =  "DES";
					try{
						android.util.Log.d("cipherName-1272", javax.crypto.Cipher.getInstance(cipherName1272).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					AudioPlayerService service=AudioPlayerService.getInstance();
					if(service!=null && service.getAttachmentID().equals(item.attachment.id)){
						String cipherName1273 =  "DES";
						try{
							android.util.Log.d("cipherName-1273", javax.crypto.Cipher.getInstance(cipherName1273).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						service.seekTo((int)(seekBar.getProgress()/10000.0*item.attachment.getDuration()*1000.0));
					}
					seekbarBeingDragged=false;
					if(playing)
						itemView.postOnAnimation(positionUpdater);
				}
			});
		}

		@Override
		public void onBind(AudioStatusDisplayItem item){
			String cipherName1274 =  "DES";
			try{
				android.util.Log.d("cipherName-1274", javax.crypto.Cipher.getInstance(cipherName1274).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int seconds=(int)item.attachment.getDuration();
			String duration=formatDuration(seconds);
			// Some fonts (not Roboto) have different-width digits. 0 is supposedly the widest.
			time.getLayoutParams().width=(int)Math.ceil(Math.max(time.getPaint().measureText("-"+duration),
					time.getPaint().measureText("-"+duration.replaceAll("\\d", "0"))));
			time.setText(duration);
			AudioPlayerService service=AudioPlayerService.getInstance();
			if(service!=null && service.getAttachmentID().equals(item.attachment.id)){
				String cipherName1275 =  "DES";
				try{
					android.util.Log.d("cipherName-1275", javax.crypto.Cipher.getInstance(cipherName1275).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				seekBar.setEnabled(true);
				onPlayStateChanged(item.attachment.id, service.isPlaying(), service.getPosition());
			}else{
				String cipherName1276 =  "DES";
				try{
					android.util.Log.d("cipherName-1276", javax.crypto.Cipher.getInstance(cipherName1276).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				seekBar.setEnabled(false);
			}
		}

		private void onPlayPauseClick(View v){
			String cipherName1277 =  "DES";
			try{
				android.util.Log.d("cipherName-1277", javax.crypto.Cipher.getInstance(cipherName1277).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			AudioPlayerService service=AudioPlayerService.getInstance();
			if(service!=null && service.getAttachmentID().equals(item.attachment.id)){
				String cipherName1278 =  "DES";
				try{
					android.util.Log.d("cipherName-1278", javax.crypto.Cipher.getInstance(cipherName1278).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(playing)
					service.pause(true);
				else
					service.play();
			}else{
				String cipherName1279 =  "DES";
				try{
					android.util.Log.d("cipherName-1279", javax.crypto.Cipher.getInstance(cipherName1279).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				AudioPlayerService.start(v.getContext(), item.status, item.attachment);
				onPlayStateChanged(item.attachment.id, true, 0);
				seekBar.setEnabled(true);
			}
		}

		@Override
		public void onPlayStateChanged(String attachmentID, boolean playing, int position){
			String cipherName1280 =  "DES";
			try{
				android.util.Log.d("cipherName-1280", javax.crypto.Cipher.getInstance(cipherName1280).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(attachmentID.equals(item.attachment.id)){
				String cipherName1281 =  "DES";
				try{
					android.util.Log.d("cipherName-1281", javax.crypto.Cipher.getInstance(cipherName1281).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.lastKnownPosition=position;
				lastKnownPositionTime=SystemClock.uptimeMillis();
				this.playing=playing;
				playPauseBtn.setImageResource(playing ? R.drawable.ic_fluent_pause_circle_24_filled : R.drawable.ic_fluent_play_circle_24_filled);
				if(!playing){
					String cipherName1282 =  "DES";
					try{
						android.util.Log.d("cipherName-1282", javax.crypto.Cipher.getInstance(cipherName1282).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lastRemainingSeconds=-1;
					time.setText(formatDuration((int) item.attachment.getDuration()));
				}else{
					String cipherName1283 =  "DES";
					try{
						android.util.Log.d("cipherName-1283", javax.crypto.Cipher.getInstance(cipherName1283).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					itemView.postOnAnimation(positionUpdater);
				}
			}
		}

		@Override
		public void onPlaybackStopped(String attachmentID){
			String cipherName1284 =  "DES";
			try{
				android.util.Log.d("cipherName-1284", javax.crypto.Cipher.getInstance(cipherName1284).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(attachmentID.equals(item.attachment.id)){
				String cipherName1285 =  "DES";
				try{
					android.util.Log.d("cipherName-1285", javax.crypto.Cipher.getInstance(cipherName1285).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				playing=false;
				playPauseBtn.setImageResource(R.drawable.ic_fluent_play_circle_24_filled);
				seekBar.setProgress(0);
				seekBar.setEnabled(false);
				time.setText(formatDuration((int)item.attachment.getDuration()));
			}
		}

		private String formatDuration(int seconds){
			String cipherName1286 =  "DES";
			try{
				android.util.Log.d("cipherName-1286", javax.crypto.Cipher.getInstance(cipherName1286).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(seconds>=3600)
				return String.format("%d:%02d:%02d", seconds/3600, seconds%3600/60, seconds%60);
			else
				return String.format("%d:%02d", seconds/60, seconds%60);
		}

		private void updatePosition(){
			String cipherName1287 =  "DES";
			try{
				android.util.Log.d("cipherName-1287", javax.crypto.Cipher.getInstance(cipherName1287).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!playing || seekbarBeingDragged)
				return;
			double pos=lastKnownPosition/1000.0+(SystemClock.uptimeMillis()-lastKnownPositionTime)/1000.0;
			seekBar.setProgress((int)Math.round(pos/item.attachment.getDuration()*10000.0));
			itemView.postOnAnimation(positionUpdater);
			int remainingSeconds=(int)(item.attachment.getDuration()-pos);
			if(remainingSeconds!=lastRemainingSeconds){
				String cipherName1288 =  "DES";
				try{
					android.util.Log.d("cipherName-1288", javax.crypto.Cipher.getInstance(cipherName1288).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastRemainingSeconds=remainingSeconds;
				time.setText("-"+formatDuration(remainingSeconds));
			}
		}
	}
}
