package com.example.data.model

enum class RoadsideStatus {
    SEARCHING,
    ASSIGNED,
    EN_ROUTE,
    ARRIVED,
    COMPLETED,
    CANCELLED
}

data class RoadsideRequest(
    val id: String,
    val vehicle: String,
    val issue: String,
    val location: String,
    val details: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: RoadsideStatus = RoadsideStatus.ASSIGNED,
    val assignedMechanicName: String = "Manoj Roadside Auto Garage",
    val assignedMechanicPhone: String = "+91 98990 12345",
    val etaMinutes: Int = 18,
    val distanceKm: Double = 2.4
)

data class WorkerBooking(
    val id: String,
    val workerId: String,
    val workerName: String,
    val skill: String,
    val scheduledDate: String,
    val timeSlot: String,
    val customerAddress: String,
    val problemNote: String,
    val estimatedFee: String,
    val status: String = "Confirmed"
)
