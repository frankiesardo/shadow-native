module.exports = {
  resolveSnapshotPath: (testPath, snapshotExtension) =>
    testPath.replace('out/test', 'src/test/snapshots') + snapshotExtension,

  resolveTestPath: (snapshotFilePath, snapshotExtension) =>
    snapshotFilePath
    .replace('src/test/snapshots', 'out/test')
    .slice(0, -snapshotExtension.length),

  testPathForConsistencyCheck: 'example.test.js'
};