//2014-8-20
package com.kubeiwu.commontool.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.widget.TabHost;

/**
 * @author cgpllx1@qq.com (www.kubeiwu.com)
 * @date 2014-8-20
 */
abstract class KFragmentBase extends Fragment {

	protected TabConfig tabConfig;
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
