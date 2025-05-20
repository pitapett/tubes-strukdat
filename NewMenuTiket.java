
import java.util.LinkedList;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.Random;

class User {

    TicketAktifLL ticketsAktif;
    TicketHistoryLL ticketsHistory;

    void userMenu() {

    }
}

class Admin {

}

class City {

    String cityName;
    ArrayList<Cinema> cinemas;

    public City(String cityName) {
        this.cityName = cityName;
        this.cinemas = new ArrayList<>(); // <-- Add this line
    }
}

class Cinema {

    String cinemaName;
    ArrayList<Studio> studios;
    CafeMenu cafeMenu;
    City city;

    public Cinema(String cinemaName) {
        this.cinemaName = cinemaName;
        this.studios = new ArrayList<>(); // <-- Add this line
        // Optionally, initialize cafeMenu if it's always present
        // this.cafeMenu = new CafeMenu();
    }
}

class Studio {

    String studioID;
    Seat[][] seats;
    int seatRow;
    int seatColumn;
    ArrayList<MovieSession> schedule;
    Cinema cinema;

    public Studio(String studioID, int seatRow, int seatColumn) {
        this.studioID = studioID;
        this.seatRow = seatRow;       // <-- Assign parameter to field
        this.seatColumn = seatColumn; // <-- Assign parameter to field
        this.seats = new Seat[seatRow][seatColumn];
        this.schedule = new ArrayList<>(); // <-- Initialize schedule ArrayList
        initializeSeatID(); // Call this immediately after array creation
    }

    public void initializeSeatID() {
        // for (int i = 0; i < seatRow; i++) {
        //     for (int j = 0; j < seatColumn; j++) {
        //         char rowChar = (char) ('A' + i); // A, B, C...
        //         String colNum = String.format("%02d", j + 1); // 01, 02...99
        //         seats[i][j].seatID = rowChar + colNum;
        //     }
        // }
        for (int i = 0; i < seatRow; i++) {
            for (int j = 0; j < seatColumn; j++) {
                // You must create a new Seat object for each position!
                this.seats[i][j] = new Seat(); // <-- CRUCIAL: Initialize Seat object
                char rowChar = (char) ('A' + i);
                String colNum = String.format("%02d", j + 1);
                seats[i][j].seatID = rowChar + colNum;
                seats[i][j].isAvailable = true; // Seats should be available by default
            }
        }
    }

    public void studioLayout() {

        int totalWidth = seatColumn * 7 - 1;
        String screenHeader = "SCREEN";
        int padding = (totalWidth - screenHeader.length()) / 2;

        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }
        System.out.println(screenHeader + "\n");

        for (int row = 0; row < seatRow; row++) {
            for (int col = 0; col < seatColumn; col++) {
                System.out.print("[" + seats[row][col].seatID + "] ");
            }
            System.out.println();
        }
    }
}

class Seat {

    String seatID;
    boolean isAvailable;
}

class Movie {

    String judul;
    String genre;
    String ratingUsia;
    int totalPenonton;
    LocalDate bulanRilis;
    Duration movieDuration;

    public Movie(String judul, String genre, String ratingUsia) {
        this.judul = judul;
        this.genre = genre;
        this.ratingUsia = ratingUsia;
    }
}

class MovieList {

    ArrayList<Movie> movies;

    void sortMovieByReleaseDate() {

    }

    void sortMovieByTotalPenonton() {

    }

    void printMovieList() {

    }
}

class MovieSession {

    Movie movie;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Studio studio;

}

class Ticket {

    String ticketID;
    int kodeBooking;
    String passKey;
    MovieSession session;
    Seat seat; // <-- Change this to a Seat object, not String

    public Ticket(int kodeBooking, String passKey, MovieSession session, Seat seat, String ticketID) {
        // Assign the parameters directly to the fields
        this.kodeBooking = kodeBooking;
        this.passKey = passKey;
        this.ticketID = ticketID;
        this.session = session;
        this.seat = seat;
        // DO NOT generate random values here if you are passing them in main
    }

}

class TicketAktifLL {

    private LinkedList<Ticket> tickets = new LinkedList<>();

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void showTicket() {
        if (tickets.isEmpty()) {
            System.out.println("TIDAK ADA TIKET AKTIF");
            return;
        }

        int noTiket = 1;
        for (Ticket currentTicket : tickets) { // Iterate using enhanced for-loop
            System.out.println("TIKET KE-" + noTiket);
            System.out.println("Kode booking : " + currentTicket.kodeBooking);
            System.out.println("Pass key     : " + currentTicket.passKey);
            System.out.println("Seat ID      : " + currentTicket.seat.seatID);
            System.out.println("Studio ID    : " + currentTicket.session.studio.studioID);
            System.out.println("Bioskop      : " + currentTicket.session.studio.cinema.cinemaName);
            System.out.println("Kota         : " + currentTicket.session.studio.cinema.city.cityName);
            System.out.println("Tiket ID     : " + currentTicket.ticketID);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            System.out.println("Start        : " + currentTicket.session.startTime.format(formatter));
            System.out.println("End      : " + currentTicket.session.endTime.format(formatter));
            System.out.println();
            noTiket++;
        }
    }

