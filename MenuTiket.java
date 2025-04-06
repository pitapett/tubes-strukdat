import java.util.Scanner;

class User {

}

// class Admin {
//     // bisa add/remove movie, makanan, minuman di cinema terpilih;
//     // bisa nampilin jumlah pembelian movie, makanan, minuman;
// }

class City {

}

// class Cinema {
//     // diisi sama CafeMenu
// }

class Studio {

    String studioID;
    Seat[][] seats = new Seat[3][3];
}

class Ticket {

    int kodeBooking, passKey;
    String seatID, studioID, cinema, city, ticketID;

    public Ticket(int kodeBooking, int passKey, String seatID, String studioID, String cinema, String city, String ticketID) {
        this.kodeBooking = kodeBooking;
        this.passKey = passKey;
        this.seatID = seatID;
        this.studioID = studioID;
        this.cinema = cinema;
        this.city = city;
        this.ticketID = ticketID;
    }
}

class Movie {

    String judul, genre, ratingUsia;
    int totalPenonton, bulanRilis;

    public Movie(String judul, String genre, String ratingUsia, int totalPenonton, int bulanRilis) {
        this.judul = judul;
        this.genre = genre;
        this.ratingUsia = ratingUsia;
        this.totalPenonton = totalPenonton;
        this.bulanRilis = bulanRilis;
    }
}

class Seat {

    String seatID;
    boolean isAvailable;
}

// class CafeMenu {
//     // diisi makanan sama minuman
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
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
class TiketAktifNode {

    Ticket ticket; // ticket == node yg isinya kodeBooking dll.
    TiketAktifNode next;

    public TiketAktifNode(Ticket ticket) {
        this.ticket = ticket; // ticket masuk ke node
        this.next = null;
    }
}

class TiketAktifLinkedList {

    TiketAktifNode head = null;
    TiketAktifNode tail = null;

    public void addTicket(Ticket ticket) {

        TiketAktifNode newNode = new TiketAktifNode(ticket); // bikin node baru -> isinya ticket (wadah yg isinya kodeBooking dll.)

        if (head == null) { // LL kosong
            head = newNode;
            tail = head;

        } else { // LL udah ada isinya
            tail.next = newNode;
            tail = newNode;
            tail.next = null;
        }
    }

    public void showTicket() {

        TiketAktifNode current = head; // krn mau show, jadi hrs mulai dari head
        int noTiket = 1; // entar biar hasil run nya ada urutan nomer

        if (current == null) {
            System.out.println("TIDAK ADA TIKET AKTIF");
            return;

        } else {
            while (current != null) {
                System.out.println("TIKET KE-" + noTiket);
                System.out.println("Kode booking : " + current.ticket.kodeBooking);
                System.out.println("Pass key     : " + current.ticket.passKey);
                System.out.println("Seat ID      : " + current.ticket.seatID);
                System.out.println("Studio ID    : " + current.ticket.studioID);
                System.out.println("Bioskop      : " + current.ticket.cinema);
                System.out.println("Kota         : " + current.ticket.city);
                System.out.println("Tiket ID     : " + current.ticket.ticketID);

                noTiket++;
                
                current = current.next;
            }
        }
    }

    public boolean deleteTicket(int kodeBooking) {

        if (head == null) { // 1st condi : LL kosong
            System.out.println("TIDAK ADA TIKET YANG DIHAPUS");
            return false;

        } else if (head == tail) { // 2nd condi : node sisa 1
            if (head.ticket.kodeBooking == kodeBooking) {
                head = null;
                tail = null;
                return true;
            } else {
                System.out.println("DATA TIDAK ADA");
                return false;
            }

        } else { // 3rd condi : node masih banyak
            if (head.ticket.kodeBooking == kodeBooking) { // delete head
                head = head.next;
                return true;
            } else {
                TiketAktifNode current = head;

                while (current.next != null && current.next.ticket.kodeBooking != kodeBooking) {
                    current = current.next; // lanjut selagi blm nemu yg cocok
                }

                if (current.next == null) { // posisi udah jalan sampe tail tp ga nemu yg cocok
                    System.out.println("DATA TIDAK ADA");
                    return false;
                } else if (current.next == tail) { // posisi sblm tail & mau delete tail
                    tail = current;
                    tail.next = null;
                    return true;
                } else { // delete node di tengah
                    current.next = current.next.next;
                    return true;
                }
            }
        }

    }

