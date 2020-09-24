package com.sonlcr1.ex67googlemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {

    // 1.GoogleMap Library부터 추가 (play-services-maps)
    // 2.구글 지도 사용에 대한 API 키 발급(아무나 쓰지 못하게 제약)
    // 개발자 사이트에 코드가 나와있음

    // 구글지도를 제어하는 객체 참조변수
    GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if(permissionResult != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
            }
        }

        // SupportMapFragment 안에 있는 GoogleMap 객체를 얻어오기
        // 우선 xml에 만든 SupprotMapFragment를 참조하기
        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.frag_map);

        //비동기 방식(별도스레드)으로 지도를 불러오기
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //내 멤버변수에 얻어온 GoogleMap 대입
                gMap = googleMap;

                //원하는 좌표객체 생성
                LatLng seoul = new LatLng(37.56,126.97);

                //마커 옵션을 설정할수 있는 객체 생성
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(seoul);
                markerOptions.title("서울");
                markerOptions.snippet("대한민국의 수도");

                //지도에  마커 추가
                gMap.addMarker(markerOptions);

                //원하는 좌표 위치로 카메라 이동
                gMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

                //카메라 이동을 스무스하게 효과 주면서 zoom까지 적용
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(seoul,11));  //2파라미터 : 줌 강도 1~25까지 설정

                //마커 여러개 추가도 가능함
                LatLng mrhi = new LatLng(37.5608,127.0346);
                MarkerOptions markerOptions2 = new MarkerOptions();
                markerOptions2.position(mrhi);
                markerOptions2.title("미래능력개발교육원");
                markerOptions2.snippet("http://www.mrhi.or.kr");
//                markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.dollar));   // 마커를 이미지로 사용 가능
                markerOptions2.anchor(1f,1f); //그림 아이콘의 위치를 설정 가로,세로를 0~1까지 비율로 위치를 조정,아이콘을 마커 위치에 알맞게 보이도록 설정

                Marker marker = gMap.addMarker(markerOptions2); //추가된 마커객체를 리턴해줌
                //마커를 클릭하지 않아도 이미 InfoWindow가 보이도록...
                marker.showInfoWindow();    //showinfowindow : 정보창

                //지도의 정보창을 클릭했을때 반응하기
                gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String title = marker.getTitle();


                        switch (title){
                            case "서울":
                                break;
                            case "미래능력개발교육원":
                                //교육원 홈페이지로 이동(웹브라우저 실행)
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                Uri uri = Uri.parse("http://www.mrhi.or.kr");
                                intent.setData(uri);

                                startActivity(intent);

                                break;
                        }
                    }
                });

                //카메라 위치 변경
//                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mrhi,1));
//
                //정보창의 커스텀모양을 만들고 싶다면..
                //정보창을 만들어주는 Adapter객체 생성
                MyInfoWinAdapter adapter = new MyInfoWinAdapter(MainActivity.this);
                gMap.setInfoWindowAdapter(adapter);

                //줌컨트롤 (+,-) 보이도록 설정
                UiSettings settings = gMap.getUiSettings();
                settings.setZoomControlsEnabled(true);
//
//                //내 위치 보여주기 (위치정보 제공 퍼미션 작업 필요, 동적퍼미션), 근데 시간상 동적퍼미션 코드 안썼다.
//                //정적 퍼미션(매니페스트)에서 설정하고 내가 직접 권한 설정 하였다.
                gMap.setMyLocationEnabled(true);
            }
        });
    }
    

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "위치정보 사용가능", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "위치정보 제공에 동의 하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
