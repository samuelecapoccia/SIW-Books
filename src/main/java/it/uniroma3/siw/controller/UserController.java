package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/updateProfile")
	public String updateProfile(
	        @RequestParam("id") Long id,
	        @RequestParam("name") String name,
	        @RequestParam("surname") String surname,
	        @RequestParam("bio") String bio,
	        @RequestParam("birthDate") String birthDate,
	        @RequestParam("birthPlace") String birthPlace,
	        @RequestParam("profilePic") MultipartFile profilePic,
	        Model model) {

	    System.out.println("ID: " + id);
	    System.out.println("Name: " + name);
	    System.out.println("Surname: " + surname);
	    System.out.println("Bio: " + bio);
	    System.out.println("Birth Date: " + birthDate);
	    System.out.println("Birth Place: " + birthPlace);
	    System.out.println("ProfilePic: " + (profilePic != null ? profilePic.getOriginalFilename() : "No file uploaded"));

	    try {
	        User existingUser = userService.findById(id);
	        if (existingUser != null) {
	            existingUser.setName(name);
	            existingUser.setSurname(surname);
	            existingUser.setBio(bio);
	            existingUser.setBirthDate(birthDate);
	            existingUser.setBirthPlace(birthPlace);

	            if (profilePic != null && !profilePic.isEmpty()) {
	                userService.updateUser(existingUser, profilePic);
	            } else {
	                userService.updateUser(existingUser, null);
	            }

	            model.addAttribute("message", "Profilo aggiornato con successo!");
	        } else {
	            model.addAttribute("errorMessage", "Utente non trovato.");
	        }
	    } catch (Exception e) {
	        model.addAttribute("errorMessage", "Errore durante l'aggiornamento del profilo: " + e.getMessage());
	    }

	    return "redirect:/myProfile";
	}

}
