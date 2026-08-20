# Speed Math Warrior — Ads setup

Ads have been integrated with Google Mobile Ads SDK.

## Included
- Native ad card on the Home screen.
- Interstitial ad at a natural transition after every 2 completed practice sessions.
- No banner ads.
- No rewarded video ads.
- Official Google test IDs are currently enabled so the app can be safely tested.

## Before production monetization
Create your app in AdMob and replace:

1. `app/src/main/AndroidManifest.xml`
   - Replace the `APPLICATION_ID` test value with your AdMob App ID.
2. `app/src/main/java/com/example/ads/AdsManager.kt`
   - Replace `NATIVE_AD_UNIT_ID` with your Native ad unit ID.
   - Replace `INTERSTITIAL_AD_UNIT_ID` with your Interstitial ad unit ID.

Do not click your own live ads. Use the Google test IDs while testing.
