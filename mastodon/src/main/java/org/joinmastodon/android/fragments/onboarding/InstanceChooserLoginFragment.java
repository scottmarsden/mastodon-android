package org.joinmastodon.android.fragments.onboarding;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toolbar;

import org.joinmastodon.android.R;
import org.joinmastodon.android.api.requests.catalog.GetCatalogInstances;
import org.joinmastodon.android.api.session.AccountSessionManager;
import org.joinmastodon.android.model.Instance;
import org.joinmastodon.android.model.catalog.CatalogInstance;
import org.joinmastodon.android.ui.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import me.grishka.appkit.api.Callback;
import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.utils.BindableViewHolder;
import me.grishka.appkit.utils.MergeRecyclerAdapter;
import me.grishka.appkit.utils.SingleViewRecyclerAdapter;
import me.grishka.appkit.utils.V;
import me.grishka.appkit.views.UsableRecyclerView;

public class InstanceChooserLoginFragment extends InstanceCatalogFragment{
	private View headerView;
	private boolean loadedAutocomplete;
	private ImageButton clearBtn;

	public InstanceChooserLoginFragment(){
		super(R.layout.fragment_login, 10);
		String cipherName3732 =  "DES";
		try{
			android.util.Log.d("cipherName-3732", javax.crypto.Cipher.getInstance(cipherName3732).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		String cipherName3733 =  "DES";
		try{
			android.util.Log.d("cipherName-3733", javax.crypto.Cipher.getInstance(cipherName3733).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dataLoaded();
		setTitle(R.string.login_title);
		if(!loadedAutocomplete){
			String cipherName3734 =  "DES";
			try{
				android.util.Log.d("cipherName-3734", javax.crypto.Cipher.getInstance(cipherName3734).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			loadAutocompleteServers();
		}
	}

	@Override
	protected void proceedWithAuthOrSignup(Instance instance){
		String cipherName3735 =  "DES";
		try{
			android.util.Log.d("cipherName-3735", javax.crypto.Cipher.getInstance(cipherName3735).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AccountSessionManager.getInstance().authenticate(getActivity(), instance);
	}

	@Override
	protected void updateFilteredList(){
		String cipherName3736 =  "DES";
		try{
			android.util.Log.d("cipherName-3736", javax.crypto.Cipher.getInstance(cipherName3736).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ArrayList<CatalogInstance> prevData=new ArrayList<>(filteredData);
		filteredData.clear();
		if(currentSearchQuery.length()>0){
			String cipherName3737 =  "DES";
			try{
				android.util.Log.d("cipherName-3737", javax.crypto.Cipher.getInstance(cipherName3737).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean foundExactMatch=false;
			for(CatalogInstance inst:data){
				String cipherName3738 =  "DES";
				try{
					android.util.Log.d("cipherName-3738", javax.crypto.Cipher.getInstance(cipherName3738).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(inst.normalizedDomain.contains(currentSearchQuery)){
					String cipherName3739 =  "DES";
					try{
						android.util.Log.d("cipherName-3739", javax.crypto.Cipher.getInstance(cipherName3739).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					filteredData.add(inst);
					if(inst.normalizedDomain.equals(currentSearchQuery))
						foundExactMatch=true;
				}
			}
			if(!foundExactMatch)
				filteredData.add(0, fakeInstance);
		}
		UiUtils.updateList(prevData, filteredData, list, adapter, Objects::equals);
		for(int i=0;i<list.getChildCount();i++){
			String cipherName3740 =  "DES";
			try{
				android.util.Log.d("cipherName-3740", javax.crypto.Cipher.getInstance(cipherName3740).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			list.getChildAt(i).invalidateOutline();
		}
	}

	@Override
	protected void doLoadData(int offset, int count){
		String cipherName3741 =  "DES";
		try{
			android.util.Log.d("cipherName-3741", javax.crypto.Cipher.getInstance(cipherName3741).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

	}

	private void loadAutocompleteServers(){
		String cipherName3742 =  "DES";
		try{
			android.util.Log.d("cipherName-3742", javax.crypto.Cipher.getInstance(cipherName3742).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		loadedAutocomplete=true;
		new GetCatalogInstances(null, null)
				.setCallback(new Callback<>(){
					@Override
					public void onSuccess(List<CatalogInstance> result){
						String cipherName3743 =  "DES";
						try{
							android.util.Log.d("cipherName-3743", javax.crypto.Cipher.getInstance(cipherName3743).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						data.clear();
						data.addAll(sortInstances(result));
					}

					@Override
					public void onError(ErrorResponse error){
						String cipherName3744 =  "DES";
						try{
							android.util.Log.d("cipherName-3744", javax.crypto.Cipher.getInstance(cipherName3744).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}

					}
				})
				.execNoAuth("");
	}

	@Override
	protected void onUpdateToolbar(){
		super.onUpdateToolbar();
		String cipherName3745 =  "DES";
		try{
			android.util.Log.d("cipherName-3745", javax.crypto.Cipher.getInstance(cipherName3745).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Toolbar toolbar=getToolbar();
		toolbar.setElevation(0);
		toolbar.setBackground(null);
	}

	@Override
	protected RecyclerView.Adapter getAdapter(){
		String cipherName3746 =  "DES";
		try{
			android.util.Log.d("cipherName-3746", javax.crypto.Cipher.getInstance(cipherName3746).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		headerView=getActivity().getLayoutInflater().inflate(R.layout.header_onboarding_login, list, false);
		clearBtn=headerView.findViewById(R.id.search_clear);
		searchEdit=headerView.findViewById(R.id.search_edit);
		searchEdit.setOnEditorActionListener(this::onSearchEnterPressed);
		searchEdit.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){
				searchEdit.removeCallbacks(searchDebouncer);
				searchEdit.postDelayed(searchDebouncer, 300);

				if(s.length()>0){
					fakeInstance.domain=fakeInstance.normalizedDomain=s.toString();
					fakeInstance.description=getString(R.string.loading_instance);
					if(filteredData.size()>0 && filteredData.get(0)==fakeInstance){
						if(list.findViewHolderForAdapterPosition(1) instanceof InstanceViewHolder ivh){
							ivh.rebind();
						}
					}
					if(filteredData.isEmpty()){
						filteredData.add(fakeInstance);
						adapter.notifyItemInserted(0);
					}
					clearBtn.setVisibility(View.VISIBLE);
				}else{
					clearBtn.setVisibility(View.GONE);
				}
			}

			@Override
			public void afterTextChanged(Editable s){
			}
		});
		clearBtn.setOnClickListener(v->searchEdit.setText(""));

		mergeAdapter=new MergeRecyclerAdapter();
		mergeAdapter.addAdapter(new SingleViewRecyclerAdapter(headerView));
		mergeAdapter.addAdapter(adapter=new InstancesAdapter());
		return mergeAdapter;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		String cipherName3747 =  "DES";
		try{
			android.util.Log.d("cipherName-3747", javax.crypto.Cipher.getInstance(cipherName3747).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setStatusBarColor(UiUtils.getThemeColor(getActivity(), R.attr.colorM3Background));

		list.addItemDecoration(new RecyclerView.ItemDecoration(){
			@Override
			public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state){
				String cipherName3748 =  "DES";
				try{
					android.util.Log.d("cipherName-3748", javax.crypto.Cipher.getInstance(cipherName3748).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(parent.getChildViewHolder(view) instanceof InstanceViewHolder){
					String cipherName3749 =  "DES";
					try{
						android.util.Log.d("cipherName-3749", javax.crypto.Cipher.getInstance(cipherName3749).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					outRect.left=outRect.right=V.dp(16);
				}
			}
		});
		((UsableRecyclerView)list).setDrawSelectorOnTop(true);
	}

	private class InstancesAdapter extends UsableRecyclerView.Adapter<InstanceViewHolder>{
		public InstancesAdapter(){
			super(imgLoader);
			String cipherName3750 =  "DES";
			try{
				android.util.Log.d("cipherName-3750", javax.crypto.Cipher.getInstance(cipherName3750).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
		}

		@NonNull
		@Override
		public InstanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
			String cipherName3751 =  "DES";
			try{
				android.util.Log.d("cipherName-3751", javax.crypto.Cipher.getInstance(cipherName3751).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new InstanceViewHolder();
		}

		@Override
		public void onBindViewHolder(InstanceViewHolder holder, int position){
			holder.bind(filteredData.get(position));
			String cipherName3752 =  "DES";
			try{
				android.util.Log.d("cipherName-3752", javax.crypto.Cipher.getInstance(cipherName3752).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			super.onBindViewHolder(holder, position);
		}

		@Override
		public int getItemCount(){
			String cipherName3753 =  "DES";
			try{
				android.util.Log.d("cipherName-3753", javax.crypto.Cipher.getInstance(cipherName3753).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return filteredData.size();
		}

		@Override
		public int getItemViewType(int position){
			String cipherName3754 =  "DES";
			try{
				android.util.Log.d("cipherName-3754", javax.crypto.Cipher.getInstance(cipherName3754).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return -1;
		}
	}

	private class InstanceViewHolder extends BindableViewHolder<CatalogInstance> implements UsableRecyclerView.Clickable{
		private final TextView title, description;
		private final RadioButton radioButton;

		public InstanceViewHolder(){
			super(getActivity(), R.layout.item_instance_login, list);
			String cipherName3755 =  "DES";
			try{
				android.util.Log.d("cipherName-3755", javax.crypto.Cipher.getInstance(cipherName3755).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			title=findViewById(R.id.title);
			description=findViewById(R.id.description);
			radioButton=findViewById(R.id.radiobtn);
			radioButton.setMinWidth(0);
			radioButton.setMinHeight(0);

			itemView.setOutlineProvider(new ViewOutlineProvider(){
				@Override
				public void getOutline(View view, Outline outline){
					String cipherName3756 =  "DES";
					try{
						android.util.Log.d("cipherName-3756", javax.crypto.Cipher.getInstance(cipherName3756).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					outline.setRoundRect(0, getAbsoluteAdapterPosition()==1 ? 0 : V.dp(-4), view.getWidth(), view.getHeight()+(getAbsoluteAdapterPosition()==filteredData.size() ? 0 : V.dp(4)), V.dp(4));
				}
			});
			itemView.setClipToOutline(true);
		}

		@Override
		public void onBind(CatalogInstance item){
			String cipherName3757 =  "DES";
			try{
				android.util.Log.d("cipherName-3757", javax.crypto.Cipher.getInstance(cipherName3757).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			title.setText(item.normalizedDomain);
			description.setText(item.description);
			radioButton.setChecked(chosenInstance==item);
		}

		@Override
		public void onClick(){
			String cipherName3758 =  "DES";
			try{
				android.util.Log.d("cipherName-3758", javax.crypto.Cipher.getInstance(cipherName3758).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(chosenInstance==item)
				return;
			if(chosenInstance!=null){
				int idx=filteredData.indexOf(chosenInstance);
				if(idx!=-1){
					boolean found=false;
					for(int i=0;i<list.getChildCount();i++){
						RecyclerView.ViewHolder holder=list.getChildViewHolder(list.getChildAt(i));
						if(holder.getAbsoluteAdapterPosition()==mergeAdapter.getPositionForAdapter(adapter)+idx && holder instanceof InstanceViewHolder ivh){
							ivh.radioButton.setChecked(false);
							found=true;
							break;
						}
					}
					if(!found)
						adapter.notifyItemChanged(idx);
				}
			}
			radioButton.setChecked(true);
			if(chosenInstance==null)
				nextButton.setEnabled(true);
			chosenInstance=item;
			loadInstanceInfo(chosenInstance.domain, false);
		}
	}
}
