package calculator;

public class WebCalculatorFactory {
    public WebCalculatorFactory() {
    }

    public Calculator createCalculator() {
        return new CalculatorImpl();
    }
}