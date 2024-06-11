package com.example.myappdecomida

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import kotlin.math.min

data class Meal(
    val strMeal: String, // nombre de la comida
    val strMealThumb: String // URL de la imagen de la comida
)

object Cart {
    val items = mutableMapOf<Meal, Int>()
}

class Comida : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comida, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val gson = Gson()

            val request = Request.Builder()
                .url("https://www.themealdb.com/api/json/v1/1/search.php?s=Chicken")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseData = response.body!!.string()
                val jsonObject = JSONObject(responseData)
                val mealsArray = jsonObject.getJSONArray("meals")
                val mealType = object : TypeToken<List<Meal>>() {}.type
                val meals = gson.fromJson<List<Meal>>(mealsArray.toString(), mealType)

                withContext(Dispatchers.Main) {
                    // Ahora tienes una lista de comidas que puedes usar para crear tus vistas
                    val gridLayout = view.findViewById<GridLayout>(R.id.grid_layout)
                    val layoutInflater = LayoutInflater.from(context)

                    for (i in 0 until min(10, meals.size)) {
                        val meal = meals[i]
                        val cardView = layoutInflater.inflate(R.layout.meal_item, gridLayout, false) as CardView

                        val imageView = cardView.findViewById<ImageView>(R.id.meal_image)
                        Picasso.get().load(meal.strMealThumb).into(imageView) // Aquí se carga la imagen

                        val addButton = cardView.findViewById<Button>(R.id.add_button)
                        val heartButton = cardView.findViewById<Button>(R.id.heart_button)
                        val mealName = cardView.findViewById<TextView>(R.id.meal_name)
                        mealName.text = meal.strMeal

                        // Aquí puedes configurar los oyentes de clic para `addButton` y `heartButton`
                        heartButton.setOnClickListener {
                            if (FavoritosData.items.contains(meal)) {
                                FavoritosData.items.remove(meal)
                                heartButton.setBackgroundResource(R.drawable.favoritehome) // Cambia a un corazón vacío
                            } else {
                                FavoritosData.items.add(meal)
                                heartButton.setBackgroundResource(R.drawable.ic_favoritorelleno) // Cambia a un corazón lleno
                            }
                        }
                        addButton.setOnClickListener {
                            val quantity = Cart.items[meal] ?: 0
                            Cart.items[meal] = quantity + 1
                            Picasso.get().load(meal.strMealThumb).into(imageView) // Aquí se carga la imagen
                        }

                        // Añade la vista al contenedor
                        gridLayout.addView(cardView)
                    }
                }
            }
        }

    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Comida().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
