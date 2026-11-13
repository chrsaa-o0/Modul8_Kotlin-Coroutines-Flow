Modul8_Kotlin-Coroutines-Flow

Nama: Charisa Salsabila Larasati

NIM: 24515020111022

Deskripsi: Tugas praktikum Pengembangan Aplikasi Perangkat Bergerak (PAPB) mengenai Kotlin Coroutines, Asynchronous Flow, dan Implementasi Fitur Refresh pada Arsitektur Android MVVM

Proyek ini merupakan pengembangan lanjutan dari NavigationLab yang menerapkan pemanfaatan Kotlin Coroutines dan Reactive StateFlow untuk menangani operasi asinkron pada aplikasi Android berbasis Jetpack Compose. Fokus utamanya adalah memastikan bahwa seluruh proses pemanggilan data berbasis jaringan (Retrofit REST API) dieksekusi secara asinkron di latar belakang tanpa memblokir thread utama, serta mempertahankan prinsip Clean Architecture di mana business logic dan penanganan state dikelola secara terpusat di dalam ViewModel.

Dalam pengembangannya, fungsionalitas utama difokuskan pada implementasi fitur Refresh manual yang dipicu dari komponen antarmuka HomeScreen melalui tombol khusus. Ketika tombol ditekan, event tersebut meneruskan instruksi ke HomeViewModel untuk menjalankan fungsi asinkron menggunakan viewModelScope.launch. Sistem secara cerdas mendeteksi kondisi query aktif baik saat kolom pencarian kosong (memuat ulang daftar awal mahasiswa secara keseluruhan) maupun saat berisi teks tertentu (mengulang pencarian dengan kata kunci aktif).

Selain itu, manajemen state reaktif diperkuat melalui pembaruan status isLoading dan errorMessage secara tepat untuk memberikan umpan balik visual yang responsif kepada pengguna. Seluruh implementasi dipastikan mematuhi standar keamanan arsitektur modern tanpa melibatkan GlobalScope ataupun membuat coroutine scope mandiri di lapisan UI, sehingga stabilitas navigasi antar layar dan alur kembali (back stack) tetap berjalan mulus dan andal.
