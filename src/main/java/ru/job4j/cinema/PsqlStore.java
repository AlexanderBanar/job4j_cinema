package ru.job4j.cinema;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

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
        List<Ticket> tickets = new ArrayList<>(9);
        Map<Integer, Ticket> map = new HashMap<>(9);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cinema.public.tickets")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    map.put(it.getInt("id"), new Ticket(
                            it.getInt("session_id"),
                            it.getInt("row"),
                            it.getInt("cell"),
                            it.getInt("account_id")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 10; i <= 18; i++) {
            tickets.add(map.get(i));
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
                    generatedAccountId = rs.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedAccountId;
    }

    @Override
    public boolean isFree(Ticket ticket) {
        int result = 0;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM cinema.public.tickets WHERE row = (?) and cell = (?)")
        ) {
            ps.setInt(1, ticket.getRow());
            ps.setInt(2, ticket.getCell());
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    result = it.getInt("account_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 0;
    }
}
