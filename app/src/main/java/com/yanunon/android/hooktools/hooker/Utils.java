package com.yanunon.android.hooktools.hooker;

import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by junyongyang on 2016/2/29.
 */
public class Utils
{
	public static final String			HOOKTOOLS_PACKAGE_NAME	= "com.yanunon.android.hooktools";
	public static final String			UC_PACKAGE_NAME			= "com.UCMobile";
	public static final String			MM_PACKAGE_NAME			= "com.tencent.mm";

	public static XSharedPreferences	sSharedPreferences		= null;

	public static SharedPreferences getDefaultSharedPreferences()
	{
		if (sSharedPreferences == null)
		{
			sSharedPreferences = new XSharedPreferences(HOOKTOOLS_PACKAGE_NAME);
            sSharedPreferences.makeWorldReadable();
		}
		return sSharedPreferences;
	}

	public static String getSharedPreferencesPath()
	{
		File pref = new File(Environment.getDataDirectory(), "data/" + HOOKTOOLS_PACKAGE_NAME + "/shared_prefs/" + HOOKTOOLS_PACKAGE_NAME + "_preferences.xml");
		return pref.getAbsolutePath();
	}
}
