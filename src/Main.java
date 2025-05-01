import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int pilihan;

        Transaksi asu = new Transaksi(100, "sdas", today);

        do {
            System.out.println("=== Financial Tracker ===");
            System.out.println("1. Pemasukan");
            System.out.println("2. Pengeluaran");
            System.out.println("3. Laporan Keuangan");
            System.out.println("4. Keluar");
            System.out.print("Masukkan pilihan: ");
            pilihan = scan.nextInt();
            scan.nextLine();

            switch (pilihan) {
                case 1:
                    Transaksi.inputTransaksi();
                    break;
                case 2:
                    Transaksi.inputTransaksi();
                    //belum jadi
                    break;
                case 3:
                    System.out.println("Laporan Keuangan (belumjadi)");
                    break;
                case 4:
                    System.out.println("Keluar");
                    break;
                default:
                    break;
            }

        } while (pilihan != 4);
    }
}