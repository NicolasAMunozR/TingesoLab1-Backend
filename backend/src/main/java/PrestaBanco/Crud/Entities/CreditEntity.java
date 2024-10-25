package PrestaBanco.Crud.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "credit")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "monthly_income", length = 10000 )
    private String monthlyIncome;

    @Column(name = "credits_history") 
    private Boolean creditsHistory;

    @Column(name = "requested_amount")

    private int requestedAmount;

    @Column(name = "loan_term")
    private int loanTerm;

    @Column(name = "annual_interest_rate")
    private Double annualInterestRate;

    @Column(name = "lien_insurance")
    private Double lienInsurance;

    @Column(name = "administration_fee")  
    private Double administrationFee;

    @Lob
    private byte[] proofOfIncome;

    @Lob
    private byte[] appraisalCertificate;

    @Lob
    private byte[] creditHistory;

    @Lob
    private byte[] deedOfTheFirstHome;

    @Lob
    private byte[] financialStatusOfTheBusiness;

    @Lob
    private byte[] businessPlan;

    @Lob
    private byte[] remodelingBudget;

    @Lob
    private byte[] updatedAppraisalCertificate;

    @Column(name = "application_status")
    private String applicationStatus;

    @Column(name = "first_installment")
    private int firstInstallment;

    @Column(name = "remaining_monthly_installments")
    private int remainingMonthlyInstallments;

    @Column(name = "type_of_loan")
    private String typeOfLoan;

    @Column(name = "monthly_debt") 
    private String monthlyDebt;

    @Column(name = "property_amount")
    private int propertyAmount;

    @Column(name = "total_amount")
    private int totalAmount;


    public CreditEntity(Long userId, String monthlyIncome, int requestedAmount, int loanTerm, double annualInterestRate, String typeOfLoan, Boolean creditsHistory, String monthlyDebt, int propertyAmount) {
        this.userId = userId;
        this.monthlyIncome = monthlyIncome;
        this.requestedAmount = requestedAmount;
        this.loanTerm = loanTerm;
        this.annualInterestRate = annualInterestRate;
        this.typeOfLoan = typeOfLoan;
        this.monthlyDebt = monthlyDebt;
        this.propertyAmount = propertyAmount;
    }
}
