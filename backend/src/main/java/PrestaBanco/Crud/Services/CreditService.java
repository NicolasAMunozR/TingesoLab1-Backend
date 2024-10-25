package PrestaBanco.Crud.Services;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.util.Map;
import java.time.Period;
import org.springframework.stereotype.Service;
import PrestaBanco.Crud.Entities.CreditEntity;
import PrestaBanco.Crud.Entities.UserEntity;
import PrestaBanco.Crud.Repositories.CreditRepository;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class CreditService {

    private final CreditRepository creditRepository;
    private final UserService userService;

    // Constructor
    public CreditService(CreditRepository creditRepository, UserService userService) {
        this.creditRepository = creditRepository;
        this.userService = userService;
    }

    /**
     * Save a loan in the database.
     * @param credit A CreditEntity with the data of the loan to save.
     * @return A CreditEntity with the data of the loan saved.
     */
    public CreditEntity saveCredit(CreditEntity credit) {
        return creditRepository.save(credit);
    }

    /**
     * Method that allows to know the status of the loan application.
     * @param credit A CreditEntity with the data of the loan to know the status of the application.
     * @return A CreditEntity with the status of the loan application.
     */
    public CreditEntity applicationStatus(CreditEntity credit) {
        String typeOfLoan = credit.getTypeOfLoan();
        if (typeOfLoan.contains("Primera Vivienda")) {
            if (credit.getProofOfIncome() != null && credit.getAppraisalCertificate() != null && credit.getCreditHistory() != null) {
                credit.setApplicationStatus("En revisión");
            } else {
                credit.setApplicationStatus("Pendiente de documentación");
            }
        } else if (typeOfLoan.contains("Segunda Vivienda")) {
            if (credit.getProofOfIncome() != null && credit.getAppraisalCertificate() != null && credit.getDeedOfTheFirstHome() != null && credit.getCreditHistory() != null) {
                credit.setApplicationStatus("En revisión");
            } else {
                credit.setApplicationStatus("Pendiente de documentación");  
            }
        } else if (typeOfLoan.contains("Propiedades Comerciales")) {
            if (credit.getProofOfIncome() != null && credit.getAppraisalCertificate() != null && credit.getFinancialStatusOfTheBusiness() != null && credit.getBusinessPlan() != null) {
                credit.setApplicationStatus("En revisión");
            } else {
                credit.setApplicationStatus("Pendiente de documentación"); 
            }
        } else if (typeOfLoan.contains("Remodelación")) {
            if (credit.getProofOfIncome() != null && credit.getUpdatedAppraisalCertificate() != null && credit.getRemodelingBudget() != null) {
                credit.setApplicationStatus("En revisión");
            } else {
                credit.setApplicationStatus("Pendiente de documentación"); 
            }
        } else {
            return null;
        }
        return creditRepository.save(credit);
    }

    /**
     * Search for a loan in the database.
     * @param userId A Long with the id of the client to search the loan.
     * @return A List with the loans found.
     */
    public CreditEntity findById(Long id) {
        return creditRepository.findById(id).orElse(null);
    }

    /**
     * Method that allows evaluating a loan.
     * @param creditFound A CreditEntity with the data of the loan to evaluate.
     * @return A CreditEntity with the loan evaluated.
     */
    public CreditEntity evaluateCredit(CreditEntity creditFound) {
        if (R1(creditFound) && R2(creditFound) && R3(creditFound) && R4(creditFound) && R5(creditFound) && R6(creditFound)) {
            String status = R7(creditFound);
            if (status.contains("Aprobado")) {
                creditFound.setApplicationStatus("Pre-aprobado");
                return creditRepository.save(creditFound);
            } else if (status.contains("Revisión")) {
                creditFound.setApplicationStatus("En evaluación");
                return creditRepository.save(creditFound);
            } else {
                creditFound.setApplicationStatus("Rechazada");
                return creditRepository.save(creditFound);
            }
        } else {
            creditFound.setApplicationStatus("Rechazada");
            return creditRepository.save(creditFound);
        }
    }

    /**
     * Method that calculates the average income of a client.
     * @param creditFound A CreditEntity with the data of the client to calculate the average income.
     * @return A double with the average income of the client.
     */
    public double averageIncome(CreditEntity creditFound) {
        String monthlyIncome = creditFound.getMonthlyIncome();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] entries = monthlyIncome.split(",");
        Map<LocalDate, Integer> incomePerMonth = new HashMap<>();
        for (String entry : entries) {
            String[] parts = entry.split(" ");
            if (parts.length != 2) {
                System.err.println("Invalid format for entry: " + entry);
                continue; 
            }
            String dateString = parts[0].trim(); 
            int amount = 0;
            try {
                amount = Integer.parseInt(parts[1]); 
            } catch (NumberFormatException e) {
                System.err.println("Error parsing amount: " + parts[1] + " - " + e.getMessage());
                continue; 
            }
            try {
                LocalDate date = LocalDate.parse(dateString, formatter);
                incomePerMonth.put(date, amount);
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date: " + dateString + " - " + e.getMessage());
            }
        }
        LocalDate currentDate = LocalDate.now();
        int totalIncome = 0;
        int countedMonths = 0;
        for (Map.Entry<LocalDate, Integer> entry : incomePerMonth.entrySet()) {
            LocalDate date = entry.getKey();
            if (!date.isBefore(currentDate.minusMonths(12))) {
                totalIncome += entry.getValue();
                countedMonths++;
            }
        }
        return countedMonths > 0 ? (double) totalIncome / countedMonths : 0;
    }

    /**
     * Method that allows evaluating a loan.
     * @param credit A CreditEntity with the data of the loan to evaluate.
     * @return A Boolean with the result of the loan evaluation.
     */
    public Boolean R1(CreditEntity creditFound) {
        int amount1 = creditFound.getRequestedAmount();
        int term = creditFound.getLoanTerm();
        Double annualInterestRate = creditFound.getAnnualInterestRate();
        int monthlyFee = userService.simulation(amount1, term, annualInterestRate);
        double averageIncome = averageIncome(creditFound);
        double FeeIncomeRatio = ((double) monthlyFee / averageIncome) * 100;
        if (FeeIncomeRatio <= 0.35) {
            return true;
        }
        return false;
    }

    /**
     * Method that allows evaluating a loan.
     * @param credit A CreditEntity with the data of the loan to evaluate.
     * @return A Boolean with the result of the loan evaluation.
     * * */
    public Boolean R2(CreditEntity creditFound) {
        return creditFound.getCreditsHistory() != null ? creditFound.getCreditsHistory() : false;
    }

    /**
     * Method that allows evaluating a loan.
     * @param credit A CreditEntity with the data of the loan to evaluate.
     * @return A Boolean with the result of the loan evaluation.
     */
    public Boolean R3(CreditEntity creditFound) {
        UserEntity user = userService.findById(creditFound.getUserId());
        Integer jobSeniority = user.getJobSeniority();
        if (jobSeniority >= 1) {
            return true;
        }
        return false;
    }

    /**
     * Method that allows evaluating a loan.
     * @param credit A CreditEntity with the data of the loan to evaluate.
     * @return A Boolean with the result of the loan evaluation.
     */
    public Boolean R4(CreditEntity creditFound) {
        String debt = creditFound.getMonthlyDebt();
        if(debt != null){
            String[] parts = debt.split(",");
            int sum = 0;
            for (String part : parts) {
                sum += Integer.parseInt(part);
            }
            sum += userService.simulation(creditFound.getRequestedAmount(), creditFound.getLoanTerm(), creditFound.getAnnualInterestRate());
            if (sum > (0.5 * averageIncome(creditFound))) {
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Method that allows evaluating a loan.
     * @param credit A CreditEntity with the data of the loan to evaluate.
     * @return A Boolean with the result of the loan evaluation.
     */
    public Boolean R5(CreditEntity creditFound) {
        String typeOfLoan = creditFound.getTypeOfLoan();
        int propertyAmount = creditFound.getPropertyAmount();
        int requestedAmount = creditFound.getRequestedAmount();
        if (typeOfLoan.contains("Primera Vivienda")) {
            if (requestedAmount > (0.8 * propertyAmount)) {
                return false;
            } else {
                return true;
            }
        } else if (typeOfLoan.contains("Segunda Vivienda")) {
            if (requestedAmount > (0.7 * propertyAmount)) {
                return false;
            } else {
                return true;
            }
        } else if (typeOfLoan.contains("Propiedades Comerciales")) {
            if (requestedAmount > (0.6 * propertyAmount)) {
                return false;
            } else {
                return true;
            }
        } else if (typeOfLoan.contains("Remodelación")) {
            if (requestedAmount > (0.5 * propertyAmount)) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that allows evaluating a loan.
     * @param credit A CreditEntity with the data of the loan to evaluate.
     * @return A Boolean with the result of the loan evaluation.
     */
    public Boolean R6(CreditEntity creditFound) {
        UserEntity user = userService.findById(creditFound.getUserId());
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(user.getBirthdate(), currentDate).getYears();
        if (age >= 70) {
            return false;
        }
        return true;
    }

    /**
     * Method that allows evaluating a loan.
     * @param credit A CreditEntity with the data of the loan to evaluate.
     * @return A String with the result of the loan evaluation.
     */
    public String R7(CreditEntity creditFound) {
        int countTrue = 0;
        if (R71(creditFound)) {
            countTrue++;
        }
        if (R72(creditFound)) {
            countTrue++;
        }
        if (R73(creditFound)) {
            countTrue++;
        }
        if (R74(creditFound)) {
            countTrue++;
        }
        if (R75(creditFound)) {
            countTrue++;
        }
        if (countTrue == 5) {
            return "Aprobado";
        }
        else if (countTrue >= 3 && countTrue <= 4) {
            return "Revisión";
        }
        else {
            return "Rechazado";
        }
    }

    /**
     * Method that allows evaluating a saving.
     * @param credit A CreditEntity with the data of the saving to evaluate.
     * @return A Boolean with the result of the saving evaluation.
     */  
    public Boolean R71(CreditEntity creditFound) {
        UserEntity user = userService.findById(creditFound.getUserId());
        int currentSavingsBalance = user.getCurrentSavingsBalance();
        int requestedAmount = creditFound.getRequestedAmount();
        if (currentSavingsBalance>=(0.1*requestedAmount)) {
            return true;
        }
        return false;
    }

    /**
     * Method that allows evaluating a saving.
     * @param credit A CreditEntity with the data of the saving to evaluate.
     * @return A Boolean with the result of the saving evaluation.
     */
    public Boolean R72(CreditEntity creditFound) {
        UserEntity user = userService.findById(creditFound.getUserId());
        String savingsAccount = user.getSavingsAccountHistory();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] entries = savingsAccount.split(",");
         LocalDate currentDate = LocalDate.now();
         boolean foundValidEntry = false;
         int previousBalance = 0;
         boolean noSignificantWithdrawals = true;
         for (String entry : entries) {
             String[] parts = entry.split(" ");
             LocalDate date = LocalDate.parse(parts[0], formatter);
             int currentBalance = Integer.parseInt(parts[1]);
             if (!date.isBefore(currentDate.minusMonths(12))) {
                 if (!foundValidEntry) {
                     previousBalance = currentBalance;
                     foundValidEntry = true;
                 } else {
                     double threshold = previousBalance * 0.5;
                     if (currentBalance < threshold) {
                         noSignificantWithdrawals = false;
                         break;  
                     }
                     previousBalance = currentBalance;
                 }
             }
         }
 
         if (noSignificantWithdrawals && foundValidEntry) {
            return true;
         } else if (!foundValidEntry) {
            return true;
         } else {
            return false;
         }
    }

    /**
     * Method that allows evaluating a saving.
     * @param credit A CreditEntity with the data of the saving to evaluate.
     * @return A Boolean with the result of the saving evaluation.
     */
    public Boolean R73(CreditEntity creditFound) {
        UserEntity user = userService.findById(creditFound.getUserId());
        String depositAccount = user.getDepositAccount();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] entries = depositAccount.split(",");
        LocalDate currentDate = LocalDate.now();
        double totalDeposits = 0;
        int depositCount = 0;
        for (String entry : entries) {
            String[] parts = entry.split(" ");
            LocalDate depositDate = LocalDate.parse(parts[0], formatter);
            int amount = Integer.parseInt(parts[1]);
            if (!depositDate.isBefore(currentDate.minusMonths(12))) {
                totalDeposits += amount;
                depositCount++;
            }
        }
        double minimumRequiredDeposit = 0.05 * averageIncome(creditFound);
        boolean areDepositsRegular = depositCount > 0 && (depositCount >= 12 || (depositCount >= 3 && totalDeposits >= minimumRequiredDeposit));
        if (areDepositsRegular && totalDeposits >= minimumRequiredDeposit) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Method that allows evaluating a saving.
     * @param credit A CreditEntity with the data of the saving to evaluate.
     * @return A Boolean with the result of the saving evaluation.
     */
    public Boolean R74(CreditEntity creditFound) {
        UserEntity user = userService.findById(creditFound.getUserId());
        LocalDate creationDate = user.getCreationDate();
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(creationDate, currentDate).getYears();
        if (age < 2 && user.getCurrentSavingsBalance() >= (0.2 * userService.simulation(creditFound.getRequestedAmount(), creditFound.getLoanTerm(), creditFound.getAnnualInterestRate()))) {
            return true;
        }
        else if (age >= 2 && user.getCurrentSavingsBalance() >= (0.1 * userService.simulation(creditFound.getRequestedAmount(), creditFound.getLoanTerm(), creditFound.getAnnualInterestRate()))) {
            return true;
        }
        return false;
    }

    /**
     * Method that allows evaluating a saving.
     * @param credit A CreditEntity with the data of the saving to evaluate.
     * @return A Boolean with the result of the saving evaluation.
     */
    public Boolean R75(CreditEntity creditFound) {
        UserEntity user = userService.findById(creditFound.getUserId());
        String withdrawalAccount = user.getWithdrawalAccount();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] entries = withdrawalAccount.split(",");
        LocalDate currentDate = LocalDate.now();
        boolean noLargeWithdrawals = true;
        int previousBalance = 0;
        for (String entry : entries) {
            String[] parts = entry.split(" ");
            LocalDate transactionDate = LocalDate.parse(parts[0], formatter);
            int currentBalance = Integer.parseInt(parts[1]);
            if (!transactionDate.isBefore(currentDate.minusMonths(6))) {
                if (previousBalance == 0) {
                    previousBalance = currentBalance;
                } else {
                    double threshold = previousBalance * 0.3;
                    if (previousBalance - currentBalance > threshold) {
                        noLargeWithdrawals = false;
                        break;  
                    }
                    previousBalance = currentBalance;
                }
            }
        }
        if (noLargeWithdrawals) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method that calculates the total cost of a loan.
     * @param credit A CreditEntity with the data of the loan to calculate the total cost.
     */
    public void totalCost(CreditEntity credit) {
        int averageIncome = userService.simulation(credit.getRequestedAmount(), credit.getLoanTerm(), credit.getAnnualInterestRate());
        double lienInsurance = credit.getLienInsurance() * credit.getRequestedAmount();
        double administrationFee = credit.getAdministrationFee() * credit.getRequestedAmount();
        double monthlyCost = averageIncome + lienInsurance + 20000;
        credit.setRemainingMonthlyInstallments((int)monthlyCost);
        credit.setFirstInstallment((int)(monthlyCost + administrationFee));
        credit.setTotalAmount((int)(monthlyCost * credit.getLoanTerm() * 12 + administrationFee));
    }

    /**
     * Method that allows to make a disbursement.
     * @param credit A CreditEntity with the data of the loan to disburse.
     */
    public void Disbursement(CreditEntity credit) {
        UserEntity user = userService.findById(credit.getUserId());
        int currentSavingsBalance = user.getCurrentSavingsBalance();
        int requestedAmount = credit.getRequestedAmount();
        user.setCurrentSavingsBalance(currentSavingsBalance + requestedAmount);
        String deposit = String.valueOf(requestedAmount);
        String dateDeposit = LocalDate.now().toString();
        String depositInitial = user.getDepositAccount();
        if (depositInitial.length() <= 0) {
            depositInitial =  dateDeposit + " " + deposit;
        } else {
            depositInitial = depositInitial + "," + dateDeposit + " " + deposit;  
        }
        currentSavingsBalance += requestedAmount;
        String savingsAccountHistory = user.getSavingsAccountHistory();
        if (savingsAccountHistory.length() <= 0) {
            savingsAccountHistory = dateDeposit + " " + String.valueOf(currentSavingsBalance);
        } else {
            savingsAccountHistory = savingsAccountHistory + "," + dateDeposit + " " + String.valueOf(currentSavingsBalance);
        }
        user.setSavingsAccountHistory(savingsAccountHistory);
        user.setDepositAccount(depositInitial);
        userService.saveUser(user);
    }

    /**
     * Method that allows to obtain all the loans.
     * @return A List with all the loans.
     */
    public ArrayList<CreditEntity> getAllCredits() {
        return (ArrayList<CreditEntity>) creditRepository.findAll();
    }

    /**
     * Method that allows to delete a loan.
     * @param credit A CreditEntity with the data of the loan to delete.
     */
    public void deleted(UserEntity user) {
        ArrayList<CreditEntity> credits = (ArrayList<CreditEntity>) creditRepository.findAll();
        for (CreditEntity credit : credits) {
            if (credit.getUserId() == user.getId()) {
                creditRepository.delete(credit);
            }
        }
    }
}