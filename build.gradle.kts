plugins {
    id("org.jetbrains.intellij.platform") version "2.18.1"
}

group = "io.github.enkia"
// 版本号唯一来源：gradle.properties 的 version 属性。
// CI 发版时用 -Pversion=x.y.z 覆盖（命令行 -P 优先级高于 gradle.properties，见 .github/workflows/release.yml）。
// 用 get() 而非 orNull 兜底：缺失时直接报错，避免静默退回旧版本号而被 Marketplace 拒绝上传。
version = providers.gradleProperty("version").get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// 资源目录位于项目根 resources/（非标准 src/main/resources），显式声明
sourceSets {
    main {
        resources.srcDir("resources")
    }
}



dependencies {
    intellijPlatform {
        rider("2026.2")
    }
}

intellijPlatform {
    pluginConfiguration {
        name = "Tokyo Night YuKi"
        ideaVersion {
            sinceBuild = "251"
        }
    }
    // 纯资源主题插件无 searchable options / 无 Java 代码 / 无需字节码插桩
    buildSearchableOptions = false
    instrumentCode = false
}

tasks {
    withType<Zip>().configureEach {
        // 保持 zip 内路径稳定（META-INF/ 在根）
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
}
