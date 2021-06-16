package com.employee.demo.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UnAuthEntryPoint implements AuthenticationEntryPoint {


    /**
     * unauthorized requests go here
     * @param req
     * @param res
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {

        String errorMsg = "{\"status\":401 , \"message\":\"token expired, please login.\"}";
        res.setHeader("Access-Control-Allow-Headers", "content-type, token");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Origin", "*");

        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json; charset=utf-8");
        res.setStatus(401);
        PrintWriter out = res.getWriter();
        out.print(errorMsg);
        out.flush();
        out.close();

    }
}
