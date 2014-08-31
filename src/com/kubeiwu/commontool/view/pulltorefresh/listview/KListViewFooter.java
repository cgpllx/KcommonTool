package com.kubeiwu.commontool.view.pulltorefresh.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kubeiwu.commontool.R;
import com.kubeiwu.commontool.view.ViewFactory;
import com.kubeiwu.commontool.view.pulltorefresh.domain.StringHoder;

public class KListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;
	private StringHoder stringHoder;

	public KListViewFooter(Context context, StringHoder stringHoder) {
		super(context);
		this.stringHoder = stringHoder;
		initView(context);
	}

	public KListViewFooter(Context context, AttributeSet attrs, StringHoder stringHoder) {
		super(context, attrs);
		this.stringHoder = stringHoder;
		initView(context);
	}

	public void setState(int state) {
		mHintView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		mHintView.setVisibility(View.INVISIBLE);
		if (state == STATE_READY) {
			mHintView.setVisibility(View.VISIBLE);
			//			mHintView.setText(R.string.xlistview_footer_hint_ready);
			mHintView.setText(stringHoder.footer_hint_ready);
		} else if (state == STATE_LOADING) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(stringHoder.footer_hint_normal);
			//			mHintView.setText(R.string.xlistview_footer_hint_normal);
		}
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * loading status 
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) ViewFactory.getKListview_footer(mContext, stringHoder.footer_heaght);
		//(LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.klistview_footer, null);

		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.klistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.klistview_footer_progressbar);
		mHintView = (TextView) moreView.findViewById(R.id.klistview_footer_hint_textview);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		setFooterHeaght();
	}

	private void setFooterHeaght() {
		mContentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, stringHoder.footer_heaght));
	}
}
