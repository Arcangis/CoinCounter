package com.example.nativeopencvandroidtemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ConvertFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context;

        View rootView = inflater.inflate(R.layout.convert_price, container, false);
        context = rootView.getContext(); // Assign your rootView to context

        Intent intent = new Intent(context, Convert_content.class);
        startActivity(intent);

        return rootView;
    }

}

