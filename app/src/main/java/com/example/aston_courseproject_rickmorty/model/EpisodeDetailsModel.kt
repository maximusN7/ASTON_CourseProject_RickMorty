package com.example.aston_courseproject_rickmorty.model

import android.util.Log
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

class EpisodeDetailsModel(val episodeID: Int) {

    private val mService: RetrofitServices = Common.retrofitService
    lateinit var currentEpisode: Episode
    lateinit var characterList: MutableList<Character>

    /*init {
        loadOneEpisode(episodeID)
    }*/

    /*fun loadOneEpisode(id: Int) {
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
    }*/

    /*private fun loadSeveralCharacters(urls: Array<String>) {
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
    }*/

    /* override suspend fun getOneEpisode(): Episode {
         val result = mService.getOneEpisode(episodeID).await()
         return result
     }*/

    suspend fun getOneEpisode(): ApiResponse<Episode> {
        return try {
            val response = mService.getOneEpisode(episodeID)

            ApiResponse.Success(data = response)
        } catch (e: HttpException) {
            ApiResponse.Error(exception = e)
        } catch (e: IOException) {
            ApiResponse.Error(exception = e)
        }
    }

    /*suspend fun getOneEpisode(): Episode {
        val result = loadOneEpisode(episodeID).await()
        return currentEpisode
    }*/

    fun getSeveralCharacters(): MutableList<Character> {
        return characterList
    }

    fun separateIdFromUrl(urlArray: Array<String>): String {
        var str = ""
        for (url in urlArray) {
            val baseUrl = "https://rickandmortyapi.com/api/character/"
            str += "${url.substring(baseUrl.length)},"
        }

        return str.dropLast(1)
    }

    sealed class ApiResponse<T>(
        data: T? = null,
        exception: Exception? = null
    ) {
        data class Success<T>(val data: T) : ApiResponse<T>(data, null)

        data class Error<T>(
            val exception: Exception
        ) : ApiResponse<T>(null, exception)
    }

}