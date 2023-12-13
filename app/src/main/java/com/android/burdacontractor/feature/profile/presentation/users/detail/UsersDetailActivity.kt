package com.android.burdacontractor.feature.profile.presentation.users.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.Constant.INTENT_PARCEL
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListDeliveryOrderAdapter
import com.android.burdacontractor.core.presentation.adapter.ListStatistikDoSjAdapter
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.finishAction
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
class UsersDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerusahaanDetailBinding
    private lateinit var id: String
    private val usersDetailViewModel: UsersDetailViewModel by viewModels()
    private val storageViewModel: StorageViewModel by viewModels()
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
        usersDetailViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        onBackPressedCallback()
        usersDetailViewModel.id.observe(this) {
            if (it == null) usersDetailViewModel.setId(id)
        }
        usersDetailViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        usersDetailViewModel.perusahaan.observe(this) { perusahaan ->
            usersDetailViewModel.user.observe(this) { user ->
                usersDetailViewModel.deliveryOrder.observe(this) {
                    setDeliveryOrderAdapter(user, it)
                }
                usersDetailViewModel.statDeliveryOrder.observe(this) {
                    setStatAdapter(binding.rvStatDo, it)
                }
                initUi(perusahaan)
            }
        }
        usersDetailViewModel.state.observe(this) {
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
                this@UsersDetailActivity,
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
            GridLayoutManager(this@UsersDetailActivity, 3, GridLayoutManager.VERTICAL, false)
        rv.adapter = adapter
    }

    private fun initUi(perusahaan: PerusahaanById) {
        binding.apply {
            srLayout.setOnRefreshListener {
                refreshData()
            }
            ivPerusahaan.setImageFromUrl(perusahaan.gambar, this@UsersDetailActivity)
            tvNama.text = perusahaan.nama
            tvKotaProvinsi.text =
                getString(R.string.kota_dan_provinsi, perusahaan.kota, perusahaan.provinsi)
            tvTanggalDibuat.text = getDateFromMillis(perusahaan.createdAt)
            tvTerakhirDiperbarui.text = getDateFromMillis(perusahaan.updatedAt)
            tvAlamat.text = perusahaan.alamat
            layoutButton.isVisible =
                storageViewModel.role == UserRole.ADMIN.name || storageViewModel.role == UserRole.ADMIN_GUDANG.name || storageViewModel.role == UserRole.PURCHASING.name
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
                        usersDetailViewModel.deletePerusahaan(perusahaan.id) {
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
        binding.btnBack.setOnClickListener { finishAction() }
        customBackPressed()
    }

    private fun refreshData() {
        usersDetailViewModel.getPerusahaanById(id)
    }
}