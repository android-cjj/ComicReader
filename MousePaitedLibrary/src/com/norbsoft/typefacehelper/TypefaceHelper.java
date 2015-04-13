package com.norbsoft.typefacehelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Helper class for setting custom typefaces to {@link android.view.View} and
 * {@link android.view.ViewGroup}. Supports String styling using {@link android.text.SpannableString}
 *
 * Applies typeface to all child views recursively.
 *
 * For detailed information and guide: https://github.com/jskierbi/android-typeface-helper
 *
 * @author Jakub Skierbiszewski
 */
public class TypefaceHelper {

	private static TypefaceCollection sDefaultTypefaceCollection;

	/**
	 * Initialize helper with typeface collection.
	 * This should be called inside {@link android.app.Application#onCreate()}
	 * @param typefaceCollection
	 */
	public static void init(TypefaceCollection typefaceCollection) {
		sDefaultTypefaceCollection = typefaceCollection;
	}

	/**
	 * Return spannable string with default typeface style (style: Typeface.NORMAL)
	 * see: http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
	 *
	 * @param context to obtain string resource
	 * @param strResId string resource id, content
	 *
	 * @throws IllegalStateException {@link #init(TypefaceCollection)} has not been called
	 * @return SpannableString that can be used in TextView.setText() method
	 */
	public static SpannableString typeface(Context context, int strResId) {
		if (sDefaultTypefaceCollection == null) {
			throw new IllegalStateException("Default typeface collection not initialized. Forgot to call init()?");
		}
		return typeface(context.getString(strResId), sDefaultTypefaceCollection, Typeface.NORMAL);
	}

	/**
	 * Return spannable string with typeface in certain style
	 * see: http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
	 *
	 * @param context to obtain string resource
	 * @param strResId string resource id, content
	 * @param style    Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC or Typeface.BOLD_ITALIC
	 *
	 * @throws IllegalStateException {@link #init(TypefaceCollection)} has not been calledC
	 * @return SpannableString that can be used in TextView.setText() method
	 */
	public static SpannableString typeface(Context context, int strResId, int style) {
		if (sDefaultTypefaceCollection == null) {
			throw new IllegalStateException("Default typeface collection not initialized. Forgot to call init()?");
		}
		return typeface(context.getString(strResId), sDefaultTypefaceCollection, style);
	}

	/**
	 * Return spannable string with typeface in certain style
	 * see: http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
	 *
	 * @param context to obtain string resource
	 * @param strResId string resource id, content
	 * @param collection TypefaceCollection instance
	 * @param style    Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC or Typeface.BOLD_ITALIC
	 *
	 * @return SpannableString that can be used in TextView.setText() method
	 */
	public static SpannableString typeface(Context context, int strResId, TypefaceCollection collection, int style) {
		return typeface(context.getString(strResId), collection, style);
	}

	/**
	 * Return spannable string with default typeface style (style: Typeface.NORMAL)
	 * see: http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
	 *
	 * @param sequence to typeface typeface to
	 *
	 * @throws IllegalStateException {@link #init(TypefaceCollection)} has not been called
	 * @return SpannableString that can be used in TextView.setText() method
	 */
	public static SpannableString typeface(CharSequence sequence) {
		if (sDefaultTypefaceCollection == null) {
			throw new IllegalStateException("Default typeface collection not initialized. Forgot to call init()?");
		}
		return typeface(sequence, sDefaultTypefaceCollection, Typeface.NORMAL);
	}

