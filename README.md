# Mobile Inc (Android)
IF3111 Pengembangan Aplikasi pada Platform Khusus

## Anggota kelompok Chlordane
- [Reinhard Benjamin Linardi (13515011)](https://github.com/reinhardlinardi)
- [Erick Wijaya (13515057)](https://github.com/wijayaerick)
- [Roland Hartanto (13515107)](https://github.com/rolandhartanto)

## Deskripsi subsistem Android
Subsistem Android merupakan simulasi aplikasi *mobile* dari Mobile, Inc. Subsistem ini berperan sebagai *client* yang digunakan untuk melakukan pemesanan produk *handphone*.  Setelah pengguna masuk halaman utama, maka lokasi pengguna akan dicatat dan ditampilkan semua jenis *handphone* yang dapat dibeli. Jika pengguna sudah selesai memilih *handphone*, maka pengguna dapat membuka cart dan melakukan pembayaran. 

## Fitur-fitur subsistem Android
- Subsistem memberikan layanan untuk melakukan *sign in* dengan akun Google
- Subsistem memiliki tampilan yang responsif dan dapat menyimpan data user saat user memilih *handphone* yang akan dibeli
- Subsistem dapat menerima notifikasi dari server saat pemesanan sudah diselesaikan
- Subsistem dapat menerima notifikasi dan data dari server saat server mengirimkan kode promosi
- Subsistem dapat memindai, memverifikasi dan menyimpan QR code
- Subsistem dapat di-*minimize* dengan mendeteksi *shake*
- Subsistem dapat mengecek trend *handphone* saat ini dengan memanfaatkan service
- Subsistem dapat mengirimkan dan menerima data dari server dengan HttpURLConnection

## Cara instalasi aplikasi
1. Login menggunakan Webmail STEI
2. Buka link berikut :
https://drive.google.com/a/std.stei.itb.ac.id/file/d/0B4a9OEwiZUdLQU1sZXpidW5GMFU/view?usp=sharing
3. Download APK pada link tersebut
4. Install pada device yang diinginkan

## Panduan pemakaian aplikasi
1. Login dengan menggunakan Google Account. Jika Google Account yang terdaftar pada device lebih dari satu, maka pilih Google Account yang ingin digunakan.
2. User akan menerima permission request untuk location services. Pilih Allow untuk melanjutkan.
3. User akan dibawa ke tampilan utama. Disini user dapat melihat semua jenis handphone yang dapat dipesan. Untuk melakukan filtering terhadap merk handphone, user dapat memilih fragment-fragment yang tersedia. Untuk melihat spesifikasi lengkap dari setiap handphone, user dapat melakukan *tap* pada gambar handphone. Aplikasi akan membuka browser dan membawa user ke official website dari handphone tersebut.
4. Untuk melakukan pemesanan, atur jumlah pemesanan dari tiap handphone dengan menekan tombol + dan - . Angka yang ada di antara tombol + dan - menunjukkan jumlah handphone yang akan dibeli. Jika sudah selesai, pilih cart di kanan atas, masukan detail kartu kredit dan tekan tombol payment. User akan mendapat notifikasi jika transaksi berhasil dan kembali ke tampilan utama.
    
<br /> 
<br />Homepage : http://mobileinc.herokuapp.com
<br />API server : https://github.com/reinhardlinardi/mobile-inc