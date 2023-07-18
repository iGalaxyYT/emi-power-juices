package dev.igalaxy.emipowerjuices

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object EmiPowerJuices : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("EMI Power Juices")

    override fun onInitialize(mod: ModContainer) {
        LOGGER.info("Hello Quilt world from {}!", mod.metadata()?.name())
    }
}
