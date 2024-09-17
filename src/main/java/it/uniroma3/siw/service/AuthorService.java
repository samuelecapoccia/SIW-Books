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
import it.uniroma3.siw.repository.AuthorRepository;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;
    
    // Percorso per salvare le immagini dei libri
    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/authors/";

    public List<Author> findAll() {
        return (List<Author>) authorRepository.findAll();
    }

    public Author findById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }
    
 // Aggiorna autore, inclusa l'immagine del profilo
    public void updateAuthor(Author existingAuthor, Author newAuthor, MultipartFile profilePic) {
        existingAuthor.setName(newAuthor.getName());
        existingAuthor.setSurname(newAuthor.getSurname());
        existingAuthor.setBirthDate(newAuthor.getBirthDate());
        existingAuthor.setBiography(newAuthor.getBiography());

        // Se è stata caricata una nuova immagine di profilo
        if (profilePic != null && !profilePic.isEmpty()) {
            try {
                // Salva l'immagine in una directory e ottieni il percorso
                String profilePicPath = saveProfilePic(profilePic);
                existingAuthor.setProfilePicPath(profilePicPath);
            } catch (IOException e) {
                // Gestione errore durante il caricamento dell'immagine
                e.printStackTrace();
            }
        }

        authorRepository.save(existingAuthor);
    }
    
 // Metodo per eliminare autore
    public void deleteAuthorById(Long id) {
        Author author = findById(id);
        if (author != null) {
            // Potresti voler gestire la cancellazione dei libri collegati prima di eliminare l'autore
            authorRepository.deleteById(id);
        }
    }
    
 // Metodo per salvare un nuovo autore con immagine del profilo
    public void saveAuthor(Author author, MultipartFile profilePic) {
        if (profilePic != null && !profilePic.isEmpty()) {
            try {
            	byte[] bytes = profilePic.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + profilePic.getOriginalFilename());
                Files.write(path, bytes);

                // Imposta il percorso dell'immagine nel libro
                author.setProfilePicPath("/images/authors/" + profilePic.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        authorRepository.save(author);
    }

    
 // Metodo per salvare l'immagine di profilo e restituire il percorso
    private String saveProfilePic(MultipartFile profilePic) throws IOException {
        // Logica per salvare l'immagine in una directory e restituire il percorso
        // Ad esempio, salva nel file system o su un cloud storage
        String fileName = profilePic.getOriginalFilename();
        Path path = Paths.get(UPLOADED_FOLDER + fileName);
        try {
            Files.write(path, profilePic.getBytes());
            return "/images/authors/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}