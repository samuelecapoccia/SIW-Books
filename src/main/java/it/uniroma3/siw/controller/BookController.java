package it.uniroma3.siw.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.UserService;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private UserService userService;  // Per recuperare l'utente autenticato

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books"; // Template per visualizzare tutti i libri
    }

    @GetMapping("/books/{id}")
    public String viewBook(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("author", book.getAuthor()); // Aggiunge l'autore al modello
        return "book"; // Template per visualizzare i dettagli del libro
    }
    
    @PostMapping("/books/reserve/{id}")
    public String reserveBook(@PathVariable("id") Long bookId, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (bookService.reserveBook(bookId, user)) {
            return "redirect:/books?success";
        } else {
            return "redirect:/books?error";  // Messaggio di errore
        }
    }

    @PostMapping("/books/cancel/{id}")
    public String cancelReservation(@PathVariable("id") Long bookId, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
            return "redirect:/login";
        }

        boolean success = bookService.cancelReservation(bookId, user);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Prenotazione annullata con successo.");
            return "redirect:/books";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Non è stato possibile annullare la prenotazione.");
            return "redirect:/books";
        }
    }
}