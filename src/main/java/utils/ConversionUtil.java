package utils;

import java.util.List;

import static constants.ControllerConstants.EMPTY_SYMBOL;
import static constants.ControllerConstants.SPACES;

public class ConversionUtil {

    private ConversionUtil() {
    }

    public static String deleteSpacesAndConvertListToString(List<String> expression) {
        return String.join(EMPTY_SYMBOL, expression)
                .replaceAll(SPACES, EMPTY_SYMBOL);
    }
}