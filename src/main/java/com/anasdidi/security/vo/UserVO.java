package com.anasdidi.security.vo;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Table("TBL_USER")
@AllArgsConstructor
@Data
@ToString(exclude = {"password"})
public class UserVO {

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

  @LastModifiedDate
  @Column("LAST_MODIFIED_DT")
  private Instant lastModifiedDate;

  @LastModifiedBy
  @Column("LAST_MODIFIED_BY")
  private String lastModifiedBy;

  @Version
  @Column("VERSION")
  private Integer version;
}
