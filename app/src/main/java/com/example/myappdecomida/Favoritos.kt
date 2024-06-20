package com.example.myappdecomida

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myappdecomida.comida.Meal
import com.squareup.picasso.Picasso

object FavoritosData {
    val items = mutableSetOf<Meal>()
}

class Favoritos : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favoritos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoritesLayout = view.findViewById<LinearLayout>(R.id.favorites_layout)
        val layoutInflater = LayoutInflater.from(context)

        for (item in FavoritosData.items) {
            val productView = layoutInflater.inflate(R.layout.meal_item, favoritesLayout, false)

            val nameView = productView.findViewById<TextView>(R.id.meal_name)
            nameView.text = item.strMeal

            val imageView = productView.findViewById<ImageView>(R.id.meal_image)
            Picasso.get().load(item.strMealThumb).into(imageView) // Aquí se carga la imagen del producto

            val heartButton = productView.findViewById<Button>(R.id.heart_button)
            heartButton.setBackgroundResource(R.drawable.ic_favoritorelleno) // Establece el corazón como lleno

            heartButton.setOnClickListener {
                FavoritosData.items.remove(item)
                favoritesLayout.removeView(productView)
            }

            favoritesLayout.addView(productView)
        }
    }
}