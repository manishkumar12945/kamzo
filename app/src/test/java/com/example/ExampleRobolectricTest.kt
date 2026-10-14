package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.KamzoViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("KAMZO", appName)
  }

  @Test
  fun `test viewModel initial state and actions`() {
    val vm = KamzoViewModel()
    val initialState = vm.uiState.value
    assertEquals(0, initialState.currentTab)
    assertTrue(initialState.workers.isNotEmpty())
    assertTrue(initialState.jobs.isNotEmpty())

    // Switch tab
    vm.setTab(1)
    assertEquals(1, vm.uiState.value.currentTab)

    // Filter workers
    vm.setWorkerQuery("Plumber")
    assertEquals("Plumber", vm.uiState.value.workerQuery)

    // Post job
    val countBefore = vm.uiState.value.jobs.size
    vm.postJob(
        title = "AC Technician Needed",
        company = "Cool Air Co",
        pay = "₹1000/day",
        type = "Skilled work",
        location = "Sector 18",
        details = "Split AC repair"
    )
    assertEquals(countBefore + 1, vm.uiState.value.jobs.size)

    // Submit Roadside request
    vm.submitRoadsideRequest(
        vehicle = "Car / SUV",
        issue = "Battery / jump-start",
        location = "Expressway km 14",
        details = "Car won't crank"
    )
    val request = vm.uiState.value.activeRoadsideRequest
    assertNotNull(request)
    assertEquals("Car / SUV", request?.vehicle)
  }
}
