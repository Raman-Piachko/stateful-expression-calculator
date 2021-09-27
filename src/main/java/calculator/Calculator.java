package calculator;

import javax.servlet.http.HttpSession;

public interface Calculator {
    int calculate(HttpSession session);
}