package com.example.nativeopencvandroidtemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class Convert_content extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] money = { "Real", "Dollar", "Euro"};

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new  Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convert_price);
        EditText quantity = (EditText) findViewById(R.id.text_quantity_from);
        TextView price = (TextView) findViewById(R.id.text_price);
        Button convert = (Button) findViewById(R.id.button_scrapper);
        convert.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            //Intent intent = new Intent(getBaseContext(), Scrapper.class);
            //startActivity(intent);
        }
        });


        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin_from = (Spinner) findViewById(R.id.spinner_from);
        Spinner spin_to = (Spinner) findViewById(R.id.spinner_to);
        spin_from.setOnItemSelectedListener(this);
        spin_to.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance
        ArrayAdapter aa_from = new ArrayAdapter(this,android.R.layout.simple_spinner_item,money);
        aa_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin_from.setAdapter(aa_from);

        //Creating the ArrayAdapter instance
        ArrayAdapter aa_to = new ArrayAdapter(this,android.R.layout.simple_spinner_item,money);
        aa_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin_to.setAdapter(aa_to);

    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        //if(parent.getId() == R.id.spinner_from)
        //{
        //    Toast.makeText(getApplicationContext(),money[position] , Toast.LENGTH_SHORT).show();
        //}
        //else if(parent.getId() == R.id.spinner_to)
        //{
            Toast.makeText(getApplicationContext(),money[position] , Toast.LENGTH_SHORT).show();
        //}

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
