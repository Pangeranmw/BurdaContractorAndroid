package com.android.burdacontractor.feature.deliveryorder.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PdfPrint
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.android.burdacontractor.BuildConfig
import com.android.burdacontractor.R
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
    private val deliveryOrderDetailViewModel: DeliveryOrderDetailViewModel by viewModels()
    private var deliveryOrderId: String? = null
    private var deliveryOrderKode: String? = null
    private lateinit var fileName: String
    private lateinit var path: File
    private lateinit var downloadedFile: File
    private var snackbar: Snackbar? = null
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryOrderCetakBinding.inflate(layoutInflater)
        setContentView(binding.root)
        deliveryOrderId = intent.getStringExtra(ID_DELIVERY_ORDER)
        deliveryOrderKode = intent.getStringExtra(KODE_DELIVERY_ORDER)
        binding.progressBar.isVisible = true
        snackbar = Snackbar.make(binding.mainLayout,getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
        deliveryOrderDetailViewModel.liveNetworkChecker.observe(this){
            checkConnection(snackbar,it){ initUi() }
        }
        fileName = "Memo $deliveryOrderKode"
        path = getExternalFilesDir("DeliveryOrder") as File
        if(!path.exists()){
            path.mkdirs()
        }
        downloadedFile = File(path,"${fileName.replace("/","_")}.pdf")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doWebViewPrint() {
        with(binding){
            // Create a WebView object specifically for printing
            val newWebView = WebView(this@DeliveryOrderCetakActivity)
            newWebView.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false

                override fun onPageFinished(view: WebView, url: String) {
                    progressBar.isVisible = false
                    btnPrint.setVisible()
                    val exists = downloadedFile.exists()
                    btnDownload.isVisible = !exists
                    btnOpenFile.isVisible = exists
                }
            }
            val headerMap = HashMap<String,String>()
            val url = "${BuildConfig.BASE_URL}/delivery-order/cetak/${deliveryOrderId}"
            headerMap["Authorization"] = storageViewModel.token
            webView.webViewClient = newWebView.webViewClient
            webView.settings.loadWithOverviewMode = true
            webView.settings.builtInZoomControls = true
            webView.settings.displayZoomControls = false
            webView.settings.useWideViewPort = false
            webView.settings.allowFileAccess = true
            webView.setInitialScale(110)
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
    private fun downloadPDF(webView: WebView, path: File, fileName:String) {
        val printAttributes = PrintAttributes.Builder()
        printAttributes.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
        printAttributes.setMinMargins(PrintAttributes.Margins.NO_MARGINS)
        printAttributes.setResolution(PrintAttributes.Resolution("pdf", "pdf", 600, 600))
        // Get a print adapter instance
        val printAdapter = webView.createPrintDocumentAdapter(fileName)
        val pdfPrint = PdfPrint(printAttributes.build())

        pdfPrint.savePDF(printAdapter, path, fileName)
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
                val uri = Uri.parse(path.path)
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
            btnBack.setOnClickListener {
                finish()
                overridePendingTransition(0,0)
            }
            btnPrint.setOnClickListener{
                createWebPrintJob(webView,fileName)
            }
            btnDownload.setOnClickListener{
                downloadPDF(webView,path,"$fileName.pdf")
            }
        }
    }
    companion object{
        const val ID_DELIVERY_ORDER = "deliveryOrderId"
        const val KODE_DELIVERY_ORDER = "kodeDeliveryOrder"
    }
}