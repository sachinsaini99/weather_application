package com.example.whetherapp

import Model
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var main : LinearLayout
    lateinit var searchcity : EditText
    lateinit var button : Button
    lateinit var locationcity : TextView
    lateinit var today : TextView
    lateinit var tem : TextView
    lateinit var feellike : TextView
    lateinit var day : TextView
    lateinit var date : TextView
    lateinit var sunrise : TextView
    lateinit var sunset : TextView
    lateinit var windspeed : TextView
    lateinit var humidity : TextView
    lateinit var condtion : TextView
    lateinit var sea : TextView
    lateinit var layoutbackground : LottieAnimationView
    lateinit var cloudani : LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main = findViewById<LinearLayout>(R.id.main)
        searchcity = findViewById<EditText>(R.id.searchcity)
        button = findViewById<Button>(R.id.button)
        locationcity = findViewById<TextView>(R.id.ciylocation)
        today = findViewById<TextView>(R.id.today)
        tem = findViewById<TextView>(R.id.tem)
        feellike = findViewById<TextView>(R.id.feellike)
        day = findViewById<TextView>(R.id.day)
        date = findViewById<TextView>(R.id.date)
        sunrise = findViewById<TextView>(R.id.sunrise)
        sunset = findViewById<TextView>(R.id.sunset)
        windspeed = findViewById<TextView>(R.id.windspeed)
        humidity = findViewById<TextView>(R.id.humidity)
        condtion = findViewById<TextView>(R.id.condition)
        sea = findViewById<TextView>(R.id.sea)
        layoutbackground = findViewById<LottieAnimationView>(R.id.layoutbackground)
        cloudani = findViewById<LottieAnimationView>(R.id.cloudani)

        day.text = getday()
        date.text = getdate()
        weatherinfo("JAIPUR")

        button.setOnClickListener {
            val cityname =  searchcity.text.toString().toUpperCase()
            locationcity.text = cityname.toString()
            weatherinfo(cityname)
            searchcity.text.clear()


            }



    }
    fun weatherinfo(cityname: String) {

        val retrofilBulider = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://api.openweathermap.org/data/2.5/").build().create(ApiInterface::class.java)

        val response = retrofilBulider.getwhetherdata("$cityname","3db4b35c3672d992a3d079682cfab28a", "metric")
        response.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: retrofit2.Response<Model>) {
                if (response.isSuccessful && response != null){
                    val responsebody = response.body()



                    val temp = responsebody?.main?.temp
                    tem.text = temp.toString() +"°C"
                    feellike.text = "feel like " + responsebody?.main?.feels_like.toString()

                    val sunriseTime = responsebody?.sys?.sunrise?.toInt()
                    val sunsetTime = responsebody?.sys?.sunset?.toInt()
                    val sunriseFormatted = convertUnixToTime(sunriseTime)
                    val sunsetFormatted = convertUnixToTime(sunsetTime)

                    sunrise.text = "Sunrise: $sunriseFormatted"
                    sunset.text = "Sunset: $sunsetFormatted"


                    windspeed.text = responsebody?.wind?.speed.toString() +" Km/h"
                    humidity.text = responsebody?.main?.humidity.toString() +"%"

                    sea.text = responsebody?.main?.sea_level.toString() +" hpA"
                    val raincondition = responsebody?.clouds?.toString()
                    val cloudcondition =
                        raincondition?.substringAfter("all=")?.substringBefore(")")?.toInt()
                    condtion.text = cloudcondition.toString()

                    if (temp != null) {
                        if (temp <= 20){
                            main.setBackgroundResource(R.drawable.winter)

                            tem.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
                            feellike.setTextColor(ContextCompat.getColor(this@MainActivity,R.color.black))
                            day.setTextColor(ContextCompat.getColor(this@MainActivity,R.color.black))
                            date.setTextColor(ContextCompat.getColor(this@MainActivity,R.color.black))
                            today.setTextColor(ContextCompat.getColor(this@MainActivity,R.color.black))



                        }
                        else if (temp> 20 && temp<= 30) {
                            main.setBackgroundResource(R.drawable.winterbackground)


                        }
                        else if (temp>30 && temp <=40){
                            main.setBackgroundResource(R.drawable.sunday)

                        }
                        else{
                            main.setBackgroundResource(R.drawable.hotday)
                            cloudani.setAnimation(R.raw.hotday)
                            cloudani.playAnimation()
                        }


                    }

                    if (cloudcondition != null) {
                        if (temp != null) {
                            if (cloudcondition  <= 20 && temp <30) {
                                cloudani.setAnimation(R.raw.sunday)
                                cloudani.playAnimation()

                            } else if (cloudcondition> 20 && cloudcondition<= 50) {

                                cloudani.setAnimation(R.raw.sunday)
                                cloudani.playAnimation()
                            } else if (cloudcondition>50 && cloudcondition <=70){

                                cloudani.setAnimation(R.raw.cloudanima)
                                cloudani.playAnimation()
                            } else{

                                cloudani.setAnimation(R.raw.rain)
                                cloudani.playAnimation()
                            }
                        }


                    }


                }

            }

            override fun onFailure(call: Call<Model?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

    }

    fun getday(): String{
        val calender = Calendar.getInstance()
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return  dayFormat.format(calender.time)
    }

    fun getdate() : String{
        val calendar = Calendar.getInstance()
        val dateformat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return  dateformat.format(calendar.time)


    }
    private fun convertUnixToTime(unixTime: Int?): String {
        val date = Date(/* date = */ unixTime?.toLong()?.times(1000) ?: 0) // Convert seconds to milliseconds
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return format.format(date)
    }




}