    public boolean moveTicket(TiketHistoryLinkedList historyList, int kodeBooking) { // kan mau move dari aktif -> history, makanya history hrs ada di parameter

        if (head == null) { // 1st condi : LL kosong
            System.out.println("TIDAK ADA TIKET YANG DIPINDAH");
            return false;

        } else if (head == tail) { // 2nd condi : node sisa 1
            if (head.ticket.kodeBooking == kodeBooking) {
                historyList.addTicket(head.ticket); // mau move si head.ticket ke historyList, jd butuh function addTicket
                head = null;
                tail = null;
                return true;
            } else {
                System.out.println("DATA TIDAK ADA");
                return false;
            }

        } else { // 3rd condi : node masih banyak
            if (head.ticket.kodeBooking == kodeBooking) { // delete head
                historyList.addTicket(head.ticket);
                head = head.next;
                return true;
            } else {
                TiketAktifNode current = head;

                while (current.next != null && current.next.ticket.kodeBooking != kodeBooking) {
                    current = current.next; // lanjut selagi blm nemu yg cocok 
                }

                if (current.next == null) { // posisi udah jalan sampe tail tp ga nemu yg cocok
                    System.out.println("DATA TIDAK ADA");
                    return false;
                } else if (current.next == tail) { // posisi sblm tail & mau delete tail
                    historyList.addTicket(current.next.ticket);
                    tail = current;
                    tail.next = null;
                    return true;
                } else { // delete node di tengah
                    historyList.addTicket(current.next.ticket);
                    current.next = current.next.next;
                    return true;
                }
            }
        }
    }
}

class TiketHistoryNode {

    Ticket ticket;
    TiketHistoryNode next;

    public TiketHistoryNode(Ticket ticket) {
        this.ticket = ticket;
        this.next = null;
    }
}

class TiketHistoryLinkedList {

    TiketHistoryNode head = null;
    TiketAktifNode tail = null;

    public void addTicket(Ticket ticket) {

        TiketHistoryNode newNode = new TiketHistoryNode(ticket); // bikin node baru -> isinya ticket

        if (head == null) { // LL kosong
            head = newNode;

        } else { // LL ada isi
            TiketHistoryNode current = head;

            while (current.next != null) { // masih ada node
                current = current.next;
            }

            current.next = newNode;
        }
    }

    public void showTicket() {

        TiketHistoryNode current = head; // mau show mulai dari head
        int noTiket = 1;

        if (current == null) {
            System.out.println("TIDAK ADA HISTORY TIKET");
            return;

        } else {
            while (current != null) {
                System.out.println("TIKET KE-" + noTiket);
                System.out.println("Kode booking : " + current.ticket.kodeBooking);
                System.out.println("Pass key     : " + current.ticket.passKey);
                System.out.println("Seat ID      : " + current.ticket.seatID);
                System.out.println("Studio ID    : " + current.ticket.studioID);
                System.out.println("Bioskop      : " + current.ticket.cinema);
                System.out.println("Kota         : " + current.ticket.city);
                System.out.println("Tiket ID     : " + current.ticket.ticketID);

                noTiket++;

                current = current.next;
            }
        }
    }
}

public class MenuTiket {

    // bikin LL nya dulu
    TiketAktifLinkedList aktifList = new TiketAktifLinkedList();
    TiketHistoryLinkedList historyList = new TiketHistoryLinkedList();

