import huffmanTree.HuffmanSolution
import processData.ProcessorHiddenData

fun main(){
    val temp = HashMap<String,Float>()
    temp["casa"]= 0.017391304F
    temp["perro"]= 0.14782609F
    temp["vida"]= 0.008695652F
    temp["mancha"]= 0.026086956F
    temp["muerte"]= 0.20869565F
    val hidenData = ProcessorHiddenData().processData("192.13.26.255")
    HuffmanSolution().encode(temp,hidenData)
    //.1+.1+.1= .3 == .3
}
