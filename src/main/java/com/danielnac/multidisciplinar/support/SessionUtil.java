package com.danielnac.multidisciplinar.support;

import com.danielnac.multidisciplinar.dto.TokenDTO;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionUtil {

    public static TokenDTO getSession() {
        return (TokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Integer getId() {
        return getSession().getId();
    }

    public static String getCargo() {
        return getSession().getCargo();
    }
}
