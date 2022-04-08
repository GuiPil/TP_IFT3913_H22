package test;

import currencyConverter.Currency;
import currencyConverter.MainWindow;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.NullSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MainWindowTest {
    static ArrayList<Currency> currencies = new ArrayList<Currency>();
    static Double[] amountsTest;

    //USD TO CAD
    static Double exchangeValueUSDtoCAD;
    static Double[] expectedValueUSDtoCAD;
    static Double exchangeValueCADtoUSD;
    static Double[] expectedValueCADtoUSD;


    @BeforeAll
    static void initAll(){
        amountsTest = new Double[] {-5000.0, -1.0, 0.0, 5000.0, 10000.0, 10001.0, 20000.0};
        exchangeValueUSDtoCAD = 1.26;
        expectedValueUSDtoCAD = new Double[]{
                0.0,
                0.0,
                0.0,
                5000 * exchangeValueUSDtoCAD,
                10000.0 * exchangeValueUSDtoCAD,
                0.0,
                0.0
        };

        //adding valid currencies
        currencies.add( new Currency("US Dollar", "USD") );
        currencies.get(0).setExchangeValues("USD", 1.00);
        currencies.get(0).setExchangeValues("CAD", exchangeValueUSDtoCAD);
        currencies.add( new Currency("CAN Dollar", "CAD") );
        currencies.get(1).setExchangeValues("CAD", 1.00);
        currencies.get(1).setExchangeValues("USD", exchangeValueCADtoUSD);
    }

    @Test
    void convertBlackBoxTest() {

        //Currency1
        assertEquals(0.0, MainWindow.convert("US Dollar", "CAN Dollar", currencies, 1000d ));
        assertEquals(0.0, MainWindow.convert("US Dollar", "PESO Peso", currencies, 1000d ));
        assertEquals(0.0, MainWindow.convert("PESO Peso", "US Dollar", currencies, 1000d ));

        //USD to CAD
        for (int i = 0; i < amountsTest.length; i++){
            assertEquals(expectedValueUSDtoCAD[i],
                    MainWindow.convert(
                            "US Dollar",
                            "CAN Dollar",
                            currencies,
                            amountsTest[i]));
        }
    }

    @Nested
    @DisplayName("White Tests")
    class MainWindowTestWT{
        ArrayList<Currency> testTable = Currency.init();

        @Test
        @DisplayName("Couverture des instruction")
        void couvertureInstructionWT(){
            assertEquals(93,MainWindow.convert("US Dollar","Euro", testTable, 100d));
        }

        @Test
        @DisplayName("Couverture des arcs de graphe de flot de controle")
        void couvertureArcWT(){
            assertEquals(93, MainWindow.convert("US Dollar", "Euro", testTable, 100d));
            assertEquals(0.0, MainWindow.convert("US Dollar", null, testTable, 100d));
            assertEquals(0.0, MainWindow.convert("CAD Dollar", "Euro", testTable, 100d));
        }

        @Test
        @DisplayName("Couverture des chemins independants")
        void couvertureIndependant(){

        }

    }
}