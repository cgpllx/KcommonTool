package com.kubeiwu.commontool.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ApkUtils {
	public static void installApk(Context context, Uri uri) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// intent.setType("application/vnd.android.package-archive");
		// intent.setData(Uri.fromFile(file));
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
 }
