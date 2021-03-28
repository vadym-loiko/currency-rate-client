package org.vloiko.currencyrateclient.currencyrateclient;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vloiko.currencyrate.rate.CurrencyRate;
import org.vloiko.currencyrate.rate.RateRequest;
import org.vloiko.currencyrate.rate.RateServiceGrpc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

@Service
public class RateServiceImpl implements RateService {

    private RateServiceGrpc.RateServiceBlockingStub blockingStub;
    private RateServiceGrpc.RateServiceStub asyncStub;

    @Autowired
    public RateServiceImpl(RateServiceGrpc.RateServiceBlockingStub blockingStub,
                           RateServiceGrpc.RateServiceStub asyncStub) {
        this.blockingStub = blockingStub;
        this.asyncStub = asyncStub;
    }

    @Override
    public double getRate() {
        RateRequest rateRequest = RateRequest
                .newBuilder()
                .setFromCurrency("USD")
                .setToCurrency("EUR")
                .setRateDate("01/03/2021")
                .build();

        CurrencyRate currencyRate = blockingStub.getRate(rateRequest);

        return currencyRate.getRate();
    }

    @Override
    public List<Double> getRateList() {
        RateRequest rateRequest = RateRequest
                .newBuilder()
                .setFromCurrency("USD")
                .setToCurrency("EUR")
                .setRateDate("01/03/2021")
                .build();

        List<Double> rates = new ArrayList<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        asyncStub.getRateList(rateRequest, new StreamObserver<>() {
           @Override
           public void onNext(CurrencyRate currencyRate) {
               System.out.println("Received " + currencyRate.getRate());
               rates.add(currencyRate.getRate());
           }

           @Override
           public void onError(Throwable throwable) {
               System.out.println(throwable);
               countDownLatch.countDown();
           }

           @Override
           public void onCompleted() {
                countDownLatch.countDown();
           }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }

        return rates;
    }

    @Override
    public double getMonthAverage() {
        List<Double> rate = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        StreamObserver<CurrencyRate> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(CurrencyRate currencyRate) {
                rate.add(currencyRate.getRate());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        };

        StreamObserver<RateRequest> requestObserver = asyncStub.getAverageRate(responseObserver);

        LocalDate startDate = LocalDate.of(2021,1,1);
        IntStream.rangeClosed(0,30).forEach(value -> {
            requestObserver.onNext(RateRequest.newBuilder()
                    .setFromCurrency("USD")
                    .setToCurrency("EUR")
                    .setRateDate(startDate.plusDays(value).toString())
                    .build());
        });
        requestObserver.onCompleted();

        try {
            countDownLatch.await();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }


        return rate.size() > 0 ? rate.get(0) : 0;
    }
}
