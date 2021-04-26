package com.yuk.miuihome

import android.telephony.TelephonyManager
import de.robv.android.xposed.*
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage


class MainHook: IXposedHookLoadPackage {
    companion object {
        const val SELF_PACKAGENAME = BuildConfig.APPLICATION_ID
        var Complete = Default().complete
        var Simple = Default().simple
        var None = Default().none
        var Test = Default().test
        var Folder = Default().folder
        var Maml = Default().maml
        var Smooth = Default().smooth
        var Clock = Default().clock
        var Transition = Default().transition
        var Simplea = Default().simplea
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.miui.home") {
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
                if (getData("TEST", Test) == 1) {
                    findAndHookMethod(
                        "com.miui.home.launcher.common.Utilities",
                        lpparam.classLoader,
                        "isUseCompleteBlurOnDev",
                        XC_MethodReplacement.returnConstant(true)
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

                // 动画速度
                val tran: Float = (Transition/100.0).toFloat()
                findAndHookMethod(
                    "com.miui.home.launcher.common.DeviceLevelUtils",
                    lpparam.classLoader,
                    "getDeviceLevelTransitionAnimRatio",
                    Float::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            param.args[1] = tran
                            super.beforeHookedMethod(param)
                        }
                    }
                )

                //设备分级
                findAndHookMethod(
                    "com.miui.home.launcher.common.DeviceLevelUtils",
                    lpparam.classLoader,
                    "getDeviceLevel",Int::class.java,
                    XC_MethodReplacement.returnConstant(2)
                )
                findAndHookMethod(
                    "miuix.animation.util.DeviceUtils",
                    lpparam.classLoader,
                    "getDeviceLevel", Int::class.java,
                    XC_MethodReplacement.returnConstant(2)
                )

                findAndHookMethod(
                    "miuix.animation.util.DeviceUtils",
                    lpparam.classLoader,
                    "getQualcommCpuLevel", Int::class.java, String::class.java,
                    XC_MethodReplacement.returnConstant(2)
                )
                findAndHookMethod(
                    "com.miui.home.launcher.common.CpuLevelUtils",
                    lpparam.classLoader,
                    "getQualcommCpuLevel", Int::class.java, String::class.java,
                    XC_MethodReplacement.returnConstant(2)
                )
                findAndHookMethod(
                    "miuix.animation.util.DeviceUtils",
                    lpparam.classLoader,
                    "getMtkCpuLevel", Int::class.java, String::class.java,
                    XC_MethodReplacement.returnConstant(1)
                )
            } catch (e: Exception) {
                XposedBridge.log("[MIUIHome]Error:" + e.message)}
        }
    }


    private fun getData(key: String, defValue: Int): Int {
        try {
            val pref = XSharedPreferences(SELF_PACKAGENAME, Default().DATAFILENAME)
            return pref.getInt(key, defValue)
        } catch (e: Exception) {
            XposedBridge.log("[MIUIHome]Can not getdata:" + key)
        }
        return defValue
    }
}