package com.example.androidapplearning

import de.jensklingenberg.ktorfit.Ktorfit
import io.github.theapache64.retrosheet.core.RetrosheetConfig
import io.github.theapache64.retrosheet.core.RetrosheetConverter
import io.github.theapache64.retrosheet.core.createRetrosheetPlugin
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.engine.android.*
import io.ktor.client.*

suspend fun main() {
    val myApi = createMyApi()

    // Reading sample
    val rows = myApi.getRows()
    println(rows)

    // Adding sample order
    val newRow = myApi.addRow(
        AddRowRequest(
            count = "sample 'Count' input",
        )
    )

    println(newRow)
}


fun createMyApi(
    configBuilder: RetrosheetConfig.Builder.() -> Unit = {}
): MyApi {
    val config = RetrosheetConfig.Builder()
        .apply { this.configBuilder() }
        .setLogging(true)
        // To Read
        .addSheet(
            "FormResponses", // sheet name
            "_Timestamp", "Count"  // columns in same order
        )
        // To write
        .addForm(
            "add_row",
            "https://docs.google.com/forms/d/e/1FAIpQLSdDmUXZOCVj0g-EHsReEel721SPs3WCr21Jcc_MxOZ-Yjc4gw/viewform?usp=dialog"
        )
        .build()

    val ktorClient = HttpClient(Android) {
        install(createRetrosheetPlugin(config)) {}
        install(ContentNegotiation) {
            json()
        }
    }

    val ktorfit = Ktorfit.Builder()
        // GoogleSheet Public URL
        .baseUrl("https://docs.google.com/spreadsheets/d/1f7XZ1Hn-7ZtvBFQHgjCRpnU9CCjsYM62Zxyew26PO9A/")
        .httpClient(ktorClient)
        .converterFactories(RetrosheetConverter(config))
        .build()

    return ktorfit.create<MyApi>()
}