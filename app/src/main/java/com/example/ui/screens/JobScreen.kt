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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import com.example.data.model.Job
import com.example.ui.components.ApplyJobDialog
import com.example.ui.components.InfoDialog
import com.example.ui.components.PostJobDialog
import com.example.ui.theme.KamzoGreen
import com.example.ui.theme.KamzoNavy
import com.example.ui.theme.KamzoOrange

@Composable
fun JobScreen(
    jobs: List<Job>,
    query: String,
    filterType: String,
    onQueryChange: (String) -> Unit,
    onFilterTypeChange: (String) -> Unit,
    onPostJob: (title: String, company: String, pay: String, type: String, location: String, details: String) -> Unit,
    onApplyJob: (String) -> Unit
) {
    var showPostJobDialog by remember { mutableStateOf(false) }
    var applyingJob by remember { mutableStateOf<Job?>(null) }
    var applicationSubmittedDialog by remember { mutableStateOf<String?>(null) }

    val filterTypes = listOf("All", "Daily wage", "Skilled work", "Contract", "Full-time", "Part-time")

    val filteredJobs by remember(jobs, query, filterType) {
        derivedStateOf {
            jobs.filter { j ->
                val matchesType = filterType == "All" || j.type.equals(filterType, ignoreCase = true)
                val q = query.trim().lowercase()
                val matchesQuery = q.isEmpty() ||
                        j.title.lowercase().contains(q) ||
                        j.company.lowercase().contains(q) ||
                        j.location.lowercase().contains(q) ||
                        j.type.lowercase().contains(q)
                matchesType && matchesQuery
            }
        }
    }

    if (showPostJobDialog) {
        PostJobDialog(
            onDismiss = { showPostJobDialog = false },
            onSubmit = { title, company, pay, type, location, details ->
                onPostJob(title, company, pay, type, location, details)
                applicationSubmittedDialog = "Job \"$title\" has been published to the local jobs feed!"
            }
        )
    }

    applyingJob?.let { job ->
        ApplyJobDialog(
            job = job,
            onDismiss = { applyingJob = null },
            onConfirm = {
                onApplyJob(job.id)
                applicationSubmittedDialog = "Your application for \"${job.title}\" has been submitted to ${job.company}. They will reach out via phone."
            }
        )
    }

    applicationSubmittedDialog?.let { msg ->
        InfoDialog(
            title = "Job Update",
            message = msg,
            onDismiss = { applicationSubmittedDialog = null }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showPostJobDialog = true },
                containerColor = KamzoNavy,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("post_job_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Post a Job")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Post a Job", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("job_screen_list"),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Search field
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("job_search_input"),
                    placeholder = { Text("Search jobs, contractors, or skills") },
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
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
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

            // Job Type Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterTypes.forEach { type ->
                        val isSelected = filterType == type
                        FilterChip(
                            selected = isSelected,
                            onClick = { onFilterTypeChange(type) },
                            label = { Text(type, fontSize = 13.sp) },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = KamzoNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Title & Count
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Local opportunities",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = KamzoNavy
                    )
                    Text(
                        text = "${filteredJobs.size} listings",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Empty state
            if (filteredJobs.isEmpty()) {
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
                                Icons.Default.Work,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No matching jobs found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Be the first to post a job opportunity or reset search filters.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Job Items
            items(filteredJobs, key = { it.id }) { job ->
                JobItemCard(
                    job = job,
                    onApply = { applyingJob = job }
                )
            }

            // Footer disclaimer
            item {
                Text(
                    text = "Demo listings only. Real vacancies must be supplied by verified employers.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun JobItemCard(
    job: Job,
    onApply: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("job_card_${job.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(KamzoGreen.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = KamzoGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = job.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${job.company} • ${job.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Job type pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(KamzoNavy.copy(alpha = 0.08f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = job.type,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = KamzoNavy
                    )
                }
            }

            Text(
                text = job.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Pay & Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = job.pay,
                        fontWeight = FontWeight.Bold,
                        color = KamzoGreen,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "${job.applicantsCount} applicants • ${job.postedTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (job.isApplied) {
                    Button(
                        onClick = {},
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(disabledContainerColor = KamzoGreen.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = KamzoGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Applied", color = KamzoGreen, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = onApply,
                        colors = ButtonDefaults.buttonColors(containerColor = KamzoNavy),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("job_apply_button_${job.id}")
                    ) {
                        Text("Apply Now")
                    }
                }
            }
        }
    }
}
