package org.example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Wallet {

    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal totalExpenses = BigDecimal.ZERO;
    private Map<String, BigDecimal> incomeByCategory = new HashMap<>();
    private Map<String, BigDecimal> expensesByCategory = new HashMap<>();
    private Map<String, BigDecimal> budgets = new HashMap<>();

    public void addIncome(BigDecimal amount, String category) {
        totalIncome = totalIncome.add(amount);
        incomeByCategory.put(category, incomeByCategory.getOrDefault(category, BigDecimal.ZERO).add(amount));
    }

    public void addExpense(BigDecimal amount, String category) {
        totalExpenses = totalExpenses.add(amount);
        expensesByCategory.put(category, expensesByCategory.getOrDefault(category, BigDecimal.ZERO).add(amount));
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getBalance() {
        return totalIncome.subtract(totalExpenses);
    }

    public Map<String, BigDecimal> getIncomeByCategory() {
        return incomeByCategory;
    }

    public Map<String, BigDecimal> getExpensesByCategory() {
        return expensesByCategory;
    }

    public Map<String, BigDecimal> getBudgets() {
        return budgets;
    }

    public void setBudget(String category, BigDecimal amount) {
        budgets.put(category, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(totalIncome, wallet.totalIncome) && Objects.equals(totalExpenses, wallet.totalExpenses) &&
                Objects.equals(incomeByCategory, wallet.incomeByCategory) &&
                Objects.equals(expensesByCategory, wallet.expensesByCategory) &&
                Objects.equals(budgets, wallet.budgets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalIncome, totalExpenses, incomeByCategory, expensesByCategory, budgets);
    }
}
