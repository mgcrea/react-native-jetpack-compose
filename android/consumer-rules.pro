# ProGuard rules for react-native-jetpack-compose

# Keep the React Native package class (required for auto-linking)
-keep class com.mgcrea.reactnative.jetpackcompose.RNJetpackComposePackage { *; }

# Keep ViewManagers (looked up by name through the generated codegen delegates)
-keep class com.mgcrea.reactnative.jetpackcompose.**Manager { *; }
