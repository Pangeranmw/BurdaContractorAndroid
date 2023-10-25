package com.android.burdacontractor.core.utils

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.android.burdacontractor.BuildConfig
import com.android.burdacontractor.R
import com.android.burdacontractor.core.data.source.remote.response.Routes
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.presentation.customview.CustomTextInputLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.imageview.ShapeableImageView
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
import java.util.UUID


// Extension For Distance Matrix Response
fun List<Routes>.getDuration(): String{
    val duration = this[0].duration
    return when(duration.toInt()){
        in 0..60 -> String.format("%.2f Dtk", duration)
        in 60..3600 -> String.format("%.2f Mnt", duration / 60)
        else -> String.format("%.2f Jam", duration / 3600)
    }
}

fun List<Routes>.getDistance(): String {
    val distance = this[0].distance
    return when (distance.toInt()) {
        in 0..1000 -> String.format("%.2f Mtr", distance)
        else -> String.format("%.2f Km", distance / 1000)
    }
}

fun Double.convertDuration(): String {
    val durationConvert = when (this.toInt()) {
        in 0..60 -> String.format("%.2f Dtk", this)
        in 60..3600 -> String.format("%.2f Mnt", this / 60)
        else -> String.format("%.2f Jam", this / 3600)
    }
    return durationConvert
}

fun Double.convertDistance(): String {
    val distanceConvert = when (this.toInt()) {
        in 0..1000 -> String.format("%.2f Mtr", this)
        else -> String.format("%.2f Km", this / 1000)
    }
    return distanceConvert
}

fun ShapeableImageView.setImageFromUrl(
    url: String?, context: Context, useLoad: Boolean = true
) {
    if (url != null && useLoad) {
        Glide.with(context)
            .load(getPhotoUrl(url))
            .apply(requestOptionWithLoading(context))
            .signature(ObjectKey(System.currentTimeMillis().toString()))
            .into(this)
    } else if (url != null && !useLoad) {
        Glide.with(context)
            .load(getPhotoUrl(url))
            .signature(ObjectKey(System.currentTimeMillis().toString()))
            .into(this)
    }
}
fun ImageView.setImageFromUrl(
    url: String?, context: Context, useLoad: Boolean = true
) {
    if (url != null && useLoad) {
        Glide.with(context)
            .load(getPhotoUrl(url))
            .apply(requestOptionWithLoading(context))
            .signature(ObjectKey(System.currentTimeMillis().toString()))
            .into(this)
    } else if (url != null && !useLoad) {
        Glide.with(context)
            .load(getPhotoUrl(url))
            .signature(ObjectKey(System.currentTimeMillis().toString()))
            .into(this)
    }
}
fun requestOptionWithLoading(context: Context): RequestOptions{
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    return RequestOptions()
        .placeholder(circularProgressDrawable).fitCenter()
//        .override(Target.SIZE_ORIGINAL)
}
fun View.openWhatsAppChat(toNumber: String) {
    val newNumber = convertNumberToIndonesia(toNumber)
    val url = "https://api.whatsapp.com/send?phone=$newNumber"
    try {
        context.packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
        context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) })
    } catch (e: PackageManager.NameNotFoundException) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))}}

fun convertNumberToIndonesia(toNumber: String): String{
    var newNumber = toNumber.replace(" ", "").replace("(","").replace(")","").replace("+","")
    if(newNumber.first() == '0') newNumber = newNumber.replaceFirst("0","+62")
    else if(newNumber[0] == '6' && newNumber[1] == '2') newNumber = newNumber.replaceFirst("6","+6") //
    else if(newNumber[0] != '6' && newNumber[1] != '2') newNumber = "+62${newNumber}" //81283749378 -> +6281283749378
    return newNumber
}
fun Activity.dialIntent(number: String){
    val newNumber = convertNumberToIndonesia(number)
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$newNumber")
    }
    this.startActivity(intent)
}
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

fun String.getLatitude(): Double {
    return this.split("|")[0].toDouble()
}

fun String.getLongitude(): Double {
    return this.split("|")[1].toDouble()
}

fun LogisticCoordinate.combine(): String {
    return "${this.latitude}|${this.longitude}"
}

fun getDistanceMatrixCoordinate(latitude: String, longitude: String): String {
    return "$longitude,$latitude"
}

fun getCoordinate(originCoordinate: String, destinationCoordinate: String): String {
    val originLat = originCoordinate.split("|")[0]
    val originLon = originCoordinate.split("|")[1]
    val destinationLat = destinationCoordinate.split("|")[0]
    val destinationLon = destinationCoordinate.split("|")[1]
    return "$originLon,$originLat;$destinationLon,$destinationLat"
}

fun toCoordinateFormat(latitude: Double, longitude: Double): String {
    return "$latitude|$longitude"
}

fun String.toIntegerNumber(): Int {
    var titleToInteger = 0
    this.forEach {
        val charDigit = it.toInt()
        titleToInteger+=charDigit
    }
    return titleToInteger
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
fun <T> Activity.openActivityWithExtrasTes(it: Class<T>, isFinished: Boolean = true, clearAllTask: Boolean = false, extras: Intent.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.putExtras(Intent().apply(extras))
    if(clearAllTask) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
    if (isFinished) this.finish()
    this.overridePendingTransition(0, 0)
}
fun <T> Activity.openActivityWithExtras(it: Class<T>, isFinished: Boolean = true, clearAllTask: Boolean = false, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this.applicationContext, it)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.putExtras(Bundle().apply(extras))
    if(clearAllTask) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    if (isFinished) finish()
    overridePendingTransition(0, 0)
}
fun <T> Activity.openActivity(it: Class<T>, isFinished: Boolean = true, clearAllTask: Boolean = false) {
    val intent = Intent(this, it)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    if(clearAllTask) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
    if (isFinished) this.finish()
    this.overridePendingTransition(0, 0)
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
fun bitmapToFile(imageBitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int,context: Context): File{
    val wrapper = ContextWrapper(context)
    var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
    file = File(file,"${UUID.randomUUID()}.png")
    val stream: OutputStream = FileOutputStream(file)
    imageBitmap.compress(format,quality,stream)
    stream.flush()
    stream.close()
    return file
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
private const val MAXIMAL_SIZE = 1000000 //1 MB

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        // content://media/external/images/media/1000000062
        // storage/emulated/0/Pictures/MyCamera/20230825_155303.jpg
    }
    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.provider",
        imageFile
    )
}

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
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

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}
fun Bitmap.getRotatedBitmap(file: File): Bitmap? {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}
fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
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
    val format = dateFormat ?: "dd MMMM yyyy, HH:mm"
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
fun String.withDateFormat(currentFormat: String = "yyyy/MM/dd", toFormat: String = "dd MMM yyyy"): String {
    return this.toDate(currentFormat).formatTo(toFormat)
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
fun Context.setToastLong(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
fun Context.setToastShort(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}