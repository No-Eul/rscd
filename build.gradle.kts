plugins {
	id("java")
	id("net.fabricmc.fabric-loom") version "1.15.5"
	id("maven-publish")
}

group = "dev.noeul.fabricmod"
version = property("mod_version")!!

repositories {
	mavenCentral()
	maven("https://maven.terraformersmc.com/releases")
	maven("https://api.modrinth.com/maven") { name = "Modrinth" }
	maven("https://jitpack.io")
}


dependencies {
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	implementation("net.fabricmc:fabric-loader:${property("loader_version")}")

//	modImplementation("com.terraformersmc:modmenu:${property("mod_menu_version")}")

	runtimeOnly("maven.modrinth:mixintrace:1.1.1+1.17")
//	modRuntimeOnly("maven.modrinth:notenoughcrashes:4.4.5+1.20.1-fabric")
//	modRuntimeOnly("maven.modrinth:language-reload:1.5.8+1.20.1")
//	modRuntimeOnly("maven.modrinth:smoothboot-fabric:1.19.4-1.7.0")
//	modRuntimeOnly("maven.modrinth:immediatelyfast:1.1.20+1.20.1")
//	modRuntimeOnly("com.github.LlamaLad7:MixinExtras:0.2.0-beta.9")
//	modRuntimeOnly("maven.modrinth:ferrite-core:6.0.0-fabric")
//	modRuntimeOnly("maven.modrinth:lithium:mc1.20.1-0.11.2")
}

loom {
	sourceSets.forEach {
		it.resources.files
			.find { file -> file.name.endsWith(".accesswidener") }
			?.let(accessWidenerPath::set)
	}

	runs {
		getByName("client") {
			configName = "Minecraft Client"
			runDir = "run/client"
			client()
		}

		getByName("server") {
			configName = "Minecraft Server"
			runDir = "run/server"
			server()
		}
	}
}

val targetJavaVersion: JavaVersion = JavaVersion.toVersion(property("target_java_version")!!)
java {
	targetCompatibility = targetJavaVersion
	sourceCompatibility = targetJavaVersion
	if (JavaVersion.current() < targetJavaVersion)
		toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion.majorVersion.toInt()))
}

tasks {
	compileJava {
		if (targetJavaVersion.majorVersion.toInt() >= 10 || JavaVersion.current().isJava10Compatible)
			options.release.set(targetJavaVersion.majorVersion.toInt())
		this.options.encoding = "UTF-8"
	}

	processResources {
		val inputs = mapOf(
			"mod_id" to project.property("mod_id"),
			"version" to project.version,
			"name" to project.property("mod_name"),
			"minecraft_version" to project.property("minecraft_version"),
			"loader_version" to project.property("loader_version")
		)
		this.inputs.properties(inputs)
		filesMatching("fabric.mod.json") {
			expand(inputs)
		}
	}

	jar {
		from("LICENSE.txt")
		archiveBaseName.set("${project.property("archive_base_name")}")
		archiveAppendix.set("fabric")
		archiveVersion.set("${project.version}+mc${project.property("minecraft_version")}")
	}
}

// configure the maven publication
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}
