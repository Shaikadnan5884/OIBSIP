# To-Do App with Authentication

A feature-rich, secure, and modern productivity application built for Android. This app allows users to manage tasks, schedules, and notes with local data persistence and a premium dark-themed interface.

## 🚀 Core Features

### 🔐 Authentication & Security
*   **User Registration:** Create an account with a username, email, and password.
*   **Secure Login:** Credential validation against a local SQLite database.
*   **Input Validation:** Real-time feedback for email formats and password strength.
*   **Persistent Sessions:** Stay logged in across app restarts. The login page only appears if the user explicitly logs out.

### 📝 Task Management (CRUD)
*   **Create:** Add tasks with a title, description, due date, priority, and category.
*   **Read:** View a list of tasks in a clean, organized dashboard.
*   **Update:** Modify existing tasks effortlessly.
*   **Delete:** Permanently remove tasks with a confirmation prompt.

### 🌟 Advanced Functionality
*   **Tabbed Organization:** Navigate between **All**, **Active**, and **Expired** tasks using a modern TabLayout.
    *   **All:** Displays every task created by the user.
    *   **Active:** Shows tasks with current or future due dates.
    *   **Expired:** Filters for tasks whose due dates have already passed.
*   **Search System:** Quickly find tasks using the real-time search bar in the toolbar.
*   **Priority Levels:** Categorize tasks as **High**, **Medium**, or **Low** with visual color-coded indicator strips.
*   **Task Categories:** Organize work by labels like **Work**, **Personal**, **Urgent**, or **Other**.
*   **Date Selection:** Integrated `DatePicker` for setting task deadlines.
*   **Empty State:** A professional "No tasks" view when your list is empty or matches no filters.

## 🎨 UI/UX Design
*   **Permanent Dark Theme:** Optimized for eye comfort and a premium aesthetic.
*   **Branding:** Signature text color implemented in **#41AB41** (Green).
*   **Material Design 3:** Utilizes `MaterialCardView`, `ExtendedFloatingActionButton`, `TabLayout`, and `Chips`.

## 🛠️ Tech Stack
*   **Language:** Java
*   **UI Design:** XML (Material Design Components)
*   **Database:** SQLite (Local Persistence)
*   **Session Management:** SharedPreferences
*   **IDE:** Android Studio

## 📊 Database Schema

### Users Table
| Column | Type | Description |
| :--- | :--- | :--- |
| `id` | INTEGER | Primary Key (Auto-increment) |
| `username` | TEXT | User's display name |
| `email` | TEXT | Unique email address for login |
| `password` | TEXT | Securely stored password |

### Tasks Table
| Column | Type | Description |
| :--- | :--- | :--- |
| `id` | INTEGER | Primary Key (Auto-increment) |
| `user_id` | INTEGER | Foreign Key (References Users) |
| `title` | TEXT | Task title |
| `description` | TEXT | Detailed task notes |
| `task_date` | TEXT | User-selected due date |
| `priority` | TEXT | High, Medium, or Low |
| `category` | TEXT | Task label |
| `timestamp` | DATETIME | Automatic creation time |

## 📂 Project Structure
*   `DatabaseHelper.java`: Manages SQLite operations and schema migrations.
*   `MainActivity.java`: Core dashboard with TabLayout and task filtering logic.
*   `LoginActivity.java`: Manages user authentication and auto-login logic.
*   `RegisterActivity.java`: Handles new user account creation with validation.
*   `TaskAdapter.java`: Bridges the task data with the RecyclerView UI.
*   `item_task.xml`: The Material 3 design for individual task cards.

---
Developed as a robust productivity tool for modern Android devices.
