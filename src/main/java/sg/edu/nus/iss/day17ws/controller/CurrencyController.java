package sg.edu.nus.iss.day17ws.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.day17ws.service.CurrencyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/currencies")
    public Map<String, String> getCurrencies() {
        return currencyService.getCurrencies();
    }
    
    
}
