package com.example.user.test_ots;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoFragment extends Fragment {
    private final static String process_info = "Process";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(process_info, "Info 프래그먼트의 뷰를 생성했습니다.");
        return inflater.inflate(R.layout.fragment_info, null);
    }
}
