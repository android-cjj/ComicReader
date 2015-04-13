package com.cjj.cartoon.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageAndTitle implements Parcelable{
	public String link;
	public String title;
	public String imageUrl;
	
	public ImageAndTitle(){}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(link);
		dest.writeString(title);
		dest.writeString(imageUrl);
	}

	public static final Parcelable.Creator<ImageAndTitle> CREATOR = new Parcelable.Creator<ImageAndTitle>() {
		public ImageAndTitle createFromParcel(Parcel in) {
			ImageAndTitle model = new ImageAndTitle();
			model.link = in.readString();
			model.title = in.readString();
			model.imageUrl = in.readString();
			return model;
		}

		public ImageAndTitle[] newArray(int size) {
			return new ImageAndTitle[size];
		}
	};

	
}
