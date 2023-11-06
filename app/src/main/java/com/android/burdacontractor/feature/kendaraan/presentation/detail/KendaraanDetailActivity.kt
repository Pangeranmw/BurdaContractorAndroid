package com.android.burdacontractor.feature.kendaraan.presentation.detail

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.Constant.INTENT_PARCEL
import com.android.burdacontractor.core.domain.model.enums.JenisKendaraan
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.StatusKendaraan
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.adapter.ListDeliveryOrderAdapter
import com.android.burdacontractor.core.presentation.adapter.ListSuratJalanAdapter
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityKendaraanDetailBinding
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.presentation.detail.DeliveryOrderDetailActivity
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import com.android.burdacontractor.feature.kendaraan.presentation.pantau.PantauLokasiPengendaraActivity
import com.android.burdacontractor.feature.kendaraan.presentation.update.UpdateKendaraanActivity
import com.android.burdacontractor.feature.logistic.domain.model.LogisticById
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanDetailActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KendaraanDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKendaraanDetailBinding
    private lateinit var id: String
    private val kendaraanDetailViewModel: KendaraanDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKendaraanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra(INTENT_ID).toString()
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        kendaraanDetailViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        onBackPressedCallback()
        kendaraanDetailViewModel.id.observe(this) {
            if (it == null) kendaraanDetailViewModel.setId(id)
        }
        kendaraanDetailViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        kendaraanDetailViewModel.kendaraan.observe(this) { kendaraan ->
            kendaraanDetailViewModel.user.observe(this) { user ->
                if (user.role == UserRole.LOGISTIC.name) {
                    binding.btnUbahKendaraan.setGone()
                    binding.btnDelete.setGone()
                    binding.btnHapusPengendara.setGone()
                    binding.btnAjukanPengembalian.setVisible()
                } else {
                    binding.btnUbahKendaraan.setVisible()
                    binding.btnDelete.setVisible()
                    binding.btnHapusPengendara.setVisible()
                }
                kendaraan.logisticId?.let {
                    kendaraanDetailViewModel.logistic.observe(this) {
                        setPengendara(it)
                        binding.btnPantauPengendara.setOnClickListener { view ->
                            openActivityWithExtras(
                                PantauLokasiPengendaraActivity::class.java,
                                false
                            ) {
                                putString(INTENT_ID, kendaraan.logisticId)
                                putString(PantauLokasiPengendaraActivity.NAMA_PENGENDARA, it.nama)
                                putString(
                                    PantauLokasiPengendaraActivity.NAMA_KENDARAAN,
                                    "${kendaraan.merk} (${kendaraan.platNomor})"
                                )
                            }
                        }
                    }
                } ?: binding.layoutLogistic.setGone()
                kendaraanDetailViewModel.deliveryOrder.observe(this) {
                    setDeliveryOrderAdapter(user, it)
                }
                kendaraanDetailViewModel.gudang.observe(this) {
                    setGudang(it)
                    initUi(kendaraan, it)
                }
                kendaraanDetailViewModel.sjPengembalian.observe(this) {
                    setSuratJalanAdapter(user, it, SuratJalanTipe.PENGEMBALIAN)
                }
                kendaraanDetailViewModel.sjPengirimanGp.observe(this) {
                    setSuratJalanAdapter(user, it, SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
                }
                kendaraanDetailViewModel.sjPengirimanPp.observe(this) {
                    setSuratJalanAdapter(user, it, SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
                }
            }
        }
        kendaraanDetailViewModel.state.observe(this) {
            binding.srLayout.isRefreshing = it == StateResponse.LOADING
        }
    }

    private fun setPengendara(logistic: LogisticById) {
        binding.apply {
            layoutLogistic.setVisible()
            tvNamaLogistic.text = logistic.nama
            tvSjgpActive.text = getString(R.string.active_sjgp, logistic.countSjgpActive)
            tvSjppActive.text = getString(R.string.active_sjpp, logistic.countSjppActive)
            tvSjpgActive.text = getString(R.string.active_sjpg, logistic.countSjpgActive)
            tvDoActive.text = getString(R.string.active_do, logistic.countDoActive)
            tvNoHpLogistic.text = logistic.noHp
            ivLogistic.setImageFromUrl(logistic.foto, this@KendaraanDetailActivity)
        }
    }

    private fun setGudang(gudang: GudangById) {
        binding.apply {
            ivGudang.setImageFromUrl(gudang.gambar, this@KendaraanDetailActivity)
            tvAlamatGudang.text = gudang.alamat
            tvNamaGudang.text = gudang.nama
        }
    }

    private fun setDeliveryOrderAdapter(user: UserByTokenItem, listDo: List<AllDeliveryOrder>) {
        binding.apply {
            val newList = if (user.role == UserRole.LOGISTIC.name) {
                listDo.filter { it.idDriver == user.id }
            } else {
                listDo
            }
            tvEmptyDeliveryOrderAktif.setGone()
            val adapter = ListDeliveryOrderAdapter(user.role, user.id) {
                openActivityWithExtras(DeliveryOrderDetailActivity::class.java, false) {
                    putString(INTENT_ID, it.id)
                }
            }
            adapter.submitList(newList)
            rvDeliveryOrder.layoutManager = LinearLayoutManager(
                this@KendaraanDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvDeliveryOrder.adapter = adapter
            if (newList.isEmpty()) {
                tvEmptyDeliveryOrderAktif.setVisible()
            }
        }
    }

    private fun setSuratJalanAdapter(
        user: UserByTokenItem,
        listSj: List<AllSuratJalan>,
        tipe: SuratJalanTipe
    ) {
        binding.apply {
            val newList = if (user.role == UserRole.LOGISTIC.name) {
                listSj.filter { it.idDriver == user.id }
            } else {
                listSj
            }
            val adapter = ListSuratJalanAdapter(user.role, user.id) {
                openActivityWithExtras(SuratJalanDetailActivity::class.java, false) {
                    putString(INTENT_ID, it.id)
                }
            }
            adapter.submitList(newList)
            when (tipe) {
                SuratJalanTipe.PENGEMBALIAN -> {
                    tvEmptySjPengembalianAktif.setGone()
                    rvSjPengembalian.layoutManager = LinearLayoutManager(
                        this@KendaraanDetailActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    rvSjPengembalian.adapter = adapter
                }

                SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> {
                    tvEmptySjPengirimanGudangProyekAktif.setGone()
                    rvSjPengirimanGudangProyek.layoutManager = LinearLayoutManager(
                        this@KendaraanDetailActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    rvSjPengirimanGudangProyek.adapter = adapter
                }

                SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> {
                    tvEmptySjPengirimanProyekProyekAktif.setGone()
                    rvSjPengirimanProyekProyek.layoutManager = LinearLayoutManager(
                        this@KendaraanDetailActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    rvSjPengirimanProyekProyek.adapter = adapter
                }
            }
            if (newList.isEmpty()) {
                when (tipe) {
                    SuratJalanTipe.PENGEMBALIAN -> tvEmptySjPengembalianAktif.setVisible()
                    SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> tvEmptySjPengirimanGudangProyekAktif.setVisible()
                    SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> tvEmptySjPengirimanProyekProyekAktif.setVisible()
                }
            }
        }
    }

    private fun initUi(kendaraan: Kendaraan, gudang: GudangById) {
        binding.apply {
            srLayout.setOnRefreshListener {
                refreshData()
            }
            ivKendaraan.setImageFromUrl(kendaraan.gambar, this@KendaraanDetailActivity)
            when (kendaraan.jenis) {
                JenisKendaraan.MINIBUS.name -> tvNama.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.vehicle_minibus,
                    0,
                    0,
                    0
                )

                JenisKendaraan.MOBIL.name -> tvNama.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.vehicle_car,
                    0,
                    0,
                    0
                )

                JenisKendaraan.MOTOR.name -> tvNama.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.vehicle_motorcycle,
                    0,
                    0,
                    0
                )

                JenisKendaraan.PICKUP.name -> tvNama.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.vehicle_truck_pickup,
                    0,
                    0,
                    0
                )

                JenisKendaraan.TRONTON.name -> tvNama.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.vehicle_truck_tronton,
                    0,
                    0,
                    0
                )

                JenisKendaraan.TRUCK.name -> tvNama.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.vehicle_truck_box,
                    0,
                    0,
                    0
                )
            }
            tvNama.text = kendaraan.merk
            tvPlatNomor.text = kendaraan.platNomor
            tvJenisKendaraan.text = enumValueToNormal(kendaraan.jenis)
            tvStatus.text = enumValueToNormal(kendaraan.status)

            btnDelete.setOnClickListener {
                CustomDialog(
                    mainButtonText = "Hapus",
                    mainButtonBackgroundDrawable = R.drawable.semi_rounded_red,
                    secondaryButtonText = "Batal",
                    secondaryButtonTextColor = R.color.red,
                    mainButtonTextColor = null,
                    secondaryButtonBackgroundDrawable = R.drawable.semi_rounded_outline_red,
                    title = "Hapus Kendaraan",
                    subtitle = "Apakah anda yakin ingin menghapus kendaraan ${kendaraan.merk} ${kendaraan.platNomor}?",
                    image = null,
                    blockMainButton = {
                        kendaraanDetailViewModel.deleteKendaraan(kendaraan.id) {
                            finish()
                        }
                    },
                    blockSecondaryButton = {}).show(supportFragmentManager, "DeleteKendaraan")
            }
            btnHapusPengendara.setOnClickListener {
                CustomDialog(
                    mainButtonText = "Hapus",
                    mainButtonBackgroundDrawable = R.drawable.semi_rounded_red,
                    secondaryButtonText = "Batal",
                    secondaryButtonTextColor = R.color.red,
                    mainButtonTextColor = null,
                    secondaryButtonBackgroundDrawable = R.drawable.semi_rounded_outline_red,
                    title = "Hapus Pengendara",
                    subtitle = "Apakah anda yakin ingin menghapus pengendara pada kendaraan ini?",
                    image = null,
                    blockMainButton = {
                        kendaraanDetailViewModel.deletePengendara(kendaraan.id) {
                            refreshData()
                        }
                    },
                    blockSecondaryButton = {}).show(supportFragmentManager, "DeletePengendara")
            }
            btnUbahKendaraan.setOnClickListener {
                openActivityWithExtras(UpdateKendaraanActivity::class.java, false) {
                    putParcelable(INTENT_PARCEL, kendaraan)
                    putParcelable(UpdateKendaraanActivity.GUDANG_BY_ID, gudang)
                }
            }
            if (kendaraan.status == StatusKendaraan.AJUKAN_PENGEMBALIAN.name) {
                btnAjukanPengembalian.text = getString(R.string.batalkan_pengajuan_pengembalian)
            } else btnAjukanPengembalian.text = getString(R.string.ajukan_pengembalian)

            btnAjukanPengembalian.setOnClickListener {
                if (kendaraan.status == StatusKendaraan.AJUKAN_PENGEMBALIAN.name) {
                    CustomDialog(
                        mainButtonText = "Batal Ajukan",
                        secondaryButtonText = "Kembali",
                        title = "Batalkan Pengajuan Pengembalian",
                        subtitle = "Apakah anda yakin ingin membatalkan pengajuan pengembalian pada kendaraan ini?",
                        blockMainButton = {
                            kendaraanDetailViewModel.cancelReturnKendaraan(kendaraan.id) {
                                refreshData()
                            }
                        },
                        blockSecondaryButton = {}).show(
                        supportFragmentManager,
                        "AjukanPengembalian"
                    )
                } else {
                    CustomDialog(
                        mainButtonText = "Ajukan",
                        secondaryButtonText = "Batal",
                        title = "Ajukan Pengembalian",
                        subtitle = "Apakah anda yakin ingin mengajukan pengembalian pada kendaraan ini?",
                        blockMainButton = {
                            kendaraanDetailViewModel.returnKendaraan(kendaraan.id) {
                                refreshData()
                            }
                        },
                        blockSecondaryButton = {}).show(
                        supportFragmentManager,
                        "AjukanPengembalian"
                    )
                }
            }
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

    override fun onRestart() {
        super.onRestart()
        refreshData()
    }

    private fun refreshData() {
        kendaraanDetailViewModel.getKendaraanById(id)
        kendaraanDetailViewModel.getActiveDeliveryOrder(id)
        kendaraanDetailViewModel.getActiveSuratJalan(id, SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
        kendaraanDetailViewModel.getActiveSuratJalan(id, SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
        kendaraanDetailViewModel.getActiveSuratJalan(id, SuratJalanTipe.PENGEMBALIAN)
    }
}