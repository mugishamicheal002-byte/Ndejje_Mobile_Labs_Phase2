package com.ndejje.momocalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ndejje.momocalc.ui.theme.MoMoCalculatorAppTheme
import kotlinx.coroutines.delay
import java.lang.reflect.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoMoCalculatorAppTheme {
                MaterialTheme (typography = MoMoTypography) {
                    Surface { MoMoCalcScreen() }
                }

                }
            }
        }
    }
@Composable
fun MoMoCalcScreen(){
    var amountInput by remember { mutableStateOf("") }
    var isCalculating by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }


    val numericAmount = amountInput.toDoubleOrNull()
    val isError = amountInput.isNotEmpty() && numericAmount == null


    LaunchedEffect(amountInput) {
        // Reset the UI when input changes
        showResult = false
        isCalculating = false

        // Only proceed if there is valid input and no error
        if (amountInput.isNotEmpty() && !isError) {
            // Wait 1 second after last keystroke
            delay(1000)

            // Show "Calculating..."
            isCalculating = true

            // Simulate network/processing delay
            delay(500)

            // Show the final result
            isCalculating = false
            showResult = true
        }
    }


    val rate = if ((numericAmount ?: 0.0) < 2500000) 0.03 else 0.015
    val fee = (numericAmount ?: 0.0) * rate
    val formattedFee = "UGX %,.0f".format(fee)

    Column(
        modifier = Modifier
            .fillMaxSize() // Crucial: Takes up the whole screen so items can be centered
            .padding(24.dp),
        verticalArrangement = Arrangement.Center, // Centers items vertically (top to bottom)
        horizontalAlignment = Alignment.CenterHorizontally // Centers items horizontally (left to right)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        HoistedAmountInput(
            amount = amountInput,
            onAmountChange = { amountInput = it },
            isError = isError,
            // Ensure your HoistedAmountInput accepts a Modifier if you want it full width
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // UI Logic for loading vs. result
        when {
            isCalculating -> {
                Text(
                    text = "Calculating...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
            showResult && !isError && amountInput.isNotEmpty() -> {
                Text(
                    text = stringResource(R.string.fee_label, formattedFee),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HoistedAmountInput(
    amount: String,
    onAmountChange: (String) -> Unit,
    isError: Boolean = false,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        TextField(
            value = amount,
            onValueChange = onAmountChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(stringResource(R.string.enter_amount))
            },
            supportingText = {
                if (isError) {
                    Text(
                        text = stringResource(R.string.error_numbers_only),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun MoMoCalcPreview() {
    MaterialTheme { MoMoCalcScreen() }
}