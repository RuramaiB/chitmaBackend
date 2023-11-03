package me.ruramaibotso.umc.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.Section;
import me.ruramaibotso.umc.token.Token;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private String firstname;
  private String lastname;
  @Enumerated(EnumType.STRING)
  private Gender gender;
  @DateTimeFormat(pattern = "dd/MMM/yyyy")
  private LocalDate dateOfBirth;
  private String phoneNumber;
  @Column(unique = true)
  private String membershipNumber;
  private String password;
  @Enumerated(EnumType.STRING)
  private Role role;
  @Enumerated(EnumType.STRING)
  private Organisation organisation;
  @Enumerated(EnumType.STRING)
  private Membership membershipStatus;
  @JsonBackReference
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<Token> tokens;
  @ManyToOne
  private Locals locals;
  @ManyToOne(fetch = FetchType.EAGER)
  private Section section;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return membershipNumber;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}