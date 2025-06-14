package com.example.fttry;

public class InputValidator {
    public static boolean validateTransaction(String amount, String category) {
        if (amount == null || amount.trim().isEmpty() ||
                category == null || category.trim().isEmpty()) {
            return false;
        }

        try {
            double amt = Double.parseDouble(amount);
            return amt > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*public static String formatAmount(String amount) {
        try {
            return String.format("%.2f", Double.parseDouble(amount));
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }*/
}