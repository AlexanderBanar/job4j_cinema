package ru.job4j.cinema;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Ticket> findAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cinema.public.tickets")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    tickets.add(new Ticket(
                            it.getInt("session_id"),
                            it.getInt("row"),
                            it.getInt("cell"),
                            it.getInt("account")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public void buyTicket(Account account, Ticket ticket) {
        int accountId = saveAccount(account);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "UPDATE cinema.public.tickets SET account_id = (?) WHERE row = (?) and cell = (?)")
        ) {
            ps.setInt(1, accountId);
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getCell());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int saveAccount(Account account) {
        int generatedAccountId = 0;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO cinema.public.accounts(username, email, phone) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, account.getName());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPhone());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedAccountId = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedAccountId;
    }

    public String getSeatsHtml() {
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
