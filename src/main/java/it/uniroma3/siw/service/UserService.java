package it.uniroma3.siw.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private CredentialsRepository credentialsRepository;
	
	private static final String PROFILE_PIC_DIR = "src/main/resources/static/images/profilePics";
	
	public User findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public void updateUser(User user, MultipartFile profilePic) throws IOException {
	    // Salva l'immagine di profilo se presente
	    if (profilePic != null && !profilePic.isEmpty()) {
	        // Assicurati che la directory esista
	        File dir = new File(PROFILE_PIC_DIR);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }

	        // Costruisci il percorso per salvare l'immagine
	        String fileName = profilePic.getOriginalFilename();
	        Path filePath = Paths.get(PROFILE_PIC_DIR, fileName);

	        // Salva il file sul disco
	        Files.write(filePath, profilePic.getBytes());
	        
	        if (Files.exists(filePath)) {
	            System.out.println("File salvato con successo: " + filePath.toString());
	        } else {
	            System.out.println("Errore nel salvataggio del file: " + filePath.toString());
	        }

	        // Imposta il percorso dell'immagine nel presidente
	        user.setProfilePic("/images/profilePics/" + fileName);
	    }

	    // Salva o aggiorna l'utente nel database
	    userRepository.save(user);
	}
	
    public User findByEmail(String email) {
        Optional<Credentials> credentialsOptional = credentialsRepository.findByEmail(email);
        
        // Se troviamo le credenziali con l'email specificata, restituiamo l'utente collegato
        return credentialsOptional.map(Credentials::getUser).orElse(null);
    }
}