    public static void main(String[] args) {

        // bikin objek menu -> jd nnt bs akses aktifList, historyList, showMenu
        MenuTiket menu = new MenuTiket();
    
        // contoh -> pake data dummy
        // urutan : kodeBooking, passKey, seatID, studioID, cinema, city, ticketID
        Ticket ticket1 = new Ticket(1111, 1111, "A1", "S1", "CGV", "Bandung", "T1");
        Ticket ticket2 = new Ticket(2222, 2222, "B2", "S2", "XXI", "Jakarta", "T2");
        Ticket ticket3 = new Ticket(3333, 3333, "C3", "S3", "Cinepolis", "Surabaya", "T3");

        // masukin data dummy ke aktiflist
        menu.aktifList.addTicket(ticket1);
        menu.aktifList.addTicket(ticket2);
        menu.aktifList.addTicket(ticket3);

        menu.showMenu();
    }    

    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int pilihMenu = 0;

        while (pilihMenu != 3) {
            System.out.println("Menu Tiket : ");
            System.out.println("1. Tiket Aktif");
            System.out.println("2. Daftar Transaksi");
            System.out.println("3. Keluar program");

            System.out.print("Pilih menu : ");
            pilihMenu = sc.nextInt();
            sc.nextLine();

            switch (pilihMenu) {
                case 1:
                    aktifList.showTicket();

                    if (aktifList.head != null) { // kl ada tiket aktif, nanya user mau dekete / move ga

                        System.out.println("Move / delete : ");
                        System.out.println("1. Move tiket");
                        System.out.println("2. Delete tiket");
                        System.out.println("3. Keluar program");

                        System.out.print("Pilih : ");
                        int pilih = sc.nextInt();
                        sc.nextLine();

                        switch (pilih) {
                            case 1:
                                System.out.print("Kode booking yang mau dipjndah : ");
                                int kodeBookingMove = sc.nextInt();
                                sc.nextLine();

                                if (aktifList.moveTicket(historyList, kodeBookingMove)) {
                                    System.out.println("TIKET BERHASIL DIPINDAH");
                                } else {
                                    System.out.println("DATA TIDAK ADA");
                                }
                                break;
                            case 2:
                                System.out.print("Kode booking yang mau dihaous : ");
                                int kodeBookingDelete = sc.nextInt();
                                sc.nextLine();

                                if (aktifList.deleteTicket(kodeBookingDelete)) {
                                    System.out.println("TIKET BERHASIL DIHAPUS");
                                } else {
                                    System.out.println("DATA TIDAK ADA");
                                }
                                break;
                            default:
                                System.out.println("Error");
                        }
                        // System.out.print("Move? (y/n) ");
                        // String move = sc.next().trim();
                        // sc.nextLine();

                        // if (move.equalsIgnoreCase("y")) {
                        //     System.out.print("Kode booking yang mau dipindah : ");
                        //     int kodeBookingMove = sc.nextInt();
                        //     sc.nextLine();

                        //     boolean isMoved = aktifList.moveTicket(historyList, kodeBookingMove);

                        //     if (isMoved) {
                        //         System.out.println("TIKET BERHASIL DIPINDAH");
                        //     } else {
                        //         System.out.println("DATA TIDAK ADA");
                        //     }
                        // }  
                        
                        // System.out.print("Delete? (y/n) ");
                        // String delete = sc.next().trim();

                        // if (delete.equalsIgnoreCase("y")) {
                        //     System.out.print("Kode booking yang mau dihaous : ");
                        //     int kodeBookingDelete = sc.nextInt();
                        //     sc.nextLine();

                        //     boolean isDeleted = aktifList.deleteTicket(kodeBookingDelete);

                        //     if (isDeleted) {
                        //         System.out.println("TIKET BERHASIL DIHAPUS");
                        //     } else {
                        //         System.out.println("DATA TIDAK ADA");
                        //     }
                        // }

                    }
                    break;
                case 2:
                    historyList.showTicket();
                    break;
                case 3:
                    System.out.println("KELUAR");
                    break;
                default:
                    System.out.println("Error");
            }
        }
    }
}