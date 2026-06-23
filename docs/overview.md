# LibraryAPI

Project is a REST API for library of books. It supports:
- By titles we define a generic class of a books, that share the same title, author, publisher, publication date and are identified by ISBN. Titles can be
    - Added - Library can have a new book that wasn't present before, either via a donation, buy of a new one or other sources
    - Removed - Title is no longer in the library
    - Banned - Title is no longer availabe in the library if it previously used to and new books with same title wont be added.
    - Unbanned - Title was banned previously, but it no longer is, which might change in the future.
      Notes:
      Some books have no exact date when they were written.
- By book we define a certain instance of a book. They can share title, but might have  Book can be
    - Rented - Someone can rent a book, making the specific book to be no longer rentable by anyone, untill the being who rented it, returned it.
    - Returned - When a being finishes using a book, it can return it to the library, increasing  how many instanced can be retnted.
    - Added - Library can have a new supply of a book, increasing how many instances of a certain book can be rented.
    - Can have state: A book instance can be destroyed, meaning no one will be able to rent the specific book instance again.
    - Reserved - Reserved book cannot be rented by anyone, but the person who reserved it.
- Rent can be:
    - Terminated - Being returned the book.
    - Started - Being picked up the book from a library.
    - Overdue - Being rented a book, but it failed to return it in time specified by due date.
- Book reservation can be:
    - Made - A new reservation for a certain book has been made
    - Canceled - When a being gives up on reserving certain book, other beings can reserve it or rent it.
- Title reservation: Sometimes people want specific title of a book, such as from a certain publisher or certain publication date. That's why we distinguish between book reservation and title reservation. Title reservation, just like book reservation can be:
    - Made - Same as for books
    - Cancelled - Same as for books
- History
    - Title can have a history
        - When it first appeared in the library
        - When was the last time it was rented by someone
        - Who rented it last time?
        - Who reserved it?
        - Is book currently reserved?
        - How many copies are available?
    - Book
        - When was book was rented last time
        - When it was reserved last time?
        - Is book currently reserved?
        - Is book currently rented?
    - Library can also have history, so It can be displayed on a page, but it's mostly constant.



These concepts don't necessarily mean that they need to map 1-1 into database!
