cmake_minimum_required(VERSION 3.13)
set(CMAKE_VERBOSE_MAKEFILE ON)

set(LIB_LITERAL RNJetpackComposeSpec)
set(LIB_TARGET_NAME react_codegen_${LIB_LITERAL})

set(LIB_ANDROID_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../..)
set(LIB_GENERATED_DIR ${LIB_ANDROID_DIR}/generated/jni)
set(LIB_COMPONENTS_DIR ${LIB_GENERATED_DIR}/react/renderer/components/${LIB_LITERAL})

file(GLOB LIB_CODEGEN_SRCS CONFIGURE_DEPENDS ${LIB_COMPONENTS_DIR}/*.cpp)

add_library(
  ${LIB_TARGET_NAME}
  SHARED
  placeholder.cpp
  ${LIB_CODEGEN_SRCS}
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

if (REACTNATIVE_MERGED_SO)
  target_link_libraries(
    ${LIB_TARGET_NAME}
    fbjni
    jsi
    reactnative
  )
else()
  target_link_libraries(
    ${LIB_TARGET_NAME}
    fbjni
    folly_runtime
    glog
    jsi
    react_codegen_rncore
    react_debug
    react_nativemodule_core
    react_render_core
    react_render_debug
    react_render_graphics
    react_render_mapbuffer
    react_render_componentregistry
    react_utils
    rrc_view
    turbomodulejsijni
    yoga
  )
endif()

target_compile_options(
  ${LIB_TARGET_NAME}
  PRIVATE
  -fexceptions
  -frtti
  -std=c++20
  -Wall
  -Wpedantic
  -Wno-gnu-zero-variadic-macro-arguments
  -Wno-dollar-in-identifier-extension
)
