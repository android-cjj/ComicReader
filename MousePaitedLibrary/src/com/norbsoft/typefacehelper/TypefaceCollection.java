package com.norbsoft.typefacehelper;

import android.graphics.Typeface;
import android.util.SparseArray;

/**
 * Represents collection of typefaces. This includes 4 variants (styles):
 * <ul>
 *      <li>{@link android.graphics.Typeface#NORMAL}</li>
 *      <li>{@link android.graphics.Typeface#BOLD}</li>
 *      <li>{@link android.graphics.Typeface#ITALIC}</li>
 *      <li>{@link android.graphics.Typeface#BOLD_ITALIC}</li>
 * </ul>
 * @author Jakub Skierbiszewski
 */
public class TypefaceCollection {

	/** Builder interface */
	public static class Builder {

		/**
		 * This typeface is set in fist {@link #set(int, android.graphics.Typeface)}
		 * call and is used for all unset styles in {@link #create()} call
		 */
		private Typeface mDefaultTypeface;
		private TypefaceCollection mCollection = new TypefaceCollection();

		/**
		 * Sets typeface for certain style.
		 *
		 * @param typefaceStyle one of:
		 * <ul>
		 *      <li>{@link android.graphics.Typeface#NORMAL}</li>
		 *      <li>{@link android.graphics.Typeface#BOLD}</li>
		 *      <li>{@link android.graphics.Typeface#ITALIC}</li>
		 *      <li>{@link android.graphics.Typeface#BOLD_ITALIC}</li>
		 * </ul>
		 * @throws IllegalArgumentException when invalid typefaceStyle is passed
		 * @return self
		 */
		public Builder set(int typefaceStyle, Typeface typeface) {
			validateTypefaceStyle(typefaceStyle);
			if (mDefaultTypeface == null) {
				mDefaultTypeface = typeface;
			}
			mCollection.mTypefaces.put(typefaceStyle, typeface);
			return this;
		}

		/**
		 * Creates typeface collection.
		 * If not all styles are set, uses fist typeface that has been set
		 * for all unset styles.
		 * @throws IllegalStateException when no {@link android.graphics.Typeface}
		 *         has been set via {@link #set(int, android.graphics.Typeface)}
		 * @return TypefaceCollection
		 */
		public TypefaceCollection create() {
			if (mDefaultTypeface == null) {
				throw new IllegalStateException("At least one typeface style have to be set!");
			}

			if (mCollection.mTypefaces.get(Typeface.NORMAL) == null) {
				mCollection.mTypefaces.put(Typeface.NORMAL, mDefaultTypeface);
			}

			if (mCollection.mTypefaces.get(Typeface.BOLD) == null) {
				mCollection.mTypefaces.put(Typeface.BOLD, mDefaultTypeface);
			}

			if (mCollection.mTypefaces.get(Typeface.ITALIC) == null) {
				mCollection.mTypefaces.put(Typeface.ITALIC, mDefaultTypeface);
			}

			if (mCollection.mTypefaces.get(Typeface.BOLD_ITALIC) == null) {
				mCollection.mTypefaces.put(Typeface.BOLD_ITALIC, mDefaultTypeface);
			}

			TypefaceCollection collection = mCollection;
			mCollection = null;
			return collection;
		}
	}

	/**
	 * Creates default typeface collection, containing system fonts:
	 * <ul>
	 *     <li>{@link android.graphics.Typeface#DEFAULT}</li>
	 *     <li>{@link android.graphics.Typeface#DEFAULT_BOLD}</li>
	 * </ul>
	 * @return typeface collection
	 */
	public static TypefaceCollection createSystemDefault() {
		return new Builder()
				.set(Typeface.NORMAL, Typeface.DEFAULT)
				.set(Typeface.BOLD, Typeface.DEFAULT_BOLD)
				.set(Typeface.ITALIC, Typeface.DEFAULT)
				.set(Typeface.BOLD_ITALIC, Typeface.DEFAULT_BOLD)
				.create();
	}

	private SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(4);
	private TypefaceCollection() {}

	/**
	 *
	 * @param typefaceStyle
	 * <ul>
	 *      <li>{@link android.graphics.Typeface#NORMAL}</li>
	 *      <li>{@link android.graphics.Typeface#BOLD}</li>
	 *      <li>{@link android.graphics.Typeface#ITALIC}</li>
	 *      <li>{@link android.graphics.Typeface#BOLD_ITALIC}</li>
	 * </ul>
	 * @throws IllegalArgumentException when invalid typefaceStyle is passed
	 *
	 * @return selected Typeface
	 */
	Typeface getTypeface(int typefaceStyle) {
		validateTypefaceStyle(typefaceStyle);
		return mTypefaces.get(typefaceStyle);
	}

	private static void validateTypefaceStyle(int typefaceStyle) {
		switch (typefaceStyle) {
		case Typeface.NORMAL:
		case Typeface.BOLD:
		case Typeface.ITALIC:
		case Typeface.BOLD_ITALIC:
			break;
		default:
			throw new IllegalArgumentException("Invalid typeface style! Have to be one of " +
					"Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC or Typeface.BOLD_ITALIC");
		}
	}
}
