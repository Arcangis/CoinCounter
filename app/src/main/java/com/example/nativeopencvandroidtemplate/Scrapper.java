package com.example.nativeopencvandroidtemplate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Scrapper {

    public static void main(String... args) throws IOException {
        Document document = Jsoup.connect("https://www.infomoney.com.br/ferramentas/cambio/").timeout(0).get();
        Elements allElements = document.select("h2[href*=tr] ~ font:containsOwn(Real VS Moedas)");
        for (Element element : allElements) {
            System.out.println(element.previousElementSibling().ownText());
        }
    }
}