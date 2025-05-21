import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.time.Duration;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

class CinemaSystem {
    public static void main(String[] args) {
        CinemaSystem system = new CinemaSystem();
        system.start();
    }

    Scanner scn = new Scanner(System.in);

    User user = new User();
    Admin admin = new Admin();

    ArrayList<City> cities = new ArrayList<>();
    ArrayList<DailySalesReport> salesReports = new ArrayList<>();
    MovieList movieList = new MovieList();

    public void printCities() {
        int counter = 1;
        for (City city : cities) {
            System.out.println(counter + ". " + city.cityName);
            counter++;
        }
    }

    public void displayCafeMenu(Cinema cinema) {
        if (cinema.cafeMenu == null) {
            System.out.println("No menu available");
            return;
        }

        System.out.println("\n=== FOOD ===");
        for (Makanan m : cinema.cafeMenu.makananList) {
            System.out.println("- " + m.nama + " (Rp" + m.harga + ")");
        }

        System.out.println("\n=== DRINKS ===");
        for (Minuman d : cinema.cafeMenu.minumanList) {
            System.out.println("- " + d.nama + " (Rp" + d.harga + ")");
        }
    }

    public FoodOrder createFoodOrder(Ticket ticket, ArrayList<String> foodNames, ArrayList<String> drinkNames) {
        Cinema cinema = getCinemaFromTicket(ticket);
        if (cinema == null || cinema.cafeMenu == null) {
            System.out.println("Cafe not available for this ticket");
            return null;
        }

        FoodOrder order = new FoodOrder();
        order.associatedTicket = ticket;
        order.totalPrice = 0;

        // Process food items
        for (String foodName : foodNames) {
            for (Makanan m : cinema.cafeMenu.makananList) {
                if (m.nama.equalsIgnoreCase(foodName)) {
                    order.orderedFood.add(m);
                    order.totalPrice += m.harga;
                    break;
                }
            }
        }

        // Process drink items
        for (String drinkName : drinkNames) {
            for (Minuman d : cinema.cafeMenu.minumanList) {
                if (d.nama.equalsIgnoreCase(drinkName)) {
                    order.orderedDrinks.add(d);
                    order.totalPrice += d.harga;
                    break;
                }
            }
        }

        return order;
    }

    public Cinema getCinemaFromTicket(Ticket ticket) {
        for (City city : cities) {
            for (Cinema cinema : city.cinemas) {
                for (Studio studio : cinema.studios) {
                    if (studio.schedule.contains(ticket.session)) {
                        return cinema;
                    }
                }
            }
        }
        return null;
    }

    public CinemaSystem() {
        initializeDummyData();
    }

