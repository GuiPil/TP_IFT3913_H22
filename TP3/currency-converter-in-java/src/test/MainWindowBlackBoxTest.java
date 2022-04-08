package test;

import currencyConverter.Currency;
import currencyConverter.MainWindow;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MainWindowBlackBoxTest {
    static ArrayList<Currency> currencies = new ArrayList<Currency>();
    static Double[] amountsValid;


    //USD TO CAD
    static Double exchangeValueUSDtoCAD;
    static Double[] expectedValueUSDtoCAD;
    static Double exchangeValueCADtoUSD;
    static Double[] expectedValueCADtoUSD;



    @BeforeAll
    static void initAll(){
        exchangeValueUSDtoCAD = 1.26;
        exchangeValueCADtoUSD = 0.79;

        //adding valid currencies
        currencies.add( new Currency("US Dollar", "USD") );
        currencies.get(0).setExchangeValues("USD", 1.00);
        currencies.get(0).setExchangeValues("CAD", exchangeValueUSDtoCAD);
        currencies.add( new Currency("CAN Dollar", "CAD") );
        currencies.get(1).setExchangeValues("CAD", 1.00);
        currencies.get(1).setExchangeValues("USD", exchangeValueCADtoUSD);

    }
    @DisplayName("Should accept all these amount and return the correct price from USD to CAD")
    @ParameterizedTest
    @CsvSource({"0","5000","10000"})
    void convert_test_valid_amount_usd_to_cad(Double validAmount){
        assertEquals(validAmount * exchangeValueUSDtoCAD,
                MainWindow.convert(
                        "US Dollar",
                        "CAN Dollar",
                        currencies,
                        validAmount));
    }

    @DisplayName("Test should return -1 to indicated an invalid amount")
    @ParameterizedTest
    @CsvSource({"-5000.0", "-1.0", "10001.0", "20000.0"})
    void convert_test_invalid_amount(Double invalidAmount){
        Double actualPrice = MainWindow.convert(
                "US Dollar",
                "CAN Dollar",
                currencies,
                invalidAmount);

        Double expectedPrice = -1.0;

        assertEquals(
                expectedPrice,
                actualPrice,
                "Price output should be -1 to indicate that the input amount was invalid."
                );
    }

    @DisplayName("Converting currency that are not in currencies should return -1 to indicated an error")
    @Test
    void convert_test_with_valid_currencies_array_only_value() {
        ArrayList<Currency> invalidCurrencies = new ArrayList<Currency>();
        invalidCurrencies.add(new Currency("US Dollar", "USD"));
        invalidCurrencies.get(0).setExchangeValues("USD", 1.0);
        invalidCurrencies.get(0).setExchangeValues("CAD", 1.26);

        Double actualPrice = MainWindow.convert(
                "US Dollar",
                "CAN Dollar",
                invalidCurrencies,
                5000d
        );
        Double expectedPrice = -1.0;

        assertEquals(expectedPrice, actualPrice, "array only have one entry and should return -1");

    }

    @DisplayName("Testing and invalid currency2 with currency2 being USD")
    @Test
    void convert_test_varying_currency1() {
        Double actual = MainWindow.convert(
                "PESO Peso",
                "US Dollar",
                currencies,
                5000d
        );

        assertEquals(-1.0,
                actual,
                "PESO is not in the valid currency and should therefore return -1.0 to indicate an error");
    }

    @DisplayName("Testing and invalid currency2 with currency1 being USD")
    @Test
    void convert_test_varying_currency2() {
        Double actual = MainWindow.convert(
                "US Dollar",
                "PESO Peso",
                currencies,
                5000d
        );

        assertEquals(-1.0,
                actual,
                "PESO is not in the valid currency and should therefore return -1.0 to indicate an error");
    }

}