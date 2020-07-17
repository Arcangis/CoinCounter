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


public class CounterFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context;

        View rootView = inflater.inflate(R.layout.counter, container, false);
        context = rootView.getContext(); // Assign your rootView to context

        Button start = (Button) rootView.findViewById(R.id.button_start);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Pass the context and the Activity class you need to open from the Fragment Class, to the Intent
                Intent intent = new Intent(context, Counter_content.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

}
