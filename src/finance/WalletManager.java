package finance;

import java.util.ArrayList;

public class WalletManager {

    public int balance    = 0;
    public int totalSpent = 0;

    // expense category totals
    public int foodCosts = 0;
    public int vetCosts  = 0;
    public int toyCosts  = 0;

    public ArrayList<Transaction> history = new ArrayList<>();

    /** Add income: increases balance, records a positive transaction. */
    public void earn(String description, int amount) {
        balance += amount;
        history.add(new Transaction(description, amount));
    }

    /**
     * Spend money: decreases balance, updates totalSpent, records a negative transaction.
     * Returns false without changing state if balance is insufficient.
     */
    public boolean spend(String description, int amount) {
        if (balance < amount) return false;
        balance    -= amount;
        totalSpent += amount;
        history.add(new Transaction(description, -amount));
        return true;
    }
}
