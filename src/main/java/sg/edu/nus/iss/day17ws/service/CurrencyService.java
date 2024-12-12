package sg.edu.nus.iss.day17ws.service;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.day17ws.constant.Url;

@Service
public class CurrencyService 
{
    private final Map<String, String> currencies = new HashMap<>();

    @Value("${currency.api.key}") // inject the API key dynamically, uses application.properties one which is overridden with EXPORT
    private String apiKey;

    public void fetchCurrencies()
    {
        System.out.println("key is >>> " + apiKey);
        // Build the URL with the injected API key
        String url = String.format("%s/countries?apiKey=%s", Url.apiBaseUrl, apiKey);

        // Make the HTTP GET request
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        
        // .getForObject sends a GET request and retrieves the response body directly.
        // The body is automatically deserialized into the specified type (e.g., String, JsonObject, or custom POJO).
        // Use getForObject if you're only interested in the response body and don't need additional HTTP metadata (e.g., status code, headers).

        // .getForEntity sends a GET request and retrieves the entire HTTP response wrapped in a ResponseEntity object.
        // ResponseEntity includes:
        // The response body.
        // The HTTP status code.
        // Response headers.
        // Use getForEntity when you need access to both the response body and additional HTTP metadata.

        // e.g.
        // ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        // String responseBody = responseEntity.getBody();
        // HttpStatus statusCode = responseEntity.getStatusCode();
        // HttpHeaders headers = responseEntity.getHeaders();


        // Parse the response JSON
        JsonReader jReader = Json.createReader(new StringReader(response));
        JsonObject jResponse = jReader.readObject();

        JsonObject results = jResponse.getJsonObject("results");

        // Use a stream to iterate and populate the map
        // results.keySet().stream()
        // .forEach(countryCode -> {
        //     JsonObject country = results.getJsonObject(countryCode);
        //     currencies.put(countryCode, country.getString("currencyName"));
        // });

        //.keySet() is a map method that returns a set of all the keys in the object
        // results.keySet() returns
        // Set<String> keys = ["SGD", "USD", "EUR"];

        // traditional loop
        // for (String countryCode : results.keySet()) {
        //     JsonObject country = results.getJsonObject(countryCode);
        //     currencies.put(countryCode, country.getString("currencyName"));
        // }


        results.keySet().forEach(countryCode -> {
            JsonObject country = results.getJsonObject(countryCode);

            // Get the id like sgd etc
            String currencyId = country.getString("currencyId");

            // store the currencyId as key and currecyName as value
            currencies.put(currencyId, country.getString("currencyName"));
        });

        // 1. JsonObject results = jResponse.getJsonObject("results");
            // Extracts the results JSON object from the response.

        // How the JSON looks like
        //
        // {
        //     "results": {
        //         "SGD": {
        //             "currencyName": "Singapore Dollar"
        //         },
        //         "USD": {
        //             "currencyName": "United States Dollar"
        //         }
        //     }
        // }

        // 2. results.keySet().forEach(countryCode -> {...});
            // Retrieves all keys from the results object (e.g., "SGD", "USD").
            // Iterates over each key (here, countryCode).
        
        
        // 3. JsonObject country = results.getJsonObject(countryCode);
            // For each key (e.g., "SGD"), retrieves its corresponding JSON object (e.g., { "currencyName": "Singapore Dollar" }).

        // 4. currencies.put(countryCode, country.getString("currencyName"));
            // Extracts the currencyName field from the country JSON object and stores it in the currencies map.

        // e.g. after processing
        // currencies = {
        //     "SGD" -> "Singapore Dollar",
        //     "USD" -> "United States Dollar"
        // };


        


        System.out.println("Fetched currencies: " + currencies);
    }

    public Map<String, String> getCurrencies()
    {
        return currencies;
    }

    public double convertCurrency (String fromCurrency, String toCurrency, double amount)
    {
        String key = fromCurrency + "_" + toCurrency;

        // Construct API URL
        String url = Url.apiConvertUrl.replace("{key}", key).replace("{apiKey}", apiKey); // apiKey from @Value

        System.out.println("Calling API: " + url);

         // Call external API
         RestTemplate restTemplate = new RestTemplate();
         Map<String, Double> response = restTemplate.getForObject(url, Map.class);
 

         // The response we trying to get is, rate
        // {
        //      "XCD_AFN": 25.461392
        // }

        // Get the conversion rate and calculate the result

        double rate = response.get(key); // key is the XCD_AFN
        double conversion = rate * amount; // Calculate converted amount

         return conversion;
     }
    

}
