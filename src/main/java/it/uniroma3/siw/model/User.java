package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
	
	private final static String DEFAULT_USER_IMAGE = "/images/defaultimage.png"; 
    @SuppressWarnings("unused")
	public final static String UPLOADED_FOLDER_PROFILEPICS = "src/main/resources/static/images/profilePics/";


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Surname is mandatory")
    private String surname;
    
    private String bio;
    
    @NotBlank(message = "Birth date is mandatory")
    private String birthDate;
    
    @NotBlank(message = "Birth place is mandatory")
    private String birthPlace;
    
	 @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	 private Credentials credentials;
	 
	 private String profilePic;
	 
	 @OneToMany(mappedBy = "reservedBy")
	 private List<Book> reservedBooks = new ArrayList<>();  // Lista dei libri prenotati
	 
    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public List<Book> getReservedBooks() {
		return reservedBooks;
	}

	public void setReservedBooks(List<Book> reservedBooks) {
		this.reservedBooks = reservedBooks;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bio, birthDate, birthPlace, credentials, id, name, profilePic, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(bio, other.bio) && Objects.equals(birthDate, other.birthDate)
				&& Objects.equals(birthPlace, other.birthPlace) && Objects.equals(credentials, other.credentials)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(profilePic, other.profilePic) && Objects.equals(surname, other.surname);
	}
	
	
    
    
}