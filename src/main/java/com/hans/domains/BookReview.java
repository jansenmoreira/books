package com.hans.domains;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@ApiModel(description = "Class representing a book review made by an user")
public class BookReview {

    @ApiModelProperty(hidden = true)
    private String username;

    @ApiModelProperty(
            notes = "Score that was given by the user",
            example = "5",
            required = true,
            position = 0
    )
    @Min(0)
    @Max(5)
    private Integer rating;

    @ApiModelProperty(
            notes = "Comment made by the user about the book",
            example = "Jack Torrance. Did you mean Jack Nicholson?",
            required = true,
            position = 1
    )
    @Size(min = 0, max = 140)
    private String comment;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
