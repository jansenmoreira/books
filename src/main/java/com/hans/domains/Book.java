package com.hans.domains;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(description = "Class representing a book")
public class Book {

    @ApiModelProperty(
            notes = "Title of the book",
            example = "The Shining",
            required = true,
            position = 0
    )
    private String title;

    @Id
    @ApiModelProperty(
            notes = "International Standard Book Number of the book",
            example = "9780385121675",
            required = true,
            position = 1
    )
    @Size(min = 10, max = 13)
    private String isbn;

    @ApiModelProperty(
            notes = "Synopsis of the book",
            example = "Jack and his family move into an isolated hotel with a violent past. Living in isolation, Jack begins to lose his sanity, which affects his family members.",
            required = false,
            position = 2
    )
    @Size(min = 0, max = 140)
    private String synopsis;

    @ApiModelProperty(hidden = true)
    private List<BookReview> reviews;

    public Book(String title, String isbn, String synopsis) {
        this.title = title;
        this.isbn = isbn;
        this.synopsis = synopsis;
    }

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public List<BookReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<BookReview> reviews) {
        this.reviews = reviews;
    }

}
