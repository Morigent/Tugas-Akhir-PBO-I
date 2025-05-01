import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Transaksi {
    private static int inputJumlah;
    private static String kategori;
    private static LocalDate today = LocalDate.now();

    // Constructor
    public Transaksi (int inputJumlah, String kategori, LocalDate today){
        this.inputJumlah = inputJumlah;
        this.kategori = kategori;
        this.today = today;
    }

    // Getter
    public int getInputJumlah() {
        return inputJumlah;
    }

    public String getKategori() {
        return kategori;
    }

    public LocalDate getToday() {
        return today;
    }

    // Setter
    public void setInputJumlah(int inputJumlah) {
        this.inputJumlah = inputJumlah;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setToday(LocalDate today) {
        this.today = today;
    }


    // method input transaksi
    public static void inputTransaksi () {
        Scanner scan = new Scanner(System.in);

        System.out.print("Masukkan jumlah transaksi: ");
        inputJumlah = scan.nextInt();
        scan.nextLine();
        System.out.print("Masukkan Kategori: ");
        kategori = scan.nextLine();
        System.out.print("Tanggal transaksi: " + today);
        scan.nextLine();
    }

    public static void rincianTransaksi () {
        LocalTime sekarang = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String waktuFormatted = sekarang.format(formatter);
        System.out.println("Waktu sekarang: " + waktuFormatted);
        System.out.println("\n=====================================");
        System.out.println("Jumlah Transaksi: Rp." + inputJumlah);
        System.out.println("Kategori        : "  + kategori);
        System.out.println("Tanggal & Waktu : " + today + ", " + waktuFormatted);
        System.out.println("=====================================\n");
    }

}
