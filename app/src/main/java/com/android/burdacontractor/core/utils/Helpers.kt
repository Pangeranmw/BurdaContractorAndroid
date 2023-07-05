package com.android.burdacontractor.core.utils

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.burdacontractor.BuildConfig
import com.android.burdacontractor.R
import com.android.burdacontractor.core.data.source.remote.response.Routes
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.presentation.customview.CustomTextInputLayout
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Extension For Distance Matrix Response
fun List<Routes>.getDuration(): String{
    val duration = this[0].duration
    return when(duration.toInt()){
        in 0..60 -> String.format("%.2f Dtk", duration)
        in 60..3600 -> String.format("%.2f Mnt", duration/60)
        else -> String.format("%.2f Jam", duration/3600)
    }
}
fun List<Routes>.getDistance(): String {
    val distance = this[0].distance
    return when (distance.toInt()) {
        in 0..1000 -> String.format("%.2f Mtr", distance)
        else -> String.format("%.2f Km", distance / 1000)
    }
}
fun String.getLatitude(): Double{
    return this.split("|",this)[0].toDouble()
}
fun String.getLongitude(): Double{
    return this.split("|",this)[1].toDouble()
}
fun LogisticCoordinate.combine(): String{
    return "${this.latitude}|${this.longitude}"
}

fun getCoordinate(originCoordinate: String, destinationCoordinate: String): String{
    val originLat = originCoordinate.split("|")[0]
    val originLon = originCoordinate.split("|")[1]
    val destinationLat = destinationCoordinate.split("|")[0]
    val destinationLon = destinationCoordinate.split("|")[1]
    return "$originLon,$originLat;$destinationLon,$destinationLat"
}

fun getPhotoUrl(photoUrl: String): String{
    return if(!photoUrl.contains("http", true)){
        "${BuildConfig.BASE_URL}/storage/$photoUrl"
    }else{
        photoUrl
    }
}

