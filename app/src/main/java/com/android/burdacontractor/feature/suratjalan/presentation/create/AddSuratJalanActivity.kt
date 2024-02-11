package com.android.burdacontractor.feature.suratjalan.presentation.create

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListPeminjamanSuratJalanAdapter
import com.android.burdacontractor.core.presentation.adapter.ListPenggunaanSuratJalanAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.openActivityWithExtras
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
import com.android.burdacontractor.feature.suratjalan.presentation.BarangPeminjamanSuratJalanFragment
import com.android.burdacontractor.feature.suratjalan.presentation.BarangPenggunaanSuratJalanFragment
import com.android.burdacontractor.feature.suratjalan.presentation.detail.SuratJalanDetailActivity
import com.google.android.material.snackbar.Snackbar
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddSuratJalanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSuratJalanBinding
    private val addSuratJalanViewModel: AddSuratJalanViewModel by viewModels()
    private val pilihPenggunaanSuratJalanViewModel: PilihPenggunaanSuratJalanViewModel by viewModels()
    private val pilihPeminjamanSuratJalanViewModel: PilihPeminjamanSuratJalanViewModel by viewModels()
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
        addSuratJalanViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        addSuratJalanViewModel.state.observe(this) {
            binding.progressBar.isVisible = it == StateResponse.LOADING
        }
        addSuratJalanViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        binding.btnSubmit.setOnClickListener {
            addSuratJalanViewModel.addSuratJalan(
                pilihLogisticViewModel.logistic.value!!.id,
                pilihKendaraanViewModel.kendaraan.value!!.id,
                pilihPeminjamanSuratJalanViewModel.selectedListPeminjaman.value!!,
                pilihPenggunaanSuratJalanViewModel.selectedListPenggunaan.value!!,
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

        addSuratJalanViewModel.listProyek.observe(this) { listProyek ->
            val list = listProyek.map { it.namaProyek }
            binding.spinnerProyek.setItems(list)
            addSuratJalanViewModel.proyekIndex.value.let {
                addSuratJalanViewModel.setProyekIndex(it)
                if (it != null) binding.spinnerProyek.selectItemByIndex(it)
            }
        }
        binding.spinnerProyek.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            addSuratJalanViewModel.setProyekIndex(newIndex)
            pilihPenggunaanSuratJalanViewModel.resetPenggunaan()
            pilihPeminjamanSuratJalanViewModel.resetPeminjaman()
            isInputCorrect()
        })

        addSuratJalanViewModel.listTipe.observe(this) { listTipe ->
            binding.spinnerTipe.setItems(listTipe)
        }
        binding.spinnerTipe.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            if (newItem == "Pengembalian") {
                binding.headerPeminjaman.text = getString(R.string.pengembalian_peminjaman)
                binding.headerPenggunaan.text = getString(R.string.pengembalian_penggunaan)
            } else {
                binding.headerPeminjaman.text = getString(R.string.peminjaman)
                binding.headerPenggunaan.text = getString(R.string.penggunaan)
            }
            addSuratJalanViewModel.setTipe(newItem)
            binding.spinnerProyek.setItems(emptyList<String>())
            addSuratJalanViewModel.getAvailableProyekBySuratJalanType()
            pilihPenggunaanSuratJalanViewModel.resetPenggunaan()
            pilihPeminjamanSuratJalanViewModel.resetPeminjaman()
            isInputCorrect()
        })

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
            binding.rvPeminjaman.adapter = peminjamanAdapter
            peminjamanAdapter.submitList(it)
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
            binding.rvPenggunaan.adapter = penggunaanAdapter
            penggunaanAdapter.submitList(it)
        }
        addSuratJalanViewModel.tipe.observe(this) { tipe ->
            addSuratJalanViewModel.proyekIndex.observe(this) { proyekIndex ->
                val listProyek = addSuratJalanViewModel.listProyek.value!!
                binding.cvLogistic.setOnClickListener {
                    if (tipe == null || proyekIndex == null) {
                        setToastShort("Harap Isi Data Secara Berurutan")
                    } else {
                        val pilihLogisticFragment = PilihLogisticFragment.newInstance()
                        pilihLogisticFragment.show(supportFragmentManager)
                    }
                }
                binding.cvPeminjaman.setOnClickListener {
                    if (tipe == null || proyekIndex == null) {
                        setToastShort("Harap Isi Data Secara Berurutan")
                    } else {
                        val pilihPeminjamanSuratJalanFragment =
                            PilihPeminjamanSuratJalanFragment.newInstance(
                                tipe,
                                listProyek[proyekIndex].id
                            )
                        pilihPeminjamanSuratJalanFragment.show(supportFragmentManager)
                    }
                }
                binding.cvPenggunaan.setOnClickListener {
                    if (tipe == null || proyekIndex == null) {
                        setToastShort("Harap Isi Data Secara Berurutan")
                    } else {
                        val pilihPenggunaanSuratJalanFragment =
                            PilihPenggunaanSuratJalanFragment.newInstance(
                                tipe,
                                listProyek[proyekIndex].id
                            )
                        pilihPenggunaanSuratJalanFragment.show(supportFragmentManager)
                    }
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
                        if (tipe == null || proyekIndex == null || logistic == null) {
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
        }
    }

    private fun isInputCorrect() {
        val peminjaman = pilihPeminjamanSuratJalanViewModel.selectedListPeminjaman.value!!
        val penggunaan = pilihPenggunaanSuratJalanViewModel.selectedListPenggunaan.value!!
        val tipe = addSuratJalanViewModel.tipe.value
        val proyek = addSuratJalanViewModel.proyekIndex.value
        val logistic = pilihLogisticViewModel.logistic.value
        val kendaraan = pilihKendaraanViewModel.kendaraan.value
        binding.btnSubmit.isEnabled =
            (peminjaman.isNotEmpty() || penggunaan.isNotEmpty())
                    && logistic != null && kendaraan != null && proyek != null && tipe != null
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
                    this@AddSuratJalanActivity,
                    R.color.secondary_main
                )
            )
        } else {
            this.text = getString(R.string.belum_dipilih)
            this.setTextColor(ContextCompat.getColor(this@AddSuratJalanActivity, R.color.gray))
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