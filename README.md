# 🌿 **Luontopeli**  
*Android-sovellus luonnon tutkimiseen, havaintojen tallentamiseen ja retkien seuraamiseen.*

Luontopeli on Android-sovellus, jossa käyttäjä lähtee luontoretkelle ja kirjaa havaintojaan: kasveja, eläimiä ja muita luontokohteita. Sovellus laskee askeleita, tallentaa GPS-reitin, ottaa kuvia, tunnistaa lajeja ML Kitillä ja synkronoi havainnot Firebaseen (offline-first).

---

## ✅ **Toteutetut tehtävät**

Olen toteuttanut **kaikki pakolliset tehtävät (perussuoritus)** sekä **lisäominaisuuksia (lisäominaisuudet)**.

### 🔹 Perustoiminnot (70 p)
| Viikko | Aihe | Toteutus | Pisteet |
|-------|------|-----------|---------|
| Vk 1 | M3 + navigaatio | Toimiva navigaatio, Material 3 -teema, MVVM-kansiorakenne | 10 p |
| Vk 2 | Anturit | Askelmittari toimii, WalkStatsCard näyttää datan | 10 p |
| Vk 3 | GPS + kartat | osmdroid-kartta, reitti piirtyy Polyline-komponentilla | 10 p |
| Vk 4 | CameraX | Kuvan ottaminen ja tallennus Room-tietokantaan | 10 p |
| Vk 5 | ML Kit | Lajintunnistus toimii, luottamusprosentti näkyy | 10 p |
| Vk 6 | Offline-arkkitehtuuri | Firebase-stubit, DiscoverScreen listaa Room-datan | 10 p |
| Vk 7 | Viimeistely | Splash screen, adaptive icon, release AAB | 10 p |

**Perussuoritus yhteensä: 70 p**

---

### 🔹 Lisäominaisuudet (20 p)
| Lisäominaisuus | Kuvaus | Pisteet |
|----------------|--------|---------|
| Löydöt kartalla | NatureSpot-merkit näkyvät kartalla oikeissa GPS-koordinaateissa | 5 p |
| Oma kommentti | Käyttäjä voi lisätä muistiinpanon havaintoon | 5 p |
| Useampi luokka | ML Kit tunnistaa kasvien lisäksi eläimiä, sieniä, hyönteisiä, esineitä jne. | 5 p |
| Käyttäjäprofiili | Profiili-näkymä: nimi, kuva, tilastot | 5 p |

**Lisäominaisuudet yhteensä: 20 p**  

**Kokonaispisteet: 90**

---

## 🛠️ **Teknologiat ja käyttötarkoitus**

| Teknologia | Viikko | Käyttötarkoitus |
|-----------|--------|------------------|
| **Jetpack Compose + Material 3** | Vk 1 | UI-rakennus, moderni käyttöliittymä |
| **Navigation Compose** | Vk 1 | Sovelluksen navigaatio |
| **Room + KSP** | Vk 1 | Paikallinen tietokanta, entiteetit ja DAO:t |
| **Hilt** | Vk 1 | Riippuvuusinjektio (DI) |
| **SensorManager** | Vk 2 | Askelmittari, gyroskooppi |
| **Accompanist Permissions** | Vk 2–3 | Ajonaikaiset luvat (kamera, sijainti) |
| **Android LocationManager** | Vk 3 | GPS-sijainti (GPS + verkko fallback) |
| **osmdroid (OpenStreetMap)** | Vk 3 | Karttanäkymä, reitin piirtäminen ilman API-avainta |
| **CameraX** | Vk 4 | Kameran hallinta ja kuvan ottaminen |
| **Coil (AsyncImage)** | Vk 4 | Kuvien lataus ja näyttö |
| **ML Kit Image Labeling** | Vk 5 | On-device kasvi- ja eläintunnistus |
| **Firebase (offline-stub)** | Vk 6 | Firebase-arkkitehtuuri, toteutettu paikallisesti |
| **SplashScreen API** | Vk 7 | Käynnistysruutu |
| **Adaptive Icons** | Vk 7 | Sovelluskuvake |
| **Play Store / AAB** | Vk 7 | Julkaisuvalmius |

---

## 🏗️ **Arkkitehtuuri**

Luontopeli noudattaa **MVVM-arkkitehtuuria** ja **offline-first** -periaatetta.

```
UI Layer (Compose)
MapScreen  CameraScreen  DiscoverScreen  StatsScreen
BottomNavBar
        │ observes StateFlow / collectAsState
        ▼
ViewModel Layer
NatureSpotViewModel  WalkViewModel  CameraViewModel
(Hilt @HiltViewModel tai manual DI)
        │ calls suspend fun / Flow
        ▼
Repository Layer
NatureSpotRepository  WalkRepository
(Room + Firebase-stub)
   ├───────────────┬───────────────────────┐
   │               │                       │
Room (SQLite)   Firebase-stub         Platform APIs
NatureSpotDao   AuthManager           SensorManager
WalkSessionDao  FirestoreManager      FusedLocation
AppDatabase     StorageManager        CameraX
```

---

## 📂 **Kansiorakenne**

```
app/
└── src/main/java/com/example/luontopeli/
    ├── data/
    │   ├── local/
    │   │   ├── entity/
    │   │   │   ├── NatureSpot.kt
    │   │   │   ├── WalkSession.kt
    │   │   │   └── UserProfile.kt    ←  Lisäominaisuus
    │   │   ├── dao/
    │   │   │   ├── NatureSpotDao.kt
    │   │   │   ├── WalkSessionDao.kt
    │   │   │   └── UserProfileDao.kt    ←  Lisäominaisuus
    │   │   └── AppDatabase.kt
    │   ├── remote/firebase/
    │   │   ├── AuthManager.kt
    │   │   ├── FirestoreManager.kt
    │   │   └── StorageManager.kt
    │   └── repository/
    │       ├── NatureSpotRepository.kt
    │       ├── WalkRepository.kt
    │       └── UserProfileRepository.kt    ←  Lisäominaisuus
    ├── sensor/
    │   └── StepCounterManager.kt
    ├── location/
    │   └── LocationManager.kt
    ├── camera/
    │   └── CameraScreen.kt
    ├── ml/
    │   └── PlantClassifier.kt
    ├── ui/
    │   ├── theme/
    │   ├── navigation/
    │   ├── map/MapScreen.kt
    │   ├── discover/DiscoverScreen.kt
    │   ├── stats/StatsScreen.kt
    │   └── profile/ProfileScreen.kt    ←  Lisäominaisuus
    ├── viewmodel/
    │   ├── MapViewModel.kt
    │   ├── CameraViewModel.kt
    │   ├── WalkViewModel.kt
    │   ├── DiscoverViewModel.kt
    │   ├── StatsViewModel.kt
    │   └── ProfileViewModel.kt    ←  Lisäominaisuus
    ├── LuontopeliApplication.kt
    └── MainActivity.kt
```

---

## 🙌 **Tekijä**

**Gharib Hussain Amiri**  
Android-kehittäjä & opiskelija
