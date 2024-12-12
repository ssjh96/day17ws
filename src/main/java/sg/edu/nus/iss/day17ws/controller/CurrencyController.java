package sg.edu.nus.iss.day17ws.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import sg.edu.nus.iss.day17ws.constant.Url;
import sg.edu.nus.iss.day17ws.service.CurrencyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


@Controller
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Value("${currency.api.key}") // inject the API key dynamically, uses application.properties one which is overridden with EXPORT
    private String apiKey;

    @GetMapping("/currencies")
    @ResponseBody // Explicitly marks this method to return JSON
    public Map<String, String> getCurrencies() {
        return currencyService.getCurrencies();
        // Map<String, String> is response body
        // @RestController combines @Controller and @ResponseBody
        // This means the return value of your method is automatically serialized into JSON and sent as the HTTP response body.
    }
    
    @GetMapping("/")
    public String showLandingPage(Model model) {

        Map<String, String> currencies = currencyService.getCurrencies();

        // debug
        System.out.println("Available currencies: " + currencies);

        // Pass the list of currencies to Thymeleaf template
        model.addAttribute("currencies", currencies);
        

        return "index";
    }
    
    @GetMapping("/convert")
    public String convertCurrencies(
        @RequestParam ("fromCurrency") String from,
        @RequestParam ("toCurrency") String to,
        @RequestParam ("amount") double amount,
        Model model) 
        {

            try
            {
                Double conversion = currencyService.convertCurrency(from, to, amount);
                
                // pass data to view
                model.addAttribute("fromCurrency", from);
                model.addAttribute("toCurrency", to);
                model.addAttribute("amount", amount);
                model.addAttribute("convertedAmount", conversion);
                
                return "result";
            } 
            catch (Exception e) 
            {
                System.err.println("Error calling the API: " + e.getMessage());
                model.addAttribute("error", "Unable to fetch conversion rate. Please try again.");
                return "index"; // Stay on the index.html page
            }
    }
    






    // EXAMPLE LOGICS - Spring automatically maps form parameters to method arguments. fromCurrency, toCurrency, and amount which were names in index.html
    // @GetMapping("/convert")
    // // @ResponseBody
    // // requestparam takes the name of the name's value to map to att
    // public String convert(
    //         @RequestParam("fromCurrency") String fromCurrency,
    //         @RequestParam("toCurrency") String toCurrency,
    //         @RequestParam("amount") double amount,
    //         Model model) {
    //     // Log or process the received parameters
    //     System.out.println("From: " + fromCurrency);
    //     System.out.println("To: " + toCurrency);
    //     System.out.println("Amount: " + amount);

    //     // Example: Add the result to the model for Thymeleaf to display
    //     double result = calculateConversion(fromCurrency, toCurrency, amount);
    //     model.addAttribute("conversionResult", result);

    //     // Return a view name (e.g., "result.html")
    //     return "result";
    // }

    // // Example: Stub method for conversion logic
    // private double calculateConversion(String from, String to, double amount) {
    //     // Replace with actual API or logic
    //     return amount * 1.35; // Example conversion rate
    // }

}
