package ru.job4j.servlet;

import ru.job4j.cinema.Account;
import ru.job4j.cinema.PsqlStore;
import ru.job4j.cinema.Ticket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PaymentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String ticketNumber = req.getParameter("ticket");
        int row = Integer.parseInt("" + ticketNumber.charAt(0));
        int cell = Integer.parseInt("" + ticketNumber.charAt(1));
        Ticket ticket = new Ticket(1, row, cell, 0);
        if (PsqlStore.instOf().isFree(ticket)) {
            Account account = new Account(
                    req.getParameter("username"),
                    req.getParameter("email"),
                    req.getParameter("phone")
            );
            PsqlStore.instOf().buyTicket(account, ticket);
        } else {
            req.setAttribute("payment", ticket);
        }
        req.getRequestDispatcher("/afterPayment.jsp").forward(req, resp);
    }
}
