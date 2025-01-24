package com.library.backend.dto;

import com.library.backend.book.Book;
import com.library.backend.category.Category;
import com.library.backend.rent.Rent;
import com.library.backend.user.User;
import com.library.backend.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapStructMapperImpl implements MapStructMapper{

    public final UserRepository userRepository;

    public MapStructMapperImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BookFullDTO bookToDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookFullDTO bookFullDto = new BookFullDTO(
                book.getId(),
                book.getImage(),
                book.getAuthorName(),
                book.getAuthorSurname(),
                book.getTitle(),
                book.getDescription(),
                categoriesToDto(book.getCategories()));

        return bookFullDto;
    }

    @Override
    public List<BookFullDTO> booksToDto(List<Book> books) {
        if ( books == null ) {
            return null;
        }

        List<BookFullDTO> list = new ArrayList<>(books.size());
        for ( Book book : books ) {
            list.add( bookToDto(book));
        }
        return list;
    }

    @Override
    public CategoryDTO categoryToDto(Category category) {
        if ( category == null ) {
            return null;
        }

        return new CategoryDTO(category.getId(), category.getName());

    }

    @Override
    public List<CategoryDTO> categoriesToDto(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryDTO> list = new ArrayList<>(categories.size());
        for ( Category category : categories ) {
            list.add( categoryToDto(category));
        }

        return list;
    }

    @Override
    public Book DtoTobook(BookFullDTO bookFullDTO) {
        if ( bookFullDTO == null ) {
            return null;
        }

        Book book = new Book(
                bookFullDTO.id(),
                bookFullDTO.image(),
                bookFullDTO.authorName(),
                bookFullDTO.authorSurname(),
                bookFullDTO.title(),
                bookFullDTO.description(),
                DtoToCategories(bookFullDTO.categories())
        );

        return book;
    }

    @Override
    public List<Category> DtoToCategories(List<CategoryDTO> categoryDTOS) {
        if ( categoryDTOS == null ) {
            return null;
        }
        List<Category> categories = new ArrayList<>(categoryDTOS.size());
        for (CategoryDTO categoryDTO : categoryDTOS){
            categories.add(DtoToCategory(categoryDTO));
        }
        return categories;
    }

    @Override
    public Category DtoToCategory(CategoryDTO categoryDTO) {
        if ( categoryDTO == null ) {
            return null;
        }

        Category category = new Category(
                categoryDTO.id(),
                categoryDTO.name(),
                null
        );

        return category;
    }

    @Override
    public CategoryFullDTO categoryToFullDto(Category category) {
        if ( category == null ) {
            return null;
        }

        return new CategoryFullDTO(
                category.getId(),
                category.getName(),
                booksToDto(category.getBooksToDescribe()));
    }

    @Override
    public UserFullDTO userToFullDto(User user) {
        if ( user == null ) {
            return null;
        }
        return new UserFullDTO(
                user.getUsername(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getAge(),
                rentsToDto(user.getRents())
        );

    }

    @Override
    public UserSlimDTO userToSlimDto(User user) {
        if ( user == null ) {
            return null;
        }
        return new UserSlimDTO(
                user.getId(),
                user.getUsername()
        );
    }

    @Override
    public RentDTO rentToDto(Rent rent) {
        if ( rent == null ) {
            return null;
        }
        return new RentDTO(
                rent.getId(),
                userToSlimDto(rent.getUser()),
                bookToDto(rent.getBook()),
                rent.getBorrow(),
                rent.getTaken()
        );
    }

    @Override
    public List<RentDTO> rentsToDto(List<Rent> rents) {
        if ( rents == null ) {
            return null;
        }
        List<RentDTO> rentDTOS = new ArrayList<>(rents.size());
        for (Rent rent : rents){
            rentDTOS.add(rentToDto(rent));
        }
        return rentDTOS;
    }

    @Override
    public Rent DtoToRent(RentDTO rentDTO) {
        if ( rentDTO == null ) {
            return null;
        }

        Rent rent = new Rent(
                rentDTO.id(),
                userRepository.findByUsername(rentDTO.user().username()).orElseThrow(),
                DtoTobook(rentDTO.book()),
                rentDTO.borrow(),
                rentDTO.taken()
        );

        return rent;
    }



}
