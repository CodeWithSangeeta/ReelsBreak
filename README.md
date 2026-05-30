# ReelBreak

ReelBreak is an Android app designed to help users reduce short-form video distractions and take back control of their screen time. It detects reels/shorts-style screens and allows users to block them instantly, apply daily limits, or start a focus session for selected apps.

## Features

- Block short-form video feeds instantly
- Set a daily reel count or time limit
- Start Focus Mode to block distracting apps for a selected duration
- Detect short-video screens using Android Accessibility Service
- Support apps like YouTube, Instagram, and Snapchat for reels detection
- Support a wider app list in Focus Mode including Facebook, TikTok, X, ShareChat, and Moj
- Premium Jetpack Compose UI with dark and light theme screens

## Screenshots

### Onboarding
The onboarding flow introduces the app, explains the protection modes, and guides users through setup.

### Dashboard
The dashboard shows the available protection modes like **Block Instantly** and **Set a Limit**.

### Focus Mode
Focus Mode allows users to choose apps, start a focus session, and block distractions during that time.

### Settings
The settings screen provides reminders, FAQ, feedback, sharing, and privacy options.

> Add your screenshots here using GitHub image markdown after uploading them to your repository.

Example:
```md
<img width="504" height="1114" alt="image" src="https://github.com/user-attachments/assets/01cb57d4-9761-4fe7-b12a-dfbbf844595d" />
<img width="504" height="1136" alt="image" src="https://github.com/user-attachments/assets/d6183b92-6c6a-4ea0-8973-d853a253740f" />
<img width="504" height="1125" alt="image" src="https://github.com/user-attachments/assets/b2ebd023-c889-4cc1-bddf-9c45add841c4" />
<img width="504" height="1125" alt="image" src="https://github.com/user-attachments/assets/0706ed43-a1f2-4450-87c1-73ab025808ba" />
<img width="504" height="1114" alt="image" src="https://github.com/user-attachments/assets/d6c18809-f7f4-4764-ae8f-d4c8ffd04526" />

<img width="717" height="1600" alt="WhatsApp Image 2026-05-21 at 3 35 34 PM (1)" src="https://github.com/user-attachments/assets/f283f8df-c7d4-48de-bbc3-2132e7abbfac" />

<img width="717" height="1600" alt="WhatsApp Image 2026-05-21 at 3 35 35 PM" src="https://github.com/user-attachments/assets/f32ee3aa-7759-48da-a785-a8781edafcaf" />


```

## Tech Stack

- Kotlin
- Jetpack Compose
- Hilt
- MVVM Architecture
- Android Accessibility Service
- DataStore Preferences

## Project Structure

```text
app/src/main/java/com/practice/reelbreak/
├── core/
│   ├── accessibility/
│   ├── action/
│   ├── debug/
│   ├── detection/
│   ├── engine/
│   ├── permission/
│   └── registry/
├── data/
│   └── preferences/
├── domain/
│   └── model/
├── ui/
│   ├── component/
│   ├── dashboard/
│   ├── focused_mode/
│   ├── navigation/
│   ├── onboarding/
│   ├── permission/
│   ├── settings/
│   └── theme/
└── viewmodel/
```

## How It Works

ReelBreak uses `ReelsAccessibilityService` to listen for accessibility events and detect when a supported short-form video screen is open. Detection is routed through app-specific detector classes such as Instagram, Snapchat, and YouTube detectors.

Once a reels/shorts screen is detected, the app uses `BlockingDecisionEngine` to decide whether to allow access, block immediately, or enforce a user-defined limit.

Focus Mode uses separate registries and a blocked-app activity to prevent selected apps from being opened until the focus session ends.

## Supported Modes

| Mode | Description |
|------|-------------|
| Block Instantly | Immediately blocks reels/short-form feeds when opened. |
| Set a Limit | Lets users define a daily reel count or time limit. |
| Focus Mode | Blocks selected distracting apps for a chosen duration. |

## Supported Apps

### Reels Detection
- YouTube
- Instagram
- Snapchat

### Focus Mode
- YouTube
- Instagram
- Instagram Lite
- Facebook
- Facebook Lite
- Snapchat
- TikTok
- X
- ShareChat
- Moj

## Getting Started

### Prerequisites

- Android Studio
- Android SDK
- Physical Android device for accessibility testing
- Gradle support matching the project setup

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/ReelBreak.git
   ```

2. Open the project in Android Studio.

3. Sync Gradle dependencies.

4. Run the app on a physical Android device.

5. Grant Accessibility permission when prompted.

## Permissions

ReelBreak requires **Accessibility permission** to detect short-form video screens and apply blocking rules. The app only checks which app/screen is active and does not read personal messages, passwords, or private content.

## UI Highlights

- Modern purple-themed UI
- Dark and light mode support
- Smooth onboarding flow
- Focus session timer screen
- Branded blocked-app interruption screen
- Expandable settings FAQ section

## Use Cases

- Reduce mindless scrolling on reels and shorts
- Enforce healthy daily limits
- Stay focused during study or work sessions
- Block distracting social media apps temporarily

## Future Improvements

- Add support for more apps
- Improve short-video detection accuracy
- Add analytics and usage insights
- Add more customizable blocking strategies

## Author

**sangeeta Yadav**

Android Developer focused on Jetpack Compose, accessibility-first design, and productivity apps.

## License

This project is licensed under the MIT License - see the `LICENSE` file for details.
