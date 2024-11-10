
plugins {
    // Java support
    id("java")
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "0.7.3"
}

// Import variables from gradle.properties file
val pluginGroup: String by project
val pluginName_: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project
val pluginVerifierIdeVersions: String by project
val platformType: String by project
val platformVersion: String by project
val platformPlugins: String by project
val platformDownloadSources: String by project
val sourceVersion: String by project
val targetVersion: String by project

group = pluginGroup

// Configure project's dependencies
repositories {
    mavenCentral()
//    jcenter()
}
dependencies {

    // https://mvnrepository.com/artifact/org.ow2.asm/asm
    implementation("org.ow2.asm:asm:9.7")

    // https://mvnrepository.com/artifact/org.ow2.asm/asm-commons
    implementation("org.ow2.asm:asm-commons:9.7")

    // https://mvnrepository.com/artifact/org.ow2.asm/asm-util
    implementation("org.ow2.asm:asm-util:9.7")

    // https://mvnrepository.com/artifact/org.ow2.asm/asm-tree
    implementation("org.ow2.asm:asm-tree:9.7")

    // https://mvnrepository.com/artifact/org.apache.bcel/bcel
    implementation("org.apache.bcel:bcel:6.10.0")

}

// Include the generated files in the source set
sourceSets {
    main {
        java {
            srcDirs("src/main/gen")
        }
    }
}


intellij {
    pluginName = pluginName_
    version = platformVersion
    type = platformType
    downloadSources = platformDownloadSources.toBoolean()
    updateSinceUntilBuild = false

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    setPlugins(*platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty).toTypedArray())
}


tasks {
    withType<JavaCompile> {
        sourceCompatibility = sourceVersion
        targetCompatibility = targetVersion
        options.encoding = "UTF-8"
    }

    patchPluginXml {
        sinceBuild(pluginSinceBuild)
        untilBuild(pluginUntilBuild)
    }

    buildSearchableOptions{
        enabled = false
    }

    publishPlugin {
        token(System.getenv("PUBLISH_TOKEN"))
    }
}
