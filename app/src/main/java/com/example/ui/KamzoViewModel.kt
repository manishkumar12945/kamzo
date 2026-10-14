package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Job
import com.example.data.model.RoadsideRequest
import com.example.data.model.RoadsideStatus
import com.example.data.model.Worker
import com.example.data.model.WorkerBooking
import com.example.data.model.initialJobs
import com.example.data.model.initialWorkers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class KamzoUiState(
    val currentTab: Int = 0,
    val workers: List<Worker> = initialWorkers,
    val workerQuery: String = "",
    val workerCategory: String = "All",
    val jobs: List<Job> = initialJobs,
    val jobQuery: String = "",
    val jobTypeFilter: String = "All",
    val activeRoadsideRequest: RoadsideRequest? = null,
    val bookings: List<WorkerBooking> = emptyList(),
    val appLanguage: String = "English",
    val userNotificationMessage: String? = null
)

class KamzoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(KamzoUiState())
    val uiState: StateFlow<KamzoUiState> = _uiState.asStateFlow()

    fun setTab(index: Int) {
        _uiState.update { it.copy(currentTab = index) }
    }

    fun setWorkerQuery(query: String) {
        _uiState.update { it.copy(workerQuery = query) }
    }

    fun setWorkerCategory(category: String) {
        _uiState.update { it.copy(workerCategory = category) }
    }

    fun setJobQuery(query: String) {
        _uiState.update { it.copy(jobQuery = query) }
    }

    fun setJobTypeFilter(type: String) {
        _uiState.update { it.copy(jobTypeFilter = type) }
    }

    fun setLanguage(lang: String) {
        _uiState.update { it.copy(appLanguage = lang) }
    }

    fun postJob(title: String, company: String, pay: String, type: String, location: String, details: String) {
        val newJob = Job(
            id = "job_${UUID.randomUUID()}",
            title = title,
            company = company.ifBlank { "Direct Client" },
            location = location.ifBlank { "Local Area" },
            pay = pay,
            type = type,
            description = details.ifBlank { "Urgent requirement. Contact employer to arrange interview and start date." },
            postedTime = "Just now",
            applicantsCount = 0
        )
        _uiState.update { it.copy(jobs = listOf(newJob) + it.jobs) }
    }

    fun applyToJob(jobId: String) {
        _uiState.update { state ->
            val updatedJobs = state.jobs.map { j ->
                if (j.id == jobId) {
                    j.copy(isApplied = true, applicantsCount = j.applicantsCount + 1)
                } else j
            }
            state.copy(jobs = updatedJobs)
        }
    }

    fun bookWorker(
        worker: Worker,
        date: String,
        timeSlot: String,
        address: String,
        notes: String
    ) {
        val booking = WorkerBooking(
            id = "bk_${UUID.randomUUID().toString().take(8)}",
            workerId = worker.id,
            workerName = worker.name,
            skill = worker.skill,
            scheduledDate = date,
            timeSlot = timeSlot,
            customerAddress = address,
            problemNote = notes,
            estimatedFee = worker.price
        )
        _uiState.update { it.copy(bookings = listOf(booking) + it.bookings) }
    }

    fun registerWorker(
        name: String,
        skill: String,
        area: String,
        experience: String,
        price: String,
        phone: String
    ) {
        val newWorker = Worker(
            id = "w_${UUID.randomUUID().toString().take(6)}",
            name = name,
            skill = skill,
            area = area,
            experience = "$experience years experience",
            price = price,
            rating = 5.0,
            reviewsCount = 1,
            phone = phone,
            isVerified = true
        )
        _uiState.update { it.copy(workers = listOf(newWorker) + it.workers) }
    }

    fun submitRoadsideRequest(
        vehicle: String,
        issue: String,
        location: String,
        details: String
    ) {
        val newRequest = RoadsideRequest(
            id = "RS-${(1000..9999).random()}",
            vehicle = vehicle,
            issue = issue,
            location = location,
            details = details,
            status = RoadsideStatus.SEARCHING,
            assignedMechanicName = "Manoj Roadside Auto Garage",
            assignedMechanicPhone = "+91 98990 12345",
            etaMinutes = 18,
            distanceKm = 2.1
        )
        _uiState.update { it.copy(activeRoadsideRequest = newRequest) }

        // Simulate rapid mechanic dispatch update
        viewModelScope.launch {
            delay(2500)
            _uiState.update { state ->
                state.activeRoadsideRequest?.let { r ->
                    state.copy(activeRoadsideRequest = r.copy(status = RoadsideStatus.ASSIGNED))
                } ?: state
            }
            delay(3500)
            _uiState.update { state ->
                state.activeRoadsideRequest?.let { r ->
                    state.copy(activeRoadsideRequest = r.copy(status = RoadsideStatus.EN_ROUTE, etaMinutes = 15))
                } ?: state
            }
        }
    }

    fun cancelRoadsideRequest() {
        _uiState.update { it.copy(activeRoadsideRequest = null) }
    }

    fun markRoadsideCompleted() {
        _uiState.update { state ->
            state.activeRoadsideRequest?.let { r ->
                state.copy(activeRoadsideRequest = r.copy(status = RoadsideStatus.COMPLETED))
            } ?: state
        }
    }
}
