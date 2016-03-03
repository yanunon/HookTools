package com.yanunon.android.hooktools.hooker;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.yanunon.android.hooktools.BuildConfig;

import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by junyongyang on 2016/3/1.
 */
public class MMWebViewHooker implements IXposedHookLoadPackage
{
	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable
	{
		if (loadPackageParam == null)
		{
			return;
		}

		if (!TextUtils.equals(loadPackageParam.packageName, Utils.MM_PACKAGE_NAME))
		{
			return;
		}

        boolean debugEnable = Utils.getDefaultSharedPreferences().getBoolean("settings_mm_webview_debuggable", false);
        setMMWebViewDebuggingEnable(loadPackageParam, debugEnable);
	}

	@TargetApi(19)
	private void setMMWebViewDebuggingEnable(XC_LoadPackage.LoadPackageParam loadPackageParam, final boolean enable)
	{
		XposedBridge.log("setWebContentsDebuggingEnabled start... enable:" + enable);

		final Class QbSdkclass = XposedHelpers.findClass("com.tencent.smtt.sdk.QbSdk", loadPackageParam.classLoader);
		XposedHelpers.findAndHookConstructor("com.tencent.smtt.sdk.WebView", loadPackageParam.classLoader, Context.class, AttributeSet.class,
				int.class, Map.class, boolean.class, new XC_MethodHook()
				{
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable
					{
						super.beforeHookedMethod(param);
						try
						{
							if (QbSdkclass != null)
							{
								XposedHelpers.callStaticMethod(QbSdkclass, enable ? "forceSysWebView" : "unForceSysWebView", new Object[]{});
							}
							else
							{
								XposedBridge.log("QbSdkclass == null ...");
							}
						}
						catch (Throwable e)
						{
							XposedBridge.log(e);
						}
					}

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable
					{
						super.afterHookedMethod(param);
						if (BuildConfig.VERSION_CODE >= 19)
						{
							WebView.setWebContentsDebuggingEnabled(enable);
							XposedBridge.log("setWebContentsDebuggingEnabled success...");
						}
					}
				});


	}
}
