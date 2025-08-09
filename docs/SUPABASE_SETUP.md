# Supabase Backend Setup Guide for Ayna App

## Overview

This guide will help you set up the complete Supabase backend for the Ayna salon booking app. The backend includes authentication, real-time features, geographic queries, and comprehensive data management.

## Prerequisites

1. **Supabase Account**: Create a free account at [supabase.com](https://supabase.com)
2. **Project Setup**: Create a new Supabase project
3. **Environment Variables**: You'll need your project URL and anon key

## Step 1: Database Setup

### 1.1 Run Initial Schema Migration

In your Supabase SQL Editor, run the migration files in order:

1. `supabase/migrations/create_initial_schema.sql`
2. `supabase/migrations/create_geographic_functions.sql`
3. `supabase/migrations/seed_initial_data.sql`

### 1.2 Enable Required Extensions

Ensure these extensions are enabled in your Supabase project:
- `uuid-ossp` (for UUID generation)
- `postgis` (for geographic queries)

## Step 2: Authentication Configuration

### 2.1 Configure Auth Settings

In your Supabase Dashboard → Authentication → Settings:

- **Site URL**: Set to your app's URL
- **Email Confirmation**: Disabled (for development)
- **Email Templates**: Customize as needed

### 2.2 Configure Providers

Enable Email provider in Authentication → Providers:
- Email: Enabled
- Confirm email: Disabled (for development)

## Step 3: Environment Configuration

### 3.1 Update Supabase Client

In `composeApp/src/commonMain/kotlin/com/techtactoe/ayna/data/supabase/SupabaseClient.kt`:

```kotlin
private const val SUPABASE_URL = "https://your-project.supabase.co"
private const val SUPABASE_ANON_KEY = "your-anon-key"
```

### 3.2 Add Dependencies

The required Supabase dependencies have been added to `build.gradle.kts`:
- `postgrest-kt` for database operations
- `gotrue-kt` for authentication
- `realtime-kt` for live updates
- `storage-kt` for file uploads

## Step 4: Switch to Supabase Backend

### 4.1 Update Dependency Injection

Replace the mock DataModule usage with SupabaseDataModule:

In your ViewModels and screens, replace:
```kotlin
DataModule.createHomeViewModel()
```

With:
```kotlin
SupabaseDataModule.createHomeViewModel()
```

### 4.2 Update Koin Configuration

In `composeApp/src/commonMain/kotlin/com/techtactoe/ayna/di/initKoin.kt`:

```kotlin
fun initKoin() {
    startKoin {
        modules(
            supabaseModule, // Replace dataModule with supabaseModule
            viewModelModule,
        )
    }
}
```

## Step 5: Features Overview

### 5.1 Implemented Features

✅ **User Authentication**
- Email/password sign up and sign in
- Session management
- Password reset functionality

✅ **Salon Management**
- Complete salon CRUD operations
- Geographic search within radius
- Image gallery management
- Operating hours tracking

✅ **Appointment System**
- Time slot generation and management
- Booking creation and cancellation
- Appointment status tracking
- Conflict detection

✅ **User Profiles**
- Profile management
- Payment method storage
- Favorite salons tracking
- Loyalty points system

✅ **Reviews & Ratings**
- User review submission
- Automatic rating calculations
- Review moderation capabilities

✅ **Waitlist System**
- Join waitlist when fully booked
- Automatic notifications when slots open
- Preference-based matching

✅ **Real-time Updates**
- Live appointment updates
- Instant notifications
- Availability changes
- Waitlist status updates

✅ **Geographic Features**
- PostGIS-powered location search
- Distance calculations
- Radius-based salon discovery

## Step 6: Database Functions

### 6.1 Geographic Functions

- `salons_within_radius(lat, lng, radius_km)`: Find salons within specified radius
- `calculate_distance(lat1, lng1, lat2, lng2)`: Calculate distance between points

### 6.2 Utility Functions

- `generate_time_slots()`: Automatically generate available time slots
- `update_salon_statistics()`: Recalculate ratings and review counts
- `cleanup_old_time_slots()`: Remove expired time slots
- `expire_old_waitlist_requests()`: Mark old waitlist requests as expired

## Step 7: Row Level Security (RLS)

### 7.1 Security Policies

All tables have RLS enabled with appropriate policies:

- **User Data**: Users can only access their own data
- **Public Data**: Salons, services, and reviews are publicly readable
- **Admin Data**: Salon owners can manage their salon data
- **Audit Trail**: All changes are tracked with timestamps

### 7.2 Performance Optimizations

- **Indexes**: Strategic indexes on frequently queried columns
- **Full-text Search**: Optimized search across salons and services
- **Geographic Indexes**: GIST indexes for location-based queries
- **Composite Indexes**: Multi-column indexes for complex queries

## Step 8: Testing the Backend

### 8.1 Test Data

The seed migration includes realistic test data:
- 5 salons in Istanbul and Nicosia
- 20+ services across different categories
- 9 employees with specialties
- Sample opening hours and images

### 8.2 API Testing

Test the following endpoints:
1. **Authentication**: Sign up, sign in, sign out
2. **Salon Discovery**: Search, filter, geographic queries
3. **Appointment Booking**: Create, cancel, reschedule
4. **Real-time Updates**: Subscribe to live changes

## Step 9: Production Considerations

### 9.1 Performance Monitoring

- Enable Supabase Analytics
- Monitor query performance
- Set up alerts for slow queries
- Track API usage and limits

### 9.2 Backup Strategy

- Enable automated backups
- Test restore procedures
- Document recovery processes
- Set up monitoring alerts

### 9.3 Scaling Considerations

- Monitor database connections
- Implement connection pooling
- Consider read replicas for heavy read workloads
- Plan for horizontal scaling

## Step 10: Troubleshooting

### Common Issues:

1. **Connection Errors**: Check URL and API keys
2. **RLS Violations**: Verify user authentication and policies
3. **Geographic Queries**: Ensure PostGIS extension is enabled
4. **Real-time Issues**: Check WebSocket connections and subscriptions

### Debug Tools:

- Supabase Dashboard → Logs
- Database → Query Performance
- Authentication → Users and Sessions
- Real-time → Connections and Messages

## Next Steps

1. **Deploy Edge Functions**: For complex business logic
2. **Set up Storage**: For user profile pictures and salon images
3. **Configure CDN**: For optimal image delivery
4. **Implement Analytics**: Track user behavior and app performance
5. **Add Monitoring**: Set up alerts and health checks

## Support

For issues or questions:
1. Check Supabase documentation
2. Review the implementation code
3. Test with the provided sample data
4. Monitor logs for detailed error messages

The backend is now production-ready with enterprise-grade features including authentication, real-time updates, geographic search, and comprehensive data management.