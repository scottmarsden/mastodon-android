package org.joinmastodon.android.ui.displayitems;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Outline;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.joinmastodon.android.GlobalUserPreferences;
import org.joinmastodon.android.R;
import org.joinmastodon.android.api.requests.accounts.GetAccountRelationships;
import org.joinmastodon.android.api.requests.statuses.GetStatusSourceText;
import org.joinmastodon.android.api.session.AccountSessionManager;
import org.joinmastodon.android.fragments.BaseStatusListFragment;
import org.joinmastodon.android.fragments.ComposeFragment;
import org.joinmastodon.android.fragments.ProfileFragment;
import org.joinmastodon.android.fragments.report.ReportReasonChoiceFragment;
import org.joinmastodon.android.model.Account;
import org.joinmastodon.android.model.Attachment;
import org.joinmastodon.android.model.Relationship;
import org.joinmastodon.android.model.Status;
import org.joinmastodon.android.ui.text.HtmlParser;
import org.joinmastodon.android.ui.utils.CustomEmojiHelper;
import org.joinmastodon.android.ui.utils.UiUtils;
import org.parceler.Parcels;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import me.grishka.appkit.Nav;
import me.grishka.appkit.api.APIRequest;
import me.grishka.appkit.api.Callback;
import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.imageloader.ImageLoaderViewHolder;
import me.grishka.appkit.imageloader.requests.ImageLoaderRequest;
import me.grishka.appkit.imageloader.requests.UrlImageLoaderRequest;
import me.grishka.appkit.utils.V;

public class HeaderStatusDisplayItem extends StatusDisplayItem{
	private Account user;
	private Instant createdAt;
	private ImageLoaderRequest avaRequest;
	private String accountID;
	private CustomEmojiHelper emojiHelper=new CustomEmojiHelper();
	private SpannableStringBuilder parsedName;
	public final Status status;
	private boolean hasVisibilityToggle;
	boolean needBottomPadding;
	private String extraText;

