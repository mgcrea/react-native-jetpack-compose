#pragma once

#include <ReactCommon/JavaTurboModule.h>
#include <ReactCommon/TurboModule.h>

namespace facebook {
namespace react {

std::shared_ptr<TurboModule> RNJetpackComposeSpec_ModuleProvider(
  const std::string& moduleName,
  const JavaTurboModule::InitParams& params
);

} // namespace react
} // namespace facebook
