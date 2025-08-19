# EssentialsMonitor

*Helping users stay informed during economic uncertainty.*

## Overview
**EssentialsMonitor** is an Android app that helps users track real-time prices and availability of essential goods—such as food, gas, and medicine—especially during times of economic crisis. It delivers location-based pricing, crowd-sourced reports, and timely alerts to help users make informed decisions.

## Features

### Core Features
- **Real-time price updates** - Get current prices from multiple stores
- **Nearby store search** using location services
- **User-submitted price reports** with verification system
- **Notifications** for price changes and alerts
- **User authentication and profiles**
- **Favorites system** for frequently checked items
- **Interactive maps integration** with Google Maps

### New Enhanced Features
- **Price Analytics Dashboard** - View price trends, charts, and statistics
- **Smart Shopping List** - Budget tracking with real-time calculations
- **Advanced Notification System** - Background price monitoring service
- **Product Review System** - Rate and review products
- **Emergency Alert System** - Find nearby essential stores during emergencies
- **Price Trend Analysis** - Detailed charts showing price history
- **Budget Management** - Set and track shopping budgets
- **Enhanced Maps with Real-time Updates** - CRUD store operations with 30-second auto-refresh
- **Hybrid Database Architecture** - Intelligent switching between MongoDB and SQLite
- **Dark Mode Support** (planned)
- **Offline Data Sync** (planned)

### Smart Features
- **AI-powered price predictions** (coming soon)
- **Automatic product categorization**
- **Location-based price alerts**
- **Store hours and real-time status**
- **Emergency contact integration**

## Tech Stack
- **Android (Java)** - Native Android development
- **SQLite** - Local database for caching and offline functionality
- **MongoDB Atlas** - Cloud database with Data API integration
- **Google Maps API** - Location services and store mapping
- **Google Play Services** - Location and maps integration
- **Material Design** - Modern UI/UX components
- **Background Services** - Real-time notifications

## App Flow
1. **Splash Screen** → Checks login status
2. **Authentication** → Login/Signup with validation
3. **Dashboard** → Enhanced navigation hub with new features
4. **Core Features** → Search, Maps, Reports, Profile
5. **New Features** → Analytics, Shopping List, Emergency

## What's New in Version 1.1.0

### Enhanced User Experience
- **Smart Shopping Lists**: Create shopping lists with budget tracking and real-time price calculations
- **Advanced Analytics**: View comprehensive price trends with interactive charts
- **Background Monitoring**: Automatic price tracking with smart notifications

### Improved Functionality
- **Emergency Features**: Quick access to emergency services and nearby essential stores
- **Review System**: Rate and review products to help the community
- **Enhanced Database**: Improved data structure with better performance
- **Better Notifications**: More intelligent alerts based on user preferences

### Technical Improvements
- **Background Services**: Continuous price monitoring without draining battery
- **Better Performance**: Optimized database queries and UI rendering
- **Enhanced Security**: Improved data encryption and user privacy

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+ (Android 7.0+)
- Google Maps API Key
- MongoDB Atlas account (for cloud database features)

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

3. **Configure MongoDB Connection**
   - **Single Environment Setup** - Only MONGODB_URI is used (no multiple environments)
   - Update `MongoDBManager.java` with your MongoDB Atlas connection string:
     ```java
     private static final String API_KEY = "mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
     ```
   - The app uses a unified MongoDB URI approach for simplicity and consistency

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

### Code Quality Guidelines - Avoiding Duplicate Code

#### Anti-Duplication Strategies Implemented:
1. **Unified Database Interface** - `DatabaseManager.java` provides single interface for both MongoDB and SQLite
2. **DRY Principle** - No duplicate methods across database classes
3. **Single Configuration** - One MONGODB_URI configuration instead of multiple environment setups
4. **Shared Components** - Reusable UI components and helper classes
5. **Centralized Constants** - All database constants in one location

#### Key Anti-Duplication Features:
- **DatabaseManager Pattern** - Eliminates duplicate database operation code
- **Hybrid Architecture** - Single codebase handles both cloud and local databases
- **Unified API Calls** - Common interface for all database operations
- **Shared Data Models** - Single entity classes used across all database layers
- **Common Exception Handling** - Centralized error management

#### Best Practices Followed:
```java
// Good: Single method handles both databases
databaseManager.addStore(name, address, lat, lng, phone, hours, callback);

// Avoid: Duplicate methods for different databases
// mongoManager.addStore(...);
// sqliteHelper.addStore(...);
```

### Recent Bug Fixes (v1.0.3)
- **Eliminated Code Duplication** - Removed duplicate methods in DatabaseHelper.java
- **Unified Database Architecture** - Single MONGODB_URI configuration (no multiple environments)
- **Enhanced Map Features** - Real-time updates with CRUD store operations
- Fixed duplicate resource error (`GOOGLE_MAP_API_KEY`)
- Corrected app launch flow (Splash → Login → Main)
- Fixed database schema with proper User table
- Improved authentication flow and session management
- Updated dependencies and removed duplicates
- Added network security configuration
- Enhanced error handling and user experience

