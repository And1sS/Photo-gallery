package com.and1ss.android.photogallery.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import com.and1ss.android.photogallery.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.chrisbanes.photoview.PhotoView


class ImageDialog : DialogFragment() {
    private lateinit var url: String
    private lateinit var title: String

    private lateinit var photoView: PhotoView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        url = arguments?.getString(ARG_PATH) ?: ""
        title = arguments?.getString(ARG_TITLE) ?: ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = LayoutInflater.from(activity)
            .inflate(R.layout.dialog_image, null)

        photoView = (view.findViewById(R.id.image) as PhotoView)

        progressBar = view.findViewById(R.id.progress_circular)

        Glide.with(this)
            .asBitmap()
            .placeholder(R.drawable.seekbar)
            .override(1600, 1600)
            .error(R.drawable.error)
            .centerCrop()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    photoView.setImageBitmap(resource)
                    progressBar.visibility = View.GONE
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        return AlertDialog.Builder(activity)
            .setView(view)
            .setPositiveButton(
                R.string.cancel
            ) { _, _ -> dismiss() }.setTitle(title)
            .create()
    }

    companion object {
        const val TAG = "ImageDialog"
        
        private const val ARG_PATH = "PATH"
        private const val ARG_TITLE = "TITLE"

        fun newInstance(path: String, title: String): ImageDialog =
            ImageDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PATH, path)
                    putString(ARG_TITLE, title)
                }
            }
    }
}
