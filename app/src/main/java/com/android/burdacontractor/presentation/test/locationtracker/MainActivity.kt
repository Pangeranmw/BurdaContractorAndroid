//package ir.reza_mahmoudi.locationtracker.presentation
//
//import android.Manifest
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.content.ServiceConnection
//import android.os.Bundle
//import android.os.IBinder
//import androidx.activity.ComponentActivity
//import androidx.activity.viewModels
//import androidx.core.app.ActivityCompat
//import androidx.lifecycle.lifecycleScope
//import dagger.hilt.android.AndroidEntryPoint
//import ir.reza_mahmoudi.locationtracker.R
//import ir.reza_mahmoudi.locationtracker.domain.entity.LocationHistoryItem
//import ir.reza_mahmoudi.locationtracker.presentation.location_service.LocationService
//import ir.reza_mahmoudi.locationtracker.presentation.ui.theme.LocationTrackerTheme
//import ir.reza_mahmoudi.locationtracker.util.context.showToast
//import ir.reza_mahmoudi.locationtracker.util.date.getCurrentDate
//import kotlinx.coroutines.launch
//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//
//    private val mainViewModel: MainViewModel by viewModels()
//
//    private var mService: LocationService? = null
//
//    private var mBound: Boolean = false
//
//    private val connection = object : ServiceConnection {
//        override fun onServiceConnected(className: ComponentName, service: IBinder) {
//            val binder = service as LocationService.LocalBinder
//            mService = binder.getService()
//            mBound = true
//            collectData()
//        }
//        override fun onServiceDisconnected(arg0: ComponentName) {
//            mBound = false
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            LocationTrackerTheme {
//                val locationHistoryList = mainViewModel.locationHistoryList.collectAsState()
//                Column(
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    Button(
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = {
//                            startService()
//                        }) {
//                        Text(text = stringResource(id = R.string.start))
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Button(
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = {
//                            stopService()
//                        }) {
//                        Text(text = stringResource(id = R.string.stop))
//                    }
//                    LazyColumn(
//                        modifier = Modifier.fillMaxWidth(),
//                    ) {
//                        locationHistoryList.value.forEach{
//                            item {
//                                Text(text = "${it.date} : ${it.location.latitude} , ${it.location.longitude} \n")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        requestPermission()
//    }
//
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//            ),
//            0
//        )
//    }
//    private fun stopService() {
//        if (mBound) {
//            mBound = false
//            unbindService(connection)
//            Intent(applicationContext, LocationService::class.java).apply {
//                action = LocationService.ACTION_STOP
//                startService(this)
//            }
//            applicationContext.showToast(R.string.service_stopped_successfully)
//        } else {
//            applicationContext.showToast(R.string.service_not_started_yet)
//        }
//    }
//
//    private fun startService() {
//        if (!mBound){
//            Intent(applicationContext, LocationService::class.java).apply {
//                action = LocationService.ACTION_START
//                startService(this)
//            }
//            applicationContext.showToast(R.string.service_started_successfully)
//        } else {
//            applicationContext.showToast(R.string.service_already_started)
//        }
//        bindService()
//    }
//
//    private fun collectData() {
//        lifecycleScope.launch {
//            mService?.currentLocation?.collect {
//                mainViewModel.addLocationHistory(
//                    LocationHistoryItem(
//                        getCurrentDate(), it
//                    )
//                )
//            }
//        }
//    }
//
//    private fun bindService() {
//        Intent(this, LocationService::class.java).also { intent ->
//            bindService(intent, connection, Context.BIND_AUTO_CREATE)
//        }
//    }
//}