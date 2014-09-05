package com.kubeiwu.commontool.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kubeiwu.commontool.R;
import com.kubeiwu.commontool.view.ViewFactory;

public abstract class KTabFragment extends Fragment {
	private FragmentTabHost fragmentTabHost;
	private TabConfig tabConfig;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (tabConfig == null) {
			tabConfig = TabConfig.getSimpleInstance();
		}
		View view = ViewFactory.getFragmentTabHostView(inflater.getContext(), tabConfig.getGravity());
		fragmentTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		fragmentTabHost.setup(view.getContext(), getChildFragmentManager(), R.id.realtabcontent);
		initTab(fragmentTabHost);
		int tabcount = fragmentTabHost.getTabWidget().getChildCount();
		if (tabcount == 0) {
			throw new IllegalArgumentException("Please in the initTab method to add Tab Fragment");
		}
		for (int i = 0; i < tabcount; i++) {
			fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(tabConfig.getWidgetChildViewBackgroundResource());
		}
		fragmentTabHost.getTabWidget().setBackgroundResource(tabConfig.getWidgetBackgroundResource());
		return view;
	}

	public FragmentTabHost getFragmentTabHost() {
		return fragmentTabHost;
	}

	/**
	 * eg:mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator(
	 * getTabItemView(0)), IndexFragment.class, null);
	 */
	public abstract void initTab(FragmentTabHost mTabHost);

	/**
	 * 调用这里可以设置widget背景 在onCreateView才能生效
	 * 
	 * @param tabConfig
	 */
	public void setTabConfig(TabConfig tabConfig) {
		this.tabConfig = tabConfig;
	}

	public static class TabConfig {
		private int widgetChildViewBackgroundResource = android.R.color.white;
		private int widgetBackgroundResource = android.R.color.white;
		private int gravity = Gravity.BOTTOM;

		public int getGravity() {
			return gravity;
		}

		public TabConfig(int widgetChildViewBackgroundResource, int widgetBackgroundResource) {
			super();
			this.widgetChildViewBackgroundResource = widgetChildViewBackgroundResource;
			this.widgetBackgroundResource = widgetBackgroundResource;
		}

		public static TabConfig getSimpleInstance() {
			return new TabConfig();
		}

		public void setGravity(int gravity) {
			this.gravity = gravity;
		}

		/**
		 * 
		 * @param widgetChildViewBackgroundResource
		 * @param widgetBackgroundResource
		 * @param gravity
		 *            tab位置，目前值支持上面和下面两种 Gravity.BOTTOM ,Gravity.TOP
		 */
		public TabConfig(int widgetChildViewBackgroundResource, int widgetBackgroundResource, int gravity) {
			super();
			this.widgetChildViewBackgroundResource = widgetChildViewBackgroundResource;
			this.widgetBackgroundResource = widgetBackgroundResource;
			this.gravity = gravity;
		}

		public TabConfig() {
			super();
		}

		public int getWidgetChildViewBackgroundResource() {
			return widgetChildViewBackgroundResource;
		}

		public TabConfig setWidgetChildViewBackgroundResource(int widgetChildViewBackgroundResource) {
			this.widgetChildViewBackgroundResource = widgetChildViewBackgroundResource;
			return this;
		}

		public int getWidgetBackgroundResource() {
			return widgetBackgroundResource;
		}

		public TabConfig setWidgetBackgroundResource(int mWidgetBackgroundResource) {
			this.widgetBackgroundResource = mWidgetBackgroundResource;
			return this;
		}
	}
}
