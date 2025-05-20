// import java.time.Duration;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.Scanner;

// // class User {

//     TicketAktifLL ticketsAktif;
//     TicketHistoryLL ticketsHistory;

//     void userMenu() {

//     }
// }

// // class Admin {
// //     // bisa add/remove movie, makanan, minuman di cinema terpilih;
// //     // bisa nampilin jumlah pembelian movie, makanan, minuman;
// // }

// class City {

//     String cityName;
//     ArrayList<Cinema> cinemas;

//     public City(String cityName) {
//         this.cityName = cityName;
//     }
// }

// class Cinema {

//     String cinemaName;
//     ArrayList<Studio> studios;
//     CafeMenu cafeMenu;
//     City city;

//     public Cinema(String cinemaName) {
//         this.cinemaName = cinemaName;
//     }
// }

// class Studio {

//     String studioID;
//     Seat[][] seats;
//     int seatRow;
//     int seatColumn;
//     ArrayList<MovieSession> schedule;
//     Cinema cinema;

//     public Studio(String studioID, int seatRow, int seatColumn) {
//         this.studioID = studioID;
//         this.seats = new Seat[seatRow][seatColumn];
//     }

//     public void initializeSeatID() {
//         for (int i = 0; i < seatRow; i++) {
//             for (int j = 0; j < seatColumn; j++) {
//                 char rowChar = (char) ('A' + i); // A, B, C...
//                 String colNum = String.format("%02d", j + 1); // 01, 02...99
//                 seats[i][j].seatID = rowChar + colNum;
//             }
//         }
//     }

//     public void studioLayout() {

//         int totalWidth = seatColumn * 7 - 1;
//         String screenHeader = "SCREEN";
//         int padding = (totalWidth - screenHeader.length()) / 2;

//         for (int i = 0; i < padding; i++) {
//             System.out.print(" ");
//         }
//         System.out.println(screenHeader + "\n");

//         for (int row = 0; row < seatRow; row++) {
//             for (int col = 0; col < seatColumn; col++) {
//                 System.out.print("[" + seats[row][col].seatID + "] ");
//             }
//             System.out.println();
//         }
//     }
// }

// class Seat {

//     String seatID;
//     boolean isAvailable;
// }

// class Movie {

//     String judul;
//     String genre;
//     String ratingUsia;
//     int totalPenonton;
//     LocalDate bulanRilis;
//     Duration movieDuration;

//     public Movie(String judul, String genre, String ratingUsia) {
//         this.judul = judul;
//         this.genre = genre;
//         this.ratingUsia = ratingUsia;
//     }
// }

// class MovieList {

//     ArrayList<Movie> movies;

//     void sortMovieByReleaseDate() {

//     }

//     void sortMovieByTotalPenonton() {

//     }

//     void printMovieList() {

//     }
// }

// class MovieSession {

//     Movie movie;
//     LocalDateTime startTime;
//     LocalDateTime endTime;
//     Studio studio;

// }

// class Ticket {

//     String ticketID;
//     int kodeBooking;
//     String passKey;
//     MovieSession session;
//     Seat seat;

//     public Ticket(int kodeBooking, String passKey, MovieSession session, Seat seat, String ticketID) {
//         this.kodeBooking = kodeBooking;
//         this.passKey = passKey;
//         this.session = session;
//         this.seat = seat;
//         this.ticketID = ticketID;
//     }

// }

// class TicketAktifLL {

//     class TicketAktifNode {

//         Ticket ticket; // ticket == node yg isinya kodeBooking dll.
//         TicketAktifNode next;

//         public TicketAktifNode(Ticket ticket) {
//             this.ticket = ticket; // ticket masuk ke node
//             this.next = null;
//         }
//     }

//     TicketAktifNode head = null;
//     TicketAktifNode tail = null;

// //     public void addTicket(Ticket ticket) {

//         TicketAktifNode newNode = new TicketAktifNode(ticket); // bikin node baru isinya ticket (wadah yg isinya kodeBooking dll.)

// //         if (head == null) { // LL kosong
// //             head = newNode;
// //             tail = head;

