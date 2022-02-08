package com.algebra.projekt44

import android.app.ActivityManager
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.algebra.projekt44.databinding.ActivityMainBinding
import kotlin.system.exitProcess
import android.os.Build
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var counter = MutableLiveData<Int>(0)
    private var usernameEditText   : EditText? = null
    private var passwordEditText   : EditText? = null
    private var submitButton       : Button? = null
    private var dialogLayout       : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FullScreencall()
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        this.window.setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG)
        binding = DataBindingUtil.setContentView(this,  R.layout.activity_main)

        setupWebView("https://www.24sata.hr/")


        counter.observe(this, {
            Log.i("MainActivity", "Evo sad je $it")
            if (counter.value == 4){
                //exitProcess(-1)
                //sakrij webview
                binding.webView.visibility = View.GONE;
                //pokazi dialog
                layoutInflater.inflate(R.layout.dialog_signin, binding.mainLayout)
                //setup the dialog widgets
                initWidgets()
                //call the submit btn fun
                login()
            }
        });

    }

    private fun setupWebView(url: String) {
        binding.webView.webViewClient = MyWebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(url)
    }

    private fun login() {
        submitButton!!.setOnClickListener {
            Log.i("MainActivity", "Desio se submit click")
            var username = usernameEditText!!.text.toString()
            var password = passwordEditText!!.text.toString()
            if (username.equals("admin") && password.equals("admin")) {
                Log.i("MainActivity", "Ispravni credentialsi")
                exitProcess(-1)
            } else {
                Log.i("MainActivity", "Neispravni credentialsi")
                //reset counter to 0
                counter.postValue(0);
                //remove dialog layout
                binding.mainLayout.removeView(dialogLayout)
                //display the webview again
                binding.webView.visibility = View.VISIBLE
            }
        }
    }

    private fun initWidgets() {
        usernameEditText    = findViewById(R.id.username)
        passwordEditText    = findViewById(R.id.password)
        submitButton        = findViewById(R.id.loginBtn)
        dialogLayout        = findViewById(R.id.dialogLayout)
    }

    override fun onPause() {
        super.onPause()
        val activityManager = applicationContext
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.moveTaskToFront(taskId, 0)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //Log.i("MainActivity", "KeyCode: ${keyCode}")
        if (keyCode == 4) {
            counter.postValue(counter.value?.inc())
            /*First option*/
            //moveTaskToBack(true);
            //exitProcess(-1)
            /*Second option*/
            //finishAffinity()
        }

        return true
    }

}