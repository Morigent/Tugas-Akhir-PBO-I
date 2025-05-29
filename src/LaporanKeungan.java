import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class LaporanKeuangan {
    private final String fileName = "laporan_keuangan.CSV";

    public void writePemasukkan(int inputJumlah, String kategori) {
        writeToFile("Pemasukan", inputJumlah, kategori);
    }

    public void writePengeluaran(int inputJumlah, String kategori) {
        writeToFile("Pengeluaran", inputJumlah, kategori);
    }

    private void writeToFile(String tipe, int jumlah, String kategori) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            String waktu = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            LocalDate tanggal = LocalDate.now();

            String data = String.format("%s | Rp.%d | %s | %s %s\n", tipe, jumlah, kategori, tanggal, waktu);
            bw.write(data);


        } catch (IOException e) {
            System.out.println("Gagal menyimpan data: " + e.getMessage());
        }
    }

    public void displayLaporan() {
        System.out.println("\n===== Laporan Keuangan =====");
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                System.out.println(baris);
            }
        } catch (IOException e) {
            System.out.println("Tidak ada laporan tersedia atau file tidak ditemukan.");
        }
        System.out.println("============================\n");
    }
}
