package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorkerBooking
import com.example.ui.components.InfoDialog
import com.example.ui.components.LanguageDialog
import com.example.ui.components.RegisterWorkerDialog
import com.example.ui.theme.KamzoGreen
import com.example.ui.theme.KamzoNavy
import com.example.ui.theme.KamzoOrange

@Composable
fun ProfileScreen(
    currentLanguage: String,
    bookings: List<WorkerBooking>,
    onSelectLanguage: (String) -> Unit,
    onRegisterWorker: (name: String, skill: String, area: String, exp: String, price: String, phone: String) -> Unit
) {
    var showRegisterDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var activeInfoDialog by remember { mutableStateOf<Pair<String, String>?>(null) }
    var registrationSuccessMsg by remember { mutableStateOf<String?>(null) }

    if (showRegisterDialog) {
        RegisterWorkerDialog(
            onDismiss = { showRegisterDialog = false },
            onSubmit = { name, skill, area, exp, price, phone ->
                onRegisterWorker(name, skill, area, exp, price, phone)
                registrationSuccessMsg = "Congratulations $name! You are registered as a $skill in KAMZO. Your listing is now live for nearby clients."
            }
        )
    }

    if (showLanguageDialog) {
        LanguageDialog(
            currentLanguage = currentLanguage,
            onSelectLanguage = onSelectLanguage,
            onDismiss = { showLanguageDialog = false }
        )
    }

    registrationSuccessMsg?.let { msg ->
        InfoDialog(
            title = "Provider Registered",
            message = msg,
            onDismiss = { registrationSuccessMsg = null }
        )
    }

    activeInfoDialog?.let { (title, msg) ->
        InfoDialog(
            title = title,
            message = msg,
            onDismiss = { activeInfoDialog = null }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_screen_scroll"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(KamzoNavy.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = KamzoNavy,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Welcome to KAMZO",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = KamzoNavy
                    )
                    Text(
                        text = "Har Zarurat Ka Hal, Bas Ek App Par",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Active Prototype • Ready for Service",
                        style = MaterialTheme.typography.bodySmall,
                        color = KamzoGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Active Bookings Summary (if any booked)
        if (bookings.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Assignment, contentDescription = null, tint = KamzoNavy)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Your Service Requests (${bookings.size})",
                                fontWeight = FontWeight.Bold,
                                color = KamzoNavy,
                                fontSize = 16.sp
                            )
                        }

                        bookings.forEach { b ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(KamzoNavy.copy(alpha = 0.05f))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(b.workerName, fontWeight = FontWeight.Bold)
                                        Text(b.status, color = KamzoGreen, fontWeight = FontWeight.SemiBold)
                                    }
                                    Text("${b.skill} • ${b.scheduledDate} (${b.timeSlot})", style = MaterialTheme.typography.bodySmall)
                                    Text("Address: ${b.customerAddress}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Menu Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ProfileMenuRow(
                        icon = Icons.Default.PersonAddAlt1,
                        title = "Register as a worker / mechanic",
                        subtitle = "Offer services and get jobs nearby",
                        testTag = "profile_register_worker_button",
                        onClick = { showRegisterDialog = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuRow(
                        icon = Icons.Default.StarOutline,
                        title = "Ratings and reviews",
                        subtitle = "Verified customer satisfaction rating guidelines",
                        testTag = "profile_ratings_button",
                        onClick = {
                            activeInfoDialog = Pair(
                                "Ratings & Reviews Policy",
                                "All ratings on KAMZO are generated only after verified job completion. Customers rate work quality, punctuality, and fair pricing. Inappropriate behavior triggers immediate review."
                            )
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuRow(
                        icon = Icons.Default.Translate,
                        title = "Language",
                        subtitle = currentLanguage,
                        testTag = "profile_language_button",
                        onClick = { showLanguageDialog = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuRow(
                        icon = Icons.Default.Payments,
                        title = "Commission and payments",
                        subtitle = "0% registration fee • Transparent earnings model",
                        testTag = "profile_commission_button",
                        onClick = {
                            activeInfoDialog = Pair(
                                "Commission & Payment Transparency",
                                "Workers keep the majority of what they earn. KAMZO charges zero sign-up fees. A minimal convenience fee is only collected on completed service matches to sustain emergency roadside dispatch."
                            )
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuRow(
                        icon = Icons.Default.PrivacyTip,
                        title = "Privacy and safety",
                        subtitle = "Data encryption, phone masking & support",
                        testTag = "profile_privacy_button",
                        onClick = {
                            activeInfoDialog = Pair(
                                "Privacy & Safety at KAMZO",
                                "Your contact numbers are protected. We adhere to high security standards, background verification for mechanics, and dedicated 24/7 helpline for roadside issues."
                            )
                        }
                    )
                }
            }
        }

        // Footer disclaimer
        item {
            Text(
                text = "This is a starter prototype. Do not use demo provider names, ratings or prices as real listings.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(KamzoNavy.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = KamzoNavy,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Open",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}
