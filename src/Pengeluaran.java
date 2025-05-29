import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Pengeluaran extends Transaksi{

    int inputJumlah;
    String kategori;
    public Pengeluaran(int inputJumlah, String kategori) {
        super(inputJumlah, kategori);
        this.kategori = kategori;
        this.inputJumlah = inputJumlah;
    }

    @Override
    public void inputTransaksi() {
        Scanner input = new Scanner(System.in);
        System.out.print("Masukkan jumlah pengeluaran: ");
        inputJumlah = input.nextInt();
        input.nextLine();
        System.out.print("Masukkan Kategori Pengeluaran: ");
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