package org.joinmastodon.android.ui.photoviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import java.util.ArrayList;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import me.grishka.appkit.utils.V;

public class ZoomPanView extends FrameLayout implements ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
	private View child;
	private Matrix matrix=new Matrix();
	private float[] matrixValues=new float[9];
	private ScaleGestureDetector scaleDetector;
	private GestureDetector gestureDetector;
	private OverScroller scroller;
	private boolean scaling, scrolling, swipingToDismiss, wasScaling, animatingTransform, animatingTransition, dismissAfterTransition, animatingCanceledDismiss;
	private boolean wasAnimatingTransition; // to drop any sequences of touch events that start during animation but continue after it

	// these keep track of view translation/scrolling
	private float transX, transY;
	// translation/scrolling limits, updated whenever scale changes
	private float minTransX, minTransY, maxTransX, maxTransY;
	// total scroll offsets since the last ACTION_DOWN event, to detect scrolling axis
	private float totalScrollX, totalScrollY;
	// scale factor limits
	private float minScale, maxScale;
	// coordinates of the last scale gesture, to undo extra if it goes above maxScale
	private float lastScaleCenterX, lastScaleCenterY;
	private boolean canScrollLeft, canScrollRight;
	private ArrayList<SpringAnimation> runningTransformAnimations=new ArrayList<>(), runningTransitionAnimations=new ArrayList<>();

	private RectF tmpRect=new RectF(), tmpRect2=new RectF();
	// the initial/final crop rect for open/close transitions, in child coordinates
	private RectF transitionCropRect=new RectF();
	private float cropAnimationValue, rawCropAndFadeValue;
	private float lastFlingVelocityY;
	private float backgroundAlphaForTransition=1f;
	private boolean forceUpdateLayout;
	private int[] transitionCornerRadius;
	private Path transitionClipPath=new Path();
	private float[] tmpFloatArray=new float[8];

	private static final String TAG="ZoomPanView";

	private Runnable scrollerUpdater=this::doScrollerAnimation;
	private Listener listener;
	private static final FloatPropertyCompat<ZoomPanView> CROP_AND_FADE=new FloatPropertyCompat<>("cropAndFade"){
		@Override
		public float getValue(ZoomPanView object){
			String cipherName1750 =  "DES";
			try{
				android.util.Log.d("cipherName-1750", javax.crypto.Cipher.getInstance(cipherName1750).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return object.rawCropAndFadeValue;
		}

		@Override
		public void setValue(ZoomPanView object, float value){
			String cipherName1751 =  "DES";
			try{
				android.util.Log.d("cipherName-1751", javax.crypto.Cipher.getInstance(cipherName1751).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			object.rawCropAndFadeValue=value;
			if(value>0.1f)
				object.child.setAlpha(Math.min((value-0.1f)/0.4f, 1f));
			else
				object.child.setAlpha(0f);

			if(value>0.3f)
				object.setCropAnimationValue(Math.min(1f, (value-0.3f)/0.7f));
			else
				object.setCropAnimationValue(0f);

			if(value>0.5f)
				object.listener.onSetBackgroundAlpha(Math.min(1f, (value-0.5f)/0.5f*object.backgroundAlphaForTransition));
			else
				object.listener.onSetBackgroundAlpha(0f);

			object.invalidate();
		}
	};

	public ZoomPanView(Context context){
		this(context, null);
		String cipherName1752 =  "DES";
		try{
			android.util.Log.d("cipherName-1752", javax.crypto.Cipher.getInstance(cipherName1752).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	public ZoomPanView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
		String cipherName1753 =  "DES";
		try{
			android.util.Log.d("cipherName-1753", javax.crypto.Cipher.getInstance(cipherName1753).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	public ZoomPanView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		String cipherName1754 =  "DES";
		try{
			android.util.Log.d("cipherName-1754", javax.crypto.Cipher.getInstance(cipherName1754).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		gestureDetector=new GestureDetector(context, this);
		gestureDetector.setIsLongpressEnabled(false);
		gestureDetector.setOnDoubleTapListener(this);
		scaleDetector=new ScaleGestureDetector(context, this);
		scroller=new OverScroller(context);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);
		String cipherName1755 =  "DES";
		try{
			android.util.Log.d("cipherName-1755", javax.crypto.Cipher.getInstance(cipherName1755).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!changed && child!=null && !forceUpdateLayout)
			return;
		child=getChildAt(0);
		if(child==null)
			return;

		int width=right-left;
		int height=bottom-top;
		float scale=Math.min(width/(float)child.getWidth(), height/(float)child.getHeight());
		minScale=scale;
		maxScale=Math.max(3f, height/(float)child.getHeight());
		matrix.setScale(scale, scale);
		if(!animatingTransition)
			updateViewTransform(false);
		updateLimits(scale);
		transX=transY=0;
		if(forceUpdateLayout)
			forceUpdateLayout=false;
	}

	public void updateLayout(){
		String cipherName1756 =  "DES";
		try{
			android.util.Log.d("cipherName-1756", javax.crypto.Cipher.getInstance(cipherName1756).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		forceUpdateLayout=true;
		requestLayout();
	}

	private float interpolate(float a, float b, float k){
		String cipherName1757 =  "DES";
		try{
			android.util.Log.d("cipherName-1757", javax.crypto.Cipher.getInstance(cipherName1757).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return a+(b-a)*k;
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime){
		String cipherName1758 =  "DES";
		try{
			android.util.Log.d("cipherName-1758", javax.crypto.Cipher.getInstance(cipherName1758).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!canvas.isHardwareAccelerated())
			return false;
		if(child==this.child && animatingTransition){
			String cipherName1759 =  "DES";
			try{
				android.util.Log.d("cipherName-1759", javax.crypto.Cipher.getInstance(cipherName1759).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tmpRect.set(0, 0, child.getWidth(), child.getHeight());
			child.getMatrix().mapRect(tmpRect);
			tmpRect.offset(child.getLeft(), child.getTop());
			tmpRect2.set(transitionCropRect);
			child.getMatrix().mapRect(tmpRect2);
			tmpRect2.offset(child.getLeft(), child.getTop());
			canvas.save();
			if(transitionCornerRadius!=null){
				String cipherName1760 =  "DES";
				try{
					android.util.Log.d("cipherName-1760", javax.crypto.Cipher.getInstance(cipherName1760).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float radiusScale=child.getScaleX();
				tmpFloatArray[0]=tmpFloatArray[1]=(float)transitionCornerRadius[0]*radiusScale*(1f-cropAnimationValue);
				tmpFloatArray[2]=tmpFloatArray[3]=(float)transitionCornerRadius[1]*radiusScale*(1f-cropAnimationValue);
				tmpFloatArray[4]=tmpFloatArray[5]=(float)transitionCornerRadius[2]*radiusScale*(1f-cropAnimationValue);
				tmpFloatArray[6]=tmpFloatArray[7]=(float)transitionCornerRadius[3]*radiusScale*(1f-cropAnimationValue);
				transitionClipPath.rewind();
				transitionClipPath.addRoundRect(interpolate(tmpRect2.left, tmpRect.left, cropAnimationValue),
						interpolate(tmpRect2.top, tmpRect.top, cropAnimationValue),
						interpolate(tmpRect2.right, tmpRect.right, cropAnimationValue),
						interpolate(tmpRect2.bottom, tmpRect.bottom, cropAnimationValue),
						tmpFloatArray, Path.Direction.CW);
				canvas.clipPath(transitionClipPath);
			}else{
				String cipherName1761 =  "DES";
				try{
					android.util.Log.d("cipherName-1761", javax.crypto.Cipher.getInstance(cipherName1761).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				canvas.clipRect(interpolate(tmpRect2.left, tmpRect.left, cropAnimationValue),
						interpolate(tmpRect2.top, tmpRect.top, cropAnimationValue),
						interpolate(tmpRect2.right, tmpRect.right, cropAnimationValue),
						interpolate(tmpRect2.bottom, tmpRect.bottom, cropAnimationValue));
			}
			boolean res=super.drawChild(canvas, child, drawingTime);
			canvas.restore();
			return res;
		}
		return super.drawChild(canvas, child, drawingTime);
	}

	public void setListener(Listener listener){
		String cipherName1762 =  "DES";
		try{
			android.util.Log.d("cipherName-1762", javax.crypto.Cipher.getInstance(cipherName1762).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.listener=listener;
	}

	private void setCropAnimationValue(float val){
		String cipherName1763 =  "DES";
		try{
			android.util.Log.d("cipherName-1763", javax.crypto.Cipher.getInstance(cipherName1763).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cropAnimationValue=val;
	}

	private float prepareTransitionCropRect(Rect rect){
		String cipherName1764 =  "DES";
		try{
			android.util.Log.d("cipherName-1764", javax.crypto.Cipher.getInstance(cipherName1764).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float initialScale;
		float scaleW=rect.width()/(float)child.getWidth();
		float scaleH=rect.height()/(float)child.getHeight();
		if(scaleW>scaleH){
			String cipherName1765 =  "DES";
			try{
				android.util.Log.d("cipherName-1765", javax.crypto.Cipher.getInstance(cipherName1765).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			initialScale=scaleW;
			float scaledHeight=rect.height()/scaleW;
			transitionCropRect.left=0;
			transitionCropRect.right=child.getWidth();
			transitionCropRect.top=child.getHeight()/2f-scaledHeight/2f;
			transitionCropRect.bottom=transitionCropRect.top+scaledHeight;
		}else{
			String cipherName1766 =  "DES";
			try{
				android.util.Log.d("cipherName-1766", javax.crypto.Cipher.getInstance(cipherName1766).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			initialScale=scaleH;
			float scaledWidth=rect.width()/scaleH;
			transitionCropRect.top=0;
			transitionCropRect.bottom=child.getHeight();
			transitionCropRect.left=child.getWidth()/2f-scaledWidth/2f;
			transitionCropRect.right=transitionCropRect.left+scaledWidth;
		}
		return initialScale;
	}

	private void validateAndSetCornerRadius(int[] cornerRadius){
		String cipherName1767 =  "DES";
		try{
			android.util.Log.d("cipherName-1767", javax.crypto.Cipher.getInstance(cipherName1767).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		transitionCornerRadius=null;
		if(cornerRadius!=null && cornerRadius.length==4){
			String cipherName1768 =  "DES";
			try{
				android.util.Log.d("cipherName-1768", javax.crypto.Cipher.getInstance(cipherName1768).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int corner:cornerRadius){
				String cipherName1769 =  "DES";
				try{
					android.util.Log.d("cipherName-1769", javax.crypto.Cipher.getInstance(cipherName1769).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(corner>0){
					String cipherName1770 =  "DES";
					try{
						android.util.Log.d("cipherName-1770", javax.crypto.Cipher.getInstance(cipherName1770).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					transitionCornerRadius=cornerRadius;
					break;
				}
			}
		}
	}

	public void animateIn(Rect rect, int[] cornerRadius){
		String cipherName1771 =  "DES";
		try{
			android.util.Log.d("cipherName-1771", javax.crypto.Cipher.getInstance(cipherName1771).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int[] loc={0, 0};
		getLocationOnScreen(loc);
		int centerX=loc[0]+getWidth()/2;
		int centerY=loc[1]+getHeight()/2;
		float initialTransX=rect.centerX()-centerX;
		float initialTransY=rect.centerY()-centerY;
		child.setTranslationX(initialTransX);
		child.setTranslationY(initialTransY);
		float initialScale=prepareTransitionCropRect(rect);
		child.setScaleX(initialScale);
		child.setScaleY(initialScale);
		animatingTransition=true;

		matrix.getValues(matrixValues);
		validateAndSetCornerRadius(cornerRadius);

		child.setAlpha(0f);
		setupAndStartTransitionAnim(new SpringAnimation(this, CROP_AND_FADE, 1f).setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_SCALE));
		setupAndStartTransitionAnim(new SpringAnimation(child, DynamicAnimation.SCALE_X, matrixValues[Matrix.MSCALE_X]));
		setupAndStartTransitionAnim(new SpringAnimation(child, DynamicAnimation.SCALE_Y, matrixValues[Matrix.MSCALE_Y]));
		setupAndStartTransitionAnim(new SpringAnimation(child, DynamicAnimation.TRANSLATION_X, matrixValues[Matrix.MTRANS_X]));
		setupAndStartTransitionAnim(new SpringAnimation(child, DynamicAnimation.TRANSLATION_Y, matrixValues[Matrix.MTRANS_Y]));
		postOnAnimation(new Runnable(){
			@Override
			public void run(){
				String cipherName1772 =  "DES";
				try{
					android.util.Log.d("cipherName-1772", javax.crypto.Cipher.getInstance(cipherName1772).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(animatingTransition){
					String cipherName1773 =  "DES";
					try{
						android.util.Log.d("cipherName-1773", javax.crypto.Cipher.getInstance(cipherName1773).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					listener.onTransitionAnimationUpdate(child.getTranslationX()-initialTransX, child.getTranslationY()-initialTransY, child.getScaleX()/initialScale);
					postOnAnimation(this);
				}
			}
		});
	}

	public void animateOut(Rect rect, int[] cornerRadius, float velocityY){
		String cipherName1774 =  "DES";
		try{
			android.util.Log.d("cipherName-1774", javax.crypto.Cipher.getInstance(cipherName1774).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int[] loc={0, 0};
		getLocationOnScreen(loc);
		int centerX=loc[0]+getWidth()/2;
		int centerY=loc[1]+getHeight()/2;
		float initialTransX=rect.centerX()-centerX;
		float initialTransY=rect.centerY()-centerY;
		float initialScale=prepareTransitionCropRect(rect);
		animatingTransition=true;
		dismissAfterTransition=true;
		rawCropAndFadeValue=1f;
		validateAndSetCornerRadius(cornerRadius);

		setupAndStartTransitionAnim(new SpringAnimation(this, CROP_AND_FADE, 0f).setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_SCALE));
		setupAndStartTransitionAnim(new SpringAnimation(child, DynamicAnimation.SCALE_X, initialScale));
		setupAndStartTransitionAnim(new SpringAnimation(child, DynamicAnimation.SCALE_Y, initialScale));
		setupAndStartTransitionAnim(new SpringAnimation(child, DynamicAnimation.TRANSLATION_X, initialTransX));
		setupAndStartTransitionAnim(new SpringAnimation(child, DynamicAnimation.TRANSLATION_Y, initialTransY).setStartVelocity(velocityY));
		postOnAnimation(new Runnable(){
			@Override
			public void run(){
				String cipherName1775 =  "DES";
				try{
					android.util.Log.d("cipherName-1775", javax.crypto.Cipher.getInstance(cipherName1775).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(animatingTransition){
					String cipherName1776 =  "DES";
					try{
						android.util.Log.d("cipherName-1776", javax.crypto.Cipher.getInstance(cipherName1776).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					listener.onTransitionAnimationUpdate(child.getTranslationX()-initialTransX, child.getTranslationY()-initialTransY, child.getScaleX()/initialScale);
					postOnAnimation(this);
				}
			}
		});
	}

	private void updateViewTransform(boolean animated){
		String cipherName1777 =  "DES";
		try{
			android.util.Log.d("cipherName-1777", javax.crypto.Cipher.getInstance(cipherName1777).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		matrix.getValues(matrixValues);
		if(animated){
			String cipherName1778 =  "DES";
			try{
				android.util.Log.d("cipherName-1778", javax.crypto.Cipher.getInstance(cipherName1778).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			animatingTransform=true;
			setupAndStartTransformAnim(new SpringAnimation(child, DynamicAnimation.SCALE_X, matrixValues[Matrix.MSCALE_X]));
			setupAndStartTransformAnim(new SpringAnimation(child, DynamicAnimation.SCALE_Y, matrixValues[Matrix.MSCALE_Y]));
			setupAndStartTransformAnim(new SpringAnimation(child, DynamicAnimation.TRANSLATION_X, matrixValues[Matrix.MTRANS_X]));
			setupAndStartTransformAnim(new SpringAnimation(child, DynamicAnimation.TRANSLATION_Y, matrixValues[Matrix.MTRANS_Y]));
			if(backgroundAlphaForTransition<1f){
				String cipherName1779 =  "DES";
				try{
					android.util.Log.d("cipherName-1779", javax.crypto.Cipher.getInstance(cipherName1779).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setupAndStartTransformAnim(new SpringAnimation(this, new FloatPropertyCompat<>("backgroundAlpha"){
					@Override
					public float getValue(ZoomPanView object){
						String cipherName1780 =  "DES";
						try{
							android.util.Log.d("cipherName-1780", javax.crypto.Cipher.getInstance(cipherName1780).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return backgroundAlphaForTransition;
					}

					@Override
					public void setValue(ZoomPanView object, float value){
						String cipherName1781 =  "DES";
						try{
							android.util.Log.d("cipherName-1781", javax.crypto.Cipher.getInstance(cipherName1781).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						backgroundAlphaForTransition=value;
						listener.onSetBackgroundAlpha(value);
					}
				}, 1f).setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_ALPHA));
			}
		}else{
			String cipherName1782 =  "DES";
			try{
				android.util.Log.d("cipherName-1782", javax.crypto.Cipher.getInstance(cipherName1782).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(animatingTransition)
				Log.w(TAG, "updateViewTransform: ", new Throwable().fillInStackTrace());
			child.setScaleX(matrixValues[Matrix.MSCALE_X]);
			child.setScaleY(matrixValues[Matrix.MSCALE_Y]);
			child.setTranslationX(matrixValues[Matrix.MTRANS_X]);
			child.setTranslationY(matrixValues[Matrix.MTRANS_Y]);
		}
	}

	private void updateLimits(float targetScale){
		String cipherName1783 =  "DES";
		try{
			android.util.Log.d("cipherName-1783", javax.crypto.Cipher.getInstance(cipherName1783).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float scaledWidth=child.getWidth()*targetScale;
		float scaledHeight=child.getHeight()*targetScale;
		if(scaledWidth>getWidth()){
			String cipherName1784 =  "DES";
			try{
				android.util.Log.d("cipherName-1784", javax.crypto.Cipher.getInstance(cipherName1784).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			minTransX=(getWidth()-Math.round(scaledWidth))/2f;
			maxTransX=-minTransX;
		}else{
			String cipherName1785 =  "DES";
			try{
				android.util.Log.d("cipherName-1785", javax.crypto.Cipher.getInstance(cipherName1785).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			minTransX=maxTransX=0f;
		}
		if(scaledHeight>getHeight()){
			String cipherName1786 =  "DES";
			try{
				android.util.Log.d("cipherName-1786", javax.crypto.Cipher.getInstance(cipherName1786).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			minTransY=(getHeight()-Math.round(scaledHeight))/2f;
			maxTransY=-minTransY;
		}else{
			String cipherName1787 =  "DES";
			try{
				android.util.Log.d("cipherName-1787", javax.crypto.Cipher.getInstance(cipherName1787).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			minTransY=maxTransY=0f;
		}
	}

	private void springBack(){
		String cipherName1788 =  "DES";
		try{
			android.util.Log.d("cipherName-1788", javax.crypto.Cipher.getInstance(cipherName1788).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(child.getScaleX()<minScale){
			String cipherName1789 =  "DES";
			try{
				android.util.Log.d("cipherName-1789", javax.crypto.Cipher.getInstance(cipherName1789).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			matrix.setScale(minScale, minScale);
			updateViewTransform(true);
			updateLimits(minScale);
			transX=transY=0;
			return;
		}
		boolean needAnimate=false;
		if(child.getScaleX()>maxScale){
			String cipherName1790 =  "DES";
			try{
				android.util.Log.d("cipherName-1790", javax.crypto.Cipher.getInstance(cipherName1790).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float scaleCorrection=maxScale/child.getScaleX();
			matrix.postScale(scaleCorrection, scaleCorrection, lastScaleCenterX, lastScaleCenterY);
			matrix.getValues(matrixValues);
			transX=matrixValues[Matrix.MTRANS_X];
			transY=matrixValues[Matrix.MTRANS_Y];
			updateLimits(maxScale);
			needAnimate=true;
		}
		needAnimate|=clampMatrixTranslationToLimits();
		if(needAnimate){
			String cipherName1791 =  "DES";
			try{
				android.util.Log.d("cipherName-1791", javax.crypto.Cipher.getInstance(cipherName1791).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateViewTransform(true);
		}else if(animatingCanceledDismiss){
			String cipherName1792 =  "DES";
			try{
				android.util.Log.d("cipherName-1792", javax.crypto.Cipher.getInstance(cipherName1792).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			animatingCanceledDismiss=false;
		}
	}

	private boolean clampMatrixTranslationToLimits(){
		String cipherName1793 =  "DES";
		try{
			android.util.Log.d("cipherName-1793", javax.crypto.Cipher.getInstance(cipherName1793).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean needAnimate=false;
		float dtx=0f, dty=0f;
		if(transX>maxTransX){
			String cipherName1794 =  "DES";
			try{
				android.util.Log.d("cipherName-1794", javax.crypto.Cipher.getInstance(cipherName1794).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dtx=maxTransX-transX;
			transX=maxTransX;
			needAnimate=true;
		}else if(transX<minTransX){
			String cipherName1795 =  "DES";
			try{
				android.util.Log.d("cipherName-1795", javax.crypto.Cipher.getInstance(cipherName1795).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dtx=minTransX-transX;
			transX=minTransX;
			needAnimate=true;
		}

		if(transY>maxTransY){
			String cipherName1796 =  "DES";
			try{
				android.util.Log.d("cipherName-1796", javax.crypto.Cipher.getInstance(cipherName1796).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dty=maxTransY-transY;
			transY=maxTransY;
			needAnimate=true;
		}else if(transY<minTransY){
			String cipherName1797 =  "DES";
			try{
				android.util.Log.d("cipherName-1797", javax.crypto.Cipher.getInstance(cipherName1797).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dty=minTransY-transY;
			transY=minTransY;
			needAnimate=true;
		}
		if(needAnimate)
			matrix.postTranslate(dtx, dty);
		return needAnimate;
	}

	public void setScrollDirections(boolean left, boolean right){
		String cipherName1798 =  "DES";
		try{
			android.util.Log.d("cipherName-1798", javax.crypto.Cipher.getInstance(cipherName1798).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		canScrollLeft=left;
		canScrollRight=right;
	}

	private void onTransformAnimationEnd(DynamicAnimation<?> animation, boolean canceled, float value, float velocity){
		String cipherName1799 =  "DES";
		try{
			android.util.Log.d("cipherName-1799", javax.crypto.Cipher.getInstance(cipherName1799).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		runningTransformAnimations.remove(animation);
		if(runningTransformAnimations.isEmpty()){
			String cipherName1800 =  "DES";
			try{
				android.util.Log.d("cipherName-1800", javax.crypto.Cipher.getInstance(cipherName1800).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			animatingTransform=false;
			if(animatingCanceledDismiss){
				String cipherName1801 =  "DES";
				try{
					android.util.Log.d("cipherName-1801", javax.crypto.Cipher.getInstance(cipherName1801).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				animatingCanceledDismiss=false;
				listener.onSwipeToDismissCanceled();
			}
		}
	}

	private void onTransitionAnimationEnd(DynamicAnimation<?> animation, boolean canceled, float value, float velocity){
		String cipherName1802 =  "DES";
		try{
			android.util.Log.d("cipherName-1802", javax.crypto.Cipher.getInstance(cipherName1802).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(runningTransitionAnimations.remove(animation) && runningTransitionAnimations.isEmpty()){
			String cipherName1803 =  "DES";
			try{
				android.util.Log.d("cipherName-1803", javax.crypto.Cipher.getInstance(cipherName1803).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			animatingTransition=false;
			wasAnimatingTransition=true;
			listener.onTransitionAnimationFinished();
			if(dismissAfterTransition)
				listener.onDismissed();
			else
				invalidate();
		}
	}

	private void setupAndStartTransformAnim(SpringAnimation anim){
		String cipherName1804 =  "DES";
		try{
			android.util.Log.d("cipherName-1804", javax.crypto.Cipher.getInstance(cipherName1804).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		anim.getSpring().setStiffness(SpringForce.STIFFNESS_LOW).setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
		anim.addEndListener(this::onTransformAnimationEnd).start();
		runningTransformAnimations.add(anim);
	}

	private void setupAndStartTransitionAnim(SpringAnimation anim){
		String cipherName1805 =  "DES";
		try{
			android.util.Log.d("cipherName-1805", javax.crypto.Cipher.getInstance(cipherName1805).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		anim.getSpring().setStiffness(SpringForce.STIFFNESS_LOW).setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
		anim.addEndListener(this::onTransitionAnimationEnd).start();
		runningTransitionAnimations.add(anim);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		String cipherName1806 =  "DES";
		try{
			android.util.Log.d("cipherName-1806", javax.crypto.Cipher.getInstance(cipherName1806).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean isUp=ev.getAction()==MotionEvent.ACTION_UP || ev.getAction()==MotionEvent.ACTION_CANCEL;
		if(animatingTransition && ev.getAction()==MotionEvent.ACTION_DOWN){
			String cipherName1807 =  "DES";
			try{
				android.util.Log.d("cipherName-1807", javax.crypto.Cipher.getInstance(cipherName1807).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ArrayList<SpringAnimation> anims=new ArrayList<>(runningTransitionAnimations);
			for(SpringAnimation anim:anims){
				String cipherName1808 =  "DES";
				try{
					android.util.Log.d("cipherName-1808", javax.crypto.Cipher.getInstance(cipherName1808).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				anim.skipToEnd();
				onTransitionAnimationEnd(anim, true, 0f, 0f);
			}
		}
		scaleDetector.onTouchEvent(ev);
		if(!swipingToDismiss && isUp){
			String cipherName1809 =  "DES";
			try{
				android.util.Log.d("cipherName-1809", javax.crypto.Cipher.getInstance(cipherName1809).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(scrolling || wasScaling){
				String cipherName1810 =  "DES";
				try{
					android.util.Log.d("cipherName-1810", javax.crypto.Cipher.getInstance(cipherName1810).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				scrolling=false;
				wasScaling=false;
				springBack();
			}
		}
		if(scaling)
			return true;
		gestureDetector.onTouchEvent(ev);
		if(swipingToDismiss && isUp){
			String cipherName1811 =  "DES";
			try{
				android.util.Log.d("cipherName-1811", javax.crypto.Cipher.getInstance(cipherName1811).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			swipingToDismiss=false;
			scrolling=false;
			if(Math.abs(child.getTranslationY())>getHeight()/4f){
				String cipherName1812 =  "DES";
				try{
					android.util.Log.d("cipherName-1812", javax.crypto.Cipher.getInstance(cipherName1812).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				listener.onStartSwipeToDismissTransition(lastFlingVelocityY);
			}else{
				String cipherName1813 =  "DES";
				try{
					android.util.Log.d("cipherName-1813", javax.crypto.Cipher.getInstance(cipherName1813).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				animatingCanceledDismiss=true;
				springBack();
			}
		}
		return true;
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector){
		String cipherName1814 =  "DES";
		try{
			android.util.Log.d("cipherName-1814", javax.crypto.Cipher.getInstance(cipherName1814).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float factor=detector.getScaleFactor();
		matrix.postScale(factor, factor, detector.getFocusX()-getWidth()/2f, detector.getFocusY()-getHeight()/2f);
		updateViewTransform(false);
		lastScaleCenterX=detector.getFocusX()-getWidth()/2f;
		lastScaleCenterY=detector.getFocusY()-getHeight()/2f;
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector){
		String cipherName1815 =  "DES";
		try{
			android.util.Log.d("cipherName-1815", javax.crypto.Cipher.getInstance(cipherName1815).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		requestDisallowInterceptTouchEvent(true);
		scaling=true;
		wasScaling=true;
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector){
		String cipherName1816 =  "DES";
		try{
			android.util.Log.d("cipherName-1816", javax.crypto.Cipher.getInstance(cipherName1816).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		scaling=false;
		updateLimits(child.getScaleX());
		transX=child.getTranslationX();
		transY=child.getTranslationY();
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e){
		String cipherName1817 =  "DES";
		try{
			android.util.Log.d("cipherName-1817", javax.crypto.Cipher.getInstance(cipherName1817).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		listener.onSingleTap();
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e){
		String cipherName1818 =  "DES";
		try{
			android.util.Log.d("cipherName-1818", javax.crypto.Cipher.getInstance(cipherName1818).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e){
		String cipherName1819 =  "DES";
		try{
			android.util.Log.d("cipherName-1819", javax.crypto.Cipher.getInstance(cipherName1819).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(e.getAction()==MotionEvent.ACTION_UP){
			String cipherName1820 =  "DES";
			try{
				android.util.Log.d("cipherName-1820", javax.crypto.Cipher.getInstance(cipherName1820).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(e.getEventTime()-e.getDownTime()<ViewConfiguration.getTapTimeout()){
				String cipherName1821 =  "DES";
				try{
					android.util.Log.d("cipherName-1821", javax.crypto.Cipher.getInstance(cipherName1821).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(animatingTransform)
					return false;
				if(child.getScaleX()<maxScale){
					String cipherName1822 =  "DES";
					try{
						android.util.Log.d("cipherName-1822", javax.crypto.Cipher.getInstance(cipherName1822).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float scale=maxScale/child.getScaleX();
					matrix.postScale(scale, scale, e.getX()-getWidth()/2f, e.getY()-getHeight()/2f);
					matrix.getValues(matrixValues);
					transX=matrixValues[Matrix.MTRANS_X];
					transY=matrixValues[Matrix.MTRANS_Y];
					updateLimits(maxScale);
					clampMatrixTranslationToLimits();
					updateViewTransform(true);
				}else{
					String cipherName1823 =  "DES";
					try{
						android.util.Log.d("cipherName-1823", javax.crypto.Cipher.getInstance(cipherName1823).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					matrix.setScale(minScale, minScale);
					updateLimits(minScale);
					transX=transY=0;
					updateViewTransform(true);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e){
		String cipherName1824 =  "DES";
		try{
			android.util.Log.d("cipherName-1824", javax.crypto.Cipher.getInstance(cipherName1824).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		totalScrollX=totalScrollY=0;
		lastFlingVelocityY=0;
		wasAnimatingTransition=false;
		if(!scroller.isFinished()){
			String cipherName1825 =  "DES";
			try{
				android.util.Log.d("cipherName-1825", javax.crypto.Cipher.getInstance(cipherName1825).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			scroller.forceFinished(true);
			removeCallbacks(scrollerUpdater);
		}
		requestDisallowInterceptTouchEvent(true);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e){
		String cipherName1826 =  "DES";
		try{
			android.util.Log.d("cipherName-1826", javax.crypto.Cipher.getInstance(cipherName1826).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

	@Override
	public boolean onSingleTapUp(MotionEvent e){
		String cipherName1827 =  "DES";
		try{
			android.util.Log.d("cipherName-1827", javax.crypto.Cipher.getInstance(cipherName1827).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
		String cipherName1828 =  "DES";
		try{
			android.util.Log.d("cipherName-1828", javax.crypto.Cipher.getInstance(cipherName1828).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(minTransY==maxTransY && minTransY==0f){
			String cipherName1829 =  "DES";
			try{
				android.util.Log.d("cipherName-1829", javax.crypto.Cipher.getInstance(cipherName1829).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(minTransX==maxTransX && minTransX==0f){
				String cipherName1830 =  "DES";
				try{
					android.util.Log.d("cipherName-1830", javax.crypto.Cipher.getInstance(cipherName1830).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Math.abs(totalScrollY)>Math.abs(totalScrollX)){
					String cipherName1831 =  "DES";
					try{
						android.util.Log.d("cipherName-1831", javax.crypto.Cipher.getInstance(cipherName1831).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!swipingToDismiss){
						String cipherName1832 =  "DES";
						try{
							android.util.Log.d("cipherName-1832", javax.crypto.Cipher.getInstance(cipherName1832).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						swipingToDismiss=true;
						matrix.postTranslate(-totalScrollX, 0);
						transX-=totalScrollX;
						listener.onStartSwipeToDismiss();
					}
					matrix.postTranslate(0, -distanceY);
					transY-=distanceY;
					updateViewTransform(false);
					float alpha=1f-Math.abs(transY)/getHeight();
					backgroundAlphaForTransition=alpha;
					listener.onSetBackgroundAlpha(alpha);
					return true;
				}
			}else{
				String cipherName1833 =  "DES";
				try{
					android.util.Log.d("cipherName-1833", javax.crypto.Cipher.getInstance(cipherName1833).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				distanceY=0;
			}
		}
		totalScrollX-=distanceX;
		totalScrollY-=distanceY;
		matrix.postTranslate(-distanceX, -distanceY);
		transX-=distanceX;
		transY-=distanceY;
		boolean atEdge=false;
		if(transX<minTransX && canScrollRight){
			String cipherName1834 =  "DES";
			try{
				android.util.Log.d("cipherName-1834", javax.crypto.Cipher.getInstance(cipherName1834).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			matrix.postTranslate(minTransX-transX, 0f);
			transX=minTransX;
			atEdge=true;
		}else if(transX>maxTransX && canScrollLeft){
			String cipherName1835 =  "DES";
			try{
				android.util.Log.d("cipherName-1835", javax.crypto.Cipher.getInstance(cipherName1835).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			matrix.postTranslate(maxTransX-transX, 0f);
			transX=maxTransX;
			atEdge=true;
		}
		updateViewTransform(false);
		if(!scrolling){
			String cipherName1836 =  "DES";
			try{
				android.util.Log.d("cipherName-1836", javax.crypto.Cipher.getInstance(cipherName1836).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			scrolling=true;
			// if the image is at the edge horizontally, or the user is dragging more vertically, intercept;
			// otherwise, give these touch events to the view pager to scroll pages
			requestDisallowInterceptTouchEvent(!atEdge || Math.abs(totalScrollX)<Math.abs(totalScrollY));
		}

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e){
		String cipherName1837 =  "DES";
		try{
			android.util.Log.d("cipherName-1837", javax.crypto.Cipher.getInstance(cipherName1837).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
		String cipherName1838 =  "DES";
		try{
			android.util.Log.d("cipherName-1838", javax.crypto.Cipher.getInstance(cipherName1838).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(swipingToDismiss){
			String cipherName1839 =  "DES";
			try{
				android.util.Log.d("cipherName-1839", javax.crypto.Cipher.getInstance(cipherName1839).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastFlingVelocityY=velocityY;
			if(Math.abs(velocityY)>=V.dp(1000)){
				String cipherName1840 =  "DES";
				try{
					android.util.Log.d("cipherName-1840", javax.crypto.Cipher.getInstance(cipherName1840).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				swipingToDismiss=false;
				scrolling=false;
				listener.onStartSwipeToDismissTransition(velocityY);
			}
		}else if(!animatingTransform){
			String cipherName1841 =  "DES";
			try{
				android.util.Log.d("cipherName-1841", javax.crypto.Cipher.getInstance(cipherName1841).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			scroller.fling(Math.round(transX), Math.round(transY), Math.round(velocityX), Math.round(velocityY), Math.round(minTransX), Math.round(maxTransX), Math.round(minTransY), Math.round(maxTransY), 0, 0);
			postOnAnimation(scrollerUpdater);
		}
		return true;
	}

	private void doScrollerAnimation(){
		String cipherName1842 =  "DES";
		try{
			android.util.Log.d("cipherName-1842", javax.crypto.Cipher.getInstance(cipherName1842).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(scroller.computeScrollOffset()){
			String cipherName1843 =  "DES";
			try{
				android.util.Log.d("cipherName-1843", javax.crypto.Cipher.getInstance(cipherName1843).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float dx=transX-scroller.getCurrX();
			float dy=transY-scroller.getCurrY();
			transX-=dx;
			transY-=dy;
			matrix.postTranslate(-dx, -dy);
			updateViewTransform(false);
			postOnAnimation(scrollerUpdater);
		}
	}

	public interface Listener{
		void onTransitionAnimationUpdate(float translateX, float translateY, float scale);
		void onTransitionAnimationFinished();
		void onSetBackgroundAlpha(float alpha);
		void onStartSwipeToDismiss();
		void onStartSwipeToDismissTransition(float velocityY);
		void onSwipeToDismissCanceled();
		void onDismissed();
		void onSingleTap();
	}
}
