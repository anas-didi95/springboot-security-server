package com.anasdidi.security.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "TBL_USER")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = { "password" })
public class UserVO {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  private String id;

  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "last_modified_dt", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModifiedDate;

  @Column(name = "version", nullable = false)
  private Integer version;
}
