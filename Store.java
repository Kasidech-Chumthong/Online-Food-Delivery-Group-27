import java.util.*;

public class Store {
	private String __Name__;
	private double __Distance__;
	private double __TransferCost__;
	private ArrayList<Menu> __Items__ = new ArrayList<Menu>();
    public Store(String Name, double DistanceToStore, double TransferCost) {
        this.__Name__ = Name;
        this.__Distance__ = DistanceToStore;
        this.__TransferCost__ = TransferCost;
    }
    public String Name() {
        return this.__Name__;
    }
    public double Distance() {
        return this.__Distance__;
    }
    public double TransferCost() {
    	return this.__TransferCost__;
    }
    public void Add(String Name, int Price) {
    	Menu v = new Menu(Name, Price, this);
    	this.__Items__.add(v);
    }
    public ArrayList<Menu> Items() {
    	return this.__Items__;
    }
}
