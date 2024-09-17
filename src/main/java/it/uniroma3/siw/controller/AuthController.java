package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.RegistrationForm;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import jakarta.validation.Valid;

@Controller
@Validated
public class AuthController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private CredentialsValidator credentialsValidator;
    
    @Autowired
    private BookService bookService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showUserRegisterForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "userRegister";
    }

 /*  @GetMapping("/register/librarian")
    public String showLibrarianRegisterForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "librarianRegister";
    }*/

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm,
                               BindingResult result, @RequestParam("profilePic") MultipartFile profilePic,
                               RedirectAttributes redirectAttributes) throws IOException {

        Credentials credentials = registrationForm.getCredentials();
        credentialsValidator.validate(credentials, result);

        if (!result.hasErrors()) {

            User user = registrationForm.getUser();
            credentialsService.saveUserCredentials(credentials, user, profilePic);
            return "redirect:/login";
        }
        
        redirectAttributes.addAttribute("error", true);
        return "redirect:/register";
    }

   /* @PostMapping("/register/librarian")
    public String registerLibrarian(@Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm,
                                    BindingResult result, @RequestParam("file") MultipartFile profilePic,
                                    RedirectAttributes redirectAttributes) throws IOException {

        Credentials credentials = registrationForm.getCredentials();
        credentialsValidator.validate(credentials, result);

        if (!result.hasErrors()) {
        	Librarian librarian = registrationForm.getLibrarian();
            credentialsService.saveLibrarianCredentials(credentials, librarian, profilePic);
            return "redirect:/login";
        }
        
        redirectAttributes.addAttribute("error", true);
        return "redirect:/register";
    }*/
	@GetMapping("/myProfile")
	public String myProfile(Authentication auth, Model model) {
	    // Recupera le credenziali dell'utente autenticato
	    Credentials credentials = credentialsService.getCredentials(auth.getName());
	    User user = credentials.getUser();

	    // Aggiungi il presidente al modello, anche se manca il nome o il cognome
	    if (user != null) {
	        model.addAttribute("user", user);
	        model.addAttribute("reservedBooks", bookService.getReservedBooks(user));
	        return "userPage";
	    }
	    
	    // Se l'utente non è un presidente, mostra un'altra pagina o messaggio
	    model.addAttribute("message", "Il profilo non è completo o l'utente non è registrato.");
	    return "librarianPage"; // Puoi mostrare una pagina alternativa qui
	}
}