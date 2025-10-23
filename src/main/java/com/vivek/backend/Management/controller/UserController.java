package com.vivek.backend.Management.controller;



import com.vivek.backend.Management.dto.SignupDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.exception.UserNotFoundException;
import com.vivek.backend.Management.service.UserService;
import com.vivek.backend.Management.vo.RecentUserVO;
import com.vivek.backend.Management.vo.UserVO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
 @RequestBody — For JSON/XML Payloads
- Purpose: Binds the entire HTTP request body to a Java object.


- Usage: Commonly used in POST and PUT requests where the client sends data in the request body (e.g., JSON or XML) to create or update a resource.

🔗 @RequestParam — For Query/Form Parameters
- Purpose: Extracts simple parameters from the query string or form data.

- Usage: Typically used in GET requests to filter or sort results, or in POST requests to handle form submissions where parameters are sent as key-value pairs.

 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(createdUser); // 201 Created
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<?>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/dto/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody SignupDto signupDto) {
        UserResponseDto registeredUser = userService.registerUser(signupDto);
        return ResponseEntity.status(201).body(registeredUser); // 201 Created
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully with id: " + id);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        String result = userService.verify(email, password);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<RecentUserVO>> getRecentUsers() {
        return ResponseEntity.ok(userService.getRecentUsers());
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<UserVO>> getUserDashboardView() {
        return ResponseEntity.ok(userService.getUserDashboardView());
    }
}