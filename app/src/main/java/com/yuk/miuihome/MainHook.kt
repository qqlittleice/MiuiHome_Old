package com.yuk.miuihome

import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

class MainHook: IXposedHookLoadPackage {
    companion object {
        var Complete = Default().complete
        var Simple = Default().simple
        var None = Default().none
        var Folder = Default().folder
        var Maml = Default().maml
        var Smooth = Default().smooth
        var Clock = Default().clock
        var Transition = Default().transition
        var Simplea = Default().simplea
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            "com.yuk.miuihome" -> {
                findAndHookMethod(
                    "com.yuk.miuihome.MainActivity",
                    lpparam.classLoader,
                    "isModuleEnable",
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            param.result = true
                        }
                    }
                )
            }
            "com.miui.home" -> {
                try {
                    //模糊级别
                    if (getData("COMPLETE", Complete) == 1) {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.BlurUtils",
                            lpparam.classLoader,
                            "getBlurType",
                            XC_MethodReplacement.returnConstant(2)
                        )
                    }
                    if (getData("SIMPLE", Simple) == 1) {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.BlurUtils",
                            lpparam.classLoader,
                            "getBlurType",
                            XC_MethodReplacement.returnConstant(1)
                        )
                    }
                    if (getData("NONE", None) == 1) {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.BlurUtils",
                            lpparam.classLoader,
                            "getBlurType",
                            XC_MethodReplacement.returnConstant(0)
                        )
                    }

                    //平滑动画
                    if (getData("SMOOTH", Smooth) == 1) {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.Utilities",
                            lpparam.classLoader,
                            "isUseSmoothAnimationEffect",
                            XC_MethodReplacement.returnConstant(true)
                        )
                    } else {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.Utilities",
                            lpparam.classLoader,
                            "isUseSmoothAnimationEffect",
                            XC_MethodReplacement.returnConstant(false)
                        )
                    }

                    //简单动画
                    if (getData("SIMPLEA", Simplea) == 1) {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.DeviceLevelUtils",
                            lpparam.classLoader,
                            "isUseSimpleAnim",
                            XC_MethodReplacement.returnConstant(true)
                        )
                    } else {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.DeviceLevelUtils",
                            lpparam.classLoader,
                            "isUseSimpleAnim",
                            XC_MethodReplacement.returnConstant(false)
                        )
                    }

                    //文件夹模糊
                    if (getData("FOLDER", Folder) == 1) {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.BlurUtils",
                            lpparam.classLoader,
                            "isUserBlurWhenOpenFolder",
                            XC_MethodReplacement.returnConstant(true)
                        )
                    } else {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.BlurUtils",
                            lpparam.classLoader,
                            "isUserBlurWhenOpenFolder",
                            XC_MethodReplacement.returnConstant(false)
                        )
                    }

                    //下载水波纹
                    if (getData("MAML", Maml) == 1) {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.CpuLevelUtils",
                            lpparam.classLoader,
                            "needMamlDownload",
                            XC_MethodReplacement.returnConstant(true)
                        )
                    } else {
                        findAndHookMethod(
                            "com.miui.home.launcher.common.CpuLevelUtils",
                            lpparam.classLoader,
                            "needMamlDownload",
                            XC_MethodReplacement.returnConstant(false)
                        )
                    }

                    // 时钟常显
                    if (getData("CLOCK", Clock) == 1) {
                        findAndHookMethod(
                            "com.miui.home.launcher.Workspace",
                            lpparam.classLoader,
                            "isScreenHasClockGadget", Long::class.java,
                            XC_MethodReplacement.returnConstant(false)
                        )
                    }

                    //设备分级
                    findAndHookMethod(
                        "com.miui.home.launcher.common.DeviceLevelUtils",
                        lpparam.classLoader,
                        "getDeviceLevel",
                        XC_MethodReplacement.returnConstant(2)
                    )

                    //动画速度
                    findAndHookMethod(
                        "com.miui.home.launcher.common.DeviceLevelUtils",
                        lpparam.classLoader,
                        "getDeviceLevelTransitionAnimRatio",
                        object : XC_MethodHook() {
                            override fun beforeHookedMethod(param: MethodHookParam) {
                                param.result = (Transition / 10.0).toFloat()
                            }
                        }
                    )
                } catch (e: Throwable) {
                    XposedBridge.log("[MiuiHome]Method:" + e.message)
                }
            }
            else -> {
                return
            }
        }
    }
}

private fun getData(key: String, defValue: Int): Int {
    try {
        val pref = XSharedPreferences("com.yuk.miuihome", Default().DATAFILENAME)
        return pref.getInt(key, defValue)
    } catch (e: Throwable) {
        XposedBridge.log("[MIUIHome]Error:$key")
    }
    return defValue
}