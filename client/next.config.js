/** @type {import('next').NextConfig} */
const removeImports = require("next-remove-imports")();

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

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
  async rewrites() {
    return [
      {
        source: "/apis/:path*",
        destination: BASE_URL + "/:path*",
      },
    ];
  },
};

module.exports = removeImports({
  ...nextConfig,
});
