package com.example.aston_courseproject_rickmorty.model

import android.util.Log
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpisodeDetailsModel(episodeID: Int) {

    private val mService: RetrofitServices = Common.retrofitService
    lateinit var currentEpisode: Episode
    lateinit var characterList: MutableList<Character>

    init {
        loadOneEpisode(episodeID)
    }

    private fun loadOneEpisode(id: Int) {
        mService.getOneEpisode(id).enqueue(object :
            Callback<Episode> {
            override fun onResponse(
                call: Call<Episode>,
                response: Response<Episode>
            ) {
                currentEpisode = response.body() as Episode
                loadSeveralCharacters(currentEpisode.characters)
            }

            override fun onFailure(call: Call<Episode>, t: Throwable) {
                Log.e("EpisodeDetailsModel", t.toString())
            }

        })
    }

    private fun loadSeveralCharacters(urls: Array<String>) {
        mService.getSeveralCharacters(separateIdFromUrl(urls)).enqueue(object :
            Callback<MutableList<Character>> {
            override fun onResponse(
                call: Call<MutableList<Character>>,
                response: Response<MutableList<Character>>
            ) {
                characterList = response.body() as MutableList<Character>
            }

            override fun onFailure(call: Call<MutableList<Character>>, t: Throwable) {
                Log.e("EpisodeDetailsModel", t.toString())
            }

        })
    }

    fun getOneEpisode(): Episode {
        return currentEpisode
    }

    fun getSeveralCharacters(): MutableList<Character> {
        return characterList
    }

    fun separateIdFromUrl(urlArray: Array<String>): String{
        var str = ""
        for (url in urlArray) {
            val baseUrl = "https://rickandmortyapi.com/api/character/"
            str += "${url.substring(baseUrl.length)},"
        }

        return str.dropLast(1)
    }
}