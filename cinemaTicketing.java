import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.Duration;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

class CinemaSystem {
    Scanner scn = new Scanner(System.in);

    User user = new User();
    Admin admin = new Admin();
    ArrayList<City> cities = new ArrayList<>();
    ArrayList<DailySalesReport> salesReports = new ArrayList<>();
    MovieList movieList = new MovieList();

    void printCities() {
        int counter = 1;
        for (City city : cities) {
            System.out.println(counter + ". " + city.cityName);
            counter++;
        }
    }

    public CinemaSystem() {
        initializeDummyData();
    }

    public void initializeDummyData() {
        // 1. Create Movies
        Movie[] movies = {
                new Movie("Dune: Part Two", "Sci-Fi", "PG-13",
                        LocalDate.of(2023, 11, 15), Duration.ofMinutes(166)),
                new Movie("Oppenheimer", "Biography", "R",
                        LocalDate.of(2023, 7, 21), Duration.ofMinutes(180)),
                new Movie("The Marvels", "Action", "PG-13",
                        LocalDate.of(2023, 11, 10), Duration.ofMinutes(105)),
                new Movie("Wonka", "Adventure", "PG",
                        LocalDate.of(2023, 12, 15), Duration.ofMinutes(116)),
                new Movie("The Hunger Games: Ballad of Songbirds & Snakes", "Action", "PG-13",
                        LocalDate.of(2023, 11, 17), Duration.ofMinutes(157))
        };

        movieList.movies.addAll(Arrays.asList(movies));

        // 2. Create Cities, Cinemas, and Studios
        String[] cityNames = { "Jakarta", "Bandung", "Surabaya" };
        String[][] cinemaNames = {
                { "CGV Grand Indonesia", "XXI Senayan", "Cinépolis Plaza Senayan" },
                { "CGV Paris Van Java", "XXI Braga", "Flix Paskal 23" },
                { "CGV Tunjungan Plaza", "XXI Pakuwon Mall", "Moviplex Grand City" }
        };

        Random random = new Random();

        for (int i = 0; i < cityNames.length; i++) {
            City city = new City(cityNames[i]);
            cities.add(city);

            for (String cinemaName : cinemaNames[i]) {
                Cinema cinema = new Cinema(cinemaName);
                city.cinemas.add(cinema);

                // Add 2 studios per cinema (A1 and A2)
                for (int s = 1; s <= 2; s++) {
                    int rows = 5 + random.nextInt(5); // 5-9 rows
                    int cols = 8 + random.nextInt(4); // 8-11 columns
                    Studio studio = new Studio("A" + s, rows, cols);
                    cinema.studios.add(studio);

                    // Create 3-5 random sessions per studio
                    createRandomSessions(studio, movies);
                }
            }
        }
    }

    private void createRandomSessions(Studio studio, Movie[] movies) {
        Random random = new Random();
        int sessionCount = 3 + random.nextInt(3); // 3-5 sessions

        for (int i = 0; i < sessionCount; i++) {
            // Random movie selection
            Movie movie = movies[random.nextInt(movies.length)];

            // Random date (today ±7 days)
            int dayOffset = random.nextInt(15) - 7; // -7 to +7 days
            LocalDate sessionDate = LocalDate.now().plusDays(dayOffset);

            // Random time between 10:00-21:00 in 30-min increments
            int hour = 10 + random.nextInt(12); // 10-21
            int minute = random.nextBoolean() ? 0 : 30;
            LocalDateTime startTime = LocalDateTime.of(
                    sessionDate,
                    LocalTime.of(hour, minute));

            // Create and add session
            MovieSession session = new MovieSession(movie, startTime, studio);
            studio.schedule.add(session);
        }
    }

