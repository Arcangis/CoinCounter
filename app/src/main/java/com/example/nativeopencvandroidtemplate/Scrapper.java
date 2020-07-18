package com.example.nativeopencvandroidtemplate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scrapper extends AppCompatActivity {

    String url = "https://www.infomoney.com.br/ferramentas/cambio/";

    ArrayList<String> currency_name = new ArrayList<String>();
    ArrayList<String> currency_value = new ArrayList<String>();

    int from_number;
    int to_number;
    int currency_converted;

    TextView textView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Content().execute();
    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //Connect to the website
                Document currency = Jsoup.connect(url).get();
                Elements table = currency.select("table[class=default-table]");
                Elements rows = table.get(0).select("tr");

                for (Element row : rows) {
                    //currency_name.add(row.select("td").get(0).text());
                    //currency_value.add(row.select("td").get(2).text());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            final TextView price = (TextView) findViewById(R.id.text_price);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String from = extras.getString("from");
                String to = extras.getString("to");
                String quantity_string = extras.getString("quantity");

                int i;
                for (i = 0; i < currency_name.size();i++){
                    assert from != null;
                    if (from.equals(currency_name.get(i))){
                        from_number = Integer.parseInt(currency_value.get(i));
                    }
                    assert to != null;
                    if (to.equals(currency_name.get(i))) {
                        to_number = Integer.parseInt(currency_value.get(i));
                    }
                }
                assert from != null;
                if (from.equals("Real")){
                    from_number = 1;
                }
                if (from.equals("Real")){
                    to_number = 1;
                }
                currency_converted = from_number/to_number;

                assert quantity_string != null;
                int current_price = Integer.parseInt(quantity_string)*(from_number/to_number);

                price.setText(String.valueOf(current_price));
            }

        }
    }

}
