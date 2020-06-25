# Android Bubble Logger

[![Build Status](https://travis-ci.org/hiking93/android-bubble-logger.svg?branch=master)](https://travis-ci.org/hiking93/android-bubble-logger)
[![JitPack](https://jitpack.io/v/hiking93/android-bubble-logger.svg)](https://jitpack.io/#hiking93/android-bubble-logger)

A tool to display logs within a floating bubble.

![Hero image](https://user-images.githubusercontent.com/3449834/85727654-caab7d80-b729-11ea-9b80-42b5d829279f.png)

## Features

* Display logs within a floating bubble.
* Filter items with mulitiple keywords.
* System dark mode support of Android 10+.

## Requirements

To display logs in a bubble, you need Android version that supports [Bubble API](https://developer.android.com/guide/topics/ui/bubbles).

* Android 11 or later
* Android 10 with Bubbles enabled in developer settings.

## Screenshots

![Sample](https://user-images.githubusercontent.com/3449834/85727699-d139f500-b729-11ea-8150-2c4041569e0c.png)
![Filter](https://user-images.githubusercontent.com/3449834/85727691-d008c800-b729-11ea-900b-06d41d6c1287.png)
![Customized](https://user-images.githubusercontent.com/3449834/85727685-ced79b00-b729-11ea-8d45-0e6d8b59a615.png)

## Add dependency

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Then add the dependency:

```groovy
dependencies {
    implementation 'com.github.hiking93:android-bubble-logger:latestVersion'
}
```

## Usage

To add a log in the bubble:

```kotlin
BubbleLogger.log(
    context = context,
    title = title,
    message = message,
    notificationId = NOTIFICATION_ID,
    notificationChannelId = CHANNEL_ID_BUBBLE_LOGGER
)
```

See [example project](https://github.com/hiking93/android-bubble-logger/tree/master/example) for full usage example.

## License

```lang-none
Copyright Hsingchien Cheng

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
