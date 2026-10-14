package com.example.data.model

data class Job(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val pay: String,
    val type: String,
    val description: String = "Required immediately for ongoing project in the local area. Work schedule and terms to be settled with contractor.",
    val postedTime: String = "Today",
    val applicantsCount: Int = 3,
    val isApplied: Boolean = false
)

val initialJobs = listOf(
    Job(
        id = "j1",
        title = "Electrician needed",
        company = "Sharma & Sons Electricals",
        location = "Sector 18 Commercial Hub",
        pay = "₹700 - ₹900 / day",
        type = "Skilled work",
        description = "Requires certified wireman for new residential flat wiring and MCB box installation.",
        postedTime = "2 hours ago",
        applicantsCount = 4
    ),
    Job(
        id = "j2",
        title = "Construction helper",
        company = "Green City Developers",
        location = "Ring Road Phase 2",
        pay = "₹550 / day (cash daily)",
        type = "Daily wage",
        description = "Material handling, cement mixing, and site clearing. Lunch tea provided.",
        postedTime = "4 hours ago",
        applicantsCount = 12
    ),
    Job(
        id = "j3",
        title = "Painter for house project",
        company = "Royale Decorators",
        location = "Mayur Vihar Extension",
        pay = "Project based (₹18,000)",
        type = "Contract",
        description = "Full 3BHK interior putty, primer, and double coat plastic emulsion finish.",
        postedTime = "Yesterday",
        applicantsCount = 7
    ),
    Job(
        id = "j4",
        title = "Truck mechanic assistant",
        company = "National Transport Garage",
        location = "Transport Nagar Bypass",
        pay = "₹16,000 / month",
        type = "Full-time",
        description = "Brake overhaul, diesel filter change, and suspension repair for commercial fleet.",
        postedTime = "1 day ago",
        applicantsCount = 5
    ),
    Job(
        id = "j5",
        title = "Plumber for maintenance",
        company = "A-One Property Services",
        location = "Sector 62 Tech Park",
        pay = "₹8,000 / month (Part-time)",
        type = "Part-time",
        description = "Morning 3 hours daily maintenance check for washroom fixtures and water motor.",
        postedTime = "2 days ago",
        applicantsCount = 3
    )
)
