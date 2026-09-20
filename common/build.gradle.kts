import org.springframework.boot.gradle.tasks.bundling.BootJar

// This module is a plain library, not an application. The root `subprojects` block applies the
// Spring Boot plugin to every module, which enables `bootJar` and disables `jar`; that produces a
// fat jar the services cannot depend on. Flip it back.
tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
    // The Spring Boot plugin adds a "plain" classifier to the normal jar; this module has no fat
    // jar to disambiguate from, so drop it.
    archiveClassifier.set("")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
