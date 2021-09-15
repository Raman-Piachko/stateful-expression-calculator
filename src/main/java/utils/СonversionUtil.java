package utils;

import java.util.List;

import static calculator.CalculatorConstants.EMPTY_SYMBOL;
import static calculator.CalculatorConstants.SPACES;

public class СonversionUtil {

    private СonversionUtil() {
    }

    public static String deleteSpacesAndConvertListToString(List<String> expression) {
        return String.join(EMPTY_SYMBOL, expression)
                .replaceAll(SPACES, EMPTY_SYMBOL);
    }
}