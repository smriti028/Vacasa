package com.airbnb.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/countries")// this will work only after you logged in
public class CountryController {

    @PostMapping("/addCountry")
    public String addCountry(){
        return "done";
    }

}
