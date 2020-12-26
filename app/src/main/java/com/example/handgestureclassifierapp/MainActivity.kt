package com.example.handgestureclassifierapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

//    The results are stored in "result" variable

    private var btmap: Bitmap? = null
    private val REQUEST = 8293
    private val mInputSize = 200
    private val mModelPath = "Gesture_recogniser_quantised_1.tflite"
    private val mLabelPath = "labels.txt"
    private lateinit var classifier: Classifier
    private var result: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initClassifier()
    }

    private fun getResults(bitmap: Bitmap): String {
        val results = classifier.recognizeImage(bitmap)
        return results.toString()
    }

    private fun initClassifier() {
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
    }

    fun capturePic(view: View) {
        startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==8293) {
            val bitmap1 = data?.extras?.get("data")
            btmap= bitmap1 as Bitmap?
            result = getResults(btmap as Bitmap)
            showResults()
        }

    }

    private fun showResults() {
        val resultview = findViewById<TextView>(R.id.resultsview1)
        val res = result.toString()
        resultview.text = res
        openApps(res)
    }

    private fun openApps(resulltstr:String){
        Toast.makeText(this, "$resulltstr[16] is value", Toast.LENGTH_SHORT).show()

        if (resulltstr[16] == '4'){
            val whatsapp = packageManager.getLaunchIntentForPackage("com.whatsapp") //Intent is created to launch the app
            if (whatsapp != null) {
                startActivity(whatsapp)
            } else {
                Toast.makeText(this@MainActivity, "App not found", Toast.LENGTH_LONG).show() // If app does not exist in device, this will toast message "App not found"
            }
        }
        else if (resulltstr[16] == '1'){
            val youtubeintent = packageManager.getLaunchIntentForPackage("com.google.android.youtube")
            if (youtubeintent != null) {
                startActivity(youtubeintent)
            } else {
                Toast.makeText(this@MainActivity, "There is no package available in android", Toast.LENGTH_LONG).show()
            }
        }
        else if (resulltstr[16] == '2'){
            val message = Intent(Intent.ACTION_MAIN)
            message.addCategory(Intent.CATEGORY_APP_MESSAGING)
            startActivity(message)

        }
        else {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, 8293)
        }
    }
}