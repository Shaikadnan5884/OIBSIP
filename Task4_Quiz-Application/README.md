# Quiz Master - Advanced Android Quiz Application

An interactive and robust Android quiz application built with modern development practices. This app allows users to create custom quizzes, join others via access codes, and track their performance through detailed analytics and leaderboards, all within a sleek, modern Dark Theme.

## 🚀 Features

### 1. Identity-Based Experience
*   **Unified Authentication**: Users enter their name on the home screen which personalizes the entire experience.
*   **Quiz Creator**: Identifies the author when a new quiz is designed.
*   **Quiz Player**: Tracks who played the quiz and displays their identity in the local leaderboard.

### 2. Quiz Management & Categories
*   **Create Quiz**: Design custom multiple-choice quizzes with a title, unique access code, and category.
*   **Join Quiz**: Participate in quizzes created locally using a simple access code.
*   **Category Support**: Organize quizzes into categories such as Science, History, Technology, Sports, and General Knowledge.

### 3. Interactive Gameplay & Feedback
*   **Timed Questions**: A 15-second countdown timer for every question. The app automatically skips to the next question upon expiration.
*   **Haptic Feedback**: High-quality vibrations provide tactile feedback for incorrect answers and timer expiration.
*   **Visual Progress**: A top-mounted Progress Bar keeps players informed of their quiz completion status.

### 4. Modern Dark UI/UX
*   **Permanent Dark Mode**: A sleek, high-contrast "Midnight" theme optimized for readability and battery saving.
*   **Material Design 3**: Utilizes Material Cards, outlined input fields, and modern typography.
*   **Consistent Branding**: A professional green gradient design (`#41AB41` to `#073806`) applied across all interactive elements.

### 5. Performance Tracking & Detailed Analytics
*   **Local Leaderboard**: Scores are persistently saved using **SQLite (Room Database)**. View player names, quiz titles, scores, and timestamps.
*   **Detailed Analytics**: Post-quiz breakdown showing exactly what went right or wrong:
    *   List of all questions answered.
    *   Comparison of the user's selected answer vs. the correct answer.
    *   Visual color coding (Green for Correct, Red for Wrong).

### 6. 100% Offline Capability
*   **Room Database**: All quiz data, question banks, and results are stored locally. No internet connection is required to create, join, or analyze quizzes.

## 🛠️ Technology Stack
*   **Language**: Java
*   **UI Framework**: XML (ConstraintLayout, Material Design 3)
*   **Database**: Room Persistence Library (SQLite)
*   **Data Conversion**: Gson (used for storing complex question lists within the SQLite database)
*   **Theme**: Material3 Dark Theme (Permanent)

---
*Developed as a comprehensive learning project for Android Application Development.*
