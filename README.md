# 🛍️ ShopMini — Android E-Ticaret Uygulaması

> **Kotlin · Jetpack Compose · MVVM · Hilt · Room · Supabase · Retrofit**

ShopMini, kullanıcıların ürünleri keşfedip favorileyebileceği, sepete ekleyip sipariş verebileceği modern bir Android mobil e-ticaret uygulamasıdır. Uygulama; staj projesi kapsamında, gerçek bir ürün mimarisini öğrenmek amacıyla Clean Architecture prensipleri ve Android güncel best-practice yığını kullanılarak geliştirilmiştir.

---

##  İçindekiler

1. [Proje Amacı](#-proje-amacı)
2. [Özellikler](#-özellikler)
3. [Teknoloji Yığını](#-teknoloji-yığını)
4. [Mimari](#-mimari)
5. [Dosya Yapısı](#-dosya-yapısı)
6. [Veri Akış Diyagramı](#-veri-akış-diyagramı)
7. [Ekranlar ve Navigasyon](#-ekranlar-ve-navigasyon)
8. [Veritabanı Şeması](#-veritabanı-şeması)
9. [API Entegrasyonları](#-api-entegrasyonları)
10. [Kurulum ve Çalıştırma](#-kurulum-ve-çalıştırma)
11. [Ortam Değişkenleri](#-ortam-değişkenleri)

---

##  Proje Amacı

ShopMini'nin temel hedefi, "mağaza vitrininden ödemeye" kadar olan çekirdek alışveriş akışını sağlam, hızlı ve offline'a dayanıklı biçimde kurmaktır. Gerçek bir ürün gibi tasarlanmıştır: internet bağlantısı kesildiğinde çökmemeli, yükleme sırasında boş beyaz ekran göstermemeli ve hata olduğunda kullanıcıyı bilgilendirmelidir.

---

##  Özellikler

| Alan | Özellik |
|------|---------|
| 🏠 **Ana Ekran** | Ürün grid/liste görünümü, sonsuz kaydırma (pagination), pull-to-refresh, kategori filtresi |
| 🔍 **Arama** | Debounce'lu canlı arama, arama geçmişi kaydetme/silme |
| 📦 **Ürün Detay** | Görsel galeri, yorum kartları, favoriye ekle, sepete ekle, stok kontrolü |
| 🛒 **Sepet** | Room DB ile offline sepet, adet güncelleme, ürün silme, fiyat özeti |
| ❤️ **Favoriler** | Room DB ile kalıcı favoriler, hızlı kaldırma |
| 👤 **Auth** | Supabase ile e-posta/şifre kayıt ve giriş, oturum yönetimi |
| 🗺️ **Adres Yönetimi** | Supabase'de CRUD adres işlemleri, il/ilçe seçimi (Türkiye API) |
| 💳 **Ödeme** | Kart bilgisi girişi, Luhn algoritması ile kart doğrulama, sipariş kaydetme |
| 📋 **Siparişlerim** | Supabase'den geçmiş siparişleri listeleme |
| 🛡️ **Hata Yönetimi** | Her ekranda Loading / Success / Error state'leri, tekrar dene butonu |
| 🌓 **Tema** | Light ve Dark tema desteği (Material 3, Teal renk paleti) |

---

## ️ Teknoloji Yığını

| Katman | Teknoloji | Versiyon |
|--------|-----------|----------|
| **Dil** | Kotlin | — |
| **UI** | Jetpack Compose + Material 3 | BOM ile yönetilir |
| **Mimari** | MVVM + Clean Architecture | — |
| **Async** | Kotlin Coroutines + Flow | 1.11.0 |
| **DI** | Hilt (Dagger) | 2.59.2 |
| **Network** | Retrofit + OkHttp | 2.11.0 / 4.12.0 |
| **Serializasyon** | kotlinx.serialization JSON | 1.7.3 |
| **Görsel Yükleme** | Coil (Compose) | 2.6.0 |
| **Yerel DB** | Room | 2.8.4 |
| **Navigation** | Navigation Compose | 2.9.8 |
| **Auth ve DB (Cloud)** | Supabase (auth-kt + postgrest-kt) | BOM 3.1.4 |
| **Supabase HTTP** | Ktor Client Android | 3.1.3 |
| **Crash Raporlama** | Firebase Crashlytics | BOM 34.16.0 |
| **Analitik** | Firebase Analytics | — |
| **Test** | JUnit, Turbine, Compose UI Test, Espresso | — |
| **Build** | Gradle KTS + KSP | — |

---

##  Mimari

Proje **Clean Architecture** prensiplerini benimser ve 3 ana katmana ayrılır:

```
+------------------------------------------------------------------+
|                           UI LAYER                               |
|   Screen (Compose) <-> ViewModel <-> UiState (StateFlow)        |
+------------------------------------------------------------------+
|                         DOMAIN LAYER                             |
|       UseCase  <->  Repository Interface  <->  Domain Model      |
+------------------------------------------------------------------+
|                          DATA LAYER                              |
| RepositoryImpl -> { Remote (Retrofit/Supabase) | Local (Room) } |
+------------------------------------------------------------------+
```

### Katman Sorumlulukları

| Katman | Konum | Görev |
|--------|-------|-------|
| **UI** | `ui/screens/`, `ui/components/` | Kullanıcıya gösterilecek Compose bileşenleri ve ViewModel'lar |
| **Domain** | `domain/usecase/`, `domain/repository/`, `domain/model/` | İş kuralları; ne Framework'e ne de veri kaynağına bağlı |
| **Data** | `data/remote/`, `data/local/`, `data/repository/` | Verilerin gerçekte nereden geldiğini bilen tek katman |
| **DI** | `di/` | Hilt modülleri; bağımlılıkları oluşturur ve enjekte eder |

---

##  Dosya Yapısı

```
ShopMini/
+-- app/
|   +-- build.gradle.kts             # Bagimliliklar, plugin'ler, BuildConfig alanlari
|   +-- google-services.json         # Firebase yapilandirmasi
|   +-- src/main/java/com/example/shopmini/
|       |
|       +-- ShopMiniApplication.kt        # @HiltAndroidApp giris noktasi
|       +-- MainActivity.kt               # Tek Activity, NavHost barindirir
|       |
|       +-- di/                           # Dependency Injection Modulleri
|       |   +-- NetworkModule.kt          # Retrofit (DummyJSON + Turkiye API)
|       |   +-- SupabaseModule.kt         # Supabase istemcisi
|       |   +-- DatabaseModule.kt         # Room veritabani
|       |   +-- RepositoryModule.kt       # Repository baglamalari
|       |   +-- AuthModule.kt
|       |   +-- CartModule.kt
|       |   +-- CategoryModule.kt
|       |   +-- FavoriteModule.kt
|       |   +-- OrderModule.kt
|       |   +-- AddressModule.kt
|       |   +-- SearchHistoryModule.kt
|       |
|       +-- data/                         # Veri Katmani
|       |   +-- remote/
|       |   |   +-- ShopMiniApi.kt        # Retrofit: DummyJSON urun endpoint'leri
|       |   |   +-- TurkiyeApiService.kt  # Retrofit: Turkiye il/ilce endpoint'leri
|       |   |
|       |   +-- local/
|       |   |   +-- AppDatabase.kt        # Room veritabani tanimi (version 7)
|       |   |   +-- dao/
|       |   |   |   +-- CartDao.kt
|       |   |   |   +-- FavoriteDao.kt
|       |   |   |   +-- ProductDao.kt
|       |   |   |   +-- SearchHistoryDao.kt
|       |   |   +-- entity/
|       |   |       +-- CartEntity.kt
|       |   |       +-- FavoriteEntity.kt
|       |   |       +-- ProductEntity.kt
|       |   |       +-- SearchHistoryEntity.kt
|       |   |
|       |   +-- model/                    # DTO'lar (Network/DB yanit modelleri)
|       |   |   +-- Product.kt
|       |   |   +-- ProductResponse.kt
|       |   |   +-- CategoryDto.kt
|       |   |   +-- Review.kt
|       |   |   +-- UserProfile.kt        # Supabase profiles tablosu
|       |   |   +-- OrderDto.kt           # Supabase orders tablosu
|       |   |   +-- address/
|       |   |       +-- AddressDto.kt
|       |   |       +-- ProvinceDto.kt
|       |   |       +-- ProvincesResponse.kt
|       |   |       +-- DistrictDto.kt
|       |   |       +-- DistrictsResponse.kt
|       |   |
|       |   +-- mapper/                   # DTO <-> Domain model donusumleri
|       |   |   +-- ProductMapper.kt
|       |   |   +-- AddressMapper.kt
|       |   |
|       |   +-- repository/              # Repository implementasyonlari
|       |       +-- ProductRepositoryImpl.kt
|       |       +-- CategoryRepositoryImpl.kt
|       |       +-- CartRepositoryImpl.kt
|       |       +-- FavoriteRepositoryImpl.kt
|       |       +-- AuthRepositoryImpl.kt        # Supabase Auth
|       |       +-- AddressRepositoryImpl.kt      # Supabase PostgREST
|       |       +-- OrderRepositoryImpl.kt        # Supabase PostgREST
|       |       +-- SearchHistoryRepositoryImpl.kt
|       |
|       +-- domain/                       # Domain Katmani
|       |   +-- model/
|       |   |   +-- Address.kt
|       |   |   +-- Order.kt
|       |   |
|       |   +-- repository/               # Soyut repository arayuzleri
|       |   |   +-- ProductRepository.kt
|       |   |   +-- CategoryRepository.kt
|       |   |   +-- CartRepository.kt
|       |   |   +-- FavoriteRepository.kt
|       |   |   +-- AuthRepository.kt
|       |   |   +-- AddressRepository.kt
|       |   |   +-- OrderRepository.kt
|       |   |   +-- SearchHistoryRepository.kt
|       |   |
|       |   +-- usecase/                  # Is kurallarini kapsayan Use Case'ler
|       |       +-- product/  (GetProducts, GetDetail, GetByCategory)
|       |       +-- category/ (GetCategories)
|       |       +-- cart/     (GetItems, Insert, Delete, UpdateQty, Clear)
|       |       +-- favorite/ (GetFavorites, Toggle, CheckIf, Delete)
|       |       +-- auth/     (SignUp, SignIn, SignOut, GetCurrentUser, IsLoggedIn)
|       |       +-- address/  (GetAddresses, Add, Update, Delete)
|       |       +-- order/    (SaveOrder, GetOrders)
|       |       +-- searchHistory/ (GetRecent, Insert, Delete, Search)
|       |
|       +-- ui/                           # UI Katmani
|           +-- navigation/
|           |   +-- Screen.kt             # Type-safe navigasyon rotalari
|           |   +-- BottomNavItem.kt
|           |
|           +-- theme/
|           |   +-- Color.kt              # Teal tabanli renk paleti
|           |   +-- Theme.kt              # Material 3 tema tanimi
|           |   +-- Type.kt              # Tipografi sistemi
|           |
|           +-- util/
|           |   +-- Validators.kt         # Regex tabanli form dogrulama
|           |
|           +-- components/               # Yeniden kullanilabilir Compose bilesenleri
|           |   +-- BottomNavigationBar.kt
|           |   +-- home/   (ProductCard, ProductGridWithFab, SearchBar, SearchHistoryList)
|           |   +-- cart/   (CartItemRow, CartSummaryBar)
|           |   +-- detail/ (AddFavoriteBtn, AddToCartBtn, CartQuantityControl, ReviewCard)
|           |   +-- favorite/ (FavoriteCard)
|           |   +-- checkout/ (AddressDropdown, CheckoutContent, CheckoutSummaryBar, ...)
|           |   +-- address/ (AddressCard)
|           |
|           +-- screens/
|               +-- home/       (HomeScreen + HomeViewModel + HomeUiState)
|               +-- detail/     (ProductDetailScreen + ViewModel + UiState)
|               +-- cart/       (CartScreen + CartViewModel + CartUiState)
|               +-- favorites/  (FavoriteScreen + FavoritesViewModel + FavoriteUiState)
|               +-- login/      (LoginScreen + LoginViewModel + LoginUiState)
|               +-- signup/     (SignUpScreen + SignUpViewModel + SignUpUiState)
|               +-- checkout/   (CheckoutScreen + CheckoutViewModel + CheckoutUiState)
|               +-- payment/    (PaymentScreen + PaymentViewModel + PaymentUiState)
|               +-- payment/    (OrderSuccessScreen)
|               +-- profile/    (ProfileScreen + ProfileViewModel + ProfileUiState)
|                   +-- orders/  (OrdersScreen + OrdersViewModel + OrdersUiState)
|                   +-- address/ (AddressesScreen, AddEditAddressScreen + ViewModels)
|
+-- build.gradle.kts          # Proje duzeyinde plugin tanimlari
+-- settings.gradle.kts       # Proje adi ve modul tanimi
+-- gradle.properties         # JVM/Gradle ayarlari
+-- local.properties          # [GIT DISINDA] Supabase URL ve ANON KEY
+-- .gitignore
```

---

##  Veri Akış Diyagramı

```
[KULLANICI ETKILESIMI]
        |
        v
[COMPOSE SCREEN]
  - UiState'i collectAsState() ile gozlemler
  - Kullanici eylemini ViewModel'e iletir
        |
        | viewModel.someAction()
        v
[@HiltViewModel]
  - viewModelScope.launch { ... }
  - UseCase'i cagir
  - Sonucu MutableStateFlow olarak yayinla
        |
        | useCase()
        v
[USE CASE]
  - Tek is kuralini kapsar
  - Repository arayuzunu cagir
        |
        | repository.getData()
        v
[REPOSITORY - Interface]  (Domain katmaninda tanimli soyutlama)
        |
        | (Hilt implementasyona yonlendirir)
        v
[REPOSITORY IMPLEMENTATION]
        |
  +-----+-----+
  |           |
  v           v
[REMOTE]   [LOCAL]
  |           |
  +-- ShopMiniApi (Retrofit -> DummyJSON)
  +-- TurkiyeApiService (Retrofit -> turkiyeapi.dev)
  +-- SupabaseClient (Auth + PostgREST)
              |
              +-- Room DB (AppDatabase)
                  +-- CartDao      -> cart tablosu
                  +-- FavoriteDao  -> favorites tablosu
                  +-- ProductDao   -> products tablosu
                  +-- SearchHistoryDao -> search_history tablosu
```

---

## 🗺 Ekranlar ve Navigasyon

```
         [Baslangic]
               |
       isUserLoggedIn()?
        +------+------+
       Evet         Hayir
        |              |
        v              v
   [Ana Ekran]    [Giris Yap] ---> [Kayit Ol]
        |
   +----+--------------------------------------------+
   | Alt Navigasyon: Home | Cart | Favorites | Profile |
   +----+--------------------------------------------+
        |
        +-- [Urun Detay] ---> [Sepet]
        |
        +-- [Sepet] ---> [Odeme Ozeti] ---> [Kart Odeme] ---> [Siparis Basarili]
        |
        +-- [Profil]
                +-- [Adreslerim] ---> [Adres Ekle/Duzenle]
                +-- [Siparişlerim]
```

### Tüm Rotalar

| Rota | Parametre | Açıklama |
|------|-----------|---------|
| `Screen.Home` | — | Ana ürün listesi |
| `Screen.ProductDetailScreen` | `productId: Int` | Ürün detay |
| `Screen.Cart` | — | Sepet |
| `Screen.Favorites` | — | Favoriler |
| `Screen.Profile` | — | Profil |
| `Screen.Login` | — | Giriş yap |
| `Screen.SignUp` | — | Kayıt ol |
| `Screen.Checkout` | — | Sipariş özeti / adres seçimi |
| `Screen.Payment` | — | Kart ile ödeme |
| `Screen.OrderSuccess` | — | Sipariş onay ekranı |
| `Screen.Orders` | — | Geçmiş siparişler |
| `Screen.Addresses` | — | Adres listesi |
| `Screen.AddEditAddress` | `addressId: String?` | Adres ekle / düzenle |

---

## 🗄 Veritabanı Şeması

### Room (Yerel — Cihaz İçi)

| Tablo | Sütunlar |
|-------|---------|
| `cart` | id (PK), title, price, quantity, thumbnail, discountPercentage, stock |
| `favorites` | id (PK), title, price, thumbnail, discountPercentage |
| `products` | id (PK), title, price, thumbnail, ... |
| `search_history` | id (PK, AutoGen), query, timestamp |

### Supabase (Bulut — PostgreSQL)

| Tablo | Sütunlar |
|-------|---------|
| `profiles` | id (UUID, FK → auth.users), first_name, last_name, phone, email |
| `addresses` | id (UUID, PK), user_id (FK), title, city, district, full_address, is_default |
| `orders` | id (UUID, PK), user_id (FK), items (JSONB), total_amount, created_at |

---

##  API Entegrasyonları

### 1. DummyJSON (`https://dummyjson.com`)

| Method | Endpoint | Açıklama |
|--------|----------|---------|
| `GET` | `/products?limit=20&skip=0` | Sayfalı ürün listesi |
| `GET` | `/products/{id}` | Ürün detayı |
| `GET` | `/products/category/{slug}` | Kategoriye göre ürünler |
| `GET` | `/products/categories` | Kategori listesi |
| `GET` | `/products/search?q={query}` | Ürün arama |

### 2. Türkiye API (`https://api.turkiyeapi.dev`)

| Method | Endpoint | Açıklama |
|--------|----------|---------|
| `GET` | `/v2/provinces` | Tüm iller |
| `GET` | `/v2/provinces/{id}/districts` | İle ait ilçeler |

### 3. Supabase

| Servis | Tablo | İşlem |
|--------|-------|-------|
| Auth | `/auth/v1/signup` | Kayıt |
| Auth | `/auth/v1/token` | Giriş |
| PostgREST | `profiles` | Profil okuma |
| PostgREST | `addresses` | CRUD adres |
| PostgREST | `orders` | Kaydet ve listele |

---

## 🚀 Kurulum ve Çalıştırma

### Gereksinimler

- Android Studio Meerkat veya üstü
- JDK 17
- Android SDK (minSdk **26**, targetSdk **37**)
- Supabase hesabı

### Adımlar

```bash
# 1. Repoyu klonla
git clone https://github.com/HilmiKilavuz/ShopMini.git
cd ShopMini

# 2. local.properties dosyasını düzenle (aşağıya bak)

# 3. Android Studio'da projeyi aç ve "Sync Project with Gradle Files" yap

# 4. Bir emülatör veya fiziksel cihazda çalıştır
```

---

##  Ortam Değişkenleri

Proje, hassas bilgileri `local.properties` dosyasında saklar. Bu dosya `.gitignore` ile kayıt dışı tutulur; **asla repository'e push edilmemelidir.**

`local.properties` dosyasına aşağıdaki satırları ekleyin:

```properties
# Android SDK yolu (Android Studio otomatik ekler)
sdk.dir=C\:\\Users\\<KULLANICI_ADI>\\AppData\\Local\\Android\\Sdk

# Supabase baglanti bilgileri (Supabase Dashboard > Project Settings > API)
SUPABASE_URL=https://xxxxxxxxxxxxxxxxxxxx.supabase.co
SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

> **Dikkat:** Bu değerler olmadan uygulama derlenir fakat kimlik doğrulama, adres
> ve sipariş işlemleri çalışmaz. Değerler derleme sırasında `BuildConfig` aracılığıyla
> koda gömülür (`app/build.gradle.kts` içindeki `buildConfigField` ile).

---


## Tema ve Renk Sistemi

Material 3 ile iki tema desteklenir (Light / Dark):

| Token | Light | Dark |
|-------|-------|------|
| Primary | `#00897B` | `#80CBC4` |
| Secondary | `#4DB6AC` | `#B2DFDB` |
| Tertiary | `#80CBC4` | `#E0F2F1` |
| Background | `#F5F5F5` | `#121212` |
| Surface | `#FFFFFF` | `#1E1E1E` |

---

##  Form Doğrulama Kuralları

`Validators.kt` içinde tüm doğrulamalar regex tabanlı ve saf (pure) fonksiyon olarak yazılmıştır:

| Alan | Kural |
|------|-------|
| E-posta | `kullanici@alan.com` formatı |
| Şifre | Min 8 karakter, min 1 büyük harf, min 1 rakam |
| Telefon | Türkiye formatı: `5XX XXX XX XX` |
| Ad/Soyad | Min 2 karakter, yalnızca harf (Türkçe karakterler dahil) |
| Kart Numarası | 16 hane + Luhn algoritması |
| Son Kullanma | `AA/YY` formatı + tarih geçerlilik kontrolü |
| CVV | Tam olarak 3 rakam |

---

##  Build Yapılandırması

| Parametre | Değer |
|-----------|-------|
| `applicationId` | `com.example.shopmini` |
| `minSdk` | 26 (Android 8.0 Oreo) |
| `targetSdk` | 37 |
| `versionCode` | 1 |
| `versionName` | `1.0` |
| `compileOptions` | Java 17 |
| `buildFeatures` | Compose, BuildConfig |

---

*Bu proje, staj süreci kapsamında Android geliştirme becerilerini pekiştirmek amacıyla geliştirilmektedir.*
