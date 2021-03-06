package com.blooburn.owere.user.activity.signUpActivity

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blooburn.owere.R
import com.blooburn.owere.util.DESIGNER_DATA_KEY
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.io.IOException
import java.util.*

//내 위치 좌표를 불러오고 설정함.
class SetPositionActivity : AppCompatActivity(), MapView.CurrentLocationEventListener {

    //주소를 받아오기 위한 지오코더 객체
    //주소 -> 좌표, 좌표 -> 주소( 역지오코딩)
    private val geoCoder = Geocoder(this, Locale.KOREA)
    private var addresses = mutableListOf<Address>()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //내위치를 보여줄 지도 (카카오 맵 api사용)
    private lateinit var mapView: MapView

    private val myposition_keepButton: TextView by lazy {
        findViewById(R.id.myposition_searchButton)
    }

    var REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)

    //    mapPointGeo?.latitude, mapPointGeo?.longitude
    var latitude: Double = 0.0   //위도
    var longitude: Double = 0.0  //경도


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_position)


        //맵뷰 호출
        initMapView()


        initPositionSetButton() //내 위치로 설정


    }

    override fun onDestroy() {
        super.onDestroy()
        //추적 모드 중지
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView.setShowCurrentLocationMarker(false)
        //MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading

    }


    private fun initPositionSetButton() {
        val userId = auth.currentUser?.uid.orEmpty()
        myposition_keepButton.setOnClickListener {
            var userLatitude = latitude
            var userLongitue = longitude
            var area = ""
//                geoCoder.getFromLocation(userLatitude, userLongitue, 1).first()
//                    .getAddressLine(0)

            try {
                addresses = geoCoder.getFromLocation(latitude, longitude, 1);


                var stringBuilder = StringBuilder()

                if (addresses.size > 0) {
                    var returnAddress = addresses[0];

                    var localityString = returnAddress.locality;
                    var name = returnAddress.featureName;
                    var subLocality = returnAddress.subLocality;
                    var country = returnAddress.countryName;
                    var region_code = returnAddress.countryCode;
                    var zipcode = returnAddress.postalCode;
                    var state = returnAddress.adminArea;

                    //구단위
                    area = returnAddress.subLocality

                } else {

                }
            } catch (e: IOException) {
                e.printStackTrace();
            }


//            if(userOrDesigner == "User"){
//                val currentUserDB1 = Firebase.database.reference.child("Users").child(userId).child("위도")
//                val currentUserDB2 = Firebase.database.reference.child("Users").child(userId).child("경도")
//
//                currentUserDB1.setValue(userLatitude)
//                currentUserDB2.setValue(userLongitue)
//
//            }else if(userOrDesigner == "Designer"){
//                val currentUserDB1 = Firebase.database.reference.child("LocationDesigner").child(userId)
////
//
//                var designerUpdate = mutableMapOf<String,Any>()
//                designerUpdate["latitude"] = userLatitude
//                designerUpdate["longitude"] = userLongitue
//                currentUserDB1.updateChildren(designerUpdate)
//                databaseInstance.reference.child("Designers").child(userId).child("area").setValue(area)
//
//
////                currentUserDB1.setValue(userLatitude)
////                currentUserDB2.setValue(userLongitue)
//
//            }

            // 내 위치 정하기 프래그먼트로 콜백전송 -> 주소를 정했어! 하는 신호
            intent.putExtra("latitude", userLatitude)
            intent.putExtra("longitude", userLongitue)
            intent.putExtra("area", area)
            setResult(100, intent)
            //액티비티 종료
            finish()


        }
    }

    //맵뷰 설정
    fun initMapView() {
        //Activity 의 content-view 에 삽입하면 지도화면을 손쉽게 구현
        val mapViewContainer: ViewGroup = findViewById(R.id.map_view)

        //맵 사용
        mapView = MapView(this) //net.daum.mf.map.api.MapView 객체를 생성
        mapView.setCurrentLocationEventListener(this)
        mapView.setDaumMapApiKey("API_KEY");

        mapViewContainer.addView(mapView)

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()

        } else {
            checkRunTimePermission()
        }

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true)
        mapView.setZoomLevel(2, true)    //작을수록 가까움
        mapView.zoomIn(true)
        mapView.zoomOut(true)

    }


    private fun checkRunTimePermission() {
        //런타입 퍼미션 처리
        //1. 위치 퍼미션을 가지고 있는지 체크
        var hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            //이미 퍼미션을 가지고 있다면
            // 안드로이드 6.0이하 버전은 퍼미션 체크 안 해도 통과

            //위치값을 가져올 수 있음
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading


        } else {  //퍼미션 요청 허용한 적 없는 경우 -> 요청 필요

            //사용자가 퍼미션 거부를 한 적이 있는 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {

                //요청을 다시하기 전에 퍼미션이 필요한 이유를 설명
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                //사용자에게 퍼미션 요청-> 요청 결과는 onRequestPermissionResult에서 수신
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    PERMISSION_REQUEST_CODE
                )

            } else {
                //사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                //사용자에게 퍼미션 요청-> 요청 결과는 onRequestPermissionResult에서 수신
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    PERMISSION_REQUEST_CODE
                )
            }

        }
    }

    //GPS 활성화를 위한 메소드
    private fun showDialogForLocationServiceSetting() {

        var intent =
            Intent(this, android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS::class.java)

        var builder = AlertDialog.Builder(this)
        builder.setTitle("위치서비스 비활성화")
            .setMessage(
                "앱을 사용하기 위해서는 위치 서비스가 필요합니다. \n" +
                        "위치 설정을 수정하실래요?"
            )
            .setCancelable(true)
            .setPositiveButton("설정", DialogInterface.OnClickListener { dialog, which ->
                startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE)

            })
            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()

            })
            .create()
            .show()


    }

    //인텐트 반환
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS활성화 했는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }

        }
    }


    private fun checkLocationServicesStatus(): Boolean {
        var locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER))
    }


    //    setCurrentLocationEventListener(this)에 필요한 구현메소드
    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        var mapPointGeo: MapPoint.GeoCoordinate? = p1?.mapPointGeoCoord
        Log.d(
            LOG_TAG, String.format(
                "MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                mapPointGeo?.latitude,
                mapPointGeo?.longitude,
                p2
            )
        )

        latitude = mapPointGeo!!.latitude //위도 값 업데이트
        longitude = mapPointGeo!!.longitude  //경도 값 업데이트
    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {

    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {

    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {

    }


    //    사용자에게 퍼미션 요청-> 요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //요청코드가 PERMISSION_REQUEST_CODE이고 요청한 퍼미션 개수 만큼 수신되었다면
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {
            var check_result = true

            //모든 퍼미션 허용 체크
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }

            if (check_result) {
                Log.d("@@@", "start")

                //위치값을 가져올수 있음
                mapView.currentLocationTrackingMode
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    )
                ) {
                    Toast.makeText(
                        this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }


    }

    companion object {
        val PERMISSION_REQUEST_CODE = 100
        val GPS_ENABLE_REQUEST_CODE = 2001

        val LOG_TAG = "MainActivity"
    }
}

//디버깅 해시키 받을 때 사용
//        fun getAppKeyHash() {
//            try {
//                val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//                for(i in info.signatures) {
//                    val md: MessageDigest = MessageDigest.getInstance("SHA")
//                    md.update(i.toByteArray())
//
//                    val something = String(Base64.encode(md.digest(), 0)!!)
//                    Log.e("Debug key", something)
//                }
//            } catch(e: Exception) {
//                Log.e("Not found", e.toString())
//            }
//        }
//
//        getAppKeyHash()
