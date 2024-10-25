package PrestaBanco.Crud.Controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import PrestaBanco.Crud.Entities.CreditEntity;
import PrestaBanco.Crud.Services.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreditControllerTest {
    @InjectMocks
    private CreditController creditController;
    @Mock
    private CreditService creditService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void applyForCredit_ShouldReturnSavedCredit_WhenAllParametersAreProvided() throws Exception {
        Long userId = 1L;
        MultipartFile proofOfIncome = new MockMultipartFile("proofOfIncome", "test.pdf", "application/pdf", "PDF content".getBytes());
        MultipartFile appraisalCertificate = new MockMultipartFile("appraisalCertificate", "test.pdf", "application/pdf", "PDF content".getBytes());
        MultipartFile creditHistory = new MockMultipartFile("creditHistory", "test.pdf", "application/pdf", "PDF content".getBytes());
        MultipartFile deedOfTheFirstHome = null;
        MultipartFile financialStatusOfTheBusiness = null;
        MultipartFile businessPlan = null;
        MultipartFile remodelingBudget = null;
        MultipartFile updatedAppraisalCertificate = null;
        String monthlyIncome1 = "2024-10-01 1000";
        String monthlyIncome2 = "2024-09-01 1100";
        String monthlyIncome3 = "2024-08-01 1200";
        String monthlyIncome4 = "2024-07-01 1300";
        String monthlyIncome5 = "2024-06-01 1400";
        String monthlyIncome6 = "2024-05-01 1500";
        String monthlyIncome7 = "2024-04-01 1600";
        String monthlyIncome8 = "2024-03-01 1700";
        String monthlyIncome9 = "2024-02-01 1800";
        String monthlyIncome10 = "2024-01-01 1900";
        String monthlyIncome11 = "2023-12-01 2000";
        String monthlyIncome12 = "2023-11-01 2100";
        int requestedAmount = 50000;
        int loanTerm = 12;
        double annualInterestRate = 5.0;
        String typeOfLoan = "Primera Vivienda";
        Boolean creditsHistory = true;
        String monthlyDebt = "500";
        int propertyAmount = 100000;
        CreditEntity savedCredit = new CreditEntity(userId,
                monthlyIncome1 + "," + monthlyIncome2 + "," + monthlyIncome3 + "," + monthlyIncome4 + "," +
                        monthlyIncome5 + "," + monthlyIncome6 + "," + monthlyIncome7 + "," + monthlyIncome8 + "," +
                        monthlyIncome9 + "," + monthlyIncome10 + "," + monthlyIncome11 + "," + monthlyIncome12,
                requestedAmount, loanTerm, annualInterestRate, typeOfLoan, creditsHistory, monthlyDebt, propertyAmount);
        when(creditService.applicationStatus(any(CreditEntity.class))).thenReturn(savedCredit);
        ResponseEntity<CreditEntity> response = creditController.applyForCredit(
                userId, proofOfIncome, appraisalCertificate, creditHistory, deedOfTheFirstHome,
                financialStatusOfTheBusiness, businessPlan, remodelingBudget,
                updatedAppraisalCertificate, monthlyIncome1, monthlyIncome2, monthlyIncome3,
                monthlyIncome4, monthlyIncome5, monthlyIncome6, monthlyIncome7,
                monthlyIncome8, monthlyIncome9, monthlyIncome10, monthlyIncome11,
                monthlyIncome12, requestedAmount, loanTerm, annualInterestRate, typeOfLoan,
                creditsHistory, monthlyDebt, propertyAmount
        );
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedCredit, response.getBody());
        verify(creditService, times(1)).applicationStatus(any(CreditEntity.class));
    }
    @Test
    public void applyForCredit_ShouldReturnNull_WhenExceptionOccurs() throws Exception {
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenThrow(new IOException("Error reading file"));
        ResponseEntity<CreditEntity> response = creditController.applyForCredit(userId, file, null, null, null, null, null, null, null, "2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000", 100000, 12, 5.5, "Personal", true, "10000", 200000);
        assertNull(response);
    }
    @Test
    public void applyForCredit_ShouldHandleMissingFilesGracefully() throws Exception {
        Long userId = 1L;
        CreditEntity creditEntity = new CreditEntity(userId, "formattedMonthlyIncome", 100000, 12, 5.5, "Personal", true, "10000", 200000);
        when(creditService.applicationStatus(any(CreditEntity.class))).thenReturn(creditEntity);
        ResponseEntity<CreditEntity> response = creditController.applyForCredit(userId, null, null, null, null, null, null, null, null, "monthlyIncome1","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000", 100000, 12, 5.5, "Personal", true, "10000", 200000);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(creditEntity, response.getBody());
    }
    @Test
    public void applyForCredit_ShouldReturnNull_WhenServiceThrowsException() throws Exception {
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(creditService.applicationStatus(any(CreditEntity.class))).thenThrow(new RuntimeException("Service error"));
        ResponseEntity<CreditEntity> response = creditController.applyForCredit(userId, file, null, null, null, null, null, null, null, "2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000","2023-09-03 200000",100000, 12, 5.5, "Personal", true, "10000", 200000);
        assertNull(response);
    }
    @Test
    public void applyForCredit_ShouldReturnSavedCredit_WhenAllOptionalFilesProvided() throws Exception {
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(file2.getBytes()).thenReturn(new byte[]{4, 5, 6});
        CreditEntity expectedCredit = new CreditEntity();
        when(creditService.applicationStatus(any(CreditEntity.class))).thenReturn(expectedCredit);
        ResponseEntity<CreditEntity> response = creditController.applyForCredit(
                userId, file, file2, null, null, null, null, null, null,
                "2023-09-01 5000", "2023-10-01 5500", "2023-11-01 6000", "2023-12-01 6500",
                "2024-01-01 7000", "2024-02-01 7500", "2024-03-01 8000", "2024-04-01 8500",
                "2024-05-01 9000", "2024-06-01 9500", "2024-07-01 10000", "2024-08-01 10500",
                100000, 24, 5.5, "Personal", true, "2000", 300000
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCredit, response.getBody());
        verify(creditService, times(1)).applicationStatus(any(CreditEntity.class));
    }
    @Test
    public void applyForCredit_ShouldReturnNull_WhenFileReadFails() throws Exception {
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenThrow(new IOException("Error reading file"));
        ResponseEntity<CreditEntity> response = creditController.applyForCredit(
                userId, file, null, null, null, null, null, null, null,
                "2023-09-01 5000", "2023-10-01 5500", "2023-11-01 6000", "2023-12-01 6500",
                "2024-01-01 7000", "2024-02-01 7500", "2024-03-01 8000", "2024-04-01 8500",
                "2024-05-01 9000", "2024-06-01 9500", "2024-07-01 10000", "2024-08-01 10500",
                100000, 24, 5.5, "Personal", true, "2000", 300000
        );
        assertNull(response);
    }
    @Test
    public void evaluateCredit_ShouldReturnApprovedCredit_WhenCreditFoundAndEvaluatedSuccessfully() {
        Long creditId = 1L;
        CreditEntity foundCredit = new CreditEntity();
        CreditEntity approvedCredit = new CreditEntity();
        when(creditService.findById(creditId)).thenReturn(foundCredit);
        when(creditService.evaluateCredit(foundCredit)).thenReturn(approvedCredit);
        ResponseEntity<CreditEntity> response = creditController.evaluateCredit(creditId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(approvedCredit, response.getBody());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).evaluateCredit(foundCredit);
    }
    @Test
    public void evaluateCredit_ShouldReturnNull_WhenCreditNotFound() {
        Long creditId = 1L;
        when(creditService.findById(creditId)).thenReturn(null);
        ResponseEntity<CreditEntity> response = creditController.evaluateCredit(creditId);
        assertNotNull(response);
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, never()).evaluateCredit(any(CreditEntity.class));
    }
    @Test
    public void evaluateCredit_ShouldReturnApprovedCredit_WhenCreditIsFound() {
        Long creditId = 1L;
        CreditEntity mockCredit = new CreditEntity();
        mockCredit.setId(creditId);
        mockCredit.setApplicationStatus("Pendiente de evaluación");
        CreditEntity approvedCredit = new CreditEntity();
        approvedCredit.setId(creditId);
        approvedCredit.setApplicationStatus("Aprobado");
        when(creditService.findById(creditId)).thenReturn(mockCredit);
        when(creditService.evaluateCredit(mockCredit)).thenReturn(approvedCredit);
        ResponseEntity<CreditEntity> response = creditController.evaluateCredit(creditId);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(approvedCredit, response.getBody());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).evaluateCredit(mockCredit);
    }
    @Test
    public void evaluateCredit_ShouldReturnNull_WhenCreditDoesNotMeetConditions() {
        Long creditId = 1L;
        CreditEntity foundCredit = new CreditEntity();
        CreditEntity rejectedCredit = null;
        when(creditService.findById(creditId)).thenReturn(foundCredit);
        when(creditService.evaluateCredit(foundCredit)).thenReturn(rejectedCredit);
        ResponseEntity<CreditEntity> response = creditController.evaluateCredit(creditId);
        assertNotNull(response);
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).evaluateCredit(foundCredit);
    }
    @Test
    public void evaluateCredit_ShouldHandleCreditWithNullFields() {
        Long creditId = 1L;
        CreditEntity foundCredit = new CreditEntity();
        foundCredit.setTypeOfLoan(null);
        CreditEntity approvedCredit = new CreditEntity();
        when(creditService.findById(creditId)).thenReturn(foundCredit);
        when(creditService.evaluateCredit(foundCredit)).thenReturn(approvedCredit);
        ResponseEntity<CreditEntity> response = creditController.evaluateCredit(creditId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(approvedCredit, response.getBody());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).evaluateCredit(foundCredit);
    }
    @Test
    public void evaluateCredit_ShouldReturnNull_WhenFindByIdThrowsException() {
        Long creditId = 1L;
        when(creditService.findById(creditId)).thenThrow(new RuntimeException("Service error"));
        ResponseEntity<CreditEntity> response = creditController.evaluateCredit(creditId);
        assertNull(response);
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, never()).evaluateCredit(any(CreditEntity.class));
    }
    @Test
    public void updateStatus_ShouldChangeStatusToInEvaluation_WhenStatusIsPendingDocumentation() {
        Long creditId = 1L;
        CreditEntity credit = new CreditEntity();
        credit.setApplicationStatus("Pendiente de documentación");
        when(creditService.findById(creditId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.updateStatus(creditId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("En evaluación", credit.getApplicationStatus());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).saveCredit(credit);
    }
    @Test
    public void updateStatus_ShouldChangeStatusToPreApproved_WhenStatusIsInEvaluation() {
        Long creditId = 1L;
        CreditEntity credit = new CreditEntity();
        credit.setApplicationStatus("En evaluación");
        when(creditService.findById(creditId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.updateStatus(creditId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pre-aprobado", credit.getApplicationStatus());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).saveCredit(credit);
    }
    @Test
    public void updateStatus_ShouldChangeStatusToFinalApproval_WhenStatusIsPreApproved() {
        Long creditId = 1L;
        CreditEntity credit = new CreditEntity();
        credit.setApplicationStatus("Pre-aprobado");
        when(creditService.findById(creditId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.updateStatus(creditId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Aprobación final", credit.getApplicationStatus());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).saveCredit(credit);
    }
    @Test
    public void updateStatus_ShouldChangeStatusToDisbursement_WhenStatusIsApproved() {
        Long creditId = 1L;
        CreditEntity credit = new CreditEntity();
        credit.setApplicationStatus("Aprobada");
        when(creditService.findById(creditId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.updateStatus(creditId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Desembolso", credit.getApplicationStatus());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).saveCredit(credit);
        verify(creditService, times(1)).Disbursement(credit);
    }
    @Test
    public void updateStatus_ShouldReturnNull_WhenStatusIsUnknown() {
        Long creditId = 1L;
        CreditEntity credit = new CreditEntity();
        credit.setApplicationStatus("Estado Desconocido");
        when(creditService.findById(creditId)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.updateStatus(creditId);
        assertNull(response);
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, never()).saveCredit(any(CreditEntity.class));
    }
    @Test
    public void updateStatus_ShouldReturnNull_WhenFindByIdThrowsException() {
        Long creditId = 1L;
        when(creditService.findById(creditId)).thenThrow(new RuntimeException("Service error"));
        ResponseEntity<CreditEntity> response = creditController.updateStatus(creditId);
        assertNull(response);
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, never()).saveCredit(any(CreditEntity.class));
    }
    @Test
    public void updateTerms_ShouldUpdateLienInsuranceAndAdministrationFee_WhenParametersAreValid() {
        Long userId = 1L;
        Double lienInsurance = 500.0;
        Double administrationFee = 100.0;
        CreditEntity credit = new CreditEntity();
        when(creditService.findById(userId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.updateTerms(userId, lienInsurance, administrationFee);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lienInsurance, credit.getLienInsurance());
        assertEquals(administrationFee, credit.getAdministrationFee());
        verify(creditService, times(1)).findById(userId);
        verify(creditService, times(1)).saveCredit(credit);
        verify(creditService, times(1)).totalCost(credit);
    }
    @Test
    public void updateTerms_ShouldReturnNull_WhenCreditNotFound() {
        when(creditService.findById(1L)).thenThrow(new RuntimeException("Credit not found"));
        ResponseEntity<CreditEntity> response = creditController.updateTerms(1L, 600.0, 150.0);
        assertNull(response);
        verify(creditService, never()).saveCredit(any(CreditEntity.class));
        verify(creditService, never()).totalCost(any(CreditEntity.class));
    }
    @Test
    public void updateTerms_ShouldReturnNull_WhenCreditEntityIsInvalid() {
        Long userId = 1L;
        Double lienInsurance = 500.0;
        Double administrationFee = 100.0;
        CreditEntity credit = null;
        when(creditService.findById(userId)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.updateTerms(userId, lienInsurance, administrationFee);
        assertNull(response);
        verify(creditService, times(1)).findById(userId);
        verify(creditService, never()).saveCredit(any(CreditEntity.class));
    }
    @Test
    public void updateTerms_ShouldUpdate_WhenValuesAreMinimum() {
        Long userId = 1L;
        Double lienInsurance = 0.01;
        Double administrationFee = 0.01;
        CreditEntity credit = new CreditEntity();
        when(creditService.findById(userId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.updateTerms(userId, lienInsurance, administrationFee);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lienInsurance, credit.getLienInsurance());
        assertEquals(administrationFee, credit.getAdministrationFee());
        verify(creditService, times(1)).findById(userId);
        verify(creditService, times(1)).saveCredit(credit);
        verify(creditService, times(1)).totalCost(credit);
    }
    @Test
    public void updateTerms_ShouldUpdate_WhenValuesAreMaximum() {
        Long userId = 1L;
        Double lienInsurance = Double.MAX_VALUE;
        Double administrationFee = Double.MAX_VALUE;
        CreditEntity credit = new CreditEntity();
        when(creditService.findById(userId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.updateTerms(userId, lienInsurance, administrationFee);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lienInsurance, credit.getLienInsurance());
        assertEquals(administrationFee, credit.getAdministrationFee());
        verify(creditService, times(1)).findById(userId);
        verify(creditService, times(1)).saveCredit(credit);
        verify(creditService, times(1)).totalCost(credit);
    }
    @Test
    public void updateTerms_ShouldUpdate_WhenValuesAreZero() {
        Long userId = 1L;
        Double lienInsurance = 0.0;
        Double administrationFee = 0.0;
        CreditEntity credit = new CreditEntity();
        when(creditService.findById(userId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.updateTerms(userId, lienInsurance, administrationFee);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lienInsurance, credit.getLienInsurance());
        assertEquals(administrationFee, credit.getAdministrationFee());
        verify(creditService, times(1)).findById(userId);
        verify(creditService, times(1)).saveCredit(credit);
        verify(creditService, times(1)).totalCost(credit);
    }
    @Test
    public void rejectTerms_ShouldCancelCredit_WhenCreditExists() {
        Long creditId = 1L;
        CreditEntity credit = new CreditEntity();
        credit.setApplicationStatus("En evaluación");
        when(creditService.findById(creditId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.rejectTerms(creditId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cancelada", credit.getApplicationStatus());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).saveCredit(credit);
    }
    @Test
    public void rejectTerms_ShouldRemainCancelled_WhenCreditIsAlreadyCancelled() {
        Long creditId = 1L;
        CreditEntity credit = new CreditEntity();
        credit.setApplicationStatus("Cancelada");
        when(creditService.findById(creditId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.rejectTerms(creditId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cancelada", credit.getApplicationStatus());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).saveCredit(credit);
    }
    @Test
    public void rejectTerms_ShouldReturnNull_WhenCreditDoesNotExist() {
        Long nonExistentCreditId = 99L;
        when(creditService.findById(nonExistentCreditId)).thenReturn(null);
        ResponseEntity<CreditEntity> response = creditController.rejectTerms(nonExistentCreditId);
        assertNull(response);
        verify(creditService, times(1)).findById(nonExistentCreditId);
        verify(creditService, times(0)).saveCredit(any(CreditEntity.class));
    }
    @Test
    public void rejectTerms_ShouldHandleNullStatus_WhenCreditHasNullStatus() {
        Long creditId = 1L;
        CreditEntity credit = new CreditEntity();
        credit.setApplicationStatus(null);
        when(creditService.findById(creditId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.rejectTerms(creditId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cancelada", credit.getApplicationStatus());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).saveCredit(credit);
    }
    @Test
    public void rejectTerms_ShouldReturnNull_WhenExceptionOccursOnSave() {
        Long creditId = 1L;
        CreditEntity credit = new CreditEntity();
        credit.setApplicationStatus("En evaluación");
        when(creditService.findById(creditId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenThrow(new RuntimeException("Database error"));
        ResponseEntity<CreditEntity> response = creditController.rejectTerms(creditId);
        assertNull(response);
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).saveCredit(credit);
    }
    @Test
    public void rejectTerms_ShouldCorrectlyUpdateStatus_WhenCreditIsValid() {
        Long creditId = 1L;
        CreditEntity credit = new CreditEntity();
        credit.setApplicationStatus("Pre-aprobado");
        when(creditService.findById(creditId)).thenReturn(credit);
        when(creditService.saveCredit(credit)).thenReturn(credit);
        ResponseEntity<CreditEntity> response = creditController.rejectTerms(creditId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cancelada", credit.getApplicationStatus());
        verify(creditService, times(1)).findById(creditId);
        verify(creditService, times(1)).saveCredit(credit);
    }
    @Test
    public void getAllCredits_ShouldReturnEmptyList_WhenNoCreditsAvailable() {
        when(creditService.getAllCredits()).thenReturn(new ArrayList<>());
        ResponseEntity<List<CreditEntity>> response = creditController.getAllCredits();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(creditService, times(1)).getAllCredits();
    }
    @Test
    public void getAllCredits_ShouldHandleExceptionGracefully() {
        when(creditService.getAllCredits()).thenThrow(new RuntimeException("Unexpected Error"));
        ResponseEntity<List<CreditEntity>> response = null;
        try {
            response = creditController.getAllCredits();
        } catch (Exception e) {
            assertEquals("Unexpected Error", e.getMessage());
        }
        assertNull(response);
        verify(creditService, times(1)).getAllCredits();
    }
    @Test
    public void applyForCredit_ShouldHandleExceptionGracefully() throws Exception {
        Long userId = 1L;
        MultipartFile proofOfIncome = new MockMultipartFile("proofOfIncome", "test.pdf", "application/pdf", "PDF content".getBytes());
        MultipartFile appraisalCertificate = null;
        MultipartFile creditHistory = null;
        when(creditService.applicationStatus(any(CreditEntity.class))).thenThrow(new RuntimeException("Unexpected Error"));
        ResponseEntity<CreditEntity> response = null;
        try {
            response = creditController.applyForCredit(userId, proofOfIncome, appraisalCertificate, creditHistory, null, null, null, null, null,
                    "2024-10-01 1000", "2024-09-01 1100", "2024-08-01 1200", "2024-07-01 1300",
                    "2024-06-01 1400", "2024-05-01 1500", "2024-04-01 1600", "2024-03-01 1700",
                    "2024-02-01 1800", "2024-01-01 1900", "2023-12-01 2000", "2023-11-01 2100",
                    50000, 12, 5.0, "Primera Vivienda", true, "500", 100000);
        } catch (Exception e) {
            assertEquals("Unexpected Error", e.getMessage());
        }
        assertNull(response);
        verify(creditService, times(1)).applicationStatus(any(CreditEntity.class));
    }
    @Test
    public void getAllCredits_ShouldReturnEmptyList_WhenNoCreditsExist() {
        when(creditService.getAllCredits()).thenReturn(new ArrayList<>());
        ResponseEntity<List<CreditEntity>> response = creditController.getAllCredits();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(creditService, times(1)).getAllCredits();
    }
    @Test
    public void getAllCredits_ShouldHandleSpecificExceptionGracefully() {
        when(creditService.getAllCredits()).thenThrow(new IllegalArgumentException("Invalid Argument"));
        ResponseEntity<List<CreditEntity>> response = null;
        try {
            response = creditController.getAllCredits();
        } catch (Exception e) {
            assertEquals("Invalid Argument", e.getMessage());
        }
        assertNull(response);
        verify(creditService, times(1)).getAllCredits();
    }
    @Test
    public void getAllCredits_ShouldThrowExceptionOnServiceFailure() {
        when(creditService.getAllCredits()).thenThrow(new RuntimeException("Error fetching credits"));
        ResponseEntity<List<CreditEntity>> response = null;
        try {
            response = creditController.getAllCredits();
        } catch (Exception e) {
            assertEquals("Error fetching credits", e.getMessage());
        }
        assertNull(response);
        verify(creditService, times(1)).getAllCredits(); // Asegura que el servicio fue llamado
    }
    @Test
    public void getAllCredits_ShouldHandleMultipleExceptionsGracefully() {
        when(creditService.getAllCredits()).thenThrow(new RuntimeException("Database connection error"));
        ResponseEntity<List<CreditEntity>> response = null;
        try {
            response = creditController.getAllCredits();
        } catch (Exception e) {
            assertEquals("Database connection error", e.getMessage());
        }
        assertNull(response);
        verify(creditService, times(1)).getAllCredits();
    }
}
