package com.example.handgestureclassifierapp

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView

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
        resultview.text = result.toString()
    }

}