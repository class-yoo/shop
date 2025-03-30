package com.shoptest.common.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.text.DecimalFormat

class PriceToStringSerializer : JsonSerializer<Int>() {
    private val formatter = DecimalFormat("#,###")

    override fun serialize(value: Int, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeString(formatter.format(value))
    }
}
