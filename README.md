














Weather 
	Weather is weather app that showing of your current location and specific city/town/village. It is built using jetpack compose and kotlin. The app show’s sevaral weather data.
Features
	Work Flow- a) Showing Splash screen then weather screen shows in that the 
                                     acutal weather data shows based on your current location.
                                b) In weather screen shows In that at top end postion one navigation           
                                     button is shows then we will navigate to search weather screen.
                                C) In the search screen we can put the specific city/town/village/ name 
                                     on that basis we can get weather data by navigating to 
                                     SpecificWeatherCityScreen, in  that screen we will see the weather  
                                     Data.



                               



 





















	User Interface- The app has a smooth and interactive user interface built by jetpack compose which is google’s official declarative ui framework for android OS.
	Interactive Elements- The app includes interative elements such as Progrees bar, Modal bottom sheet, Alert dialog.
	Color Scheme- The app using descent color’s on ui component’s.
Technologies Used 
Languages and Frameworks
	Jetpack Compose- The Declarative framework built on kotlin used to create ui.
	 Kotlin- The main programming language which is used to develop the app        
major backend.

Architecture
	MVVM(Model-View-ViewModel)- The app using the mvvm architecture pattern to arrange code separatly. It includes data,presentation,domain layers.
 
Libraries
	Retrofit- Used for making network call and fetching weather data from external data source.
	Gson- Used Gson convertor factory to convert json response into user readable format.
	Navigation- Using google official type-safe navigation library for seamless navigation between all screens.
	Coil- Used the coil library to load the images.
	For fetching location- Used the google library to fetch user current location.
	 Dagger Hilt- Used dagger hilt for dependency injection with retrofit.

API Source
	Weather API- The app fetches weather data from WeatherApi. You will need to sign up and obtain the API key to use the service & get weather data. 



