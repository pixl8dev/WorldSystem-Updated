plugins {
    id("com.gradleup.shadow") version "8.3.6"
    id("io.freefair.lombok") version "8.13"
    id("java")
    id("jacoco")
    id("base")
}

base { 
    archivesName = pluginname 
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val pluginname = "WorldSystem"
val authors = "[Butzlabben, Trainerlord, Cycodly]"
val version = "2.4.40-dev"
val description = "WorldSystem plugin to create per player worlds"
val apiversion = "1.16"
val minecraft = "1.21.4"
val depend = "[WorldEdit]"
val softdepend = "[PlaceholderAPI, Vault, Chunky]"
/*java { sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}*/

repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo.extendedclip.com/releases/")
        maven("https://maven.enginehub.org/repo/")
        maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    annotationProcessor(libs.lombok)
    implementation(libs.commonsio)
    implementation(libs.minimessage)
    compileOnly(libs.spigotapi)
    compileOnly(libs.lombok)
    compileOnly(libs.placeholderapi)
    compileOnly(libs.vaultapi)
    compileOnly(libs.authlib)
    compileOnly(libs.worldedit)
    compileOnly(libs.fawe)
    compileOnly(libs.chunky)
    testImplementation(libs.jupiter)
    testImplementation(libs.mockito)
    testImplementation(libs.assertj)
    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

configurations.all {
    resolutionStrategy {
        force(libs.gson)
        force(libs.guava)
    }
}

task processResources {
    expand(project.properties)
    from(sourceSets["main"].resources.srcDirs) {
        include("plugin.yml")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
    filesMatching("plugin.yml") {
            expand(
                "pluginname" to pluginname,
                "group" to group,
                "version" to version,
                "authors" to authors,
                "description" to description,
                "apiversion" to apiversion,
                "depend" to depend,
                "softdepend" to softdepend
            )
        }
    }
}

tasks {
               
    shadowJar {
        minimize()
        archiveClassifier.set("")
        archiveFileName.set("${pluginname}-${version}.jar")
        // dependencies {
        //     exclude(dependency("commons-io:commons-io"))
        //     exclude(dependency("net.kyori:adventure-text-minimessage"))
        // }
    }
    javadoc {
        options {
            links("https://javadoc.io/static/org.jetbrains/annotations/20.1.0/")
            links("https://docs.oracle.com/javase/21/docs/api/")
            links("https://papermc.io/javadocs/paper/$minecraft/")
        }
        source = sourceSets["main"].allJava
        include("**/api/*")
        destinationDir = file("build/javadocs")
    }
    withType<JavaCompile> {
        options.isDeprecation = false
        options.encoding = "UTF-8"
        //options.compilerArgs += "-parameters"
        options.isFork = true
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        ignoreFailures = false
    }
    

}

build {
        dependsOn(task.shadowJar)
    }

defaultTasks("build")