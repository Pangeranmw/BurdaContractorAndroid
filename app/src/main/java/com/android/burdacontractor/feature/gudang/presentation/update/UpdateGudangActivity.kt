package com.android.burdacontractor.feature.gudang.presentation.update

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
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
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.emptyData
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.core.utils.getImageUri
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.parcelable
import com.android.burdacontractor.core.utils.reduceFileImage
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.core.utils.uriToFile
import com.android.burdacontractor.databinding.ActivityAddGudangBinding
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.gudang.presentation.detail.GudangDetailActivity
import com.google.android.material.snackbar.Snackbar
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class UpdateGudangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddGudangBinding
    private val updateGudangViewModel: UpdateGudangViewModel by viewModels()
    private val pinPointLokasiViewModel: PinPointLokasiViewModel by viewModels()
    private var gudang: GudangById? = null
    private var currentImageUri: Uri? = null
    private var selectedProvinsi: String? = null
    private var selectedKota: String? = null
    private var currentAlamat: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGudangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gudang = intent.parcelable(INTENT_PARCEL)
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        updateGudangViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        updateGudangViewModel.state.observe(this) {
            binding.progressBar.isVisible = it == StateResponse.LOADING
        }
        updateGudangViewModel.messageResponse.observe(this) {
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
            btnSubmit.text = getString(R.string.ubah_gudang)
            tvAppBar.text = getString(R.string.ubah_gudang)

            spinnerProvinsi.text = gudang!!.provinsi
            spinnerKota.text = gudang!!.kota
            selectedProvinsi = gudang!!.provinsi
            selectedKota = gudang!!.kota
            etNama.setText(gudang!!.nama)
            tvKoordinat.text = getString(
                R.string.latitude_longitude,
                gudang!!.latitude.toString(),
                gudang!!.longitude.toString()
            )
            pinPointLokasiViewModel.setLatitude(gudang!!.latitude.toString())
            pinPointLokasiViewModel.setLongitude(gudang!!.longitude.toString())
            etAlamat.setText(gudang!!.alamat)
            tvGambar.setGone()
            ivGambar.setVisible()
            ivGambar.setImageFromUrl(gudang!!.gambar, this@UpdateGudangActivity)

            updateGudangViewModel.province.observe(this@UpdateGudangActivity) {
                it?.let {
                    spinnerProvinsi.setItems(it)
                }
            }
            spinnerProvinsi.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                selectedKota = null
                selectedProvinsi = newItem
                updateGudangViewModel.getCityByProvince(newItem)
                spinnerKota.clearSelectedItem()
                spinnerKota.setItems(emptyList<String>())
                isInputCorrect()
            })
            pinPointLokasiViewModel.latitude.observe(this@UpdateGudangActivity) { lat ->
                pinPointLokasiViewModel.longitude.observe(this@UpdateGudangActivity) { lon ->
                    if (lat != null && lon != null) {
                        binding.tvKoordinat.text = getString(R.string.latitude_longitude, lat, lon)
                    }
                }
            }
            updateGudangViewModel.city.observe(this@UpdateGudangActivity) {
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
                            this@UpdateGudangActivity
                        ).reduceFileImage()
                }
                btnSubmit.isEnabled = false
                updateGudangViewModel.updateGudang(
                    id = gudang!!.id,
                    latitude = pinPointLokasiViewModel.latitude.value!!,
                    longitude = pinPointLokasiViewModel.longitude.value!!,
                    nama = etNama.text.toString(),
                    alamat = etAlamat.text.toString(),
                    provinsi = selectedProvinsi.toString(),
                    kota = selectedKota.toString(),
                    gambar = gambar,
                    successListener = {
                        openActivityWithExtras(GudangDetailActivity::class.java) {
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
            pinPointLokasiViewModel.alamat.observe(this@UpdateGudangActivity) {
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
        binding.btnBack.setOnClickListener { finishAction() }
        customBackPressed()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}