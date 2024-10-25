package PrestaBanco.Crud.Controllers;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import PrestaBanco.Crud.Entities.UserEntity;
import PrestaBanco.Crud.Services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;

    /**
     * Controller that allows obtaining a client by id.
     * @param id A Long with the client's id to search.
     * @return A UserEntity with the client's data found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id) {
        try {
            // The user is searched in the database.
            UserEntity user = userService.findUserById(id);
            // The user found is returned.
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // If the user is not found, return null.
            return null;
        }
    }

    /**
     * Controller that allows obtaining all the clients in the database.
     * @return A List with all the clients found.
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        // All users are searched in the database.
        List<UserEntity> users = userService.findAll();
        // The users found are returned.
        return ResponseEntity.ok(users);
    }

    /**
     * Controller that allows creating a new client.
     * @param body A Map with the client's data to save.
     * @return A UserEntity with the client's data saved.
     */
    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<UserEntity> registerUser( @RequestParam("file") MultipartFile file, 
                            @RequestParam("identifyingDocument") String identifyingDocument, 
                            @RequestParam("name") String name, 
                            @RequestParam("email") String email, 
                            @RequestParam("password") String password,
                            @RequestParam("jobSeniority") int jobSeniority,
                            @RequestParam("birthdate") LocalDate birthdate) {
        try {
            String filename = file.getOriginalFilename();
            // If the file is a PDF, the client is saved in the database.
            if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
                // The file is converted to bytes.
                byte[] pdfBytes = file.getBytes();
                UserEntity user = new UserEntity(identifyingDocument, name, email, password, pdfBytes, jobSeniority, birthdate);
                user.setCreationDate(LocalDate.now());
                user.setCurrentSavingsBalance(0);
                user.setSavingsAccountHistory("");
                user.setDepositAccount(""); 
                user.setWithdrawalAccount("");
                userService.saveUser(user);
                // The user is saved in the database.
                return ResponseEntity.ok(user);
            } else {
                // If the file is not a PDF, return null.
                return null;
            }
        } catch (Exception e) {
            // If there is an error, return null.
            return null;
        }
    }

    /**
     * Controller that allows updating a client's data.
     * @param body A Map with the client's data to update.
     * @return A UserEntity with the client's data updated.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<UserEntity> updateUser(
        @RequestParam("file") MultipartFile file, 
        @RequestParam("email") String email,
        @PathVariable Long id) {
        try {
            String filename = file.getOriginalFilename();
            if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
                byte[] pdfBytes = file.getBytes();
                UserEntity user = userService.findById(id);
                user.setFile(pdfBytes);
                user.setEmail(email);
                userService.saveUser(user);
                return ResponseEntity.ok(user);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Controller that allows deleting a client.
     * @param id A Long with the client's id to delete.
     * @return A boolean that indicates if the client was deleted.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        try {
            // The user is searched in the database.
            UserEntity user = userService.findUserById(id);
            // The user is deleted from the database.
            userService.deleteUser(user);
            // If the user is deleted, return true.
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // If the user is not found, return false.
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Controller that allows simulating a mortgage credit for a client.
     * @param body A Map with the data of the mortgage credit to simulate.
     * @return An Integer with the monthly payment of the mortgage credit.
     */
    @PostMapping("/simulation")
    public ResponseEntity<Integer> simulation(@RequestBody Map<String, String> body) {
        try {
            // The amount of the loan is obtained.
            int amount = Integer.parseInt(body.get("amount"));
            // The term of the loan is obtained.
            int term = Integer.parseInt(body.get("term"));
            // The interest rate of the loan is obtained.
            double interestRate = Double.parseDouble(body.get("interestRate"));
            // The monthly payment of the loan is calculated.
            int monthlyPayment = userService.simulation(amount, term, interestRate);
            // The monthly payment of the loan is returned.
            return ResponseEntity.ok(monthlyPayment);
        } catch (Exception e) {
            // If there is an error, return 0.
            return null;
        }
    }

    /**
     * Controller that allows depositing money into a client's account.
     * @param id A Long with the client's id to deposit money.
     * @param body A Map with the data of the deposit to make.
     * @return A UserEntity with the client's data updated.
     */
    @PutMapping("/deposit")
    public ResponseEntity<UserEntity> deposit(@RequestParam("userId") Long id, @RequestParam("depositAccount") int depositAccount) {
        try {
            // The user is searched in the database.
            UserEntity user = userService.findUserById(id);
            // The deposit is made in the user's account.
            UserEntity userModify = userService.deposit(user, depositAccount);
            // The user's data is returned.
            return ResponseEntity.ok(userModify);
        } catch (Exception e) {
            // If there is an error, return null.
            return null;
        }
    }

    /**
     * Controller that allows withdrawing money from a client's account.
     * @param id A Long with the client's id to withdraw money.
     * @param body A Map with the data of the withdrawal to make.
     * @return A UserEntity with the client's data updated.
     */
    @PutMapping("/withdrawal")
    public ResponseEntity<UserEntity> withdrawal(@RequestParam("userId") Long id, @RequestParam("withdrawalAccount") int withdrawalAccount) {
        try {
            // The user is searched in the database.
            UserEntity user = userService.findUserById(id);
            // The withdrawal is made in the user's account.
            UserEntity userModify = userService.withdrawal(user, withdrawalAccount);
            // The user's data is returned.
            return ResponseEntity.ok(userModify);
        } catch (Exception e) {
            // If there is an error, return null.
            return null;
        }
    }

    /**
     * Controller that allows finding a client's name by id.
     * @param id A Long with the client's id to find the name.
     * @return A String with the client's name found.
     */
    @GetMapping("/users/name/{id}")
    public ResponseEntity<String> findNameById(@PathVariable Long id) {
        try {
            // The user is searched in the database.
            UserEntity user = userService.findUserById(id);
            // The user's name is returned.
            return ResponseEntity.ok(user.getName());
        } catch (Exception e) {
            // If the user is not found, return null.
            return null;
        }
    }
}
