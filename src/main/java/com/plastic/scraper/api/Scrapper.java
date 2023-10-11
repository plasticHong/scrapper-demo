package com.plastic.scraper.api;

import com.plastic.scraper.response.ScrappingResult;
import com.plastic.scraper.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/scrap")
@RequiredArgsConstructor
public class Scrapper {

    private final ScrapService scrapService;

    @RequestMapping(value = "/key-word",method = RequestMethod.GET)
    public ResponseEntity<?> extract(@RequestParam String keyWord) {

        ScrappingResult scrap = null;

        try {
            scrap = scrapService.naverCafeScrap(keyWord);
        }catch (IOException e){

        }

        return new ResponseEntity<>(scrap,HttpStatus.OK);
    }


}
