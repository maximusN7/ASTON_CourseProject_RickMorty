package com.example.aston_courseproject_rickmorty.model

data class Episode(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: Array<String>,
    val url: String,
    val created: String
)

class AllEpisodes(val info: Info, val results: MutableList<Episode>)