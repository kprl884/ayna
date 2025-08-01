# Ayna App - Clean Architecture Implementation

## Overview

This implementation follows Clean Architecture principles with clear separation of concerns across three layers:

### 🏗️ Architecture Layers

#### 1. Domain Layer (`domain/`)
- **Models**: Core business entities (`SalonV2`, `Appointment`, `UserProfile`, etc.)
- **Repositories**: Interfaces defining data access contracts
- **Use Cases**: Business logic encapsulation with single responsibility
- **Utils**: Shared utilities like `Resource<T>` wrapper

#### 2. Data Layer (`data/`)
- **Repository Implementations**: Mock implementations with realistic data
- **Network delay simulation**: All repositories include `delay()` for realistic UX testing

#### 3. Presentation Layer (`presentation/`)
- **ViewModels**: UI state management using `StateFlow`
- **UI State Classes**: Immutable data classes representing screen states

### 🔄 Data Flow

```
UI Screen → ViewModel → Use Case → Repository → Data Source
    ↑                                              ↓
    └── StateFlow ← Resource<T> ← Flow ← Response ←┘
```

### 📦 Key Components

#### Resource Wrapper
```kotlin
sealed class Resource<T> {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String) : Resource<T>(message = message)
    class Loading<T> : Resource<T>()
}
```

#### Use Case Pattern
```kotlin
class GetRecommendedSalonsUseCase(private val repository: SalonRepositoryV2) {
    operator fun invoke(): Flow<Resource<List<SalonV2>>> = flow {
        emit(Resource.Loading())
        try {
            val salons = repository.getRecommendedSalons()
            emit(Resource.Success(salons))
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}
```

#### ViewModel Integration
```kotlin
class HomeViewModelV2(private val getRecommendedSalonsUseCase: GetRecommendedSalonsUseCase) {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    fun loadRecommendedSalons() {
        viewModelScope.launch {
            getRecommendedSalonsUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = HomeUiState(isLoading = true)
                    is Resource.Success -> _uiState.value = HomeUiState(salons = result.data ?: emptyList())
                    is Resource.Error -> _uiState.value = HomeUiState(error = result.message)
                }
            }
        }
    }
}
```

### 🚀 Usage Example

```kotlin
// In your Compose screen
@Composable
fun HomeScreen() {
    val viewModel = DataModule.createHomeViewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    when {
        uiState.isLoading -> LoadingIndicator()
        uiState.error != null -> ErrorMessage(uiState.error)
        else -> SalonList(uiState.salons)
    }
}
```

### 🧪 Mock Data

The implementation includes realistic mock data for:
- **4 Salons** with Turkish names and Istanbul locations
- **8 Employees** with specialties and ratings
- **12 Services** with realistic pricing in Turkish Lira
- **4 Appointments** in different states (upcoming, completed, cancelled)
- **1 User Profile** with payment methods and loyalty points

### 🔧 Benefits

- ✅ **Testable**: Each layer can be tested independently
- ✅ **Maintainable**: Clear separation of concerns
- ✅ **Scalable**: Easy to add new features or change implementations
- ✅ **Type-Safe**: Strong typing throughout with null safety
- ✅ **Reactive**: Real-time UI updates with StateFlow
- ✅ **Error Handling**: Consistent error handling with Resource wrapper
- ✅ **Immutable**: Data classes promote immutability and thread safety

### 📱 Production Ready

This foundation provides:
- Proper error handling and loading states
- Network simulation for realistic testing
- Type-safe navigation between screens
- Consistent API patterns across all features
- MVVM architecture with unidirectional data flow