	/**
	 * Return spannable string with applied typeface in certain style
	 * see: http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
	 *
	 * @param sequence    content
	 * @param typefaceCollection Collection of typefaces
	 * @param style     Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC or Typeface.BOLD_ITALIC
	 * @throws IllegalArgumentException when invalid style is passed
	 * @return SpannableString that can be used in TextView.setText() method
	 */
	public static SpannableString typeface(CharSequence sequence, TypefaceCollection typefaceCollection, int style) {
		checkTypefaceStyleThrowing(style);
		SpannableString s = new SpannableString(sequence);
		s.setSpan(new TypefaceSpan(typefaceCollection.getTypeface(style)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return s;
	}

	/**
	 * Return spannable string with default typeface style (style: Typeface.NORMAL)
	 * see: http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
	 *
	 * @param sequence    content
	 * @param typefaceCollection Collection of typefaces
	 * @return SpannableString that can be used in TextView.setText() method
	 */
	public static SpannableString typeface(CharSequence sequence, TypefaceCollection typefaceCollection) {
		return typeface(sequence, typefaceCollection, Typeface.NORMAL);
	}

	/**
	 * Apply typefaces to main acitivty view (android.R.id.content).
	 * @throws IllegalStateException {@link #init(TypefaceCollection)} has not been called
	 * @param activity to typeface custom typefaces to
	 */
	public static void typeface(Activity activity) {
		if (sDefaultTypefaceCollection == null) {
			throw new IllegalStateException("Default typeface collection not initialized. Forgot to call init()?");
		}
		typeface(activity.findViewById(android.R.id.content), sDefaultTypefaceCollection);
	}

	/**
	 * Apply typefaces to view
	 * @throws IllegalStateException {@link #init(TypefaceCollection)} has not been called
	 * @param view to typeface custom typefaces to
	 */
	public static void typeface(View view) {
		if (sDefaultTypefaceCollection == null) {
			throw new IllegalStateException("Default typeface collection not initialized. Forgot to call init()?");
		}
		typeface(view, sDefaultTypefaceCollection);
	}

	/**
	 * Apply typefaces to main acitivty view (android.R.id.content).
	 * @param activity to typeface custom typefaces to
	 * @param typefaceCollection to obtain typefaces SparseArray.
	 */
	public static void typeface(Activity activity, TypefaceCollection typefaceCollection) {
		typeface(activity.findViewById(android.R.id.content), typefaceCollection);
	}

	/**
	 * Apply fonts from map to all children of view (or view itself)
	 *
	 * @param view view for which to typeface fonts
	 * @param typefaceCollection Collection of typefaces
	 */
	public static void typeface(View view, TypefaceCollection typefaceCollection) {

		if (view instanceof ViewGroup) {
			applyTypeface((ViewGroup) view, typefaceCollection);
		} else {
			applyForView(view, typefaceCollection);
		}
	}

	/**
	 * Check if typeface style int is one of:
	 * <ul>
	 *      <li>{@link android.graphics.Typeface#NORMAL}</li>
	 *      <li>{@link android.graphics.Typeface#BOLD}</li>
	 *      <li>{@link android.graphics.Typeface#ITALIC}</li>
	 *      <li>{@link android.graphics.Typeface#BOLD_ITALIC}</li>
	 * </ul>
	 * @param style
	 */
	private static void checkTypefaceStyleThrowing(int style) {
		switch (style) {
		case Typeface.NORMAL:
		case Typeface.BOLD:
		case Typeface.ITALIC:
		case Typeface.BOLD_ITALIC:
			break;
		default:
			throw new IllegalArgumentException("Style have to be in (Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC, Typeface.BOLD_ITALIC)");
		}
	}

	/**
	 * Apply typeface to all ViewGroup childs
	 *
	 * @param viewGroup to typeface typeface
	 * @param typefaceCollection typeface collection
	 */
	private static void applyTypeface(ViewGroup viewGroup, TypefaceCollection typefaceCollection) {

		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View childView = viewGroup.getChildAt(i);
			if (childView instanceof ViewGroup) {
				applyTypeface((ViewGroup) childView, typefaceCollection);
			} else {
				applyForView(childView, typefaceCollection);
			}
		}
	}

	/**
	 * Apply typeface to single view
	 *
	 * @param view      to typeface typeface
	 * @param typefaceCollection typeface collection
	 */
	private static void applyForView(View view, TypefaceCollection typefaceCollection) {

		if (view instanceof TextView) {
			TextView textView = (TextView) view;
			Typeface oldTypeface = textView.getTypeface();
			final int style = oldTypeface == null ? Typeface.NORMAL : oldTypeface.getStyle();
			textView.setTypeface(typefaceCollection.getTypeface(style));
			textView.setPaintFlags(textView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}
	}

	private static class TypefaceSpan extends MetricAffectingSpan {

		Typeface typeface;

		TypefaceSpan(Typeface typeface) {
			this.typeface = typeface;
		}

		@Override public void updateMeasureState(TextPaint p) {
			p.setTypeface(typeface);
			p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}

		@Override public void updateDrawState(TextPaint tp) {
			tp.setTypeface(typeface);
			tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}
	}
}
