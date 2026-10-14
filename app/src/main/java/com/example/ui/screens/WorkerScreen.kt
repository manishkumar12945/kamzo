package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Worker
import com.example.ui.components.BookWorkerDialog
import com.example.ui.components.ContactWorkerDialog
import com.example.ui.components.InfoDialog
import com.example.ui.theme.KamzoBlue
import com.example.ui.theme.KamzoGreen
import com.example.ui.theme.KamzoNavy
import com.example.ui.theme.KamzoOrange

@Composable
fun WorkerScreen(
    workers: List<Worker>,
    query: String,
    category: String,
    onQueryChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onBookWorker: (Worker, String, String, String, String) -> Unit
) {
    val categories = listOf(
        "All",
        "Electrician",
        "Plumber",
        "Carpenter",
        "Painter",
        "Mason (Rajmistri)",
        "Helper / Labour",
        "Appliance Repair",
        "Mechanic"
    )

    val filteredWorkers by remember(workers, query, category) {
        derivedStateOf {
            workers.filter { w ->
                val matchesCat = category == "All" || w.skill.equals(category, ignoreCase = true)
                val q = query.trim().lowercase()
                val matchesQuery = q.isEmpty() ||
                        w.name.lowercase().contains(q) ||
                        w.skill.lowercase().contains(q) ||
                        w.area.lowercase().contains(q)
                matchesCat && matchesQuery
            }
        }
    }

    var selectedWorkerForContact by remember { mutableStateOf<Worker?>(null) }
    var selectedWorkerForBooking by remember { mutableStateOf<Worker?>(null) }
    var bookingSuccessDialog by remember { mutableStateOf<String?>(null) }

    selectedWorkerForContact?.let { worker ->
        ContactWorkerDialog(
            worker = worker,
            onDismiss = { selectedWorkerForContact = null }
        )
    }

    selectedWorkerForBooking?.let { worker ->
        BookWorkerDialog(
            worker = worker,
            onDismiss = { selectedWorkerForBooking = null },
            onConfirm = { date, slot, address, notes ->
                onBookWorker(worker, date, slot, address, notes)
                bookingSuccessDialog = "Service request booked with ${worker.name}! Scheduled for $date ($slot). The professional has been notified."
            }
        )
    }

    bookingSuccessDialog?.let { msg ->
        InfoDialog(
            title = "Booking Request Sent",
            message = msg,
            onDismiss = { bookingSuccessDialog = null }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("worker_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search Input
        item {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("worker_search_input"),
                placeholder = { Text("Search service, skill, or area") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = KamzoNavy
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KamzoNavy,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = category == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategoryChange(cat) },
                        label = { Text(cat, fontSize = 13.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = KamzoNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Results count
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredWorkers.size} providers available nearby",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Empty state
        if (filteredWorkers.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No providers found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try clearing your search query or selecting 'All' categories.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                onQueryChange("")
                                onCategoryChange("All")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = KamzoNavy)
                        ) {
                            Text("Reset Filters")
                        }
                    }
                }
            }
        }

        // Worker cards
        items(filteredWorkers, key = { it.id }) { worker ->
            WorkerItemCard(
                worker = worker,
                onContact = { selectedWorkerForContact = worker },
                onRequest = { selectedWorkerForBooking = worker }
            )
        }
    }
}

@Composable
private fun WorkerItemCard(
    worker: Worker,
    onContact: () -> Unit,
    onRequest: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("worker_card_${worker.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Avatar, Name, Skill, Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(KamzoBlue.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = KamzoNavy,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = worker.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (worker.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Provider",
                                tint = KamzoGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = worker.skill,
                        style = MaterialTheme.typography.bodyMedium,
                        color = KamzoBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Rating badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(KamzoOrange.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = KamzoOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = worker.rating.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = KamzoOrange
                    )
                }
            }

            // Experience and Location
            Text(
                text = "${worker.experience} • ${worker.area}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Pricing
            Text(
                text = worker.price,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = KamzoGreen
            )

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onContact,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("worker_contact_button_${worker.id}")
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Contact")
                }

                Button(
                    onClick = onRequest,
                    colors = ButtonDefaults.buttonColors(containerColor = KamzoNavy),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("worker_request_button_${worker.id}")
                ) {
                    Icon(Icons.Default.EventNote, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Request")
                }
            }
        }
    }
}
