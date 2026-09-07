```mermaid
flowchart LR

    User["👤 User"]
    Librarian["👤 Librarian"]
    Admin["👤 Admin"]

    subgraph LMS["Library API"]

        subgraph CAT["Book catalog"]
            Browse(["Browse / Search Catalog"])
            Availability(["View Availability"])
            ManageBooks(["Manage Book Titles"])
            ManageCopies(["Manage Physical Copies"])
        end

        subgraph CIRC["Circulation"]
            Request(["Request Rental"])
            Reservation(["Manage Reservation"])
            Rentals(["View Rentals / History"])
            Checkout(["Checkout Book"])
            Return(["Return Book"])
            Extension(["Request / Manage Extension"])
        end

        subgraph POLICY["Policies"]
            Limit(["Manage Rental Limit"])
            Strike(["Manage Strikes"])
            Lockout(["Manage Lockout"])
        end

        subgraph ADMIN["Administration"]
            Users(["Manage Users"])
            Roles(["Manage Roles"])
            GDPR(["Anonymize / Remove User Data"])
        end

        Notify(["Receive Notifications"])
        Audit(["Audit Significant Actions"])
    end

    %% User
    User --> Browse
    User --> Availability
    User --> Request
    User --> Reservation
    User --> Rentals
    User --> Extension
    User --> Notify

    %% Librarian
    Librarian --> ManageBooks
    Librarian --> ManageCopies
    Librarian --> Checkout
    Librarian --> Return
    Librarian --> Limit
    Librarian --> Strike
    Librarian --> Lockout

    %% Admin
    Admin --> Users
    Admin --> Roles
    Admin --> GDPR

    %% Admin has librarian capabilities, which in turn has user capabilities
    Admin -.->|Extends| Librarian -.->|Extends| User

    Request -.-> Notify
    Reservation -.-> Notify
    Extension -.-> Notify
    Lockout -.-> Notify

    classDef actor fill:#fff,stroke:#333,stroke-width:2px
    classDef uc fill:#fff,stroke:#555,stroke-width:1.5px

    class User,Librarian,Admin actor
    class Browse,Availability,ManageBooks,ManageCopies,Request,Reservation,Rentals,Checkout,Return,Extension,Limit,Strike,Lockout,Users,Roles,GDPR,Notify,Audit uc
```

Admin user:
- Can do anything a regular user can do
  User - Most common user, represents people who want to rent books from the library:
- Makes request to rent a book
- View existing requests to rent books
- View existing reservations for books
- View currently rented books, along their info, such as due dates
  Librarian - Represents the library staff who manage the book rental process:
- Approves or denies requests to rent a book
- Dispenses books to users who have been approved to rent them
- Accepts returns of books from users