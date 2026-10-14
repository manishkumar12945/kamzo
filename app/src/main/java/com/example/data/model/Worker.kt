package com.example.data.model

data class Worker(
    val id: String,
    val name: String,
    val skill: String,
    val area: String,
    val experience: String,
    val price: String,
    val rating: Double,
    val reviewsCount: Int = 42,
    val phone: String = "+91 98765 43210",
    val isVerified: Boolean = true
)

val initialWorkers = listOf(
    Worker(
        id = "w1",
        name = "Ravi Kumar",
        skill = "Electrician",
        area = "Sector 14 / Nearby area",
        experience = "5 years experience",
        price = "Visit fee from ₹199",
        rating = 4.8,
        reviewsCount = 94,
        phone = "+91 98111 22334"
    ),
    Worker(
        id = "w2",
        name = "Suresh Yadav",
        skill = "Plumber",
        area = "Market Road / Nearby area",
        experience = "7 years experience",
        price = "Visit fee from ₹149",
        rating = 4.6,
        reviewsCount = 78,
        phone = "+91 98222 33445"
    ),
    Worker(
        id = "w3",
        name = "Amit Sharma",
        skill = "Carpenter",
        area = "Civil Lines / Nearby area",
        experience = "4 years experience",
        price = "Quote after inspection",
        rating = 4.7,
        reviewsCount = 56,
        phone = "+91 98333 44556"
    ),
    Worker(
        id = "w4",
        name = "Imran Ansari",
        skill = "Painter",
        area = "Gandhi Nagar / Nearby area",
        experience = "6 years experience",
        price = "Quote after inspection",
        rating = 4.5,
        reviewsCount = 63,
        phone = "+91 98444 55667"
    ),
    Worker(
        id = "w5",
        name = "Mahesh Mahto",
        skill = "Mason (Rajmistri)",
        area = "Industrial Area / Nearby area",
        experience = "8 years experience",
        price = "Daily rate by agreement",
        rating = 4.8,
        reviewsCount = 112,
        phone = "+91 98555 66778"
    ),
    Worker(
        id = "w6",
        name = "Pintu Kumar",
        skill = "Helper / Labour",
        area = "Railway Colony / Nearby area",
        experience = "Available for daily work",
        price = "Daily rate by agreement",
        rating = 4.4,
        reviewsCount = 39,
        phone = "+91 98666 77889"
    ),
    Worker(
        id = "w7",
        name = "Neeraj Singh",
        skill = "Appliance Repair",
        area = "Central Plaza / Nearby area",
        experience = "3 years experience",
        price = "Visit fee from ₹199",
        rating = 4.5,
        reviewsCount = 47,
        phone = "+91 98777 88990"
    ),
    Worker(
        id = "w8",
        name = "Vikram Chauhan",
        skill = "Mechanic",
        area = "Highway Bypass / Nearby area",
        experience = "10 years experience",
        price = "Inspection from ₹249",
        rating = 4.9,
        reviewsCount = 135,
        phone = "+91 98888 99001"
    )
)
