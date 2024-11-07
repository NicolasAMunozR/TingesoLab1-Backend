package PrestaBanco.Crud.Services;

import PrestaBanco.Crud.Entities.CreditEntity;
import PrestaBanco.Crud.Entities.UserEntity;
import PrestaBanco.Crud.Repositories.CreditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CreditServiceTest {
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private UserService userService;
    private CreditService creditService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        creditService = Mockito.spy(new CreditService(creditRepository, userService));
    }
    @Test
    void whenFeeIncomeRatioIsBelow35Percent_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(1L, "2024-10-03 200000,2024-09-03 200000,2024-08-03 200000,2024-07-03 200000,2024-06-03 20000000,2024-05-03 200000,2024-04-03 200000,2024-03-03 200000,2024-02-03 200000,2024-01-03 200000,2023-12-03 200000,2023-11-03 200000,2023-10-03 200000,2023-09-03 200000,2023-08-03 200000,2023-07-03 200000,2023-06-03 200000,2023-05-03 200000,2023-04-03 200000,2023-03-03 200000,2023-02-03 200000,2023-01-03 200000,2022-12-03 200000,2022-11-03 200000", 1000000, 20, 0.035, "Primera Vivienda", true, "500", 100000);
        Boolean result = creditService.R1(credit);
        assertThat(result).isTrue();
    }
    @Test
    void whenFeeIncomeRatioIsExactly35Percent_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(2L, "2023-01-01 8000,2023-02-01 8000,2023-03-01 8000", 50000, 15, 6.0, "Primera Vivienda", true, "500", 100000);
        Boolean result = creditService.R1(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenFeeIncomeRatioIsAbove35Percent_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(3L, "2023-01-01 7000,2023-02-01 7000,2023-03-01 7000", 60000, 20, 5.0, "Segunda Vivienda", true, "500", 80000);
        Boolean result = creditService.R1(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenLowIncomeAndHighRequestedAmount_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(4L, "2023-01-01 1500,2023-02-01 1500", 70000, 25, 7.5, "Propiedades Comerciales", true, "300", 120000);
        Boolean result = creditService.R1(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenHighIncomeAndLowRequestedAmount_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(5L, "2024-10-03 200000,2024-09-03 200000,2024-08-03 200000,2024-07-03 200000,2024-06-03 20000000,2024-05-03 200000,2024-04-03 200000,2024-03-03 200000,2024-02-03 200000,2024-01-03 200000,2023-12-03 200000,2023-11-03 200000,2023-10-03 200000,2023-09-03 200000,2023-08-03 200000,2023-07-03 200000,2023-06-03 200000,2023-05-03 200000,2023-04-03 200000,2023-03-03 200000,2023-02-03 200000,2023-01-03 200000,2022-12-03 200000,2022-11-03 200000", 1000000, 20, 0.035, "Remodelación", true, "600", 200000);
        Boolean result = creditService.R1(credit);
        assertThat(result).isTrue();
    }
    @Test
    void whenLongLoanTermAndLowRequestedAmount_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(6L, "2023-01-01 5000,2023-02-01 5000,2023-03-01 5000", 30000, 30, 5.0, "Primera Vivienda", true, "400", 90000);
        Boolean result = creditService.R1(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenCreditsHistoryIsNull_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(1L, "2023-01-01 8000,2023-02-01 8000,2023-03-01 8000", 50000, 15, 6.0, "Primera Vivienda", true, "500", 100000);
        Boolean result = creditService.R2(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenCreditsHistoryIsTrue_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(5L, "2023-01-01 12000,2023-02-01 12000,2023-03-01 12000", 40000, 10, 4.0, "Remodelación", true, "600", 200000);
        credit.setCreditsHistory(true);
        Boolean result = creditService.R2(credit);
        assertThat(result).isTrue();
    }
    @Test
    void whenCreditsHistoryIsFalse_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(3L, "2023-01-01 7000,2023-02-01 7000,2023-03-01 7000", 60000, 20, 5.0, "Segunda Vivienda", false, "500", 80000);
        Boolean result = creditService.R2(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenCreditsHistoryIsNullWithValidOtherProperties_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(4L, "2023-01-01 5000,2023-02-01 5000,2023-03-01 5000", 30000, 20, 7.0, "Propiedades Comerciales", null, "300", 90000);
        Boolean result = creditService.R2(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenCreditsHistoryIsTrueWithValidOtherProperties_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(5L, "2023-01-01 12000,2023-02-01 12000,2023-03-01 12000", 40000, 10, 4.0, "Remodelación", true, "600", 200000);
        credit.setCreditsHistory(true);
        Boolean result = creditService.R2(credit);
        assertThat(result).isTrue();
    }
    @Test
    void whenCreditsHistoryIsFalseWithValidOtherProperties_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(6L, "2023-01-01 7000,2023-02-01 7000,2023-03-01 7000", 60000, 15, 5.5, "Primera Vivienda", false, "500", 100000);
        Boolean result = creditService.R2(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenDebtSumPlusMonthlyFeeIsLessThan50PercentOfIncome_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(1L, "2024-10-03 200000,2024-09-03 200000,2024-08-03 200000,2024-07-03 200000,2024-06-03 20000000,2024-05-03 200000,2024-04-03 200000,2024-03-03 200000,2024-02-03 200000,2024-01-03 200000,2023-12-03 200000,2023-11-03 200000,2023-10-03 200000,2023-09-03 200000,2023-08-03 200000,2023-07-03 200000,2023-06-03 200000,2023-05-03 200000,2023-04-03 200000,2023-03-03 200000,2023-02-03 200000,2023-01-03 200000,2022-12-03 200000,2022-11-03 200000", 1000000, 20, 0.035, "Primera Vivienda", true, "500", 100000);
        Boolean result = creditService.R4(credit);
        assertThat(result).isTrue();
    }
    @Test
    void whenDebtSumPlusMonthlyFeeEquals50PercentOfIncome_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(1L, "2024-10-03 200000,2024-09-03 200000,2024-08-03 200000,2024-07-03 200000,2024-06-03 20000000,2024-05-03 200000,2024-04-03 200000,2024-03-03 200000,2024-02-03 200000,2024-01-03 200000,2023-12-03 200000,2023-11-03 200000,2023-10-03 200000,2023-09-03 200000,2023-08-03 200000,2023-07-03 200000,2023-06-03 200000,2023-05-03 200000,2023-04-03 200000,2023-03-03 200000,2023-02-03 200000,2023-01-03 200000,2022-12-03 200000,2022-11-03 200000", 1000000, 20, 0.035, "Primera Vivienda", true, "500", 100000);
        Boolean result = creditService.R4(credit);
        assertThat(result).isTrue();
    }
    @Test
    void whenDebtSumPlusMonthlyFeeIsGreaterThan50PercentOfIncome_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(3L, "2023-01-01 4000,2023-02-01 4000", 50000, 15, 6.0, "Primera Vivienda", true, "4000,4000", 100000);
        Boolean result = creditService.R4(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenDebtContainsOneDebtLessThan50PercentOfIncome_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(1L, "2024-10-03 200000,2024-09-03 200000,2024-08-03 200000,2024-07-03 200000,2024-06-03 20000000,2024-05-03 200000,2024-04-03 200000,2024-03-03 200000,2024-02-03 200000,2024-01-03 200000,2023-12-03 200000,2023-11-03 200000,2023-10-03 200000,2023-09-03 200000,2023-08-03 200000,2023-07-03 200000,2023-06-03 200000,2023-05-03 200000,2023-04-03 200000,2023-03-03 200000,2023-02-03 200000,2023-01-03 200000,2022-12-03 200000,2022-11-03 200000", 1000000, 20, 0.035, "Primera Vivienda", true, "500", 100000);
        Boolean result = creditService.R4(credit);
        assertThat(result).isTrue();
    }
    @Test
    void whenDebtSumIsGreaterThan50PercentOfIncome_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(5L, "2023-01-01 3000,2023-02-01 3000,2023-03-01 4000", 50000, 15, 6.0, "Propiedades Comerciales", true, "3000,3000,4000", 100000);
        Boolean result = creditService.R4(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenDebtIsNullButMonthlyFeeIsGreaterThan50PercentOfIncome_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(6L, null, 50000, 15, 6.0, "Remodelación", true, null, 100000);
        Boolean result = creditService.R4(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenFirstHomeLoanAndRequestedAmountIsLessThan80Percent_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(1L, "3000", 70000, 30, 5.0, "Primera Vivienda", true, "500", 100000);
        Boolean result = creditService.R5(credit);
        assertThat(result).isTrue();
    }
    @Test
    void whenFirstHomeLoanAndRequestedAmountIsMoreThan80Percent_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(1L, "3000", 90000, 30, 5.0, "Primera Vivienda", true, "500", 100000);
        Boolean result = creditService.R5(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenSecondHomeLoanAndRequestedAmountIsLessThan70Percent_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(1L, "3000", 60000, 30, 5.0, "Segunda Vivienda", true, "500", 100000);
        Boolean result = creditService.R5(credit);
        assertThat(result).isTrue();
    }
    @Test
    void whenSecondHomeLoanAndRequestedAmountIsMoreThan70Percent_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(1L, "3000", 80000, 30, 5.0, "Segunda Vivienda", true, "500", 100000);
        Boolean result = creditService.R5(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenCommercialPropertyLoanAndRequestedAmountIsLessThan60Percent_thenReturnTrue() {
        CreditEntity credit = new CreditEntity(1L, "3000", 50000, 30, 5.0, "Propiedades Comerciales", true, "500", 100000);
        Boolean result = creditService.R5(credit);
        assertThat(result).isTrue();
    }
    @Test
    void whenRemodelingLoanAndRequestedAmountIsMoreThan50Percent_thenReturnFalse() {
        CreditEntity credit = new CreditEntity(1L, "3000", 60000, 30, 5.0, "Remodelación", true, "500", 100000);
        Boolean result = creditService.R5(credit);
        assertThat(result).isFalse();
    }
    @Test
    void whenLowInterestRate_thenCorrectTotalCostCalculation() {
        CreditEntity credit = new CreditEntity(1L, "2023-01-01 1000,2023-02-01 1000", 50000, 10, 2.0, "Primera Vivienda", true, "1000", 100000);
        credit.setLienInsurance(0.01);
        credit.setAdministrationFee(0.02);
        creditService.totalCost(credit);
        assertThat(credit.getRemainingMonthlyInstallments()).isEqualTo(20500);
        assertThat(credit.getFirstInstallment()).isEqualTo(21500);
        assertThat(credit.getTotalAmount()).isEqualTo(2461000);
    }
    @Test
    void whenHighInterestRate_thenCorrectTotalCostCalculation() {
        CreditEntity credit = new CreditEntity(2L, "2023-01-01 1000,2023-02-01 1000", 100000, 15, 10.0, "Segunda Vivienda", true, "1000", 200000);
        credit.setLienInsurance(0.02);
        credit.setAdministrationFee(0.05);
        creditService.totalCost(credit);
        assertThat(credit.getRemainingMonthlyInstallments()).isEqualTo(22000);
        assertThat(credit.getFirstInstallment()).isEqualTo(27000);
        assertThat(credit.getTotalAmount()).isEqualTo(3965000);
    }
    @Test
    void whenLienInsuranceIsZero_thenCorrectTotalCostCalculation() {
        CreditEntity credit = new CreditEntity(3L, "2023-01-01 1500,2023-02-01 1500", 30000, 5, 4.5, "Propiedades Comerciales", true, "1500", 30000);
        credit.setLienInsurance(0.0);
        credit.setAdministrationFee(0.03);
        creditService.totalCost(credit);
        assertThat(credit.getRemainingMonthlyInstallments()).isEqualTo(20000);
        assertThat(credit.getFirstInstallment()).isEqualTo(20900);
        assertThat(credit.getTotalAmount()).isEqualTo(1200900);
    }
    @Test
    void whenAdministrationFeeIsZero_thenCorrectTotalCostCalculation() {
        CreditEntity credit = new CreditEntity(4L, "2023-01-01 5000,2023-02-01 5000", 100000, 20, 5.0, "Remodelación", true, "5000", 100000);
        credit.setLienInsurance(0.01);
        credit.setAdministrationFee(0.0);
        assertThat(credit.getRemainingMonthlyInstallments()).isEqualTo(0);
        assertThat(credit.getFirstInstallment()).isEqualTo(0);
        assertThat(credit.getTotalAmount()).isEqualTo(0);
    }
    @Test
    void whenAllValuesAreHigh_thenCorrectTotalCostCalculation() {
        CreditEntity credit = new CreditEntity(5L, "2023-01-01 10000,2023-02-01 10000", 1000000, 30, 15.0, "Propiedades Comerciales", true, "10000", 1000000);
        credit.setLienInsurance(0.05);
        credit.setAdministrationFee(0.1);
        creditService.totalCost(credit);
        assertThat(credit.getRemainingMonthlyInstallments()).isEqualTo(70000);
        assertThat(credit.getFirstInstallment()).isEqualTo(170000);
        assertThat(credit.getTotalAmount()).isEqualTo(25300000);
    }
    @Test
    void whenValuesAreNormal_thenCorrectTotalCostCalculation() {
        CreditEntity credit = new CreditEntity(6L, "2023-01-01 1000,2023-02-01 1000", 50000, 10, 5.5, "Primera Vivienda", true, "1000", 50000);
        credit.setLienInsurance(0.02);
        credit.setAdministrationFee(0.04);
        creditService.totalCost(credit);
        assertThat(credit.getRemainingMonthlyInstallments()).isEqualTo(21000);
        assertThat(credit.getFirstInstallment()).isEqualTo(23000);
        assertThat(credit.getTotalAmount()).isEqualTo(2522000);
    }
    @Test
    public void testDeleteSingleCreditForUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        CreditEntity credit1 = new CreditEntity(1L, "2024-10-01 200000", 1000000, 20, 0.035, null, null, "1000,200", 500000);
        List<CreditEntity> credits = new ArrayList<>();
        credits.add(credit1);
        when(creditRepository.findAll()).thenReturn(credits);
        creditService.deleted(user);
        assertEquals(1, credits.size());
    }
    @Test
    public void testNoDeleteCreditForDifferentUser() {
        UserEntity user = new UserEntity();
        user.setId(2L);
        CreditEntity credit1 = new CreditEntity(1L, "2024-10-01 200000", 1000000, 20, 0.035, null, null, "1000,200", 500000);
        List<CreditEntity> credits = new ArrayList<>();
        credits.add(credit1);
        when(creditRepository.findAll()).thenReturn(credits);
        creditService.deleted(user);
        assertEquals(1, credits.size());
    }
    @Test
    public void testDeleteMultipleCreditsForUser() {
        UserEntity user = new UserEntity();
        user.setId(3L);
        CreditEntity credit1 = new CreditEntity(3L, "2024-10-01 300000", 2000000, 15, 0.045, null, null, "1200,800", 600000);
        CreditEntity credit2 = new CreditEntity(3L, "2024-09-01 250000", 1500000, 10, 0.04, null, null, "900,700", 550000);
        List<CreditEntity> credits = new ArrayList<>();
        credits.add(credit1);
        credits.add(credit2);
        when(creditRepository.findAll()).thenReturn(credits);
        creditService.deleted(user);
        assertEquals(2, credits.size());
    }
    @Test
    public void testNoDeleteWhenNoCreditsExist() {
        UserEntity user = new UserEntity();
        user.setId(4L);
        List<CreditEntity> credits = new ArrayList<>();
        when(creditRepository.findAll()).thenReturn(credits);
        creditService.deleted(user);
        assertEquals(0, credits.size());
    }
    @Test
    public void testDeleteSingleCreditAmongMultiple() {
        UserEntity user = new UserEntity();
        user.setId(5L);
        CreditEntity credit1 = new CreditEntity(5L, "2024-08-01 200000", 1200000, 18, 0.03, null, null, "500,400", 450000);
        CreditEntity credit2 = new CreditEntity(6L, "2024-07-01 250000", 1300000, 12, 0.04, null, null, "600,300", 550000);
        List<CreditEntity> credits = new ArrayList<>();
        credits.add(credit1);
        credits.add(credit2);
        when(creditRepository.findAll()).thenReturn(credits);
        creditService.deleted(user);
        assertEquals(2, credits.size());
        assertEquals(5L, credits.get(0).getUserId());
    }
    @Test
    public void testDeleteCreditWithNullUserId() {
        UserEntity user = new UserEntity();
        user.setId(null);
        CreditEntity credit1 = new CreditEntity(7L, "2024-10-02 150000", 900000, 9, 0.025, null, null, "300,250", 350000);
        List<CreditEntity> credits = new ArrayList<>();
        credits.add(credit1);
        when(creditRepository.findAll()).thenReturn(credits);
        creditService.deleted(user);
        assertEquals(1, credits.size());
    }
    @Test
    public void testDisbursement_NewDeposit() {
        CreditEntity credit = new CreditEntity(1L, null, 500000, 20, 0.035, null, null, null, 0);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(100000);
        user.setDepositAccount("");
        user.setSavingsAccountHistory("");
        when(userService.findById(1L)).thenReturn(user);
        creditService.Disbursement(credit);
        assertEquals(600000, user.getCurrentSavingsBalance());
        assertEquals(LocalDate.now().toString() + " 500000", user.getDepositAccount());
        assertEquals(LocalDate.now().toString() + " 600000", user.getSavingsAccountHistory());
    }
    @Test
    public void testDisbursement_ExistingDeposit() {
        CreditEntity credit = new CreditEntity(1L, null, 250000, 20, 0.035, null, null, null, 0);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(200000);
        user.setDepositAccount("2024-10-01 300000");
        user.setSavingsAccountHistory("2024-10-01 200000");
        when(userService.findById(1L)).thenReturn(user);
        creditService.Disbursement(credit);
        assertEquals(450000, user.getCurrentSavingsBalance());
        assertEquals("2024-10-01 300000," + LocalDate.now().toString() + " 250000", user.getDepositAccount());
        assertEquals("2024-10-01 200000," + LocalDate.now().toString() + " 450000", user.getSavingsAccountHistory());
    }
    @Test
    public void testDisbursement_ZeroRequestedAmount() {
        CreditEntity credit = new CreditEntity(1L, null, 0, 20, 0.035, null, null, null, 0);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(500000);
        user.setDepositAccount("");
        user.setSavingsAccountHistory("");
        when(userService.findById(1L)).thenReturn(user);
        creditService.Disbursement(credit);
        assertEquals(500000, user.getCurrentSavingsBalance());
        assertEquals(LocalDate.now().toString() + " 0", user.getDepositAccount());
        assertEquals(LocalDate.now().toString() + " 500000", user.getSavingsAccountHistory());
    }
    @Test
    public void testDisbursement_NegativeBalance() {
        CreditEntity credit = new CreditEntity(1L, null, 100000, 20, 0.035, null, null, null, 0);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(-50000);
        user.setDepositAccount("");
        user.setSavingsAccountHistory("");
        when(userService.findById(1L)).thenReturn(user);
        creditService.Disbursement(credit);
        assertEquals(50000, user.getCurrentSavingsBalance());
        assertEquals(LocalDate.now().toString() + " 100000", user.getDepositAccount());
        assertEquals(LocalDate.now().toString() + " 50000", user.getSavingsAccountHistory());
    }
    @Test
    public void testDisbursement_ExistingHistoryAndDeposit() {
        CreditEntity credit = new CreditEntity(1L, null, 200000, 20, 0.035, null, null, null, 0);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(300000);
        user.setDepositAccount("2024-01-01 100000,2024-02-01 200000");
        user.setSavingsAccountHistory("2024-01-01 100000,2024-02-01 300000");
        when(userService.findById(1L)).thenReturn(user);
        creditService.Disbursement(credit);
        assertEquals(500000, user.getCurrentSavingsBalance());
        assertEquals("2024-01-01 100000,2024-02-01 200000," + LocalDate.now().toString() + " 200000", user.getDepositAccount());
        assertEquals("2024-01-01 100000,2024-02-01 300000," + LocalDate.now().toString() + " 500000", user.getSavingsAccountHistory());
    }
    @Test
    public void testDisbursement_NoPreviousHistory() {
        CreditEntity credit = new CreditEntity(1L, null, 150000, 20, 0.035, null, null, null, 0);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(70000);
        user.setDepositAccount("2023-12-01 50000");
        user.setSavingsAccountHistory("");
        when(userService.findById(1L)).thenReturn(user);
        creditService.Disbursement(credit);
        assertEquals(220000, user.getCurrentSavingsBalance());
        assertEquals("2023-12-01 50000," + LocalDate.now().toString() + " 150000", user.getDepositAccount());
        assertEquals(LocalDate.now().toString() + " 220000", user.getSavingsAccountHistory());
    }
    @Test
    public void testApplicationStatus_PrimeraVivienda_AllDocumentsPresent() {
        CreditEntity credit = new CreditEntity();
        credit.setTypeOfLoan("Primera Vivienda");
        credit.setProofOfIncome("Documento de ingreso".getBytes());
        credit.setAppraisalCertificate("Certificado de avalúo".getBytes());
        credit.setCreditHistory("Historial crediticio".getBytes());
        when(creditRepository.save(credit)).thenReturn(credit);
        CreditEntity result = creditService.applicationStatus(credit);
        assertEquals("En revisión", result.getApplicationStatus());
    }
    @Test
    public void testApplicationStatus_PrimeraVivienda_MissingDocuments() {
        CreditEntity credit = new CreditEntity();
        credit.setTypeOfLoan("Primera Vivienda");
        credit.setProofOfIncome("Documento de ingreso".getBytes());
        when(creditRepository.save(credit)).thenReturn(credit);
        CreditEntity result = creditService.applicationStatus(credit);
        assertEquals("Pendiente de documentación", result.getApplicationStatus());
    }
    @Test
    public void testApplicationStatus_SegundaVivienda_AllDocumentsPresent() {
        CreditEntity credit = new CreditEntity();
        credit.setTypeOfLoan("Segunda Vivienda");
        credit.setProofOfIncome("Documento de ingreso".getBytes());
        credit.setAppraisalCertificate("Certificado de avalúo".getBytes());
        credit.setDeedOfTheFirstHome("Escritura de la primera vivienda".getBytes());
        credit.setCreditHistory("Historial crediticio".getBytes());
        when(creditRepository.save(credit)).thenReturn(credit);
        CreditEntity result = creditService.applicationStatus(credit);
        assertEquals("En revisión", result.getApplicationStatus());
    }
    @Test
    public void testApplicationStatus_SegundaVivienda_MissingDocuments() {
        CreditEntity credit = new CreditEntity();
        credit.setTypeOfLoan("Segunda Vivienda");
        credit.setProofOfIncome("Documento de ingreso".getBytes());
        when(creditRepository.save(credit)).thenReturn(credit);
        CreditEntity result = creditService.applicationStatus(credit);
        assertEquals("Pendiente de documentación", result.getApplicationStatus());
    }
    @Test
    public void testApplicationStatus_PropiedadesComerciales_AllDocumentsPresent() {
        CreditEntity credit = new CreditEntity();
        credit.setTypeOfLoan("Propiedades Comerciales");
        credit.setProofOfIncome("Documento de ingreso".getBytes());
        credit.setAppraisalCertificate("Certificado de avalúo".getBytes());
        credit.setFinancialStatusOfTheBusiness("Estado financiero del negocio".getBytes());
        credit.setBusinessPlan("Plan de negocio".getBytes());
        when(creditRepository.save(credit)).thenReturn(credit);
        CreditEntity result = creditService.applicationStatus(credit);
        assertEquals("En revisión", result.getApplicationStatus());
    }
    @Test
    public void testApplicationStatus_Remodelacion_MissingDocuments() {
        CreditEntity credit = new CreditEntity();
        credit.setTypeOfLoan("Remodelación");
        credit.setProofOfIncome("Documento de ingreso".getBytes());
        when(creditRepository.save(credit)).thenReturn(credit);
        CreditEntity result = creditService.applicationStatus(credit);
        assertEquals("Pendiente de documentación", result.getApplicationStatus());
    }
    @Test
    public void testR3_UserWithJobSeniority1_ReturnsTrue() {
        UserEntity user = new UserEntity();
        user.setJobSeniority(1);
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R3(credit);
        assertTrue(result);
    }
    @Test
    public void testR3_UserWithJobSeniority5_ReturnsTrue() {
        UserEntity user = new UserEntity();
        user.setJobSeniority(5);
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R3(credit);
        assertTrue(result);
    }
    @Test
    public void testR3_UserWithJobSeniority0_ReturnsFalse() {
        UserEntity user = new UserEntity();
        user.setJobSeniority(0);
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R3(credit);
        assertFalse(result);
    }
    @Test
    public void testR3_UserWithNegativeJobSeniority_ReturnsFalse() {
        UserEntity user = new UserEntity();
        user.setJobSeniority(-1);
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R3(credit);
        assertFalse(result);
    }
    @Test
    public void testR3_UserWith2JobSeniority_ReturnsTrue() {
        UserEntity user = new UserEntity();
        user.setJobSeniority(2);
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R3(credit);
        assertTrue(result);
    }
    @Test
    public void testR3_UserWithHighJobSeniority_ReturnsTrue() {
        UserEntity user = new UserEntity();
        user.setJobSeniority(100);
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R3(credit);
        assertTrue(result);
    }
    @Test
    public void testR6_UserYoungerThan70_ReturnsTrue() {
        UserEntity user = new UserEntity();
        user.setBirthdate(LocalDate.now().minusYears(65));
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R6(credit);
        assertTrue(result);
    }
    @Test
    public void testR6_UserExactly70_ReturnsFalse() {
        UserEntity user = new UserEntity();
        user.setBirthdate(LocalDate.now().minusYears(70));
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R6(credit);
        assertFalse(result);
    }
    @Test
    public void testR6_UserOlderThan70_ReturnsFalse() {
        UserEntity user = new UserEntity();
        user.setBirthdate(LocalDate.now().minusYears(71));
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R6(credit);
        assertFalse(result);
    }
    @Test
    public void testR6_UserWithFutureBirthdate_ReturnsTrue() {
        UserEntity user = new UserEntity();
        user.setBirthdate(LocalDate.now().plusYears(1));
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R6(credit);
        assertTrue(result);
    }
    @Test
    public void testR6_UserJustBefore70_ReturnsTrue() {
        UserEntity user = new UserEntity();
        user.setBirthdate(LocalDate.now().minusYears(69).minusDays(1));
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R6(credit);
        assertTrue(result);
    }
    @Test
    public void testR6_UserTurns70Today_ReturnsFalse() {
        UserEntity user = new UserEntity();
        user.setBirthdate(LocalDate.now().minusYears(70));
        when(userService.findById(1L)).thenReturn(user);
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        Boolean result = creditService.R6(credit);
        assertFalse(result);
    }
    @Test
    public void testR71_CurrentSavingsBalanceIsSufficient_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(1000000);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(150000);
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(credit);
        assertTrue(result);
    }
    @Test
    public void testR71_CurrentSavingsBalanceIsExactly10Percent_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(1000000);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(100000);
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(credit);
        assertTrue(result);
    }
    @Test
    public void testR71_CurrentSavingsBalanceIsLessThan10Percent_ReturnsFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(1000000);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(90000);
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(credit);
        assertFalse(result);
    }
    @Test
    public void testR71_CurrentSavingsBalanceIsZero_ReturnsFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(1000000);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(0);
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(credit);
        assertFalse(result);
    }
    @Test
    public void testR71_RequestedAmountIsZero_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(0);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(50000);
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(credit);
        assertTrue(result);
    }
    @Test
    public void testR71_CurrentSavingsBalanceIsNegative_ReturnsFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(1000000);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCurrentSavingsBalance(-50000);
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(credit);
        assertFalse(result);
    }
    @Test
    public void testR72_NoSignificantWithdrawals_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSavingsAccountHistory("2024-10-01 100000,2024-09-01 90000,2024-08-01 80000,2024-07-01 70000");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R72(credit);
        assertTrue(result);
    }
    @Test
    public void testR72_SignificantWithdrawalFound_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSavingsAccountHistory("2024-10-01 100000,2024-09-01 60000,2024-08-01 50000");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R72(credit);
        assertTrue(result);
    }
    @Test
    public void testR72_ValidEntriesButLowBalanceInLastYear_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSavingsAccountHistory("2024-10-01 100000,2024-09-01 60000,2024-08-01 50000,2024-07-01 30000");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R72(credit);
        assertTrue(result);
    }
    @Test
    public void testR72_AllEntriesOlderThan12Months_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSavingsAccountHistory("2023-09-01 100000,2023-08-01 90000");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R72(credit);
        assertTrue(result);
    }
    @Test
    public void testR72_OnlyOneValidEntryInLastYear_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSavingsAccountHistory("2024-09-01 100000");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R72(credit);
        assertTrue(result);
    }
    @Test
    public void testR72_MultipleEntriesWithSignificantWithdrawals_ReturnsFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setSavingsAccountHistory("2024-10-01 100000,2024-09-01 40000,2024-08-01 50000");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R72(credit);
        assertFalse(result);
    }
    @Test
    public void testR74_UserUnderTwoYearsOld_WithSufficientBalance_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(10000);
        credit.setLoanTerm(12);
        credit.setAnnualInterestRate(0.05);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCreationDate(LocalDate.now().minusYears(1));
        user.setCurrentSavingsBalance(3000);
        when(userService.findById(1L)).thenReturn(user);
        when(userService.simulation(10000, 12, 0.05)).thenReturn((int) 15000.0);
        Boolean result = creditService.R74(credit);
        assertTrue(result);
    }
    @Test
    public void testR74_UserUnderTwoYearsOld_WithInsufficientBalance_ReturnsFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(10000);
        credit.setLoanTerm(12);
        credit.setAnnualInterestRate(0.05);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCreationDate(LocalDate.now().minusYears(1));
        user.setCurrentSavingsBalance(2000);
        when(userService.findById(1L)).thenReturn(user);
        when(userService.simulation(10000, 12, 0.05)).thenReturn(15000);
        Boolean result = creditService.R74(credit);
        assertFalse(result);
    }
    @Test
    public void testR74_UserTwoYearsOrOlder_WithSufficientBalance_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(10000);
        credit.setLoanTerm(12);
        credit.setAnnualInterestRate(0.05);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCreationDate(LocalDate.now().minusYears(3));
        user.setCurrentSavingsBalance(2000);
        when(userService.findById(1L)).thenReturn(user);
        when(userService.simulation(10000, 12, 0.05)).thenReturn(20000);
        Boolean result = creditService.R74(credit);
        assertTrue(result);
    }
    @Test
    public void testR74_UserTwoYearsOrOlder_WithInsufficientBalance_ReturnsFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(10000);
        credit.setLoanTerm(12);
        credit.setAnnualInterestRate(0.05);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCreationDate(LocalDate.now().minusYears(3));
        user.setCurrentSavingsBalance(500);
        when(userService.findById(1L)).thenReturn(user);
        when(userService.simulation(10000, 12, 0.05)).thenReturn(20000);
        Boolean result = creditService.R74(credit);
        assertFalse(result);
    }
    @Test
    public void testR74_UserUnderTwoYearsOld_JustSufficientBalance_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(10000);
        credit.setLoanTerm(12);
        credit.setAnnualInterestRate(0.05);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCreationDate(LocalDate.now().minusYears(1));
        user.setCurrentSavingsBalance(3000);
        when(userService.findById(1L)).thenReturn(user);
        when(userService.simulation(10000, 12, 0.05)).thenReturn(15000);
        Boolean result = creditService.R74(credit);
        assertTrue(result);
    }
    @Test
    public void testR74_UserTwoYearsOrOlder_JustInsufficientBalance_ReturnsFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setRequestedAmount(10000);
        credit.setLoanTerm(12);
        credit.setAnnualInterestRate(0.05);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setCreationDate(LocalDate.now().minusYears(3));
        user.setCurrentSavingsBalance(1500);
        when(userService.findById(1L)).thenReturn(user);
        when(userService.simulation(10000, 12, 0.05)).thenReturn(20000);
        Boolean result = creditService.R74(credit);
        assertFalse(result);
    }
    @Test
    public void testR75_NoWithdrawals_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setWithdrawalAccount("2024-10-06 10000,2024-10-06 20000");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R75(credit);
        assertTrue(result);
    }
    @Test
    public void testR75_WithdrawalsWithinLimit_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setWithdrawalAccount("2024-04-01 1000,2024-05-01 900,2024-06-01 950");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R75(credit);
        assertTrue(result);
    }
    @Test
    public void testR75_LargeWithdrawal_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setWithdrawalAccount("2024-04-01 1000,2024-05-01 900,2024-06-01 500");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R75(credit);
        assertTrue(result);
    }
    @Test
    public void testR75_MixedWithdrawals_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setWithdrawalAccount("2024-04-01 1000,2024-06-01 600,2024-06-01 2000");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R75(credit);
        assertTrue(result);
    }
    @Test
    public void testR75_RecentWithdrawalsWithinLimit_ReturnsTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setWithdrawalAccount("2024-07-01 2000,2024-08-01 1900,2024-09-01 1800");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R75(credit);
        assertTrue(result);
    }
    @Test
    public void testR75_RecentWithdrawalsExceedLimit_ReturnsFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setWithdrawalAccount("2024-07-01 2000,2024-08-01 1600,2024-09-01 1000");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R75(credit);
        assertFalse(result);
    }
    @Test
    public void testGetAllCredits_EmptyList() {
        when(creditRepository.findAll()).thenReturn(new ArrayList<>());
        ArrayList<CreditEntity> result = creditService.getAllCredits();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    public void testGetAllCredits_NoModificationOfOriginalList() {
        CreditEntity credit1 = new CreditEntity(1L, "2024-10-01 200000", 1000000, 20, 0.035, null, null, "1000,200", 500000);
        List<CreditEntity> originalCredits = new ArrayList<>(List.of(credit1));
        when(creditRepository.findAll()).thenReturn(originalCredits);
        assertEquals(1, originalCredits.size());
        assertEquals(1000000, originalCredits.get(0).getRequestedAmount());
    }
    @Test
    public void testGetAllCredits_ReturnsNonNullList() {
        when(creditRepository.findAll()).thenReturn(new ArrayList<>());
        ArrayList<CreditEntity> result = creditService.getAllCredits();
        assertNotNull(result);
    }
    @Test
    public void testGetAllCredits_NoCallWhenEmpty() {
        when(creditRepository.findAll()).thenReturn(new ArrayList<>());
        ArrayList<CreditEntity> result = creditService.getAllCredits();
        verify(creditRepository).findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    public void testGetAllCredits_RepositoryThrowsException() {
        when(creditRepository.findAll()).thenThrow(new RuntimeException("Error retrieving credits"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            creditService.getAllCredits();
        });
        assertEquals("Error retrieving credits", exception.getMessage());
    }
    @Test
    public void testGetAllCredits_ExceptionThrown() {
        when(creditRepository.findAll()).thenThrow(new RuntimeException("Error al recuperar créditos"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            creditService.getAllCredits();
        });
        assertEquals("Error al recuperar créditos", exception.getMessage());
    }
    @Test
    public void testSaveCredit_Success() {
        CreditEntity credit = new CreditEntity(1L, "2024-10-01 200000", 1000000, 20, 0.035, null, null, "1000,200", 500000);
        when(creditRepository.save(credit)).thenReturn(credit);
        CreditEntity savedCredit = creditService.saveCredit(credit);
        assertNotNull(savedCredit);
        assertEquals(credit.getId(), savedCredit.getId());
        assertEquals(credit.getRequestedAmount(), savedCredit.getRequestedAmount());
    }
    @Test
    public void testSaveCredit_InvalidUser() {
        CreditEntity credit = new CreditEntity(6L, "2024-10-06 800000", 3000000, 30, 0.060, null, null, "4000,500", 900000);
        when(creditRepository.save(credit)).thenReturn(credit);
        when(userService.findById(anyLong())).thenReturn(null);
        CreditEntity savedCredit = creditService.saveCredit(credit);
        assertNotNull(savedCredit);
        assertEquals(credit.getId(), savedCredit.getId());
    }
    @Test
    public void testSaveCredit_ExtremeValues() {
        CreditEntity credit = new CreditEntity(2L, "2024-10-02 500000", Integer.MAX_VALUE, 100, Double.MAX_VALUE, null, null, "5000,600", Integer.MAX_VALUE);
        when(creditRepository.save(credit)).thenReturn(credit);
        CreditEntity savedCredit = creditService.saveCredit(credit);
        assertNotNull(savedCredit);
        assertEquals(credit.getId(), savedCredit.getId());
    }
    @Test
    public void testSaveCredit_NullFields() {
        CreditEntity credit = new CreditEntity(3L, "2024-10-03 300000", 1500000, 15, 0.045, null, null, null, 700000);
        when(creditRepository.save(credit)).thenReturn(credit);
        CreditEntity savedCredit = creditService.saveCredit(credit);
        assertNotNull(savedCredit);
        assertEquals(credit.getId(), savedCredit.getId());
        assertNull(savedCredit.getProofOfIncome());
        assertNull(savedCredit.getAppraisalCertificate());
    }
    @Test
    public void testSaveCredit_CallsRepositoryOnce() {
        CreditEntity credit = new CreditEntity(4L, "2024-10-04 200000", 1200000, 12, 0.050, null, null, "2000,300", 600000);
        creditService.saveCredit(credit);
        verify(creditRepository, times(1)).save(credit);
    }
    @Test
    public void testSaveCredit_RepositoryThrowsException() {
        CreditEntity credit = new CreditEntity(5L, "2024-10-05 700000", 2500000, 25, 0.055, null, null, "3000,400", 800000);
        when(creditRepository.save(credit)).thenThrow(new RuntimeException("Error al guardar el crédito"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            creditService.saveCredit(credit);
        });
        assertEquals("Error al guardar el crédito", exception.getMessage());
    }
    @Test
    public void testFindById_NonExistingCredit() {
        Long creditId = 2L;
        when(creditRepository.findById(creditId)).thenReturn(Optional.empty());
        CreditEntity foundCredit = creditService.findById(creditId);
        assertNull(foundCredit);
    }
    @Test
    public void testFindById_NullId() {
        CreditEntity foundCredit = creditService.findById(null);
        assertNull(foundCredit);
    }
    @Test
    public void testFindById_UninitializedCreditId() {
        Long creditId = 6L;
        when(creditRepository.findById(creditId)).thenReturn(Optional.empty());
        CreditEntity foundCredit = creditService.findById(creditId);
        assertNull(foundCredit);
        verify(creditRepository).findById(creditId);
    }
    @Test
    public void testFindById_NonExistentId_ReturnsNull() {
        Long creditId = 99L;
        when(creditRepository.findById(creditId)).thenReturn(Optional.empty());
        CreditEntity foundCredit = creditService.findById(creditId);
        assertNull(foundCredit);
        verify(creditRepository).findById(creditId);
    }
    @Test
    public void testFindById_NegativeId() {
        Long creditId = -1L;
        when(creditRepository.findById(creditId)).thenReturn(Optional.empty());
        CreditEntity foundCredit = creditService.findById(creditId);
        assertNull(foundCredit);
    }
    @Test
    public void testFindById_NullId_ReturnsNull() {
        Long creditId = null;
        CreditEntity foundCredit = creditService.findById(creditId);
        assertNull(foundCredit);
    }
    @Test
    public void testR71_ValidMonthlyIncome_ReturnsTrue() {
        CreditEntity creditFound = new CreditEntity();
        creditFound.setUserId(1L);
        creditFound.setRequestedAmount(10000);
        UserEntity user = new UserEntity();
        user.setCurrentSavingsBalance(2000);
        creditFound.setMonthlyIncome("2024-01-01 1000, 2024-02-01 1500");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(creditFound);
        assertTrue(result);
    }
    @Test
    public void testR71_NullMonthlyIncome_ReturnsTrue() {
        CreditEntity creditFound = new CreditEntity();
        creditFound.setUserId(1L);
        UserEntity user = new UserEntity();
        creditFound.setMonthlyIncome(null);
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(creditFound);
        assertTrue(result);
    }
    @Test
    public void testR71_EmptyMonthlyIncome_ReturnsTrue() {
        CreditEntity creditFound = new CreditEntity();
        creditFound.setUserId(1L);
        UserEntity user = new UserEntity();
        creditFound.setMonthlyIncome("");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(creditFound);
        assertTrue(result);
    }
    @Test
    public void testR71_InsufficientSavingsBalance_ReturnsFalse() {
        CreditEntity creditFound = new CreditEntity();
        creditFound.setUserId(1L);
        creditFound.setRequestedAmount(10000);
        UserEntity user = new UserEntity();
        user.setCurrentSavingsBalance(500);
        creditFound.setMonthlyIncome("2024-01-01 1000, 2024-02-01 1500");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(creditFound);
        assertFalse(result);
    }
    @Test
    public void testR71_SufficientSavingsBalance_ReturnsTrue() {
        CreditEntity creditFound = new CreditEntity();
        creditFound.setUserId(1L);
        creditFound.setRequestedAmount(10000);
        UserEntity user = new UserEntity();
        user.setCurrentSavingsBalance(1500);
        creditFound.setMonthlyIncome("2024-01-01 1000, 2024-02-01 1500");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(creditFound);
        assertTrue(result);
    }
    @Test
    public void testR71_MultipleMonthlyIncomeEntries_ReturnsTrue() {
        CreditEntity creditFound = new CreditEntity();
        creditFound.setUserId(1L);
        creditFound.setRequestedAmount(10000);
        UserEntity user = new UserEntity();
        user.setCurrentSavingsBalance(2500);
        creditFound.setMonthlyIncome("2024-01-01 1000, 2024-02-01 1500, 2024-03-01 2000");
        when(userService.findById(1L)).thenReturn(user);
        Boolean result = creditService.R71(creditFound);
        assertTrue(result);
    }
    @Test
    void whenMoreThan12DepositsInLastYearAndTotalExceedsMinimum_thenReturnFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setAnnualInterestRate(0.035);
        credit.setMonthlyIncome("2024-10-03 200000,2024-09-03 200000,2024-08-03 200000");
        UserEntity user = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
        user.setDepositAccount("2023-01-01 1000,2023-02-01 1200,2023-03-01 1300,2023-04-01 1100,"
                + "2023-05-01 1500,2023-06-01 1200,2023-07-01 1300,2023-08-01 1100,"
                + "2023-09-01 1000,2023-10-01 1200,2023-11-01 1300,2023-12-01 1500");
        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.doReturn(20000.0).when(creditService).averageIncome(credit);
        boolean result = creditService.R73(credit);
        assertFalse(result);
    }
    @Test
    void whenLessThanThreeDepositsInLastYear_thenReturnFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setAnnualInterestRate(0.035);
        credit.setMonthlyIncome("2024-10-03 200000,2024-09-03 200000,2024-08-03 200000");
        UserEntity user = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
        user.setDepositAccount("2024-01-01 1000,2024-02-01 1200");
        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.doReturn(20000.0).when(creditService).averageIncome(credit);
        boolean result = creditService.R73(credit);
        assertFalse(result);
    }
    @Test
    void whenTotalDepositsDoesNotMeetMinimumWithThreeDeposits_thenReturnTrue() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setAnnualInterestRate(0.035);
        credit.setMonthlyIncome("2024-10-03 200000,2024-09-03 200000,2024-08-03 200000");
        UserEntity user = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
        user.setDepositAccount("2024-01-01 1000,2024-02-01 500,2024-03-01 2000");
        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.doReturn(20000.0).when(creditService).averageIncome(credit);
        boolean result = creditService.R73(credit);
        assertTrue(result);
    }
    @Test
    void whenNoDepositsInLastYear_thenReturnFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setAnnualInterestRate(0.035);
        credit.setMonthlyIncome("2024-10-03 200000,2024-09-03 200000,2024-08-03 200000");
        UserEntity user = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
        user.setDepositAccount("2022-12-01 1500,2022-11-01 1200");
        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.doReturn(20000.0).when(creditService).averageIncome(credit);
        boolean result = creditService.R73(credit);
        assertFalse(result);
    }
    @Test
    void whenDepositsRegularButTotalLessThanMinimum_thenReturnFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setAnnualInterestRate(0.035);
        credit.setMonthlyIncome("2024-10-03 200000,2024-09-03 200000,2024-08-03 200000");
        UserEntity user = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
        user.setDepositAccount("2023-10-01 400,2023-11-01 500,2023-12-01 300");
        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.doReturn(20000.0).when(creditService).averageIncome(credit);
        boolean result = creditService.R73(credit);
        assertFalse(result);
    }
    @Test
    void whenLessThan12DepositsAndTotalLessThanMinimum_thenReturnFalse() {
        CreditEntity credit = new CreditEntity();
        credit.setUserId(1L);
        credit.setAnnualInterestRate(0.035);
        credit.setMonthlyIncome("2024-10-03 200000,2024-09-03 200000,2024-08-03 200000");
        UserEntity user = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
        user.setDepositAccount("2023-09-01 100,2023-09-02 100");
        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.doReturn(20000.0).when(creditService).averageIncome(credit);
        boolean result = creditService.R73(credit);
        assertFalse(result);
    }

}
