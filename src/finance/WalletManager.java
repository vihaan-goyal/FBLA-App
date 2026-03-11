package finance;

import java.util.ArrayList;

public class WalletManager {


    public ArrayList<Transaction> history = new ArrayList<>();

    public void addTransaction(String description, int amount){
        history.add(new Transaction(description, amount));
    }
}
