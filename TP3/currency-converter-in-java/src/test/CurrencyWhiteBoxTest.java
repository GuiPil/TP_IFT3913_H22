package test;

import currencyConverter.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyWhiteBoxTest {

    @Test
    public void whiteBoxCouvertureIns(){
        assertEquals(79, Currency.convert(100d, 0.79));
        assertEquals(1.07, Currency.convert(0.85, 1.26));
    }


}