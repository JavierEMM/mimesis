package com.mimesis.google;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User, Serializable {

    private OAuth2User oAuth2User;

    public CustomOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public void setRole(String rol){

    }

    public String getEmail(){
        return oAuth2User.getAttribute("email");
    }

    public String getFirstName(){
        return oAuth2User.getAttribute("given_name");
    }
    public String getLastName(){
        return oAuth2User.getAttribute("family_name");
    }
    public Boolean getVerification(){
        return oAuth2User.getAttribute("email_verified");
    }

}
