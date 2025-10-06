package com.amme.aldunyaeyh

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.amme.aldunyaeyh.databinding.FragmentWeatherBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentWeatherBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)

        binding.Wtv.isVisible = false
        binding.Ctv.isVisible = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    showWeather(lat, lon)
                } else {
                    Snackbar
                        .make(binding.root, "Unable to detect your location", Snackbar.LENGTH_LONG)
                        .show()

                }
            }
        }
    }

    private fun showWeather(lat: Double, lon: Double) {
        val apiKey = "api_key"
        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherCallable::class.java)

        retrofit.getWeather(lat, lon, apiKey).enqueue(object : Callback<Weather>{
            override fun onResponse(
                call: Call<Weather>,
                response: Response<Weather>
            ) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    val temp = weather?.main?.temp
                    val city = weather?.name
                    binding.prog.isVisible = false
                    binding.Wtv.isVisible = true
                    binding.Wtv.text = "Current Temp: $temp°C"
                    binding.Ctv.isVisible = true
                    binding.Ctv.text = "City: $city"

                } else {
                    Snackbar
                        .make(binding.root, "Error: ${response.code()}", Snackbar.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(
                call: Call<Weather>,
                t: Throwable
            ) {
                Snackbar
                    .make(binding.root, "Failed: ${t.message}", Snackbar.LENGTH_LONG)
                    .show()
            }
        })
    }
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    showWeather(location.latitude, location.longitude)
                }
            }
        }
    }

}
