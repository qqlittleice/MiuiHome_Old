package com.yuk.miuihome

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import miui.*


@SuppressLint("UseSwitchCompatOrMaterialCode")
class MainActivity : AppCompatActivity() {
    private var isModuleEnable = false
    var complete = Default().complete
    var simple = Default().simple
    var none = Default().none
    var test = Default().test
    var folder = Default().folder
    var maml = Default().maml
    var smooth = Default().smooth
    var clock = Default().clock
    var transition = Default().transition
    var simplea = Default().simplea
    val module_not_enable = "模块未激活"
    val module_enable = "模块已激活"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isModuleEnable = Default().getData(this, "TEST_MODULE", 1) == 1
        if (!isModuleEnable) {
            val textview = findViewById<TextView>(R.id.textView)
            textview.text = module_not_enable
        } else  {
            val textview = findViewById<TextView>(R.id.textView)
            textview.text = module_enable
        }
        complete = Default().getData(this, "COMPLETE", complete)
        simple = Default().getData(this, "SIMPLE", simple)
        none = Default().getData(this, "NONE", none)
        test = Default().getData(this, "TEST", test)
        folder = Default().getData(this, "FOLDER", folder)
        maml = Default().getData(this, "MAML", maml)
        smooth = Default().getData(this, "SMOOTH", smooth)
        clock = Default().getData(this, "CLOCK", clock)
        simplea = Default().getData(this, "SIMPLEA", simplea)
        transition = Default().getData(this, "TRANSITION", transition)
        init()
    }

    fun Home() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", "com.miui.home", null)
        intent.data = uri
        startActivity(intent)
    }

    fun init() {
        val transition_seekBar = findViewById<SeekBar>(R.id.transition_seekBar)
        val transition_max = findViewById<TextView>(R.id.transition_max)
        val transition_min = findViewById<TextView>(R.id.transition_min)
        val value_TextView = findViewById<TextView>(R.id.value_TextView)
        val blur_when_open_folder = findViewById<Switch>(R.id.blur_when_open_folder)
        val complete_blur = findViewById<Switch>(R.id.complete_blur)
        val simple_blur = findViewById<Switch>(R.id.simple_blur)
        val none_blur = findViewById<Switch>(R.id.none_blur)
        val test_blur = findViewById<Switch>(R.id.test_blur)
        val maml_download = findViewById<Switch>(R.id.maml_download)
        val smooth_animation = findViewById<Switch>(R.id.smooth_animation)
        val clock_a = findViewById<Switch>(R.id.clock_a)
        val simple_a = findViewById<Switch>(R.id.simple_a)
        val button = findViewById<Button>(R.id.button)
        val textview = findViewById<TextView>(R.id.textView)


        transition_min.text = 0.toString()
        transition_max.text = 100.toString()
        transition_seekBar.progress = transition
        value_TextView.text = transition.toString()
        transition_seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                value_TextView.text = p1.toString()}
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}})

        complete_blur.isChecked = complete == 1
        simple_blur.isChecked = simple == 1
        none_blur.isChecked = none == 1
        test_blur.isChecked = test == 1
        blur_when_open_folder.isChecked = folder == 1
        maml_download.isChecked = maml == 1
        smooth_animation.isChecked = smooth == 1
        clock_a.isChecked = clock == 1
        simple_a.isChecked = clock == 1

        button.setOnClickListener {
            transition = transition_seekBar.progress
            complete = if (complete_blur.isChecked) 1 else 0
            simple = if (simple_blur.isChecked) 1 else 0
            none = if (none_blur.isChecked) 1 else 0
            test = if (test_blur.isChecked) 1 else 0
            folder = if (blur_when_open_folder.isChecked) 1 else 0
            maml = if (maml_download.isChecked) 1 else 0
            smooth = if (smooth_animation.isChecked) 1 else 0
            clock = if (clock_a.isChecked) 1 else 0
            simplea = if (simple_a.isChecked) 1 else 0
            Default().saveData(this, "TRANSITION", transition)
            Default().saveData(this, "COMPLETE", complete)
            Default().saveData(this, "SIMPLE", simple)
            Default().saveData(this, "NONE", none)
            Default().saveData(this, "TEST", test)
            Default().saveData(this, "FOLDER", folder)
            Default().saveData(this, "MAML", maml)
            Default().saveData(this, "SMOOTH", smooth)
            Default().saveData(this, "CLOCK", clock)
            Default().saveData(this, "SIMPLEA", simplea)
            if (textview.text == module_not_enable)
            {val toast = Toast(this)
                toast.setText(R.string.not_enable)
                toast.show()
            } else {
            val toast = Toast(this)
            toast.setText(R.string.save_tips)
            toast.show()
                Home()
            }
        }
    }
}