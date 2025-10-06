package data;

import javafx.beans.property.*;

public class Item {
    private final IntegerProperty code = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty count = new SimpleIntegerProperty(1); // قيمة ابتدائية

    public Item(int code, String name, double price, int count) {
        this.code.set(code);
        this.name.set(name);
        this.price.set(price);
        this.count.set(count);
    }

    public Item(int code, String name, double price) {
        this.code.set(code);
        this.name.set(name);
        this.price.set(price);
        this.count.set(1);
    }

    public int getCode() { return code.get(); }
    public String getName() { return name.get(); }
    public double getPrice() { return price.get(); }
    public int getCount() { return count.get(); }

    public IntegerProperty codeProperty() { return code; }
    public StringProperty nameProperty() { return name; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty countProperty() { return count; }

    public void setCount(int count) { this.count.set(count); }

    public void setCode(int code) {
        this.code.set(code);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPrice(double price) {
        this.price.set(price);
    }


    public void increaseCount() {
        this.count.set(this.count.get() + 1);
    }

    public void decreaseCount() {
        if (this.count.get() > 1) {
            this.count.set(this.count.get() - 1);
        }
    }
}
