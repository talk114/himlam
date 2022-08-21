package vn.nganluong.naba.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @GetMapping("/403")
	public String dashboard() {
		return "403";
	}

    @GetMapping("/login")
    public String login(HttpServletRequest request,
                        HttpServletResponse response, Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");

        if (logout != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
                model.addAttribute("message", "Bạn đã đăng xuất thành công.");
            }
        }


        return "login";
	}

}
