# Ayna Font System Documentation

## Overview

Ayna font sistemi, Compose Multiplatform için enterprise-level bir tipografi çözümüdür. 5 farklı font ailesi, 5 farklı font ağırlığı ve platform-specific implementasyonlar içerir.

## Font Families

### 1. Roboto (System Default)
- **Kullanım**: Ratings, numbers, system text
- **Ağırlıklar**: Light (300), Regular (400), Medium (500), SemiBold (600), Bold (700)
- **Platform**: Android (native), iOS (SF Pro Text fallback)

### 2. Bodoni Moda (Elegant Serif)
- **Kullanım**: Business names, premium content
- **Ağırlıklar**: Light (300), Regular (400), Medium (500), SemiBold (600), Bold (700)
- **Platform**: Android (custom), iOS (Didot fallback)

### 3. Inter (Modern Sans-Serif)
- **Kullanım**: Descriptions, body text, navigation
- **Ağırlıklar**: Light (300), Regular (400), Medium (500), SemiBold (600), Bold (700)
- **Platform**: Android (custom), iOS (SF Pro Text fallback)

### 4. Poppins (Friendly Rounded)
- **Kullanım**: Category headers, buttons
- **Ağırlıklar**: Light (300), Regular (400), Medium (500), SemiBold (600), Bold (700)
- **Platform**: Android (custom), iOS (SF Pro Text fallback)

### 5. Playfair Display (Elegant Display)
- **Kullanım**: App title, branding, large headlines
- **Ağırlıklar**: Light (300), Regular (400), Medium (500), SemiBold (600), Bold (700)
- **Platform**: Android (custom), iOS (Georgia fallback)

## Architecture

### File Structure
```
commonMain/
├── designsystem/
│   ├── foundation/
│   │   └── typography/
│   │       ├── FontFamilies.kt      # Font family definitions
│   │       ├── Typography.kt        # Material Design 3 typography
│   │       └── TextStyles.kt        # Semantic text styles
│   └── components/
│       └── text/
│           └── AppText.kt           # Text composables
├── androidMain/
│   └── FontProvider.android.kt      # Android font loading
└── iosMain/
    └── FontProvider.ios.kt          # iOS font loading
```

### Core Components

#### 1. FontFamilies.kt
```kotlin
object AppFontFamilies {
    val roboto: FontFamily by lazy {  }
    val bodoniModa: FontFamily by lazy {  }
    val inter: FontFamily by lazy {  }
    val poppins: FontFamily by lazy {  }
    val playfairDisplay: FontFamily by lazy {  }
}
```

#### 2. TextStyles.kt
```kotlin
object AppTextStyles {
    val appTitle = TextStyle()
    val categoryTitle = TextStyle()
    val businessName = TextStyle()
    val bodyText = TextStyle()
    // ... more styles
}
```

#### 3. AppText.kt
```kotlin
@Composable
fun AppText(
    text: String,
    style: TextStyle = AppTextStyles.bodyText,
    // ... other parameters
)
```

## Usage Examples

### Basic Text Usage
```kotlin
// Using semantic text styles
AppTitle(text = "Ayna")
CategoryTitle(text = "Kategoriler")
BusinessName(text = "Pink Ivy Salon")
BodyText(text = "Güzellik ve wellness hizmetleri")
```

### Custom Styling
```kotlin
AppText(
    text = "Custom styled text",
    style = AppTextStyles.bodyText.copy(
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.primary
    )
)
```

### Button Text
```kotlin
ButtonText(
    text = "Rezervasyon Yap",
    color = MaterialTheme.colorScheme.onPrimary
)
```

### Navigation Text
```kotlin
NavigationText(
    text = "Ana Sayfa",
    isSelected = true,
    color = MaterialTheme.colorScheme.primary
)
```

## Font Metrics

Her font ailesi için özel line-height çarpanları tanımlanmıştır:

```kotlin
object FontMetrics {
    const val ROBOTO_LINE_HEIGHT_MULTIPLIER = 1.2f
    const val BODONI_LINE_HEIGHT_MULTIPLIER = 1.4f
    const val INTER_LINE_HEIGHT_MULTIPLIER = 1.3f
    const val POPPINS_LINE_HEIGHT_MULTIPLIER = 1.25f
    const val PLAYFAIR_LINE_HEIGHT_MULTIPLIER = 1.35f
}
```

## Error Handling

Font yükleme hatalarında otomatik fallback sistemi:

```kotlin
object FontErrorHandler {
    fun getFallbackFont(fontFamily: FontFamily): FontFamily {
        return when (fontFamily) {
            AppFontFamilies.roboto -> FontFamily.Default
            AppFontFamilies.bodoniModa -> AppFontFamilies.playfairDisplay
            AppFontFamilies.inter -> AppFontFamilies.roboto
            AppFontFamilies.poppins -> AppFontFamilies.inter
            AppFontFamilies.playfairDisplay -> AppFontFamilies.bodoniModa
            else -> FontFamily.Default
        }
    }
}
```

## Platform-Specific Implementation

### Android
- Native Roboto fontları kullanır
- Custom fontlar için resource-based loading
- Blocking font loading strategy

### iOS
- System fontları kullanır (SF Pro Text, Didot, Georgia)
- Custom fontlar için fallback mekanizması
- Device font family name kullanımı

## Performance Optimization

1. **Lazy Initialization**: Font aileleri lazy olarak yüklenir
2. **Memory Management**: Font cache'leme ve resource yönetimi
3. **Error Recovery**: Font yükleme hatalarında otomatik fallback
4. **Platform Optimization**: Platform-specific optimizasyonlar

## Testing

Font sistemi için kapsamlı unit testler:

```kotlin
class FontSystemTest {
    @Test
    fun `test font families are initialized`() {  }
    
    @Test
    fun `test font metrics are defined`() {  }
    
    @Test
    fun `test text styles are defined`() {  }
    
    @Test
    fun `test typography system is defined`() {  }
    
    @Test
    fun `test font error handler fallbacks`() {  }
}
```

## Migration Guide

### From Material Typography
```kotlin
// Old way
Text(
    text = "Hello",
    style = MaterialTheme.typography.headlineLarge
)

// New way
AppTitle(text = "Hello")
```

### From Custom Text Styles
```kotlin
// Old way
Text(
    text = "Category",
    style = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
)

// New way
CategoryTitle(text = "Category")
```

## Best Practices

1. **Semantic Usage**: Her text tipi için uygun component kullanın
2. **Consistency**: Tutarlı font kullanımı için AppText componentlerini tercih edin
3. **Accessibility**: Font boyutları ve contrast oranlarına dikkat edin
4. **Performance**: Font yükleme performansını optimize edin
5. **Testing**: Font sistemi değişikliklerini test edin

## Future Enhancements

1. **Dynamic Font Scaling**: Accessibility için dinamik font boyutlandırma
2. **RTL Support**: Sağdan sola yazım desteği
3. **Font Preloading**: Uygulama başlangıcında font ön yükleme
4. **Custom Font Loading**: Daha gelişmiş custom font yükleme mekanizması
5. **Font Metrics API**: Gelişmiş font metrics API'si

## Support

Font sistemi ile ilgili sorular için:
- Technical documentation: Bu dosya
- Code examples: `AppText.kt` ve `TextStyles.kt`
- Unit tests: `FontSystemTest.kt`
- Platform-specific issues: `FontProvider.android.kt` ve `FontProvider.ios.kt` 