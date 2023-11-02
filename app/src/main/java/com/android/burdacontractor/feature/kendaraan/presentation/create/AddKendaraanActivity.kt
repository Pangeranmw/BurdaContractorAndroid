package com.android.burdacontractor.feature.kendaraan.presentation.create

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.enums.JenisKendaraan
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.emptyData
import com.android.burdacontractor.core.utils.getImageUri
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.reduceFileImage
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.core.utils.uriToFile
import com.android.burdacontractor.databinding.ActivityAddKendaraanBinding
import com.android.burdacontractor.feature.gudang.presentation.PilihGudangFragment
import com.android.burdacontractor.feature.gudang.presentation.PilihGudangViewModel
import com.android.burdacontractor.feature.kendaraan.presentation.detail.KendaraanDetailActivity
import com.google.android.material.snackbar.Snackbar
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddKendaraanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddKendaraanBinding
    private val pilihGudangViewModel: PilihGudangViewModel by viewModels()
    private val addKendaraanViewModel: AddKendaraanViewModel by viewModels()
    private var currentImageUri: Uri? = null
    private var selectedJenis: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddKendaraanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        addKendaraanViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        addKendaraanViewModel.state.observe(this) {
            binding.progressBar.isVisible = it == StateResponse.LOADING
        }
        addKendaraanViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        pilihGudangViewModel.gudang.observe(this) {
            it?.let {
                binding.tvGudangBelumDipilih.setGone()
                binding.layoutGudang.setVisible()
                binding.ivGudang.setImageFromUrl(it.gambar, this)
                binding.tvNamaGudang.text = it.nama
                binding.tvAlamatGudang.text = it.alamat
                isInputCorrect()
            }
        }
        initUi()
        onBackPressedCallback()
    }

    private fun initUi() {
        binding.apply {
            val listJenis = JenisKendaraan.values().toList().map { it.name }
            spinnerJenis.setItems(listJenis)
            spinnerJenis.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                selectedJenis = newItem
                isInputCorrect()
            })
            etPlatNomor.doAfterTextChanged {
                emptyData(it.toString(), etPlatNomorLayout)
                isInputCorrect()
            }
            cvGudang.setOnClickListener {
                val fragment = PilihGudangFragment.newInstance()
                fragment.show(supportFragmentManager)
            }
            cvGambar.setOnClickListener {
                CustomDialog(
                    mainButtonText = "Camera",
                    mainButtonBackgroundDrawable = null,
                    secondaryButtonText = "Gallery",
                    secondaryButtonTextColor = null,
                    mainButtonTextColor = null,
                    secondaryButtonBackgroundDrawable = null,
                    title = "Opsi Ambil Gambar",
                    subtitle = "Silahkan memilih pilihan opsi untuk mengambil gambar!",
                    image = null,
                    blockMainButton = { startCamera() },
                    blockSecondaryButton = { startGallery() }).show(
                    supportFragmentManager,
                    "ChooseImage"
                )
            }
            btnSubmit.setOnClickListener {
                val gambar =
                    uriToFile(currentImageUri!!, this@AddKendaraanActivity).reduceFileImage()
                btnSubmit.isEnabled = false
                addKendaraanViewModel.addKendaraan(
                    gudangId = pilihGudangViewModel.gudang.value!!.id,
                    jenis = selectedJenis.toString(),
                    merk = etMerk.text.toString(),
                    platNomor = etPlatNomor.text.toString(),
                    gambar = gambar,
                    successListener = {
                        openActivityWithExtras(KendaraanDetailActivity::class.java) {
                            putString(INTENT_ID, it)
                        }
                    }
                )
            }
            etMerk.doAfterTextChanged {
                emptyData(it.toString(), etMerkLayout)
                isInputCorrect()
            }
        }
    }

    private fun isInputCorrect() {
        with(binding) {
            btnSubmit.isEnabled = TextUtils.isEmpty(etPlatNomorLayout.error)
                    && TextUtils.isEmpty(etMerkLayout.error)
                    && etMerk.text.toString().isNotEmpty()
                    && etPlatNomor.text.toString().isNotEmpty()
                    && spinnerJenis.selectedIndex != -1
                    && currentImageUri != null
                    && pilihGudangViewModel.gudang.value != null
        }
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
            binding.tvGambar.setGone()
            binding.ivGambar.setVisible()
            binding.ivGambar.setImageURI(it)
            isInputCorrect()
        }
    }

    fun onBackPressedCallback() {
        val finishAction = {
            finish()
            overridePendingTransition(0, 0)
        }
        binding.btnBack.setOnClickListener {
            finishAction()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAction()
            }
        })
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}