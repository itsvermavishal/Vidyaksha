package com.example.vidyaksha.data.local

import com.google.gson.JsonDeserializer
import java.lang.reflect.Type

class LevelTypeAdapter : JsonDeserializer<LevelType> {
    override fun deserialize(
        json: com.google.gson.JsonElement,
        typeOfT: Type,
        context: com.google.gson.JsonDeserializationContext
    ): LevelType {
        return LevelType.valueOf(json.asString.uppercase())
    }
}