object Constants {
	const val MINECRAFT_VERSION = "1.20.6"
	const val YARN_VERSION = "1.20.6+build.3"
	const val LOADER_VERSION = "0.15.11"

	const val DEVAUTH_VERSION = "1.2.0"
}

plugins {
	id("fabric-loom") version "1.6-SNAPSHOT"
}

group = "dev.caoimhe.thirdpersonboating"
version = "1.0.0"
base.archivesName = "thirdpersonboating"

loom {
	runs {
		remove(getByName("server"))
	}
}

repositories {
	maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

dependencies {
	minecraft("com.mojang:minecraft:${Constants.MINECRAFT_VERSION}")
	mappings("net.fabricmc:yarn:${Constants.YARN_VERSION}:v2")
	modImplementation("net.fabricmc:fabric-loader:${Constants.LOADER_VERSION}")

	modRuntimeOnly("me.djtheredstoner:DevAuth-fabric:${Constants.DEVAUTH_VERSION}")
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

tasks {
	jar {
		from("LICENSE") {
			rename { "${it}_${project.base.archivesName}" }
		}
	}

	processResources {
		inputs.property("version", project.version)

		filesMatching("fabric.mod.json") {
			expand(mapOf("version" to project.version))
		}
	}
}