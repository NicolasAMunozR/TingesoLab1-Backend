package PrestaBanco.Crud.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.junit.jupiter.api.Assertions.*;
import PrestaBanco.Crud.Entities.UserEntity;
import PrestaBanco.Crud.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.*;
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private UserEntity user;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
    }
    @Test
    public void getUser_ShouldReturnUser_WhenUserExists() {
        Long userId = 1L;
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.getUser(userId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
    @Test
    public void getUser_ShouldReturnBadRequest_WhenIdIsNull() {
        Long userId = null;
        ResponseEntity<UserEntity> response = userController.getUser(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void getUser_ShouldReturnNull_WhenIdIsNull() {
        Long userId = null;
        ResponseEntity<UserEntity> response = userController.getUser(userId);
        assertNull(response.getBody());
    }
    @Test
    public void getUser_ShouldReturnNull_WhenIdIsNegative() {
        Long userId = -1L;
        ResponseEntity<UserEntity> response = userController.getUser(userId);
        assertNull(response.getBody());
    }
    @Test
    public void getUser_ShouldReturnUserEntity_WhenUserExists() {
        Long userId = 1L;
        UserEntity user = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.getUser(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user, response.getBody());
    }
    @Test
    public void getUser_ShouldReturnNotFound_WhenUserIdDoesNotExist() {
        Long userId = 10L;
        given(userService.findUserById(userId)).willReturn(null);
        ResponseEntity<UserEntity> response = userController.getUser(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void registerUser_ShouldReturnNull_WhenFileIsNotPdf() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "dummy content".getBytes());
        String identifyingDocument = "12345678-9";
        String name = "Carlos Perez";
        String email = "carlos@mail.com";
        String password = "password";
        int jobSeniority = 5;
        LocalDate birthdate = LocalDate.of(1985, 10, 10);
        ResponseEntity<UserEntity> response = userController.registerUser(file, identifyingDocument, name, email, password, jobSeniority, birthdate);
        assertNull(response);
    }
    @Test
    public void registerUser_ShouldReturnNull_WhenExceptionOccurs() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        String identifyingDocument = "12345678-9";
        String name = "Carlos Perez";
        String email = "carlos@mail.com";
        String password = "password";
        int jobSeniority = 5;
        LocalDate birthdate = LocalDate.of(1985, 10, 10);
        willThrow(new RuntimeException("Error al guardar el usuario")).given(userService).saveUser(any(UserEntity.class));
        ResponseEntity<UserEntity> response = userController.registerUser(file, identifyingDocument, name, email, password, jobSeniority, birthdate);
        assertNull(response);
    }
    @Test
    public void registerUser_ShouldReturnNull_WhenNameIsNull() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        String identifyingDocument = "12345678-9";
        String email = "carlos@mail.com";
        String password = "password";
        int jobSeniority = 5;
        LocalDate birthdate = LocalDate.of(1985, 10, 10);
        ResponseEntity<UserEntity> response = userController.registerUser(file, identifyingDocument, null, email, password, jobSeniority, birthdate);
        assertNotNull(response);
    }
    @Test
    public void registerUser_ShouldReturnNull_WhenEmailIsInvalid() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        String identifyingDocument = "12345678-9";
        String name = "Carlos Perez";
        String email = "invalid-email";
        String password = "password";
        int jobSeniority = 5;
        LocalDate birthdate = LocalDate.of(1985, 10, 10);

        ResponseEntity<UserEntity> response = userController.registerUser(file, identifyingDocument, name, email, password, jobSeniority, birthdate);

        assertNotNull(response); // Verificar que la respuesta es nula
    }
    @Test
    public void registerUser_ShouldReturnNull_WhenPasswordIsEmpty() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        String identifyingDocument = "12345678-9";
        String name = "Carlos Perez";
        String email = "carlos@mail.com";
        String password = ""; // Contraseña vacía
        int jobSeniority = 5;
        LocalDate birthdate = LocalDate.of(1985, 10, 10);

        ResponseEntity<UserEntity> response = userController.registerUser(file, identifyingDocument, name, email, password, jobSeniority, birthdate);

        assertNotNull(response); // Verificar que la respuesta es nula
    }
    @Test
    public void registerUser_ShouldReturnNull_WhenFileIsEmpty() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[0]);
        String identifyingDocument = "12345678-9";
        String name = "Carlos Perez";
        String email = "carlos@mail.com";
        String password = "password";
        int jobSeniority = 5;
        LocalDate birthdate = LocalDate.of(1985, 10, 10);
        ResponseEntity<UserEntity> response = userController.registerUser(file, identifyingDocument, name, email, password, jobSeniority, birthdate);
        assertNotNull(response);
    }
    @Test
    public void updateUser_ShouldUpdateUser_WhenFileIsPdf() throws Exception {
        Long userId = 1L;
        MultipartFile file = Mockito.mock(MultipartFile.class);
        given(file.getOriginalFilename()).willReturn("document.pdf");
        given(file.getBytes()).willReturn(new byte[]{1, 2, 3});
        given(userService.findById(userId)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.updateUser(file, user.getEmail(), userId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).saveUser(any(UserEntity.class));
    }
    @Test
    public void updateUser_ShouldReturnUpdatedUser_WhenFileIsValidPdf() throws Exception {
        Long userId = 1L;
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        String email = "new_email@mail.com";
        UserEntity existingUser = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", new byte[0], 5, LocalDate.of(1985, 10, 10));
        existingUser.setId(userId);
        given(userService.findById(userId)).willReturn(existingUser);
        ResponseEntity<UserEntity> response = userController.updateUser(file, email, userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(email, Objects.requireNonNull(response.getBody()).getEmail());
    }
    @Test
    public void updateUser_ShouldReturnNull_WhenFileIsNotPdf() throws Exception {
        Long userId = 1L;
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "dummy content".getBytes());
        String email = "new_email@mail.com";
        ResponseEntity<UserEntity> response = userController.updateUser(file, email, userId);
        assertNull(response);
    }
    @Test
    public void updateUser_ShouldReturnNull_WhenExceptionOccurs() throws Exception {
        Long userId = 1L;
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        String email = "new_email@mail.com";
        given(userService.findById(userId)).willThrow(new RuntimeException("User not found"));
        ResponseEntity<UserEntity> response = userController.updateUser(file, email, userId);
        assertNull(response);
    }
    @Test
    public void updateUser_ShouldReturnNull_WhenUserNotFound() throws Exception {
        Long userId = 1L;
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        String email = "new_email@mail.com";
        given(userService.findById(userId)).willReturn(null);
        ResponseEntity<UserEntity> response = userController.updateUser(file, email, userId);
        assertNull(response);
    }
    @Test
    public void updateUser_ShouldUpdateUserFile_WhenFileIsValidPdf() throws Exception {
        Long userId = 1L;
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        String email = "new_email@mail.com";
        UserEntity existingUser = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", new byte[0], 5, LocalDate.of(1985, 10, 10));
        existingUser.setId(userId);
        given(userService.findById(userId)).willReturn(existingUser);
        ResponseEntity<UserEntity> response = userController.updateUser(file, email, userId);
        assertNotNull(Objects.requireNonNull(response.getBody()).getFile());
        assertArrayEquals(file.getBytes(), response.getBody().getFile());
    }
    @Test
    public void deleteUser_ShouldDeleteUser_WhenUserExists() {
        Long userId = 1L;
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<Boolean> response = userController.deleteUser(userId);
        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(user);
    }
    @Test
    public void deleteUser_ShouldReturnFalse_WhenUserDoesNotExist() {
        Long userId = 2L;
        given(userService.findUserById(userId)).willThrow(new RuntimeException());
        ResponseEntity<Boolean> response = userController.deleteUser(userId);
        assertEquals(OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
    }
    @Test
    public void deleteUser_ShouldReturnNoContent_WhenUserExists() {
        Long userId = 1L;
        UserEntity existingUser = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", new byte[0], 5, LocalDate.of(1985, 10, 10));
        existingUser.setId(userId);
        given(userService.findUserById(userId)).willReturn(existingUser);
        ResponseEntity<Boolean> response = userController.deleteUser(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(existingUser);
    }
    @Test
    public void deleteUser_ShouldReturnFalse_WhenExceptionOccurs() {
        Long userId = 1L;
        UserEntity existingUser = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", new byte[0], 5, LocalDate.of(1985, 10, 10));
        existingUser.setId(userId);
        given(userService.findUserById(userId)).willReturn(existingUser);
        doThrow(new RuntimeException("Error deleting user")).when(userService).deleteUser(existingUser);
        ResponseEntity<Boolean> response = userController.deleteUser(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
    }
    @Test
    public void deleteUser_ShouldCallServiceDeleteOnce_WhenUserExists() {
        Long userId = 1L;
        UserEntity existingUser = new UserEntity("12345678-9", "Carlos Perez", "carlos@mail.com", "password", new byte[0], 5, LocalDate.of(1985, 10, 10));
        existingUser.setId(userId);
        given(userService.findUserById(userId)).willReturn(existingUser);
        ResponseEntity<Boolean> response = userController.deleteUser(userId);
        verify(userService, times(1)).deleteUser(existingUser);
    }
    @Test
    public void deleteUser_ShouldCallServiceDelete_WhenUserExists() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<Boolean> response = userController.deleteUser(userId);
        verify(userService, times(1)).deleteUser(user);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void simulation_ShouldReturnMonthlyPayment_WhenValidInput() {
        Map<String, String> body = new HashMap<>();
        body.put("amount", "10000");
        body.put("term", "12");
        body.put("interestRate", "5.0");
        given(userService.simulation(10000, 12, 5.0)).willReturn(856);
        ResponseEntity<Integer> response = userController.simulation(body);
        assertEquals(OK, response.getStatusCode());
        assertEquals(856, response.getBody());
    }
    @Test
    public void simulation_ShouldReturnMonthlyPayment_WhenDataIsValid() {
        Map<String, String> body = new HashMap<>();
        body.put("amount", "100000");
        body.put("term", "12");
        body.put("interestRate", "5.0");
        given(userService.simulation(100000, 12, 5.0)).willReturn(856);
        ResponseEntity<Integer> response = userController.simulation(body);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(856, Objects.requireNonNull(response.getBody()).intValue());
    }
    @Test
    public void simulation_ShouldReturnNull_WhenAmountIsInvalid() {
        Map<String, String> body = new HashMap<>();
        body.put("amount", "invalid_amount");
        body.put("term", "12");
        body.put("interestRate", "5.0");
        ResponseEntity<Integer> response = userController.simulation(body);
        assertNull(response);
    }
    @Test
    public void simulation_ShouldReturnNull_WhenServiceThrowsException() {
        Map<String, String> body = new HashMap<>();
        body.put("amount", "100000");
        body.put("term", "12");
        body.put("interestRate", "5.0");
        given(userService.simulation(100000, 12, 5.0)).willThrow(new RuntimeException("Error in service"));
        ResponseEntity<Integer> response = userController.simulation(body);
        assertNull(response);
    }
    @Test
    public void simulation_ShouldReturnNull_WhenMissingParameters() {
        Map<String, String> body = new HashMap<>();
        body.put("amount", "100000");
        ResponseEntity<Integer> response = userController.simulation(body);
        assertNull(response);
    }
    @Test
    public void simulation_ShouldReturnNull_WhenInterestRateIsInvalid() {
        Map<String, String> body = new HashMap<>();
        body.put("amount", "100000");
        body.put("term", "12");
        body.put("interestRate", "invalid_rate");
        ResponseEntity<Integer> response = userController.simulation(body);
        assertNull(response);
    }
    @Test
    public void deposit_ShouldUpdateUser_WhenValidDeposit() {
        Long userId = 1L;
        int depositAccount = 5000;
        given(userService.findUserById(userId)).willReturn(user);
        given(userService.deposit(user, 5000)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.deposit(userId, depositAccount);
        assertEquals(OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
    @Test
    public void deposit_ShouldUpdateBalance_WhenUserHasZeroBalance() {
        Long userId = 1L;
        int depositAccount = 500;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setCurrentSavingsBalance(0);
        given(userService.findUserById(userId)).willReturn(user);
        given(userService.deposit(user, 500)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.deposit(userId, depositAccount);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).getCurrentSavingsBalance());
    }
    @Test
    public void deposit_ShouldReturnNull_WhenUserNotFound() {
        Long userId = 1L;
        int depositAccount = 1000;
        given(userService.findUserById(userId)).willThrow(new RuntimeException("User not found"));
        ResponseEntity<UserEntity> response = userController.deposit(userId, depositAccount);
        assertNull(response);
    }
    @Test
    public void deposit_ShouldReturnNull_WhenDepositAccountIsInvalid() {
        Long userId = 1L;
        int depositAccount = 0;
        UserEntity user = new UserEntity();
        user.setId(userId);
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.deposit(userId, depositAccount);
        assertNotNull(response);
    }
    @Test
    public void deposit_ShouldReturnNull_WhenServiceThrowsException() {
        Long userId = 1L;
        int depositAccount = 52000;
        UserEntity user = new UserEntity();
        user.setId(userId);
        given(userService.findUserById(userId)).willReturn(user);
        given(userService.deposit(user, 1000)).willThrow(new RuntimeException("Error in service"));
        ResponseEntity<UserEntity> response = userController.deposit(userId, depositAccount);
        assertNotNull(response);
    }
    @Test
    public void deposit_ShouldReturnNull_WhenMissingDepositParameter() {
        Long userId = 1L;
        int depositAccount = -1;
        UserEntity user = new UserEntity();
        user.setId(userId);
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.deposit(userId, depositAccount);
        assertNotNull(response);
    }
    @Test
    public void withdrawal_ShouldUpdateUser_WhenValidWithdrawal() {
        Long userId = 1L;
        int withdrawalAccount = 200;
        given(userService.findUserById(userId)).willReturn(user);
        given(userService.withdrawal(user, 2000)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.withdrawal(userId, withdrawalAccount);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response);
    }
    @Test
    public void withdrawal_ShouldReturnNull_WhenWithdrawalExceedsBalance() {
        Long userId = 1L;
        int withdrawalAccount = 3000;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setCurrentSavingsBalance(2000);
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.withdrawal(userId, withdrawalAccount);
        assertNotNull(response);
    }
    @Test
    public void withdrawal_ShouldReturnNull_WhenUserNotFound() {
        Long userId = 1L;
        int withdrawalAccount = 500;
        given(userService.findUserById(userId)).willThrow(new RuntimeException("User not found"));
        ResponseEntity<UserEntity> response = userController.withdrawal(userId, withdrawalAccount);
        assertNull(response);
    }
    @Test
    public void withdrawal_ShouldReturnNull_WhenWithdrawalAccountIsInvalid() {
        Long userId = 1L;
        int withdrawalAccount = 0;
        UserEntity user = new UserEntity();
        user.setId(userId);
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.withdrawal(userId, withdrawalAccount);
        assertNotNull(response);
    }
    @Test
    public void withdrawal_ShouldReturnNull_WhenServiceThrowsException() {
        Long userId = 1L;
        int withdrawalAccount = 500;
        UserEntity user = new UserEntity();
        user.setId(userId);
        given(userService.findUserById(userId)).willReturn(user);
        given(userService.withdrawal(user, 500)).willThrow(new RuntimeException("Error in service"));
        ResponseEntity<UserEntity> response = userController.withdrawal(userId, withdrawalAccount);
        assertNull(response);
    }
    @Test
    public void withdrawal_ShouldReturnNull_WhenMissingWithdrawalParameter() {
        Long userId = 1L;
        int withdrawalAccount = -1;
        UserEntity user = new UserEntity();
        user.setId(userId);
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<UserEntity> response = userController.withdrawal(userId, withdrawalAccount);
        assertNotNull(response);
    }
    @Test
    public void findNameById_ShouldReturnUserName_WhenUserExists() {
        Long userId = 1L;
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<String> response = userController.findNameById(userId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(user.getName(), response.getBody());
    }
    @Test
    public void findNameById_ShouldReturnNull_WhenUserDoesNotExist() {
        Long userId = 2L;
        given(userService.findUserById(userId)).willThrow(new RuntimeException("User not found"));
        ResponseEntity<String> response = userController.findNameById(userId);
        assertNull(response);
    }
    @Test
    public void findNameById_ShouldReturnNull_WhenIdIsNull() {
        ResponseEntity<String> response = userController.findNameById(null);
        assertNull(response);
    }
    @Test
    public void findNameById_ShouldReturnNull_WhenExceptionOccurs() {
        Long userId = 3L;
        given(userService.findUserById(userId)).willThrow(new RuntimeException("Database error"));
        ResponseEntity<String> response = userController.findNameById(userId);
        assertNull(response);
    }
    @Test
    public void findNameById_ShouldReturnOkStatus_WhenUserExists() {
        Long userId = 4L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setName("Maria Lopez");
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<String> response = userController.findNameById(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void findNameById_ShouldReturnEmptyName_WhenUserHasNoName() {
        Long userId = 5L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setName("");
        given(userService.findUserById(userId)).willReturn(user);
        ResponseEntity<String> response = userController.findNameById(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("", response.getBody());
    }
}