    public void start() {
        while (true) {
            System.out.println("\n=== TIX ID ===");
            System.out.println("1. User Menu");
            System.out.println("2. Admin Menu");
            System.out.println("0. Exit");

            int choice = Integer.parseInt(scn.nextLine());

            switch (choice) {
                case 1:
                    userMenuLoop();
                    break;
                case 2:
                    admin.adminMenu();
                    break;
                case 0:
                    System.out.println("Thank you for using TIX ID!");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void userMenuLoop() {
        User currentUser = new User();
        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. Book a Movie");
            System.out.println("2. View Tickets");
            System.out.println("3. Cafe Order");
            System.out.println("0. Back to Main");

            int choice = Integer.parseInt(scn.nextLine());

            switch (choice) {
                case 1:
                    currentUser.bookMovieFlow(this); // Pass CinemaSystem reference
                    break;
                case 2:
                    // currentUser.viewTickets();
                    break;
                case 3:
                    // currentUser.cafeOrder();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    // public DailySalesReport generateDailyReport(LocalDate date) {
    // }

    // public FoodOrder createFoodOrder(Ticket ticket) {
    // }

    public static void main(String[] args) {
        CinemaSystem system = new CinemaSystem();
        system.start();
    }
}

class User {
    TicketAktifLL ticketsAktif = new TicketAktifLL();
    TicketHistoryLL ticketsHistory;
    Scanner scn = new Scanner(System.in);

    public User() {

    }

    public void bookMovieFlow(CinemaSystem system) {
        System.out.println("\n=== BOOK A MOVIE ===");
        System.out.println("1. By Movie Recommendation");
        System.out.println("2. By Cinema Location");
        System.out.println("0. Back");

        int choice = Integer.parseInt(scn.nextLine());

        switch (choice) {
            case 1:
                bookByMovie(system);
                break;
            case 2:
                bookByCinema(system);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice");
        }
    }

    private void bookByMovie(CinemaSystem system) {
        // 1: Memilih cara sorting movie
        System.out.println("\nSort movies by:");
        System.out.println("1. Newest Releases");
        System.out.println("2. Most Popular");
        System.out.println("0. Back");

        int sortChoice = Integer.parseInt(scn.nextLine());

        switch (sortChoice) {
            case 1:
                system.movieList.sortMovieByReleaseDate();
                break;
            case 2:
                system.movieList.sortMovieByTotalPenonton();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice");
                return;
        }

        // 2: Display dan select movie
        system.movieList.printMovieList();
        Movie selectedMovie = selectMovie(system);
        if (selectedMovie == null) {
            return;
        }

        // 3: Show sessions and select
        MovieSession session = selectSessionByCity(selectedMovie, system);
        if (session == null) {
            return;
        }

        // Step 4: Seat selection
        selectSeat(session);
    }

    private void bookByCinema(CinemaSystem system) {
        // Pilih city
        system.printCities();
        System.out.println("\nSelect a city (enter number, 0 to cancel):");
        int cityChoice = Integer.parseInt(scn.nextLine()) - 1;

        if (cityChoice == -1)
            return;
        if (cityChoice < 0 || cityChoice >= system.cities.size()) {
            System.out.println("Invalid city selection");
            bookByCinema(system);
            return;
        }

        City selectedCity = system.cities.get(cityChoice);

        // Membuat list cinema-movie pairs
        ArrayList<CinemaMoviePair> cinemaMovies = new ArrayList<>();

        for (Cinema cinema : selectedCity.cinemas) {
            for (Studio studio : cinema.studios) {
                for (MovieSession session : studio.schedule) {
                    CinemaMoviePair pair = new CinemaMoviePair(cinema, session.movie);
                    if (!containsPair(cinemaMovies, pair)) {
                        cinemaMovies.add(pair);
                    }
                }
            }
        }

        // Display movies grouped by cinema
        if (cinemaMovies.isEmpty()) {
            System.out.println("No movies available in " + selectedCity.cityName);
            return;
        }

        System.out.println("\nMovies available in " + selectedCity.cityName + ":");
        String currentCinema = "";
        int itemNumber = 1;

        for (int i = 0; i < cinemaMovies.size(); i++) {
            CinemaMoviePair pair = cinemaMovies.get(i);

            // (only print cinema name if it is different)
            if (!pair.cinema.cinemaName.equals(currentCinema)) {
                currentCinema = pair.cinema.cinemaName;
                System.out.println("\n" + currentCinema + ":");
            }

            System.out.printf("%d. %s (%s)\n",
                    itemNumber,
                    pair.movie.judul,
                    pair.movie.ratingUsia);
            itemNumber++;
        }

        // Step 4: Select movie
        System.out.println("\nSelect a movie (enter number, 0 to cancel):");
        int movieChoice = Integer.parseInt(scn.nextLine()) - 1;

        if (movieChoice == -1)
            return;
        if (movieChoice < 0 || movieChoice >= cinemaMovies.size()) {
            System.out.println("Invalid selection");
            bookByCinema(system);
            return;
        }

        // Step 5: Show sessions for selected movie
        CinemaMoviePair selectedPair = cinemaMovies.get(movieChoice);
        MovieSession session = selectSessionByMovie(selectedPair.movie, selectedPair.cinema, system);

        if (session == null)
            return;
        selectSeat(session);

    }

    // Helper class to pair Cinema and Movie
    class CinemaMoviePair {
        Cinema cinema;
        Movie movie;

        public CinemaMoviePair(Cinema cinema, Movie movie) {
            this.cinema = cinema;
            this.movie = movie;
        }
    }

    // Method buat cek apakah newPair udah ada di ArrayList CinemaMoviePair
    private boolean containsPair(ArrayList<CinemaMoviePair> pairs, CinemaMoviePair newPair) {
        for (CinemaMoviePair pair : pairs) {
            if (pair.cinema.equals(newPair.cinema) && pair.movie.equals(newPair.movie)) {
                return true;
            }
        }
        return false;
    }

    private MovieSession selectSessionByMovie(Movie movie, Cinema cinema, CinemaSystem system) {
        // Collect all sessions for this movie in this specific cinema
        ArrayList<MovieSession> availableSessions = new ArrayList<>();

        for (Studio studio : cinema.studios) {
            for (MovieSession session : studio.schedule) {
                if (session.movie.equals(movie)) {
                    availableSessions.add(session);
                }
            }
        }

        if (availableSessions.isEmpty()) {
            System.out.println("No sessions available for " + movie.judul + " at " + cinema.cinemaName);
            return null;
        }

        // Display sessions
        System.out.println("\nAvailable sessions for " + movie.judul + " at " + cinema.cinemaName + ":");
        for (int i = 0; i < availableSessions.size(); i++) {
            MovieSession session = availableSessions.get(i);
            System.out.printf("%d. %s | Studio %s | %s - %s\n",
                    i + 1,
                    session.startTime.format(DateTimeFormatter.ofPattern("EEE, MMM d")),
                    session.sessionStudio.studioID,
                    session.startTime.toLocalTime(),
                    session.endTime.toLocalTime());
        }

        // Get selection
        System.out.println("\nSelect a session (enter number, 0 to cancel):");
        int sessionChoice = Integer.parseInt(scn.nextLine()) - 1;

        if (sessionChoice == -1)
            return null;
        if (sessionChoice < 0 || sessionChoice >= availableSessions.size()) {
            System.out.println("Invalid selection");
            return selectSessionByMovie(movie, cinema, system);
        }

        return availableSessions.get(sessionChoice);
    }

    private Movie selectMovie(CinemaSystem system) {
        System.out.println("Enter movie number (0 to cancel):");
        int choice = Integer.parseInt(scn.nextLine()) - 1;

        if (choice == -1)
            return null;
        if (choice < 0 || choice >= system.movieList.movies.size()) {
            System.out.println("Invalid selection");
            return selectMovie(system);
        }
        return system.movieList.movies.get(choice);
    }

    private MovieSession selectSessionByCity(Movie movie, CinemaSystem system) {
        // Step 1: Choose city
        system.printCities();
        System.out.println("\nSelect a city (enter number, 0 to cancel):");
        int cityChoice = Integer.parseInt(scn.nextLine()) - 1;

        if (cityChoice == -1)
            return null;
        if (cityChoice < 0 || cityChoice >= system.cities.size()) {
            System.out.println("Invalid city selection");
            return selectSessionByCity(movie, system);
        }

        City selectedCity = system.cities.get(cityChoice);

        // Step 2: Collect all sessions in city
        ArrayList<MovieSession> availableSessions = new ArrayList<>();

        for (Cinema cinema : selectedCity.cinemas) {
            for (Studio studio : cinema.studios) {
                for (MovieSession session : studio.schedule) {
                    if (session.movie.equals(movie)) {
                        availableSessions.add(session);
                    }
                }
            }
        }

        if (availableSessions.isEmpty()) {
            System.out.println("No sessions available in " + selectedCity.cityName);
            return null;
        }

        System.out.println("\nAvailable sessions in " + selectedCity.cityName + ":");
        for (int i = 0; i < availableSessions.size(); i++) {
            MovieSession session = availableSessions.get(i);
            System.out.printf("%d. %s | %s | Studio %s | %s - %s\n",
                    i + 1,
                    session.startTime.format(DateTimeFormatter.ofPattern("EEE, MMM d")),
                    session.sessionStudio.studioID,
                    session.startTime.toLocalTime(),
                    session.endTime.toLocalTime(),
                    getCinemaName(session, selectedCity));
        }

        System.out.println("\nSelect a session (enter session number, 0 for cancel):");
        int sessionChoice = Integer.parseInt(scn.nextLine()) - 1;

        if (sessionChoice == -1)
            return null;
        if (sessionChoice < 0 || sessionChoice >= availableSessions.size()) {
            System.out.println("Invalid selection");
            return selectSessionByCity(movie, system);
        }

        return availableSessions.get(sessionChoice);
    }

    private String getCinemaName(MovieSession session, City city) {
        for (Cinema cinema : city.cinemas) {
            if (cinema.studios.contains(session.sessionStudio)) {
                return cinema.cinemaName;
            }
        }
        return "Unknown";
    }

    private void selectSeat(MovieSession session) {
        session.sessionLayout();
        System.out.println("Enter seat ID (e.g. A01) or 0 to cancel:");
        String seatId = scn.nextLine().toUpperCase();

        if (seatId.equals("0"))
            return;

        // Find and book seat
        for (Seat[] row : session.sessionSeats) {
            for (Seat seat : row) {
                if (seat.seatID.equals(seatId)) {
                    if (seat.isAvailable) {
                        seat.isAvailable = false;
                        Ticket ticket = new Ticket(session, seatId);
                        ticketsAktif.createTicket(ticket);
                        System.out.println("Booked! Ticket ID: " + ticket.ticketID);
                        return;
                    } else {
                        System.out.println("Seat already taken!");
                        selectSeat(session);
                    }
                }
            }
        }
        System.out.println("Invalid seat ID");
        selectSeat(session);
    }

    public void bookFromCinema() {
        System.out.println("Choose a City");
    }
}

class Admin {

    void adminMenu() {

    }
}

class City {
    String cityName;
    ArrayList<Cinema> cinemas = new ArrayList<>();

    public City(String cityName) {
        this.cityName = cityName;

    }

    void printCinemas() {
        int counter = 1;
        for (Cinema cinema : cinemas) {
            System.out.printf("%d. %s ",
                    counter++,
                    cinema.cinemaName);
        }
    }
}

class Cinema {
    String cinemaName;
    ArrayList<Studio> studios = new ArrayList<>();
    CafeMenu cafeMenu;

    public Cinema(String cinemaName) {
        this.cinemaName = cinemaName;
    }

}

class Studio {
    String studioID;
    Seat[][] seats;
    ArrayList<MovieSession> schedule = new ArrayList<>();
    int seatRow;
    int seatColumn;

    public Studio(String studioID, int seatRow, int seatColumn) {
        this.studioID = studioID;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.seats = new Seat[seatRow][seatColumn];
        this.schedule = new ArrayList<>();
        initializeSeats();
    }

    public boolean addSession(MovieSession newSession) {
        for (MovieSession existing : schedule) {
            if (sessionsOverlap(existing, newSession)) {
                System.out.println("Conflict with existing session!");
                return false;
            }
        }
        schedule.add(newSession);
        return true;
    }

    private boolean sessionsOverlap(MovieSession a, MovieSession b) {
        return a.startTime.isBefore(b.endTime) &&
                b.startTime.isBefore(a.endTime);
    }

    public void initializeSeats() {
        for (int i = 0; i < seatRow; i++) {
            for (int j = 0; j < seatColumn; j++) {
                char rowChar = (char) ('A' + i); // A, B, C...
                String colNum = String.format("%02d", j + 1); // 01, 02...99
                seats[i][j] = new Seat(rowChar + colNum);
            }
        }
    }
}

class Seat {
    String seatID;
    boolean isAvailable;

    public Seat(String seatID) {
        this.seatID = seatID;
        this.isAvailable = true;
    }

}

class Movie {
    String judul;
    String genre;
    String ratingUsia;
    int totalPenonton;
    LocalDate releaseDate;
    Duration movieDuration;

    public Movie(String judul, String genre, String ratingUsia,
            LocalDate releaseDate, Duration movieDuration) {
        this.judul = judul;
        this.genre = genre;
        this.ratingUsia = ratingUsia;
        this.releaseDate = releaseDate;
        this.movieDuration = movieDuration;
        this.totalPenonton = 0;
    }
}

class MovieList {
    ArrayList<Movie> movies = new ArrayList<>();

    void sortMovieByReleaseDate() {
        for (int i = 0; i < movies.size() - 1; i++) {
            for (int j = 0; j < movies.size() - i - 1; j++) {
                Movie current = movies.get(j);
                Movie next = movies.get(j + 1);

                if (current.releaseDate.isBefore(next.releaseDate)) {
                    movies.set(j, next);
                    movies.set(j + 1, current);
                }
            }
        }
    }

    void sortMovieByTotalPenonton() {
        for (int i = 0; i < movies.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < movies.size(); j++) {
                if (movies.get(j).totalPenonton > movies.get(maxIndex).totalPenonton) {
                    maxIndex = j;
                }
            }

            Movie temp = movies.get(maxIndex);
            movies.set(maxIndex, movies.get(i));
            movies.set(i, temp);
        }
    }

    void printMovieList() {
        System.out.println("Daftar Film:");
        int counter = 1;
        for (Movie movie : movies) {
            System.out.printf("%d. %s (%s) - %d penonton%n",
                    counter++,
                    movie.judul,
                    movie.ratingUsia,
                    movie.totalPenonton);
        }
    }

}

class MovieSession {
    Movie movie;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Studio sessionStudio;
    Seat[][] sessionSeats;

    public MovieSession(Movie movie, LocalDateTime startTime, Studio studio) {
        this.movie = movie;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(movie.movieDuration.toMinutes() + 30); // +30min cleanup
        this.sessionStudio = studio;
        copySeatLayout();
    }

    private void copySeatLayout() {
        this.sessionSeats = new Seat[sessionStudio.seatRow][sessionStudio.seatColumn];
        for (int i = 0; i < sessionStudio.seatRow; i++) {
            for (int j = 0; j < sessionStudio.seatColumn; j++) {
                sessionSeats[i][j] = new Seat(sessionStudio.seats[i][j].seatID);
            }
        }
    }

    public void sessionLayout() {
        int totalWidth = sessionStudio.seatColumn * 7 - 1;
        String screenHeader = "SCREEN";
        int padding = (totalWidth - screenHeader.length()) / 2;

        System.out.println(" ".repeat(padding) + screenHeader + "\n");

        for (int i = 0; i < sessionStudio.seatRow; i++) {
            for (int j = 0; j < sessionStudio.seatColumn; j++) {
                String status = sessionSeats[i][j].isAvailable ? "A" : "X";
                System.out.print("[" + sessionSeats[i][j].seatID + "(" + status + ")" + "] ");
            }
            System.out.println();
        }
    }
}

class Ticket {
    String ticketID;
    String kodeBooking;
    String passKey;
    MovieSession session;
    String seatID;

    public Ticket(MovieSession session, String chosenSeatID) {
        Random rand = new Random();
        this.ticketID = "T" + new DecimalFormat("000").format(rand.nextInt(999) + 1);
        this.kodeBooking = String.format("%06d", rand.nextInt(999999));
        this.passKey = String.format("%04d", rand.nextInt(9999));
        this.session = session;
        this.seatID = chosenSeatID;
    }

}

class TicketAktifLL {
    ArrayList<Ticket> listTiketAkitf = new ArrayList<>();

    public void createTicket(Ticket ticket) {
        listTiketAkitf.add(ticket);
    }
}

class TicketHistoryLL {
    ArrayList<Ticket> listTiketHistory = new ArrayList<>();
}

// Menu Cafe
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
