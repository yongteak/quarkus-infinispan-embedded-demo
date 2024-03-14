import io.quarkus.gradle.tasks.QuarkusBuild

plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val infinispanVersion: String by project
val protostreamVersion: String by project;

dependencies {
    implementation("org.apache.camel.quarkus:camel-quarkus-timer")
    // implementation("org.apache.camel.quarkus:camel-quarkus-web3j")
    implementation("io.quarkus:quarkus-jackson")
    implementation("org.apache.camel.quarkus:camel-quarkus-file")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("org.apache.camel.quarkus:camel-quarkus-netty-http")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("org.apache.camel.quarkus:camel-quarkus-microprofile-health")
    implementation("org.apache.camel.quarkus:camel-quarkus-jackson")
    implementation("org.apache.camel.quarkus:camel-quarkus-rest")
    implementation("org.apache.camel.quarkus:camel-quarkus-platform-http")
    implementation("org.apache.camel.quarkus:camel-quarkus-direct")
    implementation("org.apache.camel.quarkus:camel-quarkus-log")
    implementation("org.apache.camel.quarkus:camel-quarkus-sql")
    implementation("org.apache.camel.quarkus:camel-quarkus-core")
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:quarkus-camel-bom:${quarkusPlatformVersion}"))
    implementation("io.quarkiverse.jdbc:quarkus-jdbc-sqlite:3.0.6")
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    // implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-container-image-jib")
    implementation("io.quarkus:quarkus-arc")
    // implementation("io.quarkus:quarkus-resteasy-reactive")

    implementation("org.infinispan:infinispan-quarkus-embedded:${infinispanVersion}")
    implementation("org.infinispan.protostream:protostream:${protostreamVersion}")
    annotationProcessor("org.infinispan.protostream:protostream-processor:${protostreamVersion}")
    // for the compile time dependency on org.infinispan.protostream.annotations.impl.processor.OriginatingClasses
    compileOnly("org.infinispan.protostream:protostream-processor:${protostreamVersion}")
    // incompatible: implementation("org.infinispan:infinispan-cdi-embedded:${infinispanVersion}")
    implementation("org.jboss.slf4j:slf4j-jboss-logmanager")

    // implementation("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0") // Kotlin DSL
    

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.testcontainers:testcontainers:1.19.0")
}

group = "io.github.renegrob"
version = "1.0.0-SNAPSHOT"

//project.configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
//        .withDependencies {
//            val resolvedArtifacts = project.configurations.getByName(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME).copy().resolvedConfiguration.resolvedArtifacts;
//
//            for (artifact in resolvedArtifacts) {
//                val id = artifact.moduleVersion.id;
//                if ("org.infinispan.protostream".equals(id.getGroup()) && "protostream".equals(id.getName()) && !id.getVersion().isEmpty()) {
//                    add(project.dependencies.create("org.infinispan.protostream:protostream-processor" + ':' + id.getVersion()));
//                }
//            }
//        }

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
