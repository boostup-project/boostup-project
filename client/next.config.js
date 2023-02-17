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
        headers: [{ key: "Access-Control-Allow-Origin", value: BASE_URL }],
        destination: BASE_URL + "/:path*",
      },
    ];
  },
};

module.exports = nextConfig;
// removeImports({
//   ...nextConfig,
// });
