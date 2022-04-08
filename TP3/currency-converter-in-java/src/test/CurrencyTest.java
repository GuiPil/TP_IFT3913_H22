package test;

import currencyConverter.Currency;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @BeforeAll
    void initAll(){
        Integer[] amounts = {-5000, -1, 0, 5000, 10000, 10001, 20000};
        String[] currencyNames = {"USD", "CAD", "JPY"};
        ArrayList<Currency> currencies = new ArrayList<Currency>();

        //adding valid currencies
        currencies.add( new Currency("US Dollar", "USD") );
        currencies.get(0).setExchangeValues("USD", 1.00);
        currencies.get(0).setExchangeValues("CAD", 1.26);
        currencies.add( new Currency("CAN Dollar", "CAD") );
        currencies.get(1).setExchangeValues("CAD", 1.00);
        currencies.get(1).setExchangeValues("USD", 0.79);

    }

    @Test
    void convertBlackBox() {

    }
}