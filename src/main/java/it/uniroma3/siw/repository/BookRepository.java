package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.User;

public interface BookRepository extends CrudRepository<Book, Long>{
	List<Book> findByReservedByNull();  // Libri disponibili
    List<Book> findByReservedBy(User user);  // Libri prenotati da un utente specifico
}
