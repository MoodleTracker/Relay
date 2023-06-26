import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.quarkus")
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.8.21"
}

repositories {
    mavenCentral()
    // this is less than ideal and will be replaced by a proper release soon™
    maven {
        url = uri("https://maven.pkg.github.com/MoodleTracker/Protocol")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
    maven {
        url = uri("https://packages.confluent.io/maven/")
        content {
            includeGroup("io.confluent")
        }
    }
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.21")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-hibernate-reactive-panache-kotlin")
    implementation("io.quarkus:quarkus-reactive-pg-client")
    implementation("io.quarkus:quarkus-rest-client-reactive")
    implementation("io.quarkus:quarkus-rest-client-reactive-jackson")
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka")
    implementation("io.quarkus:quarkus-apicurio-registry-avro")
    implementation("io.quarkus:quarkus-scheduler")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-smallrye-jwt")
    implementation("io.quarkus:quarkus-smallrye-jwt-build")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")

    implementation("com.github.moodletracker:protocol:1.0.0")
    implementation("io.confluent:kafka-protobuf-serializer:7.4.0")
    implementation("com.google.protobuf:protobuf-java:3.23.0")
}

group = "com.github.moodletracker"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.majorVersion
    kotlinOptions.javaParameters = true
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
