package com.library.backend.rent;

import com.library.backend.book.Book;
import com.library.backend.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "userId", "bookId","taken" }) })
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    User user;
    @ManyToOne
    @JoinColumn(name="bookId", nullable=false)
    Book book;
    LocalDateTime borrow;
    LocalDateTime taken;

    public Rent(User user, Book book){
        this.book = book;
        this.user = user;
        this.borrow = LocalDateTime.now();
    }


}
