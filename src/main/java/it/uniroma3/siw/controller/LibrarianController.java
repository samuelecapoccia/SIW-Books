package it.uniroma3.siw.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import jakarta.validation.Valid;

@Controller
public class LibrarianController {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/librarian/addBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showAddBookForm(Model model, Authentication auth) throws AccessDeniedException{
    	System.out.println(credentialsService.getCredentials(auth.getName()).getRole());
    	if(auth == null || credentialsService.getCredentials(auth.getName()).getRole().equals(Credentials.USER_ROLE)){
    		throw new AccessDeniedException("Non autorizzato");
    	}
    		model.addAttribute("book", new Book());
            model.addAttribute("authors", bookService.findAllAuthors());
            return "librarian/formNewBook"; // Template per aggiungere un nuovo libro
    	}

    @PostMapping("/librarian/addBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String submitAddBookForm(@Valid @ModelAttribute("book") Book book, BindingResult bookBR,
                                    @RequestParam("file") MultipartFile coverImage,
                                    Authentication auth, RedirectAttributes redirectAttributes) {
        
    	
    	
    	if (!bookBR.hasErrors()) {
            bookService.saveBook(book, coverImage);
            redirectAttributes.addFlashAttribute("message", "Libro aggiunto con successo!");
        } else {
            redirectAttributes.addFlashAttribute("bookError", true);
        }
        return "redirect:/books";
    }

    @GetMapping("/librarian/editBook")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String showEditBookForm(@RequestParam Long id, Model model) {
        Book book = bookService.findById(id);
        if (book == null) {
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        model.addAttribute("authors", bookService.findAllAuthors());
        return "librarian/formEditBook"; // Template per modificare un libro
    }

    @PostMapping("/librarian/editBook/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String submitEditBookForm(@PathVariable("id") Long id,
                                     @Valid @ModelAttribute("book") Book newBook, 
                                     BindingResult bookBR,
                                     @RequestParam(value = "file", required = false) MultipartFile coverImage,
                                     Authentication auth,
                                     RedirectAttributes redirectAttributes) {
        if (!bookBR.hasErrors()) {
            Book existingBook = bookService.findById(id);
            if (coverImage != null && !coverImage.isEmpty()) {
                bookService.updateBook(existingBook, newBook, coverImage);
            } else {
                bookService.updateBook(existingBook, newBook, null);
            }
            redirectAttributes.addFlashAttribute("message", "Libro aggiornato con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/books";
    }

    @PostMapping("/librarian/deleteBook")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String deleteBook(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBookById(id);
        redirectAttributes.addFlashAttribute("message", "Libro eliminato con successo!");
        return "redirect:/books";
    }
    
    @GetMapping("/librarian/addAuthor")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showAddAuthorForm(Model model) {
    		model.addAttribute("author", new Author());
            return "librarian/formNewAuthor"; // Template per aggiungere un nuovo libro
    	}

    @PostMapping("/librarian/addAuthor")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String submitAddAuthorForm(@Valid @ModelAttribute("author") Author author, BindingResult authorBR,
                                    @RequestParam("file") MultipartFile profilePic,
                                    Authentication auth, RedirectAttributes redirectAttributes) {
        
    	
    	
    	if (!authorBR.hasErrors()) {
            authorService.saveAuthor(author, profilePic);
            redirectAttributes.addFlashAttribute("message", "Libro aggiunto con successo!");
        } else {
            redirectAttributes.addFlashAttribute("bookError", true);
        }
        return "redirect:/authors";
    }
    
    @GetMapping("/librarian/editAuthor")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String showEditAuthorForm(@RequestParam Long id, Model model) {
        Author author = authorService.findById(id);
        if (author == null) {
            return "redirect:/authors";
        }
        model.addAttribute("author", author);
        return "librarian/formEditAuthor"; // Template per modificare un autore
    }

    @PostMapping("/librarian/editAuthor/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String submitEditAuthorForm(@PathVariable("id") Long id,
                                     @Valid @ModelAttribute("author") Author newAuthor, 
                                     BindingResult authorBR,
                                     @RequestParam(value = "file", required = false) MultipartFile profilePic,
                                     Authentication auth,
                                     RedirectAttributes redirectAttributes) {
        if (!authorBR.hasErrors()) {
            Author existingAuthor = authorService.findById(id);
            if (profilePic != null && !profilePic.isEmpty()) {
                authorService.updateAuthor(existingAuthor, newAuthor, profilePic);
            } else {
                authorService.updateAuthor(existingAuthor, newAuthor, null);
            }
            redirectAttributes.addFlashAttribute("message", "Autore aggiornato con successo!");
        } else {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/authors";
    }

    @PostMapping("/librarian/deleteAuthor")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public String deleteAuthor(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        authorService.deleteAuthorById(id);
        redirectAttributes.addFlashAttribute("message", "Libro eliminato con successo!");
        return "redirect:/authors";
    }
}