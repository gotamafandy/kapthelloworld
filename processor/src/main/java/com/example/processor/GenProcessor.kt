package com.example.processor

import com.example.annotation.GenName
import com.example.annotation.IntentParam
import com.example.annotation.NewIntent
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.Element

@AutoService(Processor::class)
@SupportedOptions(GenProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
@Suppress("UNUSED")
class GenProcessor: AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(NewIntent::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(set: MutableSet<out TypeElement>?,
                         roundEnv: RoundEnvironment?): Boolean {

        var pack = ""

        val activities = hashMapOf<String, Data>()

        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "Gen Processor, Round Env: " + roundEnv?.rootElements)

        roundEnv?.getElementsAnnotatedWith(NewIntent::class.java)?.forEach {

            val intentParams = hashMapOf<String, Element>()

            it.enclosedElements.forEach { element ->
                val paramAnnotation = element.getAnnotation(IntentParam::class.java)

                paramAnnotation?.let { intentParam ->
                    intentParams[intentParam.value] = element
                }
            }

            val className = it.simpleName.toString()

            pack = processingEnv.elementUtils.getPackageOf(it).toString()

            activities[className] = Data(pack, intentParams)
        }

        if (activities.isNotEmpty()) {
            generatingCode(pack, activities)
        }

        return false
    }

    private fun generatingCode(pack: String, activities: HashMap<String, Data>) {
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()

        if(kaptKotlinGeneratedDir.isEmpty()) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")

            return
        }

        // Kotlin Poet in action
        val companionBuilder = TypeSpec.companionObjectBuilder()

        activities.forEach { className, data ->

            val goToIntentBuilder = FunSpec.builder("start$className")
                .addParameter("context", ClassName("android.content", "Context"))

            goToIntentBuilder
                .addStatement("val intent = %T(context, %T::class.java)",
                    ClassName("android.content", "Intent"),
                    ClassName(pack, className))

            data.params.forEach { key, value ->
                goToIntentBuilder
                    .addParameter(value.simpleName.toString(), value.javaToKotlinType())

                goToIntentBuilder
                    .addStatement("intent.putExtra(%S, %L)", key, value)
            }

            val goToIntentFunction = goToIntentBuilder
                .addStatement("context.startActivity(intent)")
                .build()

            companionBuilder.addFunction(goToIntentFunction)
        }

        val file = FileSpec.builder(pack, "Navigator")
            .addType(
                TypeSpec.classBuilder("Navigator")
                    .addType(
                        companionBuilder.build()
                    ).build()
            ).build()

        file.writeTo(File(kaptKotlinGeneratedDir))
    }

    private fun generateClass(className: String, pack: String) {
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()

        if(kaptKotlinGeneratedDir.isEmpty()) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")

            return
        }

        val fileName = "Generated_$className"
        val file = FileSpec.builder(pack, fileName)
            .addType(
                TypeSpec.classBuilder(fileName)
                    .addFunction(
                        FunSpec.builder("getName")
                            .addStatement("return \"World\"")
                            .build())
                    .build())
            .build()


        file.writeTo(File(kaptKotlinGeneratedDir))
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    data class Data(val pack: String, val params: HashMap<String, Element>)


}
