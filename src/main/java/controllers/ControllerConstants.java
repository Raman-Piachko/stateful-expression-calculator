package controllers;

public final class ControllerConstants {

    private ControllerConstants() {
    }

    public static final String EMPTY_SYMBOL = "";
    public static final String EXPRESSION = "expression";
    public static final String SPACES = "\\s+";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String DIVIDE = "/";
    public static final String MULTIPLY = "*";
    public static final int ABS_RANGE = 10000;
    public static final int URL_SUBSTRING = 6;
    public static final String OVER_RANGE = "VALUE IS OVER RANGE";
    public static final String WRONG_EXPRESSION = "WRONG FORMAT EXPRESSION";
    public static final String LOCATION = "Location";
    public static final String MISSING_EXPRESSION = "The expression is missing";
    public static final String PARSING_INFO = "%s cannot be parsed to Integer";
    public static final String CALCULATE_EXCEPTION = "SOMETHING WAS WRONG AT CALCULATE EXPRESSION: EXPRESSION IS -- %s";
}