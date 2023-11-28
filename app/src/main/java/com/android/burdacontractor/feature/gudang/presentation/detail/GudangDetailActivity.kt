package com.android.burdacontractor.feature.gudang.presentation.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.Constant.INTENT_PARCEL
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.presentation.adapter.ListDeliveryOrderAdapter
import com.android.burdacontractor.core.presentation.adapter.ListStatistikDoSjAdapter
import com.android.burdacontractor.core.presentation.adapter.ListSuratJalanAdapter
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.core.utils.getDateFromMillis
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityGudangDetailBinding
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.presentation.detail.DeliveryOrderDetailActivity
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.gudang.presentation.update.UpdateGudangActivity
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.presentation.detail.SuratJalanDetailActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GudangDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGudangDetailBinding
    private lateinit var id: String
    private val gudangDetailViewModel: GudangDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGudangDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra(INTENT_ID).toString()
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        gudangDetailViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        onBackPressedCallback()
        gudangDetailViewModel.id.observe(this) {
            if (it == null) gudangDetailViewModel.setId(id)
        }
        gudangDetailViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        gudangDetailViewModel.gudang.observe(this) { gudang ->
            gudangDetailViewModel.user.observe(this) { user ->
                gudangDetailViewModel.deliveryOrder.observe(this) {
                    setDeliveryOrderAdapter(user, it)
                }
                gudangDetailViewModel.statDeliveryOrder.observe(this) {
                    setStatAdapter(binding.rvStatDo, it)
                }
                gudangDetailViewModel.statSjPengirimanGp.observe(this) {
                    setStatAdapter(binding.rvStatSjgp, it)
                }
                gudangDetailViewModel.statSjPengirimanPp.observe(this) {
                    setStatAdapter(binding.rvStatSjpp, it)
                }
                gudangDetailViewModel.statSjPengembalian.observe(this) {
                    setStatAdapter(binding.rvStatSjpg, it)
                }
                gudangDetailViewModel.sjPengembalian.observe(this) {
                    setSuratJalanAdapter(user, it, SuratJalanTipe.PENGEMBALIAN)
                }
                gudangDetailViewModel.sjPengirimanGp.observe(this) {
                    setSuratJalanAdapter(user, it, SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
                }
                gudangDetailViewModel.sjPengirimanPp.observe(this) {
                    setSuratJalanAdapter(user, it, SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
                }
                initUi(gudang)
            }
        }
        gudangDetailViewModel.state.observe(this) {
            binding.srLayout.isRefreshing = it == StateResponse.LOADING
        }
    }

    private fun setDeliveryOrderAdapter(user: UserByTokenItem, listDo: List<AllDeliveryOrder>) {
        binding.apply {
            tvEmptyDeliveryOrderAktif.setGone()
            val adapter = ListDeliveryOrderAdapter(user.role, user.id) {
                openActivityWithExtras(DeliveryOrderDetailActivity::class.java, false) {
                    putString(INTENT_ID, it.id)
                }
            }
            adapter.submitList(listDo)
            rvDeliveryOrder.layoutManager = LinearLayoutManager(
                this@GudangDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvDeliveryOrder.adapter = adapter
            if (listDo.isEmpty()) {
                tvEmptyDeliveryOrderAktif.setVisible()
            }
        }
    }

    private fun setStatAdapter(rv: RecyclerView, listStat: List<StatisticCountTitleItem>) {
        val adapter = ListStatistikDoSjAdapter()
        adapter.submitList(listStat)
        rv.layoutManager =
            GridLayoutManager(this@GudangDetailActivity, 3, GridLayoutManager.VERTICAL, false)
        rv.adapter = adapter
    }

    private fun setSuratJalanAdapter(
        user: UserByTokenItem,
        listSj: List<AllSuratJalan>,
        tipe: SuratJalanTipe
    ) {
        binding.apply {
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
                        this@GudangDetailActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    rvSjPengembalian.adapter = adapter
                }

                SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> {
                    tvEmptySjPengirimanGudangProyekAktif.setGone()
                    rvSjPengirimanGudangProyek.layoutManager = LinearLayoutManager(
                        this@GudangDetailActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    rvSjPengirimanGudangProyek.adapter = adapter
                }

                SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> {
                    tvEmptySjPengirimanProyekProyekAktif.setGone()
                    rvSjPengirimanProyekProyek.layoutManager = LinearLayoutManager(
                        this@GudangDetailActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    rvSjPengirimanProyekProyek.adapter = adapter
                }
            }
            if (listSj.isEmpty()) {
                when (tipe) {
                    SuratJalanTipe.PENGEMBALIAN -> tvEmptySjPengembalianAktif.setVisible()
                    SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> tvEmptySjPengirimanGudangProyekAktif.setVisible()
                    SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> tvEmptySjPengirimanProyekProyekAktif.setVisible()
                }
            }
        }
    }

    private fun initUi(gudang: GudangById) {
        binding.apply {
            srLayout.setOnRefreshListener {
                refreshData()
            }
            ivGudang.setImageFromUrl(gudang.gambar, this@GudangDetailActivity)
            tvNama.text = gudang.nama
            tvKotaProvinsi.text =
                getString(R.string.kota_dan_provinsi, gudang.kota, gudang.provinsi)
            tvTanggalDibuat.text = getDateFromMillis(gudang.createdAt)
            tvTerakhirDiperbarui.text = getDateFromMillis(gudang.updatedAt)
            tvAlamat.text = gudang.alamat

            btnDelete.setOnClickListener {
                CustomDialog(
                    mainButtonText = "Hapus",
                    mainButtonBackgroundDrawable = R.drawable.semi_rounded_red,
                    secondaryButtonText = "Batal",
                    secondaryButtonTextColor = R.color.red,
                    mainButtonTextColor = null,
                    secondaryButtonBackgroundDrawable = R.drawable.semi_rounded_outline_red,
                    title = "Hapus Gudang",
                    subtitle = "Apakah anda yakin ingin menghapus gudang ${gudang.nama}?",
                    image = null,
                    blockMainButton = {
                        gudangDetailViewModel.deleteGudang(gudang.id) {
                            finish()
                        }
                    },
                    blockSecondaryButton = {}).show(supportFragmentManager, "DeleteGudang")
            }
            btnUbahGudang.setOnClickListener {
                openActivityWithExtras(UpdateGudangActivity::class.java, false) {
                    putParcelable(INTENT_PARCEL, gudang)
                }
            }
        }
    }

    fun onBackPressedCallback() {
        binding.btnBack.setOnClickListener { finishAction() }
        customBackPressed()
    }

    private fun refreshData() {
        gudangDetailViewModel.getGudangById(id)
    }
}