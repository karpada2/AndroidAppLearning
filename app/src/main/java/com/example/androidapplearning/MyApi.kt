package com.example.androidapplearning

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import io.github.theapache64.retrosheet.annotations.Read
import io.github.theapache64.retrosheet.annotations.Write
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface MyApi {
    @Read("SELECT *")
    @GET("FormResponses")
    suspend fun getRows(): List<Row>

    @Write
    @POST("add_row")
    suspend fun addRow(@Body addRowRequest: AddRowRequest): AddRowRequest

    // Add more API functions here
}

// Models
@Serializable
data class AddRowRequest(
    @SerialName("Count")
    val count: String,
)

@Serializable
data class Row(
    @SerialName("Timestamp")
    val timestamp: String,
    @SerialName("Count")
    val count: String,
)