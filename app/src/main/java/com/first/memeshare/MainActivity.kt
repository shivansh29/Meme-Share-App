package com.first.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.logging.Level.INFO

class MainActivity : AppCompatActivity() {

    var currentImage: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    private fun loadMeme(){

        val pBar: ProgressBar = findViewById<ProgressBar>(R.id.progressBar)
        pBar.visibility= View.VISIBLE
        // Instantiate the RequestQueue.
        val url = "https://meme-api.herokuapp.com/gimme"
        Log.d("mggg",url)
// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,Response.Listener
            { response ->
                currentImage = response.getString("url")
                val imageView: ImageView = findViewById<ImageView>(R.id.memeimage)
                Log.d("message Shiv",url.substring(0,20))
                Glide.with(this).load(currentImage).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                            pBar.visibility = View.GONE
                            return false
                    }
                }).into(imageView)
            },
            Response.ErrorListener
            {
                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_LONG,).show()
            })

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type="plain/text"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this cool meme $currentImage")
        val chooser = Intent.createChooser(intent,"Share this meme using ...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}

