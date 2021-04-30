package model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @Type(type="org.hibernate.type.NumericBooleanType")
    private boolean enabled = true;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private List<Authority> authorities = new ArrayList<>();


}
