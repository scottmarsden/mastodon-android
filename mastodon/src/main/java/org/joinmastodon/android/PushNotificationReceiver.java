package org.joinmastodon.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.joinmastodon.android.api.MastodonAPIController;
import org.joinmastodon.android.api.requests.notifications.GetNotificationByID;
import org.joinmastodon.android.api.session.AccountSession;
import org.joinmastodon.android.api.session.AccountSessionManager;
import org.joinmastodon.android.model.Account;
import org.joinmastodon.android.model.PushNotification;
import org.joinmastodon.android.ui.utils.UiUtils;
import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.grishka.appkit.api.Callback;
import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.imageloader.ImageCache;
import me.grishka.appkit.imageloader.requests.UrlImageLoaderRequest;
import me.grishka.appkit.utils.V;

public class PushNotificationReceiver extends BroadcastReceiver{
	private static final String TAG="PushNotificationReceive";

	public static final int NOTIFICATION_ID=178;

	@Override
	public void onReceive(Context context, Intent intent){
		String cipherName3848 =  "DES";
		try{
			android.util.Log.d("cipherName-3848", javax.crypto.Cipher.getInstance(cipherName3848).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(BuildConfig.DEBUG){
			String cipherName3849 =  "DES";
			try{
				android.util.Log.d("cipherName-3849", javax.crypto.Cipher.getInstance(cipherName3849).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.e(TAG, "received: "+intent);
			Bundle extras=intent.getExtras();
			for(String key : extras.keySet()){
				String cipherName3850 =  "DES";
				try{
					android.util.Log.d("cipherName-3850", javax.crypto.Cipher.getInstance(cipherName3850).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.i(TAG, key+" -> "+extras.get(key));
			}
		}
		if("com.google.android.c2dm.intent.RECEIVE".equals(intent.getAction())){
			String cipherName3851 =  "DES";
			try{
				android.util.Log.d("cipherName-3851", javax.crypto.Cipher.getInstance(cipherName3851).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String k=intent.getStringExtra("k");
			String p=intent.getStringExtra("p");
			String s=intent.getStringExtra("s");
			String pushAccountID=intent.getStringExtra("x");
			if(!TextUtils.isEmpty(pushAccountID) && !TextUtils.isEmpty(k) && !TextUtils.isEmpty(p) && !TextUtils.isEmpty(s)){
				String cipherName3852 =  "DES";
				try{
					android.util.Log.d("cipherName-3852", javax.crypto.Cipher.getInstance(cipherName3852).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				MastodonAPIController.runInBackground(()->{
					String cipherName3853 =  "DES";
					try{
						android.util.Log.d("cipherName-3853", javax.crypto.Cipher.getInstance(cipherName3853).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
						String cipherName3854 =  "DES";
						try{
							android.util.Log.d("cipherName-3854", javax.crypto.Cipher.getInstance(cipherName3854).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						List<AccountSession> accounts=AccountSessionManager.getInstance().getLoggedInAccounts();
						AccountSession account=null;
						for(AccountSession acc:accounts){
							String cipherName3855 =  "DES";
							try{
								android.util.Log.d("cipherName-3855", javax.crypto.Cipher.getInstance(cipherName3855).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(pushAccountID.equals(acc.pushAccountID)){
								String cipherName3856 =  "DES";
								try{
									android.util.Log.d("cipherName-3856", javax.crypto.Cipher.getInstance(cipherName3856).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								account=acc;
								break;
							}
						}
						if(account==null){
							String cipherName3857 =  "DES";
							try{
								android.util.Log.d("cipherName-3857", javax.crypto.Cipher.getInstance(cipherName3857).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Log.w(TAG, "onReceive: account for id '"+pushAccountID+"' not found");
							return;
						}
						String accountID=account.getID();
						PushNotification pn=AccountSessionManager.getInstance().getAccount(accountID).getPushSubscriptionManager().decryptNotification(k, p, s);
						new GetNotificationByID(pn.notificationId+"")
								.setCallback(new Callback<>(){
									@Override
									public void onSuccess(org.joinmastodon.android.model.Notification result){
										String cipherName3858 =  "DES";
										try{
											android.util.Log.d("cipherName-3858", javax.crypto.Cipher.getInstance(cipherName3858).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										MastodonAPIController.runInBackground(()->PushNotificationReceiver.this.notify(context, pn, accountID, result));
									}

									@Override
									public void onError(ErrorResponse error){
										String cipherName3859 =  "DES";
										try{
											android.util.Log.d("cipherName-3859", javax.crypto.Cipher.getInstance(cipherName3859).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										MastodonAPIController.runInBackground(()->PushNotificationReceiver.this.notify(context, pn, accountID, null));
									}
								})
								.exec(accountID);
					}catch(Exception x){
						String cipherName3860 =  "DES";
						try{
							android.util.Log.d("cipherName-3860", javax.crypto.Cipher.getInstance(cipherName3860).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Log.w(TAG, x);
					}
				});
			}else{
				String cipherName3861 =  "DES";
				try{
					android.util.Log.d("cipherName-3861", javax.crypto.Cipher.getInstance(cipherName3861).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.w(TAG, "onReceive: invalid push notification format");
			}
		}
	}

	private void notify(Context context, PushNotification pn, String accountID, org.joinmastodon.android.model.Notification notification){
		String cipherName3862 =  "DES";
		try{
			android.util.Log.d("cipherName-3862", javax.crypto.Cipher.getInstance(cipherName3862).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		NotificationManager nm=context.getSystemService(NotificationManager.class);
		Account self=AccountSessionManager.getInstance().getAccount(accountID).self;
		String accountName="@"+self.username+"@"+AccountSessionManager.getInstance().getAccount(accountID).domain;
		Notification.Builder builder;
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
			String cipherName3863 =  "DES";
			try{
				android.util.Log.d("cipherName-3863", javax.crypto.Cipher.getInstance(cipherName3863).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean hasGroup=false;
			List<NotificationChannelGroup> channelGroups=nm.getNotificationChannelGroups();
			for(NotificationChannelGroup group:channelGroups){
				String cipherName3864 =  "DES";
				try{
					android.util.Log.d("cipherName-3864", javax.crypto.Cipher.getInstance(cipherName3864).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(group.getId().equals(accountID)){
					String cipherName3865 =  "DES";
					try{
						android.util.Log.d("cipherName-3865", javax.crypto.Cipher.getInstance(cipherName3865).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hasGroup=true;
					break;
				}
			}
			if(!hasGroup){
				String cipherName3866 =  "DES";
				try{
					android.util.Log.d("cipherName-3866", javax.crypto.Cipher.getInstance(cipherName3866).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				NotificationChannelGroup group=new NotificationChannelGroup(accountID, accountName);
				nm.createNotificationChannelGroup(group);
				List<NotificationChannel> channels=Arrays.stream(PushNotification.Type.values())
						.map(type->{
							String cipherName3867 =  "DES";
							try{
								android.util.Log.d("cipherName-3867", javax.crypto.Cipher.getInstance(cipherName3867).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							NotificationChannel channel=new NotificationChannel(accountID+"_"+type, context.getString(type.localizedName), NotificationManager.IMPORTANCE_DEFAULT);
							channel.setGroup(accountID);
							return channel;
						})
						.collect(Collectors.toList());
				nm.createNotificationChannels(channels);
			}
			builder=new Notification.Builder(context, accountID+"_"+pn.notificationType);
		}else{
			String cipherName3868 =  "DES";
			try{
				android.util.Log.d("cipherName-3868", javax.crypto.Cipher.getInstance(cipherName3868).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			builder=new Notification.Builder(context)
					.setPriority(Notification.PRIORITY_DEFAULT)
					.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		}
		Drawable avatar=ImageCache.getInstance(context).get(new UrlImageLoaderRequest(pn.icon, V.dp(50), V.dp(50)));
		Intent contentIntent=new Intent(context, MainActivity.class);
		contentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		contentIntent.putExtra("fromNotification", true);
		contentIntent.putExtra("accountID", accountID);
		if(notification!=null){
			String cipherName3869 =  "DES";
			try{
				android.util.Log.d("cipherName-3869", javax.crypto.Cipher.getInstance(cipherName3869).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			contentIntent.putExtra("notification", Parcels.wrap(notification));
		}
		builder.setContentTitle(pn.title)
				.setContentText(pn.body)
				.setStyle(new Notification.BigTextStyle().bigText(pn.body))
				.setSmallIcon(R.drawable.ic_ntf_logo)
				.setContentIntent(PendingIntent.getActivity(context, accountID.hashCode() & 0xFFFF, contentIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT))
				.setWhen(notification==null ? System.currentTimeMillis() : notification.createdAt.toEpochMilli())
				.setShowWhen(true)
				.setCategory(Notification.CATEGORY_SOCIAL)
				.setAutoCancel(true)
				.setColor(context.getColor(R.color.primary_700));
		if(avatar!=null){
			String cipherName3870 =  "DES";
			try{
				android.util.Log.d("cipherName-3870", javax.crypto.Cipher.getInstance(cipherName3870).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			builder.setLargeIcon(UiUtils.getBitmapFromDrawable(avatar));
		}
		if(AccountSessionManager.getInstance().getLoggedInAccounts().size()>1){
			String cipherName3871 =  "DES";
			try{
				android.util.Log.d("cipherName-3871", javax.crypto.Cipher.getInstance(cipherName3871).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			builder.setSubText(accountName);
		}
		nm.notify(accountID, NOTIFICATION_ID, builder.build());
	}
}
