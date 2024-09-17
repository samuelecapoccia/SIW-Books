package it.uniroma3.siw.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredentialsRepository credentialsRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Credentials getCredentials(String email) {
        Optional<Credentials> result = this.credentialsRepository.findByEmail(email);
        return result.orElse(null);
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

    @Transactional
    public void saveUserCredentials(Credentials credentials, User user, MultipartFile profilePic) throws IOException {
        // Salva l'utente separatamente
        User savedUser = this.userRepository.save(user);
        // Imposta l'utente salvato nelle credenziali
        credentials.setUser(savedUser);
        // Gestisci l'immagine del profilo
        saveProfilePicUser(savedUser, profilePic);
        // Imposta il ruolo e salva le credenziali
        credentials.setRole(Credentials.USER_ROLE);
        this.saveCredentials(credentials);
    }

    private void saveProfilePicUser(User user, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String filename = file.getOriginalFilename();
            Path path = Paths.get(User.UPLOADED_FOLDER_PROFILEPICS + filename);
            Files.write(path, file.getBytes());
            user.setProfilePic("/images/profilePics/" + filename);
        }
    }
   

    public boolean emailExists(String email) {
        return credentialsRepository.existsByEmail(email);
    }
}