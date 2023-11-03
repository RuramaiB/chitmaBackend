package me.ruramaibotso.umc.auth;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.*;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.repository.SectionsRepository;
import me.ruramaibotso.umc.user.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final UserRepository userRepository;
  private final SectionsRepository sectionsRepository;
  private final LocalsRepository localsRepository;
  private final PasswordEncoder passwordEncoder;
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(service.authenticate(request));
  }
  @GetMapping("getNumOfUsers")
  public  Long numOfUsers(){
    return  userRepository.count();
  }
  @GetMapping("/getAllUsers")
  public List<User> getAllApplications(){
    return service.getAllUsers();
  }
  @GetMapping("/getAllUsersAccounts/{offset}/{local}")
  public Page<User> getAllUsersAccounts(@PathVariable Integer offset, @PathVariable String local){
    return service.getAllAccountsByRole(offset, Role.User, local);
  }
  @GetMapping("/getAllFinanceAccounts/{offset}/{local}")
  public Page<User> getAllFinanceAccounts(@PathVariable Integer offset, @PathVariable String local){
    return service.getAllAccountsByRole(offset, Role.Finance, local);
  }
  @GetMapping("/getAllAdminAccounts/{offset}")
  public Page<User> getAllAdminAccounts(@PathVariable Integer offset){
    return service.getAllAdminAccounts(offset,Role.Admin);
  }
  @GetMapping("/getAllUsersPaged/{offset}/{local}")
  public Page<User> getAllUsers(@PathVariable int offset, @PathVariable String local){
      return service.getAllUsersPaged(offset, local);
  }
  @GetMapping("getUserByMembershipNumber/{membershipNumber}")
  public Optional<User> getUserByMembershipNumber(@PathVariable String membershipNumber){
    return service.findUserByMembershipNumber(membershipNumber);
  }
  @GetMapping("/{organisation}")
  public Optional<List<User>> getUsersByOrganisation(@PathVariable Organisation organisation){
    return service.findUsersByOrganisation(organisation);
  }
  @GetMapping("/getSectionMembersBySection/{sectionName}")
  public List<User> getSectionMembers(@PathVariable String sectionName){
    Section section = sectionsRepository.findByName(sectionName)
            .orElseThrow(()-> new ResourceNotFoundException("Section not found"));
      return userRepository.findUsersBySection(section)
            .orElseThrow(() -> new ResourceNotFoundException("Users not found"));
  }
  @GetMapping("/{membershipStatus}")
    public Optional<List<User>> getUsersByMembershipStatus(@PathVariable Membership membershipStatus){
    return service.findUsersByMembershipStatus(membershipStatus);
    }
  @GetMapping(value = "/setAccountToAdmin/{membershipNumber}")
  public String setAccountToAdmin( @PathVariable String membershipNumber){
    User user = service.findUserByMembershipNumber(membershipNumber)
            .orElseThrow(() -> new ResourceNotFoundException(("User does not exist")));
    user.setRole(Role.Admin);
    userRepository.save(user);
    return "Account now has admin privileges.";
  }
  @GetMapping(value = "/setAccountToUser/{membershipNumber}")
  public String setAccountToUser( @PathVariable String membershipNumber){
    User user = service.findUserByMembershipNumber(membershipNumber)
            .orElseThrow(() -> new ResourceNotFoundException(("User does not exist")));
    user.setRole(Role.User);
    userRepository.save(user);
    return "Account now has user privileges.";
  }
  @PutMapping("/updateUserByMembershipNumber/{membershipNumber}")
  public User updateUserByMembershipNumber(@PathVariable String membershipNumber, @RequestBody RegisterRequest request){
    User user = userRepository.findUserByMembershipNumber(membershipNumber)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    Locals locals = localsRepository.findByName(request.getLocal())
            .orElseThrow(() -> new ResourceNotFoundException("Local was not found"));
    Section section = sectionsRepository.findByLocalsAndName(locals,request.getSection())
            .orElseThrow(() -> new ResourceNotFoundException("Section was not found"));
    user.setFirstname(request.getFirstname());
    user.setLastname(request.getLastname());
    user.setGender(request.getGender());
    user.setDateOfBirth(request.getDateOfBirth());
    user.setRole(request.getRole());
    user.setOrganisation(request.getOrganisation());
    user.setMembershipStatus(request.getMembershipStatus());
    user.setLocals(locals);
    user.setSection(section);
    user.setPassword(user.getPassword());
    return userRepository.save(user);
  }

  @PutMapping("/updateAdminByMembershipNumber/{membershipNumber}")
  public User updateAdminByMembershipNumber(@PathVariable String membershipNumber, @RequestBody AdminRegisterRequest adminRegisterRequest){
    User user = userRepository.findUserByMembershipNumber(membershipNumber)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    Locals locals = localsRepository.findByName(adminRegisterRequest.getLocal())
            .orElseThrow(() -> new ResourceNotFoundException("Local was not found"));
    user.setFirstname(adminRegisterRequest.getFirstname());
    user.setLastname(adminRegisterRequest.getLastname());
    user.setGender(adminRegisterRequest.getGender());
    user.setDateOfBirth(adminRegisterRequest.getDateOfBirth());
    user.setLocals(locals);
    user.setPassword(user.getPassword());
    return userRepository.save(user);
  }

  @PutMapping("/updatePasswordByMembershipNumber/{membershipNumber}/{password}")
  public String updatePasswords(@PathVariable String membershipNumber, @PathVariable String password){
    User user = userRepository.findUserByMembershipNumber(membershipNumber)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
    return "Password updated successfully";
  }


  @DeleteMapping("/deleteUserByID/{ID}")
  public String deleteUserByID(@PathVariable Integer ID){
    User user = userRepository.findById(ID)
            .orElseThrow(()-> new ResourceNotFoundException("User not found"));
    userRepository.delete(user);
    return "User deleted successfully";
  }
  @DeleteMapping("/deleteUserByMembershipNumber/{membershipNumber}")
  public String deleteUserByMembershipNumber(@PathVariable String membershipNumber){
    User user = userRepository.findUserByMembershipNumber(membershipNumber)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    userRepository.delete(user);
    return "User deleted successfully.";
  }


}