fun enumValueToNormal(enumValue: String): String{
    val result = enumValue.replace("_", " ").lowercase(Locale.ROOT)
    return result.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

// Lambda with receiver (extras: Bundle.() -> Unit = {})
fun <T> Context.openActivityWithExtras(it: Class<T>, extras: Bundle.() -> Unit = {}, activity: Activity) {
    val intent = Intent(this, it)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
    activity.overridePendingTransition(0, 0)
}
fun <T> Context.openActivity(it: Class<T>, activity: Activity , isFinished: Boolean = true) {
    val intent = Intent(this, it)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    activity.startActivity(intent)
    if (isFinished) activity.finish()
    activity.overridePendingTransition(0, 0)
}
fun setParcelable(fragment: Fragment, parcelable: Bundle.() -> Unit = {}) {
    val bundle = Bundle()
    bundle.apply(parcelable)
    fragment.arguments = bundle
}
fun Context.checkConnection(snackbar: Snackbar?, state: Boolean, nextListener: () -> Unit){
    if(!state){
        snackbar?.setBackgroundTint(ContextCompat.getColor(this,R.color.error_color))
        snackbar?.show()
    }else{
        snackbar?.setBackgroundTint(ContextCompat.getColor(this,R.color.green_light_full))
        snackbar?.dismiss()
        nextListener()
    }
}

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun getAddress(location: LatLng, context: Context): String {
    val lat = location.latitude
    val lng = location.longitude
    val geocoder = Geocoder(context, Locale("id", "ID"))
    var result = context.getString(R.string.unknown_address)
    geocoder.getFromLocation(lat,lng,1,
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        object : Geocoder.GeocodeListener{
        override fun onGeocode(addresses: MutableList<Address>) {
            result = addresses[0].getAddressLine(0)
        }
        override fun onError(errorMessage: String?) {
            super.onError(errorMessage)
            result = errorMessage.toString()
        }
    })
    return result
}
//fun getAddress(latitude: Double, longitude: Double, context: Context): String? {
//    val geocoder = Geocoder(context, Locale("id", "ID"))
//    val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1) as List<Address>
//    if (addresses.isNotEmpty()) {
//        return addresses[0].getAddressLine(0)
//    }
//    return null
//}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun getCity(latitude: Double, longitude: Double, context: Context): String {
    var result = context.getString(R.string.unknown_address)
    val geocoder = Geocoder(context, Locale("id", "ID"))
    geocoder.getFromLocation(latitude,longitude,1,
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        object : Geocoder.GeocodeListener{
            override fun onGeocode(addresses: MutableList<Address>) {
                result = addresses[0].subAdminArea
            }
            override fun onError(errorMessage: String?) {
                super.onError(errorMessage)
                result = errorMessage.toString()
            }
        })
    return result
}
fun Context.checkPassword(text: String, customTextInputLayout: CustomTextInputLayout){
    when{
        text.isEmpty() -> this.emptyData(text,customTextInputLayout)
        text.length in 1..5 -> customTextInputLayout.error = this.getString(R.string.password_less_6)
        else -> hideTextInputError(customTextInputLayout)
    }
}
fun Context.checkEmail(text: String, customTextInputLayout: CustomTextInputLayout){
    when{
        text.isEmpty() -> this.emptyData(text,customTextInputLayout)
        !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches() -> customTextInputLayout.error = this.getString(R.string.email_incorrect)
        else -> hideTextInputError(customTextInputLayout)
    }
}
fun Context.checkNumber(text: String, customTextInputLayout: CustomTextInputLayout){
    when{
        text.isEmpty() -> this.emptyData(text,customTextInputLayout)
        text.length in 1..9 -> customTextInputLayout.error = this.getString(R.string.number_less_10)
        else -> hideTextInputError(customTextInputLayout)
    }
}
fun Context.emptyData(text: String, customTextInputLayout: CustomTextInputLayout){
    when{
        text.isEmpty() -> customTextInputLayout.error = this.getString(R.string.empty_form)
        else -> hideTextInputError(customTextInputLayout)
    }
}
fun hideTextInputError(customTextInputLayout: CustomTextInputLayout){
    customTextInputLayout.error = null
    customTextInputLayout.isErrorEnabled = false
}

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)

    var compressQuality = 100
    var streamLength: Int

    do {
        streamLength = getImageSize(bitmap,compressQuality)
        compressQuality -= 5
    } while (streamLength > 1000000)

    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

    return file
}
fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (isBackCamera) {
        matrix.postRotate(90f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        matrix.postRotate(-90f)
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
fun getImageSize(bitmap: Bitmap, compressQuality: Int): Int{
    val bmpStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
    val bmpPicByteArray = bmpStream.toByteArray()
    return bmpPicByteArray.size
}

fun resizeImage(bitmap: Bitmap, maxHeight: Int, maxWidth: Int): Bitmap {
    val height = bitmap.height
    val width = bitmap.width

    val ratio: Float = width.toFloat() / height.toFloat()
    val maxRatio: Float = maxWidth.toFloat() / maxWidth.toFloat()

    maxWidth.apply {
        if (maxRatio > ratio) (maxHeight.toFloat() * ratio).toInt()
        else (maxWidth.toFloat() / ratio).toInt()
    }

    return Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, true)
}

fun flipImage(bitmap: Bitmap): Bitmap {
    val matrix = Matrix()
    matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f) // flip gambar
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
fun cropImage(image: Bitmap): Bitmap {
    return when{
        image.width>image.height -> resizeImage(image, 768, 1024)
        image.width<image.height -> resizeImage(image, 1024, 1024)
        else -> resizeImage(image, 1024, 768)
    }
}

fun Context.getTimeDifference(millis: Long): String{
    val currentLocale = this.resources.configuration.locales.get(0)

    val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", currentLocale)

//    val curMil = currentDate.calendar.timeInMillis
    val curMil = System.currentTimeMillis()
    val diff = if(millis>curMil) millis - curMil else curMil - millis

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val week = days / 7
    val month = week / 4
    val year = month / 12
    var result = when{
        year>0 -> "${year.toInt()} tahun yang"
        month>0 -> "${month.toInt()} bulan yang"
        week>0 -> "${week.toInt()} minggu yang"
        days>0 -> "${days.toInt()} hari yang"
        hours>0 -> "${hours.toInt()} jam yang"
        minutes>0 -> "${minutes.toInt()} menit yang"
        seconds>0 -> "${seconds.toInt()} detik yang"
        else -> "$seconds detik yang"
    }
    result += if(millis>curMil) " akan datang" else " lalu"
    return result
}

fun getDateFromMillis(millis: Long, dateFormat: String? = null): String{
    val format = dateFormat ?: "yyyy-MM-dd HH:mm:ss"
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    val calendar = Calendar.getInstance()

    calendar.timeInMillis = millis
    return formatter.format(calendar.time)
}
fun String.getDateFromTimeStamp(): String = substring(0, 10)

fun String.getTimeFromTimeStamp(): String = substring(11, 16)
fun String.getTimeWithSecondFromTimeStamp(): String = substring(11, 19)

fun String.getFirstName(): String = if (contains(" ")) {
    split(" ")[0].replaceFirstChar { it.uppercase() }
} else replaceFirstChar { it.uppercase() }

fun String.withDateFormat(): String {
    return this.toDate("yyyy-MM-dd").formatTo("EEEE, dd MMM yyyy")
}
fun String.withTimeFormat(): String {
    return this.toDate("k:mm").formatTo("kk:mm")
}
fun String.toDate(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): Date {
    val parser = SimpleDateFormat(dateFormat, Locale("id", "ID"))
    parser.timeZone = timeZone
    return parser.parse(this) as Date
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale("id", "ID"))
    formatter.timeZone = timeZone
    return formatter.format(this)
}
fun setToastLong(message: String, context: Context){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
fun setToastShort(message: String, context: Context){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}