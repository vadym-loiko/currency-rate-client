package org.vloiko.currencyrateclient.currencyrateclient;

import java.util.List;

public interface RateService {

    double getRate();

    List<Double> getRateList();

    double getMonthAverage();
}
