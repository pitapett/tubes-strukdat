import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;

class User {
    String username;
    public User(String username) {
        this.username = username;
    }
}

class Admin {
    String username;
    public Admin(String username) {
        this.username = username;
    }
}

class City {
    String name;
    public City(String name) {
        this.name = name;
    }
}

class Cinema {
    String name;
    String address;
    City city;
    public Cinema(String name, String address, City city) {
        this.name = name;
        this.address = address;
        this.city = city;
    }
}

class Studio {
    String name;
    int seatCount;
    public Studio(String name, int seatCount) {
        this.name = name;
        this.seatCount = seatCount;
    }
}

class Movie {
    String title;
    String genre;
    int duration;
    public Movie(String title, String genre, int duration) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
    }
}

class MovieSession {
    Movie movie;
    Studio studio;
    LocalDateTime dateTime;
    public MovieSession(Movie movie, Studio studio, LocalDateTime dateTime) {
        this.movie = movie;
        this.studio = studio;
        this.dateTime = dateTime;
    }
}

class Ticket {
    User user;
    MovieSession session;
    String seatNumber;
    int price;
    public Ticket(User user, MovieSession session, String seatNumber, int price) {
        this.user = user;
        this.session = session;
        this.seatNumber = seatNumber;
        this.price = price;
    }
}

class TicketAktifLL {
    Ticket ticket;
    public TicketAktifLL(Ticket ticket) {
        this.ticket = ticket;
    }
}

class TicketHistoryLL {
    Ticket ticket;
    public TicketHistoryLL(Ticket ticket) {
        this.ticket = ticket;
    }
}

