package com.example.aston_courseproject_rickmorty.utils.mapper

import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeDto
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class EpisodeMapperTest {

    lateinit var SUT: EpisodeMapper

    @Before
    fun setUp() {
        SUT = EpisodeMapper()
    }

    @Test
    fun transform_usualEpisode_usualEpisodeDto() {
        val input = Episode(1, "Pilot", "December 2, 2013", "S01E01", arrayOf("https://rickandmortyapi.com/api/character/1","https://rickandmortyapi.com/api/character/2","https://rickandmortyapi.com/api/character/35","https://rickandmortyapi.com/api/character/38","https://rickandmortyapi.com/api/character/62","https://rickandmortyapi.com/api/character/92","https://rickandmortyapi.com/api/character/127","https://rickandmortyapi.com/api/character/144","https://rickandmortyapi.com/api/character/158","https://rickandmortyapi.com/api/character/175","https://rickandmortyapi.com/api/character/179","https://rickandmortyapi.com/api/character/181","https://rickandmortyapi.com/api/character/239","https://rickandmortyapi.com/api/character/249","https://rickandmortyapi.com/api/character/271","https://rickandmortyapi.com/api/character/338","https://rickandmortyapi.com/api/character/394","https://rickandmortyapi.com/api/character/395","https://rickandmortyapi.com/api/character/435"), "https://rickandmortyapi.com/api/episode/1", "2017-11-10T12:56:33.798Z")
        val expected = EpisodeDto(1, "Pilot", "December 2, 2013", "S01E01", "1,2,35,38,62,92,127,144,158,175,179,181,239,249,271,338,394,395,435")
        val result = SUT.transform(input)
        assertEquals(result, expected)
    }

    @Test
    fun transform_emptyArrayOfCharactersEpisode_usualEpisodeDto() {
        val input = Episode(1, "Pilot", "December 2, 2013", "S01E01", arrayOf(), "https://rickandmortyapi.com/api/episode/1", "2017-11-10T12:56:33.798Z")
        val expected = EpisodeDto(1, "Pilot", "December 2, 2013", "S01E01", "")
        val result = SUT.transform(input)
        assertEquals(result, expected)
    }

    @Test
    fun transform_nullArrayOfCharactersEpisode_usualEpisodeDto() {
        val input = Episode(1, "Pilot", "December 2, 2013", "S01E01", null, "https://rickandmortyapi.com/api/episode/1", "2017-11-10T12:56:33.798Z")
        val expected = EpisodeDto(1, "Pilot", "December 2, 2013", "S01E01", "")
        val result = SUT.transform(input)
        assertEquals(result, expected)
    }

    @Test
    fun transform_episodeOfNulls_emptyEpisodeDto() {
        val input = Episode(null, null, null, null, null, null, null)
        val expected = EpisodeDto(0, "", "", "", "")
        val result = SUT.transform(input)
        assertEquals(result, expected)
    }
}