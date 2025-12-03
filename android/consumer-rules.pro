# ProGuard rules for react-native-jetpack-compose

# Keep the React Native package class (required for auto-linking)
-keep class com.mgcrea.reactnative.jetpackcompose.RNJetpackComposePackage { *; }

# Keep ViewManagers (required for React Native bridge)
-keep class com.mgcrea.reactnative.jetpackcompose.ColorViewManager { *; }
-keep class com.mgcrea.reactnative.jetpackcompose.ModalBottomSheetManager { *; }

# Keep View classes (instantiated by ViewManagers)
-keep class com.mgcrea.reactnative.jetpackcompose.ColorView { *; }
-keep class com.mgcrea.reactnative.jetpackcompose.ModalBottomSheetView { *; }
