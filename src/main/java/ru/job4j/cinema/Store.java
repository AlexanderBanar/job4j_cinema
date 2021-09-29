package ru.job4j.cinema;

import java.util.Collection;

public interface Store {
    Collection<Ticket> findAllTickets();

    void buyTicket(Account account, Ticket ticket);

    boolean isFree(Ticket ticket);
}
