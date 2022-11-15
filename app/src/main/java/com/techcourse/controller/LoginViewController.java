package com.techcourse.controller;

import static nextstep.web.support.RequestMethod.GET;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginViewController {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @RequestMapping(value = "/login/view", method = GET)
    public ModelAndView renderLoginView(final HttpServletRequest request, final HttpServletResponse response) {
        return UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return new ModelAndView(new JspView("redirect:/index.jsp"));
                })
                .orElse(new ModelAndView(new JspView("/login.jsp")));
    }
}
