package com.example.nativeopencvandroidtemplate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class ConvertFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    String[] money = { "Real", "Dollar", "Euro"};
    private String firstSpinner = "";
    private String secondSpinner = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.convert_price, container, false);
        final EditText quantity = (EditText) rootView.findViewById(R.id.text_quantity_from);
        quantity.addTextChangedListener(new NumberTextWatcherWithSeperator(quantity));
        Button convert = (Button) rootView.findViewById(R.id.button_scrapper);
        convert.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                String quantity_string = quantity.getText().toString();
                //Intent intent = new Intent(getContext(), Scrapper.class);
                //intent.putExtra("from",firstSpinner);
                //intent.putExtra("value",secondSpinner);
                //intent.putExtra("quantity",quantity_string);
                //startActivity(intent);
            }
        });

        Spinner spin_from = (Spinner) rootView.findViewById(R.id.spinner_from);
        Spinner spin_to = (Spinner) rootView.findViewById(R.id.spinner_to);
        spin_from.setOnItemSelectedListener(this);
        spin_to.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,money);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_from.setAdapter(aa);
        spin_to.setAdapter(aa);


        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if(keycode==keyEvent.KEYCODE_BACK){
                    Intent intent = new  Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        return rootView;
    }


    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
        Spinner spinner = (Spinner) parent;

        if(spinner.getId() == R.id.spinner_from)
        {
            firstSpinner = parent.getSelectedItem().toString();
        }
        else if(spinner.getId() == R.id.spinner_to)
        {
            secondSpinner = parent.getSelectedItem().toString();
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}

class NumberTextWatcherWithSeperator implements TextWatcher {

    private EditText editText;


    public NumberTextWatcherWithSeperator(EditText editText) {
        this.editText = editText;


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            editText.removeTextChangedListener(this);
            String value = editText.getText().toString();

            if (!value.equals("")) {

                if (value.startsWith(".")) {
                    editText.setText("0.");
                }
                if (value.startsWith("0") && !value.startsWith("0.")) {
                    editText.setText("");

                }

                String str = editText.getText().toString().replaceAll(",", "");
                if (!value.equals(""))
                    editText.setText(getDecimalFormattedString(str));
                editText.setSelection(editText.getText().toString().length());
            }
            editText.addTextChangedListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
            editText.addTextChangedListener(this);
        }
    }

    private static String getDecimalFormattedString(String value) {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3) {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }

    }

}