// //         } else { // LL udah ada isinya
// //             tail.next = newNode;
// //             tail = newNode;
// //             tail.next = null;
// //         }
// //     }

// //     public void showTicket() {

//         TicketAktifNode current = head; // krn mau show, jadi hrs mulai dari head
//         int noTiket = 1; // entar biar hasil run nya ada urutan nomer

// //         if (current == null) {
// //             System.out.println("TIDAK ADA TIKET AKTIF");
// //             return;

//         } else {
//             while (current != null) {
//                 System.out.println("TIKET KE-" + noTiket);
//                 System.out.println("Kode booking : " + current.ticket.kodeBooking);
//                 System.out.println("Pass key     : " + current.ticket.passKey);
//                 System.out.println("Seat ID      : " + current.ticket.seat.seatID);
//                 System.out.println("Studio ID    : " + current.ticket.session.studio.studioID);
//                 System.out.println("Bioskop      : " + current.ticket.session.studio.cinema.cinemaName);
//                 System.out.println("Kota         : " + current.ticket.session.studio.cinema.city.cityName);
//                 System.out.println("Tiket ID     : " + current.ticket.ticketID);

//                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
//                 System.out.println("Mulai        : " + current.ticket.session.startTime.format(formatter));
//                 System.out.println("Selesai      : " + current.ticket.session.endTime.format(formatter));

//                 noTiket++;

//                 current = current.next;
//             }
//         }
//     }

// //     public boolean deleteTicket(int kodeBooking) {

// //         if (head == null) { // 1st condi : LL kosong
// //             System.out.println("TIDAK ADA TIKET YANG DIHAPUS");
// //             return false;

// //         } else if (head == tail) { // 2nd condi : node sisa 1
// //             if (head.ticket.kodeBooking == kodeBooking) {
// //                 head = null;
// //                 tail = null;
// //                 return true;
// //             } else {
// //                 System.out.println("DATA TIDAK ADA");
// //                 return false;
// //             }

//         } else { // 3rd condi : node masih banyak
//             if (head.ticket.kodeBooking == kodeBooking) { // delete head
//                 head = head.next;
//                 return true;
//             } else {
//                 TicketAktifNode current = head;

// //                 while (current.next != null && current.next.ticket.kodeBooking != kodeBooking) {
// //                     current = current.next; // lanjut selagi blm nemu yg cocok
// //                 }

// //                 if (current.next == null) { // posisi udah jalan sampe tail tp ga nemu yg cocok
// //                     System.out.println("DATA TIDAK ADA");
// //                     return false;
// //                 } else if (current.next == tail) { // posisi sblm tail & mau delete tail
// //                     tail = current;
// //                     tail.next = null;
// //                     return true;
// //                 } else { // delete node di tengah
// //                     current.next = current.next.next;
// //                     return true;
// //                 }
// //             }
// //         }

// //     }

//     public boolean moveTicket(TicketHistoryLL ticketsHistory, int kodeBooking) { // kan mau move dari aktif ke history, makanya history hrs ada di parameter

// //         if (head == null) { // 1st condi : LL kosong
// //             System.out.println("TIDAK ADA TIKET YANG DIPINDAH");
// //             return false;

//         } else if (head == tail) { // 2nd condi : node sisa 1
//             if (head.ticket.kodeBooking == kodeBooking) {
//                 ticketsHistory.addTicket(head.ticket); // mau move si head.ticket ke ticketsHistory, jd butuh function addTicket
//                 head = null;
//                 tail = null;
//                 return true;
//             } else {
//                 System.out.println("DATA TIDAK ADA");
//                 return false;
//             }

//         } else { // 3rd condi : node masih banyak
//             if (head.ticket.kodeBooking == kodeBooking) { // delete head
//                 ticketsHistory.addTicket(head.ticket);
//                 head = head.next;
//                 return true;
//             } else {
//                 TicketAktifNode current = head;

// //                 while (current.next != null && current.next.ticket.kodeBooking != kodeBooking) {
// //                     current = current.next; // lanjut selagi blm nemu yg cocok 
// //                 }

