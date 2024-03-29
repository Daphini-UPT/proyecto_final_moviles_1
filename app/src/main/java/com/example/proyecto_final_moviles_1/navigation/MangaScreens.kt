package com.example.proyecto_final_moviles_1.navigation

enum class MangaScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    MangaHomeScreen,
    SearchScreen,
    DetailsScreen,
    UpdateScreen,
    MangaStatsScreen;
    companion object {
        fun fromRoute(route: String):MangaScreens
        = when(route?.substringBefore("/")){
            SplashScreen.name ->SplashScreen
            LoginScreen.name -> LoginScreen  // no se si esta bien ahorita
            CreateAccountScreen.name -> CreateAccountScreen
            SearchScreen.name -> SearchScreen
            DetailsScreen.name -> DetailsScreen
            UpdateScreen.name -> UpdateScreen
            MangaStatsScreen.name -> MangaStatsScreen
            null -> MangaHomeScreen
            else -> throw IllegalArgumentException("Ruta $route desconocida")
        }
    }

}