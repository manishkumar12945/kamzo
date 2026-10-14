package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.CarRepair
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.KamzoViewModel
import com.example.ui.components.InfoDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JobScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RoadsideScreen
import com.example.ui.screens.WorkerScreen
import com.example.ui.theme.KamzoNavy
import com.example.ui.theme.KamzoOrange
import com.example.ui.theme.KamzoTheme

class MainActivity : ComponentActivity() {

    private val viewModel: KamzoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KamzoTheme {
                KamzoMainShell(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KamzoMainShell(viewModel: KamzoViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var showTopNotificationDialog by remember { mutableStateOf(false) }

    // Hardware / gesture back handling: return to Home tab if on another screen
    BackHandler(enabled = uiState.currentTab != 0) {
        viewModel.setTab(0)
    }

    if (showTopNotificationDialog) {
        InfoDialog(
            title = "Notifications",
            message = "Notifications are active. You will receive updates about worker bookings, new local jobs, and roadside mechanic dispatch status.",
            onDismiss = { showTopNotificationDialog = false }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationSearching,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "KAMZO",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showTopNotificationDialog = true },
                        modifier = Modifier.testTag("topbar_notification_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KamzoNavy,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = uiState.currentTab == 0,
                    onClick = { viewModel.setTab(0) },
                    icon = {
                        Icon(
                            if (uiState.currentTab == 0) Icons.Default.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KamzoNavy,
                        selectedTextColor = KamzoNavy,
                        indicatorColor = KamzoNavy.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_home")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == 1,
                    onClick = { viewModel.setTab(1) },
                    icon = {
                        Icon(
                            if (uiState.currentTab == 1) Icons.Default.Handyman else Icons.Outlined.Handyman,
                            contentDescription = "Workers"
                        )
                    },
                    label = { Text("Workers", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KamzoNavy,
                        selectedTextColor = KamzoNavy,
                        indicatorColor = KamzoNavy.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_workers")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == 2,
                    onClick = { viewModel.setTab(2) },
                    icon = {
                        Icon(
                            if (uiState.currentTab == 2) Icons.Default.Work else Icons.Outlined.WorkOutline,
                            contentDescription = "Jobs"
                        )
                    },
                    label = { Text("Jobs", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KamzoNavy,
                        selectedTextColor = KamzoNavy,
                        indicatorColor = KamzoNavy.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_jobs")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == 3,
                    onClick = { viewModel.setTab(3) },
                    icon = {
                        Icon(
                            if (uiState.currentTab == 3) Icons.Default.CarRepair else Icons.Outlined.CarRepair,
                            contentDescription = "Roadside"
                        )
                    },
                    label = { Text("Roadside", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KamzoOrange,
                        selectedTextColor = KamzoOrange,
                        indicatorColor = KamzoOrange.copy(alpha = 0.14f)
                    ),
                    modifier = Modifier.testTag("nav_roadside")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == 4,
                    onClick = { viewModel.setTab(4) },
                    icon = {
                        Icon(
                            if (uiState.currentTab == 4) Icons.Default.Person else Icons.Outlined.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text("Profile", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KamzoNavy,
                        selectedTextColor = KamzoNavy,
                        indicatorColor = KamzoNavy.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_profile")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                0 -> HomeScreen(
                    activeRoadsideRequest = uiState.activeRoadsideRequest,
                    onNavigateToTab = { index -> viewModel.setTab(index) },
                    onFilterWorkerCategory = { cat -> viewModel.setWorkerCategory(cat) }
                )
                1 -> WorkerScreen(
                    workers = uiState.workers,
                    query = uiState.workerQuery,
                    category = uiState.workerCategory,
                    onQueryChange = { q -> viewModel.setWorkerQuery(q) },
                    onCategoryChange = { c -> viewModel.setWorkerCategory(c) },
                    onBookWorker = { worker, date, slot, address, notes ->
                        viewModel.bookWorker(worker, date, slot, address, notes)
                    }
                )
                2 -> JobScreen(
                    jobs = uiState.jobs,
                    query = uiState.jobQuery,
                    filterType = uiState.jobTypeFilter,
                    onQueryChange = { q -> viewModel.setJobQuery(q) },
                    onFilterTypeChange = { t -> viewModel.setJobTypeFilter(t) },
                    onPostJob = { title, comp, pay, type, loc, desc ->
                        viewModel.postJob(title, comp, pay, type, loc, desc)
                    },
                    onApplyJob = { jobId -> viewModel.applyToJob(jobId) }
                )
                3 -> RoadsideScreen(
                    activeRequest = uiState.activeRoadsideRequest,
                    onSubmitRequest = { veh, iss, loc, det ->
                        viewModel.submitRoadsideRequest(veh, iss, loc, det)
                    },
                    onCancelRequest = { viewModel.cancelRoadsideRequest() },
                    onCompleteRequest = { viewModel.markRoadsideCompleted() }
                )
                4 -> ProfileScreen(
                    currentLanguage = uiState.appLanguage,
                    bookings = uiState.bookings,
                    onSelectLanguage = { lang -> viewModel.setLanguage(lang) },
                    onRegisterWorker = { name, skill, area, exp, price, phone ->
                        viewModel.registerWorker(name, skill, area, exp, price, phone)
                    }
                )
            }
        }
    }
}
