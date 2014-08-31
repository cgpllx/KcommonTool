package com.kubeiwu.commontool.volley.cache.core;


public final class DisplayImageOptions {
	private int imageResOnLoading = 0;
	private int imageResForEmptyUri = 0;
	private int imageResOnFail = 0;

	public int getImageResOnLoading() {
		return imageResOnLoading;
	}

	public int getImageResForEmptyUri() {
		return imageResForEmptyUri;
	}

	public int getImageResOnFail() {
		return imageResOnFail;
	}

	public int getImageScaleType() {
		return imageScaleType;
	}

	private int imageScaleType = 0;

	private DisplayImageOptions(Builder builder) {
		imageResOnLoading = builder.imageResOnLoading;
		imageResForEmptyUri = builder.imageResForEmptyUri;
		imageResOnFail = builder.imageResOnFail;
		imageScaleType = builder.imageScaleType;
	}

	public static class Builder {
		private int imageResOnLoading = 0;
		private int imageResForEmptyUri = 0;
		private int imageResOnFail = 0;
		private int imageScaleType;

		public Builder setImageScaleType(int mImageScaleType) {
			this.imageScaleType = mImageScaleType;
			return this;
		}

		public Builder setImageOnLoading(int imageResource) {
			imageResOnLoading = imageResource;
			return this;
		}

		public Builder setImageForEmptyUri(int imageRes) {
			imageResForEmptyUri = imageRes;
			return this;
		}

		public Builder setImageOnFail(int imageRes) {
			imageResOnFail = imageRes;
			return this;
		}

		public DisplayImageOptions build() {
			return new DisplayImageOptions(this);
		}
	}

	public static class ImageScaleType {
		public static final int NONE = 0, IN_SAMPLE_POWER_OF_2 = 1, IN_SAMPLE_INT = 2, EXACTLY = 3, EXACTLY_STRETCHED = 4;
	}

	public static DisplayImageOptions createSimple() {
		return new Builder().build();
	}
}
