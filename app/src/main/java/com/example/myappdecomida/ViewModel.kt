package com.example.myappdecomida

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var cartItems: Array<Dish> = arrayOf()
}