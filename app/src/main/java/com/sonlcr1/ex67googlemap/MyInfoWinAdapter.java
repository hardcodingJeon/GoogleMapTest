package com.sonlcr1.ex67googlemap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MyInfoWinAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;

    public MyInfoWinAdapter(Context context) {  //클래스에서 필요한건 생성자로 new할때 파라미터로 받아 온다
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.infowin,null);

        switch (marker.getTitle()){
            case "미래능력개발교육원":
                TextView tv = view.findViewById(R.id.tv);
                tv.setText("미래능력개발교육원");
                break;
            case "서울":
                TextView tv2 = view.findViewById(R.id.tv);
                tv2.setText("서울");
                break;
        }


        return view;
    }
}
