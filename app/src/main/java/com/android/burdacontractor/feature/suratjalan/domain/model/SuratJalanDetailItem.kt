package com.android.burdacontractor.feature.suratjalan.domain.model

import android.os.Parcelable
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanSimple
import com.android.burdacontractor.feature.profile.domain.model.UserSimple
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuratJalanDetailItem(

    @field:SerializedName("kendaraan")
    val kendaraan: KendaraanSimple,

    @field:SerializedName("admin_gudang")
    val adminGudang: UserSimple,

    @field:SerializedName("penanggung_jawab")
    val penanggungJawab: UserSimple? = null,

    @field:SerializedName("penanggung_jawab_peminjam")
    val penanggungJawabPeminjam: UserSimple? = null,

    @field:SerializedName("tempat_asal")
    val tempatAsal: List<TempatSuratJalan>,

    @field:SerializedName("ttd_admin")
    val ttdAdmin: TtdVerification? = null,

    @field:SerializedName("ttd_driver")
    val ttdDriver: TtdVerification? = null,

    @field:SerializedName("created_at")
    val createdAt: Long,

    @field:SerializedName("foto_bukti")
    val fotoBukti: String? = null,

    @field:SerializedName("logistic")
    val logistic: UserSimple,

    @field:SerializedName("untuk_saya")
    val untukSaya: Boolean,

    @field:SerializedName("menangani_proyek_tujuan")
    val menanganiProyekTujuan: Boolean,

    @field:SerializedName("updated_at")
    val updatedAt: Long,

    @field:SerializedName("tempat_tujuan")
    val tempatTujuan: TempatSuratJalan,

    @field:SerializedName("kode_surat")
    val kodeSurat: String,

    @field:SerializedName("ttd_penanggung_jawab")
    val ttdPenanggungJawab: TtdVerification? = null,

    @field:SerializedName("ttd_penanggung_jawab_peminjam")
    val ttdPenanggungJawabPeminjam: TtdVerification? = null,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("site_manager")
    val siteManager: UserSimple,

    @field:SerializedName("tipe")
    val tipe: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("peminjaman")
    val peminjaman: List<PeminjamanSuratJalan>,

    @field:SerializedName("penggunaan")
    val penggunaan: List<PenggunaanSuratJalan>,
) : Parcelable