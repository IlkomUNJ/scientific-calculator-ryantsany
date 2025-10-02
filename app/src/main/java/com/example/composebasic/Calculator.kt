package com.example.composebasic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composebasic.ui.theme.Background
import com.example.composebasic.ui.theme.BlueEqualButton
import com.example.composebasic.ui.theme.Chartreuse
import com.example.composebasic.ui.theme.DarkBlueButton
import com.example.composebasic.ui.theme.DarkGreen
import com.example.composebasic.ui.theme.DarkGreyButton
import com.example.composebasic.ui.theme.LightGrey
import com.example.composebasic.ui.theme.Navy
import com.example.composebasic.ui.theme.Red
import com.example.composebasic.ui.theme.onDarkBlueButton
import com.example.composebasic.ui.theme.onDarkGreyButton
import kotlin.math.*

@Composable
fun Calculator()
{
    val modifier : Modifier = Modifier

    var inputs by remember {mutableStateOf("0")}
    var shouldShowScientific by remember {mutableStateOf(false)}
    var shouldShowInverse by remember {mutableStateOf(false)}
    var resetDisplay by remember {mutableStateOf(false)}

    fun addNumber(input: String)
    {
        if(resetDisplay){
            resetDisplay = false
            inputs = input
        } else if(inputs == "0"){
            inputs = input
        } else {
            inputs += input
        }
    }

    fun addOperator(operator: String)
    {
        if(inputs.isNotEmpty() && !(inputs.last() == '+' || inputs.last() == '-' || inputs.last() == '÷' || inputs.last() == 'x')){
            inputs += operator
        }
    }

    fun basicMath(expr: String): Double
    {
        val terms = mutableListOf<String>()
        val operators = mutableListOf<String>()
        var currentTerm = ""

        for (i in expr.indices) {
            val char = expr[i]
            if (char in listOf('+', '-') && i > 0) {
                terms.add(currentTerm)
                operators.add(char.toString())
                currentTerm = ""
            } else {
                currentTerm += char
            }
        }
        terms.add(currentTerm)

        val evaluatedTerms = terms.map { term ->
            var result = 0.0
            var currentNum = ""
            var operation = ""

            for (char in term) {
                if (char in listOf('*', '/')) {
                    if (currentNum.isNotEmpty()) {
                        if (operation.isEmpty()) {
                            result = currentNum.toDouble()
                        } else {
                            when (operation) {
                                "*" -> result = result * currentNum.toDouble()
                                "/" -> result = result / currentNum.toDouble()
                            }
                        }
                    }
                    operation = char.toString()
                    currentNum = ""
                } else {
                    currentNum += char
                }
            }

            if (currentNum.isNotEmpty()) {
                if (operation.isEmpty()) {
                    result = currentNum.toDouble()
                } else {
                    when (operation) {
                        "*" -> result = result * currentNum.toDouble()
                        "/" -> result = result / currentNum.toDouble()
                    }
                }
            }
            result
        }

        var finalResult = evaluatedTerms[0]
        for (i in operators.indices) {
            when (operators[i]) {
                "+" -> finalResult += evaluatedTerms[i + 1]
                "-" -> finalResult -= evaluatedTerms[i + 1]
            }
        }

        return finalResult
    }


    fun calculateFactorial(n: Int) : Double
    {
        if (n > 1){
            return n * calculateFactorial(n - 1)
        }
        return 1.0
    }


    fun evaluate(expr: String): Double {
        val expression = expr.replace("x", "*").replace("÷", "/").replace("−", "-")

        when {
            expression.startsWith("√") -> {
                val num = expression.drop(1).toDouble()
                return sqrt(num)
            }
            expression.endsWith("!") -> {
                val num = expression.dropLast(1).toInt()
                return calculateFactorial(num)
            }
            expression.contains("^") -> {
                val parts = expression.split("^")
                if (parts.size == 2) {
                    val base = evaluate(parts[0])
                    val exp = evaluate(parts[1])
                    return base.pow(exp)
                }
            }
            expression.startsWith("1/") -> {
                val num = expression.drop(2).toDouble()
                return 1 / num
            }
            expression.startsWith("eksp(") -> {
                val num = expression.removePrefix("eksp(").toDouble()
                return exp(num)
            }
            expression.startsWith("log(") -> {
                val num = expression.removePrefix("log(").toDouble()
                return log10(num)
            }
            expression.startsWith("ln(") -> {
                val num = expression.removePrefix("ln(").toDouble()
                return ln(num)
            }
            expression.startsWith("sin(") -> {
                val num = expression.removePrefix("sin(").toDouble()
                return sin(Math.toRadians(num))
            }
            expression.startsWith("cos(") -> {
                val num = expression.removePrefix("cos(").toDouble()
                return cos(Math.toRadians(num))
            }
            expression.startsWith("tan(") -> {
                val num = expression.removePrefix("tan(").toDouble()
                return tan(Math.toRadians(num))
            }
            expression.startsWith("asin(") -> {
                val num = expression.removePrefix("asin(").toDouble()
                return Math.toDegrees(asin(num))
            }
            expression.startsWith("acos(") -> {
                val num = expression.removePrefix("acos(").toDouble()
                return Math.toDegrees(acos(num))
            }
            expression.startsWith("atan(") -> {
                val num = expression.removePrefix("atan(").toDouble()
                return Math.toDegrees(atan(num))
            }
        }

        return basicMath(expression)
    }


    fun calculate()
    {
        try {
            val result = evaluate(inputs)

            inputs = if (result.isNaN() || result.isInfinite()) {
                "Error"
            } else if (result % 1.0 == 0.0) {
                result.toLong().toString()
            } else {
                result.toString()
            }
            resetDisplay = true
        } catch (e: Exception) {
            inputs = "Error"
        }

    }

    fun calculatePercent() {
        val current = inputs.toDoubleOrNull() ?: return
        val result = current / 100
        inputs = if (result % 1.0 == 0.0) {
            result.toLong().toString()
        } else {
            result.toString()
        }
    }



    fun clear()
    {
        inputs = "0"
    }

    fun backspace()
    {
        if (resetDisplay){
            inputs = "0"
        }else if (inputs.length > 1){
            inputs = inputs.dropLast(1)
        } else if (inputs.length == 1 && inputs != "0") {
            inputs = "0"
        }
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(color = DarkGreyButton),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box (
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            contentAlignment = Alignment.BottomEnd
        ){
            Text(
                text = inputs,
                fontSize = 50.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = onDarkBlueButton
            )
        }

        Column (
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = Navy,
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
                .padding(bottom = 15.dp)

            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            if(shouldShowScientific){
                Row (
                    modifier = modifier
                        .padding(top = 10.dp)
                ) {
                    CalcButton(modifier = modifier.weight(1f), num = "⌄", click = { shouldShowScientific = !shouldShowScientific }, fontSize = 30.sp, contentColor = onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "x!", click = { addNumber("!") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "xʸ", click = { addNumber("^") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "√x", click = { addNumber("√") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)

                }

                Row (
                    modifier = modifier
                        .padding(top = 10.dp)
                ) {
                    CalcButton(modifier = modifier.weight(1f), num = "INV", click = { shouldShowInverse = !shouldShowInverse }, fontSize = 20.sp, contentColor =  onDarkGreyButton)
                    if(!shouldShowInverse) {
                        CalcButton(modifier = modifier.weight(1f), num = "sin", click = { addNumber("sin(") }, fontSize = 25.sp, contentColor = onDarkGreyButton)
                        CalcButton(modifier = modifier.weight(1f), num = "cos", click = { addNumber("cos(") }, fontSize = 22.sp, contentColor = onDarkGreyButton)
                        CalcButton(modifier = modifier.weight(1f), num = "tan", click = { addNumber("tan(") }, fontSize = 25.sp, contentColor = onDarkGreyButton)
                    } else {
                        CalcButton(modifier = modifier.weight(1f), num = "sin⁻¹", click = { addNumber("asin(") }, fontSize = 15.sp, contentColor = onDarkGreyButton)
                        CalcButton(modifier = modifier.weight(1f), num = "cos⁻¹", click = { addNumber("acos(") }, fontSize = 15.sp, contentColor = onDarkGreyButton)
                        CalcButton(modifier = modifier.weight(1f), num = "tan⁻¹", click = { addNumber("atan(") }, fontSize = 15.sp, contentColor = onDarkGreyButton)
                    }
                }

                Row (
                    modifier = modifier
                        .padding(top = 10.dp)
                ) {
                    CalcButton(modifier = modifier.weight(1f), num = "1/x", click = { addNumber("1/") }, fontSize = 25.sp, contentColor = onDarkGreyButton)
                    if(!shouldShowInverse){
                        CalcButton(modifier = modifier.weight(1f), num = "ln", click = { addNumber("ln(")}, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    } else {
                        CalcButton(modifier = modifier.weight(1f), num = "e^", click = { addNumber("eksp(") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    }
                    if(!shouldShowInverse){
                        CalcButton(modifier = modifier.weight(1f), num = "log", click = { addNumber("log(") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    } else {
                        CalcButton(modifier = modifier.weight(1f), num = "10^", click = { addNumber("10^") }, fontSize = 22.sp, contentColor =  onDarkGreyButton)
                    }

                    CalcButton(modifier = modifier.weight(1f), num = "e", click = { addNumber("$E") }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                }
            }
                Row (
                    modifier = modifier
                        .padding(top = 10.dp)
                ) {
                    CalcButton(modifier = modifier.weight(1f), num = "AC", click = { clear() }, contentColor = onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "⌫", click = { backspace() }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "SC", click = { shouldShowScientific = !shouldShowScientific }, fontSize = 25.sp, contentColor =  onDarkGreyButton)
                    CalcButton(modifier = modifier.weight(1f), num = "÷", click = { addOperator("÷") }, fontSize = 25.sp, containerColor = DarkBlueButton, contentColor = onDarkBlueButton)

                }

                Row {
                    CalcButton(modifier = modifier.weight(1f), num = "7", click = { addNumber("7") })
                    CalcButton(modifier = modifier.weight(1f), num = "8", click = { addNumber("8") })
                    CalcButton(modifier = modifier.weight(1f), num = "9", click = { addNumber("9") })
                    CalcButton(modifier = modifier.weight(1f), num = "x", click = { addOperator("x") }, fontSize = 25.sp, containerColor = DarkBlueButton, contentColor = onDarkBlueButton)
                }



                Row {
                    CalcButton(modifier = modifier.weight(1f), num = "4", click = { addNumber("4") })
                    CalcButton(modifier = modifier.weight(1f), num = "5", click = { addNumber("5") })
                    CalcButton(modifier = modifier.weight(1f), num = "6", click = { addNumber("6")  })
                    CalcButton(modifier = modifier.weight(1f), num = "−", click = { addOperator("-") }, fontSize = 25.sp, containerColor = DarkBlueButton, contentColor = onDarkBlueButton)
                }



                Row {
                    CalcButton(modifier = modifier.weight(1f), num = "1", click = { addNumber("1")  })
                    CalcButton(modifier = modifier.weight(1f), num = "2", click = { addNumber("2")  })
                    CalcButton(modifier = modifier.weight(1f), num = "3", click = { addNumber("3") })
                    CalcButton(modifier = modifier.weight(1f), num = "+", click = { addOperator("+") }, fontSize = 25.sp, containerColor = DarkBlueButton, contentColor = onDarkBlueButton
                    )
                }



                Row {
                    CalcButton(modifier = modifier.weight(1f), num = "%", click = { calculatePercent() }, fontSize = 25.sp)
                    CalcButton(modifier = modifier.weight(1f), num = "0", click = { addNumber("0") })
                    CalcButton(modifier = modifier.weight(1f), num = ".", click = { addNumber(".") }, fontSize = 25.sp)
                    CalcButton(modifier = modifier.weight(1f), num = "=", click = { calculate() }, fontSize = 25.sp, containerColor = BlueEqualButton)
                }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview()
{
    Calculator()
}

@Composable
fun CalcButton (
    modifier : Modifier = Modifier,
    num : String = "",
    click : ()-> Unit,
    containerColor : Color = DarkGreyButton,
    contentColor : Color = onDarkBlueButton,
    fontSize : TextUnit = 20.sp
)
{
    Button(
        modifier = modifier
            .padding(5.dp)
            .aspectRatio(1.5f),
        onClick = click,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor

        ),
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            text = num,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
