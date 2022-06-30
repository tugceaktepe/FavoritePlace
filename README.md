# FavoritePlace

FavoritePlace is simple foursquare clone app. It stores data from user, and store Firebase Realtime Database. 

## Architecture

FavoritePlace is simple foursquare clone app. Location information entered by users stored in Firebase Realtime Database. 

I'm trying to follow clean architecture principles and MVVM architecture. The folder structure is as follows : 

### Base
Base package includes base classes that used throughout the application
 

### Data
Data package includes data model and firebase repository classes.


### DI
DI package includes components for dependency injection.

### Domain
Domain package includes logical operations such as mapping and usecases.

### UI
UI package includes Application, Activity, Fragment, ViewModel and related classes like a ReyclerViewAdapter.

### Util
Util package includes Response model for UI states. E.g. ```sealed class Response<out T> ```


## Technology

* Firebase Realtime Database
* Firebase Authentication
* Firebase Storage
* Google Maps
* View Binding
* ViewModel
* Navigation Component
* Hilt
* Kotlin Flow, Coroutines

## TODOs and Improvements
* Fix of Performance Issues
* Additional Features
* UI Design
* Unit Tests
* Instrumentation Tests




