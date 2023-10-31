package com.android.burdacontractor.core.utils

import com.android.burdacontractor.core.domain.model.TempatSimple
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanSimple
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import com.android.burdacontractor.feature.profile.domain.model.UserSimple

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