    public boolean deleteTicket(int kodeBooking) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).kodeBooking == kodeBooking) {
                tickets.remove(i);
                System.out.println("TIKET BERHASIL DIHAPUS");
                return true;
            }
        }
        System.out.println("TIKET DENGAN KODE BOOKING " + kodeBooking + " TIDAK DITEMUKAN");
        return false;
    }

    public boolean moveTicket(TicketHistoryLL ticketsHistory, int kodeBooking) {
        Ticket ticketToMove = null;
        int indexToRemove = -1;

        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).kodeBooking == kodeBooking) {
                ticketToMove = tickets.get(i);
                indexToRemove = i;
                break;
            }
        }

        if (ticketToMove != null) {
            ticketsHistory.addTicket(ticketToMove);
            tickets.remove(indexToRemove);
            System.out.println("TIKET BERHASIL DIPINDAH");
            return true;
        } else {
            System.out.println("TIKET DENGAN KODE BOOKING " + kodeBooking + " TIDAK DITEMUKAN ATAU TIDAK DAPAT DIPINDAH.");
            return false;
        }
    }
}

class TicketHistoryLL {

    private LinkedList<Ticket> tickets = new LinkedList<>();

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void showTicket() {
        if (tickets.isEmpty()) {
            System.out.println("TIDAK ADA HISTORY TIKET");
            return;
        }

        int noTiket = 1;
        for (Ticket currentTicket : tickets) {
            System.out.println("TIKET KE-" + noTiket);
            System.out.println("Kode booking : " + currentTicket.kodeBooking);
            System.out.println("Pass key     : " + currentTicket.passKey);
            System.out.println("Seat ID      : " + currentTicket.seat.seatID);
            System.out.println("Studio ID    : " + currentTicket.session.studio.studioID);
            System.out.println("Bioskop      : " + currentTicket.session.studio.cinema.cinemaName);
            System.out.println("Kota         : " + currentTicket.session.studio.cinema.city.cityName);
            System.out.println("Tiket ID     : " + currentTicket.ticketID);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            System.out.println("Start        : " + currentTicket.session.startTime.format(formatter));
            System.out.println("End      : " + currentTicket.session.endTime.format(formatter));
            System.out.println();
            noTiket++;
        }
    }
}

public class MenuTiket { // Renamed from NewMenuTiket to MenuTiket

    // Instances of your ticket linked lists
    TicketAktifLL ticketsAktif = new TicketAktifLL();
    TicketHistoryLL ticketsHistory = new TicketHistoryLL();

    // Helper method to create a complete cinema structure for a city
    // and directly return the Studio.
    private Studio createFullStudio(String cityName, String cinemaName, String studioID, int rows, int cols) {
        City city = new City(cityName);
        Cinema cinema = new Cinema(cinemaName);
        cinema.city = city; // Link Cinema to City
        city.cinemas.add(cinema); // Add cinema to city's list

        Studio studio = new Studio(studioID, rows, cols); // Studio constructor calls initializeSeatID()
        studio.cinema = cinema; // Link Studio to Cinema
        cinema.studios.add(studio); // Add studio to cinema's list

        return studio; // Return the fully set up Studio
    }

    // Helper method to create a MovieSession
    private MovieSession createMovieSession(Movie movie, Studio studio, LocalDateTime startTime, LocalDateTime endTime) {
        MovieSession session = new MovieSession();
        session.movie = movie;
        session.studio = studio;
        session.startTime = startTime;
        session.endTime = endTime;
        studio.schedule.add(session); // Add session to studio's schedule
        return session;
    }

