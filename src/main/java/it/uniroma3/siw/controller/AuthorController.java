package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.AuthorService;

@Controller
public class AuthorController {
    
	@Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public String listBooks(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "authors"; // Template per visualizzare tutti i libri
    }

    @GetMapping("/authors/{id}")
    public String viewBook(@PathVariable("id") Long id, Model model) {
        Author author = authorService.findById(id);
     // Controlla se l'autore esiste
        if (author != null) {
            model.addAttribute("author", author);
            return "author"; // Nome del template Thymeleaf per la pagina del singolo autore
        } else {
            // Se l'autore non esiste, reindirizza ad una pagina di errore o alla lista
            return "redirect:/authors";
        }
    }
}