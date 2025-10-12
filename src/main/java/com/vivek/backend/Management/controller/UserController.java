package com.vivek.backend.Management.controller;



import com.vivek.backend.Management.dto.SignupDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
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

    public UserController(UserService userService)
    {
        this.userService = userService;
    }


    // only admin can create user
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('UDEMY_WRITE')")
    public User createUser(@RequestBody User user)
    {

        return userService.createUser(user);
    }


    // get all users


       /*
    @GetMapping             // this api uses jpa
    @PreAuthorize("hasAuthority('UDEMY_READ')")
    public List<UserResponseDto> getAllUser()
    {
        return userService.getAllUser();
    }

    */

    @GetMapping("/all")             // this api uses custom dao
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }


        // get user by id

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('UDEMY_READ')")
    public UserResponseDto getUserById(@PathVariable Long id)
    {
        return userService.getUserById(id);
    }






    // user registration by self (it does not require any permission)

    @PostMapping("/dto/register")
    public UserResponseDto registerUser(@RequestBody SignupDto signupDto)
    {
        return userService.registerUser(signupDto);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('UDEMY_DELETE')")
    public String deleteUserById(@PathVariable Long id)
    {
            userService.deleteUserById(id);
             return "User deleted successfully with id: " + id;
    }



    //api for for update user



    //login

    @PostMapping("/login")
    public String login(@RequestParam String email , @RequestParam String password)
    {

        return userService.verify(email,password );

    }



    // use -- DAO

    // get all the users registered in last 7 days with role USER
    @GetMapping("/recent")
    public ResponseEntity<List<RecentUserVO>> getRecentUsers() {
        return ResponseEntity.ok(userService.getRecentUsers());
    }



    // get user dashboard view (only name, email, role)

    @GetMapping("/dashboard")
    public ResponseEntity<List<UserVO>> getUserDashboardView() {
        return ResponseEntity.ok(userService.getUserDashboardView());
    }




}
