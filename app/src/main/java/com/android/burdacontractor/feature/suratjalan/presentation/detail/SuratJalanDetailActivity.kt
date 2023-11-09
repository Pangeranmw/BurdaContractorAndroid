package com.android.burdacontractor.feature.suratjalan.presentation.detail

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.adapter.ListPeminjamanSuratJalanAdapter
import com.android.burdacontractor.core.presentation.adapter.ListPenggunaanSuratJalanAdapter
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.dialIntent
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getDateFromMillis
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.openWhatsAppChat
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivitySuratJalanDetailBinding
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetailItem
import com.android.burdacontractor.feature.suratjalan.presentation.location.PantauLokasiSuratJalanActivity
import com.android.burdacontractor.feature.suratjalan.presentation.location.TelusuriLokasiSuratJalanActivity
import com.android.burdacontractor.feature.suratjalan.presentation.print.SuratJalanCetakActivity
import com.android.burdacontractor.feature.suratjalan.presentation.uploadphoto.UploadFotoBuktiSuratJalanActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuratJalanDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuratJalanDetailBinding
    private var user: UserByTokenItem? = null
    private val suratJalanDetailViewModel: SuratJalanDetailViewModel by viewModels()
    private lateinit var id: String
    private var suratJalan: SuratJalanDetailItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuratJalanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra(Constant.INTENT_ID).toString()
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        suratJalanDetailViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        suratJalanDetailViewModel.id.observe(this) {
            if (it == null) suratJalanDetailViewModel.setId(id)
        }
        suratJalanDetailViewModel.suratJalan.observe(this) { suratJalan ->
            suratJalanDetailViewModel.user.observe(this) { user ->
                suratJalan?.let { sj ->
                    user?.let { us ->
                        this.suratJalan = sj
                        this.user = us
                        initLayout()
                        initUi()
                    }
                }
            }
        }
        suratJalanDetailViewModel.state.observe(this) {
            binding.srLayout.isRefreshing = it == StateResponse.LOADING
        }
        suratJalanDetailViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setButtonStyle(button: AppCompatButton, isPrimary: Boolean) {
        if (isPrimary) {
            button.background =
                AppCompatResources.getDrawable(this, R.drawable.semi_rounded_primary)
            button.setTextColor(AppCompatResources.getColorStateList(this, R.color.white))
        } else {
            button.background =
                AppCompatResources.getDrawable(this, R.drawable.semi_rounded_outline_main)
            button.setTextColor(AppCompatResources.getColorStateList(this, R.color.primary_main))
        }
    }

    override fun onRestart() {
        super.onRestart()
        refreshData()
    }

    private fun refreshData() {
        suratJalanDetailViewModel.getSuratJalanById(id)
    }

    private fun initUi() {
        with(binding) {
            val peminjamanAdapter = ListPeminjamanSuratJalanAdapter(
                checkedVisible = false,
                deleteVisible = false,
                checkedListData = listOf(),
                listener = {},
                deleteListener = {},
                checkedDataListener = { _, _ -> },
                barangListener = {})
            peminjamanAdapter.submitList(suratJalan!!.peminjaman.sortedBy { it.asal.id })
            rvPeminjaman.layoutManager = LinearLayoutManager(
                this@SuratJalanDetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvPeminjaman.adapter = peminjamanAdapter

            val penggunaanAdapter = ListPenggunaanSuratJalanAdapter(
                checkedVisible = false,
                deleteVisible = false,
                checkedListData = listOf(),
                listener = {},
                deleteListener = {},
                checkedDataListener = { _, _ -> },
                barangListener = {})
            penggunaanAdapter.submitList(suratJalan!!.penggunaan.sortedBy { it.asal.id })
            rvPenggunaan.layoutManager = LinearLayoutManager(
                this@SuratJalanDetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvPenggunaan.adapter = penggunaanAdapter

            tvKodeSj.text = suratJalan!!.kodeSurat
            tvStatus.text = enumValueToNormal(suratJalan!!.status)
            when (suratJalan!!.status) {
                SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name -> {
                    tvStatus.setTextColor(
                        ContextCompat.getColor(
                            this@SuratJalanDetailActivity,
                            R.color.orange_dark_full
                        )
                    )
                }

                SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                    tvStatus.setTextColor(
                        ContextCompat.getColor(
                            this@SuratJalanDetailActivity,
                            R.color.red
                        )
                    )
                }

                SuratJalanStatus.SELESAI.name -> {
                    tvStatus.setTextColor(
                        ContextCompat.getColor(
                            this@SuratJalanDetailActivity,
                            R.color.secondary_main
                        )
                    )
                }
            }
            tvCreatedAt.text = getDateFromMillis(suratJalan!!.createdAt)
            tvUpdatedAt.text = getDateFromMillis(suratJalan!!.updatedAt)

            tvNamaTujuan.text = suratJalan!!.tempatTujuan.nama
            tvAlamatTujuan.text = suratJalan!!.tempatTujuan.alamat

            ivSiteManager.setImageFromUrl(
                suratJalan!!.siteManager.foto,
                this@SuratJalanDetailActivity
            )
            if (suratJalan?.siteManager?.foto != null) {
                ivSiteManager.layoutParams.width = 100
                ivSiteManager.layoutParams.height = 100
                ivSiteManager.requestLayout()
            }
            tvNoHpSiteManager.text = suratJalan!!.siteManager.noHp
            tvNamaSiteManager.text = suratJalan!!.siteManager.nama

            ivKendaraan.setImageFromUrl(
                suratJalan!!.kendaraan.gambar,
                this@SuratJalanDetailActivity
            )
            if (suratJalan?.kendaraan?.gambar != null) {
                ivKendaraan.layoutParams.width = 100
                ivKendaraan.layoutParams.height = 100
                ivKendaraan.requestLayout()
            }
            tvMerkKendaraan.text = suratJalan!!.kendaraan.merk
            tvPlatKendaraan.text = suratJalan!!.kendaraan.platNomor

            tvNamaDriver.text = suratJalan!!.logistic.nama
            tvNoHpDriver.text = suratJalan!!.logistic.noHp
            ivDriver.setImageFromUrl(suratJalan!!.logistic.foto, this@SuratJalanDetailActivity)
            if (suratJalan?.logistic?.foto != null) {
                ivDriver.layoutParams.width = 100
                ivDriver.layoutParams.height = 100
                ivDriver.requestLayout()
            }

            btnWaDriver.setOnClickListener {
                it.openWhatsAppChat(suratJalan!!.logistic.noHp)
            }
            btnHubungiDriver.setOnClickListener {
                dialIntent(suratJalan!!.logistic.noHp)
            }

            btnWaSiteManager.setOnClickListener {
                it.openWhatsAppChat(suratJalan!!.siteManager.noHp)
            }
            btnHubungiSiteManager.setOnClickListener {
                dialIntent(suratJalan!!.siteManager.noHp)
            }

            suratJalan?.fotoBukti?.let {
                layoutFotoBukti.setVisible()
                ivFotoBukti.setImageFromUrl(it, this@SuratJalanDetailActivity, false)
            }

            ivAdminGudang.setImageFromUrl(
                suratJalan!!.adminGudang!!.foto,
                this@SuratJalanDetailActivity
            )
            if (suratJalan?.adminGudang?.foto != null) {
                ivAdminGudang.layoutParams.width = 100
                ivAdminGudang.layoutParams.height = 100
                ivAdminGudang.requestLayout()
            }
            tvNoHpAdminGudang.text = suratJalan!!.adminGudang!!.noHp
            tvNamaAdminGudang.text = suratJalan!!.adminGudang!!.nama
            btnWaAdminGudang.setOnClickListener {
                it.openWhatsAppChat(suratJalan!!.adminGudang!!.noHp)
            }
            btnHubungiAdminGudang.setOnClickListener {
                dialIntent(suratJalan!!.adminGudang!!.noHp)
            }

            btnDownload.setOnClickListener {
                openActivityWithExtras(SuratJalanCetakActivity::class.java, false) {
                    putString(Constant.INTENT_ID, suratJalan!!.id)
                    putString(Constant.INTENT_KODE, suratJalan!!.kodeSurat)
                }
            }
            srLayout.setOnRefreshListener {
                refreshData()
            }

            suratJalan!!.ttdAdmin?.let { verif ->
                layoutTtdAdminGudang.setVisible()
                ivTtdAdminGudang.setImageFromUrl(verif.ttd, this@SuratJalanDetailActivity)
                tvTertandaAdminGudang.text =
                    getString(R.string.nama_dan_role, verif.nama, enumValueToNormal(verif.role))
                tvSebagaiAdmin.text = enumValueToNormal(verif.sebagai)
            } ?: layoutTtdAdminGudang.setGone()

            suratJalan!!.ttdDriver?.let { verif ->
                layoutTtdDriver.setVisible()
                ivTtdDriver.setImageFromUrl(verif.ttd, this@SuratJalanDetailActivity)
                tvTertandaDriver.text =
                    getString(R.string.nama_dan_role, verif.nama, "Driver")
            } ?: layoutTtdDriver.setGone()

            suratJalan!!.ttdPenanggungJawab?.let { verif ->
                layoutTtdTggJwb.setVisible()
                ivTtdTggJwb.setImageFromUrl(verif.ttd, this@SuratJalanDetailActivity)
                tvTertandaTggJwb.text =
                    getString(R.string.nama_dan_role, verif.nama, enumValueToNormal(verif.role))
                tvSebagaiTggJwb.text = enumValueToNormal(verif.sebagai)
            } ?: layoutTtdTggJwb.setGone()

            suratJalan!!.ttdPenanggungJawabPeminjam?.let { verif ->
                layoutTtdTggJwbPeminjam.setVisible()
                ivTtdTggJwbPeminjam.setImageFromUrl(verif.ttd, this@SuratJalanDetailActivity)
                tvTertandaTggJwbPeminjam.text =
                    getString(R.string.nama_dan_role, verif.nama, enumValueToNormal(verif.role))
                tvSebagaiTggJwbPeminjam.text = enumValueToNormal(verif.sebagai)
            } ?: layoutTtdTggJwbPeminjam.setGone()

            when (suratJalan!!.tipe) {
                SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK.name -> {
                    ivJenis.setImageResource(R.drawable.ic_send_req)
                }

                SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK.name -> {
                    ivJenis.setImageResource(R.drawable.ic_send_req)
                }

                SuratJalanTipe.PENGEMBALIAN.name -> {
                    ivJenis.setImageResource(R.drawable.ic_return_req)
                    tvPeminjaman.text = getString(R.string.pengembalian)
                    tvPenggunaan.text = getString(R.string.pengembalian_penggunaan)
                }
            }
            onBackPressedCallback()
        }
    }

    private fun onBackPressedCallback() {
        binding.btnBack.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                overridePendingTransition(0, 0)
            }
        })
    }

    private fun initLayout() {
        with(binding) {
            if (user!!.role == UserRole.LOGISTIC.name && (suratJalan!!.logistic.id != user!!.id)) {
                CustomDialog(
                    mainButtonText = "Keluar",
                    mainButtonBackgroundDrawable = null,
                    secondaryButtonText = null,
                    secondaryButtonTextColor = null,
                    mainButtonTextColor = null,
                    secondaryButtonBackgroundDrawable = null,
                    title = "Tidak Bisa Melihat Delivery Order",
                    subtitle = "Delivery order ini sudah bukan untuk anda",
                    canTouchOutside = false,
                    image = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_error),
                    blockMainButton = { finish() },
                    blockSecondaryButton = {}).show(supportFragmentManager, "DOUnavailable")
            }

            // Hilangkan button hubungi pada data diri sendiri
            if (suratJalan!!.siteManager.id == user!!.id)
                layoutHubungiSiteManager.setGone()
            if (suratJalan!!.adminGudang?.id == user!!.id)
                layoutHubungiAdminGudang.setGone()
            if (suratJalan!!.logistic.id == user!!.id)
                layoutHubungiDriver.setGone()

            when (user!!.role) {
                UserRole.ADMIN_GUDANG.name, UserRole.PURCHASING.name, UserRole.ADMIN.name -> {
                    btnPantauLokasi.setOnClickListener {
                        openActivityWithExtras(PantauLokasiSuratJalanActivity::class.java, false) {
                            putParcelable(Constant.INTENT_PARCEL, suratJalan)
                        }
                    }
                }
            }
            when (suratJalan!!.status) {
                SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name -> {
                    when (user!!.role) {
                        UserRole.ADMIN_GUDANG.name, UserRole.PURCHASING.name, UserRole.ADMIN.name -> {
                            btnTandaiSelesai.setVisible()
                            btnTandaiSelesai.setOnClickListener {
                                CustomDialog(
                                    mainButtonText = "Selesai",
                                    secondaryButtonText = "Batal",
                                    title = "Tandai Selesai Delivery Order",
                                    subtitle = "Apakah anda yakin ingin menandai selesai surat jalan ${suratJalan!!.kodeSurat} ?",
                                    image = null,
                                    blockMainButton = {
                                        suratJalanDetailViewModel.markCompleteSuratJalan(
                                            suratJalan!!.id
                                        ) {
                                            refreshData()
                                        }
                                    },
                                    blockSecondaryButton = {}
                                ).show(supportFragmentManager, "MarkCompleteFragment")
                            }
                        }
                    }
                }

                SuratJalanStatus.SELESAI.name -> {
                    layoutButton.setGone()
                }
            }
            when (user!!.role) {
                UserRole.ADMIN_GUDANG.name -> {
                    when (suratJalan!!.status) {
                        SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                            btnPantauLokasi.setVisible()
                            setButtonStyle(btnPantauLokasi, true)
                        }

                        SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name -> {
                            if (suratJalan!!.adminGudang?.id == user!!.id)
                                btnPantauLokasi.setVisible()
                        }
                    }
                }

                UserRole.LOGISTIC.name -> {
                    btnPantauLokasi.setGone()
                    btnTelusuriLokasi.setVisible()
                    btnTelusuriLokasi.setOnClickListener {
                        openActivityWithExtras(
                            TelusuriLokasiSuratJalanActivity::class.java,
                            false
                        ) {
                            putParcelable(Constant.INTENT_PARCEL, suratJalan)
                        }
                    }
                    btnAntarBarang.setGone()
                    when (suratJalan!!.status) {
                        SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                            btnAntarBarang.setVisible()
                            btnAntarBarang.setOnClickListener {
                                CustomDialog(
                                    mainButtonText = "Antar",
                                    secondaryButtonText = "Batal",
                                    title = "Antar Barang",
                                    subtitle = "Apakah anda yakin ingin mengantar surat jalan ${suratJalan!!.kodeSurat} ?",
                                    image = null,
                                    blockMainButton = {
                                        suratJalanDetailViewModel.sendSuratJalan(suratJalan!!.id) {
                                            refreshData()
                                        }
                                    },
                                    blockSecondaryButton = {}
                                ).show(supportFragmentManager, "SendSjFragment")
                            }
                        }

                        SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name -> {
                            btnUploadFotoBukti.setVisible()
                            btnUploadFotoBukti.setOnClickListener {
                                openActivityWithExtras(
                                    UploadFotoBuktiSuratJalanActivity::class.java,
                                    false
                                ) {
                                    putParcelable(
                                        UploadFotoBuktiSuratJalanActivity.DELIVERY_ORDER,
                                        suratJalan
                                    )
                                }
                            }
                        }
                    }
                }

                UserRole.PURCHASING.name -> {
                    when (suratJalan!!.status) {
                        SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                            if (suratJalan!!.siteManager.id == user!!.id) {
                                btnUbahSj.setVisible()
                                btnDelete.setVisible()
                                btnUbahSj.setOnClickListener {
//                                    openActivityWithExtras(
//                                        UpdateSuratJalanActivity::class.java,
//                                        false
//                                    ) {
//                                        putParcelable(Constant.INTENT_PARCEL, suratJalan!!)
//                                    }
                                }
                                btnDelete.setOnClickListener {
                                    CustomDialog(
                                        mainButtonText = "Ya",
                                        secondaryButtonText = "Tidak",
                                        secondaryButtonBackgroundDrawable = R.drawable.semi_rounded_outline_red,
                                        secondaryButtonTextColor = R.color.red,
                                        mainButtonBackgroundDrawable = R.drawable.semi_rounded_red,
                                        title = "Hapus Delivery Order",
                                        subtitle = "Apakah anda yakin ingin menghapus surat jalan ${suratJalan!!.kodeSurat} ?",
                                        image = null,
                                        blockMainButton = {
                                            suratJalanDetailViewModel.deleteSuratJalan(
                                                suratJalan!!.id
                                            ) {
                                                finish()
                                            }
                                        },
                                        blockSecondaryButton = {}).show(
                                        supportFragmentManager,
                                        "DeleteSj"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}