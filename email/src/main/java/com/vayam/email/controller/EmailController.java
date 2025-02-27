package com.vayam.email.controller;
import com.vayam.email.request.EmailRequest;
import com.vayam.email.service.EmailProducer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/email")
public class EmailController {

    private final EmailProducer emailProducer;

    public EmailController(EmailProducer emailProducer) {
        this.emailProducer = emailProducer;
    }

    @GetMapping("/form")
    public String showEmailForm(Model model) {
        model.addAttribute("emailRequest", new EmailRequest());
        return "send-email";
    }

    @PostMapping("/send")
    public String sendEmail(@ModelAttribute EmailRequest emailRequest, RedirectAttributes redirectAttributes) {
        emailProducer.sendEmailRequest(emailRequest);
        redirectAttributes.addFlashAttribute("message", "Email request sent!");
        return "email-template";
    }
}
