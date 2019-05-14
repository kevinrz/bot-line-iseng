package com.bot.woyhemat.handler;

import com.bot.woyhemat.database.*;

import java.util.Calendar;
import java.util.List;

/**
 * HistoriHandler
 */
public class HistoriHandler {

    public String getHistoriPengeluaran(String userId,
                                        UserRepository userRepository,
                                        ExpenditureRepository expenditureRepository,
                                        DebtRepository debtRepository) {

        User user = userRepository.findByUsername(userId);
        if (user == null) {
            return "Maaf, anda belum terdaftar";
        }

        List<Expenditure> userExpense = expenditureRepository.findByUser(user);

        if (userExpense.isEmpty()) {
            return "Anda belum memiliki pengeluaran";
        }

        Expenditure expense = userExpense.get(0);

        Calendar expenseDate = Calendar.getInstance();
        expenseDate.setTime(expense.getTimestamp());

        int expenseMonth = expenseDate.get(Calendar.MONTH);
        String reply = "Histori Pengeluaran:\n";
        int expensePerMonth = expense.getAmount();

        for (int i = 1; i < userExpense.size(); i++) {
            Expenditure nextExpense = userExpense.get(i);

            Calendar nextExpenseDate = Calendar.getInstance();
            nextExpenseDate.setTime(nextExpense.getTimestamp());

            int nextExpenseMonth = nextExpenseDate.get(Calendar.MONTH);

            if (expenseMonth == nextExpenseMonth) {
                expensePerMonth += nextExpense.getAmount();
                if (i == userExpense.size() - 1) {
                    reply += "bulan ke-" + (expenseMonth + 1) + ": " + expensePerMonth + "\n";
                    expensePerMonth = 0;
                }
            } else {
                reply += "bulan ke-" + (expenseMonth + 1) + ": " + expensePerMonth + "\n";
                expensePerMonth = 0;
            }

            expenseMonth = nextExpenseMonth;
        }

        return reply;
    }
}