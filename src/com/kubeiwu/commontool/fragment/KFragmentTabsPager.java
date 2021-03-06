package com.kubeiwu.commontool.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.kubeiwu.commontool.R;
import com.kubeiwu.commontool.view.ViewFactory;

/**
 * @author 耳东 www.kubeiwu.com
 */
public abstract class KFragmentTabsPager extends KFragmentBase {
	private TabHost mTabHost;
	private ViewPager mViewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (tabConfig == null) {
			tabConfig = TabConfig.getSimpleInstance();
		}
		mTabHost = ViewFactory.getTabHostAndPagerView(inflater.getContext(), tabConfig.getGravity());
		mTabHost.setup();
		mViewPager = (ViewPager) mTabHost.findViewById(R.id.pager);
		TabsPagerAdapter mTabsAdapter = new TabsPagerAdapter(this, mTabHost, mViewPager);
		initTab(mTabsAdapter, mTabHost);
		int tabcount = mTabHost.getTabWidget().getChildCount();
		if (tabcount == 0) {
			throw new IllegalArgumentException("Please in the initTab method to add Tab Fragment");
		}
		for (int i = 0; i < tabcount; i++) {
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(tabConfig.getWidgetChildViewBackgroundResource());
		}
		mTabHost.getTabWidget().setBackgroundResource(tabConfig.getWidgetBackgroundResource());
		return mTabHost;
	}

	public void setOffscreenPageLimit(int limit) {
		mViewPager.setOffscreenPageLimit(limit);
	}

	public ViewPager getViewPager() {
		return mViewPager;
	}
	/**
	 * eg:mTabsAdapter.addTab(mTabHost.newTabSpec("TabSpec").setIndicator(getTabItemView(0)), YourFragment.class, null);
	 */
	protected abstract void initTab(TabsPagerAdapter mTabsAdapter, TabHost mTabHost);

	/**
	 * 适配器
	 * 
	 * @author 耳东 www.kubeiwu.com
	 */
	public class TabsPagerAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private Context mContext;
		private TabHost mTabHost;
		private ViewPager mViewPager;
		private ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		class TabInfo {
			@SuppressWarnings("unused")
			private String tag;
			private Class<?> clss;
			private Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsPagerAdapter(Fragment activity, TabHost tabHost, ViewPager pager) {
			super(activity.getChildFragmentManager());// 在fragment中使用
			mContext = activity.getActivity();
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {

			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}
}
