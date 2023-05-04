import java.util.*;
import java.io.*;

// 1.Classes, 2.Inheritance, 3.Try / catch, 4.Input & Output, or 5.Collections.

public class Program extends Utilitys {
	private Terminal Console = new Terminal();
	private String DATABASE_KEY = "eacd5fdb828da7f51b387472358c94b53c284bda";
	private ArrayList<Store> Stores = new ArrayList<Store>();
	private HashMap<String, Menu> Cart = new HashMap<String, Menu>(); // [MenuName] = MenuInstance
	public Program() {
		File MyCafeMenuFile = new File(DATABASE_KEY + ".stor");
		try (BufferedReader BuffReader = new BufferedReader(new FileReader(MyCafeMenuFile))) { // 4) IO
			String Line = null;
			Store StoreInst = null;
			while ((Line = BuffReader.readLine()) != null) {
				if (StoreInst != null) {
					String[] ItemInfo = Line.split(", ");
					String ContainKey = SafeGet(ItemInfo, 0);
			        if (ContainKey.equals("}")) {
			        	StoreInst = null;
			        	continue;
			        }
			        String ItemName = SafeGet(ItemInfo, 0);
			        int ItemPrice =  Integer.parseInt(SafeGet(ItemInfo, 1));
			        StoreInst.Add(ItemName, ItemPrice);
				} else {
					String[] StoreInfo = Line.split(", ");
			        String StoreName = SafeGet(StoreInfo, 0);
			        double StoreDistance =  Double.parseDouble(SafeGet(StoreInfo, 1));
			        double StoreTransferCost = Double.parseDouble(SafeGet(StoreInfo, 2));
			        String ContainKey = SafeGet(StoreInfo, 3);
			        if (ContainKey.equals("{")) {
			        	StoreInst = new Store(StoreName, StoreDistance, StoreTransferCost);
				        Stores.add(StoreInst); // add store
			        	continue;
			        }
				}
			}
		} catch (IOException e) {}
	}
	public void FoodPage(boolean PickupMode) {
		boolean StillInStoresPage = true;
    	do {
    		Console.Clear();
    		if (Cart.size() > 0) {
    			System.out.println("========== <Cart> ==========");
    			for (Map.Entry<String, Menu> Set : Cart.entrySet()) {
    			    String MenuName = Set.getKey();
    			    Menu vMenu = Set.getValue();
    				System.out.println(String.format(" - %s %s, %d baht", vMenu.GetName(), vMenu.GetAmount() == 0 ? "" : String.format("(x%d)", vMenu.GetAmount()), vMenu.GetAmount() * vMenu.GetPrice()));
    			}
    			System.out.println("========== ====== ==========");
    		}
    		System.out.println("============================");
	        System.out.println("           Stores          ");
	        System.out.println("============================");
        	for (int Index = 0; Index < Stores.size(); Index++) {
        		Store vStore = Stores.get(Index);
        		System.out.println(String.format("%d. %s (%.2f km)", Index + 1, vStore.Name(), vStore.Distance()));
        	}
        	int ConfirmMenu = Stores.size() + 1;
        	int BackMenu = Stores.size() + 2;
        	System.out.println(String.format("%d. ;Confirm", ConfirmMenu));
        	System.out.println(String.format("%d. ;Back", BackMenu));
        	int StoreIdx = Console.PromptInteger("// Select your store", 1, BackMenu);
        	if (StoreIdx == ConfirmMenu) {
        		Console.Clear();
        		System.out.println(String.format("Receipt Id: [%s]", UUID.randomUUID().toString()));
        		HashMap<String, Store> FromStore = new HashMap<String, Store>();
        		double TotalPrice = 0;
        		System.out.println(" Menu:");
        		for (Map.Entry<String, Menu> Set : Cart.entrySet()) {
    			    String MenuName = Set.getKey();
    			    Menu vMenu = Set.getValue();
    			    TotalPrice += vMenu.GetAmount() * vMenu.GetPrice();
    			    System.out.println(String.format("  - %s: x%d (%s baht)", vMenu.GetName(), vMenu.GetAmount(), vMenu.GetAmount() * vMenu.GetPrice()));
    			    Store SelfStore = vMenu.GetAncestorFromStore();
    			    FromStore.put(SelfStore.Name(), SelfStore);
        		}
        		// Calculate distance & total cost transfer
        		double EstimateDistance = 0;
        		double EstimateTransferDistanceCost = 0;
        		for (Map.Entry<String, Store> Set : FromStore.entrySet()) {
        			Store vStore = Set.getValue();
        			EstimateDistance += vStore.Distance();
        			EstimateTransferDistanceCost += vStore.TransferCost();
        		}
        		System.out.println(" Summarize:");
        		System.out.println("  - Total Cost: " + TotalPrice + " baht");
        		if (PickupMode) {
        			System.out.println("  - Estimate Distance: " + EstimateDistance + " km");
            		System.out.println("  - Estimate Transfer Cost: " + EstimateTransferDistanceCost + " baht");
        		}
        		Console.Pause();
        		Cart = new HashMap<String, Menu>();
    		} else if (StoreIdx != BackMenu) {	
        		Store vStore = Stores.get(StoreIdx - 1); // index start at 0
        		ArrayList<Menu> vItems = vStore.Items();
        		Console.Clear();
        		System.out.println(String.format("=== %s / Menu ===", vStore.Name()));
        		for (int Index = 0; Index < vItems.size(); Index++) {
        			Menu vMenu = vItems.get(Index);
        			System.out.println(String.format("%d. %s, %d baht", Index + 1, vMenu.GetName(), vMenu.GetPrice()));
            	}
        		int BackToStores = vItems.size() + 1;
        		System.out.println(String.format("%d. ;Back", BackToStores));
        		int MenuIdx = Console.PromptInteger("// Select your menu", 1, BackToStores);
        		if (MenuIdx != BackToStores) {
            		int MenuAmount;
            		try { 
            			MenuAmount = Clamp((int) Double.parseDouble(Console.PromptString("// Set amount of menu", "int: CLAMP<0, 99>")), 0, 99);
            			Menu vMenu = vItems.get(MenuIdx - 1);  // index start at 0
            			vMenu = vMenu.clone();
            			vMenu.SetAmount(MenuAmount); // 0-99
            			if (vMenu.GetAmount() > 0) {
            				Cart.put(vMenu.GetName(), vMenu); // set to cart.
            				System.out.println(">> Update in your cart successfully. " + (vMenu.GetAmount() == 0 ? "" : String.format("(x%d)", vMenu.GetAmount())));
            			} else {
            				Cart.remove(vMenu.GetName()); // remove out from cart.
            				System.out.println(">> Remove in your cart successfully.");
            			}
            		} catch (NumberFormatException | CloneNotSupportedException e) {
            			System.out.println(">> [Error: your input amount is invaild for number format] (Skipped Process)");
            		}
            		Console.Pause();
        		}
        	} else {
        		StillInStoresPage = false;
        		break;
        	}
    	} while (StillInStoresPage);
	}
	public void Start() {
		boolean Running = true;
		do {
			Console.Clear();
			System.out.println("===========================");
	        System.out.println("   Welcome to Ninja Food  ");
	        System.out.println("===========================");
	        System.out.println("1. Food order");
	        System.out.println("2. Food pickup");
	        System.out.println("3. Nearby Shop");
	        System.out.println("4. ;Exit");
	        System.out.print("Please choose your menu : ");
	        int UserChoice = Console.PromptInteger("Please choose your menu", 1, 4);
	        Console.Clear();
	        switch (UserChoice) {
	            case 1:
	            	FoodPage(true);
	                break;
	            case 2:
	            	FoodPage(false);
	                break;
	            case 3:
	            	Console.Clear();
	            	try {
		            	double Range = Double.parseDouble(Console.PromptString("// Input your maximum range", "km"));
		            	Console.Clear();
		            	System.out.println(String.format("List of store based on kilometer. (< %.2f)", Range));
		            	for (Store v: Stores) {
	            			if (v.Distance() < Range) {
	            				System.out.println(String.format(" - %s (%.2f km)", v.Name(), v.Distance()));
	            			}
		            	}
	            	} catch (NumberFormatException | NullPointerException e) {
	            		System.out.println(">> [Error: your input amount is invaild for number format] (Skipped Process)");
	            	}
	                Console.Pause();
	                break;
	            case 4:
	            default:
	            	Console.Close();
	            	break;

	        }
		} while (!Console.Suspended);
	}
}