package com.hans.repositories;

import com.hans.domains.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    @Query("{ 'isbn': ?0, 'reviews.username': ?1 }}")
    Optional<Book> findReview(String isbn, String username);

}
