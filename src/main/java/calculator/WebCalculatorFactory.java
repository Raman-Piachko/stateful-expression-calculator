package calculator;

import repository.Repository;
import repository.RepositoryImpl;

public class WebCalculatorFactory {
    public WebCalculatorFactory() {
    }

    public CalculatorService createCalculator() {
        return new CalculatorServiceImpl();
    }

    public Repository createRepository() {
        return RepositoryImpl.getInstance();
    }
}