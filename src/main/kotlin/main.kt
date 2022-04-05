import arrow.core.Try
import arrow.core.getOrElse
import ngramSolution.LanguajeModel
import ngramSolution.Ngrams
import processData.PreProcessor
import processData.ProcessorHiddenData
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


suspend  fun main(args: Array<String>) {
    //first variables
    val data = object {}.javaClass.getResource(args[0])!!.readText()// texto de entrenamiento
    val modelFileSaved = args[1]//archivo para guardar y cargar el modelo
    val order = args[2].toInt()
    val processData = object : PreProcessor {}.processData(data)
    val hidenDat = ProcessorHiddenData().processData("192.13.26.255")
    println("$hidenDat \n------------------------")
    val m = Ngrams()

    //load model or training
    val model = loadModel(modelFileSaved).getOrElse {
        val lm = trainModel(m,processData,order)
        saveModel(lm,modelFileSaved)
        lm
    }
    //val tempModel = m.train(data,order)
    //numero de n-grams del orden dado encontrados
    //obtencion de ngramas del orden dado
    val totalNgrma = model.filterKeys {
        val keyTemp = it.drop(1).dropLast(1).split(",")//eliminacion de corchetes y separcion de comas
        keyTemp.size == order
    }.size
    println("Model numbers: ")
    println("$order-ngrams -> $totalNgrma")
    Ngrams().testGenerateText(model,order,hidenDat,"mancha")
}

private suspend fun trainModel(ngramModel: Ngrams, data:String, order:Int) = ngramModel.train(data,order)

private fun saveModel(languajeModel: LanguajeModel, modelFile: String){
    ObjectOutputStream(FileOutputStream(modelFile)).use {
        it.writeObject(languajeModel)
    }
}

@Suppress("UNCHECKED_CAST")
private fun loadModel(file: String): Try<LanguajeModel> {
    return Try{
        ObjectInputStream(FileInputStream(file)).use{
            it.readObject() as LanguajeModel
        }
    }
}
