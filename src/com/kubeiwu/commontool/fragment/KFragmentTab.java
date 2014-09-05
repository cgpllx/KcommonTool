package com.kubeiwu.commontool.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kubeiwu.commontool.R;
import com.kubeiwu.commontool.view.ViewFactory;

public abstract class KFragmentTab extends KFragmentBase {
	private FragmentTabHost fragmentTabHost;
	

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
	protected abstract void initTab(FragmentTabHost mTabHost);
}
