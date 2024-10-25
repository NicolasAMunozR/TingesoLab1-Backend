package PrestaBanco.Crud.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import PrestaBanco.Crud.Entities.CreditEntity;
import PrestaBanco.Crud.Services.CreditService;

@RestController
@RequestMapping("/credits")
@CrossOrigin(origins = "*")
public class CreditController {

    @Autowired
    private CreditService creditService;

    /**
     * Controller that allows applying for a loan.
     * @param id A Long with the client's id to apply for the loan.
     * @param body A Map with the data of the loan to apply for.
     * @return A UserEntity with the client's data updated.
     */
    @PostMapping(value = "/", consumes = {"multipart/form-data"})
    public ResponseEntity<CreditEntity> applyForCredit(@RequestParam("userId") Long id,
    @RequestParam(value = "proofOfIncome", required = false) MultipartFile file,
    @RequestParam(value = "appraisalCertificate", required = false) MultipartFile file2,
    @RequestParam(value = "creditHistory", required = false) MultipartFile file3,
    @RequestParam(value = "deedOfTheFirstHome", required = false) MultipartFile file4,
    @RequestParam(value = "financialStatusOfTheBusiness", required = false) MultipartFile file5,
    @RequestParam(value = "businessPlan", required = false) MultipartFile file6,
    @RequestParam(value = "remodelingBudget", required = false) MultipartFile file7,
    @RequestParam(value = "updatedAppraisalCertificate", required = false) MultipartFile file8,
    @RequestParam("monthlyIncome1") String monthlyIncome1,
    @RequestParam("monthlyIncome2") String monthlyIncome2,
    @RequestParam("monthlyIncome3") String monthlyIncome3,
    @RequestParam("monthlyIncome4") String monthlyIncome4,
    @RequestParam("monthlyIncome5") String monthlyIncome5,
    @RequestParam("monthlyIncome6") String monthlyIncome6,
    @RequestParam("monthlyIncome7") String monthlyIncome7,
    @RequestParam("monthlyIncome8") String monthlyIncome8,
    @RequestParam("monthlyIncome9") String monthlyIncome9,
    @RequestParam("monthlyIncome10") String monthlyIncome10,
    @RequestParam("monthlyIncome11") String monthlyIncome11,
    @RequestParam("monthlyIncome12") String monthlyIncome12,
    @RequestParam("requestedAmount") int requestedAmount,
    @RequestParam("loanTerm") int loanTerm,
    @RequestParam("annualInterestRate") double annualInterestRate,
    @RequestParam("typeOfLoan") String typeOfLoan,
    @RequestParam("creditsHistory") Boolean creditsHistory,
    @RequestParam("monthlyDebt") String monthlyDebt,
    @RequestParam("propertyAmount") int propertyAmount) {
        try {
            // The monthly income is concatenated.
            String monthlyIncome = monthlyIncome1 + "," + monthlyIncome2 + "," + monthlyIncome3 + "," + monthlyIncome4 + "," + monthlyIncome5 + "," + monthlyIncome6 + "," + monthlyIncome7 + "," + monthlyIncome8 + "," + monthlyIncome9 + "," + monthlyIncome10 + "," + monthlyIncome11 + "," + monthlyIncome12;
            // A new loan is created.
            CreditEntity credit = new CreditEntity(id, monthlyIncome, requestedAmount, loanTerm, annualInterestRate, typeOfLoan, creditsHistory, monthlyDebt, propertyAmount);
            // The loan is saved in the database.
            byte[] pdfBytes = null;
            // If the proof of income is not empty.
            if (file != null && !file.isEmpty()) {
                // The proof of income is saved.
                pdfBytes = file.getBytes();
            }
            // The proof of income is saved.
            credit.setProofOfIncome(pdfBytes);
            // The loan is saved in the database.
            byte[] pdfBytes2 = null;
            // If the appraisal certificate is not empty.
            if (file2 != null && !file2.isEmpty()) {
                // The appraisal certificate is saved.
                pdfBytes2 = file2.getBytes();
            }
            // The appraisal certificate is saved.
            credit.setAppraisalCertificate(pdfBytes2);
            // The loan is saved in the database.
            byte[] pdfBytes3 = null;
            // If the credit history is not empty.
            if (file3 != null && !file3.isEmpty()) {
                // The credit history is saved.
                pdfBytes3 = file3.getBytes();
            }
            // The credit history is saved.
            credit.setCreditHistory(pdfBytes3);
            // The loan is saved in the database.
            byte[] pdfBytes4 = null;
            // If the deed of the first home is not empty.
            if (file4 != null && !file4.isEmpty()) {
                // The deed of the first home is saved.
                pdfBytes4 = file4.getBytes();
            }
            // The deed of the first home is saved.
            credit.setDeedOfTheFirstHome(pdfBytes4);
            // The loan is saved in the database.
            byte[] pdfBytes5 = null;
            // If the financial status of the business is not empty.
            if (file5 != null && !file5.isEmpty()) {
                // The financial status of the business is saved.
                pdfBytes5 = file5.getBytes();
            }
            // The financial status of the business is saved.
            credit.setFinancialStatusOfTheBusiness(pdfBytes5);
            // The loan is saved in the database.
            byte[] pdfBytes6 = null;
            // If the business plan is not empty.
            if (file6 != null && !file6.isEmpty()) {
                // The business plan is saved.
                pdfBytes6 = file6.getBytes();
            }
            // The business plan is saved.
            credit.setBusinessPlan(pdfBytes6);
            // The loan is saved in the database.
            byte[] pdfBytes7 = null;
            // If the remodeling budget is not empty.
            if (file7 != null && !file7.isEmpty()) {
                // The remodeling budget is saved.
                pdfBytes7 = file7.getBytes();
            }
            // The remodeling budget is saved.
            credit.setRemodelingBudget(pdfBytes7);
            // The loan is saved in the database.
            byte[] pdfBytes8 = null;
            // If the updated appraisal certificate is not empty.
            if (file8 != null && !file8.isEmpty()) {
                // The updated appraisal certificate is saved.
                pdfBytes8 = file8.getBytes();
            }
            // The updated appraisal certificate is saved.
            credit.setCreditsHistory(creditsHistory);
            // The loan is saved in the database.
            credit.setUpdatedAppraisalCertificate(pdfBytes8);
            // The loan is saved in the database.
            CreditEntity creditSaved = creditService.applicationStatus(credit);
            // The loan is returned.
            return ResponseEntity.ok(creditSaved);
        } catch (Exception e) {
            // If there is an error, return null.
            return null;
        }
    }
    
