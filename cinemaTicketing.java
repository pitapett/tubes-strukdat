import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;

class User {
    TicketAktifLL ticketsAktif; 
    TicketHistoryLL ticketsHistory; 
}

class Admin {
   
}


class City {
    String cityName;
    ArrayList<Cinema> cinemas;
}

class Cinema {
    String cinemaName;
    ArrayList<Studio> studios;
    CafeMenu cafeMenu;
}

class Studio {
    String studioID;
    Seat[][] seats;
    ArrayList<MovieSession> schedule;
}

class Seat {
}

// Movie Classes
class Movie {
    String judul;
    String genre;
    String ratingUsia;
    int totalPenonton;
    int bulanRilis;
    int duration;
}

class MovieSession {
    Movie movie;
    LocalDateTime startTime;
    Studio studio;
}

// Ticket System
class Ticket {
    String ticketID;
    String kodeBooking;
    int passKey;
    MovieSession session;
    Seat seat;
}

class TicketAktifLL {
    // Nodes and basic linked list structure
    class TicketAktifNode {
        Ticket ticket;
        TicketAktifNode next;
    }

    TicketAktifNode head;
}

class TicketHistoryLL {
    // Nodes and basic linked list structure
    class TicketHistoryNode {
        Ticket ticket;
        TicketHistoryNode next;
    }

    TicketHistoryNode head;
}

// Cafe System
class CafeMenu {
    ArrayList<Makanan> makananList;
    ArrayList<Minuman> minumanList;
}

class Makanan {
    String nama;
    int harga;
    int totalPembeli;
}

class Minuman {
    String nama;
    int harga;
    int totalPembeli;
}

// Buat Order Makanan
class FoodOrder {
    ArrayList<Makanan> orderedFood;
    ArrayList<Minuman> orderedDrinks;
    Ticket associatedTicket;
    double totalPrice;
}

// Buat Admin
class DailySalesReport {
    LocalDate date;
    int totalTicketsSold;
    double totalFoodRevenue;
    double totalDrinkRevenue;
    ArrayList<Movie> topMovies;
}

// Main System Class
class CinemaSystem {
    ArrayList<City> cities;
    ArrayList<DailySalesReport> salesReports;

    // Empty method shells
    public boolean checkSeatAvailability(MovieSession session, String seatID) {
    }

    public DailySalesReport generateDailyReport(LocalDate date) {
    }

    public FoodOrder createFoodOrder(Ticket ticket) {
    }
}