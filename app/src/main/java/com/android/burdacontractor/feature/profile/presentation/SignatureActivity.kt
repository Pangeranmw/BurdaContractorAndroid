package com.android.burdacontractor.feature.profile.presentation

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.utils.bitmapToFile
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivitySignatureBinding
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@AndroidEntryPoint
class SignatureActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignatureBinding
    private var isTtdSubmit = false
    private val signatureViewModel: SignatureViewModel by viewModels()
    private val storageViewModel: StorageViewModel by viewModels()
    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignatureBinding.inflate(layoutInflater)
        snackbar= Snackbar.make(findViewById(android.R.id.content), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
        signatureViewModel.liveNetworkChecker.observe(this){
            this.checkConnection(snackbar,it){ initObserver() }
        }
        setContentView(binding.root)
    }
    private fun initObserver() {
        signatureViewModel.state.observe(this){
            when(it){
                StateResponse.LOADING -> binding.progressBar.setVisible()
                StateResponse.ERROR -> binding.progressBar.setGone()
                StateResponse.SUCCESS -> {
                    binding.progressBar.setGone()
                    if(isTtdSubmit) {
                        signatureViewModel.getUserByToken()
                        isTtdSubmit=false
                    }
                }
                else -> {}
            }
        }
        signatureViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        signatureViewModel.user.observe(this){user->
            initProfile(user)
        }
        initView()
        setClickListener()
    }
    private fun initProfile(user: UserByTokenItem){
        if(user.ttd !=null){
            storageViewModel.updateTtd(user.ttd)
            binding.tvSignIsEmpty.isVisible = false
            binding.ivSignature.setImageFromUrl(user.ttd, this)
        }else binding.tvSignIsEmpty.isVisible = true
    }
    private fun initView() {

        binding.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() { }

            override fun onSigned() { }

            override fun onClear() { }
        })
    }

    private fun setClickListener() {
        binding.btnComplete.setOnClickListener {
            val file = bitmapToFile(binding.signaturePad.transparentSignatureBitmap,Bitmap.CompressFormat.PNG, 100,this)
            //Set the captured signature in Imageview
            val requestImageFile = file.asRequestBody("image/png".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "ttd",
                file.name,
                requestImageFile
            )
            signatureViewModel.uploadTtd(imageMultipart)
            isTtdSubmit = true
        }
        binding.btnClear.setOnClickListener {
            //Clear captured signature
            binding.signaturePad.clear()
        }
    }
}