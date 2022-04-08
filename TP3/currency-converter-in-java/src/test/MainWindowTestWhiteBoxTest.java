package test;

import currencyConverter.Currency;
import currencyConverter.MainWindow;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MainWindowTestWhiteBoxTest {

        static ArrayList<Currency> testTable = Currency.init();

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
            assertEquals(0.0, MainWindow.convert("CAD Dollar", null, testTable, 100d));
            assertEquals(0.0, MainWindow.convert("US Dollar", null, testTable, 100d));
            assertEquals(0.0, MainWindow.convert("US Dollar", "CAD Dollar",testTable, 100d));
            assertEquals(100d,MainWindow.convert("US Dollar", "US Dollar", testTable, 100d));

    }
}