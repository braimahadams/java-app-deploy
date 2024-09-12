plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("java")
    id("com.google.cloud.tools.jib") version "3.4.3"
    id ("org.sonarqube") version "5.0.0.4638"
}

sonar {
  properties {
    property("sonar.projectKey", "intern8622730_JavaApp-Deployment-Project_e3e72ccc-2860-4249-884a-3d821bd6d671")
    property("sonar.projectName", "JavaApp-Deployment-Project")
    property("sonar.qualitygate.wait", true)
    property("sonar.java.binaries", "build/classes/java/main")
    property("sonar.analysisCache.enabled", "false")
  }
}

version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

tasks.compileJava {
    //options.compilerArgs.add("--enable-preview")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.spring.io/milestone")
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
    
    implementation("io.micrometer:micrometer-registry-prometheus")

    implementation("com.h2database:h2")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks {
    withType<Test> {
        useJUnitPlatform()
        jvmArgs("-Duser.language=en")
    }
}

jib {
    from {
        image = "eclipse-temurin:21.0.1_12-jre"
    }
    to {
        image = "registry.gitlab.com/intern8622730/javaapp-deployment-project:1.0"
    }
    container {
        //jvmFlags = listOf("--enable-previw")
        ports = listOf("8080")
    }
}
