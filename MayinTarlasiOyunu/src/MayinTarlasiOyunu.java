import java.util.Random;
import java.util.Scanner;

public class MayinTarlasiOyunu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Mayın Tarlası Oyuna Hoşgeldiniz!");

        System.out.print("Satır sayısını giriniz: ");
        int satirSayisi = scanner.nextInt();

        System.out.print("Sütun sayısını giriniz: ");
        int sutunSayisi = scanner.nextInt();

        char[][] mayinTarlasi = new char[satirSayisi][sutunSayisi];
        boolean[][] mayinKonumlari = new boolean[satirSayisi][sutunSayisi];

        mayinlariYerlestir(mayinKonumlari);
        tarlayiOlustur(mayinTarlasi);

        oyunuBaslat(mayinTarlasi, mayinKonumlari, scanner);

        scanner.close();
    }

    private static void mayinlariYerlestir(boolean[][] mayinKonumlari) {
        Random random = new Random();
        int mayinSayisi = (mayinKonumlari.length * mayinKonumlari[0].length) / 4;

        for (int i = 0; i < mayinSayisi; i++) {
            int satir = random.nextInt(mayinKonumlari.length);
            int sutun = random.nextInt(mayinKonumlari[0].length);

            mayinKonumlari[satir][sutun] = true;
        }
    }

    private static void tarlayiOlustur(char[][] tarla) {
        for (int i = 0; i < tarla.length; i++) {
            for (int j = 0; j < tarla[0].length; j++) {
                tarla[i][j] = '-';
            }
        }
    }

    private static void oyunuBaslat(char[][] tarla, boolean[][] mayinKonumlari, Scanner scanner) {
        int oyunDurumu = 0; // 0: devam ediyor, 1: kazandı, -1: kaybetti

        do {
            tarlayiGoster(tarla);

            int satir, sutun;

            do {
                System.out.print("Satır Giriniz : ");
                satir = scanner.nextInt();

                System.out.print("Sütun Giriniz : ");
                sutun = scanner.nextInt();

                if (satir < 0 || satir >= tarla.length || sutun < 0 || sutun >= tarla[0].length) {
                    System.out.println("Geçersiz bir konum girdiniz. Lütfen tekrar giriniz.");
                }

            } while (satir < 0 || satir >= tarla.length || sutun < 0 || sutun >= tarla[0].length);

            if (mayinKonumlari[satir][sutun]) {
                oyunDurumu = -1; // Kaybettiniz
            } else {
                int etrafdakiMayinlar = etrafdakiMayinSayisi(mayinKonumlari, satir, sutun);
                tarla[satir][sutun] = (char) (etrafdakiMayinlar + '0');

                if (etrafdakiMayinlar == 0) {
                    acilmayanNoktalariAc(tarla, mayinKonumlari, satir, sutun);
                }

                oyunDurumu = kontrolEt(tarla, mayinKonumlari);
            }

        } while (oyunDurumu == 0);

        tarlayiGoster(tarla);

        if (oyunDurumu == 1) {
            System.out.println("Oyunu Kazandınız !");
        } else {
            System.out.println("Game Over!!");
        }
    }

    private static int etrafdakiMayinSayisi(boolean[][] mayinKonumlari, int satir, int sutun) {
        int mayinSayisi = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int yeniSatir = satir + i;
                int yeniSutun = sutun + j;

                if (yeniSatir >= 0 && yeniSatir < mayinKonumlari.length &&
                        yeniSutun >= 0 && yeniSutun < mayinKonumlari[0].length) {
                    mayinSayisi += (mayinKonumlari[yeniSatir][yeniSutun] ? 1 : 0);
                }
            }
        }

        return mayinSayisi;
    }

    private static void acilmayanNoktalariAc(char[][] tarla, boolean[][] mayinKonumlari, int satir, int sutun) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int yeniSatir = satir + i;
                int yeniSutun = sutun + j;

                if (yeniSatir >= 0 && yeniSatir < tarla.length &&
                        yeniSutun >= 0 && yeniSutun < tarla[0].length &&
                        tarla[yeniSatir][yeniSutun] == '-') {
                    int etrafdakiMayinlar = etrafdakiMayinSayisi(mayinKonumlari, yeniSatir, yeniSutun);
                    tarla[yeniSatir][yeniSutun] = (char) (etrafdakiMayinlar + '0');

                    if (etrafdakiMayinlar == 0) {
                        acilmayanNoktalariAc(tarla, mayinKonumlari, yeniSatir, yeniSutun);
                    }
                }
            }
        }
    }

    private static int kontrolEt(char[][] tarla, boolean[][] mayinKonumlari) {
        for (int i = 0; i < tarla.length; i++) {
            for (int j = 0; j < tarla[0].length; j++) {
                if (tarla[i][j] == '-' && !mayinKonumlari[i][j]) {
                    return 0; // Oyun devam ediyor
                } else if (tarla[i][j] == '-' && mayinKonumlari[i][j]) {
                    return -1; // Oyun kaybedildi
                }
            }
        }

        return 1; // Oyun kazanıldı
    }

    private static void tarlayiGoster(char[][] tarla) {
        System.out.println("===========================");
        for (int i = 0; i < tarla.length; i++) {
            for (int j = 0; j < tarla[0].length; j++) {
                System.out.print(tarla[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("===========================");
    }
}
