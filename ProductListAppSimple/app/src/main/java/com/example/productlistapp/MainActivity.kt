package com.example.productlistapp

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val scroll = androidx.core.widget.NestedScrollView(this)
        scroll.addView(layout)
        setContentView(scroll)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonkeeper.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getProducts().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                val products = response.body()?.products ?: emptyList()
                for (product in products) {
                    val textView = TextView(this@MainActivity).apply {
                        text = "${product.name} - $${product.price} ${product.currency}"
                        textSize = 18f
                        setPadding(0, 16, 0, 16)
                    }
                    layout.addView(textView)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                val errorText = TextView(this@MainActivity).apply {
                    text = "Error al obtener productos: ${t.message}"
                    textSize = 16f
                }
                layout.addView(errorText)
            }
        })
    }
}