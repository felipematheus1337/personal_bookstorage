package com.bookstorage.app.amqp;

import com.bookstorage.app.dto.BookProcessingMessage;
import com.bookstorage.app.models.Book;
import com.bookstorage.app.repository.BookRepository;
import com.bookstorage.app.repository.UserRepository;
import com.bookstorage.app.service.S3Service;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookProcessingConsumer {

    private final UserRepository repository;
    private final BookRepository bookRepository;
    private final S3Service s3Service;
    private static final Logger log = LoggerFactory.getLogger(BookProcessingConsumer.class);

    public BookProcessingConsumer(UserRepository repository,
                                  S3Service s3Service, BookRepository bookRepository) {
        this.repository = repository;
        this.s3Service = s3Service;
        this.bookRepository = bookRepository;
    }

    @RabbitListener(queues = "bookstorage", concurrency = "5-10")
    public void processMessage(@Payload BookProcessingMessage message) {

        Long id = Long.parseLong(message.getUserId());

        var user = this.repository.findById(id);

      if (user.isEmpty()) {
          log.info("Generating Exception... cause: User doesn't exist");
          throw new RuntimeException("Invalid ID.");
      }

      s3Service.uploadFile(message.getFileBytes())
              .thenAccept(fileUrl -> {
                  Book book = new Book(message.getDto());
                  book.setPhotoURL(fileUrl);
                  var savedBook = bookRepository.save(book);

                  var userToSave = user.get();

                  if (userToSave.getBooks() == null) {
                      userToSave.setBooks(new ArrayList<>());
                  }

                  userToSave.getBooks().add(savedBook);
                  repository.save(userToSave);

                  log.info("Book saved successfully for user ID: {}", id);

              })
              .exceptionally(ex -> {
                  log.info("Error processing the file.");
                  return null;
              });

    }

}
