import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.5.7" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.github.spotbugs") version "6.4.8" apply false
    id("com.diffplug.spotless") version "7.0.2" apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "checkstyle")
    apply(plugin = "pmd")
    apply(plugin = "com.github.spotbugs")
    apply(plugin = "jacoco")
    apply(plugin = "com.diffplug.spotless")

    group = "com.fastbank"
    version = "0.0.1-SNAPSHOT"

    // Override Spring BOM's commons-lang3 downgrade (required by SpotBugs/BCEL)
    extra["commons-lang3.version"] = "3.+"

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    dependencies {
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
        "testRuntimeOnly"("com.h2database:h2")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<BootJar>().configureEach {
        archiveFileName.set("${project.name}.jar")
    }

    // Checkstyle Configuration
    configure<CheckstyleExtension> {
        toolVersion = "13.+"
        configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
        isIgnoreFailures = false
        maxWarnings = 0
    }

    // PMD Configuration
    configure<PmdExtension> {
        toolVersion = "7.+"
        isConsoleOutput = true
        ruleSetFiles = files("${rootDir}/config/pmd/ruleset.xml")
        ruleSets = listOf()
        isIgnoreFailures = false
    }

    // SpotBugs Configuration
    configure<com.github.spotbugs.snom.SpotBugsExtension> {
        toolVersion.set("4.9.8")
        effort.set(Effort.MAX)
        reportLevel.set(Confidence.LOW)
        ignoreFailures.set(false)
    }

    tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
        reports {
            create("html") {
                required.set(true)
                outputLocation.set(file("${layout.buildDirectory.get()}/reports/spotbugs/${name}.html"))
            }
            create("xml") {
                required.set(false)
            }
        }
    }

    // JaCoCo Configuration
    configure<JacocoPluginExtension> {
        toolVersion = "0.8.14"
    }

    tasks.named<Test>("test") {
        finalizedBy(tasks.named("jacocoTestReport"))
    }

    val jacocoExclusions = listOf(
        "**/*Application.class",
        "**/*Mapper.class"
    )

    tasks.named<JacocoReport>("jacocoTestReport") {
        dependsOn(tasks.named("test"))
        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) { exclude(jacocoExclusions) }
            })
        )
        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(false)
        }
    }

    tasks.register<JacocoCoverageVerification>("jacocoVerify") {
        dependsOn(tasks.named("jacocoTestReport"))
        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) { exclude(jacocoExclusions) }
            })
        )
        violationRules {
            rule {
                limit {
                    minimum = "0.80".toBigDecimal()
                }
            }
            rule {
                element = "CLASS"
                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = "0.75".toBigDecimal()
                }
            }
        }
    }

    tasks.named("check") {
        dependsOn(tasks.named("jacocoVerify"))
    }

    // Spotless Configuration
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            googleJavaFormat().aosp()
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }
    }

    // Custom task aliases
    tasks.register("testWithCoverage") {
        group = "verification"
        description = "Runs tests and generates coverage report"
        dependsOn(tasks.named("test"), tasks.named("jacocoTestReport"))
        doLast {
            val xmlReport = file("${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml")
            if (xmlReport.exists()) {
                val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance().apply {
                    setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
                    setFeature("http://xml.org/sax/features/validation", false)
                }
                val doc = factory.newDocumentBuilder().parse(xmlReport)

                val reset  = "\u001B[0m"
                val bold   = "\u001B[1m"
                val cyan   = "\u001B[36m"
                val green  = "\u001B[32m"
                val yellow = "\u001B[33m"
                val red    = "\u001B[31m"
                fun pctColor(pct: Double) = when {
                    pct >= 80.0 -> green
                    pct >= 60.0 -> yellow
                    else        -> red
                }

                println("\n${bold}${cyan}Class Coverage (Lines):$reset")
                println(cyan + "-".repeat(60) + reset)
                println("${bold}%-40s %8s %8s %8s$reset".format("Class", "Covered", "Total", "Ratio"))
                println(cyan + "-".repeat(60) + reset)
                var totalCovered = 0; var totalLines = 0
                val classes = doc.getElementsByTagName("class")
                for (i in 0 until classes.length) {
                    val cls = classes.item(i)
                    val className = cls.attributes.getNamedItem("name").nodeValue.substringAfterLast("/")
                    val sourcefileName = cls.attributes.getNamedItem("sourcefilename")?.nodeValue

                    val missedLines = mutableListOf<Int>()
                    if (sourcefileName != null) {
                        val siblings = cls.parentNode.childNodes
                        for (j in 0 until siblings.length) {
                            val sibling = siblings.item(j)
                            if (sibling.nodeName == "sourcefile" &&
                                sibling.attributes.getNamedItem("name")?.nodeValue == sourcefileName) {
                                val lines = sibling.childNodes
                                for (k in 0 until lines.length) {
                                    val line = lines.item(k)
                                    if (line.nodeName == "line") {
                                        val mi = line.attributes.getNamedItem("mi")?.nodeValue?.toInt() ?: 0
                                        val mb = line.attributes.getNamedItem("mb")?.nodeValue?.toInt() ?: 0
                                        if (mi > 0 || mb > 0) {
                                            missedLines.add(line.attributes.getNamedItem("nr").nodeValue.toInt())
                                        }
                                    }
                                }
                                break
                            }
                        }
                    }

                    val children = cls.childNodes
                    for (j in 0 until children.length) {
                        val child = children.item(j)
                        if (child.nodeName == "counter" &&
                            child.attributes.getNamedItem("type").nodeValue == "LINE") {
                            val missed = child.attributes.getNamedItem("missed").nodeValue.toInt()
                            val covered = child.attributes.getNamedItem("covered").nodeValue.toInt()
                            val total = missed + covered
                            val pct = if (total > 0) covered * 100.0 / total else 0.0
                            val ranges = mutableListOf<String>()
                            var rangeStart = -1; var rangeEnd = -1
                            for (line in missedLines.sorted()) {
                                if (rangeStart == -1) { rangeStart = line; rangeEnd = line }
                                else if (line == rangeEnd + 1) rangeEnd = line
                                else { ranges.add(if (rangeStart == rangeEnd) "$rangeStart" else "$rangeStart-$rangeEnd"); rangeStart = line; rangeEnd = line }
                            }
                            if (rangeStart != -1) ranges.add(if (rangeStart == rangeEnd) "$rangeStart" else "$rangeStart-$rangeEnd")
                            val missedStr = if (ranges.isNotEmpty()) "  ${red}(missed lines: ${ranges.joinToString(", ")})$reset" else ""
                            totalCovered += covered; totalLines += total
                            val color = pctColor(pct)
                            println("%-40s %8d %8d $color%7.1f%%%s$reset".format(className, covered, total, pct, missedStr))
                        }
                    }
                }
                println(cyan + "-".repeat(60) + reset)
                val totalPct = if (totalLines > 0) totalCovered * 100.0 / totalLines else 0.0
                println("${bold}%-40s %8d %8d ${pctColor(totalPct)}%7.1f%%%s".format("TOTAL", totalCovered, totalLines, totalPct, reset))

                println("\n${bold}${cyan}Overall Summary:$reset")
                println(cyan + "-".repeat(45) + reset)
                println("${bold}%-15s %10s %10s %8s$reset".format("Type", "Covered", "Total", "Ratio"))
                println(cyan + "-".repeat(45) + reset)
                val counters = doc.getElementsByTagName("counter")
                for (i in 0 until counters.length) {
                    val counter = counters.item(i)
                    if (counter.parentNode.nodeName == "report") {
                        val type = counter.attributes.getNamedItem("type").nodeValue
                        val missed = counter.attributes.getNamedItem("missed").nodeValue.toInt()
                        val covered = counter.attributes.getNamedItem("covered").nodeValue.toInt()
                        val total = missed + covered
                        val pct = if (total > 0) covered * 100.0 / total else 0.0
                        val color = pctColor(pct)
                        println("%-15s %10d %10d $color%7.1f%%%s".format(type, covered, total, pct, reset))
                    }
                }
                println(cyan + "-".repeat(45) + reset)
            }
            println("\nFull report: ${layout.buildDirectory.get()}/reports/jacoco/test/html/index.html")
        }
    }

    tasks.register("qualityCheck") {
        group = "verification"
        description = "Runs all quality checks (Checkstyle, PMD, SpotBugs)"
        dependsOn(
            tasks.named("checkstyleMain"), tasks.named("checkstyleTest"),
            tasks.named("pmdMain"), tasks.named("pmdTest"),
            tasks.named("spotbugsMain"), tasks.named("spotbugsTest")
        )
        doLast {
            println("\nAll quality checks passed!")
        }
    }

    tasks.register("fullBuild") {
        group = "build"
        description = "Clean, build, test with coverage, and run all quality checks"
        dependsOn(tasks.named("clean"), tasks.named("build"))
        doLast {
            println("\n Full build completed successfully!")
            println(" Artifact: ${layout.buildDirectory.get()}/libs/${project.name}.jar")
            println(" Coverage: ${layout.buildDirectory.get()}/reports/jacoco/test/html/index.html")
            println(" Quality Reports:")
            println("   - Checkstyle: ${layout.buildDirectory.get()}/reports/checkstyle/")
            println("   - PMD: ${layout.buildDirectory.get()}/reports/pmd/")
            println("   - SpotBugs: ${layout.buildDirectory.get()}/reports/spotbugs/")
        }
    }

    tasks.register("reports") {
        group = "reporting"
        description = "Shows all generated report locations"
        doLast {
            val reports = listOf(
                "${layout.buildDirectory.get()}/reports/jacoco/test/html/index.html",
                "${layout.buildDirectory.get()}/reports/tests/test/index.html"
            )
            println("\n Generated reports:")
            reports.forEach { println("   - $it") }
        }
    }
}
