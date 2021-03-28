package org.vloiko.currencyrateclient.currencyrateclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vloiko.currencyrate.rate.RateServiceGrpc;

@Configuration
public class Config {

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
    }

    @Bean
    public RateServiceGrpc.RateServiceBlockingStub blockingStub() {
        return RateServiceGrpc.newBlockingStub(managedChannel());
    }

    @Bean
    public RateServiceGrpc.RateServiceStub asyncStub() {
        return RateServiceGrpc.newStub(managedChannel());
    }
}
