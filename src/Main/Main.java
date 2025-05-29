package Main;

import Laporan.LaporanKeuangan;
import Transaksi.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int pilihan;

        Transaksi user;
        LaporanKeuangan tulis = new LaporanKeuangan();

        

        switch (pilihan) {
            case 1:
                user = new Pemasukan(0, "");//default inputan
                user.inputTransaksi();
                tulis.writePemasukkan(user.getInputJumlah(), user.getKategori());//menulis pemasukkan ke filehandling
                break;
            case 2:
                user = new Pengeluaran(0, "");
                user.inputTransaksi();
                tulis.writePengeluaran(user.getInputJumlah(), user.getKategori());//menulis pengeluaran ke filehandling
                break;
            case 3:
                tulis.displayLaporan();
                break;
            case 4:
                System.out.println("Keluar");
                break;
        }
    }
}
