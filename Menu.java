public class Menu implements Cloneable {
    private String name;
    private int price;
    private int amount = 0;
    private Store fromStore;
    public Menu(String name, int price, Store from) {
        this.name = name;
        this.price = price;
        this.fromStore = from;
    }
    public String GetName() {
        return name;
    }
    public void SetName(String name) {
        this.name = name;
    }
    public int GetPrice() {
        return price;
    }
    public void SetPrice(int price) {
        this.price = price;
    }
    public int GetAmount() {
        return amount;
    }
    public void SetAmount(int amount) {
        this.amount = amount;
    }
    public Store GetAncestorFromStore() {
    	return this.fromStore;
    }
    @Override
    public Menu clone() throws CloneNotSupportedException {
        return (Menu) super.clone();
    }
}