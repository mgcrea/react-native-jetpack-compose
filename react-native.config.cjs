module.exports = {
  dependency: {
    platforms: {
      android: {
        sourceDir: './android',
        packageImportPath:
          'import com.mgcrea.reactnative.jetpackcompose.RNJetpackComposePackage;',
        packageInstance: 'new RNJetpackComposePackage()',
        componentDescriptors: [
          'ColorViewComponentDescriptor',
        ],
        cmakeListsPath: 'src/main/jni/CMakeLists.txt',
      },
    },
  },
};
