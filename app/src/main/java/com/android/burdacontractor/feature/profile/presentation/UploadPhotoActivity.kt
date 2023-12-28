package com.android.burdacontractor.feature.profile.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.core.utils.getImageUri
import com.android.burdacontractor.core.utils.reduceFileImage
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setToastLong
import com.android.burdacontractor.core.utils.uriToFile
import com.android.burdacontractor.databinding.ActivityUploadPhotoBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadPhotoBinding
    private val uploadPhotoViewModel: UploadPhotoViewModel by viewModels()
    private val storageViewModel: StorageViewModel by viewModels()
    private var snackbar: Snackbar? = null
    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        uploadPhotoViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        uploadPhotoViewModel.state.observe(this) {
            when (it) {
                StateResponse.LOADING -> binding.progressBar.isVisible = true
                StateResponse.ERROR -> binding.progressBar.isVisible = false
                StateResponse.SUCCESS -> {
                    binding.progressBar.isVisible = false
                }
            }
        }
        uploadPhotoViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        initUI()
    }

    private fun initUI() {
        with(binding) {
            galleryButton.setOnClickListener { startGallery() }
            cameraButton.setOnClickListener { startCamera() }
            uploadButton.setOnClickListener { uploadImage() }
            if (storageViewModel.photo.isNotBlank() && currentImageUri == null) {
                previewImageView.setImageFromUrl(storageViewModel.photo, this@UploadPhotoActivity)
            } else {
                if (currentImageUri == null) {
                    previewImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_image
                        )
                    )
                }
            }
        }
        onBackPressedCallback()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            uploadPhotoViewModel.uploadPhoto(imageFile)
            binding.previewImageView.setImageURI(uri)
        } ?: setToastLong(getString(R.string.empty_image_warning))
    }

    private fun onBackPressedCallback() {
        binding.btnBack.setOnClickListener { finishAction() }
        customBackPressed()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}