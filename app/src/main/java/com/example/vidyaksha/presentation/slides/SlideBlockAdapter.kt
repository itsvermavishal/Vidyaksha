package com.example.vidyaksha.presentation.slides

import com.example.vidyaksha.data.local.SlideBlock
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import java.lang.reflect.Type

class SlideBlockAdapter : JsonDeserializer<SlideBlock> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): SlideBlock {

        val obj = json.asJsonObject

        return when (obj["type"].asString.trim().uppercase()) {

            "TEXT" -> SlideBlock.Text(
                obj["text"].asString
            )

            "MARKDOWN" -> SlideBlock.Markdown(
                obj["markdown"].asString
            )

            "IMAGE" -> SlideBlock.Image(
                obj["images"].asJsonArray.map { it.asString }
            )

            "TABLE" -> SlideBlock.Table(
                headers = obj["headers"].asJsonArray.map { it.asString },
                rows = obj["rows"].asJsonArray.map { row ->
                    row.asJsonArray.map { it.asString }
                }
            )

            else -> throw IllegalArgumentException(
                "Unknown block type: ${obj["type"].asString}"
            )
        }
    }
}