    public static void main(String[] args) {
        MenuTiket menu = new MenuTiket(); // Create an instance to access non-static fields/methods

        // --- Setup for Ticket 1 (Bandung) ---
        Studio studio1 = menu.createFullStudio("Bandung", "CGV", "S1", 5, 5);
        Movie movie1 = new Movie("The Java Adventure", "Action", "PG-13");
        MovieSession session1 = menu.createMovieSession(movie1, studio1,
                LocalDateTime.of(2025, 5, 19, 7, 0),
                LocalDateTime.of(2025, 5, 19, 9, 0));
        // Get the specific Seat object. It will be initialized by Studio constructor.
        Seat seatA1 = studio1.seats[0][0];
        if (seatA1 != null) { // Defensive check, though it should not be null now
            seatA1.isAvailable = false; // Mark as taken for this dummy ticket
        }
        Ticket ticket1 = new Ticket(1111, "P1111", session1, seatA1, "T1"); // Corrected Ticket constructor usage

        // --- Setup for Ticket 2 (Jakarta) ---
        Studio studio2 = menu.createFullStudio("Jakarta", "XXI", "S2", 6, 6);
        Movie movie2 = new Movie("The Sequel Saga", "Fantasy", "R");
        MovieSession session2 = menu.createMovieSession(movie2, studio2,
                LocalDateTime.of(2025, 5, 20, 10, 0),
                LocalDateTime.of(2025, 5, 20, 12, 0));
        Seat seatB2 = studio2.seats[1][1];
        if (seatB2 != null) {
            seatB2.isAvailable = false;
        }
        Ticket ticket2 = new Ticket(2222, "P2222", session2, seatB2, "T2");

        // --- Setup for Ticket 3 (Surabaya) ---
        Studio studio3 = menu.createFullStudio("Surabaya", "Cinepolis", "S3", 7, 7);
        Movie movie3 = new Movie("The Last Byte", "Sci-Fi", "G");
        MovieSession session3 = menu.createMovieSession(movie3, studio3,
                LocalDateTime.of(2025, 5, 21, 13, 0),
                LocalDateTime.of(2025, 5, 21, 15, 0));
        Seat seatC3 = studio3.seats[2][2];
        if (seatC3 != null) {
            seatC3.isAvailable = false;
        }
        Ticket ticket3 = new Ticket(3333, "P3333", session3, seatC3, "T3");

        // Add tickets to the active list
        menu.ticketsAktif.addTicket(ticket1);
        menu.ticketsAktif.addTicket(ticket2);
        menu.ticketsAktif.addTicket(ticket3);

        // Start the main menu loop
        menu.showMenu();
    }

    // --- showMenu method (without try-catch) ---
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int pilihMenu = 0;

        while (pilihMenu != 3) {
            System.out.println("\n--- Menu Tiket ---");
            System.out.println("1. Tiket Aktif");
            System.out.println("2. Daftar Transaksi (History)");
            System.out.println("3. Keluar program");

            System.out.print("Pilih menu : ");
            pilihMenu = sc.nextInt(); // No try-catch, expect valid integer input
            sc.nextLine(); // Consume newline

            switch (pilihMenu) {
                case 1:
                    System.out.println("\n--- Tiket Aktif ---");
                    ticketsAktif.showTicket();

                    // Check if there are tickets to manage before offering options
                    if (!ticketsAktif.tickets.isEmpty()) {
                        System.out.println("\n--- Opsi Tiket Aktif ---");
                        System.out.println("1. Pindahkan tiket ke History");
                        System.out.println("2. Hapus tiket");
                        System.out.println("3. Kembali ke Menu Utama");

                        System.out.print("Pilih : ");
                        int pilih = sc.nextInt(); // No try-catch, expect valid integer input
                        sc.nextLine(); // Consume newline

                        switch (pilih) {
                            case 1:
                                System.out.print("Masukkan Kode Booking tiket yang mau dipindahkan: ");
                                int kodeBookingMove = sc.nextInt(); // No try-catch
                                sc.nextLine();

                                if (ticketsAktif.moveTicket(ticketsHistory, kodeBookingMove)) {
                                    // Success message already handled inside moveTicket
                                } else {
                                    // Error message already handled inside moveTicket
                                }
                                break;
                            case 2:
                                System.out.print("Masukkan Kode Booking tiket yang mau dihapus: ");
                                int kodeBookingDelete = sc.nextInt(); // No try-catch
                                sc.nextLine();

                                if (ticketsAktif.deleteTicket(kodeBookingDelete)) {
                                    // Success message already handled inside deleteTicket
                                } else {
                                    // Error message already handled inside deleteTicket
                                }
                                break;
                            case 3:
                                System.out.println("Kembali ke Menu Utama...");
                                break;
                            default:
                                System.out.println("Pilihan tidak valid.");
                        }
                    }
                    break;
                case 2:
                    System.out.println("\n--- Daftar Transaksi ---");
                    ticketsHistory.showTicket();
                    break;
                case 3:
                    System.out.println("KELUAR DARI PROGRAM. SAMPAI JUMPA!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        }
        sc.close(); // Close the scanner when the program exits
    }
}

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
    // public boolean checkSeatAvailability(MovieSession session, String seatID) {
    // }
    // public DailySalesReport generateDailyReport(LocalDate date) {
    // }
    // public FoodOrder createFoodOrder(Ticket ticket) {
    // }
    public static void main(String[] args) {

    }
}
