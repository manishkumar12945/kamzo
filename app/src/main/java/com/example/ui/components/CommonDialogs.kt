package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Job
import com.example.data.model.Worker
import com.example.ui.theme.KamzoGreen
import com.example.ui.theme.KamzoNavy
import com.example.ui.theme.KamzoOrange

@Composable
fun InfoDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = KamzoNavy)
            ) {
                Text("Close")
            }
        }
    )
}

@Composable
fun ContactWorkerDialog(
    worker: Worker,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(KamzoNavy.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Call, contentDescription = "Call", tint = KamzoNavy)
            }
        },
        title = { Text("Contact ${worker.name}", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Trade: ${worker.skill}", fontWeight = FontWeight.SemiBold)
                Text("Service Area: ${worker.area}")
                Text("Rate: ${worker.price}", color = KamzoGreen, fontWeight = FontWeight.Medium)
                Text("Phone: ${worker.phone}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "You can call directly for immediate emergency service or send an SMS inquiry.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${worker.phone}")
                    }
                    context.startActivity(intent)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = KamzoGreen),
                modifier = Modifier.testTag("dial_worker_button")
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Call Now")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookWorkerDialog(
    worker: Worker,
    onDismiss: () -> Unit,
    onConfirm: (date: String, slot: String, address: String, notes: String) -> Unit
) {
    var date by remember { mutableStateOf("Today (Urgent / ASAP)") }
    var slot by remember { mutableStateOf("Morning (9 AM - 12 PM)") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val dateOptions = listOf("Today (Urgent / ASAP)", "Tomorrow", "Within 3 Days", "Weekend")
    val slotOptions = listOf("Morning (9 AM - 12 PM)", "Afternoon (12 PM - 3 PM)", "Evening (3 PM - 7 PM)")

    var dateExpanded by remember { mutableStateOf(false) }
    var slotExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Book ${worker.name}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("${worker.skill} • ${worker.price}", style = MaterialTheme.typography.bodyMedium, color = KamzoNavy)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = dateExpanded,
                    onExpandedChange = { dateExpanded = it }
                ) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Service Date") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dateExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = dateExpanded,
                        onDismissRequest = { dateExpanded = false }
                    ) {
                        dateOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    date = opt
                                    dateExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = slotExpanded,
                    onExpandedChange = { slotExpanded = it }
                ) {
                    OutlinedTextField(
                        value = slot,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Preferred Time Slot") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = slotExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = slotExpanded,
                        onDismissRequest = { slotExpanded = false }
                    ) {
                        slotOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    slot = opt
                                    slotExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        if (error != null) error = null
                    },
                    label = { Text("Your Address / Colony / Landmark *") },
                    placeholder = { Text("e.g. Flat 302, Green Enclave") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("booking_address_input")
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Work Details / Issue Description") },
                    placeholder = { Text("e.g. Kitchen tap leaking or switch sparking") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (address.isBlank()) {
                        error = "Please provide your address so the worker can visit."
                        return@Button
                    }
                    onConfirm(date, slot, address.trim(), notes.trim())
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = KamzoNavy),
                modifier = Modifier.testTag("confirm_booking_button")
            ) {
                Text("Confirm Request")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun PostJobDialog(
    onDismiss: () -> Unit,
    onSubmit: (title: String, company: String, pay: String, type: String, location: String, details: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var pay by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Daily wage") }
    var location by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val types = listOf("Daily wage", "Skilled work", "Contract", "Full-time", "Part-time")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Post a Job Vacancy", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; error = null },
                    label = { Text("Job Title *") },
                    placeholder = { Text("e.g. Mason needed for wall work") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_job_title_input")
                )

                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Employer / Contractor Name") },
                    placeholder = { Text("e.g. Apex Builders or Homeowner") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = pay,
                    onValueChange = { pay = it },
                    label = { Text("Wage / Payment Terms") },
                    placeholder = { Text("e.g. ₹600 / day or ₹15,000 / month") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Work Location / Site") },
                    placeholder = { Text("e.g. Sector 44 Site Office") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Job Type", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    types.take(3).forEach { t ->
                        val selected = type == t
                        Button(
                            onClick = { type = t },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) KamzoNavy else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(t, fontSize = 11.sp)
                        }
                    }
                }

                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("Requirements & Instructions") },
                    placeholder = { Text("Experience required, tools to bring, timings...") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        error = "Job title is required."
                        return@Button
                    }
                    val finalPay = if (pay.isBlank()) "Pay discussed with employer" else pay.trim()
                    onSubmit(title.trim(), company.trim(), finalPay, type, location.trim(), details.trim())
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = KamzoNavy),
                modifier = Modifier.testTag("submit_post_job_button")
            ) {
                Text("Publish Job")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ApplyJobDialog(
    job: Job,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var applicantName by remember { mutableStateOf("") }
    var applicantPhone by remember { mutableStateOf("") }
    var applicantExperience by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Apply for Position", fontWeight = FontWeight.Bold)
                Text(job.title, color = KamzoNavy, style = MaterialTheme.typography.bodyMedium)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Employer: ${job.company}", fontWeight = FontWeight.Medium)
                Text("Wage: ${job.pay}", color = KamzoGreen, fontWeight = FontWeight.SemiBold)
                Text("Location: ${job.location}")
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = applicantName,
                    onValueChange = { applicantName = it; error = null },
                    label = { Text("Your Full Name *") },
                    placeholder = { Text("e.g. Ramesh Singh") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = applicantPhone,
                    onValueChange = { applicantPhone = it; error = null },
                    label = { Text("Mobile Number *") },
                    placeholder = { Text("e.g. 9876543210") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = applicantExperience,
                    onValueChange = { applicantExperience = it },
                    label = { Text("Years of Relevant Experience") },
                    placeholder = { Text("e.g. 3 years") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (applicantName.isBlank() || applicantPhone.isBlank()) {
                        error = "Please enter both name and mobile number."
                        return@Button
                    }
                    onConfirm()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = KamzoGreen),
                modifier = Modifier.testTag("submit_job_application_button")
            ) {
                Text("Submit Application")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RegisterWorkerDialog(
    onDismiss: () -> Unit,
    onSubmit: (name: String, skill: String, area: String, exp: String, price: String, phone: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var skill by remember { mutableStateOf("Electrician") }
    var area by remember { mutableStateOf("") }
    var exp by remember { mutableStateOf("3") }
    var price by remember { mutableStateOf("Visit fee from ₹199") }
    var phone by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val tradeSkills = listOf(
        "Electrician", "Plumber", "Carpenter", "Painter",
        "Mason (Rajmistri)", "Helper / Labour", "Appliance Repair", "Mechanic"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Register as Worker / Mechanic", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; error = null },
                    label = { Text("Full Name *") },
                    placeholder = { Text("e.g. Sunil Verma") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Trade / Skill:", style = MaterialTheme.typography.labelMedium)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    tradeSkills.chunked(2).forEach { rowList ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            rowList.forEach { s ->
                                val selected = skill == s
                                Button(
                                    onClick = { skill = s },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selected) KamzoNavy else MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(s, fontSize = 11.sp, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text("Service Area / Local City") },
                    placeholder = { Text("e.g. Sector 18 & nearby") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = exp,
                    onValueChange = { exp = it },
                    label = { Text("Years of Experience") },
                    placeholder = { Text("e.g. 5") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Standard Rate / Fee Description") },
                    placeholder = { Text("e.g. Visit fee from ₹199") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number *") },
                    placeholder = { Text("e.g. +91 98765 00000") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank() || phone.isBlank()) {
                        error = "Name and phone are required for registration."
                        return@Button
                    }
                    onSubmit(
                        name.trim(),
                        skill,
                        if (area.isBlank()) "Local Area" else area.trim(),
                        exp.trim(),
                        price.trim(),
                        phone.trim()
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = KamzoGreen)
            ) {
                Text("Complete Registration")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun LanguageDialog(
    currentLanguage: String,
    onSelectLanguage: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf("English", "हिन्दी (Hindi)", "Hinglish (Casual)")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select App Language", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                languages.forEach { lang ->
                    val isSelected = currentLanguage.startsWith(lang.take(4))
                    OutlinedButton(
                        onClick = {
                            onSelectLanguage(lang)
                            onDismiss()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) KamzoNavy.copy(alpha = 0.1f) else Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(lang, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                            if (isSelected) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = KamzoGreen)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}
