package br.com.order.catalog.management.specification.filter;

public class OrderFilter {

    private String status;

    public OrderFilter(String status) {
        this.status = status;
    }

    public OrderFilter() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
