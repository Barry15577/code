package io.java.modules.sys.jwt;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 *
 * @author Jiasi Lu
 */
public class JWTToken implements AuthenticationToken {
    private String token;

    public JWTToken(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
