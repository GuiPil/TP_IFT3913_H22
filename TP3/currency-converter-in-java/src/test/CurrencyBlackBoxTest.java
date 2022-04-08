package test;

import currencyConverter.Currency;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class CurrencyBlackBoxTest {

    @DisplayName("Testing valid amount 0, 5000, 10000")
    @ParameterizedTest
    @CsvSource({"0", "5000", "10000"})
    void convert_valid_amount_test(Double validAmounts) {
        Double exchangeValue = 1.5;
        Double actual = Currency.convert(validAmounts, exchangeValue);
        assertEquals(
                validAmounts * exchangeValue,
                actual,
                "Expecting price to be amount * exchangeValue");
    }

    @DisplayName("Testing invalid and frontiere value -2000, -1, 10001, 20000")
    @ParameterizedTest
    @CsvSource({"-2000", "-1", "10001", "20000"})
    void convert_invalid_amount_test(Double invalidAmounts) {
        Double exchangeValue = 1.5;
        Double actual = Currency.convert(invalidAmounts, exchangeValue);
        assertEquals(
                -1.0,
                actual,
                "Expecting price to be -1 because the amount is invalid");
    }

    @DisplayName("Testing valid exchangeValue")
    @ParameterizedTest
    @CsvSource({"0", "10", "1000000"})
    void convert_valid_exchange_value_test(Double validExchangeRate) {
        Double amount = 1.0;
        Double actual = Currency.convert(amount, validExchangeRate);
        assertEquals(
                amount * validExchangeRate,
                actual,
                "Expecting convert to succeed with value amount * validExchangeRate");
    }

    @DisplayName("Testing invalid exchangeValue")
    @ParameterizedTest
    @CsvSource({"-10", "-1", "1000001", "2000000"})
    void convert_invalid_exchange_value_test(Double validExchangeRate) {
        Double amount = 1000.0;
        Double actual = Currency.convert(amount, validExchangeRate);
        assertEquals(
                -1.0,
                actual,
                "Expecting convert to return -1.0 because of invalid exchange value");
    }



}