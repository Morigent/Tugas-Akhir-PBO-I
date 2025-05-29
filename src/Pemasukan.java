import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Pemasukan extends Transaksi {
    private int inputJumlah;
    private String kategori;

    public Pemasukan(int inputJumlah, String kategori) {
        super(inputJumlah, kategori);
    }

    @Override
    public LocalDate getToday() {
        return super.getToday();
    }

    @Override
    public void inputTransaksi() {
        Scanner input = new Scanner(System.in);
        System.out.print("Masukkan jumlah Pemasukkan: ");
        inputJumlah = input.nextInt();
        input.nextLine();
        System.out.print("Masukkan Kategori Pemasukkan: ");
        kategori = input.nextLine();

        setInputJumlah(inputJumlah);
        setKategori(kategori);

        rincianTransaksi();
    }

    @Override
    public void rincianTransaksi() {
        LocalTime sekarang = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String waktuFormatted = sekarang.format(formatter);
        System.out.println("Waktu sekarang: " + waktuFormatted);
        System.out.println("\n=====================================");
        System.out.println("Jumlah Transaksi: Rp." + inputJumlah);
        System.out.println("Kategori        : "  + kategori);
        System.out.println("Tanggal & Waktu : " + getToday() + ", " + waktuFormatted);
        System.out.println("=====================================\n");
    }
}