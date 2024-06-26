package com.nghiatd.requestpermission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nghiatd.requestpermission.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val mBinding: ActivityMainBinding by lazy { requireNotNull(binding) }

    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    } else {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    private val requestCameraMultiPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (isGranted(permissions)) {
                openCamera()
            } else {
                Toast.makeText(this@MainActivity, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            btnOpenCamera.setOnClickListener {
                if (isGranted(permissions)) {
                    openCamera()
                } else {
                    requestCameraMultiPermissionResult.launch(permissions.toTypedArray())
                }
            }

            btnOpenGallery.setOnClickListener {
                if (isGranted(permissions)) {
                    openGallery()
                } else {
                    requestCameraMultiPermissionResult.launch(permissions.toTypedArray())
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1001)
    }

    @Suppress("DEPRECATION")
    private fun openGallery() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(i, 1002)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val uri = intent?.data
            Toast.makeText(this@MainActivity, "$uri", Toast.LENGTH_SHORT).show()
            mBinding.img.setImageURI(uri)
        }

        if (requestCode == 1002 && resultCode == RESULT_OK) {
            val uri = intent?.data
            Toast.makeText(this@MainActivity, "$uri", Toast.LENGTH_SHORT).show()
            mBinding.img.setImageURI(uri)
        }
    }

    private fun isGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isDenied(permission: String): Boolean {
        return !isGranted(permission)
    }

    private fun isGranted(permissions: List<String>): Boolean {
        for (permission in permissions) {
            if (isDenied(permission)) return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}