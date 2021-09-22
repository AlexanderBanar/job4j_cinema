package ru.job4j.cinema;

import java.util.Objects;

public class Ticket {
    private int session;
    private int row;
    private int cell;
    private int accountId;

    public Ticket(int session, int row, int column, int accountId) {
        this.session = session;
        this.row = row;
        this.cell = column;
        this.accountId = accountId;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int column) {
        this.cell = column;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return session == ticket.session && row == ticket.row
                && cell == ticket.cell && accountId == ticket.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, row, cell, accountId);
    }
}
