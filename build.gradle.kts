plugins {
    id("java")
    application
}

group = "com.unvise"
version = "1.0-SNAPSHOT"

val mainClazz = "com.unvise.oop.ORMApplication"

repositories {
    mavenCentral()
}

java {
    toolchain{
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set(mainClazz)
}

dependencies {
    // logger
    implementation("org.slf4j:slf4j-api:2.0.4")
    implementation("org.slf4j:slf4j-simple:2.0.4")
    // h2 database
    implementation("com.h2database:h2:2.1.214")
    // hikari
    implementation("com.zaxxer:HikariCP:5.0.1")
    // cglib for proxy
    runtimeOnly("cglib:cglib:3.3.0")
    compileOnly("cglib:cglib:3.3.0")
    implementation("org.ow2.asm:asm:9.4")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.javassist:javassist:3.29.2-GA")
    // lombok
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    // lombok for tests
    testImplementation("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    // tests
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events.add(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED)
        events.add(org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED)
        events.add(org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED)
        events.add(org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT)
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = mainClazz
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    val sourcesMain = sourceSets.main.get()
    from(sourcesMain.output)
}