#include "RNJetpackComposeSpec.h"

namespace facebook {
namespace react {

std::shared_ptr<TurboModule> RNJetpackComposeSpec_ModuleProvider(
    const std::string &moduleName,
    const JavaTurboModule::InitParams &params) {
  // This library only contains components, not TurboModules
  return nullptr;
}

} // namespace react
} // namespace facebook
