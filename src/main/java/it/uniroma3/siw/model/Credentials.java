package it.uniroma3.siw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Credentials {

	public static final String USER_ROLE = "USER";
	public static final String LIBRARIAN_ROLE = "LIBRARIAN";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "Email non valida")
	public String email;
	
	@NotEmpty(message = "La password non puo essere vuota")
	private String password;
	
	@NotBlank(message = "Il ruolo è obbligatorio")
	private String role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "librarian_id", referencedColumnName = "id")
	private Librarian librarian;

	public Credentials() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Librarian getLibrarian() {
		return librarian;
	}

	public void setLibrarian(Librarian librarian) {
		this.librarian = librarian;
	}

	@Override
	public String toString() {
		return "Credentials [id=" + id + ", email=" + email + ", password=" + password + ", role=" + role + ", user="
				+ user + ", librarian=" + librarian + "]";
	}
	
}
