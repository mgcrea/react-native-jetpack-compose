cmake_minimum_required(VERSION 3.13)
set(CMAKE_VERBOSE_MAKEFILE on)

# Include React Native's compile flags (provides target_compile_reactnative_options)
include(${REACT_ANDROID_DIR}/../ReactCommon/cmake-utils/react-native-flags.cmake)

set(LIB_LITERAL RNJetpackComposeSpec)
set(LIB_TARGET_NAME react_codegen_${LIB_LITERAL})

set(LIB_ANDROID_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../..)
set(LIB_GENERATED_DIR ${LIB_ANDROID_DIR}/generated/jni)
set(LIB_COMPONENTS_DIR ${LIB_GENERATED_DIR}/react/renderer/components/${LIB_LITERAL})

file(GLOB LIB_CODEGEN_SRCS CONFIGURE_DEPENDS ${LIB_GENERATED_DIR}/*.cpp ${LIB_COMPONENTS_DIR}/*.cpp)
set(LIB_CUSTOM_SRCS ${CMAKE_CURRENT_SOURCE_DIR}/RNJetpackComposeSpec.cpp)

# Use OBJECT library like React Native's generated codegen (not SHARED)
add_library(
  ${LIB_TARGET_NAME}
  OBJECT
  ${LIB_CODEGEN_SRCS}
  ${LIB_CUSTOM_SRCS}
)

target_include_directories(
  ${LIB_TARGET_NAME}
  PUBLIC
  ${CMAKE_CURRENT_SOURCE_DIR}
  ${LIB_GENERATED_DIR}
  ${LIB_COMPONENTS_DIR}
)

target_include_directories(
  ${CMAKE_PROJECT_NAME}
  PUBLIC
  ${CMAKE_CURRENT_SOURCE_DIR}
  ${LIB_GENERATED_DIR}
  ${LIB_COMPONENTS_DIR}
)

target_link_libraries(
  ${LIB_TARGET_NAME}
  fbjni
  jsi
  reactnative
)

# Use React Native's standard compile options
target_compile_reactnative_options(${LIB_TARGET_NAME} PRIVATE)
