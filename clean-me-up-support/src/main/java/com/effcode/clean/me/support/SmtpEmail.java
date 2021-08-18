package com.effcode.clean.me.support;

import java.util.Arrays;

public class SmtpEmail {

    public String username;
    public String password;
    public String [] adrs;
    public String subject;
    public String content;
    
    @Override
    public String toString() {
        return "SmtpEmail [username=" + username + ", password=" + password + ", adrs=" + Arrays.toString(adrs)
                + ", subject=" + subject + ", content=" + content + "]";
    }
    
}