class CafeMenu {
    String name;
    int price;
    public CafeMenu(String name, int price) {
        this.name = name;
        this.price = price;
    }
}

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Admin> admins = new ArrayList<>();
    static ArrayList<City> cities = new ArrayList<>();
    static ArrayList<Cinema> cinemas = new ArrayList<>();
    static ArrayList<Studio> studios = new ArrayList<>();
    static ArrayList<Movie> movies = new ArrayList<>();
    static ArrayList<MovieSession> sessions = new ArrayList<>();
    static ArrayList<Ticket> tickets = new ArrayList<>();
    static ArrayList<CafeMenu> menus = new ArrayList<>();

    public static void adminMenu() {
        while (true) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Lihat Laporan Penjualan (perminggu)");
            System.out.println("2. Generate Laporan Harian");
            System.out.println("3. Kelola Film");
            System.out.println("4. Kelola Jadwal Tayang");
            System.out.println("5. Kelola Menu Cafe");
            System.out.println("6. Keluar");
            System.out.print("Pilih menu: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewWeeklySalesReportMenu();
                    break;
                case 2:
                    generateDailyReportMenu();
                    break;
                case 3:
                    manageMoviesMenu();
                    break;
                case 4:
                    manageSchedulesMenu();
                    break;
                case 5:
                    manageCafeMenu();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

     //penjualan per minggu
    public static void viewWeeklySalesReportMenu() {
        System.out.println("\nLaporan Penjualan Mingguan");
        LocalDate now = LocalDate.now();
        // dari Senin sampai Minggu
        LocalDate weekStart = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);
        int total = 0;
        int count = 0;

        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            LocalDate ticketDate = t.session.dateTime.toLocalDate();
            if (!ticketDate.isBefore(weekStart) && !ticketDate.isAfter(weekEnd)) {
                System.out.println((count+1) + ". " + t.user.username + " - " + t.session.movie.title + " - " + ticketDate + " - Rp" + t.price);
                total += t.price;
                count++;
            }
        }
        System.out.println("Total Pendapatan Minggu ini (" + weekStart + " s/d " + weekEnd + "): Rp" + total);
        if (count == 0) {
            System.out.println("Tidak ada penjualan pada minggu ini.");
        }
    }

    //laporan harian
    public static void generateDailyReportMenu() {
        System.out.println("\nGenerate Laporan Harian");
        LocalDate today = LocalDate.now();
        int total = 0;
        int count = 0;
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            if (t.session.dateTime.toLocalDate().equals(today)) {
                System.out.println((count+1) + ". " + t.user.username + " - " + t.session.movie.title + " - Rp" + t.price);
                total += t.price;
                count++;
            }
        }
        System.out.println("Total Hari Ini: Rp" + total);
        if (count == 0) {
            System.out.println("Tidak ada penjualan pada hari ini.");
        }
    }

    // Kelola Film (Tambah, Edit, Hapus)
    public static void manageMoviesMenu() {
        while (true) {
            System.out.println("\n=== Kelola Film ===");
            for (int i = 0; i < movies.size(); i++) {
                Movie m = movies.get(i);
                System.out.println((i + 1) + ". " + m.title + " (" + m.genre + ", " + m.duration + " menit)");
            }
            System.out.println("a. Tambah Film");
            System.out.println("b. Edit Film");
            System.out.println("c. Hapus Film");
            System.out.println("d. Kembali");
            System.out.print("Pilih opsi: ");
            String pilihan = scanner.nextLine();

            if (pilihan.equalsIgnoreCase("a")) {
                System.out.print("Judul Film: ");
                String title = scanner.nextLine();
                System.out.print("Genre Film: ");
                String genre = scanner.nextLine();
                System.out.print("Durasi (menit): ");
                int durasi = scanner.nextInt();
                scanner.nextLine();
                movies.add(new Movie(title, genre, durasi));
                System.out.println("Film berhasil ditambahkan.");
            } else if (pilihan.equalsIgnoreCase("b")) {
                System.out.print("Masukkan nomor film yang ingin diedit: ");
                int idx = scanner.nextInt();
                scanner.nextLine();
                if (idx >= 1 && idx <= movies.size()) {
                    Movie m = movies.get(idx - 1);
                    System.out.print("Judul Film (" + m.title + "): ");
                    String title = scanner.nextLine();
                    System.out.print("Genre Film (" + m.genre + "): ");
                    String genre = scanner.nextLine();
                    System.out.print("Durasi (menit) (" + m.duration + "): ");
                    int durasi = scanner.nextInt();
                    scanner.nextLine();
                    if (!title.isEmpty()) m.title = title;
                    if (!genre.isEmpty()) m.genre = genre;
                    m.duration = durasi;
                    System.out.println("Film berhasil diperbarui.");
                } else {
                    System.out.println("Nomor film tidak valid.");
                }
            } else if (pilihan.equalsIgnoreCase("c")) {
                System.out.print("Masukkan nomor film yang ingin dihapus: ");
                int idx = scanner.nextInt();
                scanner.nextLine();
                if (idx >= 1 && idx <= movies.size()) {
                    movies.remove(idx - 1);
                    System.out.println("Film berhasil dihapus.");
                } else {
                    System.out.println("Nomor film tidak valid.");
                }
            } else if (pilihan.equalsIgnoreCase("d")) {
                return;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // Kelola Jadwal Tayang (Tambah, Edit, Hapus)
    public static void manageSchedulesMenu() {
        while (true) {
            System.out.println("\n=== Kelola Jadwal Tayang ===");
            for (int i = 0; i < sessions.size(); i++) {
                MovieSession s = sessions.get(i);
                System.out.println((i + 1) + ". " + s.movie.title + " di " + s.studio.name + " pada " + s.dateTime);
            }
            System.out.println("a. Tambah Jadwal");
            System.out.println("b. Edit Jadwal");
            System.out.println("c. Hapus Jadwal");
            System.out.println("d. Kembali");
            System.out.print("Pilih opsi: ");
            String pilihan = scanner.nextLine();

            if (pilihan.equalsIgnoreCase("a")) {
                if (movies.isEmpty() || studios.isEmpty()) {
                    System.out.println("Pastikan ada data Film dan Studio terlebih dahulu.");
                    continue;
                }
                System.out.println("Pilih Film:");
                for (int i = 0; i < movies.size(); i++) {
                    System.out.println((i + 1) + ". " + movies.get(i).title);
                }
                int filmIdx = scanner.nextInt();
                scanner.nextLine();
                if (filmIdx < 1 || filmIdx > movies.size()) {
                    System.out.println("Pilihan film tidak valid.");
                    continue;
                }
                System.out.println("Pilih Studio:");
                for (int i = 0; i < studios.size(); i++) {
                    System.out.println((i + 1) + ". " + studios.get(i).name);
                }
                int studioIdx = scanner.nextInt();
                scanner.nextLine();
                if (studioIdx < 1 || studioIdx > studios.size()) {
                    System.out.println("Pilihan studio tidak valid.");
                    continue;
                }
                System.out.print("Tanggal dan waktu (yyyy-MM-ddTHH:mm), contoh: 2025-05-20 T19:30 : ");
                String dtStr = scanner.nextLine();
                LocalDateTime dt;
                try {
                    dt = LocalDateTime.parse(dtStr);
                } catch (Exception e) {
                    System.out.println("Format tanggal dan waktu salah.");
                    continue;
                }
                sessions.add(new MovieSession(movies.get(filmIdx - 1), studios.get(studioIdx - 1), dt));
                System.out.println("Jadwal tayang berhasil ditambahkan.");
            } else if (pilihan.equalsIgnoreCase("b")) {
                System.out.print("Masukkan nomor jadwal yang ingin diedit: ");
                int idx = scanner.nextInt();
                scanner.nextLine();
                if (idx >= 1 && idx <= sessions.size()) {
                    MovieSession s = sessions.get(idx - 1);
                    System.out.println("Pilih Film Baru:");
                    for (int i = 0; i < movies.size(); i++) {
                        System.out.println((i + 1) + ". " + movies.get(i).title);
                    }
                    int filmIdx = scanner.nextInt();
                    scanner.nextLine();
                    if (filmIdx < 1 || filmIdx > movies.size()) {
                        System.out.println("Pilihan film tidak valid.");
                        continue;
                    }
                    System.out.println("Pilih Studio Baru:");
                    for (int i = 0; i < studios.size(); i++) {
                        System.out.println((i + 1) + ". " + studios.get(i).name);
                    }
                    int studioIdx = scanner.nextInt();
                    scanner.nextLine();
                    if (studioIdx < 1 || studioIdx > studios.size()) {
                        System.out.println("Pilihan studio tidak valid.");
                        continue;
                    }
                    System.out.print("Tanggal dan waktu (yyyy-MM-ddTHH:mm), contoh: 2025-05-20T19:30 : ");
                    String dtStr = scanner.nextLine();
                    LocalDateTime dt;
                    try {
                        dt = LocalDateTime.parse(dtStr);
                    } catch (Exception e) {
                        System.out.println("Format tanggal dan waktu salah.");
                        continue;
                    }
                    s.movie = movies.get(filmIdx - 1);
                    s.studio = studios.get(studioIdx - 1);
                    s.dateTime = dt;
                    System.out.println("Jadwal tayang berhasil diperbarui.");
                } else {
                    System.out.println("Nomor jadwal tidak valid.");
                }
            } else if (pilihan.equalsIgnoreCase("c")) {
                System.out.print("Masukkan nomor jadwal yang ingin dihapus: ");
                int idx = scanner.nextInt();
                scanner.nextLine();
                if (idx >= 1 && idx <= sessions.size()) {
                    sessions.remove(idx - 1);
                    System.out.println("Jadwal tayang berhasil dihapus.");
                } else {
                    System.out.println("Nomor jadwal tidak valid.");
                }
            } else if (pilihan.equalsIgnoreCase("d")) {
                return;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // Kelola Menu Cafe (Tambah, Edit, Hapus)
    public static void manageCafeMenu() {
        while (true) {
            System.out.println("\n=== Kelola Menu Cafe ===");
            for (int i = 0; i < menus.size(); i++) {
                CafeMenu m = menus.get(i);
                System.out.println((i + 1) + ". " + m.name + " - Rp" + m.price);
            }
            System.out.println("a. Tambah Menu");
            System.out.println("b. Edit Menu");
            System.out.println("c. Hapus Menu");
            System.out.println("d. Kembali");
            System.out.print("Pilih opsi: ");
            String pilihan = scanner.nextLine();

            if (pilihan.equalsIgnoreCase("a")) {
                System.out.print("Nama Menu: ");
                String name = scanner.nextLine();
                System.out.print("Harga: ");
                int price = scanner.nextInt();
                scanner.nextLine();
                menus.add(new CafeMenu(name, price));
                System.out.println("Menu cafe berhasil ditambahkan.");
            } else if (pilihan.equalsIgnoreCase("b")) {
                System.out.print("Masukkan nomor menu yang ingin diedit: ");
                int idx = scanner.nextInt();
                scanner.nextLine();
                if (idx >= 1 && idx <= menus.size()) {
                    CafeMenu m = menus.get(idx - 1);
                    System.out.print("Nama Menu (" + m.name + "): ");
                    String name = scanner.nextLine();
                    System.out.print("Harga (" + m.price + "): ");
                    int price = scanner.nextInt();
                    scanner.nextLine();
                    if (!name.isEmpty()) m.name = name;
                    m.price = price;
                    System.out.println("Menu cafe berhasil diperbarui.");
                } else {
                    System.out.println("Nomor menu tidak valid.");
                }
            } else if (pilihan.equalsIgnoreCase("c")) {
                System.out.print("Masukkan nomor menu yang ingin dihapus: ");
                int idx = scanner.nextInt();
                scanner.nextLine();
                if (idx >= 1 && idx <= menus.size()) {
                    menus.remove(idx - 1);
                    System.out.println("Menu cafe berhasil dihapus.");
                } else {
                    System.out.println("Nomor menu tidak valid.");
                }
            } else if (pilihan.equalsIgnoreCase("d")) {
                return;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public static void main(String[] args) {
        // Dummy Data
        cities.add(new City("Jakarta"));
        cities.add(new City("Bandung"));
        cities.add(new City("Surabaya"));

        cinemas.add(new Cinema("Cinema XXI", "Jl. Sudirman", cities.get(0)));
        cinemas.add(new Cinema("CGV", "Jl. Merdeka", cities.get(1)));
        cinemas.add(new Cinema("Cinepolis", "Jl. Pemuda", cities.get(2)));

        studios.add(new Studio("Studio 1", 50));
        studios.add(new Studio("Studio 2", 40));
        studios.add(new Studio("Studio 3", 30));

        movies.add(new Movie("Avengers", "Action", 120));
        movies.add(new Movie("Frozen", "Animation", 90));
        movies.add(new Movie("Joker", "Drama", 110));

        //film tayang
        sessions.add(new MovieSession(movies.get(0), studios.get(0), LocalDateTime.now()));
        sessions.add(new MovieSession(movies.get(1), studios.get(1), LocalDateTime.now().plusHours(2)));
        sessions.add(new MovieSession(movies.get(2), studios.get(2), LocalDateTime.now().plusHours(4)));

        //user
        users.add(new User("budi"));
        users.add(new User("sari"));
        users.add(new User("andi"));

        //admin
        admins.add(new Admin("admin1"));
        admins.add(new Admin("admin2"));
        admins.add(new Admin("admin3"));

        // Buat sessions dengan tanggal berbeda selama seminggu (7 hari terakhir)
        sessions.clear();
        sessions.add(new MovieSession(movies.get(0), studios.get(0), LocalDateTime.now().minusDays(6).withHour(13).withMinute(0)));
        sessions.add(new MovieSession(movies.get(1), studios.get(1), LocalDateTime.now().minusDays(5).withHour(14).withMinute(0)));
        sessions.add(new MovieSession(movies.get(2), studios.get(2), LocalDateTime.now().minusDays(4).withHour(15).withMinute(0)));
        sessions.add(new MovieSession(movies.get(1), studios.get(0), LocalDateTime.now().minusDays(3).withHour(16).withMinute(0)));
        sessions.add(new MovieSession(movies.get(2), studios.get(1), LocalDateTime.now().minusDays(2).withHour(17).withMinute(0)));
        sessions.add(new MovieSession(movies.get(0), studios.get(2), LocalDateTime.now().minusDays(1).withHour(18).withMinute(0)));
        sessions.add(new MovieSession(movies.get(2), studios.get(1), LocalDateTime.now().withHour(19).withMinute(0)));

        // Buat tiket dengan session yang sudah ada
        tickets.clear();
        tickets.add(new Ticket(users.get(0), sessions.get(0), "A1", 50000));
        tickets.add(new Ticket(users.get(1), sessions.get(1), "B2", 45000));
        tickets.add(new Ticket(users.get(2), sessions.get(2), "C3", 48000));
        tickets.add(new Ticket(users.get(0), sessions.get(3), "D4", 52000));
        tickets.add(new Ticket(users.get(1), sessions.get(4), "E5", 47000));
        tickets.add(new Ticket(users.get(2), sessions.get(5), "F6", 49000));
        tickets.add(new Ticket(users.get(0), sessions.get(6), "G7", 51000));

        //menu
        menus.add(new CafeMenu("Popcorn", 20000));
        menus.add(new CafeMenu("Cola", 15000));
        menus.add(new CafeMenu("Nachos", 25000));

        while (true) {
            System.out.println("\n=== MENU UTAMA ===");
            System.out.println("1. Menu User");
            System.out.println("2. Menu Admin");
            System.out.println("3. Keluar");
            System.out.print("Pilih menu: ");
            int mainChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainChoice) {
                case 1:
                    //userMenu()
                    break;
                case 2:
                    adminMenu();
                    break;
                case 3:
                    System.out.println("Terima kasih!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

}