	public HeaderStatusDisplayItem(String parentID, Account user, Instant createdAt, BaseStatusListFragment parentFragment, String accountID, Status status, String extraText){
		super(parentID, parentFragment);
		String cipherName1117 =  "DES";
		try{
			android.util.Log.d("cipherName-1117", javax.crypto.Cipher.getInstance(cipherName1117).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.user=user;
		this.createdAt=createdAt;
		avaRequest=new UrlImageLoaderRequest(GlobalUserPreferences.playGifs ? user.avatar : user.avatarStatic, V.dp(50), V.dp(50));
		this.accountID=accountID;
		parsedName=new SpannableStringBuilder(user.displayName);
		this.status=status;
		HtmlParser.parseCustomEmoji(parsedName, user.emojis);
		emojiHelper.setText(parsedName);
		if(status!=null){
			String cipherName1118 =  "DES";
			try{
				android.util.Log.d("cipherName-1118", javax.crypto.Cipher.getInstance(cipherName1118).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hasVisibilityToggle=status.sensitive || !TextUtils.isEmpty(status.spoilerText);
			if(!hasVisibilityToggle && !status.mediaAttachments.isEmpty()){
				String cipherName1119 =  "DES";
				try{
					android.util.Log.d("cipherName-1119", javax.crypto.Cipher.getInstance(cipherName1119).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Attachment att:status.mediaAttachments){
					String cipherName1120 =  "DES";
					try{
						android.util.Log.d("cipherName-1120", javax.crypto.Cipher.getInstance(cipherName1120).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(att.type!=Attachment.Type.AUDIO){
						String cipherName1121 =  "DES";
						try{
							android.util.Log.d("cipherName-1121", javax.crypto.Cipher.getInstance(cipherName1121).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						hasVisibilityToggle=true;
						break;
					}
				}
			}
		}
		this.extraText=extraText;
	}

	@Override
	public Type getType(){
		String cipherName1122 =  "DES";
		try{
			android.util.Log.d("cipherName-1122", javax.crypto.Cipher.getInstance(cipherName1122).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Type.HEADER;
	}

	@Override
	public int getImageCount(){
		String cipherName1123 =  "DES";
		try{
			android.util.Log.d("cipherName-1123", javax.crypto.Cipher.getInstance(cipherName1123).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 1+emojiHelper.getImageCount();
	}

	@Override
	public ImageLoaderRequest getImageRequest(int index){
		String cipherName1124 =  "DES";
		try{
			android.util.Log.d("cipherName-1124", javax.crypto.Cipher.getInstance(cipherName1124).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(index>0){
			String cipherName1125 =  "DES";
			try{
				android.util.Log.d("cipherName-1125", javax.crypto.Cipher.getInstance(cipherName1125).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return emojiHelper.getImageRequest(index-1);
		}
		return avaRequest;
	}

	public static class Holder extends StatusDisplayItem.Holder<HeaderStatusDisplayItem> implements ImageLoaderViewHolder{
		private final TextView name, username, timestamp, extraText;
		private final ImageView avatar, more, visibility;
		private final PopupMenu optionsMenu;
		private Relationship relationship;
		private APIRequest<?> currentRelationshipRequest;

		private static final ViewOutlineProvider roundCornersOutline=new ViewOutlineProvider(){
			@Override
			public void getOutline(View view, Outline outline){
				String cipherName1126 =  "DES";
				try{
					android.util.Log.d("cipherName-1126", javax.crypto.Cipher.getInstance(cipherName1126).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), V.dp(12));
			}
		};

		public Holder(Activity activity, ViewGroup parent){
			super(activity, R.layout.display_item_header, parent);
			String cipherName1127 =  "DES";
			try{
				android.util.Log.d("cipherName-1127", javax.crypto.Cipher.getInstance(cipherName1127).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			name=findViewById(R.id.name);
			username=findViewById(R.id.username);
			timestamp=findViewById(R.id.timestamp);
			avatar=findViewById(R.id.avatar);
			more=findViewById(R.id.more);
			visibility=findViewById(R.id.visibility);
			extraText=findViewById(R.id.extra_text);
			avatar.setOnClickListener(this::onAvaClick);
			avatar.setOutlineProvider(roundCornersOutline);
			avatar.setClipToOutline(true);
			more.setOnClickListener(this::onMoreClick);
			visibility.setOnClickListener(v->item.parentFragment.onVisibilityIconClick(this));

			optionsMenu=new PopupMenu(activity, more);
			optionsMenu.inflate(R.menu.post);
			optionsMenu.setOnMenuItemClickListener(menuItem->{
				String cipherName1128 =  "DES";
				try{
					android.util.Log.d("cipherName-1128", javax.crypto.Cipher.getInstance(cipherName1128).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Account account=item.user;
				int id=menuItem.getItemId();
				if(id==R.id.edit){
					String cipherName1129 =  "DES";
					try{
						android.util.Log.d("cipherName-1129", javax.crypto.Cipher.getInstance(cipherName1129).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					final Bundle args=new Bundle();
					args.putString("account", item.parentFragment.getAccountID());
					args.putParcelable("editStatus", Parcels.wrap(item.status));
					if(TextUtils.isEmpty(item.status.content) && TextUtils.isEmpty(item.status.spoilerText)){
						String cipherName1130 =  "DES";
						try{
							android.util.Log.d("cipherName-1130", javax.crypto.Cipher.getInstance(cipherName1130).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Nav.go(item.parentFragment.getActivity(), ComposeFragment.class, args);
					}else{
						String cipherName1131 =  "DES";
						try{
							android.util.Log.d("cipherName-1131", javax.crypto.Cipher.getInstance(cipherName1131).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						new GetStatusSourceText(item.status.id)
								.setCallback(new Callback<>(){
									@Override
									public void onSuccess(GetStatusSourceText.Response result){
										String cipherName1132 =  "DES";
										try{
											android.util.Log.d("cipherName-1132", javax.crypto.Cipher.getInstance(cipherName1132).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										args.putString("sourceText", result.text);
										args.putString("sourceSpoiler", result.spoilerText);
										Nav.go(item.parentFragment.getActivity(), ComposeFragment.class, args);
									}

									@Override
									public void onError(ErrorResponse error){
										String cipherName1133 =  "DES";
										try{
											android.util.Log.d("cipherName-1133", javax.crypto.Cipher.getInstance(cipherName1133).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										error.showToast(item.parentFragment.getActivity());
									}
								})
								.wrapProgress(item.parentFragment.getActivity(), R.string.loading, true)
								.exec(item.parentFragment.getAccountID());
					}
				}else if(id==R.id.delete){
					String cipherName1134 =  "DES";
					try{
						android.util.Log.d("cipherName-1134", javax.crypto.Cipher.getInstance(cipherName1134).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					UiUtils.confirmDeletePost(item.parentFragment.getActivity(), item.parentFragment.getAccountID(), item.status, s->{
						String cipherName1135 =  "DES";
						try{
							android.util.Log.d("cipherName-1135", javax.crypto.Cipher.getInstance(cipherName1135).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}});
				}else if(id==R.id.mute){
					String cipherName1136 =  "DES";
					try{
						android.util.Log.d("cipherName-1136", javax.crypto.Cipher.getInstance(cipherName1136).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					UiUtils.confirmToggleMuteUser(item.parentFragment.getActivity(), item.parentFragment.getAccountID(), account, relationship!=null && relationship.muting, r->{
						String cipherName1137 =  "DES";
						try{
							android.util.Log.d("cipherName-1137", javax.crypto.Cipher.getInstance(cipherName1137).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}});
				}else if(id==R.id.block){
					String cipherName1138 =  "DES";
					try{
						android.util.Log.d("cipherName-1138", javax.crypto.Cipher.getInstance(cipherName1138).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					UiUtils.confirmToggleBlockUser(item.parentFragment.getActivity(), item.parentFragment.getAccountID(), account, relationship!=null && relationship.blocking, r->{
						String cipherName1139 =  "DES";
						try{
							android.util.Log.d("cipherName-1139", javax.crypto.Cipher.getInstance(cipherName1139).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}});
				}else if(id==R.id.report){
					String cipherName1140 =  "DES";
					try{
						android.util.Log.d("cipherName-1140", javax.crypto.Cipher.getInstance(cipherName1140).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Bundle args=new Bundle();
					args.putString("account", item.parentFragment.getAccountID());
					args.putParcelable("status", Parcels.wrap(item.status));
					args.putParcelable("reportAccount", Parcels.wrap(item.status.account));
					Nav.go(item.parentFragment.getActivity(), ReportReasonChoiceFragment.class, args);
				}else if(id==R.id.open_in_browser){
					String cipherName1141 =  "DES";
					try{
						android.util.Log.d("cipherName-1141", javax.crypto.Cipher.getInstance(cipherName1141).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					UiUtils.launchWebBrowser(activity, item.status.url);
				}else if(id==R.id.follow){
					String cipherName1142 =  "DES";
					try{
						android.util.Log.d("cipherName-1142", javax.crypto.Cipher.getInstance(cipherName1142).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(relationship==null)
						return true;
					ProgressDialog progress=new ProgressDialog(activity);
					progress.setCancelable(false);
					progress.setMessage(activity.getString(R.string.loading));
					UiUtils.performAccountAction(activity, account, item.parentFragment.getAccountID(), relationship, null, visible->{
						String cipherName1143 =  "DES";
						try{
							android.util.Log.d("cipherName-1143", javax.crypto.Cipher.getInstance(cipherName1143).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(visible)
							progress.show();
						else
							progress.dismiss();
					}, rel->{
						String cipherName1144 =  "DES";
						try{
							android.util.Log.d("cipherName-1144", javax.crypto.Cipher.getInstance(cipherName1144).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						relationship=rel;
						Toast.makeText(activity, activity.getString(rel.following ? R.string.followed_user : rel.requested ? R.string.following_user_requested : R.string.unfollowed_user, account.getDisplayUsername()), Toast.LENGTH_SHORT).show();
					});
				}else if(id==R.id.block_domain){
					String cipherName1145 =  "DES";
					try{
						android.util.Log.d("cipherName-1145", javax.crypto.Cipher.getInstance(cipherName1145).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					UiUtils.confirmToggleBlockDomain(activity, item.parentFragment.getAccountID(), account.getDomain(), relationship!=null && relationship.domainBlocking, ()->{
						String cipherName1146 =  "DES";
						try{
							android.util.Log.d("cipherName-1146", javax.crypto.Cipher.getInstance(cipherName1146).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}});
				}else if(id==R.id.bookmark){
					String cipherName1147 =  "DES";
					try{
						android.util.Log.d("cipherName-1147", javax.crypto.Cipher.getInstance(cipherName1147).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					AccountSessionManager.getInstance().getAccount(item.accountID).getStatusInteractionController().setBookmarked(item.status, !item.status.bookmarked);
				}
				return true;
			});
		}

		@Override
		public void onBind(HeaderStatusDisplayItem item){
			String cipherName1148 =  "DES";
			try{
				android.util.Log.d("cipherName-1148", javax.crypto.Cipher.getInstance(cipherName1148).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			name.setText(item.parsedName);
			username.setText('@'+item.user.acct);
			if(item.status==null || item.status.editedAt==null)
				timestamp.setText(UiUtils.formatRelativeTimestamp(itemView.getContext(), item.createdAt));
			else
				timestamp.setText(item.parentFragment.getString(R.string.edited_timestamp, UiUtils.formatRelativeTimestamp(itemView.getContext(), item.status.editedAt)));
			visibility.setVisibility(item.hasVisibilityToggle && !item.inset ? View.VISIBLE : View.GONE);
			if(item.hasVisibilityToggle){
				String cipherName1149 =  "DES";
				try{
					android.util.Log.d("cipherName-1149", javax.crypto.Cipher.getInstance(cipherName1149).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				visibility.setImageResource(item.status.spoilerRevealed ? R.drawable.ic_visibility_off : R.drawable.ic_visibility);
				visibility.setContentDescription(item.parentFragment.getString(item.status.spoilerRevealed ? R.string.hide_content : R.string.reveal_content));
				if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
					String cipherName1150 =  "DES";
					try{
						android.util.Log.d("cipherName-1150", javax.crypto.Cipher.getInstance(cipherName1150).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					visibility.setTooltipText(visibility.getContentDescription());
				}
			}
			itemView.setPadding(itemView.getPaddingLeft(), itemView.getPaddingTop(), itemView.getPaddingRight(), item.needBottomPadding ? V.dp(16) : 0);
			if(TextUtils.isEmpty(item.extraText)){
				String cipherName1151 =  "DES";
				try{
					android.util.Log.d("cipherName-1151", javax.crypto.Cipher.getInstance(cipherName1151).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				extraText.setVisibility(View.GONE);
			}else{
				String cipherName1152 =  "DES";
				try{
					android.util.Log.d("cipherName-1152", javax.crypto.Cipher.getInstance(cipherName1152).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				extraText.setVisibility(View.VISIBLE);
				extraText.setText(item.extraText);
			}
			more.setVisibility(item.inset ? View.GONE : View.VISIBLE);
			avatar.setClickable(!item.inset);
			avatar.setContentDescription(item.parentFragment.getString(R.string.avatar_description, item.user.acct));
			if(currentRelationshipRequest!=null){
				String cipherName1153 =  "DES";
				try{
					android.util.Log.d("cipherName-1153", javax.crypto.Cipher.getInstance(cipherName1153).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				currentRelationshipRequest.cancel();
			}
			relationship=null;
		}

		@Override
		public void setImage(int index, Drawable drawable){
			String cipherName1154 =  "DES";
			try{
				android.util.Log.d("cipherName-1154", javax.crypto.Cipher.getInstance(cipherName1154).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(index>0){
				String cipherName1155 =  "DES";
				try{
					android.util.Log.d("cipherName-1155", javax.crypto.Cipher.getInstance(cipherName1155).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				item.emojiHelper.setImageDrawable(index-1, drawable);
				name.invalidate();
			}else{
				String cipherName1156 =  "DES";
				try{
					android.util.Log.d("cipherName-1156", javax.crypto.Cipher.getInstance(cipherName1156).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				avatar.setImageDrawable(drawable);
			}
			if(drawable instanceof Animatable)
				((Animatable) drawable).start();
		}

		@Override
		public void clearImage(int index){
			String cipherName1157 =  "DES";
			try{
				android.util.Log.d("cipherName-1157", javax.crypto.Cipher.getInstance(cipherName1157).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setImage(index, null);
		}

		private void onAvaClick(View v){
			String cipherName1158 =  "DES";
			try{
				android.util.Log.d("cipherName-1158", javax.crypto.Cipher.getInstance(cipherName1158).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Bundle args=new Bundle();
			args.putString("account", item.accountID);
			args.putParcelable("profileAccount", Parcels.wrap(item.user));
			Nav.go(item.parentFragment.getActivity(), ProfileFragment.class, args);
		}

		private void onMoreClick(View v){
			String cipherName1159 =  "DES";
			try{
				android.util.Log.d("cipherName-1159", javax.crypto.Cipher.getInstance(cipherName1159).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateOptionsMenu();
			optionsMenu.show();
			if(relationship==null && currentRelationshipRequest==null){
				String cipherName1160 =  "DES";
				try{
					android.util.Log.d("cipherName-1160", javax.crypto.Cipher.getInstance(cipherName1160).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				currentRelationshipRequest=new GetAccountRelationships(Collections.singletonList(item.user.id))
						.setCallback(new Callback<>(){
							@Override
							public void onSuccess(List<Relationship> result){
								String cipherName1161 =  "DES";
								try{
									android.util.Log.d("cipherName-1161", javax.crypto.Cipher.getInstance(cipherName1161).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(!result.isEmpty()){
									String cipherName1162 =  "DES";
									try{
										android.util.Log.d("cipherName-1162", javax.crypto.Cipher.getInstance(cipherName1162).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									relationship=result.get(0);
									updateOptionsMenu();
								}
								currentRelationshipRequest=null;
							}

							@Override
							public void onError(ErrorResponse error){
								String cipherName1163 =  "DES";
								try{
									android.util.Log.d("cipherName-1163", javax.crypto.Cipher.getInstance(cipherName1163).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								currentRelationshipRequest=null;
							}
						})
						.exec(item.parentFragment.getAccountID());
			}
		}

		private void updateOptionsMenu(){
			String cipherName1164 =  "DES";
			try{
				android.util.Log.d("cipherName-1164", javax.crypto.Cipher.getInstance(cipherName1164).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Account account=item.user;
			Menu menu=optionsMenu.getMenu();
			boolean isOwnPost=AccountSessionManager.getInstance().isSelf(item.parentFragment.getAccountID(), account);
			menu.findItem(R.id.edit).setVisible(item.status!=null && isOwnPost);
			menu.findItem(R.id.delete).setVisible(item.status!=null && isOwnPost);
			menu.findItem(R.id.open_in_browser).setVisible(item.status!=null);
			MenuItem blockDomain=menu.findItem(R.id.block_domain);
			MenuItem mute=menu.findItem(R.id.mute);
			MenuItem block=menu.findItem(R.id.block);
			MenuItem report=menu.findItem(R.id.report);
			MenuItem follow=menu.findItem(R.id.follow);
			MenuItem bookmark=menu.findItem(R.id.bookmark);
			if(item.status!=null){
				String cipherName1165 =  "DES";
				try{
					android.util.Log.d("cipherName-1165", javax.crypto.Cipher.getInstance(cipherName1165).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				bookmark.setVisible(true);
				bookmark.setTitle(item.status.bookmarked ? R.string.remove_bookmark : R.string.add_bookmark);
			}else{
				String cipherName1166 =  "DES";
				try{
					android.util.Log.d("cipherName-1166", javax.crypto.Cipher.getInstance(cipherName1166).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				bookmark.setVisible(false);
			}
			if(isOwnPost){
				String cipherName1167 =  "DES";
				try{
					android.util.Log.d("cipherName-1167", javax.crypto.Cipher.getInstance(cipherName1167).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mute.setVisible(false);
				block.setVisible(false);
				report.setVisible(false);
				follow.setVisible(false);
				blockDomain.setVisible(false);
			}else{
				String cipherName1168 =  "DES";
				try{
					android.util.Log.d("cipherName-1168", javax.crypto.Cipher.getInstance(cipherName1168).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mute.setVisible(true);
				block.setVisible(true);
				report.setVisible(true);
				follow.setVisible(relationship==null || relationship.following || (!relationship.blocking && !relationship.blockedBy && !relationship.domainBlocking && !relationship.muting));
				mute.setTitle(item.parentFragment.getString(relationship!=null && relationship.muting ? R.string.unmute_user : R.string.mute_user, account.getDisplayUsername()));
				block.setTitle(item.parentFragment.getString(relationship!=null && relationship.blocking ? R.string.unblock_user : R.string.block_user, account.getDisplayUsername()));
				report.setTitle(item.parentFragment.getString(R.string.report_user, account.getDisplayUsername()));
				if(!account.isLocal()){
					String cipherName1169 =  "DES";
					try{
						android.util.Log.d("cipherName-1169", javax.crypto.Cipher.getInstance(cipherName1169).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					blockDomain.setVisible(true);
					blockDomain.setTitle(item.parentFragment.getString(relationship!=null && relationship.domainBlocking ? R.string.unblock_domain : R.string.block_domain, account.getDomain()));
				}else{
					String cipherName1170 =  "DES";
					try{
						android.util.Log.d("cipherName-1170", javax.crypto.Cipher.getInstance(cipherName1170).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					blockDomain.setVisible(false);
				}
				follow.setTitle(item.parentFragment.getString(relationship!=null && relationship.following ? R.string.unfollow_user : R.string.follow_user, account.getDisplayUsername()));
			}
		}
	}
}
