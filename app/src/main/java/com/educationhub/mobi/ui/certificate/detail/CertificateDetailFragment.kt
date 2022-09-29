package com.educationhub.mobi.ui.certificate.detail

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.educationhub.mobi.databinding.FragmentCertificateDetailBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CertificateDetailFragment : Fragment() {

    private var _binding: FragmentCertificateDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: CertificateDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val shareBtn: Button = binding.btnShareCert
        val certView: RelativeLayout = binding.certView
        shareBtn.setOnClickListener { shareCert(certView, args.courseTitle) }

        binding.certTextCourseTitle.text = args.courseTitle

        return root
    }

    private fun shareCert(view: RelativeLayout, courseTitle: String) {
        val bitmap: Bitmap = captureViewAsGraphic(view)
        val fileName: String =
            SimpleDateFormat.getDateTimeInstance().format(Date()) + "_" + courseTitle
        storeViewGraphic(bitmap, fileName)
    }

    private fun captureViewAsGraphic(view: RelativeLayout): Bitmap {
        val b: Bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        view.draw(c)
        return b
    }

    private fun storeViewGraphic(b: Bitmap, filename: String) {
//        Check permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                0
            )
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }

            val resolver = context?.contentResolver
            var uri: Uri? = null

            try {
                uri = resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: throw IOException("Failed to create new MediaStore record.")

                resolver.openOutputStream(uri)?.use {
                    if (!b.compress(Bitmap.CompressFormat.PNG, 95, it))
                        throw IOException("Failed to save bitmap.")
                } ?: throw IOException("Failed to open output stream.")

//              Share bitmap
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/png"
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(intent, "Share"))

                return

            } catch (e: IOException) {
                uri?.let { orphanUri ->
                    // Don't leave an orphan entry in the MediaStore
                    resolver?.delete(orphanUri, null, null)
                }
                throw e
            }
        } else {

            val dirPath: String =
                Environment.getExternalStorageDirectory().absolutePath + "/Certificates"
            val dir = File(dirPath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dirPath, filename)
            try {
                val fOut = FileOutputStream(file)
                b.compress(Bitmap.CompressFormat.PNG, 85, fOut)
                fOut.flush()
                fOut.close()
                scanFile(file.absolutePath)

//              Share bitmap
////            Get uri
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.educationhub.mobi.provider",
                    file
                )
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/png"
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(intent, "Share"))
            } catch (e: Exception) {
                Log.e("e", e.message.toString())
            }
        }
    }

    private fun scanFile(file: String) {
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file),
            null
        ) { path, uri -> Log.e("file", "File saved successfully") }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
