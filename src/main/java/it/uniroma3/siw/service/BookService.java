package it.uniroma3.siw.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.AuthorRepository;
import it.uniroma3.siw.repository.BookRepository;
import jakarta.transaction.Transactional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private AuthorRepository authorRepository;
    
    // Percorso per salvare le immagini dei libri
    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/books/";

    public boolean reserveBook(Long bookId, User user) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null || book.getReservedBy() != null) {
            return false;  // Il libro è già prenotato o non esiste
        }

        // Controlla se l'utente ha già prenotato un libro
        if (!user.getReservedBooks().isEmpty()) {
            return false;  // L'utente ha già prenotato un libro
        }

        // Prenota il libro
        book.setReservedBy(user);
        bookRepository.save(book);
        return true;
    }

    public boolean cancelReservation(Long bookId, User user) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null || book.getReservedBy() == null || !book.getReservedBy().getId().equals(user.getId())) {
            return false;  // Il libro non è prenotato da questo utente o non esiste
        }

        // Rimuovi la prenotazione
        book.setReservedBy(null);
        bookRepository.save(book);
        return true;  // Prenotazione annullata con successo
    }
    public List<Book> getAvailableBooks() {
        return bookRepository.findByReservedByNull();
    }

    public List<Book> getReservedBooks(User user) {
        return bookRepository.findByReservedBy(user);
    }
    
    /**
     * Metodo per salvare un libro.
     * @param book
     * @param coverImage
     * @return
     */
    @Transactional
    public void saveBook(Book book, MultipartFile coverImage) {
        // Salva l'immagine di copertura se è stata caricata
        if (coverImage != null && !coverImage.isEmpty()) {
            try {
                byte[] bytes = coverImage.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + coverImage.getOriginalFilename());
                Files.write(path, bytes);

                // Imposta il percorso dell'immagine nel libro
                book.setMainImagePath("/images/books/" + coverImage.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bookRepository.save(book);
    }

    /**
     * Metodo per aggiornare un libro esistente.
     * @param oldBook
     * @param newBook
     * @param coverImage
     */
    @Transactional
    public void updateBook(Book oldBook, Book newBook, MultipartFile coverImage) {
        // Aggiorna i dettagli del libro
        if (newBook.getTitle() != null && !newBook.getTitle().isBlank()) {
            oldBook.setTitle(newBook.getTitle());
        }
        // Controllo sull'autore: non è necessario verificare con isBlank, basta verificare che non sia nullo
        if (newBook.getAuthor() != null) {
            oldBook.setAuthor(newBook.getAuthor());
        }
        if (newBook.getPublicationYear() != 0) {
            oldBook.setPublicationYear(newBook.getPublicationYear());
        }
        if (newBook.getGenre() != null && !newBook.getGenre().isBlank()) {
            oldBook.setGenre(newBook.getGenre());
        }
        if (newBook.getIsbn() != null && !newBook.getIsbn().isBlank()) {
            oldBook.setIsbn(newBook.getIsbn());  // Aggiungi il controllo per l'ISBN
        }

        // Aggiorna l'immagine di copertura solo se è stato caricato un nuovo file
        if (coverImage != null && !coverImage.isEmpty()) {
            String filePath = saveBookImage(coverImage); // Salva l'immagine e ottieni il percorso
            oldBook.setMainImagePath(filePath); // Imposta il percorso dell'immagine
        }

        bookRepository.save(oldBook);
    }

    /**
     * Metodo per salvare un'immagine di copertura del libro.
     * @param file
     * @return
     */
    private String saveBookImage(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path path = Paths.get(UPLOADED_FOLDER + fileName);

        try {
            Files.write(path, file.getBytes());
            return "/images/books/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo per recuperare tutti i libri.
     * @return
     */
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * Metodo per trovare un libro per ID.
     * @param id
     * @return
     */
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * Metodo per cancellare un libro per ID.
     * @param id
     */
    @Transactional
    public void deleteBookById(Long id) {
        // Controlla se il libro esiste prima di eliminarlo
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            bookRepository.deleteById(id); // Elimina il libro
        } else {
            // Gestisci il caso in cui il libro non esista
            throw new IllegalArgumentException("Il libro con ID " + id + " non esiste.");
        }
    }
    
    public Iterable<Author> findAllAuthors() {
        return authorRepository.findAll();
    }
}