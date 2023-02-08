/** @type {import('next').NextConfig} */
const removeImports = require("next-remove-imports")();

const nextConfig = {
  reactStrictMode: false,
  swcMinify: true,
  //   images:{
  //     loader: 'imgix',
  //     path: '/',
  //   },
  images: {
    unoptimized: true,
  },
  trailingSlash: true,
};

module.exports = removeImports({
  ...nextConfig,
});
