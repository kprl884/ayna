package com.techtactoe.ayna.di

import com.techtactoe.ayna.data.auth.SupabaseAuthManager
import com.techtactoe.ayna.data.realtime.SupabaseRealtimeManager
import com.techtactoe.ayna.data.repository.SupabaseAppointmentRepositoryImpl
import com.techtactoe.ayna.data.repository.SupabaseExploreRepositoryImpl
import com.techtactoe.ayna.data.repository.SupabaseProfileRepositoryImpl
import com.techtactoe.ayna.data.repository.SupabaseSalonRepositoryImpl
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import com.techtactoe.ayna.domain.repository.ProfileRepository
import com.techtactoe.ayna.domain.repository.SalonRepository
import com.techtactoe.ayna.domain.usecase.*
import com.techtactoe.ayna.presentation.ui.screens.appointments.AppointmentsViewModel
import com.techtactoe.ayna.presentation.ui.screens.booking.BookingConfirmationViewModel
import com.techtactoe.ayna.presentation.ui.screens.explore.ExploreViewModel
import com.techtactoe.ayna.presentation.ui.screens.home.HomeViewModel
import com.techtactoe.ayna.presentation.ui.screens.map.MapViewModel
import com.techtactoe.ayna.presentation.ui.screens.notifications.NotificationsViewModel
import com.techtactoe.ayna.presentation.ui.screens.salon.SalonDetailViewModel
import com.techtactoe.ayna.presentation.ui.screens.selecttime.SelectTimeViewModel
import com.techtactoe.ayna.presentation.ui.screens.waitlist.JoinWaitlistViewModel
import org.koin.dsl.module

/**
 * Koin dependency injection module for Supabase backend
 * Replaces the mock DataModule with real Supabase implementations
 */
val supabaseModule = module {

    // Authentication
    single { SupabaseAuthManager() }

    // Real-time manager
    single { SupabaseRealtimeManager() }

    // Repository implementations
    single<SalonRepository> { SupabaseSalonRepositoryImpl() }
    single<AppointmentRepository> { SupabaseAppointmentRepositoryImpl() }
    single<ProfileRepository> { SupabaseProfileRepositoryImpl() }
    single { SupabaseExploreRepositoryImpl() }

    // Use cases
    single { GetRecommendedSalonsUseCase(get()) }
    single { GetNearbySalonsUseCase(get()) }
    single { GetSalonDetailsUseCase(get()) }
    single { SearchSalonsUseCase(get()) }
    single { GetUserAppointmentsUseCase(get()) }
    single { BookAppointmentUseCase(get()) }
    single { CancelAppointmentUseCase(get()) }
    single { GetUserProfileUseCase(get()) }
    single { GetAvailableTimeSlotsUseCase(get()) }
    single { JoinWaitlistUseCase(get()) }
    single { CreateAppointmentUseCase(get()) }
    single { ValidateSlotSelectionUseCase() }

    // ViewModels with Supabase dependencies
    factory { HomeViewModel(get(), get()) }
    factory { AppointmentsViewModel(get()) }
    factory { SelectTimeViewModel(get(), get(), get()) }
    factory { JoinWaitlistViewModel(get()) }
    factory { (salonId: String) -> SalonDetailViewModel(salonId) }
    factory { BookingConfirmationViewModel() }
    factory { NotificationsViewModel() }
    factory { MapViewModel() }
    factory { ExploreViewModel(get()) } // Updated to use Supabase repository
}

/**
 * Updated DataModule that uses Supabase instead of mocks
 * This maintains compatibility with existing code while switching to real backend
 */
object SupabaseDataModule {

    // Use cases with Supabase repositories
    private val salonRepository: SalonRepository by lazy { SupabaseSalonRepositoryImpl() }
    private val appointmentRepository: AppointmentRepository by lazy { SupabaseAppointmentRepositoryImpl() }
    private val profileRepository: ProfileRepository by lazy { SupabaseProfileRepositoryImpl() }
    private val exploreRepository: SupabaseExploreRepositoryImpl by lazy { SupabaseExploreRepositoryImpl() }

    // Authentication and real-time managers
    val authManager: SupabaseAuthManager by lazy { SupabaseAuthManager() }
    val realtimeManager: SupabaseRealtimeManager by lazy { SupabaseRealtimeManager() }

    // Use case instances
    val getRecommendedSalonsUseCase = GetRecommendedSalonsUseCase(salonRepository)
    val getNearbySalonsUseCase = GetNearbySalonsUseCase(salonRepository)
    val getSalonDetailsUseCase = GetSalonDetailsUseCase(salonRepository)
    val searchSalonsUseCase = SearchSalonsUseCase(salonRepository)
    val getUserAppointmentsUseCase = GetUserAppointmentsUseCase(appointmentRepository)
    val bookAppointmentUseCase = BookAppointmentUseCase(appointmentRepository)
    val cancelAppointmentUseCase = CancelAppointmentUseCase(appointmentRepository)
    val getUserProfileUseCase = GetUserProfileUseCase(profileRepository)
    val getAvailableTimeSlotsUseCase = GetAvailableTimeSlotsUseCase(appointmentRepository)
    val joinWaitlistUseCase = JoinWaitlistUseCase(appointmentRepository)
    val createAppointmentUseCase = CreateAppointmentUseCase(appointmentRepository)
    val validateSlotSelectionUseCase = ValidateSlotSelectionUseCase()

    // ViewModel factory functions with Supabase backend
    fun createHomeViewModel(): HomeViewModel {
        return HomeViewModel(getRecommendedSalonsUseCase, getNearbySalonsUseCase)
    }

    fun createAppointmentsViewModel(): AppointmentsViewModel {
        return AppointmentsViewModel(getUserAppointmentsUseCase)
    }

    fun createSelectTimeViewModel(): SelectTimeViewModel {
        return SelectTimeViewModel(getAvailableTimeSlotsUseCase, createAppointmentUseCase, validateSlotSelectionUseCase)
    }

    fun createJoinWaitlistViewModel(): JoinWaitlistViewModel {
        return JoinWaitlistViewModel(joinWaitlistUseCase)
    }

    fun createSalonDetailViewModel(salonId: String): SalonDetailViewModel {
        return SalonDetailViewModel(salonId)
    }

    fun createBookingConfirmationViewModel(): BookingConfirmationViewModel {
        return BookingConfirmationViewModel()
    }

    fun createNotificationsViewModel(): NotificationsViewModel {
        return NotificationsViewModel()
    }

    fun createMapViewModel(): MapViewModel {
        return MapViewModel()
    }

    fun createExploreViewModel(): ExploreViewModel {
        return ExploreViewModel(exploreRepository)
    }
}