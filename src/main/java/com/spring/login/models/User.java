package com.spring.login.models;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
@Setter
@Getter
@NoArgsConstructor
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String nama;

  private String no_handphone;

  private String email;

  @JsonIgnore
  private String password;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  private String jenis_kelamin;

  @Column(columnDefinition = "BOOLEAN DEFAULT false")
  private boolean isEmailActivated;

  public User(String username, String nama, String no_handphone, String jenis_kelamin, String email, String password) {
    this.username = username;
    this.nama = nama;
    this.no_handphone = no_handphone;
    this.jenis_kelamin = jenis_kelamin;
    this.email = email;
    this.password = password;
  }
}
