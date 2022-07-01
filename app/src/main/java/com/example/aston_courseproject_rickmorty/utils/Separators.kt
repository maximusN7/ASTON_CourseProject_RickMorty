package com.example.aston_courseproject_rickmorty.utils

class Separators {
    companion object {

        fun separateIdFromUrlEpisode(url: String): Int {
            val id: Int
            val baseUrl = "https://rickandmortyapi.com/api/episode/"
            id = url.substring(baseUrl.length).toInt()
            return id
        }

        fun separateIdFromUrlCharacter(url: String): Int {
            val id: Int
            val baseUrl = "https://rickandmortyapi.com/api/character/"
            id = url.substring(baseUrl.length).toInt()
            return id
        }

        fun separateIdFromUrlLocation(urlArray: String?): Int {
            val id: Int
            val baseUrl = "https://rickandmortyapi.com/api/location/"
            id = urlArray!!.substring(baseUrl.length).toInt()
            return id
        }

        fun separateIdFromUrlCharacter(urlArray: Array<String>?): String {
            var str = ""
            if (urlArray == null) return ""
            for (url in urlArray) {
                val baseUrl = "https://rickandmortyapi.com/api/character/"
                str += "${url.substring(baseUrl.length)},"
            }

            return str.dropLast(1)
        }

        fun separateIdFromUrlEpisode(urlArray: Array<String>?): String {
            var str = ""
            if (urlArray == null) return ""
            for (url in urlArray) {
                val baseUrl = "https://rickandmortyapi.com/api/episode/"
                str += "${url.substring(baseUrl.length)},"
            }

            return str.dropLast(1)
        }

    }
}