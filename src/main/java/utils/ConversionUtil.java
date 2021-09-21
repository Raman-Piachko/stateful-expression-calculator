package utils;

import java.util.List;

import static controllers.ControllerConstants.EMPTY_SYMBOL;
import static controllers.ControllerConstants.SPACES;

public class ConversionUtil {

    private ConversionUtil() {
    }

    public static String deleteSpacesAndConvertListToString(List<String> expression) {
        return String.join(EMPTY_SYMBOL, expression)
                .replaceAll(SPACES, EMPTY_SYMBOL);
    }
}