package com.yanunon.android.hooktools.hooker;

import java.util.List;
import java.util.Map;

import android.app.AndroidAppHelper;
import android.text.TextUtils;
import android.widget.Toast;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by junyongyang on 2016/2/29.
 */
public class UCInfoFlowHooker implements IXposedHookLoadPackage
{
	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable
	{
		if (loadPackageParam == null)
		{
			return;
		}

		XposedBridge.log("Loaded app: " + loadPackageParam.packageName);

		if (!TextUtils.equals(loadPackageParam.packageName, Utils.UC_PACKAGE_NAME))
		{
			return;
		}

		XposedHelpers.findAndHookMethod("com.uc.application.infoflow.f.i.b.e", loadPackageParam.classLoader, "jR", new XC_MethodHook()
		{
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable
			{
				super.beforeHookedMethod(param);

			}

			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable
			{
				super.afterHookedMethod(param);
				String resultUrl = (String) param.getResult();
				XposedBridge.log("UCInfoFlowHooker resultUrl:" + resultUrl);
				int count = XposedHelpers.getIntField(param.thisObject, "mCount");
				Toast.makeText(AndroidAppHelper.currentApplication().getApplicationContext(), "InfoFlow ReqCount:" + count, Toast.LENGTH_SHORT).show();
			}
		});

        XposedHelpers.findAndHookMethod("com.uc.application.infoflow.f.c.f", loadPackageParam.classLoader, "a",
                "com.uc.application.infoflow.f.c.e", boolean.class, "com.uc.application.infoflow.f.d.a.h", Map.class,
                new XC_MethodHook()
        {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable
            {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                super.afterHookedMethod(param);
                List resultList = (List) param.getResult();
                if(resultList != null)
                {
                    XposedBridge.log("====================UCInfoFlowHooker Response====================");
                    for(Object obj : resultList)
                    {
                        Object ED = XposedHelpers.getObjectField(obj, "ED");
                        printB(ED);
                    }
                    XposedBridge.log("================================================================");
                }
                int count =resultList != null ? resultList.size() : 0;
                Toast.makeText(AndroidAppHelper.currentApplication().getApplicationContext(), "InfoFlow RespCount:" + count, Toast.LENGTH_SHORT).show();
            }
        });


	}

    private void printB(Object obj)
    {
        if(obj == null)
        {
            XposedBridge.log("null...");
            return;
        }
        String title = (String) XposedHelpers.getObjectField(obj, "title");
        String url = (String) XposedHelpers.getObjectField(obj, "url");
        XposedBridge.log("title:" + title + " url:" + url);
    }
}
