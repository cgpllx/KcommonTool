package com.kubeiwu.commontool.wedget;

import com.kubeiwu.commontool.volley.cache.KRequestQueueManager;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class KApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();   
		init(getApplicationContext());
		initImageLoder(getApplicationContext());
	}

	private void init(Context context) {
		if (PreferenceManager.getDefaultSharedPreferences(context).getInt("screenheight", -1) < 0) {
			WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			windowManager.getDefaultDisplay().getMetrics(dm);
			PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("screenwidth", dm.widthPixels).commit();
			PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("screenheight", dm.heightPixels).commit();
		}
		
	}
	public void initImageLoder(Context context) {
		KRequestQueueManager.getInstance().init(context);

	}
}