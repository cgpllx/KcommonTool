package com.kubeiwu.commontool.view;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.kubeiwu.commontool.R;

/**
 * @author 耳东    www.kubeiwu.com
 *
 */
public class ViewFactory {  
	/**
	* 	获取TabHost
	* @param mContext
	* @param gravity   tabs的位置，上面TOP 下面BOTTOM，目前只支持两个
	* @return
	*/
	public static FragmentTabHost getFragmentTabHostView(Context mContext, int gravity) {
		//init FragmentTabHost
		FragmentTabHost tabhost = new FragmentTabHost(mContext);
		tabhost.setId(android.R.id.tabhost);
		tabhost.setLayoutParams(new FragmentTabHost.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		LinearLayout mLinearLayout = new LinearLayout(mContext);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//init FrameLayout
		FrameLayout realtabcontent = new FrameLayout(mContext);
		realtabcontent.setId(R.id.realtabcontent);
		realtabcontent.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));

		//init TabWidget
		TabWidget tabs = new TabWidget(mContext);
		tabs.setId(android.R.id.tabs);
		tabs.setLayoutParams(new TabWidget.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0));
		tabs.setOrientation(LinearLayout.HORIZONTAL);

		FrameLayout tabcontent = new FrameLayout(mContext);
		tabcontent.setId(android.R.id.tabcontent);
		tabcontent.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 0f));

		tabhost.addView(mLinearLayout);
		switch (gravity) {
		case Gravity.BOTTOM:
			mLinearLayout.addView(realtabcontent);

			mLinearLayout.addView(tabs);//wiget在下面
			mLinearLayout.addView(tabcontent);
			break;
		case Gravity.TOP:
			mLinearLayout.addView(tabs);//wiget在上面
			mLinearLayout.addView(tabcontent);

			mLinearLayout.addView(realtabcontent);
			break;
		}
		return tabhost;
	}

	/**
	 * 	获取TabHost
	 * @param mContext
	 * @param gravity   tabs的位置，上面TOP 下面BOTTOM，目前只支持两个
	 * @return
	 */
	public static TabHost getTabHostAndPagerView(Context mContext, int gravity) {
		//init TabHost
		TabHost tabhost = new TabHost(mContext);
		tabhost.setId(android.R.id.tabhost);
		tabhost.setLayoutParams(new FragmentTabHost.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		LinearLayout mLinearLayout = new LinearLayout(mContext);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		//init TabWidget
		TabWidget tabs = new TabWidget(mContext);
		tabs.setId(android.R.id.tabs);
		tabs.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0));
		tabs.setOrientation(LinearLayout.HORIZONTAL);

		FrameLayout tabcontent = new FrameLayout(mContext);
		tabcontent.setId(android.R.id.tabcontent);
		tabcontent.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 0f));

		ViewPager viewPager = new ViewPager(mContext);
		viewPager.setId(R.id.pager);
		viewPager.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));

		tabhost.addView(mLinearLayout);
		switch (gravity) {
		case Gravity.BOTTOM:
			mLinearLayout.addView(viewPager);

			mLinearLayout.addView(tabs);//wiget在下面
			mLinearLayout.addView(tabcontent);
			break;
		case Gravity.TOP:
			mLinearLayout.addView(tabs);//wiget在上面
			mLinearLayout.addView(tabcontent);

			mLinearLayout.addView(viewPager);
			break;
		}
		return tabhost;
	}

	public static View getKListview_footer(Context mContext, int headerHeaght) {
		LinearLayout mLinearLayout = new LinearLayout(mContext);
		mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mLinearLayout.setGravity(Gravity.BOTTOM);
		//----------------
		RelativeLayout xlistview_footer_content = new RelativeLayout(mContext);
		xlistview_footer_content.setId(R.id.klistview_footer_content);
		xlistview_footer_content.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, headerHeaght));
		xlistview_footer_content.setPadding(0, 0, 0, 0);
		//-----------------------
		ProgressBar xlistview_footer_progressbar = new ProgressBar(mContext);
		xlistview_footer_progressbar.setId(R.id.klistview_footer_progressbar);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		xlistview_footer_progressbar.setLayoutParams(layoutParams);
		//-------------------------
		TextView xlistview_footer_hint_textview = new TextView(mContext);
		xlistview_footer_hint_textview.setId(R.id.klistview_footer_hint_textview);
		RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
		xlistview_footer_hint_textview.setLayoutParams(layoutParams2);
		xlistview_footer_hint_textview.setGravity(Gravity.CENTER);
		//-----------------------
		mLinearLayout.addView(xlistview_footer_content);
		xlistview_footer_content.addView(xlistview_footer_progressbar);
		xlistview_footer_content.addView(xlistview_footer_hint_textview);
		return mLinearLayout;
	}

	public static View getKListview_header(Context mContext, int headerHeaght) {
		LinearLayout mLinearLayout = new LinearLayout(mContext);
		mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mLinearLayout.setGravity(Gravity.BOTTOM);
		//----------------
		RelativeLayout xlistview_header_content = new RelativeLayout(mContext);
		xlistview_header_content.setId(R.id.klistview_header_content);
		xlistview_header_content.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, headerHeaght));
		//-----------------
		LinearLayout xlistview_header_text = new LinearLayout(mContext);
		xlistview_header_text.setId(R.id.klistview_header_text);
		xlistview_header_text.setOrientation(LinearLayout.VERTICAL);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		xlistview_header_text.setLayoutParams(layoutParams);
		xlistview_header_text.setGravity(Gravity.CENTER);
		//---------------------------
		TextView xlistview_header_hint_textview = new TextView(mContext);
		xlistview_header_hint_textview.setId(R.id.klistview_header_hint_textview);
		xlistview_header_hint_textview.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		LinearLayout mLinearLayoutgone = new LinearLayout(mContext);
		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams1.topMargin = 10;
		mLinearLayoutgone.setLayoutParams(layoutParams1);
		mLinearLayoutgone.setVisibility(View.GONE);
		//-------------------------
		TextView textView = new TextView(mContext);
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		textView.setTextSize(15);
		//-------------------------
		TextView xlistview_header_time = new TextView(mContext);
		xlistview_header_time.setId(R.id.klistview_header_time);
		xlistview_header_time.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		xlistview_header_time.setTextSize(12);
		//-----------------------
		ImageView xlistview_header_arrow = new ImageView(mContext);
		xlistview_header_arrow.setId(R.id.klistview_header_arrow);
		RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams2.addRule(RelativeLayout.ALIGN_LEFT, R.id.klistview_header_text);
		layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams2.leftMargin = -50;
		xlistview_header_arrow.setLayoutParams(layoutParams2);
		//-----------------------
		ProgressBar xlistview_header_progressbar = new ProgressBar(mContext);
		xlistview_header_progressbar.setId(R.id.klistview_header_progressbar);
		RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(40, 40);
		layoutParams3.addRule(RelativeLayout.ALIGN_LEFT, R.id.klistview_header_text);
		layoutParams3.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams3.leftMargin = -55;
		xlistview_header_progressbar.setLayoutParams(layoutParams3);
		//-----------------------
		mLinearLayout.addView(xlistview_header_content);
		xlistview_header_content.addView(xlistview_header_text);
		xlistview_header_text.addView(xlistview_header_hint_textview);
		xlistview_header_text.addView(mLinearLayoutgone);
		mLinearLayoutgone.addView(textView);
		mLinearLayoutgone.addView(xlistview_header_time);
		xlistview_header_content.addView(xlistview_header_arrow);
		xlistview_header_content.addView(xlistview_header_progressbar);
		//-------------		
		return mLinearLayout;
	}
}
