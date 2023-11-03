package me.ruramaibotso.umc.auth;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.config.JwtService;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.Section;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.repository.SectionsRepository;
import me.ruramaibotso.umc.token.Token;
import me.ruramaibotso.umc.token.TokenRepository;
import me.ruramaibotso.umc.token.TokenType;
import me.ruramaibotso.umc.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final SectionsRepository sectionsRepository;
  private final LocalsRepository localsRepository;
  public AuthenticationResponse register(RegisterRequest request) {
    Locals locals = localsRepository.findByName(request.getLocal())
            .orElseThrow(() -> new ResourceNotFoundException("Local was not found"));
    Section section = sectionsRepository.findByLocalsAndName(locals,request.getSection())
            .orElseThrow(() -> new ResourceNotFoundException("Section was not found"));
    Integer IDs = repository.findLastGeneratedId();
    String prefix = locals.getPrefix();
      var user = User.builder()
              .firstname(request.getFirstname())
              .lastname(request.getLastname())
              .gender(request.getGender())
              .dateOfBirth(request.getDateOfBirth())
              .membershipNumber(prefix + String.format("%04d", IDs))
              .membershipStatus(request.getMembershipStatus())
              .phoneNumber(request.getPhoneNumber())
              .organisation(request.getOrganisation())
              .password(passwordEncoder.encode(request.getPassword()))
              .locals(locals)
              .section(section)
              .role(request.getRole())
              .build();
      var savedUser = repository.save(user);
      var jwtToken = jwtService.generateToken(user);
      var membershipNumber = jwtService.getMembershipNumber(user);
      var role = jwtService.getRole(user);
      saveUserToken(savedUser, jwtToken);
      return AuthenticationResponse.builder()
              .token(jwtToken)
              .membershipNumber(membershipNumber)
              .role(role)
              .build();
    }
  public AuthenticationResponse authenticate(AuthenticationRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getMembershipNumber(),
            request.getPassword()
        )
    );

    var user = repository.findUserByMembershipNumber(request.getMembershipNumber())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var userRole = jwtService.getRole(user);
    var membershipNumber = jwtService.getMembershipNumber(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
          .role(userRole)
           .local(user.getLocals().getName())
          .membershipNumber(membershipNumber)
          .token(jwtToken)
          .build();
  }
  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }
  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public List<User> getAllUsers(){
    return repository.findAll();
  }
public Page<User> getAllAccountsByRole(int offset, Role role, String local){
  Locals locals = localsRepository.findByName(local)
          .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
    return repository.findUsersByRoleAndLocals(PageRequest.of(offset, 10), role, locals);
}
public Page<User> getAllAdminAccounts(int offset, Role role){
    return repository.findAllByRole(PageRequest.of(offset, 10), role);
}
  public Optional<User> findUserByMembershipNumber(String membershipNumber){
    return repository.findUserByMembershipNumber(membershipNumber);
  }
  public Optional<List<User>> findUsersByOrganisation(Organisation organisation){
    return repository.findUsersByOrganisation(organisation);
  }
  public Optional<List<User>> findUsersByMembershipStatus(Membership membershipStatus){
    return repository.findUsersByMembershipStatus(membershipStatus);
  }
  public Page<User> getAllUsersPaged(int offset, String local){
    Locals locals = localsRepository.findByName(local)
            .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
    return repository.findAllByLocals(PageRequest.of(offset, 10), locals);
  }

}
