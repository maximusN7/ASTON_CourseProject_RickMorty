package com.example.aston_courseproject_rickmorty.model

import android.util.Log
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailsModel(characterID: Int) {

    private val mService: RetrofitServices = Common.retrofitService
    lateinit var currentCharacter: Character
    lateinit var currentOrigin: Location
    lateinit var currentLocation: Location
    lateinit var episodeList: MutableList<Episode>

    init {
        loadOneCharacter(characterID)
    }

    private fun loadOneCharacter(id: Int) {
        mService.getOneCharacter(id).enqueue(object :
            Callback<Character> {
            override fun onResponse(
                call: Call<Character>,
                response: Response<Character>
            ) {
                currentCharacter = response.body() as Character
                loadOneOrigin(currentCharacter.origin.url!!)
                loadOneLocation(currentCharacter.location.url!!)
                loadSeveralEpisodes(currentCharacter.episode)
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                Log.e("CharacterDetailsModel", t.toString())
            }

        })
    }

    private fun loadOneLocation(url: String) {
        mService.getOneLocationByUrl(url).enqueue(object :
            Callback<Location> {
            override fun onResponse(
                call: Call<Location>,
                response: Response<Location>
            ) {
                currentLocation = response.body() as Location
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                Log.e("CharacterDetailsModel", t.toString())
            }

        })
    }

    private fun loadOneOrigin(url: String) {
        mService.getOneLocationByUrl(url).enqueue(object :
            Callback<Location> {
            override fun onResponse(
                call: Call<Location>,
                response: Response<Location>
            ) {
                currentOrigin = response.body() as Location
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                Log.e("CharacterDetailsModel", t.toString())
            }

        })
    }

    private fun loadSeveralEpisodes(urls: Array<String?>) {
        mService.getSeveralEpisodes(separateIdFromUrl(urls)).enqueue(object :
            Callback<MutableList<Episode>> {
            override fun onResponse(
                call: Call<MutableList<Episode>>,
                response: Response<MutableList<Episode>>
            ) {
                episodeList = response.body() as MutableList<Episode>
            }

            override fun onFailure(call: Call<MutableList<Episode>>, t: Throwable) {
                Log.e("LocationDetailsModel", t.toString())
            }

        })
    }

    fun getOneCharacter(): Character {
        return currentCharacter
    }

    fun getOrigin(): Location {
        return currentOrigin
    }

    fun getLocation(): Location {
        return currentLocation
    }

    fun getSeveralEpisodes(): MutableList<Episode> {
        return episodeList
    }

    fun separateIdFromUrl(urlArray: Array<String?>): String{
        var str = ""
        for (url in urlArray) {
            val baseUrl = "https://rickandmortyapi.com/api/episode/"
            str += "${url!!.substring(baseUrl.length)},"
        }

        return str.dropLast(1)
    }
}