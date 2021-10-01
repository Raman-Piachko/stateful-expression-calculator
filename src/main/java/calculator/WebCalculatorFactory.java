package calculator;

public class WebCalculatorFactory {
    public WebCalculatorFactory() {
    }

    public CalculatorService createCalculator() {
        return new CalculatorServiceImpl();
    }
}