package PrestaBanco.Crud.Services;

import java.util.ArrayList;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import PrestaBanco.Crud.Repositories.UserRepository;
import PrestaBanco.Crud.Entities.UserEntity;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private CreditService creditService;

    /**
     * Save a client in the database.
     * @param user A UserEntity with the data of the client to save.
     * @return A UserEntity with the data of the client saved.
     */
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    /**
     * Search for a client in the database.
     * @param email A String with the email of the client to search.
     * @return A UserEntity with the data of the client found.
     */
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Search for a client in the database.
     * @param id A Long with the id of the client to search.
     * @return A UserEntity with the data of the client found.
     */
    public UserEntity findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Delete a client from the database.
     * @param user A UserEntity with the data of the client to delete.
     */
    public void deleteUser(UserEntity user) {
        userRepository.delete(user);

        if (creditService.getAllCredits().size() > 0) {
            creditService.deleted(user);
        }
    }

    /**
     * Simulate a loan.
     * @param amount An int with the amount of the loan.
     * @param type A String with the type of loan.
     * @param term An int with the term of the loan.
     * @param interestRate A double with the interest rate of the loan.
     * @return An int with the monthly payment of the loan.
     */
    public int simulation(int amount, int term, double interestRate) {
        if (term == 0) {
            return amount; 
        }
        double monthlyInterestRate = interestRate / 100 / 12; 
        int termInMonths = term * 12; 
        double monthlyPayment = amount * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, termInMonths)) /
                (Math.pow(1 + monthlyInterestRate, termInMonths) - 1);
        return (int) Math.round(monthlyPayment); 
    }

    /**
     * List all clients in the database.
     * @return An ArrayList with all the clients found.
     */
    public ArrayList<UserEntity> findAll() {
        return (ArrayList<UserEntity>) userRepository.findAll();  
    }

    /**
     * Search for a client in the database.
     * @param email A String with the email of the client to search.
     * @return A UserEntity with the data of the client found.
     */
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Search for a client in the database.
     * @param identifyingDocument A String with the identifying document of the client to search.
     * @return A UserEntity with the data of the client found.
     */
    public UserEntity findByIdentifyingDocument(String identifyingDocument) {
        return userRepository.findByIdentifyingDocument(identifyingDocument);
    }

    /**
     * Search for a client in the database.
     * @param id A Long with the id of the client to search.
     * @return A UserEntity with the data of the client found.
     */
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Deposit money into the account.
     * @param user A UserEntity with the user data.
     * @param depositAccount An int with the amount to deposit.
     * @return A UserEntity with the updated user data.
     */
    public UserEntity deposit(UserEntity user, int depositAccount) {
        String deposit = String.valueOf(depositAccount);
        String dateDeposit = LocalDate.now().toString();
        String depositInitial = user.getDepositAccount() != null ? user.getDepositAccount() : "";
        if (depositInitial.length() <= 0) {
            depositInitial = dateDeposit + " " + deposit;
        } else {
            depositInitial = depositInitial + "," + dateDeposit + " " + deposit;
        }
        int currentSavingsBalance = user.getCurrentSavingsBalance();
        currentSavingsBalance += depositAccount;
        String savingsAccountHistory = user.getSavingsAccountHistory() != null ? user.getSavingsAccountHistory() : "";
        if (savingsAccountHistory.length() <= 0) {
            savingsAccountHistory = dateDeposit + " " + String.valueOf(currentSavingsBalance);
        } else {
            savingsAccountHistory = savingsAccountHistory + "," + dateDeposit + " " + String.valueOf(currentSavingsBalance);
        }
        user.setSavingsAccountHistory(savingsAccountHistory);
        user.setCurrentSavingsBalance(currentSavingsBalance);
        user.setDepositAccount(depositInitial);
        return userRepository.save(user);
    }

    /**
     * Withdraw money from the account.
     * @param user A UserEntity with the user data.
     * @param withdrawalAccount An int with the amount to withdraw.
     * @return A UserEntity with the updated user data.
     */
    public UserEntity withdrawal(UserEntity user, int withdrawalAccount) {
        String withdrawal = String.valueOf(withdrawalAccount);
        String dateWithdrawal = LocalDate.now().toString();
        String withdrawalInitial = user.getWithdrawalAccount() != null ? user.getWithdrawalAccount() : "";
        if (withdrawalInitial.length() <= 0) {
            withdrawalInitial = dateWithdrawal + " " + withdrawal;
        } else {
            withdrawalInitial = withdrawalInitial + "," + dateWithdrawal + " " + withdrawal;
        }
        int currentSavingsBalance = user.getCurrentSavingsBalance();
        currentSavingsBalance -= withdrawalAccount;
        String savingsAccountHistory = user.getSavingsAccountHistory() != null ? user.getSavingsAccountHistory() : "";
        if (savingsAccountHistory.length() <= 0) {
            savingsAccountHistory = dateWithdrawal + " " + String.valueOf(currentSavingsBalance);
        } else {
            savingsAccountHistory = savingsAccountHistory + "," + dateWithdrawal + " " + String.valueOf(currentSavingsBalance);
        }
        user.setSavingsAccountHistory(savingsAccountHistory);
        user.setCurrentSavingsBalance(currentSavingsBalance);
        user.setWithdrawalAccount(withdrawalInitial);
        return userRepository.save(user);
    }
}

