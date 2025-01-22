There are two ways to run this app:
To run the server separately, either
- From it's own repo, or
- From its' location in this repo, at
```app/src/main/java/dtu/dk/introDistributedProjectApp/server/Main.java```


Then, run at least two clients (including the one that is potentially hosting) and make them join.

Minimum number of players can be configured easily in the source code, but playing alone is lonely.

If running the client in android studio to an external server on the same computer, the desired IP address is by default 10.0.2.2. Otherwise, you are in a GitHub repo and know how to find your own IP address

Known issues related to building with Gradle:
 - Make sure to sync with gradle
 - You may need to change your Gradle version to 8.10.2
 - You should be using Android Studio 2024.2.2 or newer
 - If you are having issues with Java 23, downgrading to 21 may help:
```
   compileOptions {
   sourceCompatibility = JavaVersion.VERSION_23
   targetCompatibility = JavaVersion.VERSION_23
   }
```
Can be changed to
```
   compileOptions {
   sourceCompatibility = JavaVersion.VERSION_21
   targetCompatibility = JavaVersion.VERSION_21
   }
```

and
```
kotlinOptions {
jvmTarget = "23"
}
```
to
```
kotlinOptions {
jvmTarget = "21"
}
```

Source code is hosted on GitHub:
Client (This repo): https://github.com/luucmeldgaard/IntroDistributedProjectApp
and Server (A subsection of this repo): https://github.com/s224268/introDistributedSystemsProject

