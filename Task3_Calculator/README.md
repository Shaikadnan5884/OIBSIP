# Modern Green Gradient Calculator

A professional, feature-rich Android calculator app with a modern dark aesthetic and vibrant green gradient design.

## 🚀 Key Features

### 🎨 Stunning Visual Design
- **Custom Green Gradient Theme**: All buttons, including digits and operators, feature a premium green gradient (`#41AB41` to `#077307`) for a sleek, modern look.
- **High-End Dark Aesthetic**: Deep black background (`#000000`) optimized for battery efficiency and high contrast.
- **Circular Button Layout**: Perfectly rounded interactive elements with Material ripple effects.

### 🧠 Advanced Calculation Logic
- **Real-time Evaluation**: Results are calculated and displayed instantly as you type, without needing to press the "Equals" button.
- **Multi-Operation Support**: Handle complex equations in one go (e.g., `6 + 5 * 7 - 10`).
- **Operator Precedence**: Built-in recursive-descent parser that correctly respects mathematical rules (PEMDAS/BODMAS).
- **Full Arithmetic Suite**: Supports addition, subtraction, multiplication, division, and percentage/modulo.

### 📱 Intelligent Display
- **Dynamic Text Resizing**: The equation text automatically scales down as you type more characters (from 56sp to 24sp), ensuring long expressions never clip off-screen.
- **Intermediate Results**: A secondary display line shows the calculated result of your current partial equation.
- **Animated Cursor**: A custom visual cursor to indicate the current input position.

### 🛠 User Experience (UX)
- **Backspace Support**: Easily correct mistakes one character at a time.
- **Double Zero (00)**: Specialized button for faster entry of large numbers.
- **Material 3 Integration**: Uses modern Android styles while maintaining complete control over custom branding.

## 🛠 Built With
- **Java**: Robust core logic.
- **XML Layouts**: High-performance UI using `ConstraintLayout` and `GridLayout`.
- **Custom Vector Graphics**: Precise icons for backspace and operations.

## 📂 Project Structure
- `MainActivity.java`: Contains the expression parser and UI binding logic.
- `activity_main.xml`: The primary layout definition.
- `btn_circle_bg.xml`: Custom drawable defining the green gradient and circular shape.
- `themes.xml`: Global styling and Material 3 overrides.

---
*Created with care for a seamless mathematical experience.*
