/**
 * Unit tests for BookService
 * Tests all CRUD operations and business logic methods
 */

import { BookService } from '../services/bookService';
import { CreateBookDTO, UpdateBookDTO } from '../models/Book';

describe('BookService', () => {
  let bookService: BookService;

  beforeEach(() => {
    bookService = new BookService();
  });

  describe('getAllBooks', () => {
    it('should return all books', () => {
      const books = bookService.getAllBooks();
      
      expect(books).toBeDefined();
      expect(Array.isArray(books)).toBe(true);
      expect(books.length).toBeGreaterThan(0);
    });
  });

  describe('getBookById', () => {
    it('should return a book when found', () => {
      const book = bookService.getBookById(1);
      
      expect(book).toBeDefined();
      expect(book?.id).toBe(1);
      expect(book?.title).toBe('The Pragmatic Programmer');
    });

    it('should return undefined when book not found', () => {
      const book = bookService.getBookById(999);
      
      expect(book).toBeUndefined();
    });
  });

  describe('createBook', () => {
    it('should create a new book', () => {
      const newBookData: CreateBookDTO = {
        title: 'Test Book',
        author: 'Test Author',
        isbn: '978-1234567890',
        publishedYear: 2023,
        isAvailable: true,
      };

      const createdBook = bookService.createBook(newBookData);
      
      expect(createdBook).toBeDefined();
      expect(createdBook.id).toBeDefined();
      expect(createdBook.title).toBe(newBookData.title);
      expect(createdBook.author).toBe(newBookData.author);
      expect(createdBook.createdAt).toBeDefined();
    });

    it('should assign incremental IDs', () => {
      const book1Data: CreateBookDTO = {
        title: 'Book 1',
        author: 'Author 1',
        isbn: '111',
        publishedYear: 2020,
        isAvailable: true,
      };
      
      const book2Data: CreateBookDTO = {
        title: 'Book 2',
        author: 'Author 2',
        isbn: '222',
        publishedYear: 2021,
        isAvailable: true,
      };

      const book1 = bookService.createBook(book1Data);
      const book2 = bookService.createBook(book2Data);
      
      expect(book2.id).toBe(book1.id + 1);
    });
  });

  describe('updateBook', () => {
    it('should update an existing book', () => {
      const updateData: UpdateBookDTO = {
        title: 'Updated Title',
        isAvailable: false,
      };

      const updatedBook = bookService.updateBook(1, updateData);
      
      expect(updatedBook).toBeDefined();
      expect(updatedBook?.title).toBe('Updated Title');
      expect(updatedBook?.isAvailable).toBe(false);
      expect(updatedBook?.updatedAt).toBeDefined();
    });

    it('should return undefined when updating non-existent book', () => {
      const updateData: UpdateBookDTO = {
        title: 'Updated Title',
      };

      const result = bookService.updateBook(999, updateData);
      
      expect(result).toBeUndefined();
    });
  });

  describe('deleteBook', () => {
    it('should delete an existing book', () => {
      const result = bookService.deleteBook(1);
      
      expect(result).toBe(true);
      
      const deletedBook = bookService.getBookById(1);
      expect(deletedBook).toBeUndefined();
    });

    it('should return false when deleting non-existent book', () => {
      const result = bookService.deleteBook(999);
      
      expect(result).toBe(false);
    });
  });

  describe('getBooksByAuthor', () => {
    it('should return books by author (case-insensitive)', () => {
      const books = bookService.getBooksByAuthor('martin');
      
      expect(books).toBeDefined();
      expect(books.length).toBe(1);
      expect(books[0].author).toContain('Martin');
    });

    it('should return empty array when no books found', () => {
      const books = bookService.getBooksByAuthor('NonexistentAuthor');
      
      expect(books).toBeDefined();
      expect(books.length).toBe(0);
    });
  });

  describe('getBooksByYear', () => {
    it('should return books published in a specific year', () => {
      const books = bookService.getBooksByYear(2008);
      
      expect(books).toBeDefined();
      expect(books.length).toBe(1);
      expect(books[0].publishedYear).toBe(2008);
    });

    it('should return empty array when no books found for year', () => {
      const books = bookService.getBooksByYear(2025);
      
      expect(books).toBeDefined();
      expect(books.length).toBe(0);
    });
  });
});
