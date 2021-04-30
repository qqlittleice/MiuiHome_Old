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
    private var complete = Default().complete
    private var simple = Default().simple
    private var none = Default().none
    private var folder = Default().folder
    private var maml = Default().maml
    private var smooth = Default().smooth
    private var clock = Default().clock
    private var transition = Default().transition
    private var simplea = Default().simplea
    private var icon = Default().icon
    private val moduleNotEnable = "模块未激活"
    private val moduleEnable = "模块已激活"

    private fun isModuleEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(miui.R.style.Theme_Light_Settings)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isModuleEnable()) {
            val textview = findViewById<TextView>(R.id.textView)
            textview.text = moduleNotEnable
        } else  {
            val textview = findViewById<TextView>(R.id.textView)
            textview.text = moduleEnable
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

    @SuppressLint("SetTextI18n")
    private fun init() {
        val transitionSeekBar = findViewById<SeekBar>(R.id.transition_seekBar)
        val valueTextView = findViewById<TextView>(R.id.value_TextView)
        val blurWhenOpenFolder = findViewById<Switch>(R.id.blur_when_open_folder)
        val completeBlur = findViewById<Switch>(R.id.complete_blur)
        val simpleBlur = findViewById<Switch>(R.id.simple_blur)
        val noneBlur = findViewById<Switch>(R.id.none_blur)
        val mamlDownload = findViewById<Switch>(R.id.maml_download)
        val smoothAnimation = findViewById<Switch>(R.id.smooth_animation)
        val clockA = findViewById<Switch>(R.id.clock_a)
        val simpleA = findViewById<Switch>(R.id.simple_a)
        val button = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.textView)
        val hideIcon = findViewById<Switch>(R.id.hide_icon)

        transitionSeekBar.progress = transition
        valueTextView.text = (transition/100.0).toString() + "f"
        transitionSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                valueTextView.text = (p1 / 100.0).toString() + "f"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        completeBlur.isChecked = complete == 1
        simpleBlur.isChecked = simple == 1
        noneBlur.isChecked = none == 1
        blurWhenOpenFolder.isChecked = folder == 1
        mamlDownload.isChecked = maml == 1
        smoothAnimation.isChecked = smooth == 1
        clockA.isChecked = clock == 1
        simpleA.isChecked = simplea == 1
        hideIcon.isChecked = icon == 1

        button.setOnClickListener {
            transition = transitionSeekBar.progress
            complete = if (completeBlur.isChecked) 1 else 0
            simple = if (simpleBlur.isChecked) 1 else 0
            none = if (noneBlur.isChecked) 1 else 0
            folder = if (blurWhenOpenFolder.isChecked) 1 else 0
            maml = if (mamlDownload.isChecked) 1 else 0
            smooth = if (smoothAnimation.isChecked) 1 else 0
            clock = if (clockA.isChecked) 1 else 0
            simplea = if (simpleA.isChecked) 1 else 0
            icon = if (hideIcon.isChecked) 1 else 0
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

            if (textView.text == moduleNotEnable) {
                val toast = Toast(this)
                toast.setText(R.string.not_enable)
                toast.show()
            } else {
                home()
            }
        }
    }
}