    /**
     * Method that allows evaluating a loan.
     * @param credit A CreditEntity with the data of the loan to evaluate.
     * @return A CreditEntity with the loan evaluated.
     */
    @PutMapping("/evaluateCredit/{id}")
    public ResponseEntity<CreditEntity> evaluateCredit(@PathVariable Long id) {
        try{
            // The loan is searched in the database.
            CreditEntity creditFound = creditService.findById(id);
            // The loan is evaluated.
            CreditEntity approved = creditService.evaluateCredit(creditFound);
            // The loan is returned.
            return ResponseEntity.ok(approved);
        } 
        catch (Exception e) {
            // If there is an error, return null.    
            return null;
        }
    }

    /**
     * Method that allows updating the status of a loan.
     * @param id A Long with the id of the loan to update.
     * @return A CreditEntity with the loan updated.
     */
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<CreditEntity> updateStatus(@PathVariable Long id) {
        try{
            // The loan is searched in the database.
            CreditEntity credit = creditService.findById(id);
            // If the loan is pending documentation.
            if(credit.getApplicationStatus().equals("Pendiente de documentación")) {
                // The loan is in review.
                credit.setApplicationStatus("En evaluación");
            }
            // If the loan is under review.
            else if(credit.getApplicationStatus().equals("En evaluación")) {
                // The loan is approved.
                credit.setApplicationStatus("Pre-aprobado");
            }
            // If the loan is pre-approved.
            else if (credit.getApplicationStatus().equals("Pre-aprobado")) {
                // The loan is approved.
                credit.setApplicationStatus("Aprobación final");
            }
            // If the loan is finally approved.
            else if (credit.getApplicationStatus().equals("Aprobación final")) {
                // The loan is approved.
                credit.setApplicationStatus("Aprobada");
            }
            // If the loan is approved.
            else if (credit.getApplicationStatus().equals("Aprobada")) {
                // The loan is disbursed.
                credit.setApplicationStatus("Desembolso");
                // The loan is disbursed.
                creditService.Disbursement(credit);
            }
            else {
                // If the loan is rejected.
                return null;
            }
            // The loan is saved in the database.
            CreditEntity creditSaved = creditService.saveCredit(credit);
            // The loan is returned.
            return ResponseEntity.ok(creditSaved);
        }
        catch (Exception e) {
            // If there is an error, return null.
            return null;
        }
    }

    /**
     * Method that allows updating the terms of a loan.
     * @param id A Long with the id of the loan to update.
     * @param lienInsurance A Double with the lien insurance of the loan.
     * @param administrationFee A Double with the administration fee of the loan.
     * @return A CreditEntity with the loan updated.
     */
    @PutMapping("/updateTerms")
    public ResponseEntity<CreditEntity> updateTerms(@RequestParam("userId") Long id,
    @RequestParam("lienInsurance") Double lienInsurance,
    @RequestParam("administrationFee") Double administrationFee) {
        try{
            // The loan is searched in the database.
            CreditEntity credit = creditService.findById(id);
            // The loan is updated.
            credit.setLienInsurance(lienInsurance);
            // The loan is updated.
            credit.setAdministrationFee(administrationFee);
            // The total cost of the loan is calculated.
            creditService.totalCost(credit);
            // The loan is saved in the database.
            CreditEntity creditSaved = creditService.saveCredit(credit);
            // The loan is returned.
            return ResponseEntity.ok(creditSaved);
        }
        catch (Exception e) {
            // If there is an error, return null.
            return null;
        }
    }

    /**
     * Method that allows rejecting the terms of a loan.
     * @param id A Long with the id of the loan to reject.
     * @return A CreditEntity with the loan rejected.
     */
    @PutMapping("/rejectTerms/{id}")
    public ResponseEntity<CreditEntity> rejectTerms(@PathVariable Long id) {
        try{
            // The loan is searched in the database.
            CreditEntity credit = creditService.findById(id);
            // The loan is rejected.
            credit.setApplicationStatus("Cancelada");
            // The loan is saved in the database.
            CreditEntity creditSaved = creditService.saveCredit(credit);
            // The loan is returned.
            return ResponseEntity.ok(creditSaved);
        }
        catch (Exception e) {
            // If there is an error, return null.
            return null;
        }
    }

    /**
     * Controller that allows obtaining all the loans.
     * @return A List with all the loans.
     */
    @GetMapping("/all")
    public ResponseEntity<List<CreditEntity>> getAllCredits() {
        // The loans are searched in the database.
        List<CreditEntity> credits = creditService.getAllCredits();
        // The loans are returned.
        return ResponseEntity.ok(credits);
    }
}
