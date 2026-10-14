package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RoadsideRequest
import com.example.data.model.RoadsideStatus
import com.example.ui.theme.KamzoBlue
import com.example.ui.theme.KamzoGreen
import com.example.ui.theme.KamzoNavy
import com.example.ui.theme.KamzoOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadsideScreen(
    activeRequest: RoadsideRequest?,
    onSubmitRequest: (vehicle: String, issue: String, location: String, details: String) -> Unit,
    onCancelRequest: () -> Unit,
    onCompleteRequest: () -> Unit
) {
    val context = LocalContext.current

    val vehicleTypes = listOf(
        "Bike / Scooter",
        "Car / SUV",
        "Auto-rickshaw",
        "Van / Light commercial",
        "Bus",
        "Truck / Heavy vehicle"
    )

    val issues = listOf(
        "Puncture / tyre",
        "Battery / jump-start",
        "Engine / starting problem",
        "Overheating",
        "Fuel delivery request",
        "Other mechanical issue",
        "Need towing"
    )

    var vehicle by remember { mutableStateOf(vehicleTypes.first()) }
    var issue by remember { mutableStateOf(issues[2]) }
    var location by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var locationError by remember { mutableStateOf<String?>(null) }

    var vehicleExpanded by remember { mutableStateOf(false) }
    var issueExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("roadside_screen_scroll"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // If an active request exists and is not cancelled
        if (activeRequest != null && activeRequest.status != RoadsideStatus.CANCELLED) {
            item {
                ActiveRequestCard(
                    request = activeRequest,
                    onCallMechanic = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${activeRequest.assignedMechanicPhone}")
                        }
                        context.startActivity(intent)
                    },
                    onCancel = onCancelRequest,
                    onComplete = onCompleteRequest
                )
            }
        }

        // Safety Caution Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(KamzoOrange.copy(alpha = 0.12f))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CarRepair,
                        contentDescription = null,
                        tint = KamzoOrange,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Emergency Roadside Assistance",
                            fontWeight = FontWeight.Bold,
                            color = KamzoNavy,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Mechanical breakdown help — not an accident-response service. Share a safe location and request a suitable nearby mechanic.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Section Form Heading
        item {
            Text(
                text = if (activeRequest == null) "Request Immediate Mechanic" else "Submit Another Request",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = KamzoNavy
            )
        }

        // Vehicle Type Selector
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Vehicle Type",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                ExposedDropdownMenuBox(
                    expanded = vehicleExpanded,
                    onExpandedChange = { vehicleExpanded = it }
                ) {
                    OutlinedTextField(
                        value = vehicle,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = vehicleExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .testTag("roadside_vehicle_dropdown"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = vehicleExpanded,
                        onDismissRequest = { vehicleExpanded = false }
                    ) {
                        vehicleTypes.forEach { vt ->
                            DropdownMenuItem(
                                text = { Text(vt) },
                                onClick = {
                                    vehicle = vt
                                    vehicleExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Issue Selector
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "What is the problem?",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                ExposedDropdownMenuBox(
                    expanded = issueExpanded,
                    onExpandedChange = { issueExpanded = it }
                ) {
                    OutlinedTextField(
                        value = issue,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = issueExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .testTag("roadside_issue_dropdown"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = issueExpanded,
                        onDismissRequest = { issueExpanded = false }
                    ) {
                        issues.forEach { iss ->
                            DropdownMenuItem(
                                text = { Text(iss) },
                                onClick = {
                                    issue = iss
                                    issueExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Location Input with Quick Landmark Filler
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current location / landmark *",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextButton(
                        onClick = {
                            location = "NH-48 Milestone 24, near Highway Petrol Pump"
                            locationError = null
                        }
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Auto-fill GPS", fontSize = 12.sp)
                    }
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = {
                        location = it
                        if (locationError != null) locationError = null
                    },
                    placeholder = { Text("e.g. highway name, milestone, nearby landmark") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = KamzoOrange
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("roadside_location_input"),
                    shape = RoundedCornerShape(14.dp),
                    isError = locationError != null,
                    supportingText = {
                        if (locationError != null) {
                            Text(locationError!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }

        // Additional Details
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Additional details (optional)",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    placeholder = { Text("Describe the symptoms or vehicle model (e.g. white Swift Dzire, tyre flat)") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }

        // Submit Button
        item {
            Button(
                onClick = {
                    if (location.isBlank()) {
                        locationError = "Please enter your location or landmark so the mechanic can reach you."
                        return@Button
                    }
                    onSubmitRequest(vehicle, issue, location.trim(), details.trim())
                },
                colors = ButtonDefaults.buttonColors(containerColor = KamzoOrange),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("roadside_submit_button")
            ) {
                Icon(Icons.Default.NearMe, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Find Nearby Mechanic", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Help Information & Process Steps
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "How KAMZO Roadside Works",
                        fontWeight = FontWeight.Bold,
                        color = KamzoNavy
                    )
                    Text("1. Match nearest qualified mechanic by location & vehicle type.")
                    Text("2. Mechanic verifies tool requirement and accepts dispatch.")
                    Text("3. Live ETA & direct contact details shared immediately.")
                    Text("4. Pay verified mechanic standard inspection or repair fee on site.")
                }
            }
        }

        // Live GPS Notice
        item {
            Text(
                text = "Demo only: location is typed manually. Live GPS, real provider matching, tracking and guaranteed ETAs are simulated for prototype verification.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ActiveRequestCard(
    request: RoadsideRequest,
    onCallMechanic: () -> Unit,
    onCancel: () -> Unit,
    onComplete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("active_roadside_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(KamzoGreen.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = KamzoGreen, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Assistance Request #${request.id}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = KamzoNavy
                        )
                        Text(
                            text = request.status.name,
                            fontWeight = FontWeight.Bold,
                            color = when (request.status) {
                                RoadsideStatus.SEARCHING -> KamzoOrange
                                RoadsideStatus.ASSIGNED, RoadsideStatus.EN_ROUTE -> KamzoBlue
                                RoadsideStatus.ARRIVED, RoadsideStatus.COMPLETED -> KamzoGreen
                                RoadsideStatus.CANCELLED -> MaterialTheme.colorScheme.error
                            },
                            fontSize = 13.sp
                        )
                    }
                }

                if (request.status == RoadsideStatus.SEARCHING || request.status == RoadsideStatus.EN_ROUTE) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp,
                        color = KamzoOrange
                    )
                }
            }

            HorizontalDivider()

            // Request Details
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                DetailRow(label = "Vehicle", value = request.vehicle)
                DetailRow(label = "Problem", value = request.issue)
                DetailRow(label = "Location", value = request.location)
                if (request.details.isNotBlank()) {
                    DetailRow(label = "Notes", value = request.details)
                }
            }

            HorizontalDivider()

            // Assigned Mechanic Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(KamzoNavy.copy(alpha = 0.05f))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CarRepair,
                    contentDescription = null,
                    tint = KamzoNavy,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.assignedMechanicName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "ETA: ~${request.etaMinutes} mins • ${request.distanceKm} km away",
                        style = MaterialTheme.typography.bodySmall,
                        color = KamzoNavy,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(
                    onClick = onCallMechanic,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(KamzoGreen)
                        .size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = "Call Mechanic",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cancel")
                }

                Button(
                    onClick = onComplete,
                    colors = ButtonDefaults.buttonColors(containerColor = KamzoGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CheckCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Resolved")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(90.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}
