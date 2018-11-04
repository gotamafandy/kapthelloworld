package com.example.processor

import com.example.annotation.GenName
import com.example.annotation.NewIntent
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedOptions(GenProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
@Suppress("UNUSED")
class Gen2Processor: AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(GenName::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(
        set: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "Gen2Processor, Round Env: " + roundEnv?.rootElements)

        return false
    }
}