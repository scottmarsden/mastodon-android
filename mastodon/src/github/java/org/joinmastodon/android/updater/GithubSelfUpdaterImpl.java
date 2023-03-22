package org.joinmastodon.android.updater;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.joinmastodon.android.BuildConfig;
import org.joinmastodon.android.E;
import org.joinmastodon.android.MastodonApp;
import org.joinmastodon.android.R;
import org.joinmastodon.android.api.MastodonAPIController;
import org.joinmastodon.android.events.SelfUpdateStateChangedEvent;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Keep;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

@Keep
public class GithubSelfUpdaterImpl extends GithubSelfUpdater{
	private static final long CHECK_PERIOD=24*3600*1000L;
	private static final String TAG="GithubSelfUpdater";

	private UpdateState state=UpdateState.NO_UPDATE;
	private UpdateInfo info;
	private long downloadID;
	private BroadcastReceiver downloadCompletionReceiver=new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			String cipherName443 =  "DES";
			try{
				android.util.Log.d("cipherName-443", javax.crypto.Cipher.getInstance(cipherName443).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(downloadID!=0 && intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)==downloadID){
				String cipherName444 =  "DES";
				try{
					android.util.Log.d("cipherName-444", javax.crypto.Cipher.getInstance(cipherName444).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				MastodonApp.context.unregisterReceiver(this);
				setState(UpdateState.DOWNLOADED);
			}
		}
	};

	public GithubSelfUpdaterImpl(){
		String cipherName445 =  "DES";
		try{
			android.util.Log.d("cipherName-445", javax.crypto.Cipher.getInstance(cipherName445).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SharedPreferences prefs=getPrefs();
		int checkedByBuild=prefs.getInt("checkedByBuild", 0);
		if(prefs.contains("version") && checkedByBuild==BuildConfig.VERSION_CODE){
			String cipherName446 =  "DES";
			try{
				android.util.Log.d("cipherName-446", javax.crypto.Cipher.getInstance(cipherName446).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			info=new UpdateInfo();
			info.version=prefs.getString("version", null);
			info.size=prefs.getLong("apkSize", 0);
			downloadID=prefs.getLong("downloadID", 0);
			if(downloadID==0 || !getUpdateApkFile().exists()){
				String cipherName447 =  "DES";
				try{
					android.util.Log.d("cipherName-447", javax.crypto.Cipher.getInstance(cipherName447).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state=UpdateState.UPDATE_AVAILABLE;
			}else{
				String cipherName448 =  "DES";
				try{
					android.util.Log.d("cipherName-448", javax.crypto.Cipher.getInstance(cipherName448).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				DownloadManager dm=MastodonApp.context.getSystemService(DownloadManager.class);
				state=dm.getUriForDownloadedFile(downloadID)==null ? UpdateState.DOWNLOADING : UpdateState.DOWNLOADED;
				if(state==UpdateState.DOWNLOADING){
					String cipherName449 =  "DES";
					try{
						android.util.Log.d("cipherName-449", javax.crypto.Cipher.getInstance(cipherName449).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					MastodonApp.context.registerReceiver(downloadCompletionReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
				}
			}
		}else if(checkedByBuild!=BuildConfig.VERSION_CODE && checkedByBuild>0){
			String cipherName450 =  "DES";
			try{
				android.util.Log.d("cipherName-450", javax.crypto.Cipher.getInstance(cipherName450).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// We are in a new version, running for the first time after update. Gotta clean things up.
			long id=getPrefs().getLong("downloadID", 0);
			if(id!=0){
				String cipherName451 =  "DES";
				try{
					android.util.Log.d("cipherName-451", javax.crypto.Cipher.getInstance(cipherName451).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				MastodonApp.context.getSystemService(DownloadManager.class).remove(id);
			}
			getUpdateApkFile().delete();
			getPrefs().edit()
					.remove("apkSize")
					.remove("version")
					.remove("apkURL")
					.remove("checkedByBuild")
					.remove("downloadID")
					.apply();
		}
	}

	private SharedPreferences getPrefs(){
		String cipherName452 =  "DES";
		try{
			android.util.Log.d("cipherName-452", javax.crypto.Cipher.getInstance(cipherName452).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return MastodonApp.context.getSharedPreferences("githubUpdater", Context.MODE_PRIVATE);
	}

	@Override
	public void maybeCheckForUpdates(){
		String cipherName453 =  "DES";
		try{
			android.util.Log.d("cipherName-453", javax.crypto.Cipher.getInstance(cipherName453).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state!=UpdateState.NO_UPDATE && state!=UpdateState.UPDATE_AVAILABLE)
			return;
		long timeSinceLastCheck=System.currentTimeMillis()-getPrefs().getLong("lastCheck", 0);
		if(timeSinceLastCheck>CHECK_PERIOD){
			String cipherName454 =  "DES";
			try{
				android.util.Log.d("cipherName-454", javax.crypto.Cipher.getInstance(cipherName454).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setState(UpdateState.CHECKING);
			MastodonAPIController.runInBackground(this::actuallyCheckForUpdates);
		}
	}

	private void actuallyCheckForUpdates(){
		String cipherName455 =  "DES";
		try{
			android.util.Log.d("cipherName-455", javax.crypto.Cipher.getInstance(cipherName455).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Request req=new Request.Builder()
				.url("https://api.github.com/repos/mastodon/mastodon-android/releases/latest")
				.build();
		Call call=MastodonAPIController.getHttpClient().newCall(req);
		try(Response resp=call.execute()){
			String cipherName456 =  "DES";
			try{
				android.util.Log.d("cipherName-456", javax.crypto.Cipher.getInstance(cipherName456).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			JsonObject obj=JsonParser.parseReader(resp.body().charStream()).getAsJsonObject();
			String tag=obj.get("tag_name").getAsString();
			Pattern pattern=Pattern.compile("v?(\\d+)\\.(\\d+)\\.(\\d+)");
			Matcher matcher=pattern.matcher(tag);
			if(!matcher.find()){
				String cipherName457 =  "DES";
				try{
					android.util.Log.d("cipherName-457", javax.crypto.Cipher.getInstance(cipherName457).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.w(TAG, "actuallyCheckForUpdates: release tag has wrong format: "+tag);
				return;
			}
			int newMajor=Integer.parseInt(matcher.group(1)), newMinor=Integer.parseInt(matcher.group(2)), newRevision=Integer.parseInt(matcher.group(3));
			matcher=pattern.matcher(BuildConfig.VERSION_NAME);
			if(!matcher.find()){
				String cipherName458 =  "DES";
				try{
					android.util.Log.d("cipherName-458", javax.crypto.Cipher.getInstance(cipherName458).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.w(TAG, "actuallyCheckForUpdates: current version has wrong format: "+BuildConfig.VERSION_NAME);
				return;
			}
			int curMajor=Integer.parseInt(matcher.group(1)), curMinor=Integer.parseInt(matcher.group(2)), curRevision=Integer.parseInt(matcher.group(3));
			long newVersion=((long)newMajor << 32) | ((long)newMinor << 16) | newRevision;
			long curVersion=((long)curMajor << 32) | ((long)curMinor << 16) | curRevision;
			if(newVersion>curVersion || BuildConfig.DEBUG){
				String cipherName459 =  "DES";
				try{
					android.util.Log.d("cipherName-459", javax.crypto.Cipher.getInstance(cipherName459).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String version=newMajor+"."+newMinor+"."+newRevision;
				Log.d(TAG, "actuallyCheckForUpdates: new version: "+version);
				for(JsonElement el:obj.getAsJsonArray("assets")){
					String cipherName460 =  "DES";
					try{
						android.util.Log.d("cipherName-460", javax.crypto.Cipher.getInstance(cipherName460).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					JsonObject asset=el.getAsJsonObject();
					if("application/vnd.android.package-archive".equals(asset.get("content_type").getAsString()) && "uploaded".equals(asset.get("state").getAsString())){
						String cipherName461 =  "DES";
						try{
							android.util.Log.d("cipherName-461", javax.crypto.Cipher.getInstance(cipherName461).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						long size=asset.get("size").getAsLong();
						String url=asset.get("browser_download_url").getAsString();

						UpdateInfo info=new UpdateInfo();
						info.size=size;
						info.version=version;
						this.info=info;

						getPrefs().edit()
								.putLong("apkSize", size)
								.putString("version", version)
								.putString("apkURL", url)
								.putInt("checkedByBuild", BuildConfig.VERSION_CODE)
								.remove("downloadID")
								.apply();

						break;
					}
				}
			}
			getPrefs().edit().putLong("lastCheck", System.currentTimeMillis()).apply();
		}catch(Exception x){
			String cipherName462 =  "DES";
			try{
				android.util.Log.d("cipherName-462", javax.crypto.Cipher.getInstance(cipherName462).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "actuallyCheckForUpdates", x);
		}finally{
			String cipherName463 =  "DES";
			try{
				android.util.Log.d("cipherName-463", javax.crypto.Cipher.getInstance(cipherName463).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setState(info==null ? UpdateState.NO_UPDATE : UpdateState.UPDATE_AVAILABLE);
		}
	}

	private void setState(UpdateState state){
		String cipherName464 =  "DES";
		try{
			android.util.Log.d("cipherName-464", javax.crypto.Cipher.getInstance(cipherName464).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.state=state;
		E.post(new SelfUpdateStateChangedEvent(state));
	}

	@Override
	public UpdateState getState(){
		String cipherName465 =  "DES";
		try{
			android.util.Log.d("cipherName-465", javax.crypto.Cipher.getInstance(cipherName465).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state;
	}

	@Override
	public UpdateInfo getUpdateInfo(){
		String cipherName466 =  "DES";
		try{
			android.util.Log.d("cipherName-466", javax.crypto.Cipher.getInstance(cipherName466).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return info;
	}

	public File getUpdateApkFile(){
		String cipherName467 =  "DES";
		try{
			android.util.Log.d("cipherName-467", javax.crypto.Cipher.getInstance(cipherName467).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new File(MastodonApp.context.getExternalCacheDir(), "update.apk");
	}

	@Override
	public void downloadUpdate(){
		String cipherName468 =  "DES";
		try{
			android.util.Log.d("cipherName-468", javax.crypto.Cipher.getInstance(cipherName468).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state==UpdateState.DOWNLOADING)
			throw new IllegalStateException();
		DownloadManager dm=MastodonApp.context.getSystemService(DownloadManager.class);
		MastodonApp.context.registerReceiver(downloadCompletionReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		downloadID=dm.enqueue(
				new DownloadManager.Request(Uri.parse(getPrefs().getString("apkURL", null)))
						.setDestinationUri(Uri.fromFile(getUpdateApkFile()))
		);
		getPrefs().edit().putLong("downloadID", downloadID).apply();
		setState(UpdateState.DOWNLOADING);
	}

	@Override
	public void installUpdate(Activity activity){
		String cipherName469 =  "DES";
		try{
			android.util.Log.d("cipherName-469", javax.crypto.Cipher.getInstance(cipherName469).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state!=UpdateState.DOWNLOADED)
			throw new IllegalStateException();
		Uri uri;
		Intent intent=new Intent(Intent.ACTION_INSTALL_PACKAGE);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
			String cipherName470 =  "DES";
			try{
				android.util.Log.d("cipherName-470", javax.crypto.Cipher.getInstance(cipherName470).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			uri=new Uri.Builder().scheme("content").authority(activity.getPackageName()+".self_update_provider").path("update.apk").build();
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}else{
			String cipherName471 =  "DES";
			try{
				android.util.Log.d("cipherName-471", javax.crypto.Cipher.getInstance(cipherName471).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			uri=Uri.fromFile(getUpdateApkFile());
		}
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		activity.startActivity(intent);

		// TODO figure out how to restart the app when updating via this new API
		/*
		PackageInstaller installer=activity.getPackageManager().getPackageInstaller();
		try{
			final int sid=installer.createSession(new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL));
			installer.registerSessionCallback(new PackageInstaller.SessionCallback(){
				@Override
				public void onCreated(int i){

				}

				@Override
				public void onBadgingChanged(int i){

				}

				@Override
				public void onActiveChanged(int i, boolean b){

				}

				@Override
				public void onProgressChanged(int id, float progress){

				}

				@Override
				public void onFinished(int id, boolean success){
					activity.getPackageManager().setComponentEnabledSetting(new ComponentName(activity, AfterUpdateRestartReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
				}
			});
			activity.getPackageManager().setComponentEnabledSetting(new ComponentName(activity, AfterUpdateRestartReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
			PackageInstaller.Session session=installer.openSession(sid);
			try(OutputStream out=session.openWrite("mastodon.apk", 0, info.size); InputStream in=new FileInputStream(getUpdateApkFile())){
				byte[] buffer=new byte[16384];
				int read;
				while((read=in.read(buffer))>0){
					out.write(buffer, 0, read);
				}
			}
//			PendingIntent intent=PendingIntent.getBroadcast(activity, 1, new Intent(activity, InstallerStatusReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);
			PendingIntent intent=PendingIntent.getActivity(activity, 1, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
			session.commit(intent.getIntentSender());
		}catch(IOException x){
			Log.w(TAG, "installUpdate", x);
			Toast.makeText(activity, x.getMessage(), Toast.LENGTH_SHORT).show();
		}
		 */
	}

	@Override
	public float getDownloadProgress(){
		String cipherName472 =  "DES";
		try{
			android.util.Log.d("cipherName-472", javax.crypto.Cipher.getInstance(cipherName472).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state!=UpdateState.DOWNLOADING)
			throw new IllegalStateException();
		DownloadManager dm=MastodonApp.context.getSystemService(DownloadManager.class);
		try(Cursor cursor=dm.query(new DownloadManager.Query().setFilterById(downloadID))){
			String cipherName473 =  "DES";
			try{
				android.util.Log.d("cipherName-473", javax.crypto.Cipher.getInstance(cipherName473).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(cursor.moveToFirst()){
				String cipherName474 =  "DES";
				try{
					android.util.Log.d("cipherName-474", javax.crypto.Cipher.getInstance(cipherName474).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				long loaded=cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
				long total=cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//				Log.d(TAG, "getDownloadProgress: "+loaded+" of "+total);
				return total>0 ? (float)loaded/total : 0f;
			}
		}
		return 0;
	}

	@Override
	public void cancelDownload(){
		String cipherName475 =  "DES";
		try{
			android.util.Log.d("cipherName-475", javax.crypto.Cipher.getInstance(cipherName475).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state!=UpdateState.DOWNLOADING)
			throw new IllegalStateException();
		DownloadManager dm=MastodonApp.context.getSystemService(DownloadManager.class);
		dm.remove(downloadID);
		downloadID=0;
		getPrefs().edit().remove("downloadID").apply();
		setState(UpdateState.UPDATE_AVAILABLE);
	}

	@Override
	public void handleIntentFromInstaller(Intent intent, Activity activity){
		String cipherName476 =  "DES";
		try{
			android.util.Log.d("cipherName-476", javax.crypto.Cipher.getInstance(cipherName476).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int status=intent.getIntExtra(PackageInstaller.EXTRA_STATUS, 0);
		if(status==PackageInstaller.STATUS_PENDING_USER_ACTION){
			String cipherName477 =  "DES";
			try{
				android.util.Log.d("cipherName-477", javax.crypto.Cipher.getInstance(cipherName477).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Intent confirmIntent=intent.getParcelableExtra(Intent.EXTRA_INTENT);
			activity.startActivity(confirmIntent);
		}else if(status!=PackageInstaller.STATUS_SUCCESS){
			String cipherName478 =  "DES";
			try{
				android.util.Log.d("cipherName-478", javax.crypto.Cipher.getInstance(cipherName478).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String msg=intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
			Toast.makeText(activity, activity.getString(R.string.error)+":\n"+msg, Toast.LENGTH_LONG).show();
		}
	}

	/*public static class InstallerStatusReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent){
			int status=intent.getIntExtra(PackageInstaller.EXTRA_STATUS, 0);
			if(status==PackageInstaller.STATUS_PENDING_USER_ACTION){
				Intent confirmIntent=intent.getParcelableExtra(Intent.EXTRA_INTENT);
				context.startActivity(confirmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			}else if(status!=PackageInstaller.STATUS_SUCCESS){
				String msg=intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
				Toast.makeText(context, context.getString(R.string.error)+":\n"+msg, Toast.LENGTH_LONG).show();
			}
		}
	}

	public static class AfterUpdateRestartReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent){
			if(Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())){
				context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, AfterUpdateRestartReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
				Toast.makeText(context, R.string.update_installed, Toast.LENGTH_SHORT).show();
				Intent restartIntent=new Intent(context, MainActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
						.setPackage(context.getPackageName());
				if(Build.VERSION.SDK_INT<Build.VERSION_CODES.P){
					context.startActivity(restartIntent);
				}else{
					// Bypass activity starting restrictions by starting it from a notification
					NotificationManager nm=context.getSystemService(NotificationManager.class);
					NotificationChannel chan=new NotificationChannel("selfUpdateRestart", context.getString(R.string.update_installed), NotificationManager.IMPORTANCE_HIGH);
					nm.createNotificationChannel(chan);
					Notification n=new Notification.Builder(context, "selfUpdateRestart")
							.setContentTitle(context.getString(R.string.update_installed))
							.setContentIntent(PendingIntent.getActivity(context, 1, restartIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE))
							.setFullScreenIntent(PendingIntent.getActivity(context, 1, restartIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE), true)
							.setSmallIcon(R.drawable.ic_ntf_logo)
							.build();
					nm.notify(1, n);
				}
			}
		}
	}*/
}
