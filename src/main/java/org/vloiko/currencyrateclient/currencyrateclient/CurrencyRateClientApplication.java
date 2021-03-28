package org.vloiko.currencyrateclient.currencyrateclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CurrencyRateClientApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CurrencyRateClientApplication.class, args);

        RateService rateService = context.getBean(RateService.class);
        System.out.println(rateService.getRate());
        System.out.println(rateService.getRateList());
        System.out.println(rateService.getMonthAverage());
    }

}
