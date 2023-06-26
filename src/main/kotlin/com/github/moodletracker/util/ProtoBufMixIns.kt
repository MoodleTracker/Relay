package com.github.moodletracker.util

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.github.moodletracker.protocol.CourseOuterClass.Course
import com.github.moodletracker.protocol.EventsUpcoming.UpcomingEvent
import com.github.moodletracker.protocol.TimestampOuterClass.Timestamp
import com.google.protobuf.Descriptors.FieldDescriptor
import io.quarkus.jackson.ObjectMapperCustomizer
import jakarta.inject.Singleton

internal abstract class CourseMixin : ProtoBufIgnoredMethods() {
	@JsonProperty("id")
	var id_: Int = 0

	@JsonProperty("fullname")
	var fullname_: String? = null

	@JsonProperty("shortname")
	var shortname_: String? = null

	@JsonProperty("viewurl")
	var viewurl_: String? = null
}

internal abstract class UpcomingEventMixin : ProtoBufIgnoredMethods() {
	@JsonProperty("id")
	var id_ = 0

	@JsonProperty("userid")
	var userid_ = 0

	@JsonProperty("timestart")
	var timestart_: Timestamp? = null

	@JsonProperty("activityname")
	var activityname_: String? = null

	@JsonProperty("course")
	var course_: Course? = null
}


internal abstract class TimestampMixin : ProtoBufIgnoredMethods() {
	@JsonProperty("seconds")
	var seconds_: String? = null
}

class TimestampSerializer : JsonSerializer<Timestamp>() {
	override fun serialize(value: Timestamp, gen: JsonGenerator, serializers: SerializerProvider) {
		gen.writeNumber(value.seconds)
	}
}

class TimestampDeserializer : JsonDeserializer<Timestamp>() {
	override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Timestamp {
		return Timestamp.newBuilder().setSeconds(p.longValue).build()
	}
}

internal abstract class ProtoBufIgnoredMethods {
	@get:JsonIgnore
	abstract val allFields: Map<FieldDescriptor?, Any?>?
}


@Singleton
class RegisterProtobufMixInsCustomizer : ObjectMapperCustomizer {
	override fun customize(mapper: ObjectMapper) {
		mapper.addMixIn(Course::class.java, CourseMixin::class.java)
		mapper.addMixIn(UpcomingEvent::class.java, UpcomingEventMixin::class.java)
		mapper.addMixIn(Timestamp::class.java, TimestampMixin::class.java)

		val module = SimpleModule()
		module.addSerializer(Timestamp::class.java, TimestampSerializer())
		module.addDeserializer(Timestamp::class.java, TimestampDeserializer())
		mapper.registerModule(module)
	}
}
