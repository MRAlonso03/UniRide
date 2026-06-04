<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:1a3a6b,50:1e4d8c,100:2563ab&height=180&section=header&text=UniRide&fontSize=52&fontColor=ffffff&fontAlignY=40&desc=University+Carpooling+App+%7C+CUCEI+%E2%80%94+UdeG&descSize=16&descAlignY=62&descColor=bfdbfe" />

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com)
[![Google Maps](https://img.shields.io/badge/Google%20Maps-4285F4?style=for-the-badge&logo=googlemaps&logoColor=white)](https://developers.google.com/maps)

> A mobile carpooling platform exclusively for university students at CUCEI, Universidad de Guadalajara. Connect with verified peers, share rides, and reduce transportation costs.

</div>

---

## 📱 Screenshots

<div align="center">

| Login | Sign Up | Publish Ride | Ride Detail |
|-------|---------|--------------|-------------|
| Institutional email auth | Student ID verification | Interactive map route | Driver & trip info |

</div>

---

## 🚀 About UniRide

UniRide solves a real problem faced by thousands of students at CUCEI: expensive, inefficient, and unreliable daily commuting. By connecting drivers and passengers who share similar routes to campus, UniRide reduces individual transportation costs while building a safer, trust-based community.

**Why UniRide over generic apps?**
- 🔒 Access restricted to verified UdeG students (`@alumnos.udg.mx` email)
- 🎓 Student ID (matrícula) required at registration
- 🗺️ Routes optimized around the CUCEI campus area
- ⭐ Peer-to-peer reputation system for safety

---

## ✨ Features

- **Institutional Authentication** — Login exclusively with UdeG student email
- **Verified Registration** — Full name, student ID, and institutional email required
- **Publish a Ride** — Set origin, destination, departure time, and available seats on an interactive map
- **Find Rides** — Browse available trips with driver details and route information
- **Request to Join** — Send a join request to a driver with a single tap
- **Real-time Maps** — Google Maps integration for route visualization

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java |
| Platform | Android (API 26+) |
| IDE | Android Studio |
| Backend & Auth | Firebase Authentication |
| Database | Firebase Firestore |
| Maps | Google Maps SDK for Android |
| Build System | Gradle |

---

## ⚙️ Getting Started

### Prerequisites

- Android Studio (Ladybug or higher)
- Android SDK API 26+
- A Firebase project with Authentication and Firestore enabled
- Google Maps API key

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/MRAlonso03/UniRide.git

# 2. Open the project in Android Studio

# 3. Add your configuration files
#    - google-services.json → app/
#    - Add your Maps API key in AndroidManifest.xml

# 4. Sync Gradle and run on an emulator or physical device
```

### Firebase Setup

1. Create a project at [Firebase Console](https://console.firebase.google.com)
2. Enable **Email/Password** authentication
3. Create a **Firestore** database
4. Download `google-services.json` and place it inside the `app/` folder

---

## 📁 Project Structure

```
UniRide/
├── app/
│   ├── src/main/java/
│   │   ├── activities/       # Login, Register, Main, PublishRide, RideDetail
│   │   ├── adapters/         # RecyclerView adapters for ride listings
│   │   ├── models/           # User, Ride, Request data models
│   │   └── utils/            # Firebase helpers, map utilities
│   ├── res/
│   │   ├── layout/           # XML UI layouts
│   │   └── drawable/         # Icons and assets
│   └── google-services.json  # (not committed — add your own)
├── build.gradle
└── README.md
```

---

## 🎯 Target Audience

Students at **CUCEI, Universidad de Guadalajara** — approximately 18,000 enrolled students across engineering and exact sciences programs in the Guadalajara Metropolitan Area.

---

## 🌱 Roadmap

- [x] Institutional authentication
- [x] Ride publishing with map
- [x] Ride request system
- [ ] In-app notifications
- [ ] Ride history
- [ ] Driver/passenger rating system
- [ ] Chat between driver and passenger
- [ ] Wearable integration

---

## 👤 Author

**Alonso España**
[![GitHub](https://img.shields.io/badge/GitHub-MRAlonso03-181717?style=flat-square&logo=github)](https://github.com/MRAlonso03)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Alonso%20España-0077B5?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/luis-alonso-espa%C3%B1a-gomez-12baa1295/)

---

<div align="center">
<img src="https://capsule-render.vercel.app/api?type=waving&color=0:2563ab,50:1e4d8c,100:1a3a6b&height=100&section=footer"/>

*Built with ❤️ for the CUCEI community — Guadalajara, México*
</div>
