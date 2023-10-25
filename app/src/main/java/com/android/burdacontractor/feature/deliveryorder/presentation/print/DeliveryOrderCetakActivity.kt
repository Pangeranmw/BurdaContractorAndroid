package com.android.burdacontractor.feature.deliveryorder.presentation.print

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.print.PdfPrint
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.android.burdacontractor.BuildConfig
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.utils.*
import com.android.burdacontractor.databinding.ActivityDeliveryOrderCetakBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class DeliveryOrderCetakActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryOrderCetakBinding
    private val storageViewModel: StorageViewModel by viewModels()
    private val deliveryOrderCetakViewModel: DeliveryOrderCetakViewModel by viewModels()
    private var deliveryOrderId: String? = null
    private var deliveryOrderKode: String? = null
    private lateinit var fileName: String
    private lateinit var file: File
    private lateinit var downloadedFile: File
    private var snackbar: Snackbar? = null
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryOrderCetakBinding.inflate(layoutInflater)
        setContentView(binding.root)
        deliveryOrderId = intent.getStringExtra(Constant.INTENT_ID)
        deliveryOrderKode = intent.getStringExtra(Constant.INTENT_KODE)
        binding.progressBar.isVisible = true
        snackbar = Snackbar.make(binding.mainLayout,getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
        deliveryOrderCetakViewModel.liveNetworkChecker.observe(this){
            checkConnection(snackbar,it){ initUi() }
        }
        fileName = "Memo $deliveryOrderKode"
        file = getExternalFilesDir("DeliveryOrder") as File
        if(!file.exists()){
            file.mkdirs()
        }
        downloadedFile = File(file,"${fileName.replace("/","_")}.pdf")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doWebViewPrint() {
        with(binding){
            val headerMap = HashMap<String,String>()
            val url = "${BuildConfig.BASE_URL}/delivery-order/cetak/${deliveryOrderId}"
            headerMap["Authorization"] = storageViewModel.token
            webView.settings.loadWithOverviewMode = true
            webView.settings.domStorageEnabled = true
            webView.settings.builtInZoomControls = true
            webView.settings.displayZoomControls = false
            webView.settings.useWideViewPort = false
            webView.settings.allowFileAccess = true
            webView.setInitialScale(110)
            // Create a WebView object specifically for printing
            webView.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = true

                override fun onPageFinished(view: WebView, url: String) {
                    progressBar.isVisible = false
                    btnPrint.setVisible()
                    val exists = downloadedFile.exists()
                    btnDownload.isVisible = !exists
                    btnOpenFile.isVisible = exists
                }
            }
            webView.loadUrl(url,headerMap)
        }
    }

    private fun createWebPrintJob(webView: WebView, jobName:String) {
        // Get a PrintManager instance
        (getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->
            val printAttributes = PrintAttributes.Builder()
            printAttributes.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            printAttributes.setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)
            // Create a print job with name and adapter instance
            printManager.print(
                jobName,
                printAdapter,
                printAttributes.build()
            )
        }
    }
    private fun downloadPDF(webView: WebView, file: File, fileName:String) {
        val printAttributes = PrintAttributes.Builder()
        printAttributes.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
        printAttributes.setMinMargins(PrintAttributes.Margins.NO_MARGINS)
        printAttributes.setResolution(PrintAttributes.Resolution("pdf", "pdf", 600, 600))
        // Get a print adapter instance
        val printAdapter = webView.createPrintDocumentAdapter(fileName)
        val pdfPrint = PdfPrint(printAttributes.build())

        pdfPrint.savePDF(printAdapter, file, fileName)
        pdfPrint.isFinished.observe(this){
            binding.progressBar.isVisible = !it
            binding.btnOpenFile.isVisible = it
            binding.btnDownload.isVisible = !it
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUi(){
        doWebViewPrint()
        with(binding){
            btnOpenFolder.setOnClickListener{
                val uri = Uri.parse(file.path)
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(uri, "*/*")
                startActivity(intent)
            }
            btnOpenFile.setOnClickListener {
                if(downloadedFile.exists()){
                    val intent = Intent(Intent.ACTION_VIEW)
                    val uri = FileProvider.getUriForFile(
                        this@DeliveryOrderCetakActivity,
                        applicationContext.packageName + ".provider",
                        downloadedFile
                    )
                    intent.setDataAndType(
                        uri,
                        "application/pdf"
                    )
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    startActivity(intent)
                }
            }
            onBackPressedCallback()
            btnPrint.setOnClickListener{
                createWebPrintJob(webView,fileName)
            }
            btnDownload.setOnClickListener{
                downloadPDF(webView,file,"$fileName.pdf")
            }
        }
    }
    private fun onBackPressedCallback(){
        binding.btnBack.setOnClickListener {
            finish()
            overridePendingTransition(0,0)
        }
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                overridePendingTransition(0,0)
            }
        })
    }
}