package com.kubeiwu.commontool.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kubeiwu.commontool.R;
import com.kubeiwu.commontool.view.ViewFactory;

/**
 * @author 耳东 www.kubeiwu.com
 * 
 * @code 使用方法 public View onCreateView(LayoutInflater inflater, ViewGroup
 *       container, Bundle savedInstanceState) { initTabs(mSimpleHandleTabs,
 *       Gravity.TOP); return super.onCreateView(inflater, container,
 *       savedInstanceState); }
 * 
 */
public class BaseFragmentTabs extends BaseFragment {
	private FragmentTabHost mTabHost;
	private HandleTabs mHandleTabs;
	private int gravity = Gravity.BOTTOM;

	protected void initTabs(HandleTabs mHandleTabs) {
		this.mHandleTabs = mHandleTabs;
	}

	/**
	 * @param mHandleTabs
	 * @param gravity
	 *            tabs的位置，上面Gravity.TOP 下面Gravity.BOTTOM，目前只支持两个
	 */
	protected void initTabs(HandleTabs mHandleTabs, int gravity) {
		this.mHandleTabs = mHandleTabs;
		this.gravity = gravity;
	}

	// public abstract void init(FragmentTabHost mFragmentTabHost, int gravity);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = ViewFactory.getFragmentTabHostView(inflater.getContext(), gravity);
		mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		mTabHost.setup(view.getContext(), getChildFragmentManager(), R.id.realtabcontent);
		if (mHandleTabs != null) {
			mHandleTabs.addTab(mTabHost);
			// init(mTabHost, gravity);
			for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
				mHandleTabs.setTabWidgetChildViewBackground(mTabHost.getTabWidget().getChildAt(i));
			}
			mHandleTabs.setTabWidgetBackground(mTabHost.getTabWidget());
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	/**
	 * @author 耳东 www.kubeiwu.com
	 *
	 */
	public interface HandleTabs extends HandlePublic {
		/**
		 * eg:mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator(
		 * getTabItemView(0)), IndexFragment.class, null);
		 */
		void addTab(FragmentTabHost mFragmentTabHost);
	}
}