//                 if (current.next == null) { // posisi udah jalan sampe tail tp ga nemu yg cocok
//                     System.out.println("DATA TIDAK ADA");
//                     return false;
//                 } else if (current.next == tail) { // posisi sblm tail & mau delete tail
//                     ticketsHistory.addTicket(current.next.ticket);
//                     tail = current;
//                     tail.next = null;
//                     return true;
//                 } else { // delete node di tengah
//                     ticketsHistory.addTicket(current.next.ticket);
//                     current.next = current.next.next;
//                     return true;
//                 }
//             }
//         }
//     }
// }

// class TicketHistoryLL {

//     class TicketHistoryNode {

//         Ticket ticket;
//         TicketHistoryNode next;

//         public TicketHistoryNode(Ticket ticket) {
//             this.ticket = ticket;
//             this.next = null;
//         }
//     }

//     TicketHistoryNode head = null;
//     // TicketAktifNode tail = null;

// //     public void addTicket(Ticket ticket) {

//         TicketHistoryNode newNode = new TicketHistoryNode(ticket); // bikin node baru isinya ticket

// //         if (head == null) { // LL kosong
// //             head = newNode;

//         } else { // LL ada isi
//             TicketHistoryNode current = head;

// //             while (current.next != null) { // masih ada node
// //                 current = current.next;
// //             }

// //             current.next = newNode;
// //         }
// //     }

// //     public void showTicket() {

//         TicketHistoryNode current = head; // mau show mulai dari head
//         int noTiket = 1;

// //         if (current == null) {
// //             System.out.println("TIDAK ADA HISTORY TIKET");

//         } else {
//             while (current != null) {
//                 System.out.println("TIKET KE-" + noTiket);
//                 System.out.println("Kode booking : " + current.ticket.kodeBooking);
//                 System.out.println("Pass key     : " + current.ticket.passKey);
//                 System.out.println("Seat ID      : " + current.ticket.seat.seatID);
//                 System.out.println("Studio ID    : " + current.ticket.session.studio.studioID);
//                 System.out.println("Bioskop      : " + current.ticket.session.studio.cinema.cinemaName);
//                 System.out.println("Kota         : " + current.ticket.session.studio.cinema.city.cityName);
//                 System.out.println("Tiket ID     : " + current.ticket.ticketID);

//                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
//                 System.out.println("Mulai        : " + current.ticket.session.startTime.format(formatter));
//                 System.out.println("Selesai      : " + current.ticket.session.endTime.format(formatter));

//                 noTiket++;

// //                 current = current.next;
// //             }
// //         }
// //     }
// // }

// // public class MenuTiket {

//     // bikin LL nya dulu
//     TicketAktifLL ticketsAktif = new TicketAktifLL();
//     TicketHistoryLL ticketsHistory = new TicketHistoryLL();

// //     public static void main(String[] args) {

//         // bikin objek menu jd nnt bs akses ticketsAktif, ticketsHistory, showMenu
//         // knp bikin objek? krn drtd methodnya bukan static
//         // knp gapake static? biar tiap user bs punya data yg beda2
//         MenuTiket menu = new MenuTiket();

//         // contoh pake data dummy
//         // ticket1
//         City bandung = new City("Bandung");
//         Cinema cgvBandung = new Cinema("CGV");
//         cgvBandung.city = bandung;
//         bandung.cinemas = new ArrayList<>(); 
//         bandung.cinemas.add(cgvBandung);

//         Studio studio1 = new Studio("S1", 1, 1);
//         studio1.cinema = cgvBandung;
//         studio1.initializeSeatID();
//         cgvBandung.studios.add(studio1);

//         Movie dummyMovie1 = new Movie("Film 1", "Action", "PG-13");
//         MovieSession session1 = new MovieSession();
//         session1.movie = dummyMovie1;
//         session1.startTime = LocalDateTime.of(2025, 5, 19, 7, 00);
//         session1.endTime = LocalDateTime.of(2025, 5, 19, 9, 00);
//         session1.studio = studio1;

