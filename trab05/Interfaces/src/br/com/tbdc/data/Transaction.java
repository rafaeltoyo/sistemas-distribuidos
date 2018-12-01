package br.com.tbdc.data;

import java.time.LocalDate;

public class Transaction {

    private String content;

    private LocalDate timestamp;

    private Transaction.Status status;

    public Transaction(String content, LocalDate timestamp, Status status) {
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "content='" + content + '\'' +
                ", timestamp=" + timestamp.toString() +
                ", status=" + status +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        WAINTG,
        CANCELED,
        CONFIRMED
    }
}
