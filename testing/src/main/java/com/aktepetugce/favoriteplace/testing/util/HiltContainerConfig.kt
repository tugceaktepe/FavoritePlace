package com.aktepetugce.favoriteplace.testing.util

import android.os.Bundle
import androidx.navigation.testing.TestNavHostController

class HiltContainerConfig(
    val fragmentArgs: Bundle? = null,
    val navController: TestNavHostController,
    val navResId: Int = 0,
    val currentDestination: Int = 0
)
