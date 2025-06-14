This is a Kotlin Multiplatform project targeting Android, iOS.

🌟 Swipe App (Kotlin Multiplatform)
This is a modern, lightweight, and stylish Swipe App designed with Kotlin Multiplatform (KMP).
The app lets you explore content by swiping through cards — much like popular dating or photo-browsing applications — and it's fully functional on both Android and iOS with a single codebase.

🚀 Features
Kotlin Multiplatform: Share common UI and business logic across Android and iOS.

Jetpack Compose: Built with Compose Multiplatform for a declarative UI, reducing redundancy.

Swipeable Cards: Smooth Tinder-like UI to navigate through content.

Scalable: Easily adaptable to different use cases — dating, photo gallery, products, and more.

Clean Codebase: Follows modern Android architecture patterns, making it easy to maintain and scale.


🏹 Project Structure
.
├── composeApp/
│ ├─ commonMain/          // Shared UI, ViewModel, and business logic
│ ├─ androidMain/         // Android-specific code
│ └─ iosMain/             // iOS-specific code
└── iosApp/
    └─ entry for iOS application (Swift UI & Compose Multiplatform)
    
🟣 Screenshot
Here’s a preview of the Swipe App UI:
![image](https://github.com/user-attachments/assets/240995ee-20e1-4f61-a3dc-47d956b023d7)

![image](https://github.com/user-attachments/assets/30e1d63d-2537-45cb-b801-0b8d6b6a3891)


📝 How to Run
1️⃣ Clone this repository:
git clone https://github.com/your-username/swipe-app.git
2️⃣ Open in Android Studio or IntelliJ IDEA.

3️⃣ Select composeApp or iosApp to run on your preferred platform.

4️⃣ Launch the app on Emulator or a physical phone.

📘 Learn More
Kotlin Multiplatform official docs

Compose Multiplatform

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
