package org.example;

import java.math.BigDecimal;
import java.util.*;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static Map<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }

            String command = scanner.nextLine().trim();

            if (currentUser == null) {
                processLoginCommand(command);
            } else {
                processMainCommand(command);
                continue;
            }

            if (command.equals("3")) {
                System.out.println("Выход из приложения.");
                break;
            }
        }

        scanner.close();
    }

    private static void showLoginMenu() {
        System.out.println("--- Система управления личными финансами ---");
        System.out.println("1. Войти");
        System.out.println("2. Зарегистрироваться");
        System.out.println("3. Выход");
    }

    private static void processLoginCommand(String command) {
        switch (command) {
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            case "3":
                break;
            default:
                System.out.println("Неверная команда. Попробуйте ещё раз");
                break;
        }
    }

    private static void register() {
        System.out.println("Введите логин: ");
        String username = scanner.nextLine().trim();

        if (users.containsKey(username)) {
            System.out.println("Имя пользователя уже занято. Пожалуйста, выберите другое имя.");
            return;
        }

        System.out.println("Введите пароль: ");
        String password = scanner.nextLine().trim();

        Wallet wallet = new Wallet();
        User user = new User(username, password, wallet);
        users.put(username, user);

        System.out.println("Вы зарегистрированы под именем " + username);
        currentUser = user;
    }

    private static void login() {
        System.out.println("Введите логин: ");
        String username = scanner.nextLine().trim();

        if (!users.containsKey(username)) {
            System.out.println("Пользователь не найден. Пожалуйста, зарегистрируйтесь.");
            return;
        }

        System.out.println("Введите пароль: ");
        String password = scanner.nextLine().trim();

        User user = users.get(username);

        if (user.getPassword().equals(password)) {
            System.out.println("Вы вошли под именем пользователя " + username);
            currentUser = user;
        } else {
            System.out.println("Неверный пароль. Попробуйте снова.");
        }
    }

    private static void showMainMenu() {
        System.out.println("--- Доступные операции ---");
        System.out.println("1. Установить бюджет");
        System.out.println("2. Добавить доход");
        System.out.println("3. Добавить расход");
        System.out.println("4. Посмотреть сводку");
        System.out.println("5. Перевод средств");
        System.out.println("6. Выйти из учетной записи");
        System.out.println("7. Завершить работу приложения");
    }

    private static void processMainCommand(String command) {
        switch (command) {
            case "1":
                setBudget();
                break;
            case "2":
                addIncome();
                break;
            case "3":
                addExpense();
                break;
            case "4":
                viewSummary();
                break;
            case "5":
                transferFunds();
                break;
            case "6":
                logout();
                break;
            case "7":
                break;
            default:
                System.out.println("Неверная команда. Попробуйте еще раз.");
                break;
        }
    }

    private static void addIncome() {
        System.out.println("Введите сумму дохода: ");
        BigDecimal amount = getValidAmount();
        if (amount == null) return;

        System.out.println("Введите категорию дохода: ");
        String category = scanner.nextLine().trim();

        currentUser.getWallet().addIncome(amount, category);
        System.out.println("Доход успешно добавлен.");
    }

    private static void addExpense() {
        System.out.println("Введите сумму расхода: ");
        BigDecimal amount = getValidAmount();
        if (amount == null) return;

        System.out.println("Введите категорию расхода: ");
        String category = scanner.nextLine().trim();

        currentUser.getWallet().addExpense(amount, category);
        checkBudgetLimit(category, amount);
        System.out.println("Расход успешно добавлен.");
    }

    private static void setBudget() {
        System.out.println("Введите категорию для бюджета: ");
        String category = scanner.nextLine().trim();

        System.out.println("Введите сумму бюджета для категории " + category + ": ");
        BigDecimal amount = getValidAmount();
        if (amount == null) return;

        currentUser.getWallet().setBudget(category, amount);
        System.out.println("Бюджет успешно установлен для " + category + ".");
    }

    private static void viewSummary() {
        System.out.println("--- Финансовая сводка ---");
        Wallet wallet = currentUser.getWallet();

        System.out.println("Общий доход: " + wallet.getTotalIncome());
        System.out.println("Общий расход: " + wallet.getTotalExpenses());
        System.out.println("Баланс: " + wallet.getBalance());

        System.out.println("Доход по категориям:");
        wallet.getIncomeByCategory().forEach((category, amount) -> System.out.println(category + ": " + amount));

        System.out.println("Расход по категориям:");
        wallet.getExpensesByCategory().forEach((category, amount) -> System.out.println(category + ": " + amount));

        System.out.println("Состояние бюджета:");
        wallet.getBudgets().forEach((category, budget) -> {
            BigDecimal expenses = wallet.getExpensesByCategory().getOrDefault(category, BigDecimal.ZERO);
            BigDecimal remaining = budget.subtract(expenses);
            System.out.println(category + ": Бюджет = " + budget + ", Расходы = " + expenses + ", Остаток = " + remaining);
        });

        if (wallet.getTotalExpenses().compareTo(wallet.getTotalIncome()) > 0) {
            System.out.println("ВНИМАНИЕ: Расходы превышают доходы!");
        }
    }

    private static void transferFunds() {
        System.out.println("Логин получателя: ");
        String recipientUsername = scanner.nextLine().trim();

        if (!users.containsKey(recipientUsername)) {
            System.out.println("Пользователь с таким именем не найден.");
            return;
        }

        if (recipientUsername.equals(currentUser.getUsername())) {
            System.out.println("Нельзя перевести средства самому себе.");
            return;
        }

        System.out.println("Сумма перевода: ");
        BigDecimal amount = getValidAmount();
        if (amount == null) return;

        if (currentUser.getWallet().getBalance().compareTo(amount) < 0) {
            System.out.println("Недостаточно средств на балансе.");
            return;
        }

        User recipient = users.get(recipientUsername);
        currentUser.getWallet().addExpense(amount, "Перевод пользователю " + recipientUsername);
        recipient.getWallet().addIncome(amount, "Перевод от пользователя " + currentUser.getUsername());

        System.out.println("Перевод успешно выполнен.");
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Вы вышли из системы.");
    }

    private static BigDecimal getValidAmount() {
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Сумма должна быть положительной. Попробуйте снова.");
                return null;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат суммы. Введите число.");
            return null;
        }
    }

    private static void checkBudgetLimit(String category, BigDecimal amount) {
        BigDecimal budget = currentUser.getWallet().getBudgets().get(category);
        if (budget != null) {
            BigDecimal expenses = currentUser.getWallet().getExpensesByCategory().getOrDefault(category, BigDecimal.ZERO);
            if (expenses.add(amount).compareTo(budget) > 0) {
                System.out.println("ВНИМАНИЕ: Превышен лимит бюджета для категории " + category + "!");
            }
        } else {
            System.out.println("Вы не установили бюджет для категории " + category);
        }
    }

}