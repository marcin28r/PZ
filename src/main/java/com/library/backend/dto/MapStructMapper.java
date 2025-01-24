package com.library.backend.dto;

import com.library.backend.book.Book;
import com.library.backend.category.Category;
import com.library.backend.rent.Rent;
import com.library.backend.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface MapStructMapper {
    BookFullDTO bookToDto(Book book);

    Book DtoTobook(BookFullDTO bookFullDTO);

    List<BookFullDTO> booksToDto(List<Book> books);

    CategoryDTO categoryToDto(Category category);

    Category DtoToCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> categoriesToDto(List<Category> categories);

    List<Category> DtoToCategories(List<CategoryDTO> categoryDTOS);

    CategoryFullDTO categoryToFullDto(Category category);

    UserFullDTO userToFullDto(User user);

    UserSlimDTO userToSlimDto(User user);

    RentDTO rentToDto(Rent rent);

    List<RentDTO> rentsToDto(List<Rent> rents);

    Rent DtoToRent(RentDTO rentDTO);

}
