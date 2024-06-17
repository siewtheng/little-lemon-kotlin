package com.siewtheng.littlelemon.composables

interface Destinations {
    val route: String
}

object OnboardingDestination : Destinations {
    override val route = "onboarding"
}

object HomeDestination : Destinations {
    override val route = "home"
}

object ProfileDestination : Destinations {
    override val route = "profile"
}