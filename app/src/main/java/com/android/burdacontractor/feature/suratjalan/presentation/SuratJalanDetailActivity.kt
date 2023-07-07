package com.android.burdacontractor.feature.suratjalan.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.databinding.ActivitySuratJalanDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuratJalanDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuratJalanDetailBinding
    private lateinit var layout: View
    private val storageViewModel: StorageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuratJalanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layout = binding.mainLayout
        when(storageViewModel.role){
//            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name ->
//                setBottomNavigationMenu(R.menu.bottom_menu_admingudang, R.id.surat_jalan_admin_gudang)
//            UserRole.LOGISTIC.name ->
//                setBottomNavigationMenu(R.menu.bottom_menu_logistic, R.id.surat_jalan_logistic)
//            UserRole.PROJECT_MANAGER.name, UserRole.SUPERVISOR.name ->
//                setBottomNavigationMenu(R.menu.bottom_menu_sv_pm, R.id.surat_jalan_sv_pm)
        }
    }
    companion object{
        val ID_SURAT_JALAN = "id_surat_jalan"
    }
}