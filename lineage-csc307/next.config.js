const WorkerPlugin = require("worker-plugin");

module.exports = {
  output: "standalone",
  webpack: (config, { isServer }) => {
    if (!isServer) {
      config.plugins.push(new WorkerPlugin());
    }
    return config;
  },
};
