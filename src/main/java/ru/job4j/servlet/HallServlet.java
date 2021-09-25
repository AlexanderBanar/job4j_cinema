package ru.job4j.servlet;

import ru.job4j.cinema.PsqlStore;
import ru.job4j.cinema.Ticket;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HallServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Collection<Ticket> allTickets = PsqlStore.instOf().findAllTickets();
        final Gson gson = new GsonBuilder().create();
        String ticketsToJson = gson.toJson(allTickets);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        resp.getWriter().write(ticketsToJson);
    }
}
