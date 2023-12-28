package com.android.burdacontractor.core.utils

import com.android.burdacontractor.core.domain.model.TempatSimple
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanSimple
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.model.User
import com.android.burdacontractor.feature.profile.domain.model.UserSimple
import com.android.burdacontractor.feature.proyek.domain.model.AllLogistic
import com.android.burdacontractor.feature.suratjalan.domain.model.AddUpdatePeminjamanPenggunaanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanSuratJalan

object DataMapper {

    fun kendaraanSimpleToAllKendaraan(kendaraan: KendaraanSimple): AllKendaraan {
        return AllKendaraan(
            merk = kendaraan.merk,
            totalData = 0,
            createdAt = 0,
            gambar = kendaraan.gambar,
            namaLogistic = null,
            gudangId = "",
            updatedAt = 0,
            logisticId = "",
            jenis = kendaraan.jenis,
            id = kendaraan.id,
            platNomor = kendaraan.platNomor,
            namaGudang = "",
            status = ""
        )
    }

    fun mapPeminjamanSuratJalanToAddUpdatePeminjamanPenggunaanSuratJalan(peminjaman: List<PeminjamanSuratJalan>): List<AddUpdatePeminjamanPenggunaanSuratJalan> {
        return peminjaman.map {
            AddUpdatePeminjamanPenggunaanSuratJalan(
                sjChildId = it.sjChildId,
                id = it.id,
                menanganiId = it.menanganiUser.menanganiId,
                menanganiAsalId = it.menanganiAsalUser?.menanganiId,
                userId = it.menanganiUser.userId
            )
        }
    }

    fun mapPenggunaanSuratJalanToAddUpdatePeminjamanPenggunaanSuratJalan(penggunaan: List<PenggunaanSuratJalan>): List<AddUpdatePeminjamanPenggunaanSuratJalan> {
        return penggunaan.map {
            AddUpdatePeminjamanPenggunaanSuratJalan(
                sjChildId = it.sjChildId,
                id = it.id,
                menanganiId = it.menanganiUser.menanganiId,
                menanganiAsalId = it.menanganiAsalUser?.menanganiId,
                userId = it.menanganiUser.userId
            )
        }
    }

    fun userToUserByToken(user: User): UserByTokenItem = UserByTokenItem(
        role = user.role,
        nama = user.nama,
        foto = user.foto,
        noHp = user.noHp,
        updatedAt = user.updatedAt,
        ttd = user.ttd,
        deviceToken = null,
        createdAt = user.createdAt,
        id = user.id,
        email = user.email
    )

    fun gudangByIdToAllGudang(gudangById: GudangById): AllGudang = AllGudang(
        provinsi = gudangById.provinsi,
        kota = gudangById.kota,
        jarak = null,
        latitude = gudangById.latitude,
        totalData = 0,
        createdAt = 0,
        gambar = gudangById.gambar,
        alamat = gudangById.alamat,
        nama = gudangById.nama,
        updatedAt = 0,
        id = gudangById.id,
        durasi = null,
        longitude = gudangById.longitude
    )

    fun tempatSimpleToAllGudang(tempatSimple: TempatSimple): AllGudang = AllGudang(
        provinsi = "",
        kota = "",
        jarak = null,
        latitude = 0.0,
        totalData = 0,
        createdAt = 0,
        gambar = tempatSimple.gambar,
        alamat = tempatSimple.alamat,
        nama = tempatSimple.nama,
        updatedAt = 0,
        id = tempatSimple.id,
        durasi = null,
        longitude = 0.0
    )

    fun tempatSimpleToAllPerusahaan(tempatSimple: TempatSimple): AllPerusahaan = AllPerusahaan(
        provinsi = "",
        kota = "",
        latitude = 0.0,
        jarak = null,
        durasi = null,
        totalData = 0,
        createdAt = 0,
        gambar = tempatSimple.gambar,
        alamat = tempatSimple.alamat,
        nama = tempatSimple.nama,
        updatedAt = 0,
        doDalamPerjalanan = 0,
        id = tempatSimple.id,
        longitude = 0.0,
        doMenungguKonfirmasi = 0
    )

    fun combineLogisticWithKendaraanSimpleToAllLogistic(
        userSimple: UserSimple,
        kendaraan: KendaraanSimple
    ): AllLogistic =
        AllLogistic(
            kendaraan = Kendaraan(
                merk = kendaraan.merk,
                gudangId = "",
                updatedAt = 0,
                logisticId = null,
                jenis = kendaraan.jenis,
                createdAt = 0,
                id = kendaraan.id,
                platNomor = kendaraan.platNomor,
                gambar = kendaraan.gambar,
                status = ""
            ),
            role = userSimple.role,
            noHp = userSimple.noHp,
            ttd = "",
            jarak = null,
            doActive = 0,
            totalData = 0,
            createdAt = 0,
            nama = userSimple.nama,
            foto = userSimple.foto,
            updatedAt = 0,
            sjgpActive = 0,
            sjppActive = 0,
            sjpgActive = 0,
            id = userSimple.id,
            durasi = null,
            email = ""
        )

}