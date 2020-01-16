package com.hans.controllers;

import com.hans.Application;
import com.hans.annotations.Secured;
import com.hans.domains.Book;
import com.hans.domains.BookReview;
import com.hans.exceptions.HttpException;
import com.hans.repositories.BookRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private BookRepository bookRepository;

    private MongoTemplate mongoTemplate;

    public BookController(BookRepository bookRepository, MongoTemplate mongoTemplate) {
        this.bookRepository = bookRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @ApiOperation("Lists all the books and reviews from the service")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Book> all() {
        return bookRepository.findAll();
    }

    @Secured
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ApiOperation("Add a new book to the service")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = Application.MIME_JSON, produces = Application.MIME_JSON)
    public Book add(@RequestBody Book book) {
        Optional<Book> conflict = bookRepository.findById(book.getIsbn());

        if (conflict.isPresent()) {
            throw new HttpException("Book already exists", HttpStatus.FORBIDDEN);
        }

        book.setReviews(null);
        return bookRepository.save(book);
    }

    @Secured
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ApiOperation("Add a review to a book")
    @RequestMapping(value = "/{isbn}/review/", method = RequestMethod.POST, consumes = Application.MIME_JSON, produces = Application.MIME_JSON)
    public BookReview addReview(@PathVariable String isbn, @RequestBody BookReview review, HttpServletRequest request) {
        Optional<Book> book = bookRepository.findById(isbn);

        if (!book.isPresent()) {
            throw new HttpException(String.format("Book with ISBN = %s does not exist", isbn), HttpStatus.NOT_FOUND);
        }

        String username = (String) request.getAttribute("username");
        review.setUsername(username);

        Optional<Book> conflict = bookRepository.findReview(isbn, username);

        if (conflict.isPresent()) {
            Update update = new Update();
            update.set("reviews.$", review);
            Criteria criteria = Criteria.where("_id").is(isbn).and("reviews.username").is(username);
            mongoTemplate.updateFirst(Query.query(criteria), update, "book");
        } else {
            Update update = new Update();
            update.push("reviews", review);
            Criteria criteria = Criteria.where("_id").is(isbn);
            mongoTemplate.updateFirst(Query.query(criteria), update, "book");
        }

        return review;
    }

}
