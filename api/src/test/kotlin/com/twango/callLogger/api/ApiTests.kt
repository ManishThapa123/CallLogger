package com.twango.callLogger.api

import junit.framework.Assert.assertNotNull
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ApiTests {
    private val apiClient = CallLoggerClient
    /**
     * Jnit5 assertions: (Use it according to the needs)
     * assertNotNull = When we want to declare or assert that the response wouldn't be null.
     * assertEquals = Asserts that expected and actual are equal.
     * assertFalse(boolean condition) = Asserts that the supplied condition is not true.
     * assertAll = Declares that all supplied executables do not throw exceptions.
     */

    @Test
    fun `Post call logs details`() {
        runBlocking {
            val callLogsResponse = apiClient.service.saveClientCallLogs(
            "+919470105448",
                "meraj alam",
                "+919905508718",
                "2021-11-09 12:30:30",
                "11:50:30",
                "1",
                "2021-11-15 18:55:55",
                )
           assertNotNull(callLogsResponse.body()?.type)
        }
    }
}