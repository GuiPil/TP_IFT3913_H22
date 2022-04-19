package test;

import currencyConverter.Currency;
import currencyConverter.MainWindow;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MainWindowTestWhiteBoxTest {

    static ArrayList<Currency> currencies = new ArrayList<Currency>();
    static ArrayList<Currency> empty = new ArrayList<>();
    static ArrayList<Currency> USDsolo = new ArrayList<>();

    static Double[] amountsValid;

    static Double exchangeValueUSDtoCAD; //USD to CAD
    static Double exchangeValueCADtoUSD; //CAD to USD



    @BeforeAll
    static void initAll(){
        exchangeValueUSDtoCAD = 1.26;
        exchangeValueCADtoUSD = 0.79;


        //adding valid currencies
        currencies.add( new Currency("CAN Dollar", "CAD") );
        currencies.get(0).setExchangeValues("CAD", 1.00);
        currencies.get(0).setExchangeValues("USD", exchangeValueCADtoUSD);
        currencies.add( new Currency("US Dollar", "USD") );
        currencies.get(1).setExchangeValues("USD", 1.00);
        currencies.get(1).setExchangeValues("CAD", exchangeValueUSDtoCAD);
        USDsolo.add(new Currency("US Dollar", "USD"));
        USDsolo.get(0).setExchangeValues(("USD"), 1.00);

    }

        @Test
        @DisplayName("Couverture des instruction")
        void couvertureInstructionWT(){
            assertEquals(126d,MainWindow.convert("US Dollar","CAN Dollar",currencies, 100d));
        }

        @Test
        @DisplayName("Couverture des arcs de graphe de flot de controle")
        void couvertureArcWTD1(){
            assertEquals(126d, MainWindow.convert("US Dollar", "CAN Dollar", currencies, 100d));
        }

        @Test
        void couvertureArcWTD2(){
            assertEquals(0.0, MainWindow.convert("US Dollar", null, currencies, 100d));
    }

        @Test
        void couvertureArcWTD3(){
            assertEquals(0.0, MainWindow.convert("PESO Peso", "US Dollar", currencies, 100d));
        }

    @Test
    @DisplayName("Couverture des chemins independants")
        void couvertureIndependant(){
            assertEquals(0.0, MainWindow.convert("PESO Pesos", "CAN Dollar",empty, 100d));
    }

    @Test
    void couvertureIndependantD2(){
        assertEquals(0.0, MainWindow.convert("PESO Pesos", "US Dollar", USDsolo, 100d));
    }

    @Test
    void couvertureIndependantD3(){
        assertEquals(100d,MainWindow.convert("US Dollar", "US Dollar", currencies, 100d));
    }
}