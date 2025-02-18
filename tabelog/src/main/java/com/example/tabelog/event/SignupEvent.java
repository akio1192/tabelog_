package com.example.tabelog.event;

import org.springframework.context.ApplicationEvent;

import com.example.tabelog.entity.User;

import lombok.Getter;

@Getter
public class SignupEvent extends ApplicationEvent {
    private final User user;
    private final String requestUrl;        

    public SignupEvent(Object source, User user, String requestUrl) {
        super(source);
        
        this.user = user;        
        this.requestUrl = requestUrl;
    }
}
