package com.example.dividendsproject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class DividendsProjectApplication {

    /**
    야후->코카콜라->주식->배당금 -> 월기준
     1. http커넥션을 맺고 그 커낵션으로부터 html문서를 받아서
     Document document = connect.get(); 제이숩라이브러리로 활용해서 얻고

     그파싱된 데이터가 들어있는 도큐먼트로부터
     Elements elementsByAttributeValue = document.getElementsByAttributeValue("data-test", "historical-prices");
     Element element = elementsByAttributeValue.get(0);

     for~ 데이터 추출
     근데 이규칙이 안쓰는홈페이지면? 이렇게 못쓸거니까 도메인파악 중요
     */

    public static void main(String[] args) {
//        SpringApplication.run(DividendsProjectApplication.class, args);

        try {
            Connection connect = Jsoup.connect("https://finance.yahoo.com/quote/COKE/history?period1=99100800&period2=1667606400&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
            Document document = connect.get();

            Elements elementsByAttributeValue = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element element = elementsByAttributeValue.get(0);


            Element tbody = element.children().get(1);

            for( Element e : tbody.children()){
                String txt = e.text();
                if(!txt.endsWith("Dividend")){
                    continue;
                }
            //  Oct 26, 2022 0.25 Dividend -> 2022/Oct/31 -> 0.25

                String[] splits = txt.split(" ");

                String month = splits[0];
                int day = Integer.valueOf(splits[1].replace(",",""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];

                System.out.println(year+"/"+month+"/"+day+" ---> "+dividend);


            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
