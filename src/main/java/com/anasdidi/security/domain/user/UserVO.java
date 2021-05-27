package com.anasdidi.security.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
class UserVO {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  private String id;

  @Column(name = "username", unique = true, nullable = false)
  private String username;

  static UserVO fromDTO(UserDTO dto) {
    UserVO vo = new UserVO();
    vo.setUsername(dto.username);
    return vo;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


}
