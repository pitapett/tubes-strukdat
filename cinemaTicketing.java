class User {
    int a;
}

class Admin {
    // bisa add/remove movie, makanan, minuman di cinema terpilih;
    // bisa nampilin jumlah pembelian movie, makanan, minuman;
}

class City {
    
}

class Cinema {
    // diisi sama CafeMenu
}

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

class CafeMenu {
    // diisi makanan sama minuman
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