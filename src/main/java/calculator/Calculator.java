package calculator;

import javax.servlet.ServletException;
import java.io.IOException;

public interface Calculator {
    int calculate(String expression) throws ServletException, IOException;
}