//         Seat seatA1 = studio1.seats[0][0];
//         if (seatA1 != null) {
//             seatA1.isAvailable = false;
//         }

//         Ticket ticket1 = new Ticket(1111, "P1111", session1, seatA1, "T1");

//         // masukin data dummy ke ticketsAktif
//         menu.ticketsAktif.addTicket(ticket1);
//         // menu.ticketsAktif.addTicket(ticket2);
//         // menu.ticketsAktif.addTicket(ticket3);

//         menu.showMenu();
//     }

// //     public void showMenu() {
// //         Scanner sc = new Scanner(System.in);
// //         int pilihMenu = 0;

// //         while (pilihMenu != 3) {
// //             System.out.println("Menu Tiket : ");
// //             System.out.println("1. Tiket Aktif");
// //             System.out.println("2. Daftar Transaksi");
// //             System.out.println("3. Keluar program");

// //             System.out.print("Pilih menu : ");
// //             pilihMenu = sc.nextInt();
// //             sc.nextLine();

//             switch (pilihMenu) {
//                 case 1:
//                     ticketsAktif.showTicket();

//                     if (ticketsAktif.head != null) { // kl ada tiket aktif, nanya user mau move / deelte ga

// //                         System.out.println("Move / delete : ");
// //                         System.out.println("1. Move tiket");
// //                         System.out.println("2. Delete tiket");
// //                         System.out.println("3. Keluar program");

// //                         System.out.print("Pilih : ");
// //                         int pilih = sc.nextInt();
// //                         sc.nextLine();

//                         switch (pilih) {
//                             case 1:
//                                 System.out.print("Kode booking yang mau dipindah : ");
//                                 int kodeBookingMove = sc.nextInt();
//                                 sc.nextLine();

//                                 if (ticketsAktif.moveTicket(ticketsHistory, kodeBookingMove)) {
//                                     System.out.println("TIKET BERHASIL DIPINDAH");
//                                 } else {
//                                     System.out.println("DATA TIDAK ADA");
//                                 }
//                                 break;
//                             case 2:
//                                 System.out.print("Kode booking yang mau dihapus : ");
//                                 int kodeBookingDelete = sc.nextInt();
//                                 sc.nextLine();

//                                 if (ticketsAktif.deleteTicket(kodeBookingDelete)) {
//                                     System.out.println("TIKET BERHASIL DIHAPUS");
//                                 } else {
//                                     System.out.println("DATA TIDAK ADA");
//                                 }
//                                 break;
//                             default:
//                                 System.out.println("Error");
//                         }
//                     }
//                     break;
//                 case 2:
//                     ticketsHistory.showTicket();
//                     break;
//                 case 3:
//                     System.out.println("KELUAR");
//                     break;
//                 default:
//                     System.out.println("Error");
//             }
//         }
//     }
// }

// class CafeMenu {

//     ArrayList<Makanan> makananList;
//     ArrayList<Minuman> minumanList;
// }

// class Makanan {

//     String nama;
//     int harga;
//     int totalPembeli;
// }

// class Minuman {

//     String nama;
//     int harga;
//     int totalPembeli;
// }

// // Buat Order Makanan
// class FoodOrder {

//     ArrayList<Makanan> orderedFood;
//     ArrayList<Minuman> orderedDrinks;
//     Ticket associatedTicket;
//     double totalPrice;
// }

// // Buat Admin
// class DailySalesReport {

//     LocalDate date;
//     int totalTicketsSold;
//     double totalFoodRevenue;
//     double totalDrinkRevenue;
//     ArrayList<Movie> topMovies;
// }

// // Main System Class
// class CinemaSystem {

//     ArrayList<City> cities;
//     ArrayList<DailySalesReport> salesReports;
//     // Empty method shells
//     // public boolean checkSeatAvailability(MovieSession session, String seatID) {
//     // }

//     // public DailySalesReport generateDailyReport(LocalDate date) {
//     // }
//     // public FoodOrder createFoodOrder(Ticket ticket) {
//     // }
//     public static void main(String[] args) {

//     }
// }
