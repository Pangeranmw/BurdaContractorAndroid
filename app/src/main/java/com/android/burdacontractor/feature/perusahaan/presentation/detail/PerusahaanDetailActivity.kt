package com.android.burdacontractor.feature.perusahaan.presentation.detail

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.Constant.INTENT_PARCEL
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.adapter.ListDeliveryOrderAdapter
import com.android.burdacontractor.core.presentation.adapter.ListStatistikDoSjAdapter
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.getDateFromMillis
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityPerusahaanDetailBinding
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.presentation.detail.DeliveryOrderDetailActivity
import com.android.burdacontractor.feature.perusahaan.domain.model.PerusahaanById
import com.android.burdacontractor.feature.perusahaan.presentation.update.UpdatePerusahaanActivity
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerusahaanDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerusahaanDetailBinding
    private lateinit var id: String
    private val perusahaanDetailViewModel: PerusahaanDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerusahaanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra(INTENT_ID).toString()
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        perusahaanDetailViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        onBackPressedCallback()
        perusahaanDetailViewModel.id.observe(this) {
            if (it == null) perusahaanDetailViewModel.setId(id)
        }
        perusahaanDetailViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        perusahaanDetailViewModel.perusahaan.observe(this) { perusahaan ->
            perusahaanDetailViewModel.user.observe(this) { user ->
                perusahaanDetailViewModel.deliveryOrder.observe(this) {
                    setDeliveryOrderAdapter(user, it)
                }
                perusahaanDetailViewModel.statDeliveryOrder.observe(this) {
                    setStatAdapter(binding.rvStatDo, it)
                }
//                perusahaanDetailViewModel.sjPengembalian.observe(this) {
//                    setSuratJalanAdapter(user, it, SuratJalanTipe.PENGEMBALIAN)
//                }
//                perusahaanDetailViewModel.sjPengirimanGp.observe(this) {
//                    setSuratJalanAdapter(user, it, SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
//                }
//                perusahaanDetailViewModel.sjPengirimanPp.observe(this) {
//                    setSuratJalanAdapter(user, it, SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
//                }
                initUi(perusahaan)
            }
        }
        perusahaanDetailViewModel.state.observe(this) {
            binding.srLayout.isRefreshing = it == StateResponse.LOADING
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
                    this@PerusahaanDetailActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                rvDeliveryOrder.adapter = adapter
            } else {
                tvEmptyDeliveryOrderAktif.setVisible()
            }
        }
    }

    private fun setStatAdapter(rv: RecyclerView, listStat: List<StatisticCountTitleItem>) {
        val adapter = ListStatistikDoSjAdapter()
        adapter.submitList(listStat)
        rv.layoutManager =
            GridLayoutManager(this@PerusahaanDetailActivity, 3, GridLayoutManager.VERTICAL, false)
        rv.adapter = adapter
    }
//    private fun setSuratJalanAdapter(
//        user: UserByTokenItem,
//        listSj: List<AllSuratJalan>,
//        tipe: SuratJalanTipe
//    ) {
//        binding.apply {
//            if (listSj.isNotEmpty()) {
//                val adapter = ListSuratJalanAdapter(user.role, user.id) {
//                    openActivityWithExtras(SuratJalanDetailActivity::class.java, false) {
//                        putString(INTENT_ID, it.id)
//                    }
//                }
//                adapter.submitList(listSj)
//                when (tipe) {
//                    SuratJalanTipe.PENGEMBALIAN -> {
//                        tvEmptySjPengembalianAktif.setGone()
//                        rvSjPengembalian.layoutManager = LinearLayoutManager(
//                            this@PerusahaanDetailActivity,
//                            LinearLayoutManager.HORIZONTAL,
//                            false
//                        )
//                        rvSjPengembalian.adapter = adapter
//                    }
//
//                    SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> {
//                        tvEmptySjPengirimanGudangProyekAktif.setGone()
//                        rvSjPengirimanGudangProyek.layoutManager = LinearLayoutManager(
//                            this@PerusahaanDetailActivity,
//                            LinearLayoutManager.HORIZONTAL,
//                            false
//                        )
//                        rvSjPengirimanGudangProyek.adapter = adapter
//                    }
//
//                    SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> {
//                        tvEmptySjPengirimanProyekProyekAktif.setGone()
//                        rvSjPengirimanProyekProyek.layoutManager = LinearLayoutManager(
//                            this@PerusahaanDetailActivity,
//                            LinearLayoutManager.HORIZONTAL,
//                            false
//                        )
//                        rvSjPengirimanProyekProyek.adapter = adapter
//                    }
//                }
//            } else {
//                when (tipe) {
//                    SuratJalanTipe.PENGEMBALIAN -> tvEmptySjPengembalianAktif.setVisible()
//                    SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> tvEmptySjPengirimanGudangProyekAktif.setVisible()
//                    SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> tvEmptySjPengirimanProyekProyekAktif.setVisible()
//                }
//            }
//        }
//    }

    private fun initUi(perusahaan: PerusahaanById) {
        binding.apply {
            srLayout.setOnRefreshListener {
                refreshData()
            }
            ivPerusahaan.setImageFromUrl(perusahaan.gambar, this@PerusahaanDetailActivity)
            tvNama.text = perusahaan.nama
            tvKotaProvinsi.text =
                getString(R.string.kota_dan_provinsi, perusahaan.kota, perusahaan.provinsi)
            tvTanggalDibuat.text = getDateFromMillis(perusahaan.createdAt)
            tvTerakhirDiperbarui.text = getDateFromMillis(perusahaan.updatedAt)
            tvAlamat.text = perusahaan.alamat

            btnDelete.setOnClickListener {
                CustomDialog(
                    mainButtonText = "Hapus",
                    mainButtonBackgroundDrawable = R.drawable.semi_rounded_red,
                    secondaryButtonText = "Batal",
                    secondaryButtonTextColor = R.color.red,
                    mainButtonTextColor = null,
                    secondaryButtonBackgroundDrawable = R.drawable.semi_rounded_outline_red,
                    title = "Hapus Perusahaan",
                    subtitle = "Apakah anda yakin ingin menghapus perusahaan ${perusahaan.nama}?",
                    image = null,
                    blockMainButton = {
                        perusahaanDetailViewModel.deletePerusahaan(perusahaan.id) {
                            finish()
                        }
                    },
                    blockSecondaryButton = {}).show(supportFragmentManager, "DeletePerusahaan")
            }
            btnUbahPerusahaan.setOnClickListener {
                openActivityWithExtras(UpdatePerusahaanActivity::class.java, false) {
                    putParcelable(INTENT_PARCEL, perusahaan)
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
        perusahaanDetailViewModel.getPerusahaanById(id)
    }
}