package com.plastic.scraper.service;

import com.plastic.scraper.response.NaverCafe;
import com.plastic.scraper.response.ScrappingResult;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ScrapService {


    public ScrappingResult naverCafeScrap(String keyWord) throws IOException {

        ArrayList<NaverCafe> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            String pageSource = getPageSourceFromNaverCafe(keyWord, i);

            Document document = Jsoup.parse(pageSource);

            Elements elements = document.select("div.ArticleItem");

            System.out.println("elements.size() = " + elements.size());


            for (Element element : elements) {
                NaverCafe naverCafe = new NaverCafe();
                String text = element.select("strong").text();
                String link = element.select("a").attr("href");

                String cafeName = element.select("span.cafe_name").text();

                if (cafeName.contains("분양") || cafeName.contains("입양")) {
                    continue;
                }
                if (text.contains("분양") || text.contains("입양")) {
                    continue;
                }
                naverCafe.setTitle(text);
                naverCafe.setUrl(link);
                naverCafe.setImage(element.select("img").attr("src"));

                list.add(naverCafe);
            }
        }

        ScrappingResult scrappingResult = new ScrappingResult();
        scrappingResult.setCafeArticle(list);

        return scrappingResult;
    }

    private static String getPageSourceFromNaverCafe(String keyWord, int pageNum) {
        long time = System.currentTimeMillis();

        final String cafeSearchUrl = "https://section.cafe.naver.com/ca-fe/home/search/articles";
        String url = cafeSearchUrl + "?q=" + keyWord + "&" + time + "&od=1&p=" + pageNum;
        System.setProperty("webdriver.chrome.driver", "C:\\dev\\scraper\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        WebDriver driver = new ChromeDriver(options);

        StringBuilder pageSource = new StringBuilder();


        driver.get(url);

        pageSource.append(driver.getPageSource());

        driver.quit();
        return pageSource.toString();
    }

}
