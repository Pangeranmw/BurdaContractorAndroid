package android.print

import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PrintDocumentAdapter.LayoutResultCallback
import android.print.PrintDocumentAdapter.WriteResultCallback
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import java.io.File


class PdfPrint(private val printAttributes: PrintAttributes) {
    private val _isFinished = MutableLiveData(false)
    val isFinished : LiveData<Boolean> = _isFinished
    fun savePDF(printAdapter: PrintDocumentAdapter, path: File, fileName: String) {
        _isFinished.value = false
        printAdapter.onLayout(null, printAttributes, null, object : LayoutResultCallback() {
            override fun onLayoutFinished(info: PrintDocumentInfo, changed: Boolean) {
                printAdapter.onWrite(
                    arrayOf(PageRange.ALL_PAGES),
                    getOutputFile(path, fileName),
                    CancellationSignal(),
                    object : WriteResultCallback() {
                        override fun onWriteFinished(pages: Array<PageRange>) {
                            super.onWriteFinished(pages)
                            _isFinished.value = true
                        }
                    })
            }
        }, null)
    }

    private fun getOutputFile(path: File, fileName: String): ParcelFileDescriptor? {
        if (!path.exists()) {
            path.mkdirs()
        }
        val file = File(path, fileName.replace("/","_"))
        if (file.exists()) file.delete()
        try {
            file.createNewFile()
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open ParcelFileDescriptor", e)
        }
        return null
    }

    companion object {
        private val TAG = PdfPrint::class.java.simpleName
    }
}