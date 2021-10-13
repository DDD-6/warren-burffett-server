package com.warrenbuffett.server.config.auth;
import com.warrenbuffett.server.domain.User;
import lombok.Getter;
import java.io.Serializable;
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getUser_name();
        this.email = user.getEmail();
        this.picture = user.getImage();
    }
}