package com.anasdidi.security.vo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table("TBL_USER")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = { "password" })
public class UserVO implements Persistable<String> {

  @Id
  @Column("ID")
  private String id;

  @Column("USERNAME")
  private String username;

  @Column("PASSWORD")
  private String password;

  @Column("FULL_NAME")
  private String fullName;

  @Column("EMAIL")
  private String email;

  @Column("LAST_MODIFIED_DT")
  private LocalDateTime lastModifiedDate;

  @Column("VERSION")
  private Integer version;

  @Override
  public boolean isNew() {
    return version == 0;
  }
}
