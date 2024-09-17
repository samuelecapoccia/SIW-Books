package it.uniroma3.siw.usersDataLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Librarian;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.AuthorRepository;
import it.uniroma3.siw.repository.BookRepository;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.LibrarianRepository;
import it.uniroma3.siw.repository.UserRepository;

@Component
public class SampleDataLoader implements CommandLineRunner {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Creazione dell'utente normale
        User user = new User();
        user.setName("Giuseppe");
        user.setSurname("Verdi");
        user.setProfilePic("/images/profilePics/president.jpg");
        user.setBio("ciao");
        user.setBirthDate("17/01/2003");
        user.setBirthPlace("Roma");

        Credentials userCredentials = new Credentials();
        userCredentials.setEmail("giuseppe.verdi@example.com");
        userCredentials.setPassword(passwordEncoder.encode("userpass"));
        userCredentials.setRole(Credentials.USER_ROLE);
        userCredentials.setUser(user);  // Associa l'utente alle credenziali

        user.setCredentials(userCredentials);
        userRepository.save(user);
        credentialsRepository.save(userCredentials);

        // Creazione del bibliotecario
        Librarian librarian = new Librarian();
        librarian.setName("Mario");
        librarian.setSurname("Rossi");
        librarian.setIdCode("LIB12345");
        librarian.setDepartment("Reference");

        Credentials librarianCredentials = new Credentials();
        librarianCredentials.setEmail("mario.rossi@example.com");
        librarianCredentials.setPassword(passwordEncoder.encode("libpass"));
        librarianCredentials.setRole(Credentials.LIBRARIAN_ROLE);
        librarianCredentials.setLibrarian(librarian);  // Associa il bibliotecario alle credenziali

        librarian.setCredentials(librarianCredentials);
        librarianRepository.save(librarian);
        credentialsRepository.save(librarianCredentials);

        // Creazione di autori
        Author author1 = createAuthor("Umberto", "Eco", "05/01/1932", "Filosofo, semiologo e scrittore italiano.", "/images/authors/umberto_eco.jpg");
        Author author2 = createAuthor("George", "Orwell", "25/06/1903", "Scrittore e giornalista inglese, noto per la critica dei regimi totalitari.", "/images/authors/george_orwell.jpg");
        Author author3 = createAuthor("Giuseppe", "Tomasi di Lampedusa", "23/12/1896", "Scrittore italiano, autore de Il Gattopardo.", "/images/authors/tomasi_di_lampedusa.jpg");

        // Salvataggio degli autori
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);

        // Creazione di libri associati agli autori
        createBook("Il Nome della Rosa", author1, 1980, "Storico", "/images/books/nome_della_rosa.jpg", "000000000");
        createBook("1984", author2, 1949, "Dystopian", "/images/books/1984.jpg", "111111111");
        createBook("Il Gattopardo", author3, 1958, "Romanzo", "/images/books/gattopardo.jpg", "22222222");

        // Nuovi autori e libri
        Author author4 = createAuthor("J.K.", "Rowling", "31/07/1965", "Autrice britannica, nota per la serie di Harry Potter.", "/images/authors/jk_rowling.jpg");
        Author author5 = createAuthor("J.R.R.", "Tolkien", "03/01/1892", "Scrittore e filologo britannico, noto per Il Signore degli Anelli.", "/images/authors/jrr_tolkien.jpg");
        Author author6 = createAuthor("Agatha", "Christie", "15/09/1890", "Autrice britannica di romanzi gialli e polizieschi.", "/images/authors/agatha_christie.jpg");

        // Salvataggio dei nuovi autori
        authorRepository.save(author4);
        authorRepository.save(author5);
        authorRepository.save(author6);

        // Creazione di nuovi libri associati ai nuovi autori
        createBook("Harry Potter e la Pietra Filosofale", author4, 1997, "Fantasy", "/images/books/harry_potter_pietra_filosofale.jpg", "333333333");
        createBook("Il Signore degli Anelli", author5, 1954, "Fantasy", "/images/books/signore_degli_anelli.jpg", "444444444");
        createBook("Assassinio sull'Orient Express", author6, 1934, "Giallo", "/images/books/assassinio_orient_express.jpg", "555555555");

        System.out.println("Users, librarians, authors, and books data loaded.");
    }

    // Metodo per creare un autore
    private Author createAuthor(String name, String surname, String birthDate, String biography, String profilePicPath) {
        Author author = new Author();
        author.setName(name);
        author.setSurname(surname);
        author.setBirthDate(birthDate);
        author.setBiography(biography);
        author.setProfilePicPath(profilePicPath);
        return author;
    }

    // Metodo per creare un libro
    private void createBook(String title, Author author, int publicationYear, String genre, String imagePath, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);  // Associa l'autore al libro
        book.setPublicationYear(publicationYear);
        book.setGenre(genre);
        book.setMainImagePath(imagePath);
        book.setIsbn(isbn);

        bookRepository.save(book);
    }
}