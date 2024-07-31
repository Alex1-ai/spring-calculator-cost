package com.chidi.moneycalculator.calculate;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RandomStringService {

    private final RestTemplate restTemplate;

    public RandomStringService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getRandomString() {
        String url = "https://www.random.org/strings/?num=1&len=10&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new";
        return restTemplate.getForObject(url, String.class).trim();
    }
}
