package com.example.internetbanking.ui.shared

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.internetbanking.R
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_red
import com.example.internetbanking.ui.theme.custom_light_green1
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.absoluteValue

// Gradient Background
@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.app_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.fillMaxSize()
        )
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
                brush = GradientColors.Green_DarkToLight
            )
    ) {
        Text(
            text = buttonText,
            fontSize = 20.sp
        )
    }
}

@Composable
fun BalanceInformation(
    cardNumber: String = "",
    balance: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = GradientColors.Green_LightToDark,
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
                text = cardNumber,
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
                    text = balance,
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

@Composable
fun PagerBalanceInformation(
    pages: List<@Composable ()-> Unit>,
    onAddAccountClick: () -> Unit
) {
    val pageCount = pages.size
    val pagerState = rememberPagerState { pageCount }

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp) // chỉnh chiều cao cho phù hợp
        ) { page ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        // We animate the alpha, between 50% and 100%
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }) {
            pages[page]()
            }
        }
        // Thêm indicator nếu muốn
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            repeat(pageCount) { index ->
                val selected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (selected) 12.dp else 8.dp)
                        .background(
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.3f
                            ),
                            shape = RoundedCornerShape(50)
                        )
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
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    suffix: @Composable () -> Unit = {},
    errorMessage: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .border(
                    width = if (errorMessage == "") 1.dp else 3.dp,
                    color = if (errorMessage == "") Color.Gray else Color.Red,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(10.dp)
                .height(60.dp)
                .clickable {
                    expanded = true
                }

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold
                )
                if (selectedOption != "") {
                    Text(
                        text = selectedOption,
                        color = Color.Black,
                        modifier = Modifier.clickable {
                            expanded = true
                        }
                    )
                } else {
                    Text(
                        text = placeholder,
                        color = Color.Gray,
                        modifier = Modifier.clickable {
                            expanded = true
                        }
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedOption = option
                                expanded = false
                                onOptionSelected(selectedOption)
                            }
                        )
                    }
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
        if (errorMessage != "") {
            Text(
                text = errorMessage,
                fontSize = 14.sp,
                color = Color.Red
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationLine(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEnable: Boolean = true,
    suffix: @Composable () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    errorMessage: String = ""
) {
    Column(
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .border(
                    width = if (errorMessage == "") 1.dp else 3.dp,
                    color = if (errorMessage == "") Color.Gray else Color.Red,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(10.dp)
                .height(60.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Box {
                    Text(
                        text = if (value == "") placeholder else "",
                        color = Color.Gray
                    )
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        enabled = isEnable,
                        singleLine = true,
                        keyboardOptions = keyboardOptions,
                        modifier = Modifier.fillMaxWidth()
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
        if (errorMessage != "") {
            Text(
                text = errorMessage,
                fontSize = 14.sp,
                color = Color.Red
            )
        }
    }
}

@Composable
fun LogoutDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = onConfirmLogout) {
                    Text(
                        text = "Yes",
                        color = Color.Red
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No")
                }
            }
        )
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
    suffix: @Composable () -> Unit = {},
    errorMessage: String = ""
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

    Column(
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .border(
                    width = if (errorMessage == "") 1.dp else 3.dp,
                    color = if (errorMessage == "") Color.Gray else Color.Red,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(10.dp)
                .height(60.dp)
                .clickable {
                    openSheet = true
                }

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 10.dp)
            ) {
                Text(label, fontWeight = FontWeight.Bold)
                Text(
                    text = if (date == "") placeholder else date,
                    color = Color.Gray,
                    modifier = Modifier.clickable {
                        openSheet = true
                    }
                )
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
        if (errorMessage != "") {
            Text(
                text = errorMessage,
                fontSize = 14.sp,
                color = Color.Red
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppAlertDialog(
    isShow: Boolean = false,
    title: String,
    content: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (isShow) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title
                )
            },
            text = {
                Text(
                    text = content
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text(
                        text = "Change",
                        color = custom_light_green1
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(
                        text = "Cancel",
                        color = custom_dark_red
                    )
                }
            }
        )
    }
}

fun Long.toReadableDateTime(pattern: String = "HH:mm - dd/MM/yyyy"): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter
        .ofPattern(pattern)
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

fun dateStringToTimestamp(dateString: String): Long? {
    return try {
        // Define the date format for dd/mm/yyyy
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.isLenient = false
        val date = sdf.parse(dateString)
        date?.time ?: throw IllegalArgumentException("Parsed date is null")
    } catch (e: Exception) {
        Log.e("DateConversion", "Error parsing date '$dateString': ${e.message}")
        null
    }
}

// Check Exist Data
suspend fun checkExistData(
    collectionName: String,
    fieldName: String,
    value: String
): Boolean {
    return try {
        val snapshot = Firebase.firestore
            .collection(collectionName)
            .whereEqualTo(fieldName, value)
            .limit(1)
            .get()
            .await()
        !snapshot.isEmpty
    } catch (e: Exception) {
        Log.e("FirestoreCheck", "Error checking $fieldName in $collectionName", e)
        false
    }
}

// Check Card Number
suspend fun checkCardNumberExistsInAllDocuments(
    collectionName: String,
    cardNumber: String
): Boolean {
    return try {
        val collectionSnapshot = Firebase.firestore
            .collection(collectionName)
            .get()
            .await()

        for (document in collectionSnapshot.documents) {
            val checking = document.get("Checking") as? Map<*, *>
            val saving = document.get("Saving") as? Map<*, *>
            val mortgage = document.get("Mortgage") as? Map<*, *>

            val allAccounts = listOfNotNull(checking, saving, mortgage)

            for (accountMap in allAccounts) {
                if (accountMap["cardNumber"] == cardNumber) {
                    return true
                }
            }
        }

        false
    } catch (e: Exception) {
        Log.e("CheckCardNumber", "Error checking cardNumber: ${e.message}")
        false
    }
}


// Add Document To Collection
fun addDocumentToCollection(
    collectionName: String,
    data: Map<String, Any>,
    documentId: String? = null,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit = {}
) {
    val db = Firebase.firestore

    val docRef = if (documentId != null) {
        db.collection(collectionName).document(documentId)
    } else {
        db.collection(collectionName).document()
    }

    docRef.set(data)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { e -> onFailure(e) }
}

// Get Data From Document
suspend fun getFieldFromDocument(
    collectionName: String,
    documentId: String,
    fieldName: String
): Any? {
    return try {
        val documentSnapshot = FirebaseFirestore.getInstance()
            .collection(collectionName)
            .document(documentId)
            .get()
            .await()

        if (documentSnapshot.exists()) {
            documentSnapshot.get(fieldName)
        } else {
            null
        }
    } catch (_: Exception) {
        null
    }
}

// Update Data
fun updateUserFieldByAccountId(
    accountId: String,
    fieldName: String,
    newValue: Any,
    onSuccess: () -> Unit = {},
    onFailure: (Exception) -> Unit = {}
) {
    val db = Firebase.firestore

    db.collection("users")
        .document(accountId)
        .update(fieldName, newValue)
        .addOnSuccessListener {
            Log.d("FirestoreUpdate", "Field $fieldName updated successfully")
            onSuccess()
        }
        .addOnFailureListener { exception ->
            Log.e("FirestoreUpdate", "Failed to update $fieldName", exception)
            onFailure(exception)
        }
}

// Big Decimal to VN Currency String
fun formatCurrencyVN(amount: BigDecimal): String {
    val symbols = DecimalFormatSymbols(Locale("vi", "VN")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(amount)
}

