package sg.edu.nus.iss.day17ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sg.edu.nus.iss.day17ws.service.CurrencyService;

@SpringBootApplication
public class Day17wsApplication implements CommandLineRunner{

	@Autowired
	private CurrencyService currencyService;

	public static void main(String[] args) {
		SpringApplication.run(Day17wsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		currencyService.fetchCurrencies();
	}

}
