//2014-8-20
package com.kubeiwu.commontool.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TabWidget;

/**
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-20
 */
public class BaseFragment extends Fragment {

	public interface HandlePublic {
		/**
		 * 设置Widget背景
		 * @param widgetview  
		 */
		void setTabWidgetBackground(TabWidget widgetview);

		/**
		 * 设置Widget item的背景
		 * @param widgetview
		 */
		void setTabWidgetChildViewBackground(View widgetchildview);
	}
}
