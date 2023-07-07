package com.android.burdacontractor.core.utils

import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.domain.model.CountActive
import com.android.burdacontractor.core.domain.model.PeminjamanPengembalianBarangHabisPakai
import com.android.burdacontractor.core.domain.model.PeminjamanPengembalianBarangTidakHabisPakai
import com.android.burdacontractor.core.domain.model.User
import com.android.burdacontractor.feature.auth.data.source.remote.response.LoginItem
import com.android.burdacontractor.feature.auth.domain.model.UserLogin
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DataAllDeliveryOrderWithCountItem
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderDetailItem
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderItem
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.PreOrderItem
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.domain.model.DataAllDeliveryOrderWithCount
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetail
import com.android.burdacontractor.feature.deliveryorder.domain.model.PreOrder
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.KendaraanByLogisticItem
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.BarangHabisPakaiItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.BarangTidakHabisPakaiItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.DataAllSuratJalanWithCountItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.DataAllSuratJalanWithCount
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetail

object DataMapper {
    fun userByTokenResponsesToDomain(input: UserByTokenItem): User {
        return User(
            id = input.id!!,
            nama = input.nama!!,
            email = input.email!!,
            foto = input.foto,
            ttd = input.ttd,
            noHp = input.noHp,
            role = input.role!!,
            deviceToken = input.deviceToken,
            createdAt = input.createdAt!!,
            updatedAt = input.updatedAt!!
        )
    }
    fun countActiveResponsesToDomain(input: CountActiveResponse): CountActive {
        return CountActive(
            totalActive = input.totalActive,
        )
    }
    fun kendaraanByLogisticResponsesToDomain(input: KendaraanByLogisticItem): KendaraanByLogistic {
        return KendaraanByLogistic(
            idGudang = input.idGudang,
            merk = input.merk,
            alamatGudang = input.alamatGudang,
            logisticId = input.logisticId,
            jenis = input.jenis,
            gambarGudang = input.gambarGudang,
            id = input.id,
            platNomor = input.platNomor,
            namaGudang = input.namaGudang,
            gambar = input.gambar,
            coordinateGudang = input.coordinateGudang
        )
    }
    fun mapAllSuratJalanResponsesToDomain(input: List<SuratJalanItem>): List<AllSuratJalan> {
        val allSuratJalanList = ArrayList<AllSuratJalan>()
        input.map {
            val allSuratJalan = AllSuratJalan(
                id = it.id,
                kodeSurat = it.kodeSurat,
                status = it.status,
                tipe = it.tipe,
                namaTempatAsal = it.namaTempatAsal,
                alamatTempatAsal = it.alamatTempatAsal,
                coordinateTempatAsal = it.coordinateTempatAsal,
                namaTempatTujuan = it.namaTempatTujuan,
                alamatTempatTujuan = it.alamatTempatTujuan,
                namaAdminGudang = it.namaAdminGudang,
                fotoDriver = it.fotoDriver,
                coordinateTempatTujuan = it.coordinateTempatTujuan,
                updatedAt = it.updatedAt,
                fotoProjectManager = it.fotoProjectManager,
                namaDriver = it.namaDriver,
                fotoAdminGudang = it.fotoAdminGudang,
                namaProjectManager = it.namaProjectManager,
                namaSupervisor = it.namaSupervisor,
                fotoSupervisor = it.fotoSupervisor,
                namaSupervisorPeminjam = it.namaSupervisorPeminjam,
                fotoSupervisorPeminjam = it.fotoSupervisorPeminjam,
            )
            allSuratJalanList.add(allSuratJalan)
        }
        return allSuratJalanList
    }
    fun mapAllDeliveryOrderResponsesToDomain(input: List<DeliveryOrderItem>): List<AllDeliveryOrder> {
        val allDeliveryOrderList = ArrayList<AllDeliveryOrder>()
        input.map {
            val allDeliveryOrder = AllDeliveryOrder(
                id = it.id,
                kodeDo = it.kodeDo,
                status = it.status,
                namaTempatAsal = it.namaTempatAsal,
                alamatTempatAsal = it.alamatTempatAsal,
                coordinateTempatAsal = it.coordinateTempatAsal,
                namaTempatTujuan = it.namaTempatTujuan,
                alamatTempatTujuan = it.alamatTempatTujuan,
                namaAdminGudang = it.namaAdminGudang,
                fotoDriver = it.fotoDriver,
                coordinateTempatTujuan = it.coordinateTempatTujuan,
                updatedAt = it.updatedAt,
                namaDriver = it.namaDriver,
                namaPurchasing = it.namaPurchasing,
                fotoPurchasing = it.fotoPurchasing,
                fotoAdminGudang = it.fotoAdminGudang,
            )
            allDeliveryOrderList.add(allDeliveryOrder)
        }
        return allDeliveryOrderList
    }
    fun dataAllSuratJalanWithCountResponsesToDomain(input: DataAllSuratJalanWithCountItem): DataAllSuratJalanWithCount {
        val allSuratJalanList = mapAllSuratJalanResponsesToDomain(input.suratJalan!!)
        val count = input.count
        return DataAllSuratJalanWithCount(
            count = count,
            suratJalan = allSuratJalanList
        )
    }
    fun dataAllDeliveryOrderWithCountResponsesToDomain(input: DataAllDeliveryOrderWithCountItem): DataAllDeliveryOrderWithCount {
        val allDeliveryOrderList = mapAllDeliveryOrderResponsesToDomain(input.suratJalan!!)
        val count = input.count
        return DataAllDeliveryOrderWithCount(
            count = count,
            deliveryOrder = allDeliveryOrderList
        )
    }
    fun mapSjListBarangHabisPakaiToDomain(input: List<BarangHabisPakaiItem>): List<PeminjamanPengembalianBarangHabisPakai>{
        return input.map {
            PeminjamanPengembalianBarangHabisPakai(
                id = it.id, merk = it.merk,
                jumlahSatuan = it.jumlahSatuan,
                nama = it.nama, ukuran = it.ukuran,
                gambar = it.gambar
            )
        }
    }
    fun mapSjListBarangTidakHabisPakaiResponsesToDomain(input: List<BarangTidakHabisPakaiItem>): List<PeminjamanPengembalianBarangTidakHabisPakai>{
        return input.map {
            PeminjamanPengembalianBarangTidakHabisPakai(
                id = it.id, merk = it.merk,
                jumlahSatuan = it.jumlahSatuan,
                nama = it.nama,
                gambar = it.gambar
            )
        }
    }
    fun mapSjListPreOrderToDomain(input: List<PreOrderItem>): List<PreOrder>{
        return input.map {
            PreOrder(
                id = it.id,
                kodePo = it.kodePo,
                namaMaterial = it.namaMaterial,
                satuan = it.satuan,
                keterangan = it.keterangan,
                ukuran = it.ukuran,
                jumlah = it.jumlah
            )
        }
    }
    fun loginResponsesToDomain(input: LoginItem): UserLogin {
        return UserLogin(
            id = input.id,
            role = input.role,
            nama = input.nama,
            foto = input.foto,
            noHp = input.noHp,
            ttd = input.ttd,
            email = input.email,
            token = input.token,
            createdAt = input.createdAt,
            updatedAt = input.updatedAt,
        )
    }
    fun suratJalanDetailResponsesToDomain(input: SuratJalanDetailItem): SuratJalanDetail {
        return SuratJalanDetail(
            id = input.id,
            kendaraanMerk = input.kendaraan.merk,
            kendaraanJenis = input.kendaraan.jenis,
            kendaraanPlatNomor = input.kendaraan.platNomor,
            kendaraanGambar = input.kendaraan.gambar,
            adminGudangNama = input.adminGudang?.nama,
            adminGudangFoto = input.adminGudang?.foto,
            adminGudangNoHp = input.adminGudang?.noHp,
            projectManagerNama = input.projectManager?.nama,
            projectManagerFoto = input.projectManager?.foto,
            projectManagerNoHp = input.projectManager?.noHp,
            supervisorNama = input.supervisor?.nama,
            supervisorFoto = input.supervisor?.foto,
            supervisorNoHp = input.supervisor?.noHp,
            logisticId = input.logistic?.id,
            logisticNama = input.logistic?.nama,
            logisticFoto = input.logistic?.foto,
            logisticNoHp = input.logistic?.noHp,
            tempatAsalNama = input.tempatAsal?.nama,
            tempatAsalCoordinate = input.tempatAsal?.coordinate,
            tempatAsalFoto = input.tempatAsal?.foto,
            tempatAsalAlamat = input.tempatAsal?.alamat,
            tempatTujuanNama = input.tempatAsal?.nama,
            tempatTujuanCoordinate = input.tempatAsal?.coordinate,
            tempatTujuanFoto = input.tempatAsal?.foto,
            tempatTujuanAlamat = input.tempatAsal?.alamat,
            ttdSupervisor = input.ttdSupervisor,
            ttdSupervisorPeminjam = input.ttdSupervisorPeminjam,
            ttdAdmin = input.ttdAdmin,
            ttdDriver = input.ttdDriver,
            createdAt = input.createdAt,
            updatedAt = input.updatedAt,
            fotoBukti = input.fotoBukti,
            barangHabisPakai = mapSjListBarangHabisPakaiToDomain(input.barangHabisPakai!!),
            barangTidakHabisPakai = mapSjListBarangTidakHabisPakaiResponsesToDomain(input.barangTidakHabisPakai!!),
        )
    }
    fun deliveryOrderDetailResponsesToDomain(input: DeliveryOrderDetailItem): DeliveryOrderDetail {
        return DeliveryOrderDetail(
            id = input.id,
            kendaraanMerk = input.kendaraan.merk,
            kendaraanJenis = input.kendaraan.jenis,
            kendaraanPlatNomor = input.kendaraan.platNomor,
            kendaraanGambar = input.kendaraan.gambar,
            adminGudangNama = input.adminGudang?.nama,
            adminGudangFoto = input.adminGudang?.foto,
            adminGudangNoHp = input.adminGudang?.noHp,
            logisticId = input.logistic?.id,
            logisticNama = input.logistic?.nama,
            logisticFoto = input.logistic?.foto,
            logisticNoHp = input.logistic?.noHp,
            tempatAsalNama = input.tempatAsal?.nama,
            tempatAsalCoordinate = input.tempatAsal?.coordinate,
            tempatAsalFoto = input.tempatAsal?.foto,
            tempatAsalAlamat = input.tempatAsal?.alamat,
            tempatTujuanNama = input.tempatAsal?.nama,
            tempatTujuanCoordinate = input.tempatAsal?.coordinate,
            tempatTujuanFoto = input.tempatAsal?.foto,
            tempatTujuanAlamat = input.tempatAsal?.alamat,
            ttd = input.ttd,
            perihal = input.perihal,
            untukPerhatian = input.untukPerhatian,
            createdAt = input.createdAt,
            updatedAt = input.updatedAt,
            tglPengambilan = input.tglPengambilan,
            fotoBukti = input.fotoBukti,
            preOrder = mapSjListPreOrderToDomain(input.preOrder!!),
        )
    }
}