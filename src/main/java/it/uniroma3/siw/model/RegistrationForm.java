package it.uniroma3.siw.model;

public class RegistrationForm {

    private Credentials credentials;
    private User user;
    //private Librarian librarian;
    //private boolean isLibrarian; // Flag per distinguere se l'utente è un bibliotecario

    // Getters e setters

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /*public Librarian getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }

    public boolean isLibrarian() {
        return isLibrarian;
    }

    public void setLibrarian(boolean isLibrarian) {
        this.isLibrarian = isLibrarian;
    }*/
}