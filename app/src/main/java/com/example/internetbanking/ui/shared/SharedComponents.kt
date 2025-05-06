package com.example.internetbanking.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(
                brush = GradientColors.GreenDarkToLight
            )
    ) {
        content()
    }
}

@Composable
fun GreenGradientButton(
    onButtonClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onButtonClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .background(
                shape = RoundedCornerShape(12.dp),
                brush = GradientColors.GreenDarkToLight
            )
    ) {
        Text(
            text = buttonText,
            fontSize = 20.sp
        )
    }
}

@Composable
fun BalanceInformation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = GradientColors.GreenLightToDark,
                shape = RoundedCornerShape(
                    topStart = CornerSize(40),
                    bottomEnd = CornerSize(40),
                    topEnd = CornerSize(20),
                    bottomStart = CornerSize(20)
                )
            )
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(
                    topStart = CornerSize(40),
                    bottomEnd = CornerSize(40),
                    topEnd = CornerSize(20),
                    bottomStart = CornerSize(20)
                )
            )
            .padding(all = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Debit account",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Balance",
                color = Color.White
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "023456000000",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        color = Color.Green,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(7.dp)
            )
            Spacer(Modifier.height(5.dp))
            Row {
                Text(
                    text = "120,000",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "VND",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationSelect(
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    suffix: @Composable () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = 10.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp)
            .height(50.dp)
            .clickable {
                expanded = true
            }

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold
            )
            TextButton(
                onClick = { expanded = true }
            ) {
                Text(
                    text = placeholder,
                    color = Color.Gray
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                DropdownMenuItem(
                    text =
                        { Text("Vietcombank") },
                    onClick = { expanded = false }
                )
                DropdownMenuItem(
                    text =
                        { Text("Sacombank") },
                    onClick = { expanded = false }
                )
                DropdownMenuItem(
                    text =
                        { Text("Techcombank") },
                    onClick = { expanded = false }
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            suffix()
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    label: String,
    placeholder: String,
    onDatePick: (String) -> Unit,
    modifier: Modifier = Modifier,
    canSelectFuture: Boolean = false,
    suffix: @Composable () -> Unit = {}
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val selectableDates = if (canSelectFuture) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean = true
        }
    } else {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis < Clock.systemUTC().millis()
            }
        }
    }
    val datePickerState = rememberDatePickerState(
        selectableDates = selectableDates
    )
    var openSheet by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = 10.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp)
            .height(50.dp)
            .clickable {
               openSheet = true
            }

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
        ) {
            Text(label, fontWeight = FontWeight.Bold)
            TextButton(
                onClick = { openSheet = true }
            ) {
                Text(
                    text = if (date == "") placeholder else date,
                    color = Color.Gray)
            }
            if (openSheet) {
                ModalBottomSheet(
                    onDismissRequest = { openSheet = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                ) {
                    Column(modifier = Modifier.systemBarsPadding()) {

                        DatePicker(state = datePickerState)
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                openSheet = false
                            }) {
                                Text("Cancel")
                            }
                            TextButton(onClick = {
                                date = datePickerState.selectedDateMillis?.let {
                                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                }?.format(formatter) ?: ""
                                openSheet = false
                                onDatePick(date)
                            }) {
                                Text("Select")
                            }
                        }
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            suffix()
        }
    }
}



