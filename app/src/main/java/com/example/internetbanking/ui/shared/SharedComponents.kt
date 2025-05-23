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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.DialogProperties
import com.example.internetbanking.R
import com.example.internetbanking.ui.customer.LineConfirm
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_red
import com.example.internetbanking.ui.theme.custom_light_green1
import com.example.internetbanking.ui.theme.custom_mint_green
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
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
import java.util.Calendar
import java.util.Locale
import kotlin.math.absoluteValue

// COMPONENTS
// App Background
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

// Gradient Button
@Composable
fun GreenGradientButton(
    onButtonClick: () -> Unit,
    buttonCustom:@Composable ()-> Unit,
//    buttonText: String,
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
//        Text(
//            text = buttonText,
//            fontSize = 20.sp
//        )
        buttonCustom()
    }
}

// Balance Information
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

// Card Swiper
@Composable
fun PagerBalanceInformation(
    pages: List<@Composable () -> Unit>,
    onAddAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pageCount = pages.size + 1
    val pagerState = rememberPagerState { pageCount }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
        ) { page ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                // Create Card
                if (page == pageCount - 1) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(
                                    corner = CornerSize(20.dp)
                                )
                            )
                            .drawBehind {
                                drawRoundRect(
                                    color = Color.Gray,
                                    style = Stroke(
                                        width = 3.dp.toPx(),
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(
                                                10f,
                                                10f
                                            ), 0f
                                        )
                                    ), cornerRadius = CornerRadius(
                                        20.dp.toPx(),
                                        20.dp.toPx()
                                    )
                                )
                            }
                            .clickable(
                                onClick = onAddAccountClick
                            )
                            .padding(20.dp),

                        ) {
                        Icon(Icons.Outlined.Add, contentDescription = null)
                        Text(
                            "Add Account",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    // Cards
                    pages[page]()
                }
            }
        }
        //indicator
        Row(
            verticalAlignment = Alignment.CenterVertically,
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
                            color = if (selected) custom_light_green1
                            else Color.Gray,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }
    }
}

// Information Select Options
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

// Information Line
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

//@Preview(showBackground = true)
@Composable
fun ViewProfitRatesAndProfit(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (expanded) {

        AlertDialog(
            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            containerColor = custom_mint_green,
            onDismissRequest = onDismiss,
            title = { Text("Profit Rates and Profit") },
            text = {
                Column {
                    LineConfirm(
                        label = "Deposit term",
                        data = "1 month(1.1%/month)"
                    )
                    Spacer(Modifier.height(20.dp))
                    LineConfirm(
                        label = "Deposit Amount",
                        data = "1.000.000 VND"
                    )
                }

            },

            confirmButton ={
                Button(
                    onClick = onConfirmClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = custom_light_green1,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                        .height(50.dp)
                        .width(100.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Confirm")
                }
            },


        )
    }
}

// LogOut Dialog
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

// Date Picker
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

// App Alert Dialog
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

// FUNCTION - FORMAT
// Timestamp to Readable Date Time
fun Long.toReadableDateTime(pattern: String = "HH:mm - dd/MM/yyyy"): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter
        .ofPattern(pattern)
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

// Readable Date Time to Timestamp
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

// Big Decimal to VN Currency String
fun formatCurrencyVN(amount: BigDecimal): String {
    val symbols = DecimalFormatSymbols(Locale("vi", "VN")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(amount)
}

fun getMillisAfterMonths(monthsToAdd: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, monthsToAdd)
    return calendar.timeInMillis
}



// FUNCTION - FIREBASE
// Check Exist Data From A Collection
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

// Create Unique Transaction ID
fun generateTransactionId(): String {
    val datePart = java.time.LocalDate.now().toString().replace("-", "")
    val randomPart = (1..8)
        .map { ('A'..'Z') + ('0'..'9') }
        .flatten()
        .shuffled()
        .take(8)
        .joinToString("")
    return "LB-$datePart-$randomPart"
}

suspend fun generateUniqueTransactionId(): String {
    var transactionId: String
    do {
        transactionId = generateTransactionId()
    } while (checkTransactionIdExists(transactionId))
    return transactionId
}

suspend fun checkTransactionIdExists(transactionId: String): Boolean {
    val docSnapshot = Firebase.firestore
        .collection("transactionHistories")
        .document(transactionId)
        .get()
        .await()

    return docSnapshot.exists()
}

// Check Existing Card Number
suspend fun checkCardNumberExistsInCollections(
    cardNumber: String
): Boolean {
    return try {
        val db = Firebase.firestore

        val collectionsToCheck = listOf("checking", "saving", "mortgage")

        for (collection in collectionsToCheck) {
            val docSnapshot = db.collection(collection)
                .document(cardNumber)
                .get()
                .await()

            if (docSnapshot.exists()) {
                return true
            }
        }

        false
    } catch (e: Exception) {
        Log.e("CheckCardNumber", "Error checking documentId: ${e.message}")
        false
    }
}

// Generate Random Card Number
suspend fun generateUniqueCardNumber(): String {
    var cardNumber: String
    while (true) {
        cardNumber = (1000000000..9999999999).random().toString()
        val isCardNumberExist = checkCardNumberExistsInCollections(cardNumber)
        if (!isCardNumberExist) {
            return cardNumber
        }
    }
}

// Add A Document To Collection
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

// Update User's Data
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

// Get Field
suspend fun getFieldValueFromDocument(
    collectionName: String,
    documentId: String,
    fieldName: String
): Any? {
    return try {
        val snapshot = Firebase.firestore
            .collection(collectionName)
            .document(documentId)
            .get()
            .await()

        if (snapshot.exists()) {
            snapshot.get(fieldName)
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("GetFieldValue", "Error getting field '$fieldName' from $collectionName/$documentId: ${e.message}")
        null
    }
}

// Password Confirm
@Composable
fun PasswordConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    shape = RoundedCornerShape(CornerSize(10.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = custom_light_green1
                    ),
                    onClick = {
                        onConfirm(password)
                        onDismiss()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Cancel", color = Color.Black)
                }
            },
            title = { Text("Password Authentication") },
            text = {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Ascii
                    )
                )
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }
}

// Update A Field
suspend fun updateFieldInDocument(
    collectionName: String,
    documentId: String,
    fieldName: String,
    newValue: Any
): Boolean {
    return try {
        val db = Firebase.firestore
        db.collection(collectionName)
            .document(documentId)
            .update(fieldName, newValue)
            .await()
        true
    } catch (e: Exception) {
        Log.e("UpdateField", "Error updating field '$fieldName' in $collectionName/$documentId: ${e.message}")
        false
    }
}
