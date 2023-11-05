package com.android.burdacontractor.feature.perusahaan.presentation.update

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
import com.android.burdacontractor.core.domain.model.Constant.INTENT_PARCEL
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.presentation.pinpoint.PinPointLokasiFragment
import com.android.burdacontractor.core.presentation.pinpoint.PinPointLokasiViewModel
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.emptyData
import com.android.burdacontractor.core.utils.getImageUri
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.parcelable
import com.android.burdacontractor.core.utils.reduceFileImage
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.core.utils.uriToFile
import com.android.burdacontractor.databinding.ActivityAddPerusahaanBinding
import com.android.burdacontractor.feature.perusahaan.domain.model.PerusahaanById
import com.android.burdacontractor.feature.perusahaan.presentation.detail.PerusahaanDetailActivity
import com.google.android.material.snackbar.Snackbar
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class UpdatePerusahaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPerusahaanBinding
    private val updatePerusahaanViewModel: UpdatePerusahaanViewModel by viewModels()
    private val pinPointLokasiViewModel: PinPointLokasiViewModel by viewModels()
    private var perusahaan: PerusahaanById? = null
    private var currentImageUri: Uri? = null
    private var selectedProvinsi: String? = null
    private var selectedKota: String? = null
    private var currentAlamat: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPerusahaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        perusahaan = intent.parcelable(INTENT_PARCEL)
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        updatePerusahaanViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        updatePerusahaanViewModel.state.observe(this) {
            binding.progressBar.isVisible = it == StateResponse.LOADING
        }
        updatePerusahaanViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        initUi()
        onBackPressedCallback()
    }

    private fun initUi() {
        binding.apply {
            btnSubmit.text = getString(R.string.ubah_perusahaan)
            tvAppBar.text = getString(R.string.ubah_perusahaan)

            spinnerProvinsi.text = perusahaan!!.provinsi
            spinnerKota.text = perusahaan!!.kota
            selectedProvinsi = perusahaan!!.provinsi
            selectedKota = perusahaan!!.kota
            etNama.setText(perusahaan!!.nama)
            tvKoordinat.text = getString(
                R.string.latitude_longitude,
                perusahaan!!.latitude.toString(),
                perusahaan!!.longitude.toString()
            )
            pinPointLokasiViewModel.setLatitude(perusahaan!!.latitude.toString())
            pinPointLokasiViewModel.setLongitude(perusahaan!!.longitude.toString())
            etAlamat.setText(perusahaan!!.alamat)
            tvGambar.setGone()
            ivGambar.setVisible()
            ivGambar.setImageFromUrl(perusahaan!!.gambar, this@UpdatePerusahaanActivity)

            updatePerusahaanViewModel.province.observe(this@UpdatePerusahaanActivity) {
                it?.let {
                    spinnerProvinsi.setItems(it)
                }
            }
            spinnerProvinsi.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                selectedProvinsi = newItem
                updatePerusahaanViewModel.getCityByProvince(newItem)
                spinnerKota.clearSelectedItem()
                spinnerKota.setItems(emptyList<String>())
                isInputCorrect()
            })
            pinPointLokasiViewModel.latitude.observe(this@UpdatePerusahaanActivity) { lat ->
                pinPointLokasiViewModel.longitude.observe(this@UpdatePerusahaanActivity) { lon ->
                    if (lat != null && lon != null) {
                        binding.tvKoordinat.text = getString(R.string.latitude_longitude, lat, lon)
                    }
                }
            }
            updatePerusahaanViewModel.city.observe(this@UpdatePerusahaanActivity) {
                it?.let {
                    spinnerKota.setItems(it)
                }
            }
            spinnerKota.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                selectedKota = newItem
                isInputCorrect()
            })
            etNama.doAfterTextChanged {
                emptyData(it.toString(), etNamaLayout)
                isInputCorrect()
            }
            etAlamat.doAfterTextChanged {
                emptyData(it.toString(), etAlamatLayout)
                if (!cbAlamat.isChecked) currentAlamat = it.toString()
                isInputCorrect()
            }
            cvKoordinat.setOnClickListener {
                val fragment = PinPointLokasiFragment.newInstance()
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
                var gambar: File? = null
                currentImageUri?.let {
                    gambar =
                        uriToFile(
                            currentImageUri!!,
                            this@UpdatePerusahaanActivity
                        ).reduceFileImage()
                }
                btnSubmit.isEnabled = false
                updatePerusahaanViewModel.updatePerusahaan(
                    id = perusahaan!!.id,
                    latitude = pinPointLokasiViewModel.latitude.value!!,
                    longitude = pinPointLokasiViewModel.longitude.value!!,
                    nama = etNama.text.toString(),
                    alamat = etAlamat.text.toString(),
                    provinsi = selectedProvinsi.toString(),
                    kota = selectedKota.toString(),
                    gambar = gambar,
                    successListener = {
                        openActivityWithExtras(PerusahaanDetailActivity::class.java) {
                            putString(INTENT_ID, it)
                        }
                    }
                )
            }
            cbAlamat.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    pinPointLokasiViewModel.alamat.value?.let {
                        etAlamat.setText(it)
                    }
                    etAlamat.isEnabled = false
                } else {
                    if (currentAlamat != null) etAlamat.setText(currentAlamat)
                    etAlamat.isEnabled = true
                }
            }
            pinPointLokasiViewModel.alamat.observe(this@UpdatePerusahaanActivity) {
                it?.let {
                    cbAlamat.setVisible()
                    if (cbAlamat.isChecked)
                        etAlamat.setText(it)
                } ?: cbAlamat.setGone()
            }
        }
    }

    private fun isInputCorrect() {
        with(binding) {
            btnSubmit.isEnabled = TextUtils.isEmpty(etNamaLayout.error)
                    && TextUtils.isEmpty(etAlamatLayout.error)
                    && etNama.text.toString().isNotEmpty()
                    && etAlamat.text.toString().isNotEmpty()
                    && pinPointLokasiViewModel.latitude.value != null
                    && pinPointLokasiViewModel.longitude.value != null
                    && selectedProvinsi != null
                    && selectedKota != null
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