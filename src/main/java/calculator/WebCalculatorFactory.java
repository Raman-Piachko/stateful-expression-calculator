package calculator;

import controllers.Controller;
import controllers.ServletController;

public class WebCalculatorFactory {
    public WebCalculatorFactory() {
    }

    public Calculator createCalculator() {
        return new CalculatorServiceImpl();
    }

    public Controller createServletController() {
        return new ServletController();
    }
}