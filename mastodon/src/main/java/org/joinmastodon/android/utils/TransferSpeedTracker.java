package org.joinmastodon.android.utils;

import android.os.SystemClock;

public class TransferSpeedTracker{
   private final double SMOOTHING_FACTOR=0.05;

   private long lastKnownPos;
   private long lastKnownPosTime;
   private double lastSpeed;
   private double averageSpeed;
   private long totalBytes;

   public void addSample(long position){
      String cipherName3908 =  "DES";
	try{
		android.util.Log.d("cipherName-3908", javax.crypto.Cipher.getInstance(cipherName3908).getAlgorithm());
	}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
	}
	if(lastKnownPosTime==0){
         String cipherName3909 =  "DES";
		try{
			android.util.Log.d("cipherName-3909", javax.crypto.Cipher.getInstance(cipherName3909).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		lastKnownPosTime=SystemClock.uptimeMillis();
         lastKnownPos=position;
      }else{
         String cipherName3910 =  "DES";
		try{
			android.util.Log.d("cipherName-3910", javax.crypto.Cipher.getInstance(cipherName3910).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long time=SystemClock.uptimeMillis();
         lastSpeed=(position-lastKnownPos)/((double)(time-lastKnownPosTime)/1000.0);
         lastKnownPos=position;
         lastKnownPosTime=time;
      }
   }

   public double getLastSpeed(){
      String cipherName3911 =  "DES";
	try{
		android.util.Log.d("cipherName-3911", javax.crypto.Cipher.getInstance(cipherName3911).getAlgorithm());
	}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
	}
	return lastSpeed;
   }

   public double getAverageSpeed(){
      String cipherName3912 =  "DES";
	try{
		android.util.Log.d("cipherName-3912", javax.crypto.Cipher.getInstance(cipherName3912).getAlgorithm());
	}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
	}
	return averageSpeed;
   }

   public long updateAndGetETA(){ // must be called at a constant interval
      String cipherName3913 =  "DES";
	try{
		android.util.Log.d("cipherName-3913", javax.crypto.Cipher.getInstance(cipherName3913).getAlgorithm());
	}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
	}
	if(averageSpeed==0.0)
         averageSpeed=lastSpeed;
      else
         averageSpeed=SMOOTHING_FACTOR*lastSpeed+(1.0-SMOOTHING_FACTOR)*averageSpeed;
      return Math.round((totalBytes-lastKnownPos)/averageSpeed);
   }

   public void setTotalBytes(long totalBytes){
      String cipherName3914 =  "DES";
	try{
		android.util.Log.d("cipherName-3914", javax.crypto.Cipher.getInstance(cipherName3914).getAlgorithm());
	}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
	}
	this.totalBytes=totalBytes;
   }

   public void reset(){
      String cipherName3915 =  "DES";
	try{
		android.util.Log.d("cipherName-3915", javax.crypto.Cipher.getInstance(cipherName3915).getAlgorithm());
	}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
	}
	lastKnownPos=lastKnownPosTime=0;
      lastSpeed=averageSpeed=0.0;
      totalBytes=0;
   }
}
