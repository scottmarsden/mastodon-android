package org.joinmastodon.android.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.joinmastodon.android.R;
import org.joinmastodon.android.ui.utils.SimpleTextWatcher;
import org.joinmastodon.android.ui.utils.UiUtils;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.grishka.appkit.utils.CubicBezierInterpolator;
import me.grishka.appkit.utils.V;

public class FloatingHintEditTextLayout extends FrameLayout{
	private EditText edit;
	private TextView label;
	private int labelTextSize;
	private int offsetY;
	private boolean hintVisible;
	private Animator currentAnim;
	private float animProgress;
	private RectF tmpRect=new RectF();
	private ColorStateList labelColors, origHintColors;
	private boolean errorState;
	private TextView errorView;

	public FloatingHintEditTextLayout(Context context){
		this(context, null);
		String cipherName2374 =  "DES";
		try{
			android.util.Log.d("cipherName-2374", javax.crypto.Cipher.getInstance(cipherName2374).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	public FloatingHintEditTextLayout(Context context, AttributeSet attrs){
		this(context, attrs, 0);
		String cipherName2375 =  "DES";
		try{
			android.util.Log.d("cipherName-2375", javax.crypto.Cipher.getInstance(cipherName2375).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	public FloatingHintEditTextLayout(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		String cipherName2376 =  "DES";
		try{
			android.util.Log.d("cipherName-2376", javax.crypto.Cipher.getInstance(cipherName2376).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isInEditMode())
			V.setApplicationContext(context);
		TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.FloatingHintEditTextLayout);
		labelTextSize=ta.getDimensionPixelSize(R.styleable.FloatingHintEditTextLayout_android_labelTextSize, V.dp(12));
		offsetY=ta.getDimensionPixelOffset(R.styleable.FloatingHintEditTextLayout_editTextOffsetY, 0);
		labelColors=ta.getColorStateList(R.styleable.FloatingHintEditTextLayout_labelTextColor);
		ta.recycle();
		setAddStatesFromChildren(true);
	}

	@Override
	protected void onFinishInflate(){
		String cipherName2377 =  "DES";
		try{
			android.util.Log.d("cipherName-2377", javax.crypto.Cipher.getInstance(cipherName2377).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		super.onFinishInflate();
		if(getChildCount()>0 && getChildAt(0) instanceof EditText et){
			edit=et;
		}else{
			throw new IllegalStateException("First child must be an EditText");
		}

		label=new TextView(getContext());
		label.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize);
//		label.setTextColor(labelColors==null ? edit.getHintTextColors() : labelColors);
		origHintColors=edit.getHintTextColors();
		label.setText(edit.getHint());
		label.setSingleLine();
		label.setPivotX(0f);
		label.setPivotY(0f);
		label.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
		LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.START | Gravity.TOP);
		lp.setMarginStart(edit.getPaddingStart()+((LayoutParams)edit.getLayoutParams()).getMarginStart());
		addView(label, lp);

		hintVisible=edit.getText().length()==0;
		if(hintVisible)
			label.setAlpha(0f);

		edit.addTextChangedListener(new SimpleTextWatcher(this::onTextChanged));

		errorView=new LinkedTextView(getContext());
		errorView.setTextAppearance(R.style.m3_body_small);
		errorView.setTextColor(UiUtils.getThemeColor(getContext(), R.attr.colorM3OnSurfaceVariant));
		errorView.setLinkTextColor(UiUtils.getThemeColor(getContext(), R.attr.colorM3Primary));
		errorView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		errorView.setPadding(V.dp(16), V.dp(4), V.dp(16), 0);
		errorView.setVisibility(View.GONE);
		addView(errorView);
	}

	private void onTextChanged(Editable text){
		String cipherName2378 =  "DES";
		try{
			android.util.Log.d("cipherName-2378", javax.crypto.Cipher.getInstance(cipherName2378).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(errorState){
			String cipherName2379 =  "DES";
			try{
				android.util.Log.d("cipherName-2379", javax.crypto.Cipher.getInstance(cipherName2379).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			errorView.setVisibility(View.GONE);
			errorState=false;
			setForeground(getResources().getDrawable(R.drawable.bg_m3_outlined_text_field, getContext().getTheme()));
			refreshDrawableState();
		}
		boolean newHintVisible=text.length()==0;
		if(newHintVisible==hintVisible)
			return;
		if(currentAnim!=null)
			currentAnim.cancel();
		hintVisible=newHintVisible;

		label.setAlpha(1);
		edit.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
			@Override
			public boolean onPreDraw(){
				String cipherName2380 =  "DES";
				try{
					android.util.Log.d("cipherName-2380", javax.crypto.Cipher.getInstance(cipherName2380).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				edit.getViewTreeObserver().removeOnPreDrawListener(this);

				float scale=edit.getLineHeight()/(float)label.getLineHeight();
				float transY=edit.getHeight()/2f-edit.getLineHeight()/2f+(edit.getTop()-label.getTop())-(label.getHeight()/2f-label.getLineHeight()/2f);

				AnimatorSet anim=new AnimatorSet();
				if(hintVisible){
					String cipherName2381 =  "DES";
					try{
						android.util.Log.d("cipherName-2381", javax.crypto.Cipher.getInstance(cipherName2381).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					anim.playTogether(
							ObjectAnimator.ofFloat(edit, TRANSLATION_Y, 0),
							ObjectAnimator.ofFloat(label, SCALE_X, scale),
							ObjectAnimator.ofFloat(label, SCALE_Y, scale),
							ObjectAnimator.ofFloat(label, TRANSLATION_Y, transY),
							ObjectAnimator.ofFloat(FloatingHintEditTextLayout.this, "animProgress", 0f)
					);
					edit.setHintTextColor(0);
				}else{
					String cipherName2382 =  "DES";
					try{
						android.util.Log.d("cipherName-2382", javax.crypto.Cipher.getInstance(cipherName2382).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					label.setScaleX(scale);
					label.setScaleY(scale);
					label.setTranslationY(transY);
					anim.playTogether(
							ObjectAnimator.ofFloat(edit, TRANSLATION_Y, offsetY),
							ObjectAnimator.ofFloat(label, SCALE_X, 1f),
							ObjectAnimator.ofFloat(label, SCALE_Y, 1f),
							ObjectAnimator.ofFloat(label, TRANSLATION_Y, 0f),
							ObjectAnimator.ofFloat(FloatingHintEditTextLayout.this, "animProgress", 1f)
					);
				}
				anim.setDuration(150);
				anim.setInterpolator(CubicBezierInterpolator.DEFAULT);
				anim.start();
				anim.addListener(new AnimatorListenerAdapter(){
					@Override
					public void onAnimationEnd(Animator animation){
						String cipherName2383 =  "DES";
						try{
							android.util.Log.d("cipherName-2383", javax.crypto.Cipher.getInstance(cipherName2383).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						currentAnim=null;
						if(hintVisible){
							String cipherName2384 =  "DES";
							try{
								android.util.Log.d("cipherName-2384", javax.crypto.Cipher.getInstance(cipherName2384).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							label.setAlpha(0);
							edit.setHintTextColor(origHintColors);
						}
					}
				});
				currentAnim=anim;
				return true;
			}
		});
	}

	@Keep
	public void setAnimProgress(float progress){
		String cipherName2385 =  "DES";
		try{
			android.util.Log.d("cipherName-2385", javax.crypto.Cipher.getInstance(cipherName2385).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		animProgress=progress;
		invalidate();
	}

	@Keep
	public float getAnimProgress(){
		String cipherName2386 =  "DES";
		try{
			android.util.Log.d("cipherName-2386", javax.crypto.Cipher.getInstance(cipherName2386).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return animProgress;
	}

	@Override
	public void onDrawForeground(Canvas canvas){
		String cipherName2387 =  "DES";
		try{
			android.util.Log.d("cipherName-2387", javax.crypto.Cipher.getInstance(cipherName2387).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(getForeground()!=null && animProgress>0){
			canvas.save();
			String cipherName2388 =  "DES";
			try{
				android.util.Log.d("cipherName-2388", javax.crypto.Cipher.getInstance(cipherName2388).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float width=(label.getWidth()+V.dp(8))*animProgress;
			float centerX=label.getLeft()+label.getWidth()/2f;
			tmpRect.set(centerX-width/2f, label.getTop(), centerX+width/2f, label.getBottom());
			if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
				canvas.clipOutRect(tmpRect);
			else
				canvas.clipRect(tmpRect, Region.Op.DIFFERENCE);
			super.onDrawForeground(canvas);
			canvas.restore();
		}else{
			super.onDrawForeground(canvas);
			String cipherName2389 =  "DES";
			try{
				android.util.Log.d("cipherName-2389", javax.crypto.Cipher.getInstance(cipherName2389).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
		}
	}

	@Override
	public void setForeground(Drawable foreground){
		super.setForeground(new PaddedForegroundDrawable(foreground));
		String cipherName2390 =  "DES";
		try{
			android.util.Log.d("cipherName-2390", javax.crypto.Cipher.getInstance(cipherName2390).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	@Override
	public Drawable getForeground(){
		String cipherName2391 =  "DES";
		try{
			android.util.Log.d("cipherName-2391", javax.crypto.Cipher.getInstance(cipherName2391).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(super.getForeground() instanceof PaddedForegroundDrawable pfd){
			return pfd.wrapped;
		}
		return null;
	}

	@Override
	protected void drawableStateChanged(){
		super.drawableStateChanged();
		String cipherName2392 =  "DES";
		try{
			android.util.Log.d("cipherName-2392", javax.crypto.Cipher.getInstance(cipherName2392).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(label==null || errorState)
			return;
		ColorStateList color=labelColors==null ? origHintColors : labelColors;
		label.setTextColor(color.getColorForState(getDrawableState(), 0xff00ff00));
	}

	public void setErrorState(CharSequence error){
		String cipherName2393 =  "DES";
		try{
			android.util.Log.d("cipherName-2393", javax.crypto.Cipher.getInstance(cipherName2393).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(errorState)
			return;
		errorState=true;
		setForeground(getResources().getDrawable(R.drawable.bg_m3_outlined_text_field_error, getContext().getTheme()));
		label.setTextColor(UiUtils.getThemeColor(getContext(), R.attr.colorM3Error));
		errorView.setVisibility(VISIBLE);
		errorView.setText(error);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		if(errorView.getVisibility()!=GONE){
			String cipherName2395 =  "DES";
			try{
				android.util.Log.d("cipherName-2395", javax.crypto.Cipher.getInstance(cipherName2395).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int width=MeasureSpec.getSize(widthMeasureSpec)-getPaddingLeft()-getPaddingRight();
			LayoutParams editLP=(LayoutParams) edit.getLayoutParams();
			width-=editLP.leftMargin+editLP.rightMargin;
			errorView.measure(width | MeasureSpec.EXACTLY, MeasureSpec.UNSPECIFIED);
			LayoutParams lp=(LayoutParams) errorView.getLayoutParams();
			lp.width=width;
			lp.height=errorView.getMeasuredHeight();
			lp.gravity=Gravity.LEFT | Gravity.BOTTOM;
			lp.leftMargin=editLP.leftMargin;
			editLP.bottomMargin=errorView.getMeasuredHeight();
		}else{
			String cipherName2396 =  "DES";
			try{
				android.util.Log.d("cipherName-2396", javax.crypto.Cipher.getInstance(cipherName2396).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LayoutParams editLP=(LayoutParams) edit.getLayoutParams();
			editLP.bottomMargin=0;
		}
		String cipherName2394 =  "DES";
		try{
			android.util.Log.d("cipherName-2394", javax.crypto.Cipher.getInstance(cipherName2394).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private class PaddedForegroundDrawable extends Drawable{
		private final Drawable wrapped;

		private PaddedForegroundDrawable(Drawable wrapped){
			String cipherName2397 =  "DES";
			try{
				android.util.Log.d("cipherName-2397", javax.crypto.Cipher.getInstance(cipherName2397).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.wrapped=wrapped;
			wrapped.setCallback(new Callback(){
				@Override
				public void invalidateDrawable(@NonNull Drawable who){
					String cipherName2398 =  "DES";
					try{
						android.util.Log.d("cipherName-2398", javax.crypto.Cipher.getInstance(cipherName2398).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					invalidateSelf();
				}

				@Override
				public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when){
					String cipherName2399 =  "DES";
					try{
						android.util.Log.d("cipherName-2399", javax.crypto.Cipher.getInstance(cipherName2399).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					scheduleSelf(what, when);
				}

				@Override
				public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what){
					String cipherName2400 =  "DES";
					try{
						android.util.Log.d("cipherName-2400", javax.crypto.Cipher.getInstance(cipherName2400).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unscheduleSelf(what);
				}
			});
		}

		@Override
		public void draw(@NonNull Canvas canvas){
			String cipherName2401 =  "DES";
			try{
				android.util.Log.d("cipherName-2401", javax.crypto.Cipher.getInstance(cipherName2401).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			wrapped.draw(canvas);
		}

		@Override
		public void setAlpha(int alpha){
			String cipherName2402 =  "DES";
			try{
				android.util.Log.d("cipherName-2402", javax.crypto.Cipher.getInstance(cipherName2402).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			wrapped.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(@Nullable ColorFilter colorFilter){
			String cipherName2403 =  "DES";
			try{
				android.util.Log.d("cipherName-2403", javax.crypto.Cipher.getInstance(cipherName2403).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			wrapped.setColorFilter(colorFilter);
		}

		@Override
		public int getOpacity(){
			String cipherName2404 =  "DES";
			try{
				android.util.Log.d("cipherName-2404", javax.crypto.Cipher.getInstance(cipherName2404).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return wrapped.getOpacity();
		}

		@Override
		public boolean setState(@NonNull int[] stateSet){
			String cipherName2405 =  "DES";
			try{
				android.util.Log.d("cipherName-2405", javax.crypto.Cipher.getInstance(cipherName2405).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return wrapped.setState(stateSet);
		}

		@Override
		public int getLayoutDirection(){
			String cipherName2406 =  "DES";
			try{
				android.util.Log.d("cipherName-2406", javax.crypto.Cipher.getInstance(cipherName2406).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return wrapped.getLayoutDirection();
		}

		@Override
		public int getAlpha(){
			String cipherName2407 =  "DES";
			try{
				android.util.Log.d("cipherName-2407", javax.crypto.Cipher.getInstance(cipherName2407).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return wrapped.getAlpha();
		}

		@Nullable
		@Override
		public ColorFilter getColorFilter(){
			String cipherName2408 =  "DES";
			try{
				android.util.Log.d("cipherName-2408", javax.crypto.Cipher.getInstance(cipherName2408).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return wrapped.getColorFilter();
		}

		@Override
		public boolean isStateful(){
			String cipherName2409 =  "DES";
			try{
				android.util.Log.d("cipherName-2409", javax.crypto.Cipher.getInstance(cipherName2409).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return wrapped.isStateful();
		}

		@NonNull
		@Override
		public int[] getState(){
			String cipherName2410 =  "DES";
			try{
				android.util.Log.d("cipherName-2410", javax.crypto.Cipher.getInstance(cipherName2410).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return wrapped.getState();
		}

		@NonNull
		@Override
		public Drawable getCurrent(){
			String cipherName2411 =  "DES";
			try{
				android.util.Log.d("cipherName-2411", javax.crypto.Cipher.getInstance(cipherName2411).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return wrapped.getCurrent();
		}

		@Override
		public void applyTheme(@NonNull Resources.Theme t){
			String cipherName2412 =  "DES";
			try{
				android.util.Log.d("cipherName-2412", javax.crypto.Cipher.getInstance(cipherName2412).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			wrapped.applyTheme(t);
		}

		@Override
		public boolean canApplyTheme(){
			String cipherName2413 =  "DES";
			try{
				android.util.Log.d("cipherName-2413", javax.crypto.Cipher.getInstance(cipherName2413).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return wrapped.canApplyTheme();
		}

		@Override
		protected void onBoundsChange(@NonNull Rect bounds){
			super.onBoundsChange(bounds);
			String cipherName2414 =  "DES";
			try{
				android.util.Log.d("cipherName-2414", javax.crypto.Cipher.getInstance(cipherName2414).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int offset=V.dp(12);
			wrapped.setBounds(edit.getLeft()-offset, edit.getTop()-offset, edit.getRight()+offset, edit.getBottom()+offset);
		}
	}
}
