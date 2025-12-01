# 🏦 Internet Banking Application

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2025.04.01-green.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-33.13.0-orange.svg)](https://firebase.google.com)
[![Material Design 3](https://img.shields.io/badge/Material%20Design-3-purple.svg)](https://m3.material.io)

Ứng dụng ngân hàng di động toàn diện được phát triển bằng Kotlin và Jetpack Compose, cung cấp giải pháp quản lý tài chính hiện đại với giao diện người dùng trực quan và nhiều tính năng đa dạng.

## 📋 Mục Lục
- [Tổng Quan](#-tổng-quan)
- [Kiến Trúc Hệ Thống](#️-kiến-trúc-hệ-thống)
- [Công Nghệ Sử Dụng](#-công-nghệ-sử-dụng)
- [Tính Năng Chính](#-tính-năng-chính)
- [Cấu Trúc Dự Án](#-cấu-trúc-dự-án)
- [Yêu Cầu Hệ Thống](#-yêu-cầu-hệ-thống)
- [Cài Đặt và Chạy](#-cài-đặt-và-chạy)

## 🎯 Tổng Quan

Internet Banking là ứng dụng ngân hàng di động đa chức năng, được xây dựng theo kiến trúc **MVVM** (Model-View-ViewModel) kết hợp với **Jetpack Compose** để tạo ra trải nghiệm người dùng mượt mà và hiện đại. Ứng dụng hỗ trợ đầy đủ các giao dịch ngân hàng cơ bản, mở rộng đến các dịch vụ giá trị gia tăng như đặt vé máy bay, đặt phòng khách sạn, và thanh toán hóa đơn.

### ✨ Điểm Nổi Bật

- **100% Jetpack Compose UI**: Giao diện được xây dựng hoàn toàn bằng Declarative UI, không sử dụng XML
- **Material Design 3**: Tuân thủ nguyên tắc thiết kế Material Design 3 mới nhất
- **Firebase Backend**: Tích hợp Firebase cho xác thực, database và lưu trữ
- **Real-time Location**: Tích hợp OSM và Google Location Services để định vị chi nhánh ngân hàng
- **Multi-role System**: Hỗ trợ đa vai trò (Customer, Officer) với quyền hạn khác nhau
- **Secure Authentication**: Bảo mật cao với Firebase Authentication và BCrypt password hashing

## 🏗️ Kiến Trúc Hệ Thống

### Mô Hình MVVM (Model-View-ViewModel)

```
┌─────────────────────────────────────────────────────────────┐
│                        View Layer                           │
│  ┌──────────────────────────────────────────────────────┐   │
│  │         Jetpack Compose UI Components                │   │
│  │  - LoginScreen, CustomerHome, OfficerHome            │   │
│  │  - Transaction Screens, Payment Screens              │   │
│  │  - Profile, Settings, Location Screens               │   │
│  └─────────────────────┬────────────────────────────────┘   │
└────────────────────────┼────────────────────────────────────┘
                         │ Observes StateFlow
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    ViewModel Layer                          │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  - LoginViewModel: Quản lý authentication            │   │
│  │  - CustomerViewModel: Quản lý giao dịch khách hàng   │   │
│  │  - OfficerViewModel: Quản lý nghiệp vụ nhân viên     │   │
│  │                                                       │   │
│  │  State Management: MutableStateFlow & StateFlow      │   │
│  └─────────────────────┬────────────────────────────────┘   │
└────────────────────────┼────────────────────────────────────┘
                         │ Updates
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                      Model Layer                            │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Data Classes (UI State):                            │   │
│  │  - LoginUiState, CustomerUiState, OfficerUiState     │   │
│  │                                                       │   │
│  │  Domain Models:                                      │   │
│  │  - User, Transaction, Bill, SavingAccount            │   │
│  └─────────────────────┬────────────────────────────────┘   │
└────────────────────────┼────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                   Data/Backend Layer                        │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Firebase Services:                                  │   │
│  │  - Firebase Authentication                           │   │
│  │  - Cloud Firestore (NoSQL Database)                 │   │
│  │  - Firebase Storage                                  │   │
│  │  - Firebase Analytics                                │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Luồng Dữ Liệu

1. **User Interaction** → View (Composable functions)
2. **Event Triggered** → ViewModel methods called
3. **Business Logic** → ViewModel processes data
4. **Firebase Operations** → CRUD operations on Firestore
5. **State Update** → StateFlow emits new state
6. **UI Recomposition** → Compose automatically updates UI

### Navigation Architecture

Sử dụng **Jetpack Navigation Compose** với type-safe routing:
- Single Activity Architecture
- NavHost với 25+ màn hình
- Deep linking support
- Shared ViewModels across navigation graph

## 🔧 Công Nghệ Sử Dụng

### Core Technologies

| Công nghệ | Phiên bản | Mục đích |
|-----------|-----------|----------|
| **Kotlin** | 2.0.21 | Ngôn ngữ lập trình chính |
| **Jetpack Compose** | BOM 2025.04.01 | Declarative UI framework |
| **Material Design 3** | Latest | Design system & components |
| **Coroutines** | 1.8.1 | Asynchronous programming |

### Android Jetpack Components

- **Lifecycle** (2.8.7): ViewModel, LiveData lifecycle management
- **Navigation Compose** (2.8.9): Type-safe navigation
- **Activity Compose** (1.10.1): Integration with Activity
- **Core KTX** (1.16.0): Kotlin extensions

### Firebase Suite

```kotlin
implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
```

- **Firebase Authentication**: Xác thực người dùng với email/password
- **Cloud Firestore**: NoSQL database cho realtime data sync
- **Firebase Storage**: Lưu trữ file và media
- **Firebase Analytics**: Theo dõi hành vi người dùng

### Security Libraries

- **BCrypt** (0.9.0): Password hashing algorithm
- **JBCrypt** (0.4): Alternative BCrypt implementation

### Location & Mapping

- **OSMDroid** (6.1.16): OpenStreetMap integration
- **Google Location Services** (21.0.1): GPS và location tracking

### Testing

- **JUnit** (4.13.2): Unit testing framework
- **Espresso** (3.6.1): UI testing
- **Compose UI Test**: Composable testing utilities

### Build & Configuration

- **Gradle** (8.9.2): Build system
- **Kotlin Gradle Plugin** (2.0.21)
- **Compose Compiler Plugin** (2.0.21)

## 🎨 Tính Năng Chính

### 👤 Hệ Thống Người Dùng

#### 1. Authentication & Authorization
- ✅ Đăng nhập với Account ID và Password
- ✅ Firebase Authentication integration
- ✅ BCrypt password encryption
- ✅ Role-based access control (Customer/Officer)
- ✅ Session management
- ✅ Secure logout

#### 2. Quản Lý Hồ Sơ
- ✅ Xem và chỉnh sửa thông tin cá nhân
- ✅ Cập nhật ảnh đại diện
- ✅ Thay đổi mật khẩu
- ✅ Quản lý thông tin liên hệ

### 💰 Dịch Vụ Ngân Hàng Cơ Bản

#### 1. Quản Lý Tài Khoản
- ✅ Xem số dư tài khoản realtime
- ✅ Nạp tiền (Deposit)
- ✅ Rút tiền (Withdraw)
- ✅ Lịch sử giao dịch đầy đủ
- ✅ Tìm kiếm và lọc giao dịch
- ✅ Export transaction history

#### 2. Chuyển Khoản (Transfer)
- ✅ Chuyển tiền nội bộ
- ✅ Chuyển tiền liên ngân hàng (mô phỏng)
- ✅ Lưu danh sách người nhận thường xuyên
- ✅ Xác nhận OTP (mô phỏng)
- ✅ Thông báo giao dịch thành công

#### 3. Tài Khoản Tiết Kiệm (Saving Account)
- ✅ Mở tài khoản tiết kiệm
- ✅ Chọn kỳ hạn: 3, 6, 12, 24 tháng
- ✅ Tính lãi suất tự động
- ✅ Theo dõi ngày đáo hạn
- ✅ Rút tiền gửi tiết kiệm

#### 4. Tài Khoản Vay (Mortgage Account)
- ✅ Xem thông tin khoản vay
- ✅ Tra cứu lịch sử trả nợ
- ✅ Thanh toán khoản vay
- ✅ Tính toán lãi suất

### 🌐 Dịch Vụ Giá Trị Gia Tăng

#### 1. Thanh Toán Hóa Đơn (Pay Bills)
- ✅ Thanh toán tiền điện
- ✅ Thanh toán tiền nước
- ✅ Thanh toán Internet
- ✅ Thanh toán học phí
- ✅ Thanh toán viện phí
- ✅ Lưu lịch sử thanh toán

#### 2. Nạp Tiền Điện Thoại
- ✅ Nạp tiền cho các nhà mạng
- ✅ Chọn mệnh giá linh hoạt
- ✅ Nạp tiền nhanh cho số thường dùng

#### 3. Location Services
- ✅ Xác định vị trí hiện tại của người dùng
- ✅ Tìm chi nhánh ngân hàng gần nhất
- ✅ Hiển thị bản đồ OSM
- ✅ Chỉ đường đến chi nhánh
- ✅ Thông tin chi tiết chi nhánh (địa chỉ, số điện thoại, giờ làm việc)

#### Các tính năng khác đang triển khai
- Đặt phòng khách sạn
- Đặt vé xem phim
- ...
- VNPay Sandbox

### 👨‍💼 Tính Năng Dành Cho Officer

#### Officer Management Panel
- ✅ Tạo tài khoản khách hàng mới
- ✅ Chỉnh sửa thông tin khách hàng
- ✅ Kích hoạt/Vô hiệu hóa tài khoản


## 📁 Cấu Trúc Dự Án

```
app/src/main/java/com/example/internetbanking/
│
├── MainActivity.kt                    # Entry point
├── AppScreen.kt                       # Navigation graph
│
├── data/                              # Data layer
│   ├── CustomerUiState.kt            # Customer state management
│   ├── LoginUiState.kt               # Authentication state
│   └── OfficerUiState.kt             # Officer state management
│
├── viewmodels/                        # ViewModel layer
│   ├── CustomerViewModel.kt          # Customer business logic
│   ├── LoginViewModel.kt             # Authentication logic
│   └── OfficerViewModel.kt           # Officer business logic
│
├── ui/                                # UI layer
│   ├── customer/                      # Customer screens
│   │   ├── CustomerHome.kt           # Dashboard
│   │   ├── Transfer.kt               # Money transfer
│   │   ├── DepositAndWithdraw.kt     # Deposit/Withdraw
│   │   ├── TransactionHistory.kt     # Transaction logs
│   │   ├── PayBills.kt               # Bill payments
│   │   ├── DepositPhoneMoney.kt      # Mobile top-up
│   │   ├── BuyFlightTickets.kt       # Flight booking
│   │   ├── BuyMovieTickets.kt        # Movie booking
│   │   ├── BookHotelRooms.kt         # Hotel booking
│   │   ├── SeatSelection.kt          # Seat selection UI
│   │   ├── LocateUserAndBank.kt      # Location services
│   │   ├── ViewAndEditProfile.kt     # Profile management
│   │   ├── mortgage/                  # Mortgage account
│   │   │   └── ViewMortgageMoney.kt
│   │   └── saving/                    # Saving account
│   │       └── CreateSaveAccount.kt
│   │
│   ├── officer/                       # Officer screens
│   │   ├── OfficerHome.kt            # Officer dashboard
│   │   ├── CreateCustomer.kt         # Customer creation
│   │   └── EditCustomerProfile.kt    # Customer editing
│   │
│   ├── shared/                        # Shared components
│   │   ├── LoginScreen.kt            # Login page
│   │   └── SharedComponents.kt       # Reusable UI components
│   │
│   └── theme/                         # Material Design 3 theme
│       ├── Color.kt                  # Color palette
│       ├── Theme.kt                  # Theme configuration
│       └── Type.kt                   # Typography system
│
└── libs/                              # Third-party libraries
    ├── merchant-1.0.24.aar           # Payment SDK
    └── merchant-1.0.25.aar           # Payment SDK (updated)
```

### Database Structure (Firebase Firestore)

```
Firestore Collections:
│
├── users/                             # User accounts
│   └── {accountId}/
│       ├── accountId: String
│       ├── fullName: String
│       ├── email: String
│       ├── phoneNumber: String
│       ├── role: String (Customer/Officer)
│       └── ...
│
├── transactions/                      # Transaction records
│   └── {transactionId}/
│       ├── fromAccount: String
│       ├── toAccount: String
│       ├── amount: Number
│       ├── timestamp: Timestamp
│       └── type: String
│
├── saving/                            # Saving accounts
│   └── {savingId}/
│       ├── accountId: String
│       ├── amount: Number
│       ├── interestRate: Number
│       └── maturityDate: Timestamp
│
├── mortgage/                          # Mortgage accounts
│   └── {mortgageId}/
│       ├── accountId: String
│       ├── loanAmount: Number
│       ├── interestRate: Number
│       └── paymentSchedule: Array
│
└── bills/                             # Bill records
    └── {billId}/
        ├── accountId: String
        ├── type: String
        ├── amount: Number
        └── paidAt: Timestamp
```

## 📱 Yêu Cầu Hệ Thống

### Development Environment
- **Android Studio**: Ladybug | 2024.2.1 hoặc mới hơn
- **JDK**: Java 11 (OpenJDK hoặc Oracle JDK)
- **Gradle**: 8.9.2 (tự động cài đặt qua wrapper)
- **Kotlin**: 2.0.21

### Target Devices
- **Min SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 15 (API 35)
- **Compile SDK**: Android 15 (API 35)

### Required Permissions
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

## 🚀 Cài Đặt và Chạy

### 1. Clone Repository

```bash
git clone https://github.com/Tan-1106/internet-banking.git
cd internet-banking
```

### 2. Cấu Hình Firebase

1. Tạo project trên [Firebase Console](https://console.firebase.google.com)
2. Thêm Android app với package name: `com.example.internetbanking`
3. Download file `google-services.json`
4. Copy vào thư mục `app/`
5. Enable các services:
   - Firebase Authentication (Email/Password)
   - Cloud Firestore
   - Firebase Storage

### 3. Build Project

```bash
# Windows
gradlew.bat assembleDebug

# Linux/Mac
./gradlew assembleDebug
```

### 4. Run Application

- Mở project trong Android Studio
- Kết nối device hoặc khởi động emulator
- Click **Run** (Shift + F10)

### 5. Database Setup (Optional)

Khởi tạo dữ liệu mẫu trong Firestore:

```javascript
// Collection: users
{
  "accountId": "CUST001",
  "email": "customer@example.com",
  "fullName": "Nguyen Van A",
  "role": "Customer",
  "balance": 10000000
}

// Collection: users (Officer)
{
  "accountId": "OFF001",
  "email": "officer@example.com",
  "fullName": "Tran Thi B",
  "role": "Officer"
}
```

## 📊 Highlights cho CV

### Technical Skills Demonstrated

**Frontend Development:**
- Xây dựng ứng dụng Android native với **100% Jetpack Compose**
- Implement Material Design 3 guidelines
- Responsive UI cho nhiều kích thước màn hình
- Complex navigation với 25+ screens

**Architecture & Patterns:**
- **MVVM Architecture** với separation of concerns
- **Unidirectional Data Flow** với StateFlow
- **Dependency Injection** principles
- Single Activity Architecture

**Backend Integration:**
- Firebase Authentication với secure password hashing
- Real-time data synchronization với Cloud Firestore
- Offline-first approach với local caching
- RESTful API concepts

**Advanced Features:**
- Location-based services với OSM integration
- Payment gateway simulation
- Real-time notifications
- Multi-role authorization system

**Code Quality:**
- Kotlin best practices và idiomatic code
- Coroutines cho async operations
- Error handling và validation
- Unit testing với JUnit

### Business Impact

- ✨ **25+ screens** với navigation phức tạp
- 🔐 **Security-first approach** với BCrypt encryption
- 📱 **Modern UI/UX** tuân thủ Material Design 3
- 🌍 **Location services** tích hợp GPS và maps
- 💳 **15+ dịch vụ** banking và value-added services
- 📊 **Real-time data** sync với Firebase
- 🏗️ **Scalable architecture** dễ maintain và extend

## 📝 License

This project is developed for educational purposes.

## 👨‍💻 Author

**Tan Nguyen**
- GitHub: [@Tan-1106](https://github.com/Tan-1106)
- Project: Internet Banking Application
- Year: 2025

---