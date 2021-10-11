package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {

    void deleteData(HttpServletRequest request, HttpServletResponse response);

    void putNewData(HttpServletRequest request, HttpServletResponse response);

    void getResult(HttpServletRequest request, HttpServletResponse response);
}