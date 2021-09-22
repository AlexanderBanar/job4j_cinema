package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.cinema.Ticket;
import ru.job4j.cinema.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HallServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final Gson gson = new GsonBuilder().create();
        String ticketsToHtml = gson.toJson(getSeatsHtml());
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        resp.getWriter().write(ticketsToHtml);
    }

    private String getSeatsHtml() {
        List<Ticket> allTickets = (List<Ticket>) PsqlStore.instOf().findAllTickets();
        StringBuilder ticketsToHtml = new StringBuilder();
        int counter = 0;
        for (int i = 1; i < 4; i++) {
            ticketsToHtml.append("<tr><th>").append(i).append("</th>");
            for (int j = 1; j < 4; j++) {
                if (allTickets.size() > counter) {
                    if (allTickets.get(counter).getAccountId() == 0) {
                        ticketsToHtml.append("<td>")
                                .append("<input type='radio' name='ticket' value='")
                                .append(allTickets.get(counter).getRow())
                                .append(allTickets.get(counter).getCell()).append("'>")
                                .append(" Место, ")
                                .append(allTickets.get(counter).getCell()).append("</td>");

                    } else {
                        ticketsToHtml.append("<input disabled type='radio' name='ticket' value='")
                                .append(allTickets.get(counter).getRow())
                                .append(allTickets.get(counter).getCell()).append("'>")
                                .append(" Место, ")
                                .append(allTickets.get(counter).getCell()).append("</td>");
                    }
                    counter++;
                }
            }
            ticketsToHtml.append("</tr>");
        }
        return ticketsToHtml.toString();
    }

}
