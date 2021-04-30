package model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity(name="authorities")
@NoArgsConstructor
public class Authority {
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_DBA = "ROLE_DBA";

    private @EmbeddedId AuthorityId id;

    public Authority(String username, String role){
        id = new AuthorityId(username, role);
    }

}

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
class AuthorityId implements Serializable{
    String username;

    @Column
    String authority;
}