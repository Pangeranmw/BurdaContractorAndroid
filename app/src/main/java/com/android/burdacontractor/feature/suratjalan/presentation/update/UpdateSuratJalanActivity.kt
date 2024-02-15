package com.android.burdacontractor.feature.suratjalan.presentation.update

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.Constant.INTENT_PARCEL
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListPeminjamanSuratJalanAdapter
import com.android.burdacontractor.core.presentation.adapter.ListPenggunaanSuratJalanAdapter
import com.android.burdacontractor.core.utils.DataMapper
import com.android.burdacontractor.core.utils.DataMapper.toProyek
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.parcelable
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setToastShort
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityAddSuratJalanBinding
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.kendaraan.presentation.PilihKendaraanFragment
import com.android.burdacontractor.feature.kendaraan.presentation.PilihKendaraanViewModel
import com.android.burdacontractor.feature.logistic.presentation.PilihLogisticFragment
import com.android.burdacontractor.feature.logistic.presentation.PilihLogisticViewModel
import com.android.burdacontractor.feature.profile.presentation.SignatureActivity
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetailItem
import com.android.burdacontractor.feature.suratjalan.presentation.BarangPeminjamanSuratJalanFragment
import com.android.burdacontractor.feature.suratjalan.presentation.BarangPenggunaanSuratJalanFragment
import com.android.burdacontractor.feature.suratjalan.presentation.create.PilihPeminjamanSuratJalanFragment
import com.android.burdacontractor.feature.suratjalan.presentation.create.PilihPeminjamanSuratJalanViewModel
import com.android.burdacontractor.feature.suratjalan.presentation.create.PilihPenggunaanSuratJalanFragment
import com.android.burdacontractor.feature.suratjalan.presentation.create.PilihPenggunaanSuratJalanViewModel
import com.android.burdacontractor.feature.suratjalan.presentation.detail.SuratJalanDetailActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UpdateSuratJalanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSuratJalanBinding
    private val updateSuratJalanViewModel: UpdateSuratJalanViewModel by viewModels()
    private val pilihPenggunaanSuratJalanViewModel: PilihPenggunaanSuratJalanViewModel by viewModels()
    private val pilihPeminjamanSuratJalanViewModel: PilihPeminjamanSuratJalanViewModel by viewModels()
    private var suratJalan: SuratJalanDetailItem? = null
    private val storageViewModel: StorageViewModel by viewModels()
    private val pilihKendaraanViewModel: PilihKendaraanViewModel by viewModels()
    private val pilihLogisticViewModel: PilihLogisticViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSuratJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        suratJalan = intent.parcelable(INTENT_PARCEL)
        updateSuratJalanViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        updateSuratJalanViewModel.state.observe(this) {
            binding.progressBar.isVisible = it == StateResponse.LOADING
        }
        updateSuratJalanViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        pilihLogisticViewModel.setProyek(suratJalan!!.tempatTujuan.toProyek())
        binding.btnSubmit.setOnClickListener {
            updateSuratJalanViewModel.updateSuratJalan(
                suratJalan!!.id,
                pilihLogisticViewModel.logistic.value!!.id,
                pilihKendaraanViewModel.kendaraan.value!!.id,
                pilihPeminjamanSuratJalanViewModel.selectedListPeminjaman.value!!,
                pilihPenggunaanSuratJalanViewModel.selectedListPenggunaan.value!!,
                suratJalan!!.tempatTujuan.id,
            ) {
                openActivityWithExtras(SuratJalanDetailActivity::class.java) {
                    putString(INTENT_ID, it)
                }
            }
        }
        onBackPressedCallback()

        binding.btnUbahTtd.setOnClickListener {
            openActivity(SignatureActivity::class.java, false)
        }
        binding.ivTtd.setImageFromUrl(storageViewModel.ttd, this)

        pilihLogisticViewModel.setLogistic(
            DataMapper.combineLogisticWithKendaraanSimpleToAllLogistic(
                suratJalan!!.logistic,
                suratJalan!!.kendaraan
            )
        )

        binding.tvAppBar.text = getString(R.string.ubah_surat_jalan)
        binding.btnSubmit.text = getString(R.string.ubah_surat_jalan)

        binding.spinnerProyek.text = suratJalan!!.tempatTujuan.nama
        updateSuratJalanViewModel.setTipe(suratJalan!!.tipe)
        binding.spinnerTipe.text = enumValueToNormal(suratJalan!!.tipe)

        if (suratJalan!!.tipe == "PENGEMBALIAN") {
            binding.headerPeminjaman.text = getString(R.string.pengembalian_peminjaman)
            binding.headerPenggunaan.text = getString(R.string.pengembalian_penggunaan)
        } else {
            binding.headerPeminjaman.text = getString(R.string.peminjaman)
            binding.headerPenggunaan.text = getString(R.string.penggunaan)
        }
        pilihPeminjamanSuratJalanViewModel.selectedListPeminjaman.observe(this) {
            isInputCorrect()
            binding.tvPeminjamanBelumDipilih.setTextBelumDipilih(it.size)
            binding.rvPeminjaman.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val peminjamanAdapter = ListPeminjamanSuratJalanAdapter(
                barangListener = { barang ->
                    val fragment = BarangPeminjamanSuratJalanFragment.newInstance(barang)
                    fragment.show(supportFragmentManager, "BarangPeminjaman")
                },
                userId = storageViewModel.userId
            )
            val listAdapter = mutableListOf<PeminjamanSuratJalan>()
            listAdapter.addAll(it)
            listAdapter.addAll(suratJalan!!.peminjaman)
            binding.rvPeminjaman.adapter = peminjamanAdapter
            peminjamanAdapter.submitList(listAdapter)
        }
        pilihPenggunaanSuratJalanViewModel.selectedListPenggunaan.observe(this) {
            isInputCorrect()
            binding.tvPenggunaanBelumDipilih.setTextBelumDipilih(it.size)
            binding.rvPenggunaan.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val penggunaanAdapter = ListPenggunaanSuratJalanAdapter(
                barangListener = { barang ->
                    val fragment = BarangPenggunaanSuratJalanFragment.newInstance(barang)
                    fragment.show(supportFragmentManager, "BarangPenggunaan")
                },
                userId = storageViewModel.userId
            )
            val listAdapter = mutableListOf<PenggunaanSuratJalan>()
            listAdapter.addAll(it)
            listAdapter.addAll(suratJalan!!.penggunaan)
            binding.rvPenggunaan.adapter = penggunaanAdapter
            penggunaanAdapter.submitList(listAdapter)
        }
        binding.cvLogistic.setOnClickListener {
            val pilihLogisticFragment = PilihLogisticFragment.newInstance()
            pilihLogisticFragment.show(supportFragmentManager)
        }
        val tipe = suratJalan!!.tipe
        val proyekId = suratJalan!!.tempatTujuan.id
        binding.cvPeminjaman.setOnClickListener {
            val pilihPeminjamanSuratJalanFragment =
                PilihPeminjamanSuratJalanFragment.newInstance(tipe, proyekId)
            pilihPeminjamanSuratJalanFragment.show(supportFragmentManager)
        }
        binding.cvPenggunaan.setOnClickListener {
            val pilihPenggunaanSuratJalanFragment =
                PilihPenggunaanSuratJalanFragment.newInstance(tipe, proyekId)
            pilihPenggunaanSuratJalanFragment.show(supportFragmentManager)
        }
        pilihLogisticViewModel.logistic.observe(this) { logistic ->
            isInputCorrect()
            logistic?.let { lgtc ->
                binding.tvLogisticBelumDipilih.setGone()
                binding.layoutLogistic.setVisible()
                binding.ivLogistic.setImageFromUrl(lgtc.foto, this)
                binding.tvNamaLogistic.text = lgtc.nama
            }
            if (logistic?.kendaraan == null) {
                pilihKendaraanViewModel.setKendaraan(null)
            } else {
                val vehicle = AllKendaraan(
                    merk = logistic.kendaraan.merk,
                    totalData = 1,
                    createdAt = logistic.kendaraan.createdAt,
                    gambar = logistic.kendaraan.gambar,
                    namaLogistic = logistic.nama,
                    gudangId = logistic.kendaraan.gudangId,
                    updatedAt = logistic.kendaraan.updatedAt,
                    logisticId = logistic.kendaraan.logisticId,
                    jenis = logistic.kendaraan.jenis,
                    id = logistic.kendaraan.id,
                    platNomor = logistic.kendaraan.platNomor,
                    namaGudang = "",
                    status = logistic.kendaraan.status
                )
                pilihKendaraanViewModel.setKendaraan(vehicle)
            }
            binding.cvKendaraan.setOnClickListener {
                if (logistic == null) {
                    setToastShort("Harap Isi Data Secara Berurutan")
                } else {
                    if (logistic.kendaraan == null) {
                        val pilihKendaraanFragment = PilihKendaraanFragment.newInstance()
                        pilihKendaraanFragment.show(supportFragmentManager)
                    }
                }
            }
            pilihKendaraanViewModel.kendaraan.observe(this) { kendaraan ->
                isInputCorrect()
                kendaraan?.let { kdrn ->
                    showKendaraan(kdrn)
                } ?: hideKendaraan()
            }
        }
    }

    private fun isInputCorrect() {
        val logistic = pilihLogisticViewModel.logistic.value
        val kendaraan = pilihKendaraanViewModel.kendaraan.value
        binding.btnSubmit.isEnabled =
            logistic != null && kendaraan != null
    }

    private fun showKendaraan(kendaraan: AllKendaraan) {
        binding.tvKendaraanBelumDipilih.setGone()
        binding.layoutKendaraan.setVisible()
        binding.ivKendaraan.setImageFromUrl(kendaraan.gambar, this)
        binding.tvNamaKendaraan.text = kendaraan.merk
        binding.tvPlatKendaraan.text = kendaraan.platNomor
    }

    private fun TextView.setTextBelumDipilih(size: Int) {
        if (size > 0) {
            this.text = getString(R.string.jumlah_dipilih, size.toString())
            this.setTextColor(
                ContextCompat.getColor(
                    this@UpdateSuratJalanActivity,
                    R.color.secondary_main
                )
            )
        } else {
            this.text = getString(R.string.belum_dipilih)
            this.setTextColor(ContextCompat.getColor(this@UpdateSuratJalanActivity, R.color.gray))
        }
    }

    private fun hideKendaraan() {
        binding.tvKendaraanBelumDipilih.setVisible()
        binding.layoutKendaraan.setGone()
    }

    fun onBackPressedCallback() {
        binding.btnBack.setOnClickListener { finishAction() }
        customBackPressed()
    }
}