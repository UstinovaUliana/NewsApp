package com.ustinovauliana.newsapi.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object DateTimeUTCSerializer : KSerializer<Date> {

    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): Date =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            .parse(decoder.decodeString())
}