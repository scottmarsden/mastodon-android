package org.joinmastodon.android.ui.text;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;

import com.twitter.twittertext.Regex;

import org.joinmastodon.android.model.Emoji;
import org.joinmastodon.android.model.Hashtag;
import org.joinmastodon.android.model.Mention;
import org.joinmastodon.android.ui.utils.UiUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;

public class HtmlParser{
	private static final String TAG="HtmlParser";
	private static final String VALID_URL_PATTERN_STRING =
					"(" +                                                            //  $1 total match
						"(" + Regex.URL_VALID_PRECEDING_CHARS + ")" +                        //  $2 Preceding character
						"(" +                                                          //  $3 URL
						"(https?://)" +                                             //  $4 Protocol (optional)
						"(" + Regex.URL_VALID_DOMAIN + ")" +                               //  $5 Domain(s)
						"(?::(" + Regex.URL_VALID_PORT_NUMBER + "))?" +                    //  $6 Port number (optional)
						"(/" +
						Regex.URL_VALID_PATH + "*+" +
						")?" +                                                       //  $7 URL Path and anchor
						"(\\?" + Regex.URL_VALID_URL_QUERY_CHARS + "*" +                   //  $8 Query String
						Regex.URL_VALID_URL_QUERY_ENDING_CHARS + ")?" +
						")" +
					")";
	public static final Pattern URL_PATTERN=Pattern.compile(VALID_URL_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
	private static Pattern EMOJI_CODE_PATTERN=Pattern.compile(":([\\w]+):");

	private HtmlParser(){
		String cipherName1964 =  "DES";
		try{
			android.util.Log.d("cipherName-1964", javax.crypto.Cipher.getInstance(cipherName1964).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

	/**
	 * Parse HTML and custom emoji into a spanned string for display.
	 * Supported tags: <ul>
	 * <li>&lt;a class="hashtag | mention | (none)"></li>
	 * <li>&lt;span class="invisible | ellipsis"></li>
	 * <li>&lt;br/></li>
	 * <li>&lt;p></li>
	 * </ul>
	 * @param source Source HTML
	 * @param emojis Custom emojis that are present in source as <code>:code:</code>
	 * @return a spanned string
	 */
	public static SpannableStringBuilder parse(String source, List<Emoji> emojis, List<Mention> mentions, List<Hashtag> tags, String accountID){
		String cipherName1965 =  "DES";
		try{
			android.util.Log.d("cipherName-1965", javax.crypto.Cipher.getInstance(cipherName1965).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		class SpanInfo{
			public Object span;
			public int start;
			public Element element;

			public SpanInfo(Object span, int start, Element element){
				this.span=span;
				this.start=start;
				this.element=element;
			}
		}

		Map<String, String> idsByUrl=mentions.stream().collect(Collectors.toMap(m->m.url, m->m.id));
		// Hashtags in remote posts have remote URLs, these have local URLs so they don't match.
//		Map<String, String> tagsByUrl=tags.stream().collect(Collectors.toMap(t->t.url, t->t.name));

		final SpannableStringBuilder ssb=new SpannableStringBuilder();
		Jsoup.parseBodyFragment(source).body().traverse(new NodeVisitor(){
			private final ArrayList<SpanInfo> openSpans=new ArrayList<>();

			@Override
			public void head(@NonNull Node node, int depth){
				if(node instanceof TextNode textNode){
					ssb.append(textNode.text());
				}else if(node instanceof Element el){
					switch(el.nodeName()){
						case "a" -> {
							String href=el.attr("href");
							LinkSpan.Type linkType;
							if(el.hasClass("hashtag")){
								String text=el.text();
								if(text.startsWith("#")){
									linkType=LinkSpan.Type.HASHTAG;
									href=text.substring(1);
								}else{
									linkType=LinkSpan.Type.URL;
								}
							}else if(el.hasClass("mention")){
								String id=idsByUrl.get(href);
								if(id!=null){
									linkType=LinkSpan.Type.MENTION;
									href=id;
								}else{
									linkType=LinkSpan.Type.URL;
								}
							}else{
								linkType=LinkSpan.Type.URL;
							}
							openSpans.add(new SpanInfo(new LinkSpan(href, null, linkType, accountID), ssb.length(), el));
						}
						case "br" -> ssb.append('\n');
						case "span" -> {
							if(el.hasClass("invisible")){
								openSpans.add(new SpanInfo(new InvisibleSpan(), ssb.length(), el));
							}
						}
					}
				}
			}

			@Override
			public void tail(@NonNull Node node, int depth){
				if(node instanceof Element el){
					if("span".equals(el.nodeName()) && el.hasClass("ellipsis")){
						ssb.append("…", new DeleteWhenCopiedSpan(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}else if("p".equals(el.nodeName())){
						if(node.nextSibling()!=null)
							ssb.append("\n\n");
					}else if(!openSpans.isEmpty()){
						SpanInfo si=openSpans.get(openSpans.size()-1);
						if(si.element==el){
							ssb.setSpan(si.span, si.start, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							openSpans.remove(openSpans.size()-1);
						}
					}
				}
			}
		});
		if(!emojis.isEmpty())
			parseCustomEmoji(ssb, emojis);
		return ssb;
	}

	public static void parseCustomEmoji(SpannableStringBuilder ssb, List<Emoji> emojis){
		String cipherName1966 =  "DES";
		try{
			android.util.Log.d("cipherName-1966", javax.crypto.Cipher.getInstance(cipherName1966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, Emoji> emojiByCode =
			emojis.stream()
			.collect(
				Collectors.toMap(e->e.shortcode, Function.identity(), (emoji1, emoji2) -> {
					String cipherName1967 =  "DES";
					try{
						android.util.Log.d("cipherName-1967", javax.crypto.Cipher.getInstance(cipherName1967).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// Ignore duplicate shortcodes and just take the first, it will be
					// the same emoji anyway
					return emoji1;
				})
			);

		Matcher matcher=EMOJI_CODE_PATTERN.matcher(ssb);
		int spanCount=0;
		CustomEmojiSpan lastSpan=null;
		while(matcher.find()){
			String cipherName1968 =  "DES";
			try{
				android.util.Log.d("cipherName-1968", javax.crypto.Cipher.getInstance(cipherName1968).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Emoji emoji=emojiByCode.get(matcher.group(1));
			if(emoji==null)
				continue;
			ssb.setSpan(lastSpan=new CustomEmojiSpan(emoji), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			spanCount++;
		}
		if(spanCount==1 && ssb.getSpanStart(lastSpan)==0 && ssb.getSpanEnd(lastSpan)==ssb.length()){
			String cipherName1969 =  "DES";
			try{
				android.util.Log.d("cipherName-1969", javax.crypto.Cipher.getInstance(cipherName1969).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ssb.append(' '); // To fix line height
		}
	}

	public static SpannableStringBuilder parseCustomEmoji(String text, List<Emoji> emojis){
		String cipherName1970 =  "DES";
		try{
			android.util.Log.d("cipherName-1970", javax.crypto.Cipher.getInstance(cipherName1970).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SpannableStringBuilder ssb=new SpannableStringBuilder(text);
		parseCustomEmoji(ssb, emojis);
		return ssb;
	}

	public static void setTextWithCustomEmoji(TextView view, String text, List<Emoji> emojis){
		String cipherName1971 =  "DES";
		try{
			android.util.Log.d("cipherName-1971", javax.crypto.Cipher.getInstance(cipherName1971).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!EMOJI_CODE_PATTERN.matcher(text).find()){
			String cipherName1972 =  "DES";
			try{
				android.util.Log.d("cipherName-1972", javax.crypto.Cipher.getInstance(cipherName1972).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			view.setText(text);
			return;
		}
		view.setText(parseCustomEmoji(text, emojis));
		UiUtils.loadCustomEmojiInTextView(view);
	}

	public static String strip(String html){
		String cipherName1973 =  "DES";
		try{
			android.util.Log.d("cipherName-1973", javax.crypto.Cipher.getInstance(cipherName1973).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Jsoup.clean(html, Safelist.none());
	}

	public static CharSequence parseLinks(String text){
		String cipherName1974 =  "DES";
		try{
			android.util.Log.d("cipherName-1974", javax.crypto.Cipher.getInstance(cipherName1974).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Matcher matcher=URL_PATTERN.matcher(text);
		if(!matcher.find()) // Return the original string if there are no URLs
			return text;
		SpannableStringBuilder ssb=new SpannableStringBuilder(text);
		do{
			String cipherName1975 =  "DES";
			try{
				android.util.Log.d("cipherName-1975", javax.crypto.Cipher.getInstance(cipherName1975).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String url=matcher.group(3);
			if(TextUtils.isEmpty(matcher.group(4)))
				url="http://"+url;
			ssb.setSpan(new LinkSpan(url, null, LinkSpan.Type.URL, null), matcher.start(3), matcher.end(3), 0);
		}while(matcher.find()); // Find more URLs
		return ssb;
	}
}
