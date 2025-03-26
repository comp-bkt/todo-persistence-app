# todo-persistence-app

This project was originally written in Java - as developing for Android has evolved 
using Kotlin and Jetpack Compose - this repository now contains 3 branches. The 
application does the same on each branch - albeit Compose may look different on the UI.

All branches compile and target to Android 15 (SDK 35)

### main branch
* kotlin and compose (2nd Draft)
### kotlin-room-db branch
* This changes the database implementation from the sqllitedatabase object, used in the kotlin branch, to a roomdatabase object.
* Note that the db has been built with allowMainThreadQueries() to allow it to run on the mainUI thread
### kotlin branch
* Code refactored to Kotlin (non room database implementation)
### java branch
* original java code (non room database implementation)