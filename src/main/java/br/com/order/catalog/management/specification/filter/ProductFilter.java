package br.com.order.catalog.management.specification.filter;

public class ProductFilter {
    private String name;
    private String type;
    private Boolean active;

    public ProductFilter() {
    }

    public ProductFilter(String name, String type, Boolean active) {
        this.name = name;
        this.type = type;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
