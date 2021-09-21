package calculator;

import com.google.code.mathparser.MathParser;
import com.google.code.mathparser.MathParserFactory;


public class CalculatorImpl implements Calculator {

    @Override
    public int calculate(String expression) {
        MathParser mathParser = MathParserFactory.create();

        return mathParser.calculate(expression)
                .doubleValue()
                .intValue();
    }
}