    public void initializeDummyData() {

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

                for (int s = 1; s <= 2; s++) {
                    int rows = 5 + random.nextInt(5); // 5-9 rows
                    int cols = 8 + random.nextInt(4); // 8-11 columns
                    Studio studio = new Studio("A" + s, rows, cols);
                    cinema.studios.add(studio);

                    createRandomSessions(studio, movies);
                }
            }
        }

        // Add cafe menus to cinemas
        for (City city : cities) {
            for (Cinema cinema : city.cinemas) {
                cinema.cafeMenu = new CafeMenu();
                cinema.cafeMenu.makananList = new ArrayList<>();
                cinema.cafeMenu.minumanList = new ArrayList<>();

                // Add sample food
                cinema.cafeMenu.makananList.add(new Makanan("Popcorn Regular", 30000));
                cinema.cafeMenu.makananList.add(new Makanan("Popcorn Large", 45000));
                cinema.cafeMenu.makananList.add(new Makanan("Nachos", 35000));

                // Add sample drinks
                cinema.cafeMenu.minumanList.add(new Minuman("Soft Drink Regular", 20000));
                cinema.cafeMenu.minumanList.add(new Minuman("Soft Drink Large", 25000));
                cinema.cafeMenu.minumanList.add(new Minuman("Mineral Water", 15000));
            }
        }
    }

    public void createRandomSessions(Studio studio, Movie[] movies) {
        Random random = new Random();
        int sessionCount = 3 + random.nextInt(3); // 3-5 sessions

        for (int i = 0; i < sessionCount; i++) {
            // Random movie selection
            Movie movie = movies[random.nextInt(movies.length)];

            // Random date
            int dayOffset = random.nextInt(8); // range dari hari ini ke h+7
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
                    adminMenuLoop();
                    break;
                case 0:
                    System.out.println("Thank you for using TIX ID!");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public void userMenuLoop() {
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
                    currentUser.viewTickets();
                    break;
                case 3:
                    currentUser.cafeOrder(this);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public void adminMenuLoop() {
        Admin currentAdmin = new Admin();

        while (true) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Lihat Laporan Penjualan (perminggu)");
            System.out.println("2. Generate Laporan Harian");
            System.out.println("3. Kelola Film");
            System.out.println("4. Kelola Jadwal Tayang");
            System.out.println("5. Kelola Menu Cafe");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            int choice = scn.nextInt();
            scn.nextLine();

            switch (choice) {
                case 1:
                    currentAdmin.viewWeeklySalesReportMenu(this);
                    break;
                case 2:
                    currentAdmin.generateDailyReportMenu(this);
                    break;
                case 3:
                    currentAdmin.manageMoviesMenu(this);
                    break;
                case 4:
                    currentAdmin.manageSchedulesMenu(this);
                    break;
                case 5:
                    currentAdmin.manageCafeMenu(this);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");

            }
        }
    }

}

class User {
    TicketAktifLL ticketsAktif = new TicketAktifLL();
    TicketHistoryLL ticketsHistory = new TicketHistoryLL();
    Scanner scn = new Scanner(System.in);

    public User() {

    }

    // PROSES PEMESANAN TIKET

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

    public void bookByMovie(CinemaSystem system) {
        // Memilih cara sorting movie
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

        // display dan pilih movie
        system.movieList.printMovieList();
        Movie selectedMovie = selectMovie(system);
        if (selectedMovie == null) {
            return;
        }

        // display session dan pilih
        MovieSession session = selectSessionByCity(selectedMovie, system);
        if (session == null) {
            return;
        }

        // pilih tempat duduk
        selectSeat(session, system);
    }

    public void bookByCinema(CinemaSystem system) {
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

        // Display movies di group berdasarkan cinema
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

        // pilih movie
        System.out.println("\nSelect a movie (enter number, 0 to cancel):");
        int movieChoice = Integer.parseInt(scn.nextLine()) - 1;

        if (movieChoice == -1)
            return;
        if (movieChoice < 0 || movieChoice >= cinemaMovies.size()) {
            System.out.println("Invalid selection");
            bookByCinema(system);
            return;
        }

        // Display sessions for selected movie
        CinemaMoviePair selectedPair = cinemaMovies.get(movieChoice);
        MovieSession session = selectSessionByMovie(selectedPair.movie, selectedPair.cinema, system);

        if (session == null)
            return;
        selectSeat(session, system);

    }

    class CinemaMoviePair {
        Cinema cinema;
        Movie movie;

        public CinemaMoviePair(Cinema cinema, Movie movie) {
            this.cinema = cinema;
            this.movie = movie;
        }
    }

    // Method buat cek apakah newPair udah ada di ArrayList CinemaMoviePair
    public boolean containsPair(ArrayList<CinemaMoviePair> pairs, CinemaMoviePair newPair) {
        for (CinemaMoviePair pair : pairs) {
            if (pair.cinema.equals(newPair.cinema) && pair.movie.equals(newPair.movie)) {
                return true;
            }
        }
        return false;
    }

    public MovieSession selectSessionByMovie(Movie movie, Cinema cinema, CinemaSystem system) {

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

    public Movie selectMovie(CinemaSystem system) {
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

    public MovieSession selectSessionByCity(Movie movie, CinemaSystem system) {
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

    public String getCinemaName(MovieSession session, City city) {
        for (Cinema cinema : city.cinemas) {
            if (cinema.studios.contains(session.sessionStudio)) {
                return cinema.cinemaName;
            }
        }
        return "Unknown";
    }

    public void selectSeat(MovieSession session, CinemaSystem system) {
        session.sessionLayout();
        System.out.println("Enter seat ID (e.g. A01) or 0 to cancel:");
        String seatId = scn.nextLine().toUpperCase();

        if (seatId.equals("0"))
            return;

        String cinemaName = "";
        String cityName = "";

        // Search through cities and cinemas to find this studio
        for (City city : system.cities) {
            for (Cinema cinema : city.cinemas) {
                if (cinema.studios.contains(session.sessionStudio)) {
                    cinemaName = cinema.cinemaName;
                    cityName = city.cityName;
                    break;
                }
            }
        }

        // Find and book seat
        for (Seat[] row : session.sessionSeats) {
            for (Seat seat : row) {
                if (seat.seatID.equals(seatId)) {
                    if (seat.isAvailable) {
                        seat.isAvailable = false;
                        Ticket ticket = new Ticket(session, seatId, cinemaName, cityName);
                        ticketsAktif.addTicket(ticket);
                        System.out.println("Booked! Ticket ID: " + ticket.ticketID);
                        return;
                    } else {
                        System.out.println("Seat already taken!");
                        selectSeat(session, system);
                    }
                }
            }
        }
        System.out.println("Invalid seat ID");
        selectSeat(session, system);
    }

    // PROSES VIEW TIKET

    public void viewTickets() {
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
                                String kodeBookingMove = sc.nextLine(); // No try-catch
                                sc.nextLine();

                                if (ticketsAktif.moveTicket(ticketsHistory, kodeBookingMove)) {
                                    // Success message already handled inside moveTicket
                                } else {
                                    // Error message already handled inside moveTicket
                                }
                                break;
                            case 2:
                                System.out.print("Masukkan Kode Booking tiket yang mau dihapus: ");
                                String kodeBookingDelete = sc.nextLine(); // No try-catch
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
    }

    public void cafeOrder(CinemaSystem system) {
        System.out.println("\n=== CAFE ORDER ===");
        System.out.println("1. Order by Active Ticket");
        System.out.println("2. View Order History");
        System.out.println("0. Back");

        int choice = Integer.parseInt(scn.nextLine());

        switch (choice) {
            case 1:
                orderByActiveTicket(system);
                break;
            case 2:
                // viewOrderHistory();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice");
        }
    }

    private void orderByActiveTicket(CinemaSystem system) {
        // Check for active tickets
        if (ticketsAktif.tickets.isEmpty()) {
            System.out.println("No active tickets found");
            return;
        }

        // Display active tickets
        System.out.println("\nSelect an active ticket:");
        ticketsAktif.showTicket();

        System.out.print("Enter ticket number (0 to cancel): ");
        int ticketNum = Integer.parseInt(scn.nextLine()) - 1;

        if (ticketNum == -1)
            return;
        if (ticketNum < 0 || ticketNum >= ticketsAktif.tickets.size()) {
            System.out.println("Invalid selection");
            return;
        }

        Ticket selectedTicket = ticketsAktif.tickets.get(ticketNum);
        Cinema cinema = system.getCinemaFromTicket(selectedTicket);

        if (cinema == null || cinema.cafeMenu == null) {
            System.out.println("No cafe available for this cinema");
            return;
        }

        // Start ordering process
        ArrayList<String> foodItems = new ArrayList<>();
        ArrayList<String> drinkItems = new ArrayList<>();
        boolean ordering = true;

        while (ordering) {
            System.out.println("\n=== CAFE MENU ===");
            system.displayCafeMenu(cinema);

            System.out.println("\nCurrent Order:");
            System.out.println("Food: " + String.join(", ", foodItems));
            System.out.println("Drinks: " + String.join(", ", drinkItems));

            System.out.println("\n1. Add Food");
            System.out.println("2. Add Drink");
            System.out.println("3. Remove Item");
            System.out.println("4. Confirm Order");
            System.out.println("0. Cancel");

            int option = Integer.parseInt(scn.nextLine());

            switch (option) {
                case 1:
                    System.out.print("Enter food name: ");
                    foodItems.add(scn.nextLine());
                    break;
                case 2:
                    System.out.print("Enter drink name: ");
                    drinkItems.add(scn.nextLine());
                    break;
                case 3:
                    removeItemFromOrder(foodItems, drinkItems);
                    break;
                case 4:
                    FoodOrder order = system.createFoodOrder(selectedTicket, foodItems, drinkItems);
                    if (order != null) {
                        System.out.println("Order confirmed! Total: Rp" + order.totalPrice);
                    }
                    ordering = false;
                    break;
                case 0:
                    ordering = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void removeItemFromOrder(ArrayList<String> foodItems, ArrayList<String> drinkItems) {
        System.out.println("Select item to remove:");
        int counter = 1;

        System.out.println("Food Items:");
        for (String item : foodItems) {
            System.out.println(counter++ + ". " + item);
        }

        System.out.println("Drink Items:");
        for (String item : drinkItems) {
            System.out.println(counter++ + ". " + item);
        }

        System.out.print("Enter item number to remove (0 to cancel): ");
        int choice = Integer.parseInt(scn.nextLine());

        if (choice == 0)
            return;

        if (choice <= foodItems.size()) {
            foodItems.remove(choice - 1);
        } else if (choice <= foodItems.size() + drinkItems.size()) {
            drinkItems.remove(choice - 1 - foodItems.size());
        } else {
            System.out.println("Invalid selection");
        }
    }
}

class Admin {
    Scanner scn = new Scanner(System.in);

    // penjualan per minggu
    public void viewWeeklySalesReportMenu(CinemaSystem system) {
        System.out.println("\n=== Laporan Penjualan Mingguan ===");
        LocalDate now = LocalDate.now();
        // dari Senin sampai Minggu
        LocalDate weekStart = now.minusDays(now.getDayOfWeek().getValue() - 1); // Monday
        LocalDate weekEnd = weekStart.plusDays(6); // Sunday
        double total = 0;
        int count = 0;

        System.out.println("Periode: " + weekStart + " s/d " + weekEnd);

        for (City city : system.cities) {
            for (Cinema cinema : city.cinemas) {
                for (Studio studio : cinema.studios) {
                    for (MovieSession session : studio.schedule) {

                        LocalDate sessionDate = session.startTime.toLocalDate();
                        if (!sessionDate.isBefore(weekStart) && !sessionDate.isAfter(weekEnd)) {

                            int ticketsSold = countOccupiedSeats(session);
                            if (ticketsSold > 0) {
                                double sessionRevenue = ticketsSold * 50000; // Assuming 50k per ticket
                                System.out.printf("%d. %s - %s (%s) - %d tiket - Rp%.0f\n",
                                        count + 1,
                                        session.startTime.format(DateTimeFormatter.ofPattern("EEE, dd MMM")),
                                        session.movie.judul,
                                        cinema.cinemaName,
                                        ticketsSold,
                                        sessionRevenue);
                                total += sessionRevenue;
                                count++;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Total Pendapatan Minggu Ini: Rp" + total);
        if (count == 0) {
            System.out.println("Tidak ada penjualan pada minggu ini.");
        }
    }

    private int countOccupiedSeats(MovieSession session) {
        int count = 0;
        for (Seat[] row : session.sessionSeats) {
            for (Seat seat : row) {
                if (!seat.isAvailable)
                    count++;
            }
        }
        return count;
    }

    // laporan harian

    public void generateDailyReportMenu(CinemaSystem system) {
        System.out.println("\n=== Laporan Harian ===");
        LocalDate today = LocalDate.now();
        double total = 0;
        int count = 0;

        System.out.println("Tanggal: " + today);

        for (City city : system.cities) {
            for (Cinema cinema : city.cinemas) {
                for (Studio studio : cinema.studios) {
                    for (MovieSession session : studio.schedule) {
                        if (session.startTime.toLocalDate().equals(today)) {
                            int ticketsSold = countOccupiedSeats(session);
                            if (ticketsSold > 0) {
                                double sessionRevenue = ticketsSold * 50000;
                                System.out.printf("%d. %s - %s (%s) - %d tiket - Rp%.0f\n",
                                        count + 1,
                                        session.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                        session.movie.judul,
                                        cinema.cinemaName,
                                        ticketsSold,
                                        sessionRevenue);
                                total += sessionRevenue;
                                count++;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Total Pendapatan Hari Ini: Rp" + total);
        if (count == 0) {
            System.out.println("Tidak ada penjualan pada hari ini.");
        }
    }

    // Kelola Film (Tambah, Edit, Hapus)

    public void manageMoviesMenu(CinemaSystem system) {
        while (true) {
            System.out.println("\n=== KELOLA FILM ===");
            system.movieList.printMovieList();

            System.out.println("\nMenu:");
            System.out.println("1. Tambah Film");
            System.out.println("2. Edit Film");
            System.out.println("3. Hapus Film");
            System.out.println("0. Kembali");
            System.out.print("Pilihan: ");

            int choice = Integer.parseInt(scn.nextLine());

            switch (choice) {
                case 1:
                    addMovie(system);
                    break;
                case 2:
                    editMovie(system);
                    break;
                case 3:
                    deleteMovie(system);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void addMovie(CinemaSystem system) {
        System.out.println("\n=== TAMBAH FILM BARU ===");
        System.out.print("Judul: ");
        String judul = scn.nextLine();
        System.out.print("Genre: ");
        String genre = scn.nextLine();
        System.out.print("Rating Usia: ");
        String rating = scn.nextLine();
        System.out.print("Durasi (menit): ");
        int durasi = Integer.parseInt(scn.nextLine());

        system.movieList.movies.add(new Movie(judul, genre, rating,
                LocalDate.now(), Duration.ofMinutes(durasi)));
        System.out.println("Film berhasil ditambahkan!");
    }

    private void editMovie(CinemaSystem system) {
        System.out.print("\nMasukkan nomor film yang akan diedit: ");
        int index = Integer.parseInt(scn.nextLine()) - 1;

        if (index < 0 || index >= system.movieList.movies.size()) {
            System.out.println("Nomor tidak valid!");
            return;
        }

        Movie movie = system.movieList.movies.get(index);
        System.out.println("Editing: " + movie.judul);

        System.out.print("Judul Baru [" + movie.judul + "]: ");
        String judul = scn.nextLine();
        if (!judul.isEmpty())
            movie.judul = judul;

        System.out.print("Genre Baru [" + movie.genre + "]: ");
        String genre = scn.nextLine();
        if (!genre.isEmpty())
            movie.genre = genre;

        System.out.print("Rating Usia Baru [" + movie.ratingUsia + "]: ");
        String rating = scn.nextLine();
        if (!rating.isEmpty())
            movie.ratingUsia = rating;

        System.out.print("Durasi Baru (menit) [" + movie.movieDuration.toMinutes() + "]: ");
        String durasiStr = scn.nextLine();
        if (!durasiStr.isEmpty()) {
            movie.movieDuration = Duration.ofMinutes(Integer.parseInt(durasiStr));
        }

        System.out.println("Film berhasil diperbarui!");
    }

    private void deleteMovie(CinemaSystem system) {
        System.out.print("\nMasukkan nomor film yang akan dihapus: ");
        int index = Integer.parseInt(scn.nextLine()) - 1;

        if (index < 0 || index >= system.movieList.movies.size()) {
            System.out.println("Nomor tidak valid!");
            return;
        }

        String judul = system.movieList.movies.get(index).judul;
        system.movieList.movies.remove(index);
        System.out.println("Film '" + judul + "' berhasil dihapus!");
    }

    // Kelola Jadwal Tayang (Tambah, Edit, Hapus)
    public void manageSchedulesMenu(CinemaSystem system) {
        while (true) {
            System.out.println("\n=== KELOLA JADWAL TAYANG ===");

            int counter = 1;
            for (City city : system.cities) {
                for (Cinema cinema : city.cinemas) {
                    for (Studio studio : cinema.studios) {
                        for (MovieSession session : studio.schedule) {
                            System.out.printf("%d. %s | %s | %s | %s - %s\n",
                                    counter++,
                                    session.movie.judul,
                                    cinema.cinemaName,
                                    studio.studioID,
                                    session.startTime.format(DateTimeFormatter.ofPattern("EEE, dd MMM HH:mm")),
                                    session.endTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                        }
                    }
                }
            }

            System.out.println("\nMenu:");
            System.out.println("1. Tambah Jadwal");
            System.out.println("2. Edit Jadwal");
            System.out.println("3. Hapus Jadwal");
            System.out.println("0. Kembali");
            System.out.print("Pilihan: ");

            int choice = Integer.parseInt(scn.nextLine());

            switch (choice) {
                case 1:
                    addSchedule(system);
                    break;
                case 2:
                    editSchedule(system);
                    break;
                case 3:
                    deleteSchedule(system);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void addSchedule(CinemaSystem system) {
        System.out.println("\n=== TAMBAH JADWAL BARU ===");

        // Select city
        system.printCities();
        System.out.print("Pilih kota: ");
        int cityIdx = Integer.parseInt(scn.nextLine()) - 1;
        if (cityIdx < 0 || cityIdx >= system.cities.size()) {
            System.out.println("Pilihan tidak valid!");
            return;
        }
        City selectedCity = system.cities.get(cityIdx);

        // Select cinema
        System.out.println("\nPilih bioskop:");
        for (int i = 0; i < selectedCity.cinemas.size(); i++) {
            System.out.println((i + 1) + ". " + selectedCity.cinemas.get(i).cinemaName);
        }
        System.out.print("Pilihan: ");
        int cinemaIdx = Integer.parseInt(scn.nextLine()) - 1;
        if (cinemaIdx < 0 || cinemaIdx >= selectedCity.cinemas.size()) {
            System.out.println("Pilihan tidak valid!");
            return;
        }
        Cinema selectedCinema = selectedCity.cinemas.get(cinemaIdx);

        // Select studio
        System.out.println("\nPilih studio:");
        for (int i = 0; i < selectedCinema.studios.size(); i++) {
            System.out.println((i + 1) + ". Studio " + selectedCinema.studios.get(i).studioID);
        }
        System.out.print("Pilihan: ");
        int studioIdx = Integer.parseInt(scn.nextLine()) - 1;
        if (studioIdx < 0 || studioIdx >= selectedCinema.studios.size()) {
            System.out.println("Pilihan tidak valid!");
            return;
        }
        Studio selectedStudio = selectedCinema.studios.get(studioIdx);

        // Select movie
        system.movieList.printMovieList();
        System.out.print("Pilih film: ");
        int movieIdx = Integer.parseInt(scn.nextLine()) - 1;
        if (movieIdx < 0 || movieIdx >= system.movieList.movies.size()) {
            System.out.println("Pilihan tidak valid!");
            return;
        }
        Movie selectedMovie = system.movieList.movies.get(movieIdx);

        // Get date/time
        System.out.print("Tanggal dan waktu (yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scn.nextLine();
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dateTimeStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            System.out.println("Format tanggal salah!");
            return;
        }

        MovieSession newSession = new MovieSession(selectedMovie, dateTime, selectedStudio);
        if (selectedStudio.addSession(newSession)) {
            System.out.println("Jadwal berhasil ditambahkan!");
        }
    }

    private void editSchedule(CinemaSystem system) {
        System.out.println("\n=== EDIT JADWAL TAYANG ===");

        // Find the schedule to edit
        System.out.print("Masukkan nomor jadwal yang akan diedit: ");
        int scheduleNum = Integer.parseInt(scn.nextLine()) - 1;

        // Find the corresponding session
        MovieSession sessionToEdit = null;
        Studio parentStudio = null;
        Cinema parentCinema = null;
        City parentCity = null;

        int counter = 0;
        outerloop: for (City city : system.cities) {
            for (Cinema cinema : city.cinemas) {
                for (Studio studio : cinema.studios) {
                    for (MovieSession session : studio.schedule) {
                        if (counter == scheduleNum) {
                            sessionToEdit = session;
                            parentStudio = studio;
                            parentCinema = cinema;
                            parentCity = city;
                            break outerloop;
                        }
                        counter++;
                    }
                }
            }
        }

        if (sessionToEdit == null) {
            System.out.println("Nomor jadwal tidak valid!");
            return;
        }

        // Display current details
        System.out.println("\nJadwal saat ini:");
        System.out.println("Film: " + sessionToEdit.movie.judul);
        System.out.println("Bioskop: " + parentCinema.cinemaName);
        System.out.println("Studio: " + parentStudio.studioID);
        System.out.println(
                "Waktu: " + sessionToEdit.startTime.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm")));

        // Select new movie
        system.movieList.printMovieList();
        System.out.print("Pilih film baru (0 untuk tidak mengubah): ");
        int movieChoice = Integer.parseInt(scn.nextLine()) - 1;
        if (movieChoice >= 0 && movieChoice < system.movieList.movies.size()) {
            sessionToEdit.movie = system.movieList.movies.get(movieChoice);
        }

        // Select new time
        System.out.print("Waktu baru (yyyy-MM-dd HH:mm) (kosongkan untuk tidak mengubah): ");
        String newTimeStr = scn.nextLine();
        if (!newTimeStr.isEmpty()) {
            try {
                LocalDateTime newTime = LocalDateTime.parse(newTimeStr,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                // Check for conflicts
                boolean conflict = false;
                for (MovieSession existing : parentStudio.schedule) {
                    if (existing != sessionToEdit &&
                            sessionToEdit.startTime.isBefore(existing.endTime) &&
                            existing.startTime.isBefore(sessionToEdit.endTime)) {
                        conflict = true;
                        break;
                    }
                }

                if (!conflict) {
                    sessionToEdit.startTime = newTime;
                    sessionToEdit.endTime = newTime.plusMinutes(
                            sessionToEdit.movie.movieDuration.toMinutes() + 30);
                } else {
                    System.out.println("Konflik jadwal! Waktu tidak diubah.");
                }
            } catch (Exception e) {
                System.out.println("Format waktu salah!");
            }
        }

        System.out.println("Jadwal berhasil diperbarui!");
    }

    private void deleteSchedule(CinemaSystem system) {
        System.out.println("\n=== HAPUS JADWAL TAYANG ===");

        System.out.print("Masukkan nomor jadwal yang akan dihapus: ");
        int scheduleNum = Integer.parseInt(scn.nextLine()) - 1;

        // Find the corresponding session
        MovieSession sessionToDelete = null;
        Studio parentStudio = null;

        int counter = 0;
        outerloop: for (City city : system.cities) {
            for (Cinema cinema : city.cinemas) {
                for (Studio studio : cinema.studios) {
                    for (MovieSession session : studio.schedule) {
                        if (counter == scheduleNum) {
                            sessionToDelete = session;
                            parentStudio = studio;
                            break outerloop;
                        }
                        counter++;
                    }
                }
            }
        }

        if (sessionToDelete == null) {
            System.out.println("Nomor jadwal tidak valid!");
            return;
        }

        // Confirm deletion
        System.out.println("\nAnda akan menghapus jadwal:");
        System.out.println("Film: " + sessionToDelete.movie.judul);
        System.out.println(
                "Waktu: " + sessionToDelete.startTime.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm")));
        System.out.print("Yakin ingin menghapus? (y/n): ");
        String confirm = scn.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            parentStudio.schedule.remove(sessionToDelete);
            System.out.println("Jadwal berhasil dihapus!");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    public void manageCafeMenu(CinemaSystem system) {
        while (true) {
            System.out.println("\n=== KELOLA MENU CAFE ===");

            // Display all cafe menus from all cinemas
            int counter = 1;
            for (City city : system.cities) {
                for (Cinema cinema : city.cinemas) {
                    if (cinema.cafeMenu != null) {
                        System.out.println("\n" + cinema.cinemaName + ":");
                        for (Makanan makanan : cinema.cafeMenu.makananList) {
                            System.out.printf("%d. %s - Rp%d (Makanan)\n",
                                    counter++,
                                    makanan.nama,
                                    makanan.harga);
                        }
                        for (Minuman minuman : cinema.cafeMenu.minumanList) {
                            System.out.printf("%d. %s - Rp%d (Minuman)\n",
                                    counter++,
                                    minuman.nama,
                                    minuman.harga);
                        }
                    }
                }
            }

            System.out.println("\nMenu:");
            System.out.println("1. Tambah Menu");
            System.out.println("2. Edit Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("0. Kembali");
            System.out.print("Pilihan: ");

            int choice = Integer.parseInt(scn.nextLine());

            switch (choice) {
                case 1:
                    addCafeItem(system);
                    break;
                case 2:
                    editCafeItem(system);
                    break;
                case 3:
                    deleteCafeItem(system);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void addCafeItem(CinemaSystem system) {
        System.out.println("\n=== TAMBAH MENU CAFE ===");

        // 1. Select City
        system.printCities();
        System.out.print("Pilih kota (0 untuk batal): ");
        int cityChoice = Integer.parseInt(scn.nextLine()) - 1;

        if (cityChoice == -1)
            return;
        if (cityChoice < 0 || cityChoice >= system.cities.size()) {
            System.out.println("Pilihan kota tidak valid!");
            return;
        }
        City selectedCity = system.cities.get(cityChoice);

        // 2. Select Cinema
        System.out.println("\nDaftar Bioskop di " + selectedCity.cityName + ":");
        for (int i = 0; i < selectedCity.cinemas.size(); i++) {
            System.out.println((i + 1) + ". " + selectedCity.cinemas.get(i).cinemaName);
        }
        System.out.print("Pilih bioskop (0 untuk batal): ");
        int cinemaChoice = Integer.parseInt(scn.nextLine()) - 1;

        if (cinemaChoice == -1)
            return;
        if (cinemaChoice < 0 || cinemaChoice >= selectedCity.cinemas.size()) {
            System.out.println("Pilihan bioskop tidak valid!");
            return;
        }
        Cinema selectedCinema = selectedCity.cinemas.get(cinemaChoice);

        // Initialize cafe menu if not exists
        if (selectedCinema.cafeMenu == null) {
            selectedCinema.cafeMenu = new CafeMenu();
            selectedCinema.cafeMenu.makananList = new ArrayList<>();
            selectedCinema.cafeMenu.minumanList = new ArrayList<>();
        }

        // 3. Select Menu Type
        System.out.println("\nTipe Menu:");
        System.out.println("1. Makanan");
        System.out.println("2. Minuman");
        System.out.print("Pilih tipe menu (0 untuk batal): ");
        int typeChoice = Integer.parseInt(scn.nextLine());

        if (typeChoice == 0)
            return;
        if (typeChoice != 1 && typeChoice != 2) {
            System.out.println("Pilihan tidak valid!");
            return;
        }

        // 4. Input Item Details
        System.out.print("\nNama Menu: ");
        String nama = scn.nextLine().trim();

        // Validate name not empty
        if (nama.isEmpty()) {
            System.out.println("Nama menu tidak boleh kosong!");
            return;
        }

        // Check for duplicate items
        if (typeChoice == 1) {
            for (Makanan m : selectedCinema.cafeMenu.makananList) {
                if (m.nama.equalsIgnoreCase(nama)) {
                    System.out.println("Menu makanan dengan nama tersebut sudah ada!");
                    return;
                }
            }
        } else {
            for (Minuman d : selectedCinema.cafeMenu.minumanList) {
                if (d.nama.equalsIgnoreCase(nama)) {
                    System.out.println("Menu minuman dengan nama tersebut sudah ada!");
                    return;
                }
            }
        }

        // Input price with validation
        int harga = 0;
        while (true) {
            try {
                System.out.print("Harga (Rp): ");
                harga = Integer.parseInt(scn.nextLine());
                if (harga <= 0) {
                    System.out.println("Harga harus lebih dari 0!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka yang valid!");
            }
        }

        // 5. Confirm and add item
        System.out.printf("\nAnda akan menambahkan:\nNama: %s\nHarga: Rp%,d\nTipe: %s\n",
                nama, harga, (typeChoice == 1 ? "Makanan" : "Minuman"));
        System.out.print("Konfirmasi tambahkan menu? (y/n): ");
        String confirm = scn.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            if (typeChoice == 1) {
                selectedCinema.cafeMenu.makananList.add(new Makanan(nama, harga));
            } else {
                selectedCinema.cafeMenu.minumanList.add(new Minuman(nama, harga));
            }
            System.out.println("Menu berhasil ditambahkan!");
        } else {
            System.out.println("Penambahan menu dibatalkan.");
        }
    }

    private void editCafeItem(CinemaSystem system) {
        System.out.println("\n=== EDIT MENU CAFE ===");

        System.out.print("Masukkan nomor menu yang akan diedit: ");
        int menuNum = Integer.parseInt(scn.nextLine()) - 1;

        // Find the corresponding item
        Object itemToEdit = null;
        List<?> parentList = null;
        CafeMenu parentMenu = null;
        String itemType = "";

        int counter = 0;
        outerloop: for (City city : system.cities) {
            for (Cinema cinema : city.cinemas) {
                if (cinema.cafeMenu != null) {
                    for (Makanan makanan : cinema.cafeMenu.makananList) {
                        if (counter == menuNum) {
                            itemToEdit = makanan;
                            parentList = cinema.cafeMenu.makananList;
                            parentMenu = cinema.cafeMenu;
                            itemType = "makanan";
                            break outerloop;
                        }
                        counter++;
                    }
                    for (Minuman minuman : cinema.cafeMenu.minumanList) {
                        if (counter == menuNum) {
                            itemToEdit = minuman;
                            parentList = cinema.cafeMenu.minumanList;
                            parentMenu = cinema.cafeMenu;
                            itemType = "minuman";
                            break outerloop;
                        }
                        counter++;
                    }
                }
            }
        }

        if (itemToEdit == null) {
            System.out.println("Nomor menu tidak valid!");
            return;
        }

        // Edit item
        if (itemType.equals("makanan")) {
            Makanan makanan = (Makanan) itemToEdit;
            System.out.println("\nEditing Makanan: " + makanan.nama);
            System.out.print("Nama baru [" + makanan.nama + "]: ");
            String newName = scn.nextLine();
            if (!newName.isEmpty())
                makanan.nama = newName;

            System.out.print("Harga baru [" + makanan.harga + "]: ");
            String newPriceStr = scn.nextLine();
            if (!newPriceStr.isEmpty())
                makanan.harga = Integer.parseInt(newPriceStr);
        } else {
            Minuman minuman = (Minuman) itemToEdit;
            System.out.println("\nEditing Minuman: " + minuman.nama);
            System.out.print("Nama baru [" + minuman.nama + "]: ");
            String newName = scn.nextLine();
            if (!newName.isEmpty())
                minuman.nama = newName;

            System.out.print("Harga baru [" + minuman.harga + "]: ");
            String newPriceStr = scn.nextLine();
            if (!newPriceStr.isEmpty())
                minuman.harga = Integer.parseInt(newPriceStr);
        }

        System.out.println("Menu berhasil diperbarui!");
    }

    private void deleteCafeItem(CinemaSystem system) {
        System.out.println("\n=== HAPUS MENU CAFE ===");

        System.out.print("Masukkan nomor menu yang akan dihapus: ");
        int menuNum = Integer.parseInt(scn.nextLine()) - 1;

        // Find the corresponding item
        Object itemToDelete = null;
        List<?> parentList = null;
        String itemName = "";
        String itemType = "";

        int counter = 0;
        outerloop: for (City city : system.cities) {
            for (Cinema cinema : city.cinemas) {
                if (cinema.cafeMenu != null) {
                    for (Makanan makanan : cinema.cafeMenu.makananList) {
                        if (counter == menuNum) {
                            itemToDelete = makanan;
                            parentList = cinema.cafeMenu.makananList;
                            itemName = makanan.nama;
                            itemType = "makanan";
                            break outerloop;
                        }
                        counter++;
                    }
                    for (Minuman minuman : cinema.cafeMenu.minumanList) {
                        if (counter == menuNum) {
                            itemToDelete = minuman;
                            parentList = cinema.cafeMenu.minumanList;
                            itemName = minuman.nama;
                            itemType = "minuman";
                            break outerloop;
                        }
                        counter++;
                    }
                }
            }
        }

        if (itemToDelete == null) {
            System.out.println("Nomor menu tidak valid!");
            return;
        }

        // Confirm deletion
        System.out.println("\nAnda akan menghapus menu " + itemType + ": " + itemName);
        System.out.print("Yakin ingin menghapus? (y/n): ");
        String confirm = scn.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            parentList.remove(itemToDelete);
            System.out.println("Menu berhasil dihapus!");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
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

    public boolean sessionsOverlap(MovieSession a, MovieSession b) {
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

    public void copySeatLayout() {
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
    String cinemaName;
    String cityName;

    public Ticket(MovieSession session, String chosenSeatID, String cinemaName, String cityName) {
        Random rand = new Random();
        this.ticketID = "T" + new DecimalFormat("000").format(rand.nextInt(999) + 1);
        this.kodeBooking = String.format("%06d", rand.nextInt(999999));
        this.passKey = String.format("%04d", rand.nextInt(9999));
        this.session = session;
        this.seatID = chosenSeatID;
        this.cinemaName = cinemaName;
        this.cityName = cityName;
    }

}

class TicketAktifLL {

    LinkedList<Ticket> tickets = new LinkedList<>();

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
            System.out.println("Seat ID      : " + currentTicket.seatID);
            System.out.println("Studio ID    : " + currentTicket.session.sessionStudio.studioID);
            System.out.println("Bioskop      : " + currentTicket.cinemaName);
            System.out.println("Kota         : " + currentTicket.cityName);
            System.out.println("Tiket ID     : " + currentTicket.ticketID);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            System.out.println("Start        : " + currentTicket.session.startTime.format(formatter));
            System.out.println("End      : " + currentTicket.session.endTime.format(formatter));
            System.out.println();
            noTiket++;
        }
    }

    public boolean deleteTicket(String kodeBooking) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).kodeBooking.equals(kodeBooking)) {
                tickets.remove(i);
                System.out.println("TIKET BERHASIL DIHAPUS");
                return true;
            }
        }
        System.out.println("TIKET DENGAN KODE BOOKING " + kodeBooking + " TIDAK DITEMUKAN");
        return false;
    }

    public boolean moveTicket(TicketHistoryLL ticketsHistory, String kodeBooking) {
        Ticket ticketToMove = null;
        int indexToRemove = -1;

        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).kodeBooking.equals(kodeBooking)) {
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
            System.out.println(
                    "TIKET DENGAN KODE BOOKING " + kodeBooking + " TIDAK DITEMUKAN ATAU TIDAK DAPAT DIPINDAH.");
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
            System.out.println("Seat ID      : " + currentTicket.seatID);
            System.out.println("Studio ID    : " + currentTicket.session.sessionStudio.studioID);
            System.out.println("Bioskop      : " + currentTicket.cinemaName);
            System.out.println("Kota         : " + currentTicket.cityName);
            System.out.println("Tiket ID     : " + currentTicket.ticketID);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            System.out.println("Start        : " + currentTicket.session.startTime.format(formatter));
            System.out.println("End      : " + currentTicket.session.endTime.format(formatter));
            System.out.println();
            noTiket++;
        }
    }
}

// Menu Cafe
class CafeMenu {
    ArrayList<Makanan> makananList;
    ArrayList<Minuman> minumanList;
}

class Makanan {
    String nama;
    int harga;

    public Makanan(String nama, int harga) {
        this.nama = nama;
        this.harga = harga;
    }
}

class Minuman {
    String nama;
    int harga;

    public Minuman(String nama, int harga) {
        this.nama = nama;
        this.harga = harga;
    }
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
