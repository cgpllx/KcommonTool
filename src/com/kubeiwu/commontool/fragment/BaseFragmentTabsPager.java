package com.kubeiwu.commontool.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.kubeiwu.commontool.R;
import com.kubeiwu.commontool.view.ViewFactory;

/**
 * @author 耳东    www.kubeiwu.com
 * 
 *  @code   使用方法
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
 	  		initTabs(mSimpleHandleTabs, Gravity.TOP); 
 	  return super.onCreateView(inflater, container, savedInstanceState);
    }  
 *  
 * 
 */
public abstract class BaseFragmentTabsPager extends BaseFragment {
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsPagerAdapter mTabsAdapter;
	private HandleTabsPager mHandleTabsPager;
	private int gravity = Gravity.BOTTOM;

	/**
	 * 子类必须super.onCreateView之前调用
	 * @param mHandleTabsPager
	 */
	protected void initTabs(HandleTabsPager mHandleTabsPager) {
		this.mHandleTabsPager = mHandleTabsPager;
	};

	/**
	 * @param mHandleTabs
	 * @param gravity   tabs的位置，上面Gravity.TOP 下面Gravity.BOTTOM，目前只支持两个 
	 */
	protected void initTabs(HandleTabsPager mHandleTabsPager, int gravity) {
		this.mHandleTabsPager = mHandleTabsPager;
		this.gravity = gravity;
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mTabHost = ViewFactory.getTabHostAndPagerView(inflater.getContext(), gravity);
		mTabHost.setup();
		mViewPager = (ViewPager) mTabHost.findViewById(R.id.pager);
		mTabsAdapter = new TabsPagerAdapter(this, mTabHost, mViewPager);
		if (mHandleTabsPager != null) {
			mHandleTabsPager.addTab(mTabsAdapter, mTabHost);
			for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
				mHandleTabsPager.setTabWidgetChildViewBackground(mTabHost.getTabWidget().getChildAt(i));
			}
			mHandleTabsPager.setTabWidgetBackground(mTabHost.getTabWidget());
		}
		return mTabHost;
	}

	/**
	 * @author 耳东    www.kubeiwu.com
	 *
	 */
	public interface HandleTabsPager extends HandlePublic {
		/**
		 * eg:mTabsAdapter.addTab(mTabHost.newTabSpec("TabSpec").setIndicator(getTabItemView(0)), YourFragment.class, null);
		 */
		void addTab(TabsPagerAdapter mTabsAdapter, TabHost mTabHost);
	}

	/**
	 * 适配器
	 * @author 耳东    www.kubeiwu.com
	 *
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
			super(activity.getChildFragmentManager());//在fragment中使用
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
