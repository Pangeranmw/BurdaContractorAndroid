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
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.presentation.adapter.ListDeliveryOrderAdapter
import com.android.burdacontractor.core.presentation.adapter.ListSuratJalanAdapter
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
                kendaraan.logisticId?.let {
                    kendaraanDetailViewModel.logistic.observe(this) {
                        setPengendara(it)
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
            if (listDo.isNotEmpty()) {
                tvEmptyDeliveryOrderAktif.setGone()
                val adapter = ListDeliveryOrderAdapter(user.role, user.id) {
                    openActivityWithExtras(DeliveryOrderDetailActivity::class.java, false) {
                        putString(INTENT_ID, it.id)
                    }
                }
                adapter.submitList(listDo)
                rvDeliveryOrder.layoutManager = LinearLayoutManager(
                    this@KendaraanDetailActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                rvDeliveryOrder.adapter = adapter
            } else {
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
            if (listSj.isNotEmpty()) {
                val adapter = ListSuratJalanAdapter(user.role, user.id) {
                    openActivityWithExtras(SuratJalanDetailActivity::class.java, false) {
                        putString(INTENT_ID, it.id)
                    }
                }
                adapter.submitList(listSj)
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
            } else {
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
                kendaraanDetailViewModel.deleteKendaraan(kendaraan.id) {
                    finish()
                }
            }
            btnUbahKendaraan.setOnClickListener {
                openActivityWithExtras(UpdateKendaraanActivity::class.java, false) {
                    putParcelable(INTENT_PARCEL, kendaraan)
                    putParcelable(UpdateKendaraanActivity.GUDANG_BY_ID, gudang)
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

    private fun refreshData() {
        kendaraanDetailViewModel.getKendaraanById(id)
    }
}