### Architecture Highlights
- **Hybrid Database System** - Intelligent switching between MongoDB Atlas and SQLite
- **Single Configuration** - Uses only MONGODB_URI (simplified setup, no environment complexity)
- **Real-time Updates** - 30-second auto-refresh for dynamic map data
- **CRUD Operations** - Complete Create, Read, Update, Delete for store management
- **Anti-Duplication Design** - Centralized database operations through unified interface
- **Material Design UI** - Enhanced layouts with real-time status indicators

## Project Structure

```bash
/essentials-monitor                # Root project directory
├── /app/                          # Main Android app module
│   ├── /build/                    # Build outputs (auto-generated)
│   ├── /release/                  # release-related assets (e.g., signing configs)
│   └── /src/                      # Source code and resources
│       ├── /main/                 # Main source set
│       │   ├── /java/             # Java/Kotlin source files
│       │   │   ├── /utils/
│       │   │   │   ├── ApiConfig.java             # ApiConfig File
│       │   │   │   └── CustomUtils.java           # Custom Utils Files
│       │   │   ├── /model/
│       │   │   │   └── User.java                  # User Model
│       │   │   ├── /database/
│       │   │   │   └── DatabaseHelper.java        # SQLite database operations
│       │   │   └── /com/s23010843/essentialsmonitor/
│       │   │       ├── MongoDBManager.java        # MongoDB Atlas operations (MONGODB_URI only)
│       │   │       ├── DatabaseManager.java       # Unified database interface (anti-duplication)
│       │   │       ├── MapViewActivity.java       # Enhanced maps with real-time updates
│       │   │       └── GoogleMapManager.java      # Map marker and CRUD operations
│       │   ├── /res/                              # Resources (layouts, drawables, strings, etc.)
│       │   │   ├── /layout/
│       │   │   │   └── activity_....xml           # XML Files
│       │   │   └── /values/
│       │   │       └── colors.xml                 # Enhanced color scheme
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

### Key Design Patterns
- **Unified Database Interface** - `DatabaseManager.java` eliminates code duplication
- **Single Configuration** - Only MONGODB_URI used (no multiple environments)
- **Factory Pattern** - Intelligent database selection based on connectivity
- **Observer Pattern** - Real-time updates with callback interfaces
- **Repository Pattern** - Clean separation between data sources

## UI Designs
<div style="display: flex; flex-wrap: wrap; gap: 10px; justify-content: space-between;">
  <img src="screenshots/signup/wireframe-signup.png" alt="Wireframe Signup Screen" style="width: 48%; max-width: 100%;" />
  <img src="screenshots/home_welcome/emulator-home.png" alt="Emulator Home Screen" style="width: 48%; max-width: 100%;" />
  <img src="screenshots/dashboard/wireframe-dashboard.png" alt="Wireframe Dashboard Screen" style="width: 48%; max-width: 100%;" />
  <img src="screenshots/maps/wireframe-maps.png" alt="Wireframe Maps Screen" style="width: 48%; max-width: 100%;" />
</div>

## Quick Start
1. **Clone the repository:**
   ```bash
   git clone https://github.com/s23010843/essentials-monitor.git
   cd essentials-monitor
   ```

2. **Configure MongoDB (Single Environment):**
   - Update `MongoDBManager.java` with your MONGODB_URI:
   ```java
   private static final String API_KEY = "mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
   ```
   - **Note:** Only one MONGODB_URI is used - no multiple environments for simplicity

3. **Configure Google Maps API:**
   - Add your Google Maps API key to `local.properties`:
   ```
   GOOGLE_MAPS_API_KEY=YOUR_API_KEY_HERE
   ```

4. **Open in Android Studio and build the project**

5. **Run on device or emulator**

## Database Configuration
- **Unified Approach:** Single MONGODB_URI configuration (avoids environment complexity)
- **Hybrid System:** Automatic fallback from MongoDB to SQLite when offline
- **Anti-Duplication:** `DatabaseManager.java` provides single interface for all operations
- **Real-time Sync:** 30-second updates between cloud and local databases

## Usage
- Allow location permissions for real-time store mapping
- Search for essential goods to view current prices  
- Add/edit/delete stores using enhanced map interface
- Submit updated prices with verification system
- Enable notifications for price alerts and updates

## Code Quality Features
- **No Duplicate Code:** Eliminated all duplicate methods and classes
- **Single Configuration:** Only MONGODB_URI used (no multiple environment setups)
- **Unified Interface:** All database operations through single `DatabaseManager`
- **Real-time Updates:** Enhanced maps with 30-second auto-refresh
- **CRUD Operations:** Complete store management with instant UI feedback

## Contribution
Feel free to fork, create issues, or submit pull requests.

## License
This project is licensed under the [MIT License](LICENSE).