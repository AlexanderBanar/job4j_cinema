package ru.job4j.servlet;

import ru.job4j.cinema.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HallServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String ticketsToHtml = PsqlStore.instOf().getSeatsHtml();
        System.out.println(ticketsToHtml);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        resp.getWriter().write(ticketsToHtml);
    }
}
