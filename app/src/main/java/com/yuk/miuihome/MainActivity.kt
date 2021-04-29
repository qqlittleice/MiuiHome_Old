package com.yuk.miuihome

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import miui.app.Activity
import java.io.DataOutputStream


@SuppressLint("UseSwitchCompatOrMaterialCode")
class MainActivity : Activity() {
    var complete = Default().complete
    var simple = Default().simple
    var none = Default().none
    var folder = Default().folder
    var maml = Default().maml
    var smooth = Default().smooth
    var clock = Default().clock
    var transition = Default().transition
    var simplea = Default().simplea
    var icon = Default().icon
    val module_not_enable = "模块未激活"
    val module_enable = "模块已激活"

    private fun isModuleEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(miui.R.style.Theme_Light_Settings)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isModuleEnable()) {
            val textview = findViewById<TextView>(R.id.textView)
            textview.text = module_not_enable
        } else  {
            val textview = findViewById<TextView>(R.id.textView)
            textview.text = module_enable
        }
        complete = Default().getData(this, "COMPLETE", complete)
        simple = Default().getData(this, "SIMPLE", simple)
        none = Default().getData(this, "NONE", none)
        folder = Default().getData(this, "FOLDER", folder)
        maml = Default().getData(this, "MAML", maml)
        smooth = Default().getData(this, "SMOOTH", smooth)
        clock = Default().getData(this, "CLOCK", clock)
        simplea = Default().getData(this, "SIMPLEA", simplea)
        transition = Default().getData(this, "TRANSITION", transition)
        icon = Default().getData(this, "ICON", icon)
        init()
    }

    private fun home() {
        var switch: Int = PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        if (icon == 1) {
            switch = PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }
        this.packageManager.setComponentEnabledSetting(
            ComponentName(this, this.javaClass.name + "Alias"),
            switch, PackageManager.DONT_KILL_APP
        )
        try {
            val suProcess = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(suProcess.outputStream)
            os.writeBytes("am force-stop com.miui.home;exit;")
            os.flush()
            os.close()
            val exitValue = suProcess.waitFor()
            if (exitValue == 0) {
                val toast = Toast(this)
                toast.setText(R.string.save1_tips)
                toast.show()
            } else {
                val toast = Toast(this)
                toast.setText(R.string.su_tips)
                toast.show()
                throw Exception()
            }
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", "com.miui.home", null)
            intent.data = uri
            val toast = Toast(this)
            toast.setText(R.string.save2_tips)
            toast.show()
            startActivity(intent)
        }
    }

    private fun init() {
        val transition_seekBar = findViewById<SeekBar>(R.id.transition_seekBar)
        val value_TextView = findViewById<TextView>(R.id.value_TextView)
        val blur_when_open_folder = findViewById<Switch>(R.id.blur_when_open_folder)
        val complete_blur = findViewById<Switch>(R.id.complete_blur)
        val simple_blur = findViewById<Switch>(R.id.simple_blur)
        val none_blur = findViewById<Switch>(R.id.none_blur)
        val maml_download = findViewById<Switch>(R.id.maml_download)
        val smooth_animation = findViewById<Switch>(R.id.smooth_animation)
        val clock_a = findViewById<Switch>(R.id.clock_a)
        val simple_a = findViewById<Switch>(R.id.simple_a)
        val button = findViewById<Button>(R.id.button)
        val textview = findViewById<TextView>(R.id.textView)
        val hide_icon = findViewById<Switch>(R.id.hide_icon)

        transition_seekBar.progress = transition
        value_TextView.text = (transition/100.0).toString()
        transition_seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                value_TextView.text = (p1 / 100.0).toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        complete_blur.isChecked = complete == 1
        simple_blur.isChecked = simple == 1
        none_blur.isChecked = none == 1
        blur_when_open_folder.isChecked = folder == 1
        maml_download.isChecked = maml == 1
        smooth_animation.isChecked = smooth == 1
        clock_a.isChecked = clock == 1
        simple_a.isChecked = simplea == 1
        hide_icon.isChecked = icon == 1

        button.setOnClickListener {
            transition = transition_seekBar.progress
            complete = if (complete_blur.isChecked) 1 else 0
            simple = if (simple_blur.isChecked) 1 else 0
            none = if (none_blur.isChecked) 1 else 0
            folder = if (blur_when_open_folder.isChecked) 1 else 0
            maml = if (maml_download.isChecked) 1 else 0
            smooth = if (smooth_animation.isChecked) 1 else 0
            clock = if (clock_a.isChecked) 1 else 0
            simplea = if (simple_a.isChecked) 1 else 0
            icon = if (hide_icon.isChecked) 1 else 0
            Default().saveData(this, "TRANSITION", transition)
            Default().saveData(this, "COMPLETE", complete)
            Default().saveData(this, "SIMPLE", simple)
            Default().saveData(this, "NONE", none)
            Default().saveData(this, "FOLDER", folder)
            Default().saveData(this, "MAML", maml)
            Default().saveData(this, "SMOOTH", smooth)
            Default().saveData(this, "CLOCK", clock)
            Default().saveData(this, "SIMPLEA", simplea)
            Default().saveData(this, "ICON", icon)

            if (textview.text == module_not_enable) {
                val toast = Toast(this)
                toast.setText(R.string.not_enable)
                toast.show()
            } else {
                home()
            }
        }
    }
}
