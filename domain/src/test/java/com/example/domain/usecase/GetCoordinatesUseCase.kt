package com.example.domain.usecase

import com.example.domain.model.CoordinatesModel
import com.example.domain.repository.CoordinatesRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
<<<<<<< HEAD
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
=======
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
>>>>>>> 2e3ab7e (test commit two)
import org.junit.Test
import org.mockito.Mockito.`when`

class GetCoordinatesUseCaseTest {

    @Test
    fun testExecute() = runBlocking {

        val coordinatesRepository: CoordinatesRepository = mock()

        val getCoordinatesUseCase = GetCoordinatesUseCase(coordinatesRepository)

        val expectedCoordinates = listOf(
            CoordinatesModel(latitude = 123.456, longitude = 789.012),
            CoordinatesModel(latitude = 456.789, longitude = 123.456)
            // Add more sample coordinates as needed
        )
        // Задаем поведение mock-объекта при вызове метода getAllTests
        `when`( coordinatesRepository.getCoordinates()).thenReturn(expectedCoordinates)

        // Вызываем метод execute у GetAllTestsUseCase
        val actualTests = getCoordinatesUseCase.execute()

        // Проверяем, что метод getAllTests был вызван у mock-объекта testRepository
        verify(coordinatesRepository).getCoordinates()

        // Проверяем, что возвращенный список тестов совпадает с ожидаемым
        assertEquals(expectedCoordinates, actualTests)
    }
}