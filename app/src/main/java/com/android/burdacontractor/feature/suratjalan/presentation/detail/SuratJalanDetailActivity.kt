package com.android.burdacontractor.feature.suratjalan.presentation.detail

import android.os.Bundle
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
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.dialIntent
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.core.utils.getDateFromMillis
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.openWhatsAppChat
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivitySuratJalanDetailBinding
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetailItem
import com.android.burdacontractor.feature.suratjalan.presentation.BarangPeminjamanSuratJalanFragment
import com.android.burdacontractor.feature.suratjalan.presentation.BarangPenggunaanSuratJalanFragment
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
                suratJalan.let { sj ->
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

    private fun initBarang() {
        binding.apply {
            val deleteVisible = suratJalan!!.adminGudang.id == user!!.id
                    && suratJalan!!.status == SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER.name

            val peminjamanAdapter = ListPeminjamanSuratJalanAdapter(
                deleteVisible = deleteVisible,
                checkedListData = listOf(),
                listener = {},
                deleteListener = {
                    val type = if (suratJalan!!.tipe != SuratJalanTipe.PENGEMBALIAN.name) {
                        "peminjaman"
                    } else {
                        "pengembalian"
                    }
                    CustomDialog(
                        mainButtonText = "Ya",
                        secondaryButtonText = "Tidak",
                        secondaryButtonBackgroundDrawable = R.drawable.semi_rounded_outline_red,
                        secondaryButtonTextColor = R.color.red,
                        mainButtonBackgroundDrawable = R.drawable.semi_rounded_red,
                        title = "Hapus $type",
                        subtitle = "Apakah anda yakin ingin menghapus $type ${it.kode} dari surat jalan ${suratJalan!!.kodeSurat} ?",
                        blockMainButton = {
                            suratJalanDetailViewModel.deleteSuratJalanChild(
                                it.sjChildId.toString(),
                                suratJalan!!.tipe
                            ) {
                                refreshData()
                            }
                        },
                        blockSecondaryButton = {}).show(
                        supportFragmentManager,
                        "DeleteSjChild"
                    )
                },
                barangListener = { barang ->
                    val fragment = BarangPeminjamanSuratJalanFragment.newInstance(barang)
                    fragment.show(supportFragmentManager, "BarangPeminjaman")
                },
                userId = user!!.id
            )
            peminjamanAdapter.submitList(suratJalan!!.peminjaman.sortedBy { it.asal.id })
            rvPeminjaman.layoutManager = LinearLayoutManager(
                this@SuratJalanDetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvPeminjaman.adapter = peminjamanAdapter

            val penggunaanAdapter = ListPenggunaanSuratJalanAdapter(
                deleteVisible = deleteVisible,
                checkedListData = listOf(),
                listener = {},
                deleteListener = {
                    val type = if (suratJalan!!.tipe != SuratJalanTipe.PENGEMBALIAN.name) {
                        "penggunaan"
                    } else {
                        "pengembalian penggunaan"
                    }
                    CustomDialog(
                        mainButtonText = "Ya",
                        secondaryButtonText = "Tidak",
                        secondaryButtonBackgroundDrawable = R.drawable.semi_rounded_outline_red,
                        secondaryButtonTextColor = R.color.red,
                        mainButtonBackgroundDrawable = R.drawable.semi_rounded_red,
                        title = "Hapus $type",
                        subtitle = "Apakah anda yakin ingin menghapus $type ${it.kode} dari surat jalan ${suratJalan!!.kodeSurat} ?",
                        blockMainButton = {
                            suratJalanDetailViewModel.deleteSuratJalanChild(
                                it.sjChildId.toString(),
                                suratJalan!!.tipe
                            ) {
                                refreshData()
                            }
                        },
                        blockSecondaryButton = {}).show(
                        supportFragmentManager,
                        "DeleteSjChild"
                    )
                },
                barangListener = { barang ->
                    val fragment = BarangPenggunaanSuratJalanFragment.newInstance(barang)
                    fragment.show(supportFragmentManager, "BarangPenggunaan")
                },
                userId = user!!.id
            )
            penggunaanAdapter.submitList(suratJalan!!.penggunaan.sortedBy { it.asal.id })
            rvPenggunaan.layoutManager = LinearLayoutManager(
                this@SuratJalanDetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvPenggunaan.adapter = penggunaanAdapter
        }
    }

    private fun initUi() {
        with(binding) {
            tvKodeSj.text = suratJalan!!.kodeSurat
            tvStatus.text = enumValueToNormal(suratJalan!!.status)
            initBarang()
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
                    layoutButton.setGone()
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
            ivSiteManager.layoutParams.width = 100
            ivSiteManager.layoutParams.height = 100
            ivSiteManager.requestLayout()
            tvNoHpSiteManager.text = suratJalan!!.siteManager.noHp
            tvNamaSiteManager.text = suratJalan!!.siteManager.nama
            btnWaSiteManager.setOnClickListener {
                it.openWhatsAppChat(suratJalan!!.siteManager.noHp)
            }
            btnHubungiSiteManager.setOnClickListener {
                dialIntent(suratJalan!!.siteManager.noHp)
            }

            ivKendaraan.setImageFromUrl(
                suratJalan!!.kendaraan.gambar,
                this@SuratJalanDetailActivity
            )
            ivKendaraan.layoutParams.width = 100
            ivKendaraan.layoutParams.height = 100
            ivKendaraan.requestLayout()
            tvMerkKendaraan.text = suratJalan!!.kendaraan.merk
            tvPlatKendaraan.text = suratJalan!!.kendaraan.platNomor

            tvNamaDriver.text = suratJalan!!.logistic.nama
            tvNoHpDriver.text = suratJalan!!.logistic.noHp
            ivDriver.setImageFromUrl(suratJalan!!.logistic.foto, this@SuratJalanDetailActivity)
            ivDriver.layoutParams.width = 100
            ivDriver.layoutParams.height = 100
            ivDriver.requestLayout()
            btnWaDriver.setOnClickListener {
                it.openWhatsAppChat(suratJalan!!.logistic.noHp)
            }
            btnHubungiDriver.setOnClickListener {
                dialIntent(suratJalan!!.logistic.noHp)
            }

            suratJalan!!.fotoBukti?.let {
                layoutFotoBukti.setVisible()
                ivFotoBukti.setImageFromUrl(it, this@SuratJalanDetailActivity, false)
            } ?: layoutFotoBukti.setGone()

            ivAdminGudang.setImageFromUrl(
                suratJalan!!.adminGudang.foto,
                this@SuratJalanDetailActivity
            )
            ivAdminGudang.layoutParams.width = 100
            ivAdminGudang.layoutParams.height = 100
            ivAdminGudang.requestLayout()
            tvNoHpAdminGudang.text = suratJalan!!.adminGudang.noHp
            tvNamaAdminGudang.text = suratJalan!!.adminGudang.nama
            btnWaAdminGudang.setOnClickListener {
                it.openWhatsAppChat(suratJalan!!.adminGudang.noHp)
            }
            btnHubungiAdminGudang.setOnClickListener {
                dialIntent(suratJalan!!.adminGudang.noHp)
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

                layoutPenanggungJawab.setVisible()
                tvTitlePenanggungJawab.text = enumValueToNormal(verif.sebagai)
                suratJalan!!.penanggungJawab?.let { penanggungJawab ->
                    ivPenanggungJawab.setImageFromUrl(
                        penanggungJawab.foto,
                        this@SuratJalanDetailActivity
                    )
                    ivPenanggungJawab.layoutParams.width = 100
                    ivPenanggungJawab.layoutParams.height = 100
                    ivPenanggungJawab.requestLayout()
                    tvNoHpPenanggungJawab.text = penanggungJawab.noHp
                    tvNamaPenanggungJawab.text = penanggungJawab.nama
                    btnWaPenanggungJawab.setOnClickListener {
                        it.openWhatsAppChat(penanggungJawab.noHp)
                    }
                    btnHubungiPenanggungJawab.setOnClickListener {
                        dialIntent(penanggungJawab.noHp)
                    }
                }
            } ?: layoutTtdTggJwb.setGone()

            suratJalan!!.ttdPenanggungJawabPeminjam?.let { verif ->
                layoutTtdTggJwbPeminjam.setVisible()
                ivTtdTggJwbPeminjam.setImageFromUrl(verif.ttd, this@SuratJalanDetailActivity)
                tvTertandaTggJwbPeminjam.text =
                    getString(R.string.nama_dan_role, verif.nama, enumValueToNormal(verif.role))
                tvSebagaiTggJwbPeminjam.text = enumValueToNormal(verif.sebagai)

                layoutPenerima.setVisible()
                suratJalan!!.penanggungJawabPeminjam?.let { penanggungJawab ->
                    ivPenerima.setImageFromUrl(
                        penanggungJawab.foto,
                        this@SuratJalanDetailActivity
                    )
                    ivPenerima.layoutParams.width = 100
                    ivPenerima.layoutParams.height = 100
                    ivPenerima.requestLayout()
                    tvNoHpPenerima.text = penanggungJawab.noHp
                    tvNamaPenerima.text = penanggungJawab.nama
                    btnWaPenerima.setOnClickListener {
                        it.openWhatsAppChat(penanggungJawab.noHp)
                    }
                    btnHubungiPenerima.setOnClickListener {
                        dialIntent(penanggungJawab.noHp)
                    }
                }
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
        binding.btnBack.setOnClickListener { finishAction() }
        customBackPressed()
    }

    private fun initLayout() {
        with(binding) {
            val role = user!!.role
            val userId = user!!.id
            if ((role == UserRole.LOGISTIC.name && (suratJalan!!.logistic.id != userId))) {
                CustomDialog(
                    mainButtonText = "Keluar",
                    mainButtonBackgroundDrawable = null,
                    secondaryButtonText = null,
                    secondaryButtonTextColor = null,
                    mainButtonTextColor = null,
                    secondaryButtonBackgroundDrawable = null,
                    title = "Tidak Bisa Melihat Surat Jalan",
                    subtitle = "Surat jalan ini sudah bukan untuk anda",
                    canTouchOutside = false,
                    image = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_error),
                    blockMainButton = { finish() },
                    blockSecondaryButton = {}).show(supportFragmentManager, "DOUnavailable")

            }

            // Hilangkan button hubungi pada data diri sendiri
            if (suratJalan!!.siteManager.id == userId)
                layoutHubungiSiteManager.setGone()
            if (suratJalan!!.adminGudang.id == userId)
                layoutHubungiAdminGudang.setGone()
            suratJalan!!.penanggungJawab?.let {
                if (it.id == userId)
                    layoutHubungiPenanggungJawab.setGone()
            }
            suratJalan!!.penanggungJawabPeminjam?.let {
                if (it.id == userId)
                    layoutHubungiPenerima.setGone()
            }
            if (suratJalan!!.logistic.id == userId)
                layoutHubungiDriver.setGone()

            when (user!!.role) {
                UserRole.ADMIN_GUDANG.name,
                UserRole.SUPERVISOR.name,
                UserRole.PROJECT_MANAGER.name,
                UserRole.SITE_MANAGER.name,
                UserRole.ADMIN.name -> {
                    if (suratJalan!!.status != SuratJalanStatus.SELESAI.name) {
                        btnPantauLokasi.setVisible()
                        btnPantauLokasi.setOnClickListener {
                            openActivityWithExtras(
                                PantauLokasiSuratJalanActivity::class.java,
                                false
                            ) {
                                putParcelable(Constant.INTENT_PARCEL, suratJalan)
                            }
                        }
                    }
                }
            }
            initButtonGiveTtdAndMarkComplete()
            when (user!!.role) {
                UserRole.ADMIN_GUDANG.name -> {
                    when (suratJalan!!.status) {
                        SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                            btnPantauLokasi.setVisible()
                            setButtonStyle(btnPantauLokasi, true)
                        }

                        SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name -> {
                            btnPantauLokasi.setVisible()
                            setButtonStyle(btnPantauLokasi, true)
                        }
                    }
                }

                UserRole.SUPERVISOR.name, UserRole.PROJECT_MANAGER.name, UserRole.SITE_MANAGER.name -> {
                    when (suratJalan!!.status) {
                        SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                            btnPantauLokasi.setVisible()
                            setButtonStyle(btnPantauLokasi, true)
                        }

                        SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name -> {
                            if (suratJalan!!.adminGudang.id == user!!.id)
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

                UserRole.ADMIN_GUDANG.name -> {
                    when (suratJalan!!.status) {
                        SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                            if (suratJalan!!.adminGudang.id == user!!.id) {
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
                                        title = "Hapus Surat Jalan",
                                        subtitle = "Apakah anda yakin ingin menghapus surat jalan ${suratJalan!!.kodeSurat} ?",
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

    private fun initButtonGiveTtdAndMarkComplete() {
        binding.apply {
            when (user!!.role) {
                UserRole.ADMIN_GUDANG.name,
                UserRole.SUPERVISOR.name,
                UserRole.SITE_MANAGER.name,
                UserRole.ADMIN.name -> {
                    if (suratJalan!!.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name) {
                        if (
                            (suratJalan!!.tipe == SuratJalanTipe.PENGEMBALIAN.name
                                    && (user!!.role == UserRole.ADMIN_GUDANG.name || user!!.role == UserRole.ADMIN.name)
                                    && suratJalan!!.ttdPenanggungJawab != null)
                            || (suratJalan!!.tipe == SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK.name
                                    && (user!!.role == UserRole.SUPERVISOR.name || user!!.role == UserRole.SITE_MANAGER.name))
                            || (suratJalan!!.tipe == SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK.name
                                    && (user!!.role == UserRole.SUPERVISOR.name || user!!.role == UserRole.SITE_MANAGER.name)
                                    && suratJalan!!.ttdPenanggungJawab != null)
                        ) {
                            btnTandaiSelesai.setVisible()
                            btnTandaiSelesai.setOnClickListener {
                                CustomDialog(
                                    mainButtonText = "Selesai",
                                    secondaryButtonText = "Batal",
                                    title = "Tandai Selesai Surat Jalan",
                                    subtitle = "Apakah anda yakin ingin menandai selesai surat jalan ${suratJalan!!.kodeSurat} ?",
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
                        } else if (
                            ((suratJalan!!.tipe == SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK.name || suratJalan!!.tipe == SuratJalanTipe.PENGEMBALIAN.name)
                                    && (user!!.role == UserRole.SUPERVISOR.name || user!!.role == UserRole.SITE_MANAGER.name)
                                    && suratJalan!!.ttdPenanggungJawab == null)
                        ) {
                            btnBeriTtd.setVisible()
                            btnBeriTtd.setOnClickListener {
                                CustomDialog(
                                    mainButtonText = "Berikan TTD",
                                    secondaryButtonText = "Batal",
                                    title = "Berikan Tanda Tangan Surat Jalan",
                                    subtitle = "Apakah anda yakin ingin memberi tanda tangan untuk surat jalan ${suratJalan!!.kodeSurat} ?",
                                    blockMainButton = {
                                        suratJalanDetailViewModel.giveTtdSuratJalan(
                                            suratJalan!!.id
                                        ) {
                                            binding.btnBeriTtd.setGone()
                                            refreshData()
                                        }
                                    },
                                    blockSecondaryButton = {}
                                ).show(supportFragmentManager, "MarkCompleteFragment")
                            }
                        }
                    }
                }
            }
        }
    }
}