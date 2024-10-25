package PrestaBanco.Crud.Repositories;

import PrestaBanco.Crud.Entities.UserEntity;
import PrestaBanco.Crud.Services.UserService;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
public class UserRepositoryTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void findByEmail_ShouldReturnUser_WhenEmailExists() {
        String email = "test@example.com";
        UserEntity expectedUser = new UserEntity("12345678-9", "Carlos Perez", email, "password", null, 5, LocalDate.of(1985, 10, 10));
        given(userRepository.findByEmail(email)).willReturn(expectedUser);
        UserEntity actualUser = userService.findByEmail(email);
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findByEmail(email);
    }
    @Test
    public void findByEmail_ShouldReturnNull_WhenEmailDoesNotExist() {
        String email = "notfound@example.com";
        given(userRepository.findByEmail(email)).willReturn(null);
        UserEntity actualUser = userService.findByEmail(email);
        assertNull(actualUser);
        verify(userRepository, times(1)).findByEmail(email);
    }
    @Test
    public void findByEmail_ShouldReturnUser_WhenEmailIsEmpty() {
        String email = "";
        given(userRepository.findByEmail(email)).willReturn(null);
        UserEntity actualUser = userService.findByEmail(email);
        assertNull(actualUser);
        verify(userRepository, times(1)).findByEmail(email);
    }
    @Test
    public void findByEmail_ShouldReturnUser_WhenEmailIsNull() {
        String email = null;
        given(userRepository.findByEmail(email)).willReturn(null);
        UserEntity actualUser = userService.findByEmail(email);
        assertNull(actualUser);
        verify(userRepository, times(1)).findByEmail(email);
    }
    @Test
    public void findByEmail_ShouldCallRepositoryOnce_WhenCalled() {
        String email = "test@example.com";
        UserEntity expectedUser = new UserEntity("12345678-9", "Carlos Perez", email, "password", null, 5, LocalDate.of(1985, 10, 10));
        given(userRepository.findByEmail(email)).willReturn(expectedUser);
        userService.findByEmail(email);
        verify(userRepository, times(1)).findByEmail(email);
    }
    @Test
    public void findByEmail_ShouldHandleSpecialCharacters() {
        String email = "test+filter@example.com";
        UserEntity expectedUser = new UserEntity("12345678-9", "Carlos Perez", email, "password", null, 5, LocalDate.of(1985, 10, 10));
        given(userRepository.findByEmail(email)).willReturn(expectedUser);
        UserEntity actualUser = userService.findByEmail(email);
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findByEmail(email);
    }
    @Test
    public void findByIdentifyingDocument_ShouldReturnUser_WhenDocumentExists() {
        String doc = "12345678-9";
        UserEntity expectedUser = new UserEntity(doc, "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
        given(userRepository.findByIdentifyingDocument(doc)).willReturn(expectedUser);
        UserEntity actualUser = userService.findByIdentifyingDocument(doc);
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findByIdentifyingDocument(doc);
    }
    @Test
    public void findByIdentifyingDocument_ShouldReturnNull_WhenDocumentDoesNotExist() {
        String doc = "notfound-doc";
        given(userRepository.findByIdentifyingDocument(doc)).willReturn(null);
        UserEntity actualUser = userService.findByIdentifyingDocument(doc);
        assertNull(actualUser);
        verify(userRepository, times(1)).findByIdentifyingDocument(doc);
    }
    @Test
    public void findByIdentifyingDocument_ShouldReturnUser_WhenDocumentIsEmpty() {
        String doc = "";
        given(userRepository.findByIdentifyingDocument(doc)).willReturn(null);
        UserEntity actualUser = userService.findByIdentifyingDocument(doc);
        assertNull(actualUser);
        verify(userRepository, times(1)).findByIdentifyingDocument(doc);
    }
    @Test
    public void findByIdentifyingDocument_ShouldReturnUser_WhenDocumentIsNull() {
        String doc = null;
        given(userRepository.findByIdentifyingDocument(doc)).willReturn(null);
        UserEntity actualUser = userService.findByIdentifyingDocument(doc);
        assertNull(actualUser);
        verify(userRepository, times(1)).findByIdentifyingDocument(doc);
    }
    @Test
    public void findByIdentifyingDocument_ShouldCallRepositoryOnce_WhenCalled() {
        String doc = "12345678-9";
        UserEntity expectedUser = new UserEntity(doc, "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
        given(userRepository.findByIdentifyingDocument(doc)).willReturn(expectedUser);
        userService.findByIdentifyingDocument(doc);
        verify(userRepository, times(1)).findByIdentifyingDocument(doc);
    }
    @Test
    public void findByIdentifyingDocument_ShouldHandleSpecialDocumentCharacters() {
        String doc = "1234-5678";
        UserEntity expectedUser = new UserEntity(doc, "Carlos Perez", "carlos@mail.com", "password", null, 5, LocalDate.of(1985, 10, 10));
        given(userRepository.findByIdentifyingDocument(doc)).willReturn(expectedUser);
        UserEntity actualUser = userService.findByIdentifyingDocument(doc);
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findByIdentifyingDocument(doc);
    }
}

