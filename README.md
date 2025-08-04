# EssentialsMonitor

*Helping users stay informed during economic uncertainty.*

## Overview
**EssentialsMonitor** is an Android app that helps users track real-time prices and availability of essential goods—such as food, gas, and medicine—especially during times of economic crisis. It delivers location-based pricing, crowd-sourced reports, and timely alerts to help users make informed decisions.

## 🚀 Features
- Real-time price updates
- Nearby store search using location
- User-submitted price reports
- Notifications for price changes and alerts
- User authentication and profiles
- Favorites system
- Interactive maps integration
- Sensor-based features

## 🛠 Tech Stack
- **Android (Java)** - Native Android development
- **SQLite** - Local database for caching and offline functionality
- **Google Maps API** - Location services and store mapping
- **Google Play Services** - Location and maps integration
- **Material Design** - Modern UI/UX components

## 📱 App Flow
1. **Splash Screen** → Checks login status
2. **Authentication** → Login/Signup with validation
3. **Dashboard** → Main navigation hub
4. **Features** → Search, Maps, Reports, Profile

## 🔧 Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+ (Android 7.0+)
- Google Maps API Key

### Installation
1. **Clone the repository**
   ```bash
   git clone https://github.com/s23010843/essentials-monitor.git
   cd essentials-monitor
   ```

2. **Configure Google Maps API**
   - Copy `local.properties.example` to `local.properties`
   - Add your Google Maps API key:
     ```
     GOOGLE_MAPS_API_KEY=YOUR_API_KEY_HERE
     ```
   - Or update `app/src/main/res/values/api.xml` with your key

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

### Recent Bug Fixes (v1.0.3)
- Fixed duplicate resource error (`GOOGLE_MAP_API_KEY`)
- Corrected app launch flow (Splash → Login → Main)
- Fixed database schema with proper User table
- Improved authentication flow and session management
- Updated dependencies and removed duplicates
- Added network security configuration
- Enhanced error handling and user experience

## 📂 Project Structure

```bash
/essentials-monitor                # Root project directory
├── /app/                          # Main Android app module
│   ├── /build/                    # Build outputs (auto-generated)
│   ├── /release/                  # Optional: release-related assets (e.g., signing configs)
│   └── /src/                      # Source code and resources
│       ├── /main/                 # Main source set
│       │   ├── /java/             # Java/Kotlin source files
│       │   ├── /res/              # Resources (layouts, drawables, strings, etc.)
│       │   └── AndroidManifest.xml  # App manifest
│       ├── /test/                 # Unit tests
│       └── /androidTest/          # Instrumentation tests
├── /.gradle/                      # Gradle's cache directory
├── /.idea/                        # IDE settings (for IntelliJ/Android Studio)
├── .gitignore                     # Git ignored files list
├── build.gradle.kts              # Project-level Gradle config (Kotlin DSL)
├── settings.gradle.kts           # Settings file to include modules
├── gradle.properties             # Project-wide Gradle properties
├── gradlew                       # Unix shell script for Gradle wrapper
├── gradlew.bat                   # Windows batch script for Gradle wrapper
├── local.properties              # Local SDK path (not committed)
├── LICENSE                       # License file
└── README.md                     # Project documentation
```

## UI Designs
<div style="display: flex; flex-wrap: wrap; gap: 10px; justify-content: space-between;">
  <img src="screenshots/signup/wireframe-signup.png" alt="Wireframe Signup Screen" style="width: 48%; max-width: 100%;" />
  <img src="screenshots/home_welcome/emulator-home.png" alt="Emulator Home Screen" style="width: 48%; max-width: 100%;" />
  <img src="screenshots/dashboard/wireframe-dashboard.png" alt="Wireframe Dashboard Screen" style="width: 48%; max-width: 100%;" />
  <img src="screenshots/maps/wireframe-maps.png" alt="Wireframe Maps Screen" style="width: 48%; max-width: 100%;" />
</div>

## Setup Instructions
1. Clone the repo:
   ```bash
   git clone https://github.com/s23010843/essentials-monitor.git
   cd essentials-monitor
   ```
2. Open in Android Studio.  
3. Add your Firebase `google-services.json` to the `app/` folder.  
4. Build and run on a device or emulator.

## Usage
- Allow location permissions.  
- Search for essential goods to view prices.  
- Submit updated prices if needed.  
- Enable notifications for alerts.

## Contribution
Feel free to fork, create issues, or submit pull requests.

## License
This project is licensed under the [MIT License](LICENSE).